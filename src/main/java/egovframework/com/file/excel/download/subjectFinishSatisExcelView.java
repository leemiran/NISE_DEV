package egovframework.com.file.excel.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ziaan.library.FormatDate;
import com.ziaan.library.Log;

import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.web.EgovFileDownloadController;

public class subjectFinishSatisExcelView extends AbstractExcelView{
	
	private String nullValue(Object val)
	{
		if(val == null)
            return "";
        else
            return val.toString();
		
	}
	
	public void crateCell(HSSFWorkbook wb, HSSFCell cell, String text, short align)
	{
		cell.setCellValue(text);
		HSSFCellStyle style = wb.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBottomBorderColor(HSSFColor.BLACK.index);

		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setAlignment(align);
		cell.setCellStyle(style);
	}
	
	
	
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Map<String, Object> map = (Map)model.get("subjectFinishSatisMap");
		Map commandMap = (Map)map.get("commandMap");
		
		Map view = (Map)map.get("view");
		
		List<?> passlist = (List)map.get("passlist");
		
		List<?> notlist = (List)map.get("notlist");
		
//		이수자 변수
		int [] sido_cnt = new int [19];
		int tot = passlist.size();
		
		
//		미이수자변수
		int [] nsido_cnt = new int [19];
		int ntot = notlist.size();
		
		String edustart = FormatDate.getFormatDate(view.get("edustart")+"", "yyyy.MM.dd");
		String eduend = FormatDate.getFormatDate(view.get("eduend")+"", "yyyy.MM.dd");
		
		int edutime = Integer.parseInt(view.get("edutimes") + "");
		
		HSSFSheet sheet1 = wb.createSheet("이수현황");
		sheet1.setDefaultColumnWidth((short)12);
//		sheet1.setDefaultRowHeightInPoints((short)35);
		
		HSSFSheet sheet2 = wb.createSheet("이수자");
		sheet2.setDefaultColumnWidth((short)12);
//		sheet2.setDefaultRowHeight((short)25);
		
		HSSFSheet sheet3 = wb.createSheet("미이수자");
		sheet3.setDefaultColumnWidth((short)12);
//		sheet3.setDefaultRowHeight((short)25);
		
		HSSFRow row = null;
		HSSFCell cell = null;
		
		HSSFFont font = wb.createFont();
//		font.setBoldweight((short)12);
//		font.setFontHeight((short)12);
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBottomBorderColor(HSSFColor.BLACK.index);

		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
//		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		
		style.setFont(font);
		
		
		int row_index = 0;
//		이수자 #################################################################################################################
		sheet2.addMergedRegion(new Region(0, (short)0, 0, (short)21)); //10칸 셀병합 
		sheet2.addMergedRegion(new Region(1, (short)0, 1, (short)21)); //10칸 셀병합 
		sheet2.addMergedRegion(new Region(2, (short)0, 2, (short)21)); //10칸 셀병합 
		sheet2.addMergedRegion(new Region(3, (short)0, 3, (short)21)); //10칸 셀병합 
		sheet2.addMergedRegion(new Region(4, (short)0, 4, (short)21)); //10칸 셀병합 
		sheet2.addMergedRegion(new Region(5, (short)0, 5, (short)21)); //10칸 셀병합 
		sheet2.addMergedRegion(new Region(6, (short)0, 6, (short)21)); //10칸 셀병합 
		sheet2.addMergedRegion(new Region(7, (short)0, 7, (short)21)); //10칸 셀병합 
		sheet2.addMergedRegion(new Region(8, (short)0, 8, (short)21)); //10칸 셀병합 
		sheet2.addMergedRegion(new Region(9, (short)0, 9, (short)21)); //10칸 셀병합 
		
		for(int i=0; i<=9; i++)
		{
			for(int j=0; j<=21; j++) 
			{
					cell = getCell(sheet2, i, j);
					cell.setCellStyle(style);
			}
		}
		
