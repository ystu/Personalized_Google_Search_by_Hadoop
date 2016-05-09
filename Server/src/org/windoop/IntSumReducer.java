package org.windoop;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import javaBean.Behavior;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

import sort.MyComparator;

public class IntSumReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
	
	protected void setup(Context context) {
		System.out.println("Reducer Setup");
	}

	protected void cleanup(Context context) {
		System.out.println("Reducer Cleanup");

	}

	public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
		double sum = 0.0;
		String url = key.toString();
		// add each url score
		for (DoubleWritable val : values) {
			sum += val.get();
		}
		//write data in HDFS
		context.write(key, new DoubleWritable(sum));
		
	}

}
