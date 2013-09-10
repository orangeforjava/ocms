/*----------------------------------------------------------------------------\
|                           Column List Widget 1.02                           |
|-----------------------------------------------------------------------------|
|                          Created by Emil A Eklund                           |
|                  (http://webfx.eae.net/contact.html#emil)                   |
|                      For WebFX (http://webfx.eae.net/)                      |
|-----------------------------------------------------------------------------|
| An  object based column list widget,  can either generate the required html |
| elements from supplied data or attach to an existing html structure.        |
|-----------------------------------------------------------------------------|
|                   Copyright (c) 2003 - 2004 Emil A Eklund                   |
|-----------------------------------------------------------------------------|
| This software is provided "as is", without warranty of any kind, express or |
| implied, including  but not limited  to the warranties of  merchantability, |
| fitness for a particular purpose and noninfringement. In no event shall the |
| authors or  copyright  holders be  liable for any claim,  damages or  other |
| liability, whether  in an  action of  contract, tort  or otherwise, arising |
| from,  out of  or in  connection with  the software or  the  use  or  other |
| dealings in the software.                                                   |
| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - |
| This  software is  available under the  three different licenses  mentioned |
| below.  To use this software you must chose, and qualify, for one of those. |
| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - |
| The WebFX Non-Commercial License          http://webfx.eae.net/license.html |
| Permits  anyone the right to use the  software in a  non-commercial context |
| free of charge.                                                             |
| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - |
| The WebFX Commercial license           http://webfx.eae.net/commercial.html |
| Permits the  license holder the right to use  the software in a  commercial |
| context. Such license must be specifically obtained, however it's valid for |
| any number of  implementations of the licensed software.                    |
| - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - |
| GPL - The GNU General Public License    http://www.gnu.org/licenses/gpl.txt |
| Permits anyone the right to use and modify the software without limitations |
| as long as proper  credits are given  and the original  and modified source |
| code are included. Requires  that the final product, software derivate from |
| the original  source or any  software  utilizing a GPL  component, such  as |
| this, is also licensed under the GPL license.                               |
|-----------------------------------------------------------------------------|
| 2004-08-22 | Work started.                                                  |
| 2004-10-24 | First version published.                                       |
| 2004-11-01 | Added resizeColumns property,  allowing column  resizing to be |
|            | enabled/disabled.  Fixed a bug in the  sort method that caused |
|            | getSelectedRow to return an invalid index when called from the |
|            | function triggered by onselect. Misc bugfixes for Mozilla.     |
| 2004-11-23 | Fixed column resizing in mozilla.  It still can't make columns |
|            | smaller than their content, but otherwise it seems to work.    |
| 2005-05-22 | Fixed a bug in the removeRange method for index based removal, |
|            | where the rows where deleted in the wrong order.  Also added a |
|            | clear method, that removes all rows.                           |
|-----------------------------------------------------------------------------|
| Created 2004-08-22 | All changes are in the log above. | Updated 2005-05-22 |
\----------------------------------------------------------------------------*/

var IMG_DESC = 'desc.png';
var IMG_ASC  = '/html/columnlist/asc.png';

var TYPE_NUMERIC = 0;
var TYPE_STRING  = 1;
var TYPE_DATE    = 2;

var SORT_ASCENDING  = 0;
var SORT_DESCENDING = 1;

/*
 * oColumnList = new WebFXColumnList()
 * Default constructor
 */
function WebFXColumnList() {
	/* public properties */
	this.multiple       = true;
	this.sortCol        = -1;
	this.sortDescending  = 0;
	this.error          = '';
	this.selectedRows   = [];
	this.colorEvenRows  = true;
	this.resizeColumns  = true;
	this.bodyColResize  = true;
	//added by Weiping Ju $Date: 2006/08/31 02:26:30 $
	this.allowSort=true;
	//end added
	

	/* events */
	this.onresize       = null;
	this.onsort         = null;
	this.onselect       = null;

	/* private properties */
	this._eCont         = null;
	this._eHead         = null;
	this._eBody         = null;
	this._eHeadTable    = null;
	this._eBodyTable    = null;
	this._eHeadCols     = null;
	this._eBodyCols     = null;

	this._activeHeaders = null;
	this._aType         = null;
	this._rows          = 0;
	this._cols          = 0;
}

