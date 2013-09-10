fm_ini()
function fm_ini(){
	var fm,i,j
	for(i=0;i<document.forms.length;i++){
		fm=document.forms[i]
		for(j=0;j<fm.length;j++){
			if((fm[j].alt+"").indexOf(":")==-1)
				continue
			oo("chk_"+fm[j].name).style.color="red";			
			fm[j].onblur=function(){this.style.background=(tx_chk(this)==true)?'#EEEEEE':'#FF0000'}
			fm[j].onfocus=function(){this.style.background='#CCFF99'}
		}
	}
}
function fm_chk(fm){
	var isPass=true
	for(var i=0;i<fm.length;i++){
		if((fm[i].alt+"").indexOf(":")==-1)
			continue
		
		if((fm[i].name=='safe_num' || fm[i].name=='safe_num2' || fm[i].name=='identityinfo' || fm[i].name=='identityNum'))
			continue
		if(!tx_chk(fm[i])){
			isPass=false;
			fm[i].style.background='#FF0000';
		}
	}
	if(isPass){
		return true
	}else{
		alert("您填写的信息有误，请根据页面红字更改！")
		return false
	}
}





function tx_chk(obj){
	var name,key,val=obj.value,oShow=oo("chk_"+obj.name)
	name=obj.alt.slice(0,obj.alt.indexOf(":"))
	key="/"+obj.alt.slice(obj.alt.indexOf(":")+1)+"/"
	oo("chk_"+obj.name).style.display="none"
	if(key.indexOf("/无内容/")>-1&&val==""){
		name = (name=="password")?"密码":name;
		oShow.innerHTML="请输入"+name
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/高级无内容/")>-1){
		val=ltrim(val);
		val=rtrim(val);
		if(val==""){
			name = (name=="password")?"密码":name;
			oShow.innerHTML="请输入"+name
			oShow.style.display=""
			return false
		}
	}
	if(key.indexOf("/4-16/")>-1&&(strLen(val)<4||strLen(val)>16)){
		oShow.innerHTML="长度必须4-16位"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/6-16/")>-1&&(strLen(val)<6||strLen(val)>16)&&strLen(val)!=0){
		oShow.innerHTML="长度必须6-16位"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/8-16/")>-1&&(strLen(val)<8||strLen(val)>16)){
		oShow.innerHTML="长度必须8-16位"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/3-4/")>-1&&(strLen(val)<3||strLen(val)>4)){
		oShow.innerHTML="长度必须3-4位"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/5-12")>-1&&(strLen(val)<5||strLen(val)>12)){
		oShow.innerHTML="长度必须5-12位"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/2-12")>-1&&(strLen(val)<2||strLen(val)>12)){
		oShow.innerHTML="长度必须2-12位"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/20-1000")>-1&&(strLen(val)<20||strLen(val)>1000)){
		oShow.innerHTML="长度必须20-1000位"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/0-1000")>-1&&(strLen(val)<0||strLen(val)>1000)){
		oShow.innerHTML="长度必须1000位以内"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/怪字符/")>-1&&(/>|<|,|\[|\]|\{|\}|\?|\/|\+|=|\||\'|\\|\"|:|;|\~|\!|\@|\#|\*|\$|\%|\^|\&|\(|\)|`/i).test(val)){
		oShow.innerHTML="请勿使用特殊字符"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/有空格/")>-1&&val.indexOf(" ")>-1){
		oShow.innerHTML="不能包含空格符"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/日期/")>-1&&val!=""&&validateDate(val)!=0){
		oShow.innerHTML="错误的日期值"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/全数字/")>-1&&val!=""&&!isNaN(val)){
		oShow.innerHTML="不可以全是数字"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/数字/")>-1&&val!=""&&isNaN(val)){
		oShow.innerHTML="必须全是数字"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/有大写/")>-1&&/[A-Z]/.test(val)){
		oShow.innerHTML="不能有大写字母"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/英文数字/")>-1&&!/^[a-zA-Z0-9_]*$/.test(val)){
		oShow.innerHTML="只能为英文和数字"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/有全角/")>-1&&/[ａ-ｚ０-９]/.test(val)){
		oShow.innerHTML="不能有全角字符"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/有汉字/")>-1&&escape(val).indexOf("%u")>-1){
		oShow.innerHTML="不能有汉字"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/邮件/")>-1&&(validateEmail(val)==1)){
		oShow.innerHTML="非法的邮件地址"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/下划线/")>-1&&val.slice(val.length-1)=="_"){
		oShow.innerHTML="下划线不能在最后"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/确认密码/")>-1){
		if(obj.form[name].value!=val){
			oShow.innerHTML="确认密码不一致"
			oShow.style.display=""
			return false
		}
	}
	if(key.indexOf("/确认安全码/")>-1){
		if(obj.form[name].value!=val){
			oShow.innerHTML="确认安全码不一致"
			oShow.style.display=""
			return false
		}
	}
	if(key.indexOf("/确认证件号码/")>-1){
		if(obj.form[name].value!=val){
			oShow.innerHTML="确认证件号码不一致"
			oShow.style.display=""
			return false
		}
	}
	if(key.indexOf("/请选择/")>-1&&val=="请选择"){
		oShow.innerHTML=name+"必须选择"
		oShow.style.display=""
		return false
	}
	if(key.indexOf("/必选/")>-1){
		var ol=obj.form[obj.name],isSel=false
		for(var i=0;i<ol.length;i++){
			if(ol[i].checked)
				isSel=true
		}
		if(!isSel){
			oShow.innerHTML=name+"必须选择"
			oShow.style.display=""
			return false
		}
	}
	if(key.indexOf("/条款/")>-1){
		var ol=obj.form[obj.name],isSel=false
		if(ol.checked){
			isSel=true
		}
		if(!isSel){
			oShow.innerHTML=name+"必须选择"
			oShow.style.display=""
			return false
		}
	}
	if(key.indexOf("/电话/")>-1){
		if(validatePhoneNumber(obj)!=0){
			oShow.innerHTML=name+"格式不正确"
			oShow.style.display=""
			return false
		}//
	}
	if(key.indexOf("/传真/")>-1){
		if(validateFaxNumber(obj)!=0){
			oShow.innerHTML=name+"格式不正确"
			oShow.style.display=""
			return false
		}//
	}
	if(key.indexOf("/邮编/")>-1){
		if(validatePost(obj)!=0){
			oShow.innerHTML=name+"格式不正确"
			oShow.style.display=""
			return false
		}
	}
	return true
}
function strLen(key){
	var l=escape(key),len
	len=l.length-(l.length-l.replace(/\%u/g,"u").length)*4
	l=l.replace(/\%u/g,"uu")
	len=len-(l.length-l.replace(/\%/g,"").length)*2
	return len
}
function oo(obj){
	return document.getElementById(obj)
}
////valiator
function checkByteLength(str,minlen,maxlen)
{
    if (str == null) return false;
    var l = str.length;
    var blen = 0;
    for(i=0; i<l; i++)
    {
        if ((str.charCodeAt(i) & 0xff00) != 0)
        {
            blen ++;
        }
        blen ++;
    }
    if (blen > maxlen || blen < minlen)
    {
        return false;
    }
    return true;
}
/**
* validate the user name
**/
function validateUsername(obj)
{
    var str = obj.value;
    var patn = /^[a-zA-Z0-9]+$/;
    if(!checkByteLength(str,4,20)) return 1;
    if(!patn.test(str))
    {
        return 1;
    }
    return 0;
}
function validatePassword(obj)
{
    var str = obj.value;
    if(!checkByteLength(str,6,20)) return 1;
    var patn1 = /^[a-zA-Z0-9_]+$/;
    if(!patn1.test(str) ) return 1;
    return 0;
}
function validateSafePassword(obj)
{
    var str = obj.value;
    if(str != document.getElementById("password").value) return 1;
    return 0;
}
function validateEmail(obj)
{
    var str = obj;
    if(!checkByteLength(str,1,50)) return 1;
    var patn = /^[_a-zA-Z0-9\-]+(\.[_a-zA-Z0-9\-]*)*@[a-zA-Z0-9\-]+([\.][a-zA-Z0-9\-]+)+$/;
    if(patn.test(str))
    {
        return 0;
    }
    else
    {
        return 1;
    }
}
function validateUrl(obj)
{
    var str = obj.value;
    if(str.length>50) return 1;
    var patn = /http(s)?:\/\/[a-zA-Z0-9\-]+([\.][a-zA-Z0-9\-]+)+([\/][a-zA-Z0-9\-]+[\/]*)*$/;
    if(patn.test(str))
    {
        return 0;
    }
    else
    {
        return 2;
    }
}
function validateDate(obj){
	var str =obj;
	var path=/\d{4}-\d{2}-\d{2}/;
	if(path.test(str)){
		return 0;
	}else{
		return 2;
	}
}
function validateNum(obj)
{
    var str = obj.value;
    var patn = new RegExp("\\d{"+getAttrValueByName(obj,"minlen")+","+getAttrValueByName(obj,"maxlen")+"}");
    if(patn.test(str)) return 0;
    return 1;
}
function validateMobile(obj)
{
    var str1 = obj.value;
    var str = tot(str1);
    obj.value = str;
    if(str.length > 16)
    {
        return 1;
    }
    if(str.length == 0)
    {
        return -1;
    }
    var patn = /^[0-9]+$/;
    if(patn.test(str)) return 0;
    return 2;
}
function validatePhoneArea(obj)
{
    var str1 = obj.value;
    var str = tot(str1);
    obj.value = str;
    if(str.length == 0)
    {
        return -1;
    }
    var patn = /^[0-9]+$/;
    if(!patn.test(str)) return 1;
    return validatePhone();
}
function validateFaxArea(obj)
{
    var str1 = obj.value;
    var str = tot(str1);
    obj.value = str;
    if(str.length == 0)
    {
        return -1;
        return
    }
    var patn = /^[0-9]+$/;
    if(!patn.test(str)) return 1;
    return validateFax();
}
function validateArea(obj)
{
    var str1 = obj.value;
    var str = tot(str1);
    obj.value = str;
    if(str.length == 0)
    {
        return -1;
        return
    }
    var patn = /^[0-9]+$/;
    if(!patn.test(str)) return 1;
    return 0;
}
function validatePhone()
{
    if(validateArea(document.getElementById("phone_area")) == 0 && validateNumber(document.getElementById("phone_number")) == 0)
    {
        return 0;
    }
    else
    {
        return -1
    }
}
function validateFax()
{
    if(validateArea(document.getElementById("fax_area")) == 0 && validateNumber(document.getElementById("fax_number")) == 0)
    {
        return 0;
    }
    else
    {
        return -1
    }
}
function validateNumber(obj)
{
    var str1 = obj.value;
    var str = tot(str1);
    obj.value = str;
    if(str.length == 0)
    {
        return -1;
    }
    var patn = /^[0-9-\/]+$/;
    if(!patn.test(str)) return 1;
    return 0;
}
function validatePhoneNumber(obj)
{
    var str1 = obj.value;
    var str = tot(str1);
    obj.value = str;
    if(str.length == 0)
    {
        return 0;
    }
    var patn = /^[0-9-\/]+$/;
    if(!patn.test(str)) return 1;
	return 0;
    //return validatePhone();
}
function validateFaxNumber(obj)
{
    var str1 = obj.value;
    var str = tot(str1);
    obj.value = str;
    if(str.length == 0)
    {
        return 0;
    }
    var patn = /^[0-9-\/]+$/;
    if(!patn.test(str)) return 1;
	return 0;
    //return validateFax();
}
function validatePost(obj)
{
    var str1 = obj.value;
    
    var str = tot(str1);
    obj.value = str;
	if(str==""){
		return 0;
	}
	if(str.length!=6)
    {
        return 1;
    }
    if(str.length == 0)
    {
        return 0;
    }
    var patn = /^[0-9]+$/;
    if(!patn.test(str)) return 1;
    return 0;
}
function validateCompany(obj)
{
    var str = obj.value;
    if(str.length > 50)
    {
        return 1;
    }
    if(str.length == 0)
    {
        return -1;
    }
    if(checkDenyWords(str) != "")
    {
        return 2;
    }
    return 0;
}
function validateLegalPerson(obj)
{
    var str = obj.value;
    if(str.length > 10)
    {
        return 1;
    }
    if(str.length == 0)
    {
        return -1;
    }
    if(checkDenyWords(str) != "")
    {
        return 2;
    }
    return 0;
}
function validateJobTitle(obj)
{
    var str = obj.value;
    if(str.length > 16)
    {
        return 1;
    }
    if(str.length == 0)
    {
        return -1;
    }
    if(checkDenyWords(str) != "")
    {
        return 2;
    }
    return 0;
}
function validateLongro(obj)
{
    var str = obj.value;
    if(str.length > 500)
    {
        return 1;
    }
    if(str.length == 0)
    {
        return -1;
    }
    return 0;
}
function validateFirstName(obj)
{
    var str = obj.value;
    if(str.length > 32)
    {
        return 1;
    }
    if(str.length == 0)
    {
        return -1;
    }
    if(checkDenyWords(str) != "")
    {
        return 2;
    }
    return 0;
}
function validateAddress(obj)
{
    var str = obj.value;
    if(str.length > 80)
    {
        return 1;
    }
    if(str.length == 0)
    {
        return -1;
    }
    return 0;
}
function validateCheckCode(obj)
{
    var str = obj.value;
    var patn = /^[0-9a-zA-Z]{4}$/;
    if(patn.test(str)) return 0;
    return 1;
}
function ltrim(str) 
{ 
        var _str = " "+str; 
        var re = /[ ]+([\w|\W]*)/gi; 
        _str.match(re); 
        return RegExp.$1; 
} 
 
