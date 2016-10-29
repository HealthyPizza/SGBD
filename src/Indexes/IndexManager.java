package Indexes;

import java.util.Arrays;
import java.util.Vector;

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
	
	public static Integer[][] dummy(Integer i){
		
		return osp.getTriplet(i);
		
	}
	
	public static Vector<String> dummy1(Integer i){
		
		return osp.getTriplets(i);
		
	}
}