		//this.crateCell(wb, getCell(sheet2, 0, 0), "국립특수교육원 부설 원격교육연수원  (연수원 인가번호: 교육과학기술부 04-01)", HSSFCellStyle.ALIGN_CENTER);
		//sheet2.addMergedRegion(new Region(3, (short)0, 3, (short)20));
		cell = getCell(sheet2, row_index, 0);
		setText(cell, "국립특수교육원 부설 원격교육연수원  (연수원 인가번호: 교육과학기술부 04-01)");
		//sheet2.addMergedRegion(new Region(row_index, (short)0, row_index, (short)0));

		cell = getCell(sheet2, ++row_index, 0);
		setText(cell, " 과 정 명 : " +  this.nullValue(view.get("subjnm")));
		//sheet2.addMergedRegion(new Region(row_index, (short)0, row_index, (short)0));


		cell = getCell(sheet2, ++row_index, 0);
		setText(cell, "<연 수 개 요>");


		cell = getCell(sheet2, ++row_index, 0);
		setText(cell, " 연수기간 : " +  this.nullValue(edustart) + " ~ " +  this.nullValue(eduend));
		//cell.setCellStyle(style);


		cell = getCell(sheet2, ++row_index, 0);
		setText(cell, " 이수시간 및 학점 : " +  this.nullValue(view.get("edutimes")) + "시간(" + this.nullValue(view.get("point")) + "학점)");


		cell = getCell(sheet2, ++row_index, 0);
		setText(cell, " 시도교육청 : ");


		cell = getCell(sheet2, ++row_index, 0);
		setText(cell, " 수강인원 : " +  this.nullValue(passlist.size() + notlist.size()) + "명");


		cell = getCell(sheet2, ++row_index, 0);
		setText(cell, " 이수인원 : " +  passlist.size() + "명");


		cell = getCell(sheet2, ++row_index, 0);
		setText(cell, " 미이수인원 : " +  notlist.size() + "명");


