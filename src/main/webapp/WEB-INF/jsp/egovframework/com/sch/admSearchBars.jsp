<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/json2.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery.dotimeout.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/map.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/message.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/ui/jquery.effects.drop.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/base_new.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/ziaan/shortcut.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/calendar/datepicker.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/commonUtil.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/common/common_function.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery.autocomplete.js"></script>
<link rel="StyleSheet" href="<c:out value="${gsDomainContext}"/>/script/jquery/jquery.autocomplete.css" type="text/css" />
<style>
	/* 로딩이미지 박스 꾸미기 */
	div#viewLoading {
		text-align: center;
		padding-top: 70px;
		background: #FFFFF0;
		filter: alpha(opacity=60);
		opacity: alpha*0.6;
	}
</style>

<c:if test="${fn:indexOf(selectViewType, 'C') == -1}">



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
		//기수테이블에서 검색한다.
		if( "<c:out value="${selectViewType}"/>" == "A" || "<c:out value="${selectViewType}"/>" == "AA" ){
			return "["+item.subj+"] "+item.subjnm;
		} 
		//과정테이블에서 검색한다.
		else {
			var str = "["+item.subj+"] "+item.subjnm + " - " + item.numberSubjseq + "기";
			if( item.edustart ) str += " ["+item.edustart+" ~ "+item.eduend+"]";
			return str;
		}
	}
	
	function lookupAjax(){
		var oSuggest = $("#ses_search_subjnm")[0].autocompleter;
	 	oSuggest.findValue();
		return false;
	}



	
	$(document).ready(function () {


		// 페이지가 로딩될 때 'Loading 이미지'를 숨긴다.
		//$('#viewLoading').show();
		
		// ajax 실행 및 완료시 'Loading 이미지'의 동작을 컨트롤하자.
		/*
		$('#viewLoading')
		.ajaxStart(function()
		{
			// 로딩이미지의 위치 및 크기조절
			$('#viewLoading').css('position', 'absolute');
			$('#viewLoading').css('left', $('#adm_searchBar_box_knise').offset().left);
			$('#viewLoading').css('top', $('#adm_searchBar_box_knise').offset().top);
			$('#viewLoading').css('width', $('#adm_searchBar_box_knise').css('width'));
			$('#viewLoading').css('height', $('#adm_searchBar_box_knise').css('height'));
			
			//$(this).show();
			$(this).fadeIn(500);
		})
		.ajaxStop(function()
		{
			//$(this).hide();
			$(this).fadeOut(500);
		});
		*/


		
		if('<c:out value="${sessionScope.ses_search_subj}"/>' == '' || '<c:out value="${sessionScope.ses_search_subj}"/>' == 'null') 
			$("#selectedSubjnmTables").hide();
		else
			$("#selectedSubjnmTables").show();




		var url = "";
		var selectViewType="<c:out value="${selectViewType}"/>";
		//기수테이블에서 검색한다.
		if( selectViewType == "A" || selectViewType == "AA"){
			url = "/com/aja/sch/selectSubjList.do";
		}else {				
			url = "/com/aja/sch/selectSubjseqList.do";
		}

		
		$("#ses_search_subjnm").autocomplete(
		  "<c:out value="${gsDomainContext}" />"+url,
			  {
				delay:0,
				minChars:0,
				matchSubset:0,
				max:100,
				matchContains:true,
				cacheLength:0,
				scrollHeight : 200,
				//selectFirst:false,		
				dataType: 'json',
				contentType : "application/json:charset=utf-8",
				extraParams: {
				  	searchAtt : function() {return $("#ses_search_att").val();},
				  	searchGyear : function() {return $("#ses_search_gyear").val();},
				  	searchGrSeq : function() {return $("#ses_search_grseq").val();},
				  	searchSubjnm : function() {return $("#ses_search_subjnm").val();}
			    },
			       
				parse: function(data) {
					var parsed = [];
					
					data = data.result;
					if(data.length > 0)
					{
						for (var i = 0; i < data.length; i++) {
							//alert(data[i].commonCode);
					                if( selectViewType == "A" || selectViewType == "AA" ){
					                	parsed[parsed.length] = {
							                data: data[i],
					                		value: data[i].subj,
					                		result: data[i].subjnm
						            	};
					                } else {
				                		parsed[parsed.length] = {
							                data: data[i],
					                		value: data[i].subjseq,
					                		result: data[i].subjnm
						            	};
					                }
				                
						}
					}
					else
					{
						//alert('검색된 내용이 없습니다.');
						$("#selectedSubjnm").html('검색된 내용이 없습니다.');
					}
					return parsed;
				},
				onItemSelect:selectItem,
				onFindValue:findValue,
				formatItem:formatItem
			 }).result(function(e, item) {
				//기수테이블에서 검색한다.
				var str = "";
				if( selectViewType == "A" || selectViewType == "AA" ){
					str = "["+item.subj+"] "+item.subjnm;
					$("#selectedSubjnm").html(str);
				}else if( selectViewType == "B" || selectViewType == "D" ){
					str = "["+item.subj+"] "+item.subjnm + " - " + item.numberSubjseq + "기";
					if( item.edustart ) str += " ["+item.edustart+" ~ "+item.eduend+"]";
					$("#selectedSubjnm").html(str);
				}
				$("#ses_search_subjinfo").val(str);
				$("#ses_search_subj").val(item.subj);
				$("#ses_search_year").val(item.year);
				$("#ses_search_subjseq").val(item.subjseq);
				$("#selectedSubjnmTables").show();
			 });




		//교육기수 가져오기
		getGrSeqJsonData();
	});

	
	//교육기수가져오기
	function getGrSeqJsonData() { 
		$("#ses_search_grseq").html('');
		$("#ses_search_grseq").append('<option value=\"\">ALL</option>');
		
		var ses_search_gmonthcnt = $("#ses_search_gmonth").length;
		
		var  ses_search_gmonth = "";
		if ( $("#ses_search_gmonth").length > 0 ) {				
			ses_search_gmonth = $("#ses_search_gmonth").val(); 
		}	
		
		$.ajax({  
			url: "<c:out value="${gsDomainContext}" />/com/aja/sch/selectGrSeqList.do",  
			cache: false,
			data: {
				searchGyear : function() {return $("#ses_search_gyear").val();}
				,searchAtt : function() {return $("#ses_search_att").val();}
				,searchGmonth : function() {return ses_search_gmonth;} 			
			},
			dataType: 'json',
			contentType : "application/json:charset=utf-8",
			success: function(data) {   
				data = data.result;
				for (var i = 0; i < data.length; i++) {
					var value = data[i].grseq;
					var title = data[i].grseqnm

					if('<c:out value="${sessionScope.ses_search_grseq}"/>' == value)
						$("select#ses_search_grseq").append("<option value='"+value+"' selected>"+title+"</option>");
					else
						$("select#ses_search_grseq").append("<option value='"+value+"'>"+title+"</option>");
				}
			},    
			error: function(xhr, status, error) {   
				alert(status);   
				alert(error);    
			}   
		});   
	} 	
	
	 
	//엔터입력시 
	 function fn_checkEnter(event){		
		 if(event.keyCode == 13){
			 //fn_SearchDoPageList();
			 doPageList();
		 }
			 
	 }

	//검색 기본 파람
	function fn_SearchDoPageList()
	{
		
		//검색값이 없다면 기수정보를 삭제한다.
		if($("#ses_search_subjnm").val() == ''){
			//search_init(false);
		}
		doPageList();
	}

	function search_init(act){
		$("#ses_search_subjinfo").html("");
		$("#selectedSubjnm").html("");
		$("#ses_search_subj").val("");
		$("#ses_search_year").val("");
		$("#ses_search_subjseq").val("");
		$("#selectedSubjnmTables").hide();
		if(act) getGrSeqJsonData();
	}

	//문제은행콘텐츠
	function getExamSubjJsonData() { 
		$("#ses_search_grseq").html('');
		$("#ses_search_grseq").append('<option value=\"\">ALL</option>');
		
		$.ajax({  
			url: "<c:out value="${gsDomainContext}" />/com/aja/sch/selectExamSubjList.do",  
			cache: false,
			data: {
				searchGyear : function() {return $("#ses_search_gyear").val();}
				,searchAtt : function() {return $("#ses_search_att").val();}
			},
			dataType: 'json',
			contentType : "application/json:charset=utf-8",
			success: function(data) {   
				data = data.result;
				for (var i = 0; i < data.length; i++) {
					var value = data[i].exam_subj;
					var title = data[i].exam_subjnm

					if('<c:out value="${sessionScope.ses_search_grseq}"/>' == value)
						$("select#ses_search_grseq").append("<option value='"+value+"' selected>"+title+"</option>");
					else
						$("select#ses_search_grseq").append("<option value='"+value+"'>"+title+"</option>");
				}
			},    
			error: function(xhr, status, error) {   
				alert(status);   
				alert(error);    
			}   
		});   
	} 
		  
