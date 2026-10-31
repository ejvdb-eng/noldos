package com.noldos;

public enum Requirement {
    
    SERVICE_DESCRIPTION(
        "Art. 30(2)(a)",
        """
        a clear and complete description of all functions and ICT services to be provided by the ICT third-party service provider,
        indicating whether subcontracting of an ICT service supporting a critical or important function, or material parts
        thereof, is permitted and, when that is the case, the conditions applying to such subcontracting;""",
        Scope.ALL_CONTRACTS         
    ),
    DATA_LOCATIONS(
        "Art. 30(2)(b)",
        """
        the locations, namely the regions or countries, where the contracted or subcontracted functions and ICT services are to
        be provided and where data is to be processed, including the storage location, and the requirement for the ICT third-
        party service provider to notify the financial entity in advance if it envisages changing such locations;""",  
        Scope.ALL_CONTRACTS            
    ),
    PROVISIONS_FOR_DATA_PROTECTION(
        "Art. 30(2)(c)",
        """
        provisions on availability, authenticity, integrity and confidentiality in relation to the protection of data, including
        personal data;""",
        Scope.ALL_CONTRACTS
    ),
    PROVISIONS_FOR_DATA_ACCESS(
        "Art. 30(2)(d)",
        """
        provisions on ensuring access, recovery and return in an easily accessible format of personal and non-personal data
        processed by the financial entity in the event of the insolvency, resolution or discontinuation of the business
        operations of the ICT third-party service provider, or in the event of the termination of the contractual arrangements;""",
        Scope.ALL_CONTRACTS
    ),
    SERVICE_LEVEL_DESCRIPTION(
        "Art. 30(2)(e)",
        """
        service level descriptions, including updates and revisions thereof;""",
        Scope.ALL_CONTRACTS
    ),
    PROVIDE_FINANCIAL_ASSISTENCE(
        "Art. 30(2)(f)",
        """
        the obligation of the ICT third-party service provider to provide assistance to the financial entity at no additional cost,
        or at a cost that is determined ex-ante, when an ICT incident that is related to the ICT service provided to the financial
        entity occurs;""",
        Scope.ALL_CONTRACTS
    ),
    COOPERATION_WITH_AUTHORITIES(
        "Art. 30(2)(g)",
        """
        the obligation of the ICT third-party service provider to fully cooperate with the competent authorities and the 
        resolution authorities of the financial entity, including persons appointed by them;""",
        Scope.ALL_CONTRACTS
    ),
    TERMINATION_RIGHTS(
        "Art. 30(2)(h)",
        """
        termination rights and related minimum notice periods for the 
        termination of the contractual arrangements, in accordance with the 
        expectations of competent authorities and resolution authorities;""",
        Scope.ALL_CONTRACTS
    ),
    CONDITION_FOR_PARTICIPATION(
        "Art. 30 (2)(i)",
        """
        the conditions for the participation of ICT third-party service providers in the financial entities' ICT security awareness
        programmes and digital operational resilience training in accordance with Article 13(6);""",
        Scope.ALL_CONTRACTS
    ),
    SERVICE_LEVEL_DESCRIPTIONS(
        "Art. 30(3)(a)",
        """
        full service level descriptions, including updates and revisions thereof with precise quantitative and qualitative
        performance targets within the agreed service levels to allow effective monitoring by the financial entity of ICT services
        and enable appropriate corrective actions to be taken, without undue delay, when agreed service levels are not met;""",
        Scope.CRITICAL_OR_IMPORTANT
    ),
    NOTIFICATION_OF_DEVELOPMENT(
        "Art. 30(3)(b)",
        """
        notice periods and reporting obligations of the ICT third-party service provider to the financial entity, including
        notification of any development that might have a material impact on the ICT third-party service provider’s ability to
        effectively provide the ICT services supporting critical or important functions in line with agreed service levels;""",
        Scope.CRITICAL_OR_IMPORTANT
    ),
    BUSINESS_CONTINGENCY_PLANS(
        "Art. 30(3)(c)",
        """
        requirements for the ICT third-party service provider to implement and test business contingency plans and to have in
        place ICT security measures, tools and policies that provide an appropriate level of security for the provision of services
        by the financial entity in line with its regulatory framework;""",
        Scope.CRITICAL_OR_IMPORTANT
    ),
    OBLIGATION_TO_PARTICIPATE(
        "Art. 30(3)(d)",
        """
        the obligation of the ICT third-party service provider to participate and fully cooperate in the financial entity’s TLPT as
        referred to in Articles 26 and 27;""",
        Scope.CRITICAL_OR_IMPORTANT
    ), 
    RIGHT_TO_MONITOR(
        "Art. 30(3)(e)",
        """
        the right to monitor, on an ongoing basis, the ICT third-party service provider’s performance, which entails the
        following: 
        (i) unrestricted rights of access, inspection and audit by the financial entity, or an appointed third party, and by the
        competent authority, and the right to take copies of relevant documentation on-site if they are critical to the
        operations of the ICT third-party service provider, the effective exercise of which is not impeded or limited by
        other contractual arrangements or implementation policies;
        (ii) the right to agree on alternative assurance levels if other clients’ rights are affected;
        (iii) the obligation of the ICT third-party service provider to fully cooperate during the onsite inspections and audits
        performed by the competent authorities, the Lead Overseer, financial entity or an appointed third party; and
        (iv) the obligation to provide details on the scope, procedures to be followed and frequency of such inspections and
        audits;""",
        Scope.CRITICAL_OR_IMPORTANT
    ),
    EXIT_STRATEGIES(
        "Art. 30(3)(f)",
        """
        exit strategies, in particular the establishment of a mandatory adequate transition period:        
        (i) during which the ICT third-party service provider will continue providing the respective functions, or ICT services,
        with a view to reducing the risk of disruption at the financial entity or to ensure its effective resolution and
        restructuring;
        (ii) allowing the financial entity to migrate to another ICT third-party service provider or change to in-house solutions
        consistent with the complexity of the service provided.""",
        Scope.CRITICAL_OR_IMPORTANT
    );

    private final String reference; //keyword final makes sure that nothing change the regulation text;
    private final String description;
    private final Scope scope;

    Requirement(String reference, String description, Scope scope){ //enum constructor never has a public or private 
        this.reference = reference;
        this.description = description;
        this.scope = scope;
    }

    public String getReference(){
        return reference;
    }

    public String getDescription(){
        return description;
    }

    public Scope getScope(){
        return scope;
    }
}
