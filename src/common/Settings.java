package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Settings {
	
	public static final String HOST1;
	public static final int PORT1;
	
	//public static final String HOST2;
	public static final int PORT2;
	
	//public static final String HOST3;
	public static final int PORT3;
	
	public static final Map<String, Integer> list = new HashMap<>();
	public static final List<Integer> portList = new ArrayList<Integer>();
	
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("settings"); //from settings.properties file
		
		HOST1 = bundle.getString("host1");
		PORT1 = Integer.parseInt(bundle.getString("port1"));
		
		//HOST2 = bundle.getString("host2");
		PORT2 = Integer.parseInt(bundle.getString("port2"));
		
		//HOST3 = bundle.getString("host3");
		PORT3 = Integer.parseInt(bundle.getString("port3"));
		
		list.put(HOST1, PORT1);
		list.put(HOST1, PORT2);
		list.put(HOST1, PORT3);
		
		portList.add(PORT1);
		portList.add(PORT2);
		portList.add(PORT3);


	}
}
//fol aceasta clasa pentru a accesa datele din settings.properties

