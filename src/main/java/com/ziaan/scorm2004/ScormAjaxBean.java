/*
 * @(#)ScormAjaxBean.java
 *
 * Copyright(c) 2008, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.scorm2004;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.scorm2004.validator.MetadataCommonDataType;

/**
 * Ajax 관련 Bean Class
 *
 * @version 1.0 2008. 5. 19.
 * @author Jin-pil Chung
 *
 */
public class ScormAjaxBean
{
    /**
     * LOM_ELEMENT_TYPE 에서 TYPE에 따른 ELEMENT_NAME 목록을 가져온다 
     * 
     * @param box
     * @return String   value,name|.... 형식
     * @throws Exception
     */
    public String selectElementName(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        StringBuffer result = new StringBuffer();
        
        String sql = "";
        
        try 
        {
            connMgr = new DBConnectionManager();

            String s_meta_type = box.getString("p_name");

            sql =
                "\n  SELECT IS_PARENT, ELEMENT_NAME, ELEMENT_PNAME, DATA_TYPE, DESCRIPTION, ORD  " +
                "\n  FROM LOM_ELEMENT_TYPE  " +
                "\n  WHERE META_TYPE=':metaType'  " +
                "\n  ORDER BY ORD  ";

            sql = sql.replaceAll( ":metaType", s_meta_type );
            ls = connMgr.executeQuery( sql );

            String description = "";
            String elementName = "";
            String elementPName = "";
            String isParent = "";
            
            String parentElementName = "";
            String fullName = "";
            
            boolean isFirst = true;
            
            while ( ls.next() )
            {
                description = ls.getString("DESCRIPTION");
                elementName = ls.getString("ELEMENT_NAME");
                elementPName = ls.getString("ELEMENT_PNAME");
                isParent = ls.getString("IS_PARENT");
                
                if ( !isFirst && isParent.equals("N") ) 
                {
                    result.append( "|" );
                }
                
                if ( isParent.equals("Y") ) 
                {
                    fullName = elementName + "." + fullName;
                    parentElementName = elementName;
                }
                else
                {
                    if ( !elementPName.equals(parentElementName) )
                    {
                        parentElementName = "";
                        fullName = "";
                    }
                    
                    result.append( fullName + elementName );
                    result.append( "," );
                    result.append( description + " (" + elementName + ")" );
                    
                    isFirst = false;
                }
            }
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace( ex, box, sql );
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        return result.toString();
    }

    /**
     * LOM_META_TYPE 에서 TYPE 목록을 가져온다 
     * 
     * @param box
     * @return String   value,name|.... 형식
     * @throws Exception
     */
    public String selectMetaType(RequestBox box) throws Exception 
    {
        DBConnectionManager connMgr = null;
        ListSet ls = null;

        StringBuffer result = new StringBuffer();
        
        String sql = "";
        
        try 
        {
            connMgr = new DBConnectionManager();

            sql =
                "\n  SELECT META_TYPE, DESCRIPTION  " +
                "\n  FROM LOM_META_TYPE  " +
                "\n  ORDER BY ORD  ";            

            ls = connMgr.executeQuery( sql );

            String description = "";
            String metaType = "";
            
            int i = 0;
            while ( ls.next() )
            {
                description = ls.getString("DESCRIPTION");
                metaType = ls.getString("META_TYPE");
                
                if ( i != 0 ) {
                    result.append( "|" );
                }

                result.append( metaType );
                result.append( "," );
                result.append( description + " (" + metaType + ")" );

                i++;
            }
        }
        catch (Exception ex) 
        {
            ErrorManager.getErrorStackTrace( ex, box, sql );
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage());
        }
        finally 
        {
            if(ls != null) { try { ls.close(); }catch (Exception e) {} }
            if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
        }

        return result.toString();
    }

    public String selectDataType(RequestBox box) throws Exception
    {
        String fullName = box.getString("p_full_name");
        String typeName = MetadataCommonDataType.getDataTypeName(fullName);
        
        return typeName;
    }
}
