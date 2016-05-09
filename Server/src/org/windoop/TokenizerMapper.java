package org.windoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import algorithm.Algorithm;

public class TokenizerMapper extends
		Mapper<Object, Text, Text, DoubleWritable> {

	protected void setup(Context context) {
		System.out.println("Mapper Setup: "
				+ ((FileSplit) context.getInputSplit()).getPath().getName());
	}

	protected void cleanup(Context context) {
		System.out.println("Mapper Cleanup");
	}

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		
		// get each line content
		StringTokenizer tokenizerArticle = new StringTokenizer(line, "\n");
		while (tokenizerArticle.hasMoreTokens()) {
			// get each content in the line
			StringTokenizer tokenizerLine = new StringTokenizer(tokenizerArticle.nextToken());
			String strUrl = tokenizerLine.nextToken();
			double browserTime = Double.parseDouble(tokenizerLine.nextToken());
			int tabNum = Integer.parseInt(tokenizerLine.nextToken());
			int bookMark = Integer.parseInt(tokenizerLine.nextToken());
			// use HVSR algorithm
			double score = Algorithm.hvsrAlgorithm(browserTime, tabNum, bookMark);
			// prepare for reduce
			Text url = new Text(strUrl);
			context.write(url, new DoubleWritable(score));
		}
	}

}
