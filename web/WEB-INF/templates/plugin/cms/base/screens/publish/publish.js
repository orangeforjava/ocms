function createContent()
{
	//
	var caption="添加内容";
	var url=baseUrl+"admin/publishEdit.jhtml?type=main&action=ContentEditorFrameset&mode=add&nodeId="+NodeID;
	openWindow(url,caption);
}
function content_doing(action,IndexID,param){
	switch(action) {
		case 'topIt':
		{
			weight=param['weight'];
			if(weight != null && weight != '') {
				var url=baseUrl+'admin/publish.jhtml?action=Top&weight='+ weight + '&indexId=' + IndexID + '&nodeId=' + NodeID;
				$.post(url, function(data) {
				  var returnValue=data;
				  //alert(returnValue);
				if(returnValue!=null&&returnValue!="-1"){
					alert('置顶设置成功！');
					document.location.reload();
					return true;
				}else{
					alert('置顶设置失败！');
					document.location.reload();
					return false;
				}
				});
			}
			break;
		}
		case 'pinkIt':
		{
			var weight = param['weight'];
			if(weight != null && weight != '') {
				var url=baseUrl+'admin/publish.jhtml?action=Pink&weight='+ weight + '&indexId=' + IndexID + '&nodeId=' + NodeID;
				$.post(url,function(transport) {
						var returnValue=transport;
						if(returnValue!=null&&returnValue!="-1"){
							alert('精华设置成功！');
							document.location.reload();
							return true;
						}else{
							alert('精华设置失败！');
							document.location.reload();
							return false;
						}
					
				});
			}
			break;
		}
		case 'sortIt':
		{
			var weight = param['weight'];
			if(weight != null && weight != '') {
				var url=baseUrl+'admin/publish.jhtml?action=Sort&weight='+ weight + '&indexId=' + IndexID + '&nodeId=' + NodeID;
				$.post(url, function(transport) {
						var returnValue=transport;
						if(returnValue!=null&&returnValue!="-1"){
							alert('排序权重设置成功！');
							document.location.reload();
							return true;
						}else{
							alert('排序权重设置失败！');
							document.location.reload();
							return false;
						}
					}
				);
			}
			break;
		}
		case 'cut':
		{
			var targetNodeID=param;
			if(targetNodeID==NodeID){
				alert('您不能在同一结点内剪切！');
				return;
			}
			if(targetNodeID != null && targetNodeID != '') {
				//document.documentlist.referer.value = document.location;
				document.documentlist.action = baseUrl+'admin/publishEdit.jhtml?action=Cut&indexId=' + IndexID + '&nodeId=' + NodeID + '&targetNodeId=' + targetNodeID  ;
				document.documentlist.submit();
			}
			break;
		}
		case 'copy':
		{
			var targetNodeID=param;
			//alert(targetNodeID);
			if(targetNodeID==NodeID){
				alert('您不能在同一结点内复制！');
				return;
			}
			//
			if(targetNodeID != null && targetNodeID != '') {
				//document.documentlist.referer.value = document.location;
				document.documentlist.action = baseUrl+'admin/publishEdit.jhtml?action=Copy&indexId=' + IndexID + '&nodeId=' + NodeID + '&targetNodeId=' + targetNodeID  ;
				document.documentlist.submit();
			}
			break;
		}
	}
}

