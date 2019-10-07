<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<div id="chartdiv" style="width: 100%; height: 600px; background-color: #FFFFFF;" ></div>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/amcharts/amcharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/amcharts/serial.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery/jquery-1-9.1.js"></script>
<script type="text/javascript">
var thisYear = '${year}';
var yearAgo = ('${year}' - 1).toString();
var yearsAgo = ('${year}' - 2).toString();

var list = "${yearContentsList}";
var listArray = list.split("}, {");

var objData = {};
for(var n = 0; n < listArray.length; n++) {
	var map = opener.mapCreate(listArray[n]);

	if(null == objData[map.get('subj')]) {
		var data = {};
		data.category = map.get('subjnm');
		
		if(thisYear == map.get('year')) {
			data.thisYear = map.get('studentCnt');
		} else if(yearAgo == map.get('year')) {
			data.yearAgo = map.get('studentCnt');
		} else if(yearsAgo == map.get('year')) {
			data.yearsAgo = map.get('studentCnt');
		}
		objData[map.get('subj')] = data;
	} else {
		if(thisYear == map.get('year')) {
			objData[map.get('subj')].thisYear = map.get('studentCnt');
		} else if(yearAgo == map.get('year')) {
			objData[map.get('subj')].yearAgo = map.get('studentCnt');
		} else if(yearsAgo == map.get('year')) {
			objData[map.get('subj')].yearsAgo = map.get('studentCnt');
		}
	}
}

var amchartData = $.map(objData, function(value, index) {
    return [value];
});
AmCharts.makeChart("chartdiv",
	{
		"type": "serial",
		"categoryField": "category",
		"mouseWheelZoomEnabled": true,
		"rotate": true,
		"categoryAxis": {
			"gridPosition": "start"
		},
		"chartCursor": {
			"enabled": true
		},
		"chartScrollbar": {
			"enabled": true
		},
		"trendLines": [],
		"graphs": [
			{
				"lineAlpha": 0,
				"fillAlphas": 1,
				"title": thisYear,
				"type": "column",
				"valueField": "thisYear"
			},
			{
				"lineAlpha": 0,
				"fillAlphas": 1,
				"title": yearAgo,
				"type": "column",
				"valueField": "yearAgo"
			},
			{
				"lineAlpha": 0,
				"fillAlphas": 1,
				"title": yearsAgo,
				"type": "column",
				"valueField": "yearsAgo"
			}
		],
		"guides": [],
		"valueAxes": [
			{
				"id": "ValueAxis-1",
				"title": ""
			}
		],
		"allLabels": [],
		"balloon": {},
		"legend": {
			"enabled": true
		},
		"titles": [
			{
				"id": "Title-1",
				"size": 15,
				"text": "연간 콘텐츠 사용자 추이"
			}
		],
		"dataProvider": amchartData
	}
);
</script>


<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
