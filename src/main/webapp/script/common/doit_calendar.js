var UserAgent = navigator.userAgent;
var AppVersion = (((navigator.appVersion.split('; '))[1].split(' '))[1]);

// default NSelect style
var SL_ActiveIDX = null;
var SL_Focused = null;
var SL_FocusedIDX = null;
var SL_Table = " cellspacing=0 cellpadding=0 border=0";
var SL_Text = "text-indent:2px;padding-top:3px;";
var SL_IPrefix = "http://sstatic.naver.com/search/images5/";
var SL_AImage = "arrow.gif";
var SL_BImage = "blank.gif";
var SL_SBLen = 10;
var SL_SBWidth = 18;
var SL_ScrollBar = ""
    +"scrollbar-face-color:#ffffff;"
    +"scrollbar-shadow-color:999999;"
    +"scrollbar-highlight-color:#E8E8E8;"
    +"scrollbar-3dlight-color:999999;"
    +"scrollbar-darkshadow-color:#EEEEEE;"
    +"scrollbar-track-color:#999999;"
    +"scrollbar-arrow-color:#E8E8E8;";

var SL_BGColor = "#FEFFCB";
var SL_BGColor_M = "#225688";
var SL_Border = "1px solid #AFB086";
var SL_FontSize = "10pt";
var SL_FontColor = "#000000";
var SL_Height = "18px";

var divClick = false;
var imagePath;
var calStr = "";

SList = new Array();
document.write( "<div id=NSDiv style='position:absolute;top:-100px;z-index:3;'></div>" );

