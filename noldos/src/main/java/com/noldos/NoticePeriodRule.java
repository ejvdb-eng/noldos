package com.noldos;

import java.util.Set;

public class NoticePeriodRule implements Rule {

    private final Requirement requirement;

    public NoticePeriodRule(Requirement requirement){
        this.requirement = requirement;
    }

    @Override
    public boolean appliesTo(Contract contract){
        if(requirement.equals(Requirement.TERMINATION_RIGHTS)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public Finding evaluate(Contract contract){
        if(contract.getClausesFor(requirement).isEmpty()){
            return new Finding(requirement, Status.MISSING, contract.clauseSearchForRequirement(requirement), "Requirement is missing");
        }
        else{
            boolean numberFound = false;
            Set<Clause> taggedClauses = contract.getClausesFor(requirement);
            for(Clause clause : taggedClauses){
                if(clause.getClauseText().matches(".*\\d.*")){
                    numberFound = true;
                }
            }
            if(numberFound == false){
                return new Finding(requirement, Status.WEAK, contract.clauseSearchForRequirement(requirement), "Requirement is weak");
            }
            return new Finding(requirement, Status.COVERED, contract.clauseSearchForRequirement(requirement), "Requirement is covered");
        }    
    }
}
