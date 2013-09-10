var elo;
var loadmenuok=false;
var type = 'main';


cssFile='${baseUrl}html/common/menu/skins/officexp/officexp.css';
//
Menu.prototype.cssFile = cssFile;
var ua = '' + navigator.userAgent;
//
var addheight=0;
if(ua.indexOf('Windows XP')!=-1||ua.indexOf('Windows NT')!=-1){
	if(ua.indexOf('MSIE 6')!=-1||ua.indexOf('MSIE 7')!=-1){
		addheight=20;
		//alert(addheight);
	}
}
function going(action, cId) {
	switch(action) {
		
		case 'publish':
		{
			parent.contentPanel.location = '${baseUrl}admin/publish.jhtml?action=ListContent&nodeId='+cId;

			break;
		}
		case 'create_node':
		{
			parent.contentPanel.location = '${baseUrl}admin/nodeEdit.jhtml?parentId=' + cId; 			
			break;
		}
		case 'create_system_node':
		{
			parent.contentPanel.location = '${baseUrl}admin/nodeEdit.jhtml?extra=system&parentId=' + cId; 			
			break;
		}
		case 'create_node_based':
		{
			var targetNodeID = showModalDialog("${baseUrl}admin/adminSelect.jhtml?action=TargetNodeWindow","node","dialogWidth:324px;dialogHeight:"+(331+addheight)+"px;help:0;status:0;scroll:no");
			//
			if(targetNodeID != null && targetNodeID != '') {
 				parent.panelWork.location = '${baseUrl}admin/nodeEdit.jhtml?mode=new&parentId=' + cId + '&basedNodeId=' + targetNodeID;
			}
			break;
		}
 		case 'del_node':
		{
			if(confirm("确认删除该结点吗?")) {
				parent.menuPanel.location = '${baseUrl}admin/node.jhtml?action=Del&nodeId=' + cId;

			}
			break;
		}
		case 'move_node':
		{
			var targetNodeID = showModalDialog("${baseUrl}admin/adminSelect.jhtml?action=TargetNodeWindow","node","dialogWidth:324px;dialogHeight:"+(331+addheight)+"px;help:0;status:0;scroll:no");

			if(targetNodeID != null && targetNodeID != '') {
				parent.menuPanel.location = '${baseUrl}admin/node.jhtml?action=Move&nodeId=' + cId + '&targetNodeId=' + targetNodeID;
			}
			break;
		}
		case 'sort_node':
		{
			var weight = showModalDialog("${baseUrl}admin/node.jhtml?action=Sort&nodeId=" + cId,"sort","dialogWidth:324px;dialogHeight:"+(150+addheight)+"px;help:0;status:0;scroll:no");

			if(weight != null&&weight!="") {
				returnValue = cms_send('${baseUrl}admin/node.jhtml','action=SortSubmit&weight='+ weight + '&nodeId=' + cId);
				//
				if(returnValue == '1') {
					alert("结点排序权重设置成功");
					document.location.reload();

				} else {
					alert("结点排序权重设置失败");
					document.location.reload();

				}
			}
			break;
		}
		case 'empty_recycle_bin':
		{
			if(confirm("确认清空回收站吗?")) {
				parent.menuPanel.location = '${baseUrl}admin/node.jhtml?action=EmptyRecycleBin&nodeId=' + cId;
			}
			break;
		}
		case 'destroy':
		{
			if(confirm("确认永久删除该结点吗？")) {
				parent.menuPanel.location = '${baseUrl}admin/node.jhtml?action=Destroy&nodeId=' + cId;

			}
			break;
		}
		case 'restore':
		{
			var targetNodeID = showModalDialog("${baseUrl}admin/adminSelect.jhtml?action=TargetNodeWindow","node","dialogWidth:324px;dialogHeight:"+(331+addheight)+"px;help:0;status:0;scroll:no");

			if(targetNodeID != null && targetNodeID != '') {
				//
				parent.actionFrame.location = '${baseUrl}admin/node.jhtml?action=Restore&nodeId=' + cId+ '&targetNodeId=' + targetNodeID;

			}
			break;
		}
	}
}


