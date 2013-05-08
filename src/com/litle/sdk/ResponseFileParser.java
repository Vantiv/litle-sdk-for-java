package com.litle.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ResponseFileParser {
	private File fileToParse = null;
	InputStream in = null;
	Reader reader = null;
	Reader buffer = null;
	
	public ResponseFileParser(File responseFile){
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
	
	public String getNextTag(String tagToLookFor){
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
		try {
			while((r = buffer.read()) != -1){
				char ch = (char) r;
				
				if( startRecordingRetString ) {
					retStringBuf.append(ch);
					
					if( lastChar == '<' && ch == '/' ){
						startRecordingEndingTag = true;
						currentEndingTagInFile.append(lastChar);
					}
					
					// override process for elements like litleResponse and batchResponse
					if( (tagToLookFor.compareToIgnoreCase("batchResponse") == 0 ||
						tagToLookFor.compareToIgnoreCase("litleResponse") == 0)
						&&
						ch == '>'){
						retStringBuf.append(closingTagToLookFor);
						break;
					}
				}
				
				// We want to look for startingTag only if we aren't already recording the string to return.
				if( ch == '<' && !startRecordingRetString ){
					startRecordingStartingTag = true;
				}
				
				if( startRecordingStartingTag ){
					currentStartingTagInFile.append(ch);
					
					if( openingTagToLookFor.compareToIgnoreCase(currentStartingTagInFile.toString()) == 0 ){
						startRecordingRetString = true;
						retStringBuf.append(currentStartingTagInFile);
						startRecordingStartingTag = false;
						currentStartingTagInFile.delete(0, currentStartingTagInFile.length());
					}
					
					// tag declaration has ended. Safe to discard.
					if( ch == '>' ){
						startRecordingStartingTag = false;
						currentStartingTagInFile.delete(0, currentStartingTagInFile.length());
					}
				}
				
				
				if( startRecordingEndingTag ){
					currentEndingTagInFile.append(ch);
					if( ch == '>' ){
						startRecordingEndingTag = false;
						if( closingTagToLookFor.compareToIgnoreCase(currentEndingTagInFile.toString()) == 0 ){
							startRecordingRetString = false;
							currentEndingTagInFile.delete(0, currentEndingTagInFile.length());
							break;
						}
					}
				}
				
				lastChar = ch;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retStringBuf.toString();
	}
}
