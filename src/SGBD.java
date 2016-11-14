
import org.openrdf.rio.RDFFormat;
import Database.Database;
import Log.FileLog;
import Parsing.Parser;


public class SGBD {


	/*http://swat.cse.lehigh.edu/onto/univ-bench.owl#University*/
	public static void main(String args[]){

		if(args.length!=3){
			System.out.println("3 parameters needed : datasetDirectory queriesDir nbOfPasses");
			return;
		}

		
		Database db=new Database(args[0],RDFFormat.NTRIPLES);
		Parser p=new Parser(args[1]);
		FileLog.createLog(0);
		for(int i=0;i<Integer.parseInt(args[2]);i++){
			try {
				while(p.parse()){
					if(!p.isPath()){
						db.queryNStar(p.getPredicates(), p.getObjects());
						db.printResults();
					}
				}
				p.resetIndex();
				FileLog.newPass();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		FileLog.endLog();
	}
}

