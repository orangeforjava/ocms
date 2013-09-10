function rf(){
 if ((event.srcElement.tagName!='INPUT' && event.srcElement.tagName!='TEXTAREA' && document.selection.type=='None') || (event.srcElement.tagName=='INPUT' && event.srcElement.disabled) || (event.srcElement.tagName=='TEXTAREA' && event.srcElement.disabled))
   return false; 
}
//document.oncontextmenu=rf
 
function keydown(){
  if(event.ctrlKey==true || event.keyCode==93){
    return false;
   }
}

document.onkeydown=keydown

function drag(){
  return false;
}
//document.ondragstart=drag

function noselect()
{
  if ((event.srcElement.tagName!='INPUT' && event.srcElement.tagName!='TEXTAREA') || (event.srcElement.tagName=='INPUT' && event.srcElement.disabled) || (event.srcElement.tagName=='TEXTAREA' && event.srcElement.disabled))
    return false;
}
//document.onselectstart=noselect
  
//Check Input: 2 decimal digits,1 '.' access
function checknumber()
{ 
  var str1;
  var ipos,ilen,ilen1;

  str1=event.srcElement.value;
  ilen=str1.length;
  ipos=str1.indexOf('.');
  
  if (ipos>=0) {ilen1=ilen-ipos;}
 
  if ((ipos>=0) && (event.keyCode==46) )
  { 
     event.returnValue=false;
   }
   else if ((event.keyCode<48 || event.keyCode>57) && (event.keyCode!=46) || ((ipos>=0) && (ilen1>2)))
   {
    event.returnValue=false;
   }	
}

//Check date input
function checkdate()
{ 
  if ((event.keyCode<48 || event.keyCode>57) && (event.keyCode!=45))
   {
    event.returnValue=false;
   }	
}

function popwin(url)
{
  window.open(url,"_blank","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=yes,width=790,height=520,left=0,top=0");
} 

function popmainwin()
{
  window.open("default.asp?login=1","NewWindow","toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=yes,width=800,height=600,left=0,top=0");
} 

function mainwinie5()
{
  window.location.href='default.asp?login=1';
} 

//check input is numeric
function checkinput()
{ 
  if (event.keyCode<48 || event.keyCode>57) 
  {
   event.returnValue=false;
  }	
}

//ShowCalender
function showcalender()
{
  var returnValue=showModalDialog('calendar.asp',0,"dialogWidth:256px;dialogHeight:182px;help:no;center:yes;resizable:no;status:no;scroll:no");
  return returnValue;
} 

//checkdate area
function valDate(M, D, Y)
{ 
  Months=new Array(31,28,31,30,31,30,31,31,30,31,30,31); 
  Leap=false; 

  if((Y % 4 == 0) && ((Y % 100 != 0) || (Y %400 == 0))) 
  Leap = true; 
  if((D < 1) || (D > 31) || (M < 1) || (M > 12) || (Y < 0)) 
    return(false); 
  if((D > Months[M-1]) && !((M == 2) && (D > 28))) 
    return(false); 
  if(!(Leap) && (M == 2) && (D > 28)) 
    return(false); 
  if((Leap) && (M == 2) && (D > 29)) 
    return(false); 
} 

function formatDate(dateForm)
{ 
  var cDate=dateForm.value; 
  var dSize=cDate.length; 
  var sCount=0; 

  for(var i=0; i < dSize; i++) 
  (cDate.substr(i,1) == "-") ? sCount++ : sCount; 
  if (sCount != 2)
  { 
    alert("ڸʽӦΪ--"); 
    dateForm.focus(); 
    return(false); 
   } 

  var ySize = cDate.indexOf('-');
  if(ySize!=4){ 
    alert('ӦΪ4λ!'); 
    dateForm.focus(); 
    return false; 
  }; 

 var idxBarI=cDate.indexOf("-"); 
 var idxBarII=cDate.lastIndexOf("-"); 
 var strY=cDate.substring(0,idxBarI).replace(/(^\s*|\s*$)/g,""); 
 var strM=cDate.substring(idxBarI+1,idxBarII).replace(/(^\s*|\s*$)/g,""); 
 var strD=cDate.substring(idxBarII+1,dSize).replace(/(^\s*|\s*$)/g,""); 

 var ok=valDate(strM,strD,strY); 
 if(ok==false)
 { 
    alert("Ч޸ !"); 
    dateForm.focus(); 
    return false; 
  } 

  strM = (strM.length<2 ? '0'+strM : strM); 
  strD = (strD.length<2 ? '0'+strD : strD); 
  var retstr=strY+'-'+strM+'-'+strD; 
  return retstr;
} 