/*
 * iError = create(eContent, sColumn[], aData[][])
 * Transforms the supplied container into an empty column list.
 * aData[][] is optional
*/
WebFXColumnList.prototype.create = function(eContent, aColumns) {
	var eRow, eCell, eDiv, eImg, eTableBody, eColGroup, eCol;

	for (var i = eContent.childNodes.length - 1; i >= 0; i--) {
		eContent.removeChild(eContent.childNodes[i]);
	}

	/* Create container, header and body */
	this._eCont = eContent;
	this._eHead = document.createElement('div');
	this._eBody = document.createElement('div');
	this._eCont.className = 'webfx-columnlist';
	this._eHead.className = 'webfx-columnlist-head';
	this._eBody.className = 'webfx-columnlist-body';
	this._eCont.appendChild(this._eHead);
	this._eCont.appendChild(this._eBody);

	/* Populate header */
	this._eHeadTable = document.createElement('table');
	this._eHeadTable.cellSpacing = 0;
	this._eHeadTable.cellPadding = 0;
	this._eHead.appendChild(this._eHeadTable);
	eTableBody = document.createElement('tbody');
	this._eHeadTable.appendChild(eTableBody);
	eRow = document.createElement('tr');
	eTableBody.appendChild(eRow);
	for (var i = 0; i < aColumns.length; i++) {
		eCell = document.createElement('td');  eRow.appendChild(eCell);
		eImg  = document.createElement('img');
		eCell.appendChild(document.createTextNode(aColumns[i]));
		eCell.appendChild(eImg);
	}

	/* Create main table, colgroup and col elements */
	this._eBodyTable = document.createElement('table');
	this._eBodyTable.cellSpacing = 0;
	this._eBodyTable.cellPadding = 0;
	this._eBody.appendChild(this._eBodyTable);
	eTableBody = document.createElement('tbody');
	this._eBodyTable.appendChild(eTableBody);
	eColGroup = document.createElement('colgroup');
	this._eBodyTable.appendChild(eColGroup);
	for (var i = 0; i < aColumns.length; i++) {
		eCol = document.createElement('col');
		eCol.style.width = 'auto';
		eColGroup.appendChild(eCol);
	}

	this._eHeadCols = eRow.cells;
	this._eBodyCols = null;

	this._cols = aColumns.length;
	this._rows = 0;

	this._init();

	return 0;
};

/*
 * iError = bind(eContainer, eHeader, eBody)
 * Binds column list to an existing HTML structure. Use create
 * to generate the strucutr automatically.
*/
WebFXColumnList.prototype.bind = function(eCont, eHead, eBody) {
	try {
		this._eCont      = eCont;
		this._eHead      = eHead;
		this._eBody      = eBody;
		this._eHeadTable = this._eHead.getElementsByTagName('table')[0];
		this._eBodyTable = this._eBody.getElementsByTagName('table')[0];
	 	this._eHeadCols  = this._eHeadTable.tBodies[0].rows[0].cells;
		this._eBodyCols  = this._eBodyTable.tBodies[0].rows[0].cells;
	}
	catch(oe) {
		this.error = 'Unable to bind to elements: ' + oe.message;
		return 1;
	}
	if (this._eHeadCols.length != this._eBodyCols.length) {
		this.error = 'Unable to bind to elements: Number of columns in header and body does not match.';
		return 2;
	}

	this._eHeadCols  = this._eHeadTable.tBodies[0].rows[0].cells;
	this._eBodyCols  = this._eBodyTable.tBodies[0].rows[0].cells;

	this._cols = this._eHeadCols.length;
	this._rows = this._eBodyTable.tBodies[0].rows.length;

	this._init();

	return 0;
};

/*
 * void _init(iWidth, iHeight)
 * Initializes column list, called by create and bind
*/
WebFXColumnList.prototype._init = function(iWidth, iHeight) {
	if (navigator.product == 'Gecko') {
		/*
			Mozilla does not allow the scroll* properties of containers with the
			overflow property set to 'hidden' thus we'll have to set it to
			'-moz-scrollbars-none' which is basically the same as 'hidden' in IE,
			the container has overflow type 'scroll' but no scrollbars are shown.
		*/
		for (var n = 0; n < document.styleSheets.length; n++) {
			if (document.styleSheets[n].href.indexOf('columnlist.css') == -1) { continue; }
			var rules = document.styleSheets[n].cssRules;
			for (var i = 0; i < rules.length; i++) {
				if ((rules[i].type == CSSRule.STYLE_RULE) && (rules[i].selectorText == '.webfx-columnlist-head')) {
					rules[i].style.overflow = '-moz-scrollbars-none';
	}	}	}	}

	this.calcSize();
	this._assignEventHandlers();
	if (this.colorEvenRows) { this._colorEvenRows(); }

	this._stl = new SortableTable(this._eBodyTable);
}

