package com.litle.sdk;


public enum TransactionCodeEnum {
	SUCCESS ("Success"), FAILURE ("Failed"), BATCHFULL ("Batch is Full");
	
	private final String description;
	
	TransactionCodeEnum(String desc) {
        description = desc;
    }
    
    public String getDescription()
    {
        return description;
    }
	
}
