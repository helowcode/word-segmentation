package cn.cpf.stanford;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;


/**
 * This is a very simple demo of calling the Chinese Word Segmenter
 * programmatically.  It assumes an input file in UTF8.
 * <p/>
 * <code>
 * Usage: java -mx1g -cp seg.jar SegDemo fileName
 * </code>
 * This will run correctly in the distribution home directory.  To
 * run in general, the properties for where to find dictionaries or
 * normalizations have to be set.
 *
 * @author Christopher Manning
 */
@Slf4j
public class SegDemo {

    public static final String BASE_PATH = "P:\\git\\novel-analysis\\";

    public static final String PROPS = BASE_PATH + "stanford-ner-2018-10-16\\dict\\props.props";

    private static class SegmentClassifierInner{
        private static CRFClassifier<CoreLabel> segmentClassifier = null;
        static {
            try {
                Properties props = new Properties();
                final URL resource = SegDemo.class.getClassLoader().getResource("stanford-config/props.props");
                if (resource == null) {
                    throw new FileNotFoundException("未发现配置文件: classpath:stanford-config/props.props");
                }
                props.load(new FileInputStream(new File(resource.getFile())));
                // 加载分词
                segmentClassifier = CRFClassifier.getClassifier( BASE_PATH + "edu\\stanford\\nlp\\models\\segmenter\\chinese\\ctb.gz", props);
            } catch (IOException | ClassNotFoundException e) {
                log.error("", e);
            }
        }
    }

    private static class PartOfSpeechClassifierInner{
        private static CRFClassifier<CoreLabel> partOfSpeechClassifier = null;
        static {
            try {
                partOfSpeechClassifier = CRFClassifier.getClassifier(BASE_PATH + "edu/stanford/nlp/models/ner/chinese.misc.distsim.crf.ser.gz");
            } catch (IOException | ClassNotFoundException e) {
                log.error("", e);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        for (String filename : args) {
            getSegmenterCRFClassifier().classifyAndWriteAnswers(filename);
        }
        String sample = "我住在美国。潍坊农商袁秀玲,潍坊农商袁秀玲,潍坊农商袁秀玲";
        List<String> segmented = getSegmenterCRFClassifier().segmentString(sample);
        System.out.println(segmented);

        segmented.stream().map(getDistsimCRFClassifier()::classifyToString).forEach(it->{
            if (isPeople(it)) {
                System.out.println("hello " + it);
            }
            System.out.println(getDistsimCRFClassifier().apply(it));
        });

    }


    public static CRFClassifier<CoreLabel> getSegmenterCRFClassifier() {
        return SegmentClassifierInner.segmentClassifier;
    }

    public static CRFClassifier<CoreLabel> getDistsimCRFClassifier() {
        return PartOfSpeechClassifierInner.partOfSpeechClassifier;
    }

    public static boolean isPeople(String txt){
        return getDistsimCRFClassifier().classifyToString(txt).endsWith("/PERSON");
    }

}
