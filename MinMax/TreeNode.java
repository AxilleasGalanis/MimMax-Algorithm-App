package MinMax;

public class TreeNode extends Node {
   private TreeNode[] children;
   public boolean calculated = false;
   public boolean used = false;
   private final String type = " ";
   private final double alpha = Double.NEGATIVE_INFINITY;
   private final double beta = Double.POSITIVE_INFINITY;

    public TreeNode() {
       super();
    }
   
    public TreeNode(double value) {
        super(value);
    }
    
    public TreeNode(TreeNode[] children) {
       super();
       this.children = children;
    }

    public void setChildrenSize(int size) {
        children = new TreeNode[size];
    }

    public int getChildrenSize() {
        return children.length;
    }

    public void insertChild(int pos, TreeNode X) {
        children[pos] = X;
    }

    public TreeNode getChild(int pos) {
        return children[pos];
    }
    
    public String getType() {
        return this.type;
    }
}
