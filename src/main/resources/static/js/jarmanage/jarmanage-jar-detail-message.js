var urlObject = new URLBuilder(location.href);
var ref = urlObject.get('ref');
var refs = ref.split('-');
var jarId = refs.length > 2 ? '' : refs[1];
var jarObject = {};
$(function(){
	
	parent.setContentHeightWidth(ref);
	
	bindEvent();
	
	initAboutDetailData();
});

/**
 * 初始化数据 
 */
function initAboutDetailData(){
	$.get(JarUrl.getOneJar(), {jarId: jarId}, function(res){
    	if (res.code != '200'){
    		defaultErrorContent();
    		return ;
    	}
    	jarObject = res.data;
    	ParamObjectUtils.fill2Form(jarObject, {igonreSub: true, keyValue: function(k, v){
    		if (fieldEvent[k]){
    			fieldEvent[k](jarId, k, v);
    		}else{
        		$("#" + k).text(v);
        		$("#" + k).attr("title", v);
    		}
    	}});
    	
    	if (jarObject.isRuning == 1){
    		initRuningTimeMessage();
    	}else{
            $("#jarRuningMessage").html('应用未启动，没有运行时信息...');
        }
	}).error(function(){
		defaultErrorContent();
	});	
}

/**
 * 下载附件
 * @param filePath
 */
function downloadFile(fileType){
	$.download({
		url: JarUrl.getDownload() + "?jarId=" + jarId + "&fileType=" + fileType 
	});
}

var fieldEvent = {
	
	/**
	 * 给日志路径赋予下载
	 */
	logPath: function(jarId, fieldName, fieldValue){
		$("#" + fieldName).html($("<a style='word-break: break-all;' href='javascript:void(0);' onclick='downloadFile(\""+
				encodeURIComponent(fieldValue)
				+"\")'>"+ fieldValue +"</a>"));
		$("#" + fieldName).attr("title", "点击可下载:" + fieldValue);
	},
	
	/**
	 * 给jar路径给予下载
	 */
	jarPath: function(jarId, fieldName, fieldValue){
		$("#" + fieldName).html($("<a style='word-break: break-all;' href='javascript:void(0);' onclick='downloadFile(\"jar\")'>"+ 
				fieldValue +"</a>"));
		$("#" + fieldName).attr("title", "点击可下载:" + fieldValue);
	}
}

/**
 * 初始化运行时信息
 */
function initRuningTimeMessage(){
	var jarRefJmxRmiIpPortBean = jarObject.jarRefJmxRmiIpPortBean;
	ParamObjectUtils.fill2Form(jarRefJmxRmiIpPortBean, {keyValue: function(k, v){
		$("#jarRefJmxRmiIpPortBean-" + k).text(v);
	}});
	
	var url = JarUrl.getApplicationMessage();
	$.get(url,  {jarId: jarId}, function(res){
		if (res.code != 200){
			$("#detailRuntimeMessage").html(res.msg);
			return ;
		}
		
		var data = res.data;
		ParamObjectUtils.fill2Form(data, {keyValue: function(k, v){
			$("#rumtime-" + k).html(v);
		}});
	});
}

/**
 * 刷新运行时信息
 */
function refreshRuntimeMessage(){
	initRuningTimeMessage();
}

/**
 * 绑定事件
 */
function bindEvent(){
	
	var getTipDiv = function(v){
		var tipColor = (v == 1 ? 'green' : 'yellow')
		return '<div style="border-radius:50%;width:10px;height:10px;background-color:'+ tipColor 
		        +';display:inline-block;"></div>';
	}
	
	$(".state-change-listener").on('DOMNodeInserted', function(){
		var id = $(this).attr("id");
		var v = $(this).text();
		$("#new-" + id).html(getTipDiv(v));
	});
}

/**
 * 模块信息初始化失败默认内容
 */
function defaultErrorContent(){
	$("#jarBaseMessage").html('初始化数据失败...');
	$("#jarRuningMessage").html('初始化数据失败...');
}