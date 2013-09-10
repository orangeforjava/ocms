function commonInputPicker(sId, action, form, element)
{
	//alert(action);
	switch(action) {
		case 'color':
			var arr = showModalDialog("../html/color.htm","color","dialogWidth:200pt;dialogHeight:175pt;help:0;status:0");	break;
		case 'date':
			showCalendar(element, 'y-mm-dd');
			break;
		case 'upload':
			var arr = showModalDialog('upload.php?sId='+ sId +'&o=display&mode=one&type=img_picker&NodeID=' + NodeID,"color","dialogWidth:390px;dialogHeight:120px;help:0;status:0;scroll:no");
			break;
		case 'tpl':
			with(form){

				eval("var varlue1=" + element + ".value;")

			}

			var arr = showModalDialog("admin_select.php?sId="+ sId +"&o=tpl&tpl=" + varlue1,"color","dialogWidth:428px;dialogHeight:266px;help:0;status:0;scroll:no");
			break;	
		case 'psn':
			with(form){

				eval("var varlue1=" + element + ".value;")

			}
			
			var info = showModalDialog("admin_select.php?sId="+ sId +"&o=psn_picker&psn=" + varlue1 ,"color","dialogWidth:600px;dialogHeight:266px;help:0;status:0;scroll:no");
			if(info['filename'] != null) {
				var arr= '{PSN-URL:'+ info['PSNID'] + "}" +info['filename'];
			}
			break;
		case 'resource_img':
			var arr =  select_resource(sId, 'img');
			break;
		case 'resource_attach':
			var arr =  select_resource(sId, 'attach');
			break;
	}

	if(arr != null) {
		with(form){

			eval(element + ".value= '" +  arr + "'")

		}


	}

}





function select_resource(sId, Category)
{	
	 
	var info = showModalDialog("admin_resource.php?sId="+sId+"&o=list_ui_main&Category=" + Category,"color","dialogWidth:563px;dialogHeight:412px;help:0;status:0;scroll:no");

 	if (info != null &&   info != '') {
 		return info;
	}  else {
		return "";
	}

}


/*
 * 对URL进行编码
 *
 */
function urlencode(thisurl) 
{	thisurl = '' + thisurl;
	thisurl =  thisurl.replace(/\//g, "%2F");
	thisurl =  thisurl.replace(/:/g, "%3A");
	thisurl =  thisurl.replace(/\?/g, "%3F");
	thisurl =  thisurl.replace(/=/g, "%3D");
	thisurl =  thisurl.replace(/&/g, "%26");
	return thisurl;
}



//{{{ select框处理函数


/*
 * 移动某个选项
 *
 * FormName:表单名
 * ElementFrom:源select框
 * ElementTo:目标select框
*/
function select_move_to(FormName, ElementFrom, ElementTo) {
	eval("var sFrom = FormName." + ElementFrom);
	eval("var sTo = FormName." + ElementTo);

	if(sFrom.selectedIndex >= 0) {
	    sTo.options.add(new Option(sFrom.options[sFrom.selectedIndex].text, sFrom.options[sFrom.selectedIndex].value)); 
		sFrom.options[sFrom.selectedIndex].removeNode(true);
	}  
}


/*
 * 移动所有选项
 *
 * FormName:表单名
 * ElementFrom:源select框
 * ElementTo:目标select框
*/
function select_move_all_to(FormName, ElementFrom, ElementTo) { 
	eval("var sFrom = FormName." + ElementFrom);
	eval("var sTo = FormName." + ElementTo);

	for (var i=0; i<sFrom.options.length; i++) 
    sTo.options.add(new Option(sFrom.options[i].text, sFrom.options[i].value)); 
    sFrom.options.length = 0; 
} 


/*
 * 预提交处理
 *
 * FormName:表单名
 * SelectElement:源select框
 * SubmitElement:待提交的表单框
*/
function select_submit(FormName, SelectElement, SubmitElement) { 
	eval("var iSelectElement= document." + FormName+ "." + SelectElement);
	eval("var iSubmitElement = document." + FormName+ "." + SubmitElement);

    var returnValue = ""; 
    for (var i=0; i<iSelectElement.options.length; i++)  {
		if(i==0) {
			returnValue = iSelectElement.options[i].value;
		} else {
			returnValue += "," + iSelectElement.options[i].value;
		
		}
	}
	iSubmitElement.value = returnValue;
} 

/*
 * select选定值上移
 *
 */
function select_move_up(obj)
{
	with (obj){
		if(selectedIndex==0){
			options[length]=new Option(options[0].text,options[0].value)
			options[0]=null
			selectedIndex=length-1
			}
		else if(selectedIndex>0) moveG(obj,-1)
	}
}

/*
 * select选定值上移
 *
 */
function select_move_down(obj)
{
	with (obj){
		if(selectedIndex==length-1){
			var otext=options[selectedIndex].text
			var ovalue=options[selectedIndex].value
			for(i=selectedIndex; i>0; i--){
				options[i].text=options[i-1].text
				options[i].value=options[i-1].value
			}
			options[i].text=otext
			options[i].value=ovalue
			selectedIndex=0
			}
		else if(selectedIndex<length-1) moveG(obj,+1)
	}
}

/*
 * select选定值下移offset的位置
 *
 */
function select_move_goto(obj,offset)
{
	with (obj){
		desIndex=selectedIndex+offset
		var otext=options[desIndex].text
		var ovalue=options[desIndex].value
		options[desIndex].text=options[selectedIndex].text
		options[desIndex].value=options[selectedIndex].value
		options[selectedIndex].text=otext
		options[selectedIndex].value=ovalue
		selectedIndex=desIndex
	}
}

/*
 * select选定值删除
 *
 */
function select_del(obj) {
	with(obj) {
		options[selectedIndex]=null
		selectedIndex=length-1
	}

}


//select框处理函数 }}}



function UI_upload(url)
{
	var leftPos = (screen.availWidth-800) / 2
	var topPos = (screen.availHeight-600) / 2 
	var popupWin = window.open(url, '','width=350,height=100,scrollbars=no,resizable=yes,titlebar=0,top=' + topPos + ',left=' + leftPos);


}

function toggle_collapse(objname) {
	obj = findobj(objname);
	collapsed = getcookie("cmscollapse");
	cookie_start = collapsed ? collapsed.indexOf(objname) : -1;
	cookie_end = cookie_start + objname.length + 1;

	if(obj.style.display == "none") {
		obj.style.display = "";
		if(cookie_start != -1) collapsed = collapsed.substring(0, cookie_start) + collapsed.substring(cookie_end, collapsed.length);
	} else {
		obj.style.display = "none";
		if(cookie_start == -1) collapsed = collapsed + objname + " ";
	}

	expires = new Date();
	expires.setTime(expires.getTime() + (collapsed ? 86400 * 30 : -(86400 * 30 * 1000)));
	document.cookie = "cmscollapse=" + escape(collapsed) + "; expires=" + expires.toGMTString() + "; path=/";
}

function getcookie(name) {
	var cookie_start = document.cookie.indexOf(name);
	var cookie_end = document.cookie.indexOf(";", cookie_start);
	return cookie_start == -1 ? '' : unescape(document.cookie.substring(cookie_start + name.length + 1, (cookie_end > cookie_start ? cookie_end : document.cookie.length)));
}
function cms_send(url)
{
	var oBao = new ActiveXObject("Microsoft.XMLHTTP");
	var returnValue;
	//特殊字符：+,%,&,=,?等的传输解决办法.字符串先用escape编码的.
	//Update:2004-6-1 12:22
	oBao.open("POST", url, false);
	oBao.send();
	//服务器端处理返回的是经过escape编码的字符串.
	returnValue = unescape(oBao.responseText);

	return returnValue;
}
/**
*
**/
function cms_send(url,action)
{
	var oBao = new ActiveXObject("Microsoft.XMLHTTP");
	var returnValue;
	
	var ac=action;
	//alert(ac);
	//特殊字符：+,%,&,=,?等的传输解决办法.字符串先用escape编码的.
	//Update:2004-6-1 12:22
	oBao.open("POST", url, false);
	oBao.setRequestHeader("Content-Length",ac.length);
	oBao.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	oBao.send(ac);
	//服务器端处理返回的是经过escape编码的字符串.
	returnValue = unescape(oBao.responseText);

	return returnValue;
}
function onlyNum()
{
  if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105)))
