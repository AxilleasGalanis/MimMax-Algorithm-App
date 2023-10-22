import java.io.*;
import java.util.*;
import org.json.*;

public class Tree {
    private TreeNode root;

    public Tree() {
        
    }
    public Tree(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            // Create the tree from the JSON string
            this.root = buildTree(jsonObject);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid JSON format");
        }
    }

    public Tree(File file) throws FileNotFoundException {
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
            this.root = buildTree(jsonObject);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid JSON format");
        }
    }

    // Method to build the tree from a JSON object
    public TreeNode buildTree(JSONObject jsonObject) throws IllegalArgumentException {
        // Check if the JSON object is a leaf node
        if (jsonObject.has("value")) {
            double value = jsonObject.getDouble("value");
            TreeNode node = new TreeNode(value);
            node.setChildrenSize(0);
            // Return the leaf node
            return node;
        }

        // Otherwise, create a new internal node
        TreeNode node;

        // Set the node type to "maximizer" or "minimizer"
        if (jsonObject.getString("type").equals("max")) {
            node = new MaximizerNode();
        } else {
            node = new MinimizerNode();
        }

        // Recursively build the children of the node
        JSONArray children = jsonObject.getJSONArray("children");
        node.setChildrenSize(children.length());
        for (int i = 0; i < children.length(); i++) {
            JSONObject child = children.getJSONObject(i);
            node.insertChild(i, buildTree(child));
        }

        return node;
    }


    // method for solving MinMax algorithm on the tree
    public double minMax() {
        return minMaxRecursive(root);
    }
    
    // private recursive method for calculating minMax
    private double minMaxRecursive(TreeNode node) {
        if (node == null) {
            // if node is null return 0
            return 0;
        } 
        
        if (node.getChildrenSize() == 0) {
            node.used = true;
            // if node is leaf return its value
            return node.getValue();
        } 
        else {
            // if node is not leaf, recursively calculate the min/max of its children
            for (int i = 0; i < node.getChildrenSize(); i++) {
                minMaxRecursive(node.getChild(i));
            }
            double result = node.getValue();
            node.used = true;
            node.calculated = true;
            node.setValue(result);
            return result;
        }
    }

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
    
    @Override
    public String toString() {
        return toStringHelper(root);
    }
    
    private static String toStringHelper(TreeNode node) {
        if (node.getChildrenSize() == 0) {
            // Leaf node
            return "{\"type\": \"leaf\", \"value\": " + node.getActValue() + "}";
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
            if (node.calculated == false)
                return "{\"type\": \"" + node.getType() + "\", \"children\": " + childrenJson.toString() + "}";
            else
                return "{\"type\": \"" + node.getType() + "\", \"value\": " + node.getActValue() +  ", \"children\": " + childrenJson.toString() + "}";

        }
    }

    public String toDotString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph Tree {\n");
        toDotStringHelper(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void toDotStringHelper(TreeNode node, StringBuilder sb) {
        if (node != null) {
            if (node.getChildrenSize() == 0) { // node is a leaf node
                sb.append(node.hashCode());
                sb.append("[label=\"");
                sb.append(node.getActValue());
                sb.append("\", shape=square];\n");
            } else { // node is an internal node
                sb.append(node.hashCode());
                sb.append("[label=\"");
                sb.append(node.getActValue());
                sb.append("\", shape=circle];\n");
                for (int i = 0; i < node.getChildrenSize(); i++) {
                    TreeNode child = node.getChild(i);
                    if (child != null) {
                        sb.append(node.hashCode());
                        sb.append(" -> ");
                        sb.append(child.hashCode());
                        sb.append(";\n");
                        toDotStringHelper(child, sb);
                    }
                }
            }
        }
    }

    public void toFile(File file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(this.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

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