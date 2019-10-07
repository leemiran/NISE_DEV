package egovframework.com.innorix;

import java.lang.*;
import javax.servlet.http.*;
import org.springframework.validation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResultController extends SimpleFormController {

    protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception
	{
		TransferCommand transferCommand = (TransferCommand)command;
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(getSuccessView());
		
		request.setCharacterEncoding("UTF-8");
		modelAndView.addObject( "test1", transferCommand.getTest1() );
		modelAndView.addObject( "_SUB_DIR", request.getParameter("_SUB_DIR") );
		modelAndView.addObject( "_innorix_origfilename", request.getParameterValues("_innorix_origfilename") ); 	// 원본 파일명
		modelAndView.addObject( "_innorix_savefilename", request.getParameterValues("_innorix_savefilename") ); 	// 저장 파일명
		modelAndView.addObject( "_innorix_savepath", request.getParameterValues("_innorix_savepath") );				// 파일 저장경로
		modelAndView.addObject( "_innorix_filesize", request.getParameterValues("_innorix_filesize") );				// 파일 사이즈
		modelAndView.addObject( "_innorix_folder", request.getParameterValues("_innorix_folder") );					// 폴더정보(폴더 업로드시만)
		modelAndView.addObject( "_innorix_customvalue", request.getParameterValues("_innorix_customvalue") );		// 개발자 정의값
		modelAndView.addObject( "_innorix_componentname", request.getParameterValues("_innorix_componentname") );	// 컴포넌트 이름
		
		modelAndView.addObject( "_innorix_exist_id", request.getParameterValues("_innorix_exist_id") );				// 존재하는 파일ID
		modelAndView.addObject( "_innorix_exist_name", request.getParameterValues("_innorix_exist_name") );			// 존재하는 파일이름
		modelAndView.addObject( "_innorix_exist_size", request.getParameterValues("_innorix_exist_size") );			// 존재하는 파일용량
		
		modelAndView.addObject( "_innorix_deleted_id", request.getParameterValues("_innorix_deleted_id") );			// 삭제된 파일ID
		modelAndView.addObject( "_innorix_deleted_name", request.getParameterValues("_innorix_deleted_name") );		// 삭제된 파일이름
		modelAndView.addObject( "_innorix_deleted_size", request.getParameterValues("_innorix_deleted_size") );		// 삭제된 파일용량
		

		return modelAndView;
	}
}
