(function($) {
	$.validation = function(data, klass) {
	}

	$.extend($.validation, {
		settings : {
			isProcess : false,
			options : null
		},

		setStyle : function($element, type) {		
			if (type = "eng")
				$element.css("ime-mode", "disabled")
		},

		setRequiredElementCheck : function($element, setHelp) {
			var $requiredCheckElement = $element.parent().prev();

			if ($requiredCheckElement[0]) {
				//if(($requiredCheckElement.get(0).tagName == "TH" || $requiredCheckElement.get(0).tagName == "TD") && $requiredCheckElement.get(0).child().find("font").length == 0) {
				//if($requiredCheckElement.get(0).tagName == "TH" || $requiredCheckElement.get(0).tagName == "TD") {
				if($requiredCheckElement.get(0).tagName == "TH") {  //10.12.15
					$requiredCheckElement.css("color", "#FF6600" );
					$requiredCheckElement.html("* " + $requiredCheckElement.text().replace("* ", ""));
				}
			}
			if (setHelp)
				return this.setHelp($element, $element.attr("validType"));
			else
				return "";
		},

		setHelp : function($element, type) {
			var HTML = "";
			
			if ($element[0].tagName == "SELECT" || $element[0].tagName == "RADIO" || $element[0].tagName == "CHECKBOX") {
				HTML += "* <font color='blue'><strong>'" + $element.attr("title") + "'</strong></font> 은(는) <font color='red'>필수 선택항목</font> 입니다. ";
			} else {
				HTML += "* <font color='blue'><strong>'" + $element.attr("title") + "'</strong></font> 은(는) <font color='red'>필수 입력항목</font> 입니다. ";
			}
			var minlength = $element.attr("minlength") == undefined ? 0 : $element.attr("minlength");
        	var maxlength = $element.attr("maxlength") == undefined ? 0 : $element.attr("maxlength");
        	if (type) {
	        	switch (type) {
					case "Number" :
						HTML += "(숫자만 허용";
						if(minlength != null && minlength > 0)
		            		HTML += ", 최소  " + minlength + " 자리 이상";
		            	if(maxlength != null && maxlength > 0)
		            		HTML += ", 최대 " + maxlength + " 자리 이하";
		            	HTML += ")";
		            	break;
				}
        	} else {
        		if(minlength != null && minlength > 0) {
            		HTML += ", 최소  " + minlength + " 자리 이상";
            		if(maxlength != null && maxlength > 0) {
	            		HTML += ", 최대 " + maxlength + " 자리 이하)";
            		} else {
            			HTML += ")";
            		}
        		} else {
        			if(maxlength != null && maxlength > 0) {
	            		HTML += "(최대 " + maxlength + " 자리 이하)";
	            	}
        		}
        	}
			return HTML;
		},

		setAfterSubmitValidationCheck : function($element) {
			var	validationTypeResult = true;
			if ($element.val() == "") {
				if ($element[0].tagName == "SELECT" || $element[0].tagName == "RADIO" || $element[0].tagName == "CHECKBOX") {
					alert($element.attr("title") + "을 선택 하십시요.");
				} else {
					alert($element.attr("title") + "값이 없습니다. \n" + $element.attr("title") + "을  입력 하십시요.");
				}
				$element.focus();
				return false;
			}
			validationTypeResult = this.isValidType($element);
			
			return validationTypeResult;
		},
		
		setAfterSubmitValidationCheck2 : function($element) {
		  var	validationTypeResult = true;
			if ($.trim($element.val()) == "") { 
				return true;
			}
			validationTypeResult = this.isValidType($element);
		    
	        return validationTypeResult;
		},
		
		isValidType : function($element) {
			var validationTypeCheakResult = true;
			if( $element.attr("validType")=="Number"){
				if(!this.isNumber($element.val())){
					alert($element.attr("title") + " 문자 값이 있습니다.  \n" + $element.attr("title") + "을  숫자 값만 입력 하십시요.");
					$element.focus();
					validationTypeCheakResult = false;	
				}
				
			}else if($element.attr("validType")=="Mnumber") {
				
				if(!this.isMnumber($element.val())){
					alert($element.attr("title") + "을  숫자 값만 입력 하십시요.");
					$element.focus();
					validationTypeCheakResult = false;		
				}
			}else if($element.attr("validType")=="Date") {
				
				if(!this.isDate($element.val())){
					alert($element.attr("title") + "의  날짜를   정확하게  입력 하십시요.");
					$element.focus();
					validationTypeCheakResult = false;	
				}
			}else if($element.attr("validType")=="Email") {
				
				if(!this.isEmail($element.val())){
					alert($element.attr("title") + "의  정확하게  입력 하십시요.");
					$element.focus();
					validationTypeCheakResult = false;	
				}
			}else if($element.attr("validType")=="Eng") {
				
				if(!this.isEng($element.val())){
					alert($element.attr("title") + "은 영문만 등록 할수 있습니다.");
					$element.focus();
					validationTypeCheakResult = false;	
				}
			}else if($element.attr("validType")=="Phone1") {
				
				if(!this.isPhone1($element.val())){
					alert($element.attr("title") + "을 정확하게  입력 하십시요.  \n "+$element.attr("title") + "다시 입력해주세요  .");
					$element.focus();
					validationTypeCheakResult = false;	
				}
			}else if($element.attr("validType")=="Phone2") {
				
				if(!this.isPhone2($element.val())){
					alert($element.attr("title") + "을  정확하게  입력 하십시요.  \n "+$element.attr("title") + "다시 입력해주세요  .");
					$element.focus();
					validationTypeCheakResult = false;	
				}
			}else if($element.attr("validType")=="Phone3") {
				
				if(!this.isPhone3($element.val())){
					alert($element.attr("title") + "을  정확하게  입력 하십시요.  \n "+$element.attr("title") + "다시 입력해주세요  .");
					$element.focus();
					return false;	
				}
			}else if($element.attr("validType")=="Mobile1") {
				
				if(!this.isMobile1($element.val())){
					alert($element.attr("title") + "을  정확하게  입력 하십시요.  \n "+$element.attr("title") + "다시 입력해주세요  .");
					$element.focus();
					validationTypeCheakResult = false;	
				}
			}else if($element.attr("validType")=="Mobile2") {
				
				if(!this.isMobile2($element.val())){
					alert($element.attr("title") + "을  정확하게  입력 하십시요.   \n "+$element.attr("title") + "다시 입력해주세요  .");
					$element.focus();
					validationTypeCheakResult = false;	
				}
			}else if($element.attr("validType")=="Mobile3") {
				
				if(!this.isMobile3($element.val())){
					alert($element.attr("title") + "을  정확하게  입력 하십시요.   \n "+$element.attr("title") + "다시 입력해주세요  .");
					$element.focus();
					validationTypeCheakResult = false;	
				}
			}else if($element.attr("validType")=="Jumin") {
				
				if(!this.isJumin($element.val())){
					alert($element.attr("title") + "을  정확하게  입력 하십시요.   \n "+$element.attr("title") + "다시 입력해주세요  .");
					$element.focus();
					validationTypeCheakResult = false;	
				}
			}else if($element.attr("validType")=="EngNum") {
				
				if(!this.isEngNum($element.val())){
					alert($element.attr("title") + "는 영문과 숫자만 입력 가능합니다.   \n "+$element.attr("title") + "다시 입력해주세요  .");
					$element.focus();
					validationTypeCheakResult = false;	
				}
			}else if( $element.attr("validType")=="Hour"){  //10.12.27
				if(!this.isNumber($element.val())){
					alert($element.attr("title") + " 문자 값이 있습니다.  \n" + $element.attr("title") + "을  숫자 값만 입력 하십시요.");
					$element.focus();
					validationTypeCheakResult = false;	
				}
				
				if(validationTypeCheakResult){
					if(!this.isHour($element.val())){
						alert($element.attr("title") + "을(를) 정확하게  입력 하십시요.   \n "+$element.attr("title") + "다시 입력해주세요  .");
						$element.focus();
						validationTypeCheakResult = false;	
					}
				}
				
			}else if( $element.attr("validType")=="Minute"){  //10.12.27
				if(!this.isNumber($element.val())){
					alert($element.attr("title") + " 문자 값이 있습니다.  \n" + $element.attr("title") + "을  숫자 값만 입력 하십시요.");
					$element.focus();
					validationTypeCheakResult = false;	
				}
				
				if(validationTypeCheakResult){
					if(!this.isMinute($element.val())){
						alert($element.attr("title") + "을(를) 정확하게  입력 하십시요.   \n "+$element.attr("title") + "다시 입력해주세요  .");
						$element.focus();
						validationTypeCheakResult = false;	
					}
				}
				
			}
			
			return  validationTypeCheakResult;
			
		},

		isJumin : function(jumin) {
			jumin = jumin.replace(/\-/g, '');
			// 주민번호의 형태와 7번째 자리(성별) 유효성 검사
			if (!/^\d{6}(\-|)[1-4]\d{6}$/.test(jumin)) {
			    return false;
			}

			// 날짜 유효성 검사
			var birthYear = (jumin.charAt(7) <= "2") ? "19" : "20";
				birthYear += jumin.substr(0, 2);
			var birthMonth = jumin.substr(2, 2) - 1;
			var birthDate = jumin.substr(4, 2);
			var birth = new Date(birthYear, birthMonth, birthDate);

			if ( birth.getYear() % 100 != jumin.substr(0, 2) ||
			    birth.getMonth() != birthMonth ||
			    birth.getDate() != birthDate) {
				return false;
			}

			// Check Sum 코드의 유효성 검사 7505041671319
			buf = new Array(13);
			for (i = 0; i < 6; i++) buf[i] = parseInt(jumin.charAt(i));
			for (i = 6; i < 13; i++) buf[i] = parseInt(jumin.charAt(i));

			multipliers = [2,3,4,5,6,7,8,9,2,3,4,5];
			for (i = 0, sum = 0; i < 12; i++) sum += (buf[i] *= multipliers[i]);

			if ((11 - (sum % 11)) % 10 != buf[12]) {
				return false;
			}

			return true;
		},

		// 이메일 체크
		isEmail : function(email) {
			if($.trim(email) == '')
				return true;

			return /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i.test(email);
		},

		// 숫자 체크
		isNumber : function(number) {
			if($.trim(number) == '')
				return true;

			return /^[0-9]+$/.test(number);
		},

		// 마이너스 숫자 체크
		isMnumber : function(number) {
			if($.trim(number) == '')
				return true;

			return /^[+-]?\d+$/.test(number);
		},

		// 날짜 체크
		isDate : function(date) {
		    var arg = arguments[0] ? arguments[0] : "";
	        var sDate= date.replace(/-/g, '').replace(/:/g, '').replace(/ /g, '');

	        if( sDate.length == 8 || sDate.length == 12) {
	            if (sDate.length == 8) {
	                if( !$.validate.isNumber(sDate.substr(0,4)) || !$.validate.isNumber(sDate.substr(4,2)) || !$.validate.isNumber(sDate.substr(6,2))) {
	                    return false;
	                }
	            } else {
	                if( !$.validate.isNumber(sDate.substr(0,4)) || !$.validate.isNumber(sDate.substr(4,2)) || !$.validate.isNumber(sDate.substr(6,2)) || !$.validate.isNumber(sDate.substr(8,2)) || !$.validate.isNumber(sDate.substr(10,2))) {
	                    return false;
	                }
	            }
	        }
	        else {
	            return false;
	        }

	        var aDaysInMonth=new Array(31,28,31,30,31,30,31,31,30,31,30,31);

	        iYear=eval(sDate.substr(0,4));
	        iMonth=eval(sDate.substr(4,2));
	        iDay=eval(sDate.substr(6,2));
	        var iDaysInMonth=(iMonth!=2)?aDaysInMonth[iMonth-1]:((iYear%4==0 && iYear%100!=0 || iYear % 400==0)?29:28);

	        if( (iDay!=null && iMonth!=null && iYear!=null  && iMonth<13 && iMonth>0 && iDay>0 && iDay<=iDaysInMonth) == false )  {
	            return false;
	        }
	        return true;
		},

		// 영문 체크
		isEng : function(eng) {
			return /^[a-zA-Z]+$/.test(eng);
		},
		
		isEngNum : function(engnum) {
			if($.trim(engnum) == '')
				return true;

			return /^[0-9a-zA-Z]+$/.test(engnum);
		},

		// 주민번호 앞 6자리 체크
		isSsn1 : function(ssn) {
			if($.trim(ssn) == '')
				return true;

			return /^\d{6}$/.test(ssn);
		},

		// 주민번호 뒷 7자리 체크
		isSsn2 : function(ssn) {
			if($.trim(ssn) == '')
				return true;

			return /^[1-4]\d{6}$/.test(ssn);
		},

		// 전화번호 앞자리 체크
		isPhone1 : function(phone) {
			if($.trim(phone) == '')
				return true;

			return /^0\d{1,2}$/.test(phone);
		},

		// 전화번호 가운데자리 체크
		isPhone2 : function(phone) {
			if($.trim(phone) == '')
				return true;

			return /^[1-9]\d{2,3}$/.test(phone);
		},

		// 전화번호 뒷자리 체크
		isPhone3 : function(phone) {
			if($.trim(phone) == '')
				return true;

			return /^\d{4}$/.test(phone);
		},

		// 휴대폰 앞자리 체크
		isMobile1 : function(mobile) {
			if($.trim(mobile) == '')
				return true;

			return /^01\d{1}$/.test(mobile);
		},

		// 휴대폰 가운데자리 체크
		isMobile2 : function(mobile) {
			if($.trim(mobile) == '')
				return true;

			return /^[1-9]\d{2,3}$/.test(mobile);
		},

		// 휴대폰 뒷자리 체크
		isMobile3 : function(mobile) {
			if($.trim(mobile) == '')
				return true;

			return /^\d{4}$/.test(mobile);
		},

		// 체크박스 선택여부 체크
		checkItems : function($element) {
			var sel_count = 0;
			$element.parents().find(":checkbox").each(function(index){
			    if(this.checked)
			    	sel_count++;
			});

			if( sel_count == 0)
				return false;
			else
				return true;
	    },
	    
		// 시간체크
		isHour : function(hour) {  //10.12.27
			if($.trim(hour) == '')
				return true;
			return /^(0[0-9]|1[1-9]|2[0-3])$/.test(hour);
		},
		
		// 분체크
		isMinute : function(minute) {  //10.12.27
			if($.trim(minute) == '')
				return true;
		return /^(0[0-9]|1[0-9]|2[0-9]|3[0-9]|4[0-9]|5[0-9])$/.test(minute);
		},

		setEventPrevent : function(event) {
			if(event.keyCode && (event.keyCode < 48 || event.keyCode > 57) )
				event.preventDefault();
		}
	})

	$.fn.validation = function(focus, setHelp) {
		var HTML = "";
		this.each(function() {
			$(this).find("input[validType]").each(function() {
				$.validation.setStyle($(this), $(this).attr("validType"));
			})
			//$(this).find("td > input:not(required), select:not(required)").each(function() {
			$(this).find("td > input:not(.required), select:not(.required)").each(function() {  //10.12.15
				
			    var $checkElement = $(this).parent().prev();
			    //if($checkElement[0].tagName == "TH") {
			    if($($checkElement[0]).tagName == "TH") {  //10.12.15
				    	 $checkElement.css("color", "" );
				         $checkElement.html($checkElement.text().replace("* ", ""));
			    }
			});
			$(this).find(".required").each(function() {
				if (HTML != "")
					HTML += "<BR>"
				HTML += $.validation.setRequiredElementCheck($(this), setHelp);
			})
			if (setHelp)
				$("#help").html(HTML);
		})

		if (focus) {
		//	$(this).find('input[type=text], input[type=checkbox], input[type=radio], select').first().focus();
			$(this).find('input[type=text]').first().select().focus();
		}

// if (setHelp) {
// var HTML = "";
// $(this).find(".required").each(function() {
// if (HTML != "")
// HTML += "<BR>"
// HTML += $.validation.setHelp($(this), $(this).attr("validType"));
// })
// $("#help").append(HTML);
// }

	}
})(jQuery);


