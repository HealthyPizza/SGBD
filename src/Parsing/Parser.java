package Parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import Log.FileLog;

public class Parser {


	Vector<String> predicates;
	Vector<String> objects;
	Vector<String> subjects;
	Vector<String> requests;
	boolean isPath;
	int curindex=0;
	
	public Parser(String file){
		
		requests=new Vector<String>();
		File f= new File(file);
		if(f.isFile()){
			parseFile(f);
		}
		if(f.isDirectory()){
			parseDirectory(f);
		}
			
			//Collections.shuffle(requests);
	}
	
	private void parseDirectory(File files){
		for(File file:files.listFiles()){
			if(file.isFile())
				parseFile(file);
		}
	}
		
	private void parseFile(File f){
		BufferedReader br;
		String request;
		System.out.println("Parsing file "+ f.getName());
		try {
			br = new BufferedReader(new FileReader(f));
			while ((request = br.readLine()) != null) {
				requests.add(request);//
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void resetIndex(){
		curindex=0;
	}
	
	public boolean parse() throws Exception{
		if(curindex<requests.size()){
			parse(requests.get(curindex++));
			return true;
		}
		return false;
	}
	
	public void parse(String request) throws Exception{
		predicates=new Vector<String>();
		objects=new Vector<String>();
		subjects=new Vector<String>();
		String[] temp=request.split("WHERE ");
		if(temp.length!=2)
			throw new Exception("Error WHERE clause");
		String params =new String(temp[1]);
		String temp1[]=params.split(" \\. ");
		for(int i=0;i<temp1.length;i++){
			String temp2[]=temp1[i].split(" ");
			if(temp2.length!=3)
				throw new Exception("Error WHERE clause members");
			subjects.addElement(temp2[0]);
			predicates.add(temp2[1].replaceAll(" ", ""));
			objects.add(temp2[2].replaceAll(" ", ""));
		}
		FileLog.writeLog(request);
		checkPath();

	}
	
	public void checkPath(){
		isPath=false;
		for(int i=0;i<objects.size()-1;i++){
			if(objects.get(i).contains("?")){
				if(subjects.get(i+1).compareTo(objects.get(i))==0){
					isPath=true;
				}
				else{
					isPath=false;
					break;
				}
			}
		}
	}

	public boolean isPath(){
		return isPath;
	}
	
	public Vector<String> getPredicates() {
		return predicates;
	}

	public Vector<String> getObjects() {
		return objects;
	}
	public Vector<String> getSubjects() {
		return subjects;
	}

}