//考虑小键盘上的数字键
    event.returnValue=false;
}
function iwpc_CopyClipboard(obj)
{
	obj = findObj(obj);
	
	if (obj) {
		window.clipboardData.setData('Text', obj.value);
	}
}
/*********************************************************/
/* General functions                                     */
/*********************************************************/

/*function findObj(n, d) { 
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
  d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}*/
function findobj(n, d) {
	var p, i, x;
	if(!d) d = document;
	if((p = n.indexOf("?"))>0 && parent.frames.length) {
		d = parent.frames[n.substring(p + 1)].document;
		n = n.substring(0, p);
	}
	if(x != d[n] && d.all) x = d.all[n];
	for(i = 0; !x && i < d.forms.length; i++) x = d.forms[i][n];
	for(i = 0; !x && d.layers && i < d.layers.length; i++) x = findobj(n, d.layers[i].document);
	if(!x && document.getElementById) x = document.getElementById(n);
	return x;
}


function openWindow(theURL,winName,features) {
  window.open(theURL,winName,features);
  return false;
}

function setTextOfLayer(objName,newText) {
  if ((obj=findObj(objName))!=null) with (obj)
    if (document.layers) {document.write(unescape(newText)); document.close();}
    else innerHTML = unescape(newText);
}

function confirmLink (theLink, confirmMsg,message )

