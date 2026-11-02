package com.noldos;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        
        Contract contract = new Contract("Acme Cloud", false);
        contract.addClause("1", "dataprovider");
        contract.addClause("7", "you can cancel the contract");
        contract.tag("1", Requirement.SERVICE_DESCRIPTION);
        contract.tag("7", Requirement.TERMINATION_RIGHTS);

        if(contract.getFlag() == false){
            System.out.println(contract.getName() + " (critical or important function: no)");
        }
        else{
            System.out.println(contract.getName() + " (critical or important function: yes)");
        }
        System.out.println("----------------------------------------------");

        ComplianceChecker checker = ComplianceChecker.standardDora();
        List<Finding> findings = checker.check(contract);
        for(Finding find : findings){
            String clauseText = "";
            if (!find.clauseNumbers().isEmpty()){
                clauseText = " (clause " + find.clauseNumbers().toString().replace("[", "").replace("]", "") + ")";
            }
            System.out.printf("%-9s %-15s %s%s\n", 
            find.status(), 
            find.requirement().getReference(), 
            find.requirement().name().toLowerCase().replace("_", " "), 
            clauseText );
        }
        Map<Status, Long> summaryMap = findings.stream()
                .collect(Collectors.groupingBy(Finding::status,Collectors.counting()));
        
                summaryMap.getOrDefault(Status.COVERED, 0L); // if a contract has 0 covered requirements, the collector would skip
                summaryMap.getOrDefault(Status.WEAK, 0L); // this contract and won't make a object for status.covered.
                summaryMap.getOrDefault(Status.MISSING, 0L); // getOrDefault prevents this. 0l is number 0 but treat it as Long.

            System.out.println("----------------------------------------------");
            System.out.println("Summary: " + summaryMap.getOrDefault(Status.COVERED, 0L) + " covered, " + summaryMap.getOrDefault(Status.WEAK, 0L) + " weak, " + summaryMap.getOrDefault(Status.MISSING, 0L) + " missing");
        }   
}
