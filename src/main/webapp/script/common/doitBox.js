 
var fileLoadingImage = context + "/images/adm/common/loading.gif";
var fileBottomNavCloseImage = context + "/images/admin/closelabel.gif";
var overlayOpacity = 0.5;
var animate = true;
var resizeSpeed = 8.6;
var borderSize = 10; 

var imageArray = new Array;
var activeImage;
if(animate == true){
    overlayDuration = 0.3;  // shadow fade in/out duration
    if(resizeSpeed > 10){ resizeSpeed = 10;}
    if(resizeSpeed < 1){ resizeSpeed = 1;}
    resizeDuration = (11 - resizeSpeed) * 0.15;
} else {
    overlayDuration = 0;
    resizeDuration = 0;
}
// -----------------------------------------------------------------------------------

var opacity_level = 5; // how transparent our overlay bg is

var imgPreloader = new Image(); // create an preloader object
var loadCancelled = false;
var ibox_w_height = 0;
var oldPosition = "divPosition";
var currentPosition = "divPosition";

// button 정의
var btnConfirm =  '<input type="button" id="ibox_Confirm" 	name="ibox_Confirm" 	value="확인" 	class="btn1"/>';
var btnSave =  	  '<input type="button" id="ibox_Save" 		name="ibox_Save" 		value="저장" 	class="btn1"/>';
var btnClose =    '<input type="button" id="ibox_Close" 	name="ibox_Close" 		value="닫기" 	class="btn1"/>';
var btnCancel =   '<input type="button" id="ibox_Cancel" 	name="ibox_Cancel" 		value="취소" 	class="btn1"/>';
var btnMove =     '<input type="button" id="ibox_Move" 		name="ibox_Move" 		value="이동" 	class="btn1"/>';
var btnEdit =     '<input type="button" id="ibox_Edit" 		name="ibox_Edit" 		value="수정" 	class="btn1"/>';
var btnList =     '<input type="button" id="ibox_List" 		name="ibox_List" 		value="목록" 	class="btn1"/>';
var btnChange =   '<input type="button" id="ibox_Change" 	name="ibox_Change" 		value="변경" 	class="btn1"/>';

