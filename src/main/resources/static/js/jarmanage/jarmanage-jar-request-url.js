var JarUrl = {
		/**
		 * 列表接口
		 */
		getJarList: function(){
			return root + "/api/jarmanage/list";
		},
		
		/**
		 * 获取对应jar信息
		 */
		getOneJar: function(){
			return root + "/api/jarmanage/get";
		},
		
		/**
		 * 添加jar页面地址
		 */
	    getAddJar: function(){
			return root + "/view/jarmanage/jaraddpage";
		},
		
		/**
		 * 移除jar地址
		 */
		getRemoveJar: function(){
			return root + "/api/jarmanage/rermovejar";
		},
		
		/**
		 * 启动jar地址
		 */
		getRunJar: function(){
			return root + "/api/jarmanage/runjar";
		},
		
		/**
		 * 重启jar地址
		 */
		getReloadJar: function(){
			return root + "/api/jarmanage/reloadjar";
		},
		
		/**
		 * 停止jar地址
		 */
		getStopJar: function(){
			return root + "/api/jarmanage/stopjar";
		},
		
		/**
		 * 保存jar信息地址
		 */
		getSaveJar: function(){
			return root + "/api/jarmanage/addjar";
		},
		
		/**
		 * jar文件上传地址
		 */
		getFileUpload: function(){
			return root + "/api/jarmanage/file/upload";
		},

		/**
		 * jar文件删除地址
		 */
		getFileDelete: function(){
			return root + "/api/jarmanage/file/delete";
		},
		
		/**
		 * 下载日志地址
		 */
		getDownloadLog: function(){
			return root + "/api/jarmanage/download";
		},
		
		/**
		 * 下载地址
		 */
		getDownload: function(){
			return root + "/api/jarmanage/download";
		},
		
		/**
		 * jar日志显示地址
		 */
		getJarLogConsole: function(){
			return root + "/view/jarmanage/jarlogconsole";
		},
		
		/**
		 * 全局配配置地址
		 */
		getGobalConfig: function(){
			return root + "/view/jarmanage/jargobalconfigpage";
		},
		
		/**
		 * 全局配置数据保存地址
		 */
		getGobalConfigSave: function(){
			return root + "/api/jarmanage/gobalconfig/save";
		},
		
		/**
		 * 全局配置数据地址
		 */
		getGobalConfigData: function(){
			return root + "/api/jarmanage/gobalconfig/get";
		},
		
		/**
		 * 获取本系统运行相关信息
		 */
		getSystemMessage: function(){
			return root + "/api/jarmanage/system";
		},
		
		/**
		 * 获取应用相关信息
		 */
		getApplicationMessage: function(){
			return root + "/api/jarmanage/application";
		},
		
		/**
		 * jar详细页面
		 */
		getJarDetailPage:function(){
			return root + "/view/jarmanage/jardetailpage";
		},
		
		/**
		 * 修复AllJarEntity配置
		 */
		getRepeatAllJarEntity: function(){
			return root + "/api/jarmanage/repeat";
		},
		
		/**
		 * 导出系统配置（jar root 目录下全部文件）
		 */
		getExportJarConfig: function(){
			return root + "/api/jarmanage/exportconfig";
		},
		
		/**
		 * 导入系统配置（jar root 目录下全部文件）
		 */
		getImportJarConfig: function(){
			return root + "/api/jarmanage/importconfig";
		},
		
		/**
		 * 清除系统垃圾
		 */
		getClearSystemRubbish: function(){
			return root + "/api/jarmanage/clearsystemrubbish";
		},
		
		/**
		 * 清空日志文件内容
		 */
		getEmptyLog: function(){
			return root + "/api/jarmanage/emptylog";
		},
		
		/**
		 * 退出登录接口
		 */
		getLoginout: function(){
			return root + "/jarmanage/loginout";
		},
		
		/**
		 * 导出jar基础数据
		 */
		getExportJarBaseData: function(){
			return root + "/api/jarmanage/exportbasedata";
		}
}