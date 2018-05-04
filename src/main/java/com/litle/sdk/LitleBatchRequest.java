package com.litle.sdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.litle.sdk.generate.AccountUpdate;
import com.litle.sdk.generate.Activate;
import com.litle.sdk.generate.AuthReversal;
import com.litle.sdk.generate.Authorization;
import com.litle.sdk.generate.BalanceInquiry;
import com.litle.sdk.generate.BatchRequest;
import com.litle.sdk.generate.CancelSubscription;
import com.litle.sdk.generate.Capture;
import com.litle.sdk.generate.CaptureGivenAuth;
import com.litle.sdk.generate.CreatePlan;
import com.litle.sdk.generate.Credit;
import com.litle.sdk.generate.Deactivate;
import com.litle.sdk.generate.EcheckCredit;
import com.litle.sdk.generate.EcheckPreNoteCredit;
import com.litle.sdk.generate.EcheckPreNoteSale;
import com.litle.sdk.generate.EcheckRedeposit;
import com.litle.sdk.generate.EcheckSale;
import com.litle.sdk.generate.EcheckVerification;
import com.litle.sdk.generate.ForceCapture;
import com.litle.sdk.generate.FundingInstructionVoid;
import com.litle.sdk.generate.LitleTransactionInterface;
import com.litle.sdk.generate.Load;
import com.litle.sdk.generate.ObjectFactory;
import com.litle.sdk.generate.PayFacCredit;
import com.litle.sdk.generate.PayFacDebit;
import com.litle.sdk.generate.PhysicalCheckCredit;
import com.litle.sdk.generate.PhysicalCheckDebit;
import com.litle.sdk.generate.RegisterTokenRequestType;
import com.litle.sdk.generate.ReserveCredit;
import com.litle.sdk.generate.ReserveDebit;
import com.litle.sdk.generate.Sale;
import com.litle.sdk.generate.SubmerchantCredit;
import com.litle.sdk.generate.SubmerchantDebit;
import com.litle.sdk.generate.TransactionType;
import com.litle.sdk.generate.Unload;
import com.litle.sdk.generate.UpdateCardValidationNumOnToken;
import com.litle.sdk.generate.UpdatePlan;
import com.litle.sdk.generate.UpdateSubscription;
import com.litle.sdk.generate.VendorCredit;
import com.litle.sdk.generate.VendorDebit;
import org.bouncycastle.openpgp.PGPException;

public class LitleBatchRequest {
	private BatchRequest batchRequest;
	private JAXBContext jc;
	private File file;
	private Marshaller marshaller;
	ObjectFactory objFac;
	TransactionType txn;
	String filePath;
	OutputStream osWrttxn;


	int numOfTxn;


	private final int maxTransactionsPerBatch;
	protected int litleLimit_maxTransactionsPerBatch = 100000;
	private final LitleBatchFileRequest lbfr;