// submit Utils
(function($) {
	$.onSubmit = function(data, klass) {
	}
	$.fn.onSubmit = function(options) {
		try {
			if (this.keepProcess) {
				alert("process ing.....");
				return false;
			}

			if (options.keepProcess != null || this.keepProcess == undefined)
				this.keepProcess = options.keepProcess;

			if (options.message != undefined) {
				if (!confirm(options.message))
					return false;
			}
			
			
			if (options.validation == true) {
				var validationResult = true;
				$(this).find(".required:not([disabled])").each(function() {
					validationResult = $.validation.setAfterSubmitValidationCheck($(this));
					return validationResult;
				})
				
				if(!validationResult)
					return false;
				
				$(this).find(".notrequired:not([disabled])").each(function() {
					validationResult = $.validation.setAfterSubmitValidationCheck2($(this));
					return validationResult;
				})

				if (!validationResult)
					return false;
			}

			if (options.searchPush == true) {
				var $form = $(this);
				$form.find("input:hidden").each(function() {
					var elementId = $(this).attr("id");
					var searchid = null;
					if (elementId) {
						searchId = "#search" + elementId.substring(0, 1).toUpperCase() + elementId.substring(1, elementId.length);
					}

					if ($(searchId).length > 0) {
						$(this).val($(searchId).val());
					}

				});
			}

			if (options.callpost != null)
				options.callpost();

			if (options.method != null) {
				$(this).find("#method").val(options.method);
			}

			if (options.postConformMessage) {
				if (!confirm(options.postConformMessage)) {
					return false;
				}
			}
			if (options.target != null) {
				$(this).attr({
					action : options.url,
					target : options.target
				}).submit();
			} else {
				if(window.dialogArguments){
					$(this).attr({
						action : options.url
					}).submit();
			    }else{
					$(this).attr({
						action : options.url,
						target : "_self"
					}).submit();
			    }
		

			}
			} catch (e){				
				$.onSubmit.keepProcess = false;
				return false;
			}
		return true;
	}
})(jQuery);

