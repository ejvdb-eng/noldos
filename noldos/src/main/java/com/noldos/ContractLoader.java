package com.noldos;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.nio.file.Files;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

public class ContractLoader {
    /*Accepts a file location, verifies it, converts the raw JSON data into Java 
    Records, and runs business rules before returning a valid contract.*/
    
    public TotalContract load(Path path){
        String fileName = path.getFileName().toString();

        if(!Files.exists(path)){
            /*Checks the physical storage of your computer to see if a file is 
            actually present at the specified Path.*/
            throw new ContractLoadException(fileName, "missing file");
        }
        try {
        ObjectMapper mapper = new ObjectMapper();
        TotalContract contract = mapper.readValue(path.toFile(), TotalContract.class);
        /*Reads the raw text inside your JSON file and maps each JSON key (like "provider") to the corresponding 
         field in your Java Record structure (TotalContract)*/

        if(contract.listOfLooseClauses() == null){
            throw new ContractLoadException(fileName, "invalid JSON");
        }
        if(contract.provider() == null || contract.provider().isBlank()){
            throw new ContractLoadException(fileName, "invalid JSON");
        }

        List<LooseClause> allClauses = contract.listOfLooseClauses();
        Set<String> seenNumbers = new HashSet<>();

        for(LooseClause clause : allClauses){
            if(clause.number() == null || clause.number().isBlank()){
                throw new ContractLoadException(fileName, "clause number blank");
            }
            if(!seenNumbers.add(clause.number())){
                throw new ContractLoadException(fileName, "duplicate clause number");
            }
        }
        
        return contract;

        }
        catch (JsonParseException e) {
            // Specifieke syntaxfouten (missende komma's, accolades) met regel en kolom
            JsonLocation loc = e.getLocation();
            String locationInfo = String.format(" (line %d, column %d)", loc.getLineNr(), loc.getColumnNr());
            throw new ContractLoadException(fileName, "invalid JSON" + locationInfo, e);

        } catch (UnrecognizedPropertyException e) {
            // Typfout in een JSON-sleutelnaam (toont de onbekende sleutel)
            throw new ContractLoadException(fileName, "invalid JSON: unrecognized key '" + e.getPropertyName() + "'", e);
            
        } catch (InvalidFormatException e) {
            // Conversiefout (verkeerd type zoals "banana" voor boolean, of ongeldige enum)
            if (e.getTargetType() != null && e.getTargetType().isEnum() && e.getTargetType().getSimpleName().equals("Requirement")) {
                throw new ContractLoadException(fileName, "unknown requirement name: " + e.getValue(), e);
            }
            throw new ContractLoadException(fileName, "invalid JSON: cannot convert '" + e.getValue() + "' to " + e.getTargetType().getSimpleName(), e);

        } catch (IOException e) {
            // Overige algemene I/O of mapping fouten
            throw new ContractLoadException(fileName, "invalid JSON", e);
        }

    }
}
