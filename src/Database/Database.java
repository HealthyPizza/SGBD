package Database;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

import Dictionnary.Dictionnary;
import Indexes.IndexManager;

public class Database {

	private static Dictionnary dico;
	Pattern p;
	Matcher m;
	Vector<Integer> intPredicates;
	Vector<Integer> intObjects;
	Vector<Integer> results;
	long startTime;
	long endTime;

	private static class RDFListener extends RDFHandlerBase {

		@Override
		public void handleStatement(Statement st) {
			dico.put(st.getSubject().stringValue());
			dico.put(st.getPredicate().stringValue());
			dico.put(st.getObject().stringValue());
			IndexManager.insert(dico.getIndexOf(st.getSubject().stringValue()), dico.getIndexOf(st.getPredicate().stringValue()), dico.getIndexOf(st.getObject().stringValue()));
		}

	};

	public Database(String datasetPath) {
		dico=new Dictionnary();
		IndexManager.initIndexes();
		File files= new File(datasetPath);
		startTime=System.nanoTime();
		for(File file:files.listFiles()){
			if(file.isFile()){
				try {
					Reader reader = new FileReader(file);
					org.openrdf.rio.RDFParser rdfParser = Rio
							.createParser(RDFFormat.RDFXML);
					rdfParser.setRDFHandler(new RDFListener());
					rdfParser.parse(reader, "");
					reader.close();

				} catch (Exception e) {

				}
			}
		}
		endTime=System.nanoTime();
		System.out.println("Database ready - " + ((endTime - startTime) / 1000000) + "ms");
	}
	

	public Vector<Integer> queryNStar(Vector<String> predicates, Vector<String> objects){
		startTime=System.nanoTime();
		results=null;
		intPredicates=new Vector<Integer>();
		intObjects=new Vector<Integer>();
		boolean found=true;
		//if(predicates.size()==objects.size())
		for(int i=0;i<predicates.size();i++){
			Integer iP=dico.getIndexOf(predicates.get(i));
			Integer iO=dico.getIndexOf(objects.get(i));
			if(iP != null && iO != null){
				intPredicates.add(dico.getIndexOf(predicates.get(i)));
				intObjects.add(dico.getIndexOf(objects.get(i)));

			}
			else{
				found=false;
				break;
			}

		}
		if(found)
			results= IndexManager.subjectByPredicates(dico, intPredicates, intObjects);
		endTime = System.nanoTime();
		return results;
	}

	public Vector<Integer> queryWithPattern(String predicate,String  objectPattern){
		Integer ip=dico.getIndexOf(predicate);
		if(ip==null)
			return null;
		intObjects=dico.getIndexesOf(objectPattern);
		return IndexManager.subjectsForPredicate(dico,ip, intObjects);
	}

	public Vector<Integer> nstarRegexp(Vector<String> predicates, Vector<String> objects, int[] objectswithRE){
		startTime=System.nanoTime();
		results=null;
		Vector<String> preds=new Vector<String>();
		Vector<String> objs=new Vector<String>();
		Vector<Integer> regexpres=new Vector<Integer>();
		boolean first = true;
		for(int i=0; i<predicates.size();i++){
			if(objectswithRE[i]==0){
				preds.add(predicates.get(i));
				objs.add(objects.get(i));
			}
			else{
				Vector<Integer> temp=queryWithPattern(predicates.get(i), objects.get(i));
				if(temp==null)
					return null;
				if(first){
					regexpres.addAll(temp);
					first=false;
				}
				else
					regexpres.retainAll(temp);
			}
		}
		Vector<Integer> nstarres=queryNStar(preds, objs);
		if(nstarres!=null)
			nstarres.retainAll(regexpres);
		results=nstarres;
		endTime = System.nanoTime();
		return null;
	}

	public void printResults(){
		System.out.println("Time: " + ((endTime - startTime) / 1000000) + "ms");
		if(results==null){
			System.out.println("No results.");
		}
		else{
			System.out.println(results.size() + " results found.");
			for(Integer i:results){
				System.out.println(dico.getValueOf(i));
			}
		}
	}

}
