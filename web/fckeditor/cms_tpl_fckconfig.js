//
FCKConfig.BaseHref = '${baseUrl}resource/' ;
//
FCKConfig.ImageBrowserURL="${baseUrl}admin/resourceSelect.jhtml?action=ResourceFrameset&nodeId=0&category=img";
FCKConfig.ImageBrowserWindowWidth  = 558 ;
FCKConfig.ImageBrowserWindowHeight = 446;
//
FCKConfig.LinkBrowserURL = "${baseUrl}admin/resourceSelect.jhtml?action=ResourceFrameset&nodeId=0&category=attach";
FCKConfig.LinkBrowserWindowWidth	= 558 ;
FCKConfig.LinkBrowserWindowHeight	= 446;
//
FCKConfig.FlashBrowserURL = "${baseUrl}admin/resourceSelect.jhtml?action=ResourceFrameset&nodeId=0&category=flash";
FCKConfig.FlashBrowserWindowWidth  =  558 ;
FCKConfig.FlashBrowserWindowHeight =446;
//
FCKConfig.ImageUpload = false ;
FCKConfig.FlashUpload = false ;
FCKConfig.LinkUpload=false;

//
//FCKConfig.ProtectedSource.Add( /\[#[^\]]+[\s\S]*?\]/g );
//FCKConfig.ProtectedSource.Add( /\[\/#[^\]]+[\s\S]*?\]/g );
//FCKConfig.ProtectedSource.Add( /\[\@[^\]]+[\s\S]*?\]/g );
//FCKConfig.ProtectedSource.Add( /\[\/\@[^\]]+[\s\S]*?\]/g );
//
FCKConfig.FormatSource=false;
FCKConfig.FormatOutput= false ;
FCKConfig.FillEmptyBlocks=false;
FCKConfig.ProcessHTMLEntities	= false ;

//
FCKConfig.ToolbarSets["CMS"] = [
	['Source','DocProps','-','FitWindow','ShowBlocks','Preview','-','Templates'],
	['Cut','Copy','Paste','PasteText','PasteWord','-','Print','SpellCheck'],
	['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
	'/',
	['Bold','Italic','Underline','StrikeThrough','-','Subscript','Superscript'],
	['OrderedList','UnorderedList','-','Outdent','Indent','Blockquote'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyFull'],
	['Link','Unlink','Anchor'],
	['Image','Flash','Table','Rule','Smiley','SpecialChar','PageBreak'],
	'/',
	['Style','FontFormat','FontName','FontSize'],
	['TextColor','BGColor']// No comma for the last row.
] ;

FCKConfig.EnterMode = '' ;