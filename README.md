# Improve-Google-Ranking-by-Hadoop
Improve Google Search Ranking by Using Hadoop Distributed System

![image](https://github.com/ystu/Improve-Google-Ranking-by-Hadoop/blob/master/architecture.jpg)

In order to improve the result in Google's search ranking, we use Chrome Extension collecting user's click data and storing in MySQL database. When users type the keyword, Using MapReduce to calculate a better result and return to Client. The result of new ranking will be displayed next to web page.

Client: Chrome Extension, Javascript, HTML

1. use Chrome extension to Collect user's click data, including browsing time, bookmark or not, click relative link, open other tabs etc. return data to Server.

2. Get the new ranking from Server and display it next to web page.

Server: Hadoop, Tomcat, Java, Eclipse, MySQL, HDFS

1. Save user's click data in HDFS or MySQL

2. When user search something, use mapreduce to counting the score of each link in HDFS, then using json format to send data to client.

Build by Yun-Sheng Tu, Hsiang-Ju Chen

Aug, 2014
