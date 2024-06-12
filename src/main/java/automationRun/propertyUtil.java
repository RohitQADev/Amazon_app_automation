package automationRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class propertyUtil {
		
	private static Properties prop = new Properties();
	
	public static Properties getObjectRepository() {
		try {
		
			InputStream stream;
			stream = new FileInputStream(
					new File(System.getProperty("user.dir") + "//src//main//resources//object.properties"));
			prop.load(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}
}
