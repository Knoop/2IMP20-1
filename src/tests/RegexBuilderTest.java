package tests;

import regex.RegexBuilder;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Maurice on 28-4-2016.
 */
public class RegexBuilderTest {

    @Test
    public void testOutput(){
        RegexBuilder regexBuilder = new RegexBuilder();
        regexBuilder.add("A","\\s");
        regexBuilder.add("B","AA");
        assertEqual(regexBuilder.toString(),"(\\s|(\\s\\s))");
    }

    // \s -> \\s
    @Test
    public void testRegexBuilderCombine(){
        assertEqual(RegexBuilder.combine("a","bb","c"), "(a|(bb)|c)");
    }

    @Test
    public void testRegexBuilderGuard(){
        assertEqual(RegexBuilder.guard("a"),"a");
        assertEqual(RegexBuilder.guard("aa"),"(aa)");
    }


    public static void main(String args[]){

        RegexBuilderTest testInstance = new RegexBuilderTest();
        for(Method method : RegexBuilderTest.class.getMethods()){
            if(method.getAnnotation(Test.class) != null)
                try {
                    try {
                        method.setAccessible(true);
                        method.invoke(testInstance);
                        System.out.println("Test " + method.getName() + " succeeded");
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                }catch(Throwable t) {
                    System.out.println("Test " + method.getName() + " failed (" + t.getMessage() + ")");
                }
        }
    }


    @Documented
    @Inherited
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface Test{ }

    private static void fail(){
        throw new AssertionError();
    }

    private static void assertEqual(Object given, Object expected){
        if(!given.equals(expected))
            throw new AssertEqualsException(given, expected);
    }


    private static class AssertEqualsException extends RuntimeException{
        private AssertEqualsException(Object given, Object expected){
            super("Expected "+expected+" was given "+given);
        }
    }

}
