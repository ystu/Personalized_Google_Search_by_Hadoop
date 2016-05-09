package sort;

import java.io.Serializable;
import java.util.Comparator;

public class MyComparator implements Comparator<Object> , Serializable{

	@Override
	public int compare(Object o1, Object o2) {
		int result = 0;
		double d1 = (Double)o1;
		double d2 = (Double)o2;
		
		//descending sort
		if(d1 > d2){
			result = -1;
		}else if(d1 < d2){
			result = 1;
		}
		return result;
	}
	
}
