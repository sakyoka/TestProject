$(function(){
	
	JarManage.init();
	
	styleAdjustOfInit();
	
	setTimeout(function(){bindEvent();});
	
	//二级菜单不在范围之类，左右键点击时闭关菜单
	TabRefInit.outSideMenuBindCloseEvent($(document))
	          .bindLeftMenuClickEvent();
    
	SystemDesc.initSystemMessage()
			  .initAllApplicationMessage()
			  .initAutoRefreshStatistics()
			  .bindEvent();
			  
    $("#loginUser").html(localStorage.getItem("userCode"));
});

/**
 * 绑定事件
 */
function bindEvent(){
  
	//左侧菜单右键事件
	$('body').on('contextmenu click', '.tab-dropdrown-li', function(e){
		e.preventDefault();
		$('.tab-dropdrown-li').removeClass('open');
		var jarId = $(this).attr('jarId');
		if (jarId != undefined && jarId != ''){
			JarManage.refreshJarNavElementOnClick(jarId);
		}
		$(this).addClass('open');
	});
	
	//页签右键事件
	$('body').on('contextmenu', '.pane-parent-li', function(e){
		e.preventDefault();
		$('.pane-parent-li').removeClass('open');
		JarManage.refreshPaneParentLiAddExtraElements(this);
	    $(this).addClass('open');
	});
	
	//页签左击事件
	$('body').on('click', '.pane-parent-li', function(e){
		var href = $(this).find('a').attr("href");
		if (href != ""){
			var jarId = href.split("#")[1];
			$(".tab-dropdrown-li").removeClass("active");
			$("#jar-nav-" + jarId).addClass("active");
		}
	});
	
	//页签
	//点击选项隐藏页签,另外如果触发新页签激活新页签
	$('body').on('click', '.pane-parent-li .dropdown-menu a', function(e){
		//阻止事项冒泡到父元素
		e.stopPropagation();
		var href = $(this).attr('href');
		var currentTab = $(this).parent().parent().parent();
		if (href !== undefined){
			var tag = href.split("#")[1];
			var pretabs = $(this).parent().parent().parent().prevAll();
			var nexttabs = $(this).parent().parent().parent().nextAll();
			var checkTabExistsAndSelect = function(tabs, tag){
				if (tabs.length == 0 || tag == undefined){
					return ;
				}
				var exists = false;
				$.each(tabs, function(){
					var object = $(this).find('a[data-toggle=tab][href=#'+ tag +']');
					if (object != undefined && object.length == 1){
						exists = true;
						//选中下一个tab
						$(this).addClass('active');
						//移除当前tab选中
						currentTab.removeClass('active');
						return ;
					}
				});
				return exists;
			}
			//优先往后找，再往前找
			if (!checkTabExistsAndSelect(nexttabs, tag)){
				checkTabExistsAndSelect(pretabs, tag);
			}
		}
		//取消tab的菜单打开
		currentTab.removeClass('open');
	});
		
	//菜单全选点击事件
	$('body').on('click', '.check-all-jar', function(){
		var inputs = $('input[name=jarCheck]');
		var inputChecks = $('input[name=jarCheck]:checked');
		if (inputs.length > inputChecks.length){
			//全选
			$('input[name=jarCheck]').prop('checked', true);
		}else{
			//取消全选
			$('input[name=jarCheck]').prop('checked', false);
		}
	});
	
	//左侧菜单-停止jar
	$('body').on('click', '.stop-jar', function(){
		JarManage.stopJar($(this).attr('jarId'));
	});
	
	//左侧菜单-移除jar
	$('body').on('click', '.remove-jar', function(){
		JarManage.removeJar($(this).attr('jarId'));
	});
	
	//左侧菜单-重启jar
	$('body').on('click', '.reload-jar', function(){
		JarManage.reloadJar($(this).attr('jarId'));
	});
	
	//左侧菜单-启动jar
	$('body').on('click', '.run-jar', function(){
		JarManage.runJar($(this).attr('jarId'));
	});

	//页签-关闭
	$('body').on('click', '.pane-sub-dropdown-close', function(){
		$('button[target-id='+ $(this).attr('jarId') +']').click();
	});
	
	//关闭全部
	$('body').on('click', '.pane-sub-dropdown-closeall', function(){
		$('.bootstrap-close-button').click();
	});
	
	//页签下载-日志
	$('body').on('click', '.pane-sub-dropdown-downloadlog', function(){
		JarManage.downloadLog($(this).attr('jarId'));
	});
	
	//清除内容
	$('body').on('click', '.pane-sub-dropdown-clearlog', function(){
		Iframe.getConsole($(this).attr('jarId')).clear();
	});
	
	//刷新页签
	$('body').on('click', '.pane-sub-dropdown-refresh', function(){
		Iframe.refresh($(this).attr('jarId'));
	});
	
	//退出登录
	$("#loginout").click(function(){
		requestMethod(JarUrl.getLoginout(), {}, "退出登录", function(res){
			if (res.code == 200){
				location.href = root + "/jarmanage/login";
			}else{
				AlertMessgaeUtils.alert({id:'loginoutTips', title:'提示', content: '退出失败,msg:'+ res.msg});
			}
		}, 'post');
	});
}

/**
 * 重新调整一些样式
 */
function styleAdjustOfInit(){
    $("#topLeft").css({"line-height": $("#topContainer").height() + "px"});
}