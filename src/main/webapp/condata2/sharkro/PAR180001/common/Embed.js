

//lms commit하기 -- 진도완료
function lms_commit()
{
        //alert("진도저장!!");
        top.ContentExecutor.commit(false, 'N');
}

var statusVal = "";
function returnLessonstatus(){
        try{
                //statusVal = top.ContentExecutor.lessonstatus();
                statusVal = top.ContentExecutor.lessonstatus;
        }catch(e){
                statusVal="N";
        }
        return statusVal;
}

returnLessonstatus();