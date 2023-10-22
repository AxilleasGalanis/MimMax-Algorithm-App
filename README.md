# MimMax Algorithm Application
This application showcases the MinMax algorithm enhanced by alpha-beta pruning. To execute the program make sure to download and import the [org.json](https://repo1.maven.org/maven2/org/json/json/20230227/json-20230227.jar) library.

# Compilation and Execution
First of all download the the [org.json](https://repo1.maven.org/maven2/org/json/json/20230227/json-20230227.jar) library. Then
compile the file using the command:
```
javac -cp .:filepath/to/json/file/json-simple-1.1.1.jar MinMax.java 
```
Execute the program using the command:
```
java -cp .:filepath/to/json/file/json-simple-1.1.1.jar MinMax
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

● **-j \[<filename>\] :**
