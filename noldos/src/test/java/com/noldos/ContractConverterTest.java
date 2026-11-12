package com.noldos;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class ContractConverterTest {


    @Test
    public void acmeWeakMapsClause7ToTerminationRights() {
    ContractLoader loader = new ContractLoader();
    TotalContract raw = loader.load(Path.of("src/test/resources/samples/acme-weak.json"));
    
    ContractConverter converter = new ContractConverter();
    Contract contract = converter.toContract("acme-weak.json", raw);

    var clauses = contract.getClausesFor(Requirement.TERMINATION_RIGHTS);
    assertTrue(clauses.stream().anyMatch(c -> c.getClauseNumber().equals("7"))); // Gebruik == 7 als number een int is
    }

    @Test
    public void duplicateClauseNumberThrowsContractLoadException() {
    LooseClause first = new LooseClause("1", "First text", List.of());
    LooseClause duplicate = new LooseClause("1", "Second text", List.of());
    TotalContract raw = new TotalContract("Acme", false, List.of(first, duplicate));

    ContractConverter converter = new ContractConverter();
    assertThrows(ContractLoadException.class, () -> {
        converter.toContract("test.json", raw);
    });
    }

    @Test
    public void clauseWithoutCoversHasEmptyTags() {
    LooseClause clauseWithoutCovers = new LooseClause("1", "Some text", null);
    TotalContract raw = new TotalContract("Acme", false, List.of(clauseWithoutCovers));

    ContractConverter converter = new ContractConverter();
    Contract contract = converter.toContract("test.json", raw);

    // Check voor alle mogelijke Requirements of deze clause nergens getagd is:
    for (Requirement req : Requirement.values()) {
        assertTrue(contract.getClausesFor(req).isEmpty());
    }
    }

}
