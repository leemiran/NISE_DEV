<%!
  public boolean copyFile(String s1, String s2) throws Exception
  {
      boolean ret = false;
      FileInputStream fin = null;
      FileOutputStream fout = null;
      try
      {
         File file = new File(s1);
         fin = new FileInputStream(file);
         fout = new FileOutputStream(s2);
         byte[] buf = new byte[1024];
         int real = 0;
         while (true)
         {
            real = fin.read(buf);
            if (real <= 0)
            {
               break;
            }
            if (real < buf.length)
            {
               fout.write(buf,0,real);
               fout.flush();
               break;
            }
            fout.write(buf);
            fout.flush();

         }

         ret = true;


      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw e;
      }
      finally
      {
         if(fin != null)
            fin.close();

         if(fout != null)
            fout.close();
      }

      return ret;
   }

   public boolean extractToClass(String jarFile,String targetDir) throws Exception
   {
      boolean ret = false;

      byte abyte0[] = new byte[4096];

      JarEntry jarentry;

      JarInputStream jarinputstream = new JarInputStream(new FileInputStream(jarFile));

      FileOutputStream outputstream = null;

      try
      {
         while((jarentry = jarinputstream.getNextJarEntry()) != null)
         {
            if(!"META-INF/MANIFEST.MF".equals(jarentry.getName()))
            {
               if(jarentry.isDirectory())
               {
                  File f = new File(targetDir,jarentry.getName());
                  if(!f.exists())
                     f.mkdirs();
               }
               else
               {
                  int i = -1;
                  outputstream = new FileOutputStream(new File(targetDir,jarentry.getName()));

                  while((i = jarinputstream.read(abyte0)) != -1)
                     outputstream.write(abyte0, 0, i);

                  outputstream.flush();
                  outputstream.close();
               }
            }
         }

         ret = true;

      }
      catch (Exception ex)
      {
         throw ex;
      }
      finally
      {
         if(jarinputstream != null)
            jarinputstream.close();
      }
      return ret;
   }

   public boolean copyRDServerClass(File[] jarFile,String targetDir) throws Exception
   {
      boolean ret = false;

      if(jarFile == null || targetDir == null)
         return ret;

      boolean isclassesDir = false;

      if(targetDir.lastIndexOf("classes") != -1)
         isclassesDir = true;

      try
      {
         //if it is the case of unzipping jar file
         if(isclassesDir)
         {
            for (int i = 0; i< jarFile.length ; i++)
            {
               //if the file has already  been exised in the destination directory, Plz assign the NULL to the corresponding INDEX ELEMENT of jarFile.
               // if the case of NULL, jump out.
               if (jarFile[i] != null)
               {
                  ret = extractToClass(jarFile[i].toString(),targetDir);
               }
            }
         }
         // the case of copying the jar file into lib
         else
         {
            for (int i = 0; i< jarFile.length ; i++)
            {
               //if the file has already  been exised in the destination directory, Plz assign the NULL to the corresponding INDEX ELEMENT of jarFile.
               // if the case of NULL, jump out.
               if (jarFile[i] != null)
               {
	               String name = jarFile[i].getName();
	
	               ret = copyFile(jarFile[i].toString(), targetDir+"/"+name);
	               if(!ret)
	                  break;
               }
            }
         }

      }
      catch (Exception ex)
      {
         throw ex;
      }

      return ret;
   }
   
   public String getServerConfigPath(JspWriter out,ServletContext application,HttpServletRequest request)
   {
     String tmpRDHome = application.getRealPath(request.getServletPath());
     String tmpRDHomeDir = "";

     if(tmpRDHome.indexOf("RDServer") != -1)
     {
        tmpRDHomeDir = new File(new File(tmpRDHome).getParent()).getParent();
        tmpRDHomeDir = tmpRDHomeDir.replace('\\','/');
     }
     else
     {
         try {
            out.println ("<font color=red> setup.jsp has to be run in the path where \"RDServer\" is included (ie. RDServer/setup.jsp)</font><br><br>");
         }
         catch (Exception ex) {
         }
         
     }
     
     return tmpRDHomeDir;
   }
   
   public String getStackTraceAsString(Throwable e) 
   {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    e.printStackTrace(printWriter);
    StringBuffer error = stringWriter.getBuffer();
    return error.toString();
   }
   
   public static void saveRdAgentConfigPath(String agentFilepath,String configPath)
   {
      StringBuffer contentbuffer = new StringBuffer();

      File f = null;
      String findString = "setRDServerSetupConfigDir(\"";
      RandomAccessFile rdagentFile  = null;
      FileWriter fwriter = null;
      try
      {
         f = new File(agentFilepath);
         rdagentFile = new RandomAccessFile(f,"r");
         String line;
         int inx = -1;
         while((line = rdagentFile.readLine()) != null)
         {
            inx = line.indexOf(findString);
            if(inx != -1)
            {
               inx += findString.length();
               String setpath = line.substring(0,inx);
               setpath = setpath + configPath +"\");";
               contentbuffer.append(setpath+"\n");
            }
            else
            contentbuffer.append(line+"\n");
         }

         if(rdagentFile != null){
            rdagentFile.close();
            rdagentFile = null;
         }
         
         if(!f.canWrite())
            f.delete();

         fwriter = new FileWriter(f);
         fwriter.write(contentbuffer.toString().substring(0,contentbuffer.toString().length()-1)); //delete the last \n 

      }
      catch (Exception ex) {
      }
      finally
      {
         if(rdagentFile != null)
         try {
            rdagentFile.close();
         }
         catch (Exception e) {
         }

         if(fwriter != null)
         try {
            fwriter.close();
         }
         catch (Exception ex) {
         }
      }
   }
   
   public static void saveServerExportRdUrl(String exportFilepath,String serverUrl)
   {
      StringBuffer contentbuffer = new StringBuffer();

      File f = null;
      //String findString = "http://serverip:port/RDServer/rdagent.jsp";
      String findString = "http://serverip:port/RDServer";
      RandomAccessFile exportFile  = null;
      FileWriter fwriter = null;
      try
      {
         f = new File(exportFilepath);
         exportFile = new RandomAccessFile(f,"r");
         String line;
         int inx = -1;
         while((line = exportFile.readLine()) != null)
         {
            inx = line.indexOf(findString);
            if(inx != -1)
            {
               //String setpath = "String url = \"" + serverUrl + "/rdagent.jsp\";";
               String setpath = line.substring(0,inx)+ serverUrl + line.substring((inx+findString.length()));
               contentbuffer.append(setpath+"\n");
            }
            else
            contentbuffer.append(line+"\n");
         }

         if(exportFile != null){
            exportFile.close();
            exportFile = null;
         }
         
         if(!f.canWrite())
            f.delete();

         fwriter = new FileWriter(f);
         fwriter.write(contentbuffer.toString().substring(0,contentbuffer.toString().length()-1)); //delete the last \n

      }
      catch (Exception ex) {
      }
      finally
      {
         if(exportFile != null)
         try {
            exportFile.close();
         }
         catch (Exception e) {
         }

         if(fwriter != null)
         try {
            fwriter.close();
         }
         catch (Exception ex) {
         }
      }
   }

   public static void saveRequestFormRdUrl(String JSPpath,String ip_port)
   {
      StringBuffer contentbuffer = new StringBuffer();

      File f = null;
      String findString = "rdserverip:port";
      RandomAccessFile JspFile  = null;
      FileWriter fwriter = null;
      try
      {
         f = new File(JSPpath);
         JspFile = new RandomAccessFile(f,"r");
         String line;
         int inx = -1;
         while((line = JspFile.readLine()) != null)
         {
            inx = line.indexOf(findString);
            if(inx != -1)
            {
					String str1 = line.substring(0, inx);
					String str2 = line.substring(inx + 15);
               contentbuffer.append(str1+ip_port+str2+"\n");
            }
            else
            contentbuffer.append(line+"\n");
         }

         if(JspFile != null){
            JspFile.close();
            JspFile = null;
         }
         
         if(!f.canWrite())
            f.delete();

         fwriter = new FileWriter(f);
         fwriter.write(contentbuffer.toString().substring(0,contentbuffer.toString().length()-1)); // delete the last \n

      }
      catch (Exception ex) {
      }
      finally
      {
         if(JspFile != null)
         try {
            JspFile.close();
         }
         catch (Exception e) {
         }

         if(fwriter != null)
         try {
            fwriter.close();
         }
         catch (Exception ex) {
         }
      }
   }

   public static String getWindowSystemDir() {
        String osname = System.getProperty("os.name").toString().toLowerCase();
        if(osname.indexOf("win") == -1)
           return null;

        String userhomedrv = System.getProperty("user.home").toString().toLowerCase().substring(0,2);
        String targetSystemdir ="";
        if((osname.indexOf("nt") != -1) || (osname.indexOf("2000") != -1)){
          targetSystemdir += userhomedrv+"/WINNT/System32/";
          if(!new File(targetSystemdir).exists())
             targetSystemdir = userhomedrv+"/WINDOWS/System32/";
        }
        else if (osname.indexOf("xp") != -1 || osname.indexOf("2003") != -1)
        {
           targetSystemdir += userhomedrv+"/WINDOWS/System32/";
           if(!new File(targetSystemdir).exists())
               targetSystemdir = userhomedrv+"/WINNT/System32/";
        }
        else
           targetSystemdir += userhomedrv+"/WINDOWS/System/";

        return targetSystemdir;
    }
    
   public String changeKeyValue(String target,String search_key,String chanage_value)
   {
      String retString = target;
      if(target != null)
      {
         int inx = -1;
         inx = target.indexOf(search_key);
         if(inx != -1)
         {
            retString =target.substring(0,inx+search_key.length()+1)+chanage_value;
         }
      }
      return retString;
   }
   

   // {dbKind, driverClassName, urlTemplate, jdbcDriverFileName}
   // where jdbcDriverFileName -> "FolderName@FileName1|FileName2|.."
   private String[][] templates = 
				{
				{"odbc", "sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc:DATA_SOURCE_NAME",""}, 
				{"oraclethin", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@dbserverip:port:sidname","oracle@classes12.jar"}, 
				{"oracleoci", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:oci8:@sidname","oracle@classes12.jar"}, 
				{"db2", "COM.ibm.db2.jdbc.net.DB2Driver", "jdbc:db2://dbserverip:port/libname","db2@db2java.zip|db2jcc.jar|jt400.jar|sqlj.zip"}, 
				{"db2app", "COM.ibm.db2.jdbc.app.DB2Driver", "jdbc:db2:dbname","db2@db2java.zip|db2jcc.jar|jt400.jar|sqlj.zip"}, 
				{"db2as400", "com.ibm.as400.access.AS400JDBCDriver", "jdbc:as400://dbserverip/libname","db2@db2java.zip|db2jcc.jar|jt400.jar|sqlj.zip"}, 
				{"mssql", "com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:microsoft:sqlserver://dbserverip:port;databasename=database","mssql@msbase.jar|mssqlserver.jar|msutil.jar"}, 
				{"mysql", "org.gjt.mm.mysql.Driver", "jdbc:mysql://dbserverip:port/databasename?useUnicode=true&characterEncoding=EUC-KR","mysql@mm.mysql-2.0.4-bin.jar|mysql_comp.jar|mysql_uncomp.jar"}, 
				{"sybase", "com.sybase.jdbc2.jdbc.SybDriver", "jdbc:sybase:Tds:dbserverip:port/databasename?CHARSET=eucksc","sybase@jconn2.jar"}, 
				{"altibase", "Altibase.jdbc.driver.AltibaseDriver", "jdbc:Altibase://serverip:port/databasename",""},
				{"informix", "com.informix.jdbc.IfxDriver", "jdbc:informix-sqli://dbserverip:port/mydatabasename:INFORMIXSERVER=demo_server",""}
				};
				
   private String[] charsets = { "EUC_KR", "ISO8859_1", "SHIFT_JIS","ASCII", "Cp1252", "UnicodeBig", "UnicodeBigUnmarked", "UnicodeLittle", "UnicodeLittleUnmarked", "UTF-8", "UTF-16" };
				
   public class RDService
   {
      // The number of keyStrings elements has to be the same with the number of service elements defined as String
      private String[] keyStrings = {".url",".user",".password",".maxconn",".charset",".driver",".timeout"};
      // Labels for each of key strings. THE NUMBER OF VALUES HAS TO BE THE SAME.
      private String[] labels = {"# DB URL","# DB User ID","# DB User Password","# Maximum Number of Connections","# DB Character Set","# DB Driver","# Conn Timeout in mins"};
      
      private String serviceName = "";
      
      private String url = null;
      private String user = null;
      private String password = null;
      private String maxconn = null;
      private String charset = null;
      private String driver = null;
      private String timeout = null;
      
      private String[] values = null;

	  public RDService() {}
	  
      public RDService(String serviceName, String url, String user, String password, String maxconn, String charset, String driver, String timeout)
      {
         this.serviceName = serviceName;
         this.url = url;
         this.user = user;
         this.password = password;
         this.maxconn = maxconn;
         this.charset = charset;
         this.driver = driver;
         this.timeout = timeout;
      }
      
      public String[] getKeyStrings()
      {
         return keyStrings;
      }
      public String[] getLabels()
      {
         return labels;
      }
      
      public void setValues(String serviceName, String[] values)
      {
         if (keyStrings.length != values.length)
         {
            System.out.println("Error reported in RDServer; ");
            System.out.println("The length of values has to be the same with the length of keyStrings");
         }
         
         this.serviceName = serviceName;
         url = values[0];
         user = values[1];
         password = values[2];
         maxconn = values[3];
         charset = values[4];
         driver = values[5];
         timeout = values[6];
         
         this.values = values;
      }
      public String[] getValues()
      {
         return values;
      }
      
      public String getServiceName()
      {
         return serviceName;
      }
      
      public void setURL(String url)
      {
         this.url = url;
      }
      public String getURL()
      {
         return url;
      }
      
      public void setUser(String user)
      {
         this.user = user;
      }
      public String getUser()
      {
         return user;
      }
      
      public void setPassword(String password)
      {
         this.password = password;
      }
      public String getPassword()
      {
         return password;
      }
      
      public void setMaxconn(String maxconn)
      {
         this.maxconn = maxconn;
      }
      public String getMaxconn()
      {
         return maxconn;
      }
      
      public void setCharset(String charset)
      {
         this.charset = charset;
      }
      public String getCharset()
      {
         return charset;
      }
      
      public void setDriver(String driver)
      {
         this.driver = driver;
      }
      public String getDriver()
      {
         return driver;
      }
      
      public void setTimeout(String timeout)
      {
         this.timeout = timeout;
      }
      public String getTimeout()
      {
         return timeout;
      }
   }
%>
