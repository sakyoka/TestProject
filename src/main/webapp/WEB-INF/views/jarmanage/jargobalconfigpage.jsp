<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head> 
    <title>全局配置页面</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="renderer" content="webkit">
    <jsp:include page="/WEB-INF/views/common/commonstatic.jsp" flush="true" />
    <script type="text/javascript" src="${root}/js/common/ParamObjectUtils.js"></script>
    <script type="text/javascript" src="${root}/js/common/URLBuilder.js"></script>
<style>
.first-cols{
    height:55px;
    text-align: right;
}
.input-class{
   width: 400px;
}
#fisrt-div{
	border: 1px solid #eee;
	border-radius: 5px;
	background-color: white;
	margin: 10px;
	padding: 10px;
	height: 95%;
}
</style>
</head>
<body>
    <div id="fisrt-div">
        <table border="0px" style="margin:auto;margin-top: 50px;">
            <tbody>
	            <tr>
	                <td class="first-cols">全局运行时JDK路径：</td>
	                <td colspan="3"><input type="text" name="jdkPath" id="jdkPath" class="param-field input-class form-control"/></td>
	            </tr>
	            <tr>
	                <td class="first-cols">系统重启自动启动应用：</td>
	                <td><input type="radio" name="autoRestart" id="autoRestart" class="param-field" value="1"/>是 
	                    <input type="radio" name="autoRestart" id="autoRestart" class="param-field" value="0" checked="checked"/>否
	                </td>
	                <td class="first-cols">系统关闭自动停止应用：</td>
	                <td><input type="radio" name="autoStop" id="autoStop" class="param-field" value="1"/>是 
	                    <input type="radio" name="autoStop" id="autoStop" class="param-field" value="0" checked="checked"/>否
	                </td>
	            </tr>
	            <tr>
	                <td class="first-cols"><span title="删除的是cacheUpload内容">自动清理缓存目录：</span></td>
	                <td><input type="radio" name="autoClearCache" id="autoClearCache" class="param-field" value="1"/>是 
	                    <input type="radio" name="autoClearCache" id="autoClearCache" class="param-field" value="0" checked="checked"/>否
	                </td>
	                <td class="first-cols">
	                    <span title="删除的内容是jarStorage、jarLog下删除失败的内容">自动清理垃圾jar及相关(谨慎配置)：</span>
	                </td>
	                <td><input type="radio" name="autoClearNoAvailJar" id="autoClearNoAvailJar" class="param-field" value="1"/>是 
	                    <input type="radio" name="autoClearNoAvailJar" id="autoClearNoAvailJar" class="param-field" value="0" checked="checked"/>否
	                </td>
	            </tr>
            </tbody>
            <tfoot>
	            <tr>
	                <td colspan="4" style="text-align: center;">
	                    <button class="btn btn-primary" onclick="submitData();" style="margin-top: 30px;">提交</button>
	                </td>
	            </tr>
            </tfoot>
        </table>
    </div>
</body>
<script src="${root}/js/jarmanage/jarmanage-jar-request-url.js"></script>
<script>
//保存配置
function submitData(){
	var url = JarUrl.getGobalConfigSave();
	var paramObject = ParamObjectUtils.getParamObject();
 	$.post(url, paramObject, function(res){
		if (res.code == '200'){
			AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '保存成功'});
		}else{
			AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '保存失败,错误信息:' + res.msg});
		}
	});
}
//初始化页面数据
function initData(){
	var url = JarUrl.getGobalConfigData();
	$.get(url, {}, function(res){
    	if (res.code != '200'){
    		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '初始化失败页面数据失败,' + res.msg});
    		return ;
    	}
    	var data = res.data;
    	if (data){
    		ParamObjectUtils.fill2Form(data);
    	}		
	});
}

$(function(){
	
	var urlObject = new URLBuilder(location.href);
	var ref = urlObject.get('ref');
	parent.setContentHeightWidth(ref);

	initData();
});
</script>
</html>	