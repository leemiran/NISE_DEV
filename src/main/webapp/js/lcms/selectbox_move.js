/**************************************************
/* 두개의 SelectBox에서 메뉴(Option) 이동 관련 JavaScript
/*
 * @version		1.0, 2008/11/03
 * @author		Chung Jin-pil
/**************************************************/

// 메뉴 이동 (넣기)
function addMenu( srcMenuID, destMenuID, isMove)
{
	var srcMenuList = document.getElementById(srcMenuID);
	var destMenuList = document.getElementById(destMenuID);
	
	var items = srcMenuList.options;
	var position = 0;
	
	for(var i=0; i<items.length; i++)
	{
		if( items[i].selected==true )
		{
			if(contains( destMenuID, items[i].value) && items[i].selected==true )
			{
				alert(items[i].innerText+"은(는) 이미 존재합니다.");
				continue;
			}
			
			var opt = document.createElement("OPTION");
			destMenuList.insertBefore(opt, destMenuList.options[position]);
			opt.value = items[i].value;
			opt.innerHTML = items[i].innerHTML;
			
			if ( isMove == true )
			{
				srcMenuList.remove(i);
				i--;
			}

			position++;
		}
	}  
}

// 메뉴 이동 (빼기)
function removeMenu(srcMenuID, destMenuID, isMove)
{
	var srcMenuList = document.getElementById(srcMenuID);
	var destMenuList = document.getElementById(destMenuID);
	
	var items = srcMenuList.options;
	for(var i=0; i<items.length; i++)
	{
		if( items[i].selected==true )
		{
			if ( destMenuID == null || isMove == true ) {
				var opt = document.createElement("OPTION");
				destMenuList.insertBefore(opt, destMenuList.options[destMenuList.options.length]);
				opt.value = items[i].value;
				opt.innerHTML = items[i].innerHTML;
			}
			
			srcMenuList.remove(i);
			i--;
		}
	}
}

// 위로 이동
function moveUp(menuID)
{  
	var menuList = document.getElementById(menuID);
	
	var items = menuList.options;
	try {
		for(var i=0; i<items.length; i++)
		{
			if(items[i].selected==true)
			{
				if(i==0 || items[i-1].selected==true)
					continue;
				if( i > 0 )
				{
					var newOpt = document.createElement("OPTION");
					menuList.insertBefore(newOpt, items[i-1]);
					newOpt.value = items[i+1].value;
					newOpt.innerHTML = items[i+1].innerHTML;
					newOpt.selected=true;
					try {
						items[i+1].selected=false;
					} catch(e) {}
					menuList.removeChild(items[i+1]);   
				}
			}
		}
	} catch(e){}
}

// 맨위로 이동
function moveFirstUp(menuID)
{
	var menuList = document.getElementById(menuID);

	var items = menuList.options;
	var position = 0;
	try {
		for(var i=0; i<items.length; i++)
		{
			if(items[i].selected)
			{
				var opt = document.createElement("OPTION");
				menuList.insertBefore(opt, menuList.options[position]);
				opt.value = items[i+1].value;
				opt.innerHTML = items[i+1].innerHTML;
				opt.selected=true;
				menuList.removeChild(items[i+1]);
				position++;
			}
		}
	} catch(e){}
}

// 아래로 이동
function moveDown(menuID)
{
	var menuList = document.getElementById(menuID);
	
	var items = menuList.options;
	try {
		for(var i=items.length-1; i>=0; i--)
		{
			if(items[i].selected==true)
			{
				if(i==items.length-1 || items[i+1].selected==true)
					continue;
				if( i >= 0 )
				{				  
					var newOpt = document.createElement("OPTION");
					menuList.insertBefore(newOpt, items[i]);
					newOpt.value = items[i+2].value;
					newOpt.innerHTML = items[i+2].innerHTML;
					menuList.removeChild(items[i+2]);
				}
			}
		}
	} catch(e){}
}

// 맨아래로 이동
function moveLastDown(menuID)
{
	var menuList = document.getElementById(menuID);

	var items = menuList.options;
	var sels = new Array();
	try
	{
		for(var i=0; i<items.length; i++)
		{
			if(items[i].selected)
			{
				var opt = document.createElement("OPTION");
				menuList.appendChild(opt);
				opt.value = items[i].value;
				opt.innerHTML = items[i].innerHTML;
				sels[sels.length] = opt;
				menuList.removeChild(items[i]);
				i--;
			}
		}
		for(var i=0; i<sels.length; i++)
		{
			sels[i].selected=true;
		}
	} catch(e){}
}

// 모든 항목 삭제
function removeAllItems(menuID)
{
	var menuList = document.getElementById(menuID);

	var cnt = menuList.options.length;
	for(var i=0; i<cnt; i++)
	{
		menuList.remove(0);
	}
}

// 모든 항목 선택
function selectAll(menuID, isSelect)
{
	var menuList = document.getElementById(menuID);
	
	var items = menuList.options;
	for(var i=0; i<items.length; i++)
	{
		if ( isSelect == true ) {
			items[i].selected=true;
		} else {
			items[i].selected=false;
		}
	}
}

// 해당 값을 가진 option이 있는지 여부
function contains(menuID, value)
{
	var menuList = document.getElementById(menuID);

	var items = menuList.options;
	for(var i=0; i<items.length; i++)
	{
		if(items[i].value==value) {
			return true;
		}
	}
	return false;
}

// 해당 값을 가진 option을 선택
function selectMenuByValue( menuID, value ) {
	var menuList = document.getElementById(menuID);
	
	var items = menuList.options;
	for(var i=0; i<items.length; i++)
	{
		if(items[i].value==value) {
			items[i].selected=true;
		} else {
			items[i].selected=false;			
		}
	}

	return true;
}
