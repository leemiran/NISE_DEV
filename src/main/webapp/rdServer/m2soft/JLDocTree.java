
<%!
public class JLDocTree extends JLTreeCtrl
{
   String userFullpath = null;
   javax.servlet.jsp.JspWriter out = null;
   boolean encoding = false;
   JLDocTree(javax.servlet.jsp.JspWriter out)
   {
      super(out);
      this.out = out;
      setClassName("JLDocTree");
      //2003.10.02 kokim 
      //this.userFullpath = mrdfile_abs_dir+"/"+sessionUser;
      this.userFullpath = mrdfile_abs_dir+"/mrd";
   }
   
   Vector getSegment(String sParent)
   {
      Vector vSeg = new Vector();
      File f = new File(userFullpath);
      if(!f.exists()){
         boolean ret = f.mkdir();
         if(!ret){
            try{
               out.println("<script>alert('Cannot create "+sessionUser+" folder');</script>");
            }catch (Exception e){
            }
            return vSeg;
         }
      }
         
      //2003.10.02 kokim
      Vector v = new Vector();
      //v.addElement(sessionUser);
      //v.addElement(sessionUser);
      v.addElement("mrd");
      v.addElement("mrd");
      v.addElement("0");
      if(f.listFiles().length == 0)
         v.addElement("-1");
      else
         v.addElement(Integer.toString(f.listFiles().length));
      vSeg.addElement(v);
      vSeg = makeDirTree(f,vSeg);
      
      return vSeg;
   }

