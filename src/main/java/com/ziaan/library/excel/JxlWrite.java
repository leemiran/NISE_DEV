package com.ziaan.library.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


import java.io.FileNotFoundException;
import jxl.Cell;
import jxl.Sheet;
import com.ziaan.library.Log;

import com.ziaan.library.DataBox;



public class JxlWrite {
	private WritableWorkbook workBook   = null;
	private WritableSheet    writeSheet = null;
	
	private File excelFile = null;
	
	String sheetName = null;
	
	public JxlWrite(String pathName) {
		this(pathName, null);
	}
	
	public JxlWrite(String pathName, String sheetName) {
		if (sheetName == null) sheetName = Constants._EXCEL_SHEET_NAME;

		try {
			this.sheetName  = sheetName;
			this.excelFile  = new File(pathName);
			this.workBook   = Workbook.createWorkbook(this.excelFile);
			this.writeSheet = workBook.createSheet(this.sheetName, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Excel Sheet ÔøΩÔøΩ dÔøΩÔøΩÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩÔøΩ—¥ÔøΩ.
	 *
	 */
	public void setSheet() {
		jxl.SheetSettings sheetSet = this.writeSheet.getSettings();
		sheetSet.setPaperSize(PaperSize.A4);
		sheetSet.setOrientation(PageOrientation.LANDSCAPE);
		sheetSet.setTopMargin(0.35);
		sheetSet.setBottomMargin(0.35);
		sheetSet.setLeftMargin(0.35);
		sheetSet.setRightMargin(0.35);		
	}

	public void setMergeCells(int col1,int row1,int col2,int row2) {
		try {
			this.writeSheet.mergeCells(col1, row1, col2, row2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Excel File ; ÔøΩÔøΩÔøΩ—¥ÔøΩ.
	 * @param arrTitle
	 * @param arrContent
	 */
	public void makeExcel(List arrTitles, List arrContent) {
		int titleCount  = 0;
		int columnCount = 0;
		int rowCount    = 0;
		
		WritableCellFormat tempFormat  = null;
		Label contentLabel = null;
		Label titleLabel   = null;
		
		DataBox   dataMap = null;
		ArrayList arrTitle = null;
		
		this.setSheet();

		try {
			titleCount = arrTitles.size();
			
//			 ????ù¥????ùÑ ?Éù?Ñ±?ïú?ã§.
 			for (int i = 0; i < titleCount; i++) {
 				arrTitle = (ArrayList)arrTitles.get(i);
 				columnCount = arrTitle.size();

 				for (int j = 0; j < columnCount; j++) {
					dataMap = (DataBox)arrTitle.get(j);
					this.writeSheet.setColumnView(j, dataMap.getInt(Constants._EXCEL_COLUMN_WIDTH_NAME));					
					tempFormat = this.getTitleFormat();
					titleLabel = new Label(j, i, dataMap.getString(Constants._EXCEL_COLUMN_NAME), tempFormat);
					this.writeSheet.addCell(titleLabel);
				}
			}

// 			 ????ù¥????ùÑ ?Éù?Ñ±?ïú?ã§.
 			rowCount = arrContent.size() + titleCount;
 			
 			System.out.println("rowCount :"+ rowCount);
 			System.out.println("titleCount"+ titleCount);
 			System.out.println("columnCount"+ columnCount);

			for (int i = titleCount; i < rowCount; i++) {
			    
			     
				dataMap = (DataBox)arrContent.get(i - titleCount);
				
				for (int j = 0; j < columnCount; j++) {
					if (i == (rowCount - 1)) {
						if (j == 0) {
							tempFormat = this.getContentLeftBottom();
							System.out.println("tempFormat1"+ tempFormat);
						} else if (j == (columnCount - 1)) {
							tempFormat = this.getContentRightBottom();
							System.out.println("tempFormat2"+ tempFormat);
						} else {
							tempFormat = this.getContentCenterBottom();
							System.out.println("tempFormate"+ tempFormat);
						}
					} else {
						if (j == 0) {
							tempFormat = this.getContentLeftCenter();
							System.out.println("1 tempFormat1"+ tempFormat);
						} else if (j == (columnCount - 1)) {
							tempFormat = this.getContentRightCenter();
							System.out.println("1 tempFormat2"+ tempFormat);
						} else {
							tempFormat = this.getContentCenter();
							System.out.println("1 tempFormat3"+ tempFormat);
						}
					}

					contentLabel = new Label(j, i, dataMap.getString(Constants._EXCEL_COLUMN_NAME + "_" + j), tempFormat);
					
					
					this.writeSheet.addCell(contentLabel);
				}
			}
		
			this.workBook.write();
			this.workBook.close();
			
			this.writeSheet = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Excel File ; ÔøΩÔøΩÔøΩ—¥ÔøΩ.
	 * @param arrTitle
	 * @param arrContent
	 */
	public void makeExcelNoTitle(int columnCount, List arrContent) {
		//int titleCount  = 0;
		//int columnCount = 0;
		int rowCount    = 0;
		
		WritableCellFormat tempFormat  = null;
		Label contentLabel = null;
		//Label titleLabel   = null;
		
		DataBox   dataMap = null;
		//ArrayList arrTitle = null;
		
		this.setSheet();

		try {

 			rowCount = arrContent.size(); //+ titleCount;
 			
			for (int i = 0; i < rowCount; i++) {
			    
			     
				dataMap = (DataBox)arrContent.get(i);
				
				for (int j = 0; j < columnCount; j++) {
					if (i == (rowCount - 1)) {
						if (j == 0) {
							tempFormat = this.getContentLeftBottom();							
						} else if (j == (columnCount - 1)) {
							tempFormat = this.getContentRightBottom();
						} else {
							tempFormat = this.getContentCenterBottom();
						}
					} else {
						if (j == 0) {
							tempFormat = this.getContentLeftCenter();
						} else if (j == (columnCount - 1)) {
							tempFormat = this.getContentRightCenter();
						} else {
							tempFormat = this.getContentCenter();
						}
					}
					contentLabel = new Label(j, i, dataMap.getString(Constants._EXCEL_COLUMN_NAME + "_" + j), tempFormat);
							
					this.writeSheet.addCell(contentLabel);
				}
			}
		
			this.workBook.write();
			this.workBook.close();
			
			this.writeSheet = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ≈∏ÔøΩÔøΩ∆≤ ÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩÔøΩÔøΩ
	 * @return
	 */
	private WritableCellFormat getTitleFormat() {
		WritableCellFormat titleFormat = null;
		
		try {
			titleFormat = new WritableCellFormat();
			
			titleFormat.setAlignment(Alignment.CENTRE);
			titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

			titleFormat.setBorder(Border.TOP, BorderLineStyle.THIN);
			titleFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
			titleFormat.setBorder(Border.LEFT, BorderLineStyle.THIN);
			titleFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
			titleFormat.setBackground(Colour.VERY_LIGHT_YELLOW);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return titleFormat;
	}
	
	/**
	 * ÔøΩÔøΩÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩ ÔøΩÔøΩÔøΩÓµ• ÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩÔøΩÔøΩ
	 * @return
	 */
	private WritableCellFormat getContentLeftCenter() {
		WritableCellFormat contentFormat = null;
		
		try {
			contentFormat = new WritableCellFormat();
			
			contentFormat.setAlignment(Alignment.CENTRE);
			contentFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			contentFormat.setBorder(Border.TOP, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.BOTTOM, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.LEFT, BorderLineStyle.THIN);
			contentFormat.setBorder(Border.RIGHT, BorderLineStyle.HAIR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return contentFormat;
	}
	
	/**
	 * ÔøΩÔøΩÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩ ÔøΩÔøΩÔøΩÓµ• ÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩÔøΩÔøΩ
	 * @return
	 */
	private WritableCellFormat getContentRightCenter() {
		WritableCellFormat contentFormat = null;
		
		try {
			contentFormat = new WritableCellFormat();
			
			contentFormat.setAlignment(Alignment.CENTRE);
			contentFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			contentFormat.setBorder(Border.TOP, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.BOTTOM, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.LEFT, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return contentFormat;
	}
	
	/**
	 * ÔøΩÔøΩÔøΩÔøΩ ÔøΩÔøΩÔøΩÓµ• ÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩÔøΩÔøΩ
	 * @return
	 */
	private WritableCellFormat getContentCenter() {
		WritableCellFormat contentFormat = null;
		
		try {
			contentFormat = new WritableCellFormat();
			
			contentFormat.setAlignment(Alignment.CENTRE);
			contentFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			contentFormat.setBorder(Border.TOP, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.BOTTOM, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.LEFT, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.RIGHT, BorderLineStyle.HAIR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return contentFormat;
	}
	
	/**
	 * ÔøΩÔøΩÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩ ÔøΩœ¥ÔøΩ ÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩÔøΩÔøΩ
	 * @return
	 */
	private WritableCellFormat getContentLeftBottom() {
		WritableCellFormat contentFormat = null;
		
		try {
			contentFormat = new WritableCellFormat();
			
			contentFormat.setAlignment(Alignment.CENTRE);
			contentFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			contentFormat.setBorder(Border.TOP, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
			contentFormat.setBorder(Border.LEFT, BorderLineStyle.THIN);
			contentFormat.setBorder(Border.RIGHT, BorderLineStyle.HAIR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return contentFormat;
	}
	
	/**
	 * ÔøΩÔøΩÔøΩÔøΩ ÔøΩÔøΩÔøΩÓµ• ÔøΩœ¥ÔøΩ ÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩÔøΩÔøΩ
	 * @return
	 */
	private WritableCellFormat getContentCenterBottom() {
		WritableCellFormat contentFormat = null;
		
		try {
			contentFormat = new WritableCellFormat();
			
			contentFormat.setAlignment(Alignment.CENTRE);
			contentFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			contentFormat.setBorder(Border.TOP, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
			contentFormat.setBorder(Border.LEFT, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.RIGHT, BorderLineStyle.HAIR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return contentFormat;
	}
	
	/**
	 * ÔøΩÔøΩÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩ ÔøΩœ¥ÔøΩ ÔøΩÔøΩ ÔøΩÔøΩÔøΩÔøΩÔøΩÔøΩ
	 * @return
	 */
	private WritableCellFormat getContentRightBottom() {
		WritableCellFormat contentFormat = null;
		
		try {
			contentFormat = new WritableCellFormat();
			
			contentFormat.setAlignment(Alignment.CENTRE);
			contentFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			contentFormat.setBorder(Border.TOP, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
			contentFormat.setBorder(Border.LEFT, BorderLineStyle.HAIR);
			contentFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return contentFormat;
	}
	
	public static void main(String[] args) {
	    JxlWrite jxl = new JxlWrite("C:/jxlSample.xls");

	    DataBox dataMap = null;
	    ArrayList arrTitles  = new ArrayList();
	    ArrayList arrContent = new ArrayList();
	    ArrayList arrTitle   = null;

	    arrTitle = new ArrayList();
	    dataMap = new DataBox("EXCEL_DATA");
	    dataMap.put("column", "?ïôÍµ?");
	    dataMap.put("width",String.valueOf(15));
	    arrTitle.add(dataMap);

	    dataMap = new DataBox("EXCEL_DATA");
	    dataMap.put("column", "?ù¥Î¶?");
	    dataMap.put("width",String.valueOf(10));
	    arrTitle.add(dataMap);

	    for (int i = 2; i < 22; i++) {
		dataMap = new DataBox("EXCEL_DATA");
		dataMap.put("column",String.valueOf(i - 1));
		dataMap.put("width",String.valueOf(4));
		arrTitle.add(dataMap);
	    }

	    arrTitles.add(arrTitle);

	    arrTitle = new ArrayList();
	    dataMap  = new DataBox("EXCEL_DATA");
	    dataMap.put("column", "?ïôÎ≤?1");
	    dataMap.put("width",String.valueOf(15) );
	    arrTitle.add(dataMap);

	    dataMap =new DataBox("EXCEL_DATA");
	    dataMap.put("column", "?ù¥Î¶? 2");
	    dataMap.put("width", String.valueOf(10));
	    arrTitle.add(dataMap);

	    for (int i = 2; i < 22; i++) {
		dataMap = new DataBox("EXCEL_DATA");
		dataMap.put("column",String.valueOf(i - 1));
		dataMap.put("width", String.valueOf(4));
		arrTitle.add(dataMap);
	    }

	    arrTitles.add(arrTitle);

	    jxl.makeExcel(arrTitles, arrContent);
	}
//	public static void main(String[] args) {
//		JxlWrite jxl = new JxlWrite("C:/jxlSample.xls");
//		
//		DataRec dataMap = null;
//		ArrayList arrTitles  = new ArrayList();
//		ArrayList arrContent = new ArrayList();
//		ArrayList arrTitle   = null;
//		
//		arrTitle = new ArrayList();
//		dataMap = new DataRec();
//		dataMap.setInfo("title", "ÔøΩ–πÔøΩ");
//		dataMap.setInfo("width", 15);
//		arrTitle.add(dataMap);
//		
//		dataMap = new DataRec();
//		dataMap.setInfo("title", "ÔøΩÃ∏ÔøΩ");
//		dataMap.setInfo("width", 10);
//		arrTitle.add(dataMap);
//		
//		for (int i = 2; i < 22; i++) {
//			dataMap = new DataRec();
//			dataMap.setInfo("title", i - 1);
//			dataMap.setInfo("width", 4);
//			arrTitle.add(dataMap);
//		}
//		
//		arrTitles.add(arrTitle);
//		
//		arrTitle = new ArrayList();
//		dataMap  = new DataRec();
//		dataMap.setInfo("title", "ÔøΩ–πÔøΩ1");
//		dataMap.setInfo("width", 15);
//		arrTitle.add(dataMap);
//		
//		dataMap = new DataRec();
//		dataMap.setInfo("title", "ÔøΩÃ∏ÔøΩ2");
//		dataMap.setInfo("width", 10);
//		arrTitle.add(dataMap);
//		
//		for (int i = 2; i < 22; i++) {
//			dataMap = new DataRec();
//			dataMap.setInfo("title", i - 1);
//			dataMap.setInfo("width", 4);
//			arrTitle.add(dataMap);
//		}
//		
//		arrTitles.add(arrTitle);
//		
//		jxl.makeExcel(arrTitles, arrContent);
//	}
}
