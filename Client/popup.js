window.onload=function(){

document.getElementById("time").innerHTML= Date();

document.getElementById("show").innerHTML=showUrl;

}

chrome.tabs.query({active:true,currentWindow:true},function(tab){
  var str = tab[0].url;
  if((str.match("//www.google.com"))!=null && (str.match("https://www.google.com.tw/url?"))==null){
	  doPostRequest();
  }

var xmlHttp;

function createXMLHttpRequest() {
    if(window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
    else if(window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
}

  var urlall;  
function doPostRequest() {
    var url = "http://163.13.132.161:8080/HVSR/GetSortedServlet?timeStamp=" + new Date().getTime(); // server位置，避免快取
    createXMLHttpRequest();
    xmlHttp.open("POST", url);
    xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlHttp.onreadystatechange = handleStateChange;
    xmlHttp.send("name=" + encodeURIComponent(str));//encodeURIComponent,避免傳送出去url為亂碼，編碼轉換
}

function handleStateChange() {
    if(xmlHttp.readyState == 4) {
        if(xmlHttp.status == 200) {
			var jsonObj =JSON.parse(xmlHttp.responseText);
			
			function urlTotal(num){

				return("<a target=_blank href=" + jsonObj.webSite[num].url + ">" + decodeURIComponent(jsonObj.webSite[num].title).replace(/\+/g," ")+ "</a><br><br>");  //.replace(/\你要置換的字串/g,'換成的字串')可以取代全部，原本replace('','')只能個別
			}
			var showUrl="<br>";
			for( k=0; k<jsonObj.webSite.length; k++){//jsonObj.webSite.length  算陣列大小
				showUrl=showUrl+urlTotal(k);
			}
			document.getElementById("show").innerHTML=showUrl;

        }
    }
}

});