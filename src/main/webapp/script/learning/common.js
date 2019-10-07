var StudyrooomMenuExecutor = {
	isHide : false,
	isBottomHide : false,
    doLeftMenuHide : function(hideElement, showElement, moveX, durationParam){
    	if (this.isHide)
    		return;
    	var duration = '1.0';
    	if (durationParam != undefined)
    		duration = durationParam;
    	this.isHide = true;
    	Effect.MoveBy(hideElement, 0, moveX, {duration: duration, 
    	afterFinish:function(){
        	Element.show(showElement);
     		}
    	})
    },
	doLeftMenuShow : function(hideElement, showElement, moveX){
       	if (!this.isHide)
       		return;
       	this.isHide = false;
       	Element.hide(hideElement);
       	Effect.MoveBy(showElement, 0, moveX);
 //      	commonUI.action.shake(showElement);
    },
    
    doBottomMenuHide : function(hideElement, showElement, moveY, durationParam){
       	if (this.isBottomHide)
       		return;
       		var duration = '1.0';
       	if (durationParam != undefined)
       		duration = durationParam;
       	this.isBottomHide = true;
       	Effect.MoveBy(hideElement, moveY, 0, {duration: duration, 
       	afterFinish:function(){
            Element.show(showElement);
        }
       	})
    },
	doBottomMenuShow : function(hideElement, showElement, moveY){
       	if (!this.isBottomHide)
       		return;
       	this.isBottomHide = false;
       	Element.hide(hideElement);
       	Effect.MoveBy(showElement, moveY, 0);
    }
}

var tid,d;
var sessionTime = -1;
function myinit() {
  tid=setInterval("chktime()",1000);
}

function timeClear() {
	clearInterval(tld);
	sessionTime = -1;
}

function clearTime(totalTime) {
	sessionTime = -1;
	var hr  = parseInt(totalTime/3600);
	var min = parseInt(totalTime%3600/60);
	var sec = parseInt(totalTime%3600%60);
	d=new Date();
	d.setHours(hr);
	d.setMinutes(min);
	d.setSeconds(sec);
}

function chktime() {  
  var crrtime = padZero(d.getHours()*60+d.getMinutes())+":"+padZero(d.getSeconds());
  Element.update($('calTime'), crrtime);
  d.setSeconds(d.getSeconds()+1);
  sessionTime += 1;
}

function getSeeionTime() {
	if (sessionTime > 0)
		return sessionTime;
	else
		return 0;
}

function padZero(n) {
  return n<10?"0"+n:n;
}	

function calStudyTime (totalTime) {
	var hr1  = parseInt(totalTime/3600);
	var min1 = parseInt(totalTime%3600/60);
	var sec1 = parseInt(totalTime%3600%60);
 	var d1=new Date();
    d1.setHours(hr1);
    d1.setMinutes(min1);
    d1.setSeconds(sec1);
    return  padZero(d1.getHours()*60+d1.getMinutes())+":"+padZero(d1.getSeconds());
}