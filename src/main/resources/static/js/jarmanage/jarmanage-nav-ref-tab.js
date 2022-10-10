var TabRefInit = {
		
		/**
		 * 绑定事件-主要事件左侧菜单点击
		 */
		bindLeftMenuClickEvent: function(){
						
			//定义全局页面对象
			var tabExtraOperate = new TabExtraOperate({extraAfterActive: {jarDesc: function(id){
				//因为有些不是tabExtraOperate生成的tab没有对应的激活之后事件，扩展追加额外的事件extraAfterActive集合
				//优先执行tabExtraOperate的afterActive 没有就执行extraAfterActive，afterActive覆盖extraAfterActive
				setContentHeightWidthForSystemConsole();
			}}});
			
			//.tab-extra-origin点击事件，不限于左侧菜单点击
		    $("body").on('click', '.tab-extra-origin',function(){

		    	controlJarSelected(this);
		    	
		    	controlJarChecked(this);
		    	
		    	//判断点击对象是否符合设计所需
		    	var refTarget = ($(this).attr("href")).split("#")[1];
		    	if (!refTarget){
			        AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '获取操作源信息失败，请联管理员'});
		    		throw '获取操作源失败';
		    	}
		    	
		    	//判断页签时候存在了
		    	var tabExists = (tabExtraOperate.getTabIdObject())[refTarget];

		    	if (tabExists != undefined){
		    		//存在直接选中对应页签
		    		tabExtraOperate.activeTab(refTarget);
		    	}else{
		    		//不存在生成一个新的
		    		var orginName = $(this).attr('title');
		    		var jarConsole = new JarConsole();
		    		var pageUrl = $(this).attr('page-url');
		    		var needJarOp = $(this).attr('need-jar-op');
		    		var content = "";
		    		//判断是否是用iframe还是直接生成控制台页面
		    		if (pageUrl != undefined && pageUrl != undefined){
		    			pageUrl += "?ref=" + refTarget;
			            content = Iframe.createTemplate(refTarget, pageUrl);
			        }else{
				        content = jarConsole.createConsoleDivStr(refTarget);
			        }
		    		//添加页签
		    		tabExtraOperate.addTab({id: refTarget, tagName:orginName, content: content, onclose:function(id){
		        	
		    			    //清除记录
		        			jarConsole.clearRef();
		        			
		        			//...other
		    		}, afterCreate: function(id, tagObj){
		    			
		    			//加载完毕之后绑定事件
		    			Iframe.load(id, function(_this){
		    				TabRefInit.outSideMenuBindCloseEvent(Iframe.getBody(id));
		    			});
		        		
		        		//追加属性
		        		tagObj.find('button').attr('need-jar-op', needJarOp);
		        		
		        	}, afterActive: function(id){
		        		//因为随着窗口改动，其它窗口保持着100 * 100
		        		//所以点击时候点击改变
		        		setContentHeightWidth(id);
		        	}, dropdownContent:'<ul class="dropdown-menu"></ul>'});
		    	}
		    });	
		    
			/**
			 * 控制显示当前导航
			 * @param _this
			 */
			var controlJarSelected = function(_this){
				//移除选中对象
				$(_this).parent().parent().find('li').removeClass('active');
				//移除二级菜单
				$(_this).parent().parent().find('li').removeClass('open');
				//选中对象
				$(_this).parent().addClass('active');
				//展示二级菜单
				$(_this).parent().addClass('open');	
			}

			/**
			 * 控制jar点击勾选
			 * @param _this
			 */
			var controlJarChecked = function(_this){
				var checked = $(_this).find("input[name=jarCheck]").is(':checked');
				if (checked === true){
					$(_this).find("input[name=jarCheck]").prop('checked', false);
				}else{
					$(_this).find("input[name=jarCheck]").prop('checked', true);
				}	
			}

			/**
			 * 随着窗口变化，重置iframe里面宽高
			 * @param tabExtraOperate
			 */
			var addWindowResizeListenerForIframe = function(tabExtraOperate){
			    $(window).resize(function(){
			    	windowResizeForIframe(tabExtraOperate);
			    });
			}
			
			var windowResizeForIframe = function(tabExtraOperate){
		    	var cacheObject = tabExtraOperate.getTabIdObject();
		    	for (var k in cacheObject){
		    		//失败也没什么，iframe里面内容不会随着变化而已
		    		try{setContentHeightWidth(k);}catch(e){};
		    	}
		    	
		    	setContentHeightWidthForSystemConsole();				
			}
		    
		    /**
		     * 控制左边菜单收缩的图标
		     * <br>cookie中
		     * <br>leftMenuDrawBackStatus == 1是展开
		     * <br>leftMenuDrawBackStatus == 0是收缩
		     * @param ele
		     */
			$("#drawBackLeftMenu").click(function(){
		    	if ($(this).closest('.glyphicon-resize-full').length == 1){
		    		$(this).removeClass('glyphicon-resize-full');
		    		$(this).addClass('glyphicon-resize-small');
		    		$.cookie('leftMenuDrawBackStatus', 0);
		    	}else{
		    		$(this).removeClass('glyphicon-resize-small');
		    		$(this).addClass('glyphicon-resize-full');
		    		$.cookie('leftMenuDrawBackStatus', 1);
		    	}
		    	
		    	resizeLeftMenu();
			});

		    /**
		     * 控制左侧菜单大小
		     */
		    var resizeLeftMenu = function(){
		    	
		    	var status = $.cookie('leftMenuDrawBackStatus');
		    	status = (status === '' || status === undefined ? 1 : status);
		    	var ele = $('#drawBackLeftMenu');
		    	if (status == 0){
		    		//隐藏
		    		$("#middleLeft").css({display: 'none'});
		    		$("#middleRight").css({width: '99%'});
		    		$(ele).addClass('glyphicon-resize-small');
		    	}else{
		    		//展开
		    		$("#middleLeft").css({display: ''});
		    		//左边菜单宽度大小
		    		var middleLeft = $("#middleLeft").width();
		    		//可视文档宽度.
		    		var windowWidth = $('body').width();
		    		//计算出右边占整个宽度的比率
		    		var middleRightWiathRate = 1 - middleLeft/(windowWidth - 40);
		    		$("#middleRight").css({width: (middleRightWiathRate * 100) + '%'});
		    		$(ele).addClass('glyphicon-resize-full');
		    	}
		    	
		    	windowResizeForIframe(tabExtraOperate);
		    }
		    
		    //初始化左侧菜单大小
		    resizeLeftMenu();
		    //添加窗口监听，iframe窗口内容大小
		    addWindowResizeListenerForIframe(tabExtraOperate);
		    
		    $(window).resize(function(){
		    	resizeLeftMenu();
		    });
		    
		    return this;
		},
		
		/**
		 * 二级菜单不在范围之类，左右键点击时闭关菜单
		 * @param ele 绑定对象
		 */
		outSideMenuBindCloseEvent: function(eleObj){
			
			var commonDeal = function(e){
		        
				if($(e.target).closest('.tab-dropdrown-li').length == 0){
					//左边菜单关闭
		        	$('.tab-dropdrown-li').removeClass('open');
		        }
		         
		        if($(e.target).closest('.pane-parent-li').length == 0){
		        	//页签菜单关闭
		        	$('.pane-parent-li').removeClass('open');
		        } 		
			}
			
			eleObj.bind({
				
		    	/**
		    	 * 左键点击事件
		    	 */
				click:function(e){
					commonDeal(e);
		    	},
		    	
		    	/**
		    	 * 右键菜单事件
		    	 */
		    	contextmenu:function(e){
		    		commonDeal(e);  		
		    	}
		    });
			return this;
		}
}

/**
 * 子页面调设置子页面宽高
 * @param jarId
 */
function setContentHeightWidth(jarId){
	var iframeObject = Iframe.getIframe(jarId);
	var height = iframeObject.height();
	var width = iframeObject.width();
	Iframe.getBody(jarId).height(height);
	Iframe.getBody(jarId).width(width);
}

/**
 * 调整系统控制台宽高
 */
function setContentHeightWidthForSystemConsole(){
	//系统说明不在tab范围之类
	try{setContentHeightWidth('system');}catch(e){};	
}