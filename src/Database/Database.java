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

	public Database() {
		dico=new Dictionnary();
		IndexManager.initIndexes();
		File files= new File("./dataset");
		startTime=System.nanoTime();
		for(File file:files.listFiles()){
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
		endTime=System.nanoTime();
		System.out.println("Database ready - " + ((endTime - startTime) / 1000000) + "ms");
	}

	public Vector<Integer> queryNStar(Vector<String> predicates, Vector<String> objects){
		startTime=System.nanoTime();
		intPredicates=new Vector<Integer>();
		intObjects=new Vector<Integer>();
		//if(predicates.size()==objects.size())
		for(int i=0;i<predicates.size();i++){
			Integer iP=dico.getIndexOf(predicates.get(i));
			Integer iO=dico.getIndexOf(objects.get(i));
			if(iP != null && iO != null){
				intPredicates.add(dico.getIndexOf(predicates.get(i)));
				intObjects.add(dico.getIndexOf(objects.get(i)));

			}
			else{
				System.out.println("Pas de resultat");
				return null;
			}

		}	
		results= IndexManager.subjectByPredicates(dico, intPredicates, intObjects);
		endTime = System.nanoTime();
		printResults();
		return results;
	}

	public Vector<Integer> queryWithPattern(String predicate,String  objectPattern){
		Integer ip=dico.getIndexOf(predicate);
		intObjects=dico.getIndexesOf(objectPattern);
		return IndexManager.subjectsForPredicate(dico,ip, intObjects);
	}

	public Vector<Integer> nstarRegexp(Vector<String> predicates, Vector<String> objects, int[] objectswithRE){
		startTime=System.nanoTime();
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
				if(first){
					regexpres.addAll(queryWithPattern(predicates.get(i), objects.get(i)));
					first=false;
				}
				else
					regexpres.retainAll(queryWithPattern(predicates.get(i), objects.get(i)));
			}
		}
		Vector<Integer> nstarres=queryNStar(preds, objs);
		nstarres.retainAll(regexpres);
		results=nstarres;
		printResults();
		return null;
	}
	
	public void printResults(){
		System.out.println(results.size() + " results.");
		System.out.println("Time: " + ((endTime - startTime) / 1000000) + "ms");
		for(Integer i:results){
			System.out.println(dico.getValueOf(i));
		}
	}
	
}
