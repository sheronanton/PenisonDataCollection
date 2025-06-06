package com.twad.service;

import org.springframework.stereotype.Service;

@Service
public class HashkeyValidationService {

    // Example method to validate hashkey
    public boolean validateHashkey(String hashkey) {
    	
    	String originalhashkey = "2a446067ea33129875d75fca8598442e76e9740a"; 
        // Implement your logic to validate the hashkey
        // Example: compare with a predefined hashkey or check against a database
        return originalhashkey.equals(hashkey);
    }
}
