function rootGo()
{
	parent.contentPanel.location="admin/resource.jhtml?action=ListResource&nodeId=0";

}
/// XP Look
webFXTreeConfig.rootIcon		= "html/common/tree/images/xp/folder.png";
webFXTreeConfig.openRootIcon	= "html/common/tree/images/xp/openfolder.png";
webFXTreeConfig.folderIcon		= "html/common/tree/images/xp/folder.png";
webFXTreeConfig.openFolderIcon	= "html/common/tree/images/xp/openfolder.png";
webFXTreeConfig.fileIcon		= "html/common/tree/images/xp/folder.png";
webFXTreeConfig.lMinusIcon		= "html/common/tree/images/xp/Lminus.png";
webFXTreeConfig.lPlusIcon		= "html/common/tree/images/xp/Lplus.png";
webFXTreeConfig.tMinusIcon		= "html/common/tree/images/xp/Tminus.png";
webFXTreeConfig.tPlusIcon		= "html/common/tree/images/xp/Tplus.png";
webFXTreeConfig.iIcon			= "html/common/tree/images/xp/I.png";
webFXTreeConfig.lIcon			= "html/common/tree/images/xp/L.png";
webFXTreeConfig.tIcon			= "html/common/tree/images/xp/T.png";
webFXTreeConfig.blankIcon		= "html/common/tree/images/blank.png";
var rti;

var tree = new WebFXTree("站点根","javascript:rootGo();");
	<#list nodes as node>
	<#if nodeManager.getNodeCount(node.nodeId,0,0)==0>
	tree.add(new WebFXTreeItem("${node.name}","admin/resource.jhtml?action=ListResource&nodeId=${node.nodeId?c}", "contentPanel", "${node.nodeId?c}"));
	<#else>
	tree.add(new WebFXLoadTreeItem("${node.name}", "admin/resource.jhtml?action=ResourceXml&nodeId=${node.nodeId?c}", "admin/resource.jhtml?action=ListResource&nodeId=${node.nodeId?c}", "contentPanel", "${node.nodeId?c}"));
	</#if>
	</#list>
//
document.write("<div id='menudata'></div>");
document.write(tree);