		cell = getCell(sheet2, ++row_index, 0);
		setText(cell, " 과정구분: 1. 일반과정, 2. 특별과정(시도교육청 요청과정), 3. 무료과정");


		
		int col_index = 0;
		this.crateCell(wb, getCell(sheet2, ++row_index, col_index++), "연번", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "과정구분", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "납부방법", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "이수구분", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "시도교육청", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "하위교육청", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "소속", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "회원구분", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "성명", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "NEIS개인번호", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "생년월일", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "연수과정명", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "연수기관명", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "연수시작일", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "연수종료일", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "연수구분", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "연수시간", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "연수성적", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "직무관련", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "학점", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "이수번호", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet2, row_index, col_index++), "비고", HSSFCellStyle.ALIGN_CENTER);
		
		
		
		System.out.println("passlist.size() : " + passlist.size());
		for( int i=0; i<passlist.size(); i++ ){
			Map mp = (Map)passlist.get(i);
			
			int index = 0;
			
			String deptNm = this.nullValue(mp.get("deptNm"));
			
			if( deptNm.indexOf("서울") > -1)
			{
				sido_cnt[0] += 1;
			}
			else if( deptNm.indexOf("부산") > -1)
			{
				sido_cnt[1] += 1;
			}
			else if( deptNm.indexOf("대구") > -1)
			{
				sido_cnt[2] += 1;
			}
			else if( deptNm.indexOf("인천") > -1)
			{
				sido_cnt[3] += 1;
			}
			else if( deptNm.indexOf("광주") > -1)
			{
				sido_cnt[4] += 1;
			}
			else if( deptNm.indexOf("대전") > -1)
			{
				sido_cnt[5] += 1;
			}
			else if( deptNm.indexOf("울산") > -1)
			{
				sido_cnt[6] += 1;
			}
			else if( deptNm.indexOf("경기") > -1)
			{
				sido_cnt[7] += 1;
			}
			else if( deptNm.indexOf("강원") > -1)
			{
				sido_cnt[8] += 1;
			}
			else if( deptNm.indexOf("충청북도") > -1)
			{
				sido_cnt[9] += 1;
			}
			else if( deptNm.indexOf("세종") > -1)
			{
				sido_cnt[18] += 1;
			}
			else if( deptNm.indexOf("충청남도") > -1)
			{
				sido_cnt[10] += 1;
			}
			else if( deptNm.indexOf("전라북도") > -1)
			{
				sido_cnt[11] += 1;
			}
			else if( deptNm.indexOf("전라남도") > -1)
			{
				sido_cnt[12] += 1;
			}
			else if( deptNm.indexOf("경상북도") > -1)
			{
				sido_cnt[13] += 1;
			}
			else if( deptNm.indexOf("경상남도") > -1)
			{
				sido_cnt[14] += 1;
			}
			else if( deptNm.indexOf("제주") > -1)
			{
				sido_cnt[15] += 1;
			}
			else if( deptNm.indexOf("국립") > -1)
			{
				sido_cnt[16] += 1;
			}
			else
			{
				sido_cnt[17] += 1;
			}
			
			
			//번호
			cell = getCell(sheet2, ++row_index, index++);
			setText(cell, (i+1)+"");
			cell.setCellStyle(style);
			
			//과정구분
			String ischarge = this.nullValue(mp.get("ischarge"));
			if(ischarge != null && !"".equals(ischarge))
			{
				if(ischarge.equals("C"))
					ischarge = "일반";
				else if(ischarge.equals("S"))
					ischarge = "특별";
				else if(ischarge.equals("F"))
					ischarge = "무료";
				else
					ischarge = "";
			}
			else
			{
				ischarge = "";
			}
			cell = getCell(sheet2, row_index, index++);
			setText(cell, ischarge);
			cell.setCellStyle(style);
			
			//납부방법
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(mp.get("pay")));
			cell.setCellStyle(style);
			
			//이수구분
			cell = getCell(sheet2, row_index, index++);
			setText(cell, "이수");
			cell.setCellStyle(style);
			
			//시도교육청
			cell = getCell(sheet2, row_index, index++);
			setText(cell, deptNm);
			cell.setCellStyle(style);
			
			//하위교육청
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(mp.get("agencyNm")));
			cell.setCellStyle(style);
			//소속
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(mp.get("userPath")));
			cell.setCellStyle(style);
			//회원구분
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(mp.get("empGubunNm")));
			cell.setCellStyle(style);
			//성명
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(mp.get("name")));
			cell.setCellStyle(style);
			//나이스개인번호
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(mp.get("nicePersonalNum")));
			cell.setCellStyle(style);
			//생년월일			
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(mp.get("birthDate")));
			cell.setCellStyle(style);
			//과정명
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(view.get("subjnm")));
			cell.setCellStyle(style);
			//연수기관명
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(mp.get("compnm")));
			cell.setCellStyle(style);
			//교육시작일
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(edustart));
			cell.setCellStyle(style);
			//교육종료일
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(eduend));
			cell.setCellStyle(style);
			
			//연수구분
			String jickmoo = "-";
			if(mp.get("serno") != null && !mp.get("serno").equals("") && mp.get("upperclassnm") != null && !mp.get("upperclassnm").equals("학부모"))
			{
				jickmoo = "직무";
			}
			cell = getCell(sheet2, row_index, index++);
			setText(cell, jickmoo);
			cell.setCellStyle(style);
			
			//연수시간
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(view.get("edutimes")));
			cell.setCellStyle(style);
			
			//연수성적
			cell = getCell(sheet2, row_index, index++);
			if(edutime > 60)
				setText(cell, this.nullValue(mp.get("editscore")));
			else
				setText(cell, this.nullValue(mp.get("score")));
			cell.setCellStyle(style);
			
			//직무관련
			jickmoo = "N";
			if(mp.get("serno") != null && !mp.get("serno").equals("") && mp.get("upperclassnm") != null && !mp.get("upperclassnm").equals("학부모"))
			{
				jickmoo = "Y";
			}
			cell = getCell(sheet2, row_index, index++);
			setText(cell, jickmoo);
			cell.setCellStyle(style);
			//학점
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(view.get("point")));
			cell.setCellStyle(style);
			//이수번호
			String passnumber = "-";
			if(mp.get("serno") != null && !mp.get("serno").equals("") && mp.get("upperclassnm") != null)
			{
				if(!mp.get("upperclassnm").equals("학부모"))
				{
					passnumber = "특수-" + (mp.get("upperclassnm")+"").replaceAll("특별과정", "교원").replaceAll("기타", "") + "직무-" + FormatDate.getFormatDate(view.get("edustart")+"","yyyy") + "-" + Integer.parseInt(mp.get("subjseq")+"") + "-" + mp.get("serno") ;
				}
				else if(mp.get("upperclassnm").equals("학부모"))
				{
					passnumber = "특수-사이버-" + FormatDate.getFormatDate(view.get("edustart")+"","yyyy") + "-" + Integer.parseInt(mp.get("subjseq")+"") + "-" + mp.get("serno");
				}
			}
			cell = getCell(sheet2, row_index, index++);
			setText(cell, passnumber);
			cell.setCellStyle(style);
			//비고
			cell = getCell(sheet2, row_index, index++);
			setText(cell, "");
			cell.setCellStyle(style);
			
			
		}
		
		
		
		
		
		
