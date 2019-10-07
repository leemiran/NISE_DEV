var initDir = context+"/images/learning/";
var studyHistoryView = true;
var tempTitle="";
function dTreeInit(treeObj, classroomName, id, flag, index,studyObj)
{
	stree = new dTree("stree", initDir );  
	
	if ( treeObj == null )
	{
		if (flag == 0)
		{
			stree.add('0','-1',classroomName+"<br>(학습트리자동숨김)",'javascript:doChoiceEvent(\'\');','','','','','');
		}
		else
		{
			stree.add('0','-1',classroomName+"<br>(학습트리자동숨김)",'NO_LINK','','','','','');
		}
	} else {
		
	    stree.add(treeObj[0].module ,-1,treeObj[0].moduleName);

		for (var i = 0 ; i < treeObj.length ; i++)
		{
			var _tree = treeObj[i];
			//var visiable = _tree.visiable;
			var linkMethod = "_self";
			var updown ='';
			var link = 'NO_LINK';
			var complete = '';
			var currentS = '';
			var currentE = '';  
			var studyRecord = false;
			
			var cnt = 11 - (treeObj[i].depth *2);
			var subtitle = treeObj[i].lessonName.substring(0,cnt);
			var title = treeObj[i].lessonName;
			
			var  lessonCd = (treeObj[i].lessonCd );   
			var  parentId = (treeObj[i].owner );
			
			var  orderNum = treeObj[i].ordering;
			
			if (treeObj[i].progressYn == 'Y')
			{
				link = 'javascript:ContentExecutor.changeContent(\''+treeObj[i].ordering+'\', \'1\',\''+treeObj[i].lesson+'\');';
			}else{
				var idx=i;
				while(true){
					if( treeObj[++idx] && treeObj[idx].progressYn == "Y" ){
						link = 'javascript:ContentExecutor.changeContent(\''+treeObj[idx].ordering+'\', \'1\',\''+treeObj[idx].lesson+'\');';
						break;
					}
				}
			}

			if (index == orderNum)
			{
				currentS = '<b><FONT COLOR=#EBAA5F>';
				currentE = '</FONT></b>';
				
				tempTitle=title+"/"+treeObj[i].moduleName;
				
			}
			
			for (var j = 0 ; j < studyObj.length ; j++){
			 if(treeObj[i].module == studyObj[j].module && treeObj[i].lesson== studyObj[j].lesson){
				 
				 if(studyObj[j].lessonstatus == "Y"){
						complete = ' <FONT style="font-size:11px;" COLOR=#4baf4b id=studyStatus'+studyObj[j].lesson+'>학습완료</FONT>';
					}else if(studyObj[j].lessonstatus == "N"){
						complete = ' <FONT style="font-size:11px;" COLOR=#4baf4b id=studyStatus'+studyObj[j].lesson+'>학습중</FONT>';	
					}else{
						complete = '';
					}

				 break;
			 }
			}
			stree.add(lessonCd,
					  parentId,
					  currentS + title + currentE + complete,
					  link,
					  title,
					  '',
					  '',
					  '',
					  updown);


		}
	}
	dtreeF = document.getElementById(id);
	dtreeF.innerHTML = stree;
}
function getTree()
{
	return stree;
}


