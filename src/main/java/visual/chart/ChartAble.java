package visual.chart;

import visual.node.Node;

import java.util.Map;

/**
 * @author Xavier
 * @date 2024/7/4 21:55
 */
public interface ChartAble {

    void appendHeader();

    String draw(Node root);

    void drawNode(Node parent, Node child);

    void appendBottom();


}