//		미이수자 #################################################################################################################
		row_index = 0;
		sheet3.addMergedRegion(new Region(0, (short)0, 0, (short)21)); //10칸 셀병합 
		sheet3.addMergedRegion(new Region(1, (short)0, 1, (short)21)); //10칸 셀병합 
		sheet3.addMergedRegion(new Region(2, (short)0, 2, (short)21)); //10칸 셀병합 
		sheet3.addMergedRegion(new Region(3, (short)0, 3, (short)21)); //10칸 셀병합 
		sheet3.addMergedRegion(new Region(4, (short)0, 4, (short)21)); //10칸 셀병합 
		sheet3.addMergedRegion(new Region(5, (short)0, 5, (short)21)); //10칸 셀병합 
		sheet3.addMergedRegion(new Region(6, (short)0, 6, (short)21)); //10칸 셀병합 
		sheet3.addMergedRegion(new Region(7, (short)0, 7, (short)21)); //10칸 셀병합 
		sheet3.addMergedRegion(new Region(8, (short)0, 8, (short)21)); //10칸 셀병합 
		sheet3.addMergedRegion(new Region(9, (short)0, 9, (short)21)); //10칸 셀병합 
		

		for(int i=0; i<=9; i++)
		{
			for(int j=0; j<=21; j++) 
			{
					cell = getCell(sheet3, i, j);
					cell.setCellStyle(style);
			}
		}
		
		//this.crateCell(wb, getCell(sheet3, 0, 0), "국립특수교육원 부설 원격교육연수원  (연수원 인가번호: 교육과학기술부 04-01)", HSSFCellStyle.ALIGN_CENTER);
		//sheet3.addMergedRegion(new Region(3, (short)0, 3, (short)20));
		cell = getCell(sheet3, row_index, 0);
		setText(cell, "국립특수교육원 부설 원격교육연수원  (연수원 인가번호: 교육과학기술부 04-01)");
		
		cell = getCell(sheet3, ++row_index, 0);
		setText(cell, " 과 정 명 : " +  this.nullValue(view.get("subjnm")));


		cell = getCell(sheet3, ++row_index, 0);
		setText(cell, "<연 수 개 요>");


		cell = getCell(sheet3, ++row_index, 0);
		setText(cell, " 연수기간 : " +  this.nullValue(edustart) + " ~ " +  this.nullValue(eduend));


		cell = getCell(sheet3, ++row_index, 0);
		setText(cell, " 이수시간 및 학점 : " +  this.nullValue(view.get("edutimes")) + "시간(" + this.nullValue(view.get("point")) + "학점)");


		
		cell = getCell(sheet3, ++row_index, 0);
		setText(cell, " 시도교육청 : ");

