package com.noldos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ComplianceCheckerTest {

    private final ComplianceChecker checker = ComplianceChecker.standardDora();

    private Finding findingFor(List<Finding> findings, Requirement requirement) {
        return findings.stream()
                .filter(f -> f.requirement() == requirement)
                .findFirst()
                .orElseThrow();
    }

    @Test
    public void checkHowManyRules(){
        Contract contract1 = new Contract("Acme studios", true);
        Contract contract2 = new Contract("deb enterprises", false);
        ComplianceChecker checker = ComplianceChecker.standardDora();
        List<Finding> findings1 = checker.check(contract1);
        List<Finding> findings2 = checker.check(contract2);
        assertEquals(15, findings1.size());
        assertEquals(9, findings2.size());
    }

    @Test 
    public void checkIfMissingWorks(){
        Contract contract = new Contract("Acme studios", true);
        ComplianceChecker checker = ComplianceChecker.standardDora();
        List<Finding> findings = checker.check(contract);
        for(Finding find : findings){
            assertEquals(Status.MISSING, find.status());
        }
    }

    @Test
    public void checkOneTaggedClauseWithTwoRequirements(){
        Contract contract = new Contract("Google", false);
        contract.addClause("2", "data optimising");
        contract.tag("2", Requirement.SERVICE_DESCRIPTION);
        contract.tag("2", Requirement.DATA_LOCATIONS);
        ComplianceChecker checker = ComplianceChecker.standardDora();
        List<Finding> findings = checker.check(contract);
        for(Finding f : findings){
        assertEquals(Status.COVERED, f.status());
        }
    }

    @Test
    public void checkTerminationWithoutDigit(){
        Contract contract1 = new Contract("UBS", false);
        Contract contract2 = new Contract("Skillpoint", true);
        contract2.addClause("5", " 90 days ");
        contract1.addClause("notanumber", "data optimising");
        ComplianceChecker checker = ComplianceChecker.standardDora();
        List<Finding> findings1 = checker.check(contract1);
        List<Finding> findings2 = checker.check(contract2);
        for(Finding f : findings2){
            assertEquals(Status.COVERED, f.status());
        }
        for(Finding f : findings1){
            assertEquals(Status.WEAK, f.status());
        }
    }

    @Test
    public void checkSkipAppliesTo(){
        Contract contract = new Contract("Nordea", true);
        CoverageRule rule = new CoverageRule(Requirement.NOTIFICATION_OF_DEVELOPMENT);
        assertTrue(rule.appliesTo(contract));
    }

    @Test
    public void testRuleThatDoesNotApplyIsSkipped() {
        Contract contract = new Contract("Company B", false);
        ComplianceChecker checker = ComplianceChecker.standardDora();
        List<Finding> findings = checker.check(contract);
        for (Finding f : findings) {
            assertNotEquals(Scope.CRITICAL_OR_IMPORTANT, f.requirement().getScope());
        }
    }

    @Test
    public void testTaggingUnknownClauseThrowsException() {
        Contract contract = new Contract("Company Test", false);
        assertThrows(IllegalArgumentException.class, () -> {
        contract.tag("99", Requirement.SERVICE_DESCRIPTION);
    });
    }

    @Test
    public void checkReportSummaryCount(){
        Contract contract = new Contract("Nordea", false);
        ComplianceChecker checker = ComplianceChecker.standardDora();
        List<Finding> findings = checker.check(contract);
        for(Finding find : findings){
            
    }

    }
}
