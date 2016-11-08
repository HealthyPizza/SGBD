package Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class FileLog {

	private static FileWriter writer;
	private static FileWriter time;

	public static void createLog() {
		try {
			writer=new FileWriter(new File("log.csv"));
			time=new FileWriter(new File("time.csv"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void writeLog(String s){
		if(writer!=null){
			try {
				writer.write(s+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void writeTime(String s){
		if(time!=null){
			try {
				time.write(s+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static void endLog(){
		if(writer!=null){
			try {
				writer.flush();
				time.flush();
				writer.close();
				time.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
