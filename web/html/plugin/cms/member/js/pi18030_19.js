function changebg(A) {
	if (A == 1) {
		getObject("pCardOpen").className = "open hidden";
		getObject("pCardClose").className = "close";
		PassportSC.cElement.className = "passportc";
		PassportSC.cElement.style.display = "block"
	}
	if (A == 2) {
		getObject("pCardOpen").className = "open";
		getObject("pCardClose").className = "close hidden";
		PassportSC.cElement.style.display = "none";
		if (PassportSC.cookie && PassportSC.cookie.userid != "") {
		} else {
			PassportSS.drawPassportMail(PassportSS.banner, PassportSS.ele)
		}
	}
}
function getObject(A) {
	if (document.getElementById && document.getElementById(A)) {
		return document.getElementById(A)
	} else {
		if (document.all && document.all(A)) {
			return document.all(A)
		} else {
			if (document.layers && document.layers[A]) {
				return document.layers[A]
			} else {
				return false
			}
		}
	}
}
var TopUtils = {
	getBID : function(B) {
		var D = TopUtils.getCookie(B);
		var C = D.split("|");
		if (C.length > 3) {
			var A = parseInt(C[2], 16);
			return A
		}
	},
	getCookie : function(C) {
		var F = document.cookie;
		for ( var D = 0; D <= F.length; D++) {
			var A = D + C.length;
			if (F.substring(D, A) == C) {
				var E = A + 1;
				var B = F.indexOf(";", E);
				if (B < E) {
					B = F.length
				}
				return unescape(F.substring(E, B))
			}
		}
		return ""
	},
	Deletecookie : function(A) {
		var C = new Date();
		C.setTime(C.getTime() - 100000);
		var B = TopUtils.GetCookie(A);
		document.cookie = A + "=" + B + "; expires=" + C.toGMTString()
				+ "; path=/; domain=.sohu.com;"
	},
	GetCookie : function(D) {
		var B = D + "=";
		var F = B.length;
		var A = document.cookie.length;
		var E = 0;
		while (E < A) {
			var C = E + F;
			if (document.cookie.substring(E, C) == B) {
				return TopUtils.getCookieVal(C)
			}
			E = document.cookie.indexOf(" ", E) + 1;
			if (E == 0) {
				break
			}
		}
		return null
	},
	getCookieVal : function(B) {
		var A = document.cookie.indexOf(";", B);
		if (A == -1) {
			A = document.cookie.length
		}
		return unescape(document.cookie.substring(B, A))
	}
};
/**
 * 
 */
