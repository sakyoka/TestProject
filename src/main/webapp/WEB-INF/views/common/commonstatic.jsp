<%@ page contentType="text/html; charset=UTF-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath", basePath);
request.setAttribute("root", path);
request.setAttribute("ip", request.getLocalAddr());
request.setAttribute("port", request.getLocalPort());
%>
<script type="text/javascript">
    var root = "${pageContext.request.contextPath}";//项目名称
    var ip = "${ip}";
    var port = "${port}";
</script>
<link rel="stylesheet" type="text/css" href="${root}/css/common/common.css"/>
<link rel="stylesheet" type="text/css" href="${root}/components/bootstrap-3.4.1-dist/css/bootstrap.min.css"/>
<script type="text/javascript" src="${root}/components/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${root}/components/bootstrap-3.4.1-dist/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${root}/js/common/StringBuilder.js"></script>
<script type="text/javascript" src="${root}/js/bootstrap-extra/bootstrap.model.extra.js"></script>
<script type="text/javascript" src="${root}/js/common/ApiParams.js"></script>
<script type="text/javascript" src="${root}/js/common/request.js"></script>
<script type="text/javascript" src="${root}/js/common/StringUtils.js"></script>
<script type="text/javascript" src="${root}/js/common/jquery.download.js"></script>
<script type="text/javascript" src="${root}/js/bootstrap-extra/bootstrap-table-extra.js"></script>
<script type="text/javascript">
function onBeforeSend(xhr){
	var token = localStorage.getItem("token");
	xhr.setRequestHeader('token', StringUtils.isBlank(token) ? '' : token);	
}

function onComplete(xhr){
	//更新token
	var token = xhr.getResponseHeader("token_exprie");
	if (StringUtils.isNotBlank(token)){
		localStorage.setItem("token", token);
	}	
}

function onError(xhr){
	var status = xhr.status;
	if (status === 401){
		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '登录失效，跳转到登录页面', buttons:[{
			name:'确定',
			isClose: true,
			onclose: function(){
				location.href = root + "/jarmanage/login";
			}
		}], hiddenClose: true});
		return ;
	}
	
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
    AlertMessgaeUtils.alert({id:'tip', title:'提示', content: msg  + ''});	
}

$(function(){
	//全局设置beforeSend、complete、error
	$(document).ajaxSend(function(event, xhr) {
		onBeforeSend(xhr);
	});
	
	$(document).ajaxComplete(function (e, xhr, settings) {
		onComplete(xhr);
	});
	
	$(document).ajaxError(function (e, xhr, settings) {
		onError(xhr);
	});
});
</script>