var box = Class.create();
box.prototype = {
    initialize : function()
    {
    	var temp = new Array();
        temp[temp.length] = '<div id="ibox_w" style="display: none; position: absolute; top: 0; left: 0; z-index: 90; width: 100%; height: 100%; background-color: #000;"></div>';
        temp[temp.length] = '	<div id="ibox_wrapper" class="dvpop" style="display:none; position:absolute; z-index:100;">';
        temp[temp.length] = '    	<div id="ibox_title" class="poptit"></div>';    // 팝업 타이틀
        temp[temp.length] = '    	<div id="ibox_content" class="poptxt3"></div>'; // 팝업 내용부
        temp[temp.length] = '    	<div id="ibox_btn" class="popbtn"></div>';      // 팝업 button
        temp[temp.length] = '	</div>';
        temp[temp.length] = '<form id="box_form" name="box_form" method="post" target="ibox_img"></form>';
        
        var docBody = $$("body")[0];
		if( docBody)
			new Insertion.Top(docBody, temp.join(''));
			
    },
    // type : 1 : 이미지
    // type : 2 : #
    // type : 3 : ajax
    // type : 4 : iframe
    service : function(url, title, divHeight, divWidth, type, input, userFunction, btn)
    {
        hideSelectBoxes();
        hideFlash();
        this.contentType = type;
        this.userFunction = userFunction;
        this.btn = btn;
        this.input = input;
        if( $('sd')) {
        	this.input[this.input.length] = "sd="+$('sd').value;
        }
        if( $('md')) {
        	this.input[this.input.length] = "md="+$('md').value;
        }
        if( $('ms')) {
        	this.input[this.input.length] = "ms="+$('ms').value;
        }
        if( $('ps')) {
        	this.input[this.input.length] = "ps="+$('ps').value;
        }
        this.btn = btn;
        this.btnConfirm = null;
        this.btnSave    = null;
        this.btnClose   = null;
        this.btnCancel  = null;
        this.btnMove    = null;
        this.btnEdit    = null;
        this.btnList    = null;
        this.btnChange  = null;
        
        this.btnHidden();
        this.btnProcess();
        
        var arrayPageSize = getPageSize();
        $('ibox_w').setStyle({ height: arrayPageSize[1] + 'px' });

        new Effect.Appear($('ibox_w'), { duration: overlayDuration, from: 0.0, to: overlayOpacity });

//        document.body.style.overflow = "hidden";
        
        // 타이틀 입력 부분
        this.setTitle(title);
        
        // 타이틀 입력 부분 끝
        if (type > -1 && type < 5)
        {
            this.iboxType(type, url, divHeight, divWidth, userFunction)
        }
        
        return ;
    },
    iboxType : function(type, url, divHeight, divWidth, userFunction) {
        var typeFunction;
        var loading = "<table width=\"100%\" height=\"100%\"><tr><td align=\"center\" valign=\"middle\"><img src=" + fileLoadingImage + "></td></tr></table>";
        Element.show($('ibox_content'));
        Element.update($('ibox_content'),loading);
        switch(type) {
            case 1:
                break;
            case 2:
                    typeFunction = function() {
                    Element.update($('ibox_content'),url);
                    userFunction();
                }
                break;
            case 3:
                break;
            case 4:
                typeFunction = function() {
                    var iframeWidth = (Element.getWidth($('ibox_content'))) + "px";
                    var iframeHeight = (Element.getHeight($('ibox_content'))) + "px";
                    var strHTML = '<iframe id="ibox_iframe" name="ibox_iframe" scrolling="auto" frameborder="0" style="width: ' + iframeWidth + '; height:' + iframeHeight + '; border:0; margin:0; padding:0;"/>';
                    $('ibox_content').innerHTML = strHTML;                  

                    var inputHTML = '';
                    if (mybox.input != null) {
                        mybox.input.each(function(inputData){
                            var data = inputData.split("=");
                            for (var i = 0 ; i < data.length ; i++) {
                                if (data[i].substr(0,1) == " ")
                                {
                                    data[i] = data[i].substr(1, data[i].length);
                                }
                                if (data[i].substr(data[i].length-1,1) == " ")
                                {
                                    data[i] = data[i].substr(0, data[i].length-1);
                                }
                            }
                            inputHTML +=  '<input type="hidden"';
                            inputHTML +=    'id="'+data[0]+'" ';
                            inputHTML +=    'name="'+data[0]+'" ';
                            inputHTML +=    'value="'+data[1]+'">';
                        })
                    }
                    inputHTML += '<input type="hidden" id="pop" name="pop" value="true" />';
                    var boxForm = $('box_form');
                        
                    new Insertion.Top( boxForm,inputHTML);
                    boxForm.setAttribute( "action", url);
                    boxForm.setAttribute( "target", "ibox_iframe");
                    boxForm.submit();
                    Element.update( boxForm, "");
                    
                    if( userFunction)
                        userFunction();
                }
                break;
        }
        this.showIbox(divHeight, divWidth, typeFunction, true);
        this.clickEvent();
        return;
    },
    setTitle : function(title) {
        Element.update($('ibox_title'), "<span>" + title + " </span>");
    },
    showIbox : function(divHeight, divWidth, typeFunction, firstAction) {
    	scrollTo(0, 0);
    	
        $('ibox_wrapper').setStyle({ width: divWidth + 'px', height: divHeight + 'px' });
        
		var widthNew = parseInt(divWidth);
		var heightNew = parseInt(divHeight) - 65;

        // calculate top and left offset for the lightbox 
        var arrayPageSize = getPageSize();
        var lightboxTop = (document.viewport.getHeight() / 10);
		var lightboxLeft = Math.floor((arrayPageSize[0] - widthNew)/2);
        $('ibox_wrapper').setStyle({ top: lightboxTop + 'px', left: lightboxLeft + 'px' });
       
        var showFunction = function() {
            if (firstAction){
                if (typeFunction != undefined) {
                    typeFunction();
                }
            }
        }

        $('ibox_content').setStyle({ width: widthNew + 'px', height: heightNew + 'px' });
		new Effect.Grow( $('ibox_wrapper'), { direction: 'top-left', duration: overlayDuration, afterFinish : showFunction});
    },
    btnHidden : function() {
        $('ibox_btn').innerHTML = "";
    },
    btnProcess : function() {
        if (this.btn != null && this.btn != Prototype.emptyFunction)
        {
            for (var i = 0 ; i < this.btn.length ; i++) {
                var empty = "";
                if (i+1 < this.btn.length) {
                    empty = "&nbsp;"
                }
                
                switch (this.btn[i].type) {
                    case 1 : this.btnConfirm    = this.btn[i].func;
//                           $('ibox_Confirm').style.display="";
                             $('ibox_btn').innerHTML = $('ibox_btn').innerHTML + btnConfirm + empty;
                             break;
                    case 2 : this.btnSave       = this.btn[i].func;
//                           $('ibox_Save').style.display="";
                             $('ibox_btn').innerHTML = $('ibox_btn').innerHTML + btnSave + empty;
                             break;
                    case 3 : this.btnClose      = this.btn[i].func;
//                           $('ibox_Close').style.display="";
                             $('ibox_btn').innerHTML = $('ibox_btn').innerHTML + btnClose + empty;
                             break;
                    case 4 : this.btnCancel     = this.btn[i].func;
//                           $('ibox_Cancel').style.display="";
                             $('ibox_btn').innerHTML = $('ibox_btn').innerHTML + btnCancel + empty;
                             break;
                    case 5 : this.btnMove       = this.btn[i].func;
//                           $('ibox_Move').style.display="";
                             $('ibox_btn').innerHTML = $('ibox_btn').innerHTML + btnMove + empty;
                             break;
                    case 6 : this.btnEdit       = this.btn[i].func;
//                           $('ibox_Move').style.display="";
                             $('ibox_btn').innerHTML = $('ibox_btn').innerHTML + btnEdit + empty;
                             break;
                    case 7 : this.btnList       = this.btn[i].func;
//                           $('ibox_Move').style.display="";
                             $('ibox_btn').innerHTML = $('ibox_btn').innerHTML + btnList + empty;
                             break;
                    case 8 : this.btnChange       = this.btn[i].func;
//                           $('ibox_Move').style.display="";
                             $('ibox_btn').innerHTML = $('ibox_btn').innerHTML + btnChange + empty;
                             break;
                }
            }
            var element = $('ibox_btn').descendants;
            
            var count = 0;
            for (var i = 0 ; i < element.length ; i++) {
                if (count > 0  && element[i].style.display != 'none') {
                    new Insertion.Before(element[i], "&nbsp;");
                    count++;
                }
            }
            if (count > 0) {
                Element.show($('ibox_btn'));
            }
        }
        else {
            $('ibox_Close').style.display="";
            $('ibox_btn').innerHTML = this.btnClose;
            Element.show($('ibox_btn'));
        }
    },
    clickEvent : function()
    {
        if ($('ibox_Confirm') != null && $('ibox_Confirm') != undefined) {
            Event.observe('ibox_Confirm', 'click', function(event){
                mybox.hideIbox(mybox.btnConfirm);
            }, false);
        }
        if ($('ibox_Save') != null && $('ibox_Save') != undefined) {
            Event.observe('ibox_Save', 'click', function(event){
                mybox.hideIbox(mybox.btnSave);
            }, false);
        }
        if ($('ibox_Close') != null && $('ibox_Close') != undefined) {
            Event.observe('ibox_Close', 'click', function(event){
                mybox.hideIbox(mybox.btnClose);
            }, false);
        }
        if ($('ibox_Cancel') != null && $('ibox_Cancel') != undefined) {
            Event.observe('ibox_Cancel', 'click', function(event){
                mybox.hideIbox(mybox.btnCancel);
            }, false);
        }
        if ($('ibox_Move') != null && $('ibox_Move') != undefined) {
            Event.observe('ibox_Move', 'click', function(event){
                mybox.hideIbox(mybox.btnMove);
            }, false);
        }
        if ($('ibox_Edit') != null && $('ibox_Edit') != undefined) {
            Event.observe('ibox_Edit', 'click', function(event){
                mybox.hideIbox(mybox.btnEdit);
            }, false);
        }
        if ($('ibox_List') != null && $('ibox_List') != undefined) {
            Event.observe('ibox_List', 'click', function(event){
                mybox.hideIbox(mybox.btnList);
            }, false);
        }
        if ($('ibox_Change') != null && $('ibox_Change') != undefined) {
            Event.observe('ibox_Change', 'click', function(event){
                mybox.hideIbox(mybox.btnChange);
            }, false);
        }
    },
    getInnerWindow : function() {
        if ($('ibox_iframe') != undefined) {
            return $('ibox_iframe').contentWindow;
        }
        else {
            return self;
        }
    },
    btnUpdate : function( btn) {
        this.btnConfirm = null;
        this.btnSave    = null;
        this.btnClose   = null;
        this.btnCancel  = null;
        this.btnMove    = null;
        this.btnEdit    = null;
        this.btnList    = null;
        this.btnChange  = null;
        
        this.btn = btn;
        this.btnHidden();
        this.btnProcess();
        this.clickEvent(); 
    },
    refreshIbox : function( title, divHeight, divWidth, userFunction, btn)
    {
        this.btnUpdate( btn);
        
        this.resizeIbox( divHeight, divWidth, userFunction);
        
        // 타이틀 입력 부분
        this.setTitle( title);
    },
    resizeIbox : function(divHeight, divWidth, showFunction) {
    	contentContainer = $('ibox_content');

        // get current width and height
        var widthCurrent  = contentContainer.getWidth();
        var heightCurrent = contentContainer.getHeight();

        // get new width and height
		var widthNew = parseInt(divWidth);
		var heightNew = parseInt(divHeight) - 65;
//		var widthNew = (parseInt(divWidth)  + (borderSize * 2) - 25);
//		var heightNew = (parseInt(divHeight)  + (borderSize * 2) - 84);

        // scalars based on change from old to new
        var xScale = (widthNew  / widthCurrent)  * 100;
        var yScale = (heightNew / heightCurrent) * 100;

        // calculate size difference between new and old image, and resize if necessary
        var wDiff = widthCurrent - widthNew;
        var hDiff = heightCurrent - heightNew;

        if (hDiff != 0) new Effect.Scale( contentContainer, yScale, {scaleX: false, duration: resizeDuration, queue: 'front'}); 
        if (wDiff != 0) new Effect.Scale( contentContainer, xScale, {scaleY: false, duration: resizeDuration, delay: resizeDuration}); 

        // if new and old image are same size and no scaling transition is necessary, 
        // do a quick pause to prevent image flicker.
        var timeout = 0;
        if ((hDiff != 0) || (wDiff != 0)){
            timeout = 100;
            if (Prototype.Browser.IE) timeout = 250;   
        }

        (function(){
        	$('ibox_wrapper').setStyle({ width: divWidth + 'px', height: divHeight + 'px' });
        	if($('ibox_iframe') != undefined)
        	{
        		$('ibox_iframe').setStyle({ width: widthNew + 'px', height: heightNew + 'px'});
        	}
        }).bind(this).delay(timeout / 1000);
    },
    resizeIBoxByContent : function() {
        var dim;        
    	if( this.contentType == '4'){
    		dim = this.getInnerWindow().getPageSize();
    	}
    	else {
    		dim = $('ibox_content').getDimensions();    		
    	}
   		dim[1] = dim[1] + 65;
        
        this.resizeIbox( dim[1], $('ibox_content').getWidth(), Prototype.emptyFunction);
        
    },
    resizeIBoxByContentForContentUpload : function() {
        var dim;        
    	if( this.contentType == '4'){
    		dim = this.getInnerWindow().getPageSize();
    	}
    	else {
    		dim = $('ibox_content').getDimensions();    		
    	}
   		dim[1] = dim[1] + 65;
        
        this.resizeIbox( dim[1] + 40, $('ibox_content').getWidth(), Prototype.emptyFunction);
        
    },
    refreshIBoxByContent : function( title, btn) {
        this.btnUpdate( btn);
        
        this.resizeIBoxByContent();
        
        // 타이틀 입력 부분
        this.setTitle( title);
        
    },
    hideIbox : function(finishFunction) {
        if (finishFunction != null && finishFunction != Prototype.emptyFunction)
        {
            if (finishFunction != Prototype.emptyFunction) {
                if (!finishFunction()) {
                        return;
                }
            }
        }
        Element.hide($('ibox_content'));
  //      Element.update($('ibox_content'), "");
        var hideFunction = function(){
			if( $('box_form') != undefined)
				Element.update($('box_form'), "");
			if( $('ibox_title') != undefined)
				Element.update($('ibox_title'), "");
			if( $('ibox_content') != undefined)
				Element.update($('ibox_content'), "");
			if( $('ibox_btn') != undefined)
				Element.update($('ibox_btn'), "");
			if( $('ibox_w') != undefined)
				Element.update($('ibox_w'), "");
        }
        new Effect.Shrink( $('ibox_wrapper'), { direction: 'center', duration: resizeDuration});
        new Effect.Fade( $('ibox_w'), { duration: overlayDuration, afterFinish : hideFunction});
        showSelectBoxes();
        showFlash();
    },
    isActivated : function(){
		outerContainer = $('ibox_wrapper');
		return Object.isElement( outerContainer) && Element.visible( outerContainer);		
    }    
}

