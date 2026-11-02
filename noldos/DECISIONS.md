I added the flag for whether it supports a critical or important function to the constructor. This is done as the flag is an inherent and important property of the contract. When using a setter, you might risk the chance of forgetting to set the contract to the right flag. This is not possible now as an object can't be created without the flag.

BUG LOG:
- The bug for standardDora() and the NoticePeriodRule was solved by adding an else. first I had if(condition) with return after the if. This means that you return both, if the condition is met. implementing an else after the if solves this.
- I first had the Flag in contract as a String, which didn't give the right/wrong options I wanted, so this had to be change to a boolean.
- The same goes for the flag in the contract. A String could return anything instead of just the yes/no, true/false I wanted, such as "banana. Making this a boolean solved this.