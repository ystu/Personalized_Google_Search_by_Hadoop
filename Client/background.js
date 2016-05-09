
var nowtime =[];
var a =[];//tab存在時間，狀態
var urlID =[];
var numtab =[];//tab切換次數
var tabnumber =[];//視窗切換tab累加用
var browserTime =[];//瀏覽時間
var realtime=[];//瀏覽時間，紀綠跳出tab時間用
var bookMark=[];
var tabUrl=[];//在多個搜尋頁面時可以記錄每個tab所搜尋的keyword
var keyWord =[];

var realtime;
var timestate=0;
var startID;
var markstate=0;
var bookMarkId;
var keyWordUrl;





chrome.extension.onRequest.addListener(onRequest);////////////



chrome.tabs.onUpdated.addListener(function(tabID,props) {
	timestate=tabID/1000;//不要讓tabID=timestate
	browserTime[tabID]=0;
	bookMarkId=tabID;
	var str = props.url;
	
	if(now1()-realtime[tabID]>5 && props.url!=null){//判斷是否為進入keyword連結後，更改url跳出情況
		endID = tabID;
		endtime = now1();
		b=1;
		lifetime=endtime - realtime[endID];
		browserTime[markstate] += lifetime;		

		if((a[endID])*b==1){
	   		result();
			b=0;
			a[endID]=0;	
		}
	}//如果使用者更改url進入其他網頁時，也可以上傳使用者行為資訊
	
	
	
	if((str.match("//www.google.com"))!=null && (str.match("https://www.google.com.tw/url?"))==null){
		chrome.tabs.reload(tabID);
		tabUrl[tabID]=str;
		b=0;
		a[tabID]=0;
		keyWordUrl=str;
   		doPostRequest(str);
		doPostRequestPopup();
  	}
	if(str.match("zh.wikipedia.org/wiki")!=null){
		str=str.replace("/wiki/","/zh-tw/");	
	}//wiki個案，wiki內容為中文時，會自動轉跳，用來轉語言。


	
	for(var numUrl=0; numUrl<jsonObj.webSite.length; numUrl++){	
		if(jsonObj.webSite[numUrl].url==str){
			startID = tabID;
			nowtime[startID]=now1();
			urlID[startID] = str;
			a[startID]=1;
			numtab[tabID]=0;

			realtime[startID]=nowtime[startID];
			browserTime[startID]=0;
			keyWord[tabID]=keyWordUrl;
  		}
	}

	if(str!=urlID[tabID]){
		a[tabID]=0;
		if(str.match("https://www.google.com.tw/url?")!=null){a[tabID]=1;}//部分網頁最後一次更新為https://www.google.com.tw/url?，所以需要另外判斷		
	}
	
});

function now1(){
	var now = new Date();
	var t1 = now.getTime();
 	var tt=(t1/1000);
	return (tt);
}

chrome.tabs.onRemoved.addListener(function(tabID) {	
	endID = tabID;
	endtime = now1();
	b=1;
	lifetime=endtime - realtime[endID];
	browserTime[markstate] += lifetime;		
							//alert(a[endID])
	if((a[endID])*b==1){
	    result();
		b=0;
		a[endID]=0;	
	}

});

function result(){
	existTime=endtime-nowtime[endID];
	if(browserTime[endID]!=0 && numtab[endID]==0){numtab[endID]+=1;}//搜尋連結tab，進入連結情況
	if(bookMark[endID]!=1){bookMark[endID]=0;}
	if(numtab[endID]==0 || keyWord[endID]==null || urlID[endID]==null){return ;}//不要回傳tabNum=0的資訊，完全沒價值
	

	respone= "keyWord:"+ keyWord[endID]+ "___URL:"+ urlID[endID]+ "___Start-time"+ nowtime[endID]+ "___Exist-time:"+ existTime+ "___Browser-time:"+ browserTime[endID] +"___tabnum:"+ numtab[endID]+ "___bookMark:"+bookMark[endID];

	doPostRequestJson(endID, existTime);
	//alert(respone);
}

