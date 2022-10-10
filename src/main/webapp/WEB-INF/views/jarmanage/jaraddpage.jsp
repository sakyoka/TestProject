<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head> 
    <title>jar添加页面</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="renderer" content="webkit">
    <jsp:include page="/WEB-INF/views/common/commonstatic.jsp" flush="true" />
    <script type="text/javascript" src="${root}/js/common/ParamObjectUtils.js"></script>
    <script type="text/javascript" src="${root}/js/common/ValidUtils.js"></script>
    <link rel="stylesheet" type="text/css" href="${root}/components/bootstrap-3.4.1-dist/css/fileinput.min.css"/>
    <script type="text/javascript" src="${root}/components/bootstrap-3.4.1-dist/js/fileinput.min.js"></script>
    <script type="text/javascript" src="${root}/components/bootstrap-3.4.1-dist/js/zh.min.js"></script>
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
	height: 97%;
	overflow-y: auto;
}
input,textarea,select{
    width: 95% !important;
}
#leftTablleDiv{
    float: left;
    width: 70%;
    height: 100%;
}
#rightTablleDiv{
    float: right;
    width: 28%;
    height: 100%;
}
#rightTableDiv td{
    height: 45px;
}
</style>
</head>
<body>
    <div id="fisrt-div">
        <input type="hidden" name="cacheJarEnName" id="cacheJarEnName" valids="needUploadFile" class="param-field"/>
        <input type="hidden" name="jarId" id="jarId" class="param-field"/>
        <input type="hidden" name="dirUuid" id="dirUuid"  class="param-field"/>
        <input type="hidden" name="sufixName" id="sufixName"  class="param-field"/>
        <div id="leftTablleDiv">
	        <table border="0px" style="margin-top: 10px; width: 100%;">
	            <tbody>
		            <tr>
		                <td class="first-cols"><font color="red">*</font>jar项目中文名字：</td>
		                <td><input type="text" name="jarChName" id="jarChName" class="param-field input-class form-control" 
		                           valids="notBlank" fieldDesc="jar项目中文名字"/></td>
		            </tr>
		            <tr>
		                <td class="first-cols"><font color="red">*</font>jar项目英文名字：</td>
		                <td><input type="text" name="jarEnName" id="jarEnName" class="param-field input-class form-control" 
		                           fieldDesc="jar项目英文名字" readonly="readonly"/></td>
		            </tr>
		            <tr>
		                <td class="first-cols">jar运行时JDK：</td>
		                <td><input type="text" name="jarJdkManage.jdkPath" id="jdkPath" class="param-field input-class form-control"/></td>
		            </tr>
		            <tr style="display: none;" id="jvmtr">
		                <td class="first-cols">JVM参数配置：</td>
		                <td><textarea name="jarJdkManage.jvm" id="jvm" class="param-field input-class form-control" style="height: 120px;"></textarea></td>
		            </tr>
		            <tr>
		                <td class="first-cols">排序号：</td>
		                <td><input type="number" name="orderNumber" id="orderNumber" class="param-field input-class form-control"/></td>
		            </tr>
		            <tr>
		                <td class="first-cols">jar描述：</td>
		                <td><textarea name="jarDesc" id="jarDesc" class="param-field input-class form-control" style="height: 120px;"></textarea></td>
		            </tr>
		            <tr>
		                <td class="first-cols" style="height:100px;">上传jar：</td>
		                <td><input type="file" id="jarFile" name="file" class="file-loading" accept=".jar,.war"/></td>
		            </tr>
	 	            <tr class="spingboot-properties-select" style="display: none;">
		                <td class="first-cols">配置文件：</td>
		                <td><select id="propertiesType" name="propertiesType" class="form-control param-field">
		                        <option value="0">无</option>
		                        <option value="1">bootstrap.yml</option>
		                        <option value="2">bootstrap.properties</option>
		                        <option value="3">application.yml</option>
		                        <option value="4">application.properties</option>
		                    </select>
		                </td>
		            </tr>
		            <tr class="spingboot-properties-select" style="display: none;">
		                <td class="first-cols">配置文件内容：</td>
		                <td><textarea name="propertiesContent" id="propertiesContent" 
		                    class="param-field input-class form-control" style="height: 650px;resize:none; overflow: auto;" wrap="off"></textarea>
		                </td>
		            </tr>
	            </tbody>
	            <tfoot>
		            <tr>
		                <td colspan="2" style="text-align: center;">
		                    <button class="btn btn-primary" onclick="submitData();" style="margin-top: 10px;">提交</button>
		                </td>
		            </tr>
	            </tfoot>
	        </table>
        </div>
        <div id="rightTableDiv">
	        <table border="0px" style="margin-top: 10px;">
	            <tbody>
		            <tr>
		                <td>可选按钮：</td>
		                <td><button class="btn btn-primary" id="addPropertiesButton">添加文件属性配置</button></td>
		            </tr>
		            <tr>
		                <td></td>
		                <td><button class="btn btn-primary" id="addJVMParamButton">添加JVM配置参数</button></td>
		            </tr>
	            </tbody>
	        </table>
        </div>
    </div>
</body>
<script src="${root}/js/jarmanage/jarmanage-jar-request-url.js"></script>
<script src="${root}/js/jarmanage/jarmanage-jar-add-message.js"></script>
<script></script>
</html>	