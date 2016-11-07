import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openrdf.model.Statement;

import Database.Database;
import Log.FileLog;


public final class RDFRawParser {


	/*http://swat.cse.lehigh.edu/onto/univ-bench.owl#University*/
	public static void main(String args[]) throws InterruptedException{

		FileLog.createLog();
		/*http://www.w3.org/1999/02/22-rdf-syntax-ns#type*/
		Database db=new Database("./dataset");
		
		Vector<String> predicates=new Vector<String>();
		Vector<String> objects=new Vector<String>();
		
		
		System.out.println("?x teacherOf http://www.Department1.University0.edu/Course0 . ?x #mastersDegreeFrom http://www.University509.edu\n");
		predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teacherOf");
		predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#mastersDegreeFrom");
		objects.add("http://www.Department1.University0.edu/Course0");
		objects.add("http://www.University509.edu");
		db.queryNStar(predicates, objects);
		db.printResults();
		
		objects.remove(1);
		objects.add("http://www.University5..\\.edu");
		System.out.println("?x teacherOf http://www.Department1.University0.edu/Course0 . ?x #mastersDegreeFrom http://www.University5**.edu");
		db.nstarRegexp(predicates, objects);
		db.printResults();
		
		predicates=new Vector<String>();
		objects=new Vector<String>();
		predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#takesCourse");
		predicates.add("http://swat.cse.lehigh.edu/onto/univ-bench.owl#name");

		System.out.println("?x takesCourse ?x1 . ?x1 #name GraduateCourse16");
		objects.add("GraduateCourse16");
		db.queryPath(predicates, objects);
		db.printResults();
		
		FileLog.endLog();
		
	}
}

