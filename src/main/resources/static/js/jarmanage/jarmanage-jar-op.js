/**
 * jar 管理操作 
*/
var JarManage = {
	
		/*
		 * 获取jar列表数据
		*/
		init: function(){
			
			var url = JarUrl.getJarList();
			var _this = this;
			
			var getShortName = function(str){
				if (str.length > 9){
					str = str.substring(0, 9) + "...";
				}
				return str;
			}
			
			$.get(url, {}, function(res){
				if (res.code != 200){
					alert("获取初始化数据失败");
					return;
				}
				//tab-extra-origin
				var datas = res.data;
				var stringBuilder = new StringBuilder();
				//先清空一次
				$("#tab-extra-origin-p").html('');
				$.each(datas, function(index){
					var i = index + 1;
					stringBuilder.clear();
					stringBuilder.append('<li class="dropdown tab-dropdrown-li" id="jar-nav-'+ this.jarId +'" index="'+ i +'" jarId="'+ this.jarId +'">')
							     .append('<a data-toggle="dropdown" href="#').append(this.jarId)
					             .append('" class="tab-ref tab-extra-origin bottom-up" need-jar-op="true" need-clear-log="true"')
					             .append('jarId="').append(this.jarId).append('" title="').append(this.jarChName +'" page-url="')
					             .append(JarUrl.getJarLogConsole()).append('">')
					             .append('<input type="checkbox" name="jarCheck" jarId="'+ this.jarId +'" />').append('      ')
					             .append(i).append("、").append('<div style="display:inline-block;" id="jar-tip-'+ this.jarId +'">')
					                                    .append(JarButton.jarTipElementString(this)).append('</div>').append('    ').append(getShortName(this.jarChName))
					             .append('</a>')
					                      .append('<ul class="dropdown-menu">')
					                      .append(JarButton.jarButtonString(this, true))
					                      .append('</ul>')
					             .append('</li>');
					$("#tab-extra-origin-p").append(stringBuilder.toString());
				});
			});
		},
		
		/**
		 * 运行jar
		 */
		runJar: function(jarIds){
			requestGetMethod(JarUrl.getRunJar(), {jarIds: jarIds}, '启动应用', function(res){
				commonRunAndStopSuccess(res, '启动应用', 1, function(){
					var jarIdArr = jarIds.split(",");
					$.each(jarIdArr, function(){
						try{
							var jarConsole = new JarConsole().autoClearRef(true).load(Iframe.getConsole(this));
							jarConsole.fill(res.data);
						}catch(e){
							//失败就算，就是没有打开对日志应控制台窗口
						}
					});
				});
			}, false);
		},
		
		/**
		 * 重启jar
		 */
		reloadJar: function(jarId){
			requestMethod(JarUrl.getReloadJar(), {jarId: jarId}, '重启应用', function(res){
				if (res.code == 200){
					JarManage.refreshJarTip({jarId:jarId, isRuning: 1, isAlive: 1});
				}
				//刷新iframe内容
				Iframe.refreshIfExists("check-" + jarId);
			});
		},
		
		/**
		 * 停止运行
		 */
		stopJar: function(jarId){
			requestGetMethod(JarUrl.getStopJar(), {jarIds: jarId}, '停止应用', function(res){
				commonRunAndStopSuccess(res, '停止应用', 0);
			}, false);
		},
		
		/**
		 * 启动勾选则应用（已启动的跳过）
		 */
		stratSelectedJar: function(){
			var jarIds = this.getCheckJarIds();
			if (jarIds.length == 0){
				AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '请先勾选项目，再点击启动'});
				return ;
			}
			var jarIdstr = jarIds.join(',');
			this.runJar(jarIdstr);
		},
		
		/**
		 * 停止勾选启动的应用（已停止的跳过）
		 */
		stopSelectedJar: function(){
			var jarIds = this.getCheckJarIds();
			if (jarIds.length == 0){
				AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '请先勾选项目，再点击停止'});
				return ;
			}
			var jarIdstr = jarIds.join(',');
			this.stopJar(jarIdstr);
		},
		
		/**
		 * 移除勾选的jar（已启动的会先执行停止再执行移除）
		 */
		removeSelectedJar: function(){
			var jarIds = this.getCheckJarIds();
			if (jarIds.length == 0){
				AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '请先勾选项目，再点击移除'});
				return ;
			}
			var jarIdstr = jarIds.join(',');
			this.removeJar(jarIdstr);
		},
		
		/**
		 * 移除jar
		 */
		removeJar: function(jarId){
			var jarNumber = (jarId.split(",")).length;
			requestMethod(JarUrl.getRemoveJar(), {jarIds: jarId}, '移除('+ jarNumber+')个jar配置', function(){
				location.reload();
			}, 'post');
		},
		
		/**
		 * 下载日志
		 */
		downloadLog: function(jarId){
			$.download({
				url: JarUrl.getDownloadLog() + "?jarId=" + jarId ,
				headers:{'token': localStorage.getItem("token")}
			});
		},
		
		/**
		 * 获取jar列表勾选的jarId集合
		 */
		getCheckJarIds: function(){
			var jarIdArr = [];
			$.each($("input[name=jarCheck]:checked"), function(){
				var jarid = $(this).attr("jarId");
				jarIdArr.push(jarid);
			});
			return jarIdArr;
		},
		
		/**
		 * 什么时候刷新菜单 --> 导航左右键点击时候
		 */
		refreshJarNavElementOnClick: function(jarId){
			var url = JarUrl.getOneJar();
			var _this = this;
		    $.ajax({
		        url: url,
		        dataType: 'json',
		        type: 'get',
		        async:false,
		        data:{jarId: jarId},
		        success:function(res){
					if (res.code == 200){
						var ulObj = $('#jar-nav-' + jarId).find('ul');
						ulObj.html('');
						ulObj.append(JarButton.jarButtonString(res.data, true));
					}	        	
		        }
		    });	
		},
		
		/**
		 * 页签右键添加额外元素
		 */
		refreshPaneParentLiAddExtraElements(_this){
			
			var jarId = $(_this).find('button').attr('target-id');
			//这个值在tb -> afterCreate 赋予
			var needJarOp = $(_this).find('button').attr('need-jar-op');
			var needClearLog = $(_this).find('button').attr('need-clear-log');
			
			var ulObj = $(_this).find('ul');
			ulObj.html('');
			
			//是否需要jar操作
			if (needJarOp == 'true'){
			    $.ajax({
			        url: JarUrl.getOneJar(),
			        dataType: 'json',
			        type: 'get',
			        async:false,
			        data:{jarId: jarId},
			        success:function(res){
						if (res.code == 200){
							ulObj.prepend(JarButton.jarButtonString(res.data));
							ulObj.append('<li><a href="#" class="pane-sub-dropdown-downloadlog" jarId="'+ jarId +'">下载日志</a></li>');
						}	        	
			        }
			    });
			}
			
			//是否需要清空
			if (needClearLog == 'true'){
				ulObj.append('<li><a href="#" class="pane-sub-dropdown-clearlog" jarId="'+ jarId +'">清空日志</a></li>');
			}
			
			//默认每个页签都有
			ulObj.append('<li><a href="#" class="pane-sub-dropdown-refresh" jarId="'+ jarId +'">刷新当前</a></li>');	
			ulObj.append('<li><a href="#" class="pane-sub-dropdown-close" jarId="'+ jarId +'">关闭当前</a></li>');	
			ulObj.append('<li><a href="#" class="pane-sub-dropdown-closeall" jarId="'+ jarId +'">关闭全部</a></li>');	 
		},
		
		/**
		 * 刷新jar提示灯
		 */
		refreshJarTip: function(jarObject){
			var jarId = jarObject.jarId;
			$("#jar-tip-" + jarId).html('');
			$("#jar-tip-" + jarId).html($(JarButton.jarTipElementString(jarObject)));
		},
		
		/**
		 * 修复JAR配置数据
		 * （收集jarStorage目录下未删除的jar配置(entity.json)，重新组装成总配置allJarEntity）
		 */
		repeatJarData: function(){
			requestMethod(JarUrl.getRepeatAllJarEntity(), {}, '修复数据', function(){
				location.reload();
			});			
		}
}

