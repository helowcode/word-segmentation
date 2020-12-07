package ats.spring.test.utils;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <b>Description : </b>
 *
 * @author CPF
 * @date 2019/11/13 17:13
 **/
public class CutWorkUtils {

    public static final String regex = "[`_●~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）\\-—+|{}【】‘；：”“’。，、？]";


    public static Map<String, String> getMappingMap() throws IOException {
        final File file = new File("P:\\kmerit\\itongye\\web_main\\dal\\stanford-ner-2018-10-16\\dict\\mapping");
        final FileInputStream inStream = new FileInputStream(file);
        final long length = file.length();
        byte[] bytes = new byte[(int) length];
        inStream.read(bytes);
        Map<String, String> map = new HashMap<>();
        for (String s : new String(bytes, StandardCharsets.UTF_8).split("\r\n")) {
            final String[] split = s.split("[=,]");
            if (split.length > 0) {
                String val = split[0];
                // 从0开始, 包括其自身也作为key
                for (int i = 0, len = split.length; i<len; i++) {
                    map.put(split[i], val);
                }
            }
        }
        return map;
    }


    @Test
    public static Set<String> getAreaMap() throws IOException {
        final File file = new File("P:\\kmerit\\itongye\\web_main\\dal\\stanford-ner-2018-10-16\\dict\\china_area.txt");
        final FileInputStream inStream = new FileInputStream(file);
        final long length = file.length();
        byte[] bytes = new byte[(int) length];
        inStream.read(bytes);
        Set<String> set = new HashSet<>();
        for (String s : new String(bytes, StandardCharsets.UTF_8).split("\r\n")) {
            set.add(s);
        }
        return set;
    }

    @Test
    public static Set<String> getCounterpartSet() throws IOException {
        final File file = new File("P:\\kmerit\\itongye\\web_main\\dal\\stanford-ner-2018-10-16\\dict\\counterpart_key.txt");
        final FileInputStream inStream = new FileInputStream(file);
        final long length = file.length();
        byte[] bytes = new byte[(int) length];
        inStream.read(bytes);
        Set<String> set = new HashSet<>();
        for (String s : new String(bytes, StandardCharsets.UTF_8).split("\r\n")) {
            set.add(s);
        }
        return set;
    }

}
