/*
 * @(#)MetadataCommonDataType.java
 *
 * Copyright(c) 2008, Jin-pil Chung
 * All rights reserved.
 */

package com.ziaan.scorm2004.validator;

import org.w3c.dom.Node;

/**
 * LOM_ELEMENT_TYPE 테이블의 DATA_TYPE 참고
 */
public class MetadataCommonDataType
{
    /*
     * Common Data Type Definition.
     */
    public final static int UNKNOWN = 0;
    public final static int VOCABULARY = 1;
    public final static int LANGSTRING = 2;
    public final static int DATE = 3;
    public final static int DURATION = 4;
    public final static int VCARD = 5;

    /*
     * Common Data Type List
     */
    private static String VOCABULARYS[] =
        {
            "general.structure",
            "general.aggregationLevel",
            "lifeCycle.status",
            "lifeCycle.contribute.role",
            "metaMetadata.contribute.role",
            "technical.requirement.orComposite.type",
            "technical.requirement.orComposite.name",
            "educational.interactivityType",
            "educational.learningResourceType",
            "educational.interactivityLevel",
            "educational.semanticDensity",
            "educational.intendedEndUserRole",
            "educational.context",
            "educational.difficulty",
            "rights.cost",
            "rights.copyrightAndOtherRestrictions",
            "relation.kind",
            "classification.purpose"
        };
    
    private static String LANGSTRINGS[] =
        {
            "general.title",
            "general.description",
            "general.keyword",
            "general.coverage",
            "lifeCycle.version",
            "technical.installationRemarks",
            "technical.otherPlatformRequirements",
            "educational.typicalAgeRange",
            "educational.description",
            "rights.description",
            "relation.description",
            "annotation.description",
            "classification.taxonPath.source",
            "classification.taxonPath.taxon.entry",
            "classification.keyword"
        };
    
    private static  String DATES[] =
        {
            "lifeCycle.date",
            "metaMetadata.date",
            "annotation.date"
        };
    
    
    private static  String DURATIONS[] = 
        {
            "technical.duration",
            "educational.typicalLearningTime"
        };
    
    private static  String VCARDS[] = 
        {
            "lifeCycle.entity",
            "metaMetadata.entity",
            "anontation.entity"
        };
    
    /**
     * <lom>에서 Node까지의 모든 element를 "." 으로 연결해서 return 한다
     *
     * example>
     *    
     *    <lom>
     *      <general>
     *        <title>
     *      </general>
     *    </lom>
     *    
     *    => general.title
     * 
     * @param node
     * @return String
     */
    public static String getFullElementName( Node node )
    {
        StringBuffer fullElementName = new StringBuffer();
        
        fullElementName.append( node.getNodeName() );
        
        Node tempNode = node.getParentNode();
        
        while ( tempNode != null && !tempNode.getNodeName().equals("lom") )
        {
            fullElementName.insert( 0, "." );
            fullElementName.insert( 0, tempNode.getNodeName() );

            tempNode = tempNode.getParentNode();
        }
        
        return fullElementName.toString();
    }
    
    /**
     * Element가 어떤 DataType인지 return 한다.
     * 
     * @param elementName
     * @return Common Data Type
     */
    public static int getDataType( String fullName )
    {
        for ( int i=0; i<VOCABULARYS.length; i++ ) 
        {
            if ( fullName.equals( VOCABULARYS[i] ) ) 
            {
                return VOCABULARY;
            }
        }
        
        for ( int i=0; i<LANGSTRINGS.length; i++ ) 
        {
            if ( fullName.equals( LANGSTRINGS[i] ) ) 
            {
                return LANGSTRING;
            }
        }
        
        for ( int i=0; i<DATES.length; i++ ) 
        {
            if ( fullName.equals( DATES[i] ) ) 
            {
                return DATE;
            }
        }
        
        
        for ( int i=0; i<DURATIONS.length; i++ ) 
        {
            if ( fullName.equals( DURATIONS[i] ) ) 
            {
                return DURATION;
            }
        }
        
        for ( int i=0; i<VCARDS.length; i++ ) 
        {
            if ( fullName.equals( VCARDS[i] ) ) 
            {
                return VCARD;
            }
        }

        return UNKNOWN;
    }

    public static String getDataTypeName( String fullName )
    {
        String typeName = "";
        
        int type = getDataType(fullName);
        
        switch (type)
        {
            case MetadataCommonDataType.VOCABULARY:
            {
                typeName = "vocabulary";
                break;
            }
            case MetadataCommonDataType.LANGSTRING:
            {
                typeName = "string";
                break;
            }
            case MetadataCommonDataType.DATE:
            {
                typeName = "date";
                break;
            }
            case MetadataCommonDataType.DURATION:
            {
                typeName = "duration";
                break;
            }
            case MetadataCommonDataType.VCARD:
            {
                typeName = "vCard";
                break;
            }
            case MetadataCommonDataType.UNKNOWN:
            {
                typeName = "unknown";
                break;
            }
        }
        
        return typeName;
    }
}