var JarButton = {
		
		/**
		 * 拼接对应jar操作按钮
		 * @param _this
		 * @returns
		 */
		jarButtonString: function(_this, needSelected){
			var buttonBuilder = new StringBuilder();
			if (needSelected == true){
				buttonBuilder.append('<li><a href="javascript:void(0);" class="check-all-jar">全选/取消</a></li>');
			}
			buttonBuilder.append('<li><a href="#edit-'+ _this.jarId +'" jarId="'+ _this.jarId +'" title="'+_this.jarChName+'编辑"')
                         .append('class="tab-extra-origin" page-url="'+ JarUrl.getAddJar() +'">编辑</a></li>');
			//如果是启动了
			if (_this.isRuning === 1){
				buttonBuilder.append('<li><a href="javascript:void(0);" jarId="'+ _this.jarId +'" class="stop-jar">停止</a></li>')
				             .append('<li><a href="javascript:void(0);" jarId="'+ _this.jarId +'" class="reload-jar">重启</a></li>');
			}else{
				buttonBuilder.append('<li><a href="javascript:void(0);" jarId="'+ _this.jarId +'" class="run-jar">启动</a></li>')
				             .append('<li><a href="javascript:void(0);" jarId="'+ _this.jarId +'" class="remove-jar">移除</a></li>');
			}
			buttonBuilder.append('<li><a href="#check-'+ _this.jarId +'" jarId="'+ _this.jarId +'" title="'+_this.jarChName+'详情"')
                         .append('class="tab-extra-origin" page-url="'+ JarUrl.getJarDetailPage() +'">查看详情</a></li>')
			return buttonBuilder.toString();			
		},
		
		/**
		 * 提示灯
		 */
		jarTipElementString: function(jarObject){
			var tipColor = '';
			var tipName = '';
			if (jarObject.isRuning == 1){
				tipColor = 'green';
				tipName = '运行中';
				if (jarObject.isAlive == 0){
					tipColor = 'red';
					tipName = '应用服务不正常';
				}
			}else{
				tipColor = 'yellow';
				tipName = '未启动';
			}
			return '<div style="border-radius:50%;width:10px;height:10px;background-color:'+ tipColor 
			        +';display:inline-block;" title="'+ tipName +'"></div>';
		},		
}

