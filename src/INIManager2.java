/**
 * INIManager.java
 * Manages a standardly formatted *.ini file that can be used in any Java program.
 * It should be noted that the file must be formatted using the .ini standard.
 * This manager can:
 * <ul>
 * <li>Read an INI file</li>
 * <li>Write an INI file</li>
 * <li>Manage the data in an INI file for writing</li>
 * <li>Add headers, keys, or their values</li>
 * <li>Read back values that have been read</li>
 * <li>Overwrite values given a new value and its key name and header</li>
 * </ul>
 *
 * <strong>Update 2.0</strong>
 * <ul>
 * <li>Updated backend data structure to be nested HashMaps</li>
 * </ul>
 *
 * @author	Anthony R Garcia
 * @version 2.50 2016/8/13
 */
package com.zalgebar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class INIManager2 {
	public static boolean DEBUG = false;
	private static boolean INDICES = false;

	private int totalKeys = 0;
	private int keysRead = 0;

	private HashMap<String,HashMap<String,String>> items;

	/**
	 * Creates a new INI file manager. Initializes the headers.
	 */
	public INIManager2() {
		this(new LinkedList<String>());
	}

	/**
	 * Creates a new INI file manager. Initializes with a list of headers.
	 */
	public INIManager2(LinkedList<String> headers) {
		this.items = new HashMap<String,HashMap<String,String>>();
		if(headers==null) return;
		for(String h : headers){
			items.put(h.toUpperCase(),new HashMap<String,String>());
		}
	}

	/**
	 * Creates a new INI file manager. Initializes one header and its keys.
	 */
	public INIManager2(String header, LinkedList<String> keys) {
		this.items = new HashMap<String,HashMap<String,String>>();
		if(header==null) return;
		if(keys==null) return;
		for(String k : keys){
			this.addKey(header.toUpperCase(),k);
		}
	}

	/**
	 * Creates a new header under which to create keys.
	 *
	 * @param header	Name of the header.
	 * @return			Returns false if the header already exists.
	 */
	public boolean addHeader(String header){
		if(DEBUG) System.out.println("addHeader('"+header+"') called");
		header = header.toUpperCase();
		if(this.items.containsKey(header)){
			if(DEBUG) System.out.println("WARNING: Header '" + header +
				"' already exists. Header NOT added!");
			return false;
		}
		this.items.put(header,new HashMap<String,String>());
		return true;
	}

	/**
	 * Creates a new key under the header. If the header does not exist,
	 * it will create one.
	 *
	 * @param header	Name of the header under which this key will be found.
	 * @param key		The key to add that will be listed under the above header.
	 * @return			Returns false if the key already exists under this header.
	 */
	public boolean addKey(String header, String key){
		if(DEBUG) System.out.println("addKey('"+header+"', '"+key+"') called");
		header = header.toUpperCase();
		if(!this.items.containsKey(header)){
			this.addHeader(header);
		}

		//Check if the key already exists
		if(this.items.get(header).containsKey(key)){ //We may assume from the code above that the header exists.
			if(DEBUG) System.out.println(" WARNING: Key '" + key +
				"' already exists. Key NOT added!");
			return false;
		}
		else{
			this.items.get(header).put(key,"");
			this.totalKeys++;
		}

		return true;
	}

	/**
	 * Creates multiple keys from the parameter. Iteratively invokes the
	 * addKey() method.
	 *
	 * @see				#addKey(String, String)
	 *
	 * @param header	Name of the header under which these keys will be found.
	 * @param keys		A LinkedList of the keys.
	 * @return			Returns false if one or more of the keys already exists
	 *					under this header.
	 */
	public boolean addKeys(String header, LinkedList<String> keys){
		if(DEBUG) System.out.println("addKeys('"+header+",["+keys.size()+"keys]') called");
		boolean returnable = true;  //will be set to false if any key already exists
		for(String k : keys){
			if(!this.addKey(header.toUpperCase(), k)){
				returnable = false; //this is where it's set
			}
		}
		return returnable;
	}

	/**
	 * Adds a value under the key and header specified.
	 * OVERWRITES ANY VALUE FOR THIS KEY!
	 *
	 * @param header	Name of the header under which this value will be found.
	 * @param key		Name of the key under which this value will be found.
	 * @param value		Value of the key.
	 */
	public void addValue(String header, String key, String value){
		if(DEBUG) System.out.println("addValue('"+header+"','"+key+
			"','"+value+"') called");
		header = header.toUpperCase();
		this.addKey(header,key);
		this.items.get(header).put(key,value);
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
						System.out.println(" WARNING: File not formatted properly. " +
							"No header before key declaration. Line: "+lineNumber);
					}
					String myKey = myLine.substring(0,indexOfEquals).trim();
					if(DEBUG) System.out.println("  Key: " + myKey);

					String myValue = myLine.substring(indexOfEquals+1,myLine.length()).trim();
					if(DEBUG) System.out.println("  Value: " + myValue);

					this.addValue(myHeader,myKey,myValue);
					keysRead++;
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
			//create a list of headers
			LinkedList<String> headers = new LinkedList<String>(this.items.keySet());
			//iterate through the headers and write them to file
			for(String h : headers){
				StringBuilder sb = new StringBuilder();
				sb.append("[" + h.toUpperCase() + "]");
				if(DEBUG) System.out.println(" "+sb);
				pw.println(sb.toString());
				//create a list of keys for this header
				LinkedList<String> keys = new LinkedList<String>(this.items.get(h).keySet());
				//iterate through the keys and write them to file
				for(String k : keys){
					sb = new StringBuilder();
					sb.append(k);
					sb.append(" = ");
					sb.append(this.items.get(k).toString());
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
	 * @deprecated	As of release 2.0 this method is no longer used.
	 * @return	Returns the index of the specified header.
	 */
	@Deprecated
	private int indexOfHeader(String header){/*
		if(INDICES) System.out.println("indexOfHeader('"+header+"') called");
		int headerIndex = this.headers.indexOf(header);
		if(INDICES) System.out.println(" HeaderIndex: " + headerIndex);*/
		return 0;//return headerIndex;
	}

	/**
	 * Internally used to get the index of a key given the header.
	 *
	 * @return	Returns the index of the specified key under the specified header.
	 */
	@Deprecated
	private int indexOfKey(String header, String key){/*
		if(INDICES) System.out.println("indexOfKey('"+header+"','"+key+"') called");
		int headerIndex = indexOfHeader(header);
		if(headerIndex<0) return -1;
		int keyIndex = this.keys.get(headerIndex).indexOf(key);
		if(INDICES) System.out.println(" KeyIndex: " + keyIndex);*/
		return 0;//return keyIndex;
	}

	/**
	 * Used the get a list of all the headers.
	 *
	 * @return	The LinkedList of headers names.
	 */
	public LinkedList<String> getHeaders(){
		if(DEBUG) System.out.println("getHeaders() called");
		return new LinkedList<String>(this.items.keySet());
	}

	/**
	 * Used to get a list of keys under the specified header.
	 *
	 * @param header	Header of the key names to be returned.
	 * @return			The LinkedList of key names matching the header.
	 *					Returns null if no such header exists.
	 */
	public LinkedList<String> getKeys(String header){
		if(DEBUG) System.out.println("getKeysFromHeader('"+header+"') called");
		header = header.toUpperCase();
		if(!this.items.containsKey(header)) return null;
		return new LinkedList<String>(this.items.get(header).keySet());
	}

	/**
	 * Retreives the value of a key under the header.
	 *
	 * @param header	Header of the key to be returned.
	 * @param key		Name of the key to be returned.
	 * @return			The value of the key matching the header and key name.
	 *					Returns null if no such key or header exists.
	 */
	public String getValue(String header, String key){
		if(DEBUG) System.out.println("getValue('"+header+"','"+key+"') called");
		header = header.toUpperCase();
		if(!this.items.containsKey(header)) return null;
		if(!this.items.get(header).containsKey(key)) return null;
		return this.items.get(header).get(key);
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
	 * keys and headers included in the program. Be sure to add the
	 * headers and keys before reading the file.
	 *
	 * @see		#addHeader(String header)
	 * @see		#addKey(String header, String key)
	 * @see		#addKeys(String header, LinkedList<String> keys)
	 *
	 * @return			Returns true if all keys have been read from file.
	 */
	public boolean verifyFileRead(){
		if(DEBUG) System.out.println("verifyFileRead() called");
		return keysRead>=totalKeys;
	}
}
