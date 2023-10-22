import java.util.Scanner;
import java.io.*;
import org.json.*;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class MinMax {
    private static Tree root;
    private static OptimalTree optimalRoot;

    private static String insertTreeFromFile(String filepath) {
        try {
            File file = new File(filepath);

            if (!file.exists()) {
                System.out.println("Unable to find '" + filepath + "'");
                return null;
            }

            if (!file.canRead()) {
                System.out.println("Unable to open '" + filepath + "'");
                return null;
            }
            FileReader Reader = new FileReader(filepath);
            JSONTokener tokener = new JSONTokener(Reader);
            JSONObject json = new JSONObject(tokener);

            return new String(Files.readAllBytes(Paths.get(filepath)));
            
        } catch (IOException e) {
            System.out.println("Unable to open '" + filepath + "'");
            return null;
        } catch (JSONException e) {
            System.out.println("Invalid format");
        } 
         
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String jsonstring;
        boolean flag = false;

        while (true) {
            System.out.println("\n-i <filename>   :  insert tree from file");
            System.out.println("-j [<filename>] :  print tree in the specified filename using JSON format");
            System.out.println("-d [<filename>] :  print tree in the specified filename using DOT format");
            System.out.println("-c              :  calculate tree using min-max algorithm");
            System.out.println("-p              :  calculate tree using min-max and alpha-beta pruning optimization");
            System.out.println("-q              :  quit this program");
            System.out.print("\n$> ");

            String input = scanner.nextLine();
            String[] tokens = input.split("\\s+");
            if (tokens[0].equals("-i")) {
                System.out.print("\n");
                String filename = tokens[1];
                jsonstring = insertTreeFromFile(filename);
                if (jsonstring == null) {
                    System.out.print("\n\n");
                    continue;
                }
                root = new Tree(jsonstring);
                optimalRoot = new OptimalTree(jsonstring);
                System.out.println("OK");
            } else if (tokens[0].equals("-j")) {
                System.out.print("\n");
                if (tokens.length > 1) {
                    try {
                        File file = new File(tokens[1]);
                        if (file.exists()) {
                           System.out.println("File '" + file + "' already exists");
                           System.out.print("\n\n");
                           continue;
                        }
                        if (!file.createNewFile() || !file.canWrite()) {
                            System.out.println("Unable to write '" + file + "'");
                            System.out.print("\n\n");
                            continue;
                        }
                        if (flag == false)
                            root.toFile(file);
                        else
                            optimalRoot.toFile(file);
                        System.out.println("OK");
                    } catch (IOException e) {
                        System.out.println("Unable to write '" + tokens[1] + "'");
                        System.out.print("\n\n");
                        continue;
                    }
                } else {
                    if (flag == false)
                        System.out.println(root.toString());
                    else
                        System.out.println(optimalRoot.toString());
                }
            } else if (tokens[0].equals("-d")) {
                System.out.print("\n");
                if (tokens.length > 1) {
                    try {
                        File file = new File(tokens[1]);
                        if (file.exists()) {
                           System.out.println("File '" + file + "' already exists");
                           System.out.print("\n\n");
                           continue;
                        }
                        if (!file.createNewFile() || !file.canWrite()) {
                            System.out.println("Unable to write '" + file + "'");
                            System.out.print("\n\n");
                            continue;
                        }
                        if (flag == false)
                            root.toDotFile(file);
                        else
                            optimalRoot.toDotFile(file);
                        System.out.println("OK");
                    } catch (IOException e) {
                        System.out.println("Unable to write '" + tokens[1] + "'");
                        System.out.print("\n\n");
                        continue;
                    }   
                } else {
                    if (flag == false)
                        System.out.println(root.toDotString());
                    else
                        System.out.println(optimalRoot.toDotString());
                }
            } else if (tokens[0].equals("-c")) {
                System.out.print("\n");
                flag = false;
                double ret = 0;
                ret = root.minMax();
                if (ret == 0) {
                    System.out.println("NOK");
                    System.out.print("\n\n");
                    continue;
                }
                ArrayList<Integer> path = root.optimalPath();
                for(int i = 0 ; i < path.size(); i++) {
                    if (i != path.size() - 1)
                        System.out.print(path.get(i) + ", ");
                    else
                        System.out.print(path.get(i));
                }
                System.out.println();
            } else if (tokens[0].equals("-p")) {
                System.out.print("\n");
                flag = true;
                double ret = 0;
                ret = optimalRoot.minMax();
                if (ret == 0) {
                    System.out.println("NOK");
                    System.out.print("\n\n");
                    continue;
                }
                double prunedNodes = optimalRoot.prunedNodes();
                double totalNodes = optimalRoot.countNodes(optimalRoot.root);
                System.out.print("[" + (int)totalNodes + "," + (int)prunedNodes + "] ");
                ArrayList<Integer> path = optimalRoot.optimalPath();
                for(int i = 0 ; i < path.size(); i++) {
                    if (i != path.size() - 1)
                        System.out.print(path.get(i) + ", ");
                    else
                        System.out.print(path.get(i));
                }
                System.out.println();
            } else if (tokens[0].equals("-q")) {
                break;
            } else {
                continue;
            }
            input = null;
            tokens = null;
            System.out.print("\n\n");
        }

        scanner.close();
    }
}
