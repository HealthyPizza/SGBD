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

		
		Database db=new Database("./dataset1",RDFFormat.NTRIPLES);
		Parser p=new Parser("./queries");
		//FileLog.createLog(0);
		for(int i=0;i<15;i++){
			try {
				while(p.parse()){
					if(!p.isPath()){
						db.queryNStar(p.getPredicates(), p.getObjects());
						db.printResults();
					}
				}
				p.resetIndex();
				//FileLog.newPass();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//FileLog.endLog();*/
	}
}

