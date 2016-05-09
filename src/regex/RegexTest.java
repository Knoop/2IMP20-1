/*
 * provide names and student id numbers here
 */

package regex;

import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;
import gui.RegexBuilderForm;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        RegexBuilder regexBuilder = new RegexBuilder(new File("expressions.regex"));
        RegexBuilderForm form = new RegexBuilderForm(regexBuilder);

        System.out.println("Exercise 1 tests:");
        printTest(regexBuilder, "ID", TestStrings.idTestsMatch, TestStrings.idTestsNoMatch);
        printTest(regexBuilder, "NAT", TestStrings.natTestsMatch, TestStrings.natTestsNoMatch);
        printTest(regexBuilder, "FLOAT", TestStrings.floatTestsMatch, TestStrings.floatTestsNoMatch);

        System.out.println("==============================");

        System.out.println("Exercise 2 tests:");
        printTest(regexBuilder, "OuterString", TestStrings.ex2TestsMatch, TestStrings.ex2TestsNoMatch);

        System.out.println("==============================");

        System.out.println("Exercise 3 tests:");
        System.out.println("Java comment: ");
        printTest(regexBuilder, "CommentJava", TestStrings.javaCommentTestsMatch, TestStrings.javaCommentTestsNoMatch);
        System.out.println("Matlab comment: ");
        printTest(regexBuilder, "CommentMatlab", TestStrings.matlabCommentTestsMatch, TestStrings.matlabCommentTestsNoMatch);
    }

    /**
     * Checks whether a given input sring matches a given regular expression
     * @param regex A regular exprossion to test
     * @param input The input used to test the regular expression
     * @return true if the full input matches regex (no submatches), otherwise false
     */
    public static boolean hasMatch(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * Prints the results obtained from testing a lexical definition from a given RegexBuilder instance against a
     * number of input strings that should match and a number of input strings that should not match.
     * @param regexBuilder An instance of RegexBuilder
     * @param lexicalID The ID of the lexical definition to test
     * @param matches An array of input strings that should match
     * @param noMatches An array of input strings that should not match
     */
    private static void printTest(RegexBuilder regexBuilder, String lexicalID, String[] matches, String[] noMatches) {
        String regex = regexBuilder.getUnfoldedExpression(lexicalID);
        System.out.println(String.format("Tests for the '%s' lexical definition", lexicalID));
        System.out.println("These should match: ");
        printMatches(regex, matches);

        System.out.println();

        System.out.println("These should not match: ");
        printMatches(regex, noMatches);

        System.out.println();
    }

    /**
     * Prints the results obtained from testing a number of input strings against a given regular expression
     * @param regex
     * @param input
     */
    private static void printMatches(String regex, String[] input) {
        for (String s : input) {
            printMatch(regex, s);
        }
    }

    /**
     * Prints whether a given input string matches a given regular expression
     * @param regex
     * @param input
     */
    private static void printMatch(String regex, String input) {
        if (hasMatch(regex, input)) {
            System.out.println(String.format("%s matches %s", input, regex));
        }
        else {
            System.out.println(String.format("%s does not match %s", input, regex));
        }
    }
}
