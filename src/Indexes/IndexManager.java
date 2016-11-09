package Indexes;

import java.util.HashMap;
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

	public static HashMap<Integer, Vector<Integer>> debug(Integer i){
		return spo.getSecondLevel(i);
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

	public static int getMin(Vector<Integer> predicates){
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

	/*pour un predicat et une liste d object : retourne les sujets correspondants*/
	public static Vector<Integer> subjectsForPredicate(Dictionnary dico,Integer predicate,Vector<Integer> objects){
		Vector<Integer> res=new Vector<Integer>();
		for(Integer io:objects){
			Vector<Integer> temp=pos.getThirdLevel(predicate, io);
			if(temp!=null)
				res.addAll(temp);
		}
		return res;
	}

	/*nstarclassique*/
	public static Vector<Integer> subjectByPredicates(Dictionnary dico,Vector <Integer> predicates,Vector<Integer> objects){
		int index=getMin(predicates);
		//System.out.println("min: "+dico.getValueOf(predicates.get(index)));
		Vector<Integer> temp = pos.getThirdLevel(predicates.get(index),objects.get(index));
		//System.out.println("subjects for this predicate: "+ temp.size()+" " + temp );
		if(temp==null){
			return null;
		}
		Vector<Integer> temp1= new Vector<Integer>(temp);
		predicates.remove(index);
		objects.remove(index);
		//System.out.println(temp1);
		while(!predicates.isEmpty()){
			/*if(temp.size()==1){
				return temp;
			}*/
			index=getMin(predicates);
			//System.out.println("min: "+dico.getValueOf(predicates.get(index)));
			//System.out.println("object: "+objects.get(index));
			for(Integer i:temp){
				Vector<Integer> objs=spo.getThirdLevel(i, predicates.get(index));
				if(objs!=null){
					//System.out.println("subject "+i+ " : "+objs);
					if(!objs.contains(objects.get(index))){
						temp1.remove(i);
						//System.out.println("-REMOVED-");
					}
				}
				else{
					temp1.remove(i);
				}

			}
			predicates.remove(index);
			objects.remove(index);
		}
		//System.out.println(temp1);
		return temp1;
	}

	public static void printStats(){
		System.out.println(size+" relations.");
	}
}
