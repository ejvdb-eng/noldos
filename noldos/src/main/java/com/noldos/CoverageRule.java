package com.noldos;

import java.util.Set;

public class CoverageRule implements Rule{

    private final Requirement requirement;

    public CoverageRule(Requirement req){
        this.requirement = req;
    }

    @Override
    public boolean appliesTo(Contract contract){
        if(requirement.getScope() == Scope.ALL_CONTRACTS || contract.getFlag()){
            return true;
        }
        return false;
    }

    @Override 
    public Finding evaluate(Contract contract){
        if(contract.getClausesFor(requirement).isEmpty()){
            return new Finding(requirement, Status.MISSING, Set.of(), "Requirement is missing");
        }
        else{
            return new Finding(requirement, Status.COVERED, contract.clauseSearchForRequirement(requirement), "requirement found");
        }
    }
    
}
