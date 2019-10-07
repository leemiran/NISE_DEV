<!--
/**
 * [설명]
 * java 의 Map 인터페이스와 유사하게 사용가능하다.
 * 값에 의한 비교이므로 정수형은 문자열로 계산된다.
 * key 값에 null 이 들어가지 않는다면 try.. catch 블럭은 사용치 않아도 된다.
 *
 * [Public Method]
 *   - map.get(key):object - 지정된 key 에 해당하는 value 를 얻는다
 *   - map.remove(key):void - 지정된 key 에 해당하는 value 를 삭제한다
 *   - map.keys():array - 전체 Key 값들을 배열로 얻는다
 *   - map.values():array - 맵의 전체 값들을 배열로 얻는다
 *   - map.containsKey(key):boolean - key 가 포함되어 있다면 true 를 반환한다.
 *   - map.isEmpty():boolean - 맵이 비어있다면 true 를 반환한다.
 *   - map.clear():void - 맵을 비운다
 *   - map.size():int - 맵을 크기를 얻는다
 *   - map.toString():string - 객체를 문자열로 변환한다 (key^value|key^value|... 형태)
 *
 * [사용예]
 * <html>
 * <head>
 * <script language=javascript>
 * <!--
 *     try {
 *         var map = new JMap();
 *         map.put("key", "value");
 *         alert(map.get("0"));
 *     } catch (e) {
 *         alert(e.getMessage());
 *     }
 * -->
 * </script>
 * </head>
 * </html>
 *
 */
function JMap() {

	/** 배열의 index 상수 */
	this._INDEX_KEY = 0;
	this._INDEX_VALUE = 1;

	/** private 데이터 저장 Array */
	this._elementData = new Array(0);

	this.put = function(key, value) {
		var index = this._findIndexByKey(key);
		if (index >= 0) {
			(this._elementData[index])[this._INDEX_VALUE] = value;
		} else {
			var pair = new Array(2);
			pair[this._INDEX_KEY] = key;
			pair[this._INDEX_VALUE] = value;
			this._elementData.splice(this._elementData.length, 0, pair);
		}
	}

	/**
	 * public
	 * 지정된 key 에 해당하는 value 를 얻는다
	 * @param key 키값
	 * @return 키에 해당하는 값
	 */
	this.get = function(key) {
		var index = this._findIndexByKey(key);
		if (index >= 0) {
			return (this._elementData[index])[this._INDEX_VALUE];
		}
		return null;
	}

	/**
	 * public
	 * 지정된 key 에 해당하는 value 를 삭제한다
	 * @param key 키값
	 */
	this.remove = function(key) {
		var removeIndex = this._findIndexByKey(key);
		if (removeIndex >= 0) {
			this._elementData.splice(removeIndex, 1);
		}
	}
	/**
	 * public
	 * 맵의 전체 Key 값들을 배열로 얻는다
	 * @return key 값들의 Array
	 */
	this.keys = function() {
		var keys = new Array(this._elementData.length);
		for (var i = 0; i < this._elementData.length; i++) {
			keys[i] = (this._elementData[i])[this._INDEX_KEY];
		}
		return keys;
	}

	/**
	 * public
	 * 맵의 전체 값들을 배열로 얻는다.
	 * @return key 값들의 Array
	 */
	this.values = function() {
		var values = new Array(this._elementData.length);
		for (var i = 0; i < this._elementData.length; i++) {
			values[i] = (this._elementData[i])[this._INDEX_VALUE];
		}
		return values;
	}

	/**
	 * public
	 * 맵에 key 가 포함되어 있다면 true
	 * @param key 키값
	 * @return 키값 포함 여부
	 */
	this.containsKey = function(key) {
		return (this._findIndexByKey(key) >= 0);
	}

	/**
	 * public
	 * 맵이 비어있다면 true
	 * @return 맵이 비었는지의 여부
	 */
	this.isEmpty = function() {
		return (this.size() == 0);
	}

	/**
	 * public
	 * 맵을 비운다
	 */
	this.clear = function() {
		this._elementData.splice(0, this._elementData.length);
	}

	/**
	 * public
	 * 맵을 크기를 얻는다
	 * @return 맵의 크기
	 */
	this.size = function() {
		return this._elementData.length;
	}

	/**
	 * public
	 * 객체를 문자열로 변환한다 (key=value&key=value&... 형태)
	 * @return 문자열
	 */
	this.toString = function() {
		var str = "";
		for (var i = 0; i < this._elementData.length; i++) {
			if (i > 0) {
				str += "&";
			}
			str += (this._elementData[i]).join("=");
		}
		return str;
		
	}
	/**
	 * public
	 * 객체를 Map으로 변환한다.
	 * @return 문자열
	 */
	this.toMap = function() {
	//	this.toString().replace("&","");
		var result = this.toString();
		return result;
	}
	
	this.convertMap = function(convert) {
		var temp = convert.split("&");
		for (var i = 0 ; i < temp.length ; i++) {
			var params = temp[i].split("=");
			if (params.length == 2) {
				this.put(params[0], params[1]);
			}
		}
		
		return true;
	}
	
	
	/**
	 * private
	 * 해당 key 로 index 를 찾는다.
	 * 찾지 못하면 -1 을 return 한다. (0과의 비교가 가장 빠름)
	 * @param key 키값
	 * @exception JException
	 */
	this._findIndexByKey = function(key) {
		if (key == null) {
			return -1;
		}

		for (var i = this._elementData.length - 1; i >= 0; i--) {
			var oldKey = (this._elementData[i])[this._INDEX_KEY];
			if (key == oldKey) {
				return i;
			}
		}
		return -1;
	}
}

//-->
