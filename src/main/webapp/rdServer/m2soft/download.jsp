<%@page import="m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.Message,java.io.*" %><%@include file="properties.h" %><%String downName =request.getParameter("filename");       FileInputStream fis = null;OutputStream outs = null; int bufferSize = 4096;
      try
      {
         int nRead = 0;
         byte buffer [] = new byte[ bufferSize ];
	 	 if(downName == null)
		    return;
         if(downencoding)
            downName = RDUtil.toHangleDecode(downName);

         String fname = downName.substring(downName.lastIndexOf("/")+1);
         String waskind = getServletConfig().getServletContext().getServerInfo().toLowerCase();

         if ( downName.length() > 0 ) {
            if((waskind.indexOf("jeus") == -1) &&( fname.indexOf(".htm") != -1 || fname.indexOf(".txt") != -1 ||  fname.indexOf(".log") != -1)){
               if( fname.indexOf(".log") != -1)
                  fis = new FileInputStream( downName );
               else
                   fis = new FileInputStream( webpath+"/"+ downName );
               boolean encoding = false;
               if(waskind.indexOf("tomcat") != -1)
                  response.setContentType(Message.get("RDUtil_CharSettomcat"));
               else if(waskind.indexOf("apachejserv") != -1)
                  encoding = true;
               else
                  response.setContentType(Message.get("RDUtil_CharSetnormal"));

               if(encoding){
                  while((nRead = fis.read(buffer)) != -1){
                     out.print(RDUtil.toHangleDecode(new String(buffer,0,nRead)));
                  }
               }
               else{
                  while((nRead = fis.read(buffer)) != -1){
                     out.print(new String(buffer,0,nRead));
                  }
               }
            }else{
               response.setContentType("application/octet-stream");
               //response.setContentType("image/jpeg");
               response.setHeader( "Content-Disposition", "attachment;filename=" + fname + ";" );
               outs = response.getOutputStream();
               fis = new FileInputStream( webpath+"/"+ downName );
               while ( ( nRead = fis.read( buffer ) ) != -1 ) {
                  outs.write( buffer, 0, nRead );
               }
            }
         }

       }finally{
         if(fis != null)
            fis.close();
         if(outs != null){
            outs.close();
            outs.flush();
         }
      }%>