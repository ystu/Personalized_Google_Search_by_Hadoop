package check;

import java.io.UnsupportedEncodingException;

public class CheckURL {

	// use jsoup to get URL is chaos, it should be simply so that it can be used
	public static String simplifyURL(String url)
			throws UnsupportedEncodingException {
		if (url.startsWith("h", 16)) { // get the URL which is begin with "https..."
			url = url.substring(16);
			String[] urls = url.split("&"); // divide URL with "&", and put in array
			url = urls[0];
			url = CheckCoder.decoder(url);// check URL whether it shuld be decoder from UTF8
			return url;
		}
		return "";
	}

	// change another search URL type
	public static String checkName(String name) {
		if (name.contains("#site")) {
			name = transform(name, 2);
		}
		if (name.contains("webhp")) {
			name = transform(name, 3);
		}
		if (name.contains("safe")) {
			name = transform(name, 1);
		}
		return name;
	}

	// change the different type to regular type
	private static String transform(String name, int i) {

		String[] strs = name.split("&");
		if (strs[i].startsWith("o")) { // in order to change oq=���� --> q=����
			strs[i] = strs[i].substring(1);
			System.out.println("return strs[" + i + "] = " + strs[i]);

		}
		return "https://www.google.com.tw/search?" + strs[i]; // strs[2]="q=ooxx"

	}

}
