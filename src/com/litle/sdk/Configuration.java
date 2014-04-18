package com.litle.sdk;

import java.io.File;

public class Configuration {
	
	private static final String LITLE_SDK_CONFIG = ".litle_SDK_config.properties";

	public File location() {
		File file = new File(System.getProperty("user.home") + File.separator + LITLE_SDK_CONFIG);
		if(System.getProperty("java.specification.version").equals("1.4")) {
			if(System.getProperty("LITLE_CONFIG_DIR") != null) {
				file = new File(System.getProperty("LITLE_CONFIG_DIR") + File.separator + LITLE_SDK_CONFIG);
			}
		}
		else if(System.getenv("LITLE_CONFIG_DIR") != null) {
				file = new File(System.getenv("LITLE_CONFIG_DIR") + File.separator + LITLE_SDK_CONFIG);
		}
        else {
        	if (getClass().getClassLoader().getResource(LITLE_SDK_CONFIG) != null) {
            	String filePath = getClass().getClassLoader().getResource(LITLE_SDK_CONFIG).getPath();
            	file = new File(filePath);
            }
        }
		return file;
	}
}
