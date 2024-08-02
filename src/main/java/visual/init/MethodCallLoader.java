package visual.init;

import visual.Starter;
import visual.entity.Callee;
import visual.entity.Caller;
import visual.utils.VisualStringTools;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Xavier
 * @date 2024/7/3 14:26
 */
public class MethodCallLoader {

    static ExecutorService es = new ThreadPoolExecutor(80, 80, 5,
            TimeUnit.MINUTES, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

    public static Map<String, List<Callee>> CALLER_MAP = new HashMap<>();

    public static void loadMethodCall(String file) {
        String path = Starter.rootPath + file;
        int total = 0;
        AtomicInteger atomTotal = new AtomicInteger(0);
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String row;
            while ((row = br.readLine()) != null) {
                total++;
                String line = row;
                if (filter(line)) {
                    continue;
                }
                String[] parts = line.split("\t");
                Caller caller = VisualStringTools.parseCaller(parts[1]);
                Callee callee = VisualStringTools.parseCallee(parts);

                if (caller == null || callee == null) {
                    System.out.println("line:" + line);
                    continue;
                }
                List<Callee> callees = CALLER_MAP.get(caller.getFullName());
                if (callees == null) {
                    callees = new ArrayList<>();
                    CALLER_MAP.put(caller.getFullName(), callees);
                }
                callees.add(callee);
                atomTotal.incrementAndGet();
            }

            System.out.println("CALLER_MAP:" + CALLER_MAP.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean filter(String info) {
        return info.contains(".test.");
    }

}