	// kokim 2004.10.15	
  Vector makeDirTree(File startDir,Vector rootTree){
      
	  // 디렉토리 권한 관리를 파일에서 하는 경우
	  if(docinfo.equals("no"))
	  {
		  File flist[] = startDir.listFiles();
		  m2soft.rdsystem.server.core.rddbagent.beans.BeansDirAuthentication dirAuth = new m2soft.rdsystem.server.core.rddbagent.beans.BeansDirAuthentication();

		  for(int i=0; i<flist.length; i++){
			 
			 if(flist[i].isDirectory()){
					String DirFullPath = (String)flist[i].getParent() + "\\" + (String)flist[i].getName();
					
					Vector vSeg = new Vector();
					try
					{
						Vector user = dirAuth.getDirectoryUser(DirFullPath);

						if(user.isEmpty())
						{
							dirAuth.insert(DirFullPath, "admin;");
						}
						
						if(dirAuth.IsPermittedUser(DirFullPath, sessionUser))
						{
							File child[] = flist[i].listFiles();
							Vector mrdv = new Vector();
							mrdv.addElement((String)flist[i].getName());
							mrdv.addElement((String)flist[i].getName());
							mrdv.addElement((String)startDir.getName());
							if(child.length == 0) 
							   mrdv.addElement("-1");
							else
							   mrdv.addElement(Integer.toString(child.length));
							
							rootTree.addElement(mrdv);
							makeDirTree(flist[i],rootTree);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}					
					finally
					{
					}
							
			}
			 else
			 {
				BeansFileDescription fd = new BeansFileDescription();
				String filedesc = fd.getdesc((String)flist[i].getName());

				if(filedesc == null)
					filedesc = "";
				String file_name_desc = filedesc + '(' + (String)flist[i].getName() + ')';
				Vector v = new Vector();
				v.addElement((String)flist[i].getName());
				v.addElement(file_name_desc);
				v.addElement((String)startDir.getName());
				v.addElement("0");
				
				rootTree.addElement(v);
			 }
		  }
      }
	  // 디렉토리 권한 관리를 DB에서 하는경우
	  else
	  {
		  File flist[] = startDir.listFiles();
		  
		  for(int i=0; i<flist.length; i++){
			 
			 if(flist[i].isDirectory()){
					// 2003.10.02 kokim         	
					String DirFullPath = (String)flist[i].getParent() + "\\" + (String)flist[i].getName();
					RdmrdDBAgent agent = null;
					RDJDBCHelper rdhelper = null;
					RDJDBCHelper rdhelper2 = null;
					RDJDBCHelper rdhelper3 = null;
					
					//String sSql = "select * from scdirectory where directory='" + DirFullPath +"'";
					String sSql = Message.get("Directoryauthorization_05");
					Vector vSeg = new Vector();
					try
					{
						agent = new RdmrdDBAgent(servicename);
						rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);
						rdhelper2 = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);
						rdhelper3 = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);
						
						if(rdhelper == null){
							System.out.println("db conneciton error");
							return rootTree;
						}
						
						rdhelper.select(sSql);
						rdhelper.setString(1,DirFullPath);
						rdhelper.execute();
						
						if(!rdhelper.next())
						{
							//String sql_insert = "insert into scdirectory values('" + DirFullPath + "', 'admin')";
							String sql_insert = Message.get("Directoryauthorization_07");
							rdhelper2.select(sql_insert);
							rdhelper2.setString(1,DirFullPath);
							rdhelper2.executeUpdate();
						}
						
						//String sql_select = "select * from scdirectory where directory='" + DirFullPath + "' and userid='" + sessionUser + "'";
						String sql_select = Message.get("Directoryauthorization_04");
						rdhelper3.select(sql_select);
						rdhelper3.setString(1,DirFullPath);
						rdhelper3.setString(2,sessionUser);
						rdhelper3.execute();
						
						if(rdhelper3.next())
						{
						File child[] = flist[i].listFiles();
						Vector mrdv = new Vector();
						mrdv.addElement((String)flist[i].getName());
						mrdv.addElement((String)flist[i].getName());
						mrdv.addElement((String)startDir.getName());
						if(child.length == 0) 
						   mrdv.addElement("-1");
						else
						   mrdv.addElement(Integer.toString(child.length));
						
						rootTree.addElement(mrdv);
						makeDirTree(flist[i],rootTree);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}					
					finally
					{
						if(rdhelper != null)
							try{
								rdhelper.close();
							}catch(Exception e){}
							
						if(rdhelper2 != null)
							try{
								rdhelper2.close();
							}catch(Exception e){}
							
						if(rdhelper3 != null)
							try{
								rdhelper2.close();
							}catch(Exception e){}
							
						agent.disconnect();
					}
							
			}
			 else
			 {
				FileDescription fd = new FileDescription();
				String filedesc = fd.getdesc((String)flist[i].getName(), servicename);
				if(filedesc == null)
					filedesc = "";
				String file_name_desc = filedesc + '(' + (String)flist[i].getName() + ')';
				Vector v = new Vector();
				v.addElement((String)flist[i].getName());
				v.addElement(file_name_desc);
				v.addElement((String)startDir.getName());
				v.addElement("0");
				
				rootTree.addElement(v);
			 }
		  }
	  }
      return rootTree;
      
   }


   // format : parentid, id, text, icon, iconexpand, bexpand, sAction, nChildcount
   // bExpand : 1 - expand, 0 shrink
   String getNodeData(Vector vNode)
   {      
      if (vNode == null)
         return "";
      String sHtml = "";
      String parentid = (String)vNode.elementAt(2);
      parentid.trim();
      String id = (String)vNode.elementAt(0);
      id.trim();
      String sName = (String)vNode.elementAt(1);
      sName.trim();
      String sAction = "onclickSch('"+id+"')";
      String sChildCount = (String)vNode.elementAt(3);
      sChildCount.trim(); 

      int nExpand = 1;
      if(Integer.parseInt(sChildCount) > 0 || Integer.parseInt(sChildCount) == -1)
         sHtml += "\""+parentid+"\",\""+id+"\",\""+sName+"\",\"./images/ftv2folderclosed.gif\",\"./images/ftv2folderclosed.gif\","+nExpand+",\""+this.insertDQuoteEsc(sAction)+"\","+sChildCount+"";
      else
         sHtml += "\""+parentid+"\",\""+id+"\",\""+sName+"\",\"./images/rddoc.gif\",\"./images/log16.gif\","+nExpand+",\""+this.insertDQuoteEsc(sAction)+"\","+sChildCount+"";
      
      return sHtml;
   }

   String[] onInsertNode(String sArg[])
   {
      /*
      arg[0] = id;
      arg[1] = text;
      arg[2] = icon;
      arg[3] = iconex;
      arg[4] = expand;
      arg[5] = action;
      arg[6] = childcount;
      arg[7] = parentid;
      arg[7] = parenttreeidx;
      arg[7] = parentnodeidx;
      */

      if (sArg == null)
         return null;
         
      String sGuid = JLSystem.GetGUID();

//      sArg[0] = sID;

      return sArg;
   }

     /*
      arg[0] = id;
      arg[1] = text;
      arg[2] = parentid;
      arg[5] = newdirh
      arg[6] = olddir      
      */
      
   String[] onUpdateNode(String sArg[])
   {
      if (sArg == null){
         return null;
      }

      File oldF = null;
      File newF = null;  
      
      if(sArg[6].equals("@#*")){ 
         try{
	         if(encoding)
	            newF = new File(RDUtil.toHangleDecode(mrdfile_abs_dir+sArg[5]));
	         else
	            newF = new File(mrdfile_abs_dir+sArg[5]);
	         
	         boolean ret = newF.mkdirs();
            if(!ret)
               out.println("<script>alert('"+Message.get("JLDocTree_01")+"');parent.location.reload();</script>");            
         }catch (Exception e){
         	e.printStackTrace(); 
         }
         
         return sArg;
      }
      
      if(encoding){
         oldF = new File(RDUtil.toHangleDecode(mrdfile_abs_dir+sArg[6]));
         newF = new File(RDUtil.toHangleDecode(mrdfile_abs_dir+sArg[5]));
      }else {
         oldF = new File(mrdfile_abs_dir+sArg[6]);
         newF = new File(mrdfile_abs_dir+sArg[5]);
      }

//old file rename (new file)         
      try{
         boolean ret = oldF.renameTo(newF);
         try{
            if(!ret)
               out.println("<script>alert('"+Message.get("JLDocTree_02")+"');parent.location.reload();</script>");         
            else{
               ///dir create         
               if(!newF.exists()) {
                  boolean success = newF.mkdirs();
                  if(!success)
                  RDUtil.checkingMakeDirectory(newF.getAbsolutePath());
               }
               
               //old dir or old file delete         
               if(sArg[6] != null)
                  new File(mrdfile_abs_dir+sArg[6]).delete();
   
            }   
         }catch (Exception e){
            e.printStackTrace();
         }
         
         //java.lang.Thread.sleep(100);
      }catch (Throwable e){
          e.printStackTrace();
      }
      

      return sArg;
   
   }
   
   String[] onDeleteNode(String sArg[])
   {
      /*
      arg[0] = id;
      arg[1] = text;
      arg[2] = parentid;
      arg[3] = delpath
      */ 
      if (sArg == null && sArg[3] != null)
         return null;

      {     
          File oldF = null;
          if(encoding)
              oldF = new File(m2soft.rdsystem.server.core.rddbagent.util.RDUtil.toHangleDecode(mrdfile_abs_dir+sArg[3]));
          else
              oldF = new File(mrdfile_abs_dir+sArg[3]);
          if(oldF != null){
             boolean ret = oldF.delete();
             
            try{
               if(!ret)
                  out.println("<script>alert('"+Message.get("JLDocTree_03")+"');parent.location.reload();</script>");            
               }catch (Exception e){
            }
         
          }
       }
       
      return sArg;
   }
}

%>
