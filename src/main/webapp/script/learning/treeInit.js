var initDir = context+"/images/learning/";
var studyHistoryView = true;
var tempTitle="";
function dTreeInit(treeObj, classroomName, id, flag)
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
		stree.add(treeObj[0].count,treeObj[0].parentId,treeObj[0].title);
//		stree.add(treeObj[0].count,treeObj[0].parentId,treeObj[0].title,'javascript:doChoiceEvent(\'\');','','','','','');
		for (var i = 1 ; i < treeObj.length ; i++)
		{

			var _tree = treeObj[i];
			var visiable = _tree.visiable;
			var linkMethod = "_self";
			var updown ="";
			var link = 'NO_LINK';
			var complete = '';
			var currentS = '';
			var currentE = '';
			var studyRecord = false;
			
			if (_tree.select == true && _tree.hasChildNodeCount==0)
			{
				link = 'javascript:ContentExecutor.doChoiceEvent(\''+treeObj[i].scoId+'\');';
			}			
			if (studyHistoryView)
			{

				
				if(_tree.completionStatus == "completed")
				{
					complete = ' <FONT style="font-size:11px;" COLOR=#4baf4b>학습완료</FONT>';
				}else if(_tree.completionStatus == "incomplete"){
					complete = ' <FONT style="font-size:11px;" COLOR=#4baf4b>진행중</FONT>';	
				}else{
					complete = '';
				}
			}

			if (_tree.current == true)
			{

				complete = ' <FONT style="font-size:11px;" COLOR=#4baf4b>학습중</FONT>';

				var d = $('sco_title');
				if(d != undefined){
					d.innerHTML = treeObj[i].title;
					document.title = treeObj[i].title;
				}
			}			
			if (_tree.current == true)
			{
				currentS = '<b><FONT COLOR=#EBAA5F>';
				currentE = '</FONT></b>';
				
				tempTitle=treeObj[i].title+"/"+treeObj[i].moduleName;
				
				
			}
			//if (visiable) {
			var cnt = 11 - (treeObj[i].depth *2);
				var subtitle = treeObj[i].title.substring(0,cnt);
				stree.add(treeObj[i].count,
						  treeObj[i].parentId,
						  currentS + subtitle + currentE + complete,
						  link,
						  treeObj[i].title,
						  linkMethod,
						  '',
						  '',
						  updown);
				if (studyHistoryView)
				{
					if (_tree.hasNodeCondition != 1 && (_tree.leaf == true || _tree.hasChildNodeCount > 0))
					{
						if (_tree.attempt > 0)
						{
							  stree.add(i+"attempt",
							  treeObj[i].count,
							  "<FONT style='font-size:11px'> 학습횟수 : " +treeObj[i].attempt + "</FONT>" ,
							  "NO_LINK",
							  '',
							  '',
							  initDir+'tree/bu_aw.gif',
							  '');
							  studyRecord = true;
						}
						if (_tree.completionStatus != null)
						{

							
							    if(_tree.completionStatus == "incomplete" && _tree.current == false){
									  stree.add(i+"complete",
									  treeObj[i].count,
									  "<FONT style='font-size:11px'> 이수여부 : 진행중</FONT>",
									  "NO_LINK",
									  '',
									  '',
									  initDir+'tree/bu_aw.gif',
									  '');
									  studyRecord = true;
								}else if(_tree.current == true ){
									  stree.add(i+"complete",
									  treeObj[i].count,
									  "<FONT style='font-size:11px'> 이수여부 : 학습중</FONT>",
									  "NO_LINK",
									  '',
									  '',
									  initDir+'tree/bu_aw.gif',
									  '');
									  studyRecord = true;								
								}else if(_tree.completionStatus == "completed" && _tree.current == false){
									  stree.add(i+"complete",
									  treeObj[i].count,
									  "<FONT style='font-size:11px'> 이수여부 : 완료</FONT>",
									  "NO_LINK",
									  '',
									  '',
									  initDir+'tree/bu_aw.gif',
									  '');
									  studyRecord = true;
								}else{
									stree.add(i+"complete",
											treeObj[i].count,
											"<FONT style='font-size:11px'> 이수여부 : 학습대기</FONT>",
											"NO_LINK",
											'',
											'',
											initDir+'tree/bu_aw.gif',
									'');
									studyRecord = true;
								}
						}
						
						if (_tree.successStatus != "unknown" && _tree.successStatus != null && _tree.successStatus != "")
						{
							if (_tree.successStatus == "passed") {
							  stree.add(i+"successStatus",
							  treeObj[i].count,
							  "<FONT style='font-size:11px'> 수료여부 : 합격</FONT>",
							  "NO_LINK",
							  '',
							  '',
							  initDir+'tree/bu_aw.gif',
							  '');
							  studyRecord = true;
							}else {
							  stree.add(i+"successStatus",
							  treeObj[i].count,
							  "<FONT style='font-size:11px'> 수료여부 : 불합격</FONT>",
							  "NO_LINK",
							  '',
							  '',
							  initDir+'tree/bu_aw.gif',
							  '');
							  studyRecord = true;
							}
						}
						if (_tree.totalTime != null)
						{
							  stree.add(i+"studyTime",
							  treeObj[i].count,
							  "<FONT style='font-size:11px'> 학습시간 : "+ treeObj[i].totalTime + "</FONT>",
							  "NO_LINK",
							  '',
							  '',
							  initDir+'tree/bu_aw.gif',
							  '');
							  studyRecord = true;
						}
					}

				}
			}
		//}
	}
	dtreeF = document.getElementById(id);
	dtreeF.innerHTML = stree;
}
function getTree()
{
	return stree;
}