</script>

</c:if>


<!-- 

	############## selectViewType : 검색화면 구분 ##############
	
	- A TYPE 	: 과정검색
	- AA TYPE 	: 연도별 과정검색
	- B TYPE 	: 연도별 과정-기수검색
 -->
	
<!-- 로딩 이미지 -->
<div id="viewLoading"  style="position:absolute;width:100%;height:100%;display:none;" onClick="return false;">
	<img src="/images/loading.gif" />
</div>



<div class="searchWrap txtL" id="adm_searchBar_box_knise">


<!-- 과정명만을 검색한다. -->
<c:if test="${selectViewType == 'A'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">과정분류</strong>
				<ui:code id="ses_search_att" selectItem="${ses_search_att}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL" event="search_init(true)" />
			</li>
			<li>
				<strong class="shTit">과정명</strong>
				<input type="text" name="ses_search_subjnm" id="ses_search_subjnm" autocomplete="off" size="80" 
				onkeypress="fn_checkEnter(event)" style="ime-mode:enabled;"  title="마우스더블클릭시 전체보기 가능합니다."/>
				<input type="hidden" name="ses_search_subj" 	id="ses_search_subj" 		value="${sessionScope.ses_search_subj}"/>
				<input type="hidden" name="ses_search_subjseq" 	id="ses_search_subjseq"		value="${sessionScope.ses_search_subjseq}"/>
				
				<c:if test="${fn:indexOf(pageContext.request.requestURL, '/adm/cou/subjectList') < 0 }">
					<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
				</c:if>
			</li>
			<c:if test="${fn:indexOf(pageContext.request.requestURL, '/adm/cou/subjectList') > -1 }">
				<li>
					<strong class="shTit">과정코드</strong>
					<input type="text" name="searchSubjCode" id="searchSubjCode" size="10"/>
					
					<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
				</li>
			</c:if>
		</ul>		
	</div>
