//获取ID
function $id(objectId){
    if(document.getElementById && document.getElementById(objectId)) {
	// W3C DOM
	return document.getElementById(objectId);
    } else if (document.all && document.all(objectId)) {
	// MSIE 4 DOM
	return document.all(objectId);
    } else if (document.layers && document.layers[objectId]) {
	// NN 4 DOM.. note: this won't find nested layers
	return document.layers[objectId];
    } else {
	return false;
    }
} 

function showMenu(n){
	$id('menu'+n).style.display='';
}
function showBor(m,j){
	if(j==1){m.style.border='1px #95BCD7 solid'}
	else {m.style.border='1px #fff solid'}
}
function showHidden(m,j){
	if(j==1){m.style.display=''}
	else{m.style.display='none'}
}
function turn1(n){
	for(i=1;i<8;i++){
		if(n==i){
			$id('lm1_'+i).className="now";
			$id('content1_'+i).style.display=""
		}else{
			$id('lm1_'+i).className="";
			$id('content1_'+i).style.display="none"
		}
	}
}

function ShowMsg(){
    document.writeln('<div id="msgdiv" style="position:absolute;display:none;border:2px solid #AFCEF9;"></div>');
	document.writeln('<div id="overdiv" style="width:100%;height:100%;position:absolute;display:none;"></div>');
    this.show=function(replyId,recont,type){
    var tempobj1=$id("msgdiv");
    var tempobj2=$id("overdiv");
        $id("commentId").value=replyId;
        tempobj2.style.filter="alpha(opacity=30)";
        tempobj2.style.MozOpacity = 30/100;
        tempobj2.style.backgroundColor = "#000000";
        tempobj2.style.display = '';
        tempobj2.style.zIndex= 100;
        tempobj2.style.height= document.body.clientHeight+"px";
        tempobj2.style.width= window.screen.availWidth+"px";
        tempobj2.style.left=0;
        tempobj2.style.top=0;
        tempobj1.style.display="none";
        tempobj1.style.left= (document.documentElement.clientWidth)/3+"px";
        tempobj1.style.top= (document.documentElement.scrollTop+(document.documentElement.clientHeight)/3)+"px";
        tempobj1.style.display= '';
        tempobj1.style.width=450+"px";
       // tempobj1.style.height=290+"px";
        tempobj1.style.zIndex= 200;
        tempobj1.style.backgroundColor = "#CDDAF1";
        var OutStr;
        OutStr='<div style="font-size:12px;width:450px;color:#fff;font-weight:bolder;background-color:#6088D2;cursor:move" canmove="true" forid="msgdiv">CCUN.COM.CN</div>'
        OutStr+='<div class="cake01_080102"><div class="title01_080102"><h2><a href="javaScript:void(0);" onclick="ShowMsgo.resertwindow()"><img src="html/plugin/comment/images/pic_080102_04.gif" width="15" height="15" /></a>说吧</h2></div><div class="w422"><input type="checkbox" value="" class="input02" />匿名发表&nbsp;&nbsp;<input type="checkbox" id="hip" class="input02" />隐藏IP&nbsp;&nbsp;&nbsp;<a href="http://cms.ccun.com.cn/member/reg.jhtml" target="_blank">注册新用户</a></div><div class="cake02_080102" id="oldc" style="display:none;"></div><div class="cake02_080102" id="recom" style="display:none;"></div><div class="w422"><textarea id="tx" onclick="this.className=\'hidde\'" class="tx"></textarea></div><div class="w422"><a href="javascript:void(0);" onclick="return postDiv(';
		if(type!==null&&type==1){
		  OutStr+=type
		}
		OutStr+=');"><img src="html/plugin/comment/images/pic_080102_03.gif" width="88" height="29" class="right" /></a><input type="checkbox" id="drec" class="input02" />推荐为精华<div class="clear"></div></div><p><span></span><br />小提示:您要位您发表的言论后果负责，请各位遵守法纪注意语言文明<br /></div>';
      
	    if(type!=null&&type==2){
		  OutStr='<div class="cake01_080102"><div class="title01_080102"><h2><a href="javaScript:void(0);" onclick="ShowMsgo.closePostSucc();"><img src="html/plugin/comment/images/pic_080102_04.gif" width="15" height="15" alt="" title=""/></a>我来说两句</h2></div><div class="w423" style="margin:0 auto;"><img height="32" alt="alert" src="html/plugin/comment/images/gw_cmt_026.gif" width="31">您的帖子已发表，谢谢您的参与!</div><div class="blank5"></div><div><input name="button" type=button onclick="ShowMsgo.closePostSucc();" value="确 定" /></div><div class="blank5"></div></div>';
		}

        tempobj1.innerHTML=OutStr;
	    if(type!==null&&type==1){
       $id('recom').style.display = '';
       $id('recom').innerHTML='<strong>推荐精华：</strong>'+recont;
        }else if(replyId!==null&&replyId!==""){
       $id('oldc').style.display = '';
       $id('oldc').innerHTML='<strong>原帖：</strong>'+recont;
        }if(type!=null&&type==2){
		  setTimeout("ShowMsgo.closePostSucc()", 3000);
		}

        var md=false,mobj,ox,oy
        document.onmousedown=function(ev){
            var ev=ev||window.event;
            var evt=ev.srcElement||ev.target;
            if(typeof(evt.getAttribute("canmove"))=="undefined"){
                return;
                 }
                 if(evt.getAttribute("canmove")){
                md = true;
                mobj = document.getElementById(evt.getAttribute("forid"));
                ox = mobj.offsetLeft - ev.clientX;
                oy = mobj.offsetTop - ev.clientY;
                 }
             }
             document.onmouseup= function(){md=false;}
             document.onmousemove= function(ev){
                var ev=ev||window.event;
                if(md){
					var newTop=0;
					if(ev.clientY + oy>0){
					  newTop=ev.clientY + oy;
					} 
                   mobj.style.left= (ev.clientX + ox)+"px";
                   mobj.style.top= newTop+"px";
                }
				//try{$id('tx').focus();}catch (e){}
             }
    }
    this.resertwindow=function(){
		   try{  
			   var oldValue='';
			   if($id("oldCid"))
                    oldValue=$id("oldCid").value;
			   $id("commentId").value= oldValue;
			   }catch(e){
    	          $id("commentId").value='';
		       }

			$id('oldc').innerHTML='';
			$id('oldc').style.display = 'none';
            $id('msgdiv').style.display='none';
			$id('overdiv').style.display='none';
    }

	    this.closePostSucc=function(){
            $id('msgdiv').style.display='none';
			$id('overdiv').style.display='none';
    }
	
}

