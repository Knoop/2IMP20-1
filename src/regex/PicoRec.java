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

    private LookaheadReader input;



    /**
     * Creates a new recognizer that operates from the given InputStream.
     *
     * @param stream
     */
    public PicoRec(InputStream stream) {
        this.input = new LookaheadReader(stream);
    }

    /**
     * Creates a new
     *
     * @param string
     */
    public PicoRec(String string) {
        this.input = new LookaheadReader(new StringReader(string));
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
        try {
            this.recognizeProgram();
            return true;
        } catch (ParseException e) {
            e.getCause().printStackTrace();
            return false;
        }
    }

    /**
     * Obtain the next character from the InputStream. Newline characters and spaces are ignored.
     *
     * @return
     */
    private char next() throws ParseException {
        try {
            return this.input.read();
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    private char peek() throws ParseException {
        try {
            return this.input.peek();
        } catch (IOException e) {
            throw new ParseException(e);
        }
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

    /**
     * Matches the next character to be between the indicated inclusive bounds
     * @param lower The lowest acceptable character (inclusive).
     * @param upper The highest acceptable character (inclusive).
     */
    private void match(char lower, char upper){
        try {
            char actual = this.next();
            if (!PicoRec.withinInterval(actual, lower, upper))
                throw new MisMatchException(lower, upper, actual);
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }

    public static boolean withinInterval(char value, char lower, char upper){
        return lower <= value && value <= upper;
    }

    public static class ParseException extends RuntimeException {

        private ParseException(Exception cause){
            super(cause);
        }

    }

    private static class LookaheadReader {

        /**
         * Counters that show where we are going wrong
         */
        private int lineCounter = 1, charCounter = 0, columnCounter = 1;

        private Reader reader;
        private Character peek = null;

        private LookaheadReader(InputStream stream){
            this(new InputStreamReader(stream));
        }
        private LookaheadReader(Reader reader) {
            this.reader = reader;
        }

        /**
         * Look ahead one symbol from the current position for {@code read}. In other words, {@code peek} indicates the
         * value that would come from calling {@code read}.
         * As soon as the {@code peek} value has been
         * set it won't change until a new character is read. This means that calling peek has no influence on what
         * read
         * will return, no matter how often you call it.
         *
         * @return The character that comes after the position of {@code read}.
         * @throws IOException If the next value couldn't be obtained from the input.
         */
        private char peek() throws IOException {
            // If the peek already exists, return that, otherwise read the next character and store it as the peek
            return this.peek = (this.peek != null) ? this.peek : this.read();
        }

        /**
         * Increase the current position for {@code read} by reading the next symbol. This resets the value of {@code
         * peek}. If {@code peek} wa
         *
         * @return The next character from the input
         * @throws IOException If the next value couldn't be obtained from the input.
         */
        private char read() throws IOException {
            if(this.peek != null){
                char c = this.peek;
                this.peek = null;
                return c;
            }
            else {
                int c;
                do {
                    c = this.reader.read();

                    // Update counters: increase general count
                    ++this.charCounter;
                    ++this.columnCounter;

                    // If it is newline,
                    if (c == '\n') {
                        ++this.lineCounter;
                        this.columnCounter = 1;
                    }

                } while (c == ' ' || c == '\n' || c == '\r');

                return (char)c;
            }

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