</c:if>	

<!-- 년도별 과정명을 검색시 사용  -->
<c:if test="${selectViewType == 'AA'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">과정분류</strong>
				<ui:code id="ses_search_att" selectItem="${ses_search_att}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL" event="search_init(true)" />
			</li>
			<li>
				<strong class="shTit">연도</strong>
<!--				그룹에 대한 년도-->
				<select name="ses_search_gyear" id="ses_search_gyear" onchange="search_init(true)">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_gyear}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<strong class="shTit">월</strong>
				<select name="ses_search_gmonth" id="ses_search_gmonth" onchange="search_init(true)">
					<option value="" <c:if test="${ses_search_gmonth eq '' }">selected</c:if>>ALL</option>
				<c:forEach var="xMonth" begin="1" end="12">					
					<option value="${xMonth}" <c:if test="${ses_search_gmonth eq xMonth}">selected</c:if>>${xMonth}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<strong class="shTit">교육기수</strong>
				<select name="ses_search_grseq" id="ses_search_grseq"   onchange="search_init(false)" style="width:250px;">
				</select>
			</li>
		</ul>		
		<div class="shLine"></div>
		<ul class="datewrap">			
			<li>
				<strong class="shTit">과정명</strong>
				<input type="text" name="ses_search_subjnm" id="ses_search_subjnm" autocomplete="off" size="80" 
				onkeypress="fn_checkEnter(event)" style="ime-mode:enabled;" title="마우스더블클릭시 전체보기 가능합니다."/>
				<input type="hidden" name="ses_search_subj" 	id="ses_search_subj" 		value="${sessionScope.ses_search_subj}"/>
				<input type="hidden" name="ses_search_subjseq" 	id="ses_search_subjseq"		value="${sessionScope.ses_search_subjseq}"/>
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
</c:if>
<!-- 년도별 검색시 사용  -->
<c:if test="${selectViewType == 'AA_year'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">과정분류</strong>
				<ui:code id="ses_search_att" selectItem="${ses_search_att}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL" event="search_init(true)" />
			</li>
			<li>
				<strong class="shTit">연도</strong>
