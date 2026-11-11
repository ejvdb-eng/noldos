package com.noldos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        assertEquals(15, findings.size());
        for(Finding find : findings){
            assertEquals(Status.MISSING, find.status());
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
        assertEquals(9, findings.size());
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
    public void terminationWithNinetyDaysIsCovered(){
        Contract contract = new Contract("Acme", true);
        contract.addClause("2", "Either party may terminate this agreement with 90 days written notice.");
        contract.tag("2", Requirement.TERMINATION_RIGHTS);
        NoticePeriodRule ntr = new NoticePeriodRule(Requirement.TERMINATION_RIGHTS);
        Finding finding = ntr.evaluate(contract);
        assertEquals(Status.COVERED, finding.status());  
    }

    @Test
    public void ruleIsSkippedForNonCriticalContract(){
        Contract contract = new Contract("Acme", false);
        CoverageRule req = new CoverageRule(Requirement.EXIT_STRATEGIES);
        assertFalse(req.appliesTo(contract));
    }

    @Test
    public void toStringCountsOnlyApplicableRequirements(){
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
        String[] lines = contract.toString().split("\n");
        assertEquals("Acme: 1 clauses, 8 of 9 requirements covered", lines[0]);
        assertEquals("Missing: Art. 30(2)(i) condition for participation", lines[1]);
    }

    @Test
    public void summarizeCountsFindingsPerStatus(){
    List<Finding> findings = new ArrayList<>();
    findings.add(new Finding(Requirement.SERVICE_DESCRIPTION, Status.COVERED, Set.of(), "new msg"));
    findings.add(new Finding(Requirement.SERVICE_DESCRIPTION, Status.COVERED, Set.of(), "msg"));
    findings.add(new Finding(Requirement.SERVICE_DESCRIPTION, Status.WEAK, Set.of(), "other msg"));
    Map<Status, Long> result = ComplianceChecker.summarize(findings);
    assertEquals(2L, result.get(Status.COVERED));
    assertEquals(1L, result.get(Status.WEAK));
    }

    @Test
    public void checkIfNoticePeriodReturnWeak(){
        Contract contract = new Contract("Acme", false);
        contract.addClause("you can cancel the contract", "you can cancel the contract");
        contract.tag("you can cancel the contract", Requirement.TERMINATION_RIGHTS);
        List<Finding> findings = ComplianceChecker.standardDora().check(contract);
        Finding targetFinding = findingFor(findings, Requirement.TERMINATION_RIGHTS);
        assertEquals(Status.WEAK, targetFinding.status());
    }

    @Test
    public void checkIfOneClauseCoversTwoRequirements(){
    Contract contract = new Contract("Acme", false);
    String clauseText = "data protection for up to 60 days and service definitions";
    contract.addClause("1", clauseText);
    contract.tag("1", Requirement.DATA_LOCATIONS);
    contract.tag("1", Requirement.SERVICE_DESCRIPTION);

    assertTrue(contract.getClausesFor(Requirement.DATA_LOCATIONS).stream()
            .anyMatch(c -> c.getClauseText().equals(clauseText)));
    assertTrue(contract.getClausesFor(Requirement.SERVICE_DESCRIPTION).stream()
            .anyMatch(c -> c.getClauseText().equals(clauseText)));

    assertTrue(contract.clauseSearchForRequirement(Requirement.DATA_LOCATIONS).contains("1"));
    assertTrue(contract.clauseSearchForRequirement(Requirement.SERVICE_DESCRIPTION).contains("1"));
    assertEquals(7, contract.missingRequirements().size());
    }

    @Test
    public void checkIfUntaggedClauseLeavesRequirementsMissing() {
    Contract contract = new Contract("Acme", false);
    contract.addClause("1", "this clause is never tagged with any requirement");
    List<Finding> findings = ComplianceChecker.standardDora().check(contract);
    Finding targetFinding = findingFor(findings, Requirement.DATA_LOCATIONS);
    assertEquals(9, contract.missingRequirements().size());
    assertTrue(contract.getClausesFor(Requirement.DATA_LOCATIONS).isEmpty());
    assertEquals(Status.MISSING, targetFinding.status());
    }

    @Test
    public void addClauseThrowsExceptionOnDuplicateClauseNumber() {
    Contract contract = new Contract("Acme", false);
    contract.addClause("1", "first clause text");
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
        contract.addClause("1", "duplicate clause text");
    });
    }

    @Test
    public void addClauseThrowsExceptionWhenNumberIsNull() {
        Contract contract = new Contract("Acme", false);

        assertThrows(NullPointerException.class, () -> {
            contract.addClause(null, "valid clause text");
        });
    }

    @Test
    public void addClauseThrowsExceptionWhenTextIsNull() {
        Contract contract = new Contract("Acme", false);
        assertThrows(NullPointerException.class, () -> {
            contract.addClause("1", null);
        });
    }

    @Test
    public void addClauseThrowsExceptionWhenNumberIsBlank() {
        Contract contract = new Contract("Acme", false);
        assertThrows(IllegalArgumentException.class, () -> {
            contract.addClause(" ", "valid clause text");
        });
    }

    @Test
    public void addClauseThrowsExceptionWhenTextIsBlank() {
        Contract contract = new Contract("Acme", false);
        assertThrows(IllegalArgumentException.class, () -> {
            contract.addClause("1", " ");
        });
    }

    @Test
    public void nonCriticalContractWithNoTagsHasNineRequirementsExcludingExitStrategies() {
        Contract contract = new Contract("Acme", false);
        assertEquals(9, contract.missingRequirements().size());
        assertFalse(contract.missingRequirements().contains(Requirement.EXIT_STRATEGIES));
    }

    @Test
    public void criticalContractWithNoTagsHasFifteenRequirements() {
        Contract contract = new Contract("Acme", true);

        assertEquals(15, contract.missingRequirements().size());
    }

    @Test
    public void missingRequirementsDropsByOneAfterTaggingAnApplicableRequirement() {
        Contract contract = new Contract("Acme", false);
        contract.addClause("1", "data protection for up to 30 days");
        contract.tag("1", Requirement.DATA_LOCATIONS);
        assertEquals(8, contract.missingRequirements().size());

        assertFalse(contract.missingRequirements().contains(Requirement.DATA_LOCATIONS));
    }

    @Test
    public void summarizeOfEmptyListHasNoEntryForMissing() {
    List<Finding> emptyFindings = Collections.emptyList();
    Map<Status, Long> summary = ComplianceChecker.summarize(emptyFindings);

    assertNull(summary.get(Status.MISSING));

    }
}



    

