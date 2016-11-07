import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openrdf.model.Statement;

import Database.Database;


public final class RDFRawParser {


	/*http://swat.cse.lehigh.edu/onto/univ-bench.owl#University*/
	public static void main(String args[]) throws InterruptedException{

		
		/*http://www.w3.org/1999/02/22-rdf-syntax-ns#type*/
		Database db=new Database("./dataset");
		
		Vector<String> predicates=new Vector<String>();
		Vector<String> objects=new Vector<String>();
		//predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#telephone");
		//predicates.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		
		predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teacherOf");
		predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#mastersDegreeFrom");
		//predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teacherOf");

		//objects.add("xxx-xxx-xxxx");
		objects.add("http://www.Department1.University0.edu/Course0");
		objects.add("http://www.University509.edu");
		//objects.add("http://www.Department1.University0.edu/Course3");

		//db.nstarRegexp(predicates, objects, new int[]{0});
		
		//db.queryWithPattern("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teacherOf", "Department4.University3"); //les profs d'un cour du dep4 univ3
		//objects.add("http://www.University0.edu");
		db.queryNStar(predicates, objects);
		db.printResults();
		objects.set(1, "University5");
		predicates.addElement("http://swat.cse.lehigh.edu/onto/univ-bench.owl#doctoralDegreeFrom");
		objects.add("University8");
		db.nstarRegexp(predicates, objects, new int[]{0,1,1});
		db.printResults();
		
		
	}
}