<!--				그룹에 대한 년도-->
				<select name="ses_search_gyear" id="ses_search_gyear" onchange="search_init(true)">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_gyear}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
</c:if>

<!-- 년도별 검색시 사용  -->
<c:if test="${selectViewType == 'BB_year'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">과정분류</strong>
				<ui:code id="ses_search_att" selectItem="${ses_search_att}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL" event="search_init(true)" />
			</li>
			<li>
				<strong class="shTit">연도</strong>
<!--				그룹에 대한 년도-->
				<select name="ses_search_gyear" id="ses_search_gyear" onchange="search_init(true)">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_gyear}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
			</li>			
			<li>
				<strong class="shTit">콘텐츠명</strong>
				<input type="text" name="ses_search_subjnm" id="ses_search_subjnm" autocomplete="off" size="80" 
				onkeypress="fn_checkEnter(event)" style="ime-mode:enabled;"  title="마우스더블클릭시 전체보기 가능합니다."/>
				<input type="hidden" name="ses_search_subj" 	id="ses_search_subj" 		value="${sessionScope.ses_search_subj}"/>
				<input type="hidden" name="ses_search_subjseq" 	id="ses_search_subjseq"		value="${sessionScope.ses_search_subjseq}"/>				
			</li>
			<li>
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
</c:if>


<!-- 년도별 검색시 사용  -->
<c:if test="${selectViewType == 'CC_year'}">
	<div>		
		<ul class="datewrap">			
			<li>
				<strong class="shTit">연도</strong>
<!--				그룹에 대한 년도-->
				<select name="ses_search_gyear" id="ses_search_gyear" onchange="search_init(true)">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_gyear}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<strong class="shTit">대상</strong>
				<input type="checkbox" name="p_temp_gubun_1" id="p_temp_gubun_1" value="T" <c:if test="${p_temp_gubun_1 eq 'T' }">checked</c:if> /> 교원			
				<input type="checkbox" name="p_temp_gubun_2" id="p_temp_gubun_2" value="E" <c:if test="${p_temp_gubun_2 eq 'E' }">checked</c:if> /> 보조인력
				<input type="checkbox" name="p_temp_gubun_3" id="p_temp_gubun_3" value="P" <c:if test="${p_temp_gubun_3 eq 'P' }">checked</c:if> /> 일반
				<input type="checkbox" name="p_temp_gubun_4" id="p_temp_gubun_4" value="R" <c:if test="${p_temp_gubun_4 eq 'R' }">checked</c:if> /> 교원전문직		
									
			</li>
			<li>						
				<strong class="shTit">임의 추출</strong>
				<input type="text" name="p_per" id="p_per" value="${p_per}" size="3" onkeypress="fn_sulResultStats_checkEnter(event)"  style="ime-mode:enabled;" />%					
			</li>
			<li>
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
</c:if>


<!-- 년도별 검색시 사용  -->
<c:if test="${selectViewType == 'AA_member'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">분류</strong>
				<select name="search_att" id="search_att" >
					<optgroup label="────────">
						<%-- <option value="sex" <c:if test="${search_att eq 'sex'}">selected</c:if>>성별</option> --%>
						<option value="address" <c:if test="${search_att eq 'address'}">selected</c:if>>지역별</option>
						<option value="upperclass" <c:if test="${search_att eq 'upperclass'}">selected</c:if>>회원구분</option>
						<option value="history" <c:if test="${search_att eq 'history'}">selected</c:if>>경력</option>
						<%-- <option value="ischarge" <c:if test="${search_att eq 'ischarge'}">selected</c:if>>종별</option> --%>
						<option value="job" <c:if test="${search_att eq 'job'}">selected</c:if>>교원자격</option>
						<option value="stat" <c:if test="${search_att eq 'stat'}">selected</c:if>>상태</option>
					</optgroup>
					<optgroup label="────────">
						<option value="total" <c:if test="${search_att eq 'total'}">selected</c:if>>회원구분별</option>
					</optgroup>
				</select>
			</li>
			<li>
				<strong class="shTit">연도</strong>