function rightMenu(cId, extra) {
	//alert(extra)
	var left, top;
	left = window.event.screenX;
	top = window.event.screenY;
	//
	if(cId == 'root') {
		var toolMenu = new Menu();
		//toolMenu.width = 80;
		toolMenu.add(new MenuItem('新建根结点','javascript:going("create_node","0")'));

		//menudata.innerHTML = toolMenu

		elo=window.event.srcElement;
		toolMenu.invalidate();
		toolMenu.show( left, top );

	} else if(cId == 'recycle_bin') {
		var toolMenu = new Menu();
		//toolMenu.width = 80;
		toolMenu.add(new MenuItem('清空回收站','javascript:going("empty_recycle_bin","0")'));

		//menudata.innerHTML = toolMenu

		elo=window.event.srcElement;
		toolMenu.invalidate();
		toolMenu.show( left, top );
	}else if(cId == 'system') {
		var toolMenu = new Menu();
		//toolMenu.width = 80;
		toolMenu.add(new MenuItem('新建系统结点','javascript:going("create_system_node","-1")'));

		//menudata.innerHTML = toolMenu

		elo=window.event.srcElement;
		toolMenu.invalidate();
		toolMenu.show( left, top );
	} else if(extra == 'system') {
		var toolMenu = new Menu();
		//toolMenu.width = 80;
		//
		toolMenu.add(new MenuItem('永久删除','javascript:going("destroy","'+ cId +'")'));

		//menudata.innerHTML = toolMenu

		elo=window.event.srcElement;
		toolMenu.invalidate();
		toolMenu.show( left, top );
	
	} else if(extra == 'recycle_bin') {
		var toolMenu = new Menu();
		//toolMenu.width = 80;
		toolMenu.add(new MenuItem('恢复到..','javascript:going("restore","'+ cId +'")'));
		toolMenu.add(new MenuItem('永久删除','javascript:going("destroy","'+ cId +'")'));

		//menudata.innerHTML = toolMenu

		elo=window.event.srcElement;
		toolMenu.invalidate();
		toolMenu.show( left, top );


	} else {
		var toolMenu = new Menu();
		//toolMenu.width = 90;
		toolMenu.add(new MenuItem('新建子结点','javascript:going("create_node","'+ cId +'")'));
		toolMenu.add(new MenuItem('新建子结点基于..','javascript:going("create_node_based","'+ cId +'")'));
		toolMenu.add(new MenuSeparator());
		toolMenu.add(new MenuItem('结点排序权重','javascript:going("sort_node","'+ cId +'")'));
		toolMenu.add(new MenuItem('移动结点','javascript:going("move_node","'+ cId +'")'));
		toolMenu.add(new MenuItem('删除结点','javascript:going("del_node","'+ cId +'")'));
	    //
		toolMenu.add(new MenuSeparator());
		toolMenu.add(new MenuItem('内容发布管理','javascript:going("publish","'+ cId +'")'));

		//menudata.innerHTML = toolMenu

		elo=window.event.srcElement;
		toolMenu.invalidate();
		toolMenu.show( left, top );
	}

}


/// XP Look
webFXTreeConfig.rootIcon		= "html/common/tree/images/xp/folder.png";
webFXTreeConfig.openRootIcon	= "html/common/tree/images/xp/openfolder.png";
webFXTreeConfig.folderIcon		= "html/common/tree/images/xp/folder.png";
webFXTreeConfig.openFolderIcon	= "html/common/tree/images/xp/openfolder.png";
webFXTreeConfig.fileIcon		= "html/common/tree/images/xp/file.png";
webFXTreeConfig.lMinusIcon		= "html/common/tree/images/xp/Lminus.png";
webFXTreeConfig.lPlusIcon		= "html/common/tree/images/xp/Lplus.png";
webFXTreeConfig.tMinusIcon		= "html/common/tree/images/xp/Tminus.png";
webFXTreeConfig.tPlusIcon		= "html/common/tree/images/xp/Tplus.png";
webFXTreeConfig.iIcon			= "html/common/tree/images/xp/I.png";
webFXTreeConfig.lIcon			= "html/common/tree/images/xp/L.png";
webFXTreeConfig.tIcon			= "html/common/tree/images/xp/T.png";
webFXTreeConfig.blankIcon		= "html/common/tree/images/blank.png";
var rti;
var tree = new WebFXTree("站点根");
	<#list nodes as node>
	<#if nodeManager.getNodeCount(node.nodeId,0,0)==0>
	
	tree.add(new WebFXTreeItem("${node.name}[${node.nodeId?c}]","admin/nodeEdit.jhtml?mode=edit&nodeId=${node.nodeId?c}", "contentPanel", "${node.nodeId?c}"));
	<#else>
	tree.add(new WebFXLoadTreeItem("${node.name}[${node.nodeId?c}]", "admin/node.jhtml?action=SiteXml&nodeId=${node.nodeId?c}", "admin/nodeEdit.jhtml?mode=edit&nodeId=${node.nodeId?c}", "contentPanel", "${node.nodeId?c}"));
	</#if>
	</#list>
	 tree.add(new WebFXLoadTreeItem("**回收站**", "admin/node.jhtml?action=SiteRecycleBinXml", "javascript:void(0)", "panelWork", "recycle_bin",'','${baseUrl}html/common/tree/images/xp/recycle.gif','${baseUrl}html/common/tree/images/xp/recycle.gif'));
	 tree.add(new WebFXLoadTreeItem("**系统保留结点**", "admin/node.jhtml?action=SiteSystemXml&nodeId=-1", "javascript:void(0)", "panelWork", "system",'','${baseUrl}html/common/tree/images/xp/gears.png','${baseUrl}html/common/tree/images/xp/gears.png'));

//
document.write("<div id='menudata'></div>");
document.write(tree);