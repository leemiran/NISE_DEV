package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class propCancelMemberExcelView extends AbstractExcelView{
	
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
		
		HSSFSheet sheet = wb.createSheet("propCancelMember List");
		sheet.setDefaultColumnWidth((short)12);
		
		// put text in first cell
		cell = getCell(sheet, 0, 0);
		setText(cell, "propCancelMember List");
		
		
		Map<String, Object> map = (Map)model.get("propCancelMemberMap");
		List<Object> list = (List)map.get("list");
		
		int index = 0;
		// set header information
		setText(getCell(sheet, 1, index++), "No"); 
		setText(getCell(sheet, 1, index++), "교육기수");
		setText(getCell(sheet, 1, index++), "과정명");
		setText(getCell(sheet, 1, index++), "지역");
		setText(getCell(sheet, 1, index++), "교육구분");
		setText(getCell(sheet, 1, index++), "기수");      
		setText(getCell(sheet, 1, index++), "ID");
		setText(getCell(sheet, 1, index++), "성명");
		setText(getCell(sheet, 1, index++), "이메일");
		setText(getCell(sheet, 1, index++), "취소일");
		setText(getCell(sheet, 1, index++), "취소구분");
		setText(getCell(sheet, 1, index++), "취소사유");
		setText(getCell(sheet, 1, index++), "결제수단");
		setText(getCell(sheet, 1, index++), "금액");
		setText(getCell(sheet, 1, index++), "입금여부");
		setText(getCell(sheet, 1, index++), "환불여부");
		setText(getCell(sheet, 1, index++), "환불일시");
		setText(getCell(sheet, 1, index++), "이메일수신여부");
		setText(getCell(sheet, 1, index++), "SMS수신여부");

		
		
		for( int i=0; i<list.size(); i++ ){
			Map mp = (Map)list.get(i);
			
			index = 0;
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, (i + 1) + "");
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("grseqnm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjnm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("areaCodenm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("isonoff")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjseqgr")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("userid")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("name")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("email")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("canceldate")));
			
			
			
			cell = getCell(sheet, 2+i, index++);
			if(mp.get("cancelkind").equals("P"))
				setText(cell, "본인취소");
			else if(mp.get("cancelkind").equals("F"))
				setText(cell, "운영자반려");
			else if(mp.get("cancelkind").equals("D"))
				setText(cell, "운영자삭제");
			else
				setText(cell, "-");
				
			
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("reason")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("paynm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("amount")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("enterYn")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("repayYn")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("repayDt")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("ismailling")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("issms")));
			
		}
	}

}
