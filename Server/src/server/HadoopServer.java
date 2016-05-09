package server;

import getResult.GetResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javaBean.Data;
import javaBean.Score;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.windoop.HadoopHVSR;

import check.CheckCoder;

import sort.MyComparator;

import dao.BehaviorDAO;

public class HadoopServer extends Thread {

	private boolean OutServer = false;
	private ServerSocket server;
	private final int ServerPort = 8888;

	public static void main(String args[]) {
		(new HadoopServer()).start();
	}

	public HadoopServer() {
		try {
			server = new ServerSocket(ServerPort);

		} catch (java.io.IOException e) {
			System.out.println("Socket error");
			System.out.println("IOException :" + e.toString());
		}
	}

	public void run() {
		Socket client;
		ObjectInputStream oin;
		ObjectOutputStream oos;
		String table;
		Comparator comparator = new MyComparator();
		Map<Double, String> map = new TreeMap<Double, String>(comparator);

		System.out.println("server start !");
		while (!OutServer) {
			client = null;
			try {
				synchronized (server) {
					client = server.accept();
				}
				System.out.println("get connection : InetAddress = " + client.getInetAddress());
				client.setSoTimeout(15000);

				// b.get data from client
				oin = new ObjectInputStream(client.getInputStream());
				Data data = (Data) oin.readObject();
				table = data.getTable();
				System.out.println("data.table : " + table);
				table = CheckCoder.usePercentChar(table);
				table = CheckCoder.decoder(table);
				System.out.println("transform : " + table);

				//***delegate jobs to HadoopHVSR***
				// the input file maybe not exist
				try{
					HadoopHVSR.makeNewResult(table);
				}catch(org.apache.hadoop.mapreduce.lib.input.InvalidInputException e){
					e.getMessage();
				}
				
				//***get result from output directory***
				map = GetResult.getResult(table);

				// c.return data to client
				oos = new ObjectOutputStream(client.getOutputStream());
				oos.writeObject(map);

				oin.close();
				oos.close();
				oin = null;
				client.close();

			} catch (java.io.IOException e) {
				System.out.println("Socket error !");
				System.out.println("IOException ");
				e.printStackTrace();
			} catch (java.lang.ClassNotFoundException e) {
				System.out.println("ClassNotFoundException :");
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
