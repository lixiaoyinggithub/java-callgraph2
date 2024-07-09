package visual.chart;

import visual.utils.VisualStringTools;

import java.util.Map;

/**
 * @author Xavier
 * @date 2024/7/4 21:56
 */
public class FlowChartImpl implements ChartAble {

    private DrawRequirement drawRequiremendt;

    private StringBuffer content = new StringBuffer();

    public FlowChartImpl(DrawRequirement drawRequiremendt) {
        this.drawRequiremendt = drawRequiremendt;
    }

    @Override
    public void start() {
        content.append(drawRequiremendt.getHeader());
    }

    @Override
    public void draw(Map<String, Object> p1, Map<String, Object> p2) {
        String source = p1.get("name").toString();
        String target = p2.get("name").toString();
        boolean businessLogic = DrawRequirement.isBusinessLogic(source, target);
        if (businessLogic) {
            content.append("  " + VisualStringTools.replaceBracket(source) + chooseCls(p2) + "-->" + VisualStringTools.replaceBracket(target));
        }
    }

    @Override
    public void end() {
        content.append(drawRequiremendt.getClassSetting());
    }

    @Override
    public String chooseCls(Map<String, Object> properties) {
        return drawRequiremendt.chooseCls(properties);
    }

    @Override
    public String getContent() {
        return content.toString();
    }
}