//check ie 
function checkie()
{
  var agt=navigator.userAgent.toLowerCase();
  var major=parseInt(navigator.appVersion);
  var ie=((agt.indexOf("msie")!=-1) && (agt.indexOf("opera")==-1));
  var ie4=(ie && (major==4) && (agt.indexOf("msie 4")!=-1));
  var ie5=(ie && (major==4) && (agt.indexOf("msie 5.0")!=-1));
  var ie5_5=(ie && (major==4) && (agt.indexOf("msie 5.5")!=-1));
  var ie6=(ie && (major==4) && (agt.indexOf("msie 6.")!=-1));
  //down IE5 
  if (ie4==true || ie5==true) 
  {
    return "D5";
  }
  //down IE5.5
  if (ie5_5==true || ie6==true || (ie4==false && ie5==false && ie5_5==false && ie6==false))
  {
   return "U5";
  }
}


//check textarea input

function doKeypress(maxLength)
{
    element=event.srcElement
	if(!isNaN(maxLength))
	{
		maxLength=parseInt(maxLength)
		var oTR=element.document.selection.createRange()
		if(oTR.text.length>=1)
			event.returnValue=true
		else if(element.value.length>maxLength-1)
			event.returnValue=false
	}
}

function doKeydown(maxLength)
{
    element=event.srcElement
	setTimeout(function()
	{
		maxLength=parseInt(maxLength)
		if(!isNaN(maxLength))
		{
		  if(element.value.length>maxLength-1)
			{
			  var oTR=window.document.selection.createRange()
			  oTR.moveStart("character", -1*(element.value.length-maxLength))
			  oTR.text=""
			}
		}
	},1)
}

function doBeforePaste(maxLength)
{
	if(!isNaN(maxLength))
	  event.returnValue=false
}

function doPaste(maxLength)
{
    element=event.srcElement
	if(!isNaN(maxLength))
	{
	  event.returnValue=false
	  maxLength=parseInt(maxLength)
	  var oTR=element.document.selection.createRange()
	  var iInsertLength=maxLength - element.value.length + oTR.text.length
	  var sData=window.clipboardData.getData("Text").substr(0, iInsertLength)
	  oTR.text=sData;
	}
}

function sMenubar(st) 
{
  st.style.backgroundColor="#CECFCE";
  st.style.border="1px solid #9C9A9C";
}

function cMenubar(st) 
{ 
  st.style.backgroundColor="";
  st.style.border="";
}

function selectbar(st) 
{
  st.style.backgroundColor="#FFA663";
}

function unselectbar(st) 
{ 
  st.style.backgroundColor="";
}

 //** iframeԶӦҳ **//

 //ϣҳ߶Զ߶ȵiframeƵб
 //öŰÿiframeIDָ. : ["myframe1", "myframe2"]ֻһ壬öš

 //iframeID
 var iframeids=["test"]

 //û֧iframeǷiframe yes ʾأnoʾ
 var iframehide="yes"

 function dyniframesize()
 {
  var dyniframe=new Array()
  for (i=0; i<iframeids.length; i++)
  {
   if (document.getElementById)
   {
    //Զiframe߶
    dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
    if (dyniframe[i] && !window.opera)
    {
     dyniframe[i].style.display="block"
     if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight) //ûNetScape
      dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
     else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) //ûIE
      dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
    }
   }
   //趨Ĳ���֧iframeʾ
   if ((document.all || document.getElementById) && iframehide=="no")
   {
    var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    tempobj.style.display="block"
   }
  }
 }

 if (window.addEventListener)
 window.addEventListener("load", dyniframesize, false)
 else if (window.attachEvent)
 window.attachEvent("onload", dyniframesize)
 else
 window.onload=dyniframesize