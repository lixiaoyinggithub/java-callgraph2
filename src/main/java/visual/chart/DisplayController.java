package visual.chart;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import visual.Starter;
import visual.Constants;
import visual.enums.CacheTypeEnum;
import visual.node.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * @author Xavier
 * @date 2024/7/4 21:59
 */
public class DisplayController {

    private static Logger log = LoggerFactory.getLogger(DisplayController.class);

    private String file = "class_annotation.txt";

    private Map<String, String> clsAnnotationMap = new HashMap<>();

    private Node root;
    private FlawChartBuilder flawChartBuilder;
    private int filterCnt;
    private int cachedCnt;

    private CachedDisplayController cachedDisplayController;

    public static DisplayController instance = new DisplayController();
    private List<String> extraFilterList;


    private DisplayController() {
        // todo builder可定制和接口化
        this.flawChartBuilder = new FlawChartBuilder();
        this.cachedDisplayController = new CachedDisplayController();
        init();
    }

    public void init() {
        // 解析类的注解
        String annotationPath = Starter.rootPath + file;
        try (BufferedReader br = new BufferedReader(new FileReader(annotationPath))) {
            String row;
            while ((row = br.readLine()) != null) {
                String line = row;
                String strictLine = line.replace("\t\t", "\t");
                String[] split = strictLine.split("\t");
                clsAnnotationMap.put(split[0], split[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void filterTree(Node parent) {
        List<Node> children = parent.getChildren();
        Map<String, Object> parentData = parent.getData();
        List<Node> toRemoveNodes = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            String filterKeyword = isBusinessLogic(parentData, child);
            if (filterKeyword != null) {
                toRemoveNodes.add(child);
                continue;
            }
            CacheTypeEnum cacheType = cachedDisplayController.countRedisLoad(child.getName());
            child.setCacheType(cacheType);
            if (i != 0 && i != children.size() - 1) {
                child.setAsync(isAsync(children.get(i - 1), children.get(i + 1), child));
            }
            filterCnt += 1;
        }
        children.removeAll(toRemoveNodes);
        for (Node child : children) {
            filterTree(child);
        }
    }


    /**
     * 是否异步
     *
     * @return
     */
    public static boolean isAsync(Node prior, Node later, Node current) {
        if (prior == null || later == null) {
            return false;
        }
        String priorName = prior.getName();
        String laterName = later.getName();
        String name = current.getName();
        if (!name.contains(":lambda$")) {
            return false;
        }
        if (StringUtils.containsAnyIgnoreCase(laterName, "parallel")) {
            return true;
        }
        if ((StringUtils.containsAnyIgnoreCase(priorName, "Runnable")
                && StringUtils.containsAnyIgnoreCase(laterName, "BuzExecutor"))
                || (StringUtils.containsAnyIgnoreCase(priorName, "Runnable")
                && StringUtils.containsAnyIgnoreCase(laterName, "Thread"))
        ) {
            return true;
        }
        return false;
    }


    public String isBusinessLogic(Map<String, Object> parent, Node child) {

        Map<String, Object> childData = child.getData();
        String parentName = parent.get("name").toString();
        String childName = childData.get("name").toString();

        for (String method : extraFilterList) {
            if (parentName.contains(method)) {
                System.out.println(String.format("filter by pkg`childName=%s`parentName=%s", childName, parentName));
                return method;
            }
        }

        // 通过包名过滤
        for (String pkgPrefix : Constants.NON_BUSINESS_LOGIC_PKG_PREFIX_LIST) {
            if (childName.startsWith(pkgPrefix) && !childName.contains("Runnable") && !childName.contains("Thread")) {
                System.out.println(String.format("filter by pkg`childName=%s`parentName=%s", childName, parentName));
                return pkgPrefix;
            }
        }
        Object parentClassName = parent.get("cls");
        if (parentClassName != null) {
            String clsNameStr = parentClassName.toString();
            // 通过类的注解过滤
            String clsAnnotation1 = clsAnnotationMap.get(clsNameStr);
            if (clsAnnotation1 == null) {
                clsAnnotation1 = clsAnnotationMap.get(clsNameStr.replace("Impl", ""));
            }
            if (StringUtils.isNotBlank(clsAnnotation1)) {

                for (String annotation : Constants.NON_BUSINESS_ANNOTATION_LIST) {
                    if (annotation.equals(clsAnnotation1)) {
                        System.out.println(String.format("filter by annotation`clsAnnotation1=%s`annotation=%s", clsAnnotation1, annotation));
                        return annotation;
                    }
                }
            }
        } else {
//            System.out.println("clsName=null:" + parentName);
        }

        Object childClassName = childData.get("cls");

        if (childClassName != null) {
            // 通过包名过滤
            String simpleName = StringUtils.substringBefore(childClassName.toString(), "(");
            for (String suffix : Constants.NON_BUSINESS_LOGIC_CLS_SUFFIX_LIST) {
                if (simpleName.endsWith(suffix)) {
                    System.out.println(String.format("filter by cls suffix name`childName=%s`parentName=%s", childName, parentName));
                    return suffix;
                }
            }
        }
        if (CollectionUtil.isEmpty(child.getChildren())) {
            // fixme 优化改为有意义的关注点
            CacheTypeEnum cacheTypeEnum = cachedDisplayController.countRedisLoad(childName);
            if (cacheTypeEnum == null) {
                // 叶子节点过滤
                String simpleName = StringUtils.substringBefore(childName, "(");
                for (String suffix : Constants.NON_BUSINESS_TREE_LEAF_LIST) {
                    if (simpleName.contains(suffix)) {
                        System.out.println(String.format("filter by leaf suffix name`childName=%s`parentName=%s`suffix=%s", childName, parentName, suffix));
                        return suffix;
                    }
                }
            }
        }
        return null;
    }


    public String chooseChildCls(Node child) {
        String cls = flawChartBuilder.tryFindCacheCls(child.getCacheType());
        if (StringUtils.isNotBlank(cls)) {
            cachedCnt++;
            return cls;
        }
        if (child.isAsync()) {
            return flawChartBuilder.getAsync();
        }
        return cls;
    }

    public String getHeader() {
        return flawChartBuilder.getHeader();
    }


    public String getClassSetting() {
        return flawChartBuilder.getClassSetting();
    }


    public void reset() {
        filterCnt = 0;
        cachedCnt = 0;
    }

    public int getFilterCnt() {
        return filterCnt;
    }

    public int getCacheCnt() {
        return cachedCnt;
    }

    public String chooseParentCls(Node child) {
        return null;
    }

    public void setExtraFilterList(List<String> extraFilterList) {
        this.extraFilterList = extraFilterList;
    }
}
