/**
 * add 2022-09-09 csy
 * 下载 
 */
(function ($) {
	
	/**
	 * 默认成功处理方法
	 */
	var successDefault = function(response, xhr, fileName){
		
		var disposition = xhr.getResponseHeader("Content-Disposition");
		if (fileName == undefined && disposition){
			fileName = disposition.split("=")[1];
			fileName = decodeURIComponent(fileName);
		}
		
		if (fileName == undefined || fileName == ''){
			AlertMessgaeUtils.alert({id:'tips', title:'提示', content: '请设置下载文件名称'});
			return ;
		}
		
		var link = document.createElement('a');
		link.href = URL.createObjectURL(response);
		link.download = fileName;
		link.style.display = 'none'; 
		document.body.appendChild(link);
		link.click();
		link.remove();		
	}
	
	/**
	 * 默认下载前
	 */
	var beforeDefault = function(){
		AlertMessgaeUtils.alert({id:'downloading', title:'提示', content: '下载文件中...', hiddenClose: true});
	}
	
	/**
	 * 默认完成后
	 */
	var completeDefault = function(){
		AlertMessgaeUtils.hide('downloading');
		AlertMessgaeUtils.alert({id:'downloaded', title:'提示', content: '下载完毕.'});
	}
	
	$.download = function(config){
		config = config || {};
		var medhod = config.method || 'get';
		var headers = config.headers || {};
		var data = config.data || {};
		var requestUrl = config.url;
		var async = config.async || true;
		var fileName = config.fileName;
		var success = config.success;
		var error = config.error;
		var beforeSend = config.beforeSend;
		var complete = config.complete;
		
		var xhr = new XMLHttpRequest();
		xhr.open(medhod, requestUrl, async);
		xhr.responseType = 'blob';
		for (var k in headers){
			xhr.setRequestHeader(k, headers[k]);setRequestHeader
		}
		
		if (onBeforeSend){
			onBeforeSend(xhr);
		}
		
		xhr.onload = function(){
			var data = xhr.response;
			if (success){
				success(data, xhr, fileName);
			}else{
				successDefault(data, xhr, fileName);
			}			
		}
		
		xhr.onloadstart = function(){
			
			if (beforeSend){
				beforeSend();
			}else{
				beforeDefault();
			}
		}
		
		xhr.onloadend = function(){
			
			if (onComplete){
				onComplete(xhr);
			}
			
			if (complete){
				complete();
			}else{
				completeDefault();
			}
		}
		
		xhr.onerror = function(){
			
			if (onError){
				onError(xhr);
			}
			
			if (error){
				error(xhr);
			}
		}
		
		xhr.send(data);
	}
	
})($);