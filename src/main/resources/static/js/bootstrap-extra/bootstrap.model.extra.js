//存储id
var idObjects = {};

//存储关闭方法调用
var closeRefObject = {};

/**
 * 封装 提示框
 * add: 2022-01-16 csy
 */
var AlertMessgaeUtils = {
	
	alert: function(param){
		var id = param.id;
		var target = param.target || $('body');
		//已经存在的时候删除后创建,默认是
		var alreadyHaveDelete = (alreadyHaveDelete == undefined 
		         ? true : param.alreadyHaveDelete);
		if (idObjects[param.id] != undefined){
			if (alreadyHaveDelete === true){
				this.remove(id, target);
			}else{
				this.show(id, target);
				return ;				
			}
		}
		if (alreadyHaveDelete === true){
			this.remove(id);
		}
		
		if (param.onclose){
			closeRefObject[id] = {};
			(closeRefObject[id])['onclose-' + id] = param.onclose;
		}
		
		try{
			idObjects[id] = param;
			var alterDiv = getTemplateStr(param);
			target.append($(alterDiv));			
			this.show(id, target);
			$('.modal-backdrop').removeClass("modal-backdrop");
		}catch(e){
			delete idObjects[id];
		}
		return this;
	},
	
	show: function(id, target){
		if (target){
			target.find("#" + id).modal('show');
		}else{
			$("#" + id).modal('show');
		}
		return this;
	},
	
	hide: function(id, target){
		if (target){
			//target.find('.modal-backdrop').removeClass("modal-backdrop");
			target.find("#" + id).hide();
		}else{
			//$('.modal-backdrop').removeClass("modal-backdrop");
			$("#" + id).modal('hide');
		}
		return this;
	},
	
	remove: function(id, target){
		if (target){
			//$(target).find('.modal-backdrop').removeClass("modal-backdrop");
			$(target).find("#" + id).remove();
		}else{
			//$('.modal-backdrop').removeClass("modal-backdrop");
			$("#" + id).remove();
		}
		return this;
	},
	
	closeAll: function(target){
		for (var k in idObjects){
			this.hide(k, target);
		}
	}
}

function getTemplateStr(param){
	var id = param.id;
	if (id == undefined || id == ""){
		alert("弹出框id不能为空");
		throw '';
	}
	var title = param.title;
	var buttons = param.buttons;
	try{
		StringBuilder
	}catch(e){
		alert('需要引入StringBuilder');
		throw '需要引入StringBuilder';		
	}
	var stringBuilder = new StringBuilder();
	var hiddenClose = param.hiddenClose || false;
	//设置不能ESC关闭窗口，点击空白处不能关闭窗口
	var canclose = (hiddenClose === true ? 'data-keyboard="false" data-backdrop="static"' : '');
	var width = param.width;
	var height = param.height;
	var style = "style=" + (width ? 'width:' + width : '');
	    style += (height ? 'height:' + height : '');
	stringBuilder.append('<div class="modal fade" id="'+ id +'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" '+ canclose +'>')
	             .append('<div class="modal-dialog">')
	             .append('<div class="modal-content" '+ style +'>')
	             .append('<div class="modal-header">');
	
	if (hiddenClose === false){
    	stringBuilder.append('<button type="button" class="close model-close-button" data-dismiss="modal" aria-hidden="true" model-close-id="'+ id +'">&times;</button>'); 
    }
	
	var content = param.url ? '<iframe id="model-iframe-'+ id +'" src='+ param.url 
			+' style="width:100%; height: 100%;border: medium none; overflow: auto;"></iframe>' : param.content;
	stringBuilder.append('<h4 class="modal-title" id="title'+ id +'">'+ (title != undefined ? title : '') +'</h4>')
	             .append('</div>')
	             .append('<div class="modal-body" style="height: 85%;">' + content + '</div>');
	if (buttons != undefined && buttons.length > 0){
		stringBuilder.append('<div class="modal-footer">');
		for (var index in buttons){
			var button = buttons[index];
			var buttonId = button.id || button.name;
			var name = button.name;
			var styleClass = button.styleClass;
			var isClose = button.isClose;
			var closeStyle = (isClose == true ? 'data-dismiss="modal"': "");
			styleClass = (styleClass == undefined ? 'btn btn-primary' : styleClass)
			stringBuilder.append('<button id="'+ buttonId +'" parent-id="'+ id +'" type="button" class="'+ styleClass +' after-click-button" '+ closeStyle  +' >' + name + '</button>');
			if (button.onclose){
				if (closeRefObject[id] == undefined){
					closeRefObject[id] = {};
				}
				(closeRefObject[id])['button-' + buttonId] = button.onclose;
			}
		}
		stringBuilder.append('</div>');
	}
	stringBuilder.append('</div>')
	             .append('</div>')
	             .append('</div>');
	return stringBuilder.toString();		
}

$(function(){
	
	//button关闭事件
	$('body').on('click', '.after-click-button', function(){
		var buttonId = $(this).attr('id');
		var id = $(this).attr('parent-id');
		if (closeRefObject[id]){
			if ((closeRefObject[id])['button-' + buttonId]){
				(closeRefObject[id])['button-' + buttonId]();
			}
			
			if ((closeRefObject[id])['onclose-' + id]){
				(closeRefObject[id])['onclose-' + id]();
			}
		}
	});
	
	//x关闭事件
	$('body').on('click', '.model-close-button', function(){
		var id = $(this).attr('model-close-id');
		if (closeRefObject[id] && (closeRefObject[id])['onclose-' + id]){
			(closeRefObject[id])['onclose-' + id]();
		}
	})
})