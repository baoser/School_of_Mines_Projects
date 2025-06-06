/**
 * @Author Bao Nguyen
 * @author Cassandra Vandeventer
 * 
 * CSCI 306-A
 * C13A-2
 * 
 */
package clueGame;

public class BadConfigFormatException extends RuntimeException {
	//extends RuntimeException is necessary because it is extending Exception for an 
	//unchecked exception
	//Custom exception classes will typically have a default constructor and a constructor that
	//takes a string, which ours does: "Bad configuration format"

	private String description;
	
	@Override
	public String toString(){
		return description;
	}

	public BadConfigFormatException(String description) {
		super(description);
	}
	public BadConfigFormatException () {
		super("Bad configuration format");
	}
}
