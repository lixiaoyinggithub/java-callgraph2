package visual.chart;

import visual.enums.CacheTypeEnum;

import java.util.Map;

/**
 * @author Xavier
 * @date 2024/7/4 21:59
 */
public class FlawChartBuilder {

    /**
     * 样式设置
     */
    private String clsSetting;
    /**
     * 头部
     */
    private String header;

    private CachedDisplayController cachedDisplayController;

    public FlawChartBuilder() {
        this.cachedDisplayController = new CachedDisplayController();
        this.header = "flowchart LR";
        this.clsSetting = "  classDef annotation fill:#877BF1\n" + "  classDef client fill:#10AF17\n" + "  classDef async fill:red\n";
    }

    public String tryFindCacheCls(CacheTypeEnum cacheTypeEnum) {
        if (cacheTypeEnum == null) {
            return "";
        } else if (cacheTypeEnum == CacheTypeEnum.ANNOTATION) {
            return ":::annotation";

        } else if (cacheTypeEnum == CacheTypeEnum.CLIENT) {
            return ":::client";
        }
        return "";
    }

    public String getAsync(){
        return ":::async";
    }

    public String getClassSetting() {
        return clsSetting;
    }

    public String getHeader() {
        return header;
    }

}
