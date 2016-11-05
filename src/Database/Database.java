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
		int inter=0;
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
			//if(inter==40)
			//break;
			inter++;
		}
		System.out.println("Database Ready");
	}
	
	public void queryNStar(Vector<String> predicates, Vector<String> objects){
		String result="";
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
					return;
				}
				
			}		
			IndexManager.subjectByPredicates(dico, intPredicates, intObjects);
	}
	
	public void queryWithPattern(String predicate,String  objectPattern){
		Integer ip=dico.getIndexOf(predicate);
		intObjects=dico.getIndexesOf(objectPattern);
		IndexManager.subjectsForPredicate(dico,ip, intObjects);
	}
	
	public void nstarRegexp(Vector<String> predicates, Vector<String> objects, int[] objectswithRE){
		intPredicates=new Vector<Integer>();
		intObjects=new Vector<Integer>();
		for(int i=0; i<objectswithRE.length;i++){
			Integer ip= dico.getIndexOf(predicates.get(objectswithRE[i]));
			Vector<Integer> io=dico.getIndexesOf(objects.get(objectswithRE[i]));
			for(int j=0;j<io.size();j++){
				intPredicates.add(ip);
			}
			intObjects.addAll(io);
		}
		
	}
}