/*
 * void _assignEventHandlers()
 * Assigns event handlers to the grid elements, called by bind.
*/
WebFXColumnList.prototype._assignEventHandlers = function() {
	var oThis = this;
	this._eCont.onclick     = function(e) { oThis._click(e); }
	if (this.resizeColumns) {
		this._eCont.onmousedown = function(e) { oThis._mouseDown(e); }
		this._eCont.onmousemove = function(e) { oThis._mouseMove(e); }
	}
	this._eCont.onmouseup   = function(e) { oThis._mouseUp(e); }
	this._eCont.onselectstart = function(e) { return false; }
	this._eBody.onscroll = function() {
		oThis._eHead.scrollLeft = oThis._eBody.scrollLeft;
	};
	this._eCont.onkeydown = function(e) {
		var el = (e)?e.target:window.event.srcElement;
		var key = (e)?e.keyCode:window.event.keyCode;
		if (oThis._handleRowKey(key)) { return; }
		if (window.event) { window.event.cancelBubble = true; }
		else { e.preventDefault(); e.stopPropagation() }
		return false;
	};
};

/*
 * void setTypes(aTypes)
 * Sets the column types to the values supplied in the array
 * Valid types are: TYPE_NUMERIC, TYPE_STRING and TYPE_DATE.
 * Affects how the column is sorted.
 */
WebFXColumnList.prototype.setTypes = function(a) {
	this._aType = a;
};

/* void calcSize()
 * Used to calculate the desired size of the grid and size it accordingly.
 */
WebFXColumnList.prototype.calcSize = function() {
	if (this._eCont.offsetWidth >= 4) {

		/* Size body */
		var h = this._eCont.clientHeight - this._eHead.offsetHeight - 2;
		if (h >= 0) { this._eBody.style.height = h + 'px'; }
		this._eBody.style.width = this._eCont.offsetWidth - 2 + 'px';
		this._eBody.style.paddingTop = this._eHead.offsetHeight + 'px';
		this._eBodyTable.style.width = this._eBody.clientWidth + 'px';

		/* Size header */
		var bNoScrollbar = ((this._eBody.offsetWidth - this._eBody.clientWidth) == 2);
		this._eHeadTable.style.width = this._eHead.style.width = this._eBody.clientWidth + ((bNoScrollbar)?2:0) + 'px';

		/* Size columns */
		if (this._eBodyCols) {
			var length = this._eBodyCols.length;
			for (var i = 0; i < length; i++) {
				this._eHeadCols[i].style.width = (this._eBodyCols[i].offsetWidth - 4) + 'px';
	}	}	}

	this._eHeadTable.style.width = 'auto';
};

/*
 * iErrorCode = selectRow(iRowIndex, bMultiple)
 * Selects the row identified by the sequence number supplied,
 *
 * If bMultiple is specified and multi-select is allowed the
 * the previously selected row will not be deselected. If the
 * specified row is already selected it will be deselected.
 */
WebFXColumnList.prototype.selectRow = function(iRowIndex, bMultiple) {
	if ((iRowIndex < 0) || (iRowIndex > this._rows - 1)) {
		this.error = 'Unable to select row, index out of range.';
		return 1;
	}
	var eRows = this._eBodyTable.tBodies[0].rows;
	var bSelect = true;
	/* Normal click */
	if ((!bMultiple) || (!this.multiple)) {
		/* Deselect previously selected rows */
		while (this.selectedRows.length) {
			eRows[this.selectedRows[0]].className = (this.selectedRows[0] & 1)?'odd':'even';
			this.selectedRows.splice(0, 1);
	}	}
	else {
		/* Control + Click */
		for (var i = 0; i < this.selectedRows.length; i++) {
			if (this.selectedRows[i] == iRowIndex) {
				/* Deselect clicked row */
				eRows[this.selectedRows[i]].className = (i & 1)?'odd':'even';
				this.selectedRows.splice(i, 1);
				bSelect = false;
				break;
	}	}	}

	if (bSelect) {
		/* Select clicked row */
		this.selectedRows.push(iRowIndex);
		eRows[iRowIndex].className = 'selected';
	}

	var a = (eRows[iRowIndex].offsetTop + this._eHead.offsetHeight) + eRows[iRowIndex].offsetHeight + 1;
	var b = (this._eBody.clientHeight + this._eBody.scrollTop);
	if (a > b) {
		this._eBody.scrollTop = (a - this._eBody.clientHeight);
	}
	var c = eRows[iRowIndex].offsetTop;
	var d = this._eBody.scrollTop;
	if (c < d) {
		this._eBody.scrollTop = c;
	}

	/* Call onselect if defined */
	if (this.onselect) { this.onselect(this.selectedRows); }

	return 0;
};

