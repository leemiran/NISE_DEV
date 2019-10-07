<%@ page import="java.io.*,
				java.util.*,
				java.util.jar.*,
				javax.servlet.*,
				javax.servlet.http.*,
				javax.servlet.jsp.*"
				
	contentType="text/html; charset=euc-kr" 
%>
<%@ include file = "commonlib.h" %>
<%!
   public boolean debug = false;
   
   /********************************************/
   // Resources
   /********************************************/
   String setup4_defaultMessage = "<font color=black>Messages...</font>";
   String setup4_header = "<br>&nbsp;&nbsp;";
   String setup4_initMessage = setup4_header+"Loading from ";
   String setup4_serviceCtl = "Service Control";
   String setup4_addNewName = "# Add RD Service";
   String setup4_servercachingDirTitle="# Server Caching Directory";
   String setup4_servercachingDir="Server Caching Directory Path";
   String setup4_waspoolsTitle="# WAS Pool List";
   String setup4_waspools="Enter list of jndi names<br> with a space as a delimiter<br>(ie> jndi1 jndi2 jndi3 ...)";
   String setup4_add_success = setup4_header+"Adding a RD service has been finished successfully on the server..";
   String setup4_modify_success = setup4_header+"The selected service has been successfully modified on the server..";
   String setup4_modify_failure = setup4_header+"db.properties may be read-only. Please check the authority of the file. <a href=javascript:refreshPage()>Refresh</a>";
   String setup4_delete_success = setup4_header+"The selected service has been successfully deleted on the server..";
   String setup4_apply_success = setup4_header+"The current DB properties have been successfully applied to the server..";
   String setup4_not_exist_created = setup4_header+"db.properties does not exist in the given path. The file is newly created.";
   String setup4_not_exist_failed_to_create = setup4_header+"db.properties does not exist in the given path. Failed to create a new db.properties.";
   String setup4_classPath_error = setup4_header+"Please check the value of defined classPath in rdserversetup.config.";
   String setup4_copy_jdbcDriver_error = 
   setup4_header+"*Unable to copy JDBC driver files to the classpath <br>&nbsp;&nbsp;&nbsp;which may be because of one of following reasons...<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. Driver class is already loaded from the classpath.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. No jdbc class file name is defined internally.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3. JDBC driver files to copy do not exist.";
   String setup4_driver_not_found = " is not found under";
   String setup4_rdagent_not_exist = setup4_header+"rdagent.jsp does not exist on the RDServer home directory path. Please place the file on the path.";
   String setup4_rdagent_read_only = setup4_header+"rdagent.jsp may be read-only. Please make it writable. <a href=javascript:refreshPage()>Refresh</a>";
   String setup4_cache_not_exist = setup4_header+"cache.properties does not exist on the path. Please place the file.";
   String setup4_cache_read_only = setup4_header+"cache.properties may be read-only. Please make it writable. <a href=javascript:refreshPage()>Refresh</a>";
   String setup4_rd_properties_not_exist = setup4_header+"rd.properties does not exist on the path. Please place the file.";
   String setup4_rd_properties_read_only = setup4_header+"rd.properties may be read-only. Please make it writable. <a href=javascript:refreshPage()>Refresh</a>";
   String setup4_driverClassAlreadyLoaded = " driver class is already loaded, no need to copy the JDBC driver files.";
   String setup4_db_properties_ready_only_first_loaded = setup4_header+"db.properties may be read-only. Please check the authority of the file and click on <a href=javascript:refreshPage()>\"Refresh\"</a>.";
   String setup4_cacheDirMoved = "# This key has been moved to cache.properties, server.cachefiledir";
