package com.noldos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit test for simple App.
 */
public class AppTest 
{

    //assertEquals(expected, actual)
    @Test
    public void testGetClauseNumber(){
        Clause clause = new Clause("21", "Termination clause");
        String result = clause.getClauseNumber();
        assertEquals("21", result);
    }

    @Test
    public void testClause(){
        assertThrows(IllegalArgumentException.class, () -> {
            Clause clause = new Clause("21", " ");
        });
    }

    @Test
    public void testAddClause(){
        Contract contract = new Contract("termination");
        contract.addClause("1", "bla bla");
        assertTrue(contract.toString().contains("1"));
    }

    @Test
    public void testTag(){
        Contract contract = new Contract("TestProvider");
        contract.addClause("Art. 1", "The provider delivers service X");
        assertThrows(IllegalArgumentException.class, ()-> {
            contract.tag("Art. 99", Requirement.SERVICE_DESCRIPTION);
        });
    }

    @Test
    public void testToString(){
        Contract contract = new Contract("CloudCorp");
        assertEquals("CloudCorp: 0 clauses, 0 of 4 requirements covered\n" + //
                        "Missing: Art. 30(2)(a) service description, Art. 30(2)(b) data locations, Art. 30(2)(g) cooperation with authorities, Art. 30(2)(h) termination rights"
                        , contract.toString());
    }

    @Test
    public void testAddDuplicateClauseThrowsException(){
        Contract contract = new Contract("UBS"); //Just a random company name; 
        contract.addClause("1", "The provider delivers service X");
        assertThrows(IllegalStateException.class, ()->{
            contract.addClause("1", "The provider delivers service X");
        });
    }
    
}
