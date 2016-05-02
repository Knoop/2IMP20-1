/*
 * provide names and student id numbers here
 */

package regex;

import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;
import gui.RegexBuilderForm;

public class RegexTest {
    
    private final RunAutomaton r;

    public RegexTest(String regex) {
    	System.out.println("regular expression = " + regex);
        r =  new RunAutomaton(new RegExp(regex).toAutomaton());
    }
        
    public long dfaMatch(String input, int index) {
        long start = System.nanoTime();
        int length = r.run(input, index);
        long end = System.nanoTime();
        
        if (length == -1) {
            System.out.println("No match found!");
        } else {
            String s = input.substring(index, index + length);
            System.out.println("Found: " + s);
        }
        
        return end - start;
    }
        
    void runTest(String input, int index) {
    	System.out.println("input string = " + input);
    	System.out.println("index = " + index);
    	
        long dfaMatchTime = dfaMatch(input, index);
        System.out.println("dfaMatchTime " + dfaMatchTime);
    }
    
    public static void main(String[] args) {
        RegexBuilderForm form = new RegexBuilderForm();

        RegexTest exampleRegexTest = new RegexTest("[a-z][a-z0-9]*");
        exampleRegexTest.runTest("aap_df34_d asdf sdfd", 0);
        exampleRegexTest.runTest("a_a_pasdf sdfd", 0);
    }
}
