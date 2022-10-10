/**
 * 请求接口
 * @param url  请求地址
 * @param params 请对参数对象
 * @param opName 操作名字
 * @param successfc  成功回调方法
 * @param methodType 请求类型get/post
 * @param showDefaultSuccessTip 是否显示默认成功提示
 */
function requestMethod(url, params, opName, successfc, methodType, showDefaultSuccessTip){
	methodType = methodType || 'get';//默认get
	showDefaultSuccessTip = showDefaultSuccessTip === undefined ? true : showDefaultSuccessTip;
	AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '确定'+ opName +'吗?', buttons:[{
		name:'确定',
		isClose: true,
		onclose: function(){
			$.ajax({
			    url: url,
			    type: methodType,
			    data: params,
			    async: true,
			    beforeSend: function() {
			    	AlertMessgaeUtils.alert({id:'runWaiting', title:'提示', content: '正在'+ opName +'，请等待...', hiddenClose: true});
			    },
			    success: function(res) {
			    	
			    	AlertMessgaeUtils.hide('runWaiting');
			    	
			    	if (showDefaultSuccessTip === true){
				    	if (res.code == '200'){
				    		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: opName + '成功!'});
				    	}else{
				    		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: opName + '失败:' + res.msg});
				    	}	
			    	}
			    	
		    		if (successfc){
		    			successfc(res);
		    		}
			    },
			    complete:function(){
			    	AlertMessgaeUtils.hide('runWaiting');
			    },
			    error: function(xhr, ts, et){
			    	var status = xhr.status;
			    	var msg = '';
			    	switch (status){
			    	    case 400: msg = '请求参数存在错误'; break;
			    	    case 401: msg = '请求地址没有权限'; break;
			    	    case 403: msg = '请求地址没有权限'; break;
			    	    case 404: msg = '未知请求地址'; break;
			    	    case 500: msg = '内部服务器处理异常'; break;
			    	    default: msg = '请求失败'; break;
			    	}
				    AlertMessgaeUtils.alert({id:'tip', title:'提示', content: msg + '，' +opName + '失败。'});
			    }
			});		
		}
	},{
		name:'取消',
		isClose: true
	}]});
}

/**
 * GET 请求
 * @param url  请求地址
 * @param params 请对参数对象
 * @param opName 操作名字
 * @param successfc  成功回调方法
 * @param showDefaultSuccessTip 是否显示默认成功提示
 */
function requestGetMethod(url, params, opName, successfc, showDefaultSuccessTip){
	requestMethod(url, params, opName, successfc, 'get', showDefaultSuccessTip);
}

/**
 * POST请求
 * @param url 请求地址
 * @param params 请对参数对象
 * @param opName 操作名字
 * @param successfc 成功回调方法
 * @param showDefaultSuccessTip 是否显示默认成功提示
 */
function requestPostMethod(url, params, opName, successfc, showDefaultSuccessTip){
	requestMethod(url, params, opName, successfc, 'post', showDefaultSuccessTip);
}

/**
 * 请求公共接口
 * @param config
 */
function reqeustCommonApi(config){
	config = config || {};
	var url = config.url || (root + '/api/common');
	var showDefaultSuccessTip = (config.showDefaultSuccessTip === undefined 
			? true: config.showDefaultSuccessTip);
	var globalShowTip = config.globalShowTip;
	var opName = config.opName;
	var success = config.success;
	var error = config.error;
	var beforeSend = config.beforeSend;
	var complete = config.complete;
	var data = config.data || {};
	var async = config.async === undefined ? true : config.async;
	var request = function(){
		$.ajax({
		    url: url,
		    type: 'post',
		    contentType: 'application/json; charset=UTF-8',
		    data: JSON.stringify(data),
		    async: async,
		    beforeSend: function() {
		    	if (globalShowTip === true){
		    		AlertMessgaeUtils.alert({
		    			id:'runWaiting', 
		    			title:'提示', 
		    			content: '正在'+ opName +'，请等待...', 
		    			hiddenClose: true
		    		});
		    	}
		    	if (beforeSend){
		    		beforeSend();
		    	}
		    },
		    success: function(res) {
		    	if (globalShowTip === true){
			    	AlertMessgaeUtils.hide('runWaiting');
			    	
			    	if (showDefaultSuccessTip === true){
				    	if (res.code == '200'){
				    		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: opName + '成功!'});
				    	}else{
				    		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: opName + '失败:' + res.msg});
				    	}	
			    	}	
		    	}
		    	
	    		if (success){
	    			success(res);
	    		}
		    },
		    complete:function(){
		    	if (globalShowTip === true){
		    		AlertMessgaeUtils.hide('runWaiting');
		    	}
		    	if (complete){
		    		complete();
		    	}
		    },
		    error: function(xhr, ts, et){
		    	if (globalShowTip === true){
			    	var status = xhr.status;
			    	var msg = '';
			    	switch (status){
			    	    case 400: msg = '请求参数存在错误'; break;
			    	    case 401: msg = '请求地址没有权限'; break;
			    	    case 403: msg = '请求地址没有权限'; break;
			    	    case 404: msg = '未知请求地址'; break;
			    	    case 500: msg = '内部服务器处理异常'; break;
			    	    default: msg = '请求失败'; break;
			    	}
				    AlertMessgaeUtils.alert({id:'tip', title:'提示', content: msg + '，' +opName + '失败。'});	
		    	}
		    	if (error){
		    		error(xhr, ts, et);
		    	}
		    }
		});	
	}
	
	if (globalShowTip === true){
		AlertMessgaeUtils.alert({
			id:'tip', 
			title:'提示', 
			content: '确定'+ opName +'吗?', 
			buttons:[{
				name:'确定',
				isClose: true,
				onclose: function(){
					request();
				}
			},{
				name:'取消',
				isClose: true
		}]});	
	}else{
		request();
	}
}