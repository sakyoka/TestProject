/**
 * url字符串参数处理
 * 支持封装、解析
 * add:2019-02-21 csy
 */
var URLBuilder = function (initUrl){
	/**
	 * 存储k、v
	 */
	var paramObject = {};
	
	/**
	 * 初始化url，一直保持传入url的值
	 */
	var originUrl = initUrl;
	
	/**
	 * 会把初始化url问号及问号的数据移除，只剩一条简单的url
	 */
	var url;
		
	/**
	 * 添加k、v
	 */
	this.add = function(k , v , notNull){
		if (notNull === true){
			if (v != undefined)
				paramObject[k] = v;			
		}else{
			paramObject[k] = v;
		}
		return this;
	}
	
	/**
	 * 添加k、v ，对v进行url编码
	 */
	this.addEncode = function(k , v , notNull){
		if (notNull === true){
			if (v != undefined)
				paramObject[k] = encodeURIComponent(encodeURIComponent( v ));		
		}else{
			paramObject[k] = encodeURIComponent(encodeURIComponent( v ));
		}
		return this;
	}
	
	/**
	 * 传入的是对象
	 */
	this.addObject = function( o ){
		for (var k in o){
			paramObject[k] = o[k];
		}
		return this;
	}
	
	/**
	 * 传入的是对象，对value进行url编码
	 */
	this.addEncodeObject = function( o ){
		for (var k in o){
			paramObject[k] = encodeURIComponent(encodeURIComponent( o[k] ));
		}
		return this;
	}
	
	/**
	 * 移除k、v
	 */
	this.remove = function(k){
		delete paramObject[k];
		return this;
	}
	
	/**
	 * 根据key获取url上单个值
	 */
	this.get = function(k){
		return paramObject[k];
	}
	
	/**
	 * 获取解析后url
	 */
	this.getUrl = function(){
		return url;
	}
	
	/**
	 * 获取原始url
	 */
	this.getOriginUrl = function(){
		return originUrl;
	}
	
	/**
	 * 设置原始url
	 */
	this.setOringinUrl = function(url){
		originUrl = url;
		this.initURL();//修改原始originUrl之后对应修改解析后url
		return this;
	}
	
	/**
	 * 获取url上全部参数-对象
	 */
	this.getParamObject = function(){
		return paramObject;
	}
	
	/**
	 * initURL 初始化this.url值:
	 * 如this.originUrl = http://127.0.0.1:8080/ApprUntionControl...list.do?a=111&b=222
	 * 那this.url = http://127.0.0.1:8080/ApprUntionControl...list.do
	 */
	this.initURL = function(){
		url = undefined;
		for (var key in paramObject){
			delete paramObject[key];
		}
		if(originUrl != undefined){
			var len = originUrl.indexOf('?');
			url = (len != -1 ? originUrl.substring(0 , len) : originUrl);
			//如果url上有参数存放在paramObject对象
			if ( len != -1 ){
				var paramStr = originUrl.substring(len + 1);
				var paramArr = paramStr.split('&');
				var keyValueStr;
				for (var index in paramArr){
					keyValueStr = paramArr[index];
					var keyValueArr = keyValueStr.split('=');
					var value = (keyValueArr.length == 2 ? keyValueArr[1] : '');
					paramObject[keyValueArr[0]] = value;
				}
			}
		}
	}
	
	/**
	 * 执行初始化
	 */
	this.initURL();
	
	/**
	 * this.url和参数拼接之后的访问url
	 */
	this.toString = function(){
		//如果url为undefined返回'';如果不是判断有没有问号，没有追加问号
		var str = ( url == undefined ? '' : ( url.indexOf('?') != -1 ? url : (url + '?') ) );
		for (var key in paramObject){
			str += (key + "=" + paramObject[key] + "&");
		}
		return ( str.length == 1 ? '' : str.substring(0 , str.length - 1) );		
	}
}