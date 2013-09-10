function sorter(sort_desc,mode,name){
	if(order_desc==sort_desc){
		if(order_mode=='asc'){
			document.searchForm.order_mode.value='desc';
		}else{
			document.searchForm.order_mode.value='asc';
		}
	}else{
		//new sort field
		document.searchForm.order_mode.value=mode;
	}
	//
	document.searchForm.order.value=sort_desc;
	document.searchForm.order_name.value=name;
	//
	document.searchForm.submit();
}
function page_nav(page){
	document.searchForm.page.value=page;
	document.searchForm.submit();
}