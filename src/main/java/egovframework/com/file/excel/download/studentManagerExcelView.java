package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class studentManagerExcelView extends AbstractExcelView{
	
	private String nullValue(Object val)
	{
		if(val == null)
            return "";
        else
            return val.toString();
		
	}
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception{
		HSSFCell cell = null;
		
		HSSFSheet sheet = wb.createSheet("StudentManager List");
		sheet.setDefaultColumnWidth((short)12);
		
		// put text in first cell
		cell = getCell(sheet, 0, 0);
		setText(cell, "StudentManager List");
		
		
		Map<String, Object> map = (Map)model.get("studentManagerMap");
		List<Object> list = (List)map.get("list");
		
		int index = 0;
		// set header information
		setText(getCell(sheet, 1, index++), "No"); 
		setText(getCell(sheet, 1, index++), "과정명");
		setText(getCell(sheet, 1, index++), "지역");
		setText(getCell(sheet, 1, index++), "시작일");
		setText(getCell(sheet, 1, index++), "종료일");
		setText(getCell(sheet, 1, index++), "연수기간");
		setText(getCell(sheet, 1, index++), "기수");      
		setText(getCell(sheet, 1, index++), "ID");
		setText(getCell(sheet, 1, index++), "성명");
		setText(getCell(sheet, 1, index++), "학교명/소속근무처");
		setText(getCell(sheet, 1, index++), "전화");
		setText(getCell(sheet, 1, index++), "휴대전화");
		setText(getCell(sheet, 1, index++), "이메일");
		setText(getCell(sheet, 1, index++), "처리상태");
		setText(getCell(sheet, 1, index++), "신청일");
		setText(getCell(sheet, 1, index++), "승인일");
		//setText(getCell(sheet, 1, index++), "환급여부");
		setText(getCell(sheet, 1, index++), "수료여부");
		setText(getCell(sheet, 1, index++), "이메일수신여부");
		setText(getCell(sheet, 1, index++), "SMS수신여부");
		setText(getCell(sheet, 1, index++), "교재배송");
		setText(getCell(sheet, 1, index++), "배송우편번호");
		setText(getCell(sheet, 1, index++), "배송주소");

		
		
		for( int i=0; i<list.size(); i++ ){
			Map mp = (Map)list.get(i);
			
			index = 0;
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, (i + 1) + "");
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjnm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("areaCodenm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("edustart")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("eduend")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("edustart")) + "~" + this.nullValue(mp.get("eduend")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjseq")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("userid")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("name")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("empGubun")).equals("R") ? this.nullValue(mp.get("positionNm")) : this.nullValue(mp.get("userPath")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("hometel")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("handphone")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("email")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("chkfinalnm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("appdate")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("approvaldate")));
			
			/*cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("isgoyong")));*/
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("grdvaluenm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("ismailling")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("issms")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, (this.nullValue(mp.get("hrdc")).equals("C") ? "직장" : "자택"));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, (this.nullValue(mp.get("hrdc")).equals("C") ? this.nullValue(mp.get("zipCd1")) : this.nullValue(mp.get("zipCd"))));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, (this.nullValue(mp.get("hrdc")).equals("C") ? this.nullValue(mp.get("address1")) : this.nullValue(mp.get("address"))));
		}
	}

}
