<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head> 
    <title>jar管理界面</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="renderer" content="webkit">
    <jsp:include page="/WEB-INF/views/common/commonstatic.jsp" flush="true" />
    <link rel="stylesheet" type="text/css" href="${root}/css/jarmanage/jarmanage.css"/>
    <script type="text/javascript" src="${root}/js/bootstrap-extra/bootstrap.tab.extra.js"></script>
    <script type="text/javascript" src="${root}/components/jquery/jquery.cookie.js"></script>
</head>
<body>
    <div>
         <div id="topContainer" class="bg-red">
             <div>
                 <div id="topLeft"><span id="systemTag">JarManage<span></div>
                 <div id="topRight">
                     <div id="system-login">
                         <span id="login-user"><span class="glyphicon glyphicon-user" id="loginUser"></span></span>
                         <span id="loginout"><a href="javascript:void(0);" class="glyphicon glyphicon-log-out" title="退出登录"></a></span>
                     </div>
                 </div>
            </div>
         </div>
         <div id="middleContainer" style="width: auto;">
             <div id="middleLeft" class="middleDiv">
				<div class="container" style="width:100%;">
				    <div class="row">
				        <div class="span8">
				            <div id="projectList">
				                <div style="font-size: 20px;">
                                    <a href="javascript:void(0);" class="glyphicon glyphicon-play" title="启动应用" onclick="JarManage.stratSelectedJar();" id="startJarId"></a>
				                    <a href="javascript:void(0);" class="glyphicon glyphicon-stop" title="停止应用" onclick="JarManage.stopSelectedJar();" id="stopJarId"></a>
				                    <a href="#jar-manage-add-button" class="glyphicon glyphicon-plus tab-extra-origin" title="添加项目" 
				                       page-url="${root}/view/jarmanage/jaraddpage"></a>
					                <a href="javascript:void(0);" class="glyphicon glyphicon-minus" title="移除项目" onclick="JarManage.removeSelectedJar();" id="removeJarId"></a>
				                    <a href="#jar-manage-gobalconfig-button" class="glyphicon glyphicon-cog tab-extra-origin" title="JAR全局参数配置" 
				                       page-url="${root}/view/jarmanage/jargobalconfigpage"></a>
				                    <a href="javascript:void(0);" class="glyphicon glyphicon-repeat" title="修复JAR配置数据（仅当列表数据不对，谨慎操作）" 
				                            onclick="JarManage.repeatJarData();" id="repeatJarData"></a>
				                    <a href="#jar-manage-systemoperate-button" class="glyphicon glyphicon-th tab-extra-origin" title="其它操作" 
				                       page-url="${root}/view/jarmanage/systemoperate"></a>
				                </div>
				            </div>
				            <ul class="nav nav-tabs nav-stacked" id="tab-extra-origin-p">
				            </ul>
				        </div>
				    </div>
				</div> 
             </div>
             <div id="middleRight" class="middleDiv">
                 <div id="middleRightTab">
					<ul class="nav nav-tabs tab-extra-target"> 
					    <li>
					        <a href="javascript:void(0);" class="glyphicon" title="收缩" id="drawBackLeftMenu" ></a>
					    </li>
					    <li class="active">
					        <a href="#jarDesc" data-toggle="tab">系统说明<button target-id="jarDesc" hidden="true"></button></a>
					    </li>	    	    
					</ul> 
                 </div>
				 <div class="tab-content tab-extra-target-content" style="height:90%;">
				     <div class="tab-pane active" id="jarDesc" style="height:100%;">
				         <div id="descTitle"><span>JarManage相关运行情况<span></div>
				         <div id="systemMessage"></div>
					     <a href="#allApplicationMessageControl" class="control-showhide">hide</a>
					     <div id="allApplicationMessageControl" class="collapse in">
					         <div id="allApplicationMessage">
					             <div style="float: right;"><a href="javascript:autoRefreshOption.changeSetting();" id="autoRefreshStatistics">auto refresh</a></div>
					             <div class="font-style padding5">存在相关java进程 ，pid：<span id="fill-value-javaProcessPids"></span></div>
					             <div class="font-style padding5">本机启动过的进程 ，pid：<span id="fill-value-applicationPids"></span></div>
					             <div class="font-style padding5">其中共应用：<span id="fill-value-total"></span>，没有启动：<span id="fill-value-nonStart"></span>， 
					             运行着：<span id="fill-value-runing"></span>，已经停掉的：<span id="fill-value-unruning"></span></div>
					         </div>
						 </div>
				     </div>
				 </div>
             </div>
         </div>
         <div id="buttomContainer"></div>
    </div>
</body>

<!-- 基础start -->
<!-- iframe公共构建、调用方法 -->
<script src="${root}/js/jarmanage/iframe.js"></script>
<!-- 请求地址定义 -->
<script src="${root}/js/jarmanage/jarmanage-jar-request-url.js"></script>
<!-- console信息构建 -->
<script src="${root}/js/jarmanage/jarmanage-jar-console.js"></script>
<!-- 左侧导航-页签关联 -->
<script src="${root}/js/jarmanage/jarmanage-nav-ref-tab.js"></script>
<!-- jar基础操作方法 -->
<script src="${root}/js/jarmanage/jarmanage-jar-op.js"></script>
<!-- 系统说明模块 -->
<script src="${root}/js/jarmanage/jarmanage-jar-system-desc.js"></script>
<!-- 基础end -->

<!-- 初始化 start  -->
<script src="${root}/js/jarmanage/jarmanage-init.js"></script>
<!-- 初始化 end -->
</html>	