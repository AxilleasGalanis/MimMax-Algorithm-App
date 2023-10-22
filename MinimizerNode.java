public class MinimizerNode extends TreeNode {
    private final String type = "min";
    
    public MinimizerNode() {
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

        return getMinChildValue(this);
    }
    
    private double getMinChildValue(TreeNode node) {
        double minValue = Double.POSITIVE_INFINITY;

        for (int i = 0; i < node.getChildrenSize(); i++) {
            if (node.getChild(i).used == false) {
                continue;
            }
            double childValue = node.getChild(i).getValue();
            if (childValue < minValue) {
                minValue = childValue;
            }
        }

        return minValue;
    }
    
}