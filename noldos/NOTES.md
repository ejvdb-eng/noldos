Every ICT contract must have at least 9 core clauses. These clauses are the rules stated in enum Requirement such as SERVICE_DESCRIPTION and SERVICE_LOCATIONS. So this is what is meant with clause. 

Clause is a piece of the contract being checked. It is data about one specific contract: a number and some text, like "7" / "you can cancel the contract". Every contract has its own clauses.

Requirement is something the regulation demands. It comes from DORA Art. 30 and is the same for every contract: a reference, a description and a scope. It is a checklist item.

A tag connects the two: “clause 7 covers termination rights”. That is what tagMap stores, a map from clause number to a set of requirements. The relationship is many to many, because one clause can cover several requirements and one requirement can be covered by several clauses.

So the checker asks, for each requirement on the checklist, which clauses in this contract (if any) are tagged with it. No clauses means MISSING.

pushing to github: 
(1) git add . 
(2) git commit -m "insert message of choice here"
(3) git push

super() is always on the first line of the constructor. 

A Set knows if he has a duplicate item. You don't have to loop it searching for duplicates. 

Consider doing the following when writing code to enhance maintainability: 
• Design
• Clean code
• Documentation
• Automatized tests

Test one item per test. Otherwise the test may fail but you don't know why. Keep it at one difference every time. 



    