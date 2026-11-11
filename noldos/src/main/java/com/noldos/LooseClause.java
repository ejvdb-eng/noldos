package com.noldos;

import java.util.List;

public record LooseClause(String number, String text, List<Requirement> covers) {
    
}
