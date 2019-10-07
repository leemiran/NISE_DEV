// **********************************************************
// 1. 제      목: 학습점수DataSub
// 2. 프로그램명: EduScoreDataSub.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: LeeSuMin 2003. 08. 26
// 7. 수      정: 
//                 
// **********************************************************
package com.ziaan.lcms;


public class EduScoreDataSub { 
	
	private	String	datatype;			
	private	double weight     	=0; 	// 가중치            
	private	double score      	=0;		// 원점수
	private	double avscore    	=0;		// 가중치적용점수
	private double gradscore    	=0;     // 수료기준점수
	private String result		= "";
	
	public EduScoreDataSub() { };
	
	public EduScoreDataSub(String d, double w, double s, double a, double g, String r) { 
		this();
		this.datatype=d;
		this.weight =w;
		this.score =s;
		this.avscore=a;
		this.gradscore=g;
		this.result=r;
	};
	
	/**
	 * @return
	 */
	public double getAvscore() { 
		return avscore;
	}

	/**
	 * @return
	 */
	public String getDatatype() { 
		return datatype;
	}

	/**
	 * @return
	 */
	public double getScore() { 
		return score;
	}

	/**
	 * @return
	 */
	public double getWeight() { 
		return weight;
	}
	
	/**
	 * @return
	 */
	public double getGradscore() { 
		return gradscore;
	}
	
	/**
	 * @return
	 */
	public String getResult() { 
		return result;
	}
		
	
	/**
	 * @param d
	 */
	public void setAvscore(double d) { 
		avscore = d;
	}

	/**
	 * @param string
	 */
	public void setDatatype(String string) { 
		datatype = string;
	}

	/**
	 * @param d
	 */
	public void setScore(double d) { 
		score = d;
	}

	/**
	 * @param d
	 */
	public void setWeight(double d) { 
		weight = d;
	}
	
	/**
	 * @param d
	 */
	public void setGradscore(double g) { 
		gradscore = g;
	}

	/**
	 * @param string
	 */
	public void setResult(String string) { 
		result = string;
	}
}
