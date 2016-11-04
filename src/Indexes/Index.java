package Indexes;
	
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import Dictionnary.Dictionnary;
import Log.FileLog;

public class Index {


	protected HashMap<Integer, HashMap<Integer,Vector<Integer>>> index= new HashMap<Integer, HashMap<Integer,Vector<Integer>>>();


	public HashMap<Integer, Vector<Integer>> getSecondLevel(Integer i){
			return index.get(i);
	}

	public Vector<Integer> getThirdLevel(Integer firstI,Integer secondI){
		return getSecondLevel(firstI).get(secondI);
	}

	public HashMap<Integer, HashMap<Integer,Vector<Integer>>> getAll(){
		return index;
	}

	public void insert(Integer i1,Integer i2,Integer i3){

		FileLog.write("<"+i1+","+i2+","+i3+">");
		HashMap<Integer,Vector<Integer>> lvl1 = getSecondLevel(i1);
		if(lvl1==null){
			Vector<Integer> vect = new Vector<Integer>();
			vect.add(i3);
			HashMap<Integer, Vector<Integer>> secondLvl = new HashMap<Integer, Vector<Integer>>();
			secondLvl.put(i2, vect);
			index.put(i1, secondLvl);
			return;
		}
		Vector<Integer> lvl2 = getThirdLevel(i1, i2);
		if (lvl2==null){
			Vector<Integer> vect = new Vector<Integer>();
			vect.add(i3);
			lvl1.put(i2, vect);
			return;
		}
		if (!lvl2.contains(i3)){
			lvl2.add(i3);
			return;
		}
	}
	
	
	@Override
	public String toString() {
		for(Entry<Integer, HashMap<Integer, Vector<Integer>>> entry:index.entrySet()){
			System.out.println(entry.toString());
		}
		return null;
	}

	public Vector<String> getTriplets(Integer i){
		long startTime = System.currentTimeMillis();
		Vector<String> res = new Vector<String>();		
		for(Entry<Integer, Vector<Integer>> entry:getSecondLevel(i).entrySet()){
			for(Integer entry1:entry.getValue()){
				String str=new String(i+"-");
				str+=entry.getKey()+"-"+entry1;
				res.add(str);
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("-Flo- Temps generation liste des cles " + (endTime - startTime) + " milliseconds");
		return res;
	}
	
	public Vector<String> getTriplets(Integer i,Integer i1){
		Vector<String> res = new Vector<String>();		
		for(Integer integer:getThirdLevel(i, i1)){
				String str=new String(i+"|"+i1);
				str+="|"+integer;
				res.add(str);
		}
		return res;
	}
	
	/*public Integer[][] getTriplet(Integer i1) throws TripletNotFoundException
	{
		long startTime = System.currentTimeMillis();
		HashMap<Integer, Vector<Integer> > map = getSecondLevel(i1);
		if(map == null)
			throw new TripletNotFoundException();
		Set<Integer> keys = map.keySet();
		Collection<Vector<Integer> > vectors = map.values();
		int size = 0;
		for(Vector<Integer> v: vectors)
		size = size + v.size();
		Integer triplets[][] =  new Integer[size][3];
		int t,i2;
		t = 0;
		for(Integer k: keys)
		{
			i2 = k;
			Vector<Integer> vect = getThirdLevel(i1, i2);
			for(Integer v: vect)
			{
				triplets[t][0] = i1;
				triplets[t][1] = i2;
				triplets[t][2] = v;
				t++;
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("-Manu- Temps generation liste des cles " + (endTime - startTime) + " milliseconds");
		return triplets;
	}  

	public Integer[][] getTriplet(Integer i1, Integer i2)
	{
		Vector<Integer> vect = getThirdLevel(i1, i2);
		if(vect == null)
			return null;
		Integer triplets[][] =  new Integer[vect.size()][3];
		int t;
		t = 0;
		for(Integer v: vect)
		{
			triplets[t][0] = i1;
			triplets[t][1] = i2;
			triplets[t][2] = v;
			t++;
		}
		return triplets;
	}  

	
	public static void main(String[] args) {
		FileLog.createLog();
		Dictionnary d=new Dictionnary();
		d.put("Alice");
		d.put("Pierre");
		d.put("Michel");
		d.put("knows");
		d.put("hates");
		System.out.println(d.toString());
		Index spo = new Index();
		spo.insert(1, 4, 2);
		spo.insert(1, 4, 3);
		spo.insert(3, 4, 2);
		spo.insert(1,5,3);
		spo.toString();
		System.out.println(spo.getTriplets(1).toString());
		System.out.println(spo.getTriplets(1,5).toString());

		try {
			System.out.println(d.tripletsRDF(spo.getTriplet(1)));
		} catch (TripletNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileLog.endLog();
	}

/*	public abstract Integer getSubject();
	public abstract Integer getObject();
	public abstract Integer getPredicate();*/
}
