package visual.chart;

import java.util.Map;

/**
 * @author Xavier
 * @date 2024/7/4 21:55
 */
public interface ChartAble {

    void start();

    void draw(Map<String, Object> source, Map<String, Object> target);

    void end();

    String chooseCls(Map<String, Object> properties);

    String getContent();

}
