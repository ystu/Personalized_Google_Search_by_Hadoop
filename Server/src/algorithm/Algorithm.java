package algorithm;

public class Algorithm {
	//hvsr algorithm
	public static double hvsrAlgorithm(double browserTime, int tabNum, int bookMark){
		double score;
		score = browserTime / tabNum;
		if(score > 3600){
			return 0;
		}
		if(bookMark == 1){
			score *= 2;
		}
		if((tabNum / 50) < 1){
			score *= (1 + tabNum / 100);
		}else{
			score *= 2;
		}
		score /= 144;
		
		return score;
	}
}
