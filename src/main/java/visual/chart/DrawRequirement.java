package visual.chart;

import visual.entity.Constants;

import java.util.List;
import java.util.Map;

/**
 * @author Xavier
 * @date 2024/7/4 21:59
 */
public class DrawRequirement {

    /**
     * 关注的注解的样式
     */
    private List<String> annotationList;

    /**
     * 所关注的类粒度的样式
     */
    private List<String> clsNameList;

    /**
     * 样式设置
     */
    private String clsSetting;
    /**
     * 头部
     */
    private String header;

    private CachedManager cachedManager;


    public DrawRequirement(List<String> annotationList, List<String> clsNameList, CachedManager cachedManager) {
        this.annotationList = annotationList;
        this.clsNameList = clsNameList;
        this.cachedManager = cachedManager;
        this.header = "flowchart LR";
        this.clsSetting = "  classDef annotation fill:#877BF1\n" + "  classDef concerned fill:#10AF17\n";
    }

    public String chooseCls(Map<String, Object> properties) {
        String name = properties.get("name") != null ? properties.get("name").toString() : "";
        int redisAccessCnt = cachedManager.countRedisLoad(name);
        if (redisAccessCnt > 0) {
            return ":::redisTimes" + redisAccessCnt;
        }
        return "";
    }


    public void setClass(String clsSetting) {
        this.clsSetting = clsSetting;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getClassSetting() {
        return clsSetting;
    }

    public String getHeader() {
        return header;
    }

    /**
     * 是否有效的业务逻辑路径
     *
     * @param names
     * @return
     */
    public static boolean isBusinessLogic(String... names) {
        for (String name : names) {
            for (String pkgPrefix : Constants.nonBusinessLogicPkgPrefixList) {
                if (name.startsWith(pkgPrefix)) {
                    return false;
                }
            }
        }
        return true;
    }


}
