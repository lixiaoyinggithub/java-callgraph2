package visual;

import visual.entity.Callee;
import visual.entity.Caller;
import visual.utils.VisualStringTools;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Xavier
 * @date 2024/7/3 14:26
 */
public class MethodCallFileParser {

    static ExecutorService es = new ThreadPoolExecutor(80, 80, 5,
            TimeUnit.MINUTES, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 解析method_call，写入数据库
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        String path = "/Users/aly/Downloads/method_call.txt";


        Neo4jCommonTools tools;
//        tools = new Neo4jCommonTools("neo4j://localhost:7687", "neo4j", "neo4j");
        tools = new Neo4jCommonTools("neo4j+s://418f83db.databases.neo4j.io", "neo4j", "rGFLArEh_LOrG1JaRFSHW06D85bdm-ShufV7tocRvU0");


        /**
         1	方法调用序号，从1开始
         2	调用方，完整方法（类名+方法名+参数）
         3	(方法调用类型)被调用方，完整方法（类名+方法名+参数）
         4	调用方法源代码行号
         5	调用方法返回类型
         6	被调用对象类型，t:调用当前实例的方法，sf:调用静态字段的方法，f:调用字段的方法，v:调用其他变量的方法
         7	被调用方法原始的返回类型
         8	被调用方法实际的返回类型
         9	调用方法Jar包序号
         10	被调用方法Jar包序号
         **/

        int total = 0;
        AtomicInteger atomTotal = new AtomicInteger(0);
        /**
         * 每行得到2个实体，一个关系
         */
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String row;
            while ((row = br.readLine()) != null) {
                total++;
                String line = row;
                es.submit(() -> {
                    if (filter(line)) {
                        return;
                    }

                    String[] parts = line.split("\t");
                    Caller caller = VisualStringTools.parseCaller(parts[1]);
                    Callee callee = VisualStringTools.parseCallee(parts);

                    if (caller == null || callee == null) {
                        System.out.println("line:" + line);
                        return;
                    }

                    String callerExp = buildCallerExp(caller);
                    String calleeExp = buildCalleeExp(callee);

                    long callerId = tools.runAndGetId(callerExp);
                    long calleeId = tools.runAndGetId(calleeExp);

                    tools.createRelationship(callerId, calleeId, "invoke", caller.getFullName() + "-" + callee.getFullName());
                    atomTotal.incrementAndGet();
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


    private static Map<String, String> hashMap(String... args) {
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            objectObjectHashMap.put(args[i], args[i + 1]);
        }
        return objectObjectHashMap;
    }

    public static String buildCallerExp(Caller caller) {
        return "CREATE (n:Method {name:'" + caller.getFullName() +
                "',cls:'" + caller.getClassName() + "',methodName:'" + caller.getMethodName() + "'}) RETURN id(n) AS nodeId";
    }

    public static String buildCalleeExp(Callee callee) {
        return "CREATE (n:Method {name:'" + callee.getFullName() +
                "',cls:'" + callee.getClassName() +
                "',methodName:'" + callee.getMethodName() + "'}) RETURN id(n) AS nodeId";
    }

    public static boolean filter(String info) {
        return info.contains(".test.");
    }

}
