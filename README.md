# MimMax Algorithm Application
This application showcases the MinMax algorithm enhanced by alpha-beta pruning. To execute the program make sure to download and import the [org.json](https://repo1.maven.org/maven2/org/json/json/20230227/json-20230227.jar) library. Furthermore, sample trees in JSON format are available within the "Samples" folder.

# Compilation and Execution
First of all download the the [org.json](https://repo1.maven.org/maven2/org/json/json/20230227/json-20230227.jar) library. Then
compile the file using the command:
```
javac -cp .:filepath/to/json/file/json-simple-1.1.1.jar MinMax/MinMax.java 
```
Execute the program using the command:
```
java -cp .:filepath/to/json/file/json-simple-1.1.1.jar MinMax/MinMax
```
# Usage
The application on repeat prints the options menu below:
```
-i <filename> : insert tree from file
-j [<filename>] : print tree in the specified filename using JSON format
-d [<filename>] : print tree in the specified filename using DOT format
-c : calculate tree using min-max algorithm
-p : calculate tree using min-max and alpha-beta pruning optimization
-q : quit this program

$>
```
● **-i \<filename> :** The program parses and retrieves the data from the file located at <filepath>. if
the file is read successfully, the message "OK" is printed.

● **-j \[\<filename>\] :** Saves the tree's contents in JSON format to the file at \<filepath>. The presence of the \<filepath> file is optional. If it is not specified, the output is directed to stdout. 

● **-d \[\<filename>\] :** Outputs the tree's contents to the \<filepath> file in a format compatible with the dot program from the [graphviz](https://dreampuf.github.io/GraphvizOnline/#digraph%20G%20%7B%0A%0A%20%20subgraph%20cluster_0%20%7B%0A%20%20%20%20style%3Dfilled%3B%0A%20%20%20%20color%3Dlightgrey%3B%0A%20%20%20%20node%20%5Bstyle%3Dfilled%2Ccolor%3Dwhite%5D%3B%0A%20%20%20%20a0%20-%3E%20a1%20-%3E%20a2%20-%3E%20a3%3B%0A%20%20%20%20label%20%3D%20%22process%20%231%22%3B%0A%20%20%7D%0A%0A%20%20subgraph%20cluster_1%20%7B%0A%20%20%20%20node%20%5Bstyle%3Dfilled%5D%3B%0A%20%20%20%20b0%20-%3E%20b1%20-%3E%20b2%20-%3E%20b3%3B%0A%20%20%20%20label%20%3D%20%22process%20%232%22%3B%0A%20%20%20%20color%3Dblue%0A%20%20%7D%0A%20%20start%20-%3E%20a0%3B%0A%20%20start%20-%3E%20b0%3B%0A%20%20a1%20-%3E%20b3%3B%0A%20%20b2%20-%3E%20a3%3B%0A%20%20a3%20-%3E%20a0%3B%0A%20%20a3%20-%3E%20end%3B%0A%20%20b3%20-%3E%20end%3B%0A%0A%20%20start%20%5Bshape%3DMdiamond%5D%3B%0A%20%20end%20%5Bshape%3DMsquare%5D%3B%0A%7D) suite for graph visualization. The presence of the \<filepath> file is optional. If it is not specified, the output is directed to stdout. 

● **-c :** The MinMax algorithm is computed for the tree. Provides the path of nodes from the root to the leaves, leading to the optimal selection determined by the algorithm.

● **-p :** The MinMax algorithm with the alpha-beta pruning enhancement is computed for the tree. Presents the ratio of total nodes to pruned nodes \[Total, Pruned\], and also provides the path of nodes from the root to the leaves, which leads to the optimal selection as determined by the algorithm.

● **-c :** Quits the program.
