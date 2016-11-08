package Parsing;

import java.util.Arrays;
import java.util.Vector;

public class Parser {


	Vector<String> predicates;
	Vector<String> objects;


	public void parse(String request){
		predicates=new Vector<String>();
		objects=new Vector<String>();
		
		String[] temp=request.split("WHERE ");
		String params =new String(temp[1]);
		String temp1[]=params.split(" \\. ");
		System.out.println("Temp1: "+Arrays.toString(temp1));
		for(int i=0;i<temp1.length;i++){
			String temp2[]=temp1[i].split(" ");
			predicates.add(temp2[1]);
			objects.add(temp2[2]);
		}

	}

	public Vector<String> getPredicates() {
		return predicates;
	}


	public Vector<String> getObjects() {
		return objects;
	}
}