;
		
		cell = getCell(sheet3, ++row_index, 0);
		setText(cell, " 수강인원 : " +  this.nullValue(passlist.size() + notlist.size()) + "명");


		
		cell = getCell(sheet3, ++row_index, 0);
		setText(cell, " 이수인원 : " +  passlist.size() + "명");


		
		cell = getCell(sheet3, ++row_index, 0);
		setText(cell, " 미이수인원 : " +  notlist.size() + "명");


		
		cell = getCell(sheet3, ++row_index, 0);
		setText(cell, " 과정구분: 1. 일반과정, 2. 특별과정(시도교육청 요청과정), 3. 무료과정");


		
		
		col_index = 0;
		this.crateCell(wb, getCell(sheet3, ++row_index, col_index++), "연번", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "과정구분", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "납부방법", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "이수구분", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "시도교육청", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "하위교육청", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "소속", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "회원구분", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "성명", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "NEIS개인번호", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "생년월일", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "연수과정명", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "연수기관명", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "연수시작일", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "연수종료일", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "연수구분", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "연수시간", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "연수성적", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "직무관련", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "학점", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "이수번호", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet3, row_index, col_index++), "비고", HSSFCellStyle.ALIGN_CENTER);
		
		
		
		
		for( int i=0; i<notlist.size(); i++ ){
			Map mp = (Map)notlist.get(i);
			
			int index = 0;
			
			String deptNm = this.nullValue(mp.get("deptNm"));
			
			if( deptNm.indexOf("서울") > -1)
			{
				nsido_cnt[0] += 1;
			}
			else if( deptNm.indexOf("부산") > -1)
			{
				nsido_cnt[1] += 1;
			}
			else if( deptNm.indexOf("대구") > -1)
			{
				nsido_cnt[2] += 1;
			}
			else if( deptNm.indexOf("인천") > -1)
			{
				nsido_cnt[3] += 1;
			}
			else if( deptNm.indexOf("광주") > -1)
			{
				nsido_cnt[4] += 1;
			}
			else if( deptNm.indexOf("대전") > -1)
			{
				nsido_cnt[5] += 1;
			}
			else if( deptNm.indexOf("울산") > -1)
			{
				nsido_cnt[6] += 1;
			}
			else if( deptNm.indexOf("경기") > -1)
			{
				nsido_cnt[7] += 1;
			}
			else if( deptNm.indexOf("강원") > -1)
			{
				nsido_cnt[8] += 1;
			}
			else if( deptNm.indexOf("충청북도") > -1)
			{
				nsido_cnt[9] += 1;
			}
			else if( deptNm.indexOf("세종") > -1)
			{
				nsido_cnt[18] += 1;
			}
			else if( deptNm.indexOf("충청남도") > -1)
			{
				nsido_cnt[10] += 1;
			}
			else if( deptNm.indexOf("전라북도") > -1)
			{
				nsido_cnt[11] += 1;
			}
			else if( deptNm.indexOf("전라남도") > -1)
			{
				nsido_cnt[12] += 1;
			}
			else if( deptNm.indexOf("경상북도") > -1)
			{
				nsido_cnt[13] += 1;
			}
			else if( deptNm.indexOf("경상남도") > -1)
			{
				nsido_cnt[14] += 1;
			}
			else if( deptNm.indexOf("제주") > -1)
			{
				nsido_cnt[15] += 1;
			}
			else if( deptNm.indexOf("국립") > -1)
			{
				nsido_cnt[16] += 1;
			}
			else
			{
				nsido_cnt[17] += 1;
			}
			
			
			//번호
			cell = getCell(sheet3, ++row_index, index++);
			setText(cell, (i+1)+"");
			cell.setCellStyle(style);
			
			//과정구분
			String ischarge = this.nullValue(mp.get("ischarge"));
			if(ischarge != null && !"".equals(ischarge))
			{
				if(ischarge.equals("C"))
					ischarge = "일반";
				else if(ischarge.equals("S"))
					ischarge = "특별";
				else if(ischarge.equals("F"))
					ischarge = "무료";
				else
					ischarge = "";
			}
			else
			{
				ischarge = "";
			}
			cell = getCell(sheet3, row_index, index++);
			setText(cell, ischarge);
			cell.setCellStyle(style);
			
			//납부방법
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(mp.get("pay")));
			cell.setCellStyle(style);
			
			//이수구분
			cell = getCell(sheet3, row_index, index++);
			setText(cell, "미이수");
			cell.setCellStyle(style);
			
			//시도교육청
			cell = getCell(sheet3, row_index, index++);
			setText(cell, deptNm);
			cell.setCellStyle(style);
			
			//하위교육청
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(mp.get("agencyNm")));
			cell.setCellStyle(style);
			//소속
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(mp.get("userPath")));
			cell.setCellStyle(style);
			//회원구분
			cell = getCell(sheet2, row_index, index++);
			setText(cell, this.nullValue(mp.get("empGubunNm")));
			cell.setCellStyle(style);
			//성명
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(mp.get("name")));
			cell.setCellStyle(style);
			//나이스개인번호
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(mp.get("nicePersonalNum")));
			cell.setCellStyle(style);
			//생년월일			
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(mp.get("birthDate")));
			cell.setCellStyle(style);
			//과정명
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(view.get("subjnm")));
			cell.setCellStyle(style);
			//연수기관명
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(mp.get("compnm")));
			cell.setCellStyle(style);
			//교육시작일
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(edustart));
			cell.setCellStyle(style);
			//교육종료일
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(eduend));
			cell.setCellStyle(style);
			
			//연수구분
			String jickmoo = "-";
			if(mp.get("serno") != null && !mp.get("serno").equals("") && mp.get("upperclassnm") != null && !mp.get("upperclassnm").equals("학부모"))
			{
				jickmoo = "직무";
			}
			cell = getCell(sheet3, row_index, index++);
			setText(cell, jickmoo);
			cell.setCellStyle(style);
			
			//연수시간
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(view.get("edutimes")));
			cell.setCellStyle(style);
			
			//연수성적
			cell = getCell(sheet3, row_index, index++);
			if(edutime > 60)
				setText(cell, this.nullValue(mp.get("editscore")));
			else
				setText(cell, this.nullValue(mp.get("score")));
			cell.setCellStyle(style);
			
			//직무관련
			jickmoo = "N";
			if(mp.get("serno") != null && !mp.get("serno").equals("") && mp.get("upperclassnm") != null && !mp.get("upperclassnm").equals("학부모"))
			{
				jickmoo = "Y";
			}
			cell = getCell(sheet3, row_index, index++);
			setText(cell, jickmoo);
			cell.setCellStyle(style);
			//학점
			cell = getCell(sheet3, row_index, index++);
			setText(cell, this.nullValue(view.get("point")));
			cell.setCellStyle(style);
			//이수번호
			String passnumber = "-";
			if(mp.get("serno") != null && !mp.get("serno").equals("") && mp.get("upperclassnm") != null)
			{
				if(!mp.get("upperclassnm").equals("학부모"))
				{
					passnumber = "특수-" + (mp.get("upperclassnm")+"").replaceAll("특별과정", "교원").replaceAll("기타", "") + "직무-" + FormatDate.getFormatDate(view.get("edustart")+"","yyyy") + "-" + Integer.parseInt(mp.get("subjseq")+"") + "-" + mp.get("serno") ;
				}
				else if(mp.get("upperclassnm").equals("학부모"))
				{
					passnumber = "특수-사이버-" + FormatDate.getFormatDate(view.get("edustart")+"","yyyy") + "-" + Integer.parseInt(mp.get("subjseq")+"") + "-" + mp.get("serno");
				}
			}
			cell = getCell(sheet3, row_index, index++);
			setText(cell, passnumber);
			cell.setCellStyle(style);
			//비고
			cell = getCell(sheet3, row_index, index++);
			setText(cell, "");
			cell.setCellStyle(style);
			
			
		}
		
		
		
		
