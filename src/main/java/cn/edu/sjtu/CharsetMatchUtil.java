package cn.edu.sjtu;

import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import java.io.*;
import java.nio.charset.CharsetDecoder;

/**
 * Created by spring on 2017/4/12.
 */
public class CharsetMatchUtil {



    public static String getCharset(InputStream data) throws IOException {
        CharsetDetector detector = new CharsetDetector();
        detector.setText(data);
        CharsetMatch match = detector.detect();
        String encoding = match.getName();
        //System.out.println(encoding);
        return encoding;
    }

    public static void main(String args[]) throws IOException {
        String filename = "/Users/spring/Downloads/0223-212.txt";
        File file = new File(filename);
        InputStream data = new BufferedInputStream(new FileInputStream(file));
        String encoding = getCharset(data);
        System.out.println(encoding);
    }
}
