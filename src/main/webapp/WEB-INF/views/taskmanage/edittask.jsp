<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head> 
    <title>编辑任务</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="renderer" content="webkit">
    <jsp:include page="/WEB-INF/views/common/commonstatic.jsp" flush="true" />
    <script type="text/javascript" src="${root}/js/common/ParamObjectUtils.js"></script>
    <script type="text/javascript" src="${root}/js/common/ValidUtils.js"></script>
    <script type="text/javascript" src="${root}/js/common/URLBuilder.js"></script>
    <script type="text/javascript" src="${root}/components/my97datepicker/WdatePicker.js"></script>
</head>
<style>
.first-cols{ width: 120px;}
tr{height:50px;text-align: right;}
.form-control{width: 350px;}
textarea {height: 70px!important;}
</style>
<body>
    <div id="">
        <div id="content" style="height: 300px; overflow: auto;">
            <input type="hidden" name="taskId" id="taskId"  class="param-field"/>
	        <table border="0px" style="margin-top: 10px; width: 100%;">
		            <tr>
		                <td class="first-cols"><font color="red">*</font>任务名称：</td>
		                <td><input type="text" name="taskName" id="taskName" class="param-field input-class form-control" 
		                           valids="notBlank" fieldDesc="任务名称" groups="1,2,3,4"/></td>
		            </tr>
		            <tr>
		                <td class="first-cols"><font color="red">*</font>任务的key：</td>
		                <td><input type="text" name="taskKey" id="taskKey" class="param-field input-class form-control" 
		                           valids="notBlank" fieldDesc="任务的key" groups="1,2,3,4"/></td>
		            </tr>
		            <tr>
		                <td class="first-cols">任务描述：</td>
		                <td><textarea name="taskDesc" id="taskDesc" class="param-field input-class form-control"></textarea></td>
		            </tr>
		            
		            <!-- 任务时间 -->
		            <tr>
		                <td class="first-cols"><font color="red">*</font>任务调度类型：</td>
		                <td style="text-align: left;">
		                    <input type="radio" name="dispatchType" class="param-field" checked value="0"/>表达式
		                    <input type="radio" name="dispatchType" class="param-field" value="1"/>定点时间
		                </td>
		            </tr>
		            <tr class="dispatchTypeCls" dispatchType="0" style="display: none;">
		                <td class="first-cols"><font color="red">*</font>corn执行表达式：</td>
		                <td><input type="text" name="cron" id="cron" class="param-field input-class form-control" 
		                           valids="notBlank" fieldDesc="corn执行表达式" groups="1"/></td>
		            </tr>
		            <tr class="dispatchTypeCls" dispatchType="1" style="display: none;">
		                <td class="first-cols"><font color="red">*</font>指定执行时间：</td>
		                <td><input type="text" name="startTime" id="startTime" class="param-field input-class form-control Wdate" 
		                           valids="notBlank" fieldDesc="指定执行时间" groups="2" 
		                           onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" style="height: 30px;"/></td>
		            </tr>
		            <tr>
		                <td class="first-cols">延迟时间（秒）：</td>
		                <td><input type="number" name="delayTime" id="delayTime" class="param-field input-class form-control"/></td>
		            </tr>
		            <!-- 任务时间 -->
		            
		            <!-- 调度类型 -->
		            <tr>
		                <td class="first-cols"><font color="red">*</font>任务调度类型：</td>
		                <td style="text-align: left;">
		                    <input type="radio" name="taskType" class="param-field" checked value="0"/>请求url
		                    <input type="radio" name="taskType" class="param-field" value="2"/>执行类方法
		                    <!-- 1的上传jar类型需要单独页面上传 -->
		                </td>
		            </tr>
		            <tbody class="taskTypeCls" taskType="0" style="display: none;">
			            <tr>
			                <td class="first-cols"><font color="red">*</font>请求地址：</td>
			                <td><input type="text" name="requestUrl" id="requestUrl" class="param-field input-class form-control" 
			                           valids="notBlank" fieldDesc="请求地址" groups="3"/></td>
			            </tr>
			            <tr>
			                <td class="first-cols"><font color="red">*</font>请求类型：</td>
			                <td> 
				                <select id="methodType" name="methodType" class="param-field input-class form-control" 
				                    fieldDesc="请求类型" groups="3">
				                     <option></option>
				                     <option value="0">POST</option>
				                     <option value="1">GET</option>
				                </select>
				            </td>
			            </tr>
			            <tr>
			                <td class="first-cols"><font color="red">*</font>contentType：</td>
			                <td>
				                <select id="contentType" name="contentType" class="param-field input-class form-control" 
				                    fieldDesc="contentType" groups="3">
				                     <option></option>
				                     <option value="application/x-www-form-urlencoded;charset">application/x-www-form-urlencoded;charset</option>
				                     <option value="application/json">application/json</option>
				                     <option value="text/html">text/html</option>
				                </select>
				            </td>
			            </tr>
			            <tr>
			                <td class="first-cols">请求的参数：</td>
			                <td><textarea name="requestJsonParam" id="requestJsonParam" class="param-field input-class form-control" 
			                           fieldDesc="请求的参数"  title="请求的参数，以JOSN字符串传过来"></textarea></td>
			            </tr>
		            </tbody>
		            <tbody class="taskTypeCls" taskType="2" style="display: none;">
			            <tr>
			                <td class="first-cols"><font color="red">*</font>方法名：</td>
			                <td><input type="text" name="methodName" id="methodName" class="param-field input-class form-control" 
			                           valids="notBlank" fieldDesc="方法名" groups="4"/></td>
			            </tr>
			            <tr>
			                <td class="first-cols"><font color="red">*</font>实例名字：</td>
			                <td><input type="text" name="instanceName" id="instanceName" class="param-field input-class form-control" 
			                           valids="notBlank" fieldDesc="实例名字" groups="4"/></td>
			            </tr>
			            <tr>
			                <td class="first-cols"><font color="red">*</font>请求的参数：</td>
			                <td><textarea name="requestParam" id="requestParam" class="param-field input-class form-control" 
			                           fieldDesc="请求的参数" /></textarea></td>
			            </tr>
		            </tbody>
		            <!-- 调度类型 -->
	        </table>
        </div>
        <div style="text-align: center; position: absolute; bottom: 0px; width: 100%; border-top: 1px solid #eee;">
             <button class="btn btn-primary" onclick="submitData();" style="margin-top: 10px;">保存</button>
             <button class="btn btn-default" onclick="closeDialog();" style="margin-top: 10px;">关闭</button>
        </div>
    </div>
</body>
<script src="${root}/js/taskmanage/edittask.js"></script>
</html>	