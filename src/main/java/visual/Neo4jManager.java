package visual;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import org.neo4j.driver.*;
import visual.chart.ChartAble;
import visual.entity.Callee;
import visual.init.MethodCallImport;
import visual.node.Node;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.util.*;


public class Neo4jManager implements Closeable {

    private final Driver driver;

    public Neo4jManager(String uri, String user, String password) {
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


    public void traverByNeo4j(Node root) {
        // fullName - 节点
        Map<String, Map<String, Object>> recordMap = new HashMap<>();
        String rootName = root.getName();
        recordMap.put(rootName, MapUtil.of("name", rootName));

        try (Session session = driver.session()) {
            Queue<Node> queue = new LinkedList<>();
            Set<String> visited = new HashSet<>();

            queue.add(root);
            visited.add(rootName);

            while (!queue.isEmpty()) {
                Node currentNode = queue.poll();
                String query = "MATCH (:Method { name: $name })-->(method)RETURN method";
                Result result = session.run(query, Values.parameters("name", currentNode.getName()));

                List<Node> children = currentNode.getChildren();

                while (result.hasNext()) {
                    Record record = result.next();
                    Map<String, Object> targetMap = record.values().get(0).asMap();
                    String name = targetMap.get("name").toString();
                    recordMap.put(name, targetMap);

                    if (!visited.contains(name)) {

                        Node node = new Node(name, targetMap, new ArrayList<>());
                        children.add(node);

                        queue.add(node);
                        visited.add(name);
                    }
                }
            }
        }
    }


    public void traverByMap(Node root) {


        String rootName = root.getName();
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(root);
        visited.add(rootName);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            List<Callee> callees = MethodCallImport.CALLER_MAP.get(currentNode.getName());

            if (callees == null) {
                continue;
            }

            List<Node> children = currentNode.getChildren();

            for (Callee record : callees) {
                String name = record.getFullName();
                if (!visited.contains(name)) {
                    Map<String, Object> targetMap = toMap(record);
                    Node node = new Node(name, targetMap, new ArrayList<>());
                    children.add(node);

                    queue.add(node);
                    visited.add(name);
                }
            }

        }
    }

    public static Map<String, Object> toMap(Callee callee) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", callee.getFullName());
        map.put("fullName", callee.getFullName());
        map.put("callType", callee.getCallType());
        map.put("methodName", callee.getMethodName());
        map.put("cls", callee.getClassName());
        map.put("returnType", callee.getReturnValType());
        return map;
    }


}