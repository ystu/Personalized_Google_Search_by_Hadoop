package org.windoop;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import javaBean.Behavior;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import algorithm.Algorithm;

import dao.BehaviorDAO;

public class HadoopHVSR {

	public static void makeNewResult(String table) throws IOException, InterruptedException, ClassNotFoundException {

		boolean useJobTracker = true;
		
		//Step 0. Set configuration
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://localhost:9000");
		if(useJobTracker)
			conf.set("mapred.job.tracker", "localhost:9001");
		else
			conf.set("mapred.job.tracker", "local");

		FileSystem hdfs = FileSystem.get(conf);
		System.out.println("Working Directory -> " + hdfs.getWorkingDirectory().toString());
		Path inputPath = new Path(hdfs.getWorkingDirectory().toString() + "/input/" + table +".txt");
		Path outputPath = new Path(hdfs.getWorkingDirectory().toString() + "/output/" + table);
		
		//Step 1. prepare data, write datas to HDFS
		
		List<Behavior> behaviors = new ArrayList<Behavior>();
		behaviors.add(new Behavior("http://tw.sports.yahoo.com/mlb/", "10.0.0.1", 1000.0, 100.0, 10.0, 1, 0));
		behaviors.add(new Behavior("http://scores.espn.go.com/mlb/scoreboard", "10.0.0.2", 1050.0, 140.0, 14.0, 2, 1));
		behaviors.add(new Behavior("http://mlb.ftv.com.tw", "10.0.0.3", 1080.0, 12.0, 14.0, 3, 0));
		behaviors.add(new Behavior("http://mlb.ftv.com.tw", "10.0.0.1", 1000.0, 10.0, 10.0, 1, 0));
		behaviors.add(new Behavior("http://tw.sports.yahoo.com/mlb/", "10.0.0.2", 1050.0, 14.0, 14.0, 2, 1));
		behaviors.add(new Behavior("http://scores.espn.go.com/mlb/scoreboard", "10.0.0.3", 1080.0, 12.0, 14.0, 3, 0));
		behaviors.add(new Behavior("http://mlb.ftv.com.tw", "10.0.0.1", 1000.0, 10.0, 10.0, 1, 0));
		behaviors.add(new Behavior("http://scores.espn.go.com/mlb/scoreboard", "10.0.0.2", 1050.0, 14.0, 14.0, 2, 1));
		behaviors.add(new Behavior("http://tw.sports.yahoo.com/mlb/", "10.0.0.3", 1080.0, 12.0, 14.0, 3, 0));
		
		System.out.println("create data : mlb");
		Path dataPath = new Path(hdfs.getWorkingDirectory().toString() + "/input/" + "mlb.txt");
		FSDataOutputStream fsos = hdfs.create(dataPath);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fsos));
		//write data in HDFS
		
		for(Behavior behavior : behaviors){
			Double browserTime = behavior.getBrowserTime();
			int tabNum = behavior.getTabNum();
			int bookMark = behavior.getBookMark();
			bw.write(behavior.getUrl());
			bw.write(" ");
			bw.write(Double.toString(browserTime));
			bw.write(" ");
			bw.write(Integer.toString(tabNum));
			bw.write(" ");
			bw.write(Integer.toString(bookMark));
			bw.write("\n");
			bw.flush();
		}
		
		
		
		
		bw.close();

		
		//Step 2. Create a Job
		Job job = new Job(conf,"HadoopHVSR");
		job.setJarByClass(HadoopHVSR.class);
		
		//Step 3. Set Input format
		job.setInputFormatClass(TextInputFormat.class);
		
		//Step 4. Set Mapper
		job.setMapperClass(TokenizerMapper.class);
		
		//Step 4.1 Set Combiner (Local Reducer)
		job.setCombinerClass(IntSumReducer.class);
		
		//Step 5. Set Reducer
		job.setReducerClass(IntSumReducer.class);
		
		//Step 6. Set Input		
		FileInputFormat.addInputPath(job, inputPath);
		
		//Step 7. Set Output
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
		
		if(hdfs.exists(outputPath)) hdfs.delete(outputPath, true);
		FileOutputFormat.setOutputPath(job, outputPath);
		
		//Step 8. Submit Job
		job.submit();
		
		if(job.waitForCompletion(true)){
			System.out.println("Job Done!");
//			System.exit(0);
		}
		else{
			System.out.println("Job Failed!");
//			System.exit(1);
		}  
		
	}
}