%>
<%
	if (debug)
	{
		System.out.println("//****************************************************************************");
		System.out.println("// step4.jsp; debug flag is set to TRUE");
		System.out.println("//****************************************************************************");
	}
	setMessage(setup4_defaultMessage);
	
	String serverHome = getServerConfigPath(out,application,request);
	String rdagentPath = serverHome+"/rdagent.jsp";
	String rdserverSetupConfigPath = serverHome+"/rdserversetup.config";
	String propertiesPath = "";
	String dbPath = "";
	String rdPath = "";
	String cachePath = "";
	String classPath = "";
	boolean moveToStep5 = false;
	String defaultServercaching_dir = null;
	

	//*****************************************************************
	// rdserversetup.config
	//*****************************************************************
   	if (debug)
   		System.out.println("step4.jsp; rdserverSetupConfigPath; "+rdserverSetupConfigPath);
	Properties conf = load(rdserverSetupConfigPath);
	propertiesPath = conf.getProperty("RDSERVER_PROPERTIES_DIR","");
	classPath = conf.getProperty("RDSERVER_LIB_DIR","");
	
	if(propertiesPath == null || propertiesPath.equals(""))
	{
		dbPath = serverHome+"/conf/db.properties";
		rdPath = serverHome+"/conf/rd.properties";
		cachePath = serverHome+"/conf/cache.properties";
	}
	else
	{
		dbPath = propertiesPath + "/db.properties";
		rdPath = propertiesPath + "/rd.properties";
		cachePath = propertiesPath + "/cache.properties";
	}
	
	String method = request.getParameter("method");
   	String index = request.getParameter("index");
   	String cmd = request.getParameter("cmd");
   	
	Properties rdConf = load(rdPath);
	String rdUseWASPool = "";
	if (rdConf == null)
	   addMessage(setup4_rd_properties_not_exist);
	else
	   rdUseWASPool = rdConf.getProperty("server.usewaspool","");
	
	if (debug)
	{
	   System.out.println();
	   System.out.println("step4.jsp; server.usewaspool; "+rdUseWASPool);
	   System.out.println("step4.jsp; method; "+method);
	}

	//*****************************************************************
	// rd.properties, rdagent.jsp and cache.properties
	//*****************************************************************
	boolean isWriteMode = canWriteOnRDPropOrRDAgent(rdPath,rdagentPath);
	boolean cachePropWritable = canWriteOnCacheProp(cachePath);
	String cachefiledir = request.getParameter("cachefiledir");
	if (debug)
		System.out.println("step4.jsp; cachefiledir; "+cachefiledir);
	/**
	  * cache.properties
	  *	=============================
	  * When cache.properties is writable, and the page is initially loaded
	  * ( when the command is null) or user defined cacheFolderPath exists,
	  * write the the value to the file.
	  */
	if (debug)
	{
		System.out.println("step4.jsp; ----------------------------------------------");
		System.out.println("step4.jsp; Set the cache directory value to cache.properties, server.cachefiledir");
	}
	if (cachePropWritable)
	{
		loadFromFile(cachePath);
		if (cmd == null && cachefiledir == null)
		{
		   cachefiledir = serverHome+"/cache";
		   put("server.cachefiledir",cachefiledir);
		   saveToFile(cachePath);
		}
		else if (cmd.equals("apply") && cachefiledir != null)
		{
		   put("server.cachefiledir",cachefiledir);
		   saveToFile(cachePath);
		}
		else
		{
			cachefiledir = getValue("server.cachefiledir","");
		}
	}
	
	/**
	  * rdagent.jsp, rd.properties
	  * ===============================
	  */
	if (method != null && rdUseWASPool != null)
	{
		// When the current connection pool method is different with how the method is defined in rd.properties,
		// we modify rd.proeprteis(server.usewaspool) and rdagent.jsp(waspool)
		if (rdUseWASPool.toUpperCase().equals("FALSE") && method.equals("WAS"))
		{
		   if (debug)
		      System.out.println("step4.jsp; Set to use WAS Pool");
		   /** Set to use WAS Pool **/
		   if (isWriteMode)
		   {
		   	   //rd.properties
			   loadFromFile(rdPath);
			   put("server.usewaspool","TRUE");
			   saveToFile(rdPath);
			   //rdagent.jsp
			   setUseWASPoolStateForRDAgent(rdagentPath,true);
		   }
		}
		else if (rdUseWASPool.toUpperCase().equals("TRUE") && method.equals("RDServer"))
		{
		   if (debug)
		      System.out.println("step4.jsp; Set to use RD Pool");
		   /** Set to use RD Pool **/
		   if (isWriteMode)
		   {
			   //rd.properties
			   loadFromFile(rdPath);
			   put("server.usewaspool","FALSE");
			   saveToFile(rdPath);
			   //rdagent.jsp
			   setUseWASPoolStateForRDAgent(rdagentPath,false);
		   }
		}
	}

	//*****************************************************************
	// db.properties
	//*****************************************************************
   	addMessage(setup4_initMessage+dbPath+"..");
   	loadFromFile(dbPath);
    
   	Vector services = getServices();
   	if (method == null || method.equals(""))
   		method = "RDServer";
   		
   	// Set to the global variables	
   	setMethod(method);
   	setServices(services);
   	setIndex(index);
   	setCommand(cmd);
   	
	RDService rdservice = getRDServiceFromRequest(request);

   	if (cmd != null)
   	{
   	    try
   	    {  	
		   	// Perform the appropriate action
			if (cmd.equals("add"))
		   	{
      
		       File file = new File(dbPath);
		       if (!file.canWrite())
		       {
		          addMessage(setup4_modify_failure);
		       }
		       else
		       {
			   	   addMessage(setup4_add_success);
			   	   addRDService(rdservice);
				   
				   // Put driver class names for a value of drivers key
				   put("drivers",getDriversAsString());
			   	   
			   	   applyRDServiceToProperties(dbPath,rdservice);
			   	   copyJDBCDriverFiles(serverHome,classPath,rdservice);
		   	   }
		   	}
		   	else if (cmd.equals("modify"))
		   	{
		       File file = new File(dbPath);
		       if (!file.canWrite())
		       {
		          addMessage(setup4_modify_failure);
		       }
		       else
		       {
			   	   addMessage(setup4_modify_success);
			   	   modifyRDService(rdservice);
				   
				   // Put driver class names for a value of drivers key
				   put("drivers",getDriversAsString());
			   	   
			   	   applyRDServiceToProperties(dbPath,rdservice);
		   	   }
		   	}
		   	else if (cmd.equals("delete"))
		   	{
		       File file = new File(dbPath);
		       if (!file.canWrite())
		       {
		          addMessage(setup4_modify_failure);
		       }
		       else
		       {
			   	   addMessage(setup4_delete_success);
			   	   deleteRDService(rdservice);
				   
				   // Put driver class names for a value of drivers key
				   put("drivers",getDriversAsString());
			   	   
			   	   deleteRDServiceInProperties(dbPath,rdservice);
		   	   }
		   	}
		   	else if (cmd.equals("apply"))
		   	{
		   	   String waspools = request.getParameter("waspools");
		   	   
		   	   
			   File file = new File(dbPath);
			   if (!file.canWrite())
			   {
			      addMessage(setup4_modify_failure);
			   }
			   else
			   {
			   	   addMessage(setup4_apply_success);
			   	   if (waspools != null)
			   	      put("waspools",waspools);
			   	   
			   	   if (debug)
			   	   {
				   	   System.out.println("step4.jsp; waspools; "+waspools);
			   	   }
			   	   
				   // Put driver class names for a value of drivers key
				   put("drivers",getDriversAsString());
	
			   	   saveToFile(dbPath);
			   	   moveToStep5 = true;
		   	   }
		   	}
	   	}
	   	catch (Exception e)
	   	{
	   	   e.printStackTrace();
	   	}
   	}
   	else
   	{
   		// Set the initial values, when the page is first loaded...
   		File dbFileFirstLoaded = new File(dbPath);
   		if (debug)
   			System.out.println("step4.jsp; dbFileFirstLoaded.canWrite(); "+dbFileFirstLoaded.canWrite());
   		if (!dbFileFirstLoaded.canWrite())
   			addMessage(setup4_db_properties_ready_only_first_loaded);
	    defaultServercaching_dir = request.getParameter("cachefiledir");
		if (debug)
		{
			System.out.println("step4.jsp; Initially loaded, hence set as follows..");
			System.out.println("step4.jsp; defaultServercaching_dir; "+defaultServercaching_dir);
		}
	    if (defaultServercaching_dir == null)
	    {
	    	defaultServercaching_dir = serverHome+"/cache";
			String cacheDirTest = getValue("servercaching_dir",null);
			if (cacheDirTest != null)
			{
				remove("servercaching_dir");
				addComment(setup4_cacheDirMoved);
				put("#servercaching_dir",defaultServercaching_dir);
			}
	    }
	    saveToFile(dbPath);
   	}
