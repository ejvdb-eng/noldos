package com.noldos;

import java.util.ArrayList;
import java.util.List;

public class ComplianceChecker {

    private final List<Rule> listOfRules;
    
    public ComplianceChecker(List<Rule> rules){
        this.listOfRules = rules;
    }

    public List<Finding> check(Contract contract){
        List<Finding> findings = new ArrayList<>();
        for(Rule rule : listOfRules){
            if(rule.appliesTo(contract)){
                findings.add(rule.evaluate(contract));
            }
        }
        return findings;
    }

    public static ComplianceChecker standardDora(){
        List<Rule> ruleList = new ArrayList<>();
        for(Requirement req : Requirement.values()){
            if(req == Requirement.TERMINATION_RIGHTS){ // with enums you use == or equals()
                ruleList.add(new NoticePeriodRule(req));
            }
            else{
                ruleList.add(new CoverageRule(req));
            }
        }
        return new ComplianceChecker(ruleList);
    }
    
}
