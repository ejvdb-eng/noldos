package com.noldos;

public class NextApp {

    public static void main(String[] args){
        Contract contract = new Contract("Acme", false);
        
        contract.addClause("1", "data protection for up to 30 days");
        contract.tag("1", Requirement.DATA_LOCATIONS); 
        contract.tag("1", Requirement.SERVICE_DESCRIPTION);
        contract.tag("1", Requirement.PROVISIONS_FOR_DATA_PROTECTION);
        contract.tag("1", Requirement.PROVISIONS_FOR_DATA_ACCESS);
        contract.tag("1", Requirement.SERVICE_LEVEL_DESCRIPTION);
        contract.tag("1", Requirement.PROVIDE_ASSISTANCE_INCIDENT);
        contract.tag("1", Requirement.COOPERATION_WITH_AUTHORITIES);
        contract.tag("1", Requirement.TERMINATION_RIGHTS);
        System.out.println(contract.toString());
        
    }
    
}