/**
* 针对单条内容的操作
**/
function doing(action, IndexID,nid) {
	var returnValue;
	switch(action) {
		case 'createContent':
		{
			createContent();
			break;
		}
		case 'comment':
		{
			//
			var leftPos = (screen.availWidth-800) / 2
			var topPos = (screen.availHeight-600) / 2
			window.open('comment/view.htm?id='+IndexID,'','width=800,height=600,scrollbars=yes,resizable=yes,titlebar=0,top=' + topPos + ',left=' + leftPos);

			break;
		}
		case 'advanced_search':
			parent.mainFrame.location = baseUrl+"admin/publish.jhtml?action=ContentList&nodeId=" +NodeID + "&state=" + IndexID
			break;

		case 'viewLinkState':
		{
			//alert('对不起，此功能正在开发中！');
			//return;
			//
			var leftPos = (screen.availWidth-800) / 2
			var topPos = (screen.availHeight-600) / 2
			window.open( baseUrl+'admin/publish.jhtml?action=ViewLinkState&indexId='+ IndexID  + '&nodeId=' + NodeID,'','width=400,height=300,scrollbars=yes,resizable=yes,titlebar=0,top=' + topPos + ',left=' + leftPos);
			 break;
		}
		case 'topIt':
		{
			globalAction='content_topIt';
			globalIndexID=IndexID;
			
			var url=baseUrl+"/admin/publish.jhtml?action=TopDialog&indexId=" + IndexID + '&nodeId=' + NodeID;
			var caption='置顶权重设置';
			//
			art.dialog.open(url, {title: caption,lock:true});
			break;
		}
		case 'pinkIt':
		{
			globalAction='content_pinkIt';
			globalIndexID=IndexID;
			var url=baseUrl+"/admin/publish.jhtml?action=PinkDialog&indexId=" + IndexID + '&nodeId=' + NodeID;
			var caption='精华权重设置';
			art.dialog.open(url, {title: caption,lock:true});
			//
			

			break;
		}
		case 'sortIt':
		{
			globalAction='content_sortIt';
			globalIndexID=IndexID;
			var url=baseUrl+"admin/publish.jhtml?action=SortDialog&indexId=" + IndexID + '&nodeId=' + NodeID;
			var caption='排序权重设置';
			art.dialog.open(url, {title: caption,lock:true});
			

			break;
		}
		case 'viewpublish':
			var mPath=$('#URL_'+IndexID).attr('href');
			window.open(mPath,"","")

			break;
		case 'getURL':
			//eval('var mPath = URL_' + IndexID + '.href');
			var mPath=$('#URL_'+IndexID).attr('href');
			//alert(mPath);
			prompt("文章发布URL地址",mPath);
			break;
		case 'copyURL':
			var mPath=$('#URL_'+IndexID).attr('href');
			window.clipboardData.setData('Text', mPath);
			break;
		case 'edit':
		{
			var leftPos = (screen.availWidth-1024) / 2;
			var topPos = (screen.availHeight-768) / 2;
			if(nid){
				window.open(baseUrl+'admin/publishEdit.jhtml?action=ContentEditorFrameset&mode=edit&nodeId='+nid  + '&indexId=' + IndexID,'','width=1024,height=768,scrollbars=no,resizable=yes,titlebar=0,top=' + topPos + ',left=' + leftPos);
			
			}else{
				window.open(baseUrl+'admin/publishEdit.jhtml?action=ContentEditorFrameset&mode=edit&nodeId='+NodeID  + '&indexId=' + IndexID,'','width=1024,height=768,scrollbars=no,resizable=yes,titlebar=0,top=' + topPos + ',left=' + leftPos);
			}
			break;
		}
		case 'del':
			if(confirm("确认删除吗?")) {
				document.documentlist.referer.value = document.location;
				document.documentlist.submit();

			}
			break;
		case 'view':
		{
			var leftPos = (screen.availWidth-800) / 2
			var topPos = (screen.availHeight-600) / 2
			 window.open(baseUrl+'admin/publish.jhtml?action=View&nodeId='+NodeID  + '&indexId=' + IndexID+"&keyword=${keyword!''}",'','width=800,height=600,scrollbars=yes,resizable=yes,titlebar=0,top=' + topPos + ',left=' + leftPos);
			break;
		}
		case 'cut':
		{
			globalAction='cut';
			globalIndexID=IndexID;
			var caption='目标结点选择';
			var url=baseUrl+"admin/node.jhtml?action=TargetNodeWindow";
			art.dialog.open(url, {title: caption,lock:true,width:640,height:480});
			//var targetNodeID = showModalDialog(baseUrl+"admin/node.jhtml?action=TargetNodeWindow","color","dialogWidth:324px;dialogHeight:331px;help:0;status:0;scroll:no");
			
			break;
		}
		case 'copy':
		{
			globalAction='copy';
			globalIndexID=IndexID;
			var caption='目标结点选择';
			var url=baseUrl+"admin/node.jhtml?action=TargetNodeWindow";
			art.dialog.open(url, {title: caption,lock:true,width:640,height:480});
			
			break;
		}
		case 'createLink':
		{
			//alert('对不起，此功能正在开发中！');
			//return;
			var targetNodeID = showModalDialog(baseUrl+"admin/adminSelect.jhtml?action=TargetNodeWindow","node","dialogWidth:324px;dialogHeight:331px;help:0;status:0;scroll:no");
			
			if(targetNodeID != null && targetNodeID != '') {
				if(targetNodeID==NodeID){
					alert('对不起，您不能在同一结点创建虚链接！');
					return;
				}
				document.documentlist.referer.value = document.location;
				document.documentlist.action = baseUrl+'admin/publishEdit.jhtml?action=CreateLink&indexId=' + IndexID + '&nodeId=' + NodeID + '&targetNodeId=' + targetNodeID  ;
				document.documentlist.submit();
				//
			}
			break;
		}
		case 'createIndexLink':
		{
			//alert('对不起，此功能正在开发中！');
			//return;
			//
			var targetNodeID = showModalDialog(baseUrl+"admin/adminSelect.jhtml?action=TargetNodeWindow","node","dialogWidth:324px;dialogHeight:331px;help:0;status:0;scroll:no");

			if(targetNodeID != null && targetNodeID != '') {
				if(targetNodeID==NodeID){
					alert('对不起，您不能在同一结点创建虚链接！');
					return;
				}
				document.documentlist.referer.value = document.location;
				document.documentlist.action = baseUrl+'admin/publishEdit.jhtml?action=CreateIndexLink&indexId=' + IndexID + '&nodeId=' + NodeID + '&targetNodeId=' + targetNodeID  ;
				document.documentlist.submit();
				//
			}
			break;
		}
		case 'publish':
		{
			//
			break;
		}
		case 'unpublish':
		{
			//document.documentlist.referer.value = document.location;
			document.documentlist.action = baseUrl+'admin/publish.jhtml?action=UnPublish&indexId=' + IndexID + '&nodeId=' + NodeID;
			document.documentlist.submit();
				//alert(IndexID + '-' + targetNodeID)

			break;
		}
		case 'refresh':
			
			break;
		case 'display_record':
		{
			document.location = baseUrl+"admin/publish.jhtml?action=ContentList&nodeId=" + NodeID + "&pageNum=" + document.documentlist.offset.value;
			break;
		}
		case 'date_search':
		{
			var DateString = document.getElementById('dTime');
			
			document.location = baseUrl+"admin/publishSearch.jhtml?action=DateSearch&tableId="+ TableID +"&nodeId=" + NodeID + "&offset=" + document.documentlist.offset.value + "&date=" + DateString.value;
			//alert(document.location);
			break;
		}
		case 'keyword_search':
		{
			var KeywordsString = document.getElementById('SearchKeywords');
			document.location = baseUrl+"admin/publishSearch.jhtml?action=KeyWordSearch&tableId="+ TableID +"&nodeId=" + NodeID + "&offset=" + document.documentlist.offset.value + "&Keywords=" + encodeURIComponent(KeywordsString.value) ;
			break;
		}
		case 'search_pro':
		{
			var KeywordsString = document.getElementById('SearchKeywords');
			parent.document.location = baseUrl+"admin/publishSearch.jhtml?tableId="+ TableID +"&nodeId=" + NodeID;
			break;
		}
		case 'search':
		{
			searchForm.submit();
			//parent.document.location = "${baseUrl}/admin/publishSearch.jhtml?tableId="+ TableID +"&nodeId=" + NodeID;
			break;
		}
		default:
			document.location = document.location;

	}

}
function site_doing(action,nid,burl) {
	if(nid){
		NodeID=nid;
		globalNodeID=NodeID;
	}
	if(burl){
		baseUrl=burl;
	}
	switch(action) {
		case 'content_manager':
		{
			window.location="${baseUrl}admin/publish.jhtml?action=ContentList2&nodeId="+NodeID;
			parent.$("#menu_frame").attr('src', "${baseUrl}admin/nodeModule.jhtml?action=Menu&nodeId="+NodeID);
			break;
		}
		case 'newdoc':
			var leftPos = (screen.availWidth-1024) / 2
			var topPos = (screen.availHeight-768) / 2
			window.open(baseUrl+'admin/publishEdit.jhtml?action=ContentEditorFrameset&mode=add&nodeId='+NodeID,'','width=1024,height=768,scrollbars=yes,resizable=yes,titlebar=0,top=' + topPos + ',left=' + leftPos);
			//openFullWindow(baseUrl+'admin/publishEdit.jhtml?action=ContentEditorFrameset&mode=add&nodeId='+NodeID,"新增内容");
			break;
		
		case 'tpl':
			//top.panelWork.location = 'admin_tpl.php?op=sId::' + sId +';o::list;NodeID::' + NodeID;

			break;
		case 'site':
			parent.main.location = baseUrl+'admin/nodeEdit.jhtml?mode=edit&nodeId=' + NodeID;

			break;
		case 'publish':
			break;

		case 'refresh':
		{
			globalAction="refresh";
			var url=baseUrl+"admin/publish.jhtml?action=RefreshSettingDialog&nodeId="+ NodeID;
			var caption="设置结点更新属性";
			//openWindow(url,caption,400,330);
			art.dialog.open(url, {title: caption,lock:true});
			break;			
		}
		case 'site_publish':
		{
			globalAction="site_publish";
			var url=baseUrl+"admin/publish.jhtml?action=PublishSettingDialog&nodeId="+ NodeID;
			var caption="设置结点发布属性";
			//openWindow(url,caption,400,330);
			art.dialog.open(url, {title: caption,lock:true});
			
			break;
		}
		case 'refresh_index':
		{
		    var url=baseUrl+'admin/publish.jhtml?action=RefreshNodeIndex&nodeId=' + NodeID;
		    $.post(url,function(data) {
					var returnValue=data;
					if(returnValue!=null&&returnValue!="-1"){
						alert('恭喜，成功开始刷新首页任务！');
					}else{
						alert('对不起，无法开始刷新首页任务！');
					}
				
			});			
			break;
		}
		case 'site_unpublish':
		{
		    globalAction="site_unpublish";
		    var url=baseUrl+"admin/publish.jhtml?action=UnPublishSettingDialog&nodeId="+ NodeID;
			var caption="设置结点取消发布属性";
			//openWindow(url,caption,400,330);
			art.dialog.open(url, {title: caption,lock:true});
			break;

		}
		case 'site_republish':
		{
			globalAction="site_republish";
		    var url=baseUrl+"admin/publish.jhtml?action=UnPublishSettingDialog&nodeId="+ NodeID;
			var caption="设置结点重新发布属性";
			//openWindow(url,caption,400,330);
			art.dialog.open(url, {title: caption,lock:true});
			break;
		}
		
		case 'view_index':
		{
			window.open(baseUrl+"admin/publish.jhtml?action=ViewIndex&nodeId=" +NodeID,'')
			break;
		}
		case "edit_index_template":
		{
			var leftPos = (screen.availWidth-800) / 2
			var topPos = (screen.availHeight-600) / 2
			window.open(baseUrl+'admin/templateEdit.jhtml?action=NodeTplEdit&extra=index&mode=edit&nodeId=' + NodeID + '&indexId=0', '','width=800,height=600,scrollbars=no,resizable=yes,titlebar=0,top=' + topPos + ',left=' + leftPos);
			break;
		}
		case "edit_content_template":
		{
			var IndexID = prompt('输入IndexID（可选）', '');
			var leftPos = (screen.availWidth-800) / 2
			var topPos = (screen.availHeight-600) / 2
			var popupWin = window.open(baseUrl+'admin/templateEdit.jhtml?action=NodeTplEdit&extra=content&mode=edit&nodeId='+NodeID+'&indexId=' + IndexID, '','width=800,height=600,scrollbars=no,resizable=yes,titlebar=0,top=' + topPos + ',left=' + leftPos);			
			break;
		}
		case "edit_img_template":
		{
			var leftPos = (screen.availWidth-800) / 2
			var topPos = (screen.availHeight-600) / 2
			var popupWin = window.open(baseUrl+'admin/templateEdit.jhtml?action=NodeTplEdit&extra=img&mode=edit&nodeId=' + NodeID + '&indexId=0', '','width=800,height=600,scrollbars=no,resizable=yes,titlebar=0,top=' + topPos + ',left=' + leftPos);
			break;
		}
		case "site_migration":
		{
			if(confirm("您确认迁移此结点数据吗？如果您是新系统或者已经做过迁移这个操作是不必要的！")){
				globalAction="site_migration";
				var url=baseUrl+"admin/repo.jhtml?action=MigrationSettingDialog&nodeId="+ NodeID;
				var caption="设置结点数据迁移属性";
				openWindow(url,caption,400,330);
			}		
			break;
			
		}		
 	}
}
var cmenu;
function showMenu(ele,evt,pId, State, Type, Top,Pink, ContributionID){
	 if(evt==null)evt=window.event;//IE
	 var event = $.event.fix(evt); 
	 //alert(evt);
	 if(cmenu){
		 cmenu.hide();
	 }
	 if(Type == '1') {
		var menu = [
				{'剪切': function() {doing("cut",pId )} },
				{ '复制':function(){doing("copy",pId )} },
				$.contextMenu.separator,
				{ '置顶设置': function() {doing("topIt",pId )} },
				{ '精华设置': function() {doing("pinkIt",pId )} },
				{ '排序权重设置': function() {doing("sortIt",pId )} },
				
				];
		if(State == 1){
			menu.push($.contextMenu.separator);
			menu.push({'复制发布URL':function(){doing("copyURL",pId)}});
			menu.push({'获取发布URL':function(){doing("getURL",pId)}});
			menu.push({'取消发布':function(){doing("unpublish",pId)}})
		
		}
	 	cmenu = $.contextMenu.create(menu,{theme:'vista'});
	 	//alert('cmenu');
	 	cmenu.show($('#menu_'+pId),event);
	 	
	 }
	 evt.cancelBubble = true;
  	 evt.returnValue  = false;
	 return false;
}

