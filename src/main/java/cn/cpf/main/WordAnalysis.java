package cn.cpf.main;


import com.github.cpfniliu.common.util.io.IoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.recognition.StopWord;
import org.apdplat.word.segmentation.Segmentation;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.SegmentationFactory;
import org.apdplat.word.segmentation.Word;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

/**
 * <b>Description : </b>
 *
 * @author CPF
 * @date 2020/12/5 9:36
 **/
@Slf4j
public class WordAnalysis {

    public static final String PATH = "C:\\Users\\Private\\read-now\\fantasyworld\\";

    public static HashMap<String, Integer> map = new HashMap(1000);

    public static void main(String[] args) {
        Segmentation segmentation = SegmentationFactory.getSegmentation(SegmentationAlgorithm.MaxNgramScore);
        File file = new File(PATH);
        try (final FileInputStream inputStream = new FileInputStream(file)){
            IoUtils.readStringFromInputStream(inputStream, Charset.forName("GBK"), line -> {
                log.info("line: {}", line);
                if (StringUtils.isNotBlank(line)) {
                    final List<Word> seg = segmentation.seg(line);
                    StopWord.filterStopWords(seg);
                    seg.forEach(WordAnalysis::addMap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(map);
    }

    private static void addMap(Word s) {
        final String text = s.getText();
        final Integer orDefault = map.getOrDefault(text, 0);
        map.put(text, orDefault + 1);
    }

}
