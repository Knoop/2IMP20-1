package regex;

/**
 * This file contains the test input strings for exercise 1, 2 and 3
 */
public class TestStrings {

    /**
     * Valid input for the ID lexical definition
     */
    public static final String[] idTestsMatch = {
            "a",
            "abc",
            "abc123"
    };

    /**
     * Invalid input for the ID lexical definition
     */
    public static final String[] idTestsNoMatch= {
            "123",
            "1abc"
    };

    /**
     * Valid input for the NAT lexical definition
     */
    public static final String[] natTestsMatch = {
            "0",
            "1",
            "12",
            "3200"
    };

    /**
     * Invalid input for the NAT lexical definition
     */
    public static final String[] natTestsNoMatch= {
            "01",
            "1.0",
            "-1",
            "0.5"
    };

    /**
     * Valid input for the FLOAT lexical definition
     */
    public static final String[] floatTestsMatch = {
            "1.0",
            "0.1",
            "-1.0",
            "-0.5",
            "4.5",
            "1.004"
    };

    /**
     * Inalid input for the FLOAT lexical definition
     */
    public static final String[] floatTestsNoMatch= {
            "1.0.0",
            "0.10000",
            "2.0000",
            "-00.5",
            "-1.000",
            "0001.01"
    };

    /**
     * Valid input for the lexical definition from exercise 2
     */
    public static final String[] ex2TestsMatch= {
            "\"\"",
            "\"abc\"",
            "\"lorem\"ipsum\"dolor\"sit\"amet\"",
    };

    /**
     * Inalid input for the lexical definition from exercise 2
     */
    public static final String[] ex2TestsNoMatch= {
            "\"",
            "lorem\"ipsum\"",
            "abc",
            "\"\"\""
    };

    /**
     * Valid input for the Java Comment lexical definition
     */
    public static final String[] javaCommentTestsMatch= {
            "//abc",
            "//abc def",
            "/*lorem ipsum dolor sit amet*/",
            "/*lorem ipsum dolor \n\n sit amet*/",
    };

    /**
     * Inalid input for the Java Comment lexical definition
     */
    public static final String[] javaCommentTestsNoMatch= {
            "/abc",
            "/*abc*",
            "/*abc def/",
            "/abc def*/",
            "*abc def */"
    };

    /**
     * Valid input for the Matlab Comment lexical definition
     */
    public static final String[] matlabCommentTestsMatch= {
            "%abc",
            "%abc def",
            "%{lorem ipsum dolor sit amet%}",
            "%{lorem ipsum dolor \n\n sit amet%}",
    };

    /**
     * Invalid input for the Matlab Comment lexical definition
     */
    public static final String[] matlabCommentTestsNoMatch= {
            "abc%}",
            "{abc def%}",
            "{abc def }"
    };
}
