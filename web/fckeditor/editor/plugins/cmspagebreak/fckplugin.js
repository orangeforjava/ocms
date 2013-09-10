/*
 * FCKeditor - The text editor for Internet - http://www.fckeditor.net
 * Copyright (C) 2003-2008 Frederico Caldeira Knabben
 *
 * == BEGIN LICENSE ==
 *
 * Licensed under the terms of any of the following licenses at your
 * choice:
 *
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 *
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 *
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * == END LICENSE ==
 *
 * Plugin to insert "PageBreak" in the editor.
 */

// Register the related command.
var FCKCmsPageBreakCommand=function(){this.Name='CmsPageBreak';};
FCKCmsPageBreakCommand.prototype.Execute=function(){
	FCKUndo.SaveUndoStep() ;
	FCK.InsertHtml("#p#副标题#e#");
	//FCK.EditorDocument.selection.createRange().text="#p#副标题#e#";
	FCK.Events.FireEvent( 'OnSelectionChange' ) ;
};
FCKCmsPageBreakCommand.prototype.GetState=function(){return FCK_TRISTATE_OFF;}

FCKCommands.RegisterCommand( 'CmsPageBreak', new FCKCmsPageBreakCommand()) ;

// Create the "PageBreak" toolbar button.
var oCmsPageBreakItem = new FCKToolbarButton( 'CmsPageBreak', FCKLang.CmsPageBreakLbl,FCKLang.CmsPageBreak) ;
oCmsPageBreakItem.IconPath = FCKPlugins.Items['cmspagebreak'].Path + 'pagebreak.gif' ;

FCKToolbarItems.RegisterItem( 'CmsPageBreak', oCmsPageBreakItem) ;