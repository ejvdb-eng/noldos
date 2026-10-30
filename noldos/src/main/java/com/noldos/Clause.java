package com.noldos;

public class Clause {

    private final String clauseNumber;
    private final String clauseText;

    public Clause(String clausenumber, String clausetext){
        if(clausenumber == null || clausetext == null){
            throw new NullPointerException("Cannot be null");
        }
        if(clausenumber.isBlank() || clausetext.isBlank()){
            throw new IllegalArgumentException("Both must be filled in"); 
        }
        this.clauseNumber = clausenumber;
        this.clauseText = clausetext;
    }

    public String getClauseNumber(){
        return clauseNumber;
    }

    public String getClauseText(){
        return clauseText;
    }


    
}
