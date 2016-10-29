import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

import Dictionnary.Dictionnary;
import Indexes.Index;
import Indexes.IndexManager;
import Log.FileLog;

public final class RDFRawParser {

	static Dictionnary dico;
	private static class RDFListener extends RDFHandlerBase {

		@Override
		public void handleStatement(Statement st) {
			/*System.out.println("\n" + st.getSubject() + "\t " + st.getPredicate() + "\t "
					+ st.getObject());*/
			dico.put(st.getSubject().stringValue());
			dico.put(st.getPredicate().stringValue());
			dico.put(st.getObject().stringValue());
			IndexManager.insert(dico.getIndexOf(st.getSubject().stringValue()), dico.getIndexOf(st.getPredicate().stringValue()), dico.getIndexOf(st.getObject().stringValue()));
		}

	};
	/*http://swat.cse.lehigh.edu/onto/univ-bench.owl#University*/
	public static void main(String args[]) throws FileNotFoundException, InterruptedException {

		//FileLog.createLog();


		dico=new Dictionnary();
		//spo=new Index();
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
				rdfParser.parse(reader, "");

			} catch (Exception e) {

			}
			//System.out.println(dico.tripletsRDF(spo.getTriplet(11, 14)));
			//System.out.println(spo.getThirdLevel(11, 14));
			try {
				reader.close();

			} catch (IOException e) {
			}
			if(inter==40)
				break;
			inter++;
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Time " + (endTime - startTime) + " milliseconds");
		FileLog.endLog();

		System.out.println("Dictionnary entries : "+dico.size());
		System.out.println("Index entries : "+IndexManager.size());

		while(true){
			System.out.print("Objet à rechercher: ");
			Scanner sc=new Scanner(System.in);
			String object = sc.nextLine();
			startTime = System.currentTimeMillis();
			Integer index=dico.getIndexOf(object);
			if(index!=null){
				dico.tripletsRDF(IndexManager.dummy(index));
				dico.tripletsRDF(IndexManager.dummy1(index));
				endTime = System.currentTimeMillis();
				System.out.println("Temps recherche + affichage: " + (endTime - startTime) + " milliseconds");
			}
			else{
				System.out.println("--Objet non present--\n");
			}
		}
	}

}