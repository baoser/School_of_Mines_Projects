import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Willie Ruemmele
 * @author Joss Chapman
 * @author Adrian Estrada
 * @author Torin Johnson
 * @author Bao Nguyen 
 *
 */
public class Main {

	private static HashMap<String, HashSet<String>> adjList = new HashMap<>();
	
	public static void main(String[] args) {
		//get args
		//parse arg list for file type
		//call file type creation parser
		File folder = new File(args[0]);
		readInputFolder(folder);
		for (String key : adjList.keySet()) {
			System.out.print(key + "\t");
			for (String value : adjList.get(key)) {
				System.out.print(value + ", ");
			}
			System.out.println("\n");
		}
	}

	//TODO: function class to hold everything?
	//TODO: refactoring output?
	//TODO: add comments??????
	//TODO: add a test or two if we want to
	//TODO: traverse files?????? - Matt
	//TODO: double on dependencies? the yes no maybe never example
	//TODO: remove the require from each function and put it in a FILE1 requires FILE2
	//TODO: update kanban, add these to it?

	/**
	 * Recursively analyze all files in a given folder to build the map
	 * @param folder The folder to analyze
	 */
	public static void readInputFolder(File folder){
		File[] listOfFiles = folder.listFiles();

		//for each file or folder in the folder
		for (File file : listOfFiles){
			if (file.isFile()){
				//if it's a file determine the file type, create a parser, and get the dependencies
				String filename = file.getName();
				String filetype = filename.substring(filename.indexOf('.')+1);
				Parser parser;
				switch (filetype){
					case "cmp":
						parser = new AuraParser(file);
						break;
					case "js":
						parser = new JsParser(file);
						break;
					default:
						continue;
				}
				adjList.putAll(parser.getDependencies());
			}else if(file.isDirectory()){
				//if it's a folder call this method on it
				readInputFolder(file);
			}
		}
	}


}
