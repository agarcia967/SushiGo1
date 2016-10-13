/**
 * INIManager.java
 * Manages a standardly formatted *.ini file that can be used in any Java program.
 * It should be noted that the file must be formatted using the .ini standard.
 * This manager can:
 * <ul>
 * <li>Read an INI file</li>
 * <li>Write an INI file</li>
 * <li>Manage the data in an INI file for writing</li>
 * <li>Add headers, variables, or their values</li>
 * <li>Read back values that have been read</li>
 * <li>Overwrite values given a new value and its variable name and header</li>
 * </ul>
 *
 * @author	Anthony R Garcia
 * @version 1.00 2016/7/10
 */
package com.agarcia967;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

public class INIManager1 {
	public static boolean DEBUG = false;
	private static boolean INDICES = false;

	private int totalVariables = 0;
	private int variablesRead = 0;

	private LinkedList<String> headers;
	private LinkedList<LinkedList<String>> variables;
	private LinkedList<LinkedList<String>> values;

	/**
	 * Creates a new INI file manager. Initializes the headers.
	 */
	public INIManager1() {
		this(new LinkedList<String>());
	}

	/**
	 * Creates a new INI file manager. Initializes with a list of headers.
	 */
	public INIManager1(LinkedList<String> headers) {
		this.headers = new LinkedList<String>(headers);
		this.variables = new LinkedList<LinkedList<String>>();
		this.values = new LinkedList<LinkedList<String>>();
	}

	/**
	 * Creates a new INI file manager. Initializes one header and its variables.
	 */
	public INIManager1(String header, LinkedList<String> variables) {
		header = header.toUpperCase();
		this.headers = new LinkedList<String>();
		this.variables = new LinkedList<LinkedList<String>>();
		this.values = new LinkedList<LinkedList<String>>();
		this.addHeader(header);
		for(int i = 0; i<variables.size(); i++){
			this.addVariable(header,variables.get(i));
		}
	}

	/**
	 * Creates a new header under which to create variables.
	 *
	 * @param header	Name of the header.
	 * @return			Returns false if the header already exists.
	 */
	public boolean addHeader(String header){
		if(DEBUG) System.out.println("addHeader('"+header+"') called");
		header = header.toUpperCase();
		if(this.headers.contains(header)){
			if(DEBUG) System.out.println("WARNING: Header '" + header +
				"' already exists. Header NOT added!");
			return false;
		}
		this.headers.addLast(header);
		this.variables.addLast(new LinkedList<String>());
		this.values.addLast(new LinkedList<String>());
		return true;
	}

	/**
	 * Creates a new Variable under the header. If the header does not exist,
	 * it will create one.
	 *
	 * @param header	Name of the header under which this variable will be found.
	 * @param variable	The variable to add that will be listed under the above header.
	 * @return				Returns false if the variable already exists under this header.
	 */
	public boolean addVariable(String header, String variable){
		if(DEBUG) System.out.println("addVariable('"+header+"', '"+variable+"') called");
		header = header.toUpperCase();
		int headerIndex = this.indexOfHeader(header);
		if(headerIndex<0){
			this.addHeader(header);
			if(INDICES) System.out.print(" New ");
			headerIndex = this.indexOfHeader(header);
		}

		//Check if the variable already exists
		int variableIndex = this.indexOfVariable(header,variable);
		if(variableIndex>=0){
			if(DEBUG) System.out.println(" WARNING: Variable '" + variable +
				"' already exists. Variable NOT added!");
			return false;
		}
		else{
			this.variables.get(headerIndex).addLast(variable);
			this.values.get(headerIndex).addLast("");
			this.totalVariables++;
		}

		return true;
	}

	/**
	 * Creates multiple variables from the parameter. Iteratively invokes the
	 * addVariable() method.
	 *
	 * @see				#addVariable(String, String)
	 *
	 * @param header	Name of the header under which these variables will be found.
	 * @param variables	A LinkedList of the variables.
	 * @return			Returns false if one or more of the variables already exists
	 *					under this header.
	 */
	public boolean addVariables(String header, LinkedList<String> variables){
		if(DEBUG) System.out.println("addVariables('"+header+"') called");
		header = header.toUpperCase();
		for(int i = 0; i<variables.size(); i++){
			if(!this.addVariable(header, variables.get(i))) return false;
		}
		return true;
	}

