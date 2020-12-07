package cn.cpf.main;


import cn.cpf.stanford.SegDemo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * <b>Description : </b>
 *
 * @author CPF
 * @date 2020/12/5 9:36
 **/
@Slf4j
public class NovelAnalysis {

    public static final String PATH = "C:\\Users\\Private\\read-now\\fantasyworld\\最终幻想-现代\\学园都市\\邪书.md";

    public static HashMap<String, Integer> map = new HashMap(1000);

    public static void main(String[] args) {

        File file = new File(PATH);

        try (final FileInputStream inputStream = new FileInputStream(file)){
            readStringFromInputStream(inputStream, Charset.forName("GBK"), line -> {
                log.info("line: {}", line);
                if (StringUtils.isNotBlank(line)) {
                    final List<String> strings = SegDemo.getSegmenterCRFClassifier().segmentString(line);
                    log.info("seg ==> {}", strings);
                    strings.forEach(NovelAnalysis::addMap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(map);
    }

    private static void addMap(String s) {
        final Integer orDefault = map.getOrDefault(s, 0);
        map.put(s, orDefault + 1);
    }


    /**
     * 输入流转String
     *
     * @param inputStream 输入流
     * @param charset 编码
     * @return 转换的字符串
     */
    public static void readStringFromInputStream(InputStream inputStream, Charset charset, Consumer<String> consumer) throws IOException {
        try (InputStreamReader inputStreamReader = charset == null ?
                new InputStreamReader(inputStream) : new InputStreamReader(inputStream, charset)) {
            //字符缓冲流
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String len;
            //按行读
            while((len=bufferedReader.readLine()) != null){
                //追加到字符串缓冲区存放
                consumer.accept(len);
            }
        }
    }
}
