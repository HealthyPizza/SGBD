package Parsing;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Vector;

public class Parser {


	Vector<String> predicates;
	Vector<String> objects;
	Vector<String> subjects;
	boolean isPath;
	
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

	}
	
	public void test(){
		isPath=false;
		for(int i=0;i<objects.size()-1;i++){
			if(objects.get(i).contains("?")){
				System.out.println(subjects.get(i+1)+ " " + objects.get(i));
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
