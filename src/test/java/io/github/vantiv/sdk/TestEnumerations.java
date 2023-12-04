package io.github.vantiv.sdk;

import static org.junit.Assert.*;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import io.github.vantiv.sdk.generate.*;
import org.junit.Test;

import  io.github.vantiv.sdk.generate.CustomerInfo.ResidenceStatus;
import  io.github.vantiv.sdk.generate.CustomerInfo.CustomerType;
import  io.github.vantiv.sdk.generate.EnhancedData.DeliveryType;

/**
 * The tests in this file are to ensure that the generated code maintains
 * enumerations.  It is really only testing compilation.
 * 
 * If a restriction is introduced in the schema that starts with a number
 * instead of a letter or underscore, jaxb will not generate an enum type for
 * it.
 * 
 * If a restriction is introduced with a minOccurs=0, jaxb will not generate an
 * enum type for it.
 * 
 * This is not idiomatic. If this test breaks as part of work you are doing,
 * look at build.xml's generate target to add another enum exclusion.
 * 
 * @author gdake
 * 
 */
public class TestEnumerations {

	private JAXBContext jc;
	private Marshaller marshaller;
	
	@Test
	public void customerType() {
		CustomerInfo info = new CustomerInfo();
		info.setCustomerType(CustomerType.EXISTING);
	}
	@Test
	public void residenceStatus() {
		CustomerInfo info = new CustomerInfo();
		info.setResidenceStatus(ResidenceStatus.OWN);
	}
	@Test
	public void deliveryType() {
		EnhancedData info = new EnhancedData();
		info.setDeliveryType(DeliveryType.CNC);
	}
	@Test
	public void taxTypeIdentifier() throws Exception {
		DetailTax info = new DetailTax();
		info.setTaxTypeIdentifier(TaxTypeIdentifierEnum.ENERGY_TAX); //should be 22 in xml
		jc = JAXBContext.newInstance("io.github.vantiv.sdk.generate");
		marshaller = jc.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(info, sw);
		assertTrue(sw.toString(), sw.toString().contains("<taxTypeIdentifier>22</taxTypeIdentifier>"));
	}
	@Test
	public void pos() {
		Pos info = new Pos();
		info.setCapability(PosCapabilityTypeEnum.KEYEDONLY);
		info.setCardholderId(PosCardholderIdTypeEnum.PIN);
		info.setEntryMode(PosEntryModeTypeEnum.TRACK_1);
	}
	@Test
	public void orderSource() {
		EcheckSale info = new EcheckSale();
		info.setOrderSource(OrderSourceType.TELEPHONE);
	}
	@Test
    public void orderSourceWithApplepay() {
        EcheckSale info = new EcheckSale();
        info.setOrderSource(OrderSourceType.APPLEPAY);
    }
	@Test
    public void orderSourceWithEcheckppd() {
        EcheckSale info = new EcheckSale();
        info.setOrderSource(OrderSourceType.ECHECKPPD);
    }
	@Test
	public void methodOfPayment() {
		CardType info = new CardType();
		info.setType(MethodOfPaymentTypeEnum.VI);
	}
	@Test
	public void taxType() {
		Authorization info = new Authorization();
		info.setTaxType(GovtTaxTypeEnum.PAYMENT);
	}
	@Test
	public void currency() {
		CustomerInfo info = new CustomerInfo();
		info.setIncomeCurrency(CurrencyCodeEnum.USD);
	}
	@Test
	public void country() {
		EnhancedData info = new EnhancedData();
		info.setDestinationCountryCode(CountryTypeEnum.US);
	}
	@Test
	public void iias() {
		HealthcareIIAS info = new HealthcareIIAS();
		info.setIIASFlag(IIASFlagType.Y);
	}

	@Test
	public void processingType() {
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setProcessingType(ProcessingTypeEnum.INITIAL_COF);
		capturegivenauth.setProcessingType(ProcessingTypeEnum.MERCHANT_INITIATED_COF);
		capturegivenauth.setProcessingType(ProcessingTypeEnum.CARDHOLDER_INITIATED_COF);
	}
}
