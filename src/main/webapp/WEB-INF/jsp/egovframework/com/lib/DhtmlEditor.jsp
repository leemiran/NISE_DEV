<!-- START :: 한글깨짐현상 해결 ----------------------------------------------
  -- Updated Contents : include되면서 한글 깨짐현상을 해결.(tomcat 5.5)
  -- Updated Date     : 2008.09.22
  -- Updated Author   : ChungHyun
  ------------------------------------------------------------------------>
<%@ page pageEncoding="utf-8"%>
<!--// END :: 한글깨짐현상 해결 ---------------------------------------------->
<%-- 
	제  목    : Dhtml 에디터
	파일명    : DhtmlEditor.jsp
	작성일    : 2004.11.16
	작성자    : 이연주
	설  명    : 문서를 편집하는 에디터(전체 공용)	
--%> 
<style>
table.line td{border:0px;}
</style>
<TABLE cellpadding="0" cellspacing="0" border="0" class="line">
	<tr>
		<td><INPUT type="radio" checked value="Y" name="use_editor" onclick="changeEditor()" style="border:0;">편집기사용</td>
		<td><INPUT type="radio" value="N" name="use_editor" onclick="changeEditor()" style="border:0;">HTML 직접입력</td>
	</tr>
</TABLE>
<script language='JavaScript' src='/script/editor.js'></script>
<script for="window" event="onload()">return OnDocumentComplete()</script>
 	<!--이미지입력 테이블-->
