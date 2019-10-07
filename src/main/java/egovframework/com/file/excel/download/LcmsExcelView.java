package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class LcmsExcelView extends AbstractExcelView{
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception{
		HSSFCell cell = null;
		
		HSSFSheet sheet = wb.createSheet("Lcms List");
		sheet.setDefaultColumnWidth((short)12);
		
		Map<String, Object> map = (Map)model.get("lcmsMap");
		List<Object> list = (List)map.get("list");
		String contentType = (String)map.get("contentType");
		if( map.get("contentType") != null ){
			if( contentType.equals("C") ){
				for( int i=0; i<list.size(); i++ ){
					Map lcms = (Map)list.get(i);
					
					cell = getCell(sheet, i, 0);
					setText(cell, lcms.get("module").toString());
					
					cell = getCell(sheet, i, 1);
					setText(cell, lcms.get("owner").toString());
					
					cell = getCell(sheet, i, 2);
					setText(cell, lcms.get("lessonCd").toString());
					
					cell = getCell(sheet, i, 3);
					setText(cell, lcms.get("depth").toString());
					
					cell = getCell(sheet, i, 4);
					setText(cell, lcms.get("orderNum").toString());
					
					cell = getCell(sheet, i, 5);
					setText(cell, lcms.get("lessonName").toString());
					
					cell = getCell(sheet, i, 6);
					setText(cell, lcms.get("progressYn").toString());
					
					cell = getCell(sheet, i, 7);
					setText(cell, lcms.get("starting").toString());
					
					cell = getCell(sheet, i, 8);
					setText(cell, lcms.get("pageCount").toString());
				}
			}else{
				
				for( int i=0; i<list.size(); i++){
					Map lcms = (Map)list.get(i);
					cell = getCell(sheet, i, 0);
					setText(cell, (String)lcms.get("module"));
					
					cell = getCell(sheet, i, 1);
					setText(cell, (String)lcms.get("moduleName"));
					
					cell = getCell(sheet, i, 2);
					setText(cell, (String)lcms.get("lesson"));
					
					cell = getCell(sheet, i, 3);
					setText(cell, (String)lcms.get("lessonName"));
					
					cell = getCell(sheet, i, 4);
					setText(cell, (String)lcms.get("starting"));
				}
			}
		}
	}

}
