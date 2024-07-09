package visual;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import org.neo4j.driver.*;
import visual.chart.ChartAble;
import visual.chart.DrawRequirement;
import visual.chart.FlowChartImpl;

import java.io.Closeable;
import java.util.*;


public class Neo4jCommonTools implements Closeable {

    private final Driver driver;

    public Neo4jCommonTools(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() {
        driver.close();
    }

    public long runAndGetId(String runExp) {
        try (Session session = driver.session()) {
            Result result = session.run(runExp);
            long nodeId = result.single().get("nodeId").asLong();
            return nodeId;
        }
    }

    public long addNode(Map<String, String> properties) {
        try (Session session = driver.session()) {
            String propertiesJson = JSONUtil.toJsonStr(properties);
            // 创建一个节点
            Result result = session.run("CREATE (n:Method {name:'" + properties.get("name") + "'}) RETURN id(n) AS nodeId");
            long nodeId = result.single().get("nodeId").asLong();
            return nodeId;
        }
    }

    public void createRelationship(long startNodeId, long endNodeId, String relationshipType, String name) {
        try (Session session = driver.session()) {
            String query = String.format(
                    "MATCH (start:Method), (end:Method) " +
                            "WHERE ID(start) = %d AND ID(end) = %d " +
                            "CREATE (start)-[r:%s {name: '" + name + "'}]->(end)",
                    startNodeId, endNodeId, relationshipType
            );
            session.run(query);
        }
    }

    public void printGreeting(final String message) {
        try (Session session = driver.session()) {
            String greeting = session.writeTransaction(tx -> {
                Result result = tx.run(
                        "CREATE (a:Greeting) " + "SET a.message = $message "
                                + "RETURN a.message + ', from node ' + id(a)",
                        Values.parameters("message", message));
                return result.single().get(0).asString();
            });
            System.out.println(greeting);
        }
    }

    public String dropFlowchart(String inputName, ChartAble chartAble) {
        chartAble.start();

        // fullName - 节点
        Map<String, Map<String, Object>> recordMap = new HashMap<>();

        recordMap.put(inputName, MapUtil.of("name", inputName));

        try (Session session = driver.session()) {
            Queue<String> queue = new LinkedList<>();
            Set<String> visited = new HashSet<>();

            queue.add(inputName);
            visited.add(inputName);

            while (!queue.isEmpty()) {
                String sourceName = queue.poll();
                String query = "MATCH (:Method { name: $name })-->(method)RETURN method";
                Result result = session.run(query, Values.parameters("name", sourceName));

                while (result.hasNext()) {
                    Record record = result.next();
                    Map<String, Object> targetMap = record.values().get(0).asMap();
                    String name = targetMap.get("name").toString();
                    recordMap.put(name, targetMap);
                    if (!visited.contains(name)) {
                        Map<String, Object> sourceMap = recordMap.get(sourceName);
                        chartAble.draw(sourceMap, targetMap);
                        queue.add(name);
                        visited.add(name);
                    }
                }
            }
        }
        chartAble.end();
        return chartAble.getContent();
    }



}