public class MaximizerNode extends TreeNode {
    private final String type = "max";
    
    public MaximizerNode() {
        super();
    }
    
    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public double getValue() {
        if (getChildrenSize() == 0) {
            return super.getValue();
        }

        return getMaxChildValue(this);
    }
    
    private double getMaxChildValue(TreeNode node) {
        double maxValue = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < node.getChildrenSize(); i++) {
            if (node.getChild(i).used == false) {
                continue;
            }
            double childValue = node.getChild(i).getValue();
            if (childValue > maxValue) {
                maxValue = childValue;
            }
        }

        return maxValue;
    }
    
}