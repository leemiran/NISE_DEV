<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>

<style> 
.tab ul li{float:left; margin:0; padding:5px;} 
</style> 
 
<!-- <script type="text/javascript" src="http://code.jquery.com/jquery-1.8.3.js"></script> -->

<style type="text/css">
 ul.tabs {
    margin: 0;
    padding: 0;
    float: left;
    list-style: none;
    height: 32px;
    border-bottom: 1px solid #C9C9C9;
    border-left: 1px solid #C9C9C9;
    width: 100%;
    font-family:"dotum";
    font-size:12px;
}
ul.tabs li {
    float: left;
    text-align:center;
    cursor: pointer;
    width:220px;
    height: 31px;
    line-height: 31px;
    border: 1px solid #C9C9C9;
    border-left: none;
    font-size:15px;
    font-weight: bold;
    background: #F3F3F3;
    overflow: hidden;
    position: relative;
}
ul.tabs li.active {
    background: #FFFFFF;
    border-bottom: 1px solid #FFFFFF;
}
.tab_container {
    border: 0px solid #C9C9C9;
    border-top: none;
    clear: both;
    float: left;
    width: 752px;
    background: #FFFFFF;
}
.tab_content {
    padding: 5px;
    font-size: 12px;
    /* display: none; */
}
.tab_container .tab_content ul {
    width:100%;
    margin:0px;
    padding:0px;
}
.tab_container .tab_content ul li {
    padding:5px;
    /* list-style:none */
}
;
 #container {
    width: 249px;
    margin: 0 auto;
}
</style>
 
 <script type='text/javascript'>
//<![CDATA[
$(window).load(function(){
	$(function () {
		
	   
	    //$(".tab_content:first").show();
	    changeTab('0');
	
	    /*
	    $("ul.tabs li").click(function () {
	        $("ul.tabs li").removeClass("active").css("color", "#333");
	        //$(this).addClass("active").css({"color": "darkred","font-weight": "bolder"});
	        $(this).addClass("active").css("color", "darkred");
	        $(".tab_content").hide()
	        var activeTab = $(this).attr("rel");
	        $("#" + activeTab).fadeIn()
	    }); */
	});
});//]]> 

</script>

<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>
<script type="text/javascript"> 
function showTabMenu(n){ 
var conId; 
for(var i=1;i < 4; i++){ 
		conId = document.getElementById("con"+i);
		if(i==n){ 
			conId.style.display = ""; 
		}else{ 
			conId.style.display = "none"; 
		} 
	} 
} 

