package cn.edu.sjtu;

import org.apache.tika.Tika;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by spring on 2017/4/12.
 */
public class TikaUtilTest {

    public static void main(String[] args) {
        String filename= "/Users/spring/Downloads/0223-212.txt";
        File file = new File(filename);
        InputStream input = null;
        try {
            // BufferedInputStream
            input = new BufferedInputStream(new FileInputStream(file));
            System.out.println(input);
            Map<String, String> map = TikaUtil.parseFile(filename, input);
            System.out.println(map);

        } catch(Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if (input != null) input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
