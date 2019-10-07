package egovframework.adm.lcms.ims.mainfest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import egovframework.com.aja.lcm.controller.ScormHandlerAjaxController;
import egovframework.com.aja.service.CommonAjaxManageService;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;

public class RestructureHandler {
	
	/** log */
    protected static final Log log = LogFactory.getLog(RestructureHandler.class);
	
	
	// ManifestFile을 만들기 위한 Namespace 선언
	Namespace na0 = Namespace.getNamespace("http://www.imsglobal.org/xsd/imscp_v1p1");

	Namespace na1 = Namespace.getNamespace("adlcp", "http://www.adlnet.org/xsd/adlcp_v1p3");

	Namespace na2 = Namespace.getNamespace("adlseq", "http://www.adlnet.org/xsd/adlseq_v1p3");

	Namespace na3 = Namespace.getNamespace("adlnav", "http://www.adlnet.org/xsd/adlnav_v1p3");

	Namespace na4 = Namespace.getNamespace("imsss", "http://www.imsglobal.org/xsd/imsss");

	Namespace na5 = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	
	private String identifier = "";
	private String identifierref = "";
	private String childrenSeq = "";
	
    
    public boolean addItem(Map<String, Object> commandMap) throws Exception{
    	boolean result = true;
    	
    	String sel_val = java.net.URLDecoder.decode((String)commandMap.get("selectVal"));
    	String leftVal[] = sel_val.split("#");
    	
    	String path = Globals.MANIFEST_PATH+commandMap.get("userid")+"/imsmanifest.xml";
    	
    	List ls_1_1;
    	
    	Document doc = readManifestFile(path);
    	Element el = doc.getRootElement();
    	List ls = el.getChildren();
    	
    	FileController file = new FileController();
    	
    	if( leftVal[0].equals("ORG") ){
    		for( int i=0; i<ls.size(); i++ ){
    			if( (((Element)ls.get(i)).getName()).equals("organizations") ){
    				List ls_1 = ((Element)ls.get(i)).getChildren();
    				for( int j=0; j<ls_1.size(); j++ ){
    					if( ((Element)ls_1.get(j)).getName().equals("organization") ){
    						Element item = new Element("item");
    						Element title = new Element("title");
    						title.setText("NewItem");
    						item.addContent(title);
    						item.setAttribute("identifier", file.getUniqueID());
    						item.setAttribute("identifierref", file.getUniqueID());
    						((Element)ls_1.get(j)).addContent(item);
    						removeSequencing_org(((Element)ls_1.get(j)));
    					}
    				}
    			}
    		}
    	}else{
    		for( int i=0; i<ls.size(); i++ ){
    			if( ((Element)ls.get(i)).getName().equals("organizations") ){
    				List ls_1 = ((Element)ls.get(i)).getChildren();
    				for( int j=0; j<ls_1.size(); j++ ){
    					if( ((Element)ls_1.get(j)).getName().equals("organization") ){
    						ls_1_1 = ((Element)ls_1.get(j)).getChildren();
    						for( int y=0; y<ls_1_1.size(); y++ ){
    							if( searchInnerItem((Element)ls_1_1.get(y), leftVal[1]) != null ){
									if( leftVal[1].equals( (searchInnerItem((Element)ls_1_1.get(y), leftVal[1])).getAttributeValue("identifier")) ){
										Element item = new Element("item");
										Element title = new Element("title");
										title.setText("NewItem");
										item.addContent(title);
										item.setAttribute("identifier", file.getUniqueID());
										item.setAttribute("identifierref", file.getUniqueID());
										removeSequencing_org((Element)ls_1.get(j));
										searchInnerItem((Element)ls_1_1.get(y), leftVal[1]).addContent(item);
										searchInnerItem((Element)ls_1_1.get(y), leftVal[1]).setAttribute("identifierref", "");
										break;
									}
    							}
    						}
    					}
    				}
    			}
    		}
    	}
    	
    	result = saveManifestFile(doc, path);
    	
    	return result;
    }
    
