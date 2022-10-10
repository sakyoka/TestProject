<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head> 
    <title>用户列表页面</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="renderer" content="webkit">
    <jsp:include page="/WEB-INF/views/common/commonstatic.jsp" flush="true" />
    <script type="text/javascript" src="${root}/js/common/URLBuilder.js"></script>
</head>
<body>
    <div id="userTable"></div>
</body>
<script type="text/javascript">
$(function(){
	
	var urlObject = new URLBuilder(location.href);
	var ref = urlObject.get('ref');
	parent.setContentHeightWidth(ref);
	
	$.bootstrapTable({
		id: 'userTable',
		queryParams:{},
		data: function(pageObject, queryParams){
			var data = {};
			reqeustCommonApi({
				async: false,
				data: new ApiParams('user', 'userPageData')
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
		showIndex: true,
		columns:[{title: '用户中文名', fieldName: 'userChName'},
		         {title: '用户英文名', fieldName: 'userEnName'},
		         {title: '账号', fieldName: 'userName'},
		         {title: '是否可用', fieldName: 'valid', formatter: function(v){
		        	 return v == 1 ? '是' : '否';
		         }}],
		buttons: []
	});
});
</script>
</html>	