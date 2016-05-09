package regex;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Tokenizer for the Pico language.
 */
public class PicoTokenizer {

    private final Reader reader;
    private int columnCounter = 1, lineCounter = 1;
    /**
     * The currently read token value. It may depend on the value of the next read token what the type of this token
     * is.
     */
    private String current;
    /**
     * The next read token value. It may not yet be known what type of token this is, as there is no look ahead for it.
     * .
     */
    private String next;

    /**
     * The Token type of the current
     */
    private Token.Type typeOfCurrent;

    public PicoTokenizer(Reader reader) {
        this.reader = reader;
        // Shift twice to obtain a token for the current and next values.
        this.shiftTokens();
        this.shiftTokens();

    }

    public PicoTokenizer(InputStream inputStream) {
        this(new InputStreamReader(inputStream));
    }

    /**
     * Obtain the next token.
     *
     * @return The next Token
     * @throws NoSuchTokenException If there are no more tokens to read.
     */
    public Token next() throws NoSuchTokenException {

        Token token = this.tokenizeCurrent();
        this.shiftTokens();
        return token;
    }

    /**
     * Peeks at the value that will be returned by next. The next call to next will yield a Token that is equal to the
     * token returned by this method. The result returned by {@code peek} will not change between two calls to {@code
     * next}.
     *
     * @return
     */
    public Token peek() {
        return this.tokenizeCurrent();
    }

    /**
     * Tokenizes the current values.
     *
     * @return The created Token.
     */
    private Token tokenizeCurrent() {
        if (this.current == null)
            throw new NoSuchTokenException();

        if (this.typeOfCurrent == null)
            this.typeOfCurrent = this.determineTypeOfCurrent();

        return new Token(this.typeOfCurrent, this.current);
    }

    /**
     * Shifts the current and next tokens such that the current token becomes the old next token, while the next token
     * becomes a new value read from the {@code InputStream}.
     */
    private void shiftTokens() {
        this.current = this.next;
        this.next = this.readNextToken();
        // Reset the current type
        this.typeOfCurrent = null;
    }

    /**
     * Reads the next token from the {@code InputStream}.
     *
     * @return The next token.
     */
    private String readNextToken() {
        return null;
    }

    public int columnCounter() {
        return this.columnCounter;
    }

    public int lineCounter() {
        return this.lineCounter;
    }

    private Token.Type determineTypeOfCurrent() {
        return null;
    }

    public static class Token {

        /**
         * The string value paired with the token type.
         */
        public final String value;
        /**
         * The type of this token.
         */
        public final Type type;

        private Token(Type type) {
            this(type, type.delimiter);
        }

        private Token(Type type, String value) {
            if (type == null || value == null)
                throw new IllegalArgumentException("Type and Value can not be null");

            this.type = type;
            this.value = value;
        }

        @Override
        // Automatically generated equality check based on the values of value and type.
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Token token = (Token) o;

            return value.equals(token.value) && type == token.type;

        }

        @Override
        // Automatically generated hashcode calculation based on the values of value and type.
        public int hashCode() {
            int result = value.hashCode();
            result = 31 * result + type.hashCode();
            return result;
        }

        /**
         * Indicates whether this {@code Token} represents a keyword. This is determined by checking whether the {@code
         * Token.Type} of this
         * {@code Token} is a keyword. If it is a keyword, then the value attribute of this {@code Token} will be equal
         * to the delimiter attribute of its {@code Token.Type}. Otherwise no such guarantees are given.
         *
         * @return Whether this {@code Token} represents a keyword.
         */
        public boolean isKeyword() {
            return this.type.isKeyword();
        }

        public enum Type {
            BEGIN("begin"), END("end"), DECLARE("declare"), DECLARATION_END(","), DECLARATIONS_END("|"), STATEMENT_END(";"), ASSIGN(":="), OPEN("("), CLOSE(")"), MINUS("-"), ADD("+"), MULTIPLY("*"), IDENTIFIER, NATNUMBER;

            private String delimiter;

            /**
             * Keyword constructor. This allows for a keyword to be set, meaning that this type is indicated by the
             * given
             * keyword. Types without a keyword are interpreted as variables and must be paired with a value.
             *
             * @param delimiter The delimiter, which is the string representation of the keyword.
             */
            Type(String delimiter) {
                this.delimiter = delimiter;
            }

            /**
             * Variable constructor. This is used to indicate a type that should be paired with a value.
             */
            Type() {
                this(null);
            }

            /**
             * Indicates whether this type represents a keyword. Keywords
             * The existing keywords are shown below, in alphabetical order:
             * <ul>
             * <li>ADD <i>"+"</i></li>
             * <li>ASSIGN <i>":="</i></li>
             * <li>BEGIN <i>"begin"</i></li>
             * <li>CLOSE <i>")"</i></li>
             * <li>DECLARATIONS_END <i>"|"</i></li>
             * <li>DECLARE <i>"declare"</i></li>*
             * <li>END <i>"end"</i></li>
             * <li>MINUS <i>"-"</i></li>
             * <li>MULTIPLY <i>"*"</i></li>
             * <li>OPEN <i>"("</i></li>
             * <li>STATEMENT_END <i>";"</i></li>
             * </ul>
             *
             * @return {@code true} if the type is one of the keywords as indicated above, {@code false} otherwise.
             */
            public boolean isKeyword() {
                switch (this) {
                    case IDENTIFIER:
                    case NATNUMBER:
                        return false;
                    default:
                        return true;
                }
            }
        }
    }

    /**
     * Exception that indicates that there are no more tokens to read.
     */
    public static class NoSuchTokenException extends RuntimeException {
    }
}
