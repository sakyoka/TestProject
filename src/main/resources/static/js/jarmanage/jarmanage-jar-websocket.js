/**
 * WebSocket定义
 * add 2022-01-27 csy
 * ws.readyState ==>
    CONNECTING：值为0，表示正在连接。
    OPEN：值为1，表示连接成功，可以通信了。
    CLOSING：值为2，表示连接正在关闭。
    CLOSED：值为3，表示连接已经关闭，或者打开连接失败。
 */
var JarWebSocket = function(config){
	
	var config = config || {};//配置对象
	//是否连接
	var isConnect = false;
	//WebSocket对象
	var ws = config.ws;
	//请求地址
	var wsurl = config.url; 
	//onmessage 事件
	var onmessage = config.onmessage;
	//是否主动关闭
	var driving = false;
	//是否自动连接，不传默认是
	var autoConnect = config.autoConnect || true;
	//尝试重新连接次数
	var defaultFailTryConnTimes = config.failTryConnTimes || 5;
	var tryConnTimes = 0;
	//心跳失败次数
	var defaulFailtHeartCheckTimes = config.failHeartCheckTimes || 5;
	var tryHeartCheckTimes = 0;
	
	//心跳定时器
	var heartCheckInterval;
	
	//当前对象
	var jarWs = this;

	/**
	 * 创建
	 */
	this.create = function(url){

		if (isConnect === true && ws != undefined){
			return this;
		}
		
		//支持函数形式，为了实时更新url参数
		var socketUrl = typeof(url) == 'function' ? url() : url;
		ws = new WebSocket(socketUrl);
		return this;
	}
	
	/**
	 * 事件处理
	 */
	this.addEventListener = function(){
		
		if (ws == undefined){
			this.create(wsurl);
		}
		
		if (ws == undefined){
			throw '获取WebSocket失败.';
		}
		
		ws.onopen = function(){
			isConnect = true;
			heartCheckInterval = heartCheck();
			console.log('连接websocket服务成功.');
		}
		
		ws.onerror = function(){
			//清除保持连接定时器
			if (heartCheckInterval){
				clearInterval(heartCheckInterval)
			}
			console.log('连接websocket服务失败.');
		}
		
		ws.onclose = function(){
			console.log('websocket连接服务关闭.');
			//标识连接失败
			isConnect = false;
			//清除保持连接定时器
			if (heartCheckInterval){
				clearInterval(heartCheckInterval)
			}
			//重新连接
			if (!driving && autoConnect === true){
				console.log('websocket尝试连接...');
				jarWs.reconnect();
			}
		}
		
		/**
		 * 接收信息
		 */
		ws.onmessage = function(event){
			if (onmessage){
				onmessage(event);
			}else{
				console.log(event.data);
			}
		}
		
		/**
		 * 浏览器刷新，关闭ws
		 */
		window.onbeforeunload = function(){
			jarWs.close();
		}		
		
		return this;
	}
	
	/**
	 * 主动关闭连接
	 */
	this.close = function(){
		
		//浏览器刷新也算是主动关闭,手动调用也是
		driving = true;
		
		//清除保持连接定时器
		if (heartCheckInterval){
			clearInterval(heartCheckInterval)
		}
		
		//关闭连接
		ws.close();
		
		isConnect = false;
		
		return this;
	}
	
	/**
	 * 重新连接
	 */
	this.reconnect = function(){
		tryConnTimes += 1;
		if (defaultFailTryConnTimes < tryConnTimes){
			console.log('已超过最大尝试连接次数失败，不再重连.times:' + tryConnTimes);
			return ;
		}
		
		if (isConnect === true){
			return ;
		}
		setTimeout(function(){
			if (heartCheckInterval){
				clearInterval(heartCheckInterval);
			}
			jarWs.create(wsurl).addEventListener();
	    }, 3000);
	}
	
	/**
	 * 清除
	 */
	this.reset = function(){
		//重置重连次数
		tryConnTimes = 0;
		//重置心跳次数
		tryHeartCheckTimes = 0;
		//清空定时
		if (heartCheckInterval){
			clearInterval(heartCheckInterval);
		}
		//设置没有主动关闭
		driving = false;
		return this;
	}
	
	/**
	 * 获取WebSocket
	 */
	this.getWebSocket = function(){
		return ws;
	}
	
	/**
	 * 获取连接状态true/false
	 */
	this.isConnect = function(){
		return isConnect;
	}
	
	/**
	 * 心跳连接,10秒发送一次
	 */
	var heartCheck = function(){
		
		var heartCheckInterval = setInterval(function(){
			
			if (defaulFailtHeartCheckTimes < tryHeartCheckTimes){
				console.log('已超过最大尝试心跳发送失败次数，不再发送.times:' + tryHeartCheckTimes);
				clearInterval(this);
				return ;
			}
			
			try{
				ws.send('HEART_CHECK');
			}catch(e){
				console.log("readyState:" + ws.readyState);
				tryHeartCheckTimes += 1;
			}
			
		}, 10 * 1000);
		
		return heartCheckInterval;
	}
}  