package egovframework.com.cmm;

import egovframework.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;

public class ImagePaginationRenderer extends AbstractPaginationRenderer {
	
	public ImagePaginationRenderer() {
		firstPageLabel    = "<a href=\"#\" onclick=\"{0}({1}); \"><img src=\"/images/adm/common/pg_btnPrv1.gif\" 	alt=\"처음\"   border=\"0\"/></a>&#160;"; 
        previousPageLabel = "<a href=\"#\" onclick=\"{0}({1}); \"><img src=\"/images/adm/common/pg_btnPrv2.gif\"    alt=\"이전\"   border=\"0\"/></a>&#160;&nbsp;&nbsp;";
//        currentPageLabel  = "<strong><span class='on'>{0}</span></strong>&#160;&nbsp;&nbsp;"; 
        currentPageLabel  = "<a href=\"#\" class=\"on\" title=\"현재페이지\">{0}</a>&#160;&nbsp;&nbsp;"; 
        otherPageLabel    = "<a href=\"#\" onclick=\"{0}({1}); \">{2}</a>&#160;&nbsp;&nbsp;";
        nextPageLabel     = "<a href=\"#\" onclick=\"{0}({1}); \"><img src=\"/images/adm/common/pg_btnNext2.gif\"   alt=\"다음\"   border=\"0\"/></a>&#160;";
        lastPageLabel     = "<a href=\"#\" onclick=\"{0}({1}); \"><img src=\"/images/adm/common/pg_btnNext1.gif\" 	alt=\"마지막\" border=\"0\"/></a>&#160;";
	}
}
