import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

/**
 * @author Willie Ruemmele
 * @author Joss Chapman
 * @author Adrian Estrada
 * @author Torin Johnson
 * @author Bao Nguyen 
 *
 */
public class AuraParser extends Parser {

    /**
     * Create a Parser object to parse an Aura file
     * @param file The Aura file
     */
    public AuraParser(File file){
        super(file);
    }

    @Override
    public HashMap<String,HashSet<String>> getDependencies() {
    	//first get the name of the controller
        String controllerName = getControllerName();
        HashSet<String> functionCalls = new HashSet<>();

        //use the regex to get any functionName in the form {!c.functionName}
        String pattern = "\\{!c\\..*}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(super.readFileAsString());

        //make a list of controllerName.functionName
        while(m.find()){
            functionCalls.add(controllerName + "." + m.group(0).substring(4, m.group(0).length()-1)); //TODO: remove hardcoding or not
        }
        for(String s : functionCalls){
            System.out.println(controllerName+"."+s);
        }
        HashMap<String,HashSet<String>> result = new HashMap<String,HashSet<String>>();
        result.put(file.getAbsolutePath(), functionCalls);
        return result;
    }

    private String getControllerName(){
    	//use the regex to get any controllerName in the form controller="controllerName"
        String pattern = "controller=\".*\"";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(super.readFileAsString());

        if(m.find()){
            return m.group(0).substring(12,m.group(0).length()-1); //TODO; find a way to not hard code these values?
        }else{
            return "";
        }
    }

}
