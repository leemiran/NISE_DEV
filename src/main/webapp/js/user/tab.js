// tab 
jQuery(function($){
	var tab = $('.mstab');
	tab.removeClass('jsOff');
	tab.css('height', tab.find('>ul>li>div:visible').height()+30);
	function onSelectTab(){
		var t = $(this);
		var myClass = t.parent('li').attr('class');
		t.parents('.mstab:first').attr('class', 'mstab '+myClass);
		return false;
	}
	tab.find('>ul>li>a').click(onSelectTab).focus(onSelectTab);
	});
