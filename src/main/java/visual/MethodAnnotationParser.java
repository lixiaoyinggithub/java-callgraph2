package visual;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Xavier
 * @date 2024/7/3 14:26
 */
public class MethodAnnotationParser {


    public static void main(String[] args) {
        String path = "/Users/aly/IdeaProjects/buz/buz-dc-core/buz-dc-core-app/target/buz-dc-core-app-1.0.123-SNAPSHOT.jar-output_javacg/method_annotation.txt";

        int total = 0;
        AtomicInteger atomTotal = new AtomicInteger(0);
        /**
         * 每行得到2个实体，一个关系
         */
        Map<Integer, String> printSet = new ConcurrentHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String row;
            while ((row = br.readLine()) != null) {
                total++;
                String line = row;
                String strictLine = line.replace("\t\t", "\t");
                if (filter(strictLine)) {
                    continue;
                }
                String[] split = strictLine.split("\t");
                System.out.println("" + split[3]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean filter(String info) {
        return !info.contains("anno.Cached\tname");
    }

}
