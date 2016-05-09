package javaBean;

import java.io.Serializable;

public class Score implements Serializable{
	
	private double score;

	public Score(double score){
		setScore(score);
	}
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
}
