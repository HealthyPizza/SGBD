package Indexes;

import java.io.ObjectInputStream.GetField;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import Dictionnary.Dictionnary;

public final class IndexManager {


	/*SPO SOP OPS OSP POS PSO*/
	private static Index spo;
	private static Index sop;
	private static Index ops;
	private static Index osp;
	private static Index pos;
	private static Index pso;
	private static int size=0;

	public static void initIndexes(){
		spo=new Index();	sop=new Index();	ops=new Index();
		osp=new Index();	pos=new Index();	pso=new Index();
	}

	public static void insert(Integer s, Integer p, Integer o){
		spo.insert(s, p, o);
		sop.insert(s, o, p);
		ops.insert(o, p, s);
		osp.insert(o, s, p);
		pos.insert(p, o, s);
		pso.insert(p, s, o);
		size++;
	}

	public static int size(){
		return size;
	}

	/*public static Integer[][] dummy(Integer i) throws TripletNotFoundException{
		return spo.getTriplet(i);
	}*/

	public static int getCount(Integer i){
		return pso.getSecondLevel(i).size();
	}
	
	private static int getMin(Vector<Integer> predicates){
		int min = 0;
		min = pso.getSecondLevel(predicates.get(0)).size();
		int index=0;
		for(int j=1;j<predicates.size();j++){
			int size = pso.getSecondLevel(predicates.get(j)).size();
			if(size<min){
				min=size;
				index=j;
			}
		}
		return index;
	}

	private static void intersection(Vector<Integer> v1, Vector<Integer> v2){
		v1.removeAll(v2);
	}
	
	/*pour un predicat et une liste d object : retourne les sujets correspondants*/
	public static Vector<Integer> subjectsForPredicate(Dictionnary dico,Integer predicate,Vector<Integer> objects){
		Vector<Integer> res=new Vector<Integer>();
		for(Integer io:objects){
			Vector<Integer> temp=pos.getThirdLevel(predicate, io);
			if(temp!=null)
				res.addAll(pos.getThirdLevel(predicate, io));
		}
		return res;
	}
	
	/*nstarclassique*/
	public static Vector<Integer> subjectByPredicates(Dictionnary dico,Vector <Integer> predicates,Vector<Integer> objects){
		int index=getMin(predicates);
		Vector<Integer> temp = pos.getThirdLevel(predicates.get(index),objects.get(index));
		if(temp==null){
			return null;
		}
		Vector<Integer> temp1= new Vector<Integer>(temp);
		predicates.remove(index);
		objects.remove(index);
		while(!predicates.isEmpty()){
			if(temp.size()==1){
				return temp;
			}
			index=getMin(predicates);
			for(Integer i:temp){
				if(!spo.getThirdLevel(i, predicates.get(index)).contains(objects.get(index))){
					temp1.remove(i);
				}
			}
			predicates.remove(index);
			objects.remove(index);
		}
		return temp1;
	}
}
