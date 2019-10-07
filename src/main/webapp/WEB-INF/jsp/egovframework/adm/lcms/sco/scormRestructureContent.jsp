<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	opener.doPageList();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>콘텐츠 재구성</h2>
		</div>
		<!-- contents -->
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
			<input type="hidden" name="subj" id="subj" value="${subj}" />
			
			<input type="hidden" name="move_val" id="move_val"/>
			
			
			<input type="hidden" name="manifest" id="manifest" value=""/>
			<input type="hidden" name="organizations" id="organizations" value=""/>
			<input type="hidden" name="itemYn" id="itemYn" value=""/>
			<input type="hidden" name="organizationsItemYn" id="organizationsItemYn" value=""/>
			<input type="hidden" name="depthfull" id="depthfull" value=""/>
			<input type="hidden" name="selectVal" id="selectVal" value=""/>
			<input type="hidden" name="rsrcYn" id="rsrcYn" value=""/>
			<input type="hidden" name="seqYn" id="seqYn" value=""/>
			
			<input type="hidden" name="addItemId" id="addItemId" value=""/>
			<input type="hidden" name="oldItemId" id="oldItemId" value=""/>
			
			<input type="hidden" name="attributeId" id="attributeId" value=""/>
			<input type="hidden" name="attributeNm" id="attributeNm" value=""/>
			<input type="hidden" name="type" id="type" value=""/>
			
			<div style="padding:20px;" align="center">
				<table cellpadding="0" cellspacing="0" border="1" style="width:800px;height:500px;">
					<tr>
						<td width="49%" valign="top">
							<div class="tbList">
							<table summary="신규차시" id="newOrganization" style="width:100%">
								<caption>신규차시</caption>
								<colgroup>
					                <col width="100%" />
					            </colgroup>
					            <thead>
									<tr>
										<th>
											<div class="list">
												<div class="btnR"><a href="#" class="btn01" onclick="javascript:doMakeContent()"><span>완료</span></a></div>
												<div class="btnR"><a href="#" class="btn01" onclick="javascript:doImsView()"><span>xml확인</span></a></div>
												<div class="btnR"><a href="#" class="btn01" onclick="javascript:doEditAttribute()"><span>수정</span></a></div>
												<div class="btnR"><a href="#" class="btn01" onclick="javascript:doRemoveItem()"><span>삭제</span></a></div>
												<!-- 차후 추가 드래그앤드롭 기능추가필요 -->
												<!-- div class="btnR"><a href="#" class="btn01" onclick="javascript:doMakeItem()"><span>추가</span></a></div -->
											</div>
										</th>
									</tr>
								</thead>
					            <tbody>
					            	<tr>
					            		<td style="height:500px;" class="left" valign="top">
					            		<div style="width: 380px; overflow: auto; height: 500px; background-color: #f5f9fd;" id="rpresult">
					            			<div id="tempList" style="width:100%">
					            				<div id="textVal" title="" style="width:100%" 
					            				     onmouseover="javascript:doMouseOver(this.id)"
					            				     onmouseout="javascript:doMouseOut(this.id)"
					            				     onclick="javascript:doSelectVal(this.id, this, this.title)"
					            				     ></div>
					            			</div>
					            			<div id="addList" style="width:420px"></div>
					            		</div>
					            		</td>
					            	</tr>
					            </tbody>
					        </table>
					        </div>
						</td>
						<td width="2%"></td>
						<td valign="top">
							<div class="tbList">
							<table class="tbl" summary="메타리스트" id="list" style="width:100%">
								<caption>메타리스트</caption>
								<colgroup>
					                <col width="100%" />
					            </colgroup>
					            <thead>
									<tr>
										<th style="padding-left:5px;">
												<select id="selectTitle" name="selectTitle">
													<option value="NM">교과명</option>
													<option value="CD" <c:if test="${selectTitle == 'CD'}">selected</c:if>>교과코드</option>
												</select>
											<input type="text" id="selectText" name="selectText" value="${selectText}" class="ipt" onfocus="this.select()" style="width:60%;ime-mode:active;" onkeypress="javascript:doKeySearch()"/>
											<a href="#none" class="btn_search" onclick="javascript:doSearchRestCrs()"><span>검색</span></a>
										</th>
									</tr>
								</thead>
					            <tbody>
					            	<tr>
					            		<td style="height:500px;" class="left" valign="top">
					            			<div style="width: 380px; overflow: auto; height: 500px; background-color: #f5f9fd;" id="rpresult">
						            			<div id="tempSearchList" >
						            				<div id="textVal" title="" style="cursor:pointer;" 
						            				     onmouseover="javascript:doMouseOver(this.id)"
						            				     onmouseout="javascript:doMouseOut(this.id)"
						            				     onclick="javascript:doDetailSubj(this.id)"></div>
						            			</div>
						            			<div id="tempDetailList" >
						            				<div id="textVal" title="" ></div>
						            			</div>
					            				<div id="searchList" style="width:97%"></div>
					            			</div>
					            		</td>
					            	</tr>
					            </tbody>
					        </table>
					        </div>
						</td>
					</tr>
				</table>
			</div>
		</form>
		<iframe id="hiddenFrame" name="hiddenFrame" src="" width="0" height="0" ></iframe>
		<!-- // contents -->
		<!-- button -->
		<ul class="btnCen">			
			<li><a href="#" class="btn01" onClick="javascript:window.close();"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">
	var xmlDoc;
	window.focus();
	doSelectBrowser();

	var previous = "";
	var tid;
	var lv = 1;
    var dropStatus = false;

	var tempSubj = "";
	var arr = new Array();

	var tree1 	= "<img src='/lcms/images/icon/line_tree01.gif' width='20' height='20' align='absmiddle'/>";
	var tree2 	= "<img src='/lcms/images/icon/line_tree02.gif' width='20' height='20' align='absmiddle'/>";
	var tree3 	= "<img src='/lcms/images/icon/line_tree03.gif' width='20' height='20' align='absmiddle'/>";
	var tree4 	= "<img src='/lcms/images/icon/line_tree04.gif' width='20' height='20' align='absmiddle'/>";
	var file_s 	= "<img src='/lcms/images/icon/icon_sDoc.gif' width='18' height='14' align='absmiddle'/>";
	var file 	= "<img src='/lcms/images/icon/icon_Doc.gif' width='18' height='14' align='absmiddle'/>";
	
	var folder_s	= "<img src='/lcms/images/icon/o_icon_sFolder_open.gif' width='20' height='20' align='absmiddle'/>";
	var folder	 	= "<img src='/lcms/images/icon/o_icon_folder_open.gif' width='20' height='20' align='absmiddle'/>";

	var ajaxType = "";
	function finishAjax( result ){
		if( ajaxType == "A" ){
			doReturnpage();
		}else if( ajaxType == "B" || ajaxType == "C" ){
			doSelectBrowser();
		}else if( ajaxType == "D" ){
			doSetCrsList(result);
		}else if( ajaxType == "E" ){
			doSetDetailCrsList(result);
		}
	}

	function doSelectBrowser(){
		if (window.ActiveXObject){
			importXML2();				
		}else if(window.XMLHttpRequest){
			xmlDoc = CtlExecutor.importXML("GET", "<c:out value="${manifestPath}"/>/imsmanifest.xml", false);							
			body_Onload();
		}
	}

	function callbackfunc(){
		if (xmlhttp != null && xmlhttp.readyState == 4) {
			xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
			xmlDoc.loadXML(xmlhttp.responseText);
			body_Onload();
		}	
	}
	
	function importXML(method, _url, async){
		if (window.XMLHttpRequest) {
			xmlhttp = new XMLHttpRequest();
			if(xmlhttp.overrideMimeType){                
				xmlhttp.overrideMimeType("text/xml");
			}
		}
		else if (window.ActiveXObject) {
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}

	    xmlhttp.open(method, _url, async);
	    xmlhttp.onreadystatechange=callbackfunc;//function(){callbackfunc}
	    xmlhttp.send();
	}

	function importXML2(){
		xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.onreadystatechange = function () {
			if (xmlDoc.readyState == 4){
				body_Onload();
			}
		};
		try{	
			xmlDoc.load("<c:out value="${gsDomainContext}"/>/lcms/proxy.jsp?path="+"<c:out value="${manifestPath}"/>/imsmanifest.xml");
		}catch(e){
			alert(e);
		} 
	}

	function body_Onload(){
		//alert("body_Onload");
		var stylesheet = xmlDoc; //document.XMLDocument;

		//Tr 생성갯수 변수
		var TrCount = 0;

		var frm = eval('document.<c:out value="${gsPopForm}"/>');

		//각노드의 ID값 가져오기
		var manifestNode = stylesheet.documentElement;
		var manifest_id = manifestNode.getAttribute("identifier");
		var orgsNode = manifestNode.selectSingleNode("organizations");
		var organizations = orgsNode.getAttribute("default");
		var organization = orgsNode.selectNodes("organization");

		frm.manifest.value = manifest_id;
		if( organizations != null ){
			frm.organizations.value = organizations;
		}

		if(organization.length > 0){
			//차시가 있을경우 ItemYN을 YES로 설정 하위에 아이템이 없을경우에 NO로 변경 (차시만 존재하고 아이템이 없을경우 완료가 불가능하게 하기 위함)
			frm.itemYn.value = "YES";
		}


		//추가될 로우의 임시DIV
		//var div = frm.tempList.innerHTML;
		var div = document.getElementById("tempList").innerHTML;
		//frm.addList.innerHTML("");
		var add_list = document.getElementById("addList");
		add_list.innerHTML = "";

		for( var i=0; i<organization.length; i++ ){
			//alert(organization[i].getAttribute("identifier"));
			//상위 노드의 ID값으로 해당노의의 Title을 가져온다.
			var organ_val = organization[i].getAttribute("identifier");
			var title = organization[i].selectSingleNode("title/text()").data;
			//alert("차시명 : "+title);
			
			add_list.innerHTML += div;
			var child = this.getChildTarget(add_list, "textVal");
			child.innerHTML = folder + title;
			child.setAttribute("title", "ORG");
			child.style.paddingLeft = "10px";
			child.id = organ_val;

			

			//시퀀싱 존재 여부에 따른 이미지 변환
			var organization_imsss = organization[i].selectNodes("imsss:sequencing");
			if( organization_imsss.length >= 1 ){
				//alert("seqYn : YES");
				frm.seqYn.value = "YES";
			}else{
				//alert("seqYn : NO");
				frm.seqYn.value = "NO";
			}

			// 상위 노드의 ID값을 가진 하위 노드 리스트 가져오기
			var item = organization[i].selectNodes("item");
			if( item.length < 1 ){
				//차시가 있을경우 ItemYN을 YES로 설정 하위에 아이템이 없을경우에 NO로 변경 (차시만 존재하고 아이템이 없을경우 완료가 불가능하게 하기 위함)
				frm.itemYn.value = "NO";
			}
			frm.organizationsItemYn.value = "N";
			this.doAppendDiv(item, 1, organ_val, 1, 0);
			
		}


		if( previous != "" ){
			document.getElementById(previous).style.backgroundColor = "#D2D2D2";
		}

		
	}

	function getChildTarget( parent, searchId ){
		var result;
		for( var i=0; i<parent.childNodes.length; i++ ){
			if( parent.childNodes[i].id == searchId ) result = parent.childNodes[i]; 
		}
		return result;
	}


	function doAppendDiv( item, depth , val1, num, tmp){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		var gubun = "ORG";
		arr[depth-1] = item.length;
		for( var idx=0; idx<item.length; idx++ ){
			//해당 노드의 ID/Title 값
			var item_val = item[idx].getAttribute("identifier");
			var orgcd 	 = item[idx].getAttribute("before_org_cd");
			var item_seq = item[idx].getAttribute("before_item_seq");
			var itemnm   = item[idx].selectSingleNode("title/text()").data;
			
			if( itemnm == "NewItem"	){
				frm.organizationsItemYn.value = "Y";
			}
			
			//시퀀싱 존재여부
			var item_imsss = item[idx].selectNodes("imsss:sequencing");
			// Depth LV2의 존재여부를 확인하기 위해 하위 리스트 조회
			var item_lv2 = item[idx].selectNodes("item");

			div = document.getElementById("tempList").innerHTML;
			var add_list = document.getElementById("addList");
			add_list.innerHTML += div;
			
			var htm = "";

			if( depth > 1 ){
				gubun = "ITM";
				for( var i=0; i<depth-1; i++ ){
					if( arr[i] == num ){ 
						htm += tree4;
					}else{
						htm += tree3;
					}
				}
			}
			if( item.length == idx+1 ){
				htm += tree1;
			}else{
				htm += tree2;
			}
			if( item_lv2.length < 1 ){ // 하위노드가 존재할 경우 폴더 이미지 그렇지 않을경우 파일 이미지
				if( item_imsss.length >= 1 ){ //시퀀싱 존재여부
					htm += file_s;
				}else{
					htm += file;
				}
			
				if( itemnm == "NewItem" ){
					itemnm = "<font size='2' color='#666666'>"+itemnm+"</font>";
				}else{
					itemnm = "<font size='2' color='blue'><b>"+itemnm+"</b></font>";
				}

				var rsrc_val = item[idx].getAttribute("rsrc_seq");
				if( rsrc_val == null || rsrc_val == "" ){
					frm.rsrcYn.value = "NO";
				}else{
					frm.rsrcYn.value = "YES";
				}
				var child = this.getChildTarget(add_list, "textVal");
				child.setAttribute("drop", "YES");
				
			}else{
				if( item_imsss.length >= 1 ){ //시권싱 존재여부
					htm += folder_s;
				}else{
					htm += folder;
				}
				itemnm = "<font size='2' color='#666666'>"+itemnm+"</font>";
			}

			var child = this.getChildTarget(add_list, "textVal");
			child.innerHTML = htm + itemnm;
			child.setAttribute("title", "ITM");
			child.style.paddingLeft = "10px";
			child.id = item_val;

			if( item_lv2.length > 0 ){
				//하위노드가 존재할 경우 재귀함수 호출
				this.doAppendDiv(item_lv2, depth + lv, item_val, (idx+1), (tmp+idx+1));
			}
		}
	}

	//마우스 오버 이벤트
	function doMouseOver( objId ){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		var obj = document.getElementById(objId);
		obj.style.backgroundColor = "#D2E1FF";
		if( dropStatus ){
			if( obj.getAttribute("title") == "ITM" && obj.getAttribute("drop") == "YES"){
				doSelectVal(objId, obj, "ITM" );
				frm.addItemId.value = objId;
				doRestuctureItem();
			}else{
				dropStatus = false;
			}
		}
	}

	//마우스 아웃 이벤트
	function doMouseOut( objId ){
		var obj = document.getElementById(objId);
		if( previous != objId ){ 
			obj.style.backgroundColor = "#F5F9FD";
		}else{
			obj.style.backgroundColor = "#D2D2D2";
		}
	}

	// 아이템 선택이벤트
	function doSelectVal( idValue, obj, gubun ){ 
		//alert(idValue + " : " + obj + " : " +gubun);
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.selectVal.value = gubun+"#"+idValue;
		
		if( previous != "" ){
			document.getElementById(previous).style.backgroundColor = "#F5F9FD";
			previous = idValue;
		}
		document.getElementById(idValue).style.backgroundColor = "#D2D2D2";
		previous = idValue;
	}

	function doRestuctureItem(){
		dropStatus = false;
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		var url = "<c:out value="${gsDomainContext}" />/com/aja/lcm/courseRsrcUpdate.do";
		var param = "?addItemId="+frm.addItemId.value+"&oldItemId="+frm.oldItemId.value;
		frm.addItemId.value = "";
		frm.oldItemId.value = "";
		ajaxType = "B";
		goGetURL(url, param);
	}

	// 아이템 추가
	function doMakeItem(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.depthfull.value == "FULL" ){
			alert("더이상 하위단계의 아이템 추가는 불가능 합니다.");
			frm.depthfull.value = "NO";
			return;
		}
		if( frm.selectVal.value == "" ){
			alert("아이템이 추가될 위치를 지정하세요.");
			return;
		}
		frm.organizationsItemYn.value = "true";

		var url = "<c:out value="${gsDomainContext}" />/com/aja/lcm/addItem.do";
		var param = "?selectVal="+frm.selectVal.value;
		frm.addItemId.value = "";
		frm.oldItemId.value = "";
		ajaxType = "A";
		goGetURL(url, param);
	}

	function doReturnpage(){
		tid = setTimeout("doSelectBrowser()",500);
	}

	function doRemoveItem(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		var result = false;
		if( frm.selectVal.value == "" || previous == "" ){
			alert("삭제할 곳을 선택하여 주세요.");
			return;
		}
		if( confirm("삭제 하시겠습니까?") ){

			
			var url = "<c:out value="${gsDomainContext}" />/com/aja/lcm/removeItem.do";
			var id = frm.selectVal.value.split("#");
			if( previous == id[1] ){
				previous = "";
			}
			var param = "?type="+id[0]+"&itemId="+id[1];
			ajaxType = "C";
			goGetURL(url, param);

		}
	}

	// 수정
	function doEditAttribute(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.selectVal.value == "" || previous == ""){
			alert("수정할 곳을 선택하여 주세요.");
			return;
		}
		var id = frm.selectVal.value.split("#");
		frm.type.value = id[0];
		frm.attributeId.value = id[1];
		frm.attributeNm.value = document.getElementById(id[1]).innerText;
		
		window.open('', 'editAttribute', 'top=100px, left=100px, height=200px, width=650px, scrolls=auto');
		frm.target = "editAttribute";
		frm.action = "/adm/lcms/sco/editAttributePopup.do";
		frm.submit();
	}

	// 메니페스트파일 확인
	function doImsView(){
		window.open("<c:out value="${gsDomainContext}"/>/lcms/manifest/<c:out value="${userid}"/>/imsmanifest.xml","","fullscreen,scrollbars");
	}



	/**==============================================================================================================================================**/
	
	
	// 과정조회
	function doSearchRestCrs(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.selectText.value == "" ){
			var text = frm.selectTitle.value == "NM" ? "과정명을" : "과정코드를";
			alert(text + " 입력하세요.");
			frm.selectText.focus();
			return;
		}

		var url = "<c:out value="${gsDomainContext}" />/com/aja/lcm/searchRestCrs.do";
		var param = "?selectTitle="+frm.selectTitle.value+"&selectText="+frm.selectText.value;
		ajaxType = "D";
		goGetURL(url, param);
	}

	function doSetCrsList( result ){
		var div;
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		var search = document.getElementById("searchList");
		search.innerHTML = "";
		for( var i=0; i<result.length; i++ ){
			div = document.getElementById("tempSearchList").innerHTML;
			search.innerHTML += div;
			var child = this.getChildTarget(search, "textVal");
			child.innerHTML = "["+result[i].get("subj")+"] "+result[i].get("subjnm");
			child.setAttribute("title", result[i].get("subjnm"));
			child.style.paddingLeft = "10px";
			child.id = result[i].get("subj");
		}
	}

	// 과정상세 ( 컨텐츠 아이템 ) 조회
	function doDetailSubj( subj ){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		tempSubj = subj;
		var url = "<c:out value="${gsDomainContext}" />/com/aja/lcm/selectDetailContentList.do";
		var param = "?subj="+subj;
		ajaxType = "E";
		goGetURL(url, param);
	}

	function doSetDetailCrsList( result ){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		var div;
		var oldOrg = "";
		var idx = 0;
		var search = document.getElementById("searchList");
		search.innerHTML = "";

		for( var i=0; i<result.length; i++ ){
			var p_orgSeq = result[i].get("orgSeq");
			var p_orgTitle = result[i].get("orgTitle");
			var p_orgId = result[i].get("orgId");
			var p_rsrcSeq = result[i].get("rsrcSeq");
			var p_highItemSeq = result[i].get("highItemSeq");
			var p_imsss = result[i].get("imsss");
			var p_itemTitle = result[i].get("itemTitle");
			var p_itemId = result[i].get("itemId");
			var htm = "";
			if( oldOrg != p_orgSeq ){
				var str = "";
				if( oldOrg != "" ){
					str = "<br>";
				}
				var radio = "<input type='radio' name='selectOrg' id='selectOrg' onclick='javascript:doRestuctureOrg("+p_orgSeq+");'>";
				div = document.getElementById("tempDetailList").innerHTML;
				search.innerHTML += div;
				var child = this.getChildTarget(search, "textVal");
				child.innerHTML = str + radio + folder + p_orgTitle;
				child.setAttribute("title", p_orgTitle);
				child.style.paddingLeft = "10px";
				child.id = p_orgId;
				oldOrg = p_orgSeq;
				idx = 1;
			}else{
				idx++;
			}
			div = document.getElementById("tempDetailList").innerHTML;
			search.innerHTML += div;
			var child = this.getChildTarget(search, "textVal");
			child.style.paddingLeft = "40px";
			if( p_highItemSeq != 0 ){
				child.style.paddingLeft = "60px";
			}
			
			if( p_rsrcSeq == 0 ){
				if( p_imsss > 0 ){ //시퀀싱 존재여부
					htm += folder_s;
				}else{
					htm += folder;
				}
			}else{
				if( p_imsss > 0 ){ //시퀀싱 존재여부
					htm += file_s;
				}else{
					htm += file;
				}
			}
			child.innerHTML = htm + p_itemTitle;
			child.setAttribute("title", p_itemId + "@@" + p_rsrcSeq);
			var item = (p_itemId.replace("{", "")).replace("}", "");
			child.id = item;
			if( p_rsrcSeq != 0 ){
				//document.getElementById(item).easydrag();
				//$("#"+item).easydrag();
				//$("#"+item).ondrop(function(e, element){
				//	CtlExecutor.doChangeItem(element.id);
				//	CtlExecutor.doDetailSubj(tempSubj);
				//	});
			}
		} 
	}

	function doRestuctureOrg( orgSeq ){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		var url = "<c:out value="${gsDomainContext}" />/com/aja/lcm/courseRsrcUpdateAll.do";
		var param = "?orgSeq="+orgSeq;
		ajaxType = "B";
		goGetURL(url, param);
	}


	//완료
	function doMakeContent(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.target = "_self";
		frm.action = "/adm/lcms/sco/makeContent.do";
		frm.submit();
	}


	function doKeySearch(){
		if( event.keyCode == 13 ){
			event.returnValue = false;
			doSearchRestCrs();
		}
		return false;
	}

</script>