%>
<%!
   // Loading a file specified by the path, separately from the main loading
   public Properties load(String path)
   {
	  try
	  {
	     Properties conf = new Properties();
	  	 File f = new File(path);
	  	 FileInputStream inputStream = new FileInputStream(f);
	     conf.load(inputStream);
	     return conf;
   	  }
   	  catch (Exception e)
   	  {
   	     e.printStackTrace();
   	     return null;
      }
   }
   
   public void setUseWASPoolStateForRDAgent(String path, boolean state)
   {
	  try
	  {
		 File file = new File(path);

		 BufferedReader	in = new BufferedReader(new	FileReader(file));

		 properties	= new Vector();
		 String	s =	null;
		 int index = -1;
		 int test = -1;
		 int t = -1;
		 String	key	= null;
		 String	value =	null;

		 while (true)
		 {
			s =	in.readLine();

			if (s == null)
			{
			   break;
			}
			
			test = s.indexOf("waspool");
			t = s.indexOf("boolean");
			if (test != -1 && t != -1)
			{
			    index =	s.indexOf("=");
			    key	= s.substring(0,index);
				if (state)
					value = " true;";
				else
					value = " false;";
				Pair p = new Pair(key,value);
				properties.addElement(p);
			}
			else
			{
			   properties.addElement(s);
			}
		 }
		 in.close();
		 
		 this.saveToFile(path);
	  }
	  catch	(IOException e)
	  {
		 e.printStackTrace();
	  }
   }
   
   public boolean canWriteOnRDPropOrRDAgent(String rdPath, String agentPath)
   {
      File rdFile = new File(rdPath);
      File agentFile = new File(agentPath);
      
      if (!agentFile.exists() || !agentFile.canWrite() || !rdFile.canWrite())
      {
	      if(!agentFile.exists())
	         addMessage(setup4_rdagent_not_exist);
	      else if (!agentFile.canWrite())
	         addMessage(setup4_rdagent_read_only);
	      
	      if (!rdFile.canWrite())
	         addMessage(setup4_rd_properties_read_only);
	      
	      return false;
      }
      else
      {
         return true;
      }
   }
   
   public boolean canWriteOnCacheProp(String cachePath)
   {
      File cacheFile = new File(cachePath);
      
      if (!cacheFile.exists() || !cacheFile.canWrite())
      {
	      if(!cacheFile.exists())
	         addMessage(setup4_cache_not_exist);
	      else if (!cacheFile.canWrite())
	         addMessage(setup4_cache_read_only);
	      
	      return false;
      }
      else
      {
         return true;
      }
   }
   //=====================================
   // Load & Store From/To the File
   //=====================================
   
   private Vector properties = null;

   public void loadFromFile(String path)
   {
	  try
	  {
		 File file = new File(path);

		 if(!file.exists())
		 {
		    boolean created = file.createNewFile();
		    if (created)
		    	setMessage(setup4_not_exist_created);
		    else
		    	setMessage(setup4_not_exist_failed_to_create);
		 }

		 BufferedReader	in = new BufferedReader(new	FileReader(file));

		 properties	= new Vector();
		 String	s =	null;
		 int index = -1;
		 String	key	= null;
		 String	value =	null;

		 while (true)
		 {
			s =	in.readLine();

			if (s == null)
			{
			   break;
			}

			s =	s.trim();
			if (s.startsWith("#"))
			{
			   properties.addElement(s);
			   continue;
			}
			else if	(s.startsWith("//"))
			{
			   properties.addElement(s);
			   continue;
			}
			else if	(s.startsWith("sample"))
			{
			   //properties.addElement(s);
			   continue;
			}
			else if	(s.equals(""))
			{
			   properties.addElement(s);
			   continue;
			}

			index =	s.indexOf("=");
			key	= s.substring(0,index).trim();
			if (! s.endsWith("="))
			{
			   value = s.substring(index+1).trim();
			}
			else
			{
			   value = "";
			}
			
			Pair p = new Pair(key,value);
			properties.addElement(p);
		 }
		 
		 in.close();
	  }
	  catch	(IOException e)
	  {
		 e.printStackTrace();
	  }
   }
   
   public String getValue(String myKey, String defaultValue)
   {
      String s = defaultValue;
      int size = 0;

      size = properties.size();

      for (int i=0; i<size; i++)
      {
         Object obj = properties.elementAt(i);
         if (obj instanceof String)
         {
         }
         else if (obj instanceof Pair)
         {
            Pair pair = (Pair) obj;
            String key = pair.getKey();
            if (key.equals(myKey))
            {
               s = pair.getValue();
               break;
            }
         }
      }
      return s;
   }
   
   public Pair getPair(String myKey)
   {
      Pair pair = null;
      int size = properties.size();
      for (int i=0; i<size; i++)
      {
         Object obj = properties.elementAt(i);
         if (obj instanceof String)
         {
         }
         else if (obj instanceof Pair)
         {
            Pair p = (Pair) obj;
            String key = p.getKey();
            if (key.equals(myKey))
            {
               pair = p;
               break;
            }
         }
      }
      return pair;
   }
   
   public void put(String key, String value)
   {
      Pair old = getPair(key);
      if (old == null)
      {
         Pair p = new Pair(key, value);
         properties.addElement(p);
      }
      else
      {
         if (value == null)
            value = "";
         old.setValue(value);
      }
   }
   
   public void remove(String key)
   {
      Pair old = getPair(key);
      if (old != null)
         properties.remove(old);
   }
   
   public void addComment(String comment)
   {
      properties.addElement(comment);
   }
   
   public void saveToFile(String path)
   {
      try
      {
		 //start executing backup and save
		 File file = new File(path);
		 if(file.exists())
		 {
		    String bakupf = path+System.currentTimeMillis()+".bak";
		    if (debug)
		    {
			    System.out.println("step4.jsp; Path; "+path);
			    System.out.println("step4.jsp; Backup File; "+bakupf);
		    }
		    
		    copyFile(path,bakupf);
		 }
		 //execute backup and save 
		 
         BufferedWriter out = new BufferedWriter(new FileWriter(path));
         store(out);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   public void store(BufferedWriter out)
   {
      try
      {
        //save to the file
        int size = properties.size();
        Pair pair = null;
        for (int i=0; i<size; i++)
        {
            Object obj = properties.elementAt(i);
            if (obj instanceof String)
            {
               out.write(obj.toString()+"\r\n");
            }
            else if (obj instanceof Pair)
            {
               pair = (Pair)obj;
               String key = pair.getKey();
               String value = pair.getValue();
               if (value == null)
                  value = "";
               out.write(key+"="+value+"\r\n");
            }
         }
         out.close();
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   public class	Pair
   {
	  private String key = null;
	  private String value = null;
	   
	  public Pair(String key, String value)
	  {
		 this.key =	key;
		 this.value = value;
	  }
	  public String getKey()
	  {
		 return key;
	  }
	  public String getValue()
	  {
		 return value;
	  }
	  public void	setValue(String	value)
      {
         this.value = value;
      }
      public void setKey(String key)
      {
         this.key = key;
	  }
      public String toString()
      {
         return key+"="+value;
      }
   }
   
   //=====================================
   // Managing RD Services
   //=====================================
   
   private int index = 0;
   private String command = "";
   private Vector services = null;
   private String message = "";
   private String method = "";
   
   public void printServices(JspWriter out)
   {
	  try
	  {
	      RDService rdservice = null;
	      for (int i =0; i<services.size(); i++)
	      {
	         rdservice = (RDService)services.elementAt(i);
	         out.write("=====================================\r\n<br>");
	         out.write(""+rdservice.getServiceName()+"\r\n<br>");
	         out.write("=====================================\r\n<br>");
	         out.write("url; "+rdservice.getURL()+"\r\n<br>");
	         out.write("user; "+rdservice.getUser()+"\r\n<br>");
	         out.write("password; "+rdservice.getPassword()+"\r\n<br>");
	         out.write("maxconn; "+rdservice.getMaxconn()+"\r\n<br>");
	         out.write("charset; "+rdservice.getCharset()+"\r\n<br>");
	         out.write("driver; "+rdservice.getDriver()+"\r\n<br>");
	         out.write("timeout; "+rdservice.getTimeout()+"\r\n<br>");
	         out.write("\r\n<br>");
	      }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   public void setMethod(String method)
   {
      this.method = method;
   }
   public String getMethod()
   {
      return method;
   }
   
   public void setServices(Vector services)
   {
      this.services = services;
   }
   public Vector getServices()
   {
	      Object obj = null;
	      Pair pair = null;
	      String key = null;
	      Vector services = new Vector();
	      String serviceName = null;
	      
          RDService rdservice = null;
          String[] keyStrings = null;
          String[] values = null;

	      for (int i=0;i<properties.size();i++)
	      {
	         obj = properties.elementAt(i);
	         if (obj instanceof Pair)
	         {
	            pair = (Pair)obj;
	            key = pair.getKey();
	            
	            if(key.endsWith(".url"))
	            {  
		            serviceName = key.substring(0, key.lastIndexOf("."));
		            
		            rdservice = new RDService();
		            keyStrings = rdservice.getKeyStrings();
		            values = new String[keyStrings.length];
		            
		            
		            for (int j=0;j<keyStrings.length;j++)
		            {
		               values[j] = getValue(serviceName+keyStrings[j],"");
		            }
		            
		            rdservice.setValues(serviceName, values);
		            services.addElement(rdservice);
               }
	        }
	     }
	     return services;
   }
   
   public RDService getRDServiceFromRequest(HttpServletRequest request)
   {
	   RDService rdservice = new RDService();
	   String[] keyStrings = rdservice.getKeyStrings();
	   String[] values = new String[keyStrings.length];
	   String serviceName = request.getParameter("serviceName");
	   if (debug)
	   {
	   	   System.out.println();
		   System.out.println("step4.jsp; Service Name; "+serviceName);
	   }
	   
	   for (int i=0; i<keyStrings.length; i++)
	   {
	      values[i] = request.getParameter(rdservice.keyStrings[i].substring(rdservice.keyStrings[i].indexOf(".")+1));
	      if (debug)
	      	System.out.println("step4.jsp; "+rdservice.keyStrings[i]+"; "+values[i]);
	   }
	   if (debug)
	      System.out.println();
	   rdservice.setValues(serviceName,values);
   	   return rdservice;
   }
   
   public void setIndex(String tabIndex)
   {
      int index = 0;
      // First, convert the value of index to number
      try
      {
         index = (new Integer(tabIndex)).intValue();
         this.index = index;
      }
      catch(NumberFormatException ne)
      {
         // We don't care even if we get the number format exception, since it's already assigned to 0.
      }
   }
   
   public void setCommand(String command)
   {
      this.command = command;
   }
   public String getCommand()
   {
      return command;
   }
   
   public void setMessage(String message)
   {
      this.message = message;
   }
   public String getMessage()
   {
      return message;
   }
   public void addMessage(String message)
   {
      this.message += message;
   }
   public void printMessage(JspWriter out)
   {
      try
      {
          out.write("<font color='red'>&nbsp;&nbsp;");
	      out.write(message);
	      out.write("</font>");
	      message = null;
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   public void addRDService(RDService rdservice)
   {
      services.addElement(rdservice);
   }
   public void modifyRDService(RDService rdservice)
   {
      String serviceName = rdservice.getServiceName();
      RDService service = null;
      
      for (int i=0; i<services.size(); i++)
      {
         service = (RDService)services.elementAt(i);
         if (service.getServiceName().equals(serviceName))
            service.setValues(serviceName,rdservice.getValues());
      }
   }
   public void deleteRDService(RDService rdservice)
   {
      String serviceName = rdservice.getServiceName();
      RDService service = null;
      
      for (int i=0; i<services.size(); i++)
      {
         service = (RDService)services.elementAt(i);
         if (service.getServiceName().equals(serviceName))
            services.removeElementAt(i);
      }
   }
   
   public void applyRDServiceToProperties(String dbPath, RDService rdservice)
   {
      String serviceName = rdservice.getServiceName();
	  String[] keyStrings = rdservice.getKeyStrings();
      String[] values = rdservice.getValues();

      for (int j=0; j<keyStrings.length; j++)
      {
         put(serviceName+keyStrings[j],values[j]);
      }

   	  saveToFile(dbPath);
   }
   
   public void deleteRDServiceInProperties(String dbPath, RDService rdservice)
   {
      String serviceName = rdservice.getServiceName();
	  String[] keyStrings = rdservice.getKeyStrings();

      for (int j=0; j<keyStrings.length; j++)
      {
         remove(serviceName+keyStrings[j]);
      }

      saveToFile(dbPath);
   }
   
   public void copyJDBCDriverFiles(String serverHome, String classPath, RDService rdservice)
   {
      try
      {
          if (debug)
             System.out.println("step4.jsp; Class Path; "+classPath);
          
		  File[] jdbcDriverFiles = getJDBCDriverFiles(serverHome, classPath, rdservice);
			   	   
		  boolean copied = false;
		  if (!classPath.equals(""))
		  {
			 copied = copyRDServerClass(jdbcDriverFiles,classPath);
			 if (!copied)
		        addMessage(setup4_copy_jdbcDriver_error);
		  }
		  else
	      {
	         setMessage(setup4_classPath_error);
	      }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   public File[] getJDBCDriverFiles(String serverHome, String classPath, RDService rdservice)
   {
      if (debug)
      {
         System.out.println();
         System.out.println("step4.jsp; getJDBCDriverFiles()");
         System.out.println("step4.jsp; Server Home; "+serverHome);
         System.out.println("step4.jsp; RDService Driver; "+rdservice.getDriver());
      }
      String driverClassName = rdservice.getDriver();
      String data = null;
      String folderName = null;
      String files = null;
      Vector list = new Vector();
      
      if (isDriverClassLoaded(rdservice.getDriver()))
      {
         addMessage("<br>&nbsp;&nbsp;"+rdservice.getServiceName()+"("+rdservice.getDriver()+") "+setup4_driverClassAlreadyLoaded);
         return new File[0];
      }
         	
      for (int i=0;i<templates.length;i++)
      {
         if (templates[i][1].equals(driverClassName))
         {
            data = templates[i][3];
            break;
         }
      }
      
      if (data.equals(""))
         return null;
      
      folderName = data.substring(0,data.indexOf("@"));
      files = data.substring(data.indexOf("@")+1);
      
      StringTokenizer tokenizer = new StringTokenizer(files,"|");
      File[] driverFiles = new File[tokenizer.countTokens()];
      File file = null;
      File targetFile = null;
      String driverPath = null;
      String fileName = null;
      int j = 0;
      while (tokenizer.hasMoreTokens())
      {
         fileName = ""+tokenizer.nextToken();
         driverPath = serverHome+"/lib/"+folderName+"/"+fileName;
         file = new File(driverPath);
         targetFile = new File(classPath+"/"+fileName);
         
         if (debug)
         	System.out.println("step4.jsp; targetFile.exists() "+targetFile.exists());
         	
         if (!file.exists())
         	addMessage("<br>&nbsp;&nbsp;"+fileName+setup4_driver_not_found+" "+serverHome+"/lib/"+folderName);
         	
         if (file.exists())
         	driverFiles[j] = file;
         else
         	driverFiles[j] = null;
         	
         j++;
         if (debug)
         {
            System.out.println("step4.jsp; Driver Path; "+driverPath);
         }
      }
      
      return driverFiles;
   }
   
   public boolean isDriverClassLoaded(String driverClassName)
   {
	  try
	  {
	     Class rdserver = Class.forName(driverClassName);
	     return true;
	  }
	  catch(Exception e)
	  {
	     return false;
	  }
   }

   public String getDriversAsString()
   {
      Vector drivers = getUpdatedDriverList();
      StringBuffer buffer = new StringBuffer();
      for (int i = 0; i < drivers.size(); i++)
      {
         buffer.append(""+drivers.elementAt(i));
         if (i != (drivers.size()-1) && !drivers.elementAt(i).equals(""))
            buffer.append(" ");
      }

      return buffer.toString();
   }
   
   public Vector getUpdatedDriverList()
   {
      RDService rdservice = null;
      RDService rdser = null;
      
      String test = null;
      String t = null;
      
      Vector drivers = new Vector();
      boolean duplicate = false;
      
      for (int i = 0; i < services.size(); i++)
	  {
	     rdservice = (RDService)services.elementAt(i);
         test = rdservice.getDriver();
    	 if (test == null)
    	    test = "";
    	 duplicate = false;
		 for (int j = (i+1); j < services.size(); j++)
		 {
		    rdser = (RDService)services.elementAt(j);
		    t = rdser.getDriver();
		    if (test.equals(t))
		       duplicate = true;
		 }
		 if (!duplicate)
		    drivers.addElement(test);
	  }
   	  return drivers;
   }
   
   public void buildTabs(JspWriter out)
   {
      try
      {
      	  if (debug)
             System.out.println("step4.jsp; Currrent Index; "+index);
	      RDService rdservice = null;
	      
	      if (0 < services.size())
	      {
		      out.write("var tabs = new Array(");
		      if (index > (services.size()-1))
		         	index = services.size()-1;
		      for (int i=0;i<services.size();i++)
		      {
		            rdservice = (RDService)services.elementAt(i);
		            if (i == index && i == (services.size()-1))
		            	out.write("\""+rdservice.getServiceName()+"|*\"\r\n");
		            else if (i == index)
		            	out.write("\""+rdservice.getServiceName()+"|*\",\r\n");
		            else if (i == (services.size()-1))
		            	out.write("	\""+rdservice.getServiceName()+"|\"\r\n");
		            else
			            out.write("	\""+rdservice.getServiceName()+"|\",\r\n");	            
		      }
		      out.write(" 	);\r\n");
		      out.write("	return tabs;\r\n");
	      }
	      else
	      {
	         out.write("	var tabs = new Array();\r\n");
	         out.write("	return tabs;\r\n");
	      }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   public void setCurrentServicePanel(JspWriter out)
   {
      try
      {
      	  if (services.size() == 0)
      	  	return;
	      String name = "";
	      RDService rdservice = (RDService)services.elementAt(index);
	      String[] keyStrings = rdservice.getKeyStrings();
	      String[] labels = rdservice.getLabels();
	      String[] values = rdservice.getValues();
	      if (values == null)
	      	throw new Exception("Values of a rd service haven't been set.");
	      
	      out.write("var html = \"\";\n");
	      //out.write("var tab = tabs[ID].split(\"|\");\n"); //Questionable..
		  out.write("html += 		\"<input type ='hidden' name='serviceName' value='"+rdservice.getServiceName()+"'/>\";\r\n");
	      out.write("html += \"<table CLASS='tabFrame'>\";\r\n");
	      for (int i=0;i<keyStrings.length;i++)
	      {
	    	  out.write("html += \"<tr>\";\r\n");
	    	  out.write("html += 		\"<td bgcolor=#EFDFFF colspan=2>\";\r\n");
	    	  out.write("html += 		\""+labels[i]+"\";\r\n");
	    	  out.write("html += 		\"</td>\";\r\n");
	    	  out.write("html += \"</tr>\";\r\n");
	    	  out.write("html += \"<tr>\";\r\n");
	    	  out.write("html += 		\"<td width=200>"+rdservice.getServiceName()+rdservice.keyStrings[i]+"</td>\";\r\n");
	    	  name = rdservice.keyStrings[i].substring(rdservice.keyStrings[i].indexOf(".")+1);
	    	  if (name.equals("charset"))
	    	  {
			 	out.write("html += 		\"<td>\";\r\n");
			 	out.write("html += 		\"<input type ='text' name='"+name+"' size=43 value='"+values[i]+"' class='style2'/>&nbsp;\";\r\n");
			  	out.write("html += 		\"<select name='charsets' onClick='onClickCharSet()'>\";\r\n");
				for (int j = 0; j< charsets.length; j++)
				{
					if (values[i].equals(charsets[j]))
						out.write("html += 			\"<option value='"+charsets[j]+"' selected>"+charsets[j]+"</option>\";\r\n");
					else
						out.write("html += 			\"<option value='"+charsets[j]+"'>"+charsets[j]+"</option>\";\r\n");
				}
			 	out.write("html += 		\"</select>\";\r\n");
			 	out.write("html += 		\"<td>\";\r\n");
	    	  }
	    	  else
	    	  {
	    	  	out.write("html += 		\"<td><input type ='text' name='"+name+"' size=76 value='"+values[i]+"' class='style2'/></td>\";\r\n");
	    	  }
	    	  out.write("html += \"</tr>\";\r\n");
    	  }
    	  // Add service control buttons (modify, delete)
    	  out.write("html += \"<tr>\";\r\n");
	   	  out.write("html += 		\"<td bgcolor=#EFDFFF colspan=2 align='right'>\";\r\n");
	   	  out.write("html += 		\"<a href='javascript:modifyRDService();'><img src='images/modify_up.gif' border=0></a>&nbsp;\";\r\n");
	   	  out.write("html += 		\"<a href='javascript:deleteRDService();'><img src='images/delete_up.gif' border=0></a>\";\r\n");
	   	  out.write("html += 		\"</td>\";\r\n");
	   	  out.write("html += \"</tr>\";\r\n");
	   	  
	      out.write("html += \"</table>\";\n");
   	      out.write("Properties.innerHTML = html;\n"); 
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   public void buildHiddenFormForNewProperties(JspWriter out)
   {
      try
      {
	      RDService rdservice = new RDService();
	      String[] keyStrings = rdservice.getKeyStrings();
	      
	      out.write("<form name='setProperties' method='post' action='setup4.jsp?cmd=add&index="+services.size()+"&method="+getMethod()+"'>\r\n");
	      out.write("	<input type ='hidden' name='serviceName' value=''/>\r\n");
	      for (int i=0;i<keyStrings.length;i++)
	      {
	    	  out.write("	<input type ='hidden' name='"+rdservice.keyStrings[i].substring(rdservice.keyStrings[i].indexOf(".")+1)+"' value=''/>\r\n");
    	  }
	   	  out.write("</form>");
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   public void buildDBSelectComboBox(JspWriter out)
   {
		try
		{
			out.println("<select name='dbKind' "+getMethodStatusForField("RDServer")+" size='1'>");
			out.println( "<option selected "+getMethodStatusForField("RDServer")+" value=''>Choose DB</option>");
				
			for(int i = 0 ; i < templates.length ; i++ )
				out.println( "<option value='"+templates[i][0]+"'>"+ templates[i][0] +" </option>");
			out.println("</select>");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
   }
   
   public String getMethodStatusForRadio(String type)
   {
      if (type.equals(method))
      	return "checked";
      else
      	return "";
   }
   public String getMethodStatusForField(String type)
   {
      if (!type.equals(method))
      	return "disabled";
      else
      	return "";
   }
%>
<html>
 <style>
 <%@ include file="setup.css" %>
 </style>
<!-------------------------------------------------------->
<!-- JavaScript ------------------------------------------>
<!-------------------------------------------------------->
 <script language="JavaScript">
 <!--
  function callNextSetup()
  {
     //document.step4.submit();
     var method = "";
	 for (i=0;i<document.connection.method.length;i++)
	 {
	 	if (document.connection.method[i].checked==true)
	 		method = document.connection.method[i].value;
	 }
	 
     document.serviceControl.action="setup4.jsp?cmd=apply&method="+method;
     document.serviceControl.submit();
  }
  function callPrevSetup()
  {
     history.back();
  }

  // Add a new rd service
  function addRDService(newServiceName, dbKind)
  {
     if (newServiceName == null || newServiceName == "")
     {
     	alert("Please enter RD service name first");
     	return;
     }
	 var newWin = window.open("setup_setNewProperties.jsp?name="+ newServiceName+"&dbKind="+dbKind, "NewWindow",'Width=625,Height=475,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=0');
	 self.name = "MainWindow";
	 newWin.opener = self;
     newWin.focus();
  }
  function modifyRDService()
  {
     var method = "";
	 for (i=0;i<document.connection.method.length;i++)
	 {
	 	if (document.connection.method[i].checked==true)
	 		method = document.connection.method[i].value;
	 }
	 
     document.rdservice.action="setup4.jsp?cmd=modify&method="+method;
     document.rdservice.submit();
  }
  function deleteRDService()
  {
     var method = "";
	 for (i=0;i<document.connection.method.length;i++)
	 {
	 	if (document.connection.method[i].checked==true)
	 		method = document.connection.method[i].value;
	 }
	 
     document.rdservice.action="setup4.jsp?cmd=delete&method="+method;
     document.rdservice.submit();
  }
  function reload(index)
  {
     var method = "";
	 for (i=0;i<document.connection.method.length;i++)
	 {
	 	if (document.connection.method[i].checked==true)
	 		method = document.connection.method[i].value;
	 }
	 
     document.rdservice.action="setup4.jsp?cmd=reload&index="+index+"&method="+method;
     document.rdservice.submit();
  }

  var tabs = buildTabs();

  function buildTabs()
  {
  	<% buildTabs(out);%>
  }

  function tabOnClick(ID)
  {
	   var oElement = null;
	   // Set the style for tabs not selected
	   for (var i = 0; i < tabs.length; i++)
	   {
	      oElement = document.getElementById(i);
	      oElement.className = "tabOff";
	   }
	   // Set the style for a tab selected
	   oElement = document.getElementById(ID);
	   oElement.className = "tabOn";
	   
	   // Set the current service panel
	   <% setCurrentServicePanel(out); %>
	   
	   document.body.focus();
  }

  function tabLoad()
  {
	   var HTML = "";
	   HTML += "<P ALIGN='LEFT'>";
	   // Draw Tabs (Buttons)
	   for (var i = 0; i < tabs.length; i++)
	   {
		    var tab = tabs[i].split("|");
		    HTML += "<input type='BUTTON' ID="+i+" class='tabOff' value='"+tab[0]+"' onClick='reload("+i+");'>&nbsp;";
	   }
	   Services.innerHTML = HTML;
	
	   // Find the default tab and make it selected
	   for (var i = 0; i < tabs.length ; i++)
	   {
		    var tab = tabs[i].split("|");
		    if (tab[1] == "*")
		    {
			     tabOnClick(i);
			     break;
		    }
	   }
  }
  
  function setConnectionPoolMethod(type)
  {
     if (type == "RDServer")
     {
	     document.serviceControl.waspools.disabled = true;
	     document.serviceControl.dbKind.disabled = false;
	     document.serviceControl.newServiceName.disabled = false;
	     document.serviceControl.method.value = "RDServer";
	 }
	 else
	 {
	     document.serviceControl.waspools.disabled = false;
	     document.serviceControl.dbKind.disabled = true;
	     document.serviceControl.newServiceName.disabled = true;
	     document.serviceControl.method.value = "WAS";
	 }
  }
  
  function onClickCharSet()
  {
     document.rdservice.charset.value = document.rdservice.charsets.value;
  }
  
  function refreshPage()
  {
     location.refreshPage();
  }
  -->
 </script>

 <body class="header" onLoad="tabLoad()" leftmargin="20" topmargin="10" marginwidth="0" marginheight="0">
  
  <form name="step4" method="post" action="setup5.jsp">
  </form>
  <%
  	try
  	{
	  	if (moveToStep5)
	  		out.write("<script>document.step4.submit();</script>\r\n");
  	}
  	catch (Exception e)
  	{
  		e.printStackTrace();
  	}
  %>
  
  <%buildHiddenFormForNewProperties(out);%>

<!---------------------------------------------------------------------->
<!-- STEP Header ------------------------------------------------------->
<!---------------------------------------------------------------------->
   <table>
    <td bgcolor=#3399CC  width=600 height=2 colspan=2></td>
    <tr>
    <td bgcolor=#94B6EF  width=550 height=30><b>Report Designer5.0 Setup</td><td bgcolor=#3300CC width=50 align=center><font color=#ffffff> Step 4</td>
    <tr>
    <td bgcolor=#DBDBEA  colspan=2 align=center>RDServer Environment Setup (db)</td>
    <tr>
    <td bgcolor=#3399CC  height=2 colspan=2></td>
    <tr>
   </table>

   <br>

<!--///////////////Navigation Button Begining--->
   <table>
      <tr>
      <td align=right>
         <a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
      </td>
      </tr>
      <tr>
         <td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
      </tr>
   </table>
<!--///////////////Navigation Button END --->

<!---------------------------------------------------------------------->
<!-- Main Screen ------------------------------------------------------->
<!---------------------------------------------------------------------->
   <%
	   try
	   {
	   	   if (debug)
	   	      this.printServices(out);
		   if (getMessage() != null)
		   {
		      if (!getMessage().equals(""))
			      printMessage(out);
			  else
			  	out.write("<br>");
		   }
		   else
		   {
		      out.write("<br>");
		   }
	   }
	   catch (Exception e)
	   {
	      e.printStackTrace();
	   }
   %>
   <table>
	   <!-- Control Panel : Start -------------------------------------->
	   <FORM name="connection" method="post">
       <tr>
          <td bgcolor=#3399CC  width=605 height=2 colspan=2></td>
       </tr>
	   <tr>
		   <td width=605 bgcolor=#EFDFFF>
		      <b><%=setup4_serviceCtl%></b>
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      Connection Pool Method
		      <input type="radio" name="method" value="RDServer" <%=getMethodStatusForRadio("RDServer")%> onClick="javascript:setConnectionPoolMethod('RDServer');">RDServer
		      <input type="radio" name="method" value="WAS" <%=getMethodStatusForRadio("WAS")%> onClick="javascript:setConnectionPoolMethod('WAS');">WAS
		   </td>
	   </tr>
	   </FORM>
	   <!-- Control Panel : End ----------------------------------------->
	   
	   <!-- Services : Begin --------------------------------------------> 
	   <tr>
			<td>
			    <FORM name="rdservice" method="post">
				<DIV ID="Services"></DIV>
				<DIV ID="Properties"></DIV>
				</FORM>
			</td>
	   </tr>
	   <!-- Services : End ----------------------------------------------> 
   </table>
   <table width=610>
      <FORM name="serviceControl" method="post">
        <input type ="hidden" name="method" value="<%=getMethod()%>"/>
	   	<tr>
			<td bgcolor=#EFDFFF ><b><%= setup4_addNewName %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
			<td bgcolor=#EFDFFF ><% buildDBSelectComboBox(out); %></td>
			<td bgcolor=#EFDFFF ><input type ="text" name="newServiceName" size=58 <%=getMethodStatusForField("RDServer")%> value="" class="style2"></td>
	    </tr>
		<tr>
			<td colspan=3 align="right">
				<a href="javascript:addRDService(document.serviceControl.newServiceName.value,document.serviceControl.dbKind.value);"><img src="images/add_up.gif" border=0></a>
			</td>
		</tr>
        <tr>
            <td bgcolor=#3399CC  width=605 height=2 colspan=3></td>
        </tr>
      	<tr>
			<td bgcolor=#EFDFFF colspan=3>
				<b><%= setup4_waspoolsTitle %></b>
			</td>
		</tr>
		<tr>
			<td>
				<%= setup4_waspools %>
			</td>
			<td colspan=2>
				<input type ="text" name="waspools" size=77 <%=getMethodStatusForField("WAS")%> value="<%= getValue("waspools","") %>" class="style2">
			</td>
	    </tr>
	    
      	<tr>
			<td bgcolor=#EFDFFF colspan=3>
				<b><%= setup4_servercachingDirTitle %></b>
			</td>
		</tr>
		<tr>
			<td>
				<%= setup4_servercachingDir %>
			</td>
			<td colspan=2>
				<input type ="text" name="cachefiledir" size=77 value=
				"<%
				try
				{
					if (defaultServercaching_dir != null)
	      				out.print(defaultServercaching_dir);
	      			else
	        			out.print(cachefiledir);
        		}
        		catch (Exception e)
        		{
        		}
        		%>"
        		class="style2">
			</td>
	    </tr>
        <tr>
            <td bgcolor=#3399CC  width=605 height=2 colspan=3></td>
        </tr>
      </FORM>
   </table>
<!----------------------------------------------------------------------->
<!--///////////////Navigation Button Begining--->
   <table>
      <tr>
	      <td width=605 align=right>
	         <a href="javascript:callNextSetup();"><img src="images/next_up.gif" border=0></a>
	      </td>
      </tr>
   </table>
 </body>
</html>