    public String newAddItem(Map<String, Object> commandMap) throws Exception{
    	String result = "";
    	String sel_val = java.net.URLDecoder.decode((String)commandMap.get("selectVal"));
    	String leftVal[] = sel_val.split("#");
    	
    	String path = Globals.MANIFEST_PATH+commandMap.get("userid")+"/imsmanifest.xml";
    	
    	List ls_1_1;
    	
    	Document doc = readManifestFile(path);
    	Element el = doc.getRootElement();
    	List ls = el.getChildren();
    	
    	FileController file = new FileController();
    	
    	if( leftVal[0].equals("ORG") ){
    		for( int i=0; i<ls.size(); i++ ){
    			if( (((Element)ls.get(i)).getName()).equals("organizations") ){
    				List ls_1 = ((Element)ls.get(i)).getChildren();
    				for( int j=0; j<ls_1.size(); j++ ){
    					if( ((Element)ls_1.get(j)).getName().equals("organization") ){
    						Element item = new Element("item");
    						Element title = new Element("title");
    						title.setText("NewItem");
    						item.addContent(title);
    						result = file.getUniqueID();
    						item.setAttribute("identifier", result);
    						item.setAttribute("identifierref", file.getUniqueID());
    						((Element)ls_1.get(j)).addContent(item);
    						newRemoveSequencing_org(((Element)ls_1.get(j)));
    					}
    				}
    			}
    		}
    	}else{
    		for( int i=0; i<ls.size(); i++ ){
    			if( ((Element)ls.get(i)).getName().equals("organizations") ){
    				List ls_1 = ((Element)ls.get(i)).getChildren();
    				for( int j=0; j<ls_1.size(); j++ ){
    					if( ((Element)ls_1.get(j)).getName().equals("organization") ){
    						ls_1_1 = ((Element)ls_1.get(j)).getChildren();
    						for( int y=0; y<ls_1_1.size(); y++ ){
    							if( searchInnerItem((Element)ls_1_1.get(y), leftVal[1]) != null ){
									if( leftVal[1].equals( (searchInnerItem((Element)ls_1_1.get(y), leftVal[1])).getAttributeValue("identifier")) ){
										Element item = new Element("item");
										Element title = new Element("title");
										title.setText("NewItem");
										item.addContent(title);
										result = file.getUniqueID();
										item.setAttribute("identifier", result);
										item.setAttribute("identifierref", file.getUniqueID());
										newRemoveSequencing_org((Element)ls_1.get(j));
										searchInnerItem((Element)ls_1_1.get(y), leftVal[1]).addContent(item);
										searchInnerItem((Element)ls_1_1.get(y), leftVal[1]).setAttribute("identifierref", "");
										break;
									}
    							}
    						}
    					}
    				}
    			}
    		}
    	}
    	
    	saveManifestFile(doc, path);
    	
    	return result;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
	 * 메니페스트 파일을 저장한다.
	 * 
	 * @param Document
	 *            xml Document객체
	 * @param filename
	 *            저장할 파일 이름
	 * @throws XSLTransformException
	 * @throws IOException
	 */
	public boolean saveManifestFile(Document doc, String filename) {
		OutputStream manifestOP = null;
		String dir = "";
		boolean flag = false;
		try {
			
			resourceRename(doc);
			
			manifestOP = new FileOutputStream(filename.trim(), false);
		} catch (FileNotFoundException e) {

			String filedir[] = filename.split("/");
			for (int i = 1; i < filedir.length - 1; i++) {
				dir = dir + "/" + filedir[i].trim();
				new File(filedir[0].trim() + "/" + dir).mkdirs();
			}

			try {
				manifestOP = new FileOutputStream(filename.trim(), false);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return false;
			}

		}
		XMLOutputter outputter = new XMLOutputter();
		Format f = outputter.getFormat();

		try {

			f.setEncoding("utf-8");
			f.setLineSeparator("\r\n");
			f.setIndent("    ");
			f.setTextMode(Format.TextMode.TRIM);
			outputter.setFormat(f);
			outputter.output(doc, manifestOP);
			manifestOP.flush();
			manifestOP.close();
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return flag;
	}
    
    
	/**
	 * 파일경로의 중복을 없애기 위해 sco최상이 경로를 변경한다.
	 * @param doc
	 */
	public void resourceRename(Document doc){
		Element el = doc.getRootElement();
    	List ls = el.getChildren();
    	int cnt = 1;
    	String sco = "";
    	String tmp = "";
    	for( int i=0; i<ls.size(); i++ ){
    		if( ((Element)ls.get(i)).getName().equals("resources") ){
    			List ls_1 = ((Element)ls.get(i)).getChildren();
    			for( int j=0; j<ls_1.size(); j++ ){
    				if( cnt < 10 ){
    					sco = "sco0"+cnt;
    				}else{
    					sco = "sco"+cnt;
    				}
    				cnt++;
    				Element resource = (Element)ls_1.get(j);
    				tmp = resource.getAttributeValue("href");
    				if( tmp != null && !tmp.equals("")){
    					resource.setAttribute("href", sco + tmp.substring(tmp.indexOf("/"), tmp.length()));
    					List child = resource.getChildren();
    					for( int y=0; y<child.size(); y++ ){
    						Element file = (Element)child.get(y);
    						tmp = file.getAttributeValue("href");
    						file.setAttribute("href", sco+tmp.substring(tmp.indexOf("/"), tmp.length()));
    					}
    				}
    			}
    		}
    	}
	}
    
    
    
    /**
	 * description: manifestfile read.
	 * @return Document
	 * @throws Exception
	 */
	public Document readManifestFile(String filename) {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File(filename));

			return doc;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * sequencing 정보를 삭제한다.
	 * 
	 * @param Element
	 *            el
	 * @return
	 * @throws
	 */
	public void removeSequencing_org(Element el) {
		el.removeChildren("sequencing", na4);
		List ls = el.getChildren();
		for (int i = 0; i < ls.size(); i++) {
			removeSequencing_Item(((Element) ls.get(i)));
		}
	}
	/**
	 * sequencing 정보를 삭제한다.
	 * 
	 * @param Element
	 *            el
	 * @return
	 * @throws
	 */
	public void newRemoveSequencing_org(Element el) {
		el.removeChildren("sequencing", na4);
//		List ls = el.getChildren();
//		for (int i = 0; i < ls.size(); i++) {
//			removeSequencing_Item(((Element) ls.get(i)));
//		}
	}
	/**
	 * description: 학습객체 시퀸스 삭제.
	 * @return 
	 * @throws Exception
	 */
	public void removeSequencing_Item(Element fromitem) {
		Element returnItem = null;
		if ("item".equals(fromitem.getName())) {
			if (hasMoreChildren(fromitem, "item")) {
				List ls = fromitem.getChildren();
				for (int i = 0; i < ls.size(); i++) {
					removeSequencing_Item((Element) ls.get(i));
					if (returnItem != null) {
						break;
					}
				}
			}
			fromitem.removeChildren("sequencing", na4);

		}

	}
	
	/**
	 * 하위노드가 있는지 찾아서 있으면 true 없으면 false를 리턴한다.
	 * 
	 * @param Element
	 *            el , String name
	 * @return boolean
	 * @throws
	 */
	public boolean hasMoreChildren(Element el, String name) {
		List ls = el.getChildren();
		boolean flag = false;
		for (int i = 0; i < ls.size(); i++) {
			if (((Element) ls.get(i)).getName().equals(name)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * item을 찾아서 아이템의 Element를 리턴한다.
	 */
	public Element searchInnerItem(Element fromitem, String identifier) {
		Element returnItem = null;
		if ("item".equals(fromitem.getName())) {
			if (identifier.equals(fromitem.getAttributeValue("identifier"))) {
				returnItem = fromitem;
			} else if (hasMoreChildren(fromitem, "item")) {
				List ls = fromitem.getChildren();
				for (int i = 0; i < ls.size(); i++) {
					returnItem = searchInnerItem((Element) ls.get(i),
							identifier);
					if (returnItem != null) {
						childrenSeq = "Y";
						break;
					}
				}
			}
		}
		return returnItem;
	}
	
	/**
	 * resource를 찾아서 아이템의 Element를 리턴한다.
	 */
	public Element searchInnerResource(Element fromResource, String identifier) {
		Element returnResource = null;
		if ("resource".equals(fromResource.getName())) {
			if (identifier != null && identifier.equals(fromResource.getAttributeValue("identifier"))) {
				returnResource = fromResource;
			} else if (hasMoreChildren(fromResource, "resource")) {
				List ls = fromResource.getChildren();
				for (int i = 0; i < ls.size(); i++) {
					returnResource = searchInnerItem((Element) ls.get(i),
							identifier);
					if (returnResource != null) {
						break;
					}
				}
			}
		}
		return returnResource;
	}
	
	public boolean editItemTitle(Map<String, Object> commandMap){
		boolean result = true;
		
		String type   = (String)commandMap.get("type");
		String itemId = (String)commandMap.get("attributeId");
		String title  = (String)commandMap.get("attributeNm");
		String path   = Globals.MANIFEST_PATH+commandMap.get("userid")+"/imsmanifest.xml";
		
		Document doc = readManifestFile(path);
    	Element el = doc.getRootElement();
    	List ls = el.getChildren();
    	
    	
    	if( type.equals("ORG") ){
    		//아이템을 찾아서 타이틀을 수정한다
    		for( int i=0; i<ls.size(); i++ ){
    			if( ((Element)ls.get(i)).getName().equals("organizations") ){
    				List ls_1 = ((Element)ls.get(i)).getChildren();
    				for( int j=0; j<ls_1.size(); j++ ){
    					if( ((Element)ls_1.get(j)).getName().equals("organization") ){
    						List ls_1_1 = ((Element)ls_1.get(j)).getChildren();
    						for( int y=0; y<ls_1_1.size(); y++ ){
    							if( ((Element)ls_1_1.get(y)).getName().equals("title") ){
    								((Element)ls_1_1.get(y)).setText(title);
    							}
    						}
    					}
    				}
    			}
    		}
    	}else{
    		//아이템을 찾아서 타이틀을 수정한다
    		for( int i=0; i<ls.size(); i++ ){
    			if( ((Element)ls.get(i)).getName().equals("organizations") ){
    				List ls_1 = ((Element)ls.get(i)).getChildren();
    				for( int j=0; j<ls_1.size(); j++ ){
    					if( ((Element)ls_1.get(j)).getName().equals("organization") ){
    						List ls_1_1 = ((Element)ls_1.get(j)).getChildren();
    						for( int y=0; y<ls_1_1.size(); y++ ){
    							if( this.searchInnerItem((Element)ls_1_1.get(y), itemId) != null ){
    								List ls_1_1_1 = ((Element)ls_1_1.get(y)).getChildren();
    								for( int k=0; k<ls_1_1_1.size(); k++ ){
    									if( ((Element)ls_1_1_1.get(k)).getName().equals("title") ){
    										((Element)ls_1_1_1.get(k)).setText(title);
    									}
    								}
    							}
    						}
    					}
    				}
    			}
    		}
    	}
    	
    	result = saveManifestFile(doc, path);
		
		return result;
	}
	
	/**
	 * 드롭위치의 아이템을 드래그아이템으로 변경
	 * @param doc
	 * @param iValue
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean changeItem(ItemValue iValue, Map<String, Object> commandMap) throws Exception{
		
		boolean result = true;
		String path = Globals.MANIFEST_PATH + commandMap.get("userid")+"/imsmanifest.xml";
		String itemId = (String)commandMap.get("addItemId");
		
		Document doc = readManifestFile(path);
		Element el = doc.getRootElement();
		List ls = el.getChildren();
		for( int i=0; i<ls.size(); i++ ){
			if( ((Element)ls.get(i)).getName().equals("organizations") ){
    			List ls_1 = ((Element)ls.get(i)).getChildren();
    			for( int j=0; j<ls_1.size(); j++ ){
    				if( ((Element)ls_1.get(j)).getName().equals("organization") ){
    					List ls_1_1 = ((Element)ls_1.get(j)).getChildren();
    					boolean checkSequencing = false;
    					for( int y=0; y<ls_1_1.size(); y++ ){
    						String name = ((Element)ls_1_1.get(y)).getName();
    						if( name.equals("item") ){
    							Element item = this.searchInnerItem((Element)ls_1_1.get(y), itemId);
    							if( item != null ){
    								identifierref = item.getAttributeValue("identifierref");
    								this.setItem(item.getChildren(), iValue);
    								if( childrenSeq.equals("Y") ){
    									List seqList = item.getParentElement().getChildren();
    									for( int h=0; h<seqList.size(); h++ ){
    										if( ((Element)seqList.get(h)).getName().equals("sequencing") ){
    											// imsmanifest파일과 관계없이 교과부에 마춰 계층적선형모델 시퀀싱 적용
    			    							this.setHierarchialLinearModel("children", ((Element)seqList.get(h)));
    			    							checkSequencing = true;
    										}
    									}
    									if( !checkSequencing ){
    										// imsmanifest파일과 관계없이 교과부에 마춰 계층적선형모델 시퀀싱 적용
    										Element sequencing = new Element("sequencing", na4);
    										item.getParentElement().addContent(sequencing);
    										this.setHierarchialLinearModel("children", sequencing);
    										checkSequencing = false;
    									}else{
    										checkSequencing = false;
    									}
    								}
    							}
    						}else if( name.equals("sequencing") ){
    							// imsmanifest파일과 관계없이 교과부에 마춰 계층적선형모델 시퀀싱 적용
    							this.setHierarchialLinearModel("parent", (Element)ls_1_1.get(y));
    							checkSequencing = true;
    						}
    					}
    					if( !checkSequencing ){
    						// imsmanifest파일과 관계없이 교과부에 마춰 계층적선형모델 시퀀싱 적용
    						Element sequencing = new Element("sequencing", na4);
    						((Element)ls_1.get(j)).addContent(sequencing);
    						this.setHierarchialLinearModel("parent", sequencing);
    					}
    				}
    			}
    		}else if( ((Element)ls.get(i)).getName().equals("resources") ){
				List ls_1 = ((Element)ls.get(i)).getChildren();
				boolean chk = false;
				if( ls_1 != null && ls_1.size() > 0 ){
					for( int j=0; j<ls_1.size(); j++ ){
						Element resource = this.searchInnerResource((Element)ls_1.get(j), identifierref);
						if( resource != null ){
							this.setResource((Element)ls_1.get(j), resource, iValue);
							chk = true;
						}
					}
					if( !chk ){
						this.setResource((Element)ls.get(i), null, iValue);
					}
				}else{
					this.setResource((Element)ls.get(i), null, iValue);
					
				}
			}
		}
		
		result = saveManifestFile(doc, path);
		
		return result;
	}
	
	
	public boolean setItem(List ls, ItemValue iValue) throws Exception{
		boolean result = true;
		try{
			Element target = null;
			Element parent = ((Element)ls.get(0)).getParentElement();
			if( !iValue.getTitle().equals("") ){
				target = this.createElement(parent, ls, "title");
				target.setText(iValue.getTitle());
			}
			if( !iValue.getCompletionThreshold().equals("") ){
				target = this.createElement(parent, ls, "completionThreshold");
				target.setText(iValue.getCompletionThreshold());
				target.setNamespace(na1);
			}
			if( !iValue.getSequencing().equals("") ){
				target = this.createElement(parent, ls, "sequencing");
				target.setNamespace(na4);
				List ls_1 = target.getChildren();
				if( !iValue.getRolluprules().equals("") ){
					target = this.createElement(target, ls_1, "rollupRules");
					target.setNamespace(na4);
					target.setAttribute("rollupObjectiveSatisfied", iValue.getRollupObjectiveSatisfied());
					target.setAttribute("rollupProgressCompletion", iValue.getRollupProgressCompletion());
					target.setAttribute("objectiveMeasureWeight", iValue.getObjectiveMeasureWeight());
				}
			}
			if( !iValue.getPresentation().equals("") ){
				target = this.createElement(parent, ls, "presentation");
				target.setNamespace(na3);
				if( !iValue.getNavigationInterface().equals("") ){
					List ls_1 = target.getChildren();
					target = this.createElement(target, ls_1, "navigationInterface");
					target.setNamespace(na3);
					if( !iValue.getHideLMSUI().equals("") ){
						List ls_1_1 = target.getChildren();
						target = this.createElement(target, ls_1_1, "hideLMSUI");
						target.setText(iValue.getHideLMSUI());
						target.setNamespace(na3);
					}
				}
			}
			
			
		}catch(Exception ex){
			result = false;
			ex.printStackTrace();
		}
		return result;
	}
	
	public boolean setResource(Element parent, Element resource, ItemValue iValue) throws Exception{
		boolean result = true;
		try{
			if( iValue.getRsrcHref() != null && !iValue.getRsrcHref().equals("")){
				if( resource != null ){
					List ls = resource.getChildren();
					resource.getContent().removeAll(ls);
				}else{
					resource = new Element("resource");
					parent.addContent(resource);
				}
				resource.setAttribute("identifier", identifierref);
				resource.setAttribute("type", iValue.getRsrcType());
				resource.setAttribute("scormType", iValue.getRsrcScormType(), na1);
				resource.setAttribute("href", iValue.getRsrcHref());
				
				resource.setAttribute("rsrc", iValue.getRsrcSeq()); //파일등록을 위해 임시로 등록한다. 실제 사용되지않음
				
				Vector fileHref = iValue.getFileHref();
				for( int i=0; i<fileHref.size(); i++ ){
					Element file = new Element("file");
					resource.addContent(file);
					file.setAttribute("href", (String)fileHref.get(i));
				}
			}
			
			
		}catch(Exception ex){
			result = false;
			ex.printStackTrace();
		}
		return result;
	}
	
	
	public Element createElement(Element parent, List ls, String name){
		Element el = null;
		boolean check = false;
		if( ls != null && ls.size() > 0 ){
			for( int i=0; i<ls.size(); i++ ){
				if( ((Element)ls.get(i)).getName().equals(name) ){
					el = (Element)ls.get(i);
					check = true;
				}
			}
			if( !check ){
				Element addElement = new Element(name);
				parent.addContent(addElement);
				el = addElement;
			}
		}else{
			Element addElement = new Element(name);
			parent.addContent(addElement);
			el = addElement;
		}
		return el;
	}
	
	
	public boolean updateItem(Map<String, Object> commandMap) throws Exception{
		boolean result = true;
		
		ItemValue iValue = new ItemValue();
		
		// 추가되는 manifest파일 위치
		String path = Globals.CONTNET_REAL_PATH+commandMap.get("baseDir")+"imsmanifest.xml";
		String itemId = (String)commandMap.get("oldItemId");
		String rsrcSeq = itemId.split("@@")[1];
		itemId = itemId.split("@@")[0];
		iValue.setRsrcSeq(rsrcSeq);
		
		
		Document doc = readManifestFile(path);
		Element el = doc.getRootElement();
		List ls = el.getChildren();
		
		for( int i=0; i<ls.size(); i++ ){
			if( ((Element)ls.get(i)).getName().equals("organizations") ){
    			List ls_1 = ((Element)ls.get(i)).getChildren();
    			for( int j=0; j<ls_1.size(); j++ ){
    				if( ((Element)ls_1.get(j)).getName().equals("organization") ){
    					List ls_1_1 = ((Element)ls_1.get(j)).getChildren();
    					for( int y=0; y<ls_1_1.size(); y++ ){
    						String name = ((Element)ls_1_1.get(y)).getName();
    						if( name.equals("item") ){
    							Element item = this.searchInnerItem((Element)ls_1_1.get(y), itemId);
    							if( item != null ){
    								this.copyItem(path, item, iValue); // 해당아이템의 정보 복사
    							}
    						}else if( name.equals("sequencing") ){
    							
    						}
    					}
    				}
    			}
    		}else if( ((Element)ls.get(i)).getName().equals("resources") ){
				List ls_1 = ((Element)ls.get(i)).getChildren();
				for( int j=0; j<ls_1.size(); j++ ){
					if( iValue.getIdentifierref() != null ){
						Element resource = this.searchInnerResource((Element)ls_1.get(j), iValue.getIdentifierref());
						if( resource != null ){
							// resource정보복사
							this.copyResource(path, resource, iValue);
						}
					}
				}
			}
		}
		
		//신규아이템으로 변경
		result = this.changeItem(iValue, commandMap);
		
		return result;
	}
	
	public boolean newUpdateItem(Map<String, Object> commandMap) throws Exception{
		boolean result = true;
		
		ItemValue iValue = new ItemValue();
		
		// 추가되는 manifest파일 위치
		String path = Globals.CONTNET_REAL_PATH+commandMap.get("baseDir")+"imsmanifest.xml";
		String itemId = (String)commandMap.get("oldItemId");
		String rsrcSeq = itemId.split("@@")[1];
		itemId = itemId.split("@@")[0];
		String selectVal = (String)commandMap.get("selectVal");
		iValue.setRsrcSeq(rsrcSeq);
		selectVal = selectVal.split("#")[0];
		
		Document doc = readManifestFile(path);
		Element el = doc.getRootElement();
		List ls = el.getChildren();
		
		for( int i=0; i<ls.size(); i++ ){
			if( ((Element)ls.get(i)).getName().equals("organizations") ){
				List ls_1 = ((Element)ls.get(i)).getChildren();
				for( int j=0; j<ls_1.size(); j++ ){
					if( ((Element)ls_1.get(j)).getName().equals("organization") ){
						List ls_1_1 = ((Element)ls_1.get(j)).getChildren();
						for( int y=0; y<ls_1_1.size(); y++ ){
							String name = ((Element)ls_1_1.get(y)).getName();
							if( name.equals("item") ){
								Element item = this.searchInnerItem((Element)ls_1_1.get(y), itemId);
								if( item != null ){
									this.newCopyItem(path, item, iValue, selectVal); // 해당아이템의 정보 복사
									break;
								}
							}
						}
					}
				}
			}else if( ((Element)ls.get(i)).getName().equals("resources") ){
				List ls_1 = ((Element)ls.get(i)).getChildren();
				for( int j=0; j<ls_1.size(); j++ ){
					if( iValue.getIdentifierref() != null ){
						Element resource = this.searchInnerResource((Element)ls_1.get(j), iValue.getIdentifierref());
						if( resource != null ){
							// resource정보복사
							this.copyResource(path, resource, iValue);
						}
					}
				}
			}
		}
		
		//신규아이템으로 변경
		result = this.changeItem(iValue, commandMap);
		
		return result;
	}
	
	
	
	
	public void copyItem(String path, Element fromItem, ItemValue iValue){
		Element item = null;
		String name = fromItem.getName();
		
		iValue.setIdentifier(fromItem.getAttributeValue("identifier"));
		iValue.setIdentifierref(fromItem.getAttributeValue("identifierref"));
		
		List ls = fromItem.getChildren();
		if( ls != null && ls.size() > 0 ){
			this.copyItemChildren(ls, iValue);
		}
		
	}
	
	public void newCopyItem(String path, Element fromItem, ItemValue iValue, String selectVal){
		Element item = null;
		String name = fromItem.getName();
		
		iValue.setIdentifier(fromItem.getAttributeValue("identifier"));
		iValue.setIdentifierref(fromItem.getAttributeValue("identifierref"));
		
		List ls = fromItem.getChildren();
		if( ls != null && ls.size() > 0 ){
			this.newCopyItemChildren(ls, iValue, selectVal);
		}
		
	}
	
	public void copyItemChildren(List ls, ItemValue iValue){
		
		for( int i=0; i<ls.size(); i++ ){
			String subname = ((Element)ls.get(i)).getName();
			Element item = (Element)ls.get(i);
			if( subname.equals("title") ){
				iValue.setTitle(item.getText());
			}else if( subname.equals("completionThreshold") ){
				iValue.setCompletionThreshold(item.getText());
			}else if( subname.equals("sequencing") ){
				iValue.setSequencing("Y");
			}else if( subname.equals("rollupRules") ){
				iValue.setRolluprules("Y");
				iValue.setRollupObjectiveSatisfied(item.getAttributeValue("rollupObjectiveSatisfied"));
				iValue.setRollupProgressCompletion(item.getAttributeValue("rollupProgressCompletion"));
				iValue.setObjectiveMeasureWeight(item.getAttributeValue("objectiveMeasureWeight"));
			}else if( subname.equals("presentation") ){
				iValue.setPresentation("Y");
			}else if( subname.equals("navigationInterface") ){
				iValue.setNavigationInterface("Y");
			}else if( subname.equals("hideLMSUI") ){
				iValue.setHideLMSUI(item.getText());
			}else if( subname.equals("file") ){
				iValue.setFileHref(item.getAttributeValue("href"));
			}
			List ls2 = ((Element)ls.get(i)).getChildren();
			if( ls2 != null && ls2.size() > 0 ){
				this.copyItemChildren(ls2, iValue);
			}
		}
	}
	
	public void newCopyItemChildren(List ls, ItemValue iValue, String selectVal){
		
		for( int i=0; i<ls.size(); i++ ){
			String subname = ((Element)ls.get(i)).getName();
			Element item = (Element)ls.get(i);
			if( subname.equals("title") ){
				iValue.setTitle(item.getText());
			}else if( subname.equals("completionThreshold") ){
				iValue.setCompletionThreshold(item.getText());
			}else if( subname.equals("sequencing") ){
				iValue.setSequencing("Y");
			}else if( subname.equals("rollupRules") ){
				iValue.setRolluprules("Y");
				iValue.setRollupObjectiveSatisfied(item.getAttributeValue("rollupObjectiveSatisfied"));
				iValue.setRollupProgressCompletion(item.getAttributeValue("rollupProgressCompletion"));
				iValue.setObjectiveMeasureWeight(item.getAttributeValue("objectiveMeasureWeight"));
			}else if( subname.equals("presentation") ){
				iValue.setPresentation("Y");
			}else if( subname.equals("navigationInterface") ){
				iValue.setNavigationInterface("Y");
			}else if( subname.equals("hideLMSUI") ){
				iValue.setHideLMSUI(item.getText());
			}else if( subname.equals("file") ){
				iValue.setFileHref(item.getAttributeValue("href"));
			}
			List ls2 = ((Element)ls.get(i)).getChildren();
			if( ls2 != null && ls2.size() > 0 && !selectVal.equals("ORG") ){
				this.newCopyItemChildren(ls2, iValue, selectVal);
			}
		}
	}
	
	
	public Element copyResource(String path, Element fromResource, ItemValue iValue){
		Element resource = null;
		String name = fromResource.getName();
		
		iValue.setRsrcType(fromResource.getAttributeValue("type"));
		iValue.setRsrcScormType(fromResource.getAttributeValue("scormType", na1));
		iValue.setRsrcHref(fromResource.getAttributeValue("href"));
		
		List ls = fromResource.getChildren();
		if( ls != null && ls.size() > 0 ){
			this.copyItemChildren(ls, iValue);
		}
		
		return resource;
	}
	
	/**
	 * 계층적 선형 모델(hierarchical linear model) sequencing추가
	 * @param position
	 * @param sequencing
	 * @return
	 * @throws Exception
	 */
	public boolean setHierarchialLinearModel(String position, Element sequencing) throws Exception{
		boolean result = true;
		try{
			List ls = sequencing.getChildren();
			if( ls != null && ls.size() > 0 ){
				sequencing.getContent().removeAll(ls);
			}
			Element controlMode = new Element("controlMode");
			controlMode.setNamespace(na4);
			controlMode.setAttribute("choice", "true");
			controlMode.setAttribute("choiceExit", "true");
			controlMode.setAttribute("flow", "true");
			if( position.equals("children") ){
				controlMode.setAttribute("forwardOnly", "false");
				controlMode.setAttribute("useCurrentAttemptObjectiveInfo", "true");
				controlMode.setAttribute("useCurrentAttemptProgressInfo", "true");
			}
			sequencing.addContent(controlMode);
			
			/*
			 * 학습목표의 만족여부를 롤업, 학습진도의 완료여부를 롤업, 평가 점수 롤업 가중치는 1.0으로 설정한다.
			 */
			Element rollupRules = new Element("rollupRules", na4);
			rollupRules.setAttribute("rollupObjectiveSatisfied", "true");
			rollupRules.setAttribute("rollupProgressCompletion", "true");
			rollupRules.setAttribute("objectiveMeasureWeight", "1.0");
			sequencing.addContent(rollupRules);
			
			if( position.equals("parent") ){
				/*
				 * 자식 학습활동 모두에 대해서 이 학습활동에 대한 학습자의 학습수행이 Completed라면
				 * 부모 학습활동단위에 대한 학습수행 결과도 Completed로 한다.
				 */
				Element rollupRule = new Element("rollupRule", na4);
				rollupRule.setAttribute("childActivitySet", "all");
				rollupRules.addContent(rollupRule);
				Element rollupConditions = new Element("rollupConditions", na4);
				rollupConditions.setAttribute("conditionCombination", "any");
				rollupRule.addContent(rollupConditions);
				Element rollupCondition = new Element("rollupCondition", na4);
				rollupCondition.setAttribute("condition", "completed");
				rollupCondition.setAttribute("operator", "noOp");
				rollupConditions.addContent(rollupCondition);
				Element rollupAction = new Element("rollupAction", na4);
				rollupAction.setAttribute("action", "completed");
				rollupRule.addContent(rollupAction);
				
				/*
				 * 자식 학습활동 중 학습자의 학습수행이 하나라도 Completed가 아니라면
				 * 부모 학습활동 단위에 대한 학습수행 결과는 incomplete로 한다.
				 */
				rollupRule = new Element("rollupRule", na4);
				rollupRule.setAttribute("childActivitySet", "any");
				rollupRules.addContent(rollupRule);
				rollupConditions = new Element("rollupConditions", na4);
				rollupConditions.setAttribute("conditionCombination", "all");
				rollupRule.addContent(rollupConditions);
				rollupCondition = new Element("rollupCondition", na4);
				rollupCondition.setAttribute("condition", "completed");
				rollupCondition.setAttribute("operator", "not");
				rollupConditions.addContent(rollupCondition);
				rollupAction = new Element("rollupAction", na4);
				rollupAction.setAttribute("action", "incomplete");
				rollupRule.addContent(rollupAction);
				
				/*
				 * 자식 학습활동 모두에 대해서 이 학습활동에 대한 학습자의 학습수행이 Satisfied라면
				 * 부모 학습활동 단위에 대한 학습수행 결과도 Satisfied로 한다.
				 */
				rollupRule = new Element("rollupRule", na4);
				rollupRule.setAttribute("childActivitySet", "all");
				rollupRules.addContent(rollupRule);
				rollupConditions = new Element("rollupConditions", na4);
				rollupConditions.setAttribute("conditionCombination", "any");
				rollupRule.addContent(rollupConditions);
				rollupCondition = new Element("rollupCondition", na4);
				rollupCondition.setAttribute("condition", "satisfied");
				rollupCondition.setAttribute("operator", "noOp");
				rollupConditions.addContent(rollupCondition);
				rollupAction = new Element("rollupAction", na4);
				rollupAction.setAttribute("action", "satisfied");
				rollupRule.addContent(rollupAction);
				
				/*
				 * 자식 학습활동 중 학습자의 학습수행이 하나라도 Satisfied가 아니라면
				 * 부모 학습활동단위에 대한 학습수행 결과는 notSatisfied로 한다.
				 */
				rollupRule = new Element("rollupRule", na4);
				rollupRule.setAttribute("childActivitySet", "any");
				rollupRules.addContent(rollupRule);
				rollupConditions = new Element("rollupConditions", na4);
				rollupConditions.setAttribute("conditionCombination", "any");
				rollupRule.addContent(rollupConditions);
				rollupCondition = new Element("rollupCondition", na4);
				rollupCondition.setAttribute("condition", "satisfied");
				rollupCondition.setAttribute("operator", "not");
				rollupConditions.addContent(rollupCondition);
				rollupAction = new Element("rollupAction", na4);
				rollupAction.setAttribute("action", "notSatisfied");
				rollupRule.addContent(rollupAction);
			}else{
				/*
				 * 자식 학습활동 모두에 대해서 이 학습활동에 대한 학습자의 학습수행이 Satisfied라면
				 * 부모 학습활동단위에 대한 학습수행 결과도 Satisfied로 한다.
				 */
				Element rollupRule = new Element("rollupRule", na4);
				rollupRule.setAttribute("childActivitySet", "all");
				rollupRules.addContent(rollupRule);
				Element rollupConditions = new Element("rollupConditions", na4);
				rollupConditions.setAttribute("conditionCombination", "any");
				rollupRule.addContent(rollupConditions);
				Element rollupCondition = new Element("rollupCondition", na4);
				rollupCondition.setAttribute("condition", "satisfied");
				rollupCondition.setAttribute("operator", "noOp");
				rollupConditions.addContent(rollupCondition);
				Element rollupAction = new Element("rollupAction", na4);
				rollupAction.setAttribute("action", "satisfied");
				rollupRule.addContent(rollupAction);
				
				/*
				 * 자식 학습활동 모두에대해서 이 학습활동에 대한 학습자의 학습수행이 Completed라면
				 * 부모 학습활동단위에 대한 학습수행 결과도 Completed로한다.
				 */
				rollupRule = new Element("rollupRule", na4);
				rollupRule.setAttribute("childActivitySet", "all");
				rollupRules.addContent(rollupRule);
				rollupConditions = new Element("rollupConditions", na4);
				rollupConditions.setAttribute("conditionCombination", "any");
				rollupRule.addContent(rollupConditions);
				rollupCondition = new Element("rollupCondition", na4);
				rollupCondition.setAttribute("condition", "completed");
				rollupCondition.setAttribute("operator", "noOp");
				rollupConditions.addContent(rollupCondition);
				rollupAction = new Element("rollupAction", na4);
				rollupAction.setAttribute("action", "completed");
				rollupRule.addContent(rollupAction);
				
				/*
				 * 자식 학습활동 중 학습자의 학습수행이 하나라도 Satisfied가 아니라면
				 * 부모 학습활동단위에 대한 학습수행 결과는 nonSatisfied로한다.
				 */
				rollupRule = new Element("rollupRule", na4);
				rollupRule.setAttribute("childActivitySet", "any");
				rollupRules.addContent(rollupRule);
				rollupConditions = new Element("rollupConditions", na4);
				rollupConditions.setAttribute("conditionCombination", "any");
				rollupRule.addContent(rollupConditions);
				rollupCondition = new Element("rollupCondition", na4);
				rollupCondition.setAttribute("condition", "satisfied");
				rollupCondition.setAttribute("operator", "not");
				rollupConditions.addContent(rollupCondition);
				rollupAction = new Element("rollupAction", na4);
				rollupAction.setAttribute("action", "notSatisfied");
				rollupRule.addContent(rollupAction);
				
				/*
				 * 자식 학습활동 중 학습자의 학습수행이 하나라도 Completed가 아니라면
				 * 부모 학습활동단위에 대한 학습수행 결과는 incomplete로 한다.
				 */
				rollupRule = new Element("rollupRule", na4);
				rollupRule.setAttribute("childActivitySet", "any");
				rollupRules.addContent(rollupRule);
				rollupConditions = new Element("rollupConditions", na4);
				rollupConditions.setAttribute("conditionCombination", "any");
				rollupRule.addContent(rollupConditions);
				rollupCondition = new Element("rollupCondition", na4);
				rollupCondition.setAttribute("condition", "completed");
				rollupCondition.setAttribute("operator", "not");
				rollupConditions.addContent(rollupCondition);
				rollupAction = new Element("rollupAction", na4);
				rollupAction.setAttribute("action", "incomplete");
				rollupRule.addContent(rollupAction);
			}
				
		}catch(Exception ex ){
			result = false;
			ex.printStackTrace();
		}
		return result;
	}
	

	/**
	 * Item 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean removeItem(Map<String, Object> commandMap) throws Exception{
		boolean result = true;
		
		String path = Globals.MANIFEST_PATH+commandMap.get("userid")+"/imsmanifest.xml";
		String itemId = (String)commandMap.get("itemId");
		String type   = (String)commandMap.get("type");
		Vector identifierref = new Vector();
		
		
		Document doc = readManifestFile(path);
    	Element el = doc.getRootElement();
    	List ls = el.getChildren();
    	
    	if( type.equals("ORG") ){
    		String manifestPath = Globals.MANIFEST_PATH+commandMap.get("userid");
    		
    		
    		File exFile = new File(manifestPath, "imsmanifest.xml");
    		if( exFile.exists() ){
    			exFile.delete();
    		}
    		FileController file = new FileController();
    		file.makeManifest(manifestPath);
    	}else{
    		//아이템을 찾아서 타이틀을 수정한다
    		for( int i=0; i<ls.size(); i++ ){
    			if( ((Element)ls.get(i)).getName().equals("organizations") ){
    				List ls_1 = ((Element)ls.get(i)).getChildren();
    				for( int j=0; j<ls_1.size(); j++ ){
    					if( ((Element)ls_1.get(j)).getName().equals("organization") ){
    						List ls_1_1 = ((Element)ls_1.get(j)).getChildren();
    						for( int y=0; y<ls_1_1.size(); y++ ){
    							Element item = this.searchInnerItem((Element)ls_1_1.get(y), itemId);
    							if( item != null ){
    								if( !item.getAttributeValue("identifierref").equals("") ){
    									identifierref.add(item.getAttributeValue("identifierref"));
    								}
    								List children = item.getChildren();
    								for( int k=0; k<children.size(); k++ ){
    									if( ((Element)children.get(k)).getName().equals("item") ){
    										identifierref.add(((Element)children.get(k)).getAttributeValue("identifierref"));
    									}
    								}
    								item.getParent().removeContent(item);
    							}
    						}
    					}
    				}
    			}else if( ((Element)ls.get(i)).getName().equals("resources") ){
    				List ls_1 = ((Element)ls.get(i)).getChildren();
    				if( ls_1 != null && ls_1.size() > 0 ){
    					for( int j=0; j<ls_1.size(); j++ ){
    						for( int k=0; k<identifierref.size(); k++ ){
    							Element resource = this.searchInnerResource((Element)ls_1.get(j), (String)identifierref.get(k));
    							if( resource != null ){
    								resource.getParent().removeContent(resource);
    							}
    						}
    					}
    				}
    			}
    		}
    		result = saveManifestFile(doc, path);
    	}
    	
		
		return result;
		
	}
	
	public String[] getRsrcSeq(Map<String, Object> commandMap) throws Exception{
		Vector result = new Vector();
		String path = Globals.MANIFEST_PATH+commandMap.get("userid")+"/imsmanifest.xml";
		Document doc = readManifestFile(path);
    	Element el = doc.getRootElement();
    	List ls = el.getChildren();
    	
    	for( int i=0; i<ls.size(); i++ ){
    		if( ((Element)ls.get(i)).getName().equals("resources") ){
    			List ls_1 = ((Element)ls.get(i)).getChildren();
    			for( int j=0; j<ls_1.size(); j++ ){
    				result.add(((Element)ls_1.get(j)).getAttributeValue("rsrc"));
    				//((Element)ls_1.get(j)).removeAttribute("rsrc");
    			}
    		}
    	}
    	String[] seq = new String[result.size()];
    	for( int i=0; i<result.size(); i++ ){
    		seq[i] = (String)result.get(i);
    	}
    	
		return seq;
	}
	
	public ArrayList copyContent(List dirList, Map<String, Object> commandMap) throws Exception{
		boolean result = true;
		String path = Globals.MANIFEST_PATH+commandMap.get("userid")+"/imsmanifest.xml";
		
		
		String subj = (String)commandMap.get("subj");
		Document doc = readManifestFile(path);
		Element el = doc.getRootElement();
		List ls = el.getChildren();
		String scoFolder = "";
		String baseDir = "";
		String strSavePath = Globals.CONTNET_REAL_PATH+subj;
		
		
		FileController fc = new FileController();
		ArrayList imsPath = new ArrayList();
		String inputPath = strSavePath+"/"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"_"+fc.getRandomKey(5);
		
		
		new File(inputPath).mkdirs();
		
		
		log.info("new file : "+path);
		log.info("cop file : "+inputPath+"/imsmanifest.xml");
		
		new File(path).renameTo(new File(inputPath+"/imsmanifest.xml"));
		
		for( int i=0; i<ls.size(); i++ ){
			if( ((Element)ls.get(i)).getName().equals("resources") ){
				List ls_1 = ((Element)ls.get(i)).getChildren();
				for( int j=0; j<ls_1.size(); j++ ){
					Element resource = (Element)ls_1.get(j);
					for( int k=0; k<dirList.size(); k++ ){
						Map map = (Map)dirList.get(k);
						if( map.get("rsrcSeq").toString().equals(resource.getAttributeValue("rsrc")) ){
							String href = resource.getAttributeValue("href");
							scoFolder = href.substring(0, href.indexOf("/"));
							baseDir = (String)map.get("rsrcBaseDir");
						}
					}
					fc.copyContentFile(subj, scoFolder, baseDir, strSavePath, path, inputPath);
					resource.removeAttribute("rsrc");
				}
			}
		}
		imsPath.add(inputPath);
		return imsPath;
	}
	
}
