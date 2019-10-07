var is_page_info = new Array();

is_page_info[1] = new Array();
is_page_info[2] = new Array();
//is_page_info[3] = new Array();
//is_page_info[4] = new Array();
//is_page_info[5] = new Array();


//is_page_info[1][1] = ["차시","시퀀스번호 = 시퀀스-1","htm이름","시퀀스"];
//1차시
is_page_info[01][1]=["01","0","0101.html","0001"];
is_page_info[01][2]=["01","1","0102.html","0002"];
is_page_info[01][3]=["01","2","0103.html","0003"];
is_page_info[01][4]=["01","3","0104.html","0004"];
is_page_info[01][5]=["01","4","0105.html","0005"];
is_page_info[01][6]=["01","5","0106.html","0006"];
is_page_info[01][7]=["01","6","0107.html","0007"];
is_page_info[01][8]=["01","7","0108.html","0008"];

//2차시
is_page_info[02][1]=["02","8","0201.html","0009"];
is_page_info[02][2]=["02","9","0202.html","0010"];
is_page_info[02][3]=["02","10","0203.html","0011"];
is_page_info[02][4]=["02","11","0204.html","0012"];
is_page_info[02][5]=["02","12","0205.html","0013"];
is_page_info[02][6]=["02","13","0206.html","0014"];
is_page_info[02][7]=["02","14","0207.html","0015"];
is_page_info[02][8]=["02","15","0208.html","0016"];

//3차시
//is_page_info[03][1]=["03","39","0301.html","0040"];


//4차시
//is_page_info[04][1]=["04","55","0401.html","0056"];

//5차시
//is_page_info[05][1]=["05","78","0501.html","0079"];










var isURL = document.location.href.split("?")[0];
isURL = isURL.split("/");
var isChap = Number(isURL[isURL.length-2]);
var isPageName = isURL[isURL.length-1];
var isPageNumber=1;

var isChapStr="";
var isSeq="";
var isSeqNum="";
var isLastPage=false;
var endpage = totalPageNum;

for(i=1;i<is_page_info[isChap].length;i++){
        if(is_page_info[isChap][i][2] == isPageName){
                isPageNumber = i;
                isChapStr = is_page_info[isChap][i][0];
                isSeqNum = is_page_info[isChap][i][1];
                isSeq = is_page_info[isChap][i][3];
        }
}

//alert(isPageNumber+" , "+isChapStr+" , "+isSeqNum+" , "+isSeq);
if(isPageNumber==(is_page_info[isChap].length-1)){
        lms_commit();
}


function lms_commit()
{
      // alert("진도저장!!");
        try{
                top.ContentExecutor.commit(false, 'N');
        }catch(e){

        }
}


function lmsMove(val){
        try{
                //top.ContentExecutor.changeContent3('06','70', '1', '0071');
                var goPageName = val.split("?")[0];
                var goPageNumber;
                var goChapStr;
                var goSeqNum;
                var goSeq;

                for(i=1;i<is_page_info[isChap].length;i++){
                        if(is_page_info[isChap][i][2] == goPageName){
                                goPageNumber = i;
                                goChapStr = is_page_info[isChap][i][0];
                                goSeqNum = is_page_info[isChap][i][1];
                                goSeq = is_page_info[isChap][i][3];
                        }
                }

                //changeContent(goChapStr,goSeqNum,"1",goSeq);
                //alert(goChapStr+","+goSeqNum+",1, "+goSeq);
               // alert("changeContent3 : "+goChapStr+" , "+goSeqNum+" , "+goSeqNum+" , "+goSeq);
                top.ContentExecutor.changeContent3(goChapStr,goSeqNum,"1",goSeq);
                //document.location=val;



        }catch(e){
                document.location=val;
                //alert(goChapStr+","+goSeqNum+",1, "+goSeq);
        }

}

function lmsMove2(c,p,param){
        goPageName = is_page_info[c][p][2]+param;
        lmsMove(goPageName);
}

function changeContent(s1,s2,s3,s4){
        //alert(s1+" , "+s2+" , "+s3+" , "+s4);
}


