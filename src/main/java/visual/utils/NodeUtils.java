package visual.utils;

import visual.entity.Callee;
import visual.init.MethodCallLoader;
import visual.node.Node;

import java.util.*;

/**
 * @author Xavier
 * @date 2024/8/2 17:59
 */
public class NodeUtils {

    /**
     * 遍历节点
     *
     * @param root
     */
    public static void traverByMap(Node root) {
        String rootName = root.getName();
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(root);
        visited.add(rootName);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            List<Callee> callees = MethodCallLoader.CALLER_MAP.get(currentNode.getName());

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
