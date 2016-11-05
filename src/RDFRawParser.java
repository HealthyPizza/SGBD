import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openrdf.model.Statement;

import Database.Database;


public final class RDFRawParser {


	/*http://swat.cse.lehigh.edu/onto/univ-bench.owl#University*/
	public static void main(String args[]) throws InterruptedException{
		//FileLog.createLog();
		/*FileWriter writer = null;
		FileWriter writer1 = null;
		dico=new Dictionnary();

		long startTime = System.currentTimeMillis();
		FileLog.endLog();
		System.out.println("Dictionnary entries : "+dico.size());
		System.out.println("Index entries : "+IndexManager.size());

			startTime = System.currentTimeMillis();
				try {
					Vector<Integer> predicates=new Vector<Integer>();
					Vector<Integer> objects=new Vector<Integer>();
					predicates.add(dico.getIndexOf("http://swat.cse.lehigh.edu/onto/univ-bench.owl#telephone"));
					predicates.add(dico.getIndexOf("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teacherOf"));
					objects.add(dico.getIndexOf("xxx-xxx-xxxx"));
					objects.add(dico.getIndexOf("http://www.Department12.University0.edu/Course46"));
					writer.write(dico.tripletsRDF(IndexManager.dummy(dico.getIndexOf("http://www.Department12.University0.edu/AssistantProfessor9"))));
					//writer1.write(dico.tripletsRDF(IndexManager.dummy1(index)));

					//System.out.println("Temps recherche + affichage: " + (endTime - startTime) + " milliseconds");
					IndexManager.subjectByPredicates(dico, predicates, objects);
					writer1.flush();
					writer.flush();
				} catch (TripletNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					writer.write("TripletNonTrouve");
					writer.flush();
				}*/
		
		/*http://www.w3.org/1999/02/22-rdf-syntax-ns#type*/
		Database db=new Database();
		
		Vector<String> predicates=new Vector<String>();
		Vector<String> objects=new Vector<String>();
		predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#telephone");
		//predicates.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		
		predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teacherOf");
		predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teacherOf");
		predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teacherOf");

		objects.add("xxx-xxx-xxxx");
		objects.add("http://www.Department1.University0.edu/Course0");
		objects.add("http://www.Department1.University0.edu/Course4");
		objects.add("http://www.Department1.University0.edu/Course3");

		//db.nstarRegexp(predicates, objects, new int[]{0});
		
		//db.queryWithPattern("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teacherOf", "Department4.University3");
		//objects.add("http://www.University0.edu");
		db.queryNStar(predicates, objects);
	
		
		
	}
}