mybox = new box();

Event.observe(window, 'load', function(){
	Event.observe( document, 'mousedown', function(event){
		if( mybox.isActivated()){
        	if( Event.isRightClick(event)) {
	        	alert("먼저 팝업을 닫아주세요.");
        	}
		}
	}, false);
});

// -----------------------------------------------------------------------------------

// ---------------------------------------------------

function showSelectBoxes(){
    var selects = document.getElementsByTagName("select");
    for (i = 0; i != selects.length; i++) {
//      selects[i].style.visibility = "visible";
       	Element.show(selects[i]);
    }
}

// ---------------------------------------------------

function hideSelectBoxes(){
    var selects = document.getElementsByTagName("select");
    for (i = 0; i != selects.length; i++) {
//      selects[i].style.visibility = "hidden";
        Element.hide(selects[i]);
    }
}

// ---------------------------------------------------

function showFlash(){
    var flashObjects = document.getElementsByTagName("object");
    for (i = 0; i < flashObjects.length; i++) {
        flashObjects[i].style.visibility = "visible";
    }
    var flashEmbeds = document.getElementsByTagName("embed");
    for (i = 0; i < flashEmbeds.length; i++) {
        flashEmbeds[i].style.visibility = "visible";
    }
}

// ---------------------------------------------------

