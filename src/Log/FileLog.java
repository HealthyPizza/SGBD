package Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class FileLog {

	private static FileWriter writer;

	public static void createLog(String name) {
		try {
			writer=new FileWriter(new File(name+".txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void write(String s){
		if(writer!=null){
			try {
				writer.write(s+"\n");
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
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
