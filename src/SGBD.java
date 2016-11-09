import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import org.openrdf.rio.RDFFormat;
import Database.Database;
import Log.FileLog;
import Parsing.Parser;


public class SGBD {


	/*http://swat.cse.lehigh.edu/onto/univ-bench.owl#University*/
	public static void main(String args[]){

		if(args.length!=2){
			System.out.println("2 parameters needed : datasetDirectory queriesFile");
			return;
		}
		
		FileLog.createLog();
		Database db=new Database(args[0],RDFFormat.NTRIPLES);
		Parser p=new Parser();
		String request;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("./queries/queryT"));
			while ((request = br.readLine()) != null) {
				p.parse(request);
				System.out.println(p.getPredicates());
				System.out.println(p.getObjects());
				FileLog.writeLog(request);
				if(!p.isPath()){
					System.out.println("NOTAPATH");
					db.queryNStar(p.getPredicates(), p.getObjects());
					db.printResults();
				}
				else{
					System.out.println("path");
					db.queryPath(p.getPredicates(), p.getObjects());
					db.printResults();
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileLog.endLog();
	}
}

