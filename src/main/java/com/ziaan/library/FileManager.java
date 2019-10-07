
package com.ziaan.library;

import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

 /**
 * <p> ����: File ���� ���̺귯��</p> 
 * <p> ����: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author ������
 *@date 2003. 12
 *@version 1.0
 */
public class FileManager { 
    
    public FileManager() { 
    }
        
    /**
    * ÷�������� �ִ°�� Listȭ�鿡�� Icon���� ǥ���Ѵ�.
    @param filename ���ε�Ǵ� ���ϸ�
    @return result  ����Ȯ���ڿ� ���� �̹��������� ��θ� ��ȯ��
    */
    public static String getFileTypeImage(String filename) throws Exception { 
        String fvalue= "";
        
        if ( !filename.equals("") ) { 
            fvalue = "<img border = 0 src = '/images/common/";
            if ( StringManager.rightstring(filename,4).toLowerCase().equals(".htm")) fvalue = fvalue + "etc_html.gif' > ";
            else if ( StringManager.rightstring(filename,4).toLowerCase().equals(".jpg")) fvalue = fvalue + "etc_html.gif' > ";
            else if ( StringManager.rightstring(filename,4).toLowerCase().equals(".gif")) fvalue = fvalue + "etc_html.gif' > ";
            else if ( StringManager.rightstring(filename,4).toLowerCase().equals(".txt")) fvalue = fvalue + "etc_txt.gif' > ";
            else if ( StringManager.rightstring(filename,4).toLowerCase().equals(".zip")) fvalue = fvalue + "etc_zip.gif' > ";
            else if ( StringManager.rightstring(filename,4).toLowerCase().equals(".hwp")) fvalue = fvalue + "etc_hwp.gif' > ";
            else if ( StringManager.rightstring(filename,4).toLowerCase().equals(".doc")) fvalue = fvalue + "etc_doc.gif' > ";
            else if ( StringManager.rightstring(filename,4).toLowerCase().equals(".ppt")) fvalue = fvalue + "etc_ppt.gif' > ";
            else fvalue = fvalue + "etc_file.gif' > ";   
        }

        return fvalue;    
    }  
    
    /**
    * ÷�������� �����Ѵ�.(�迭�� ����� �ټ��� ����)
    @param savenm ������ִ� ���ϸ�(�迭)
    @param upSeq ������ ���ϰ���
    */
    public static void deleteFile(String [] savenm, int upSeq) throws Exception { 
        try { 
            ConfigSet conf = new ConfigSet();
            String v_updir = conf.getProperty("dir.upload");
                         
            for ( int i = 0; i < upSeq; i++ ) { 
                if ( !savenm [i].equals("") ) { 
                    File f = new File(v_updir, savenm [i]);
                    if ( f.exists() ) { 
                        f.delete();
                    }
                }
            }
        } catch( Exception e ) { 
            e.printStackTrace();
        }
    }
    
    /**
    * ÷�������� �����Ѵ�.(Vector�� ����� �ټ��� ����)
    @param savenm ������ִ� ���ϸ�(Vector)
    */
    public static void deleteFile(Vector savefile) throws Exception { 
        try { 
            ConfigSet conf = new ConfigSet();
            if ( savefile != null ) { 
                for ( int i = 0; i < savefile.size(); i++ ) { 
                    String v_savenm = (String)savefile.elementAt(i);
                    if ( !v_savenm.equals("") ) { 
                        String v_dirKey = conf.getDir(conf.getProperty("dir.upload"), getServletName(v_savenm));
                        String v_updir = conf.getProperty("dir.upload." + v_dirKey);
                        
                        File f = new File(v_updir, v_savenm);
                        if ( f.exists() ) { 
                            f.delete();
                        }
                    }
                }
            }
        } catch ( Exception ex ) { 
            ex.printStackTrace();
            Log.sys.println(ex, "Happen to FileManager.deleteFile(Vector savefile)");     
        }
    }
    
    /**
    * ÷�������� �����Ѵ�.
    @param savenm ������ִ� ���ϸ�
    */
    public static void deleteFile(String p_savenm) throws Exception { 
        boolean result = true;
        try { 
            if ( !p_savenm.equals("") ) { 
                ConfigSet conf = new ConfigSet();

                String v_dirKey = conf.getDir(conf.getProperty("dir.upload"), getServletName(p_savenm));
                String v_updir = conf.getProperty("dir.upload." + v_dirKey);
                System.out.println(v_updir);
                File f = new File(v_updir, p_savenm);
                if ( f.exists() ) { 
                    f.delete();
                }
            }
        } catch ( Exception ex ) { 
            ex.printStackTrace();
            Log.sys.println(ex, "Happen to FileManager.deleteFile(String " + p_savenm + ")");     
        }
    } 
    
    /**
    * ������������ ÷�������� �����Ѵ�.
    @param savenm ������ִ� ���ϸ�
    @param p_dirKey ������ִ� ��������
    */
    public static void deleteFile(String p_savenm, String p_dirKey) throws Exception { 
        boolean result = true;
        try { 
            if ( !p_savenm.equals("") ) { 
                ConfigSet conf = new ConfigSet();

                String v_updir = conf.getProperty("dir.upload." + p_dirKey);

                File f = new File(v_updir, p_savenm);
                if ( f.exists() ) { 
                    f.delete();
                }
            }
        } catch ( Exception ex ) { 
            ex.printStackTrace();
            Log.sys.println(ex, "Happen to FileManager.deleteFile(String " + p_savenm + ")");     
        }
    } 

    public static String getServletName(String p_savenm) { 
        String v_servletName = p_savenm;
        StringTokenizer st = new StringTokenizer(p_savenm, "_");
        if ( st.hasMoreElements() ) { 
            v_servletName = st.nextToken().toLowerCase();
        }

        return v_servletName;
    }
}

    