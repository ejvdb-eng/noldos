package com.noldos;

import java.util.List;

public record TotalContract(String provider, boolean criticalOrImportant, List<LooseClause> clauses) {
    
}
