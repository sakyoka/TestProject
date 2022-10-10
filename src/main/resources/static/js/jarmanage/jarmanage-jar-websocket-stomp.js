/**
 * add by csy 2022-08-26
 * stomp协议的scoket
 * 
 */
var StompSocketDefine = function(config){
	
	/**配置数据对象 */
	config = config || {};
	/**server 地址*/
	var serverUrl = config.serverUrl;
	/**订阅信息集合*/
	var subscribes = config.subscribes || [];
	/**创建socket*/
	var socket = new SockJS(serverUrl);
	/**使用stomp协议*/
	var stompClient = Stomp.over(socket);
	/**是否自动重连 */
	var autoConnect = config.autoConnect || true;
	/**判断是否主动关闭*/
	var driving = false; 
	/** 尝试重新连接次数*/
	var defaultFailTryConnTimes = config.failTryConnTimes || 5;
	/**尝试累加次数 */
	var tryConnTimes = 0;
	/**是否连接中 */
	var isConnect = false;
	var _this = this;
	var headers = config.headers || {};
	
	/**
	 * 恢复初始状态值
	 */
	this.reset = function(){
		autoConnect = config.autoConnect || true;
		driving = false; 
		defaultFailTryConnTimes = config.failTryConnTimes || 5;
		tryConnTimes = 0;
		isConnect = false;
	}
	
	/**
	 * 获取client
	 */
	this.getStompClient = function(){
		return stompClient;	
	}
	
	/**
	 * 断开连接
	 */
	this.disconnect = function(){
		driving = true;
		if (stompClient != undefined){
			stompClient.disconnect();
		}
		return this;
	}
	
	/**
	 * 连接
	 */
	this.connect = function(){
		
		//支持函数形式，为了实时更新headers
		var headerDatas = typeof(headers) == 'function' ? headers() : headers;
		
		/**
		 * 链接服务
		 */
		stompClient.connect(headerDatas, function(frame){
			isConnect = true;
			driving = false;
			tryConnTimes = 0;
			console.log("stomp socket link state: "+ frame);
			/**
			 * 遍历订阅地址
			 */
			for (var i in subscribes){
				var subscribeObject = subscribes[i];
				var subscribeUrl = subscribeObject.subscribeUrl;
				var onmessage = subscribeObject.onmessage;
				stompClient.subscribe(subscribeUrl, function(res){
					if (onmessage){
						onmessage(res);
					}
				});
			}
			
		}, function(){	
			isConnect = false;
			console.log('stomp socket link error');
			if (isConnect === false 
			         && autoConnect === true 
			         && driving === false){
				if (defaultFailTryConnTimes > tryConnTimes){
					_this.tryToConnect();
				}
			}
		});	
		
		return this;
	}
	
	/**
	 * 尝试连接
	 */
	this.tryToConnect = function(){
		console.log('stomp socket try to connect...');
		socket = new SockJS(serverUrl);
		stompClient = Stomp.over(socket);
	    var _tryToConnect = function (){
		
			tryConnTimes += 1;
			if (defaultFailTryConnTimes < tryConnTimes){
				console.log('stomp socket try to connect times is more than max times:' + tryConnTimes);
				return ;
			}
			
			if (isConnect === true){
				return ;
			}
			
			_this.connect();
			
			setTimeout(function(){
				_tryToConnect();
		    }, 3000);
		}
		
		_tryToConnect();
	}
	
	/**
	 * 刷新页面前把连接关掉
	 */
	window.onbeforeunload = function(){
		
		driving = true;
		
		if (stompClient != undefined){
			stompClient.disconnect();
		}
	}
}  