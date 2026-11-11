package com.noldos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    static Map<Status, Long> summarize(List<Finding> findings){
        return findings.stream()
                .collect(Collectors.groupingBy(Finding::status,Collectors.counting()));
    }
    
}
