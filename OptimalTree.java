import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.*;

public class OptimalTree extends Tree {
    public TreeNode root;
    public double usedNodes = 0;
    
    public OptimalTree(String jsonString) {
        super();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            // Create the tree from the JSON string
            this.root = super.buildTree(jsonObject);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid JSON format");
        }
    }

    public OptimalTree(File file) throws IOException {
        super();
        // Check if the file exists and is readable
        if (!file.exists() || !file.canRead()) {
            throw new FileNotFoundException("File does not exist or is not readable.");
        }

        // Read the contents of the file as a string
        String jsonString = "";
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                jsonString += scanner.nextLine();
            }
            scanner.close();
        } catch (Exception e) {
            throw new FileNotFoundException("Error reading file.");
        }

        // Check if the string is a valid JSON
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            // Create the tree from the JSON string
            this.root = super.buildTree(jsonObject);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid JSON format");
        }
    }

    @Override
    public double minMax() {
        return minMaxPrune(root, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    private double minMaxPrune(TreeNode node, double alpha, double beta) {
        if (node == null) {
            // if node is null return 0
            return 0;
        } 

        if (node.getChildrenSize() == 0) {
            node.used = true;
            node.calculated = true;
            usedNodes++;
            // if node is leaf return its value
            return node.getValue();
        } 

        if (node.getType().equals("max")) {
            double value = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < node.getChildrenSize(); i++) {
                value = Math.max(value, minMaxPrune(node.getChild(i), alpha, beta));
                alpha = Math.max(alpha, value);
                if (beta <= alpha) {
                    break;
                }
            }
            node.used = true;
            node.calculated = true;
            usedNodes++;
            node.setValue(value);
            return value;
        } else {
            double value = Double.POSITIVE_INFINITY;
            for (int i = 0; i < node.getChildrenSize(); i++) {
                value = Math.min(value, minMaxPrune(node.getChild(i), alpha, beta));
                beta = Math.min(beta, value);
                if (beta <= alpha) {
                    break;
                }
            }
            node.used = true;
            node.calculated = true;
            usedNodes++;
            node.setValue(value);
            return value;
        }
    }
    
    @Override
    public ArrayList<Integer> optimalPath() {
        ArrayList<Integer> path = new ArrayList<Integer>();
        findOptimal(root, path);
        return path;
    }
    
    private void findOptimal(TreeNode node, ArrayList<Integer> path) {
        if (node == null) {
            return;
        }

        if (node.getChildrenSize() == 0) {
            return;
        }

        double nodeValue = node.getValue();
        int optimalChildIndex = -1;
        double optimalChildValue = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < node.getChildrenSize(); i++) {
            double childValue = node.getChild(i).getValue();
            if (nodeValue == childValue) {
                if (childValue > optimalChildValue) {
                    optimalChildIndex = i;
                    optimalChildValue = childValue;
                }
            }
        }

        if (optimalChildIndex >= 0) {
            path.add(optimalChildIndex);
            findOptimal(node.getChild(optimalChildIndex), path);
        }
    }
    
    public double prunedNodes() {
        double totalNodes = countNodes(root);
        return totalNodes - usedNodes;
    }
    
    public int countNodes(TreeNode node) {
        int count = 1; // count the current node
        
        for (int i = 0; i < node.getChildrenSize(); i++) {
            count += countNodes(node.getChild(i));
        }
        
        return count;
    }
    
    @Override
    public String toString() {
        return toStringHelper(root);
    }
    
    private static String toStringHelper(TreeNode node) {
        if (node.getChildrenSize() == 0) {
            if (node.used == false) {
                return "{\"type\": \"leaf\", \"value\": " + node.getActValue() + ", \"pruned\": true}";
            } else {
                return "{\"type\": \"leaf\", \"value\": " + node.getActValue() + "}";
            }
        } else {
            // Non-leaf node
            StringBuilder childrenJson = new StringBuilder("[");
            for (int i = 0; i < node.getChildrenSize(); i++) {
                TreeNode child = node.getChild(i);
                childrenJson.append(toStringHelper(child));
                if (i != node.getChildrenSize() - 1) {
                    childrenJson.append(",");
                }
            }
            childrenJson.append("]");
            if (node.calculated == false){
                if (node.used == true)
                    return "{\"type\": \"" + node.getType() + "\", \"children\": " + childrenJson.toString() + "}";
                else
                    return "{\"type\": \"" + node.getType() + "\", \"children\": " + childrenJson.toString() + ", \"pruned\": true}";
            }
            else{
                if (node.used == true)
                    return "{\"type\": \"" + node.getType() + "\", \"value\": " + node.getActValue() +  ", \"children\": " + childrenJson.toString() + "}";
                else
                    return "{\"type\": \"" + node.getType() + "\", \"value\": " + node.getActValue() +  ", \"children\": " + childrenJson.toString() + ", \"pruned\": true}";
            }

        }
    }
    
    @Override
    public String toDotString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph Tree {\n");
        toDotStringPrunedHelper(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void toDotStringPrunedHelper(TreeNode node, StringBuilder sb) {
        if (node != null) {
            if (node.getChildrenSize() == 0) { // node is a leaf node
                sb.append(node.hashCode());
                sb.append("[label=\"");
                sb.append(node.getActValue());
                if (node.used == false) { // check if node is pruned
                    sb.append("\", shape=square, color=red];\n");
                } else {
                    sb.append("\", shape=square];\n");
                }
            } else { // node is an internal node
                sb.append(node.hashCode());
                sb.append("[label=\"");
                sb.append(node.getActValue());
                if (node.used == false) { // check if node is pruned
                    sb.append("\", shape=circle, color=red];\n");
                } else {
                    sb.append("\", shape=circle];\n");
                }
                for (int i = 0; i < node.getChildrenSize(); i++) {
                    TreeNode child = node.getChild(i);
                    if (child != null) {
                        sb.append(node.hashCode());
                        sb.append(" -> ");
                        sb.append(child.hashCode());
                        sb.append(";\n");
                        toDotStringPrunedHelper(child, sb);
                    }
                }
            }
        }
    }
    
    @Override
    public void toFile(File file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(this.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void toDotFile(File file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(toDotString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}