function rightMenu(evt,pId, State, Type, Top,Pink, ContributionID) {
	if(evt==null)evt=window.event;//IE
	var toolMenu = new Menu();
	

	if(Type == '1') {
		//toolMenu.add(new WebFXMenuItem('查看','javascript:doing("view","'+ pId +'")','查看文档内容'));

		if(ContributionID != '0') {
			toolMenu.add(new MenuItem('查看批注(来稿)','javascript:doing("note","'+ ContributionID +'")'));
		}

		toolMenu.add(new MenuItem('剪切','javascript:doing("cut","'+ pId +'")'));
		toolMenu.add(new MenuItem('复制','javascript:doing("copy","'+ pId +'")'));
		toolMenu.add(new MenuSeparator());
		toolMenu.add(new MenuItem('创建虚链接','javascript:doing("createLink","'+ pId +'")'));
		toolMenu.add(new MenuItem('创建索引链接','javascript:doing("createIndexLink","'+ pId +'")'));
		toolMenu.add(new MenuItem('查看链接状态','javascript:doing("viewLinkState","'+ pId +'")'));
	} else {

		toolMenu.add(new MenuItem('剪切','javascript:doing("cut","'+ pId +'")'));
		toolMenu.add(new MenuItem('复制','javascript:doing("copy","'+ pId +'")'));
		toolMenu.add(new MenuSeparator());
		toolMenu.add(new MenuItem('查看链接状态','javascript:doing("viewLinkState","'+ pId +'")'));


	}

	toolMenu.add(new MenuSeparator());

	toolMenu.add(new MenuItem('置顶设置','javascript:doing("topIt","'+ pId +'")'));

	toolMenu.add(new MenuItem('精华设置','javascript:doing("pinkIt","'+ pId +'")'));
	toolMenu.add(new MenuItem('排序权重设置','javascript:doing("sortIt","'+ pId +'")'));




	if(State == 0 ) {
		//toolMenu.add(new MenuSeparator());


	} else if(State == 1 ) {
		toolMenu.add(new MenuSeparator());

		toolMenu.add(new MenuItem('复制发布URL','javascript:doing("copyURL","'+ pId +'")'));
		toolMenu.add(new MenuItem('获取发布URL','javascript:doing("getURL","'+ pId +'")'));

		toolMenu.add(new MenuItem('取消发布','javascript:doing("unpublish","'+ pId +'")'));

	} else if (State == 1 && Type==2) {
		toolMenu.add(new MenuSeparator());

		toolMenu.add(new MenuItem('复制发布URL','javascript:doing("copyURL","'+ pId +'")'));
		toolMenu.add(new MenuItem('获取发布URL','javascript:doing("getURL","'+ pId +'")'));

	}

	var left,top;
	left = evt.screenX;
	top = evt.screenY;
	toolMenu.invalidate();
	toolMenu.show( left, top );
}
function content_going(action,rs){
	switch(action){
		case 'copy':
			{
				if(rs==NodeID){
					alert('您不能在同一结点内复制！');
					return;
				}
				//
				if(rs != null && rs != '') {
					//document.documentlist.referer.value = document.location;
					document.documentlist.action = baseUrl+'admin/publishEdit.jhtml?action=Copy&multi=1&nodeId=' + NodeID+'&targetNodeId=' + rs;
					document.documentlist.submit();
					//alert(IndexID + '-' + targetNodeID)
				}
				break;
			}
		case 'cut':
			{
				if(rs==NodeID){
					alert('您不能在同一结点内剪切！');
					return;
				}
				//
				if(rs != null && rs != '') {
					//document.documentlist.referer.value = document.location;
					document.documentlist.action = baseUrl+'admin/publishEdit.jhtml?action=Cut&multi=1&nodeId=' + NodeID+'&targetNodeId=' + rs;
					document.documentlist.submit();
				}
			}
	}
}
/**
* 多条内容处理
**/
function going(form, o)
{
	document.documentlist.referer.value = document.location;

	with(form) {
		var count = 0;
      	var checkboxObj = pData;

      	if(checkboxObj == null)
      	{
        	return ;
      	}
      	else
      	{
        	for (var i = 0; i < checkboxObj.length; i++)
        	{
          		if(checkboxObj[i].checked)
          		count++;
        	}
      	}
		if(count > 0 || (checkboxObj.length == null && checkboxObj.checked))
		{
		switch(o) {
			case 'planPublish':
			{
			alert('对不起，此功能正在开发中！');
			
				break;
			}
			case 'refresh':
			{
				//document.documentlist.referer.value = document.location;
				document.documentlist.action = baseUrl+'admin/publish.jhtml?action=Refresh&multi=1&nodeId=' + NodeID;
				document.documentlist.submit();
				break;
			}
			case 'publish':
			{
				//document.documentlist.referer.value = document.location;
				document.documentlist.action = baseUrl+'admin/publish.jhtml?action=Publish&multi=1&nodeId=' + NodeID  ;
				document.documentlist.submit();
				break;
			}
			case 'unpublish':
			{
				//document.documentlist.referer.value = document.location;
				document.documentlist.action = baseUrl+'admin/publish.jhtml?action=UnPublish&multi=1&nodeId=' + NodeID;
				document.documentlist.submit();
				break;
			}
			case 'copy':
			{
				var url = baseUrl+"admin/node.jhtml?action=TargetNodeWindow";
				var caption="选择目标结点";
				globalAction="multi_copy";
				art.dialog.open(url, {title: caption,lock:true,width:640,height:480});
				break;
			}
			case 'cut':
			{
				var url =baseUrl+"admin/node.jhtml?action=TargetNodeWindow";
				var caption="选择目标结点";
				globalAction="multi_cut";
				art.dialog.open(url, {title: caption,lock:true,width:640,height:480});
				
				
				break;
			}
			case 'del':
			{
				if(confirm("确认批量删除吗?")) {
					document.documentlist.action = baseUrl+'admin/publishEdit.jhtml?action=Del&multi=1&nodeId='+ NodeID;
					document.documentlist.submit();
				}
				break;
			}
			case 'createLink':
			{
				alert('对不起，此功能正在开发中！');
				return;
				//
				
				break;
			}
			case 'createIndexLink':
			{
				alert('对不起，此功能正在开发中！');
				return;
				//
				break;
				}
			}//end switch
			
		}//end if
		else{
			alert("请选择一个或多个内容。");
		}
	}
}
//结点更新
function refreshNode(arr){
    //
	if(arr['content_num'] != null) {
			
			var url=baseUrl+'admin/publish.jhtml?action=RefreshNode&nodeId=' + NodeID + '&refreshIndex=' + arr['refresh_index'] + '&refreshContent=' + arr['refresh_content'] + '&refreshExtra=' + arr['refresh_extra'] + '&includeSub=' + arr['include_sub'] + '&contentNum=' + arr['content_num'];
			//
			$.post(url,function(transport) {
					var returnValue=transport;
					if(returnValue!=null&&returnValue!="-1"){
						alert('恭喜，成功开始结点更新任务！');
					}else{
						alert('对不起，无法开始结点更新任务！');
					}
				}
			);
	}
}
//结点发布
function publishNode(arr){
	if(arr['content_num'] != null && arr['content_num'] != '') {
				var url=baseUrl+'admin/publish.jhtml?action=PublishNode&nodeId='+ NodeID + '&includeSub=' + arr['include_sub'] + '&contentNum=' + arr['content_num'];
				$.post(url,function(transport) {
					var returnValue=transport;
					if(returnValue!=null&&returnValue!="-1"){
						alert('恭喜，成功开始结点发布任务！');
					}else{
						alert('对不起，无法开始结点发布任务！');
					}
				}
				);

	}
}
//结点取消发布
function unpublishNode(arr){
	if(arr['content_num'] != null && arr['content_num'] != '')             {
				var url=baseUrl+'admin/publish.jhtml?action=UnPublishNode&nodeId=' + NodeID +  '&includeSub=' + arr['include_sub'] + '&contentNum=' + arr['content_num'];
				$.post(url, function(transport) {
						var returnValue=transport;
						if(returnValue!=null&&returnValue!="-1"){
							alert('恭喜，成功开始结点取消发布任务！');
						}else{
							alert('对不起，无法开始结点取消发布任务！');
						}
					}
					);
	}
}
//结点重新发布
function republishNode(arr){
	if(arr['content_num'] != null && arr['content_num'] != '')
	{

		var url=baseUrl+'admin/publish.jhtml?action=RePublishNode&nodeId=' + NodeID +  '&includeSub=' + arr['include_sub'] + '&contentNum=' + arr['content_num'];
		$.post(url,function(transport) {
				var returnValue=transport;
				if(returnValue!=null&&returnValue!="-1"){
					alert('恭喜，成功开始结点重新发布任务！');
				}else{
					alert('对不起，无法开始结点重新发布任务！');
				}
			}
			);
				
	}
}
//迁移结点数据
function migrationNodeData(arr){
	if(arr['content_num'] != null && arr['content_num'] != ''){
		this.location = baseUrl+'admin/repo.jhtml?action=Migration&nodeId=' + NodeID+'&includeSub=' + arr['include_sub'] + '&contentNum=' + arr['content_num'];
	}
}

