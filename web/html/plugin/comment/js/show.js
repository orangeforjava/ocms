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

