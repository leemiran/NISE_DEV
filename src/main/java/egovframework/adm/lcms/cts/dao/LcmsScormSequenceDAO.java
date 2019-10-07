/* 
 * LcmsScormSequenceDAO.java		1.00	2011-09-05 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.adm.lcms.cts.domain.LcmsScormSequence;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsScormSequenceDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsScormSequenceDAO")
public class LcmsScormSequenceDAO extends EgovAbstractDAO{
    public List selectLcmsScormSequencePageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsScormSequenceDAO.selectLcmsScormSequencePageList", commandMap);
    }

    public int  selectLcmsScormSequencePageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsScormSequenceDAO.selectLcmsScormSequencePageListTotCnt", commandMap);
    }
    /**
     * SEQUENCING 의 시퀀스 값 Return
     */
    public int  selectLcmsScormSequenceMaxSeq( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsScormSequenceDAO.selectLcmsScormSequenceMaxSeq", commandMap);
    }

    public List selectLcmsScormSequenceList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsScormSequenceDAO.selectLcmsScormSequenceList", commandMap);
    }

    public Object selectLcmsScormSequence( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsScormSequenceDAO.selectLcmsScormSequence", commandMap);
    }

    public Object insertLcmsScormSequence( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsScormSequenceDAO.insertLcmsScormSequence", commandMap);
    }

    public int updateLcmsScormSequence( Map<String, Object> commandMap) throws Exception{
        return update("lcmsScormSequenceDAO.updateLcmsScormSequence", commandMap);
    }

    public int updateFieldLcmsScormSequence( Map<String, Object> commandMap) throws Exception{
        return update("lcmsScormSequenceDAO.updateFieldLcmsScormSequence", commandMap);
    }

    public int deleteLcmsScormSequence( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsScormSequenceDAO.deleteLcmsScormSequence", commandMap);
    }

    public int deleteLcmsScormSequenceAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsScormSequenceDAO.deleteAllLcmsScormSequence", commandMap);
    }

    public Object existLcmsScormSequence( LcmsScormSequence lcmsScormSequence) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsScormSequenceDAO.existLcmsScormSequence", lcmsScormSequence);
    }
    
    public Map selectLcmsScormSequenceSeqIdxNum( Map<String, Object> commandMap) throws Exception{;
    return (Map)selectByPk("lcmsScormSequenceDAO.selectLcmsScormSequenceSeqIdxNum", commandMap);
}

}
