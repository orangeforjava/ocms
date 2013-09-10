var hexcase = 0;
var chrsz = 8;
function hex_md5(A) {
	return binl2hex(core_md5(str2binl(A), A.length * chrsz))
}
function core_md5(K, F) {
	K[F >> 5] |= 128 << ((F) % 32);
	K[(((F + 64) >>> 9) << 4) + 14] = F;
	var J = 1732584193;
	var I = -271733879;
	var H = -1732584194;
	var G = 271733878;
	for ( var C = 0; C < K.length; C += 16) {
		var E = J;
		var D = I;
		var B = H;
		var A = G;
		J = md5_ff(J, I, H, G, K[C + 0], 7, -680876936);
		G = md5_ff(G, J, I, H, K[C + 1], 12, -389564586);
		H = md5_ff(H, G, J, I, K[C + 2], 17, 606105819);
		I = md5_ff(I, H, G, J, K[C + 3], 22, -1044525330);
		J = md5_ff(J, I, H, G, K[C + 4], 7, -176418897);
		G = md5_ff(G, J, I, H, K[C + 5], 12, 1200080426);
		H = md5_ff(H, G, J, I, K[C + 6], 17, -1473231341);
		I = md5_ff(I, H, G, J, K[C + 7], 22, -45705983);
		J = md5_ff(J, I, H, G, K[C + 8], 7, 1770035416);
		G = md5_ff(G, J, I, H, K[C + 9], 12, -1958414417);
		H = md5_ff(H, G, J, I, K[C + 10], 17, -42063);
		I = md5_ff(I, H, G, J, K[C + 11], 22, -1990404162);
		J = md5_ff(J, I, H, G, K[C + 12], 7, 1804603682);
		G = md5_ff(G, J, I, H, K[C + 13], 12, -40341101);
		H = md5_ff(H, G, J, I, K[C + 14], 17, -1502002290);
		I = md5_ff(I, H, G, J, K[C + 15], 22, 1236535329);
		J = md5_gg(J, I, H, G, K[C + 1], 5, -165796510);
		G = md5_gg(G, J, I, H, K[C + 6], 9, -1069501632);
		H = md5_gg(H, G, J, I, K[C + 11], 14, 643717713);
		I = md5_gg(I, H, G, J, K[C + 0], 20, -373897302);
		J = md5_gg(J, I, H, G, K[C + 5], 5, -701558691);
		G = md5_gg(G, J, I, H, K[C + 10], 9, 38016083);
		H = md5_gg(H, G, J, I, K[C + 15], 14, -660478335);
		I = md5_gg(I, H, G, J, K[C + 4], 20, -405537848);
		J = md5_gg(J, I, H, G, K[C + 9], 5, 568446438);
		G = md5_gg(G, J, I, H, K[C + 14], 9, -1019803690);
		H = md5_gg(H, G, J, I, K[C + 3], 14, -187363961);
		I = md5_gg(I, H, G, J, K[C + 8], 20, 1163531501);
		J = md5_gg(J, I, H, G, K[C + 13], 5, -1444681467);
		G = md5_gg(G, J, I, H, K[C + 2], 9, -51403784);
		H = md5_gg(H, G, J, I, K[C + 7], 14, 1735328473);
		I = md5_gg(I, H, G, J, K[C + 12], 20, -1926607734);
		J = md5_hh(J, I, H, G, K[C + 5], 4, -378558);
		G = md5_hh(G, J, I, H, K[C + 8], 11, -2022574463);
		H = md5_hh(H, G, J, I, K[C + 11], 16, 1839030562);
		I = md5_hh(I, H, G, J, K[C + 14], 23, -35309556);
		J = md5_hh(J, I, H, G, K[C + 1], 4, -1530992060);
		G = md5_hh(G, J, I, H, K[C + 4], 11, 1272893353);
		H = md5_hh(H, G, J, I, K[C + 7], 16, -155497632);
		I = md5_hh(I, H, G, J, K[C + 10], 23, -1094730640);
		J = md5_hh(J, I, H, G, K[C + 13], 4, 681279174);
		G = md5_hh(G, J, I, H, K[C + 0], 11, -358537222);
		H = md5_hh(H, G, J, I, K[C + 3], 16, -722521979);
		I = md5_hh(I, H, G, J, K[C + 6], 23, 76029189);
		J = md5_hh(J, I, H, G, K[C + 9], 4, -640364487);
		G = md5_hh(G, J, I, H, K[C + 12], 11, -421815835);
		H = md5_hh(H, G, J, I, K[C + 15], 16, 530742520);
		I = md5_hh(I, H, G, J, K[C + 2], 23, -995338651);
		J = md5_ii(J, I, H, G, K[C + 0], 6, -198630844);
		G = md5_ii(G, J, I, H, K[C + 7], 10, 1126891415);
		H = md5_ii(H, G, J, I, K[C + 14], 15, -1416354905);
		I = md5_ii(I, H, G, J, K[C + 5], 21, -57434055);
		J = md5_ii(J, I, H, G, K[C + 12], 6, 1700485571);
		G = md5_ii(G, J, I, H, K[C + 3], 10, -1894986606);
		H = md5_ii(H, G, J, I, K[C + 10], 15, -1051523);
		I = md5_ii(I, H, G, J, K[C + 1], 21, -2054922799);
		J = md5_ii(J, I, H, G, K[C + 8], 6, 1873313359);
		G = md5_ii(G, J, I, H, K[C + 15], 10, -30611744);
		H = md5_ii(H, G, J, I, K[C + 6], 15, -1560198380);
		I = md5_ii(I, H, G, J, K[C + 13], 21, 1309151649);
		J = md5_ii(J, I, H, G, K[C + 4], 6, -145523070);
		G = md5_ii(G, J, I, H, K[C + 11], 10, -1120210379);
		H = md5_ii(H, G, J, I, K[C + 2], 15, 718787259);
		I = md5_ii(I, H, G, J, K[C + 9], 21, -343485551);
		J = safe_add(J, E);
		I = safe_add(I, D);
		H = safe_add(H, B);
		G = safe_add(G, A)
	}
	return Array(J, I, H, G)
}
function md5_cmn(F, C, B, A, E, D) {
	return safe_add(bit_rol(safe_add(safe_add(C, F), safe_add(A, D)), E), B)
}
function md5_ff(C, B, G, F, A, E, D) {
	return md5_cmn((B & G) | ((~B) & F), C, B, A, E, D)
}
function md5_gg(C, B, G, F, A, E, D) {
	return md5_cmn((B & F) | (G & (~F)), C, B, A, E, D)
}
function md5_hh(C, B, G, F, A, E, D) {
	return md5_cmn(B ^ G ^ F, C, B, A, E, D)
}
function md5_ii(C, B, G, F, A, E, D) {
	return md5_cmn(G ^ (B | (~F)), C, B, A, E, D)
}
function safe_add(A, D) {
	var C = (A & 65535) + (D & 65535);
	var B = (A >> 16) + (D >> 16) + (C >> 16);
	return (B << 16) | (C & 65535)
}
function bit_rol(A, B) {
	return (A << B) | (A >>> (32 - B))
}
function binl2hex(C) {
	var B = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
	var D = "";
	for ( var A = 0; A < C.length * 4; A++) {
		D += B.charAt((C[A >> 2] >> ((A % 4) * 8 + 4)) & 15)
				+ B.charAt((C[A >> 2] >> ((A % 4) * 8)) & 15)
	}
	return D
}
function str2binl(D) {
	var C = Array();
	var A = (1 << chrsz) - 1;
	for ( var B = 0; B < D.length * chrsz; B += chrsz) {
		C[B >> 5] |= (D.charCodeAt(B / chrsz) & A) << (B % 32)
	}
	return C
}
function b64_423(E) {
	var D = new Array("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
			"Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
			"l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
			"y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-",
			"_");
	var F = new String();
	for ( var C = 0; C < E.length; C++) {
		for ( var A = 0; A < 64; A++) {
			if (E.charAt(C) == D[A]) {
				var B = A.toString(2);
				F += ("000000" + B).substr(B.length);
				break
			}
		}
		if (A == 64) {
			if (C == 2) {
				return F.substr(0, 8)
			} else {
				return F.substr(0, 16)
			}
		}
	}
	return F
}
function b2i(D) {
	var A = 0;
	var B = 128;
	for ( var C = 0; C < 8; C++, B = B / 2) {
		if (D.charAt(C) == "1") {
			A += B
		}
	}
	return String.fromCharCode(A)
}
function b64_decodex(D) {
	var B = new Array();
	var C;
	var A = "";
	for (C = 0; C < D.length; C += 4) {
		A += b64_423(D.substr(C, 4))
	}
	for (C = 0; C < A.length; C += 8) {
		B += b2i(A.substr(C, 8))
	}
	return B
}
function utf8to16(I) {
	var D, F, E, G, H, C, B, A, J;
	D = [];
	G = I.length;
	F = E = 0;
	while (F < G) {
		H = I.charCodeAt(F++);
		switch (H >> 4) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
			D[E++] = I.charAt(F - 1);
			break;
		case 12:
		case 13:
			C = I.charCodeAt(F++);
			D[E++] = String.fromCharCode(((H & 31) << 6) | (C & 63));
			break;
		case 14:
			C = I.charCodeAt(F++);
			B = I.charCodeAt(F++);
			D[E++] = String.fromCharCode(((H & 15) << 12) | ((C & 63) << 6)
					| (B & 63));
			break;
		case 15:
			switch (H & 15) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				C = I.charCodeAt(F++);
				B = I.charCodeAt(F++);
				A = I.charCodeAt(F++);
				J = ((H & 7) << 18) | ((C & 63) << 12) | ((B & 63) << 6)
						| (A & 63) - 65536;
				if (0 <= J && J <= 1048575) {
					D[E] = String.fromCharCode(((J >>> 10) & 1023) | 55296,
							(J & 1023) | 56320)
				} else {
					D[E] = "?"
				}
				break;
			case 8:
			case 9:
			case 10:
			case 11:
				F += 4;
				D[E] = "?";
				break;
			case 12:
			case 13:
				F += 5;
				D[E] = "?";
				break
			}
		}
		E++
	}
	return D.join("")
}
function getStringLen(B) {
	var A = B.match(/[^\x00-\xff]/ig);
	return B.length + (A == null ? 0 : A.length)
}
function getBrowserType() {
	var A = 0;
	if (window.ActiveXObject) {
		if (window.XMLHttpRequest && !window.XDomainRequest) {
			return 5
		} else {
			if (window.XDomainRequest) {
				return 6
			} else {
				return 1
			}
		}
	} else {
		if (typeof (Components) == "object") {
			A = 2
		} else {
			if (typeof (window.opera) == "object") {
				A = 3
			} else {
				if (window.MessageEvent && !document.getBoxObjectFor) {
					A = 7
				} else {
					if (navigator.appVersion.indexOf("Safari") >= 0) {
						A = 4
					}
				}
			}
		}
	}
	return A
}
function checkCookieEnabled() {
	try {
		if (navigator.cookieEnabled == false) {
			return false
		}
	} catch (A) {
	}
	return true
}
Function.prototype.bindFunc = function(B) {
	if (typeof (B) != "object") {
		return false
	}
	var A = this;
	return function() {
		return A.apply(B, arguments)
	}
};
var login_status = "";
var logout_status = "";
var renew_status = "";
var PassportCardList = [];
/**
 * 
 */
