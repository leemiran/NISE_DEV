package egovframework.svt.adm.hom.train;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.svt.util.FileUtil;
import egovframework.svt.util.HtmlUtil;

@Controller
public class AdminTrainController {

	/** log */
	protected static final Log log = LogFactory.getLog(AdminTrainController.class);
	
	@Autowired
	AdminTrainService adminTrainService;
	
	@Autowired
	FileUtil fileUtil;
	
	@Autowired
	HtmlUtil htmlUtil;
	
	@RequestMapping(value="/adm/hom/train/trainList.do")
	public String trainList(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		
		List<?> trainList = adminTrainService.trainList();
		model.addAttribute("trainList", trainList);
		
		return "svt/adm/hom/train/trainList";
	}
	
	@RequestMapping(value="/adm/hom/train/trainReg.do")
	public String trainReg(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "svt/adm/hom/train/trainReg";
	}

	@RequestMapping(value="/adm/hom/train/insertTrain.do")
	public String insertTrain(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		htmlUtil.escapeMap(commandMap, "categoryNm");
		
		model.addAllAttributes(adminTrainService.insertTrain(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/hom/train/trainDetail.do")
	public String trainDetail(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		
		Map<String, String> train = adminTrainService.trainDetail(commandMap.get("trainSeq").toString());
		model.addAttribute("train", train);
		
		return "svt/adm/hom/train/trainReg";
	}

	@RequestMapping(value="/adm/hom/train/updateTrain.do")
	public String updateTrain(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		htmlUtil.escapeMap(commandMap, "categoryNm");
		
		model.addAllAttributes(adminTrainService.updateTrain(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/hom/train/deleteTrain.do")
	public String deleteTrain(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(adminTrainService.deleteTrain(commandMap));
		return "jsonView";
	}
	
	// 하위
	@RequestMapping(value="/adm/hom/train/trainSubjList.do")
	public String trainSubjList(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		
		List<?> trainSubjList = adminTrainService.trainSubjList(commandMap);
		model.addAttribute("trainSubjList", trainSubjList);
		
		// 카테고리 셀렉트 박스
		List<?> trainList = adminTrainService.trainList();
		model.addAttribute("trainList", trainList);
		
		return "svt/adm/hom/train/trainSubjList";
	}
	
	@RequestMapping(value="/adm/hom/train/trainSubjReg.do")
	public String trainSubjReg(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "svt/adm/hom/train/trainSubjReg";
	}
	
	@RequestMapping(value="/adm/hom/train/insertTrainSubj.do")
	public String insertTrainSubj(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		htmlUtil.escapeMap(commandMap, "subjNm,lessonNum,linkUrl");
		
		// upload File
		commandMap.putAll(fileUtil.uploadImage(request, "train", "imgFile"));
		
		model.addAllAttributes(adminTrainService.insertTrainSubj(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/hom/train/trainSubjDetail.do")
	public String trainSubjDetail(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		
		Map<String, String> trainSubj = adminTrainService.trainSubjDetail(commandMap.get("trainSubjSeq").toString());
		model.addAttribute("trainSubj", trainSubj);
		
		return "svt/adm/hom/train/trainSubjReg";
	}

	@RequestMapping(value="/adm/hom/train/updateTrainSubj.do")
	public String updateTrainSubj(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		htmlUtil.escapeMap(commandMap, "subjNm,lessonNum,linkUrl");
		
		// upload File
		commandMap.putAll(fileUtil.uploadImage(request, "train", "imgFile"));
		
		model.addAllAttributes(adminTrainService.updateTrainSubj(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/hom/train/deleteTrainSubj.do")
	public String deleteTrainSubj(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(adminTrainService.deleteTrainSubj(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/hom/train/deleteTrainSubjImg.do")
	public String deleteTrainSubjImg(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(adminTrainService.deleteTrainSubjImg(commandMap.get("imgId").toString()));
		return "jsonView";
	}
}
