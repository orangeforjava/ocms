(function() {
    /**
     *
     */
    var singleFile = (typeof UAPPassport == "object" && UAPPassport.singleFile);
    
    /**
     * Namespace: uapPP
     * The uapPP object provides a namespace for all things uapPP
     */
    window.UAPPassport = {
        
        /**
         * Property: _scriptName
         * {String} Relative path of this script.
         */
        _scriptName: (!singleFile) ? "js/uap_pp_api.js" : "uap_pp_api.js",

        /**
         * Function: _getScriptLocation
         * Return the path to this script.
         *
         * Returns:
         * {String} Path to this script
         */
        _getScriptLocation: function () {
            var scriptLocation = "";            
            var isOL = new RegExp("(^|(.*?\\/))(" + UAPPassport._scriptName + ")(\\?|$)");
         
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
     * Mapabc.singleFile is a flag indicating this file is being included
     * in a Single File Library build of the Mapabc Library.
     * 
     * When we are *not* part of a SFL build we dynamically include the
     * Mapabc library code.
     * 
     * When we *are* part of a SFL build we do not dynamically include the 
     * Mapabc library code as it will be appended at the end of this file.
      */
    if(!singleFile) {
        var jsfiles = new Array(
            "jquery-1.4.2.min.js",
            "Class.js",
            "uap_pp.js"
        ); // etc.

        var agent = navigator.userAgent;
        var docWrite = (agent.match("MSIE") || agent.match("Safari"));
        if(docWrite) {
            var allScriptTags = new Array(jsfiles.length);
        }
		
        var host = UAPPassport._getScriptLocation() + "js/";
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
UAPPassport.VERSION_NUMBER="OpenUAP Passport API 1.0 -- $Revision: 9492 $";
