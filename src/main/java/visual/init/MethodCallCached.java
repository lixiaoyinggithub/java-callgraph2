package visual.init;

import visual.Neo4jManager;
import visual.Starter;
import visual.entity.Callee;
import visual.entity.Caller;
import visual.utils.VisualStringTools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Xavier
 * @date 2024/7/3 14:26
 */
public class MethodCallCached {


    /**
     * 解析method_call，写入数据库
     *
     * @throws FileNotFoundException
     */
    public static Set<String> listRoot() {
        String path = Starter.rootPath + "method_call.txt";

        Set<String> allRootMethod = new HashSet<>();
        Set<String> rpcMethodNameSet = new HashSet<>();
        Map<String, String> linkParentMap = new HashMap<>();


        System.out.println(path);
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String row;
            while ((row = br.readLine()) != null) {

                String line = row;
                if (filter(line)) {
                    continue;
                }
                String[] parts = line.split("\t");
                Caller caller = VisualStringTools.parseCaller(parts[1]);
                Callee callee = VisualStringTools.parseCallee(parts);

                if (caller == null || callee == null) {
                    continue;
                }

                allRootMethod.add(caller.getFullName());
                allRootMethod.add(callee.getFullName());

                linkParentMap.put(callee.getFullName(), caller.getFullName());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int count = 0;
        for (String method : allRootMethod) {

            if (linkParentMap.get(method) == null && (!method.contains("infrastructure") && !method.contains("manager")
                    && !method.contains("entity") && !method.contains("enums") && !method.contains("config")
                    && !method.contains("constants") && !method.contains("util") && !method.contains("MicroservicesApplication")
                    && !method.contains("<init>")
                    && !method.contains("clinit")
                    && !method.contains("aspect")
                    && !method.contains("jmx")
            )) {
                count++;
                rpcMethodNameSet.add(method);
            }
        }

        return rpcMethodNameSet;
    }


    public static boolean filter(String info) {
        return info.contains(".test.");
    }

}