//submit Utils
(function($) {
	$.onVali = function(data, klass) {
	}
	$.fn.onVali = function(options) {
		try {
			if (this.keepProcess) {
				alert("process ing.....");
				return false;
			}
			
		
			if (options.validation == true) {
				var validationResult = true;
				$(this).find(".required:not([disabled])").each(function() {
					validationResult = $.validation.setAfterSubmitValidationCheck($(this));
					return validationResult;
				})
				
				if(!validationResult)
					return false;
				
				$(this).find(".notrequired:not([disabled])").each(function() {
					validationResult = $.validation.setAfterSubmitValidationCheck2($(this));
					return validationResult;
				})

				if (!validationResult)
					return false;
			}

			} catch (e){				
				$.onVali.keepProcess = false;
				return false;
			}
		return true;
	}
})(jQuery);

// checkbox Utils

(function($) {
	$.check = function(data, klass) {
	}

	$.extend($.check, {
		settings : {
		}
	})
	$.fn.check = function(target) {
		var $element = $(this);
		$element.unbind().click(function() {
			$(target +":not([disabled])").each(function() {
				if ($element.attr("id") == $(this).attr("id"))
					return true;
				$(this).attr("checked", $element.attr("checked"));
				if ($element.attr("checked"))
					$(this).parents('tr:first').addClass("selected");
				else
					$(this).parents('tr:first').removeClass("selected");
			});
			this.blur();
		});

		$(target).each(function() {
			if ($element.attr("id") != $(this).attr("id")) {
				$(this).unbind().click(function() {
					var checked = $(this).attr("checked");
					var checkAll = true;
					if (checked) {
						$(target).each(function() {
							if (!$(this).attr("checked")) {
								checkAll = false;
								return false;
							}
						});
						$(this).parents('tr:first').addClass("selected");
					} else {
						checkAll = false;
						$(this).parents('tr:first').removeClass("selected");
					}
					$element.attr("checked", checkAll);
					this.blur();
				});
			}
		});
	}

	$.fn.checkValidation = function(target, message) {
		if ($(target + ":checked").length == 0) {
			if (message) {
				alert(message);
			}
			return false;
		} else {
			//alert("선택된 항목은 : " + $(target + ":checked").length + "개입니다.");
			return true;
		}
	}
})(jQuery);


function ajaxCall(options) {
	var result = null;


	if (options.validationForm) {
		var validationResult = true;
		$("#"+options.validationForm).find(".required").each(function() {
			validationResult = $.validation.setAfterSubmitValidationCheck($(this));
			return validationResult;
		})
		if (!validationResult)
			return false;
	}

	options.data.put("ajaxId", options.ajaxId);
	options.data.put("method", options.method);
	$.ajax({
	    url: options.url,
	    async : options.async,
	    type: options.type,
	    timeout: 1000,
	    data: options.data.toMap(),
	    dataType : "json",
	    error: function(){
	        alert('죄송합니다. 시스템의 오류가 발생하였습니다.\n\n문제가 계속 발생할 경우 관리자에 문의하시기 바랍니다.');
	        result = false;
	    },
	    success: function(data){
	    	result = data.result;
	    }
	});
	return result;
}

function $get(id) {
	return document.getElementById(id);
}

//문자열 변환
function replaceAll(target, str1, str2) {
	var temp = $.trim(target);
	return temp.replace(eval("/" + str1 + "/gi"), str2);
}

//특수문자 처리
function convertToHtml(target) {
	target = replaceAll(target, "&amp;", "&");
	return target;
}
