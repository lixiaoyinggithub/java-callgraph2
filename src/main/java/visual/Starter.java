package visual;

import cn.hutool.json.JSONUtil;
import visual.chart.ChartAble;
import visual.chart.FlowChartImpl;
import visual.init.MethodCallCached;
import visual.init.MethodCallImport;
import visual.node.Node;
import visual.utils.FileTools;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class Starter {

    //class_annotation.txt
    public static final String rootPath = "/Users/aly/IdeaProjects/buz/buz-dc-core/buz-dc-core-app/target/buz-dc-core-app-1.0.123-SNAPSHOT.jar-output_javacg/";
    public static final String outputPath = "/Users/aly/Documents/rpc_graph/";


    public static void main(String... args) throws Exception {
//        # Wait 60 seconds before connecting using these details, or login to https://console.neo4j.io to validate the Aura Instance is available
//        NEO4J_URI=neo4j+s://418f83db.databases.neo4j.io
//        NEO4J_USERNAME=neo4j
//        NEO4J_PASSWORD=rGFLArEh_LOrG1JaRFSHW06D85bdm-ShufV7tocRvU0
//        AURA_INSTANCEID=418f83db
//        AURA_INSTANCENAME=Instance01

//        neo4jManager = new Neo4jCommonTools("bolt://localhost:7687", "neo4j", "neo4j");

        Set<String> methods = MethodCallCached.listRoot();

        List<String> extraFilter = new ArrayList<>();
//        extraFilter.add("manager.abtest.UserGrayWrapManager:getGrayGroupWithDeviceInfo");
        extraFilter.add("config.I18nConfigAdapter:getLanguage");

        // todo 线程池的标记
        // todo 异步与非异步的统计
//        Set<String> methods = mock();
        System.out.println("init methods:" + methods.size());
        start(methods, extraFilter);
    }

    private static Set<String> mock() {
        Set<String> methods = new HashSet<>();
        methods.add("com.buz.dc.core.service.activity.ChallengeServiceImpl:pushOnline(java.lang.Long)");
//        methods.add("com.buz.dc.core.service.common.CommonServiceImpl:clientHeartbeatBatch(java.util.List)");
//        methods.add("com.buz.dc.core.manager.group.GroupPortraitManager:uploadGroupImgToRome(java.util.List,cn.hutool.core.date.StopWatch)");
        return methods;
    }

    public static void start(Set<String> methods, List<String> extraFilterList) throws InterruptedException {

        Neo4jManager neo4jManager = new Neo4jManager("neo4j+s://418f83db.databases.neo4j.io", "neo4j", "rGFLArEh_LOrG1JaRFSHW06D85bdm-ShufV7tocRvU0");

        Map<String, String> bodyMap = new HashMap<>();

        AtomicInteger count = new AtomicInteger();

        MethodCallImport.loadMethodCall("method_call.txt");

        methods.stream().forEach(method -> {

            count.incrementAndGet();
            ChartAble flowChartImpl = new FlowChartImpl(extraFilterList);
            Node root = new Node();
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", method);
            root.setName(method);
            root.setChildren(new ArrayList<>());
            root.setData(data);
            neo4jManager.traverByMap(root);

            String body = flowChartImpl.draw(root);
            System.out.println(method + ",body:" + body.length());
            bodyMap.put(method, body);

            System.out.println("count:" + count.get());
            FileTools.writeToFile(body, outputPath + FileTools.createFileName(method));
        });

        for (Map.Entry<Integer, List<String>> entry : FlowChartImpl.getCachedRankMap().entrySet()) {
            FileTools.writeToFile(entry.getKey()+"", outputPath + "/1-rank.md");
            FileTools.writeToFile(JSONUtil.toJsonStr(entry.getValue()), outputPath + "/1-rank.md");
            FileTools.writeToFile("\n", outputPath + "/1-rank.md");

        }
        System.out.println("-------------------");

    }
}