<%@ page language="java" import="java.util.*,java.io.*,m2soft.rdsystem.server.core.rddbagent.util.*"%>
<%JUpload fu = new JUpload();
try{
boolean ret = fu.readForm(request,response,"/usr/local/tomcat6/webapps/rdServer/pdf",true);
 if(!ret) {out.println("error["+fu.getLastErrMsg()+"]");}
}catch (Throwable ex) {out.println("error["+RDUtil.toHangleEncode(RDUtil.getStackTraceAsString(ex),"EUC_KR")+"]");}
//}catch (Throwable ex) {out.println("error["+RDUtil.toHangleDecode(RDUtil.getStackTraceAsString(ex),"EUC_KR")+"]");}
//}catch (Throwable ex) {out.println("error["+RDUtil.toHangleEncode(ex.toString(),"EUC_KR")+"]");}
//}catch (Throwable ex) {out.println("error["+RDUtil.toHangleDecode(ex.toString(),"EUC_KR")+"]");}
%>
