<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head> 
    <title>系统操作页面</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="renderer" content="webkit">
    <jsp:include page="/WEB-INF/views/common/commonstatic.jsp" flush="true" />
    <script type="text/javascript" src="${root}/js/common/ParamObjectUtils.js"></script>
    <script type="text/javascript" src="${root}/js/common/URLBuilder.js"></script>
    <script type="text/javascript" src="${root}/components/jquery/jquery.cookie.js"></script>
<style>
.first-cols{
    height:55px;
    text-align: right;
    width: 150px;
}
.input-class{
   width: 400px;
}
.common-table{

}
.moduke-div{
	border: 1px solid #eee;
	border-radius: 5px;
	background-color: white;
	margin: 10px;
	padding: 10px;
	min-height: 60px;
	max-height: 70px;
}

#other-div{
	border: 1px solid #eee;
	border-radius: 5px;
	background-color: white;
	margin: 10px;
	padding: 10px;
}
.block-title{
    float: right;
    font-weight: bold;
    font-size: 18px;
}
.common-button{
    min-width: 120px;
}
</style>
</head>
<body>
    <div class="moduke-div">
        <span class="block-title">JAR配置管理模块</span>
        <table border="0px" class="common-table">
            <tbody>
	            <tr>
	                <td class="first-cols"><button class="btn btn-primary common-button" onclick="buttonOption.exportJarConfig();">导出配置</button></td>
	                <td class="first-cols"><button class="btn btn-primary common-button" onclick="buttonOption.importJarConfig();">导入配置</button></td>
	            </tr>
            </tbody>
        </table>
    </div>
    <div class="moduke-div">
        <span class="block-title">缓存模块</span>
        <table border="0px" class="common-table">
            <tbody>
	            <tr>
	                <td class="first-cols"><button class="btn btn-primary common-button" onclick="buttonOption.clearCookie();">清除cookie</button></td>
	                <td class="first-cols"><button class="btn btn-primary common-button" onclick="buttonOption.clearSystemRubbish();">清除系统垃圾</button></td>
	            </tr>
            </tbody>
        </table>
    </div>
    <div class="moduke-div">
        <span class="block-title">基础数据操作模块</span>
        <table border="0px" class="common-table">
            <tbody>
	            <tr>
	                <td class="first-cols"><button class="btn btn-primary common-button" onclick="buttonOption.exportJarBaseData();">导出JAR数据</button></td>
	                <td class="first-cols"><button class="btn btn-primary common-button" onclick="buttonOption.usereManage();">用户管理</button></td>
	            </tr>
            </tbody>
        </table>
    </div>
    <div class="moduke-div">
        <span class="block-title">任务管理模块</span>
        <table border="0px" class="common-table">
            <tbody>
	            <tr>
	                <td class="first-cols"><button class="btn btn-primary common-button" onclick="buttonOption.taskManage();">任务管理</button></td>
	            </tr>
            </tbody>
        </table>
    </div>
    <div id="other-div">
        <span class="block-title">其它操作模块</span>
        <table border="0px" class="common-table">
            <tbody>
	            <tr>
	                <td>待处理...</td>
	            </tr>
            </tbody>
        </table>
    </div>
</body>
<script src="${root}/js/jarmanage/jarmanage-jar-request-url.js"></script>
<script src="${root}/js/jarmanage/jarmanage-systemoperate.js"></script>
<script></script>
</html>	