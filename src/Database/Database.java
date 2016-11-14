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
import Log.FileLog;

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

	public Database(String datasetPath,RDFFormat format) {
		dico=new Dictionnary();
		IndexManager.initIndexes();
		File files= new File(datasetPath);
		startTime=System.nanoTime();
		for(File file:files.listFiles()){
			if(file.isFile()){
				try {
					Reader reader = new FileReader(file);
					org.openrdf.rio.RDFParser rdfParser = Rio
							.createParser(format);
					rdfParser.setRDFHandler(new RDFListener());
					rdfParser.parse(reader, "");
					reader.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		endTime=System.nanoTime();
		System.out.println("Database ready - " + ((endTime - startTime) / 1000000) + " ms");
		dico.printStats();
		IndexManager.printStats();
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
				intPredicates.add(iP);
				intObjects.add(iO);

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
	
	public Vector<Integer> queryWithPattern(Integer predicate,String  objectPattern){
		intObjects=dico.getIndexesOf(objectPattern);
		return IndexManager.subjectsForPredicate(dico,predicate, intObjects);
	}
	
	public void nstarRegexp(Vector<String> predicates, Vector<String> objects){
		startTime=System.nanoTime();
		results=null;
		intPredicates=new Vector<Integer>();
		boolean found=true;
		for(int i=0;i<predicates.size();i++){
			Integer iP=dico.getIndexOf(predicates.get(i));
			if(iP != null){
				intPredicates.add(dico.getIndexOf(predicates.get(i)));
			}
		}
		int index=IndexManager.getMin(intPredicates);
		Integer ip= intPredicates.get(index);
		Vector<Integer> temp= queryWithPattern(ip, objects.get(index));
		results=new Vector<Integer>(temp);
		intPredicates.remove(index);
		objects.remove(index);
		while(!intPredicates.isEmpty()){
			index=IndexManager.getMin(intPredicates);
			ip= intPredicates.get(index);
			temp= queryWithPattern(ip, objects.get(index));
			results.retainAll(temp);
			intPredicates.remove(index);
			objects.remove(index);
		}
		endTime = System.nanoTime();
	}

	public void queryPath(Vector<String> predicates, Vector<String> objects){
		startTime=System.nanoTime();

		Vector<Integer> temp=queryWithPattern(predicates.lastElement(), objects.lastElement());
		results=new Vector<Integer>();
		for(int i=predicates.size()-2;i>=0;i--){
			Integer ip= dico.getIndexOf(predicates.get(i));
			temp=IndexManager.subjectsForPredicate(dico, ip, temp);
			
		}
		results=temp;
		endTime = System.nanoTime();

	}
	
	public void printResults(){
		//System.out.println("Time: " + ((endTime - startTime))  + " ns");
		FileLog.writeTime((endTime - startTime)+"|");
		String s="";
		if(results==null || results.size()==0){
			System.out.println("No results.");
			FileLog.writeLog("No results.\n");
		}
		else{
			System.out.println(results.size() + " result(s) found.");
			s+=results.size() + " result(s) found.\n";
			for(Integer i:results){
				s+=dico.getValueOf(i)+"\n";
				
			}
			//System.out.println(s);
			FileLog.writeLog(s);
		}
	}

}
