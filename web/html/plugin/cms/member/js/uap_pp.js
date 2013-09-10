/**
 * Class: Mapabc.LonLat This class represents a longitude and latitude pair
 */
UAPPassport.Ajax = UAPPassport.Class( {

	/**
	 * 显示登录窗体
	 */
	funcShowLoginForm : null,

	/**
	 * 显示登录成功后信息
	 */
	funcShowUserInfo : null,

	obj : null,

	statusUrl : null,// "http://cms.ccun.cn/member/status.jhtml?ft=json&jsoncallback=?"
	loginUrl:null,//"http://cms.ccun.cn/member/login.jhtml?ft=json&jsoncallback=?"
	logoutUrl:null,//"http://cms.ccun.cn/member/logout.jhtml?ft=json&jsoncallback=?"

	/**
	 * 构造函数
	 */
	initialize : function(fShowLoginForm, fShowUserInfo, url,vloginUrl,vlogoutUrl) {

		this.funcShowLoginForm = fShowLoginForm;
		this.funcShowUserInfo = fShowUserInfo;
		
		this.statusUrl = url;
		this.loginUrl=vloginUrl;
		this.logoutUrl=vlogoutUrl;
		
		obj = this;
	},
	/**
	 * 显示登录状态
	 */
	showLoginStatus : function() {
		$.getJSON(this.statusUrl, function(data) {
			if(data.status=='1'||data.status=='-2'){
				obj.funcShowUserInfo(data);
			}else{
				obj.funcShowLoginForm();
			}
		});
	},
	/**
	 * 同步登录状态
	 */
	_synstatus:function(data){
		if(data.synurl){
			//
			
		}
	},
	/**
	 * 用户登录
	 */
	login:function(username,passwd){
		//
		uname=encodeURIComponent(username);
		upwd=passwd;
		//
		if(loginUrl!=null){
			$.getJSON(this.loginUrl,{'username':uname,'password':upwd}, function(data) {
				if(data.status=='1'||data.status=='-2'){
					obj.funcShowUserInfo(data);
					_synstatus(data);
				}else{
					obj.funcShowLoginForm();
				}
			});
		}
	},
	logout:function(){
	},
	CLASS_NAME : "UAPPassport.Ajax"
});