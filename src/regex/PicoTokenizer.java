package regex;

import dk.brics.automaton.AutomatonMatcher;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;

import java.io.IOException;
import java.io.InputStream;

/**
 * Tokenizer for the Pico language.
 */
public class PicoTokenizer {

    /**
     * Regular expression that matches all tokens of the Pico language
     */
    private static final String REGEX = "(begin)|(end)|(declare)|,|\\||;|(:=)|\\(|\\)|\\-|\\+|\\*|([a-z][a-z0-9]*)|([1-9][0-9]*)|0";
    private final AutomatonMatcher matcher;
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

    public PicoTokenizer(String inspectedString) {
        // Create a matcher on the given string
        this.matcher = new RunAutomaton(new RegExp(REGEX).toAutomaton()).newMatcher(inspectedString);

        // Shift twice to obtain a token for both the current value and the next value.
        this.shiftTokens();
        this.shiftTokens();
    }

    public PicoTokenizer(InputStream inputStream) {
        this(readContent(inputStream));
    }

    /**
     * Read the contents of a stream into a String.
     *
     * @param stream The stream from which to read the content
     * @return
     */
    private static String readContent(InputStream stream) {
        StringBuilder builder = new StringBuilder();

        try {
            for (int c = stream.read(); c != -1; c = stream.read())
                builder.append((char) c);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    /**
     * Obtain the next token.
     *
     * @return The next Token
     * @throws NoNextTokenException If there are no more tokens to read.
     */
    public Token next() throws NoNextTokenException {

        Token token = this.tokenizeCurrent();
        this.shiftTokens();
        return token;
    }

    /**
     * Peeks at the value that will be returned by next. The next call to next will yield a Token that is equal to the
     * token returned by this method. The result returned by {@code peek} will not change between two calls to {@code
     * next}.
     *
     * @return The token that lies one ahead of the current token.
     */
    public Token peek() throws NoNextTokenException {
        return this.tokenizeCurrent();
    }

    /**
     * Tokenizes the current value. If the current value is already tokenized (i.e. {@code typeOfCurrent} is not null)
     * then a new token is created using the stored type of the current value. If the value was null then the value for
     * {@code typeOfCurrent} is determined, after which it is returned as a token.
     * <p>
     * <b> Note that for each call a new Token is constructed. As such it is not possible to use {@code ==} to
     * determine
     * equality. Use {@code equals} instead.</b>
     *
     * @return The created Token.
     */
    private Token tokenizeCurrent() {
        if (this.current == null)
            throw new NoNextTokenException();

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
     * Read the next token.
     *
     * @return The next token or null if there is no such token.
     */
    private String readNextToken() {
        return this.matcher.find() ? this.matcher.group() : null;
    }

    /**
     * Determine the {@code Token.Type} of the current string token. This checks whether the current token string is
     * equal to the representation of any of the {@code Token.Type} elements. The elements are inspected in the order
     * they are defined in {@code Token.Type}.
     *
     * @return The determined {@code Token.Type}.
     * @throws NoMatchingTokenException If no {@code Token.Type} element exists that matches the current token string.
     */
    private Token.Type determineTypeOfCurrent() {
        for (Token.Type type : Token.Type.values())
            if (type.represents(this.current))
                return type;

        throw new NoMatchingTokenException(this.current);
    }

    /**
     * A Token is a string that is to be interpreted in a certain way. The meaning of the string is contained by the
     * {@code type} attribute. The string that is to be interpreted is contained by {@code value}. Note that for
     * keywords the contained value is equal to the representation of the type. For non keywords the string matches the
     * regular expression as defined by the regex representation of its type.
     */
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
            this(type, type.representation);
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
         * to the representation attribute of its {@code Token.Type}. Otherwise no such guarantees are given.
         *
         * @return Whether this {@code Token} represents a keyword.
         */
        public boolean isKeyword() {
            return this.type.isKeyword();
        }

        /**
         * Type indicating as what kind of token a Token should be interpreted. The types can represent keywords and
         * non-keywords. For keywords, the token must be equal to the representation of the type. For non-keywords, the
         * representation is a regular expression that matches on strings that can be interpreted as the type of token.
         */
        public enum Type {
            // All keywords
            BEGIN("begin"), END("end"), DECLARE("declare"), DECLARATION_END(","), DECLARATIONS_END("|"),
            STATEMENT_END(";"), ASSIGN(":="), OPEN("("), CLOSE(")"), MINUS("-"), ADD("+"), MULTIPLY("*"),
            // All non-keywords
            IDENTIFIER("[a-z][a-z0-9]*", false), NATNUMBER("0|([1-9][0-9]*)", false);

            /**
             * The representation of a type. The representation should be interpreted differently based on whether the
             * type is a keyword or not.
             * <p>
             * If it is a keyword, then the representation should be interpreted as is. For a string to be considered a
             * token of this type, it must be exactly equal to the value of representation.
             * </p>
             * <p>
             * If it isn't a keyword, then the representation should be interpreted as a regular expression. For a
             * string to be considered a
             * token of this type, it must match the regular expression that is the representation.
             * </p>
             */
            private String representation;
            private boolean isKeyword;

            /**
             * Keyword constructor. This allows for a keyword to be set, meaning that this type is indicated by the
             * given
             * keyword. Types without a keyword are interpreted as variables and must be paired with a value.
             *
             * @param representation The representating string of the keyword.
             */
            Type(String representation, boolean isKeyword) {
                this.representation = representation;
                this.isKeyword = isKeyword;
            }

            /**
             * Variable constructor. This is used to indicate a type that should be paired with a value.
             */
            Type(String representation) {
                this(representation, true);
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
                return this.isKeyword;
            }

            /**
             * Indicates whether the given string is represented by this {@code Token.Type}. This will account what the
             * value of the representation attribute actually represents.
             * <p>
             * Following the definition of representation, if this is a keyword, then the representation is interpreted
             * as is. For the given string to be considered a
             * token of this type, it must be exactly equal to the value of representation.
             * </p>
             * <p>
             * If this isn't a keyword, then the representation is interpreted as a regular expression. For the given
             * string to be considered a
             * token of this type, it must match the regular expression that is the representation.
             * </p>
             *
             * @param string The string for which to check whether it is represented by this {@code Token.Type}.
             * @return A boolean following the rules as indicated above.
             */
            public boolean represents(String string) {
                return this.isKeyword ? representation.equals(string) : string.matches(representation);
            }
        }
    }

    /**
     * Exception that indicates that there are no more tokens to read.
     */
    public static class NoNextTokenException extends RuntimeException {
    }

    /**
     * Exception that indicates that the value that was read can not be converted to a token
     */
    public static class NoMatchingTokenException extends RuntimeException {
        private NoMatchingTokenException(String value) {
            super("There is no token that matches the value \"" + value + "\"");
        }
    }
}