var PassportSS = {
	cvsid : "$Id: pi18030_19.js 3994 2011-01-05 11:44:40Z orangeforjava $",
	pCardMainDiv : "",
	pUserID : "",
	pNewMail : "",
	pMailUrl : "",
	pDomain : "",
	banner : false,
	ele : false,
	isRedirectMail : false,
	alldomaillist : [ "sohu.com", "sogou.com", "vip.sohu.com", "vip.sohu.net",
			"sohu.net", "2008.sohu.com", "sms.sohu.com", "chinaren.com",
			"17173.com", "focus.cn", "game.sohu.com" ],
	getMailUrl : function() {
		if (this.pUserID == "") {
			return ""
		}
		var A = this.pUserID.lastIndexOf("@");
		this.pDomain = this.pUserID.substr(A + 1);
		if (this.pDomain == "sohu.com") {
			this.pMailUrl = "http://mail.sohu.com"
		} else {
			if (this.pDomain == "sogou.com") {
				this.pMailUrl = "http://mail.sogou.com/2gmail/login.jsp"
			} else {
				this.pMailUrl = ""
			}
		}
	},
	drawPassportMail : function(A, C) {
		if (typeof (C) != "object" || typeof (A) != "object") {
			return
		}
		this.banner = A;
		this.ele = C;
		var B = [ "sohu.com", "sogou.com", "chinaren.com", "vip.sohu.com",
				"vip.sohu.net", "sohu.net", "2008.sohu.com", "sms.sohu.com" ];
		PassportSC.domainList = B;
		PassportSC.selectorTitle = "选择您所要登录的邮箱";
		PassportSC.init(C);
		PassportSC.cElement.className = "";
		if (PassportSC.cookie && PassportSC.cookie.userid != "") {
			PassportSC.cElement.className = "passportc";
			PassportSC.cElement.style.display = "none";
			this.isRedirectMail = false;
			drawPassportCardInit();
			PassportSC.drawPassportCard();
			changebg(2)
		} else {
			this.isRedirectMail = true;
			drawPassportCardInit();
			this.drawPassPortMailLogin()
		}
	},
	drawPassPortMailCard : function() {
		PassportSS.getMailUrl();
		var B = PassportSC.getDisplayName();
		if (B.length > 23) {
			B = B.substr(0, B.lastIndexOf("@"))
		}
		var A = '<div class="left"><a href="http://passport.sohu.com/" target="_blank">通行证</a> | ' + B + '</div><div class="mid"><div id="pmailcontent" class="err">';
		if (PassportSS.pMailUrl != "") {
			A += '<img height=10 alt="" src="http://js.sohu.com/passport/images/letter.gif"> <p>未读邮件（<span>0</span>）积分（<span>0</span>）</p>'
		} else {
			A += "您已经登录搜狐通行证"
		}
		A += '</div><input type="button" class="open hidden" id="pCardOpen" value="打开" onclick="changebg(1)"><input type="button" class="close" id="pCardClose" value="关闭" onclick="changebg(2)"></div>';
		this.banner.innerHTML = A
	},
	drawPassPortMailLogin : function() {
		var A = '<form method="post" onsubmit="return PassportSS.loginHandle();" name="loginform"><input type=hidden name=id value=""><input type=hidden name=password value=""><input type=hidden name=username value=""><input type=hidden name=m value=""><input type=hidden name=domain value=""><input type=hidden name=mpass value=""><input type=hidden name=loginid	value=""><input type=hidden name=passwd	value=""><input type=hidden name=appid  value=""><input type=hidden name=ru     value=""><input type=hidden name=eru    value=""><input type=hidden name=fl     value=""><input type=hidden name=ct     value=""><input type=hidden name=vr     value=""><input type=hidden name=sg     value=""><a href="http://passport.sohu.com/" target="_blank">通行证</a> | 登录名<input id="pemail" type="text" autocomplete="off" value="通行证帐号/手机号" style="color:gray" disableautocomplete size="16" /> 密码<input id="ppwd" type="password" size="11" /> <input type="Submit" name="Submit" value="登录" />  <a href="http://passport.sohu.com/web/reguser?appid=8888" target="_blank">注册</a>&nbsp;<a href="http://passport.sohu.com/help/" target="_blank">帮助</a></form>';
		this.banner.innerHTML = A;
		PassportSC.emailInput = document.getElementById("pemail");
		PassportSC.passwdInput = document.getElementById("ppwd");
		PassportSC.bindSelector()
	},
	loginHandle : function() {
		var D = PassportSC.emailInput.value;
		var A = PassportSC.passwdInput.value;
		//
		var B = D.lastIndexOf("@");
		var E = /^1\d{10}$/;
		if (A == "") {
			if (PassportSC.loginMsg) {
				PassportSC.loginMsg.innerHTML = "请输入密码"
			}
			PassportSC.passwdInput.focus();
			return false
		}
		var I = "";
		var G = "";
		if (E.test(D)) {
			I = "";
			G = D
		} else {
			if (B == -1) {
				I = "sohu.com";
				G = D
			} else {
				I = D.substr(B + 1);
				G = D.substr(0, B)
			}
		}
		var F = document.forms.loginform;
		F.elements.id.value = G;
		F.elements.domain.value = I;
		F.elements.username.value = G;
		F.elements.password.value = A;
		F.elements.m.value = G;
		F.elements.mpass.value = A;
		if (I == "sogou.com") {
			F.elements.loginid.value = D;
			F.elements.passwd.value = A;
			F.elements.appid.value = "1014";
			F.elements.fl.value = "1";
			F.elements.vr.value = "1|1";
			F.elements.ru.value = "http://mail.sogou.com/2gmail/login.jsp";
			F.elements.eru.value = "http://mail.sogou.com";
			F.elements.ct.value = "1160703837";
			F.elements.sg.value = "2bd25fce72ad4127205dad5faa064518";
			F.action = "http://passport.sohu.com/login.jsp"
		}
		if (I == "chinaren.com") {
			F.elements.loginid.value = D;
			F.elements.passwd.value = A;
			F.elements.appid.value = "1005";
			F.elements.fl.value = "1";
			F.elements.vr.value = "1|1";
			F.elements.ru.value = "http://mail.chinaren.com";
			F.elements.eru.value = "http://mail.chinaren.com";
			F.elements.ct.value = "1160703837";
			F.elements.sg.value = "f1de50cd5769dfbbb8c22ecf55a83ea7";
			F.action = "http://passport.sohu.com/login.jsp"
		} else {
			if (I == "vip.sohu.com") {
				F.elements.loginid.value = D;
				F.elements.passwd.value = A;
				F.elements.appid.value = "1013";
				F.elements.fl.value = "1";
				F.elements.vr.value = "1|1";
				F.elements.ru.value = "http://vip.sohu.com/login/viplogin11.jsp";
				F.elements.eru.value = "http://vip.sohu.com/login/viplogin11.jsp";
				F.elements.ct.value = "1160703932";
				F.elements.sg.value = "d058584c77da4a4e8e309e2c0561ebeb";
				F.action = "http://passport.sohu.com/login.jsp"
			} else {
				if (I == "vip.sohu.net" || I == "sohu.net"
						|| I == "2008.sohu.com") {
					F.action = "http://mail.sohu.net/control/login"
				} else {
					if (I == "sms.sohu.com") {
						F.action = "http://sms.sohu.com/userlogin.php"
					} else {
						var C = '<div class="left"><a target="_blank" href="http://passport.sohu.com/">通行证</a> | </div><div class="mid"><div class="err" id="loginMsg"></div><input type="button" class="open hidden" id="pCardOpen" value="打开" onclick="changebg(1)"><input type="button" class="close" id="pCardClose" value="关闭" onclick="changebg(2)"></div>';
						PassportSS.banner.innerHTML = C;
						PassportSC.cElement.className = "passportc";
						PassportSC.cElement.style.display = "block";
						var H = "正在登录搜狐通行证，请稍候...";
						document.getElementById("loginMsg").innerHTML = H;
						PassportSC.drawPassportWait(H);
						if (I == "") {
							PassportSC.domainList = [ "sohu.com", "sogou.com" ]
						}
						if (I == "" || I == "sohu.com") {
							PassportSC.parseAppid();
							PassportSC.getBottomRow()
						}
						return PassportSC.loginHandle(
								PassportSC.emailInput.value,
								PassportSC.passwdInput.value, "0",
								PassportSC.sElement, PassportSC.loginFailCall
										.bindFunc(PassportSC),
								PassportSC.loginSuccessCall
										.bindFunc(PassportSC))
					}
				}
			}
		}
		F.submit()
	},
	sohu3Init : function() {
		var A = '<div class="ssblog"><a target="_blank" href="http://passport.sohu.com/">通行证</a> | <span><a href="http://blog.sohu.com/2007" class="convention">第二届搜狐博客大会暨搜狐3.0落成大典7月21日开幕!</a></span></div><input type="button" class="open hidden" id="pCardOpen" value="打开" onclick="changebg(1)"><input type="button" class="close" id="pCardClose" value="关闭" onclick="changebg(2)">';
		PassportSS.banner.innerHTML = A;
		PassportSC.cElement.className = "passportc";
		PassportSC.cElement.style.display = "block";
		PassportSC.drawLoginForm();
		var B = '<a href="http://blog.sohu.com/2007">第二届搜狐博客大会暨搜狐3.0落成大典7月21日开幕!</a>'
	}
};
function getNewMailCount() {
	var B = document.createElement("script");
	var A = TopUtils.getBID("SOHUID");
	if (A) {
		B.src = "http://register.mail.sohu.com/servlet/GetMailContent?bucketID="
				+ A
	} else {
		B.src = "http://register.mail.sohu.com/servlet/GetMailContent"
	}
	PassportSC.sElement.appendChild(B)
}
function passportSSInit() {
	drawAppInfo = function(node) {
		PassportSC.parsePassportCookie();
		PassportSS.pUserID = PassportSC.cookie.userid;
		PassportSS.drawPassPortMailCard();
		var vlink = document.getElementById("pmailcontent");
		vlink.onclick = PassportSC.doClickLink.bindFunc(PassportSC);
		if (PassportSS.pUserID.indexOf("@sohu.com") > 0) {
			PassportSS.pNewMail = PassportSC.cookie.newmail_num;
			try {
				_drawAppInfo(node)
			} catch (e) {
				PassportSC.drawPassportInfo()
			}
		} else {
			PassportSC.drawPassportInfo()
		}
	};
	PassportCardList[0] = PassportSC;
	PassportSC.successCalledFunc = eval("drawAppInfo");
	logoutApp = function() {
		PassportSC.cElement.className = "";
		PassportSC.cElement.innerHTML = "";
		PassportSS.isRedirectMail = true;
		PassportSS.drawPassPortMailLogin();
		PassportSS.pMailUrl = "";
		TopUtils.Deletecookie("SOHUID")
	};
	PassportSC.showMsg = function(msg) {
		var e = document.getElementById("loginMsg");
		if (e != null) {
			e.innerHTML = msg
		}
	};
	PassportSC._drawLoginForm = function() {
		this.cElement.innerHTML = '<form method="post" onsubmit="return PassportSC.doLogin();" name="loginform"><div class="passportc_title"><span>搜狐<b>通行证</b></span><div class="ppthree">'
				+ PassportSC.cardTitle
				+ '</div><img src="http://js.sohu.com/passport/images/blogpic002.gif" /></div><div class="passportc_content" id="ppcontid"><ul class="card"><div id="pperrmsg" class="err">&nbsp;</div><li>登录名 <input name="email" type="text" class="ppinput" autocomplete="off" value="通行证帐号/手机号"  style="color:gray;"  disableautocomplete /></li><li>密&nbsp;&nbsp;码 <input name="password" type="password" class="ppinput" autocomplete="off" disableautocomplete /></li><li><span class="login"><input name="persistentcookie" type="checkbox" value="1" '
				+ PassportSC.defualtRemPwd
				+ ' />记住密码</span><input type="image" class="info" value="登 录" src="http://js.sohu.com/passport/images/blogpic004.gif" alt="登 录" cache /></li><li class="info"><a href="'
				+ this.registerUrl
				+ '" target="_blank">注册新用户</a><a href="'
				+ this.recoverUrl
				+ '" target="_blank">忘记密码</a><a href="http://passport.sohu.com/help/" target="_blank">帮助中心</a></li></ul></div></form>';
		PassportSC.showEmailInputTip = false
	};
	PassportSC.drawPassportInfo = function() {
		html = '<div class="candle" id="ppdefaultim"></div><ul><li>'
				+ PassportSC.getDisplayName()
				+ "</li><li><p>欢迎您，您已经成功登录搜狐通行证！ </p></li>现在即可畅游搜狐所有服务。</li></ul>";
		this.iElement.innerHTML = html;
		var isIE = window.ActiveXObject ? true : false;
		if (isIE) {
			var flashmenu = document.getElementById("ppdefaultim");
			var uid = this.cookie.userid;
			flashmenu.innerHTML = '<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"  codebase="http://images.chinaren.com/product/webim/mood/mood.swf?UserID='
					+ uid
					+ '"   width="220" height="90">    <param name="movie" value="http://images.chinaren.com/product/webim/mood/mood.swf?UserID='
					+ uid
					+ '"><param name="wmode" value="transparent"><param name="allowscriptaccess" value="always"> <embed src="http://images.chinaren.com/product/webim/mood/mood.swf?UserID='
					+ uid
					+ '" wmode="transparent" quality="high" allowscriptaccess="always" bgcolor="#ffffff" width="220" height="90" type="application/x-shockwave-flash"/></object>'
		}
	};
	PassportSC.drawPassportWait = function(str) {
		this.cElement.innerHTML = '<div class="passportc_title"><span>搜狐<b>通行证</b></span><div class="ppthree">'
				+ PassportSC.cardTitle
				+ '</div><img src="http://js.sohu.com/passport/images/blogpic002.gif" /></div><div class="passportc_content" id="ppcontid"><div class="ppWaitMsg">'
				+ str + "</div></div>"
	}
}
function drawPassportCardInit() {
	PassportSC._drawPassportCard = function() {
		if (PassportSS.isRedirectMail) {
			var C = PassportSC.cookie.emt;
			if (C == "1") {
				PassportSC.addCookie("crossdomain", this.getTime(), 336);
				PassportSC
						.gotohref("http://mail.sohu.com/servlet/LoginServlet?appid=9999");
				return
			}
		}
		var E = '<div class="passportc_title"><span>搜狐<b>通行证</b></span><div class="ppthree">' + PassportSC.cardTitle + '</div><a href="javascript:PassportSC.doLogout();">退出</a></div><div class="passportc_content" id="ppcontid"><div class="listContA"></div><div class="middle"><ul>';
		if (this.defaultApp != "") {
			E += '<li class="current">' + this.defaultApp + "</li>"
		}
		E += '<li><img src="http://js.sohu.com/passport/images/blogpic005.gif" alt="去"/></li>';
		for ( var D = 0; D < this.bottomRow[0]["length"]; D++) {
			E += '<li><a href="' + this.bottomRow[0][D]["url"]
					+ '" target="_blank">' + this.bottomRow[0][D]["name"]
					+ "</a></li>";
			if (D != (this.bottomRow[0].length - 1)) {
				E += "<li>|</li>"
			}
		}
		E += '</ul></div><div class="bottom"><ul>';
		for ( var D = 0; D < this.bottomRow[1]["length"]; D++) {
			E += '<li><a href="' + this.bottomRow[1][D]["url"]
					+ '" target="_blank">' + this.bottomRow[1][D]["name"]
					+ "</a></li>";
			if (D != (this.bottomRow[1].length - 1)) {
				E += "<li>|</li>"
			}
		}
		E += '<li class="dabenying">';
		var B = PassportSC.campUrl + this.appid;
		var A = this.cookie.userid.lastIndexOf("@");
		if (A > 0) {
			var F = this.cookie.userid.substr(A + 1);
			if (PassportSS.alldomaillist.toString().indexOf(F) < 0) {
				B = "http://login.sogou.com/modify.jsp"
			}
		}
		E += '<a href="' + B + '" target="_blank"><img src="'
				+ PassportSC.campImg + '" alt="' + PassportSC.campImgAlt
				+ '" border="0" /></a></li></ul></div></div>';
		this.cElement.innerHTML = E
	}
};