function  getAbsLeft(e)   
  {   
      var   l=e.offsetLeft;     
      while(e=e.offsetParent)     
      l   +=   e.offsetLeft;     
      return   l;   
  }

  function  getAbsTop(e)   
  {   
      var   l=e.offsetTop;     
      while(e=e.offsetParent)     
      l   +=   e.offsetTop;     
      return   l;   
  }

function sc1(){
$id("msgdiv").style.top=(document.documentElement.scrollTop+100)+"px";
$id("msgdiv").style.left=(document.documentElement.scrollLeft+getAbsLeft($id("msgdiv")))+"px";
}

function showPostSucc(){
  var cookie=get_cookie("newpost");
	if (cookie!=null&&cookie!=""){
		delCookie("newpost");
	    ShowMsgo.show('','',2);
		return;
	}
}
//
function goToPage(url) {
	document.commentForm.action = url;
	document.commentForm.submit();
}
function switchDebate() {
	var title = document.getElementById("comment.title");
	var debate = document.getElementById("comment.debate");
	if ($id("speak2").style.display == "none") {
		alert("speak2");
		$id("speak1").style.display = "none";
		$id("speak2").style.display = "";
	} else {
		alert("speak1");
		$id("speak1").style.display = "";
		$id("speak2").style.display = "none";
	}
	if (debate.checked) {
		title.value = "";
		debate.value = "true";
		title.disabled = false;
	} else {
		debate.value = "false";
		title.disabled = true;
	}
}
//通过弹出DIV提交
function postDiv(type) {
	var content = $id("tx");
	if (content.value.match(/^\s*$/)) {
		alert("\u5185\u5bb9\u4e0d\u80fd\u4e3a\u7a7a");
		return false;
	}
	document.commentForm.comment.value = content.value;
	var hip = $id("hip");
	if (hip.checked) {
		document.commentForm.hip.value = "1";
	} else {
		document.commentForm.hip.value = "0";
	}
	var drec = $id("drec");
	if (type !== null && type == 1) {
		$id("rtp").value = 3;
	}
	var oldrec = $id("rtp").value;
	if (drec.checked) {
		$id("rtp").value = "8";
	}
	document.commentForm.submit();
	$id("rtp").value = oldrec;
	return true;
}
//post comment action
function postComment() {
	var errormsg = "";
	try {
		if ($id("oldCid") && ($id("commentId").value == null || $id("commentId").value == "")) {
			$id("commentId").value = $id("oldCid").value;
		}
	}
	catch (e) {
		$id("commentId").value = "";
	}
	var content = document.commentForm.comment;
	var title = document.getElementById("title");
	var debate = document.getElementById("debate");
	var spcontent = document.getElementById("spcontent");
	var opcontent = document.getElementById("opcontent");
	try {
		var anonymous = document.loginformComment.anonymousC;
		var author = document.loginformComment.pemail;
		if (document.loginformComment.hideIpC.checked) {
			document.commentForm.hideIp.value = "true";
		} else {
			document.commentForm.hideIp.value = "false";
		}
		if (document.commentForm.isrec.checked) {
			$id("replyType").value = "8";
		} else {
			$id("replyType").value = "";
		}
		if (!anonymous.checked && author.value == "") {
			errormsg = "* \u7528\u6237\u4e0d\u80fd\u4e3a\u7a7a, \u60a8\u53ef\u4ee5\u9009\u62e9\u533f\u540d\u53d1\u8868\n";
			author.focus();
		}
	}
	catch (e) {
	}
	if (title && debate && debate.checked && spcontent && opcontent) {
		document.commentForm.debate.value = "true";
		if (spcontent.value == "" || spcontent.value.match(/^\s*$/)) {
			errormsg = errormsg + "*\u6b63\u65b9\u5185\u5bb9\u4e0d\u80fd\u4e3a\u7a7a\n";
			spcontent.focus();
		}
		if (opcontent.value == "" || opcontent.value.match(/^\s*$/)) {
			errormsg = errormsg + "*\u53cd\u65b9\u5185\u5bb9\u4e0d\u80fd\u4e3a\u7a7a\n";
			opcontent.focus();
		}
		if (debate.checked && title.value != "") {
			content.value = spcontent.value + "PK" + opcontent.value;
		}
		if (content.value.length > 1500) {
			errormsg = errormsg + "* \u5185\u5bb9\u4e0d\u80fd\u8d85\u8fc71500\u4e2a\u5b57\u7b26\n";
			content.focus();
		}
	} else {
		document.commentForm.debate.value = "false";
		if (content.value.match(/^\s*$/)) {
			errormsg = errormsg + "* \u5185\u5bb9\u4e0d\u80fd\u4e3a\u7a7a\n";
			content.focus();
		}
		if (content.value.length > 1500) {
			errormsg = errormsg + "* \u5185\u5bb9\u4e0d\u80fd\u8d85\u8fc71500\u4e2a\u5b57\u7b26\n";
			content.focus();
		}
	}
	if (title && debate) {
		if (debate.checked && (title.value == "" || title.value.match(/^\s*$/))) {
			errormsg = errormsg + "* \u8fa9\u8bba\u8bdd\u9898\u7684\u6807\u9898\u4e0d\u80fd\u4e3a\u7a7a\n";
		}
		if (title.value.length > 24) {
			errormsg = errormsg + "* \u6807\u9898\u4e0d\u80fd\u8d85\u8fc724\u4e2a\u5b57\u7b26\n";
			title.focus();
		}
	}
	if (errormsg) {
		alert(errormsg);
		return false;
	} else {
		document.commentForm.submit();
		return true;
	}
}
function restCommentContent(oObject) {
	try {
		$id("content").value = "";
		$id("comment.title").value = "";
		$id("spcontent").value = "";
		$id("opcontent").value = "";
	}
	catch (e) {
	}
}
function cbg(k) {
	if (k == 1) {
		$id("pCardOpen").className = "open hidden";
		$id("pCardClose").className = "close";
		PassportSComent.cElement.className = "passportcoment";
		PassportSComent.cElement.style.display = "block";
	}
	if (k == 2) {
		$id("pCardOpen").className = "open";
		$id("pCardClose").className = "close hidden";
		PassportSComent.cElement.style.display = "none";
		if (PassportSComent.cookie && PassportSComent.cookie["userid"] != "") {
		} else {
			PassportSScoment.drawPassportMail(PassportSScoment.banner, PassportSScoment.ele);
		}
	}
}
var TopUtils = {"getBID":function (name) {
	var str = TopUtils.getCookie(name);
	var ele = str.split("|");
	if (ele.length > 3) {
		var bid = parseInt(ele[2], 16);
		return bid;
	}
}, "getCookie":function (name) {
	var str = document.cookie;
	for (var i = 0; i <= str.length; i++) {
		var end = i + name.length;
		if (str.substring(i, end) == name) {
			var valueStart = end + 1;
			var valueEnd = str.indexOf(";", valueStart);
			if (valueEnd < valueStart) {
				valueEnd = str.length;
			}
			return unescape(str.substring(valueStart, valueEnd));
		}
	}
	return "";
}, "Deletecookie":function (name) {
	var exp = new Date();
	exp.setTime(exp.getTime() - 100000);
	var cval = TopUtils.GetCookie(name);   
        //alert(cval);
	document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString() + "; path=/; domain=.sohu.com;";
}, "GetCookie":function (name) {
	var arg = name + "=";
	var alen = arg.length;
	var clen = document.cookie.length;
	var i = 0;
	while (i < clen) {
		var j = i + alen;
		if (document.cookie.substring(i, j) == arg) {
			return TopUtils.getCookieVal(j);
		}
		i = document.cookie.indexOf(" ", i) + 1;
		if (i == 0) {
			break;
		}
	}
	return null;
}, "getCookieVal":function (offset) {
	var endstr = document.cookie.indexOf(";", offset);
	if (endstr == -1) {
		endstr = document.cookie.length;
	}
	return unescape(document.cookie.substring(offset, endstr));
}};