/*
 * iErrorCode = selectRange(iRowIndex[])
 * iErrorCode = selectRange(iFromRowIndex, iToRowIndex)
 * Selects all rows between iFromRowIndex and iToRowIndex.
 */
WebFXColumnList.prototype.selectRange = function(a, b) {
	var aRowIndex;
	if (typeof a == 'number') {
		aRowIndex = new Array();
		for (var i = a; i <= b; i++) { aRowIndex.push(i); }
		for (var i = b; i <= a; i++) { aRowIndex.push(i); }
	}
	else { aRowIndex = a; }

	for (var i = 0; i < aRowIndex.length; i++) {
		if ((aRowIndex[i] < 0) || (aRowIndex[i] > this._rows - 1)) {
			this.error = 'Unable to select rows, index out of range.';
			return 1;
	}	}

	/* Deselect previously selected rows */
	var eRows = this._eBodyTable.tBodies[0].rows;
	while (this.selectedRows.length) {
		eRows[this.selectedRows[0]].className = (this.selectedRows[0] & 1)?'odd':'even';
		this.selectedRows.splice(0, 1);
	}

	/* Select all rows indicated by range */
	var eRows = this._eBodyTable.tBodies[0].rows;
	var bMatch;
	for (var i = 0; i < aRowIndex.length; i++) {
		bMatch = false;
		for (var j = 0; j < this.selectedRows.length; j++) {
			if (this.selectedRows[j] == aRowIndex[i]) { bMatch = true; break; }
		}
		if (!bMatch) {
			/* Select row */
			this.selectedRows.push(aRowIndex[i]);
			eRows[aRowIndex[i]].className = 'selected';
	}	}

	/* Call onselect if defined */
	if (this.onselect) { this.onselect(this.selectedRows); }

	return 0;
};

/*
 * void resize(iWidth, iHeight)
 * Resize the grid to the given dimensions, the outer (border) size is given, not the inner (content) size.
 */
WebFXColumnList.prototype.resize = function(w, h) {
	this._eCont.style.width = w + 'px';
	this._eCont.style.height = h + 'px';
	this.calcSize();

	/* Call onresize if defined */
	if (this.onresize) { this.onresize(); }
};

/*
 * void _colorEvenRows()
 * Changes the color of even rows (usually to light yellow) to make it easier to read.
 * Also updates the id column to a sequence counter rather than the row ids.
 */
WebFXColumnList.prototype._colorEvenRows = function() {
	if (this._eBodyTable.tBodies.length) {
		var nodes = this._eBodyTable.tBodies[0].rows;
		var len = nodes.length;
		for (var i = 0; i < len; i++) {
			if (nodes[i].className != 'selected') {
				nodes[i].className = (i & 1)?'odd':'even';
	}	}	}
};

/*
 * iErrorCode = addRow(aRowData)
 * Appends supplied row to the column list.
 */
WebFXColumnList.prototype.addRow = function(aRowData) {
	var rc = this._addRow(aRowData);
	if (rc) { return rc; }
	this.calcSize();
	return 0;
};

/*
 * iErrorCode = addRows(aData)
 * Appends supplied rows to the column list.
 */
WebFXColumnList.prototype.addRows = function(aData) {
	for (var i = 0; i < aData.length; i++) {
		var rc = this._addRow(aData[i]);
		if (rc) { return rc; }
	}
	this.calcSize();
	return 0;
};

/*
 * void _colorEvenRows()
 * Changes the color of even rows (usually to light yellow) to make it easier to read.
 * Also updates the id column to a sequence counter rather than the row ids.
 */
WebFXColumnList.prototype._colorEvenRows = function() {
	if (this._eBodyTable.tBodies.length) {
		var nodes = this._eBodyTable.tBodies[0].rows;
		for (var i = 0; i < nodes.length; i++) {
			if (nodes[i].className != 'selected') {
				nodes[i].className = (i & 1)?'odd':'even';
	}	}	}
};

