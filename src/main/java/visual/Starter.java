package visual;

import cn.hutool.json.JSONUtil;
import visual.chart.ChartAble;
import visual.chart.FlowChartImpl;
import visual.init.MethodRootFinder;
import visual.init.MethodCallLoader;
import visual.node.Node;
import visual.utils.FileTools;
import visual.utils.NodeUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Starter {

    //class_annotation.txt
    public static final String rootPath = "";

    public static final String outputPath = "/Users/aly/Documents/rpc_graph/";


    public static void main(String... args) throws Exception {
//        # Wait 60 seconds before connecting using these details, or login to https://console.neo4j.io to validate the Aura Instance is available
//        NEO4J_URI=neo4j+s://418f83db.databases.neo4j.io
//        NEO4J_USERNAME=neo4j
//        NEO4J_PASSWORD=rGFLArEh_LOrG1JaRFSHW06D85bdm-ShufV7tocRvU0
//        AURA_INSTANCEID=418f83db
//        AURA_INSTANCENAME=Instance01

//        neo4jManager = new Neo4jCommonTools("bolt://localhost:7687", "neo4j", "neo4j");

        Set<String> methods = MethodRootFinder.listRoot();

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
        return methods;
    }

    public static void start(Set<String> methods, List<String> extraFilterList) throws InterruptedException {

        Map<String, String> bodyMap = new HashMap<>();

        AtomicInteger count = new AtomicInteger();

        MethodCallLoader.loadMethodCall("method_call.txt");

        methods.stream().forEach(method -> {

            count.incrementAndGet();
            ChartAble flowChartImpl = new FlowChartImpl(extraFilterList);
            Node root = new Node();
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", method);
            root.setName(method);
            root.setChildren(new ArrayList<>());
            root.setData(data);
            NodeUtils.traverByMap(root);

            String body = flowChartImpl.draw(root);
            System.out.println(method + ",body:" + body.length());
            bodyMap.put(method, body);

            System.out.println("count:" + count.get());
            FileTools.writeToFile(body, outputPath + FileTools.createFileName(method));
        });

        for (Map.Entry<Integer, List<String>> entry : FlowChartImpl.getCachedRankMap().entrySet()) {
            FileTools.writeToFile(entry.getKey() + "", outputPath + "/1-rank.md");
            FileTools.writeToFile(JSONUtil.toJsonStr(entry.getValue()), outputPath + "/1-rank.md");
            FileTools.writeToFile("\n", outputPath + "/1-rank.md");

        }
        System.out.println("-------------------");

    }
}