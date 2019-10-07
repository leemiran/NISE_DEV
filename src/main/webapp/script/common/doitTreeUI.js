(function($) {
	$.treeUI = function(data, check, klass) {
		$.treeUI.loading(data, check);
	}

	$.extend($.treeUI, {
		settings : {
			imagePath 	: null,
			check 		: false,
			events    	: null,
			levels    	: null,
			buffer    	: new Array(),
			mapData   	: new JMap(),
			isDrag    	: false,
			dragSeq   	: null
		},

		loading : function(imagePath, check) {
			this.settings.imagePath = imagePath;
			this.settings.check = check;
			this.settings.actionEvents = {
				"insert" : {
					useYn : 'N',
					image : null,
					event : null,
					title : "등록",
					limit : -1
				},
				"update" : {
					useYn : 'N',
					image : null,
					event : null,
					title : "수정",
					limit : -1
				},
				"del" : {
					useYn : 'N',
					image : null,
					event : null,
					title : "삭제",
					limit : -1
				},
				"move" : {
					useYn : 'N',
					image : null,
					event : null,
					title : "순서변경",
					limit : -1
				},
				"etc" : []
			}
		},
		
		insert : function (seq, title, parentSeq, level, order, extend) {
			this.settings.mapData.put(seq, this.createEntity( seq, title, parentSeq, level, order, extend));
		},
		
		createEntity : function(seq, title, parentSeq, level, order, extend) {
			var temp = {
				seq : seq,
				title : title,
				parentSeq : parentSeq,
				level : level,
				order : order,
				extend : extend || {},
				child : new JMap()
			}
			
			return temp;		
		},

		makeTree : function() {
		//	var mapData = this.settings.mapData;
			
			if (this.settings.mapData.size() == 0 )
				return;
			
			var mapKeys = this.settings.mapData.keys();
			var mapLength = mapKeys.length;
			
			for(var i = 0 ; i < mapLength ; i++) {
				
				if (this.settings.mapData.get(mapKeys[i]).seq != '0') {
					var temp = this.settings.mapData.get(mapKeys[i]);
					this.settings.mapData.get(temp.parentSeq).child.put(temp.seq, temp.order);
				} 
			}
			this.initializeTree();
		},
		
		initializeTree : function() {
			var mapKeys = this.settings.mapData.keys();
			var parentSeq = '0';
			if (mapKeys.length > 0)
				parentSeq = this.settings.mapData.get(mapKeys[0]).seq;
			if (this.settings.actionEvents != null)
				this.makeGuide();
			if (this.makeParent(parentSeq))
				this.makeChildTree(parentSeq, this.settings.mapData.get(parentSeq).child);
			else 
				return;
			
			
	//		alert(this.settings.buffer.join(""));
	//		$('#treeArea').append(this.settings.buffer.join(""));
			
			$('#treeArea').html(this.settings.buffer.join(""));
			
			this.settings.buffer = new Array();
			this.settingEvent();
		},
		
		settingEvent : function() {
			$('.insert').each(function() {
				$(this).unbind('click').click(function() {
					
					if ($.treeUI.settings.isDrag) {
						alert(message.get('message_367'));
						return;
					}
					
					var element = $(this).attr("element");
					
					if (element != 0) {
						$('#div' + element).css('background', '#FFD5B1');
					} else {
						$('#table' + element).css('background', '#FFD5B1');
					}
					var result = $.treeUI.settings.actionEvents.insert.event( $.treeUI.settings.mapData.get( element));
					
				})
			})
			
			$('.update').each(function() {
				$(this).unbind('click').click(function() {
					
					if ($.treeUI.settings.isDrag) {
						alert(message.get('message_367'));
						return;
					}
					
					var element = $(this).attr("element"); 
					
					if (element != 0) {
						$('#div' + element).css('background') != "#ffd5b1" ? $('#div' + element).css('background', '#FFD5B1') : $('#div' + element).css('background', '#ECECEC');
					} else {
						$('#table' + element).css('background') != "#ffd5b1" ? $('#table' + element).css('background', '#FFD5B1') : $('#table' + element).css('background', '#B3D6F3');
					}
					$.treeUI.settings.actionEvents.update.event( $.treeUI.settings.mapData.get( $(this).attr("element")));
				})
			})
			
			$('.delete').each(function() {
				$(this).unbind('click').click(function() {
					
					if ($.treeUI.settings.isDrag) {
						alert(message.get('message_367'));
						return;
					}
					
					var element = $(this).attr("element");
					
					if (element != 0) {
						$('#div' + element).css('background') != "#ffd5b1" ? $('#div' + element).css('background', '#FFD5B1') : $('#div' + element).css('background', '#ECECEC');
					} else {
						$('#table' + element).css('background') != "#ffd5b1" ? $('#table' + element).css('background', '#FFD5B1') : $('#table' + element).css('background', '#B3D6F3');
					}
					
					if ($("#ul" + element + " li:last-child").length > 0) {
						alert(message.get('LE_message_023'));
						$('#div' + element).css('background', '#ECECEC');
						return;
					}
					
					if (!confirm(message.get('message_009'))) {
						$('#div' + element).css('background', '#ECECEC');
						return;
					}
					
					var result = $.treeUI.settings.actionEvents.del.event( $.treeUI.settings.mapData.get( element));
					
					if (result == 0 || result == undefined) {
						$('#div' + element).css('background', '#ECECEC');
						return; 
					} else {
						
						var parentSeq = $.treeUI.settings.mapData.get(element).parentSeq;
						
						$('#li' + element).hide("drop", {}, 700, function() {
							$('#li' + element).remove();
							$("#ul" + parentSeq + " li").removeClass("end");
							$("#ul" + parentSeq + " li:last-child").addClass("end");
							if($("#ul" + parentSeq + " li:last-child").attr("seq") == undefined)
								$('#ul' + parentSeq).remove();
							
							$.treeUI.settings.mapData.get( parentSeq).child.remove(element);
							$.treeUI.settings.mapData.remove(element);
						});
					}
				})
			})
			
			$('.move').each(function() {
				$(this).unbind('click').click(function() {
					
					var element = $(this).attr("element");
					
					if ($.treeUI.settings.isDrag && $.treeUI.settings.dragSeq != element) {
						alert(message.get('message_367'));
						return;
					}
					
					if (element != 0) {
						$('#div' + element).css('background') != "#ffd5b1" ? $('#div' + element).css('background', '#FFD5B1') : $('#div' + element).css('background', '#ECECEC');
					} else {
						$('#table' + element).css('background') != "#ffd5b1" ? $('#table' + element).css('background', '#FFD5B1') : $('#table' + element).css('background', '#B3D6F3');
					}
					
					if ($(".li" + element).length < 2) {
						alert(message.get('message_281'));
						
						if (element != 0) {
							$('#div' + element).css('background', '#ECECEC');
						} else {
							$('#table' + element).css('background', '#B3D6F3');
						}
						return;
					}
					
					if ($.treeUI.settings.isDrag) {
						$.treeUI.settings.isDrag = false;
						$.treeUI.settings.dragSeq = null;
						
						$("div[parentSeq="+ element +"]").css('background', '#ECECEC');
						
						$("#ul" + element).unbind("mousedown").find("li > div").css("cursor", "auto");
						
						var entitys = new Array();
						$(".li" + element).each(function() {
							entitys.push($.treeUI.settings.mapData.get($(this).attr("seq")));
						})
						
						$.treeUI.settings.actionEvents.move.event(entitys);
						
					} else {
						$.treeUI.settings.isDrag = true;
						$.treeUI.settings.dragSeq = element;
						
						$("div[parentSeq="+ element +"]").css('background', '#FFD5B1');
						
						$("#ul" + element).dragsort({ dragSelector: ".li" + element + " > div", dragEnd: function() { 
							$("#ul" + element + " li").removeClass("end");
							$("#ul" + element + " li:last-child").addClass("end");
						}, dragBetween: true });
					}
				})
			})
			
			for (var i = 0 ; i < this.settings.actionEvents.etc.length ; i++) {
				$('.etc'+i).each(function() {
					var event = $.treeUI.settings.actionEvents.etc[i].event;
					$(this).unbind('click').click(function() {
						if ($.treeUI.settings.isDrag) {
							alert(message.get('message_367'));
							return;
						}
						
						var element = $(this).attr("element");
						
						if (element != 0) {
							$('#div' + element).css('background', '#FFD5B1');
						} else {
							$('#table' + element).css('background', '#FFD5B1');
						}
						var result = event( $.treeUI.settings.mapData.get( element));
					})
				})
			}
		},
		
		insertComplete : function(entity) {
			var element = entity.parentSeq;
			$.treeUI.settings.buffer[$.treeUI.settings.buffer.length] = "	<li class='end li" + entity.parentSeq + "' id='li" + entity.seq + "' seq='" + entity.seq + "'>";
			$.treeUI.makeDivTree(entity.seq, entity.title, entity.level, element);
			$.treeUI.settings.buffer[$.treeUI.settings.buffer.length] = "	</li>";
			
			if($("#ul" + element).length == 0) {
				entity.child = new JMap();
				
				if (element == '0') {
					$($.treeUI.settings.buffer.join("")).hide().insertAfter("#table" + element).wrap("<ul id='ul" + element +  "'></ul>").show("drop" ,{}, 500, $.treeUI.makeHighlight("#div" + entity.seq) );
				} else {
					$($.treeUI.settings.buffer.join("")).hide().insertAfter("#div" + element).wrap("<ul id='ul" + element +  "'></ul>").show("drop" ,{}, 500, $.treeUI.makeHighlight("#div" + entity.seq) );
				}	
				
			} else {
				$("#ul" + element + " > li").removeClass("end");
				
				$($.treeUI.settings.buffer.join("")).hide().appendTo("#ul" + element).show("drop" ,{}, 500, $.treeUI.makeHighlight("#div" + entity.seq, $.treeUI.makeHighlight("#div" + entity.seq) ) );
			}
			
			$.treeUI.settings.mapData.put(entity.seq, $.treeUI.createEntity( entity.seq, entity.title, element, entity.level, entity.order, entity.extend || null));
			
			$.treeUI.settings.buffer = new Array();
			$.treeUI.settingEvent();
			
			if (element != 0)
				$('#div' + element).css('background', '#ECECEC');
			else
				$('#table' + element).css('background', '#B3D6F3');
		},
		
		updateComplete : function(entity) {
			var element = entity.seq;
			$('#td' + element).html("<nobr>" + entity.title + "</nobr>");
			
			$.treeUI.settings.mapData.put(entity.seq, entity);
			
			if (element != 0)
				$('#div' + element).css('background', '#ECECEC');
			else
				$('#table' + element).css('background', '#B3D6F3');
		},
		
		
		cancel : function(element) {
			if (element != 0)
				$('#div' + element).css('background', '#ECECEC');
			else
				$('#table' + element).css('background', '#B3D6F3');
		},
		
		makeGuide : function() {
			this.enableActionEventCount = 0;
			var HTML = "";
			
			HTML += "	<div align='right'>";
			if (this.settings.actionEvents.insert.useYn == "Y") {
				HTML += "	<img src='" + this.settings.imagePath + 
										  this.settings.actionEvents.insert.image +"'> " + 
										  this.settings.actionEvents.insert.title + "&nbsp;";
			}
			if (this.settings.actionEvents.update.useYn == "Y") { 
				HTML += "	<img src='" + this.settings.imagePath + 
										  this.settings.actionEvents.update.image +"'> " + 
										  this.settings.actionEvents.update.title + "&nbsp;";
			}
			if (this.settings.actionEvents.del.useYn == "Y") {
				HTML += "	<img src='" + this.settings.imagePath + 
										  this.settings.actionEvents.del.image +"'> " + 
										  this.settings.actionEvents.del.title + "&nbsp;";
			}
			if (this.settings.actionEvents.move.useYn == "Y") {
				HTML += "	<img src='" + this.settings.imagePath + 
										  this.settings.actionEvents.move.image +"'> " + 
										  this.settings.actionEvents.move.title + "&nbsp;";
			}
			if (this.settings.actionEvents.etc != null) {
				for (var i = 0 ; i < this.settings.actionEvents.etc.length ; i++) {
					HTML += "	<img src='" + this.settings.imagePath + this.settings.actionEvents.etc[i].image +"'> " + this.settings.actionEvents.etc[i].title + "&nbsp;";
				}
			}
			
			HTML += "	</div>";
			
			this.settings.buffer[this.settings.buffer.length] = HTML;
		},
		
		makeDivTree : function(seq, title, level, parentSeq) {
			var HTML = "";
			
			var titleWidth = 70;
			
			titleWidth = titleWidth - this.settings.actionEvents.etc.length * 8;
			
			
			if (this.settings.check) 
				titleWidth = 65;
			
			HTML += "	<div id='div" + seq + "' parentSeq='" + parentSeq + "'>";
			
			HTML += "		<table id='table" + seq + "' width='100%' border='0' style='table-layout : fixed;'>";	
			HTML += "			<tr>";	
			HTML += "				<td title='"+ title +"' id='td" + seq + "' width='" + titleWidth + "%' style='text-overflow : ellipsis; overflow : hidden;'>";	
			if (this.settings.check) 
				HTML += "					<input id='chk' name='chk' type='checkbox' />&nbsp;<nobr>" + title + "</nobr>";
			else 
				HTML += "					<nobr>" + title + "</nobr>";
			HTML += "				</td>";	
			HTML += "				<td width='" + (100 - titleWidth) + "%' align='right' style='padding-right : 2px;'>";	
			
			if (this.settings.actionEvents.insert.useYn == "Y" && parseInt(this.settings.actionEvents.insert.limit) >= level)
				HTML += "					<img class='insert' src='" + this.settings.imagePath + 
																		 this.settings.actionEvents.insert.image +"' style='cursor:pointer;' alt='" + 
																		 this.settings.actionEvents.insert.title + "' title='" + 
																		 this.settings.actionEvents.insert.title + "' element='" + seq + "' />";
			
			if (this.settings.actionEvents.update.useYn == "Y" && this.settings.actionEvents.update.limit >= level)
				HTML += "					<img class='update' src='" + this.settings.imagePath + 
														  				 this.settings.actionEvents.update.image +"' style='cursor:pointer;' alt='" + 
														  				 this.settings.actionEvents.update.title + "' title='" + 
																		 this.settings.actionEvents.update.title + "' element='" + seq + "' />";
			
			if (this.settings.actionEvents.del.useYn == "Y" && this.settings.actionEvents.del.limit >= level)
				HTML += "					<img class='delete' src='" + this.settings.imagePath + this.settings.actionEvents.del.image +"' style='cursor:pointer;' alt='" + 
																		 this.settings.actionEvents.del.title + "' title='" + 
																		 this.settings.actionEvents.del.title + "' element='" + seq + "' />";
			
			if (this.settings.actionEvents.move.useYn == "Y" && this.settings.actionEvents.move.limit >= level)
				HTML += "					<img class='move' src='" + this.settings.imagePath + this.settings.actionEvents.move.image +"' style='cursor:pointer;' alt='" + 
																	   this.settings.actionEvents.move.title + "' title='" + 
																		 this.settings.actionEvents.move.title + "' element='" + seq + "' />";
			if (this.settings.actionEvents.etc != null) {
				for (var i = 0 ; i < this.settings.actionEvents.etc.length ; i++) {
					if (this.settings.actionEvents.etc[i].useYn == "Y" && this.settings.actionEvents.etc[i].limit >= level)
						HTML += "					<img class='etc"+i+"' src='" + this.settings.imagePath + this.settings.actionEvents.etc[i].image +"' style='cursor:pointer;' alt='" + 
																			  this.settings.actionEvents.etc[i].title + "' title='" + 
																			  this.settings.actionEvents.etc[i].title + "' element='" + seq + "' />";
				}
			}
			
			HTML += "				</td>";	
			HTML += "			</tr>";	
			HTML += "		</table>";	
			
			HTML += "	</div>";		
			
			this.settings.buffer[this.settings.buffer.length] = HTML;
		},
		
		makeChildTree : function(parentSeq, child) {
			var mapKeys = child.keys();
			
			if (mapKeys.length > 0) {
				this.settings.buffer[this.settings.buffer.length] = "<ul id='ul" + parentSeq +  "'>";
				this.makeChildData( mapKeys);
				this.settings.buffer[this.settings.buffer.length] = "</ul>";
			}
		},
		
		makeChildData : function( mapKeys) {
			for (var i = 0 ; i < mapKeys.length ; i++) {
				var i_tempMap = this.settings.mapData.get(mapKeys[i]);
				
				if (i + 1 < mapKeys.length) 
					this.settings.buffer[this.settings.buffer.length] = "<li class='li" + i_tempMap.parentSeq + "' id='li" + i_tempMap.seq + "' seq='" + i_tempMap.seq + "'>";
				else 
					this.settings.buffer[this.settings.buffer.length] = "<li class='end li" + i_tempMap.parentSeq + "' id='li" + i_tempMap.seq + "' seq='" + i_tempMap.seq + "'>";
				
				this.makeDivTree(i_tempMap.seq, i_tempMap.title, i_tempMap.level, i_tempMap.parentSeq);
				
				if (i_tempMap.child.size() > 0) {
					this.makeChildTree(	i_tempMap.seq, i_tempMap.child);
				} else {
					this.settings.buffer[this.settings.buffer.length] = "</li>";
				}
			}
		},
		
		makeParent : function(parentSeq) {
			var HTML = "";
			
			if (this.settings.mapData.containsKey(parentSeq)) {
				var tdWidth = "100px";
				if (this.masterActionYn != "Y")
					var tdWidth = "200px";
				HTML += "<table id='table0'  width='200px' border='0' class='blueb'>";
				HTML += "	<tr>";
				HTML += "		<td width='" + tdWidth + "'>";
				if (this.settings.check) 
					HTML += "		<input id='chk' name='chk' type='checkbox' />&nbsp;<nobr>" + this.settings.mapData.get(parentSeq).title + "</nobr>";
				else
					HTML += "		<nobr>" + this.settings.mapData.get(parentSeq).title + "</nobr>";
				HTML += "		</td>";
				HTML += "		<td width='" + tdWidth + "' align='right' style='padding-right : 2px;'>";	
				if (this.settings.actionEvents.insert.useYn == "Y")
					HTML += "			<img class='insert' src='" + this.settings.imagePath + this.settings.actionEvents.insert.image +"' style='cursor:pointer;' element='0' />";
				if (this.settings.actionEvents.move.useYn == "Y")
					HTML += "			<img class='move' src='" + this.settings.imagePath + this.settings.actionEvents.move.image +"' style='cursor:pointer;' element='0' />";
				HTML += "		</td>";
				HTML += "	</tr>";
				HTML += "</table>";
				
				this.settings.buffer[this.settings.buffer.length] = HTML;
				return true;
			} else {
				return false;
			}
		},
		
		makeHighlight : function(element) {
			$(element).effect("highlight", {}, 500);
		}

	})

	$.fn.treeUI = function(settings) {
		$.treeUI.loading('');
		return this.click(clickHandler)
	}

	var jQueryMatchedObj = this;

	function clickHandler() {
		alert();
	}

})(jQuery);

