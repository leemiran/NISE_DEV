package egovframework.com.innorix;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import com.innorix.servlet.transfer.InnorixTransfer;


public class TransferController implements Controller {	
	
	protected final Log logger = LogFactory.getLog(getClass());
	private String saveDir;
	
	public void setSaveDir(String saveDir)
	{
		this.saveDir = saveDir;
	}
    
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException , Exception 
    {
    	String retValue = "";
		int maxPostSize = 2147483000;		
		
		try {
			//
			// 사용자 편의 함수
			// public string InnorixTransfer.DelimiterReplace(String fullDir)
			// - 디렉토리 구분자를 "/" 로 변경
			//   fullDir : 디렉토리 절대 경로
			// public boolean InnorixTransfer.CreateFolder(String fullPath)
			// - 디렉토리 생성
			//   fullDir : 생성할 디렉토리 절대 경로

			//
			// - class InnorixTransfer -
			//
			// = Constructor Detail =
			// public InnorixTransfer(javax.servlet.http.HttpServletRequest request,
//									javax.servlet.http.HttpServletResponse response,
//			                      int maximumPostSize,
//			                      java.lang.String encode = "UTF-8",
//			                      java.lang.String saveDirectory = servletPath + "data",
//			                      java.lang.String tempDirectory = saveDirectory + "temp") throws IOException
			// - InnorixTransfer 클래스
			//   request : 서블릿 request
			//   response : 서블릿 response
			//   maximumPostSize : 최대 포스트 크기, 기본값은 2147483647
			//   encode : 업로드일 경우 파일 업로드 서블릿 인코딩, 기본값은 "UTF-8"
			//   saveDirectory : 업로드일 경우 저장될 디렉토리, 기본값은 servlet 디렉토리 아래에 data
			//   tempDirectory : 압축 업로드일 경우 임시파일 저장 디렉토리, 기본값은 저장 디렉토리 아래에 temp
			//InnorixTransfer innoTransfer = new InnorixTransfer(request, response, maxPostSize, "UTF-8", saveDir, tempDir); // 파일 전송과 관련된 정보값 갱신
			InnorixTransfer innoTransfer = new InnorixTransfer(request, response, maxPostSize, "UTF-8", saveDir);

			//
			// - setting custom value -
			//
			// = Method Detail =
			// public SetCustomValue(java.lang.String customValue)
			//
			// 예제)
			// 괄호 안에 값을 넣고 주석을 제거 하세요.
			//innoTransfer.SetCustomValue("");


			//
			// - setting custom error -
			//
			// = Method Detail =
			// public SetCustomError(int code,
//			                       java.lang.String message,
//			                       java.lang.String detail,
//			                       boolean confirm)
			//
			// 예제)
			// 괄호 안에 값을 넣고 주석을 제거 하세요.
			//innoTransfer.SetCustomError(0, "", "", true);




			//
			// - 재정의 함수 -
			//
			// 필요하다면 InnorixTransfer 클래스를 상속 받아 함수를 재정의하여 사용하세요.
			//
			// = Method Detail =
			// boolean CreateFolder(String fullDir)
			// - 디렉토리 생성
			// String RenameFile(String fullDir,      // 전체 경로
//			                    String orgFileName,  // 원본파일 이름
//			                    String isCompressed) // "0" : false, "1" : true
			// - 파일 이름을 리턴한다.
			// boolean MakeFile(String filePath, long fileSize)
			// - 빈 파일을 생성한다.
			// boolean ExtractFile(String fileName, String tempDir, boolean isDelete) throws IOException
			// - 압축을 해제한다.
			//
			//
			// 아래 함수들의 리턴 값은 InnorixTransfer.Save에서 넘어오는 값을 설정해줄때 필요한 값입니다.
			// 따라서 아래 함수들을 재정의하여 리턴값을 바꾸면, InnorixTransfer.Save()을 실행했을때 결과값이 바뀐값으로 적용됩니다.
			//
			// String getServerInfo()
			// String getFileInfo()
			// String attachFile()
			// String attachFileCompleted()
			// String cancel()
			// String getFileSliceCompleted()
			// String getFileCompleted()
			// String error()
			// String speedCheck()
			//
			// 리턴값 예제)
			//returnValue =
//				"<file_code>0000</file_code>\n" +									// 코드번호 0000 정상 1001 예외
//				"<file_index>" + index + "</file_index>\n" +						// 파일 인덱스
//				"<file_origfilename>" + orgFileName + "</file_origfilename>\n" +	// 업로드 원본 파일명
//				"<file_savefilename>" + saveFileName + "</file_savefilename>\n" +	// 서버에 저장할 파일명
//				"<file_filesize>" + fileSize + "</file_filesize>\n" +				// 업로드 파일 사이즈
//				"<file_clientpath>" + clientPath + "</file_clientpath>\n" +			// 클라이언트에서 선택한 파일 풀패스
//				"<file_savepath>" + serverFilePath + "</file_savepath>\n" +			// 서버에 저장할 풀패스 파일명
//				"<file_customvalue>" + _customValue + "</file_customvalue>\n";		// 개발자 정의 값 GetResponseFileInfo(index, "customvalue"); 메소드로 확인


			// 전송
			// 
			// = Method Detail =
			// public String Save(java.lang.String fileName)
			// - 전송 메소드
			// - 인자값
//				 fileName : 서버에 저장될 파일 이름(생략가능 - 원본 파일 이름과 동일하게 저장)
			// - 리턴 코드
//				0) 0000 정상
//				0) 0001 경로 없음
//			 	0) 0002 쓰기 권한 없음
//				0) 0003 무결성 검사 실패
//			 	1) 1001 디렉토리 생성실패(getFileInfo)
//			 	1) 1002 압축해제 실패(attachFileCompleted)
			retValue = innoTransfer.Save();
			if (retValue == null) {
//				innoTransfer.printAction();
//				System.out.println(result);
				retValue = innoTransfer.ShowCustomError("1007", "테스트", "에러 테스트", true);
			}
			else
			{
				retValue = innoTransfer.GetResultMessage();
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return new ModelAndView("transfer", "message", retValue);
    }
}