function rtrim(str) 
{ 
        var _str = str+" "; 
        var re = /([\w|\W]*[^ ])[ ]+/gi; 
        _str.match(re); 
        return RegExp.$1; 
} 
function tot(mobnumber)
{
    while(mobnumber.indexOf("０")!=-1)
    {
        mobnumber = mobnumber.replace("０","0");
    }
    while(mobnumber.indexOf("１")!=-1)
    {
        mobnumber = mobnumber.replace("１","1");
    }
    while(mobnumber.indexOf("２")!=-1)
    {
        mobnumber = mobnumber.replace("２","2");
    }
    while(mobnumber.indexOf("３")!=-1)
    {
        mobnumber = mobnumber.replace("３","3");
    }
    while(mobnumber.indexOf("４")!=-1)
    {
        mobnumber = mobnumber.replace("４","4");
    }
    while(mobnumber.indexOf("５")!=-1)
    {
        mobnumber = mobnumber.replace("５","5");
    }
    while(mobnumber.indexOf("６")!=-1)
    {
        mobnumber = mobnumber.replace("６","6");
    }
    while(mobnumber.indexOf("７")!=-1)
    {
        mobnumber = mobnumber.replace("７","7");
    }
    while(mobnumber.indexOf("８")!=-1)
    {
        mobnumber = mobnumber.replace("８","8");
    }
    while(mobnumber.indexOf("９")!=-1)
    {
        mobnumber = mobnumber.replace("９","9");
    }
    return mobnumber;
}