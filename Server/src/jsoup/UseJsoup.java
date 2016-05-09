package jsoup;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UseJsoup {
	public static Iterator<Element> searchPageURL(String name) throws IOException{
		
		Document doc = Jsoup.connect(name).userAgent("Mozilla/5.0").get(); // 要使用userAgent才不會error
		Elements links = doc.select("h3.r > a ");// 取出擁有href屬性的<h3 class="r"...
		Iterator<Element> it = links.iterator();
		
		return it;
	}
}
