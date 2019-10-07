package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class stuMemberCountExcelView extends AbstractExcelView{
	
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
		
		HSSFSheet sheet = wb.createSheet("stuMemberCount List");
		sheet.setDefaultColumnWidth((short)12);
		
		// put text in first cell
		cell = getCell(sheet, 0, 0);
		setText(cell, "stuMemberCount List");
		
		
		Map<String, Object> map = (Map)model.get("stuMemberCountMap");
		List<Object> list = (List)map.get("list");
		
		int index = 0;
		// set header information
		setText(getCell(sheet, 1, index++), "No"); 
		setText(getCell(sheet, 1, index++), "교육기수");
		setText(getCell(sheet, 1, index++), "과정명");
		setText(getCell(sheet, 1, index++), "교육구분");
		setText(getCell(sheet, 1, index++), "기수");      
		setText(getCell(sheet, 1, index++), "신청시작일");
		setText(getCell(sheet, 1, index++), "신청종료일");
		setText(getCell(sheet, 1, index++), "교육시작일");
		setText(getCell(sheet, 1, index++), "교육종료일");
		setText(getCell(sheet, 1, index++), "정원");
		setText(getCell(sheet, 1, index++), "신청");
		setText(getCell(sheet, 1, index++), "취소/반려");
		setText(getCell(sheet, 1, index++), "학습예정");
		setText(getCell(sheet, 1, index++), "학습진행(미수료)");
		setText(getCell(sheet, 1, index++), "학습완료(수료)");

		
		
		for( int i=0; i<list.size(); i++ ){
			Map mp = (Map)list.get(i);
			
			index = 0;
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, (i+1)+"");
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("grseqnm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjnm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("isonoff")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjseq")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("propstart")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("propend")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("edustart")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("eduend")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("studentlimit")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("procnt")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("cancnt")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("proycnt")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("stucnt")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("comcnt")));
			
		}
	}

}