/**
 * 处理启动和停止jar success函数
 * @param res 
 * @param opName
 * @param refreshJarStatus
 * @param failExtra
 */
function commonRunAndStopSuccess(res, opName, refreshJarStatus, failExtra){
	if (res.code == 200){
		var data = res.data;
		var successNum = 0;
		var total = 0;
		for (var k in data){
			total += 1;
			var runResult = data[k];
			if (runResult === true){
				successNum += 1;
				//这要刷新状态
				JarManage.refreshJarTip({jarId: k, isRuning: refreshJarStatus, isAlive: refreshJarStatus});
				//刷新iframe内容
				Iframe.refreshIfExists("check-" + k);
			}
		}
		
		if (successNum != total){
			//存在失败
			if (total == 1){
				AlertMessgaeUtils.alert({id:'tip', title:'提示', content: opName + '失败！'});
				return ;
			}
			AlertMessgaeUtils.alert({id:'tip', title:'提示', content: opName + '失败，本次一共执行：'+ total 
				+ '个应用，其中成功：' + successNum + '个，失败:'+ (total - successNum) + '个，请查看日志或控制台检查'});
		}else{
			AlertMessgaeUtils.alert({id:'tip', title:'提示', content: opName + '成功！'});
		}
	}else{
		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: opName + '失败，msg:' + res.msg});
		if (failExtra){
			failExtra();
		}
	}	
}