(function($) {
	
	$.doitBox = function(data, klass) {
		$.doitBox.loading(data);
	}
	
	$.extend($.doitBox, {
		settings : {
			fileLoadingImage : "/images/adm/common/loading.gif",
			fileBottomNavCloseImage : "/images/admin/closelabel.gif",
			overlayOpacity : 0.5,
			containerResizeSpeed : 400,
			borderSize : 10,
			markup: '<div id="ibox_w" style="display: none; position: absolute; top: 0; left: 0; z-index: 90; width: 100%; height: 100%; background-color: #000;"></div> \
						<div id="ibox_wrapper" class="dvpop" style="display:none; position:absolute; z-index:100; background-color: #fff;"> \
							<div id="ibox_title" class="poptit"></div> \
							<div id="ibox_content" class="poptxt3"></div> \
							<div id="ibox_btn" class="popbtn"></div> \
						</div> \
						<form id="box_form" name="box_form" method="post" target="ibox_img"></form>'
		},
		
		_initialize : function() {
			$('body').append(this.settings.markup);
			this._start();
		},
		
		_start : function() {
			$('embed, object, select').css( {'visibility' : 'hidden'});
			
			var arrPageSizes = this._getPageSize();
			// Style overlay and show it
			$('#ibox_w').css( {
				backgroundColor : this.settings.overlayBgColor,
				opacity : this.settings.overlayOpacity,
				width : arrPageSizes[0],
				height : arrPageSizes[1]
			}).fadeIn();
			
			var arrPageScroll = this._getPageScroll();
			
			var divWidth = "800";
			var divHeight = "400";
			
			$('#ibox_wrapper').css( { 
				width: 45,
				height: 45 
			});
	        
			var widthNew = parseInt(divWidth);
			var heightNew = parseInt(divHeight) - 65;

			var lightboxLeft = Math.floor((arrPageSizes[0] - widthNew)/2);
			
//		    $('#ibox_title').html("<span> 코드등록 </span>");
//			var strHTML = '<iframe id="ibox_iframe" name="ibox_iframe" src="http://snui.snu.ac.kr/adm/codegubun.do?method=insertForm&pop=true" scrolling="auto" frameborder="0" style="width: 100%; height:300; border:0; margin:0; padding:0;"/>';
//            $('#ibox_content').html(strHTML);  
			
			$('#ibox_wrapper').css( {
				top : arrPageScroll[1] + (arrPageSizes[3] / 10),
				left : lightboxLeft
			}).fadeIn();
			
			$('#libox_wrapper').animate( {
				width : divWidth,
				height : divHeight
			}, $.doitBox.settings.containerResizeSpeed, function() {
			});
			
			
			$(window).resize(function() {
				var arrPageSizes = $.doitBox._getPageSize();
				$('#ibox_w').css( {
					width : arrPageSizes[0],
					height : arrPageSizes[1]
				});
				var arrPageScroll = $.doitBox._getPageScroll();
				
				var lightboxLeft = Math.floor((arrPageSizes[0] - widthNew)/2);
				$('#ibox_wrapper').css( {
					top : arrPageScroll[1] + (arrPageSizes[3] / 10),
					left : lightboxLeft
				});
			});
			
			this._showIbox();
			
			$(window).scroll(function() {
				var arrPageSizes = $.doitBox._getPageSize();
				$('#ibox_w').css( {
					width : arrPageSizes[0],
					height : arrPageSizes[1]
				});
				var arrPageScroll = $.doitBox._getPageScroll();
				$('#ibox_wrapper').css( {
					top : arrPageScroll[1] + (arrPageSizes[3] / 10),
					left : arrPageScroll[0]
				});
			});
			
		},
		
		_getPageSize : function() {
			var xScroll, yScroll;
			if (window.innerHeight && window.scrollMaxY) {
				xScroll = window.innerWidth + window.scrollMaxX;
				yScroll = window.innerHeight + window.scrollMaxY;
			} else if (document.body.scrollHeight > document.body.offsetHeight) { 
				xScroll = document.body.scrollWidth;
				yScroll = document.body.scrollHeight;
			} else { // Explorer Mac...would also work in Explorer 6 Strict,
						// Mozilla and Safari
				xScroll = document.body.offsetWidth;
				yScroll = document.body.offsetHeight;
			}
			var windowWidth, windowHeight;
			if (self.innerHeight) { // all except Explorer
				if (document.documentElement.clientWidth) {
					windowWidth = document.documentElement.clientWidth;
				} else {
					windowWidth = self.innerWidth;
				}
				windowHeight = self.innerHeight;
			} else if (document.documentElement
					&& document.documentElement.clientHeight) { // Explorer 6
																// Strict Mode
				windowWidth = document.documentElement.clientWidth;
				windowHeight = document.documentElement.clientHeight;
			} else if (document.body) { // other Explorers
				windowWidth = document.body.clientWidth;
				windowHeight = document.body.clientHeight;
			}
			// for small pages with total height less then height of the
			// viewport
			if (yScroll < windowHeight) {
				pageHeight = windowHeight;
			} else {
				pageHeight = yScroll;
			}
			// for small pages with total width less then width of the viewport
			if (xScroll < windowWidth) {
				pageWidth = xScroll;
			} else {
				pageWidth = windowWidth;
			}
			arrayPageSize = new Array(pageWidth, pageHeight, windowWidth, windowHeight);
			return arrayPageSize;
		},
		
		_getPageScroll : function() {
			var xScroll, yScroll;
			if (self.pageYOffset) {
				yScroll = self.pageYOffset;
				xScroll = self.pageXOffset;
			} else if (document.documentElement
					&& document.documentElement.scrollTop) { // Explorer 6
																// Strict
				yScroll = document.documentElement.scrollTop;
				xScroll = document.documentElement.scrollLeft;
			} else if (document.body) {// all other Explorers
				yScroll = document.body.scrollTop;
				xScroll = document.body.scrollLeft;
			}
			arrayPageScroll = new Array(xScroll, yScroll);
			return arrayPageScroll;
		},
		
		_showIbox : function() {
			
			var widthNew = 800;
			var heightNew = 400 - 65;
			
			$('#ibox_content').css( {
				width : widthNew,
				height : heightNew
			}).show()
	    	
//	        var showFunction = function() {
//	            if (firstAction){
//	                if (typeFunction != undefined) {
//	                    typeFunction();
//	                }
//	            }
//	        }
//
//	        $('ibox_content').setStyle({ width: widthNew + 'px', height: heightNew + 'px' });
//			new Effect.Grow( $('ibox_wrapper'), { direction: 'top-left', duration: overlayDuration, afterFinish : showFunction});
	    }
		
	})
	
	$.doitBox.initialize = function() {
		$.doitBox._initialize();
	}
	
})(jQuery); 


alert();