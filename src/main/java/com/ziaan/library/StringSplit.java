package com.ziaan.library;

public class StringSplit{

  String theString;
  char theDelim;
  int thePos;
  String theDelimStr=null;
  int theDelimLength;



  public StringSplit(String S,char d,int p){
    theString=S; theDelim=d; thePos=p; theDelimLength=1;
    if(thePos>=theString.length())thePos=-1;
  }


  public StringSplit(String S,char d){this(S,d,0);}


  public StringSplit(String S,String d,int p){
    theString=S; theDelimStr=d; thePos=p;
    theDelimLength=d.length();
    if(thePos>=theString.length())thePos=-1;
  }

  public StringSplit(String S,String d){this(S,d,0);}

  public boolean hasMoreTokens(){return thePos>=0;}

  public String nextToken(){
    if(thePos<0) return null;
    int nextPos;
    if(theDelimStr==null) nextPos=theString.indexOf(theDelim,thePos);
    else nextPos=theString.indexOf(theDelimStr,thePos);
    String R;
    if(nextPos>=0){
      R=theString.substring(thePos,nextPos);
      thePos=nextPos+theDelimLength;
    }
    else {
      R=theString.substring(thePos);
      thePos=nextPos;
    }
    return R;
  }

}