/*
 * iErrorCode = _addRow(aRowData)
 */
WebFXColumnList.prototype._addRow = function(aRowData) {
	var eBody, eRow, eCell, eCont, i, len;

	/* Validate column count */
	if (aRowData.length != this._cols) { return 1; }

	/* Construct Body Row */
	eBody = this._eBodyTable.tBodies[0];
	eRow  = document.createElement('tr');
	eRow.className = (this._rows & 1)?'odd':'even';

	for (i = 0; i < this._cols; i++) {
		eCell = document.createElement('td');
		eCont = document.createElement('div');
		eCont.appendChild(document.createTextNode(aRowData[i]));
		eCell.appendChild(eCont);
		eRow.appendChild(eCell);
	}
	eBody.appendChild(eRow);

	/* Update row counter */
	this._rows++;

	if (this._eBodyCols == null) {
		this._eBodyCols = this._eBodyTable.tBodies[0].rows[0].cells;
	}

	return 0;
};

/*
 * iErrorCode = removeRow(iRowIndex)
 * Appends supplied row to the grid.
 */
WebFXColumnList.prototype.removeRow = function(iRowIndex) {
	/* Remove row */
	var rc = this._removeRow(iRowIndex);
	if (rc) { return rc; }

	/* Update row counter and select previous row, if any */
	this._rows--;
	this.selectRow((iRowIndex > 1)?iRowIndex-1:0);

	/* Recolor rows, if needed */
	if (this.colorEvenRows) { this._colorEvenRows(); }
	this.calcSize();

	/* Call onselect if defined */
	if (this.onselect) { this.onselect(this.selectedRows); }

	return 0;
};

/*
 * iErrorCode = removeRange(iRowIndex[])
 * iErrorCode = removeRange(iFirstRowIndex, iLastRowIndex)
 * Appends supplied row to the grid.
 */
WebFXColumnList.prototype.removeRange = function(a, b) {
	var aRowIndex = new Array();
	if (typeof a == 'number') {
		for (var i = a; i <= b; i++) { aRowIndex.push(i); }
	}
	else {
		for (var i = 0; i < a.length; i++) {
			aRowIndex.push(a[i]);
		}
		aRowIndex.sort(compareNumericAsc);
	}

	while ((i = aRowIndex.pop()) >= 0) {
		var rc = this._removeRow(i);
		this._rows--;
	}
	
	/* Recolor rows, if needed */
	if (this.colorEvenRows) { this._colorEvenRows(); }
	this.calcSize();

	/* Call onselect if defined */
	if (this.onselect) { this.onselect(this.selectedRows); }

	return 0;
};

/*
 * iErrorCode = clear()
 * Removes all rows from the column list.
 */
WebFXColumnList.prototype.clear = function() {
	return this.removeRange(0, this._rows - 1);
}

/*
 * iErrorCode = _removeRow(iRowIndex)
 */
WebFXColumnList.prototype._removeRow = function(iRowIndex) {
	if ((iRowIndex < 0) || (iRowIndex > this._rows - 1)) {
		this.error = 'Unable to remove row, row index out of range.';
		return 1;
	}

	/* Remove from selected */
	for (var i = this.selectedRows.length - 1; i >= 0; i--) {
		if (this.selectedRows[i] == iRowIndex) {
			this.selectedRows.splice(i, 1);
	}	}

	this._eBodyTable.tBodies[0].removeChild(this._eBodyTable.tBodies[0].rows[iRowIndex]);
	return 0;
};

/*
 * iRowIndex getSelectedRow()
 * Returns the index of the selected row or -1 if no row is selected.
 */
WebFXColumnList.prototype.getSelectedRow = function() {
	return (this.selectedRows.length)?this.selectedRows[this.selectedRows.length-1]:-1;
};

/*
 * iRowIndex[] getSelectedRange()
 * Returns an array with the row index of all selecteds row or null if no row is selected.
 */
WebFXColumnList.prototype.getSelectedRange = function() {
	return (this.selectedRows.length)?this.selectedRows:-1;
};

/*
 * iRows getRowCount()
 * Returns the nummer of rows.
 */
WebFXColumnList.prototype.getRowCount = function() {
	return this._rows;
};

/*
 * iRows getColumnCount()
 * Returns the nummer of columns.
 */
