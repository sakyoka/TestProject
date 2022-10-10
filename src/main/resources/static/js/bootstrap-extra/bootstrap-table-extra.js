/**
 * add 2022-09-15 csy
 * jquery 添加boostrap 表格展示
 * @param $
 */
(function($){
	
	var initAllId = function(id, innerConfig){
		innerConfig.id = id;
		innerConfig.tbodyId = 'bootstrap-body-' + id;
		innerConfig.tableId = 'bootstrap-table-' + id;
		innerConfig.divId = 'bootstrap-div-' + id;	
		innerConfig.theadId = 'bootstrap-thead-' + id;
		innerConfig.pageId = 'bootstrap-page-' + id;
		innerConfig.buttonsId = 'bootstrap-buttons-' + id;
	}
	
	$.bootstrapTable = function(config){
		config = config || {};
		
		var id = config.id;
		$("#" + config.id).css({height: '100%'});
		var innerConfig = {};
		initAllId(id, innerConfig);
		
		var buttonsStr = disposeButtons(config, innerConfig);
		var headerStr = disposeHeaders(config, innerConfig);
		var divStr = '<div id='+ innerConfig.divId +' style="height: 100%; padding: 25px;">'+ buttonsStr
		    +'<table id='+ config.tableId +' class="table">' + headerStr 
		    + '<tbody id='+ innerConfig.tbodyId 
		    + ' ></tbody></table></div>';
		$("#" + config.id).html($(divStr));
		if (config.height){
			$("#" + innerConfig.divId).css({height: config.height});
		}
		
		disposeBodyData(config, innerConfig);
		
		this.load = function(queryParams){
			config.queryParams = queryParams;
			disposeBodyData(config, innerConfig);
		}
		
		$('body').on('click', '.check-parent', function(){
			$('.check-sub').prop("checked", $(this).is(':checked'));
		});
		
		return this;
	}
	
	/**
	 * 处理按钮
	 */
	var disposeButtons = function(config, innerConfig){
		if (config.buttons == undefined 
				|| config.buttons.length == 0){
			return '';
		}
		var buttonDiv = '<div id='+ innerConfig.buttonsId +'>';
		$.each(config.buttons, function(index, item){
			var enable = item.enable == undefined ? true : item.enable;
			if (enable === true){
				var id = 'event-' + index;
				buttonDiv += '<button id='+ id 
				          +' class="btn btn-default">'+ item.name +'</button>  ';
				$('body').on('click', '#' +id, function(){
					item.handle();
				});	
			}
		});
		buttonDiv += '</div>';
		return buttonDiv;
	}
	
	/**
	 * 处理header
	 */
	var disposeHeaders = function(config, innerConfig){
		var columns = config.columns;
		var headerStr = '<thead id='+ innerConfig.theadId +'><tr>';
		var headerFields = [];
		var headerFieldFormats = {};
		
		if (config.checkBox === true){
			headerStr += '<th><input type="checkbox" class="check-parent" id=""/></th>';
		}
		
		if (config.showIndex === true){
			headerStr += '<th>序号</th>';
		}
		
		$.each(columns, function(){
			headerStr += '<th>'+ this.title +'</th>';
			headerFields.push(this.fieldName);
			headerFieldFormats[this.fieldName] = this.formatter;
		});
		headerStr += '</tr></thead>';
		config.headerFields = headerFields;
		config.headerFieldFormats = headerFieldFormats;
		return headerStr;
	}
	
	/**
	 * 处理body数据
	 */
	var disposeBodyData = function(config, innerConfig){
		var queryParams = config.queryParams || {};
		var currentPage = config.currentPage || 1;
		var pageSize = config.pageSize || 10;
		var datas = typeof(config.data) === 'function' ? 
				config.data({pageSize: pageSize, pageIndex: currentPage}, queryParams) 
				: config.data;
		var data = (config.dataFilter != undefined ? config.dataFilter(datas.data): datas.data);
		var tbodyStr = data ? disposeDatas(config, data.datas, innerConfig) : '';

		$("#" + innerConfig.tbodyId).html($(tbodyStr));
		
		var rows = data ? data.rows : 0;
		disposePagination(config, rows, innerConfig);
		
		disposeBodyHeight(config, innerConfig);
		
		bindClickColumns(config, innerConfig);
	}
	
	/**
	 * 调整tbody 
	 */
	var disposeBodyHeight = function(config, innerConfig){
		var autoHeight = config.autoHeight === undefined ? true : config.autoHeight;
		if (autoHeight === true){
			var targetHeight = $("#" + innerConfig.id).height();
			var theadHeight = $("#" + innerConfig.theadId).height();
			var pageHeight = $("#" + innerConfig.pageId).height();
			var tbodyHeight = targetHeight 
			                  - theadHeight 
			                  - (pageHeight == undefined ? 0 : pageHeight)
			                  - 25 * 2
			                  - 30;

			$("#" + innerConfig.tbodyId).css({height: tbodyHeight, overflow: 'auto', display: 'block'});
			$("#" + innerConfig.theadId).css({width: '100%', display: 'table'});
			var tbodyTrs = $("#" + innerConfig.tbodyId).find("tr");
			tbodyTrs.css({width: '100%', display: 'table'});
			var headTrs = $("#" + innerConfig.theadId).find('tr');
			var lastHeadTr = headTrs.eq(headTrs.length -1);
			var lastThs = lastHeadTr.find("th");
			var widthObject = {};
			$.each(lastThs, function(index){
				widthObject[index] = $(this).width();
			});
			tbodyTrs.each(function(){
				$(this).find('td').each(function(index){
					$(this).width(widthObject[index]);
				});
			});
		}		
	}
	
	/**
	 * 处理数据
	 */
	var disposeDatas = function(config, datas, innerConfig){
		
		if (datas == undefined || datas.length == 0){
			return '';
		}
		
		var headerFields = config.headerFields;
		var headerFieldFormats = config.headerFieldFormats;
		var bodyStr = '';
		$.each(datas, function(index){
			var data = this;
			bodyStr += '<tr class="data-td">';
			
			if (config.checkBox === true){
				bodyStr += '<td><input type="checkbox" class="check-sub" data-id=""/></td>';				
			}
			
			if (config.showIndex === true){
				bodyStr += '<td>'+ (index + 1) +'</td>';				
			}
			
			$.each(headerFields, function(index, item){
				bodyStr += '<td>' +  (headerFieldFormats[this] != undefined ? 
						          headerFieldFormats[this](data[this], data) : data[this]) 
						+ '</td>';
			});
			bodyStr += '</tr>';
		});
		return bodyStr;
	}

	/**
	 * 处理分页
	 */
	var disposePagination = function(config, total, innerConfig){
		var pagination = config.pagination === undefined || true;	
		if (pagination){
			if (innerConfig.pageId){
				$("#" + innerConfig.pageId).remove();
			}
			var currentPage = config.currentPage || 1;
			var pageSize = config.pageSize || 10;
			var totalPage = Math.ceil(parseInt(total, 10) / pageSize);
			var previousDisabled = currentPage == 1 ? 'disabled' : '';
			var nextDisabled = currentPage == totalPage ? 'disabled' : '';
			var previousId = "previous" + config.id;
			var nextId = "next" + config.id;
			var paginationStr = '<ul class="pager" id='+ innerConfig.pageId +'>'
			    + '<li id='+ previousId +' class="'+ previousDisabled +'"><a href="#">上一页</a></li>'
			    + '<li id='+ nextId +' class="'+ nextDisabled +'"><a href="#">下一页</a></li>'
			+ '</ul>';
			$("#" + config.divId).append($(paginationStr));
			
			$("#" + previousId).click(function(){
				if (currentPage > 1){
					
					currentPage--;
					config.currentPage = currentPage;
					disposeBodyData(config, innerConfig);
					
					if (currentPage == 1){
						$(this).addClass('disabled');
					}
					
					if (currentPage < totalPage){
						$("#" + nextId).removeClass('disabled');
					}
				}
			});
			
			$("#" + nextId).click(function(){
				if (currentPage < totalPage){
					currentPage++;
					config.currentPage = currentPage;
					disposeBodyData(config, innerConfig);	
					if (currentPage == totalPage){
						$(this).addClass('disabled');
					}
					
					if (currentPage > 1){
						$("#" + previousId).removeClass('disabled');
					}
				}
			});
		}
	}
	
	/**
	 * 点击行触发
	 */
	var bindClickColumns = function(config, innerConfig){
		$('.data-td').click(function(){
			$('.data-td').css({'background-color': 'white'});
			$(this).css({'background-color': '#F3F3F3'});
			if (config.clickColumn){
				config.clickColumn(this);
			}
		});
	}
})(jQuery)