Calendar.prototype = {
    initialize : function() {    	
    },
    initCalendar : function()
    {
		if( calStr.length == 0){
			var calBuf = new Array();
			
			calBuf[calBuf.length] = "<div id=tims_calendar style='display:none; position:absolute; z-index: 1000; width:225px; height: 230px;'>\n";
		    calBuf[calBuf.length] = "    <table id=table_calendar border=0 cellpadding=0 cellspacing=0 width=220>\n";
		    calBuf[calBuf.length] = "    <tr>\n";
		    calBuf[calBuf.length] = "        <td>\n";
		    calBuf[calBuf.length] = "        <table border=0 cellpadding=0 cellspacing=0 width=100%>\n";
		    calBuf[calBuf.length] = "        <tr>\n";
		    calBuf[calBuf.length] = "            <td rowspan=2 width=1 nowrap bgcolor='#C3CACD'></td>\n";
		    calBuf[calBuf.length] = "            <td width=100% bgcolor=#C3CACD></td>\n";
		    calBuf[calBuf.length] = "            <td rowspan=2 width=1 nowrap bgcolor=#A6ACAE></td>\n";
		    calBuf[calBuf.length] = "            <td rowspan=2 width=2 height=2 nowrap bgcolor=#ffffff></td>\n";
		    calBuf[calBuf.length] = "        </tr>\n";
		    calBuf[calBuf.length] = "        <tr>\n";
		    calBuf[calBuf.length] = "            <td height=1 bgcolor=#F1F6F8></td>\n";
		    calBuf[calBuf.length] = "        </tr>\n";
		    calBuf[calBuf.length] = "        <tr>\n";
		    calBuf[calBuf.length] = "            <td width=1 nowrap bgcolor=#C3CACD></td>\n";
		    calBuf[calBuf.length] = "            <td width=100% bgcolor=#F1F6F8 height=32 align=center>\n";
		    calBuf[calBuf.length] = "            <table border=0 cellpadding=0 cellspacing=0 width=95%>\n";
		    calBuf[calBuf.length] = "            <tr><td colspan=2 height=5 nowrap></td></tr>\n";
		    calBuf[calBuf.length] = "            <tr>\n";
		    calBuf[calBuf.length] = "              	<td width=80 align=center>\n";
		    calBuf[calBuf.length] = "                	<select id=yearSelect name=yearSelect style='font-size:9pt;' onChange='setCalendar()'>\n";
		
		    for (i = 1900; i <= 2030; i++)
		        calBuf[calBuf.length] = "               	<option value='" + i + "'>" + i + " 년</option>\n";
	
		    calBuf[calBuf.length] = "                	</select>\n";
		    calBuf[calBuf.length] = "               </td>\n";
		    calBuf[calBuf.length] = "               <td align=center>\n";
		    calBuf[calBuf.length] = "               <input type=hidden id=monthSelect name=monthSelect>\n";
		    calBuf[calBuf.length] = "				<table border=0 cellpadding=0 cellspacing=0 width=100%>\n";	
		    calBuf[calBuf.length] = "            	<tr>\n";
		    for (i = 1; i <= 6; i++)
		    calBuf[calBuf.length] = "               	<td align=center width=16% id=monthCell" + i + " onclick='setCalendarByMonth( this)' style='cursor:pointer; font-weight: bold;'>" + i + "</td>\n";
		    calBuf[calBuf.length] = "               	<td align=center width=20 rowspan=2>월</td>\n";
		    calBuf[calBuf.length] = "            	</tr>\n";
		    calBuf[calBuf.length] = "            	<tr>\n";
		    for (i = 7; i <= 12; i++)
		    calBuf[calBuf.length] = "               	<td align=center width=16% id=monthCell" + i + " onclick='setCalendarByMonth( this)' style='cursor:pointer; font-weight: bold;'>" + i + "</td>\n";
		    calBuf[calBuf.length] = "            	</tr>\n";
		    calBuf[calBuf.length] = "				</table>\n";	
		    calBuf[calBuf.length] = "               </td>\n";
		    calBuf[calBuf.length] = "           </tr>\n";
		    calBuf[calBuf.length] = "           <tr><td colspan=2 height=5 nowrap></td></tr>\n";
		    calBuf[calBuf.length] = "			</table>\n";	
		    
		    calStr += calBuf.join('');
			calBuf = new Array();
		    
		    calBuf[calBuf.length] = "            </td>\n";
		    calBuf[calBuf.length] = "            <td width=1 nowrap bgcolor=#A6ACAE></td>\n";
		    calBuf[calBuf.length] = "            <td width=2 nowrap bgcolor=#E0E4E6></td>\n";
		    calBuf[calBuf.length] = "        </tr>\n";
		    calBuf[calBuf.length] = "        <tr>\n";
		    calBuf[calBuf.length] = "            <td width=1 nowrap bgcolor=#C3CACD></td>\n";
		    calBuf[calBuf.length] = "            <td width=100% height=1 bgcolor=#ffffff style='border-top: 1px dotted gray;'></td>\n";
		    calBuf[calBuf.length] = "            <td width=1 nowrap bgcolor=#A6ACAE></td>\n";
		    calBuf[calBuf.length] = "            <td width=2 nowrap bgcolor=#E0E4E6></td>\n";
		    calBuf[calBuf.length] = "        </tr>\n";
		    calBuf[calBuf.length] = "        <tr>\n";
		    calBuf[calBuf.length] = "            <td width=1 nowrap bgcolor=#C3CACD></td>\n";
		    calBuf[calBuf.length] = "            <td width=100% bgcolor=#ffffff align=center>\n";
	//	    calBuf[calBuf.length] = "            <!----달력 넣는곳------>\n";
		    calBuf[calBuf.length] = "            <table border=0 cellpadding=0 cellspacing=0 width=100%>\n";	
		    calBuf[calBuf.length] = "            <tr><td colspan=7 height=7 nowrap></td></tr>\n";
		    calBuf[calBuf.length] = "            <tr>\n";
		    calBuf[calBuf.length] = "                <td width=15% align=center><font id=ln6 color=#DD7403>일</font></td>\n";
		    calBuf[calBuf.length] = "                <td width=14% align=center><font id=ln6>월</font></td>\n";
		    calBuf[calBuf.length] = "                <td width=14% align=center><font id=ln6>화</font></td>\n";
		    calBuf[calBuf.length] = "                <td width=14% align=center><font id=ln6>수</font></td>\n";
		    calBuf[calBuf.length] = "                <td width=14% align=center><font id=ln6>목</font></td>\n";
		    calBuf[calBuf.length] = "                <td width=14% align=center><font id=ln6>금</font></td>\n";
		    calBuf[calBuf.length] = "                <td width=15% align=center><font id=ln6 color=#3C8096>토</font></td>\n";
		    calBuf[calBuf.length] = "            </tr>\n";
		    calBuf[calBuf.length] = "            <tr><td colspan=7 height=7 nowrap></td></tr>\n";
	
		    calStr += calBuf.join('');
			calBuf = new Array();
		
		    for (i = 0; i < 6; i++)
		    {
		        calBuf[calBuf.length] = "            <tr>\n";	
		        for (j = 0; j < 7; j++)
		            calBuf[calBuf.length] = "            	<td align=center id='dayCell" + ( i * 7 + j )+ "'></td>\n";
		        calBuf[calBuf.length] = "            </tr>\n";
		
		        calBuf[calBuf.length] = "           <tr nowrap>\n";	
		        for (j = 0; j < 7; j++)
		            calBuf[calBuf.length] = "            	<td align=center valign=top id='memoCell" + ( i * 7 + j ) + "'></td>\n";
		        calBuf[calBuf.length] = "            </tr>\n";
		    }
		    
		    if (typeof(rege_0_1) != "undefined" && 1900 <= rege_0_1 && rege_0_1 <= 2030)
		    {
		        ayear = rege_0_1;
		        amonth = 1;
		    }
		
		    if (typeof(rege_0_2) != "undefined" && 1 <= rege_0_2 && rege_0_2 <= 12)
		        amonth = rege_0_2;
	
		    calStr += calBuf.join('');
			calBuf = new Array();
		
		    calBuf[calBuf.length] = "            <tr><td colspan=7 height=7 nowrap></td></tr>\n";
		    calBuf[calBuf.length] = "            </table>\n";
	//	    calBuf[calBuf.length] = "            <!----달력 넣는곳------>";
		    calBuf[calBuf.length] = "            </td>\n";
		    calBuf[calBuf.length] = "            <td width=1 nowrap bgcolor=#A6ACAE></td>\n";
		    calBuf[calBuf.length] = "            <td width=2 nowrap bgcolor=#E0E4E6></td>\n";
		    calBuf[calBuf.length] = "        </tr>\n";
		    calBuf[calBuf.length] = "        </table>\n";
		    calBuf[calBuf.length] = "        </td>\n";
		    calBuf[calBuf.length] = "    </tr>\n";
		    calBuf[calBuf.length] = "    <tr>\n";
		    calBuf[calBuf.length] = "        <td>\n";
		    calBuf[calBuf.length] = "        <table border=0 cellpadding=0 cellspacing=0 width=100%>\n";
		    calBuf[calBuf.length] = "        <tr>\n";
		    calBuf[calBuf.length] = "            <td colspan=2 width=100% height=1 bgcolor=#A6ACAE></td>\n";
		    calBuf[calBuf.length] = "            <td width=2 nowrap bgcolor=#E0E4E6></td>\n";
		    calBuf[calBuf.length] = "        </tr>\n";
		    calBuf[calBuf.length] = "        <tr>\n";
		    calBuf[calBuf.length] = "            <td width=2 height=2 nowrap></td>\n";
		    calBuf[calBuf.length] = "            <td width=100% bgcolor=#E0E4E6></td>\n";
		    calBuf[calBuf.length] = "            <td width=2 nowrap bgcolor=#E0E4E6></td>\n";
		    calBuf[calBuf.length] = "        </tr>\n";
		    calBuf[calBuf.length] = "        </table>\n";
		    calBuf[calBuf.length] = "        </td>\n";
		    calBuf[calBuf.length] = "    </tr>\n";
		    calBuf[calBuf.length] = "    </table>\n";
		    calBuf[calBuf.length] = "</div>\n";
		    calBuf[calBuf.length] = "<iframe id=cal_empty name=cal_empty style='display:none;position:absolute;width:225px;height:230px;border:0;filter:alpha(opacity=0);z-index: 999;'></iframe>\n";
		    
		    calStr += calBuf.join('');	    
		}
		$('body').append(calStr);
    },
	service : function( imgObject, inputObject, dateType, path) {
		if (path != null && path != undefined) {
			imagePath = path;
		}
		
		var calDiv = $('##tims_calendar');
		
		if ( calDiv == null || calDiv == undefined) {
			this.initCalendar();
			Event.observe( $('##tims_calendar'), 'click', function(event){
				divClick = true;
			}, false);
		}
		this.showCalendar(imgObject, inputObject, dateType);
		var _retDate = this.inputWindow.$(inputObject).value;
		if (_retDate != "" && _retDate != null)	{
			_retDate = _retDate.replace(/-/g, '').replace(/:/g, '').replace(/ /g, '')
			var year = _retDate.substr(0,4);
			var month = _retDate.substr(4,2);
			if ((year >= 1900 && year <= 2300) && (month >= 1 && month <= 12)) {
				if (month < 10) {
					month = month.replace("0","");
				}
				this.setCalendar(year, month);
			} else {
				this.setCalendar(ayear, amonth);
			}
		} else {
			this.setCalendar(ayear, amonth);
		}
	},
	showCalendar : function(imgObject, inputObject, dateType) {
		var imgPos = Position.cumulativeOffset(imgObject);
		var imgDim = Element.getDimensions(imgObject);
		
		var calDiv = $('#tims_calendar');
	
		if ( this.inputWindow != self) {
			var winPos = $( this.inputWindow.name).cumulativeOffset();
			calDiv.style.left	= (winPos[0] + imgPos[0] + imgDim.width + 10) + "px";
			calDiv.style.top	= (winPos[1] + imgPos[1]) + "px";
			
			this.inputObject = this.inputWindow.$(inputObject);
			this.dateType = dateType;
		} else {
			calDiv.style.left	= (imgPos[0] + imgDim.width + 10) + "px";
			calDiv.style.top	= imgPos[1] + "px";	
			
			this.inputObject = $(inputObject);
			this.dateType = dateType;
		}
	
		Element.show( calDiv);
		
		var calEmpty = $('#cal_empty');
		
		calEmpty.style.top = calDiv.style.top;
		calEmpty.style.left = calDiv.style.left;
		Element.show( calEmpty);
	},
	clearCalendar : function() {
		if ( $('#tims_calendar') != undefined) {
			Element.hide( $('#tims_calendar'));
			Element.hide( $('#cal_empty'));
			this.inputObject = null;
			this.inputWindow = null;
		}
	},
	removeCalendar : function() {
		this.clearCalendar();
		Element.remove( $('#tims_calendar'));
		Element.remove( $('#cal_empty'));
	},
	setCalendar : function(year, month)
	{
	    var i;
	    var oYearSelect = $('#yearSelect');
	    var oMonthSelect = $('#monthSelect');
	
	    if (!year)
	    {
	        year = oYearSelect.value;
	        month = oMonthSelect.value;
	    }
	    else
	    {
	        for (i = 0; i < oYearSelect.length; i++) {
	            if (oYearSelect[i].value == year)
	            {
	                oYearSelect.selectedIndex = i;
	                break;
	            }
	        }
					
			for( i=1; i<=12; i++){
				cell = $('#monthCell' + i);
				if( i == month){
					cell.style.backgroundColor = "aqua";
					oMonthSelect.value = month;
				}
				else {
					cell.style.backgroundColor = "";
				}
			}
	        
	    }
	
	    var monthDay = Array(31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	
	    /* set monthDay of Feb */
	    if (year % 400 == 0)
	        monthDay[1] = 29;
	    else if (year % 100 == 0)
	        monthDay[1] = 28;
	    else if (year % 4 == 0)
	        monthDay[1] = 29;
	    else
	        monthDay[1] = 28;
	
	
	    /* set the day before 설날 */
	    if (lunarMonthTable[year - 1 - 1899][11] == 1)
	        memorialDays[1].day = 29;
	    else if (lunarMonthTable[year - 1 - 1899][11] == 2)
	        memorialDays[1].day = 30;
	
	
	    var date = new Date(year, month - 1, 1);
	    var startWeekday = date.getDay();
	
	    /* clean all day cell */
	    for (i = 0; i < 42; i++)
	    {
	        $('#dayCell' + i).innerHTML = "";
	        $('#memoCell' + i).innerHTML = "";
	    }
	
	    /* fill day cell */
	    for (i = 0; i < monthDay[month - 1]; i ++)
	    {
	        var index = startWeekday + i;
	        var dayHTML;
	        var memoHTML;
	
	        var solarDate = new myDate(year, month, i + 1);
	        var lunarDate = lunarCalc(year, month, i + 1, 1);
	
	        /* memorial day */
	        var memorial = memorialDayCheck(solarDate, lunarDate);
	
	        /* 쉬지않는 기념일 */
	        var memorialDay = false;
	        if (memorial && memorial.holiday == false)
	            memorialDay = true;
	
	        /* day print */
	        dayHTML = "<span onClick=\"calendar.setStartDate(" +
	                    year + ", " + month + ", " + ( i + 1 ) + ")\" STYLE='CURSOR:POINTER'>" +
	                    "<font id=ln2 color='COLOR' title='TITLE'>" +
	                    "HIGHLIGHT_START" + ( i + 1 ) + "HIGHLIGHT_END" +
	                    "</font></span>";
	
	        /* decoration */
	        if ((memorial && memorial.holiday) || index % 7 == 0)
	            dayHTML = dayHTML.replace("COLOR", "#DD7403");
	        else if (index % 7 == 6)
	            dayHTML = dayHTML.replace("COLOR", "#3C8096");
	
	        if (memorial)
	            dayHTML = dayHTML.replace("TITLE", memorial.name);
	
	        if (todayDate.getFullYear() == year &&
	            todayDate.getMonth() + 1 == month &&
	            todayDate.getDate() == i + 1)
	        {
	            dayHTML = dayHTML.replace("HIGHLIGHT_START", "<b><font color=blue>");
	            dayHTML = dayHTML.replace("HIGHLIGHT_END", "</font></b>");
	        }
	
	        dayHTML = dayHTML.replace("TITLE", "");
	        dayHTML = dayHTML.replace("COLOR", "");
	        dayHTML = dayHTML.replace("HIGHLIGHT_START", "");
	        dayHTML = dayHTML.replace("HIGHLIGHT_END", "");
	
	
	        $('#dayCell' + index).innerHTML = dayHTML;
	
	        /* lunar calnedar print */
	        if (lunarDate.day == 1 || lunarDate.day == 15)
	        {
	            if (imagePath != null && imagePath != undefined) {
					memoHTML = "<img src="+imagePath+"/common/calendar/ico_IL_date" + (lunarDate.month < 10 ? "0" + lunarDate.month : lunarDate.month) + (lunarDate.day < 10 ? "0" + lunarDate.day: lunarDate.day) + ".gif border=0>";
				} else {
					memoHTML = "<img src=/images/common/calendar/ico_IL_date" + (lunarDate.month < 10 ? "0" + lunarDate.month : lunarDate.month) + (lunarDate.day < 10 ? "0" + lunarDate.day: lunarDate.day) + ".gif border=0>";
				}
			}
	        else
	            memoHTML = "<table border=0 cellpadding=0 cellspacing=0><tr height=10><td></td><tr></table>";
	
	        $('#memoCell' + index).innerHTML = memoHTML;
	    }
	},
	setStartDate : function(year, month, day)
	{
		var _ret = year + "-";
		if (month < 10)
			_ret += "0"+month;
		else
		 	_ret += month;
		_ret += "-";
		if (day < 10)
			_ret += "0"+day;
		else
		 	_ret += day;
		if ( this.dateType == 'dtime') {
			_ret += " 00:00";
		}
		
		this.inputObject.value = _ret;
		this.inputObject.focus();

		this.clearCalendar();	
	},
	isActivated : function(){
		calDiv = $('#tims_calendar');
		return Object.isElement( calDiv) && Element.visible( calDiv);		
	}
}

var calendar = new Calendar();

// set SL Style
function setEnv( pSrc, pBG, pBM, pBD, pAI )
{
	var element = $('#NSDiv');
    var oEnv = new Object();
    var oSrc = createObject( pSrc );

    if( oSrc.style.width ) {
    oEnv.Width = oSrc.style.width;
    } else {
    element.innerHTML = "<table style='top:2px;'><tr><td height=1>"+pSrc+"</td></tr></table>";
    oEnv.Width = element.scrollWidth;
    }
    if( oSrc.style.height ) oEnv.Height = oSrc.style.height; else oEnv.Height = SL_Height;
    if( oSrc.style.fontSize ) oEnv.FontSize = oSrc.style.fontSize; else oEnv.FontSize = SL_FontSize;
    if( oSrc.style.color ) oEnv.FontColor = oSrc.style.color; else oEnv.FontColor = SL_FontColor;

    if( pBG ) oEnv.BGColor = pBG;    else oEnv.BGColor = SL_BGColor;
    if( pBM ) oEnv.BGColor_M = pBM;    else oEnv.BGColor_M = SL_BGColor_M;
    if( pBD ) oEnv.Border = pBD;    else oEnv.Border = SL_Border;
    if( pAI ) oEnv.AImage = pAI;    else oEnv.AImage = SL_AImage;

    return oEnv;
}

// parameter NSelect
function NSelect( HTMLSrc, KIN, BG, BM, BD, AI )
{
    if ( UserAgent.indexOf( "MSIE" ) < 0 || AppVersion < 5 ) {
    document.write( HTMLSrc );
    return;
    } else {
    var SE = setEnv( HTMLSrc, BG, BM, BD, AI );
    var SListObj = new setNSelect( HTMLSrc, KIN, SE );
    SListObj.append();

    return SListObj;
    }
}

function appendSList()
{
    document.write("<div id=TempDiv></div>\n");
    
	var element = $('#TempDiv');
    element.appendChild( this.Table );
    element.removeNode();

    return;
}

function MouseScrollHandler() {
    var f_titleObj = SList[SL_FocusedIDX].Title;
    var f_itemObj = SList[SL_FocusedIDX].Items;
    var idx_length = f_itemObj.options.length;
    var idx_selected = f_itemObj.options.selectedIndex ;

    CancelEventHandler( window.event );

    if( window.event.wheelDelta > 0 ) {
    idx_selected = Math.max( 0, --idx_selected );
    } else {
    idx_selected = Math.min( idx_length - 1, ++idx_selected );
    }

    if( f_itemObj.options.selectedIndex != idx_selected ) {
    f_itemObj.options.selectedIndex = idx_selected;
    SList[SL_FocusedIDX].ChangeTitle();
    if( f_itemObj.onchange ) f_itemObj.onchange();
    }

    return;
}

function ActiveIDXHandler() {
    if( SL_ActiveIDX == null ) {
    for( i = 0; i < SList.length; i++ ) {
        SList[i].List.style.display = "none";
        if( i == SL_Focused ) TitleHighlightHandler( i, 1 );
        else TitleHighlightHandler( i, 0 );
    }

    if( SL_Focused == null )
        document.detachEvent( 'onclick', ActiveIDXHandler );
    }
    if( SL_Focused == null ) document.detachEvent( 'onmousewheel', MouseScrollHandler );
    else document.attachEvent( 'onmousewheel', MouseScrollHandler );

    SL_ActiveIDX = null;
    SL_Focused = null;

    return;
}

function TitleClickHandler()
{
    SL_ActiveIDX = this.entry;

    for( i = 0; i < SList.length; i++ ) {
    if( i == SL_ActiveIDX ) {
        if( SList[i].List.style.display == "block" ) {
        SList[i].List.style.display = "none";
        TitleHighlightHandler( i, 1 );
        SL_Focused = i;
        SL_FocusedIDX = i;
        } else SList[i].List.style.display = "block";
    } else {
        SList[i].List.style.display = "none";
        TitleHighlightHandler( i, 0 );
    }
    }

    document.detachEvent( 'onclick', ActiveIDXHandler );
    document.attachEvent( 'onclick', ActiveIDXHandler );

    return;
}

function TitleMouseOverHandler()
{
    this.Title.children(0).cells(2).children(0).style.filter = "";
    if( !this.kin ) this.Title.style.border = "1 solid #999999";

    return;
}

function TitleMouseOutHandler()
{
    this.Title.children(0).cells(2).children(0).style.filter = "alpha( opacity=80 );";
    if( !this.kin ) this.Title.style.border = "1 solid #C3CACD";

    return;
}

function TitleHighlightHandler( entry, t )
{
    if( t ) {
    if( this.kin )
        SList[entry].Title.children(0).cells(0).style.background = SList[entry].env.BGColor;
    else
        SList[entry].Title.children(0).cells(0).style.background = SList[entry].env.BGColor_M;
    } else {
    SList[entry].Title.children(0).cells(0).style.background = SList[entry].env.BGColor;
    }

    return;
}

function ListMouseDownHandler( f )
{
    var tObj = this.Title.children(0);
    var length = this.Items.length;

    for( i = 0; i < length; i++ ) {
    this.Items.options[i].selected = false;
    if ( i == f.idx ) {
        this.Items.options[i].selected = true;
        this.ChangeTitle();
    }
    }
    if( this.Items.onchange ) this.Items.onchange();
    if ( this.kin && ( length - 1 ) == f.idx )
    location.href = this.Items.options[f.idx].value;

    this.List.style.display = "none";

    SL_Focused = this.entry;
    SL_FocusedIDX = this.entry;

    return;
}

function ListMouseOverHandler( f )
{
    if( this.kin ) f.style.color = "#FFFFFF";
    f.style.background = this.env.BGColor_M;
    return;
}

function ListMouseOutHandler( f )
{
    f.style.color = this.env.FontColor;
    f.style.background = this.env.BGColor;
    return;
}

function CancelEventHandler( e )
{
    e.cancelBubble = true;
    e.returnValue = false;

    return;
}

function ModifyDivHandler() {
    var width = parseInt( this.Title.style.width );

    this.Items.style.width = null;
	var element = $('#NSDiv');
    
    element.innerHTML = ""
    +"<table style='top:2px;'><tr><td height=1>"+this.Items.outerHTML+"</td></tr></table>";
    var scrollWidth = parseInt( element.scrollWidth );

    if( scrollWidth > width ) {
    this.Title.style.width = scrollWidth;
    this.List.style.width = scrollWidth;
    }

    return;
}

function ChangeTitleHandler() {
    var newTitle = this.Items.options[this.Items.options.selectedIndex].innerHTML ;
    this.Title.children(0).cells(0).innerHTML = "<nobr>"+newTitle+"</nobr>";

    return;
}

function ChangeListHandler() {
    var length = this.Items.length;
    var item = "";

    var listHeight = parseInt( this.env.Height ) * Math.min( SL_SBLen, length ) + 2;
    var overflowY = ( length > SL_SBLen ) ? "scroll" : "hidden";

    this.List.innerHTML = "";
    for( i = 0; i < this.Items.options.length; i++ ) {
    item = ""
        +"<DIV idx="+i+" style='height:"+this.env.Height+";"+SL_Text+"'"
        +"  onMouseDown='SList["+this.entry+"].ListMouseDown( this );'"
        +"  onMouseOver='SList["+this.entry+"].ListMouseOver( this );'"
        +"  onMouseOut='SList["+this.entry+"].ListMouseOut( this );'>"
        +"    <nobr>"+this.Items.options[i].innerText+"</nobr>"
        +"</DIV>";
    oItem = createObject( item );
    this.List.appendChild( oItem );
    }

    this.List.style.height = listHeight;
    this.List.style.overflowY = overflowY;

    return;
}

function AddOptionHandler( sText, sValue, iIndex ) {
    var oOption = document.createElement("OPTION");
    this.Items.options.add(oOption, iIndex);

    oOption.innerText = sText;
    oOption.value = sValue;

    return;
}

function setNSelect( pSrc, pKIN, pSE )
{
    this.entry = SList.length;
    this.lower = null;
    this.src = pSrc;
    this.env = pSE;
    this.kin = pKIN;

    // NSelect Object
    this.Items;
    this.Title;
    this.List;
    this.Table;

    // Create NSelect Element
    this.ItemObj = createObject;
    this.ListObj  = createList;
    this.TitleObj = createTitle;
    this.TableObj = createSList;

    // NSelect EventHandler
    this.TitleClick = TitleClickHandler;
    this.TitleMouseOver = TitleMouseOverHandler;
    this.TitleMouseOut = TitleMouseOutHandler;
    this.ListMouseDown = ListMouseDownHandler;
    this.ListMouseOver = ListMouseOverHandler;
    this.ListMouseOut = ListMouseOutHandler;
    this.CancelEvent = CancelEventHandler;

    // NSelect Function
    this.ModifyDiv = ModifyDivHandler;
    this.ChangeTitle = ChangeTitleHandler;
    this.ChangeList = ChangeListHandler;
    this.AddOption = AddOptionHandler;

    this.append = appendSList;
    this.Table = this.TableObj();

    SList[this.entry] = this;

    return;
}

function createObject( pSrc )
{
    oObj = new Object();
    oObj.Div = document.createElement("DIV");
    oObj.Div.insertAdjacentHTML("afterBegin", pSrc);

    return oObj.Div.children(0);
}

function createTitle()
{
    var length = this.Items.length;

    for ( i = 0; i < length; i++ ) {
    if (this.Items.options[i].selected) {
        SIName = this.Items.options[i].innerText;
        SIValue = this.Items.options[i].value;
    }
    }

    this.Title = createObject(""
    +"<DIV id=title style='width:"+this.env.Width+";overflow-X:hidden;position:relative;left:0px;top:0px;"
    +"border:"+this.env.Border+";cursor:default;background:"+this.env.BGColor+";'"
    +"    onClick='SList["+this.entry+"].TitleClick( window.event );'"
    +"    onMouseOver='SList["+this.entry+"].TitleMouseOver( window.event );'"
    +"    onMouseOut='SList["+this.entry+"].TitleMouseOut( window.event );'"
    +">"
        +"<table height="+this.env.Height+" "+SL_Table+" style='table-layout:fixed;text-overflow:hidden;'>"
    +"<tr>"
        +"    <td style='width:100%;font-size:"+this.env.FontSize+";color:"+this.env.FontColor+";"+SL_Text+"'><nobr>"+SIName+"</nobr></td>"
        +"    <td style='display:none;'></td>"
        +"    <td align=center valign=center width="+SL_SBWidth+"><img src='"+SL_IPrefix+this.env.AImage+"' border=0 style='Filter:Alpha( Opacity=80 )'></td>"
    +"</tr>"
    +"</table>"
    +"</DIV>");

    oTitle_Sub = createObject(""
    +"<img style='position:absolute;top:1px;left:0;width:"+this.env.Width+";height:"+this.env.Height+";'"
    +"    ondragstart='SList["+this.entry+"].CancelEvent( window.event );'"
    +" src='"+SL_IPrefix+SL_BImage+"'>");

    this.Title.childNodes(0).cells(1).appendChild( this.Items );
    this.Title.childNodes(0).cells(2).appendChild( oTitle_Sub );

    return;
}

function createList()
{
    var ListDiv = ""
    +"<DIV id=list style='position:absolute;z-index:2;display:none;background:"+this.env.BGColor+";"
    +"border:"+this.env.Border+";font-size:"+this.env.FontSize+";color:"+this.env.FontColor+";cursor:default;"
    +"width:"+this.env.Width+";overflow-X:hidden;"+SL_ScrollBar+"'></DIV>";

    this.List = createObject( ListDiv );
    this.ChangeList();

    return;
}

function createSList()
{
    this.Items = this.ItemObj( this.src );

    this.TitleObj();
    this.ListObj();

    var table = createObject(""
        +"<table cellspacing=0 cellpadding=1 border=0>"
        +"<tr><td></td></tr>"
        +"</table>");

    table.cells(0).appendChild( this.Title );
    table.cells(0).appendChild( this.List );

    return table;
}

var yearSelect;
var monthSelect;

var todayDate;

if (typeof(headerfooter_time_year) != "undefined")
{
    /* 오늘의 날짜를 서버 날짜로 설정 */
    todayDate = new Date(
                    headerfooter_time_year, headerfooter_time_month - 1,
                    headerfooter_time_day, headerfooter_time_hour,
                    headerfooter_time_minute, headerfooter_time_second);
}
else
    todayDate = new Date();

function memorialDay(name, month, day, solarLunar, holiday, type)
{
    this.name = name;
    this.month = month;
    this.day = day;
    this.solarLunar = solarLunar;
    this.holiday = holiday;    /* true : 빨간날 false : 안빨간날 */
    this.type = type;    /* true : real time setting */
    this.techneer = true;
}

var memorialDays = Array (
    new memorialDay("신정", 1, 1, 1, true),
    new memorialDay("", 12, 0, 2, true, true),    /* 실시간으로 정해짐 */
    new memorialDay("설날", 1, 1, 2, true),
    new memorialDay("", 1, 2, 2, true),
    new memorialDay("삼일절", 3, 1, 1, true),
    new memorialDay("식목일", 4, 5, 1, true),
    new memorialDay("석가탄신일", 4, 8, 2, true),
    new memorialDay("어린이날", 5, 5, 1, true),
    new memorialDay("현충일", 6, 6, 1, true),
    new memorialDay("제헌절", 7, 17, 1, true),
    new memorialDay("광복절", 8, 15, 1, true),
    new memorialDay("", 8, 14, 2, true),
    new memorialDay("추석", 8, 15, 2, true),
    new memorialDay("", 8, 16, 2, true),
    new memorialDay("개천절", 10, 3, 1, true),
    new memorialDay("성탄절", 12, 25, 1, true),

    new memorialDay("정월대보름", 1, 15, 2, false),
    new memorialDay("단오", 5, 5, 2, false),
    new memorialDay("국군의날", 10, 1, 1, false),
    new memorialDay("한글날", 10, 9, 1, false),
    new memorialDay("625전쟁일", 6, 25, 1, false),
    new memorialDay("삼짇날", 3, 3, 2, false),
    new memorialDay("물의날", 3, 22, 1, false),
    new memorialDay("만우절", 4, 1, 1, false),
    new memorialDay("장애인의날", 4, 20, 1, false),
    new memorialDay("과학의날", 4, 21, 1 , false),
    new memorialDay("충무공탄신일", 4, 28, 1, false),
    new memorialDay("근로자의날·법의날", 5, 1, 1, false),
    new memorialDay("어버이날", 5, 8, 1, false),
    new memorialDay("스승의날", 5, 15, 1, false),
    new memorialDay("발명의날", 5, 19, 1, false),
    new memorialDay("바다의날", 5, 31, 1, false),
    new memorialDay("환경의날", 6, 5, 1, false),
    new memorialDay("유두", 6, 15, 2, false),
    new memorialDay("칠월칠석", 7, 7, 2, false),
    new memorialDay("중양절", 9, 9, 2, false),
    new memorialDay("철도의날", 9, 18, 1, false),
    new memorialDay("소방의날", 11, 9, 1, false)
);


function myDate(year, month, day, leapMonth)
{
    this.year = year;
    this.month = month;
    this.day = day;
    this.leapMonth = leapMonth;
}

// 음력 데이터 (평달 - 작은달 :1,  큰달:2 )
// (윤달이 있는 달 - 평달이 작고 윤달도 작으면 :3 , 평달이 작고 윤달이 크면 : 4)
// (윤달이 있는 달 - 평달이 크고 윤달이 작으면 :5,  평달과 윤달이 모두 크면 : 6)
var lunarMonthTable = [
[2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2],
[1, 2, 1, 1, 2, 1, 2, 5, 2, 2, 1, 2],
[1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1],   /* 1901 */
[2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2],
[1, 2, 1, 2, 3, 2, 1, 1, 2, 2, 1, 2],
[2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1],
[2, 2, 1, 2, 2, 1, 1, 2, 1, 2, 1, 2],
[1, 2, 2, 4, 1, 2, 1, 2, 1, 2, 1, 2],
[1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1],
[2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2],
[1, 5, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2],
[1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1],
[2, 1, 2, 1, 1, 5, 1, 2, 2, 1, 2, 2],   /* 1911 */
[2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2],
[2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2],
[2, 2, 1, 2, 5, 1, 2, 1, 2, 1, 1, 2],
[2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2],
[1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1],
[2, 3, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1],
[2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2],
[1, 2, 1, 1, 2, 1, 5, 2, 2, 1, 2, 2],
[1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2],
[2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2],   /* 1921 */
[2, 1, 2, 2, 3, 2, 1, 1, 2, 1, 2, 2],
[1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2],
[2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 1],
[2, 1, 2, 5, 2, 1, 2, 2, 1, 2, 1, 2],
[1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1],
[2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2],
[1, 5, 1, 2, 1, 1, 2, 2, 1, 2, 2, 2],
[1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2],
[1, 2, 2, 1, 1, 5, 1, 2, 1, 2, 2, 1],
[2, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1],   /* 1931 */
[2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2],
[1, 2, 2, 1, 6, 1, 2, 1, 2, 1, 1, 2],
[1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2],
[1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1],
[2, 1, 4, 1, 2, 1, 2, 1, 2, 2, 2, 1],
[2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1],
[2, 2, 1, 1, 2, 1, 4, 1, 2, 2, 1, 2],
[2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2],
[2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1],
[2, 2, 1, 2, 2, 4, 1, 1, 2, 1, 2, 1],   /* 1941 */
[2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 1, 2],
[1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2],
[1, 1, 2, 4, 1, 2, 1, 2, 2, 1, 2, 2],
[1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2],
[2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2],
[2, 5, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2],
[2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2],
[2, 2, 1, 2, 1, 2, 3, 2, 1, 2, 1, 2],
[2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1],
[2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2],   /* 1951 */
[1, 2, 1, 2, 4, 2, 1, 2, 1, 2, 1, 2],
[1, 2, 1, 1, 2, 2, 1, 2, 2, 1, 2, 2],
[1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2],
[2, 1, 4, 1, 1, 2, 1, 2, 1, 2, 2, 2],
[1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2],
[2, 1, 2, 1, 2, 1, 1, 5, 2, 1, 2, 2],
[1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2],
[1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1],
[2, 1, 2, 1, 2, 5, 2, 1, 2, 1, 2, 1],
[2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2],   /* 1961 */
[1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1],
[2, 1, 2, 3, 2, 1, 2, 1, 2, 2, 2, 1],
[2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2],
[1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 2],
[1, 2, 5, 2, 1, 1, 2, 1, 1, 2, 2, 1],
[2, 2, 1, 2, 2, 1, 1, 2, 1, 2, 1, 2],
[1, 2, 2, 1, 2, 1, 5, 2, 1, 2, 1, 2],
[1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1],
[2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2],
[1, 2, 1, 1, 5, 2, 1, 2, 2, 2, 1, 2],   /* 1971 */
[1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1],
[2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 2, 1],
[2, 2, 1, 5, 1, 2, 1, 1, 2, 2, 1, 2],
[2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2],
[2, 2, 1, 2, 1, 2, 1, 5, 2, 1, 1, 2],
[2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 1],
[2, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1],
[2, 1, 1, 2, 1, 6, 1, 2, 2, 1, 2, 1],
[2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2],
[1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2],   /* 1981 */
[2, 1, 2, 3, 2, 1, 1, 2, 2, 1, 2, 2],
[2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2],
[2, 1, 2, 2, 1, 1, 2, 1, 1, 5, 2, 2],
[1, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2],
[1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 1, 1],
[2, 1, 2, 2, 1, 5, 2, 2, 1, 2, 1, 2],
[1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1],
[2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2],
[1, 2, 1, 1, 5, 1, 2, 1, 2, 2, 2, 2],
[1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2],   /* 1991 */
[1, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2],
[1, 2, 5, 2, 1, 2, 1, 1, 2, 1, 2, 1],
[2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2],
[1, 2, 2, 1, 2, 2, 1, 5, 2, 1, 1, 2],
[1, 2, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2],
[1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1],
[2, 1, 1, 2, 3, 2, 2, 1, 2, 2, 2, 1],
[2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1],
[2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1],
[2, 2, 2, 3, 2, 1, 1, 2, 1, 2, 1, 2],   /* 2001 */
[2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1],
[2, 2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2],
[1, 5, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2],
[1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 1],
[2, 1, 2, 1, 2, 1, 5, 2, 2, 1, 2, 2],
[1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2],
[2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2],
[2, 2, 1, 1, 5, 1, 2, 1, 2, 1, 2, 2],
[2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2],
[2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1],   /* 2011 */
[2, 1, 6, 2, 1, 2, 1, 1, 2, 1, 2, 1],
[2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2],
[1, 2, 1, 2, 1, 2, 1, 2, 5, 2, 1, 2],
[1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 1],
[2, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2],
[2, 1, 1, 2, 3, 2, 1, 2, 1, 2, 2, 2],
[1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2],
[2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2],
[2, 1, 2, 5, 2, 1, 1, 2, 1, 2, 1, 2],
[1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1],   /* 2021 */
[2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2],
[1, 5, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2],
[1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1],
[2, 1, 2, 1, 1, 5, 2, 1, 2, 2, 2, 1],
[2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2],
[1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 2],
[1, 2, 2, 1, 5, 1, 2, 1, 1, 2, 2, 1],
[2, 2, 1, 2, 2, 1, 1, 2, 1, 1, 2, 2],
[1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1],
[2, 1, 5, 2, 1, 2, 2, 1, 2, 1, 2, 1],   /* 2031 */
[2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2],
[1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 5, 2],
[1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1],
[2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2],
[2, 2, 1, 2, 1, 4, 1, 1, 2, 2, 1, 2],
[2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2],
[2, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1],
[2, 2, 1, 2, 5, 2, 1, 2, 1, 2, 1, 1],
[2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 1],
[2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2],   /* 2041 */
[1, 5, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2],
[1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2]];

/* 양력 <-> 음력 변환 함수
* type : 1 - 양력 -> 음력
*        2 - 음력 -> 양력
* leapmonth : 0 - 평달
*             1 - 윤달 (type = 2 일때만 유효)
*/
function lunarCalc(year, month, day, type, leapmonth)
{
    var solYear, solMonth, solDay;
    var lunYear, lunMonth, lunDay;
    var lunLeapMonth, lunMonthDay;
    var i, lunIndex;

    var solMonthDay = [31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

    /* range check */
    if (year < 1900 || year > 2040)
    {
        alert('1900년부터 2040년까지만 지원합니다');
        return;
    }

    /* 속도 개선을 위해 기준 일자를 여러개로 한다 */
    if (year >= 2000)
    {
        /* 기준일자 양력 2000년 1월 1일 (음력 1999년 11월 25일) */
        solYear = 2000;
        solMonth = 1;
        solDay = 1;
        lunYear = 1999;
        lunMonth = 11;
        lunDay = 25;
        lunLeapMonth = 0;

        solMonthDay[1] = 29;    /* 2000 년 2월 28일 */
        lunMonthDay = 30;    /* 1999년 11월 */
    }
    else if (year >= 1970)
    {
        /* 기준일자 양력 1970년 1월 1일 (음력 1969년 11월 24일) */
        solYear = 1970;
        solMonth = 1;
        solDay = 1;
        lunYear = 1969;
        lunMonth = 11;
        lunDay = 24;
        lunLeapMonth = 0;

        solMonthDay[1] = 28;    /* 1970 년 2월 28일 */
        lunMonthDay = 30;    /* 1969년 11월 */
    }
    else if (year >= 1940)
    {
        /* 기준일자 양력 1940년 1월 1일 (음력 1939년 11월 22일) */
        solYear = 1940;
        solMonth = 1;
        solDay = 1;
        lunYear = 1939;
        lunMonth = 11;
        lunDay = 22;
        lunLeapMonth = 0;

        solMonthDay[1] = 29;    /* 1940 년 2월 28일 */
        lunMonthDay = 29;    /* 1939년 11월 */
    }
    else
    {
        /* 기준일자 양력 1900년 1월 1일 (음력 1899년 12월 1일) */
        solYear = 1900;
        solMonth = 1;
        solDay = 1;
        lunYear = 1899;
        lunMonth = 12;
        lunDay = 1;
        lunLeapMonth = 0;

        solMonthDay[1] = 28;    /* 1900 년 2월 28일 */
        lunMonthDay = 30;    /* 1899년 12월 */
    }

    lunIndex = lunYear - 1899;

    while (true)
    {
//        document.write(solYear + "-" + solMonth + "-" + solDay + "<->");
//        document.write(lunYear + "-" + lunMonth + "-" + lunDay + " " + lunLeapMonth + " " + lunMonthDay + "<br>");

        if (type == 1 &&
            year == solYear &&
            month == solMonth &&
            day == solDay)
        {
            return new myDate(lunYear, lunMonth, lunDay, lunLeapMonth);
        }
        else if (type == 2 &&
                year == lunYear &&
                month == lunMonth &&
                day == lunDay &&
                leapmonth == lunLeapMonth)
        {
            return new myDate(solYear, solMonth, solDay, 0);
        }

        /* add a day of solar calendar */
        if (solMonth == 12 && solDay == 31)
        {
            solYear++;
            solMonth = 1;
            solDay = 1;

            /* set monthDay of Feb */
            if (solYear % 400 == 0)
                solMonthDay[1] = 29;
            else if (solYear % 100 == 0)
                solMonthDay[1] = 28;
            else if (solYear % 4 == 0)
                solMonthDay[1] = 29;
            else
                solMonthDay[1] = 28;

        }
        else if (solMonthDay[solMonth - 1] == solDay)
        {
            solMonth++;
            solDay = 1;
        }
        else
            solDay++;


        /* add a day of lunar calendar */
        if (lunMonth == 12 &&
            ((lunarMonthTable[lunIndex][lunMonth - 1] == 1 && lunDay == 29) ||
            (lunarMonthTable[lunIndex][lunMonth - 1] == 2 && lunDay == 30)))
        {
            lunYear++;
            lunMonth = 1;
            lunDay = 1;

            lunIndex = lunYear - 1899;

            if (lunarMonthTable[lunIndex][lunMonth - 1] == 1)
                lunMonthDay = 29;
            else if (lunarMonthTable[lunIndex][lunMonth - 1] == 2)
                lunMonthDay = 30;
        }
        else if (lunDay == lunMonthDay)
        {
            if (lunarMonthTable[lunIndex][lunMonth - 1] >= 3
                && lunLeapMonth == 0)
            {
                lunDay = 1;
                lunLeapMonth = 1;
            }
            else
            {
                lunMonth++;
                lunDay = 1;
                lunLeapMonth = 0;
            }

            if (lunarMonthTable[lunIndex][lunMonth - 1] == 1)
                lunMonthDay = 29;
            else if (lunarMonthTable[lunIndex][lunMonth - 1] == 2)
                lunMonthDay = 30;
            else if (lunarMonthTable[lunIndex][lunMonth - 1] == 3)
                lunMonthDay = 29;
            else if (lunarMonthTable[lunIndex][lunMonth - 1] == 4 &&
                    lunLeapMonth == 0)
                lunMonthDay = 29;
            else if (lunarMonthTable[lunIndex][lunMonth - 1] == 4 &&
                    lunLeapMonth == 1)
                lunMonthDay = 30;
            else if (lunarMonthTable[lunIndex][lunMonth - 1] == 5 &&
                    lunLeapMonth == 0)
                lunMonthDay = 30;
            else if (lunarMonthTable[lunIndex][lunMonth - 1] == 5 &&
                    lunLeapMonth == 1)
                    lunMonthDay = 29;
            else if (lunarMonthTable[lunIndex][lunMonth - 1] == 6)
                lunMonthDay = 30;
        }
        else
            lunDay++;
    }
}

function getWeekday(year, month, day)
{
    var weekday = Array("일", "월", "화", "수", "목", "금", "토");
    var date = new Date(year, month - 1, day);

    if (date)
        return weekday[date.getDay()];
}

function getPassDay(year, month, day)
{
    var date = new Date(year, month - 1, day);

    var interval = Math.floor((todayDate - date) / (1000 * 60 * 60 * 24) + 1);

    return interval;
}

function getDDay(year, month, day)
{
    var date = new Date(year, month - 1, day);

    var interval = Math.floor((date - todayDate) / (1000 * 60 * 60 * 24) + 1);

    return interval;
}

function getDateSpecificInterval(year, month, day, interval)
{
    var date = new Date(year, month - 1, parseInt(day) + parseInt(interval) - 1);

    return date;
}

function dayCalcDisplay(type)
{
	var yearElement = $("startYear");
	var monthElement = $("startMonth");
	var dayElement = $("startDay");
	
    var startYear = parseInt(yearElement.value);
    var startMonth = parseInt(monthElement.value);
    var startDay = parseInt(dayElement.value);

    if (!startYear || startYear == 0 ||
        !startMonth || startMonth == 0 ||
        !startDay || startDay == 0)
    {
        alert('기준일을 입력해주세요');
        return;
    }

    var startDate = new Date(startYear, startMonth - 1, startDay);
    var today = new Date(todayDate.getFullYear(),
                        todayDate.getMonth(), todayDate.getDate());

    switch (type)
    {
    /* 오늘은 몇일째 */
    case 1:
        if (today < startDate)
        {
            alert("기준일을 오늘보다 이전 날짜로 설정하세요");
            return;
        }

        var interval = getPassDay(startYear, startMonth, startDay);
        $("day1").value = interval;
        break;
    /* x 일 되는 날은 */
    case 2:
        if (today < startDate)
        {
            alert("기준일을 오늘 이전 날짜로 설정하세요");
            return;
        }

        var day2 = $("day2").value;

        if (day2 <= 0)
        {
            alert("0 보다 큰 수를 입력하세요");
            return;
        }

        var date = getDateSpecificInterval(startYear, startMonth, startDay, day2);

        yearElement.value = date.getFullYear();
        monthElement.value = date.getMonth() + 1;
        dayElement.value = date.getDate();

        break;
    /* D-Day */
    case 3:
        var targetYear = parseInt($("targetYear").value);
        var targetMonth = parseInt($("targetMonth").value);
        var targetDay = parseInt($("targetDay").value);
        var interval = getDDay(targetYear, targetMonth, targetDay);

        if (!targetYear || targetYear == 0 ||
            !targetMonth || targetMonth == 0 ||
            !targetDay || targetDay == 0)
        {
            alert('날짜를 입력해주세요');
            return;
        }

        var targetDate = new Date(targetYear, targetMonth - 1, targetDay);

        if (today > targetDate)
        {
            alert("기준일을 오늘 이후 날짜로 설정하세요");
            return;
        }

        $("day3").value = interval;

        break;

    /* 요일 계산 */
    case 4:
        var year = parseInt($("weekdayYear").value);
        var month = parseInt($("weekdayMonth").value);
        var day = parseInt($("weekdayDay").value);
        var weekday = $("weekday");

        if (!year || year == "0" ||
            !month || month == "0" ||
            !day || day == "0")
        {
            alert('날짜를 입력해 주세요');
            return;
        }

        if (year < 100)
        {
            alert('년도를 100년 이후로 입력해 주세요');
            return;
        }

        weekday.value = getWeekday(year, month, day) + "요일";

        break;

    /* 양력/음력 변환 */
    case 5:
        if ($('#solarLunar').value == 'solar')
        {
            var leapMonth = $('#leapMonth').checked;
            var date = lunarCalc(startYear, startMonth, startDay, 2, leapMonth);
        }
        else
        {
            var date = lunarCalc(startYear, startMonth, startDay, 1);
        }

        if (date)
        {
            $('#solarLunarYear').value = date.year;
            $('#solarLunarMonth').value =
                (date.leapMonth ? "윤" : "") + date.month;
            $('#solarLunarDay').value = date.day;
        }
        else
        {
            $('#solarLunarYear').value = "";
            $('#solarLunarMonth').value = "";
            $('#solarLunarDay').value = "";
        }

        break;
    }
}

function memorialDayCheck(solarDate, lunarDate)
{
    var i;
    var memorial;


    for (i = 0; i < memorialDays.length; i++)
    {
        if (memorialDays[i].month == solarDate.month &&
            memorialDays[i].day == solarDate.day &&
            memorialDays[i].solarLunar == 1)
            return memorialDays[i];

        if (memorialDays[i].month == lunarDate.month &&
            memorialDays[i].day == lunarDate.day &&
            memorialDays[i].solarLunar == 2 &&
            !memorialDays[i].leapMonth)
            return memorialDays[i];
    }

    return null;
}

function setMonthMinus() {
    var oYearSelect = $('#yearSelect');
    var oMonthSelect = $('#monthSelect');
    var year1;
    var month1;
    if (oMonthSelect.value == 1) {
    	year1 = parseInt(oYearSelect.value) - 1;
    	month1 = 12;
    } else {
    	year1 = oYearSelect.value;
    	month1 = parseInt(oMonthSelect.value) - 1;
    }
    setCalendar(year1, month1)
}
function setMonthPlus() {
    var oYearSelect = $('#yearSelect');
    var oMonthSelect = $('#monthSelect');
    var year1;
    var month1;
    if (oMonthSelect.value == 12) {
    	year1 = parseInt(oYearSelect.value) + 1;
    	month1 = 1;
    } else {
    	year1 = oYearSelect.value;
    	month1 = parseInt(oMonthSelect.value) + 1;
    }
    setCalendar(year1, month1)
    
}

function checkNumberKey( e){
	code = e.keyCode;
	if ((code >= 48 && code <= 57) || (code >= 96 && code <= 105) || code == 110 || code == 190 || code == 8 || code == 9 || code == 13 || code == 46){
		return false;
	}
}
function setCalendarByYear( element){
	if( element.value < 1900)
	{
	    /* clean all day cell */
//	    for (i = 0; i < 42; i++)
//	    {
//	        $('dayCell' + i).innerHTML = "";
//	        $('memoCell' + i).innerHTML = "";
//	    }
	}
	else {	
		setCalendar();
	}
}
function setCalendarByMonth( monthCell){	
	setCalendar($('#yearSelect').value, monthCell.innerHTML);
	
	for( i=1; i<=12; i++){
		cell = $('#monthCell' + i);
		if( monthCell == cell){
			cell.style.backgroundColor = "aqua";
		}
		else {
			cell.style.backgroundColor = "";
		}
	}
}

function setCalendar(year, month)
{
    var i;
    var oYearSelect = $('#yearSelect');
    var oMonthSelect = $('#monthSelect');

    if (!year)
    {
        year = oYearSelect.value;
        month = oMonthSelect.value;
    }
    else
    {
//        for (i = 0; i < oYearSelect.length; i++)
//            if (oYearSelect[i].value == year)
//            {
//                oYearSelect.selectedIndex = i;
//                break;
//            }
//
//        for (i = 0; i < oMonthSelect.length; i++)
//            if (oMonthSelect[i].value == month)
//            {
//                oMonthSelect.selectedIndex = i;
//                break;
//            }

		oYearSelect.value = year;
				
		for( i=1; i<=12; i++){
			cell = $('#monthCell' + i);
			if( i == month){
				cell.style.backgroundColor = "aqua";
				oMonthSelect.value = month;
			}
			else {
				cell.style.backgroundColor = "";
			}
		}
        
    }

    var monthDay = Array(31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);

    /* set monthDay of Feb */
    if (year % 400 == 0)
        monthDay[1] = 29;
    else if (year % 100 == 0)
        monthDay[1] = 28;
    else if (year % 4 == 0)
        monthDay[1] = 29;
    else
        monthDay[1] = 28;


    /* set the day before 설날 */
    if (lunarMonthTable[year - 1 - 1899][11] == 1)
        memorialDays[1].day = 29;
    else if (lunarMonthTable[year - 1 - 1899][11] == 2)
        memorialDays[1].day = 30;


    var date = new Date(year, month - 1, 1);
    var startWeekday = date.getDay();

    /* clean all day cell */
    for (i = 0; i < 42; i++)
    {
        $('#dayCell' + i).innerHTML = "";
        $('#memoCell' + i).innerHTML = "";
    }

    /* fill day cell */
    for (i = 0; i < monthDay[month - 1]; i ++)
    {
        var index = startWeekday + i;
        var dayHTML;
        var memoHTML;

        var solarDate = new myDate(year, month, i + 1);
        var lunarDate = lunarCalc(year, month, i + 1, 1);

        /* memorial day */
        var memorial = memorialDayCheck(solarDate, lunarDate);

        /* 쉬지않는 기념일 */
        var memorialDay = false;
        if (memorial && memorial.holiday == false)
            memorialDay = true;

        /* day print */
        dayHTML = "<span onClick=\"calendar.setStartDate(" +
                    year + ", " + month + ", " + ( i + 1 ) + ")\" STYLE='CURSOR:POINTER'>" +
                    "<font id=ln2 color='COLOR' title='TITLE'>" +
                    "HIGHLIGHT_START" + ( i + 1 ) + "HIGHLIGHT_END" +
                    "</font></span>";

        /* decoration */
        if ((memorial && memorial.holiday) || index % 7 == 0)
            dayHTML = dayHTML.replace("COLOR", "#DD7403");
        else if (index % 7 == 6)
            dayHTML = dayHTML.replace("COLOR", "#3C8096");

        if (memorial)
            dayHTML = dayHTML.replace("TITLE", memorial.name);

        if (todayDate.getFullYear() == year &&
            todayDate.getMonth() + 1 == month &&
            todayDate.getDate() == i + 1)
        {
            dayHTML = dayHTML.replace("HIGHLIGHT_START", "<b><font color=blue>");
            dayHTML = dayHTML.replace("HIGHLIGHT_END", "</font></b>");
        }

        dayHTML = dayHTML.replace("TITLE", "");
        dayHTML = dayHTML.replace("COLOR", "");
        dayHTML = dayHTML.replace("HIGHLIGHT_START", "");
        dayHTML = dayHTML.replace("HIGHLIGHT_END", "");


        $('#dayCell' + index).innerHTML = dayHTML;

        /* lunar calnedar print */
        if (lunarDate.day == 1 || lunarDate.day == 15)
        {
            if (imagePath != null && imagePath != undefined) {
				memoHTML = "<img src="+imagePath+"/common/calendar/ico_IL_date" + (lunarDate.month < 10 ? "0" + lunarDate.month : lunarDate.month) + (lunarDate.day < 10 ? "0" + lunarDate.day: lunarDate.day) + ".gif border=0>";
			} else {
				memoHTML = "<img src=/images/common/calendar/ico_IL_date" + (lunarDate.month < 10 ? "0" + lunarDate.month : lunarDate.month) + (lunarDate.day < 10 ? "0" + lunarDate.day: lunarDate.day) + ".gif border=0>";
			}
		}
        else
            memoHTML = "<table border=0 cellpadding=0 cellspacing=0><tr height=10><td></td><tr></table>";

        $('#memoCell' + index).innerHTML = memoHTML;
    }
}

function lunarMonthCheck()
{
    if ($('#solarLunar').value == "solar")
        $('#leapMonth').disabled = false;
    else
        $('#leapMonth').disabled = true;
}

var ayear = todayDate.getFullYear(), amonth = todayDate.getMonth() + 1;

//Event.observe(window, 'load', function(){
//	Event.observe(document, 'click', function(event){
////		if ($('tims_calendar') == undefined || $('tims_calendar').style.display == 'none') {
////			return;
////		}
//		if( calendar.isActivated()) {
//			if (!divClick) { 
//				calendar.removeCalendar();
//			} else {
//				divClick = false;
//			}
//		}
//	}, false);
//});