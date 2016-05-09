package dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

//import algorithm.Algorithm;
import javaBean.Behavior;

import dataBase.DbManager;

public class BehaviorDAO {

	public static double writeInHDFS(String table,String url) throws SQLException{
		double browserTime, score = 0, resultScore = 0;
		int tabNum, bookMark;
		Collection<Double> datas = new ArrayList<Double>();
		String sql = "SELECT * FROM " + table + " WHERE url LIKE ?";
		Connection conn = DbManager.getConnection("hvsrdb");
		PreparedStatement preStat = conn.prepareStatement(sql);
		preStat.setString(1, url);
		ResultSet rs = preStat.executeQuery();
		
		
		//get all of the ResultSet one by one which url is needed
		while(rs.next()){
			browserTime = rs.getDouble("browserTime");
			tabNum = rs.getInt("tabNum");
			bookMark = rs.getInt("bookMark");
			//write datas in HDFS
			
			
			//input score in datas
			if(score != 0){
				datas.add(score);
			}
		}
		
		//final resultScore
		for(Double data : datas){
			resultScore += data;
		}
		resultScore /= datas.size();
		
		return resultScore;
	}
	

}
