/*
    Copyright 2008 Sohu.com Inc. All rights reserved. 
    搜狐公司 版权所有
*/
var isOK=1;

function sp(id,tid,count){//支持
        comment_vote(id, tid,count,"sp", comment_change_count);
        return comment_change_input(id,"sp");
}

function op(id,tid,count){//反对
        comment_vote(id, tid,count,"op", comment_change_count);
        return comment_change_input(id,"op");
}

function dsp(id,tid){
        return comment_vote(id, tid,-1,"sp", debate_change_count);
}
function dop(id,tid){
       return comment_vote(id, tid,-1,"op", debate_change_count);
       
}

function debate_change_count(id,count,type){
	var count = $id('d'+type + id);
		if(count){
		  count.innerHTML = Number(count.innerHTML) + 1;
		}
    var telm=$id('t'+ id);
    	if(telm){
		        	telm.innerHTML = Number(telm.innerHTML) + 1;
	         	}
}

function comment_vote(id,tid,count ,type, callback){
		var url = "http://comment2.news.sohu.com/service/" +  type + "comment.action?commentId=" + id+"&topicId="+tid;
		 var timer = $id("comment_timer");
        var lastid = $id("comment_lastid");
		if(timer && timer.value > 0 && lastid && lastid.value==id){
			if(count==-1)
			  alert("抱歉，请不要短时间内重复同一操作");
			return;
		}
		timer.value=5;
		lastid.value=id;
		
        var data = new Object();
        data.http = getHTTPObject();
		
        data.http.onreadystatechange = function(){
            	if (data.http.readyState == 4) {
 				    try{
				    	var response = data.http.responseXML.documentElement;
							isOK=3;
							callback(id,count,type);
			        } catch(e){
						isOK=2;
			        }
			   }
        };
	  
        data.http.open("GET", url, true);
        data.http.send(null);
		if(isOK!=2){
		//alert ("多谢您的参与 ");
		}
}

function comment_change_count(id,count,type){
    	var elm = $id(type + id);
    	var telm=$id('t'+ id);
		if(elm){
			count++;
			if(type=='op'){
				elm.innerHTML = '反对('+count+')';
				if(telm){
		        	telm.innerHTML = Number(telm.innerHTML) + 1;
	         	}
			}else{
				elm.innerHTML = '支持('+count+')';
				if(telm){
		        	telm.innerHTML = Number(telm.innerHTML) + 1;
	         	}
			}
		}
		

		//alert ("多谢您的参与 ");
}

function getHTTPObject() {
        var xmlhttp;
        /*@cc_on
          @if (@_jscript_version >= 5)
          try {
          xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
          } catch (e) {
          try {
          xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
          } catch (E) {
          xmlhttp = false; 
          }     
          }
          @else
          xmlhttp = false;
          @end @*/
        if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
                try {
                        xmlhttp = new XMLHttpRequest();
                } catch (e) {
                        xmlhttp = false;
                }
        }
        return xmlhttp;
}


function qreply(content,cid,type){
     var qcontent = $id("qcontent");
     var qtype = $id("qtype");
     qcontent.value=content;
     qtype.value=type;
     document.quickForm.commentId.value=cid;
     document.quickForm.submit();
     

}

function get_cookie(Name) {
	var search = Name + "="
	var returnvalue = "";
	if (document.cookie.length > 0) {
		offset = document.cookie.indexOf(search)
		if (offset != -1) { 
			offset += search.length
			end = document.cookie.indexOf(";", offset);
			if (end == -1)
				end = document.cookie.length;
			returnvalue=unescape(document.cookie.substring(offset, end))
		}
	}
	return returnvalue;
}

function delCookie(name){
	var exp = new Date();
	exp.setTime (exp.getTime() - 1);
	var cval = get_cookie(name);
	document.cookie = name + "=" + cval + "; expires="+ exp.toGMTString();
}

function comment_change_input(id, type){

}

/**
 *  ------------------------------above is old-----------------------------------------
 * */
function showTime() {
        var time;
        var timer = document.getElementById("comment_timer");
        if(timer){
        	var value = timer.value;
        	value--;
        	if(value < 1) value =0;
        	timer.value = value;
        }
        setTimeout("showTime()",1000);
        
}



function comment_set_message(id, msg){
	var message = document.getElementById("comment_message_" + id);
    if(message != null){	
    if(message ){
    	message.innerHTML = msg;
    	return message;
    }
   }
}


function changetext(thevalue){
	var message = document.getElementById("comment.content");
	if(message ){
    	message.innerHTML = thevalue;
    	return message;
    }
}
function checktext(id){
	var message2 = $id("inputvalue"+id);
	var message = $id("comment.content");
	if (message2.value==""||message2.value=="请输入反对理由"||message2.value=="请输入支持理由")
	{
	alert("请输入内容后再提交！");
	return;
	}
	if(message2.value){
    	message.innerHTML = message2.value;
//    	commentForm.target="_blank";
    	commentForm.submit();
//    	location.reload();
    	return message;
    }
}

/*
 create xmlhttp object depend on user's browser
 http://jibbering.com/2002/4/httprequest.2004.html
*/

 function showNewComment(pageNumber,tid,type){
   if(pageNumber!=1 || get_cookie("UCMTIDS")=="")
		return;
	var url = "/getNewselfcmt.action?id="+tid+"&type="+type;
        var data = new Object();
        data.http = getHTTPObject();
		
        data.http.onreadystatechange = function(){
            	if (data.http.readyState == 4) {
 				    try{
				    	var response = data.http.responseXML.documentElement;
				    	var msg = response.getElementsByTagName('rc')[0].firstChild.data;
			        } catch(e){
			         }
			         	var message = document.getElementById("newcoming");
			if(message ){
				message.innerHTML = msg;
				return message;
			}
			   }
        };
	  
        data.http.open("GET", url, true);
        data.http.send(null);
 };
