import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;
import java.util.Vector;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

import Dictionnary.Dictionnary;
import Indexes.Index;
import Indexes.IndexManager;
import Indexes.TripletNotFoundException;
import Log.FileLog;

public final class RDFRawParser {

	public static Dictionnary dico;
	private static class RDFListener extends RDFHandlerBase {

		@Override
		public void handleStatement(Statement st) {
			dico.put(st.getSubject().stringValue());
			dico.put(st.getPredicate().stringValue());
			dico.put(st.getObject().stringValue());
			IndexManager.insert(dico.getIndexOf(st.getSubject().stringValue()), dico.getIndexOf(st.getPredicate().stringValue()), dico.getIndexOf(st.getObject().stringValue()));
		}

	};
	/*http://swat.cse.lehigh.edu/onto/univ-bench.owl#University*/
	public static void main(String args[]) throws InterruptedException, IOException {

		//FileLog.createLog();
		FileWriter writer = null;
		FileWriter writer1 = null;
		dico=new Dictionnary();
		IndexManager.initIndexes();
		long startTime = System.currentTimeMillis();
		File files= new File("./dataset");
		int inter=0;
		for(File file:files.listFiles()){
			Reader reader = new FileReader(file);
			org.openrdf.rio.RDFParser rdfParser = Rio
					.createParser(RDFFormat.RDFXML);
			rdfParser.setRDFHandler(new RDFListener());
			try {
				writer=new FileWriter(new File("resultsM.txt"));
				writer1=new FileWriter(new File("resultsF.txt"));
				rdfParser.parse(reader, "");

			} catch (Exception e) {

			}
			try {
				reader.close();

			} catch (IOException e) {
			}
			//if(inter==40)
			//break;
			inter++;
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Time " + (endTime - startTime) + " milliseconds");
		FileLog.endLog();

		System.out.println("Dictionnary entries : "+dico.size());
		System.out.println("Index entries : "+IndexManager.size());

		//while(true){
			//System.out.print("Objet a rechercher: ");
			//Scanner sc=new Scanner(System.in);
			//String object = sc.nextLine();
			startTime = System.currentTimeMillis();
			//Integer index=dico.getIndexOf(object);
			//if(index!=null){
				try {
					Vector<Integer> predicates=new Vector<Integer>();
					Vector<Integer> objects=new Vector<Integer>();
					
					
					predicates.add(dico.getIndexOf("http://swat.cse.lehigh.edu/onto/univ-bench.owl#telephone"));
					predicates.add(dico.getIndexOf("http://swat.cse.lehigh.edu/onto/univ-bench.owl#teacherOf"));
					objects.add(dico.getIndexOf("xxx-xxx-xxxx"));
					objects.add(dico.getIndexOf("http://www.Department12.University0.edu/Course46"));
					writer.write(dico.tripletsRDF(IndexManager.dummy(dico.getIndexOf("http://www.Department12.University0.edu/AssistantProfessor9"))));
					//writer1.write(dico.tripletsRDF(IndexManager.dummy1(index)));
					endTime = System.currentTimeMillis();
					
					//System.out.println("Temps recherche + affichage: " + (endTime - startTime) + " milliseconds");
					IndexManager.subjectByPredicates(dico, predicates, objects);
					writer1.flush();
					writer.flush();
				} catch (TripletNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					writer.write("TripletNonTrouve");
					writer.flush();
				}
			}
			/*else{
				System.out.println("--Objet non present--\n");
			}*/
		//}
	}

