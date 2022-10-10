$(function(){
	
	var urlObject = new URLBuilder(location.href);
	var ref = urlObject.get('ref');
	parent.setContentHeightWidth(ref);
	
	$.bootstrapTable({
		id: 'taskTable',
		queryParams:{},
		data: function(pageObject, queryParams){
			var data = {};
			reqeustCommonApi({
				async: false,
				data: new ApiParams('taskManage', 'taskPageData')
			              .params('pageObject', pageObject)
			              .params('paramObject', queryParams)
			              .build(),
				success: function(res){
					data = res;
				}
			});
			return data;
		},
		checkBox: true,
		columns:[{title: '任务名称', fieldName: 'taskName'},
		         {title: '任务ID', fieldName: 'taskId', 
			       	 formatter: function(v, data){
			       		 return '<p style="word-wrap:break-word">'+ v +'</p>'
			     }},
		         {title: '任务key', fieldName: 'taskKey', 
			       	 formatter: function(v, data){
			       		 return '<p style="word-wrap:break-word">'+ v +'</p>'
			     }},
		         {title: '调度计划', fieldName: 'dispatchType', 
		        	 formatter: function(v, data){
			        	 switch (v){
			        	 case 0: return data.corn; break;
			        	 case 1: return data.startTime; break;
		        	     default: return '未知状态值';break;
		        	 }
		         }},
		         {title: '执行状态', fieldName: 'executeStatus', formatter: function(v){
		        	 switch (v){
		        	     case 0: return '停止(未执行)'; break;
		        	     case 1: return '执行中'; break;
		        	     case 2: return '执行失败'; break;
		        	     default: return '未知状态值';break;
		        	 }
		         }},
		         {title: '修改时间', fieldName: 'updateTime'},
		         {title: '任务描述', fieldName: 'taskDesc'},
		         {title: '是否可用', fieldName: 'enable', formatter: function(v){
		        	 return v == 1 ? '是' : '否';
		         }},
		         {title: '操作', fieldName: 'op', formatter: function(v){
		        	 return '';
		         }}],
		buttons: [{
			name: '编辑任务',
			handle: function(){
				AlertMessgaeUtils.alert({
					target: $(top.document.body),
					id:'editTask', 
					title:'编辑任务',
					url: root + '/view/taskmanage/edittask',
				    height: '450px'	
				});
			}
		},{
			name: '移除任务',
			handle: function(){
				
			}
		}]
	});
});