WebFXColumnList.prototype.getColumnCount = function() {
	return this._cols;
};

/*
 * sValue = getCellValue(iRowIndex, iColumnIndex)
 * Returns the content of the specified cell.
 */
WebFXColumnList.prototype.getCellValue = function(iRowIndex, iColIndex) {
	if ((iRowIndex < 0) || (iRowIndex > this._rows - 1)) {
		this.error = 'Unable to get cell value , row index out of range.';
		return null;
	}
	if ((iColIndex < 0) || (iColIndex > this._cols - 1)) {
		this.error = 'Unable to get cell value , row index out of range.';
		return null;
	}

	return this._eBodyTable.tBodies[0].rows[iRowIndex].cells[iColIndex].innerHTML;
};


/*
 * iError = setCellValue(iRowIndex, iColumnIndex, sValue)
 * Sets the content of the specified cell.
 */
WebFXColumnList.prototype.setCellValue = function(iRowIndex, iColIndex, sValue) {
	if ((iRowIndex < 0) || (iRowIndex > this._rows - 1)) {
		this.error = 'Unable to get cell value , row index out of range.';
		return 1;
	}
	if ((iColIndex < 0) || (iColIndex > this._cols - 1)) {
		this.error = 'Unable to get cell value , row index out of range.';
		return 2;
	}

	this._eBodyTable.tBodies[0].rows[iRowIndex].cells[iColIndex].innerHTML = sValue;
	this.calcSize();
	return 0;
};

/*
 * void setSortTypes(sSortType[]) {
 * Sets the column data types, used for sorting.
 * Valid options: Number, Date, String, CaseInsensitiveString
 */
WebFXColumnList.prototype.setSortTypes = function(aSortTypes) {
		this._stl.setSortTypes(aSortTypes);
}

/*
 * void sort(iColumnIndex, [bDescending])
 * Sorts the grid by the specified column (zero based index) and, optionally, in the specified direction.
 */
WebFXColumnList.prototype.sort = function(iCol, bDesc) {
	//i change it to return,Weiping Ju
	if(!this.allowSort){
		return;
	}	
	/* Hide arrow from header for column currently sorted by */
	if (this.sortCol != -1) {
		var eImg = this._eHeadTable.tBodies[0].rows[0].cells[this.sortCol].getElementsByTagName('img')[0];
		eImg.style.display = 'none';
	}

	/* Determine sort direction */
	if (bDesc == null) {
		bDesc = false;
		if ((!this.sortDescending) && (iCol == this.sortCol)) { bDesc = true; }
	}

	/* Indicate sorting using arrow in header */
	var eImg = this._eHeadTable.tBodies[0].rows[0].cells[iCol].getElementsByTagName('img')[0];
	eImg.src = (bDesc)?IMG_DESC:IMG_ASC;
	eImg.style.display = 'inline';

	/* Perform sort operation */
	this._stl.sort(iCol, bDesc);
	this.sortCol = iCol;
	this.sortDescending = bDesc;

	/* Update row coloring */
	if (this.colorEvenRows) { this._colorEvenRows(); }

	/* Update selection */
	var nodes = this._eBodyTable.tBodies[0].rows;
	var len = nodes.length;
	var a = new Array();
	for (var i = 0; i < len; i++) {
		if (nodes[i].className == 'selected') { a.push(i); }
	}
	this.selectRange(a);
	
	/*
	 * As the header cell may have grown to accommodate the sorting indicator
	 * we set the width of the body columns
	 */
	this._sizeBodyAccordingToHeader();

	/* Call onsort if defined */
	if (this.onsort) { this.onsort(this.sortCol, this.sortDescending); }

};

/*
 * void _handleRowKey(iKeyCode)
 * Key handler for events on row level.
 */
WebFXColumnList.prototype._handleRowKey = function(iKeyCode, bCtrl, bShift) {
	var iActiveRow = -1;
	if (this.selectedRows.length != 0) { iActiveRow = this.selectedRows[this.selectedRows.length-1]; }
	if ((!bCtrl) && (!bShift)) {
		if (iKeyCode == 38) {                                                      // Up
			if (iActiveRow > 0) { this.selectRow(iActiveRow - 1); }
		}
		else if (iKeyCode == 40) {                                                 // Down
			if (iActiveRow < this._rows - 1) { this.selectRow(iActiveRow + 1); }
		}
		if (iKeyCode == 33) {                                                      // Page Up
			if (iActiveRow > 10) { this.selectRow(iActiveRow - 10); }
			else { this.selectRow(0); }
		}
		else if (iKeyCode == 34) {                                                 // Page Down
			if (iActiveRow < this._rows - 10) { this.selectRow(iActiveRow + 10); }
			else { this.selectRow(this._rows - 1); }
		}
		else if (iKeyCode == 36) { this.selectRow(0); }                            // Home
		else if (iKeyCode == 35) { this.selectRow(this._rows - 1); }               // End
		else { return true; }
		return false;
	}
};

