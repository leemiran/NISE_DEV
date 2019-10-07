/*
 * @(#)LOMMetadata.java
 *
 * Copyright(c) 2008, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.scorm2004.validator;

public class LOMMetadata
{
    private String metaType;
    private int seq;
    private int pseq;
    private String elementName;
    private String value1;
    private String value2;
    private String langString;
    
    public String getElementName()
    {
        return elementName;
    }

    public void setElementName(String elementName)
    {
        this.elementName = elementName;
    }

    public String getLangString()
    {
        return langString;
    }

    public void setLangString(String langString)
    {
        this.langString = langString;
    }

    public String getMetaType()
    {
        return metaType;
    }

    public void setMetaType(String metaType)
    {
        this.metaType = metaType;
    }

    public int getPseq()
    {
        return pseq;
    }

    public void setPseq(int pseq)
    {
        this.pseq = pseq;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public String getValue1()
    {
        return value1;
    }

    public void setValue1(String value1)
    {
        this.value1 = value1;
    }

    public String getValue2()
    {
        return value2;
    }

    public void setValue2(String value2)
    {
        this.value2 = value2;
    }
    
    public String toString() 
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append( " metaType    : " );
        sb.append( metaType );
        sb.append( "\r\n" );
        
        sb.append( " seq         : " );
        sb.append( seq );
        sb.append( "\r\n" );
        
        sb.append( " pseq        : " );
        sb.append( pseq );
        sb.append( "\r\n" );
        
        sb.append( " elementName : " );
        sb.append( elementName );
        sb.append( "\r\n" );
        
        sb.append( " value1      : " );
        sb.append( value1 );
        sb.append( "\r\n" );
        
        sb.append( " value2      : " );
        sb.append( value2 );
        sb.append( "\r\n" );
        
        sb.append( " langString  : " );
        sb.append( langString );
        sb.append( "\r\n" );
        
        return sb.toString();
    }
}
