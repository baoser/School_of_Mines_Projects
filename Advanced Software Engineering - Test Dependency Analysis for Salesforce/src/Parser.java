import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
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
public abstract class Parser {

    protected File file;

    /**
     * Create an object to parse a specific file
     * @param file
     */
    public Parser(File file){
        this.file=file;
    }

    /**
     * @return A set of the function names for all functions that this calls 
     */
    public abstract HashMap<String,HashSet<String>> getDependencies();

    /**
     * @return A string containing the contents of the file
     */
    public String readFileAsString()
    {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        }catch (IOException e){
            System.err.println("File not found"+e);
        }
        return data;
    }
}
