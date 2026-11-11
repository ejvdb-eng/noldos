package com.noldos;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ContractLoaderTest {

    @Test
    public void missingFileTest(@TempDir Path tempDir){
        Path missingPath = tempDir.resolve("non-existent.json");
        ContractLoader contractloader = new ContractLoader();
        ContractLoadException exception = assertThrows(ContractLoadException.class, () -> {
        contractloader.load(missingPath);
        });
        String text = exception.getMessage();
        assertTrue(text.contains("missing file"));
        assertTrue(text.contains("non-existent.json"));
    }

    /*The method Files.writeString() can throw a IOException (input/output exception) 
    if something goes wrong. But you have to write this in the method header throws Exception*/
    @Test
    public void invalidJSONTest(@TempDir Path tempDir) throws Exception{
        Path missingPath = tempDir.resolve("invalid.json");
        ContractLoader contractloader = new ContractLoader();
        Files.writeString(missingPath, "{" + "broken json without closing accolades");
        ContractLoadException exception = assertThrows(ContractLoadException.class, () -> {
        contractloader.load(missingPath);
        });
        String text = exception.getMessage();
        assertTrue(text.contains("invalid.json"));
        assertTrue(text.contains("invalid JSON"));
    }

    @Test
    public void clauseNumberBlankTest(@TempDir Path tempDir) throws Exception{
        Path path = tempDir.resolve("blank-number.json");
        ContractLoader contractloader = new ContractLoader();
        String jsonContent = """
        {
        "provider": "Acme Test",
        "criticalOrImportant": false,
        "listOfLooseClauses": [
        { "number": " ", "text": "Blank number test", "covers": ["SERVICE_DESCRIPTION"] }
    ]
    }
    """;
        Files.writeString(path, jsonContent);
        ContractLoadException exception = assertThrows(ContractLoadException.class, () -> {
        contractloader.load(path);
        });
        String text = exception.getMessage();
        assertTrue(text.contains("clause number blank"));
    }

    @Test
    public void duplicateClauseNumberTest(@TempDir Path tempDir) throws Exception{
        Path path = tempDir.resolve("blank-number.json");
        ContractLoader contractloader = new ContractLoader();
        String jsonContent = """
        {
        "provider": "Acme Test",
        "criticalOrImportant": false,
        "listOfLooseClauses": [
        { "number": "1", "text": "First clause", "covers": ["SERVICE_DESCRIPTION"] },
        { "number": "1", "text": "Second clause with duplicate number", "covers": ["DATA_LOCATIONS"]}
    ]
    }
    """;
        Files.writeString(path, jsonContent);
        ContractLoadException exception = assertThrows(ContractLoadException.class, () -> {
        contractloader.load(path);
        });
        String text = exception.getMessage();
        assertTrue(text.contains("duplicate clause number"));
    }

    @Test
    public void unknownRequirementName(@TempDir Path tempDir) throws Exception{
        Path path = tempDir.resolve("blank-number.json");
        ContractLoader contractloader = new ContractLoader();
        String jsonContent = """
        {
        "provider": "Acme Test",
        "criticalOrImportant": false,
        "listOfLooseClauses": [
        { "number": "1", "text": "Blank number test", "covers": ["TYPO_IN_TAG"] }
    ]
    }
    """;
        Files.writeString(path, jsonContent);
        ContractLoadException exception = assertThrows(ContractLoadException.class, () -> {
        contractloader.load(path);
        });
        String text = exception.getMessage();
        assertTrue(text.contains("unknown requirement name"));
    }

    @Test 
    public void validLoadTest(@TempDir Path tempDir) throws Exception{
        Path path = tempDir.resolve("blank-number.json");
        String jsonContent = """
                {
        "provider": "Acme Cloud",
        "criticalOrImportant": false,
        "listOfLooseClauses": [
        { "number": "1", "text": "The provider delivers a hosted data platform.", "covers": ["SERVICE_DESCRIPTION"] },
        { "number": "7", "text": "Either party may terminate with 90 days written notice.", "covers": ["TERMINATION_RIGHTS"] }
        ]
        }
        """; 
        Files.writeString(path, jsonContent);
        ContractLoader contractloader = new ContractLoader();
          
        TotalContract contract = contractloader.load(path);
        assertEquals("Acme Cloud",contract.provider());
    }

    @Test 
    public void weakTest(@TempDir Path tempDir) throws Exception {
    Path path = tempDir.resolve("weak-contract-faulty-key.json");
    
    String jsonContent = """
    {
      "provider": "Acme Cloud",
      "criticalOrImportant": false,
      "clauses": [
        { "number": "1", "text": "The provider delivers a hosted data platform.", "covers": ["SERVICE_DESCRIPTION"] },
        { "number": "7", "text": "Either party may terminate immediately at any time without notice.", "covers": ["DATA_LOCATIONS"] }
      ]
    }
    """;
    Files.writeString(path, jsonContent);
    
    ContractLoader contractloader = new ContractLoader();
    ContractLoadException exception = assertThrows(ContractLoadException.class, () -> {
        contractloader.load(path);
    });
    
    String text = exception.getMessage();
    assertTrue(text.contains("invalid JSON"));
    assertTrue(text.contains("weak-contract-faulty-key.json"));
    }

    @Test
    public void exactReportStringTest(@TempDir Path tempDir) throws Exception {
    Path path = tempDir.resolve("weak-contract-valid-key.json");
    
    String jsonContent = """
    {
      "provider": "Acme Cloud",
      "criticalOrImportant": false,
      "listOfLooseClauses": [
        { "number": "1", "text": "The provider delivers a hosted data platform.", "covers": ["SERVICE_DESCRIPTION"] },
        { "number": "7", "text": "Either party may terminate immediately at any time without notice.", "covers": ["DATA_LOCATIONS"] }
      ]
    }
    """;
    
    Files.writeString(path, jsonContent);
    
    ContractLoader contractloader = new ContractLoader();
    TotalContract contract = contractloader.load(path);
    
    List<Finding> findings = List.of(new Finding(
        Requirement.DATA_LOCATIONS, 
        Status.WEAK, 
        Set.of("7"), 
        "Termination without notice period"
    ));
    
    ReportPrinter printer = new ReportPrinter();
    String result = printer.format(contract, findings);
    
    String expectedReport = "contract report" + System.lineSeparator() +
                           "Acme Cloud" + System.lineSeparator() +
                           "false" + System.lineSeparator() +
                           "WEAK - DATA_LOCATIONS (Clause [7]): Termination without notice period" + System.lineSeparator() +
                           "Total findings: 1";
    
    assertEquals(expectedReport, result);
    }

    
}


    

