package visual.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xavier
 * @date 2024/7/4 16:34
 */
public class Constants {

    public static List<String> nonBusinessLogicPkgPrefixList = new ArrayList<>();

    static {
        nonBusinessLogicPkgPrefixList.add("org.slf4j");
        nonBusinessLogicPkgPrefixList.add("java.lang");
    }
}
