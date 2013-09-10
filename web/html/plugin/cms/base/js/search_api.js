(function() {
    /**
     *
     */
    var singleFile = (typeof UAPSearch == "object" && UAPSearch.singleFile);
    
    /**
     * Namespace: uapPP
     * The uapPP object provides a namespace for all things uapPP
     */
    window.UAPSearch = {
        
        /**
         * Property: _scriptName
         * {String} Relative path of this script.
         */
        _scriptName: (!singleFile) ? "js/search_api.js" : "search_api.js",

        /**
         * Function: _getScriptLocation
         * Return the path to this script.
         *
         * Returns:
         * {String} Path to this script
         */
        _getScriptLocation: function () {
            var scriptLocation = "";            
            var isOL = new RegExp("(^|(.*?\\/))(" + UAPSearch._scriptName + ")(\\?|$)");
         
            var scripts = document.getElementsByTagName('script');
            for (var i=0, len=scripts.length; i<len; i++) {
                var src = scripts[i].getAttribute('src');
                if (src) {
                    var match = src.match(isOL);
                    if(match) {
                        scriptLocation = match[1];
                        break;
                    }
                }
            }
            return scriptLocation;
        }
    };
    /**
     * 
      */
    if(!singleFile) {
        var jsfiles = new Array(
            "jquery-lastest.pack.js"
        ); // etc.

        var agent = navigator.userAgent;
        var docWrite = (agent.match("MSIE") || agent.match("Safari"));
        if(docWrite) {
            var allScriptTags = new Array(jsfiles.length);
        }
		
        var host = UAPSearch._getScriptLocation() + "js/";
        for (var i=0, len=jsfiles.length; i<len; i++) {
            if (docWrite) {
                allScriptTags[i] = "<script src='" + host + jsfiles[i] +
                                   "'></script>"; 
            } else {
                var s = document.createElement("script");
                s.src = host + jsfiles[i];
                var h = document.getElementsByTagName("head").length ? 
                           document.getElementsByTagName("head")[0] : 
                           document.body;
                h.appendChild(s);
            }
        }
        if (docWrite) {
            document.write(allScriptTags.join(""));
        }
    }
})();

/**
 * Constant: VERSION_NUMBER
 */
UAPSearch.VERSION_NUMBER="OpenUAP CMS Search API 1.0 -- $Revision: 9492 $";

function UAPSearch_Ajax(url){
	this.obj = this;
	this.baseUrl=url;
	//this.callbackFunc=callbackFunc;
}
UAPSearch_Ajax.prototype.search=function(callbackFunc,keyword,tid,nid,num)
{
	if(nid==undefined){
		nid="";
	}
	if(num==undefined){
		num=1;
	}
	var kw=encodeURI(keyword);
	var url=this.baseUrl+"plugin/search.jhtml?action=SearchJson&keyword="+kw+"&tableId="+tid+"&nodeId="+nid+"&pageNum="+num+"&jsoncallback=?";
	$.getJSON(url,{},function(data){
		//alert(data);
		callbackFunc(data);
	});
}