/*
 * Event Handlers
 */
WebFXColumnList.prototype._mouseMove = function(e) {
	var el = (e)?e.target:window.event.srcElement;
	var x = (e)?e.pageX:window.event.x + this._eBody.scrollLeft;

	if ((this._activeHeaders) && (this._activeHeaders[0])) {
		/*
		 * User is resizing a column, determine and set new size
		 * based on the original size and the difference between
		 * the current mouse position and the one that was recorded
		 * once the resize operation was started.
		 */
		var w = this._activeHeaders[2] + x - this._activeHeaders[3];
		var tw = ((w - this._activeHeaders[2]) + this._activeHeaders[4]) + 1;
		this._eHeadTable.style.width = tw + 'px';
		if (w > 5) {
			this._activeHeaders[1].style.width = w + 'px';
			if (this.bodyColResize) {
				this._eBodyTable.style.width = tw + 'px';
				this._eBodyTable.getElementsByTagName('colgroup')[0].getElementsByTagName('col')[this._activeHeaders[1].cellIndex].style.width = w + 'px';
	}	}	}
	else if ((el.tagName == 'TD') && (el.parentNode.parentNode.parentNode.parentNode.className == 'webfx-columnlist-head')) {
		/*
		 * The cursor is on top of a header cell, check if it's near the edge,
		 * and in that case set the mouse cursor to 'e-resize'.
		 */
		this._checkHeaderResize(el, x);
	}
	else if (this._activeHeaders) {
		this._activeHeaders = null;
		this._eCont.style.cursor = 'default';
	}
};


WebFXColumnList.prototype._mouseDown = function(e) {
	var el = (e)?e.target:window.event.srcElement;
	var x = (e)?e.pageX:window.event.x + this._eBody.scrollLeft;

	this._checkHeaderResize(el, x);
	if ((this._activeHeaders) && (el.tagName == 'TD') && (el.parentNode.parentNode.parentNode.parentNode.className == 'webfx-columnlist-head')) {
		/*
		 * Cursor is near the edge of a header cell and the
		 * left mouse button is down, start resize operation.
		 */
		this._activeHeaders[0] = true;
		if (this.bodyColResize) { this._sizeBodyAccordingToHeader(); }
	}
};

WebFXColumnList.prototype._mouseUp = function(e) {
	var el = (e)?e.target:window.event.srcElement;
	var x = (e)?e.pageX:window.event.x + this._eBody.scrollLeft;

	if (this._activeHeaders) {
		if (this._activeHeaders[0]) {
			this._sizeBodyAccordingToHeader();
			this._checkHeaderResize(el, x);
		}
		this._activeHeaders = null;
	}
	else if ((el.tagName == 'TD') && (el.parentNode.parentNode.parentNode.parentNode.className == 'webfx-columnlist-head')) {
		this.sort(el.cellIndex);
	}
};

WebFXColumnList.prototype._click = function(e) {
	var el = (e)?e.target:window.event.srcElement;
	if (el.tagName == 'IMG') { el = el.parentNode; }
	if (el.tagName == 'DIV') { el = el.parentNode; }
	if ((el.tagName == 'TD') && (el.parentNode.parentNode.parentNode.parentNode.className == 'webfx-columnlist-body')) {
		if (((e)?e.shiftKey:window.event.shiftKey) && (this.selectedRows.length) && (this.multiple)) {
			this.selectRange(this.selectedRows[this.selectedRows.length-1], el.parentNode.rowIndex);
		}
		else { this.selectRow(el.parentNode.rowIndex, (e)?e.ctrlKey:window.event.ctrlKey); }
	}
	//this._eCont.focus();
};

