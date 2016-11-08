package Parsing;

import java.util.Arrays;
import java.util.Vector;

public class Parser {


	Vector<String> predicates;
	Vector<String> objects;
	Vector<String> subjects;

	public void parse(String request){
		predicates=new Vector<String>();
		objects=new Vector<String>();
		subjects=new Vector<String>();
		String[] temp=request.split("WHERE ");
		String params =new String(temp[1]);
		String temp1[]=params.split(" \\. ");
		System.out.println("Temp1: "+Arrays.toString(temp1));
		for(int i=0;i<temp1.length;i++){
			String temp2[]=temp1[i].split(" ");
			subjects.addElement(temp2[0]);
			predicates.add(temp2[1]);
			objects.add(temp2[2]);
		}

	}
	
	public void test(){
		boolean path=true;
		for(int i=0;i<objects.size()-1;i++){
			if(objects.get(i).contains("?")){
				if(subjects.get(i+1).compareTo(objects.get(i))!=0){
					path=false;
				}
			}
		}
		if(path){
			System.out.println("ITS A PATH");
		}
		else{
			System.out.println("IT S NOT A PATH");
		}
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

	
	public static void main(String[] args) {
		Parser p=new Parser();
		p.parse("SELECT ?x WHERE ?x takesCourse ?x1 . ?x1 #name ?x2 . ?x2 mabite something");
		
		System.out.println(p.getSubjects());
		System.out.println(p.getPredicates());
		System.out.println(p.getObjects());
		
		p.test();
	}
}
