package regex;

import regex.PicoTokenizer.Token;

import java.io.InputStream;

/**
 * Created by Maurice on 6-5-2016.
 */
public class PicoRec {

    /**
     * The tokenizer used by this recognizer.
     */
    private PicoTokenizer tokenizer;

    /**
     * Creates a new {@code PicoRecognizer} that aims to recognize the content represented by the {@code InputStream} as
     * a Pico program. To see whether the given string is a Pico program, call {@code recognize}.
     *
     * @param stream The stream which content should be inspected as being a Pico program.
     */
    public PicoRec(InputStream stream) {
        this.tokenizer = new PicoTokenizer(stream);
    }

    /**
     * Creates a new {@code PicoRecognizer} that aims to recognize the given string as a Pico program. To see whether
     * the given string is a Pico program, call {@code recognize}.
     *
     * @param string The string that contains the code that should be recognized as a Pico program.
     */
    public PicoRec(String string) {
        this.tokenizer = new PicoTokenizer(string);
    }

    /**
     * Recognize a program, following this definition:
     * {@code PROGRAM ::= "begin" DECLS "|" (STATEMENT ";")* "end"}
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeProgram() throws ParseException {

        this.match(Token.Type.BEGIN);
        this.recognizeDeclarations();
        this.match(Token.Type.DECLARATIONS_END);
        this.recognizeStatements();
        this.match(Token.Type.END);

    }

    /**
     * Recognize a single declaration, following this definition:
     * {@code ID ","}.
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeDeclaration() throws ParseException {
        this.match(Token.Type.IDENTIFIER);
        this.match(Token.Type.DECLARATION_END);
    }

    /**
     * Recognize the declaration header, following this definition:
     * {@code DECLARATION ::= "declare" (ID ",")*}
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeDeclarations() throws ParseException {
        this.match(Token.Type.DECLARE);

        while (this.tokenizer.peek().type != Token.Type.DECLARATIONS_END)
            this.recognizeDeclaration();
    }

    /**
     * Recognize a single statement, following this definition:
     * {@code STATEMENT ::= ID ":=" EXP ";" }
     * @throws ParseException
     */
    private void recognizeStatement() throws ParseException {
        this.match(Token.Type.IDENTIFIER);
        this.match(Token.Type.ASSIGN);
        this.recognizeExpression();
        this.match(Token.Type.STATEMENT_END);
    }

    /**
     * Recognize multiple statements, following this definition:
     * {@code STATEMENT*}
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeStatements() throws ParseException {
        while (this.tokenizer.peek().type != Token.Type.END)
            this.recognizeStatement();
    }


    /**
     * Recognize an expression, following these definitions:
     * {@code EXP ::= ID}
     * {@code EXP ::= NAT}
     * {@code EXP ::= "-" EXP}
     * {@code EXP ::= MULT_EXP}
     * {@code EXP ::= ADD_EXP}
     * {@code EXP ::= "(" EXP ")"}
     *
     * @throws ParseException When the input couldn't be parsed.
     */
    private void recognizeExpression() throws ParseException {

        // Obtain the next token and see what type of expression the left hand is
        Token t = this.tokenizer.next();
        switch (t.type) {

            case OPEN:
                this.recognizeExpression();
                this.match(Token.Type.CLOSE);
                break;
            case MINUS:
                this.recognizeExpression();
                break;
            case IDENTIFIER:
                break;
            case NATNUMBER:
                break;
            default:
                throw new MisMatchException(Token.Type.OPEN, t.type);
        }

        // See if there is a right hand as well (which is + or * followed by another expression)
        switch (this.tokenizer.peek().type) {
            case ADD:
                this.match(Token.Type.ADD);
                this.recognizeExpression();
                break;
            case MULTIPLY:
                this.match(Token.Type.MULTIPLY);
                this.recognizeExpression();
                break;
        }

    }

    /**
     * Recognize the earlier defined InputStream as a valid Pico program.
     *
     * @return {@code true} if the defined InputStream is a valid Pico program, {@code false} otherwise.
     */
    public synchronized void recognize() throws ParseException{
        this.recognizeProgram();
    }


    /**
     * Checks whether the next character is any of the given characters
     *
     * @param expected A set of characters that are expected.
     *                 characters, {@code false} otherwise.
     */
    private void match(Token.Type expected) throws ParseException {
        try {
            Token actual = this.tokenizer.next();
            if (actual.type != expected)
                throw new MisMatchException(expected, actual.type);
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }

    public static class ParseException extends RuntimeException {

        private ParseException(Exception cause) {
            super(cause);
        }

    }


    public class MisMatchException extends RuntimeException {

        private MisMatchException(Token.Type expected, Token.Type received) {
            super("Expected \"" + expected + "\" but received \"" + received + "\"");
        }
    }
}
