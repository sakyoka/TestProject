//jarId对应的JarConsole对象
var JarConsoleRecordObject = {};
var JarConsole = function(){
	
	var consoleStr = "";
	
	var id = "";
	
	var consoleEleObj;
	
	var autoClearRef = false;
	
	var consoleParams = {};
	
    /** 
     * 生成对应控制台div字符串 
    */
    this.createConsoleDivStr = function(id){
	    var historyObject = JarConsoleRecordObject[id];
        if (historyObject != undefined){
	        return historyObject.getConsoleStr();
        }
	    JarConsoleRecordObject[id] = this;
        id = id;
        consoleStr = '<div style="width:99%;height:99%;background-color:black;" id="console-'+ id +'"></div>';
        consoleEleObj = $(consoleStr);
        return consoleStr;
    }
    
    /**
     * 加载元素
     */
    this.load = function(idOrEle){
    	var ele = (typeof(idOrEle) == 'string' ? $('#' + idOrEle): idOrEle);
    	var id = $(ele).attr('id');
	    var historyObject = JarConsoleRecordObject[id];
        if (historyObject != undefined){
	        return historyObject;
        }   	
    	consoleStr = $(ele).html();
    	consoleEleObj = $(ele);
    	JarConsoleRecordObject[id] = this;
    	return this;
    }
    
    /**
     * 填充字符
     */
    this.fill = function(str, extraParams){
    	
    	if (str == undefined || str == '' || consoleEleObj == undefined){
    		return ;
    	}
    	extraParams = extraParams || {};
    	for (var k in extraParams){
	        consoleParams[k] = extraParams[k];
        }
    	var splitStr = consoleParams.splitStr || '\n';
    	var contents = str.split(splitStr);
    	if (contents.length == 0){
    		return ;
    	}
    	
    	var index = 0;
    	var timeoutObj ;
    	var time = consoleParams.time || 100;
    	var allowShowMaxRows = consoleParams.allowShowMaxRows || 200;
        var setTimeoutFillContent = function(){
        	
        	//控制控制台最大展示行数，以免文本内容过大
        	var length = consoleEleObj.children().length;
        	if (length > allowShowMaxRows){
	            consoleEleObj.children().eq(0).remove();
            }
        	
        	consoleEleObj.append('<p>'+ contents[index] +'</p>');
        	index += 1;
        	timeoutObj = setTimeout(function(){setTimeoutFillContent();}, time);
        	
        	if (contents.length <= index){
        		clearTimeout(timeoutObj);
        		return ;
        	}
        }
    	
        setTimeoutFillContent();
        
    	if (autoClearRef === true){
    		this.autoClearRef();
    	}
    	
    	return this;
    }
    
    /**
     * 是否自动清除
     */
    this.autoClearRef = function(clear){
    	autoClearRef = clear;
    	return this;
    }
    
    /**
     * 清空内容
     */
    this.clear = function(){
    	consoleEleObj.empty();
    }
    
    /**
     * 获取对应控制台div字符串
    */
    this.getConsoleStr = function(){
	    return consoleStr;
    }
    
    /**
     * 清除关联
     */
    this.clearRef = function(){
	    //移除其它 
      
        //清除关联
	    delete JarConsoleRecordObject[id];
    }
}