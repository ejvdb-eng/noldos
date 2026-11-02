package com.noldos;

public interface Rule {
    
    boolean appliesTo(Contract contract);

    Finding evaluate(Contract contract);
}