<!--				그룹에 대한 년도-->
				<select name="ses_search_gyear" id="ses_search_gyear" onchange="search_init(true)">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_gyear}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
	
	<script type="text/javascript">
		function attChange() {
			var att = $('select[name=search_att]');
			var attVal = att.val();
			if(attVal == 'total') {
				att.closest('li').next().hide();
			} else {
				att.closest('li').next().show();
			}
		}

		$(function() {
			$('select[name=search_att]').change(function() {
				attChange();
			});
		});
	</script>
</c:if>

<!-- 과정의 기수까지 검색을 진행할때 사용 -->
<c:if test="${selectViewType == 'B'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">과정분류</strong>
				<ui:code id="ses_search_att" selectItem="${ses_search_att}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL" event="search_init(true)" />
			</li>
			<li>
				<strong class="shTit">연도</strong>
				<select name="ses_search_gyear" id="ses_search_gyear" onchange="search_init(true)">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_gyear}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<strong class="shTit">월</strong>
				<select name="ses_search_gmonth" id="ses_search_gmonth" onchange="search_init(true)">
					<option value="" <c:if test="${ses_search_gmonth eq '' }">selected</c:if>>ALL</option>
				<c:forEach var="xMonth" begin="1" end="12">					
					<option value="${xMonth}" <c:if test="${ses_search_gmonth eq xMonth}">selected</c:if>>${xMonth}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<strong class="shTit">교육기수</strong>
				<select name="ses_search_grseq" id="ses_search_grseq"   onchange="search_init(false)" style="width:250px;">
				</select>
			</li>
		</ul>		
		<div class="shLine"></div>
		<ul class="datewrap">			
			<li>
				<strong class="shTit">과정명-기수</strong>
				<input type="text" name="ses_search_subjnm" id="ses_search_subjnm" autocomplete="off" size="80" 
				onkeypress="fn_checkEnter(event)" style="ime-mode:enabled;" title="마우스더블클릭시 전체보기 가능합니다."/>
				<input type="hidden" name="ses_search_subj" 	id="ses_search_subj" 		value="${sessionScope.ses_search_subj}"/>
<!--				과정기수의 대한 년도 -->
				<input type="hidden" name="ses_search_year" 	id="ses_search_year" 		value="${sessionScope.ses_search_year}"/>
				<input type="hidden" name="ses_search_subjseq" 	id="ses_search_subjseq"		value="${sessionScope.ses_search_subjseq}"/>
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
</c:if>
<c:if test="${selectViewType == 'B_1'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">과정분류</strong>
				<ui:code id="ses_search_att" selectItem="${ses_search_att}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL" event="search_init(true)" />
			</li>
			<li>
				<strong class="shTit">연도</strong>
				<select name="ses_search_gyear" id="ses_search_gyear" onchange="search_init(true)">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_gyear}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<strong class="shTit">월</strong>
				<select name="ses_search_gmonth" id="ses_search_gmonth" onchange="search_init(true)">
					<option value="" <c:if test="${ses_search_gmonth eq '' }">selected</c:if>>ALL</option>
				<c:forEach var="xMonth" begin="1" end="12">					
					<option value="${xMonth}" <c:if test="${ses_search_gmonth eq xMonth}">selected</c:if>>${xMonth}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<strong class="shTit">교육기수</strong>
				<select name="ses_search_grseq" id="ses_search_grseq"   onchange="search_init(false)" style="width:250px;">
				</select>
			</li>
		</ul>		
		<div class="shLine"></div>
		<ul class="datewrap">			
			<li>
				<strong class="shTit">과정명-기수</strong>
				<input type="text" name="ses_search_subjnm" id="ses_search_subjnm" autocomplete="off" size="80" 
				onkeypress="fn_checkEnter(event)" style="ime-mode:enabled;" title="마우스더블클릭시 전체보기 가능합니다."/>
				<input type="hidden" name="ses_search_subj" 	id="ses_search_subj" 		value="${sessionScope.ses_search_subj}"/>
