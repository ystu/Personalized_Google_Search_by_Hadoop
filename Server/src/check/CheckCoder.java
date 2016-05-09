package check;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CheckCoder {
	public static String decoder(String str) throws UnsupportedEncodingException{
		if(str.contains("%")){
			str = URLDecoder.decode(str, "UTF-8");
		}
		return str;
	}

	public static String encoder(String str) throws UnsupportedEncodingException
	{
//		str = new String(str.getBytes("UTF-8"), "UTF-8");//ISO-8859-1
		str = URLEncoder.encode(str,"UTF-8");
		
		return str;
	}
	
	//if keyword contains % , can't save in db's table, we substitute to _ 
	public static String changePercentChar(String str){
		
			str = str.replaceAll("%", "_");
		
		return str;
	}
	
	public static String usePercentChar(String str){
		
			str = str.replaceAll("_", "%");
		
		return str;
	}
}
