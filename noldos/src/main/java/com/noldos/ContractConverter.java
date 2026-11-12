package com.noldos;

import java.util.List;

public class ContractConverter {

    public Contract toContract(String fileName, TotalContract file){
        Contract contract = new Contract(file.provider(), file.criticalOrImportant());
        try {
            for (LooseClause clause : file.clauses()) {
                contract.addClause(clause.number(), clause.text());
                List<Requirement> covers = clause.covers() == null ? List.of() : clause.covers();
                for (Requirement requirement : covers) {
                    contract.tag(clause.number(), requirement);
                }
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ContractLoadException(fileName, e.getMessage(), e);
        }
        return contract;
    }
}
    
    