{
    var is_confirmed = confirm('确认'+ confirmMsg+ message + '？');
    if (is_confirmed) {
       
    }

    return is_confirmed;
    
}
//<!--
//this is jsMonthView 1.0, author is Chinese DengKang (2002-07-12).
//I allow your do any modify, but i don't bear any liability.
//browser: IE 4 and later
function DefineMonthView(theTextObject){ //the month view construct function
  this.GetOnlyName = function( ){ //create one and only name on the document
    var theName = "MV"; //prefix
 var aryName = new Array();
 aryName[0] = "_MonthView"; //the month view main body's name postfix
 aryName[1] = "_MonthGrid"; //the month view day area's name postfix
 aryName[2] = "_goPreviousMonth"; //the month view go previous month button's name postfix
 aryName[3] = "_goNextMonth"; //the month view go next month button's name postfix
 aryName[4] = "_YearList"; //the month view year list's name postfix
 aryName[5] = "_MonthList"; //the month view month list's name postfix
 aryName[6] = "_DayList"; //keep the month view current day's element name postfix
 var i = -1, j = 0, maxi = 2000;
 var exTag = true;
 while ((exTag == true) && (i < maxi)){
   i++;
   exTag = false;
   for (j=0;j<aryName.length;j++){
     if (document.all.item(theName + i.toString() + aryName[j]) != null){
    exTag = true;
  }
   }
 }
 if (exTag == false){
   return(theName + i.toString());
 }else{
   return("_" + theName);
 }
  }
  var theName = this.GetOnlyName();
  this.Name = theName; //the month view name
  this.Source = theTextObject; //the month view act on theTextObject
  this.MinYear = 1970; //year list min value
  //return between 1000 and 9999 and <= this.MaxYear
  this.MaxYear = 2030; //year list max value
  //return between 1000 and 9999 and >= this.MinYear
  this.Width = 300; //the month view main body's width
  this.Height = 200; //the month view main body's height
  this.DateFormat = "<yyyy>-<mm>-<dd>"; //the date format
  //<yy> or <yyyy> is year, <m> or <mm> is digital format month, <MMM> or <MMMMMM> is character format month, <d> or <dd> is day, other char unchanged
  //this function setting year, month and day sequence
  //example:
  //  <yyyy>-<mm>-<dd> : 2002-04-01
  //  <yy>.<m>.<d> : 02.4.1
  //  <yyyy> Year <MMMMMM> Month <d> Day : 2002 Year April Month 1 Day
  //  <m>/<d>/<yy> : 4/1/02
  //  <MMM> <dd>, <yyyy> : Apr 01, 2002
  //  <MMMMMM> <d>, <yyyy> : April 1, 2002
  //  <dd> <MMM> <yyyy> : 01 Apr 2002
  //  <dd>/<mm>/<yyyy> : 01/04/2002
  this.UnselectBgColor = "#FFFFFF"; //the month view default background color
  this.SelectedBgColor = "#808080"; //the selected date background color
  this.SelectedColor = "#FFFFFF"; //the selected date front color
  this.DayBdWidth = "2"; //the day unit border width, unit is px
  this.DayBdColor = this.UnselectBgColor; //the day unit border color,default is this.UnselectBgColor
  this.TodayBdColor = "#FF0000"; //denote today's date border color
  this.InvalidColor = "#808080"; //it is not current month day front color
  this.ValidColor = "#0000FF"; //it is current month day front color
  this.WeekendBgColor = this.UnselectBgColor; //the weekend background color, default is this.UnselectBgColor
  this.WeekendColor = this.ValidColor; //the weekend front color, default is  this.ValidColor
  this.YearListStyle = "font-size:12px; font-family:Verdana;"; //the year list's style
  this.MonthListStyle = "font-size:12px; font-family:Verdana;"; //the month list's style
  this.MonthName = new Array(); //month name list, font is include this.MonthListStyle
  this.MonthName[0] = "1月";
  this.MonthName[1] = "2月";
  this.MonthName[2] = "3月";
  this.MonthName[3] = "4月";
  this.MonthName[4] = "5月";
  this.MonthName[5] = "6月";
  this.MonthName[6] = "7月";
  this.MonthName[7] = "8月";
  this.MonthName[8] = "9月";
  this.MonthName[9] = "10月";
  this.MonthName[10] = "11月";
  this.MonthName[11] = "12月";
  this.TitleStyle = "cursor:default; color:#000000; background-color:" + this.UnselectBgColor + "; font-size:16px; font-weight:bolder; font-family:Times new roman; text-align:center; vertical-align:bottom;"; //the month view title area's style
  this.WeekName = new Array(); //week name list, font is include this.TitleStyle
  this.WeekName[0] = "日";
  this.WeekName[1] = "一";
  this.WeekName[2] = "二";
  this.WeekName[3] = "三";
  this.WeekName[4] = "四";
  this.WeekName[5] = "五";
  this.WeekName[6] = "六";
  this.FooterStyle = "cursor:hand; color:#000000; background-color:" + this.UnselectBgColor + "; font-size:12px; font-family:Verdana; text-align:left; vertical-align:middle;"; //the month footer area's style
  this.TodayTitle = "Today:"; //today tip string, font is include this.FooterStyle
  this.MonthBtStyle = "font-family:Marlett; font-size:12px;"; //the change month button style
  this.PreviousMonthText = "3"; //the go previous month button text
  //font is include this.MonthBtStyle
  this.NextMonthText = "4"; //the go next month button text
  //font is include this.MonthBtStyle
  this.MonthGridStyle = "border-width:1px; border-style:solid; border-color:#000000;"; //the month view main body's default style
  this.HeaderStyle = "height:32px; background-color:menu;"; //the month view header area's style
  this.LineBgStyle = "height:10px; background-color:" + this.UnselectBgColor + "; text-align:center; vertical-align:middle;"; //the month view title area and day area compart area background style
  this.LineStyle = "width:90%; height:1px; background-color:#000000;"; //the month view title area and day area compart area front style
  this.DayStyle = "cursor:hand; font-size:12px; font-family:Verdana; text-align:center; vertical-align:middle;"; //the month view day area's style
  this.OverDayStyle = "this.style.textDecoration='underline';"; //the mouse over a day style
  this.OutDayStyle = "this.style.textDecoration='none';"; //the mouse out a day style
  this.GetoffsetLeft = function(theObject){ //return theObject's absolute offsetLeft
    var absLeft = 0;
 var thePosition="";
    var tmpObject = theObject;
    while (tmpObject != null){
   thePosition = tmpObject.position;
   tmpObject.position = "static";
   absLeft += tmpObject.offsetLeft;
   tmpObject.position = thePosition;
   tmpObject = tmpObject.offsetParent;
    }
    return absLeft;
  }
  this.GetoffsetTop = function(theObject){ //return theObj's absolute offsetTop
    var absTop = 0;
 var thePosition = "";
    var tmpObject = theObject;
    while (tmpObject != null){
   thePosition = tmpObject.position;
   tmpObject.position = "static";
      absTop += tmpObject.offsetTop;
   tmpObject.position = thePosition;
   tmpObject = tmpObject.offsetParent;
    }
    return absTop;
  }
  this.GetFormatYear = function(theYear){//format theYear to 4 digit
    var tmpYear = theYear;
    if (tmpYear < 100){
      tmpYear += 1900;
      if (tmpYear < 1970){
     tmpYear += 100;
      }
    }
 if (tmpYear < this.MinYear){
   tmpYear = this.MinYear;
 }
 if (tmpYear > this.MaxYear){
   tmpYear = this.MaxYear;
 }
    return(tmpYear);
  }
  this.GetMonthDays = function(theYear, theMonth){ //get theYear and theMonth days number
    var theDays = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    var theMonthDay = 0, tmpYear = this.GetFormatYear(theYear);
    theMonthDay = theDays[theMonth];
    if (theMonth == 1){ //theMonth is February
      if(((tmpYear % 4 == 0) && (tmpYear % 100 != 0)) || (tmpYear % 400 == 0)){
     theMonthDay++;
   }
    }
    return(theMonthDay);
  }
  this.SetDateFormat = function(theYear, theMonth, theDay){//format a date to this.DateFormat
    var theDate = this.DateFormat;
 var tmpYear = this.GetFormatYear(theYear);
 var tmpMonth = theMonth;
 if (tmpMonth < 0){
   tmpMonth = 0;
 }
 if (tmpMonth > 11){
   tmpMonth = 11;
 }
 var tmpDay = theDay;
 if (tmpDay < 1){
   tmpDay = 1;
 }else{
      tmpDay = this.GetMonthDays(tmpYear, tmpMonth);
      if (theDay < tmpDay){
     tmpDay = theDay;
   }
 }
    theDate = theDate.replace(/<yyyy>/g, tmpYear.toString());
    theDate = theDate.replace(/<yy>/g, tmpYear.toString().substr(2,2));
    theDate = theDate.replace(/<MMMMMM>/g, this.MonthName[tmpMonth]);
    theDate = theDate.replace(/<MMM>/g, this.MonthName[tmpMonth].substr(0,3));
    if (theMonth < 9){
      theDate = theDate.replace(/<mm>/g, "0" + (tmpMonth + 1).toString());
    }else{
   theDate = theDate.replace(/<mm>/g, (tmpMonth + 1).toString());
    }
    theDate = theDate.replace(/<m>/g, (tmpMonth + 1).toString());
    if (theDay < 10){
      theDate = theDate.replace(/<dd>/g, "0" + tmpDay.toString());
    }else{
   theDate = theDate.replace(/<dd>/g, tmpDay.toString());
    }
    theDate = theDate.replace(/<d>/g, tmpDay.toString());
    return(theDate);
  }
  this.GetTextDate = function(theString){ //convert a string to a date, if the string is not a date, return a empty string
    var i = 0, tmpChar = "", find_tag = "";
    var start_at = 0, end_at = 0, year_at = 0, month_at = 0, day_at = 0;
    var tmp_at = 0, one_at = 0, two_at = 0, one_days = 0, two_days = 0;
    var aryDate = new Array();
    var tmpYear = -1, tmpMonth = -1, tmpDay = -1;
    var tmpDate = theString.toLowerCase();
 var defDate = "";
 end_at = tmpDate.length;
 for (i=1;i<end_at;i++){
   if (tmpDate.charAt(i)=="0"){
     tmpChar = tmpDate.charAt(i-1);
     if (tmpChar<"0" || tmpChar>"9"){
    tmpDate = tmpDate.substr(0,i-1) + "-" + tmpDate.substr(i+1);
  }
   }
 }
    for (i=0;i<9;i++){
      tmpDate = tmpDate.replace(this.MonthName[i].toLowerCase().substr(0,3), "-00" + (i+1).toString() + "-");
    }
    for (i=9;i<12;i++){
      tmpDate = tmpDate.replace(this.MonthName[i].toLowerCase().substr(0,3), "-0" + (i+1).toString() + "-");
    }
    tmpDate = tmpDate.replace(/jan/g, "-001-");
    tmpDate = tmpDate.replace(/feb/g, "-002-");
    tmpDate = tmpDate.replace(/mar/g, "-003-");
    tmpDate = tmpDate.replace(/apr/g, "-004-");
    tmpDate = tmpDate.replace(/may/g, "-005-");
    tmpDate = tmpDate.replace(/jun/g, "-006-");
    tmpDate = tmpDate.replace(/jul/g, "-007-");
    tmpDate = tmpDate.replace(/aug/g, "-008-");
    tmpDate = tmpDate.replace(/sep/g, "-009-");
    tmpDate = tmpDate.replace(/oct/g, "-010-");
    tmpDate = tmpDate.replace(/nov/g, "-011-");
    tmpDate = tmpDate.replace(/dec/g, "-012-");
    for (i=0;i<tmpDate.length;i++){
      tmpChar = tmpDate.charAt(i);
      if ((tmpChar<"0" || tmpChar>"9") && (tmpChar != "-")){
     tmpDate = tmpDate.replace(tmpChar,"-")
   }
    }
    while(tmpDate.indexOf("--") != -1){
      tmpDate = tmpDate.replace(/--/g,"-");
    }
    start_at = 0;
    end_at = tmpDate.length-1;
    while (tmpDate.charAt(start_at)=="-"){
      start_at++;
    }
    while (tmpDate.charAt(end_at)=="-"){
      end_at--;
    }
    if (start_at < end_at+1){
      tmpDate = tmpDate.substring(start_at,end_at+1);
    }else{
      tmpDate = "";
    }
    aryDate = tmpDate.split("-");
    if (aryDate.length != 3){
      return(defDate);
    }
    for (i=0;i<3;i++){
      if (parseInt(aryDate[i],10)<1){
     aryDate[i] = "1";
      }
    }
    find_tag="000";
    for (i=2;i>=0;i--){
      if (aryDate[i].length==3){
        if (aryDate[i]>="001" && aryDate[i]<="012"){
       tmpMonth = parseInt(aryDate[i],10)-1;
    switch (i){
      case 0:
        find_tag = "100";
     one_at = parseInt(aryDate[1],10);
     two_at = parseInt(aryDate[2],10);
     break;
      case 1:
        find_tag = "010";
     one_at = parseInt(aryDate[0],10);
     two_at = parseInt(aryDate[2],10);
     break;
      case 2:
        find_tag = "001";
     one_at = parseInt(aryDate[0],10);
     two_at = parseInt(aryDate[1],10);
     break;
    }
     }
   }
    }
    if (find_tag!="000"){
   one_days = this.GetMonthDays(two_at,tmpMonth);
   two_days = this.GetMonthDays(one_at,tmpMonth);
   if ((one_at>one_days)&&(two_at>two_days)){
     return(defDate);
   }
      if ((one_at<=one_days)&&(two_at>two_days)){
     tmpYear = this.GetFormatYear(two_at);
     tmpDay = one_at;
   }
   if ((one_at>one_days)&&(two_at<=two_days)){
     tmpYear = this.GetFormatYear(one_at);
     tmpDay = two_at;
   }
   if ((one_at<=one_days)&&(two_at<=two_days)){
     tmpYear = this.GetFormatYear(one_at);
     tmpDay = two_at;
     tmpDate = this.DateFormat;
     year_at = tmpDate.indexOf("<yyyy>");
     if (year_at == -1){
       year_at = tmpDate.indexOf("<yy>");
     }
     day_at = tmpDate.indexOf("<dd>");
     if (day_at == -1){
       day_at = tmpDate.indexOf("<d>");
     }
     if (year_at >= day_at){
       tmpYear = this.GetFormatYear(two_at);
    tmpDay = one_at;
     }
     }
   return(new Date(tmpYear, tmpMonth, tmpDay));
    }
    find_tag = "000";
    for (i=2;i>=0;i--){
      if (parseInt(aryDate[i],10)>31){
     tmpYear = this.GetFormatYear(parseInt(aryDate[i],10));
     switch (i){
       case 0:
         find_tag = "100";
      one_at = parseInt(aryDate[1],10);
      two_at = parseInt(aryDate[2],10);
      break;
       case 1:
         find_tag = "010";
      one_at = parseInt(aryDate[0],10);
      two_at = parseInt(aryDate[2],10);
      break;
       case 2:
         find_tag = "001";
      one_at = parseInt(aryDate[0],10);
      two_at = parseInt(aryDate[1],10);
      break;
     }
   }
    }
    if (find_tag=="000"){
   tmpDate = this.DateFormat;
   year_at = tmpDate.indexOf("<yyyy>");
   if (year_at == -1){
     year_at = tmpDate.indexOf("<yy>");
   }
   month_at = tmpDate.indexOf("<MMMMMM>");
   if (month_at == -1){
     month_at = tmpDate.indexOf("<MMM>");
   }
   if (month_at == -1){
     month_at = tmpDate.indexOf("<mm>");
   }
   if (month_at == -1){
     month_at = tmpDate.indexOf("<m>");
   }
   day_at = tmpDate.indexOf("<dd>");
   if (day_at == -1){
     day_at = tmpDate.indexOf("<d>");
   }
   if ((year_at>month_at)&&(year_at>day_at)){
     find_tag="001"
   }
   if ((year_at>month_at)&&(year_at<=day_at)){
     find_tag="010";
   }
   if ((year_at<=month_at)&&(year_at>day_at)){
     find_tag="010";
   }
   if ((year_at<=month_at)&&(year_at<=day_at)){
     find_tag="100";
   }
   switch (find_tag){
     case "100":
       tmpYear = parseInt(aryDate[0],10);
    one_at = parseInt(aryDate[1],10);
    two_at = parseInt(aryDate[2],10);
    break;
     case "010":
    one_at = parseInt(aryDate[0],10);
       tmpYear = parseInt(aryDate[1],10);
    two_at = parseInt(aryDate[2],10);
    break;
     case "001":
    one_at = parseInt(aryDate[0],10);
    two_at = parseInt(aryDate[1],10);
       tmpYear = parseInt(aryDate[2],10);
    break;
   }
   tmpYear = this.GetFormatYear(tmpYear);
    }
    if (find_tag!="000"){
      if ((one_at>12)&&(two_at>12)){
     return(defDate);
   }
   if (one_at<=12){
     if (two_at > this.GetMonthDays(tmpYear,one_at-1)){
       return(new Date(tmpYear, one_at-1, this.GetMonthDays(tmpYear,one_at-1)));
     }
     if (two_at>12){
       return(new Date(tmpYear, one_at-1, two_at));
     }
   }
   if (two_at<=12){
     if (one_at > this.GetMonthDays(tmpYear,two_at-1)){
       return(new Date(tmpYear, two_at-1, this.GetMonthDays(tmpYear,two_at-1)));
     }
     if (one_at>12){
       return(new Date(tmpYear, two_at-1, one_at));
     }
   }
   if ((one_at<=12)&&(two_at<=12)){
     tmpMonth = one_at-1;
     tmpDay = two_at;
     tmpDate = this.DateFormat;
     month_at = tmpDate.indexOf("<MMMMMM>");
     if (month_at == -1){
       month_at = tmpDate.indexOf("<MMM>");
     }
     if (month_at == -1){
       month_at = tmpDate.indexOf("<mm>");
     }
     if (month_at == -1){
       month_at = tmpDate.indexOf("<m>");
     }
     day_at = tmpDate.indexOf("<dd>");
     if (day_at == -1){
       day_at = tmpDate.indexOf("<d>");
     }
     if (month_at >= day_at){
       tmpMonth = two_at-1;
    tmpDay = one_at;
     }
      return(new Date(tmpYear, tmpMonth, tmpDay));
   }
    }
  }
  this.CreateYearList = function(MinYear, MaxYear){ //create year list
    var theName = this.Name;
    var theYearObject = document.all.item(theName + "_YearList");
 if (theYearObject == null){
   return;
 }
    var theYear = 0;
    var theYearHTML = "<select id=\"" + theName + "_YearList\" style=\"" + this.YearListStyle + "\" tabIndex=\"-1\" onChange=\"document.jsMonthView.UpdateMonthGrid(this)\" onBlur=\"document.jsMonthView.DeleteMonthGrid()\">";
    for (theYear = MinYear; theYear <= MaxYear; theYear++){
      theYearHTML += "<option value=\"" + theYear.toString() + "\">" + theYear.toString() + "</option>";
    }
    theYearHTML += "</select>";
    theYearObject.outerHTML = theYearHTML;
  }
  this.CreateMonthList = function( ){ //create month list
 var theName = this.Name;
    var theMonthObject = document.all.item(theName + "_MonthList");
 if (theMonthObject == null){
   return;
 }
    var theMonth = 0;
    var theMonthHTML = "<select id=\"" + theName + "_MonthList\" style=\"" + this.MonthListStyle + "\" tabIndex=\"-1\" onChange=\"document.jsMonthView.UpdateMonthGrid(this)\" onBlur=\"document.jsMonthView.DeleteMonthGrid()\">";
    for (theMonth = 0; theMonth < 12; theMonth++){
      theMonthHTML += "<option value=\"" + theMonth.toString() + "\">" + this.MonthName[theMonth] + "</option>";
    }
    theMonthHTML +="</select>";
    theMonthObject.outerHTML = theMonthHTML;
  }
  this.setDayList = function(theYear, theMonth, theDay){ //set the month view show a date
 var theName = this.Name;
    var theDayObject = document.all.item(theName + "_DayList");
 if (theDayObject == null){
   return;
 }
    theDayObject.value = theDay.toString();
    var theFirstDay = new Date(theYear, theMonth, 1);
    var theCurrentDate = new Date();
    var theWeek = theFirstDay.getDay();
    if (theWeek == 0){
      theWeek = 7;
    }
    var theLeftDay = 0;
    if (theMonth == 0){
      theLeftDay = 31;
    }else{
      theLeftDay = this.GetMonthDays(theYear, theMonth - 1);
    }
    var theRightDay = this.GetMonthDays(theYear, theMonth);
    var theCurrentDay = theLeftDay - theWeek + 1;
    var offsetMonth = -1; //the month is previous month
    var theColor = this.InvalidColor;
    var theBgColor = this.UnselectBgColor;
    var theBdColor = theBgColor;
    var WeekId = 0
    var DayId = 0;
    var theStyle = "";
    var theDayHTML = "<table width=\"100%\" height=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
    theDayHTML += "  <tr style=\"" + this.TitleStyle + "\">";
    for (DayId = 0; DayId < 7; DayId++){
      theDayHTML += "  <td width=\"10%\">" + this.WeekName[DayId] + "</td>";
    }
    theDayHTML += "  </tr>";
    theDayHTML += "  <tr>";
 theDayHTML += "   <td colspan=\"7\" style=\"" + this.LineBgStyle + "\">";
    theDayHTML += "    <table style=\"" + this.LineStyle + "\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
    theDayHTML += "     <tr><td></td></tr>";
 theDayHTML += "    </table>";
    theDayHTML += "   </td>";
 theDayHTML += "  </tr>";
    for (WeekId = 0; WeekId < 6; WeekId++){
      theDayHTML += " <tr style=\"" + this.DayStyle + "\">";
      for (DayId = 0; DayId < 7; DayId++){
     if ((theCurrentDay > theLeftDay) && (WeekId < 3)){
       offsetMonth++; //the month is current month;
       theCurrentDay = 1;
     }
     if ((theCurrentDay > theRightDay) && (WeekId > 3)){
       offsetMonth++; //the month is next month;
       theCurrentDay = 1;
     }
     switch (offsetMonth){
       case -1:
      theColor = this.InvalidColor;
      break;
  case 1:
      theColor = this.InvalidColor;
      break;
  case 0:
      if ((DayId==0)||(DayId==6)){
        theColor = this.WeekendColor;
      }else{
        theColor = this.ValidColor;
      }
      break;
     }
     if ((DayId==0)||(DayId==6)){
       theBgColor = this.WeekendBgColor;
     }else{
       theBgColor = this.UnselectBgColor;
     }
     theBdColor = this.DayBdColor;
     if ((theCurrentDay == theDay) && (offsetMonth == 0)){
       theColor = this.SelectedColor;
       theBgColor = this.SelectedBgColor;
    theBdColor = theBgColor;
     }
     if ((theYear == theCurrentDate.getFullYear()) && (theMonth == theCurrentDate.getMonth()) && (theCurrentDay == theCurrentDate.getDate()) && (offsetMonth == 0)){
    theBdColor = this.TodayBdColor;
     }
     theStyle = "border:" + this.DayBdWidth + "px solid " + theBdColor + "; color:" + theColor + "; background-color:" + theBgColor + ";";
     theDayHTML += "  <td style=\"" + theStyle + "\" onMouseOver=\"" + this.OverDayStyle + "\" onMouseOut=\"" + this.OutDayStyle + "\" onMouseDown=\"document.jsMonthView.CreateMonthGrid(" + theYear.toString() + ", " + (theMonth + offsetMonth).toString() + ", " + theCurrentDay.toString() + ")\">";
     theDayHTML += theCurrentDay.toString();
     theDayHTML += "  </td>";
     theCurrentDay++;
   }
   theDayHTML += " </tr>";
    }
    theDayHTML += "  <tr style=\"" + this.FooterStyle + "\" onMouseDown=\"document.jsMonthView.CreateMonthGrid(" + theCurrentDate.getFullYear().toString() + ", " + theCurrentDate.getMonth().toString() + ", " + theCurrentDate.getDate().toString() + ");\">";
    theStyle = "border:" + this.DayBdWidth + "px solid " + this.TodayBdColor + ";";
    theDayHTML += "   <td style=\"" + theStyle + "\"><br></td>";
    theDayHTML += "   <td colspan=\"6\"> " + this.TodayTitle + " " + this.SetDateFormat(theCurrentDate.getFullYear(), theCurrentDate.getMonth(), theCurrentDate.getDate()) + "</td>";
    theDayHTML += "  </tr>";
    theDayHTML += " </table>";
    var theMonthGrid = document.all.item(theName + "_MonthGrid");
    theMonthGrid.innerHTML = theDayHTML;
  }
  this.CreateMonthGrid = function(theYear, theMonth, theDay){ //refresh the month view to the date, main action is run this.setDayList() and set this.Source.value
    var theTextObject = this.Source;
    if (theTextObject == null){
      return;
    }
    var theName = this.Name;
    var theYearObject = document.all.item(theName + "_YearList");
    var theMonthObject = document.all.item(theName + "_MonthList");
    var tmpYear = theYear;
    var tmpMonth = theMonth;
    var tmpDay = 1;
    if (tmpMonth < 0){
      tmpYear--;
   tmpMonth = 11;
    }
    if (tmpMonth > 11){
      tmpYear++;
   tmpMonth = 0;
    }
    if (tmpYear < this.MinYear){
      tmpYear = this.MinYear;
    }
    if (tmpYear > this.MaxYear){
      tmpYear = this.MaxYear;
    }
    if (theDay < 1){
   tmpDay = 1;
 }else{
      tmpDay = this.GetMonthDays(tmpYear, tmpMonth);
      if (theDay < tmpDay){
     tmpDay = theDay;
   }
 }
    theYearObject.value = tmpYear;
    theMonthObject.value = tmpMonth;
    this.setDayList(tmpYear, tmpMonth, tmpDay);
    theTextObject.value = this.SetDateFormat(tmpYear, tmpMonth, tmpDay);
    theTextObject.select();
  }
  this.UpdateMonthGrid = function(theObject){ //run this.CreateMonthGrid() by theObject
    var theTextObject = this.Source;
    if (theTextObject == null){
      return;
    }
 var theName = this.Name;
    var theYearObject = document.all.item(theName + "_YearList");
    var theMonthObject = document.all.item(theName + "_MonthList");
    var theDayObject = document.all.item(theName + "_DayList");
    var tmpName = theObject.id.substr(theObject.id.lastIndexOf("_"));
    switch (tmpName){
      case "_goPreviousMonth": //go previous month button
     theObject.disabled = true;
     this.CreateMonthGrid(parseInt(theYearObject.value, 10), parseInt(theMonthObject.value, 10) - 1, parseInt(theDayObject.value, 10));
     theObject.disabled = false;
     break;
   case "_goNextMonth": //go next month button
     theObject.disabled = true;
     this.CreateMonthGrid(parseInt(theYearObject.value, 10), parseInt(theMonthObject.value, 10) + 1, parseInt(theDayObject.value, 10));
     theObject.disabled = false;
     break;
   case "_YearList": //year list
     this.CreateMonthGrid(parseInt(theYearObject.value, 10), parseInt(theMonthObject.value, 10), parseInt(theDayObject.value, 10));
     break;
   case "_MonthList": //month list
     this.CreateMonthGrid(parseInt(theYearObject.value, 10), parseInt(theMonthObject.value, 10), parseInt(theDayObject.value, 10));
     break;
   default:
     return;
    }
  }
  this.DeleteMonthGrid = function( ){ //check document focus, if blur this.Source then delete this
    var theName = this.Name;
    var theDivObject = document.all.item(theName + "_MonthView");
    if (theDivObject == null){
      return;
    }
    var tmpObject = document.activeElement;
    while (tmpObject != null){
      if (tmpObject == this.Source){
     return;
   }
      //if (tmpObject.id == theName + "_MonthView"){
      //  return;
      //}
   //if (tmpObject.id == theName + "_MonthGrid"){
   //  return;
   //}
   if (tmpObject.id == theName + "_goPreviousMonth"){
     return;
   }
   if (tmpObject.id == theName + "_goNextMonth"){
     return;
   }
   if (tmpObject.id == theName + "_YearList"){
     return;
   }
   if (tmpObject.id == theName + "_MonthList"){
     return;
   } 
   if (tmpObject.id == theName + "_DayList"){
     return;
   }
      tmpObject = tmpObject.parentElement;
    }
    if (tmpObject == null){ //delete the month view
      theDivObject.outerHTML = "";
   var theDate = new Date(this.GetTextDate(this.Source.value));
   if (isNaN(theDate)){
     this.Source.value = "";
   }else{
     this.Source.value = this.SetDateFormat(theDate.getFullYear(), theDate.getMonth(), theDate.getDate());
   }
   this.Source = null;
    }
  }
  this.InitialMonthView = function( ){
    var theName = this.Name;
    var theValue = this.Source.value;
    var theCurrentDate = new Date(this.GetTextDate(theValue));
 if (isNaN(theCurrentDate)){
   theCurrentDate = new Date();
 }
    var theDivHTML = "<div id=\"" + theName + "_MonthView\" onBlur=\"document.jsMonthView.DeleteMonthGrid();\">";
    theDivHTML += "    <table width=\"" + this.Width.toString() + "\" height=\"" + this.Height.toString() + "\" style=\"" + this.MonthGridStyle + "\" cellpadding=\"0\" cellspacing=\"0\">";
    theDivHTML += "      <tr>";
    theDivHTML += "        <td align=\"center\" valign=\"top\">";
    theDivHTML += "          <table width=\"100%\" height=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
    theDivHTML += "            <tr align=\"center\" style=\"" + this.HeaderStyle + "\">";
    theDivHTML += "              <td>";
    theDivHTML += "                <input type=\"button\" tabIndex=\"-1\" style=\"" + this.MonthBtStyle + "\" id=\"" + theName + "_goPreviousMonth\" value=\"" + this.PreviousMonthText + "\" onClick=\"document.jsMonthView.UpdateMonthGrid(this)\" onBlur=\"document.jsMonthView.DeleteMonthGrid()\">";
    theDivHTML += "              </td>";
    theDivHTML += "              <td>";
    theDivHTML += "                <select id=\"" + theName + "_MonthList\">";
    theDivHTML += "                </select>";
    theDivHTML += "              </td>";
    theDivHTML += "              <td>";
    theDivHTML += "                <select id=\"" + theName + "_YearList\">";
    theDivHTML += "                </select>";
    theDivHTML += "                <input type=\"hidden\" id=\"" + theName + "_DayList\" value=\"1\">";
    theDivHTML += "              </td>";
    theDivHTML += "              <td>";
    theDivHTML += "                <input type=\"button\" tabIndex=\"-1\" style=\"" + this.MonthBtStyle + "\" id=\"" + theName + "_goNextMonth\" value=\"" + this.NextMonthText + "\" onClick=\"document.jsMonthView.UpdateMonthGrid(this)\" onBlur=\"document.jsMonthView.DeleteMonthGrid()\">";
    theDivHTML += "              </td>";
    theDivHTML += "            </tr>";
    theDivHTML += "            <tr>";
    theDivHTML += "              <td colspan=\"4\" bgcolor=\"" + this.UnselectBgColor + "\">";
    theDivHTML += "                <div id=\"" + theName + "_MonthGrid\"><br></div>";
    theDivHTML += "              </td>";
    theDivHTML += "            </tr>";
    theDivHTML += "          </table>";
    theDivHTML += "        </td>";
    theDivHTML += "      </tr>";
    theDivHTML += "    </table>";
    theDivHTML += "  </div>";
    document.body.insertAdjacentHTML("beforeEnd", theDivHTML);
    theDivObject = document.all.item(theName + "_MonthView");
    theDivObject.style.position = "absolute";
    theDivObject.style.posLeft = this.GetoffsetLeft(this.Source);
    theDivObject.style.posTop = this.GetoffsetTop(this.Source) + this.Source.offsetHeight;
    this.CreateYearList(this.MinYear, this.MaxYear);
    this.CreateMonthList();
    this.CreateMonthGrid(theCurrentDate.getFullYear(), theCurrentDate.getMonth(), theCurrentDate.getDate());
  }
}
function CreateMonthView(theTextObject){ //the month view create interface, fire at element's onFocus event
  if (theTextObject.readOnly == true){
    return;
  }
  if (document.jsMonthView != null){
    if (document.jsMonthView.Source == theTextObject){
   return;
 }else{
      document.jsMonthView.DeleteMonthGrid();
    }
  }
  document.jsMonthView = new DefineMonthView(theTextObject);
  //insert your code, change the month view propertiy
  //example:
  //  document.jsMonthView.DateFormat = "<MMM> <d>,<yyyy>";
  document.jsMonthView.InitialMonthView();
  theTextObject.select();
}
function DeleteMonthView(theTextObject){ //the month view delete interface, fire at element's onBlur event
  if (document.jsMonthView == null){
    return;
  }
  document.jsMonthView.DeleteMonthGrid();
  if (document.jsMonthView.Source == null){
    document.jsMonthView = null;
  }
}
//-->
