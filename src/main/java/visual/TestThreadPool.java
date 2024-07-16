package visual;

import visual.chart.ChartAble;
import visual.chart.FlowChartImpl;
import visual.init.MethodCallCached;
import visual.node.Node;
import visual.utils.FileTools;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class TestThreadPool {

    //class_annotation.txt
    public static final String rootPath = "/Users/aly/IdeaProjects/buz/buz-dc-core/buz-dc-core-app/target/buz-dc-core-app-1.0.123-SNAPSHOT.jar-output_javacg/";
    public static final String outputPath1 = "/Users/aly/Documents/call_graph1.txt";
    public static final String outputPath = "/Users/aly/Documents/call_graph.txt";


    static ExecutorService es = new ThreadPoolExecutor(20, 20, 5,
            TimeUnit.MINUTES, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());


    public static void main(String... args) throws Exception {
        List<Integer> taskList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            taskList.add(i);
        }

        for (Integer integer : taskList) {
            es.submit(() -> {
                try {
                    System.out.println("id:" + integer);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        es.shutdown();
        System.out.println("shutdown");
        es.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("wait success");
    }

    private static Set<String> mock() {
        Set<String> methods = new HashSet<>();
        methods.add("com.buz.dc.core.service.user.UserServiceImpl:getUser(long)");
        methods.add("com.buz.dc.core.service.voicemoji.VoicemojiAdminServiceImpl:notifyVoicemojiCategoryChange()");
        return null;
    }

}