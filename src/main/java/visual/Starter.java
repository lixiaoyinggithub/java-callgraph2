package visual;

import visual.chart.CachedManager;
import visual.chart.ChartAble;
import visual.chart.DrawRequirement;
import visual.chart.FlowChartImpl;

import java.util.*;


public class Starter {


    public static void main(String... args) throws Exception {
//        # Wait 60 seconds before connecting using these details, or login to https://console.neo4j.io to validate the Aura Instance is available
//        NEO4J_URI=neo4j+s://418f83db.databases.neo4j.io
//        NEO4J_USERNAME=neo4j
//        NEO4J_PASSWORD=rGFLArEh_LOrG1JaRFSHW06D85bdm-ShufV7tocRvU0
//        AURA_INSTANCEID=418f83db
//        AURA_INSTANCENAME=Instance01

        Neo4jCommonTools tools;
//        tools = new Neo4jCommonTools("bolt://localhost:7687", "neo4j", "neo4j");
        tools = new Neo4jCommonTools("neo4j+s://418f83db.databases.neo4j.io", "neo4j", "rGFLArEh_LOrG1JaRFSHW06D85bdm-ShufV7tocRvU0");


        List<String> annotationList = new ArrayList<>();
        annotationList.add("com.alicp.jetcache.anno.Cached"); // cacheType
        annotationList.add("com.buz.common.mq.MqListener"); // cacheType
        annotationList.add("com.alicp.jetcache.anno.Cached");

        List<String> clsNameList = new ArrayList<>();
        clsNameList.add("redis");

        CachedManager cachedManager = new CachedManager("/Users/aly/IdeaProjects/buz/buz-dc-core/buz-dc-core-app/target/buz-dc-core-app-1.0.123-SNAPSHOT.jar-output_javacg/method_annotation_副本.txt");

        DrawRequirement config = new DrawRequirement(annotationList, clsNameList, cachedManager);

        ChartAble flowChartImpl = new FlowChartImpl(config);

        String chartBody = tools.dropFlowchart("com.buz.dc.core.service.activity.ChallengeServiceImpl:saveChallengeQuestion(com.buz.dc.core.dto.request.activity.ChallengeQuestionParams)"
                , flowChartImpl);

        System.out.println(chartBody);


    }


}