	/**
	 * Adds a value under the variable and header specified. OVERWRITES ANY DATA
	 * UNDER THIS VARIABLE!
	 *
	 * @param header	Name of the header under which this value will be found.
	 * @param variable	Name of the variable under which this value will be found.
	 * @param value		Value of the variable.
	 */
	public void addValue(String header, String variable, String value){
		if(DEBUG) System.out.println("addValue('"+header+"','"+variable+
			"','"+value+"') called");
		header = header.toUpperCase();
		this.addVariable(header,variable);
		int headerIndex = this.indexOfHeader(header);
		int variableIndex = this.indexOfVariable(header,variable);
		this.values.get(headerIndex).set(variableIndex,value);
	}

	/**
	 * Using {@link FileInputStream} and {@link Scanner} reads a file given the
	 * file name as the parameter.
	 *
	 * @see				java.io.FileInputStream
	 * @see				java.io.Scanner
	 *
	 * @param fileName	Full path of the file to be read.
	 * @return			Returns true if the file read was successful.
	 */
	public boolean readFile(String fileName){
		if(DEBUG) System.out.println("readFile('"+fileName+"') called");
		if(!fileName.endsWith(".ini")) fileName = fileName+".ini";
		FileInputStream fileIn;

		try{
			fileIn = new FileInputStream(fileName);
			Scanner scanner = new Scanner(fileIn);
			int lineNumber = 0;
			boolean hasHeader = false;
			String myHeader = "";

			while(scanner.hasNext()){
				String myLine = scanner.nextLine().trim();
				lineNumber++;
				int indexOfEquals = myLine.indexOf("=");
				if(DEBUG) System.out.println(" Line: " + myLine);

				if(myLine.startsWith("[") && myLine.endsWith("]")){
					myHeader = myLine.substring(1,myLine.length()-1).toUpperCase();
					if(DEBUG) System.out.println("  Header: " + myHeader);
					this.addHeader(myHeader);
					hasHeader = true;
				}
				else if(indexOfEquals>0){
					if(!hasHeader){
						if(DEBUG) System.out.println(" WARNING: File not formatted properly. " +
							"No header before variable declaration. Line: "+lineNumber);
					}
					String myVariable = myLine.substring(0,indexOfEquals).trim();
					if(DEBUG) System.out.println("  Variable: " + myVariable);

					String myValue = myLine.substring(indexOfEquals+1,myLine.length()).trim();
					if(DEBUG) System.out.println("  Value: " + myValue);

					this.addValue(myHeader,myVariable,myValue);
					variablesRead++;
				}
				else if(myLine.equals("")){
					//Do nothing if a blank line is found
				}
				else{
					if(DEBUG) System.out.println(" WARNING: File not formatted properly. Line: "+lineNumber);
				}
			}
			scanner.close();
			fileIn.close();
		}
		catch(FileNotFoundException e){
			System.out.println(" ERROR: File 404 - " + e.toString());
			return false;
		}
		catch(IOException e){
			System.out.println(" ERROR: File read error - " + e.toString());
			return false;
		}
		return true;
	}

