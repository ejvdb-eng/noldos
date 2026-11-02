package com.noldos;

import java.util.Set;

public record Finding(Requirement requirement, Status status, Set<String> clauseNumbers, String message) {

}
