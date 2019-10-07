(function($) {
	
	$.doitBox = {version: '1.0.0'};
	
	$.fn.doitBox = function(settings) {
		settings = jQuery.extend( {
			overlayBgColor : '#000', 
			overlayOpacity : 0.6,   
			imageLoading 	: '/images/box/lightbox-ico-loading.gif',  
			imageBtnClose 	: '/images/box/lightbox-btn-close.gif',
			imageBlank 		: '/images/box/lightbox-blank.gif',
			containerBorderSize : 10, 
			containerResizeSpeed : 400,
			options : null
		}, settings);
		
		//public method
		$.doitBox.start = function(options) {
			settings.options = options;
			_start();
		}
		
		$.doitBox.finish = function(settings) {
			_finish();
		}
		
		$.doitBox.resize = function(settings) {
			_resize();
		}
		
		function _start() {
			$('embed, object, select').css( {
				'visibility' : 'hidden'
			});
			_set_interface();
			_set_Button();
			_set_image_to_view();
		}
		
		function _set_interface() {
			$('body').append(
						'<div id="jquery-overlay"></div>' +
						'<div id="jquery-lightbox">' +
							'<div id="lightbox-container-image-box">' +
							'<div id="lightbox-title"></div>' +
								'<div id="lightbox-container-image">' +
								'<div id="lightbox-image"></div>' +
									'<div id="lightbox-loading">' +
										'<img id="lightbox-loading-link" src="/images/box/lightbox-ico-loading.gif">' +
									'</div>' +
								'</div>' +
							'</div>' +
							'<div id="lightbox-container-image-data-box">' +
								'<div id="lightbox-container-image-data">' +
									'<div id="lightbox-secNav">' +
										'<img id="lightbox-secNav-btnClose" src="/images/box/lightbox-btn-close.gif" style="cursor: pointer;">' +
										'<img class="lightbox_btn" id="lightbox-secNav-btnList"    src="/images/box/btn_list.gif" style="cursor: pointer;">' +
										'<img class="lightbox_btn" id="lightbox-secNav-btnInsert"  src="/images/box/btn_insert.gif" style="cursor: pointer;">' +
										'<img class="lightbox_btn" id="lightbox-secNav-btnUpdate"  src="/images/box/btn_update.gif" style="cursor: pointer;">' +
										'<img class="lightbox_btn" id="lightbox-secNav-btnDelete"  src="/images/box/btn_del.gif" style="cursor: pointer;">' +
									'</div>' +
								'</div>' +
							'</div>' +
						'</div>' + 
						'<form id="box_form" name="box_form" method="post" target="ibox_img"></form>'
					);
			var arrPageSizes = ___getPageSize();
			$('#jquery-overlay').css( {
				backgroundColor : settings.overlayBgColor,
				opacity : settings.overlayOpacity,
				width : arrPageSizes[0],
				height : arrPageSizes[1]
			}).fadeIn();
			var arrPageScroll = ___getPageScroll();
			$('#jquery-lightbox').css( {
				top : arrPageScroll[1] + (arrPageSizes[3] / 10),
				left : arrPageScroll[0]
			}).show();
			
			$('#lightbox-secNav-btnClose').unbind().click( function() {
				if (settings.options.cancle)
					settings.options.cancle();
				_finish();
				return false;
			});
			
			$(window).unbind("resize").resize(function() {
				var arrPageSizes = ___getPageSize();
				$('#jquery-overlay').css( {
					width : arrPageSizes[0],
					height : arrPageSizes[1]
				});
				var arrPageScroll = ___getPageScroll();
				$('#jquery-lightbox').css( {
					top : arrPageScroll[1] + (arrPageSizes[3] / 10),
					left : arrPageScroll[0]
				});
			});
			
			$(window).unbind("scroll").scroll(function() {
				var arrPageSizes = ___getPageSize();
				var arrPageScroll = ___getPageScroll();
				$('#jquery-lightbox').css( {
					top : arrPageScroll[1] + (arrPageSizes[3] / 10),
					left : arrPageScroll[0]
				});
				if (scrollFunction)
					scrollFunction();
			});
			
		}
		
		function _set_Button() {
			if (settings.options.insert) {
				$('#lightbox-secNav-btnInsert').show().unbind().click( function() {
					settings.options.insert();
				})
			}
			if (settings.options.update) {
				$('#lightbox-secNav-btnUpdate').show().unbind().click( function() {
					settings.options.update();
				})
			}
			if (settings.options.remove) {
				$('#lightbox-secNav-btnDelete').show().unbind().click( function() {
					settings.options.remove();
				})
			}
		}
		
		function _set_image_to_view() { // show the loading
			$('#lightbox-loading').show();
			$('#lightbox-image,#lightbox-container-image-data-box').hide();
			var boxWidth = settings.options.width == null ? 100 : settings.options.width;
			var boxHeight = settings.options.height == null ? 100 : settings.options.height;
			_resize_container_image_box(boxWidth, boxHeight);
		}
		function _resize_container_image_box(boxWidth, boxHeight) {
			
			var intCurrentWidth = $('#lightbox-container-image-box').width();
			var intCurrentHeight = $('#lightbox-container-image-box').height();
			var intWidth = (boxWidth + (settings.containerBorderSize * 2));
			var intHeight = (boxHeight + (settings.containerBorderSize * 2));
			
			var intDiffW = intCurrentWidth - intWidth;
			var intDiffH = intCurrentHeight - intHeight;
			$('#lightbox-container-image-box').animate( {
				width : intWidth,
				height : intHeight
			}, settings.containerResizeSpeed, function() {
				if ($("#ibox_iframe").length == 0) {
					_show_image(boxHeight);
					if (settings.options.title)
						$("#lightbox-title").append("<em>" + settings.options.title + "</em>");
				} else {
					$("#ibox_iframe").height(boxHeight);
				}
			});
			if ((intDiffW == 0) && (intDiffH == 0)) {
				if ($.browser.msie) {
					___pause(250);
				} else {
					___pause(100);
				}
			}
			$('#lightbox-container-image-data-box').css( {
				width : boxWidth
			});
		}
		function _show_image(boxHeight) {
			$('#lightbox-loading').hide();
			$('#lightbox-image').fadeIn(function() {
				$('#lightbox-image').append('<iframe id="ibox_iframe" name="ibox_iframe" width="100%" height="' + boxHeight + 'px" scrolling="auto" frameborder="0"/>');
				
				for (var i = 0 ; i < settings.options.params.length ; i++) {
					var paramData = settings.options.params[i].split("=");
					var inputHTML  =  '<input type="hidden"';
                    	inputHTML +=    'id="'+paramData[0]+'" ';
                    	inputHTML +=    'name="'+paramData[0]+'" ';
                    	inputHTML +=    'value="'+paramData[1]+'" />';
                    $("#box_form").append(inputHTML);
				}
				
				if (settings.options.method) {
					var inputHTML  =  '<input type="hidden"';
                	inputHTML +=    'id="method" ';
                	inputHTML +=    'name="method" ';
                	inputHTML +=    'value="'+settings.options.method+'" />';
                	$("#box_form").append(inputHTML);
				}
				
				$("#box_form").attr({
										action : settings.options.url, 
										target : "ibox_iframe"
									}).submit();
				
				$("#box_form").remove();
				
				if (settings.options.autoresize) {
					$('#ibox_iframe').unbind().load( function() {
						_resize();
					});
				}
				
				_show_image_data();
			});
		};
		
		function _resize() {
			var $element = $get('ibox_iframe');
			$("#ibox_iframe").height($element.contentWindow.document.body.offsetHeight);
			var boxWidth = settings.options.width == null ? 100 : $element.contentWindow.document.body.offsetWidth;
			_resize_container_image_box(boxWidth, $element.contentWindow.document.body.offsetHeight > 470 ? 470 : $element.contentWindow.document.body.offsetHeight);
		}
		
		function _show_image_data() {
			$('#lightbox-container-image-data-box').slideDown("slow");
		}
		function _finish() {
			$('#jquery-lightbox').remove();
			$('#jquery-overlay').fadeOut(function() {
				$('#jquery-overlay').remove();
			});
			$('embed, object, select').css( {
				'visibility' : 'visible'
			});
			$(window).unbind("scroll").scroll(function() {
				if (scrollFunction)
					scrollFunction();
			});
			
			
		}
		function ___getPageSize() {
			var xScroll, yScroll;
			if (window.innerHeight && window.scrollMaxY) {
				xScroll = window.innerWidth + window.scrollMaxX;
				yScroll = window.innerHeight + window.scrollMaxY;
			} else if (document.body.scrollHeight > document.body.offsetHeight) { 
				xScroll = document.body.scrollWidth;
				yScroll = document.body.scrollHeight;
			} else { 
				xScroll = document.body.offsetWidth;
				yScroll = document.body.offsetHeight;
			}
			var windowWidth, windowHeight;
			if (self.innerHeight) {
				if (document.documentElement.clientWidth) {
					windowWidth = document.documentElement.clientWidth;
				} else {
					windowWidth = self.innerWidth;
				}
				windowHeight = self.innerHeight;
			} else if (document.documentElement
					&& document.documentElement.clientHeight) { 
				windowWidth = document.documentElement.clientWidth;
				windowHeight = document.documentElement.clientHeight;
			} else if (document.body) { // other Explorers
				windowWidth = document.body.clientWidth;
				windowHeight = document.body.clientHeight;
			}
			if (yScroll < windowHeight) {
				pageHeight = windowHeight;
			} else {
				pageHeight = yScroll;
			}
			if (xScroll < windowWidth) {
				pageWidth = xScroll;
			} else {
				pageWidth = windowWidth;
			}
			arrayPageSize = new Array(pageWidth, pageHeight, windowWidth, windowHeight);
			return arrayPageSize;
		}
		function ___getPageScroll() {
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
		}
		function ___pause(ms) {
			var date = new Date();
			curDate = null;
			do {
				var curDate = new Date();
			} while (curDate - date < ms);
		}
		;
	};
})(jQuery); 

$().doitBox();