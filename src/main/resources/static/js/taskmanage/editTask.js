$(function(){
	
	radioListener();
	
	var currentUrlObject = new URLBuilder(location.href);
	var taskId = currentUrlObject.get("taskId");
	initData(taskId);
});

function initData(taskId){
	var dispatchType = 0;
	var taskType = 0;
	if (taskId != ''){
		reqeustCommonApi({
			async: false,
			data: new ApiParams('taskManage', 'saveTaskInfo')
		              .params('paramObject', paramObject)
		              .build(),
			success: function(res){
				if (res.code == '200'){
					AlertMessgaeUtils.alert({
						id:'tip', 
						title:'提示', 
						content: '保存数据成功', 
						onclose: function(){
							closeDialog();
					}});
				}else{
					AlertMessgaeUtils.alert({id:'tip', title:'提示', 
						content: '保存失败: ' + validResult.msg});
				}
			}
		});
	}
	dispatchTypeControl(dispatchType);
	taskTypeTypeControl(taskType);
}

/**
 * 关闭
 */
function closeDialog(){
	AlertMessgaeUtils.hide('editTask', $(top.document.body));
}

/**
 * 提交数据
 */
function submitData(){
	var check1 = $('input[name=dispatchType]:checked').val();
	var validResult = ParamObjectUtils.valid('.param-field', (check1 == '0' ? '1' : '2'));
	if (validResult.result === false){
		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: validResult.msg});
		return ;
	}
	
	var check2 = $('input[name=taskType]:checked').val();
	validResult = ParamObjectUtils.valid('.param-field', (check2 == '0' ? '3' : '4'));
	if (validResult.result === false){
		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: validResult.msg});
		return ;
	}
	console.log(validResult);
	var paramObject = validResult.data;
	reqeustCommonApi({
		async: false,
		data: new ApiParams('taskManage', 'saveTaskInfo')
	              .params('paramObject', paramObject)
	              .build(),
		success: function(res){
			if (res.code == '200'){
				AlertMessgaeUtils.alert({
					id:'tip', 
					title:'提示', 
					content: '保存数据成功', 
					onclose: function(){
						closeDialog();
				}});
			}else{
				AlertMessgaeUtils.alert({id:'tip', title:'提示', 
					content: '保存失败: ' + validResult.msg});
			}
		}
	});
}

function radioListener(){
	$('input[name=dispatchType]').change(function(){
		dispatchTypeControl($(this).val());
	});
	
	$('input[name=taskType]').change(function(){
		taskTypeTypeControl($(this).val());
	});	
}

function dispatchTypeControl(type){
	$(".dispatchTypeCls").css({display: 'none'});
	$("body").find("[dispatchType=" + type + "]").css({display: ''});
}

function taskTypeTypeControl(type){
	$(".taskTypeCls").css({display: 'none'});
	$("body").find("[taskType=" + type + "]").css({display: ''});
}