
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

