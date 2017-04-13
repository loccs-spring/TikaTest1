package cn.edu.sjtu;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ParserDecorator;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spring on 2017/4/12.
 */
public class TikaUtil {


    public static Map<String, String> parseFile(InputStream stream, String filename, String filelength) throws Exception {
        RecursiveMetadataParser parser = new RecursiveMetadataParser(new AutoDetectParser());
        ParseContext context = new ParseContext();
        context.set(Parser.class, parser);

        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();

        try {
            parser.parse(stream, handler, metadata, context);
            Map<String, String> map = new HashMap<String, String>();
            map.put(Metadata.RESOURCE_NAME_KEY, filename);
            map.put(Metadata.CONTENT_LENGTH, filelength);

            for(String name: metadata.names()) {
                map.put(name, metadata.get(name));
            }

            map.put("content", parser.getContent());
            return map;
        } finally {
            stream.close();
        }

    }

    private static class RecursiveMetadataParser extends ParserDecorator {
        private String content;
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }

        public RecursiveMetadataParser(Parser parser) {
            super(parser);
        }

        @Override
        public void parse(InputStream stream, ContentHandler ignore, Metadata metadata, ParseContext context) throws IOException, SAXException, TikaException {
            ContentHandler contentHandler = new BodyContentHandler(-1);
            super.parse(stream, contentHandler, metadata, context);
            content = contentHandler.toString();

        }
    }

    /**
     *
     * @param fileName
     * @param input
     * @return
     */
    public static Map<String, String> parseFile(String fileName, InputStream input) {
        Parser parser = new AutoDetectParser();
        try {
            Metadata metadata = new Metadata();
            metadata.set(Metadata.CONTENT_ENCODING, CharsetMatchUtil.getCharset(input));
            metadata.set(Metadata.RESOURCE_NAME_KEY, fileName);

            ContentHandler handler = new BodyContentHandler(-1);
            MediaType type = new DefaultDetector().detect(input, metadata);
            ParseContext context = new ParseContext();
            context.set(Parser.class, parser);
            parser.parse(input, handler, metadata, context);

            Map map = new HashMap<String, String>();

            for(String name: metadata.names()) {
                map.put(name, metadata.get(name));
            }

            map.put("content", handler.toString());
            return map;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String parseFile(File file) {
        Parser parser = new AutoDetectParser();
        InputStream input = null;
        try {
            Metadata metadata = new Metadata();
            metadata.set(Metadata.CONTENT_ENCODING, "utf-8");
            metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
            input = new FileInputStream(file);
            ContentHandler contentHandler = null;

            if (file.length() > 100000) {
                contentHandler = new BodyContentHandler(-1);
            } else {
                contentHandler = new BodyContentHandler();
            }

            ParseContext context = new ParseContext();
            context.set(Parser.class, parser);
            parser.parse(input, contentHandler, metadata, context);

            System.out.println(metadata);
            return contentHandler.toString();

        } catch(Exception e) {

        } finally {
            try {
                if (input != null) input.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