<!--				과정기수의 대한 년도 -->
				<input type="hidden" name="ses_search_year" 	id="ses_search_year" 		value="${sessionScope.ses_search_year}"/>
				<input type="hidden" name="ses_search_subjseq" 	id="ses_search_subjseq"		value="${sessionScope.ses_search_subjseq}"/>
				
			</li>
			<li>
				<strong class="shTit">신청기간</strong>
					<input name="p_search_from" type="text" size="10" maxlength="10" readonly value="${p_search_from}"> 
					<img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_search_from, 'yyyy.mm.dd')" /> ~
                    <input name="p_search_to" type="text" size="10" maxlength="10" readonly value="${p_search_to}"> 
                    <img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_search_to, 'yyyy.mm.dd')" />				
				
			</li>
		</ul>		
	</div>
</c:if>



<!-- 분야별 년도별을 검색시 사용  -->
<c:if test="${selectViewType == 'C'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">과정분류</strong>
				<ui:code id="ses_search_att" selectItem="${ses_search_att}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL"/>
			</li>
			<li>
				<strong class="shTit">연도</strong>
<!--				그룹에 대한 년도-->
				<select name="ses_search_gyear" id="ses_search_gyear">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_gyear}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
</c:if>

<!-- 분야별 년도별을 검색시 사용  -->
<jsp:useBean id="newDate" class="java.util.Date" />
<fmt:formatDate value="${newDate}" pattern="yyyy" var="nowYear" />  
<c:if test="${selectViewType == 'EDU_OFFICE'}">
	<div>		
		<ul class="datewrap">
			<li>
				<strong class="shTit">연도</strong>
				<select name="ses_search_gyear" id="ses_search_gyear">
					<c:forEach begin="2017" end="${nowYear}" varStatus="status">
						<option value="${status.index}" <c:if test="${status.index eq ses_search_gyear}">selected</c:if>>${status.index}</option>
					</c:forEach>
				</select>
			</li>
			<li>
				<strong class="shTit">월</strong>
				<select name="ses_search_gmonth" id="ses_search_gmonth">
					<option value="" <c:if test="${ses_search_gmonth eq ''}">selected</c:if>>ALL</option>
					<c:forEach begin="3" end="11" varStatus="status">
						<c:set var="gmonth">${status.index}</c:set>
						<c:if test="${fn:length(gmonth) == 1}">
							<c:set var="gmonth">0${gmonth}</c:set>
						</c:if>
						<option value="${gmonth}" <c:if test="${gmonth eq ses_search_gmonth}">selected</c:if>>${status.index}</option>
					</c:forEach>
				</select>
			</li>
			<%-- <li class="floatL">
				<strong class="shTit">과정분류</strong>
				<ui:code id="ses_search_att" selectItem="${ses_search_att}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li> --%>
			<li class="floatL">
				<strong class="shTit">과정분류</strong>
				<select name="p_cursBunryu" id="ses_search_gyear">
					<c:forEach items="${cursBunryuJob}" var="result">
					<option value="${result.code}" <c:if test="${result.code eq p_cursBunryu}">selected</c:if>>${result.codenm}</option>
				</c:forEach>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
</c:if>

<c:if test="${selectViewType == 'EDU_OFFICE_DETAIL'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">교육청</strong>
				<ui:code id="search_area" selectItem="${search_area}" codetype="0118" levels="1" title="지역구분" type="select" deleteItem="A00" selectTitle="ALL"/>
			</li>
			<li>
				<strong class="shTit">연도</strong>
				<select name="ses_search_gyear" id="ses_search_gyear">
					<c:forEach begin="2017" end="${nowYear}" varStatus="status">
						<option value="${status.index}" <c:if test="${status.index eq ses_search_gyear}">selected</c:if>>${status.index}</option>
					</c:forEach>
				</select>
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
				<!-- <a href="#none" class="btn_search" onclick="javascript:popupGraph()"><span>이수자추이</span></a> -->
			</li>
		</ul>		
	</div>
</c:if>

<c:if test="${selectViewType == 'SCORE'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">개설시기</strong>
				<input name="p_search_from" type="text" size="10" maxlength="10" readonly value="${p_search_from}"> 
				<img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_search_from, 'yyyy.mm.dd')" /> ~
				<input name="p_search_to" type="text" size="10" maxlength="10" readonly value="${p_search_to}"> 
				<img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_search_to, 'yyyy.mm.dd')" />
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
</c:if>

<c:if test="${selectViewType == 'POINT'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">연도</strong>
				<select name="ses_search_gyear" id="ses_search_gyear">
					<c:forEach items="${year_list}" var="result">
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_gyear}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
</c:if>


