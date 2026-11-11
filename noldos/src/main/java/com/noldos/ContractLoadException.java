package com.noldos;

public class ContractLoadException extends RuntimeException {

    public ContractLoadException(String fileName, String reason){  
    super(fileName + ": " + reason);   
    }
    
    public ContractLoadException(String fileName, String reason, Throwable cause) {
        super(fileName + ": " + reason, cause);
    }
    
    
}
