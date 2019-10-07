/*
 * 요소기술 스크립트  
 */

// 숫자체크
function isNumber(control, msg) {
	
	var val = control;
	var Num = "1234567890";
	for (i=0; i<val.length; i++) {
		if(Num.indexOf(val.substring(i,i+1))<0) {
			alert(msg+' 형식이 잘못되었습니다.');
			return false;
		}
	}
	return true;
}

// 날짜체크
function isDate(control, msg) {
	
	// '/'나 '-' 구분자 제거
	var val = getRemoveFormat(control);
	
	// 숫자, length 확인
	if (isNumber(val, msg) && val.length == 8) {
		var year = val.substring(0,4);
		var month = val.substring(4,6);
		var day = val.substring(6,8);
		
		// 유효날짜 확인
		if(checkDate(year,month,day,msg)){
			return true;
		} else {
			return false;
		}
	} else {
		alert(msg + " 유효하지 않은 년,월,일(YYYYMMDD)입니다. 다시 확인해 주세요!");
		return false;
	}
}

// 구분자 제거
function getRemoveFormat(val) {
	if(val.length == 10) {
		var arrDate = new Array(3);
		arrDate = val.split("/");
		if(arrDate.length != 3) {
			arrDate = val.split("-");
		}
		return arrDate[0] + arrDate[1] + arrDate[2];
	} else {
		return val;
	}	
}

// 유효날짜 확인
function checkDate(varCk1, varCk2, varCk3, msg) {
	if (varCk1>="0001" && varCk1<="9999" && varCk2>="01" && varCk2<="12") {
		febDays = "29";
		if ((parseInt(varCk1,10) % 4) == 0) {
			if ((parseInt(varCk1,10) % 100) == 0 && (parseInt(varCk1,10) % 400) != 0){
				febDays = "28";
			}
		}else{
			febDays = "28";
		}
		if (varCk2=="01" && varCk3>="01" && varCk3<="31") return true;
		if (varCk2=="02" && varCk3>="01" && varCk3<=febDays) return true;
		if (varCk2=="03" && varCk3>="01" && varCk3<="31") return true;
		if (varCk2=="04" && varCk3>="01" && varCk3<="30") return true;
		if (varCk2=="05" && varCk3>="01" && varCk3<="31") return true;
		if (varCk2=="06" && varCk3>="01" && varCk3<="30") return true;
		if (varCk2=="07" && varCk3>="01" && varCk3<="31") return true;
		if (varCk2=="08" && varCk3>="01" && varCk3<="31") return true;
		if (varCk2=="09" && varCk3>="01" && varCk3<="30") return true;
		if (varCk2=="10" && varCk3>="01" && varCk3<="31") return true;
		if (varCk2=="11" && varCk3>="01" && varCk3<="30") return true;
		if (varCk2=="12" && varCk3>="01" && varCk3<="31") return true;
	}
	alert(msg + " 유효하지 않은 년,월,일(YYYYMMDD)입니다. 다시 확인해 주세요!");
	return false;
}


/**
     * 주어진 날짜에 하루를 더한 날짜
     *
     * @param targetDate - 날짜객체
     * @param dayPrefix - 일수
     * @return 더 해진 날짜객체
     */
    function getAddDay(targetDate, dayPrefix){
        var newDate = new Date( );
        var processTime = targetDate.getTime ( ) + ( parseInt ( dayPrefix ) * 24 * 60 * 60 * 1000 );
        newDate.setTime ( processTime );
        return newDate;
    }

	
    /**
     * 집합
     * 두 날짜 사이의 평일날수를 구한다.(조회 종료일 - 조회 시작일)
     *
     * @param val1 - 조회 시작일(날짜 ex.2002-01-01)
     * @param val2 - 조회 종료일(날짜 ex.2002-01-01)
     * @return 평일날수
     */
    function getWorkDay(val1, val2)
    {
    	
        // 길이 체크
        if (val1.length != 8 || val2.length != 8)
            return null;
    		
        // 년도, 월, 일로 분리
        var start_dt = new Array(val1.substr(0, 4),
    	                         val1.substr(4, 2),
    							 val1.substr(6, 2));
    							 
        var end_dt   = new Array(val2.substr(0, 4),
                                 val2.substr(4, 2),
                                 val2.substr(6, 2));

        // 월 - 1(자바스크립트는 월이 0부터 시작하기 때문에...)
        // Number()를 이용하여 08, 09월을 10진수로 인식하게 함.
        start_dt[1] = (Number(start_dt[1]) - 1) + "";
        end_dt[1] = (Number(end_dt[1]) - 1) + "";

        var from_dt = new Date(start_dt[0], start_dt[1], start_dt[2]);
        var to_dt = new Date(end_dt[0], end_dt[1], end_dt[2]);
    	
    	var work_day_cnt = 0;
    	
    	for(var temp_dt=from_dt; 
    	        temp_dt.getTime()<=to_dt.getTime();
    	            temp_dt=getAddDay(temp_dt, 1)){
    		//[일요일=0, 토요일=6 ]은 제외하고 카운트
    		if(temp_dt.getDay()!=0 && temp_dt.getDay()!=6) {
    			work_day_cnt = work_day_cnt + 1;
    		}
    	}

        return work_day_cnt;
    }
     
     /**
      * 사이버
      * 두 날짜 사이의 평일날수를 구한다.(조회 종료일 - 조회 시작일)
      *
      * @param val1 - 조회 시작일(날짜 ex.2002-01-01)
      * @param val2 - 조회 종료일(날짜 ex.2002-01-01)
      * @return 평일날수
      */
     function getWorkDayS(val1, val2)
     {
     	
         // 길이 체크
         if (val1.length != 8 || val2.length != 8)
             return null;
     		
         // 년도, 월, 일로 분리
         var start_dt = new Array(val1.substr(0, 4),
     	                         val1.substr(4, 2),
     							 val1.substr(6, 2));
     							 
         var end_dt   = new Array(val2.substr(0, 4),
                                  val2.substr(4, 2),
                                  val2.substr(6, 2));

         // 월 - 1(자바스크립트는 월이 0부터 시작하기 때문에...)
         // Number()를 이용하여 08, 09월을 10진수로 인식하게 함.
         start_dt[1] = (Number(start_dt[1]) - 1) + "";
         end_dt[1] = (Number(end_dt[1]) - 1) + "";

         var from_dt = new Date(start_dt[0], start_dt[1], start_dt[2]);
         var to_dt = new Date(end_dt[0], end_dt[1], end_dt[2]);
     	
     	var work_day_cnt = 0;
     	
     	for(var temp_dt=from_dt; 
     	        temp_dt.getTime()<=to_dt.getTime();
     	            temp_dt=getAddDay(temp_dt, 1)){
     		//[일요일=0, 토요일=6 ]은 제외하고 카운트
     		//if(temp_dt.getDay()!=0 && temp_dt.getDay()!=6) {
     			work_day_cnt = work_day_cnt + 1;
     		//}
     	}

         return work_day_cnt;
     }

    /**
     * 주어진 날수에 주수를 구한다
     *
     * @param workday - 날수
     * @param dayOfWeek - 주당 작업일수
     * @return 주수
     */
    function getWeekOfDay(workday, dayOfWeek){
    	return Math.ceil(workday / dayOfWeek);
    }