var PassportSC = {
	version : 26,
	cvsid : "$Id: pp18030_31.js 3994 2011-01-05 11:44:40Z orangeforjava $",
	appid : 9999,
	max_line_length : 30,
	domain : "",
	cookie : false,
	email : "",
	bindDomainSelector : true,
	autopad : "",
	autoRedirectUrl : "",
	loginRedirectUrl : "",
	logoutRedirectUrl : "",
	selectorTitle : "请选择您的用户帐号类型",
	registerUrl : "http://passport.sohu.com/web/signup.jsp",
	recoverUrl : "http://passport.sohu.com/web/recover.jsp",
	postru : "",
	emailPostfix : false,
	curDSindex : -1,
	usePost : 0,
	successCalledFunc : false,
	curCardIndex : 0,
	rootElement : false,
	dsElement : false,
	sElement : false,
	cElement : false,
	dsAnchor : false,
	emailInput : false,
	passwdInput : false,
	pcInput : false,
	loginMsg : false,
	iElement : false,
	isSetFocus : true,
	loginProtocal : "https",
	http_url : false,
	eInterval : false,
	maxIntervalCount : 70,
	intervalCount : 0,
	defualtRemPwd : "",
	isShowRemPwdMsg : 0,
	campImg : "http://js.sohu.com/passport/images/pic007.gif",
	campImgAlt : "大本营",
	campUrl : "http://blog.sohu.com/camp?from=",
	cardTitle : "上搜狐，知天下",
	firstDomain : "",
	defaultApp : "",
	domainPool : [ "chinaren.com", "sogou.com" ],
	domainList : [ "sohu.com", "chinaren.com", "sogou.com", "vip.sohu.com",
			"17173.com", "focus.cn", "game.sohu.com" ],
	appList : {
		"1000" : "mail",
		"1005" : "alumni",
		"10050" : "chinaren",
		"1019" : "blog",
		"1017" : "pp",
		"1001" : "club",
		"1038" : "crclub",
		"1039" : "group",
		"1021" : "music",
		"1010" : "say",
		"1042" : "cbbs",
		"1028" : "focus",
		"1029" : "17173",
		"1013" : "vip",
		"1035" : "rpggame",
		"1044" : "pinyin",
		"1022" : "relaxgame"
	},
	appName : {
		mail : "邮件",
		alumni : "校友录",
		chinaren : "ChinaRen",
		blog : "博客",
		pp : "相册",
		club : "搜狐社区",
		crclub : "CR社区",
		group : "群组",
		music : "音乐盒",
		say : "说吧",
		cbbs : "校友论坛",
		focus : "焦点房产",
		"17173" : "游戏论坛",
		vip : "vip邮箱",
		rpggame : "RPG游戏",
		pinyin : "输入法",
		relaxgame : "休闲游戏"
	},
	appUrl : {
		mail : "",
		alumni : "http://class.chinaren.com",
		chinaren : "",
		blog : "http://blog.sohu.com/",
		pp : "http://pp.sohu.com/",
		club : "http://club.sohu.com",
		crclub : "http://club.chinaren.com",
		group : "http://i.chinaren.com/group",
		say : "http://s.sogou.com",
		music : "http://mbox.sogou.com/",
		cbbs : "http://cbbs.chinaren.com",
		focus : "http://www.focus.cn",
		"17173" : "http://bbs.17173.com",
		vip : "http://vip.sohu.com",
		rpggame : "http://game.sohu.com",
		pinyin : "http://pinyin.sogou.com",
		relaxgame : "http://game.sohu.com/index2.htm"
	},
	appPool : false,
	bottomRow : [],
	recomServ : [],
	reverseFirstDomain : false,
	showEmailInputTip : true,
	usePostFix : true,
	gotohref : function(B) {
		var A = document.createElement("a");
		if (!A.click) {
			window.location = B;
			return
		}
		A.setAttribute("href", B);
		document.body.appendChild(A);
		A.click()
	},
	getDomain : function() {
		var B = document.domain.split(".");
		var A = B.length;
		if (A <= 2) {
			return document.domain
		}
		return B[A - 2] + "." + B[A - 1]
	},
	addCookie : function(B, D, E) {
		if (this.domain == "") {
			this.domain = this.getDomain()
		}
		var C = B + "=" + escape(D) + "; path=/; domain=." + this.domain + ";";
		if (E > 0) {
			var A = new Date();
			A.setTime(A.getTime() + E * 3600 * 1000);
			C = C + "expires=" + A.toGMTString() + ";"
		}
		document.cookie = C
	},
	getCookie : function(A) {
		var E = document.cookie;
		var C = document.cookie.split("; ");
		var D = A + "=";
		for ( var B = 0; B < C.length; B++) {
			if (C[B].indexOf(D) == 0) {
				return C[B].substr(D.length)
			}
		}
		return ""
	},
	deleteCookie : function(A) {
		if (this.domain == "") {
			this.domain = this.getDomain()
		}
		var C = new Date();
		C.setTime(C.getTime() - 100000);
		var B = this.getCookie(A);
		document.cookie = A + "=" + B + "; expires=" + C.toGMTString()
				+ "; path=/; domain=." + this.domain + ";"
	},
	preventEvent : function(A) {
		A.cancelBubble = true;
		A.returnValue = false;
		if (A.preventDefault) {
			A.preventDefault()
		}
		if (A.stopPropagation) {
			A.stopPropagation()
		}
	},
	getPosition : function(B, A) {
		var C = 0;
		while (B) {
			C += B[A];
			B = B.offsetParent
		}
		return C
	},
	getTime : function() {
		var A = new Date();
		return A.getTime()
	},
	strip : function(A) {
		return A.replace(/^\s+/, "").replace(/\s+$/, "")
	},
	reportMsg : function(B) {
		var C = "";
		switch (B) {
		case "1":
			C += "请输入通行证用户名";
			break;
		case "2":
			C += "通行证用户名为邮件地址格式";
			break;
		case "3":
			C += "用户名后缀必须为" + arguments[1];
			break;
		case "4":
			C += "请输入通行证密码";
			break;
		case "5":
			var A = this.strip(this.emailInput.value);
			if (A.lastIndexOf("@focus.cn") > 0) {
				C += "用户名或密码错误!咨询电话:010-58511234"
			} else {
				C += "用户名或密码错误"
			}
			break;
		case "6":
			C += "登录超时，请稍后重试";
			break;
		case "7":
			C += "登录失败，请重试";
			break;
		case "8":
			C += "网络故障，退出失败，请重新退出";
			break;
		case "9":
			C += "登录失败，请稍后重试";
			break;
		case "10":
			C += "暂时不可登录，请稍后重试";
			break;
		case "11":
			C += "浏览器设置有误，请查看帮助修正";
			break;
		default:
			C += "登录错误，请稍后重试"
		}
		this.showMsg(C)
	},
	showMsg : function(A) {
		if (!this.loginMsg) {
			return
		}
		this.loginMsg.innerHTML = A
	},
	cookieHandle : function() {
		if (!this.cookie) {
			this.parsePassportCookie()
		}
		if (this.cookie && this.cookie.userid != "") {
			return this.cookie.userid
		} else {
			return ""
		}
	},
	getDisplayName : function() {
		var B = this.cookieHandle();
		var A = B.split("@");
		var D = A[0];
		var C = /^1\d{10}$/;
		if (C.test(D)) {
			return D.substring(0, 3) + "****" + D.substring(7)
		} else {
			return B
		}
	},
	parsePassportCookie : function() {
		var E = document.cookie.split("; ");
		for ( var D = 0; D < E.length; D++) {
			if (E[D].indexOf("ppinf=") == 0) {
				var C = E[D].substr(6);
				break
			}
			if (E[D].indexOf("ppinfo=") == 0) {
				var C = E[D].substr(7);
				break
			}
			if (E[D].indexOf("passport=") == 0) {
				var C = E[D].substr(9);
				break
			}
		}
		if (D == E.length) {
			this.cookie = false;
			return
		}
		try {
			var A = unescape(C).split("|");
			if (A[0] == "1" || A[0] == "2") {
				var B = utf8to16(b64_decodex(A[3]));
				this._parsePassportCookie(B);
				return
			}
		} catch (F) {
		}
	},
	_parsePassportCookie : function(F) {
		var J;
		var C;
		var D;
		this.cookie = new Object;
		J = 0;
		C = F.indexOf(":", J);
		while (C != -1) {
			var B;
			var A;
			var I;
			B = F.substring(J, C);
			lenEnd_offset = F.indexOf(":", C + 1);
			if (lenEnd_offset == -1) {
				break
			}
			A = parseInt(F.substring(C + 1, lenEnd_offset));
			I = F.substr(lenEnd_offset + 1, A);
			if (F.charAt(lenEnd_offset + 1 + A) != "|") {
				break
			}
			this.cookie[B] = I;
			J = lenEnd_offset + 2 + A;
			C = F.indexOf(":", J)
		}
		try {
			this.cookie.service = new Object;
			var H = this.cookie.service;
			H.mail = 0;
			H.alumni = 0;
			H.chinaren = 0;
			H.blog = 0;
			H.pp = 0;
			H.club = 0;
			H.crclub = 0;
			H.group = 0;
			H.say = 0;
			H.music = 0;
			H.focus = 0;
			H["17173"] = 0;
			H.vip = 0;
			H.rpggame = 0;
			H.pinyin = 0;
			H.relaxgame = 0;
			var G = this.cookie.serviceuse;
			if (G.charAt(0) == 1) {
				H.mail = "sohu"
			} else {
				if (G.charAt(2) == 1) {
					H.mail = "sogou"
				} else {
					if (this.cookie.userid.indexOf("@chinaren.com") > 0) {
						H.mail = "chinaren"
					}
				}
			}
			if (G.charAt(1) == 1) {
				H.alumni = 1
			}
			if (G.charAt(3) == 1) {
				H.blog = 1
			}
			if (G.charAt(4) == 1) {
				H.pp = 1
			}
			if (G.charAt(5) == 1) {
				H.club = 1
			}
			if (G.charAt(7) == 1) {
				H.crclub = 1
			}
			if (G.charAt(8) == 1) {
				H.group = 1
			}
			if (G.charAt(9) == 1) {
				H.say = 1
			}
			if (G.charAt(10) == 1) {
				H.music = 1
			}
			if (G.charAt(11) == 1
					|| this.cookie.userid.lastIndexOf("@focus.cn") > 0) {
				H.focus = 1
			}
			if (G.charAt(12) == 1
					|| this.cookie.userid.indexOf("@17173.com") > 0) {
				H["17173"] = 1
			}
			if (G.charAt(13) == 1) {
				H.vip = 1
			}
			if (G.charAt(14) == 1) {
				H.rpggame = 1
			}
			if (G.charAt(15) == 1) {
				H.pinyin = 1
			}
			if (G.charAt(16) == 1) {
				H.relaxgame = 1
			}
		} catch (E) {
		}
	},
	parseAppid : function() {
		var D = this.appid.toString();
		var C = 0;
		this.appPool = new Array();
		for ( var B in this.appList) {
			var A = this.appList[B];
			if (typeof (A) != "string") {
				continue
			}
			if (B == D) {
				this.defaultApp = this.appName[A]
			} else {
				if (B == "1028") {
					this.appPool[C] = {
						app : "focus",
						name : "北京业主论坛",
						url : "http://house.focus.cn/group/yezhu.php"
					};
					C++;
					this.appPool[C] = {
						app : "focus",
						name : "装修论坛",
						url : "http://home.focus.cn/group/group_forum.php"
					}
				} else {
					this.appPool[C] = {
						app : A,
						name : this.appName[A],
						url : this.appUrl[A]
					}
				}
				C++
			}
		}
	},
	getBottomRow : function() {
		var A = 0;
		var B = this.max_line_length - getStringLen(this.defaultApp);
		this.bottomRow[0] = new Array();
		this.bottomRow[1] = new Array();
		if (!this.cookie) {
			return
		}
		A = this._getBottomRow(this.bottomRow[0], B, 0);
		B = this.max_line_length;
		A = this._getBottomRow(this.bottomRow[1], B, A)
	},
	_getBottomRow : function(J, F, C) {
		var A, D;
		var H = this.cookie.service;
		var G = this.appPool;
		var E = C;
		var I;
		for (D = 0; E < G.length; E++) {
			A = G[E]["app"];
			if (typeof (A) != "string") {
				continue
			}
			if (typeof (H[A]) == "undefined") {
				continue
			}
			if (H[A] != 0) {
				I = getStringLen(G[E]["name"]);
				if (F - I < 0) {
					break
				}
				F -= (I + 2);
				J[D] = G[E];
				if (A == "mail") {
					if (H.mail == "sohu") {
						J[D]["url"] = "http://mail.sohu.com?appid=0001"
					} else {
						if (H.mail == "sogou") {
							J[D]["url"] = "http://mail.sogou.com"
						} else {
							J[D]["url"] = "http://mail.chinaren.com"
						}
					}
				}
				D++
			} else {
				if (G[E]["name"] == "ChinaRen") {
					continue
				}
				var B = this.recomServ.length;
				this.recomServ[B] = G[E];
				if (A == "mail") {
					this.recomServ[B]["url"] = "http://mail.chinaren.com"
				}
			}
		}
		return E
	},
	parseLastDomain : function(I) {
		this.emailPostfix = new Array();
		var B = "", L = "";
		var A = "", M = "", G = [];
		var K = document.cookie.split("; ");
		for ( var E = 0; E < K.length; E++) {
			if (K[E].indexOf("lastdomain=") == 0) {
				try {
					G = unescape(K[E].substr(11)).split("|");
					if (G.length == 4) {
						var D = G[3];
						if (D != null && D == "1") {
							this.loginProtocal = "http"
						}
					}
				} catch (H) {
				}
				break
			}
		}
		var C = 0;
		if (G.length >= 3) {
			var J = utf8to16(b64_decodex(G[1]));
			var F = J.split("|");
			for ( var E = 0; E < F.length; E++) {
				if (F[E] != "") {
					this.emailPostfix[C] = F[E];
					C++
				}
			}
		}
		if (this.firstDomain != "") {
			for ( var E in I) {
				if (this.firstDomain == I[E]) {
					L = I[E];
					break
				}
			}
			if (L != "") {
				this.emailPostfix[C] = L;
				C++
			}
		}
		if (document.domain.indexOf("game.sohu.com") >= 0) {
			B = "game.sohu.com";
			this.emailPostfix[C] = B;
			C++
		}
		this.emailPostfix[C] = this.domain;
		C++;
		for ( var E in I) {
			if (typeof (I[E]) != "string") {
				continue
			}
			if (I[E] != this.domain && I[E] != B && I[E] != L) {
				this.emailPostfix[C] = I[E];
				C++
			}
		}
	},
	doPost : function() {
		for ( var C = 0; C < document.forms.length; C++) {
			if (document.forms[C].name == "loginform") {
				break
			}
		}
		if (C == document.forms.length) {
			document.location.href = "http://passport.sohu.com";
			return false
		}
		var A = getBrowserType();
		var B = screen.width;
		document.forms[C].action = "http://passport.sohu.com/sso/login_js.jsp?appid="
				+ this.appid
				+ "&ru="
				+ this.postru
				+ "&b="
				+ A
				+ "&w="
				+ B
				+ "&v=" + this.version;
		document.forms[C].submit();
		return false
	},
	doLogin : function() {
		if (this.eInterval) {
			return
		}
		if (arguments[0]) {
			PassportCardList[index].doLogin()
		}
		login_status = "";
		this.intervalCount = 0;
		this.sElement.innerHTML = "";
		this.email = this.strip(this.emailInput.value);
		var C = this.email;
		var B = this.strip(this.passwdInput.value);
		var A = 0;
		if (this.pcInput.checked == true) {
			A = 1
		}
		if (C == "") {
			this.reportMsg("1");
			this.emailInput.focus();
			return false
		}
		if (this.autopad != "") {
			var D = C.substr(C.lastIndexOf("@") + 1);
			if (this.autopad.lastIndexOf(D) < 0) {
				this.reportMsg("3", this.autopad);
				this.emailInput.focus();
				this.passwdInput.value = "";
				return false
			}
		}
		if (B == "") {
			this.reportMsg("4");
			this.passwdInput.value = "";
			this.passwdInput.focus();
			return false
		}
		if (this.usePost == 1) {
			return this.doPost()
		}
		this.drawPassportWait("正在登录搜狐通行证，请稍候...");
		return this.loginHandle(C, B, A, this.sElement, this.loginFailCall
				.bindFunc(this), this.loginSuccessCall.bindFunc(this))
	},
	loginHandle : function(L, F, H, N, G, C) {
		if (typeof (N) != "object") {
			return false
		}
		if (checkCookieEnabled() == false) {
			G();
			return false
		}
		login_status = "";
		var I = getBrowserType();
		var J = screen.width;
		if (this.domain == "") {
			this.domain = this.getDomain()
		}
		var B = this.getTime();
		var D = hex_md5(F);
		try {
			this.http_url = "http://passport.sohu.com/sso/login.jsp?userid="
					+ encodeURIComponent(L) + "&password=" + D + "&appid="
					+ this.appid + "&persistentcookie=" + H + "&isSLogin=1&s="
					+ B + "&b=" + I + "&w=" + J + "&pwdtype=1&v="
					+ this.version;
			if (this.loginProtocal == "https") {
				var A = "https://passport.sohu.com/sso/login.jsp?userid="
						+ encodeURIComponent(L) + "&password=" + D + "&appid="
						+ this.appid + "&persistentcookie=" + H + "&s=" + B
						+ "&b=" + I + "&w=" + J + "&pwdtype=1&v="
						+ this.version
			} else {
				if (this.loginProtocal == "http") {
					var A = this.http_url
				}
			}
		} catch (E) {
			this.http_url = "http://passport.sohu.com/sso/login.jsp?userid="
					+ L + "&password=" + D + "&appid=" + this.appid
					+ "&persistentcookie=" + H + "&isSLogin=1&s=" + B + "&b="
					+ I + "&w=" + J + "&pwdtype=1&v=" + this.version;
			if (this.loginProtocal == "https") {
				var A = "https://passport.sohu.com/sso/login.jsp?userid=" + L
						+ "&password=" + D + "&appid=" + this.appid
						+ "&persistentcookie=" + H + "&s=" + B + "&b=" + I
						+ "&w=" + J + "&pwdtype=1&v=" + this.version
			} else {
				if (this.loginProtocal == "http") {
					var A = this.http_url
				}
			}
		}
		if (this.domain != "sohu.com") {
			A += "&domain=" + this.domain
		}
		var K = document.createElement("script");
		K.src = A;
		K.id = "loginele";
		N.appendChild(K);
		var M = this;
		this.eInterval = setInterval(function() {
			M.loginIntervalProc(G, C, N)
		}, 100);
		return false
	},
	loginIntervalProc : function(B, D, E) {
		if (login_status == "" && this.intervalCount < this.maxIntervalCount) {
			this.intervalCount++;
			return
		}
		clearInterval(this.eInterval);
		this.eInterval = false;
		if (login_status != "success"
				|| this.intervalCount >= this.maxIntervalCount) {
			if (this.loginProtocal == "https" && login_status == "") {
				this.intervalCount = 0;
				this.loginProtocal = "http";
				if (this.domain != "sohu.com") {
					this.http_url += "&domain=" + this.domain
				}
				var C = document.getElementById("loginele");
				C.src = this.http_url;
				var A = this;
				this.eInterval = setInterval(function() {
					A.loginIntervalProc(B, D, E)
				}, 100)
			} else {
				B()
			}
			return
		}
		if (this.loginRedirectUrl == "") {
			this.autoProcAllDomain("login", E)
		} else {
			this.addCookie("crossdomain", this.getTime(), 336)
		}
		D()
	},
	loginFailCall : function() {
		this.sElement.innerHTML = "";
		this.drawLoginForm();
		if (this.intervalCount >= this.maxIntervalCount) {
			this.reportMsg("6");
			this.emailInput.focus()
		} else {
			if (login_status == "error3" || login_status == "error2") {
				this.reportMsg("5");
				this.passwdInput.focus()
			} else {
				if (login_status == "error5") {
					this.reportMsg("10");
					this.passwdInput.focus()
				} else {
					if (login_status == "error13") {
						window.location = "http://passport.sohu.com/web/remind_activate.jsp";
						return
					} else {
						if (checkCookieEnabled() == false) {
							this.reportMsg("11");
							this.emailInput.focus()
						} else {
							this.reportMsg("9");
							this.passwdInput.focus()
						}
					}
				}
			}
		}
	},
	loginSuccessCall : function() {
		this.parsePassportCookie();
		if (this.cookie && this.cookie.userid != "") {
			this.email = "";
			if (this.loginRedirectUrl != "") {
				if (this.cookie.service["mail"] != "0"
						&& (this.appid == "1000" || this.appid == "1014" || this.appid == "1037")) {
					if (this.domain.indexOf(this.cookie.service["mail"]) == -1) {
						this.drawLoginForm()
					} else {
						PassportSC.gotohref(this.loginRedirectUrl)
					}
				} else {
					if (document.location.href == this.loginRedirectUrl) {
						document.location.reload()
					} else {
						PassportSC.gotohref(this.loginRedirectUrl)
					}
				}
			} else {
				this.getBottomRow();
				this.drawPassportCard();
				for (i = 0; i < PassportCardList.length; i++) {
					if (i == this.curCardIndex) {
						continue
					}
					PassportCardList[i].parsePassportCookie();
					PassportCardList[i].getBottomRow();
					PassportCardList[i].drawPassportCard()
				}
			}
		} else {
			this.drawLoginForm();
			this.reportMsg("7")
		}
	},
	doLogout : function() {
		if (this.eInterval) {
			return
		}
		this.intervalCount = 0;
		this.sElement.innerHTML = "";
		if (this.usePost == 1) {
			window.location = "http://passport.sohu.com/sso/logout_js.jsp?s="
					+ this.getTime() + "&ru=" + this.postru
		} else {
			this.logoutHandle(this.sElement,
					this.logoutFailCall.bindFunc(this), this.logoutSuccessCall
							.bindFunc(this, "dd"))
		}
	},
	logoutHandle : function(F, B, E) {
		if (typeof (F) != "object") {
			return false
		}
		logout_status = "";
		if (this.domain == "") {
			this.domain = this.getDomain()
		}
		var G = this.getTime();
		var D = "http://passport.sohu.com/sso/logout.jsp?s=" + G + "&appid="
				+ this.appid;
		if (this.domain != "sohu.com") {
			D += "&domain=" + this.domain
		}
		var C = document.createElement("script");
		C.src = D;
		F.appendChild(C);
		var A = this;
		this.eInterval = setInterval(function() {
			A.logoutIntervalProc(B, E, F)
		}, 100)
	},
	logoutIntervalProc : function(A, B, C) {
		if (logout_status == "" && this.intervalCount < this.maxIntervalCount) {
			this.intervalCount++;
			return
		}
		clearInterval(this.eInterval);
		this.eInterval = false;
		if (logout_status != "success"
				|| this.intervalCount >= this.maxIntervalCount) {
			A();
			return
		}
		if (this.logoutRedirectUrl == "") {
			this.autoProcAllDomain("logout", C)
		} else {
			this.addCookie("crossdomain_logout", this.getTime(), 336)
		}
		B()
	},
	logoutFailCall : function() {
		this.sElement.innerHTML = "";
		this.reportMsg("8")
	},
	logoutSuccessCall : function(B) {
		this.parseLastDomain(this.domainList);
		this.cookie = false;
		this.drawLoginForm();
		for (i = 0; i < PassportCardList.length; i++) {
			if (i == this.curCardIndex) {
				continue
			}
			PassportCardList[i].drawLoginForm()
		}
		try {
			logoutApp()
		} catch (A) {
		}
	},
	renewCookie : function(F, B, E) {
		if (typeof (F) != "object") {
			return false
		}
		if (this.domain == "") {
			this.domain = this.getDomain()
		}
		var G = this.getTime();
		var D = "http://passport.sohu.com/sso/renew.jsp?s=" + G;
		if (this.domain != "sohu.com") {
			D += "&domain=" + this.domain
		}
		var C = document.createElement("script");
		C.src = D;
		F.appendChild(C);
		var A = this;
		this.eInterval = setInterval(function() {
			A.renewIntervalProc(B, E, F)
		}, 100);
		return false
	},
	renewIntervalProc : function(A, B, C) {
		if (renew_status == "" && this.intervalCount < this.maxIntervalCount) {
			this.intervalCount++;
			return
		}
		clearInterval(this.eInterval);
		this.eInterval = false;
		if (renew_status != "success"
				|| this.intervalCount >= this.maxIntervalCount) {
			try {
				A()
			} catch (D) {
			}
			return
		}
		this.autoProcAllDomain("renew", C);
		try {
			B()
		} catch (D) {
		}
	},
	autoProcAllDomain : function(D, C) {
		var A = this.crossDomainIframeUrl(D);
		if (A) {
			var B = document.createElement("iframe");
			B.src = A;
			B.style.width = "0";
			B.style.height = "0";
			C.appendChild(B)
		}
	},
	doCrossDomainCookie : function(F, E) {
		if (typeof (F) != "object") {
			return
		}
		var B = "crossdomain";
		if (E == "logout") {
			B = "crossdomain_logout"
		}
		var C = this.getCookie(B);
		if (C == "" || C == "0") {
			return
		}
		if (this.domain == "") {
			this.domain = this.getDomain()
		}
		var A = this.crossDomainIframeUrl(E);
		if (A) {
			var D = document.createElement("iframe");
			D.src = A;
			D.style.width = "0";
			D.style.height = "0";
			F.appendChild(D);
			this.deleteCookie(B)
		}
	},
	crossDomainUrl : function(C, B) {
		var D = this.getTime();
		var A = "http://passport.sohu.com/sso/crossdomain.jsp?s=" + D
				+ "&action=" + C + "&domain=" + B;
		return A
	},
	crossDomainIframeUrl : function(B) {
		var A = "";
		if (this.domain == "sohu.com" || this.domain == "sogou.com"
				|| this.domain == "chinaren.com"
				|| this.domain == "changyou.com") {
			A = "http://passport." + this.domain
					+ "/sso/crossdomain_all.jsp?action=" + B
		} else {
			A = "http://pass." + this.domain
					+ "/sso/crossdomain_all.jsp?action=" + B
		}
		return A
	},
	setDomainCookie : function(F, E, D, B) {
		login_status = "";
		crossdomain_status = "";
		var C = this.crossDomainUrl("login", E);
		if (C) {
			newScript = document.createElement("script");
			newScript.src = C;
			F.appendChild(newScript)
		}
		var A = this;
		this.eInterval = setInterval(function() {
			A.setCookieIntervalProc(F, D, B)
		}, 100)
	},
	setCookieIntervalProc : function(C, B, A) {
		if (crossdomain_status != "") {
			clearInterval(this.eInterval);
			this.eInterval = false;
			A();
			return
		}
		if (login_status == "" && this.intervalCount < this.maxIntervalCount) {
			this.intervalCount++;
			return
		}
		clearInterval(this.eInterval);
		this.eInterval = false;
		if (login_status != "success"
				|| this.intervalCount >= this.maxIntervalCount) {
			A();
			return
		}
		B()
	},
	downDSindex : function() {
		if (this.dsAnchor.firstChild == null) {
			return
		}
		var A = this.dsAnchor.firstChild.rows;
		var B = 0;
		for (; B < A.length; B++) {
			if (A[B].firstChild.idx == this.curDSindex) {
				break
			}
		}
		if (B >= A.length - 1) {
			this.curDSindex = A[0].firstChild.idx
		} else {
			this.curDSindex = A[B + 1].firstChild.idx
		}
	},
	upDSindex : function() {
		if (this.dsAnchor.firstChild == null) {
			return
		}
		var A = this.dsAnchor.firstChild.rows;
		var C = -1;
		var B = 0;
		for (; B < A.length; B++) {
			if (A[B].firstChild.idx == this.curDSindex) {
				break
			}
			C = A[B].firstChild.idx
		}
		if (B == A.length) {
			this.curDSindex = A[0].firstChild.idx
		} else {
			if (C == -1) {
				this.curDSindex = A[A.length - 1].firstChild.idx
			} else {
				this.curDSindex = C
			}
		}
	},
	findDSindex : function(B) {
		try {
			var A = this.dsAnchor.firstChild.rows;
			for ( var C = 0; C < A.length; C++) {
				if (A[C].firstChild.idx == B) {
					return A[C].firstChild
				}
			}
		} catch (D) {
		}
		return false
	},
	clearFocus : function(B) {
		if (typeof (B) != "number") {
			B = this.curDSindex
		}
		try {
			var A = this.findDSindex(B);
			A.className = "";
			A.style.fontWeight = "normal"
		} catch (C) {
		}
	},
	setFocus : function(B) {
		if (typeof (B) != "number") {
			B = this.curDSindex
		}
		try {
			var A = this.findDSindex(B);
			A.className = "active"
		} catch (C) {
		}
	},
	fillEmailSelect : function() {
		var R = this.emailInput.value;
		if (R == "") {
			this.dsElement.style.display = "none";
			return
		}
		var S = "";
		var A = "";
		var N = R.lastIndexOf("@");
		if (N < 0) {
			A = R
		} else {
			if (N == R.length - 1) {
				A = R.substr(0, N)
			} else {
				A = R.substr(0, N);
				S = R.substr(N + 1)
			}
		}
		var J = this.getPosition(this.emailInput, "offsetLeft")
				- this.getPosition(this.cElement, "offsetLeft");
		if (document.all && !document.addEventListener) {
			J += 1
		}
		this.dsElement.style.marginLeft = J + "px";
		this.dsElement.style.marginTop = (this.getPosition(this.emailInput,
				"offsetTop")
				- this.getPosition(this.cElement, "offsetTop") + this.emailInput.offsetHeight)
				+ "px";
		this.dsElement.style.zIndex = "2000";
		this.dsElement.style.paddingRight = "0";
		this.dsElement.style.paddingLeft = "0";
		this.dsElement.style.paddingTop = "0";
		this.dsElement.style.paddingBottom = "0";
		this.dsElement.style.backgroundColor = "white";
		this.dsElement.style.display = "block";
		var I = document.createElement("TABLE");
		I.width = "100%";
		I.cellSpacing = 0;
		I.cellPadding = 3;
		var B = document.createElement("TBODY");
		I.appendChild(B);
		var O = 0;
		var D = false;
		var K = false;
		var T = -1;
		var L = "", E = "";
		var G = this.emailPostfix;
		var Q = /^1.*$/;
		if (Q.test(R)) {
			if (this.autopad != "") {
				G = [ "mobile", "qq.com", "focus.cn", this.autopad ]
			} else {
				G = [ "mobile", "qq.com", "focus.cn" ]
			}
		}
		for ( var P = 0; P < G.length; P++) {
			var F = G[P];
			if (typeof (F) != "string") {
				continue
			}
			if (S != "") {
				if (F.lastIndexOf(S) != 0) {
					continue
				}
			}
			if (F.lastIndexOf("@") > 0) {
				tmp_pos = F.lastIndexOf("@");
				if (this.autopad != ""
						&& this.autopad.lastIndexOf(F.substring(tmp_pos + 1)) < 0) {
					continue
				}
				E = F.substring(0, tmp_pos);
				if (E.lastIndexOf(A) != 0) {
					continue
				}
				if (E == A) {
					L = F.substring(F.lastIndexOf("@") + 1)
				}
				K = true
			} else {
				if (this.autopad != "" && this.autopad.lastIndexOf(F) < 0) {
					continue
				}
			}
			if (F == L) {
				continue
			}
			O++;
			if (T == -1) {
				T = P
			}
			if (this.curDSindex == P) {
				D = true
			}
			var C = document.createElement("TR");
			var H = document.createElement("TD");
			H.nowrap = "true";
			H.align = "left";
			if (F == "mobile") {
				H.innerHTML = A
			} else {
				if (K == false) {
					if (this.usePostFix) {
						H.innerHTML = A + "@" + F
					} else {
						H.innerHTML = A
					}
				} else {
					if (this.usePostFix) {
						H.innerHTML = F
					} else {
						H.innerHTML = F.substring(0, F.lastIndexOf("@"))
					}
				}
			}
			H.id = "email_postfix_" + P;
			H.idx = P;
			var M = this;
			H.onmouseover = function() {
				M.clearFocus();
				M.curDSindex = this.idx;
				M.setFocus();
				this.style.cursor = "hand"
			};
			H.onclick = function() {
				M.doSelect()
			};
			C.appendChild(H);
			B.appendChild(C);
			K = false
		}
		if (O > 0) {
			this.dsAnchor.innerHTML = "";
			this.dsAnchor.appendChild(I);
			if (D == false) {
				this.curDSindex = T
			}
			this.setFocus()
		} else {
			this.dsElement.style.display = "none";
			this.curDSindex = -1
		}
	},
	doSelect : function() {
		this.dsElement.style.display = "none";
		var A = this.findDSindex(this.curDSindex);
		if (A) {
			var B = A.innerHTML;
			if (B) {
				this.emailInput.value = B.replace(/&amp;/g, "&")
			}
		}
		if (this.emailInput.value != "") {
			this.passwdInput.focus()
		}
	},
	checkKeyDown : function(A) {
		A = A || window.event;
		var B = A.keyCode || A.which || A.charCode;
		if (B == 38 || B == 40) {
			if (A.shiftKey == 1) {
				return
			}
			this.clearFocus();
			if (B == 38) {
				this.upDSindex()
			} else {
				if (B == 40) {
					this.downDSindex()
				}
			}
			this.setFocus()
		}
	},
	checkKeyPress : function(B) {
		B = B || window.event;
		var C = B.keyCode || B.which || B.charCode;
		if (C == 13) {
			this.preventEvent(B)
		} else {
			if (C == 38 || C == 40) {
				if (B.shiftKey == 1) {
					return
				}
				this.preventEvent(B);
				this.clearFocus();
				if (C == 38) {
					this.upDSindex()
				} else {
					if (C == 40) {
						this.downDSindex()
					}
				}
				this.setFocus()
			} else {
				if (C == 108 || C == 110 || C == 111 || C == 115) {
					var A = this;
					setTimeout(function() {
						A.fillEmailSelect()
					}, 10)
				}
			}
		}
	},
	checkKeyUp : function(A) {
		A = A || window.event;
		var B = A.keyCode || A.which || A.charCode;
		this.fillEmailSelect();
		if (B == 13) {
			this.doSelect()
		}
		if (getBrowserType() == 7 || getBrowserType() == 4) {
			if (B == 38 || B == 40) {
				if (A.shiftKey == 1) {
					return
				}
				this.clearFocus();
				if (B == 38) {
					this.upDSindex()
				} else {
					if (B == 40) {
						this.downDSindex()
					}
				}
				this.setFocus()
			}
		}
	},
	init : function(A) {
		this.rootElement = A;
		this.rootElement.innerHTML = '<div class="ppselecter" style="position: absolute; display: none;"><table width="100%" cellspacing="0" cellpadding="0"><tbody><tr><td style="" class="ppseltit" id="ppseltitId">' + this.selectorTitle + '</td></tr><tr><td height="2" /></tr><tr><td /></tr></tbody></table></div><div style="display: none;"></div><div class="passportc"></div>';
		if (this.selectorTitle == null || this.selectorTitle.length == 0) {
			this.rootElement.innerHTML = '<div class="ppselecter" style="position: absolute; display: none;"><table width="100%" cellspacing="0" cellpadding="0"><tbody><tr></tr><tr><td height="0" /></tr><tr><td /></tr></tbody></table></div><div style="display: none;"></div><div class="passportc"></div>'
		}
		this.dsElement = this.rootElement.childNodes[0];
		this.sElement = this.rootElement.childNodes[1];
		this.cElement = this.rootElement.childNodes[2];
		this.dsAnchor = this.dsElement.firstChild.rows[2].firstChild;
		this.domain = this.getDomain();
		this.parseLastDomain(this.domainList);
		this.parseAppid();
		this.parsePassportCookie();
		this.getBottomRow();
		if (this.postru == "") {
			this.postru = document.location.href
		}
	},
	_drawPassportCard : function() {
	},
	drawPassportCard : function() {
		this._drawPassportCard();
		var B = document.getElementById("ppcontid");
		B.onclick = this.doClickLink.bindFunc(this);
		this.$iElement();
		try {
			this.successCalledFunc(this.iElement)
		} catch (A) {
			this.drawPassportInfo()
		}
	},
	doClickLink : function(A) {
		var H = window.event ? window.event : A;
		var G = H.srcElement || H.target;
		var D = G.tagName.toLowerCase();
		var B = this.cookie.userid;
		var F = document.location.href;
		var E = "";
		if (D == "img") {
			D = G.parentNode.tagName.toLowerCase();
			G = G.parentNode
		}
		if (D == "a") {
			var C = document.createElement("script");
			C.src = "http://passport.sohu.com/web/golog.jsp?userid=" + B
					+ "&fappid=" + this.appid + "&furl=" + F + "&turl=" + G;
			this.iElement.appendChild(C)
		}
	},
	$iElement : function() {
		this.iElement = this.$getElementByClassName("listContA")
	},
	$getElementByClassName : function(B) {
		var A = this.cElement.getElementsByTagName("div");
		for ( var C = 0; C < A.length; C++) {
			if (A[C].className.lastIndexOf(B) == 0) {
				return A[C]
			}
		}
	},
	drawPassportWait : function(A) {
	},
	drawPassportInfo : function() {
	},
	getRanServ : function() {
		var D = this.recomServ.length;
		if (D == 0) {
			return ""
		}
		var B = Math.floor(D * (Math.random()));
		var C = '<a href="' + this.recomServ[B]["url"] + '" target="_blank">'
				+ this.recomServ[B]["name"] + "</a>";
		if (D == 1) {
			return C
		}
		var A = Math.floor(D * (Math.random()));
		while (B == A) {
			A = Math.floor(D * (Math.random()))
		}
		C += ' | <a href="' + this.recomServ[A]["url"] + '" target="_blank">'
				+ this.recomServ[A]["name"] + "</a>";
		return C
	},
	_drawLoginForm : function() {
	},
	drawLoginForm : function() {
		this._drawLoginForm();
		var A = this.cElement.getElementsByTagName("input");
		for ( var C = 0; C < A.length; C++) {
			if (A[C].name == "email") {
				this.emailInput = A[C]
			}
			if (A[C].name == "password") {
				this.passwdInput = A[C]
			}
			if (A[C].name == "persistentcookie") {
				this.pcInput = A[C]
			}
		}
		this.loginMsg = this.$getElementByClassName("error");
		if (this.isShowRemPwdMsg == 1) {
			var B = this;
			this.pcInput.onclick = function() {
				if (B.pcInput.checked == false) {
					return
				}
				var D = window
						.confirm("浏览器将在两周内保持通行证的登录状态，网吧或公共机房上网者请慎用。您能确认本次操作吗？");
				if (D == false) {
					B.pcInput.checked = false
				}
			}
		}
		this.bindSelector();
		this.autoFillUserId();
		var B = this;
		if (this.emailInput.value == "") {
			if (this.isSetFocus) {
				setTimeout(function() {
					B.emailInput.focus()
				}, 50)
			}
		} else {
			if (this.isSetFocus && this.emailInput.value != "通行证帐号/手机号") {
				setTimeout(function() {
					B.passwdInput.focus()
				}, 50)
			}
		}
	},
	autoFillUserId : function() {
		if (this.showEmailInputTip) {
			this.showEmailInputTip = false;
			return
		}
		var B = this.getCookie("pptmpuserid");
		if (this.email.length > 0) {
			this.emailInput.value = this.email
		} else {
			this.emailInput.value = B
		}
		if (B.length > 0) {
			var A = this;
			setTimeout(function() {
				A.deleteCookie("pptmpuserid")
			}, 1000)
		}
	},
	bindSelector : function() {
		if (this.bindDomainSelector) {
			this.curDSindex = -1;
			try {
				this.emailInput.addEventListener("mousedown",
						this.checkMousedown.bindFunc(this), false);
				this.emailInput.addEventListener("keypress", this.checkKeyPress
						.bindFunc(this), false);
				this.emailInput.addEventListener("keyup", this.checkKeyUp
						.bindFunc(this), false);
				this.emailInput.addEventListener("blur", this.doSelect
						.bindFunc(this), false)
			} catch (A) {
				try {
					this.emailInput.attachEvent("onmousedown",
							this.checkMousedown.bindFunc(this));
					this.emailInput.attachEvent("onkeydown", this.checkKeyDown
							.bindFunc(this));
					this.emailInput.attachEvent("onkeypress",
							this.checkKeyPress.bindFunc(this));
					this.emailInput.attachEvent("onkeyup", this.checkKeyUp
							.bindFunc(this));
					this.emailInput.attachEvent("onblur", this.doSelect
							.bindFunc(this))
				} catch (A) {
				}
			}
		}
	},
	checkMousedown : function() {
		if (this.emailInput.value == "通行证帐号/手机号") {
			this.emailInput.value = "";
			this.emailInput.style.color = "black";
			this.emailInput.focus();
			return
		}
	},
	drawPassport : function(element) {
		if (typeof (element) != "object") {
			return
		}
		if (PassportCardList.length == 0) {
			PassportCardList[0] = this
		}
		if (!this.successCalledFunc) {
			try {
				this.successCalledFunc = eval("drawAppInfo")
			} catch (e) {
				this.successCalledFunc = this.drawPassportInfo
			}
		}
		this.init(element);
		if (this.cookie && this.cookie.userid != "") {
			if (this.autopad != "") {
				var at = this.cookie.userid.lastIndexOf("@");
				if (at > 0) {
					if (this.autopad.lastIndexOf(this.cookie.userid
							.substr(at + 1)) < 0) {
						this.drawLoginForm();
						return
					}
				}
			}
			if (this.autoRedirectUrl != "") {
				PassportSC.gotohref(this.autoRedirectUrl)
			} else {
				this.drawPassportCard()
			}
		} else {
			this.drawLoginForm()
		}
	},
	drawPassportNew : function(A, C, E) {
		if (typeof (A) != "object") {
			return
		}
		var B = new Function();
		B.prototype = this;
		var F = PassportCardList.length;
		var D = new B();
		D.successCalledFunc = E;
		D.appid = C;
		D.curCardIndex = F;
		D.isSetFocus = false;
		PassportCardList[F] = D;
		drawPassportNewInit(F, A);
		return
	}
};

var ele = document.getElementsByTagName("head")[0];
PassportSC.doCrossDomainCookie(ele, "login");
PassportSC.doCrossDomainCookie(ele, "logout");
if (typeof encodeURIComponent == "undefined") {
	PassportSC.usePost = 1
}
if (getBrowserType() == 3
		&& (screen.height == 5000 || window.navigator.userAgent
				.lastIndexOf("Mini") >= 0)) {
	PassportSC.usePost = 1
};