<!-- 년/월 검색시 사용  -->
<c:if test="${selectViewType == 'CC'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">연도</strong>
<!--				그룹에 대한 년도-->
				<select name="ses_search_gyear" id="ses_search_gyear">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_gyear}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<strong class="shTit">월</strong>
<!--				그룹에 대한 년도-->
				<select name="ses_search_gmonth" id="ses_search_gmonth">
				<c:forEach var="xMonth" begin="1" end="12">
					<c:set var="xMonthStr">${fn2:getNumberToString(xMonth)}</c:set>
					<option value="${xMonthStr}" <c:if test="${xMonthStr eq ses_search_gmonth}">selected</c:if>>${xMonthStr}</option>
				</c:forEach>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</li>
		</ul>		
	</div>
</c:if>


<!-- 과정의 기수까지 검색을 진행할때 사용 -->
<c:if test="${selectViewType == 'D'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">과정분류</strong>
				<ui:code id="ses_search_att" selectItem="${ses_search_att}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL" event="search_init(true)" />
			</li>
			<li>
				<strong class="shTit">연도</strong>
				<select name="ses_search_gyear" id="ses_search_gyear" onchange="search_init(true)">
					<option value="">ALL</option>
				<c:forEach items="${year_list}" var="result">
					<%-- <option value="${result.gyear}" <c:if test="${result.gyear eq sessionScope.ses_search_gyear}">selected</c:if>>${result.gyear}</option> --%>
					<option value="${result.gyear}" <c:if test="${result.gyear eq ses_search_gyear}">selected</c:if>>${result.gyear}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<strong class="shTit">월</strong>
				<select name="ses_search_gmonth" id="ses_search_gmonth" onchange="search_init(true)">
					<option value="" <c:if test="${ses_search_gmonth eq '' }">selected</c:if>>ALL</option>
				<c:forEach var="xMonth" begin="1" end="12">					
					<option value="${xMonth}" <c:if test="${ses_search_gmonth eq xMonth}">selected</c:if>>${xMonth}</option>
				</c:forEach>
				</select>
			</li>
			<li>
				<strong class="shTit">교육기수</strong>
				<select name="ses_search_grseq" id="ses_search_grseq"   onchange="search_init(false)" style="width:250px;">
				</select>
			</li>
			
			
		</ul>		
		<div class="shLine"></div>
		<ul class="datewrap">			
			<li>
				<strong class="shTit">과정명-기수</strong>
				<input type="text" name="ses_search_subjnm" id="ses_search_subjnm" autocomplete="off" size="80" 
				onkeypress="fn_checkEnter(event)" style="ime-mode:enabled;" title="마우스더블클릭시 전체보기 가능합니다."/>
				<input type="hidden" name="ses_search_subj" 	id="ses_search_subj" 		value="${sessionScope.ses_search_subj}"/>
<!--				과정기수의 대한 년도 -->
				<input type="hidden" name="ses_search_year" 	id="ses_search_year" 		value="${sessionScope.ses_search_year}"/>
				<input type="hidden" name="ses_search_subjseq" 	id="ses_search_subjseq"		value="${sessionScope.ses_search_subjseq}"/>
				
			</li>
		</ul>
		
		<div class="shLine"></div>
		<ul class="datewrap">			
			<li>
				<strong class="shTit">평가종류</strong>
				<c:if test="${empty view.papernum }">
					<ui:code id="p_examtype" selectItem="${p_examtype}" gubun="" codetype="0012"  levels="1"  condition=""
							upper="" title="평가종류" className="" type="select" selectTitle="" event="" />
				</c:if>
				<c:if test="${not empty view.papernum }">
                       <input type="hidden" name="p_examtype" value="${p_examtype}"/>
                       ${view.examtypenm}
				</c:if>			
				 <a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>                                            			
			</li>
			<input type="hidden"  name="p_setexamcnt" id="p_setexamcnt" value="50000"/>
			<input type="hidden"  name="p_setexamlessoncnt" id="p_setexamlessoncnt" value="50000"/>
			<%-- <li>
				<strong class="shTit">문제출제횟수 제한 </strong>
				<c:if test="${empty view.papernum }">
					<input name="p_setexamcnt" id="p_setexamcnt" type="text" class="input" size="3" value="${empty p_setexamcnt ? '50' : p_setexamcnt}"> 회
				</c:if>
				<c:if test="${not empty view.papernum }">
					<input name="p_setexamcnt" id="p_setexamcnt" type="text" class="input" size="3" value="${empty view.setexamcnt ? '50' : view.setexamcnt}"> 회
				</c:if>
				                            			
			</li> 
			<li>
				<strong class="shTit">동일차시 횟수 제한 </strong>
				<c:if test="${empty view.papernum }">
					<input name="p_setexamlessoncnt" id="p_setexamlessoncnt" type="text" class="input" size="3" value="${empty p_setexamlessoncnt ? '50' : p_setexamlessoncnt}"> 회
				</c:if>
				<c:if test="${not empty view.papernum }">
					<input name="p_setexamlessoncnt" id="p_setexamlessoncnt" type="text" class="input" size="3" value="${empty view.setexamlessoncnt ? '50' : view.setexamlessoncnt}"> 회
				</c:if>
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>                            			
			</li>
			--%>
		</ul>
		
	</div>
