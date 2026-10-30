package com.noldos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class Contract {

    private final String providerName;
    private final List<Clause> listOfClauses;
    private final Map<String, Set<Requirement>> tagMap;
    
    public Contract(String name){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Name not correctly filled in");
        }
        this.providerName = name;
        this.listOfClauses = new ArrayList<>();
        this.tagMap = new HashMap<>();
    }

    public List<Clause> getClauses(){
        return List.copyOf(listOfClauses);
    }

    public void addClause(String number, String text){
        boolean isDuplicate = listOfClauses.stream()
            .anyMatch(item -> item.getClauseNumber().equals(number));
                         
            if(isDuplicate){
                throw new IllegalStateException("This number is already in the set. No duplicates allowed.");
            }
            else{
                listOfClauses.add(new Clause(number, text));
            }
    }
    // A method Contract.tag(String clauseNumber, Requirement requirement)
    // that manually records that a clause covers a requirement. 
    // Unknown clause number: throw IllegalArgumentException.

    public void tag(String clauseNumber, Requirement requirement){
        boolean exists = listOfClauses.stream()
            .anyMatch(item -> item.getClauseNumber().equals(clauseNumber));

            if(!exists){
                throw new IllegalArgumentException("Unknown clause number");
            }
            if(!tagMap.containsKey(clauseNumber)){
                tagMap.put(clauseNumber, new HashSet<>());
            }
            tagMap.get(clauseNumber).add(requirement);
    }

    public Set<Requirement> missingRequirements(){
        Set<Requirement> notaggedclause = EnumSet.allOf(Requirement.class);
        
        for(String value : tagMap.keySet()){
            Set<Requirement> reqset = tagMap.get(value);
            for(Requirement req : reqset){
               notaggedclause.remove(req);
            }
        }
        return notaggedclause;
    }

    @Override
    public String toString(){
        int coveredReq = Requirement.values().length - missingRequirements().size();

        String line1 = providerName + ": " + listOfClauses.size() + " clauses, " + coveredReq + " of 4 requirements covered";
        String text = missingRequirements().stream()
                .map(item -> item.getReference() + " " +item.name().toLowerCase().replace('_',' '))
                .collect(Collectors.joining(", "));
        String line2 = "Missing: " + text;
        return line1 + "\n" +  line2;
    }

    //this method gives the clauses that cover these (input) requirements.
    public Set<String> clauseSearchForRequirement(Requirement requirement){
        Set<String> listofclauses = new HashSet<>();
        for(String number : tagMap.keySet()){ //The for loop only works on iterable arrays or classes (List or Set, not Map)
            Set<Requirement> reqset = tagMap.get(number); // so use tagmap.keySet() here as you only want to loop over the keys. 
            if(reqset.contains(requirement)){
                listofclauses.add(number);
            }
        }
        return listofclauses;
    }

    public Set<Clause> getClausesFor(Requirement requirement){
        Set<Clause> clauseSet = new HashSet<>();
        for(Clause clause : listOfClauses){
            String clauseNumber = clause.getClauseNumber();
            Set<Requirement> requirementsForThisClause = tagMap.get(clauseNumber);
            if(requirementsForThisClause != null && requirementsForThisClause.contains(requirement)){
                    clauseSet.add(clause);   
            }
        }
        return clauseSet;
    }
}
