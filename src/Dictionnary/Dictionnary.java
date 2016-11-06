package Dictionnary;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Log.FileLog;


public class Dictionnary {

	protected HashMap<Integer,String> dataIndexes;
	protected HashMap<String,Integer> dataValues;
	Pattern p;
	Matcher m;
	
	public Dictionnary() {
		// TODO Auto-generated constructor stub
		dataIndexes=new HashMap<Integer,String>();
		dataValues=new HashMap<String,Integer>();
	}
	
	public int put(String o){
		int newIndex = -1;
		if(!dataValues.containsKey(o)){
			newIndex=dataIndexes.size()+1;
			FileLog.logInsert(newIndex +" - "+ o);
			dataIndexes.put(newIndex, o);
			dataValues.put(o,newIndex);
		}
		return newIndex;
	}
	public int size(){
		return dataValues.size();
	}
	
	public Integer getIndexOf(String s){
			return dataValues.get(s);
	}
	
	public Vector<Integer> getIndexesOf(String regexp){
		Vector<Integer> results=new Vector<Integer>();
		p = Pattern.compile(regexp);
		for(String key:dataValues.keySet()){
			m = p.matcher(key);
			while(m.find()){
				results.add(dataValues.get(key));
			}
		}
		return results;
	}
	
	public String getValueOf(Integer i){
			return dataIndexes.get(i);
	}
	
	@Override
	public String toString() {
		return "Dictionnary [dataIndexes=" + dataIndexes + ", dataValues=" + dataValues + "]";
	}
	
	public String tripletsRDF(Integer[][] triplets)
	
	{
		long startTime = System.currentTimeMillis();
	    String s = "";
	    for(int i = 0; i < triplets.length; i++)
	        s = s + getValueOf(triplets[i][0]) + " " + getValueOf(triplets[i][1]) + " " + getValueOf(triplets[i][2]) + "\n";
	    long endTime = System.currentTimeMillis();
		System.out.println("-Manu- Temps generation resultats " + (endTime - startTime) + " milliseconds");
	    return s;
	}
	
	public String tripletsRDF(Vector<String> triplets){
		long startTime = System.currentTimeMillis();
		String s="";
		for(String triplet:triplets){
			String[] spo= triplet.split("-");
			s+=getValueOf(Integer.valueOf(spo[0]))+" "+getValueOf(Integer.valueOf(spo[1]))+" "+getValueOf(Integer.valueOf(spo[2]))+"\n";
		}
		long endTime = System.currentTimeMillis();
		System.out.println("-Flo- Temps generation resultats " + (endTime - startTime) + " milliseconds");
		return s;
	}

	
	public static void main(String[] args) {
		Dictionnary d = new Dictionnary();
		String value=new String("Test");
		String value1=new String("Test1");
		String value2=new String("Test2");
		//d.put(value).put(value1).put(value2).put(value1).put(value2);
		System.out.println(d.toString());
		System.out.println("Value of 2: "+d.getValueOf(2));
		System.out.println("Index of Test2: "+d.getIndexOf("Test2"));
	}
}