WebFXColumnList.prototype._keyDown = function(e) {
	var el = (e)?e.target:window.event.srcElement;
	var key = (e)?e.keyCode:window.event.keyCode;
	if (el.tagName == 'DIV') { el = el.parentNode; }
	if (el.className.indexOf('gridBox') == 0) {
		if (handleEditKey(el, key)) { return true; }
		if (window.event) { window.event.cancelBubble = true; }
		else { e.preventDefault(); e.stopPropagation() }
		return false;
	}
	else if ((el.tagName == 'TD') && (el.parentNode.parentNode.parentNode.parentNode.className == 'gridIdCol')) {
		if (handleRowKey(key)) { return; }
		if (window.event) { window.event.cancelBubble = true; }
		else { e.preventDefault(); e.stopPropagation() }
		return false;
}	};

/*
 * Event handler helpers
 */

WebFXColumnList.prototype._checkHeaderResize = function(el, x) {
	/*
	 * Checks if the mouse cursor is near the edge of a header
	 * cell, in that case the cursor is set to 'e-resize' and
	 * the _activeHeaders collection is created containing a
	 * references to the active header cell, the current mouse
	 * position and the cells original width.
	 */
	if ((el.tagName != 'TD') || (el.parentNode.parentNode.parentNode.parentNode.className != 'webfx-columnlist-head')) { return; }
	if (el.tagName == 'IMG') { el = el.parentNode; }
	var prev = el.previousSibling;
	var next = el.nextSibling;
	var left = getLeftPos(el);
	var right = left + el.offsetWidth;
	var l = (x - 5) - left;
	var r = right - x;
	if ((l < 5) && (prev)) {
		this._eCont.style.cursor = 'e-resize';
		this._activeHeaders = [false, prev, prev.offsetWidth - 5, x, this._eHeadTable.offsetWidth];
	}
	else if (r < 5) {
		this._eCont.style.cursor = 'e-resize';
		this._activeHeaders = [false, el, el.offsetWidth - 5, x, this._eHeadTable.offsetWidth];
	}
	else if (this._activeHeaders) {
		this._activeHeaders = null;
		this._eCont.style.cursor = 'default';
		el.style.backgroundColor = '';
}	}

WebFXColumnList.prototype._sizeBodyAccordingToHeader = function() {
	/*
	 * The overflow porperty on table columns is only effective if the
	 * table type is set to fixed thus this function changes the table
	 * type to fixed and sets the width of each body column to size of
	 * the corresponding header column.
	 */
	this._eBodyTable.style.width = this._eHeadTable.offsetWidth + 'px';
	this._eBodyTable.style.tableLayout = 'fixed';
	var length = this._eBodyCols.length;
	var aCols = this._eBodyTable.getElementsByTagName('colgroup')[0].getElementsByTagName('col');
	for (var i = 0; i < length; i++) {
		aCols[i].style.width = (this._eHeadCols[i].offsetWidth - ((document.all)?2:0)) + 'px';
}	}

/*
 * Helper functions
 */
function getInnerText(el) {
	if (document.all) { return el.innerText; }
	var str = '';
	var cs = el.childNodes;
	var l = cs.length;
	for (var i = 0; i < l; i++) {
		switch (cs[i].nodeType) {
			case 1: //ELEMENT_NODE
				str += getInnerText(cs[i]);
				break;
			case 3:	//TEXT_NODE
				str += cs[i].nodeValue;
				break;
	}	}
	return str;
}

function parseDate(s) {
	return Date.parse(s.replace(/\-/g, '/'));
}

function getLeftPos(_el) {
	var x = 0;
	for (var el = _el; el; el = el.offsetParent) {
		x += el.offsetLeft;
	}
	return x;
}

function compareByColumn(iCol, bDescending, iType) {
	var fTypeCast;
	switch (iType) {
		case TYPE_NUMERIC: fTypeCast = Number;    break;
		case TYPE_STRING:  fTypeCast = String;    break;
		case TYPE_DATE:    fTypeCast = parseDate; break;
		default:           fTypeCast = String
	}

	return function (n1, n2) {
		if (fTypeCast(getInnerText(n1.cells[iCol])) < fTypeCast(getInnerText(n2.cells[iCol]))) {
			return (bDescending)?-1:1;
		}
		if (fTypeCast(getInnerText(n1.cells[iCol])) > fTypeCast(getInnerText(n2.cells[iCol]))) {
			return (bDescending)?1:-1;
		}
		return 0;
   };
}

function compareNumericAsc(n1, n2) {
	if (Number(n1) < Number(n2)) { return -1; }
	if (Number(n1) > Number(n2)) { return 1; }
	return 0;
}
