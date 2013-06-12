package com.litle.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ResponseFileParser {
	private File fileToParse = null;
	InputStream in = null;
	Reader reader = null;
	Reader buffer = null;

	public ResponseFileParser(File responseFile) {
		try {
			fileToParse = responseFile;
			in = new FileInputStream(fileToParse);
			reader = new InputStreamReader(in);
			buffer = new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getNextTag(String tagToLookFor) throws Exception {
		StringBuffer currentStartingTagInFile = new StringBuffer();
		StringBuffer retStringBuf = new StringBuffer();
		StringBuffer currentEndingTagInFile = new StringBuffer();

		boolean startRecordingStartingTag = false;
		boolean startRecordingEndingTag = false;
		boolean startRecordingRetString = false;

		char lastChar = 0;

		String openingTagToLookFor = "<" + tagToLookFor;
		String closingTagToLookFor = "</" + tagToLookFor + ">";

		int r = 0;
		while ((r = buffer.read()) != -1) {
			char ch = (char) r;

			if (startRecordingRetString) {
				retStringBuf.append(ch);

				if (lastChar == '<' && ch == '/') {
					startRecordingEndingTag = true;
					currentEndingTagInFile.append(lastChar);
				}

				// override process for elements like litleResponse and
				// batchResponse
				if ((tagToLookFor.compareToIgnoreCase("batchResponse") == 0 || tagToLookFor
						.compareToIgnoreCase("litleResponse") == 0)
						&& ch == '>') {
					retStringBuf.append(closingTagToLookFor);
					break;
				}
			}

			// We want to look for startingTag only if we aren't already
			// recording the string to return.
			if (ch == '<' && !startRecordingRetString) {
				startRecordingStartingTag = true;
			}

			if (startRecordingStartingTag) {
				currentStartingTagInFile.append(ch);

				if (okToStartRecordingString(openingTagToLookFor,
						currentStartingTagInFile.toString())) {
					startRecordingRetString = true;
					retStringBuf.append(currentStartingTagInFile);
					if( openingTagToLookFor.compareToIgnoreCase("<litleResponse") != 0 ){
						retStringBuf.append(" xmlns=\"http://www.litle.com/schema\"");
					}
					startRecordingStartingTag = false;
					currentStartingTagInFile.delete(0,
							currentStartingTagInFile.length());
				}

				// tag declaration has ended. Safe to discard.
				if (ch == '>') {
					if (tagToLookFor.compareToIgnoreCase("transactionResponse") == 0
							&& currentStartingTagInFile.toString()
									.compareToIgnoreCase("</batchResponse>") == 0) {
						// Presumably this will only happen when the user is
						// requesting a new transaction info,
						// but all transactions have been exhausted. i.e. this
						// one is a batchResponse
						throw new Exception(
								"All payments in this batch have already been retrieved.");
					}
					startRecordingStartingTag = false;
					currentStartingTagInFile.delete(0,
							currentStartingTagInFile.length());
				}
			}

			if (startRecordingEndingTag) {
				currentEndingTagInFile.append(ch);
				if (ch == '>') {
					startRecordingEndingTag = false;
					if (okToStopRecordingString(closingTagToLookFor,
							currentEndingTagInFile.toString())) {
						startRecordingRetString = false;
						// currentEndingTagInFile.delete(0,
						// currentEndingTagInFile.length());
						break;
					}
					currentEndingTagInFile.delete(0,
							currentEndingTagInFile.length());
				}
			}

			lastChar = ch;
		}
		return retStringBuf.toString();
	}

	boolean okToStartRecordingString(String openingTagToLookFor,
			String currentStartingTagInFile) {
		boolean retVal = false;

		// we're looking for all transactionResponses
		if (openingTagToLookFor.compareToIgnoreCase("<transactionResponse") == 0
				&& (currentStartingTagInFile
						.compareToIgnoreCase("<authorizationResponse") == 0
						|| currentStartingTagInFile.compareToIgnoreCase("<saleResponse") == 0
						|| currentStartingTagInFile.compareToIgnoreCase("<captureResponse") == 0
	                    || currentStartingTagInFile.compareToIgnoreCase("<forceCaptureResponse") == 0
	                    || currentStartingTagInFile.compareToIgnoreCase("<captureGivenAuthResponse") == 0
	                    || currentStartingTagInFile.compareToIgnoreCase("<creditResponse") == 0
	                    || currentStartingTagInFile.compareToIgnoreCase("<echeckSaleResponse") == 0
	                    || currentStartingTagInFile.compareToIgnoreCase("<echeckCreditResponse") == 0
	                    || currentStartingTagInFile.compareToIgnoreCase("<echeckVerificationResponse") == 0
	                    || currentStartingTagInFile.compareToIgnoreCase("<echeckRedepositResponse") == 0
	                    || currentStartingTagInFile.compareToIgnoreCase("<authReversalResponse") == 0
	                    || currentStartingTagInFile.compareToIgnoreCase("<registerTokenResponse") == 0
	                    || currentStartingTagInFile.compareToIgnoreCase("<accountUpdateResponse") == 0)) {
			retVal = true;
		} else if (openingTagToLookFor
				.compareToIgnoreCase(currentStartingTagInFile) == 0) {
			retVal = true;
		}

		return retVal;
	}

	boolean okToStopRecordingString(String closingTagToLookFor,
			String currentStartingTagInFile) {
		boolean retVal = false;

		// we're looking for all transactionResponses
		if (closingTagToLookFor.compareToIgnoreCase("</transactionResponse>") == 0
				&& (currentStartingTagInFile
						.compareToIgnoreCase("</authorizationResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</saleResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</captureResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</forceCaptureResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</captureGivenAuthResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</creditResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</echeckSaleResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</echeckCreditResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</echeckVerificationResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</echeckRedepositResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</authReversalResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</registerTokenResponse>") == 0
					|| currentStartingTagInFile.compareToIgnoreCase("</accountUpdateResponse>") == 0)) {

			retVal = true;
		} else if (closingTagToLookFor
				.compareToIgnoreCase(currentStartingTagInFile) == 0) {
			retVal = true;
		}

		return retVal;
	}
}
