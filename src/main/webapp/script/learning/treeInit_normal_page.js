var initDir = context+"/images/learning/";
var studyHistoryView = true;
var tempTitle="";
//function dTreeInit(treeObj, classroomName, id, flag, index, studyObj)
function dTreeInit(treeObj, classroomName, id, flag, index, studyObj, p_contentLessonAllView)
{
	//alert(classroomName+"/" + id+"/" + flag+"/" + index+"/" + studyObj);
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
		
	    stree.add(treeObj[0].module ,-1, "[" + treeObj[0].module + "] " + treeObj[0].moduleName);

		for (var i = 0 ; i < treeObj.length ; i++)
		{
			var _tree = treeObj[i];
			//var visiable = _tree.visiable;
			var linkMethod = "_self";
			var updown ='';
			var link = 'NO_LINK';
			var complete = '';	//'<FONT style="font-size:11px;" COLOR=#4baf4b><span id="'+treeObj[i].lesson+'"></span></FONT>';
			var currentS = '';
			var currentE = '';  
			var studyRecord = false;
			
			var cnt = 11 - (treeObj[i].depth *2);
			var subtitle = treeObj[i].lessonName.substring(0,cnt);
			var title = treeObj[i].lesson+'. '+treeObj[i].lessonName;
			
			var  lesson = (treeObj[i].lesson );   
			var  parentId = (treeObj[i].module );
			
			var  orderNum = treeObj[i].ordering;
			
			if (treeObj[i].progressYn == 'Y')
			{
				//link = 'javascript:ContentExecutor.changeContent2(\''+treeObj[i].orderingpage+'\',\'1\', \''+treeObj[i].lesson+'\');';
				link = 'javascript:ContentExecutor.changeContent3(\''+treeObj[0].module+'\',\''+treeObj[i].orderingpage+'\',\'1\', \''+treeObj[i].lesson+'\');';
			}else{
				link = '#';
			}

			if (index == orderNum)
			{
				//alert(index + "/" + orderNum + "/" + parentId + "/" + lesson);
				currentS = '<b><FONT COLOR=#EBAA5F>';
				currentE = '</FONT></b>';
				tempTitle=title+"/"+treeObj[i].moduleName; 
			
					
				
			}
			

			for (var j = 0 ; j < studyObj.length ; j++){
				 if(treeObj[i].lesson== studyObj[j].lesson){
					    if(studyObj[j].lessonstatus == "Y"){
							complete = ' <FONT style="font-size:11px;" COLOR="#00959c">[학습완료]</FONT>';
						}else{ 
							complete = ' <FONT style="font-size:11px;" COLOR="#a5ce18">[학습중]</FONT>';	
						}
					 break;
				 }
			}
			
			if(complete == "")
			{
				complete = ' <FONT style="font-size:11px;" COLOR="#e6522b">[학습전]</FONT>';
			}
			
			
			//학습완료, 학습중, 학습전
			if(jp_subj != "PAR150003" && jp_subj != "PRF150011" && jp_subj != "PAR150001" && jp_subj != "PAR150002"){					
				complete ="";
			}			
			
			if(p_contentLessonAllView == 'Y'){
				
				
				if (index == orderNum )			{
					stree.add(lesson+i,
						  parentId,
						  currentS + title + currentE + complete,
						  link,
						  title,
						  '',
						  '',
						  '',
						  updown);
				}
			}else{
				stree.add(lesson+i,
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
	}
	dtreeF = document.getElementById(id);
	dtreeF.innerHTML = stree;
	
}
function getTree()
{
	return stree;
}


