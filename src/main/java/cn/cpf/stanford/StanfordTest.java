package cn.cpf.stanford;

import com.github.cpfniliu.common.util.io.PropsUtil;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.util.Triple;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.List;
import java.util.Properties;

/**
 * <b>Description : </b>
 *
 * @author CPF
 * @date 2019/11/12 14:53
 **/
@SuppressWarnings("java:S106")
public class StanfordTest {

    public static void main(String[] args) throws Exception {
        String serializedClassifier = "P:/kmerit/itongye/web_main/dal/edu/stanford/nlp/models/ner/chinese.misc.distsim.crf.ser.gz";
        final Properties properties = PropsUtil.loadProps("stanford-config/props.props");
        chinaTest(serializedClassifier, properties);
    }


    public static void chinaTest(String serializedClassifier, Properties props) throws Exception {

        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);

        String exampleStr = "我 住在 美国,潍坊 农商 袁秀玲,袁秀玲";

        // add
//        List<String> segmented = classifier.segmentString(exampleStr);
//        System.out.println(segmented);

        // This one puts in spaces and newlines between tokens, so just print not println.
        System.out.print(classifier.classifyToString(exampleStr, "slashTags", false));
        System.out.println("---");

        System.out.print(classifier.classifyToString(exampleStr, "tabbedEntities", false));
    }

    public static void englishTest(String[] args) throws Exception {

        String serializedClassifier = "P:\\kmerit\\itongye\\web_main\\dal\\stanford-ner-2018-10-16/classifiers/english.all.3class.distsim.crf.ser.gz";

        if (args.length > 0) {
            serializedClassifier = args[0];
        }

        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);

    /* For either a file to annotate or for the hardcoded text example, this
       demo file shows several ways to process the input, for teaching purposes.
    */

        if (args.length > 1) {

            String fileContents = IOUtils.slurpFile(args[1]);
            List<List<CoreLabel>> out = classifier.classify(fileContents);
            for (List<CoreLabel> sentence : out) {
                for (CoreLabel word : sentence) {
                    System.out.print(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
                }
                System.out.println();
            }

            System.out.println("---");
            out = classifier.classifyFile(args[1]);
            for (List<CoreLabel> sentence : out) {
                for (CoreLabel word : sentence) {
                    System.out.print(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
                }
                System.out.println();
            }

            System.out.println("---");
            List<Triple<String, Integer, Integer>> list = classifier.classifyToCharacterOffsets(fileContents);
            for (Triple<String, Integer, Integer> item : list) {
                System.out.println(item.first() + ": " + fileContents.substring(item.second(), item.third()));
            }
            System.out.println("---");
            System.out.println("Ten best entity labelings");
            DocumentReaderAndWriter<CoreLabel> readerAndWriter = classifier.makePlainTextReaderAndWriter();
            classifier.classifyAndWriteAnswersKBest(args[1], 10, readerAndWriter);

            System.out.println("---");
            System.out.println("Per-token marginalized probabilities");
            classifier.printProbs(args[1], readerAndWriter);

            // -- This code prints out the first order (token pair) clique probabilities.
            // -- But that output is a bit overwhelming, so we leave it commented out by default.
            // System.out.println("---");
            // System.out.println("First Order Clique Probabilities");
            // ((CRFClassifier) classifier).printFirstOrderProbs(args[1], readerAndWriter);

        } else {

      /* For the hard-coded String, it shows how to run it on a single
         sentence, and how to do this and produce several formats, including
         slash tags and an inline XML output format. It also shows the full
         contents of the {@code CoreLabel}s that are constructed by the
         classifier. And it shows getting out the probabilities of different
         assignments and an n-best list of classifications with probabilities.
      */

            String[] example = {"Good afternoon Rajat Raina, how are you today?", "I go to school at Stanford University, which is located in California."};
            for (String str : example) {
                System.out.println(classifier.classifyToString(str));
            }
            System.out.println("---");

            for (String str : example) {
                // This one puts in spaces and newlines between tokens, so just print not println.
                System.out.print(classifier.classifyToString(str, "slashTags", false));
            }
            System.out.println("---");

            for (String str : example) {
                // This one is best for dealing with the output as a TSV (tab-separated column) file.
                // The first column gives entities, the second their classes, and the third the remaining text in a document
                System.out.print(classifier.classifyToString(str, "tabbedEntities", false));
            }
            System.out.println("---");

            for (String str : example) {
                System.out.println(classifier.classifyWithInlineXML(str));
            }
            System.out.println("---");

            for (String str : example) {
                System.out.println(classifier.classifyToString(str, "xml", true));
            }
            System.out.println("---");

            for (String str : example) {
                System.out.print(classifier.classifyToString(str, "tsv", false));
            }
            System.out.println("---");

            // This gets out entities with character offsets
            int j = 0;
            for (String str : example) {
                j++;
                List<Triple<String, Integer, Integer>> triples = classifier.classifyToCharacterOffsets(str);
                for (Triple<String, Integer, Integer> trip : triples) {
                    System.out.printf("%s over character offsets [%d, %d) in sentence %d.%n", trip.first(), trip.second(), trip.third, j);
                }
            }
            System.out.println("---");

            // This prints out all the details of what is stored for each token
            int i = 0;
            for (String str : example) {
                for (List<CoreLabel> lcl : classifier.classify(str)) {
                    for (CoreLabel cl : lcl) {
                        System.out.print(i++ + ": ");
                        System.out.println(cl.toShorterString());
                    }
                }
            }

            System.out.println("---");

        }
    }

}
