package regex;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Maurice on 9-5-2016.
 */
public class Test {


    static void testParse(File file){
        try {
            if((new PicoRec(new FileInputStream(file))).recognize())
                System.out.println("Recognized!");
            else
                System.out.println("Didn't recognize");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        testParse(new File("examples/valid1.pico"));
        testParse(new File("examples/invalid1.pico"));
    }
}
