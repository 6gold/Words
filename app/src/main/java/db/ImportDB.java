package db;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
public class ImportDB {
	private final int BUFFER_SIZE = 10000;
	private String DB_NAME = null;
	public static final String PACKAGE_NAME = "com.example.words";
	public static final String DB_PATH = "data/data/" + PACKAGE_NAME;
	private String DB_FILEPATH,XML_FILEPATH = null;
	private Context context;

	public ImportDB(Context context,String Databasename) {
		this.context = context;
		setDatabasename(Databasename);
		setDatabaseFilePath();
	}
    void setDatabasename(String Dbname){
    	DB_NAME = Dbname;
    }
    void setDatabaseFilePath(){
    	DB_FILEPATH = DB_PATH + "/" + DB_NAME + ".db";
    }
    void setXMLFilePath(){
		XML_FILEPATH = DB_PATH + "/" + DB_NAME + ".xml";
	}
	public SQLiteDatabase openDatabase(boolean newdb) {
		File jhPath = new File(DB_FILEPATH);
		if (jhPath.exists()) {
			return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
		} else if (newdb == true){
			try {
				AssetManager am = context.getAssets();
				if(am == null){
				  SQLiteDatabase.openOrCreateDatabase(jhPath, null);				
				}else{
					InputStream is = am.open(DB_NAME + ".db");
					FileOutputStream fos = new FileOutputStream(jhPath);
				    byte[] buffer = new byte[BUFFER_SIZE];
				    int count = 0;
				    while ((count = is.read(buffer)) > 0) {
					   fos.write(buffer, 0, count);
				}
				fos.flush();
				fos.close();
				is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return openDatabase(true);
		}else return  null;
	}
	public Document openxml(boolean newXML){
		File file = new File(XML_FILEPATH);
		Document Doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			if (file.exists()) {
				Doc = builder.parse(file);
			}else if(newXML == true){
				try{
					AssetManager am = context.getAssets();
					if(am == null){
                       file.createNewFile();
					}else {
						InputStream is = am.open(DB_NAME + ".xml");
						FileOutputStream fos = new FileOutputStream(file);
						byte[] buffer = new byte[BUFFER_SIZE];
						int count = 0;
						while ((count = is.read(buffer)) > 0) {
							fos.write(buffer, 0, count);
						}
						fos.flush();
						fos.close();
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				Doc = openxml(true);
			}else Doc = null;
		}catch (Exception e) {
			   e.printStackTrace();
		}
		return Doc;
	}
}