chrome.tabs.onSelectionChanged.addListener(function(tabID, props) {	
	if(numtab[tabID]==null){
		numtab[tabID]=1;
	}else
	{
		numtab[tabID]++;
	}
	
	if(tabID!=timestate){
		var now11=now1();
		lifetime=now11 - realtime[markstate];
		
		if(numtab[startID]==0){
			browserTime[startID] = lifetime;	
		}else{
			browserTime[markstate] = lifetime + (browserTime[markstate]);
		}		
		realtime[tabID] = now11;
	}
	markstate=tabID;
	timestate=tabID;
	bookMarkId=tabID;
	tabnumber[props.windowId] =tabID ;
	if((tabUrl[tabID].match("https://www.google.com.tw/"))!=null){
		keyWordUrl=tabUrl[tabID];
		doPostRequest(tabUrl[tabID]);
	}//多個搜尋頁面做切換時，可以重新向server請求，不會因為有多個搜尋頁面時造成server回傳的url會被覆蓋
	
	chrome.debugger.attach(chrome.browserAction.enable());//測試自動開啟popup.html
	

});


chrome.windows.onFocusChanged.addListener(function(windowID, props) {//跳出該視窗，再切回該視窗tab切換累加用
  if(numtab[tabnumber[windowID]]!=null){
 	numtab[tabnumber[windowID]]++;
  }
});

chrome.bookmarks.onCreated.addListener(function(id) {
	bookMark[bookMarkId] = 1;
});


document.addEventListener('DOMContentLoaded', function() {
  bootStrap();
});	


var xmlHttp;

function createXMLHttpRequest() {
    if(window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if(window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
}


function doPostRequest(x) {
    var url = "http://163.13.132.161:8080/HVSR/GetURLServlet?timeStamp=" + new Date().getTime(); // server位置，避免快取
	createXMLHttpRequest();
    xmlHttp.onreadystatechange = handleStateChange;
    xmlHttp.open("POST", url);
    xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlHttp.send("name=" + encodeURIComponent(x));//encodeURIComponent,避免傳送出去url為亂碼，編碼轉換
}


function handleStateChange() {
    if(xmlHttp.readyState == 4) {
        if(xmlHttp.status == 200) {
			jsonObj =JSON.parse(xmlHttp.responseText);
        }
    }
}





function doPostRequestJson(endID, existTime) {
	var behavior = getBehaviorObject(endID, existTime);
	var behaviorAsJson = JSON.stringify(behavior);
	var url = "http://163.13.132.161:8080/HVSR/GetBehaviorServlet?timeStamp=" + new Date().getTime(); // server位置，避免快取
	createXMLHttpRequest();
    xmlHttp.open("POST", url);
    xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlHttp.send(behaviorAsJson);	
}

function getBehaviorObject(endID, existTime) {
	return new Behavior(keyWord[endID], urlID[endID], nowtime[endID], existTime, browserTime[endID], numtab[endID], bookMark[endID]);
}
function Behavior(keyWord, url, startTime, existTime, browserTime, tabNum, bookMark) {
	this.keyWord = keyWord;
	this.url = url;
	this.startTime= startTime;
	this.existTime = existTime;
	this.browserTime = browserTime;
	this.tabNum = tabNum;
	this.bookMark = bookMark;

}







////////////////////////////////////////////////

var popup_function = function(){
    // 確認有沒有 #CompanyInfo 的下方視窗
    if (!document.getElementById('CompanyInfo')) {
	var content = "<div id='CompanyInfo' style='max-height: 60%; overflow-y: scroll; background: #cc103f; bottom: 0; padding: 5px; text-align: left;right:50px ; z-index: 99999; font-size: 14.5px; line-height: 1.5; color: #fff; position: fixed'>"
	    //+ "<ul id='CompanyInfoMessage' style='list-style-type: disc'></ul>"
		+ "<a id='time'></a>"
		+ "<a id='show'></a>"
	    + "<div style='color:#000;font-weight:bold;float:right;padding-right:8px;width:46px;'>"
	    + "<span id='CompanyInfoClose' style='cursor:pointer;'>關閉</span>"                
	    + "</div></div>";
	document.body.innerHTML = content + document.body.innerHTML;
	var close = document.getElementById('CompanyInfoClose');

	close.addEventListener('click',function() {
	    document.getElementById('CompanyInfo').style.display = 'none';
	});

	var info_dom = document.getElementById('CompanyInfo');
	info_dom.style.background = 'yellow';
	info_dom.style.color = 'black';
    }
	

	document.getElementById("time").innerHTML="HVSR-2.0_"+Date();
	document.getElementById("show").innerHTML=showUrl;
};

////////////////////////////////////////////////


function onRequest(request, sender, sendResponse) {
    chrome.tabs.executeScript(sender.tab.id, {code: "(" + popup_function + ')(' + JSON.stringify(request.showUrl) +')'});
    sendResponse({});		
}