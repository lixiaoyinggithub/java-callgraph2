package visual;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xavier
 * @date 2024/7/4 16:34
 */
public class Constants {

    //包前缀非业务关键词
    public static List<String> NON_BUSINESS_LOGIC_PKG_PREFIX_LIST = new ArrayList<>();

    // 叶子节点非业务逻辑关键词
    public static List<String> NON_BUSINESS_TREE_LEAF_LIST = new ArrayList<>();

    public static List<String> NON_BUSINESS_ANNOTATION_LIST = new ArrayList<>();

    public static List<String> NON_BUSINESS_LOGIC_CLS_SUFFIX_LIST = new ArrayList<>();
    static {
        NON_BUSINESS_LOGIC_PKG_PREFIX_LIST.add("org.slf4j");
        NON_BUSINESS_LOGIC_PKG_PREFIX_LIST.add("java.lang");
        NON_BUSINESS_LOGIC_PKG_PREFIX_LIST.add("java.util");
        NON_BUSINESS_LOGIC_PKG_PREFIX_LIST.add("cn.hutool");
        NON_BUSINESS_LOGIC_PKG_PREFIX_LIST.add("fm.lizhi.biz.data");
//        NON_BUSINESS_LOGIC_PKG_PREFIX_LIST.add("com.buz.common.utils");
        NON_BUSINESS_LOGIC_PKG_PREFIX_LIST.add("org.apache.commons");
        NON_BUSINESS_LOGIC_PKG_PREFIX_LIST.add("fm.lizhi.commons.config.util.JsonUtil");

        NON_BUSINESS_ANNOTATION_LIST.add("org.mapstruct.Mapper");

        NON_BUSINESS_LOGIC_CLS_SUFFIX_LIST.add("DTO");
        NON_BUSINESS_LOGIC_CLS_SUFFIX_LIST.add("VO");

        NON_BUSINESS_TREE_LEAF_LIST.add("Example$Criteria");
        NON_BUSINESS_TREE_LEAF_LIST.add("Example$GeneratedCriteria");
        NON_BUSINESS_TREE_LEAF_LIST.add(":init");
        NON_BUSINESS_TREE_LEAF_LIST.add(":<init>");
        NON_BUSINESS_TREE_LEAF_LIST.add(":build");
        NON_BUSINESS_TREE_LEAF_LIST.add("org.springframework");
        NON_BUSINESS_TREE_LEAF_LIST.add("enums");
        NON_BUSINESS_TREE_LEAF_LIST.add("Params:set");
        NON_BUSINESS_TREE_LEAF_LIST.add("Params:get");
        NON_BUSINESS_TREE_LEAF_LIST.add("DTO:get");
        NON_BUSINESS_TREE_LEAF_LIST.add("DTO:set");
        NON_BUSINESS_TREE_LEAF_LIST.add(":set");
        NON_BUSINESS_TREE_LEAF_LIST.add(":get");
        NON_BUSINESS_TREE_LEAF_LIST.add("dto");
        NON_BUSINESS_TREE_LEAF_LIST.add("entity");
        NON_BUSINESS_TREE_LEAF_LIST.add("I18nConfigAdapter");
        NON_BUSINESS_TREE_LEAF_LIST.add("I18nTextUtils");
        NON_BUSINESS_TREE_LEAF_LIST.add(":builder");
        NON_BUSINESS_TREE_LEAF_LIST.add("Result:target");
        NON_BUSINESS_TREE_LEAF_LIST.add("Result:rCode");
    }

    public static void main(String[] args) {
        System.out.println("com.buz.dc.core.infrastructure.model.GroupInfoExample$GeneratedCriteria:init".contains("Example$GeneratedCriteria:init"));
    }
}
