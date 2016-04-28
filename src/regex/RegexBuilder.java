package regex;

import dk.brics.automaton.RegExp;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Maurice on 28-4-2016.
 */
public class RegexBuilder {


    /**
     * Split for definitions
     */
    public static final String DEFINITION_SPLIT = "\\s*\\:\\:\\=\\s*";

    private final HashMap<String, List<String>> regexes = new HashMap<>();
    private final LinkedList<String> defOrder = new LinkedList<>();

    private String string = null;
    private boolean changedSinceBuild;
    private final HashMap<String, String> unfoldedRegexes = new HashMap<>();

    public RegexBuilder(){

    }

    public RegexBuilder add(String identifier, String regex){

        if(this.defOrder.contains(identifier)){
            this.regexes.get(identifier).add(regex);
        }
        else{
            this.defOrder.addLast(identifier);
            List<String> list = new LinkedList<>();
            list.add(regex);
            this.regexes.put(identifier, list);
        }

        this.changedSinceBuild = true;
        return this;
    }

    public RegExp build(){
        return new RegExp(this.toString());
    }

    public synchronized String toString(){

        if(this.changedSinceBuild) {
            // was changed since last update
            if (this.defOrder.size() == 0) {
                this.string = "";
            } else {
                unfold();
                this.string = guard(combine(this.unfoldedRegexes.values()));
                this.changedSinceBuild = false;
            }
        }

        return this.string;

    }

    /**
     * Unfolds all definitions such that they no longer refer to other definitions
     */
    private void unfold(){

        this.unfoldedRegexes.clear();

        // First sweep: expand all versions of a definition into one
        for(String definition : this.defOrder)
            this.unfoldedRegexes.put(definition, combine(this.regexes.get(definition)));

        // Seconds weep: fill in the expanded versions from the first sweep into any definitions referencing it
        for(String definition : this.defOrder){
            boolean ahead = false; // indicates whether we're looking ahead in the file
            String unfoldSecond = this.unfoldedRegexes.get(definition);
            for(String definition2 : this.defOrder) {
                // We are looking "ahead" as soon as we encounter ourselves
                if (definition2.equals(definition)) ahead = true;

                /*
                 * The first definitions refers to the second.
                 * This is only allowed if we're not looking ahead.
                 * If we are looking ahead, then we throw an exception
                 */
                if(unfoldSecond.contains(definition2)){
                    if(ahead) throw new InvalidReferenceException(definition, definition2);
                    else unfoldSecond = unfoldSecond.replace(definition2, this.unfoldedRegexes.get(definition2));
                }
            }
            this.unfoldedRegexes.put(definition, unfoldSecond);
        }
    }

    /**
     * Appends the given regex as a single item. This means that if the regex is only one character it is appended as is, otherwise it is guarded with brackets.
     * @param regex The regex to guard.
     */
    public static String guard(String regex){
        if(regex.length() == 1) return regex;
        else if(regex.length() > 1) return '('+regex+')';
        else return "";
    }

    /**
     * Combines the given list of regexes to make one large regex.
     * For instance, it combines {@code a}, {@code bb} and {@code c} to {@code (a|(bb)|c)}.
     * @param regexes
     */
    public static String combine(Collection<String> regexes){

        // Nothing to write, write nothing
        if(regexes.size() == 0)
            return "";

        // Iterator to iterate over all regexes
        Iterator<String> iterator = regexes.iterator();

        // Append at least the first item, and then all other items as an additional option
        StringBuilder builder = new StringBuilder().append(guard(sanitize(iterator.next())));
        while(iterator.hasNext())
            builder.append('|').append(guard(sanitize(iterator.next())));

        // Add brackets if required
        return guard(builder.toString());

    }

    /**
     * Combines the given list of regexes to make one large regex.
     * For instance, it combines {@code a}, {@code bb} and {@code c} to {@code (a|(bb)|c)}.
     * @param regexes
     */
    public static String combine(String... regexes){
        return combine(Arrays.asList(regexes));
    }

    /**
     * Applies all sanitation rules to the given string and returns the result
     * @param input The string to sanitize
     * @return The string to which all sanitation rules as defined in {@code RegexBuilder.sanitationRules}
     */
    public static String sanitize(String input){
        String converted = input;
        for(Map.Entry<String, String> sanitationRule : sanitationRules.entrySet())
            converted = converted.replace(sanitationRule.getKey(), sanitationRule.getValue());
        return converted;
    }


    private static final HashMap<String, String> sanitationRules = new HashMap<>();

    /*
     * Allows you to define additional sanitation rules by putting the key (what to replace) and the value
     * (what to replace by) to the mapping of rules.
     */
    static {
        //sanitationRules.put("\\", "\\\\");
        //sanitationRules.put("\"", "\\\"");
    }

    public static RegExp fromFile(File file){




        return null;

    }

    private class InvalidReferenceException extends RuntimeException {
        public InvalidReferenceException(String definition, String definition2) {
            super("Line with identifier "+definition+" refers to "+definition2+" which is not allowed due to the second identifier occurring as the first or thereafter.");
        }
    }
}