//		이수현황 #################################################################################################################
		row_index = 0;
		sheet1.addMergedRegion(new Region(0, (short)0, 0, (short)20)); //10칸 셀병합 
		sheet1.addMergedRegion(new Region(1, (short)0, 1, (short)20)); //10칸 셀병합 
		sheet1.addMergedRegion(new Region(2, (short)0, 2, (short)20)); //10칸 셀병합 
		sheet1.addMergedRegion(new Region(3, (short)0, 3, (short)20)); //10칸 셀병합 
		sheet1.addMergedRegion(new Region(4, (short)0, 4, (short)20)); //10칸 셀병합 
		sheet1.addMergedRegion(new Region(5, (short)0, 5, (short)20)); //10칸 셀병합 
		sheet1.addMergedRegion(new Region(6, (short)0, 6, (short)20)); //10칸 셀병합 
		sheet1.addMergedRegion(new Region(7, (short)0, 7, (short)20)); //10칸 셀병합 
		sheet1.addMergedRegion(new Region(8, (short)0, 8, (short)20)); //10칸 셀병합 
		sheet1.addMergedRegion(new Region(9, (short)0, 9, (short)20)); //10칸 셀병합 
		
		for(int i=0; i<=9; i++)
		{
			for(int j=0; j<=20; j++) 
			{
					cell = getCell(sheet1, i, j);
					cell.setCellStyle(style);
			}
		}
		
