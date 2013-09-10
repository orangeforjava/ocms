
function getUserStatus(){
	$.getJSON("${baseUrl}user/us.jhtml", function(json){
  		alert("user name: " + json.userName);
    }); 
}
$(document).ready(function(){
 	getUserStatus();
});
