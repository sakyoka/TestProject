$(function(){
	var urlObject = new URLBuilder(location.href);
	var ref = urlObject.get('ref');
	//调整页面
	parent.setContentHeightWidth(ref);
	
	addEvent();
});

/**
 * button操作
 */
var buttonOption = {
		
		/**
		 * 导出JAR配置
		 */
		exportJarConfig: function(){
			AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '确定导出系统文件吗?', buttons:[{
				name:'确定',
				isClose: true,
				onclose: function(){
					$.download({
						url: JarUrl.getExportJarConfig(),
						headers:{'token': localStorage.getItem("token")}
					});
				}
			},{
				name:'取消',
				isClose: true
			}]});
		},
		
		/**
		 * 导入JAR配置
		 */
		importJarConfig: function(){
			var importForm = $("#importForm");
            if (importForm.length == 0){
	            importForm = $('<form id="importForm" enctype="multipart/form-data"></form>');
	            importForm.append('<input type="file" name="file" id="importConfigFile" accept=".rar,.zip" style="display:none;"/>');
	            $('body').append(importForm);
            }
			$("#importConfigFile").click();
		},
		
		/**
		 * 清除cookie
		 */
		clearCookie: function(){
			AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '确定要清除cookie吗?', buttons:[{
				name:'确定',
				isClose: true,
				onclose: function(){
					var cookies = document.cookie ? document.cookie.split("; ") : [];
					$.each(cookies, function(){
						var keyValues = this.split("=");
						var k = keyValues[0];
						if (k != ''){
							$.removeCookie(k);
						}
					});
					parent.location.reload();
				}
			},{
				name:'取消',
				isClose: true
			}]});
		},
		
		/**
		 * 清除系统垃圾
		 */
		clearSystemRubbish: function(){
			requestPostMethod(JarUrl.getClearSystemRubbish(), {}, '清除系统垃圾', function(){});		
		},
		
		/**
		 * 导出JAR基础数据
		 */
		exportJarBaseData: function(){
			AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '确定导出JAR基础吗?', buttons:[{
				name:'确定',
				isClose: true,
				onclose: function(){
					$.download({
						url: JarUrl.getExportJarBaseData()
					});
				}
			},{
				name:'取消',
				isClose: true
			}]});			
		},
		
		/**
		 * 用户管理
		 */
		usereManage: function(){
			createTempTab({
				pageUrl: root + "/view/users/userlist",
				title: "用户管理列表",
				hrefName: "userlist-button"
			});
		},
		
		/**
		 * 任务管理
		 */
		taskManage: function(){
			createTempTab({
				pageUrl: root + "/view/taskmanage/tasklist",
				title: "任务管理列表",
				hrefName: "tasklist-button"
			});			
		}
}

function createTempTab(config){
	config = config || {};
	var pageUrl = config.pageUrl;
	var title = config.title;
	var hrefName = config.hrefName;
	var id = "temp-uuid-" + new Date().getTime();
	var tempObject = $('<a id='+ id +' href="#'+ hrefName +'" class="tab-extra-origin" page-url='
			+ pageUrl +' title='+ title +' style="display:none;"></a>');
	$(parent.document.body).append(tempObject);
	$(parent.document.body).find("[id^="+ id +"]")[0].click();
	$(parent.document.body).find("[id^="+ id +"]")[0].remove();	
}

function addEvent(){
	bindUploadConifgFileEvent();
}

function bindUploadConifgFileEvent(){
	$("body").on("change", '#importConfigFile', function(){
	    $.ajax({
	        url: JarUrl.getImportJarConfig(),
	        type: 'POST',
	        cache: false,
	        data: new FormData($('#importForm')[0]),
	        processData: false,
	        contentType: false,
	        dataType:"json",
	        beforeSend: function(){
		        AlertMessgaeUtils.alert({id:'runWaiting', title:'提示', content: '正在导入中，请等待...', hiddenClose: true});
	        },
	        success: function(res) {
		        AlertMessgaeUtils.hide('runWaiting');
		        if (res.code == 200){
			         AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '导入成功.'});
		        }else{
			         AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '导入失败，msg:' + res.msg});
		        }
		        parent.JarManage.init();
	        },
	        complete: function(){
		        $("#importForm").remove();
		        AlertMessgaeUtils.hide('runWaiting');
	        },
		    error: function(xhr, ts, et){
			    $("#importForm").remove();
		    	var status = xhr.status;
		    	var msg = '';
		    	switch (status){
		    	    case 400: msg = '请求参数存在错误'; break;
		    	    case 401: msg = '请求地址没有权限'; break;
		    	    case 403: msg = '请求地址没有权限'; break;
		    	    case 404: msg = '未知请求地址'; break;
		    	    case 500: msg = '内部服务器处理异常'; break;
		    	    default: msg = '请求失败'; break;
		    	}
			    AlertMessgaeUtils.alert({id:'tip', title:'提示', content: msg + '，导入失败。'});
		    }
	    });
	});	
}