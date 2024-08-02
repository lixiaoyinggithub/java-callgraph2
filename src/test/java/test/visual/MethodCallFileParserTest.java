package test.visual;

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
public class MethodCallFileParserTest {

    static ExecutorService es = new ThreadPoolExecutor(80, 80, 5,
            TimeUnit.MINUTES, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 解析method_call，写入数据库
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) {
        String path = "/Users/aly/Downloads/method_call.txt";


        int total = 0;
        AtomicInteger atomTotal = new AtomicInteger(0);
        /**
         * 每行得到2个实体，一个关系
         */
        Map<Integer,String> printSet = new ConcurrentHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String row;
            while ((row = br.readLine()) != null) {
                total++;
                String line = row;
                es.submit(() -> {
                    String strictLine = line.replace("\t\t","\t");
                    if (filter(strictLine)) {
                        return;
                    }
                    String[] parts = strictLine.split("\t");
                    if(parts.length == 10 && !printSet.containsKey(10)){
                        printSet.put(10,strictLine);
                        System.out.println("10-part");
                        System.out.println(strictLine);
                        System.out.println(parts[parts.length-3]);
                    }
                    if(parts.length == 9 && !printSet.containsKey(9)){
                        printSet.put(9,strictLine);
                        System.out.println("9-part");
                        System.out.println(strictLine);
                        System.out.println(parts[parts.length-3]);
                    }
                    if(parts.length == 8 && !printSet.containsKey(8)){
                        System.out.println("8-part");
                        printSet.put(8,strictLine);
                        System.out.println(strictLine);
                        System.out.println(parts[parts.length-3]);
                    }
                });
            }

            System.out.println("Start sleep,total=" + total + ",realTotal=" + atomTotal.get());
            Thread.sleep(1000 * 20);
            es.shutdown();
            System.out.println("End of sleep,total=" + total + ",realTotal=" + atomTotal.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean filter(String info) {
        return info.contains(".test.");
    }

}
