package com.noldos;

public enum Requirement {
    
    SERVICE_DESCRIPTION(
        "Art. 30(2)(a)",
        """
        a clear and complete description of all functions and ICT services to be provided by the ICT third-party service provider,
        indicating whether subcontracting of an ICT service supporting a critical or important function, or material parts
        thereof, is permitted and, when that is the case, the conditions applying to such subcontracting;"""         
    ),
    DATA_LOCATIONS(
        "Art. 30(2)(b)",
        """
        the locations, namely the regions or countries, where the contracted or subcontracted functions and ICT services are to
        be provided and where data is to be processed, including the storage location, and the requirement for the ICT third-
        party service provider to notify the financial entity in advance if it envisages changing such locations;"""              
    ),
    COOPERATION_WITH_AUTHORITIES(
        "Art. 30(2)(g)",
        """
        the obligation of the ICT third-party service provider to fully cooperate with the competent authorities and the 
        resolution authorities of the financial entity, including persons appointed by them;"""
    ),
    TERMINATION_RIGHTS(
        "Art. 30(2)(h)",
        """
        termination rights and related minimum notice periods for the 
        termination of the contractual arrangements, in accordance with the 
        expectations of competent authorities and resolution authorities"""
    );
    
    private final String reference; //keyword final makes sure that nothing change the regulation text;
    private final String description;

    Requirement(String reference, String description){ //enum constructor never has a public or private 
        this.reference = reference;
        this.description = description;
    }

    public String getReference(){
        return reference;
    }

    public String getDescription(){
        return description;
    }
}
