//var port = window.location.port;//如果经过代理？这个经过网关是网关的端口
//var port = "${pageContext.request.serverPort}";//这个才是后端端口
var jarWebSocket;
var jarConsole;
var urlObject = new URLBuilder(location.href);
var jarId = urlObject.get('ref');
$(function(){
	
	if (isSystemType()){
		$("#cmd").css({display:'none'});
	}
	
	//调用父页面方法，调整本页面body高、宽与iframe保持
	parent.setContentHeightWidth(jarId);
	
	bindEvent();
	
    readLog2Console();
    
    registerEmptyLogSocket();
});

/**
 * 绑定事件
 */
function bindEvent(){
	
	//添加console-parent内容变化,调整滚动条位置，自动滚动最下面
    $("#console").bind("DOMNodeInserted",function(e){
   		var height = $(this).prop("scrollHeight");
   	    $(this).animate({scrollTop: height},10);		
    });
	
    addRightClickListener();
	
	//命令输入回车执行
	$("#cmdstr").keyup(function(e){
		if (e.keyCode == 13){
			var value = $(this).val();
			if (value != undefined && value != ''){
				jarConsole.fill("开始执行命令：" + value);
				value = value.trim();
				var keywordfc = keyword[value];
				//如果是关键词的方法
				if (keywordfc){
					keywordfc();
				}else{
					//其它命令
					var consoleParam = "consoleParam_"
					if (value.startsWith(consoleParam)){
						var param = value.substring(consoleParam.length);
						var paramArr = param.split("=");
						var k = paramArr[0];
						var v = paramArr[1];
						var consoleParams = {};
						consoleParams[k] = v;
						jarConsole.fill("已执行命令：" + value, consoleParams);
					}
				}
				$(this).val('');
			}
		}
	});
}

/**
 * 右键监听
 */
function addRightClickListener(){
	var items = {
            "stop": {
            	name: "停止日志", 
            	className: 'stop-log', 
            	callback: function(){
            		keyword.stop();
            }},
            "link": {
            	name: "连接日志", 
            	className: 'link-log', 
            	callback: function(){
            		keyword.link();
            }},
            'download':{
				name: "下载日志", 
				callback: function(){
	            	keyword.download();
	         }},
	         "clear": {
	            	name: "清空控制台信息", 
	            	callback: function(){
	            		keyword.clear();
	            }},
	         'emptyLog': {
	        	 name: "清空日志文件内容",
	        	 callback: function(){
	        		 keyword.emptyLog();
	        	 }
	         }
    };
	
	//右键菜单
    $.contextMenu({
        selector: '#console', 
        events:{preShow: function(){
        	if (jarWebSocket){
        		setTimeout(function(){
            		if (jarWebSocket.isConnect() === true){
            			$('.link-log').css({'display': 'none'});
            			$('.stop-log').css({'display': ''});
            		}else{
            			$('.link-log').css({'display': ''});
            			$('.stop-log').css({'display': 'none'});
            		}
        		},0);
        	}
        }},
        items: items
    });	
}

/**
 * 关键词方法
 */
var keyword = {
		
		/**停止日志*/
		stop:function(){
        	if (jarWebSocket){
        		jarWebSocket.close();
        	}
		},
		
		/**连接日志*/
		link: function(){
        	if (jarWebSocket){
        		jarWebSocket.reset().reconnect();
        	}	
		},
		
		/**清空信息*/
		clear: function(){
        	if (jarConsole){
        		jarConsole.clear();
        	}
		},
		
		/**下载日志 */
		download: function(){
			parent.JarManage.downloadLog(jarId);
		},
		
		/** 清空后台日志文件内容*/
		emptyLog: function(){
			requestMethod(JarUrl.getEmptyLog(), {jarId: jarId}, '清空日志内容', function(res){

			});
		},
		
		help: function(){
			var stringBuilder = new StringBuilder(); 
			stringBuilder.append("命令尝试:\n");
			stringBuilder.append("stop      - 停止日志\n");
			stringBuilder.append("link      - 连接日志\n");
			stringBuilder.append("clear     - 清空控制台内容\n");
			stringBuilder.append("download  - 下载日志\n");
			stringBuilder.append("showParams - 展示参数\n");
			jarConsole.fill(stringBuilder.toString());
		},
		
		showParams: function(){
			var stringBuilder = new StringBuilder(); 
			stringBuilder.append("option param:\n");
			stringBuilder.append("consoleParam_allowShowMaxRows=行数      - 展示日志最大行数\n");
			jarConsole.fill(stringBuilder.toString());			
		}
}

/**
 * 把日志信息填充到页面
 */
function readLog2Console(){
	jarConsole = new JarConsole();
	jarConsole.load('console');
    jarWebSocket= new JarWebSocket({
		url: function(){
			return 'ws://'+ ip +':'+ port + root +'/ws/handler/jarlog?jarId=' 
			              + jarId + "&showMessage=false&token=" + localStorage.getItem("token");
		},
		//获取后台返回信息
		onmessage: function(event){
			jarConsole.fill(event.data);
		}
	
	}).addEventListener();
}

/**
 * 是否系统类型
 */
function isSystemType(){
	return jarId == 'system';
}

/**
 * 注册清空日志的socket事件,采用消息订阅形式
 */
function registerEmptyLogSocket(){
	new StompSocketDefine({
		serverUrl: 'http://'+ ip +':'+ port + root + '/server',
		headers: function(){
			return {token: localStorage.getItem("token")};
		},
		subscribes: [{
			subscribeUrl: '/topic/emptylog',
			onmessage: function(res){
				//后端通知需要重新连接
				if (res.body == jarId && jarWebSocket.isConnect()){
					jarWebSocket.close();
					jarWebSocket.reset().reconnect();	
				}
			}
		}]
	}).connect();
}

