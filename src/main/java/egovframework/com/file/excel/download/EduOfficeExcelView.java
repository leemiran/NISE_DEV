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

import egovframework.com.tag.CustomFnTag;

public class EduOfficeExcelView extends AbstractExcelView{
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> map = (Map)model.get("map");
		
		String year = String.valueOf(map.get("ses_search_gyear"));
		String month = String.valueOf(map.get("ses_search_gmonth"));
		
		String fileName = DateUtil.toString("yyyyMMddhhmmss") + "_" + year + "년_" + month + "월" + "_EduOffice.xls";
		fileName = new String(fileName.getBytes("euc-kr"), "8859_1");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");	
		
		HSSFCellStyle cellStyle = wb.createCellStyle();           
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);   
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);   
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);   
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);   
		
		HSSFCell cell = null;
		
		HSSFSheet sheet = wb.createSheet("eduOffice");
		sheet.setDefaultColumnWidth((short)12);
		
		HSSFRow row = sheet.createRow(0);
		for(int i=0; i<41; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
		}
		
		cell = row.getCell(0); cell.setCellValue(new HSSFRichTextString("연수기간"));
		cell = row.getCell(2); cell.setCellValue(new HSSFRichTextString("30시간 미만과정"));
		
		String underText = year;
		String overText = year;
		if("03".equals(month)) {
			underText+=".3.8(수) ~ 3.21(화)";
			overText+=".3.8(수) ~ 3.28(화)";
		} else if("04".equals(month)) {
			underText+=".4.5(수) ~ 4.18(화)";
			overText+=".4.5(수) ~ 4.25(화)";
		} else if("05".equals(month)) {
			underText+=".5.10(수) ~ 5.23(화)";
			overText+=".5.10(수) ~ 5.30(화)";
		} else if("06".equals(month)) {
			underText+=".6.7(수) ~ 6.20(화)";
			overText+=".6.7(수) ~ 6.27(화)";
		} else if("07".equals(month)) {
			underText+=".7.5(수) ~ 7.18(화)";
			overText+=".7.5(수) ~ 7.25(화)";
		} else if("08".equals(month)) {
			underText+=".8.9(수) ~ 8.22(화)";
		} else if("09".equals(month)) {
			overText+=".8.9(수) ~ 8.29(화)";
			underText+=".9.6(수) ~ 9.19(화)";
			overText+=".9.6(수) ~ 9.26(화)";
		} else if("10".equals(month)) {
			underText+=".10.11(수) ~ 10.24(화)";
			overText+=".10.11(수) ~ 10.31(화)";
		} else if("11".equals(month)) {
			underText+=".11.8(수) ~ 11.21(화)";
			overText+=".11.8(수) ~ 11.28(화)";
		}
		cell = row.getCell(8); cell.setCellValue(new HSSFRichTextString(underText));
		
		
		cell = row.getCell(38); cell.setCellValue(new HSSFRichTextString("총합계"));
		
		row = sheet. createRow(1);
		for(int i=0; i<41; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
		}
		
		cell = row.getCell(2); cell.setCellValue(new HSSFRichTextString("30시간 이상과정"));
		cell = row.getCell(8); cell.setCellValue(new HSSFRichTextString(overText));
		
		row = sheet. createRow(2);
		for(int i=0; i<41; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
		}
		
		cell = row.getCell(0); cell.setCellValue(new HSSFRichTextString("연수구분"));
		cell = row.getCell(1); cell.setCellValue(new HSSFRichTextString("시도 교육청명"));
		cell = row.getCell(2); cell.setCellValue(new HSSFRichTextString("서울"));
		cell = row.getCell(4); cell.setCellValue(new HSSFRichTextString("부산"));
		cell = row.getCell(6); cell.setCellValue(new HSSFRichTextString("대구"));
		cell = row.getCell(8); cell.setCellValue(new HSSFRichTextString("인천"));
		cell = row.getCell(10); cell.setCellValue(new HSSFRichTextString("광주"));
		cell = row.getCell(12); cell.setCellValue(new HSSFRichTextString("대전"));
		cell = row.getCell(14); cell.setCellValue(new HSSFRichTextString("울산"));		
		cell = row.getCell(16); cell.setCellValue(new HSSFRichTextString("경기"));
		cell = row.getCell(18); cell.setCellValue(new HSSFRichTextString("강원"));
		cell = row.getCell(20); cell.setCellValue(new HSSFRichTextString("충북"));		
		cell = row.getCell(22); cell.setCellValue(new HSSFRichTextString("세종"));		
		cell = row.getCell(24); cell.setCellValue(new HSSFRichTextString("충남"));
		cell = row.getCell(26); cell.setCellValue(new HSSFRichTextString("전북"));
		cell = row.getCell(28); cell.setCellValue(new HSSFRichTextString("전남"));
		cell = row.getCell(30); cell.setCellValue(new HSSFRichTextString("경북"));
		cell = row.getCell(32); cell.setCellValue(new HSSFRichTextString("경남"));
		cell = row.getCell(34); cell.setCellValue(new HSSFRichTextString("제주"));
		cell = row.getCell(36); cell.setCellValue(new HSSFRichTextString("기타"));
		
		row = sheet. createRow(3);
		for(int i=0; i<41; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
		}
		
		cell = row.getCell(1); cell.setCellValue(new HSSFRichTextString("과정명 / 수강 및 이수현황"));
		cell = row.getCell(2); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(3); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(4); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(5); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(6); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(7); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(8); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(9); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(10); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(11); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(12); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(13); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(14); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(15); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(16); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(17); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(18); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(19); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(20); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(21); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(22); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(23); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(24); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(25); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(26); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(27); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(28); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(29); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(30); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(31); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(32); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(33); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(34); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(35); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(36); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(37); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(38); cell.setCellValue(new HSSFRichTextString("수강"));
		cell = row.getCell(39); cell.setCellValue(new HSSFRichTextString("이수"));
		cell = row.getCell(40); cell.setCellValue(new HSSFRichTextString("이수율"));

		List<Object> list = (List)map.get("eduOffice");
		if( list != null && list.size() > 0 ){
			for( int i=0; i<list.size(); i++){
				Map lcms = (Map)list.get(i);
				
				row = sheet.createRow(i+4);
				for(int j=0; j<41; j++) {
					cell = row.createCell(j);
					cell.setCellStyle(cellStyle);
				}
				
				String groupingUpperclass = String.valueOf(lcms.get("groupingUpperclass"));
				String groupingSubj = String.valueOf(lcms.get("groupingSubj"));
				
				if("0".equals(groupingUpperclass) && "0".equals(groupingSubj)) {
					cell = getCell(sheet, i+4, 0);
					setText(cell, String.valueOf(lcms.get("classname")));
					
					
					cell = getCell(sheet, i+4, 1);
					setText(cell, String.valueOf(lcms.get("subjnm")));
					
				} else if("0".equals(groupingUpperclass) && "1".equals(groupingSubj)) {
					cell = getCell(sheet, i+4, 0);
					setText(cell, String.valueOf(lcms.get("classname")));
					
					
					cell = getCell(sheet, i+4, 1);
					setText(cell, "계");
					
				} else if("1".equals(groupingUpperclass) && "1".equals(groupingSubj)) {
					cell = getCell(sheet, i+4, 0);
					setText(cell, "총합계");
					
					sheet.addMergedRegion (new Region(( int) i+4 , ( short )0 , ( int) i+4, (short )1 ));
				}
				
				cell = getCell(sheet, i+4, 2);
				setText(cell, String.valueOf(lcms.get("seoul")));
				
				
				cell = getCell(sheet, i+4, 3);
				setText(cell, String.valueOf(lcms.get("seoulIsuY")));
				
				
				cell = getCell(sheet, i+4, 4);
				setText(cell, String.valueOf(lcms.get("busan")));
				
				
				cell = getCell(sheet, i+4, 5);
				setText(cell, String.valueOf(lcms.get("busanIsuY")));
				
				
				cell = getCell(sheet, i+4, 6);
				setText(cell, String.valueOf(lcms.get("daegu")));
				
				
				cell = getCell(sheet, i+4, 7);
				setText(cell, String.valueOf(lcms.get("daeguIsuY")));
				
				
				cell = getCell(sheet, i+4, 8);
				setText(cell, String.valueOf(lcms.get("incheon")));
				
				
				cell = getCell(sheet, i+4, 9);
				setText(cell, String.valueOf(lcms.get("incheonIsuY")));
				
				
				cell = getCell(sheet, i+4, 10);
				setText(cell, String.valueOf(lcms.get("gwangju")));
				
				
				cell = getCell(sheet, i+4, 11);
				setText(cell, String.valueOf(lcms.get("gwangjuIsuY")));
				
				
				cell = getCell(sheet, i+4, 12);
				setText(cell, String.valueOf(lcms.get("daejeon")));
				
				
				cell = getCell(sheet, i+4, 13);
				setText(cell, String.valueOf(lcms.get("daejeonIsuY")));
				
				
				cell = getCell(sheet, i+4, 14);
				setText(cell, String.valueOf(lcms.get("ulsan")));
				
				
				cell = getCell(sheet, i+4, 15);
				setText(cell, String.valueOf(lcms.get("ulsanIsuY")));				
				
				
				cell = getCell(sheet, i+4, 16);
				setText(cell, String.valueOf(lcms.get("gyeonggi")));
				
				
				cell = getCell(sheet, i+4, 17);
				setText(cell, String.valueOf(lcms.get("gyeonggiIsuY")));
				
				
				cell = getCell(sheet, i+4, 18);
				setText(cell, String.valueOf(lcms.get("gangwon")));
				
				
				cell = getCell(sheet, i+4, 19);
				setText(cell, String.valueOf(lcms.get("gangwonIsuY")));
				
				
				cell = getCell(sheet, i+4, 20);
				setText(cell, String.valueOf(lcms.get("chungbuk")));
				
				
				cell = getCell(sheet, i+4, 21);
				setText(cell, String.valueOf(lcms.get("chungbukIsuY")));
				
				
				cell = getCell(sheet, i+4, 22);
				setText(cell, String.valueOf(lcms.get("sejong")));
				
				
				cell = getCell(sheet, i+4, 23);
				setText(cell, String.valueOf(lcms.get("sejongIsuY")));
				
				
				cell = getCell(sheet, i+4, 24);
				setText(cell, String.valueOf(lcms.get("chungnam")));
				
				
				cell = getCell(sheet, i+4, 25);
				setText(cell, String.valueOf(lcms.get("chungnamIsuY")));
				
				
				cell = getCell(sheet, i+4, 26);
				setText(cell, String.valueOf(lcms.get("jeonbuk")));
				
				
				cell = getCell(sheet, i+4, 27);
				setText(cell, String.valueOf(lcms.get("jeonbukIsuY")));
				
				
				cell = getCell(sheet, i+4, 28);
				setText(cell, String.valueOf(lcms.get("jeonnam")));
				
				
				cell = getCell(sheet, i+4, 29);
				setText(cell, String.valueOf(lcms.get("jeonnamIsuY")));
				
				
				cell = getCell(sheet, i+4, 30);
				setText(cell, String.valueOf(lcms.get("gyeongbuk")));
				
				
				cell = getCell(sheet, i+4, 31);
				setText(cell, String.valueOf(lcms.get("gyeongbukIsuY")));
				
				
				cell = getCell(sheet, i+4, 32);
				setText(cell, String.valueOf(lcms.get("gyeongnam")));
				
				
				cell = getCell(sheet, i+4, 33);
				setText(cell, String.valueOf(lcms.get("gyeongnamIsuY")));
				
				
				cell = getCell(sheet, i+4, 34);
				setText(cell, String.valueOf(lcms.get("jeju")));
				
				
				cell = getCell(sheet, i+4, 35);
				setText(cell, String.valueOf(lcms.get("jejuIsuY")));
				
				
				cell = getCell(sheet, i+4, 36);
				setText(cell, String.valueOf(lcms.get("edu")));
				
				
				cell = getCell(sheet, i+4, 37);
				setText(cell, String.valueOf(lcms.get("eduIsuY")));
				
				
				cell = getCell(sheet, i+4, 38);
				setText(cell, String.valueOf(lcms.get("studentCnt")));
				
				
				cell = getCell(sheet, i+4, 39);
				setText(cell, String.valueOf(lcms.get("studentCntIsuY")));
				
				
				cell = getCell(sheet, i+4, 40);
				setText(cell, String.valueOf(CustomFnTag.toPercent(lcms.get("studentCntIsuY"), lcms.get("studentCnt"))));
				
			}
		}
		
		sheet.addMergedRegion (new Region(( int) 0 , ( short )0 , ( int) 1, (short )1 ));
		sheet.addMergedRegion (new Region(( int) 0 , ( short )2 , ( int) 0, (short )7 ));
		sheet.addMergedRegion (new Region(( int) 0 , ( short )8 , ( int) 0, (short )37 ));
		sheet.addMergedRegion (new Region(( int) 0 , ( short )38 , ( int) 2, (short )40 ));
		sheet.addMergedRegion (new Region(( int) 1 , ( short )2 , ( int) 1, (short )7 ));
		sheet.addMergedRegion (new Region(( int) 1 , ( short )8 , ( int) 1, (short )37 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )0 , ( int) 3, (short )0 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )2 , ( int) 2, (short )3 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )4 , ( int) 2, (short )5 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )6 , ( int) 2, (short )7 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )8 , ( int) 2, (short )9 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )10 , ( int) 2, (short )11 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )12 , ( int) 2, (short )13 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )14 , ( int) 2, (short )15 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )16 , ( int) 2, (short )17 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )18 , ( int) 2, (short )19 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )20 , ( int) 2, (short )21 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )22 , ( int) 2, (short )23 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )24 , ( int) 2, (short )25 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )26 , ( int) 2, (short )27 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )28 , ( int) 2, (short )29 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )30 , ( int) 2, (short )31 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )32 , ( int) 2, (short )33 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )34 , ( int) 2, (short )35 ));
		sheet.addMergedRegion (new Region(( int) 2 , ( short )36 , ( int) 2, (short )37 ));
	}

}
