<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>


	

<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/map.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery.autocomplete.js"></script>

<link rel="StyleSheet" href="<c:out value="${gsDomainContext}"/>/script/jquery/jquery.autocomplete.css" type="text/css" />

<script type="text/javascript">
	function findValue(li) {
		if( li == null ) return alert("No match!"); 
	
		// if coming from an AJAX call, let's use the CityId as the value
		if( !!li.extra ) var sValue = li.extra[1];
	
		// otherwise, let's just display the value in the text box
		else var sValue = li.selectValue;
	
		alert("The value you selected was: " + sValue);
	}
	
	function selectItem(li) {
	  	//findValue(li);
	}
	
	function formatItem(item) {
		return "["+item.subj+"] "+item.subjnm;
	}
	
	function lookupAjax(){
		var oSuggest = $("#p_subjnm")[0].autocompleter;
	 	oSuggest.findValue();
		return false;
	}


	$(document).ready(function () {

		if('<c:out value="${p_subj}"/>' == '' || '<c:out value="${p_subj}"/>' == 'null') 
			$("#selectedSubjnmTables").hide();
		else
			$("#selectedSubjnmTables").show();




		var url = "/com/aja/sch/selectSubjList.do";

		
		$("#p_subjnm").autocomplete(
		  "<c:out value="${gsDomainContext}" />"+url,
			  {
				delay:10,
				minChars:0,
				matchSubset:2,
				max:100,
				matchContains:2,
				cacheLength:10,
				scrollHeight : 200,
				//selectFirst:false,		
				dataType: 'json',
				contentType : "application/json:charset=utf-8",
				extraParams: {
				  	searchAtt : function() {return $("#p_upperclass").val();},
				  	searchSubjnm : function() {return $("#p_subjnm").val();},
				  	//searchGyear : '<c:out value="${p_gyear}"/>',
				  	//searchGrSeq : '<c:out value="${p_grseq}"/>'
			    },
			       
				parse: function(data) {
					var parsed = [];
					
					data = data.result;
					if(data.length > 0)
					{
						for (var i = 0; i < data.length; i++) {
							//alert(data[i].commonCode);
		                	parsed[parsed.length] = {
				                data: data[i],
		                		value: data[i].subj,
		                		result: data[i].subjnm
			            	};
				                
						}
					}
					else
					{
						$("#selectedSubjnm").html('검색된 내용이 없습니다.');
						$("#selectedSubjnmTables").show();
					}
					return parsed;
				},
				onItemSelect:selectItem,
				onFindValue:findValue,
				formatItem:formatItem
			 }).result(function(e, item) {
				//기수테이블에서 검색한다.
				var str = "";
				str = "["+item.subj+"] "+item.subjnm;
				$("#selectedSubjnm").html(str);
				$("#p_subjinfo").val(str);
				$("#p_subj").val(item.subj);
				$("#p_year").val(item.year);
				$("#p_subjseq").val(item.subjseq);
				$("#selectedSubjnmTables").show();
			 });

	});

	 
	//엔터입력시 
	 function fn_checkEnter(event){
		 if(event.keyCode == 13){
			 fn_SearchDoPageList();
		 }
			 
	 }

	//검색 기본 파람
	function fn_SearchDoPageList()
	{
		
		//검색값이 없다면 기수정보를 삭제한다.
		if($("#p_subjnm").val() == ''){
			search_init();
		}
		doPageList();
	}

	function search_init(){
		$("#p_subjinfo").html("");
		$("#selectedSubjnm").html("");
		$("#p_subj").val("");
		$("#p_year").val("");
		$("#p_subjseq").val("");
		$("#selectedSubjnmTables").hide();
	}

</script>
<div class="searchWrap txtL">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">과정분류</strong>
				<ui:code id="p_upperclass" selectItem="${p_upperclass}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL" event="search_init()" />
			</li>
			<li>
				<strong class="shTit">과정명</strong>
				<input type="text" name="p_subjnm" id="p_subjnm" autocomplete="off" size="110" 
				onkeypress="fn_checkEnter(event)" style="ime-mode:enabled;"  title="마우스더블클릭시 전체보기 가능합니다."/>
				<input type="hidden" name="p_subj" 	id="p_subj" 		value="${p_subj}"/>
				<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
	<div id="selectedSubjnmTables">	
		<div class="shLine"></div>
		<div>		
			<ul class="datewrap">
				<li class="floatL">
					<strong class="shTit">선택과정 : <span id="selectedSubjnm">${p_subjinfo}</span></strong>
					<input type="hidden" name="p_subjinfo" id="p_subjinfo" value="${p_subjinfo}">
					<a href="#none" onclick="search_init();" class="btn_del"><span>검색정보삭제</span></a>
				</li>
			</ul>		
		</div>
	</div>		
</div>


