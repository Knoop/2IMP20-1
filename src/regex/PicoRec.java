package regex;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by Maurice on 6-5-2016.
 */
public class PicoRec {


    /**
     * Counters that show where we are going wrong
     */
    private int lineCounter = 1, charCounter = 0, columnCounter = 1;
    private Reader input;


    /**
     * Creates a new recognizer that operates from the given InputStream.
     *
     * @param stream
     */
    public PicoRec(InputStream stream) {
        this.input = new InputStreamReader(stream);
    }

    /**
     * Creates a new
     *
     * @param string
     */
    public PicoRec(String string) {
        this.input = new StringReader(string);
    }

    /**
     * Recognize a program, following this definition:
     * {@code PROGRAM ::= "begin" DECLS "|" (STATEMENT ";")* "end"}
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeProgram() throws ParseException {

    }

    /**
     * Recognize a declaration, following this definition:
     * {@code DECLARATION ::= "declare" (ID ",")*}
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeDeclaration() throws ParseException {

    }

    /**
     * Recognize a statement, following this definition:
     * {@code STATEMENT ::= ID ":=" EXP}
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeStatement() throws ParseException {

    }

    /**
     * Recognize an identifier, following this definition:
     * {@code ID ::= [a-z][a-z0-9]*}
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeIdentifier() throws ParseException {

    }

    /**
     * Recognize a natural number, following this definition:
     * {@code NAT ::= [0]|[1-9][0-9]*}
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeNaturalNumber() throws ParseException {

    }

    /**
     * Recognize an expression, following these definitions:
     * {@code EXP ::= ID}
     * {@code EXP ::= NAT}
     * {@code EXP ::= "-" EXP}
     * {@code EXP ::= MULT_EXP}
     * {@code EXP ::= ADD_EXP}
     * {@code EXP ::= "(" EXP ")"}
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeExpression() throws ParseException {

    }

    /**
     * Recognize a multiplication expression, following this definition:
     * {@code MULT_EXP ::= EXP "*" EXP}
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeMultiplicationExpression() throws ParseException {

    }

    /**
     * Recognize an addition expression, following this definition:
     * {@code ADD_EXP ::= EXP "+" EXP}
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeAdditionExpression() throws ParseException {

    }

    /**
     * Recognize the earlier defined InputStream as a valid Pico program.
     *
     * @return {@code true} if the defined InputStream is a valid Pico program, {@code false} otherwise.
     */
    public synchronized boolean recognize() {
        // Do some setup
        try{
            // Try to parse the grammar
            return true;
        }catch(ParseException e){
            return false;
        }
    }

    /**
     * Handle the next character
     *
     * @return
     */
    private char next() throws IOException {

        char c;
        do {
            c = (char) this.input.read();

            // Update counters: increase general count
            ++this.charCounter;
            ++this.columnCounter;

            // If it is newline,
            if (c == '\n') {
                ++this.lineCounter;
                this.columnCounter = 1;
            }

        } while(c ==' ' || c == '\n');

        return c;
    }


    /**
     * Checks whether the next character is any of the given characters
     *
     * @param expected A set of characters that are expected.
     * characters, {@code false} otherwise.
     */
    private void match(char expected) throws ParseException {
        try {
            char actual = this.next();
            if(expected != actual)
                throw new MisMatchException(expected, actual);
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }


    /**
     * Match the next characters to the given expected string. This is equal to continuously calling {@code
     * match(char)}
     * for every character in the expected {@code String} until the end of the {@code String} is reached, or a mismatch
     * occurred.
     *
     * @param expected
     * @return true if the next characters were equal to the given string, false otherwise.
     */
    private void match(String expected) throws ParseException{
        for (int i = 0; i < expected.length(); ++i)
            match(expected.charAt(i));
    }


    public static class ParseException extends RuntimeException {

        private ParseException(Exception cause){
            super(cause);
        }

    }


    public class MisMatchException extends RuntimeException {

        int lineCounter, columnCounter;
        char expected, received;
        private MisMatchException(char expected, char received){
            super("Expected "+expected+" but received "+received+" at "+PicoRec.this.lineCounter+":"+PicoRec.this.columnCounter);
            this.expected = expected;
            this.received = received;
            this.lineCounter = PicoRec.this.lineCounter;
            this.columnCounter = PicoRec.this.columnCounter;
        }
    }





}