function changeTab(idx) {
	$('.mtab3 li').eq(idx).find('a').addClass('on');
	$('.mtab3 li').eq(idx).siblings().find('a').removeClass('on');
	//stduyWrap2
	$('.stduyWrap3').eq(idx).show();
	$('.stduyWrap3').not(':eq(' + idx + ')').hide();
}
</script>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
  <fieldset>
    <legend>평가 종류별 배점</legend>
    <%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
    
    
    <div id="container">
	    
	    <!-- tab -->
		<div class="conwrap3" style="float: right; width: 100%;">
			<ul class="mtab3">				
				<li class="end"><a href="#" onclick="changeTab('0');return false;" class="on" style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden;">60시간 이상</a></li>
				<li class="end"><a href="#" onclick="changeTab('1');return false;" style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden;">60시간 미만</a></li>
				<li class="end"><a href="#" onclick="changeTab('2');return false;" style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden;">학부모연수</a></li>
			</ul>
		</div>
		
		<div class="tab_container">		
			<div class="stduyWrap3" style="width: 100%; float: right;">
				<!-- #tab1 s-->
		        <div id="tab1" class="tab_content">
		            <div id="con1">
				    <div class="sub_txt_1depth">
				      <h4>평가방법 및 배점</h4>
				      <ul>
				        <li class="frist">
				          <div class="courseList">          
				            
				            <table width="100%" summary="순, 평가방법, 평가배점, 평가시기로 구성">
				            <caption>평가방법 및 배점</caption>
				              <colgroup>
				                <col width="15%" />
				                <col width="35%" />
				                <col width="15%" />
				                <col width="35%" />
				              </colgroup>
				              <thead>
				                <tr>
				                  <th scope="col">순</th>
				                  <th scope="col">평가방법</th>
				                  <th scope="col">평가배점</th>
				                  <th scope="col" scope="col">평가시기</th>
				                </tr>
				              </thead>
				              <tbody>
				                <tr>
				                  <td>1</td>
				                  <td>온라인평가(20문항)</td>
				                  <td>20점</td>
				                  <td class="last">진도율이 90% 이상 시 응시 가능</td>
				                </tr>
				                <tr>
				                  <td>2</td>
				                  <td>과제평가(1문제)</td>
				                  <td>20점</td>
				                  <td class="last">3~4주차</td>
				                </tr>
				                <tr>
				                  <td>3</td>
				                  <td>출석평가(50문항)</td>
				                  <td>60점</td>
				                  <td class="last">4주차 토요일</td>			                  
				                </tr>
				                <tr>			                  
				                  <td colspan="2">평가별 점수 합계</td>
				                  <td>100점</td>
				                  <td class="last"></td>
				                </tr>
				              </tbody>
				            </table>
				          </div>
				        </li>
				      </ul>
				      <div>
				      	<table style="border:1px solid #bababa;width:100%;"  >
				      		<tr>			                  
				                  <td style="padding:5px 5px 5px 5px;" >평가방법별 점수를 합산하여 100점 만점으로 원점수를 산출(단 원점수가 60점 이상 취득해야 수료됨)한 후  ‘연수성적분포 조견표’에 의하여 80~100점의 상대점수 부여
		</br>·(동점자 처리) 최종 성적 산출시 동점자가 생길 경우 출석평가, 온라인 과제, 온라인 평가, 참여도의 순으로 높은 점수에 대하여 우선 순위 결정</td>
				                </tr>
				      	</table>
				      	
				      	
				      </div>
				    </div>
				    <div class="sub_txt_1depth">
				    	<h4>원격연수 이수 기준</h4>			      
				        <!-- <li class="frist">직무연수로 개설된 연수의 이수기준은 다음과 같고, ‘연수이수증’을 발급한다.</li> -->
				        <li class="frist">다음의 두 가지 조건을 충족하는 연수생에게 ‘이수(수료)증’을 발급한다.</li>
				        <li class="frist">① 학습진도율이 90%이상</br>
												   ② 연수성적이 60점 이상</br>
						 </li>						   	
				    </div>    
				        
				   <!--  <div class="sub_txt_1depth">
				      <h4>참여도 평가</h4>
				      <ul>
				        <li class="frist">학습진도율 측정</li>
				        <li class="frist">
				          <div class="courseList">
				            <table width="100%" summary="진도율,100%,90%,80%,70%,60%,50%,40%,30%,20%,10%,0%로 구성">
				            <caption>참여도평가</caption>
				              <colgroup>
				                <col width="10%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				              </colgroup>
				              <thead>
				                <tr>
				                  <th scope="col">진도율</th>
				                  <th scope="col">100%</th>
				                  <th scope="col">90%</th>
				                  <th scope="col">80%</th>
				                  <th scope="col">70%</th>
				                  <th scope="col">60%</th>
				                  <th scope="col">50%</th>
				                  <th scope="col">40%</th>
				                  <th scope="col">30%</th>
				                  <th scope="col">20%</th>
				                  <th scope="col">10%</th>
				                  <th class="last" scope="col">0%</th>
				                </tr>
				              </thead>
				              <tbody>
				                <tr>
				                  <td>점수</td>
				                  <td colspan="2">이수</td>
				                  <td colspan="9" class="last">미이수</td>
				                </tr>
				              </tbody>
				            </table>
				          </div>
				        </li>
				      </ul>
				    </div> -->
				    
				    <div class="sub_txt_1depth">
				      <h4>온라인 과제평가(20점)</h4>
				      <ul>
				        <li class="frist">-3가지 주제 중 하나를 선택·작성하여 제출</li>
				        <li class="frist">-과제물 분량 : A4용지 1쪽 이상(11point, 신명조, 줄간격 160)</li>
				        <li class="frist">-감점 요인</li>
				        <li class="frist">
				          <div class="courseList">
				            <table width="100%" summary="내용 감점, 제출기한 감점 기준으로 구성">
				            <caption>온라인 과제평가</caption>
				              <colgroup>
				                <col width="40%" />
				                <col width="60%" />
				              </colgroup>
				              <thead>
				                <tr>
				                  <th scope="col">내용 감점 기준</th>
				                  <th class="last" scope="col">제출기한 감점 기준</th>
				                </tr>
				              </thead>
				              <tbody>
				                <tr>
				                  <td class="left last">다음 항목 부족시 각 항목별 1점 감점(최대 4점까지 감점)</br>
									- 적절한 분량</br>
									- 주제의 일치성</br>
									- 내용의 적합성</br>
									- 내용의 참신성
								  </td>
				                  <td>1일 지연시 1점 감점<br />
				                    (최대 2점까지 감점)</td>
				                </tr>
				              </tbody>
				            </table>
				          </div>
				          
				       </li>
				      </ul>
				    </div>
				    
				    <div class="sub_txt_1depth">
				      <h4>온라인 평가(20점)</h4>
				      <ul>
				        <li class="frist">-진도율이 90% 이상 시 응시 가능</li>
				        <li class="frist">-객관식 4지 선다형 출제</li>
				        <li class="frist">-문항 수 : 20문항</li>
				      </ul>
				    </div>
				    <div class="sub_txt_1depth">
				      <h4>출석 평가(60점)</h4>
				      <ul>
				        <li class="frist">- 객관식 4지 선다형 출제</li>
				        <li class="frist">- 문항 수 : 50문항</li>
				        <li class="frist">- 평가일 : 연수 4주차 토요일</li>
				        <li class="frist">- 평가장소 : 출석평가 장소는 연수인원 증감과 평가 장소의 사정에 따라 별도로 정함.</li>
				      </ul>
				    </div>
				    <div class="sub_txt_1depth">
				      <h4>평가 방법별 시기</h4>
				      <ul>
				        
				        <li class="frist">
				          <div class="courseList">
				            <table width="100%" summary="제출기간, 내용으로 구성">
				            <caption>평가 방법별 시기</caption>
				              <colgroup>
				                <col width="10%" />
				                <col width="20%" />
				                <col width="30%" />
				                <col width="10%" />
				                <col width="20%" />
				                <col width="10%" />
				              </colgroup>
				              <thead>
				                <tr>
				                  <th scope="col">평가방법</th>
				                  <th scope="col">온라인 과제평가</th>
				                  <th scope="col">온라인 평가</th>
				                  <th scope="col">참여도</th>
				                  <th scope="col">출석평가(지필고사)</th>
				                  <th class="last" scope="col">비고</th>
				                </tr>
				              </thead>
				              <tbody>
				                <tr>
				                  <td>평가시기</td>
				                  <td>3~4 주차</td>
				                  <td>진도율이 90% 이상 시 응시 가능</td>
				                  <td>전체</td>
				                  <td>4주차 토요일</td>
				                  <td class="left last"></td>
				                </tr>
				              </tbody>
				            </table>
				          </div>        
				       </li>
				      </ul>
				    </div>
				   
				    <!-- <div class="sub_txt_1depth">
				      <h4>최종 성적 산출의 기본원칙은 다음과 같다.</h4>
				      <ul>
				        <li class="frist">평가방법별 평가 점수를 합산하여 100점 만점으로 종합점수를 산출(60점 이상 취득해야 수료됨)한 후 동일 과정 수료생 모두가 포함된 집단 내에서 서열 백분율을 산출, ‘연수성적분포 조견표’에 의하여 80~100점의 상대점수 부여</li>
				        <li class="frist">동점자 처리:  최종 성적 산출시 동점자가 생길 경우 다음의 순위에 따라 결정함</li>
				        <li class="frist">
				        	<div class="courseList">
					        	<table width="100%" summary="1순위, 2순위, 3순위, 4순위로 구성">
					        	<caption>동점자 처리 기준</caption>
					        		<colgroup>
						                <col width="25%" />
						                <col width="25%" />
						                <col width="25%" />
						                <col width="25%" />
					              	</colgroup>
					        		<thead>
					        		<tr>
					        			<th scope="col">1순위</th>
					        			<th scope="col">2순위</th>
					        			<th scope="col">3순위</th>
					        			<th class="last" scope="col">4순위</th>
					        		</tr>
					        		</thead>
					        		<tbody>
					        		<tr>
					        			<td align="center">출석 평가</td>
					        			<td align="center">온라인 과제평가</td>
					        			<td align="center">온라인 평가</td>        			
					        			<td class="last" align="center">참여도 평가</td>
					        		</tr>
					        		</tbody>
					        	</table>
				        	</div>
				        </li>
				        <li class="frist">성적 조회 및 이의 접수: 홈페이지를 통하여 최종 성적을 개별적으로 조회할 수 있음</li>
				        <li class="frist">이의 처리 후 10일 이내에 최종 성적과 교육연수 이수증은 원격교육연수원 홈페이지에서 출력 가능함</li>
				        <li class="frist">최종 성적 산출을 위한 연수성적분포 조견표</li>
				        <li class="frist">
				        	<div class="courseList">
				        		<table width="100%" summary="계급, 백분율(%)로 구성">
				        		<caption>연수성적분포 조견표</caption>
				        			<tr>
					        			<th scope="col">계급</th>
					        			<td>100</td>
					        			<td>99</td>
					        			<td>98</td>
					        			<td>97</td>
					        			<td>96</td>
					        			<td>95</td>
					        			<td>94</td>
					        			<td>93</td>
					        			<td>92</td>
					        			<td>91</td>
					        			<td class="last" >90</td>
				        			</tr>
				        			<tr>
					        			<th scope="col">백분율(%)</th>
					        			<td>2.0</td>
					        			<td>2.6</td>
					        			<td>3.2</td>
					        			<td>3.8</td>
					        			<td>4.5</td>
					        			<td>5.1</td>
					        			<td>5.7</td>
					        			<td>6.2</td>
					        			<td>6.6</td>
					        			<td>6.8</td>
					        			<td class="last" >7.0</td>
				        			</tr>
				        			<tr>
					        			<th scope="col">계급</th>
					        			<td>90</td>
					        			<td>89</td>
					        			<td>88</td>
					        			<td>87</td>
					        			<td>86</td>
					        			<td>85</td>
					        			<td>84</td>
					        			<td>83</td>
					        			<td>82</td>
					        			<td>81</td>
					        			<td class="last" >80</td>
				        			</tr>
				        			<tr>
					        			<th scope="col">백분율(%)</th>
					        			<td>7.0</td>
					        			<td>6.8</td>
					        			<td>6.6</td>
					        			<td>6.2</td>
					        			<td>5.7</td>
					        			<td>5.1</td>
					        			<td>4.5</td>
					        			<td>3.8</td>
					        			<td>3.2</td>
					        			<td>2.6</td>
					        			<td class="last" >2.0</td>
				        			</tr>
				        		</table>
				        	</div>
				        </li>
				      </ul>
				    </div> -->
				    				    
				    </div>
		        </div>
		        <!-- #tab1 e-->
			</div>
			
			<div class="stduyWrap3" style="width: 100%; float: right;">
				<!-- #tab2 s-->
		        <div id="tab2" class="tab_content">
		        	<!-- 45시간 -->
				    <!-- <div id="con2" style="display:none"> -->
				    <div id="con2">
				    <div class="sub_txt_1depth">
				      <h4>평가방법 및 배점</h4>
				      <ul>
				        <li class="frist">
				          <div class="courseList">				            
				            <table width="100%" summary="순, 평가방법, 평가배점, 평가시기로 구성">
				            <caption>평가방법 및 배점</caption>
				              <colgroup>
				                <col width="20%" />
				                <col width="20%" />
				                <col width="10%" />
				                <col width="50%" />
				              </colgroup>
				              <thead>
				                <tr>
				                  <th scope="col">순</th>
				                  <th scope="col">평가방법</th>
				                  <th scope="col">평가배점</th>
				                  <th class="last" scope="col">평가시기</th>
				                </tr>
				              </thead>
				              <tbody>
				                <tr>
				                  <td>1</td>
				                  <td>온라인평가(20문항)</td>
				                  <td>100점</td>
				                  <td class="last left">진도율이 90% 이상 시 응시 가능</td>
				                </tr>
				                <tr>			                  
				                  <td colspan="2">평가별 점수 합계</td>
				                  <td>100점</td>
				                  <td class="last"></td>
				                </tr>
				              </tbody>
				            </table>
				            
				          </div>
				        </li>
				      </ul>
				    </div>
				    
				    <div class="sub_txt_1depth">
				    	<h4>원격연수 이수 기준</h4>			      
				        <!-- <li class="frist">직무연수로 개설된 연수의 이수기준은 다음과 같고, ‘연수이수증’을 발급한다.</li> -->
				        <li class="frist">다음의 두 가지 조건을 충족하는 연수생에게 ‘이수(수료)증’을 발급한다.</li>
				        <li class="frist">① 학습진도율이 90%이상</br>
												   ② 연수성적이 60점 이상</br>
						 </li>						   	
				    </div>			    
				    				    
				    <!-- <div class="sub_txt_1depth">
				      <h4>참여도 평가</h4>
				      <ul>
				        <li class="frist">학습진도율 측정</li>
				        <li class="frist">
				          <div class="courseList">
				            <table width="100%" summary="진도율,100%,90%,80%,70%,60%,50%,40%,30%,20%,10%,0%로 구성">
				            <caption>참여도평가</caption>
				              <colgroup>
				                <col width="10%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				              </colgroup>
				              <thead>
				                <tr>
				                  <th scope="col">진도율</th>
				                  <th scope="col">100%</th>
				                  <th scope="col">90%</th>
				                  <th scope="col">80%</th>
				                  <th scope="col">70%</th>
				                  <th scope="col">60%</th>
				                  <th scope="col">50%</th>
				                  <th scope="col">40%</th>
				                  <th scope="col">30%</th>
				                  <th scope="col">20%</th>
				                  <th scope="col">10%</th>
				                  <th class="last" scope="col">0%</th>
				                </tr>
				              </thead>
				              <tbody>
				                <tr>
				                  <td>점수</td>
				                  <td colspan="2">이수</td>
				                  <td colspan="9" class="last">미이수</td>
				                </tr>
				              </tbody>
				            </table>
				          </div>
				        </li>
				      </ul>
				    </div> -->
				    
				    <div class="sub_txt_1depth">
				      <h4>온라인 평가(100점)</h4>
				      <ul>
				      	<li class="frist">- 진도율이 90% 이상 시 응시 가능</li>
				        <li class="frist">- 객관식 4지 선다형 출제</li>
				        <li class="frist">- 문항 수 : 20문항</li>
				      </ul>
				    </div>
				    
				    <div class="sub_txt_1depth">
				      <h4>평가 방법별 시기</h4>
				      <ul>
				        
				        <li class="frist">
				          <div class="courseList">
				            <table width="100%" summary="제출기간, 내용으로 구성">
				            <caption>평가 방법별 시기</caption>
				              <colgroup>                
				                <col width="30%" />			                
				                <col width="30%" />
				                <col width="30%" />
				                <col width="10%" />
				              </colgroup>
				              <thead>
				                <tr>
				                  <th scope="col">평가방법</th>			                
				                  <th scope="col">온라인 평가</th>
				                  <th scope="col">참여도</th>                  
				                  <th class="last" scope="col">비고</th>
				                </tr>
				              </thead>
				              <tbody>
				                <tr>
				                  <td>평가시기</td>			                
				                  <td>진도율이 90% 이상 시 응시 가능</td>
				                  <td>전체</td>                  
				                  <td class="left last"></td>
				                </tr>
				              </tbody>
				            </table>
				          </div>        
				       </li>
				      </ul>
				    </div>			    
				    </div>
		        </div>
		        <!-- #tab2 e-->
			</div>
			<div class="stduyWrap3" style="width: 100%; float: right;">
				<!-- #tab3 s-->
		        <div id="tab3" class="tab_content">
		        	<!-- 30시간 -->				    
				    <div id="con3">  
				    <div class="sub_txt_1depth">				      
				      <h4>평가 미실시</h4>
				      </br>※ 학부모 및 일반인에게 장애학생 이해 및 특수교육 관련 지식 함양을 위해 개설되는 연수는 학습진도율을 수료의 기준으로 하고, 별도의 연수성적은 산출하지 않음
				    </div>   
				    <div class="sub_txt_1depth">
				    	<h4>원격연수 연수이수 기준</h4>			      
				        <li class="frist">직무연수 미인정으로 개설된 연수의 수료기준은 다음과 같고, ‘연수수료증’을 발급한다.</li>
				        <li class="frist">① 학습진도율이 90%이상</br>
	①의 연수 수료기준에 만족하는 자</li>
				    </div>  
				     
				     <!-- <div class="sub_txt_1depth">
				      <h4>참여도 평가</h4>
				      <ul>
				        <li class="frist">학습진도율 측정</li>
				        <li class="frist">
				          <div class="courseList">
				            <table width="100%" summary="진도율,100%,90%,80%,70%,60%,50%,40%,30%,20%,10%,0%로 구성">
				            <caption>참여도평가</caption>
				              <colgroup>
				                <col width="10%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				                <col width="7%" />
				              </colgroup>
				              <thead>
				                <tr>
				                  <th scope="col">진도율</th>
				                  <th scope="col">100%</th>
				                  <th scope="col">90%</th>
				                  <th scope="col">80%</th>
				                  <th scope="col">70%</th>
				                  <th scope="col">60%</th>
				                  <th scope="col">50%</th>
				                  <th scope="col">40%</th>
				                  <th scope="col">30%</th>
				                  <th scope="col">20%</th>
				                  <th scope="col">10%</th>
				                  <th class="last" scope="col">0%</th>
				                </tr>
				              </thead>
				              <tbody>
				                <tr>
				                  <td>점수</td>
				                  <td colspan="2">이수</td>
				                  <td colspan="9" class="last">미이수</td>
				                </tr>
				              </tbody>
				            </table>
				          </div>
				        </li>
				      </ul>
				    </div> -->
				    
				    </div>
		        </div>
		        <!-- #tab3 e-->
			</div>
		
		</div>
		<!-- //tab_container -->
	        
	    </div>
	    <!-- container -->
 
    
    
    
    
  </fieldset>
</form>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
