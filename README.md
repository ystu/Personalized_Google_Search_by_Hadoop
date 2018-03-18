# Personalized_Google_Search_by_Hadoop
Personalized Google Search by Using Hadoop Distributed System

This is my graduate project in college. Our motivation comes from we found some top results from Google search were not appropriate. Also, each user has different preference, the results should be personalized. Therefore, we want to build personalized search results based on each user’s behavior. 

![image](https://github.com/ystu/Improve-Google-Ranking-by-Hadoop/blob/master/architecture.jpg)

Our system has two parts, front-end is Google chrome extension, and back-end is hadoop server. Chrome extension collects user’s behavior, such as exist time in current pages, number of licks, number of tabs and bookmark. Backend is a server included Hadoop system and MySQL database. When users search a keyword, back-end server executes MapReduce to calculate user’s score and add scores into Google’s top 100 results. Then, give the new results and display in Google chrome.

![image](https://github.com/ystu/Personalized_Google_Search_by_Hadoop/blob/master/demo.png)

However, this project is not finished. The basic architecture is complete and works, but the personalization search algorithm is not designed yet and it lacks user behavior data.



Build by Yun-Sheng Tu, Hsiang-Ju Chen

Aug, 2014