</c:if>

<c:if test="${selectViewType == 'M'}">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<strong class="shTit">기간</strong>
				<input name="p_search_from" type="text" size="10" maxlength="10" readonly value="${p_search_from}"> 
				<img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_search_from, 'yyyy.mm.dd')" /> ~
				<input name="p_search_to" type="text" size="10" maxlength="10" readonly value="${p_search_to}"> 
				<img src="/images/adm/ico/ico_calendar.gif" alt="달력" style="cursor:hand"  OnClick="popUpCalendar(this, document.all.p_search_to, 'yyyy.mm.dd')" />				
			</li>
			<li>
					<select id="ss_gadmin" name="ss_gadmin">
						<option value="ALL">==전 체==</option>
					<c:forEach items="${authList}" var="result">
						<option value="<c:out value="${result.gadmin}"/>"   <c:if test="${ss_gadmin == result.gadmin}">selected</c:if>><c:out value="${result.gadminnm}"/></option>
					</c:forEach>
					</select>	
			</li>
			<lli>
				<select id="p_search" name="p_search">
					<option value="userid" <c:if test="${p_search == 'userid'}">selected</c:if>>아이디</option>
					<option value="name"   <c:if test="${p_search == 'name'}">selected</c:if>>성명</option>
				</select>
				<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}"/>
				<a href="#none" class="btn_search" onclick="javascript:doPageList()"><span>검색</span></a>
			</lli>
		</ul>		
	</div>
</c:if>

<!-- 추가 검색 옵션용임 -->
<c:if test="${not empty selectParameter}">
<%@ include file = "/WEB-INF/jsp/egovframework/com/sch/admSearchParameter.jsp" %>
</c:if>
	<div id="selectedSubjnmTables" style="display:none;">	
		<div class="shLine"></div>
		<div>		
			<ul class="datewrap">
				<li class="floatL">
					<strong class="shTit">선택과정 : <span id="selectedSubjnm">${sessionScope.ses_search_subjinfo}</span></strong>
					<input type="hidden" name="ses_search_subjinfo" id="ses_search_subjinfo" value="${sessionScope.ses_search_subjinfo}">
					<a href="#none" onClick="search_init(false);" class="btn_del"><span>검색정보삭제</span></a>
				</li>
			</ul>		
		</div>
	</div>		
</div>


<div style="display:none">	
ses_search_att : <c:out value="${sessionScope.ses_search_att}"/><br>
ses_search_subj : <c:out value="${sessionScope.ses_search_subj}"/><br>
ses_search_grseq : <c:out value="${sessionScope.ses_search_grseq}"/><br>
ses_search_year : <c:out value="${sessionScope.ses_search_year}"/><br>
ses_search_subjseq : <c:out value="${sessionScope.ses_search_subjseq}"/><br>
ses_search_subjnm : <c:out value="${sessionScope.ses_search_subjnm}"/><br>
ses_search_subjinfo : <c:out value="${sessionScope.ses_search_subjinfo}"/><br>
<%-- ses_search_gyear : <c:out value="${sessionScope.ses_search_gyear}"/><br> --%>
ses_search_gyear : <c:out value="${ses_search_gyear}"/><br>
</div>


