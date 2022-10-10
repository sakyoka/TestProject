<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head> 
    <title>jarmanage login page</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="renderer" content="webkit">
    <jsp:include page="/WEB-INF/views/common/commonstatic.jsp" flush="true" />
</head>
<body>
    <div id="fisrt-div" style="width: 100%; height: 100%; text-align: center; padding: 10%;">
        <div id="loginDiv" style="width: 800px; height: 400px; margin: auto; text-align: center; border: 1px solid #eee; border-radius: 5px; background-color: white; padding: 90px;">
            <div style="height: 20%; font-weight: bolder; font-size: 21px; font-family: NSimSun;">login</div>
            <div style="height: 70%;">
		        <table border="0px" style="margin: auto;">
		            <tbody>
			            <tr style="height: 40px;">
			                <td class="first-cols"><font color="red">*</font>账号：</td>
			                <td><input type="text" name="username" id="username" class="param-field input-class form-control" 
			                           valids="notBlank" fieldDesc="账号名称"/></td>
			            </tr>
				        <tr style="height: 40px;">
			                <td class="first-cols"><font color="red">*</font>密码：</td>
			                <td><input type="password" name="password" id="password" class="param-field input-class form-control" 
			                           valids="notBlank" fieldDesc="密码"/></td>
			            </tr>
		            </tbody>
		            <tfoot>
			            <tr>
			                <td colspan="2" style="text-align: center;">
			                    <button class="btn btn-primary" onclick="login();" style="margin-top: 10px;">登录</button>
			                    <button class="btn btn-default" onclick="reset();" style="margin-top: 10px;">重置</button>
			                </td>
			            </tr>
		            </tfoot>
		        </table>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript" src="${root}/js/common/ParamObjectUtils.js"></script>
<script type="text/javascript" src="${root}/js/common/ValidUtils.js"></script>
<script type="text/javascript" src="${root}/js/common/URLBuilder.js"></script>
<script type="text/javascript" src="${root}/components/md5/md5.js"></script>
<script>
function login(){
	//password 加密？
	var loginUrl = root + "/jarmanage/login";
	var validResult = ParamObjectUtils.valid();
	if (validResult.result === false){
		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: validResult.msg});
		return ;
	}
	var paramObject = validResult.data
	paramObject.password = hex_md5(paramObject.password);
	$.ajax({
	    url: loginUrl,
	    type: "post",
	    data: paramObject,
	    success: function(res) {
	    	if (res.code == 200){
	    		var token = res.data;
	    		localStorage.setItem("token", token);
	    		localStorage.setItem("userCode", $("#username").val());
	    		location.href = root + "/view/jarmanage/main";
	    	}else{
	    		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: "return msg：" + res.msg});
	    	}
	    },
	    error: function(xhr, ts, et){
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
		    AlertMessgaeUtils.alert({id:'tip', title:'提示', content: 'msg:' + msg});
	    }
	});	
}
</script>
</html>	