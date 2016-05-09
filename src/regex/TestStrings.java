package regex;


import java.util.HashMap;

public class TestStrings {

    public static final String[] idTestsMatch = {
            "a",
            "abc",
            "abc123"
    };

    public static final String[] idTestsNoMatch= {
            "123",
            "1abc"
    };

    public static final String[] natTestsMatch = {
            "0",
            "1",
            "12",
            "3200"
    };

    public static final String[] natTestsNoMatch= {
            "01",
            "1.0",
            "-1",
            "0.5"
    };

    public static final String[] floatTestsMatch = {
            "1.0",
            "0.1",
            "-1.0",
            "-0.5",
            "4.5",
            "1.004"
    };

    public static final String[] floatTestsNoMatch= {
            "1.0.0",
            "0.10000",
            "2.0000",
            "-00.5",
            "-1.000",
            "0001.01"
    };

    public static final String[] ex2TestsMatch= {
            "\"\"",
            "\"abc\"",
            "\"lorem\"ipsum\"dolor\"sit\"amet\"",
    };

    public static final String[] ex2TestsNoMatch= {
            "\"",
            "lorem\"ipsum\"",
            "abc",
            "\"\"\""
    };

    public static final String[] javaCommentTestsMatch= {
            "//abc",
            "//abc def",
            "/*lorem ipsum dolor sit amet*/",
            "/*lorem ipsum dolor \n\n sit amet*/",
    };

    public static final String[] javaCommentTestsNoMatch= {
            "/abc",
            "/*abc*",
            "/*abc def/",
            "/abc def*/",
            "*abc def */"
    };

    public static final String[] matlabCommentTestsMatch= {
            "%abc",
            "%abc def",
            "%{lorem ipsum dolor sit amet%}",
            "%{lorem ipsum dolor \n\n sit amet%}",
    };

    public static final String[] matlabCommentTestsNoMatch= {
            "abc%}",
            "{abc def%}",
            "{abc def }"
    };
}
