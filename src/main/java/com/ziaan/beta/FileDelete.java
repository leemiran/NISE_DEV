// **********************************************************
// 1. ��      ��: ���� ���� 
// 2. ���α׷���: FileDelete.java
// 3. ��      ��:
// 4. ȯ      ��: JDK 1.3
// 5. ��      ��: 0.1
// 6. ��      ��: 2003. 10. 12
// 7. ��      ��: 
//                 
// **********************************************************
package com.ziaan.beta;

import java.io.File;

public class FileDelete { 
	public FileDelete() { 
	}

	public boolean allDelete(String v_realPath) { 
	    boolean delAllDir_success = false;
		boolean delFile_success   = false;
		boolean delDir_success    = false;
		boolean temp_success      = false;
		File [] dirsAndFiles      = null;
		int     idx_point         = 0;

		try { 	
		    File  delAllDir       = new File(v_realPath);                         // ������ ����(�Ϻ����� �� ������ ������)�� File ��ü �����Ѵ�

			dirsAndFiles          = delAllDir.listFiles();
            
            if ( dirsAndFiles != null )
            {
    			if(!(v_realPath.equals("/emc_backup/LMS/content/")
    					|| v_realPath.equals("/emc_backup/LMS/content")
    					|| v_realPath.equals("/emc_backup/LMS/dev_ROOT/content/")
    					|| v_realPath.equals("/emc_backup/LMS/dev_ROOT/content"))) {
	    			for ( int i = 0; i < dirsAndFiles.length; i++ ) { 
	    			    String dirAndFile    = dirsAndFiles [i].toString();
	                    
	    				idx_point            = dirAndFile.indexOf(".");			          // �� ��θ��� ���� ���� .�� ã�´� (���Ͽ���)
	    
	    				if ( idx_point != -1) { // ������ �����Ѵٸ� ���� ���� ����
	    					delFile_success  = dirsAndFiles [i].delete();   //  �ش� ����� ���� ����
	    				} else { // ���� �� ���
	    					temp_success     = this.allDelete(dirAndFile);    //  ������ ���� �� ���� ���� ���� Ȯ�� �� ����
	    					delDir_success   = dirsAndFiles [i].delete();   //  �ش� ����� ���� ����
	    				}
	    			}
	    	// 		delAllDir.delete();     //   �������� ������ �������� ����
	    			delAllDir_success = true;
    			}
            }
		} catch (Exception ie ) { 
			delAllDir_success = false;
			ie.printStackTrace();
		}
		return delAllDir_success;
	}
}