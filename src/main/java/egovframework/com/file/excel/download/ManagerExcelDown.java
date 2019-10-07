package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ziaan.scorm.DateUtil;

public class ManagerExcelDown extends AbstractExcelView{
	
	private String nullValue(Object val)
	{
		if(val == null)
            return "";
        else
            return val.toString();
		
	}
	
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception{
				
		String fileName = "manager_log.xls";
		fileName = new String(fileName.getBytes("euc-kr"), "8859_1");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		HSSFCellStyle cellStyle = wb.createCellStyle();           
        //cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);   
        //cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);   
        //cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);   
        //cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);		
        cellStyle.setAlignment(cellStyle.ALIGN_CENTER);  //가운데 정렬
     
      
		HSSFCell cell = null;
		
		HSSFSheet sheet = wb.createSheet("운영자 로그");
		sheet.setDefaultColumnWidth((short)12);
		
		HSSFRow row = sheet.createRow(0);	
		
		for(int i=0; i<8; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
		} 		
		sheet.addMergedRegion (new Region(( int) 0 , ( short )0 , ( int) 0, (short )7 ));
		cell = row.getCell(0); cell.setCellValue(new HSSFRichTextString("운영자 로그"));
          
        //cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);   
        //cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);   
        //cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);   
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);       		
        
		row = sheet.createRow(1);
		for(int i=0; i<8; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
		}
		
		Map<String, Object> map = (Map)model.get("managerLogMap");
		List<Object> list = (List)map.get("list");
		
		int index = 0;
		// set header information

		setText(getCell(sheet, 1, index++), "NO"); 
		setText(getCell(sheet, 1, index++), "ID");
		setText(getCell(sheet, 1, index++), "성명");
		setText(getCell(sheet, 1, index++), "권한");      
		setText(getCell(sheet, 1, index++), "권한사용기간");
		setText(getCell(sheet, 1, index++), "권한부여자");
		setText(getCell(sheet, 1, index++), "구분");
		setText(getCell(sheet, 1, index++), "날짜");

		
		for( int i=0; i<list.size(); i++ ){
			Map mp = (Map)list.get(i);
			
			index = 0;
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("num")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("userid")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("name")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("gadminnm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("tmon")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("lname")));
						
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("logmode")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("ldate")));
			
		}
	}

}