//		cell.setCellStyle(style);
//		row.setRowStyle(style);
		cell = getCell(sheet1, row_index, 0);
		setText(cell, "국립특수교육원 부설 원격교육연수원  (연수원 인가번호: 교육과학기술부 04-01)");


		cell = getCell(sheet1, ++row_index, 0);
		setText(cell, " 과 정 명 : " +  this.nullValue(view.get("subjnm")));


		cell = getCell(sheet1, ++row_index, 0);
		setText(cell, "<연 수 개 요>");


		cell = getCell(sheet1, ++row_index, 0);
		setText(cell, " 연수기간 : " +  this.nullValue(edustart) + " ~ " +  this.nullValue(eduend));


		cell = getCell(sheet1, ++row_index, 0);
		setText(cell, " 이수시간 및 학점 : " +  this.nullValue(view.get("edutimes")) + "시간(" + this.nullValue(view.get("point")) + "학점)");


		cell = getCell(sheet1, ++row_index, 0);
		setText(cell, " 시도교육청 : ");


		cell = getCell(sheet1, ++row_index, 0);
		setText(cell, " 수강인원 : " +  this.nullValue(passlist.size() + notlist.size()) + "명");


		cell = getCell(sheet1, ++row_index, 0);
		setText(cell, " 이수인원 : " +  passlist.size() + "명");


		cell = getCell(sheet1, ++row_index, 0);
		setText(cell, " 미이수인원 : " +  notlist.size() + "명");


		cell = getCell(sheet1, ++row_index, 0);
		setText(cell, " 과정구분: 1. 일반과정, 2. 특별과정(시도교육청 요청과정), 3. 무료과정");


		col_index = 0;
		this.crateCell(wb, getCell(sheet1, ++row_index, col_index++), "시도", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "서울", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "부산", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "대구", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "인천", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "광주", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "대전", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "울산", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "경기", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "강원", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "충북", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "세종", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "충남", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "전북", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "전남", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "경북", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "경남", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "제주", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "국립학교", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "복지관 외", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), "계", HSSFCellStyle.ALIGN_CENTER);
		
		col_index = 0;
		this.crateCell(wb, getCell(sheet1, ++row_index, col_index++), "이수인원", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  sido_cnt[0] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  sido_cnt[1] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  sido_cnt[2] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  sido_cnt[3] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  sido_cnt[4] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  sido_cnt[5] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  sido_cnt[6] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  sido_cnt[7] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  sido_cnt[8] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), sido_cnt[9] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), sido_cnt[18] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), sido_cnt[10] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), sido_cnt[11] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), sido_cnt[12] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), sido_cnt[13] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), sido_cnt[14] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), sido_cnt[15] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), sido_cnt[16] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), sido_cnt[17] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), tot + "", HSSFCellStyle.ALIGN_CENTER);
		
		col_index = 0;
		this.crateCell(wb, getCell(sheet1, ++row_index, col_index++), "미이수자", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  nsido_cnt[0] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  nsido_cnt[1] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  nsido_cnt[2] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  nsido_cnt[3] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  nsido_cnt[4] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  nsido_cnt[5] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  nsido_cnt[6] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  nsido_cnt[7] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  nsido_cnt[8] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), nsido_cnt[9] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), nsido_cnt[18] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), nsido_cnt[10] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), nsido_cnt[11] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), nsido_cnt[12] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), nsido_cnt[13] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), nsido_cnt[14] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), nsido_cnt[15] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), nsido_cnt[16] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), nsido_cnt[17] + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), ntot + "", HSSFCellStyle.ALIGN_CENTER);
		
		col_index = 0;
		this.crateCell(wb, getCell(sheet1, ++row_index, col_index++), "총 수강자", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  (sido_cnt[0] + nsido_cnt[0]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  (sido_cnt[1] + nsido_cnt[1]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  (sido_cnt[2] + nsido_cnt[2]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  (sido_cnt[3] + nsido_cnt[3]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  (sido_cnt[4] + nsido_cnt[4]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  (sido_cnt[5] + nsido_cnt[5]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  (sido_cnt[6] + nsido_cnt[6]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  (sido_cnt[7] + nsido_cnt[7]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),  (sido_cnt[8] + nsido_cnt[8]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), (sido_cnt[9] + nsido_cnt[9]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), (sido_cnt[18] + nsido_cnt[18]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), (sido_cnt[10] + nsido_cnt[10]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), (sido_cnt[11] + nsido_cnt[11]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), (sido_cnt[12] + nsido_cnt[12]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), (sido_cnt[13] + nsido_cnt[13]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),(sido_cnt[14] + nsido_cnt[14]) +  "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++),(sido_cnt[15] + nsido_cnt[15]) +  "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), (sido_cnt[16] + nsido_cnt[16]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), (sido_cnt[17] + nsido_cnt[17]) + "", HSSFCellStyle.ALIGN_CENTER);
		this.crateCell(wb, getCell(sheet1, row_index, col_index++), (tot + ntot) + "", HSSFCellStyle.ALIGN_CENTER);
		
		
		
		
		
		
		 String stordFilePath = EgovProperties.getProperty("Globals.fileStorePath");
		   
		  String fileName = commandMap.get("ses_search_subjinfo") + "_기이수현황(통보용)"+".xls";
		  fileName = fileName.replaceAll("\\p{Space}",  "_");
		  fileName = fileName.replaceAll("/",  "");
		  
		  File outputZip = new File(stordFilePath, fileName);
		  
		  OutputStream outPut = new FileOutputStream(outputZip);
		  wb.write(outPut);
//		         
		  String mimetype = "application/vnd.ms-excel";


		  response.setContentType(mimetype);
		  //response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fvo.getOrignlFileNm(), "utf-8") + "\"");
		  EgovFileDownloadController.setDisposition(fileName, request, response);
		  //response.setContentLength(fSize);


		  BufferedInputStream in = null;
		  BufferedOutputStream out = null;


		  try {
		     in = new BufferedInputStream(new FileInputStream(outputZip));
		     out = new BufferedOutputStream(response.getOutputStream());


		     FileCopyUtils.copy(in, out);
		     out.flush();
		  } catch (Exception ex) {


		  } finally {
		     if (in != null) {
			    try {
			       in.close();
			    } catch (Exception ignore) {
			 
			    }
		     }
		     if (out != null) {
				    try {
				       out.close();
				    } catch (Exception ignore) {
				 
				    }
		     }
		  }
		 }

	
	
	
	
	}

