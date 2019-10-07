(function () {
    epki = {};
    
    var url = window.location.host;
    // 모듈 라이브러리의 URL을 설정합니다.
    var kcaseLibUrl;
    var file;

    if(K_OSName == "Windows"){
        kcaseLibUrl = "http://203.236.236.27:8180/kcase/lib";
        file = "http://203.236.236.27:8180/kcase/KSignCASE_For_HTML5_Windows_v1.3.11.exe";
    }
    else if(K_OSName == "Mac") {
        kcaseLibUrl = "//" + url + "/kcase/lib/mac_n_linux_lib";
        file = "http://" + url + "/HTML5_KCaseAgentForMac.pkg";
    }
    else if(K_OSName == "Linux") {
        kcaseLibUrl = "//" + url + "/kcase/lib/mac_n_linux_lib";
        file = "http://" + url + "/" + "KCaseAgentForLinux-1.3.8-1.el6.i386.rpm";
    }
		
    // 버전이 일치하지 않거나, 무결성에 실패할 때 함수를 설정합니다.
    var failIntegrityCheck = function(c, msg) {
        alert(c + ": "+ msg);
        location.href = file;
    };

    // 미설치 및 미실행 시 동작할 함수를 설정합니다.
    var failAgentService = function() {
        alert("보안 모듈이 실행중이 아닙니다.");
        location.href = file;
    };

    // 보안 모듈 세션이 만료되었을 시 동작할 함수를 설정합니다.
    var failAgentSession = function() {
        alert("세션이 만료되었습니다. 다시 접속해주십시오.");
    };

    // 휴대폰 인증 서비스가 설치되지 않았을 때 동작할 함수를 설정합니다.
    var notInstalledUbiKey = function() {
        alert("휴대폰 인증 서비스가 설치되지 않았습니다.");
    };

    // Dialog에서 취소버튼 또는 X 눌렀을때 동작하는 함수를 설정합니다.
    var cancelDialog = function() {
        alert("취소되었습니다.");
    };

    // Dialog에서 추가로 생성되는 창을 취소하거나 닫았을때 동작하는 함수를 설정합니다.
    var cancelSubDialog = function() {
        alert("다이얼로그를 닫습니다.");
    };

    // 비밀번호 오류 횟수 초과시 동작할 함수
    var wrongPasswordExcess = function(errCode, errMsg) {
        alert(errCode + " : " + errMsg);
    };

    /**
     * 모듈 초기화
     *
     * @method init
     * @param {String} sessionId : 서버 세션 ID
     */
    epki.init = function(sessionId, success, fail) {

        kcaseagt.defineServiceError(function(){
        	if(typeof(fail) == "function") {
        		fail();
        	} else {
            	failAgentService();
        	}
        });
        kcaseagt.defineSessionExpired(failAgentSession);
        kcaseagt.defineNotInstalledUbiKey(notInstalledUbiKey);
        kcaseagt.defineCancelDialog(cancelDialog);
        kcaseagt.defineCancelSubDialog(cancelSubDialog);
        kcaseagt.definePasswordExcess(wrongPasswordExcess);

		/*epki.addCertPolicy("1.2.410.100001.5.3.1.1");
		epki.addCertPolicy("1.2.410.100001.5.3.1.3");
		epki.addCertPolicy("1.2.410.100001.5.3.1.5");
		epki.addCertPolicy("1.2.410.100001.5.3.1.7");
		epki.addCertPolicy("1.2.410.100001.5.3.1.9");
		epki.addCertPolicy("1.2.410.100001.2.2.1");
		epki.addCertPolicy("1.2.410.100001.2.1.1");
		

		*/

       kcaseagt.init({
            libRoot: kcaseLibUrl,
            sessId: sessionId,
            mediaOpt: kcaseagt.enable.all,
            mainTitle : "인증서 입력 (전자서명)",
            adminTitle: "인증서 관리",
            maxpwdcnt: 5,									// 비밀번호 오류 횟수 선언부
            success: success,
            error: function(c, msg){
		if(typeof(fail) == "function") {
	        	fail();
	        } else {
	        	failIntegrityCheck(c, msg);
        	}
            }
        });
    };

    /**
     * 전자서명 생성
     *
     * @method sign
     * @param {Number} inDataFlag : 전자 서명 메시지 내에 서명 원문을 포함시킬지 여부
     * @param {String} algID : 전자서명에 사용할 서명 알고리즘
     * @param {String} plainData : 전자 서명할 원문 메시지
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.sign = function(inDataFlag, algID, plainData, success, error) {
        kcaseagt.genSignData({
            input: plainData,
            success: function (output) {
                success(output);
            },
            error: function (c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 전자서명 검증
     *
     * @method verify
     * @param {Boolean} inDataFlag : 전자 서명 메시지 내에 서명 원문이 포함되어 있는지 여부
     * @param {String} signedData : Base64 형태로 인코딩 된 전자 서명 메시지 문자열
     * @param {String} originalData : 전자 서명 원문 메시지
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.verify = function(inDataFlag, signedData, originalData, success, error) {
        kcaseagt.verifySignData({
            input: signedData,
            success: function(output) {
                success(kcaseagt.decodeUtf8(output));
            },
            error: function (c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 대칭키 생성
     *
     * @method genSymmetricKey
     * @param {String} algID : 생성된 키가 사용될 대칭 키의 알고리즘
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.genSymmetricKey = function(algID, success, error) {
        var algorithm;

        if(algID == "SEED") {
            algorithm = kcaseagt.algorithm.SEED;
        } else if(algID == "ARIA") {
            algorithm = kcaseagt.algorithm.ARIA12;
        } else if(algID == "3DES") {
            algorithm = kcaseagt.algorithm.DES3;
        } else if(algID == "AES") {
            algorithm = kcaseagt.algorithm.AES;
        }

        kcaseagt.generateSymKeyIv({
            algorithm: algorithm,
            success: function(key, iv) {
                success({
                    key: key,
                    iv: iv
                });
            },
            error: function(c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 대칭키 암호화
     *
     * @method encrypt
     * @param {String} algID : 암호화 시에 사용할 대칭 키의 알고리즘 문자열
     * @param {String} symKey : 생성 된 대칭 키 정보 문자열
     * @param {String} plainData : 암호화 할 평문 메시지 문자열
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.encrypt = function(algID, symKey, plainData, success, error) {
        var algorithm;

        if(algID == "SEED") {
            algorithm = kcaseagt.algorithm.SEED;
        } else if(algID == "ARIA") {
            algorithm = kcaseagt.algorithm.ARIA12;
        } else if(algID == "3DES") {
            algorithm = kcaseagt.algorithm.DES3;
        } else if(algID == "AES") {
            algorithm = kcaseagt.algorithm.AES;
        }

        kcaseagt.blockCipher({
            mode: kcaseagt.mode.encrypt,
            algorithm: algorithm,
            key: symKey.key,
            iv: symKey.iv,
            input: plainData,
            success: function(output) {
                success(output);
            },
            error: function (c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 대칭키 복호화
     *
     * @method decrypt
     * @param {String} algID : 복호화 시에 사용할 대칭 키의 알고리즘 문자열
     * @param {String} symKey : 대칭 키 정보 문자열
     * @param {String} encryptedData : Base64 인코딩 된 암호화 메시지 문자열
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.decrypt = function(algID, symKey, encryptedData, success, error) {
        var algorithm;

        if(algID == "SEED") {
            algorithm = kcaseagt.algorithm.SEED;
        } else if(algID == "ARIA") {
            algorithm = kcaseagt.algorithm.ARIA12;
        } else if(algID == "3DES" || algID == "DESEDE") {
            algorithm = kcaseagt.algorithm.DES3;
        } else if(algID == "AES") {
            algorithm = kcaseagt.algorithm.AES;
        }

        kcaseagt.blockCipher({
            mode: kcaseagt.mode.decrypt,
            algorithm: algorithm,
            key: symKey.key,
            iv: symKey.iv,
            input: encryptedData,
            success: function(output) {
                success(output);
            },
            error: function (c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 대칭키 JSON 형태로 복호화
     *
     * @method decrypt
     * @param {String} algID : 복호화 시에 사용할 대칭 키의 알고리즘 문자열
     * @param {String} symKey : 대칭 키 정보 문자열
     * @param {String} encryptedData : Base64 인코딩 된 암호화 메시지 문자열
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.decryptJsonObj = function(algID, symKey, encryptedData, success, error) {
        var algorithm;

        if(algID == "SEED") {
            algorithm = kcaseagt.algorithm.SEED;
        } else if(algID == "ARIA") {
            algorithm = kcaseagt.algorithm.ARIA12;
        } else if(algID == "3DES") {
            algorithm = kcaseagt.algorithm.DES3;
        } else if(algID == "AES") {
            algorithm = kcaseagt.algorithm.AES;
        }

        kcaseagt.blockCipher({
            mode: kcaseagt.mode.decrypt,
            algorithm: algorithm,
            key: symKey.key,
            iv: symKey.iv,
            input: encryptedData,
            success: function(output) {
        		success(JSON.parse(output));
            },
            error: function (c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 공개키 암호화
     *
     * @method envelop
     * @param {String} algID : 메시지 암호 시 사용할 대칭 키 알고리즘
     * @param {String} recipientCerts : Base64 인코딩 된 수신 대상자 인증서 문자열 목록
     * @param {String} plainData : 암호화 할 평문 메시지 문자열
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.envelop = function(algID, recipientCerts, plainData, success, error) {
        var algorithm;

        if(algID == "SEED") {
            algorithm = kcaseagt.algorithm.SEED;
        } else if(algID == "ARIA") {
            algorithm = kcaseagt.algorithm.ARIA12;
        } else if(algID == "3DES") {
            algorithm = kcaseagt.algorithm.DES3;
        } else if(algID == "AES") {
            algorithm = kcaseagt.algorithm.AES;
        }

        kcaseagt.genEnvelopedData({
            algorithm: algorithm,
            peerCert: recipientCerts,
            input: plainData,
            success: function(output) {
                success(output);
            },
            error: function (c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 채널보안 생성
     *
     * @method requestSession
     * @param {String} serverCert : Base64 형태로 인코딩 된 서버의 공개키(암호화용 인증서 문자열)
     * @param {String} algID : 보안 채널에서 사용될 암호 알고리즘
     * @param {String} sessionID : 서버로부터 전송 받은 세션 식별자 문자열
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.requestSession = function(serverCert, algID, sessionID, success, error) {
        var algorithm;

        if(algID == "SEED") {
            algorithm = kcaseagt.algorithm.SEED;
        } else if(algID == "ARIA") {
            algorithm = kcaseagt.algorithm.ARIA12;
        } else if(algID == "3DES") {
            algorithm = kcaseagt.algorithm.DES3;
        } else if(algID == "AES") {
            algorithm = kcaseagt.algorithm.AES;
        }

        kcaseagt.initSecureChannel({
            peerCert: serverCert,
            algorithm: algorithm,
            success: function(output) {
                success(output);
            }, error: function(c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 채널보안 암호화
     *
     * @method sessionEncrypt
     * @param {String} sessionID : 서버와 세션을 유지하기 위한 세션 ID 문자열(서버의 세션 ID)
     * @param {String} plainData : 암호화 할 메시지
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.sessionEncrypt = function(sessionID, plainData, success, error) {
        plainData = kcaseagt.encode64(kcaseagt.encodeUtf8(plainData));

        kcaseagt.secureEncrypt({
            input: plainData,
            success: function(output) {
                success(output);
            }, error: function(c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 채널보안 복호화
     *
     * @method sessionDecrypt
     * @param {String} sessionID : 서버와 세션을 유지하기 위한 세션 ID 문자열(서버의 세션 ID)
     * @param {String} encryptedData : Base64 인코딩 된 암호화 문자열
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.sessionDecrypt = function (sessionID, encryptedData, success, error) {
        kcaseagt.secureDecrypt({
            input: encryptedData,
            success: function (output) {
            	//2017.07.20 DYLEE 채널보안 복호화 시 한글이 깨지는 현상이 발견되어 수정
                success(kcaseagt.decodeUtf8(kcaseagt.decode64(output)));
            }, error: function (c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 채널보안 종료
     * @param {String} sessionID : 서버와 세션을 유지하기 위한 세션 ID 문자열(서버의 세션 ID)
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.destroySession = function (sessionID, success, error) {
        kcaseagt.closeSecureChannel({
            success: function () {
                success();
            }, error: function (c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 채널보안 및 로그인 동시수행
     * @param serverCert
     * @param algID
     * @param sessionID
     * @param success
     * @param error
     */
    epki.reqSecChannelAndLogin = function(serverCert, algID, sessionID, success, error) {
        var algorithm;
        if(algID == "SEED") {
            algorithm = kcaseagt.algorithm.SEED;
        } else if(algID == "ARIA") {
            algorithm = kcaseagt.algorithm.ARIA12;
        } else if(algID == "3DES") {
            algorithm = kcaseagt.algorithm.DES3;
        } else if(algID == "AES") {
            algorithm = kcaseagt.algorithm.AES;
        }

        kcaseagt.secureChannelLogin({
            peerCert: serverCert,
            algorithm: algorithm,
            success: function(output) {
                success(output);
            }, error: function(c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 채널보안 및 암호화 동시수행
     * @param serverCert
     * @param algID
     * @param input
     * @param success
     * @param error
     */
    epki.reqSecChannelAndEncrypt = function(serverCert, algID, input, success, error) {
        var algorithm;

        if(algID == "SEED") {
            algorithm = kcaseagt.algorithm.SEED;
        } else if(algID == "ARIA") {
            algorithm = kcaseagt.algorithm.ARIA12;
        } else if(algID == "3DES") {
            algorithm = kcaseagt.algorithm.DES3;
        } else if(algID == "AES") {
            algorithm = kcaseagt.algorithm.AES;
        }

        kcaseagt.initSecureChannel({
            peerCert: serverCert,
            input: kcaseagt.encode64(kcaseagt.encodeUtf8(input)),
            algorithm: algorithm,
            success: function(output) {
                success(output);
            }, error: function(c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 인증서 로그인
     *
     * @method login
     * @param {String} serverCert : Base64 형태로 인코딩 된 서버의 공개키(암호화용 인증서 문자열)
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.login = function(serverCert, success, error) {
        kcaseagt.certLogin({
            peerCert: serverCert,
            success: function(output) {
                kcaseagt.secureEncrypt({
                    input: output,
                    success: function(encData) {
                        success(encData);
                    }, error: function(c, msg) {
                        error(c, msg);
                    }
                });
            }, error: function(c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 신원확인 기반 인증서 로그인
     *
     * @method vidLogin
     * @param {String} serverCert : Base64 형태로 인코딩 된 서버의 공개키(암호화용 인증서 문자열)
     * @param {String} userID : 인증서의 신원확인 정보 처리를 위한 주민등록 번호 또는 사업자 등록번호
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.vidLogin = function(serverCert, userID, success, error) {
        kcaseagt.certLogin({
            peerCert: serverCert,
            vid: userID,
            success: function(output) {
                kcaseagt.secureEncrypt({
                    input: output,
                    success: function(encData) {
                        success(encData);
                    }, error: function(c, msg) {
                        error(c, msg);
                    }
                });
            }, error: function(c, msg) {
                error(c, msg);
            }
        });
    };

    /**
     * 신원확인
     *
     * @method requestVerifyVID
     * @param {String} serverCert : Base64 형태로 인코딩 된 서버의 공개키(암호화용 인증서 문자열)
     * @param {String} userID : 인증서의 신원확인 정보 처리를 위한 주민등록 번호 또는 사업자 등록번호
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.requestVerifyVID = function(serverCert, userID, success, error) {
        kcaseagt.getVidInfo({
            peerCert: serverCert,
            vid: userID,
            success: function(output) {
                success(output);
            },
            error: function(c, msg) {
                error(c, msg);
            }
        });
    };

   /**
     * 선택한 인증서로 인증서 유효성 검증(SCVP)을 수행
     *
     * @method sign
     * @param {String} svrIp : SCVP 검증서버 IP
     * @param {String} svrPort : SCVP 검증서버 PORT
     * @param {Function} success : 성공 콜백함수
     * @param {Function} error : 실패 콜백함수
     */
    epki.validCert = function(svr_Ip, svr_Port , success, error) {
        kcaseagt.genSignForValid({
            svrIp: svr_Ip,
            svrPort: svr_Port,
            success: function (output) {
                success(output);
            },
            error: function (c, msg) {
                error(c, msg);
            }
        });
    };
    /**
     * 인증서 관리 기능 실행
     *
     * @method invokeCMDlg
     *
     */
    epki.invokeCMDlg = function() {
        kcaseagt.openAdminDialog();
    };

    /**
     * 다이얼로그에 인증서를 표시할 인증서 정책 OID를 입력
     *
     * @param {String} oid : 인증서 정책 OID
     */
    epki.addCertPolicy = function(oid) {
        kcaseagt.addCertPolicy(oid);
    };

    /**
     * BASE64 인코딩
     *
     * @param {String} input : 입력값
     * @returns {String} output : BASE64 인코딩값
     */
    epki.encode64 = function(input) {
        return kcaseagt.encode64(input);
    };

    /**
     * BASE64 디코딩
     * @param {String} input : BASE64 인코딩된 입력값
     * @returns {String} output : BASE64 디코딩값
     */
    epki.decode64 = function(input) {
        return kcaseagt.decode64(input);
    };

    /**
     * UTF-8 인코딩
     *
     * @param {String} input : 입력값
     * @returns {String} output : UTF-8 인코딩값
     */
    epki.encodeUtf8 = function(input) {
        return kcaseagt.encodeUtf8(input);
    };

    /**
     * UTF-8 디코딩
     * @param {String} input : UTF-8 인코딩된 입력값
     * @returns {String} output : UTF-8 디코딩값
     */
    epki.decodeUtf8 = function(input) {
        return kcaseagt.decodeUtf8(input);
    };
    
    /**
     * 비대칭키 암호화
     * @param {String} input : 서버인증서 경로
     * @param {String} input : 암호호할 원문값
     * @returns {String} output : 비대칭키로 암호화된 데이터(B64데이터)
     */
     
    epki.requestAsymmEnc = function(svrpath, PlainData, success, error) {
    	
        kcaseagt.AsymmEncrypt({
            FileName: svrpath,
            Input: PlainData,
            success: function(output) {               	
                success(output);
            },
            error: function(c, msg) {
                error(c, msg);
            }
        });
      
    };
}());
