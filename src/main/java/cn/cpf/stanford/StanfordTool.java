package cn.cpf.stanford;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <b>Description : </b>
 *
 * @author CPF
 * @date 2020/12/5 9:13
 **/
public class StanfordTool {

    public static Set<String> getCounterpartSet(String path) throws IOException {
        final File file = new File(path);
        Set<String> set;
        try (final FileInputStream inStream = new FileInputStream(file)) {
            final long length = file.length();
            byte[] bytes = new byte[(int) length];
            inStream.read(bytes);
            final String content = new String(bytes, StandardCharsets.UTF_8);
            set = new HashSet<>(Arrays.asList(content.split("\r\n")));
        }
        return set;
    }

}
