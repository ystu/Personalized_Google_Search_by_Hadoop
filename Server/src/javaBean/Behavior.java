package javaBean;

import java.io.Serializable;

public class Behavior implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private String remoteAddr;
	private double startTime;
	private double existTime;
	private double browserTime;
	private int tabNum;
	private int bookMark;
	

	public Behavior(){}
	public Behavior(String url, String remoteAddr,double startTime,double existTime,double browserTime,int tabNum,int bookMark){
		setBehavior(url,remoteAddr,startTime,existTime,browserTime,tabNum,bookMark);
	}
	public void setBehavior(String url,String remoteAddr,double startTime,double existTime,double browserTime,int tabNum,int bookMark){
		setUrl(url);
		setRemoteAddr(remoteAddr);
		setStartTime(startTime);
		setExistTime(existTime);
		setBrowserTime(browserTime);
		setTabNum(tabNum);
		setBookMark(bookMark);
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	public double getStartTime() {
		return startTime;
	}
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}
	public double getExistTime() {
		return existTime;
	}
	public void setExistTime(double existTime) {
		this.existTime = existTime;
	}
	public double getBrowserTime() {
		return browserTime;
	}
	public void setBrowserTime(double browseTime) {
		this.browserTime = browseTime;
	}
	public int getTabNum() {
		return tabNum;
	}
	public void setTabNum(int tabNum) {
		this.tabNum = tabNum;
	}
	public int getBookMark() {
		return bookMark;
	}
	public void setBookMark(int bookMark) {
		this.bookMark = bookMark;
	}

}