	/**
	 * This method initializes the batch level attributes of the XML and checks if the maxTransactionsPerBatch is not more than the value provided in the properties file
	 * @param merchantId
	 * @param lbfr
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	LitleBatchRequest(String merchantId, LitleBatchFileRequest lbfr) throws LitleBatchException{
		this.batchRequest = new BatchRequest();
		this.batchRequest.setMerchantId(merchantId);
		this.batchRequest.setMerchantSdk(Versions.SDK_VERSION);
		this.objFac = new ObjectFactory();
		this.lbfr = lbfr;
		File tmpFile = new File(lbfr.getConfig().getProperty("batchRequestFolder")+"/tmp");
		if(!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		String dateString = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS").format(new java.util.Date());
		filePath = new String(lbfr.getConfig().getProperty("batchRequestFolder")+ "/tmp/Transactions" + merchantId + dateString);
		numOfTxn = 0;
		try {
			this.jc = JAXBContext.newInstance("com.litle.sdk.generate");
			marshaller = jc.createMarshaller();
			// JAXB_FRAGMENT property required to prevent unnecessary XML info from being printed in the file during marshal.
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			// Proper formatting of XML purely for aesthetic purposes.
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		} catch (JAXBException e) {
			throw new LitleBatchException("Unable to load jaxb dependencies.  Perhaps a classpath issue?", e);
		}
		this.maxTransactionsPerBatch = Integer.parseInt(lbfr.getConfig().getProperty("maxTransactionsPerBatch","10000"));
		if( maxTransactionsPerBatch > litleLimit_maxTransactionsPerBatch ){
			throw new LitleBatchException("maxTransactionsPerBatch property value cannot exceed " + String.valueOf(litleLimit_maxTransactionsPerBatch));
		}
	}

	BatchRequest getBatchRequest(){
		return batchRequest;
	}

	/**
	 * This method is used to add transaction to a particular batch
	 * @param transactionType
	 * @return
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	public TransactionCodeEnum addTransaction(LitleTransactionInterface transactionType) throws LitleBatchException, LitleBatchFileFullException, LitleBatchBatchFullException {
		if (numOfTxn == 0) {
            Properties properties = lbfr.getConfig();
            this.file = new File(filePath);
            try {
                if ("true".equalsIgnoreCase(properties.getProperty("useEncryption"))){
                    osWrttxn = PgpHelper.encryptionStream(filePath, properties.getProperty("MerchantPublicKeyPath"));
                }
                else{
                    osWrttxn = new FileOutputStream(file.getAbsolutePath());
                }
            } catch (FileNotFoundException e) {
                throw new LitleBatchException("There was an exception while trying to create a Request file. Please check if the folder: " + properties.getProperty("batchRequestFolder") +" has read and write access. ");
            }
            catch (IOException ioe){
                throw new LitleBatchException("Could not read merchant public key at " + properties.getProperty("MerchantPublicKeyPath") +
                        "\nMake sure that the provided public key path is correct" + ioe);
            }
            catch (PGPException pgpe){
                throw new LitleBatchException("There was an error while trying to read merchant public key at " + properties.getProperty("MerchantPublicKeyPath") +
                        "\nMake sure that the provided public key path contains a valid public key" + pgpe);
            }
        }

		if(numOfTxn > 0 && batchRequest.getNumAccountUpdates().intValue() != numOfTxn
		        && (transactionType instanceof AccountUpdate)){
            throw new LitleBatchException("An account update cannot be added to a batch containing transactions other than other AccountUpdates.");
        } else if(numOfTxn > 0 && batchRequest.getNumAccountUpdates().intValue() == numOfTxn &&
                !(transactionType instanceof AccountUpdate)){
            throw new LitleBatchException("Transactions that are not AccountUpdates cannot be added to a batch containing AccountUpdates.");
        }

		TransactionCodeEnum batchFileStatus = verifyFileThresholds();
        if( batchFileStatus == TransactionCodeEnum.FILEFULL){
            Exception e = new Exception();
            throw new LitleBatchFileFullException("Batch File is already full -- it has reached the maximum number of transactions allowed per batch file.", e);
        } else if( batchFileStatus == TransactionCodeEnum.BATCHFULL ){
            Exception e = new Exception();
            throw new LitleBatchBatchFullException("Batch is already full -- it has reached the maximum number of transactions allowed per batch.", e);
        }

        //Adding 1 to the number of transaction. This is on the assumption that we are adding one transaction to the batch at a time.
        BigInteger numToAdd = new BigInteger("1");
        boolean transactionAdded = false;

		JAXBElement transaction;

		if(transactionType instanceof Sale){
            batchRequest.setNumSales(batchRequest.getNumSales().add(BigInteger.valueOf(1)));
            batchRequest.setSaleAmount(batchRequest.getSaleAmount().add(BigInteger.valueOf(((Sale) transactionType).getAmount())));
            transaction = objFac.createSale((Sale)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof Authorization){
            batchRequest.setNumAuths(batchRequest.getNumAuths().add(BigInteger.valueOf(1)));
            batchRequest.setAuthAmount(batchRequest.getAuthAmount().add(BigInteger.valueOf(((Authorization) transactionType).getAmount())));
            transaction = objFac.createAuthorization((Authorization)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof Credit){
            batchRequest.setNumCredits(batchRequest.getNumCredits().add(BigInteger.valueOf(1)));
            batchRequest.setCreditAmount(batchRequest.getCreditAmount().add(BigInteger.valueOf(((Credit) transactionType).getAmount())));
            transaction = objFac.createCredit((Credit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof RegisterTokenRequestType){
            batchRequest.setNumTokenRegistrations(batchRequest.getNumTokenRegistrations().add(BigInteger.valueOf(1)));
            transaction = objFac.createRegisterTokenRequest((RegisterTokenRequestType)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof CaptureGivenAuth){
            batchRequest.setNumCaptureGivenAuths(batchRequest.getNumCaptureGivenAuths().add(BigInteger.valueOf(1)));
            batchRequest.setCaptureGivenAuthAmount(batchRequest.getCaptureGivenAuthAmount().add(BigInteger.valueOf(((CaptureGivenAuth) transactionType).getAmount())));
            transaction = objFac.createCaptureGivenAuth((CaptureGivenAuth)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof ForceCapture){
            batchRequest.setNumForceCaptures(batchRequest.getNumForceCaptures().add(BigInteger.valueOf(1)));
            batchRequest.setForceCaptureAmount(batchRequest.getForceCaptureAmount().add(BigInteger.valueOf(((ForceCapture) transactionType).getAmount())));
            transaction = objFac.createForceCapture((ForceCapture)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof AuthReversal){
            batchRequest.setNumAuthReversals(batchRequest.getNumAuthReversals().add(BigInteger.valueOf(1)));
            batchRequest.setAuthReversalAmount(batchRequest.getAuthReversalAmount().add(BigInteger.valueOf(((AuthReversal) transactionType).getAmount())));
            transaction = objFac.createAuthReversal((AuthReversal)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof Capture){
            batchRequest.setNumCaptures(batchRequest.getNumCaptures().add(BigInteger.valueOf(1)));
            batchRequest.setCaptureAmount(batchRequest.getCaptureAmount().add(BigInteger.valueOf(((Capture) transactionType).getAmount())));
            transaction = objFac.createCapture((Capture)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof EcheckVerification){
            batchRequest.setNumEcheckVerification(batchRequest.getNumEcheckVerification().add(BigInteger.valueOf(1)));
            batchRequest.setEcheckVerificationAmount(batchRequest.getEcheckVerificationAmount().add(BigInteger.valueOf(((EcheckVerification) transactionType).getAmount())));
            transaction = objFac.createEcheckVerification((EcheckVerification)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof EcheckCredit){
            batchRequest.setNumEcheckCredit(batchRequest.getNumEcheckCredit().add(BigInteger.valueOf(1)));
            batchRequest.setEcheckCreditAmount(batchRequest.getEcheckCreditAmount().add(BigInteger.valueOf(((EcheckCredit) transactionType).getAmount())));
            transaction = objFac.createEcheckCredit((EcheckCredit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof EcheckRedeposit){
            batchRequest.setNumEcheckRedeposit(batchRequest.getNumEcheckRedeposit().add(BigInteger.valueOf(1)));
            transaction = objFac.createEcheckRedeposit((EcheckRedeposit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof EcheckSale){
            batchRequest.setNumEcheckSales(batchRequest.getNumEcheckSales().add(BigInteger.valueOf(1)));
            batchRequest.setEcheckSalesAmount(batchRequest.getEcheckSalesAmount().add(BigInteger.valueOf(((EcheckSale) transactionType).getAmount())));
            transaction = objFac.createEcheckSale((EcheckSale)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof UpdateCardValidationNumOnToken){
            batchRequest.setNumUpdateCardValidationNumOnTokens(batchRequest.getNumUpdateCardValidationNumOnTokens().add(BigInteger.valueOf(1)));
            transaction = objFac.createUpdateCardValidationNumOnToken((UpdateCardValidationNumOnToken)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof UpdateSubscription) {
            batchRequest.setNumUpdateSubscriptions(batchRequest.getNumUpdateSubscriptions().add(BigInteger.valueOf(1)));
            transaction = objFac.createUpdateSubscription((UpdateSubscription)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof CancelSubscription) {
            batchRequest.setNumCancelSubscriptions(batchRequest.getNumCancelSubscriptions().add(BigInteger.valueOf(1)));
            transaction = objFac.createCancelSubscription((CancelSubscription)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof CreatePlan) {
            batchRequest.setNumCreatePlans(batchRequest.getNumCreatePlans().add(BigInteger.valueOf(1)));
            transaction = objFac.createCreatePlan((CreatePlan)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof UpdatePlan) {
            batchRequest.setNumUpdatePlans(batchRequest.getNumUpdatePlans().add(BigInteger.valueOf(1)));
            transaction = objFac.createUpdatePlan((UpdatePlan)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof Activate) {
            batchRequest.setNumActivates(batchRequest.getNumActivates().add(BigInteger.valueOf(1)));
            batchRequest.setActivateAmount(batchRequest.getActivateAmount().add(BigInteger.valueOf(((Activate) transactionType).getAmount())));
            transaction = objFac.createActivate((Activate)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof Deactivate) {
            batchRequest.setNumDeactivates(batchRequest.getNumDeactivates().add(BigInteger.valueOf(1)));
            transaction = objFac.createDeactivate((Deactivate)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof Load) {
            batchRequest.setNumLoads(batchRequest.getNumLoads().add(BigInteger.valueOf(1)));
            batchRequest.setLoadAmount(batchRequest.getLoadAmount().add(BigInteger.valueOf(((Load) transactionType).getAmount())));
            transaction = objFac.createLoad((Load)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof Unload) {
            batchRequest.setNumUnloads(batchRequest.getNumUnloads().add(BigInteger.valueOf(1)));
            batchRequest.setUnloadAmount(batchRequest.getUnloadAmount().add(BigInteger.valueOf(((Unload) transactionType).getAmount())));
            transaction = objFac.createUnload((Unload)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof BalanceInquiry) {
            batchRequest.setNumBalanceInquirys(batchRequest.getNumBalanceInquirys().add(BigInteger.valueOf(1)));
            transaction = objFac.createBalanceInquiry((BalanceInquiry)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof EcheckPreNoteSale) {
            batchRequest.setNumEcheckPreNoteSale(batchRequest.getNumEcheckPreNoteSale().add(BigInteger.valueOf(1)));
            transaction = objFac.createEcheckPreNoteSale((EcheckPreNoteSale)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof EcheckPreNoteCredit) {
            batchRequest.setNumEcheckPreNoteCredit(batchRequest.getNumEcheckPreNoteCredit().add(BigInteger.valueOf(1)));
            transaction = objFac.createEcheckPreNoteCredit((EcheckPreNoteCredit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof PayFacCredit) {
            batchRequest.setNumPayFacCredit(batchRequest.getNumPayFacCredit().add(BigInteger.valueOf(1)));
            batchRequest.setPayFacCreditAmount((batchRequest.getPayFacCreditAmount().add(BigInteger.valueOf(((PayFacCredit) transactionType).getAmount()))));
            transaction = objFac.createPayFacCredit((PayFacCredit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof SubmerchantCredit) {
            batchRequest.setNumSubmerchantCredit(batchRequest.getNumSubmerchantCredit().add(BigInteger.valueOf(1)));
            batchRequest.setSubmerchantCreditAmount((batchRequest.getSubmerchantCreditAmount().add(BigInteger.valueOf(((SubmerchantCredit) transactionType).getAmount()))));
            transaction = objFac.createSubmerchantCredit((SubmerchantCredit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof VendorCredit) {
            batchRequest.setNumVendorCredit(batchRequest.getNumVendorCredit().add(BigInteger.valueOf(1)));
            batchRequest.setVendorCreditAmount((batchRequest.getVendorCreditAmount().add(BigInteger.valueOf(((VendorCredit) transactionType).getAmount()))));
            transaction = objFac.createVendorCredit((VendorCredit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof ReserveCredit) {
            batchRequest.setNumReserveCredit(batchRequest.getNumReserveCredit().add(BigInteger.valueOf(1)));
            batchRequest.setReserveCreditAmount((batchRequest.getReserveCreditAmount().add(BigInteger.valueOf(((ReserveCredit) transactionType).getAmount()))));
            transaction = objFac.createReserveCredit((ReserveCredit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof PhysicalCheckCredit) {
            batchRequest.setNumPhysicalCheckCredit(batchRequest.getNumPhysicalCheckCredit().add(BigInteger.valueOf(1)));
            batchRequest.setPhysicalCheckCreditAmount((batchRequest.getPhysicalCheckCreditAmount().add(BigInteger.valueOf(((PhysicalCheckCredit) transactionType).getAmount()))));
            transaction = objFac.createPhysicalCheckCredit((PhysicalCheckCredit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof PayFacDebit) {
            batchRequest.setNumPayFacDebit(batchRequest.getNumPayFacDebit().add(BigInteger.valueOf(1)));
            batchRequest.setPayFacDebitAmount((batchRequest.getPayFacDebitAmount().add(BigInteger.valueOf(((PayFacDebit) transactionType).getAmount()))));
            transaction = objFac.createPayFacDebit((PayFacDebit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof SubmerchantDebit) {
            batchRequest.setNumSubmerchantDebit(batchRequest.getNumSubmerchantDebit().add(BigInteger.valueOf(1)));
            batchRequest.setSubmerchantDebitAmount((batchRequest.getSubmerchantDebitAmount().add(BigInteger.valueOf(((SubmerchantDebit) transactionType).getAmount()))));
            transaction = objFac.createSubmerchantDebit((SubmerchantDebit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof VendorDebit) {
            batchRequest.setNumVendorDebit(batchRequest.getNumVendorDebit().add(BigInteger.valueOf(1)));
            batchRequest.setVendorDebitAmount((batchRequest.getVendorDebitAmount().add(BigInteger.valueOf(((VendorDebit) transactionType).getAmount()))));
            transaction = objFac.createVendorDebit((VendorDebit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof ReserveDebit) {
            batchRequest.setNumReserveDebit(batchRequest.getNumReserveDebit().add(BigInteger.valueOf(1)));
            batchRequest.setReserveDebitAmount((batchRequest.getReserveDebitAmount().add(BigInteger.valueOf(((ReserveDebit) transactionType).getAmount()))));
            transaction = objFac.createReserveDebit((ReserveDebit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if(transactionType instanceof PhysicalCheckDebit) {
            batchRequest.setNumPhysicalCheckDebit(batchRequest.getNumPhysicalCheckDebit().add(BigInteger.valueOf(1)));
            batchRequest.setPhysicalCheckDebitAmount((batchRequest.getPhysicalCheckDebitAmount().add(BigInteger.valueOf(((PhysicalCheckDebit) transactionType).getAmount()))));
            transaction = objFac.createPhysicalCheckDebit((PhysicalCheckDebit)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof AccountUpdate){
            batchRequest.setNumAccountUpdates(batchRequest.getNumAccountUpdates().add(BigInteger.valueOf(1)));
            transaction = objFac.createAccountUpdate((AccountUpdate)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } else if (transactionType instanceof FundingInstructionVoid){
            batchRequest.setNumFundingInstructionVoid(batchRequest.getNumFundingInstructionVoid().add(BigInteger.valueOf(1)));
            transaction = objFac.createFundingInstructionVoid((FundingInstructionVoid)transactionType);
            transactionAdded = true;
            numOfTxn ++;
        } 
        else {
            transaction = objFac.createTransaction(new TransactionType());
        }

		try {
            marshaller.marshal(transaction, osWrttxn);
        } catch (JAXBException e) {
            throw new LitleBatchException("There was an exception while marshalling the transaction object.", e);
        }

        batchFileStatus = verifyFileThresholds();
        if( batchFileStatus == TransactionCodeEnum.FILEFULL){
            return TransactionCodeEnum.FILEFULL;
        } else if( batchFileStatus == TransactionCodeEnum.BATCHFULL ){
            return TransactionCodeEnum.BATCHFULL;
        }

        if (transactionAdded) {
            return TransactionCodeEnum.SUCCESS;
        } else {
            return TransactionCodeEnum.FAILURE;
        }
	}

	/**
	 * This method makes sure that the maximum number of transactions per batch and file is not exceeded
	 * This is to ensure Performance.
	 * @return
	 */
	TransactionCodeEnum verifyFileThresholds(){
		if( this.lbfr.getNumberOfTransactionInFile() == this.lbfr.getMaxAllowedTransactionsPerFile()){
			return TransactionCodeEnum.FILEFULL;
		}
		else if( getNumberOfTransactions() == this.maxTransactionsPerBatch ){
			return TransactionCodeEnum.BATCHFULL;
		}
		return TransactionCodeEnum.SUCCESS;
	}

	/**
	 * Returns the number of transactions in the batch
	 * @return
	 */
	public int getNumberOfTransactions(){
		return (numOfTxn);
	}

	/**
	 * Gets whether the batch is full per the size specification
	 * @return boolean indicating whether the batch is full
	 */
	public boolean isFull() {
		return (getNumberOfTransactions() == this.maxTransactionsPerBatch);
	}

	/**
	 * Closes the batch output file
	 * @throws IOException
	 */
	public void closeFile() throws IOException {
		osWrttxn.close();
	}

	/**
	 * Grabs the request file
	 * @return the request file
	 */
	public File getFile() {
		return this.file;
	}

    public Marshaller getMarshaller() {
        return marshaller;
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public int getNumOfTxn() {
        return numOfTxn;
    }

    public void setNumOfTxn(int numOfTxn) {
        this.numOfTxn = numOfTxn;
    }



}
