import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Willie Ruemmele
 * @author Joss Chapman
 * @author Adrian Estrada
 * @author Torin Johnson
 * @author Bao Nguyen
 */

public class JsParser extends Parser {

    public JsParser(File file){
        super(file);
    }

    @Override
    public HashMap<String,HashSet<String>> getDependencies() {

        ArrayList<String> bodies = getFunctionBodies(super.readFileAsString());
        //add any function calls in a function definition to it's dependencies
        HashMap<String,HashSet<String>> dependencies = new HashMap<String,HashSet<String>>();
        for(String body : bodies){
        	String functionName = body.substring(body.indexOf(' ')+1, body.indexOf('('));
        	HashSet<String> functionDep = getFunctionDependencies(body);
        	functionDep.remove(functionName);
        	dependencies.put(functionName, functionDep);
        }
        //add all of the requires to the dependencies for each function
        HashSet<String> requires = getRequires();
        for (String key : dependencies.keySet()) {
        	for (String require : requires) {
        		dependencies.get(key).add(require);
        	}
        }   
        return dependencies;
    }
    
    public void getDefenitions() {
    	HashSet<String> definedFunctions = new HashSet<>();
    	//regex, searching, match, put into list for function definitions
        String definitions = "\\sfunction .*\\(";
        Pattern r = Pattern.compile(definitions);
        Matcher m = r.matcher(super.readFileAsString());
        while(m.find()){
            definedFunctions.add(m.group(0).substring(9, m.group(0).length()-1));
        }
        for(String s : definedFunctions){
            System.out.println(file.getName()+" DEFINES "+s); //.substring(9, s.length()-1)
        }
    }
    
    /**
     * @return A set of requires for this file
     */
    private HashSet<String> getRequires(){
        HashSet<String> requiredFunctions = new HashSet<>();        
    	//regex, searching, match, put into list for require calls
        String require = "require.*\\(.*\\)";
        Pattern r = Pattern.compile(require);
        Matcher m = r.matcher(super.readFileAsString());
        while(m.find()){
            requiredFunctions.add(m.group(0).substring(9, m.group(0).length()-2));
        }
        //ArrayList<JsParser> JS = new ArrayList<>();
        for(String s : requiredFunctions){
            System.out.println(file.getName()+" REQUIRES "+s); //.substring(9, s.length()-1)
            /*
            File parent = file.getParentFile();
            System.out.println(parent.getParent());
            System.out.println("something " + parent.getAbsolutePath()+s.substring(s.indexOf('/')+1, s.lastIndexOf('\''))+".js");
            JS.add(new JsParser(new File(file.getParentFile().getAbsolutePath()+s.substring(s.indexOf('/')+1, s.lastIndexOf('\''))+".js")));+
            .getAbsoluteFile
             */
        }
        return requiredFunctions;
    }
    
    /**
     * @param body The function definition to find dependencies in
     * @return A set of dependencies for that function
     */
    private HashSet<String> getFunctionDependencies(String body){
        HashSet<String> functionCalls = new HashSet<>();
    	//TODO: issue this regex matches function calls and definitions, idk if that's good or bad
        String functions = "\\w+\\([^\\)]*\\)(\\.[^\\)]*\\))?";
        Pattern r = Pattern.compile(functions);
        
    	//regex, searching, match, put into list for function calls
        Matcher m = r.matcher(body);
        while(m.find()){
        	String functionCall = m.group(0).substring(0, m.group(0).indexOf('('));
        	if (isValidFunctionName(functionCall)) {
        		functionCalls.add(functionCall);
        	}
        }
        return functionCalls;
    }
    
    /**
     * @param code The code to extract functions from
     * @return A list of Strings, each is a function starting with "function" and ending with "}"
     */
    private ArrayList<String> getFunctionBodies(String code){
    	ArrayList<String> bodies = new ArrayList<>();
    	String definitions = "\\sfunction .*\\(";
        Pattern r = Pattern.compile(definitions);
        Matcher m = r.matcher(super.readFileAsString());
    	while (m.find()) {
    		int startIndex = m.start();
    		//starting with the first {, make sure the { and } in the function balance
    		int countBraces = 1;
    		int k = code.indexOf("{", startIndex+9)+1;
    		while (true) {
    			if (code.charAt(k)=='{') countBraces++;
    			if (code.charAt(k)=='}') countBraces--;
    			if (countBraces==0) break;
    			k++;
    		}
    		int endIndex=k+1;
    		//add the function text to the list
    		bodies.add(code.substring(startIndex, endIndex));

    	}
    	return bodies;
    }
    
    /**
     * @param functionName A potential function name
     * @return Whether or not that is a valid function name
     */
    private boolean isValidFunctionName(String functionName) {
    	String[] keywords = {"break", "case", "catch", "continue", "debugger",
                "default", "delete", "do", "else", "finally", "for", "function",
                "if", "in", "instanceof", "new", "return", "switch", "this",
                "throw", "try", "typeof", "var", "void", "while", "with", "true",
                "false", "null"};
    	for (String k : keywords) {
    		if (k.equals(functionName)) {
    			return false;
    		}
    	}
    	return true;
    }

}
