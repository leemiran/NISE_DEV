package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class selectCpBookExcelView extends AbstractExcelView{
	
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
		
		HSSFSheet sheet = wb.createSheet("cpBook List");
		sheet.setDefaultColumnWidth((short)12);
		
		// put text in first cell
		cell = getCell(sheet, 0, 0);
		setText(cell, "cpBook List");
		
		
		Map<String, Object> map = (Map)model.get("cpBookMap");
		List<Object> list = (List)map.get("list");
		
		int index = 0;
		// set header information
		setText(getCell(sheet, 1, index++), "NO"); 
		setText(getCell(sheet, 1, index++), "과정명");
		setText(getCell(sheet, 1, index++), "학습기간");
		setText(getCell(sheet, 1, index++), "아이디");      
		setText(getCell(sheet, 1, index++), "성명");
		setText(getCell(sheet, 1, index++), "배송상태");
		setText(getCell(sheet, 1, index++), "택배사");
		setText(getCell(sheet, 1, index++), "송장번호");
		setText(getCell(sheet, 1, index++), "배송일");

		
		for( int i=0; i<list.size(); i++ ){
			Map mp = (Map)list.get(i);
			
			index = 0;
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("rnum")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjnm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("edustart"))+"~"+this.nullValue(mp.get("eduend")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("userid")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("name")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("deliveryStatus")));
						
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("deliveryComp")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("deliveryNumber")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("deliveryDate")));
			
		}
	}

}
