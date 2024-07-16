package visual.chart;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import visual.node.Node;
import visual.utils.VisualStringTools;

import java.util.*;

/**
 * @author Xavier
 * @date 2024/7/4 21:56
 */
public class FlowChartImpl implements ChartAble {


    private DisplayController displayController;

    private StringBuffer content;


    private static Map<Integer, List<String>> cachedRankMap = new TreeMap<>();

    private int asyncCacheCount;

    public FlowChartImpl(List<String> extraFilterList) {
        content = new StringBuffer();
        this.displayController = DisplayController.instance;
        displayController.setExtraFilterList(extraFilterList);
    }

    void appendBody(Node parent, boolean isAsync) {
        List<Node> children = parent.getChildren();
        if (CollectionUtil.isEmpty(children)) {
            return;
        }
        for (Node child : children) {
            if (child.getCacheType() != null && isAsync) {
                asyncCacheCount++;
            }
            drawNode(parent, child);
            appendBody(child, isAsync || child.isAsync());
        }
    }

    @Override
    public void appendHeader() {
        content.append(displayController.getHeader() + "\n");
    }

    @Override
    public String draw(Node root) {
        reset();
        content.append("\n");
        appendHeader();
        displayController.filterTree(root);
        displayController.filterTree(root);
        displayController.filterTree(root);
        displayController.filterTree(root);
        displayController.filterTree(root);
        appendBody(root, false);
        appendBottom();

        int cacheCnt = displayController.getCacheCnt();
        List<String> methods = cachedRankMap.get(cacheCnt);
        if (methods == null) {
            methods = new ArrayList<>();
            cachedRankMap.put(cacheCnt, methods);
        }
        methods.add(VisualStringTools.replaceBracket(root.getName()));

        content.append("filterCnt:" + displayController.getFilterCnt() + "\n");
        content.append("cacheSum:" + displayController.getCacheCnt() + "\n");
        content.append("asyncCacheSum:" + asyncCacheCount + "\n");

        return content.toString();
    }

    private void reset() {
        asyncCacheCount = 0;
        content = new StringBuffer();
        displayController.reset();
    }

    @Override
    public void drawNode(Node parent, Node child) {
        if (StringUtils.isNotBlank(child.getFilterKeyword())) {
            return;
        }
        String source = parent.getName();
        String target = child.getName();

        String childCls = displayController.chooseChildCls(child);

        String invokeComment = StringUtils.isNotBlank(childCls) ? "-- " + (childCls.replace(":::", "")) : "";

        content.append("  " + VisualStringTools.replaceBracket(trim(source)) + invokeComment + "-->" + VisualStringTools.replaceBracket(trim(target))
                + childCls);
        content.append("\n");
    }

    private String trim(String name) {
        return name.replace("com.buz.dc.core.", "");
    }

    public int getAsyncCacheCount() {
        return asyncCacheCount;
    }

    @Override
    public void appendBottom() {
        content.append(displayController.getClassSetting());
    }

    public static Map<Integer, List<String>> getCachedRankMap() {
        return cachedRankMap;
    }
}