<div id = "imageDiv"   style="position:absolute;visibility:hidden;width:373px;height:300px">            
	<table id="imageTable" border="1" cellspacing="0" cellpadding="3" bordercolor="#666666" bgcolor="white" onmousemove="clear_timeout()" onmouseout="start_timeout(this)">
		<tr id="base"> 
			<td bordercolor="F7F7F7" bgcolor="F7F7F7">
			인터넷에 올려진 이미지만 삽입이 가능합니다.<BR>
			삽입할 이미지 주소(url)을 넣어주세요 <br>
			<span style="font-size:8pt">(예:http://iedu.nise.go.kr/a.jpg) * "http://" 꼭 써야함</span> </td>
		</tr>
		<tr id="base"> 
			<td bordercolor="F7F7F7" bgcolor="F7F7F7">
			<input name="IMAGE_URL" type="text" class="input" onfocus="has_focus=true;" onblur="has_focus=false;" value="http://" size="34">
			<input type="button" value="적용" onClick="insert_image(document.all.IMAGE_URL.value);" class="button">
			<input type="button" value="취소" onClick="close_div(document.all.imageDiv)" class="button">
			</td>
		</tr>
	</table>
</div>



<!--URL 테이블-->
<div id = "urlDiv"   style="position:absolute;visibility:hidden;width:300px;height:300px">           
	<table id="urlTable" border="1" cellspacing="0" cellpadding="3" bordercolor="#666666" bgcolor="white" onmousemove="clear_timeout()" onmouseout="start_timeout(this)">
		<tr id="base"> 
			<td bordercolor="F7F7F7" bgcolor="F7F7F7"> 선택된 부분에 걸릴 링크 주소(url)을 넣어주세요. 
			<br>
			<span style="font-size:8pt">(예:http://iedu.nise.go.kr) * "http://" 꼭 	써야함</span>
			</td>
		</tr>
	<tr id="base"> 
		<td bordercolor="F7F7F7" bgcolor="F7F7F7">
		<input name="LINK_URL" type="text" class="input" onfocus="has_focus=true;" onblur="has_focus=false;" value="" size="29">
		<input type="button" value="적용" onClick="patch_url();" class="button">
		<input type="button" value="취소" onClick="close_div(document.all.urlDiv)" class="button">
		</td>
	</tr>
	</table>
</div>
 

 
<!--COLOR 테이블-->
<div id = "colorDiv"   style="position:absolute;visibility:hidden;width:300px;height:300px"></div>



<!--TABLE 테이블-->
<div id = "tableDiv"   style="position:absolute;visibility:hidden;width:300px;height:300px">
	<table id="tableTable" border="1" cellspacing="0" cellpadding="3" bordercolor="#666666" bgcolor="white" onmousemove="clear_timeout()" onmouseout="start_timeout(this)">
		<tr id="base">
			<td align="center" bordercolor="F7F7F7" bgcolor="F7F7F7">

			
			<strong>생성할 테이블의 정보를 입력하세요.</strong><br><br>
			<strong>행수 :</strong> 
			<input type="text" size=3 name="COL_NUM" value="" onfocus="has_focus=true;" onblur="has_focus=false;" class="input">
			<strong>열수 :</strong> 
			<input type="text" size=3 name="ROW_NUM" value="" onfocus="has_focus=true;" onblur="has_focus=false;" class="input"> 
			<br>
			<strong>선 두께 :</strong> 
			<select name="BORDER_THICK" onfocus="has_focus=true;" onblur="has_focus=false;" class="input" style="font-size:9pt;font-family:돋움;width:85">
			<option value=1>1</option>
			<option value=2>2</option>
			<option value=3>3</OPTION>
			<option value=4>4</option>
			</select>

			</td>
		</tr>
		<tr id="base"> 
			<td align="center" bordercolor="F7F7F7" bgcolor="F7F7F7"> 
			<input type="button" value="적용" onClick="make_table();" class="button"> 
			<input type="button" value="취소" onClick="close_div(document.all.tableDiv)" class="button"> 
			</td>
		</tr>
	</table>
</div>
						
<table height="32" border="0" align="left" cellpadding="0" cellspacing="0" id="toolbar" class="line">
	<tr>
        <td>
            <select id="font" style="font-size:9pt;font-family:돋움;" onchange="command(this)">
            <option selected>글자체</option>
            <option value='굴림체'>굴림체</option>
            <option value='바탕체'>바탕체</option>
            <option value='궁서체'>궁서체</option>
            <option value='돋움체'>돋움체</option>
            <option value='가는 목각체'>목각체</option>
            <option value='휴먼매직체'>매직체</option>
            <option value='HY얕은샘물M'>샘물체</option>
            <option value='HY엽서L'>엽서체</option>                                
            <option value='Arial'>Arial</option>
            <option value='Times'>Times</option>
            <option value='Courier'>Courier</option>
            </select>
            <select id="fontsize" style="font-size:9pt;font-family:돋움;" onchange="command(this)">
            <option selected>크 기</option>
            <option>-----</option>
            <option value="1">8pt</option>
            <option value="2">10pt</option>
            <option value="3">12pt</option>
            <option value="4">14pt</option>
            <option value="5">18pt</option>
            <option value="6">24pt</option>
            <option value="7">36pt</option>
            </select>
        </td>
     
		<td valign="bottom">
			<table height="30" border=0 cellspacing=0 cellpadding=0 onSelectStart="return false" onDragStart="return false">
				<tr>
					<td id="newdoc" title="새문서" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/01.gif' hspace="4"></td>
					<td id="bgcolor" title="배경색" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/02.gif' hspace="4"></td>
					<td><img src='/images/editor/bar.gif' hspace="2"></td>
					<td id="bold" title="굵게" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/03.gif' hspace="6"></td>
					<td id="italic" title="기울이기" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/04.gif' hspace="6"></td>
					 <td id="underline" title="밑줄" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/05.gif' hspace="6"></td>
					<td id="fontcolor" title="글자색" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/06.gif' hspace="6"></td>
					<td id="fontbgcolor" title="글자배경색" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/07.gif' hspace="6"></td>
					
					<td><img src='/images/editor/bar.gif' hspace="2"></td>
					<td id="left" title="왼쪽정렬" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/08.gif' hspace="4"></td>
					<td id="center" title="가운데정렬" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/09.gif' hspace="4"></td>
					<td id="right" title="오른쪽정렬" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/10.gif' hspace="4"></td>
					<td id="numlist" title="숫자기호" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/11.gif' hspace="4"></td>
					<td id="itemlist" title="문자기호" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/12.gif' hspace="4"></td>
					<td id="outdent" title="탭줄이기" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/13.gif' hspace="4"></td>
					<td id="indent" title="탭늘이기" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/14.gif' hspace="4"></td>
					
					<td><img src='/images/editor/bar.gif' hspace="2"></td>
					<td id="table" title="표만들기" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/15.gif' hspace="4"></td>
					<td id="tablebgcolor" title="표배경색" onmouseover="buttonover(this)" onmouseout="buttonout(this)" onmousedown="buttondown(this)" onclick="command(this)"><img src='/images/editor/16.gif' hspace="4"></td>
				</tr>
			</table>
		</td>
	</tr>
</table><br>
<xmp id="edit_div" style="display:none">
	<c:out value="${content}" escapeXml="false"/>	
</xmp>
<div style="display:block; clear:both;">
<textarea name="txtDetail" rows="22" scrollbar="no" style="border:1px solid #333; display:none; FONT-SIZE:9pt;width:<c:out value="${width}"/>px;height:<c:out value="${height}"/>px"></textarea>		
<iframe id="ObjEditor" scrolling="auto" width="<c:out value="${width}"/>px" height="<c:out value="${height}"/>px" style=" border:1; " onbeforedeactivate="deactivate_handler();"></iframe>
</div>
<input name="content" type="hidden" value="">