function viewComment(indexId){
	
}
if (!window.createPopup) {
	var __createPopup = function() {
		var SetElementStyles = function( element, styleDict ) {
			var style = element.style ;
			for ( var styleName in styleDict )style[ styleName ] = styleDict[ styleName ] ; 
		}
		var eDiv = document.createElement( 'div' ); 
		SetElementStyles( eDiv, { 'position': 'absolute', 'top': 0 + 'px', 'left': 0 + 'px', 'width': 0 + 'px', 'height': 0 + 'px', 'zIndex': 1000, 'display' : 'none', 'overflow' : 'hidden' } ) ;
		eDiv.body = eDiv ;
		var opened = false ;
		var setOpened = function( b ) {
			opened = b; 
		}
		var getOpened = function() {
			return opened ; 
		}
		var getCoordinates = function( oElement ) {
			var coordinates = {x:0,y:0} ; 
			while( oElement ) {
				coordinates.x += oElement.offsetLeft ;
				coordinates.y += oElement.offsetTop ;
				oElement = oElement.offsetParent ;
			}
			return coordinates ;
		}
		return {htmlTxt : '', document : eDiv, isOpen : getOpened(), isShow : false, hide : function() { SetElementStyles( eDiv, { 'top': 0 + 'px', 'left': 0 + 'px', 'width': 0 + 'px', 'height': 0 + 'px', 'display' : 'none' } ) ; eDiv.innerHTML = '' ; this.isShow = false ; }, show : function( iX, iY, iWidth, iHeight, oElement ) { if (!getOpened()) { document.body.appendChild( eDiv ) ; setOpened( true ) ; } ; this.htmlTxt = eDiv.innerHTML ; if (this.isShow) { this.hide() ; } ; eDiv.innerHTML = this.htmlTxt ; var coordinates = getCoordinates ( oElement ) ; eDiv.style.top = ( iX + coordinates.x ) + 'px' ; eDiv.style.left = ( iY + coordinates.y ) + 'px' ; eDiv.style.width = iWidth + 'px' ; eDiv.style.height = iHeight + 'px' ; eDiv.style.display = 'block' ; this.isShow = true ; } }
	}
	window.createPopup = function() {
		return __createPopup(); 
	}
}