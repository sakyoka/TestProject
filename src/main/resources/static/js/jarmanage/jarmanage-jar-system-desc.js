/**
 * 定时器
 */
var autoRefreshStatisticsInterval;
var autoRefreshOption = {
		
		/**
		 * 定时刷新
		 */
		autoRefreshStatisticsMessage: function(){
			autoRefreshStatisticsInterval = setInterval(function(){
				autoRefreshOption.refreshStatistics();
			}, 1000 * 30);//30s秒一次			
		},
		
		/**
		 * 清除定时任务
		 */
		clearAutoRefreshStatisticsInterval: function(){
			if (autoRefreshStatisticsInterval != undefined){
				clearInterval(autoRefreshStatisticsInterval);
			}			
		},
		
		/**
		 * 刷新统计数据
		 */
		refreshStatistics: function(){
			var url = JarUrl.getApplicationMessage();
			try{
				$.get(url, {}, function(res){
					var data = res.data;
					if (data){
						var applicationPids = data['applicationPids'];
						var stringBuilder = new StringBuilder();
						if (applicationPids != undefined && applicationPids.length > 0){
							var pidJarManageMap = data['pidJarManageMap'];
							$.each(applicationPids, function(){
								var _this = pidJarManageMap[this];
								if (_this){
									stringBuilder.append('<a href="#check-'+ _this.jarId +'" jarId="'+ _this.jarId +'" title="'+_this.jarChName+'详情"')
						             .append('class="tab-extra-origin" page-url="'+ JarUrl.getJarDetailPage() +'">').append(this).append('</a>').append(',');	
								}else{
									stringBuilder.append(this).append(',');
								}								
							});
						}
						var applicationPidsStr = stringBuilder.toString();
						applicationPidsStr = applicationPidsStr != '' && applicationPidsStr != undefined 
						                             ? applicationPidsStr.substring(0, applicationPidsStr.length - 1) : ''; 
						data['applicationPids'] = applicationPidsStr;
						for (var k in data){
							$("#fill-value-" + k).html(data[k] + "");
						}
					}
				}).error(function(){
					$("#allApplicationMessage").html('模块数据初始化失败.');
				});
			}catch(e){$("#allApplicationMessage").html('模块数据处理失败.');}			
		},
		
		/**
		 * 取消或设置
		 */
		changeSetting: function(){
			var autoRefreshStatistics = $.cookie('autoRefreshStatistics');
			autoRefreshStatistics = (autoRefreshStatistics == "" ? 1 : autoRefreshStatistics);
			$.cookie('autoRefreshStatistics', autoRefreshStatistics == 1 ? 0 : 1);
			SystemDesc.initAutoRefreshStatistics();
		}
}

var SystemDesc = {
		
		/**
		 * 初始化本系统运行信息
		 */
		initSystemMessage: function(){
			try{
				var jarId = "system";
				var iframeTemplete = Iframe.createTemplate(jarId, JarUrl.getJarLogConsole() + "?ref=" + jarId);
				$("#systemMessage").html(iframeTemplete);	
			}catch (e){
				$("#systemMessage").html('模块数据处理失败.');
			}
			return this;
		},
		
		/**
		 * 初始化管理应用基本运行情况
		 */
		initAllApplicationMessage: function(){
			
			autoRefreshOption.refreshStatistics();

			var controlMessageDivStyle = function(){
				var showOrHide = $.cookie("allApplicationMessage");
				if ($("#allApplicationMessageControl").is('.in') && showOrHide == 'hide'){
					$("#allApplicationMessageControl").toggleClass("in");
					$("#systemMessage").css({height:'80%'});
					$(".control-showhide").text('show');
				}
				//调整iframe内容高度
				setContentHeightWidthForSystemConsole();
			}
			
			controlMessageDivStyle();
			return this;
		},
		
		/**
		 * 初始化是否定时刷新
		 */
		initAutoRefreshStatistics: function(){
			var autoRefreshStatistics = $.cookie('autoRefreshStatistics');
			//是否动刷新0否1是
			autoRefreshStatistics = (autoRefreshStatistics == "" ? 1 : autoRefreshStatistics);
			if (autoRefreshStatistics == 1){
				$("#autoRefreshStatistics").text('stop auto refresh');
				autoRefreshOption.autoRefreshStatisticsMessage();
			}else{
				$("#autoRefreshStatistics").text('auto refresh');
				autoRefreshOption.clearAutoRefreshStatisticsInterval();
			}
			
			window.onbeforeunload = function(){
				autoRefreshOption.clearAutoRefreshStatisticsInterval();
			}	
			return this;
		},
		
		/**
		 * 绑定事件
		 */
		bindEvent: function(){
		    //系统运行情况div
			$(".control-showhide").click(function(){
				var id = $(this).attr("href").split("#")[1];
				$("#" + id).toggleClass("in");
				if ($("#" + id).is('.in')){
					$(this).text('hide');
					$.cookie("allApplicationMessage", 'show');
					$("#systemMessage").css({height:'45%'});
				}else{
					$.cookie("allApplicationMessage", 'hide');
					$(this).text('show');
					$("#systemMessage").css({height:'80%'});
				}
				
				//调整iframe内容高度
				setContentHeightWidthForSystemConsole();
			});
			return this;
		}
}