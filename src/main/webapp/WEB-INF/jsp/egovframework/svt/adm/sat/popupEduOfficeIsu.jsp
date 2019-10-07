<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="chartdiv" style="width: 100%; height: 600px; background-color: #FFFFFF;" ></div>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/amcharts/amcharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/amcharts/serial.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery/jquery-1-9.1.js"></script>

<script type="text/javascript">

var list = "${eduOfficeIsu}";
var listArray = list.split("}, {");

if(listArray.length > 0) {
	var amchartData = [];
	
	if(listArray == '[]') {
		var obj = {};
		obj.category = '데이터가 없습니다.';
		
		amchartData.push(obj);
	} else {
		for(var n = 0; n < listArray.length; n++) {
			var map = opener.mapCreate(listArray[n]);
			
			var obj = {};
			obj.category = map.get('year');
			obj.empT = map.get('empT');
			obj.empE = map.get('empE');
			obj.empR = map.get('empR');
			
			amchartData.push(obj);
			/* {
			"category": "category 1",
			"column-1": 8,
			"column-2": 5
			} */
		}
	}
	
	AmCharts.makeChart("chartdiv",
		{
			"type": "serial",
			"categoryField": "category",
			"categoryAxis": {
				"gridPosition": "start"
			},
			"trendLines": [],
			"graphs": [
				{
					"balloonText": "[[category]]년, [[title]]: [[value]]",
					"bullet": "round",
					"id": "AmGraph-1",
					"title": "교원",
					"valueField": "empT"
				},
				{
					"balloonText": "[[category]]년, [[title]]: [[value]]",
					"bullet": "square",
					"id": "AmGraph-2",
					"title": "보조인력",
					"valueField": "empE"
				},
				{
					"balloonText": "[[category]]년, [[title]]: [[value]]",
					"bullet": "diamond",
					"id": "AmGraph-3",
					"title": "교육전문직",
					"valueField": "empR"
				}
			],
			"guides": [],
			"valueAxes": [
				{
					"id": "ValueAxis-1",
					"integersOnly": true,
					"title": "인원"
				}
			],
			"allLabels": [],
			"balloon": {},
			"legend": {
				"enabled": true,
				"position": "right",
				"useGraphSettings": true
			},
			"titles": [
				{
					"id": "Title-1",
					"size": 15,
					"text": "${eduOfficeName}교육청 이수자 추이"
				}
			],
			"dataProvider": amchartData
		}
	);
}
</script>

<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
