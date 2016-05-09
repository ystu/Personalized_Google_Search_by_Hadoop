package getResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import jsoup.UseJsoup;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.jsoup.nodes.Element;
import check.CheckURL;

import sort.MyComparator;

public class GetResult {
	public static Map<Double, String> getResult(String table)
			throws IOException {
		String line, url, googleUrl, searchUrl;
		String[] strs;
		double hdfsScore, myScore, highToLowScore;
		Comparator comparator = new MyComparator();
		Map<String, Double> hdfsMap = new HashMap<String, Double>();
		Map<Double, String> myMap = new TreeMap<Double, String>(comparator);
		// Set configuration
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://localhost:9000");
		conf.set("mapred.job.tracker", "localhost:9001");
		FileSystem hdfs = FileSystem.get(conf);
		System.out.println("Working Directory -> "
				+ hdfs.getWorkingDirectory().toString());

		// get data from HDFS
		boolean hdfsExist = true;
		try {
			Path dataPath = new Path(hdfs.getWorkingDirectory().toString() + "/output/" + table + "/part-r-00000");
			FSDataInputStream fsis = hdfs.open(dataPath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fsis));
			while (br.read() != -1) {
				line = br.readLine();
				strs = line.split("\t"); // divide url and hdfsScore
				url = "h" + strs[0]; // I don't know why first character is missing
				System.out.println(url);
				hdfsScore = Double.parseDouble(strs[1]);
				System.out.println(hdfsScore);
				hdfsMap.put(url, hdfsScore);
			}
		} catch (FileNotFoundException e) {
			e.getMessage();
			hdfsExist = false;
		}

		// get google origin sorted
		highToLowScore = 100;
		searchUrl = "https://www.google.com/search?q=" + table;
		Iterator<Element> iterator = UseJsoup.searchPageURL("https://www.google.com.tw/search?q="
						+ table
						+ "&oq="
						+ table
						+ "&aqs=chrome.0.69i57j5j69i65j69i60j69i61j0.1826j0&sourceid=chrome&ie=UTF-8");
		while (iterator.hasNext()) {
			// use jsoup to get URL is chaos, it should be simplify so that it can be used
			googleUrl = CheckURL.simplifyURL(iterator.next().toString());
			// if URL can be used
			if (googleUrl != "") {
				// find if there are datas in HDFS
				if(hdfsExist){
					myScore = hdfsMap.get(googleUrl) == null ? 0 : hdfsMap.get(googleUrl);
				}else{
					//there is no table in HDFS
					myScore = 0;
				}
				myScore += highToLowScore-- ;
				myMap.put(myScore, googleUrl);
			}

		}

		return myMap;
	}
}