	/**
	 * Using {@link FileOutputStream} and {@link PrintWriter}, writes a file given the
	 * file name as the parameter.
	 *
	 * @see				java.io.FileOutputStream
	 * @see				java.io.PrintWriter
	 *
	 * @param fileName	Full path of the file to be written to.
	 * @return			Returns true if the file write was successful.
	 */
	public boolean writeFile(String fileName){
		FileOutputStream fileOut;
		if(DEBUG) System.out.println("writeFile('"+fileName+"') called");
		if(!fileName.endsWith(".ini")) fileName = fileName+".ini";
		try{
			fileOut = new FileOutputStream(fileName);
			PrintWriter pw = new PrintWriter(fileOut);
			for(int h = 0; h<this.headers.size(); h++){
				StringBuilder sb = new StringBuilder();
				sb.append("[" + this.headers.get(h) + "]");
				if(DEBUG) System.out.println(" "+sb);
				pw.println(sb.toString());
				for(int v = 0; v<this.variables.get(h).size(); v++){
					sb = new StringBuilder();
					sb.append(this.variables.get(h).get(v).toString());
					sb.append(" = ");
					sb.append(this.values.get(h).get(v).toString());
					if(DEBUG) System.out.println(" "+sb);
					pw.println(sb.toString());
				}
			}
			pw.close();
			fileOut.close();
		}
		catch(FileNotFoundException e){
			System.out.println("ERROR: File 404 - " + e.toString());
			return false;
		}
		catch(IOException e){
			System.out.println("ERROR: Write error - " + e.toString());
			return false;
		}
		return true;
	}

	/**
	 * Internally used to get the index of a header.
	 *
	 * @return	Returns the index of the specified header.
	 */
	private int indexOfHeader(String header){
		if(INDICES) System.out.println("indexOfHeader('"+header+"') called");
		int headerIndex = this.headers.indexOf(header);
		if(INDICES) System.out.println(" HeaderIndex: " + headerIndex);
		return headerIndex;
	}

	/**
	 * Internally used to get the index of a variable given the header.
	 *
	 * @return	Returns the index of the specified variable under the specified header.
	 */
	private int indexOfVariable(String header, String variable){
		if(INDICES) System.out.println("indexOfVariable('"+header+"','"+variable+"') called");
		int headerIndex = indexOfHeader(header);
		if(headerIndex<0) return -1;
		int variableIndex = this.variables.get(headerIndex).indexOf(variable);
		if(INDICES) System.out.println(" VariableIndex: " + variableIndex);
		return variableIndex;
	}

	/**
	 * Used the get a list of all the headers.
	 *
	 * @return	The LinkedList of headers names.
	 */
	public LinkedList<String> getHeaders(){
		if(DEBUG) System.out.println("getHeaders() called");
		return new LinkedList<String>(headers);
	}

	/**
	 * Used to get a list of variables under the specified header.
	 *
	 * @param header	Header of the variable names to be returned.
	 * @return			The LinkedList of variable names matching the header.
	 */
	public LinkedList<String> getVariableNamesFromHeader(String header){
		if(DEBUG) System.out.println("getVariableNamesFromHeader('"+header+"') called");
		header = header.toUpperCase();
		int headerIndex = this.indexOfHeader(header);
		return variables.get(headerIndex);
	}

	/**
	 * Retreives the value of a variable under the header.
	 *
	 * @param header	Header of the variable to be returned.
	 * @param variable	Name of the variable to be returned.
	 * @return			The value of the variable matching the header and variable name.
	 *					Returns null if no such variable or header exists.
	 */
	public String getValue(String header, String variable){
		if(DEBUG) System.out.println("getValue('"+header+"','"+variable+"') called");
		header = header.toUpperCase();
		int headerIndex = this.indexOfHeader(header);
		if(headerIndex<0) return null;
		int variableIndex = this.indexOfVariable(header,variable);
		if(variableIndex<0) return null;
		return this.values.get(headerIndex).get(variableIndex);
	}

	/**
	 * Toggles whether to activate debug mode or not.
	 *
	 * @return	The new status of debug mode
	 */
	public boolean toggleDebug(){
		if(DEBUG) System.out.println("toggleDebug() called");
		DEBUG=(!DEBUG);
		return DEBUG;
	}

	/**
	 * Used when reading a file after already having declared ("added") the
	 * variables and headers included in the program. Be sure to add the
	 * headers and variables before reading the file.
	 *
	 * @see		#addHeader(String header)
	 * @see		#addVariable(String header, String variable)
	 * @see		#addVariables(String header, LinkedList<String> variables)
	 *
	 * @return			Returns true if all variables have been read from file.
	 */
	public boolean verifyFileRead(){
		if(DEBUG) System.out.println("verifyFileRead() called");
		return variablesRead>=totalVariables;
	}
}
