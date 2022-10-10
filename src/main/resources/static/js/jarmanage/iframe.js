//iframe id 前缀
var PREFIX = "iframe-";
//统一生成-调用方法
var Iframe = {
		
		/**
		 * iframe加载
		 */
		load: function(id, fc){
    		this.getIframe(id).load(function(){
    			if (fc){
    				fc(this, id);
    			}
    		});			
		},
		
		/**
		 * 获取iframe对象
		 */
		getIframe: function(id){
			return $("#" + PREFIX + id);
		},
		
		/**
		 * 获取contents
		 */
		getContents: function(id){
			return this.getIframe(id).contents();
		},
		
		/**
		 * 获取body
		 */
		getBody: function(id){
			return this.getContents(id).find('body'); 
		},
		
		/**
		 * 获取parent-console
		 */
		getConsoleParent: function(id){
			return this.getBody(id).find('#parent-console');
		},
		
		/**
		 * 获取console
		 */
		getConsole: function(id){
			return this.getConsoleParent(id).find('#console');
		},
		
		/**
		 * 统一iframe模板
		 * @returns
		 */
		createTemplate: function(id, url){
			return '<iframe src="'+ url +'" id="'+ PREFIX + id +'" style="width:100%;height:97%;" frameborder="0" scrolling="no"></iframe>';
		},
		
		/**
		 * 刷新iframe
		 */
		refresh: function(id){
			this.getIframe(id).attr("src", this.getIframe(id).attr("src"));
		},
		
		/**
		 * 刷新iframe，如果存在的话
		 */
		refreshIfExists: function(id){
			var iframeObject = this.getIframe(id);
			if (iframeObject != undefined){
				iframeObject.attr("src", iframeObject.attr("src"));
			}
		}
}