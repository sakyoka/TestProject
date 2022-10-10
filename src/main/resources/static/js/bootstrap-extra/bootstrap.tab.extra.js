
/**
 * 扩展页签扩展操作
 * add by csy 
 */
var TabExtraOperate = function(gobalConfig){
	
	//存储id-名称
	var tabIdObject = {};
	
	//存储关闭额外事件
	var tabCloseFcRefObject = {};
	
	//存储id-对象
	var tabCacheObject = {};
	
	//tab激活执行函数
	var tabActiveFcRefObject = gobalConfig.extraAfterActive || {};
	
	/**
	 * tag 标识
	 * contenthtml 页签对象
	 */
	this.addTab = function(_obj){
		
		var tag = _obj.id;
		var tagName = _obj.tagName || '页签';
		var content = _obj.content || '';
		var onclose = _obj.onclose;
		var afterCreate = _obj.afterCreate;
		var dropdownContent = _obj.dropdownContent || '';
		var afterActive = _obj.afterActive;
		
		if (tabIdObject[tag] != undefined){
			//已存在不需要追加，进行激活
			$('.pane-parent-li').removeClass('open');
			tabCacheObject[tag].addClass('open');
			return;
		}
		
		try{
			
			//record
			tabIdObject[tag] = tagName;
			
			//extra close function
			if (onclose != undefined){
				tabCloseFcRefObject[tag] = onclose;
			}
			
			if (afterActive != undefined){
				tabActiveFcRefObject[tag] = afterActive;
			}
			
			//tag
			var tagObj = $('<li class="dropdown pane-parent-li"><a href="#'+ tag +'" data-toggle="tab">'+ tagName
					 +'<button type="button" class="close bootstrap-close-button"  target-id="'+ tag +'">×</button></a>'
					 + dropdownContent +'</li>')
			$(".tab-extra-target").append(tagObj);
			
			//tag content
			var contentObj = $('<div style="height:100%;" class="tab-pane" id="'+ tag +'">'+ content +'</div>');
			$(".tab-extra-target-content").append(contentObj);
		    
			//active tab
			this.activeTab(tag);
		    
		    //execute after create
		    if (afterCreate){
		    	afterCreate(tag, tagObj, contentObj);
		    }
		    tabCacheObject[tag] = tagObj;
		}catch(e){
			removeRef(tag);
			console.log(e);
		}
	},
		
	/**
	 * 选中
	 */
	this.activeTab = function(id){

		if ($('button[target-id='+ id +']').parent().parent().length != 1){
			//这里有bug待处理...关闭时候进来两次
			//由于在点击x时候，触发了选中及选中上一个，但是选中一个在之前触发，选中当前后触发
			//所以导致无法选中上一个，需要判断对象是否存在
			return ;
		}
		
		$(".tab-extra-target li").removeClass("active");
		$('button[target-id='+ id +']').parent().parent().addClass("active");
		
		$(".tab-extra-target-content .tab-pane").removeClass("active");
		$("#" + id).addClass("active");
		
		var activefc = tabActiveFcRefObject[id];
		if (activefc){
			activefc(id);
		}
	},
	
	/**
	 * 移除tab
	 */
	this.reomveTab = function(id){
		
		if (tabCloseFcRefObject[id] != undefined){
			//执行额外的关闭方法
			tabCloseFcRefObject[id](id);
		}
		
		//删除关联
		removeRef(id);
		
		//页签对应的内容移除
		$("#" + id).remove();
		
		//选中页签上一个
		var avtiveObject = $($('button[target-id='+ id +']').parent().parent().prev()).find('a');
		var preId = avtiveObject.attr('href').split("#")[1];
		this.activeTab(preId);
		
		//移除页签
		$('button[target-id='+ id +']').parent().parent().remove();
	}
	
	/**
	 * 获取缓存值id对象
	 */
	this.getTabIdObject = function(){
		return $.extend({}, tabIdObject);
	}
	
	//当前对象
	var item = this;
	
	/**
	 * 关闭监听方法-页签关闭
	 */
	var addTabCloseListener = function(){
		$('body').on('click', '.bootstrap-close-button', function(){
			var id = $(this).attr('target-id');
			item.reomveTab(id);
		});
	}
	
	/**
	 * 添加监听方法-页签激活之后
	 */
	var addAfterTabActiveListener = function(){
		//页签点击
		$('body').on('click', '.tab-extra-target li', function(){
			var id = $(this).find('a').attr('href').split("#")[1];
			item.activeTab(id);
		});	
	}
	
	//执行
	addTabCloseListener();
	
	//执行
	addAfterTabActiveListener();
	
	/**
	 * 移除关联
	 */
	var removeRef = function(id){
		delete tabIdObject[id];
		delete tabCloseFcRefObject[id];
		delete tabCacheObject[id];
		delete tabActiveFcRefObject[id]
	}
}