function hideFlash(){
    var flashObjects = document.getElementsByTagName("object");
    for (i = 0; i < flashObjects.length; i++) {
        flashObjects[i].style.visibility = "hidden";
    }

    var flashEmbeds = document.getElementsByTagName("embed");
    for (i = 0; i < flashEmbeds.length; i++) {
        flashEmbeds[i].style.visibility = "hidden";
    }

}

Object.extend(Element, {
    getWidth: function(element) {
        element = $(element);
        return element.offsetWidth;
    },
    getTop: function(element) {
        element = $(element);
        return element.offsetTop;
    },
    getLeft: function(element) {
        element = $(element);
        return element.offsetLeft;
    },
    setWidth: function(element,w) {
        element = $(element);
        element.style.width = w +"px";
    },
    setHeight: function(element,h) {
        element = $(element);
        element.style.height = h +"px";
    },
    setTop: function(element,t) {
        element = $(element);
        element.style.top = t +"px";
    },
    setLeft: function(element,l) {
        element = $(element);
        element.style.left = l +"px";
    },
    setSrc: function(element,src) {
        element = $(element);
        element.src = src;
    },
    setHref: function(element,href) {
        element = $(element);
        element.href = href;
    },
    setInnerHTML: function(element,content) {
        element = $(element);
        element.innerHTML = content;
    }
});

