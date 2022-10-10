<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head> 
    <title>jar详细信息页面</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="renderer" content="webkit">
    <jsp:include page="/WEB-INF/views/common/commonstatic.jsp" flush="true" />
    <script type="text/javascript" src="${root}/js/common/URLBuilder.js"></script>
    <script type="text/javascript" src="${root}/js/common/ParamObjectUtils.js"></script>
<style>
#jarBaseMessage{
	border: 1px solid #eee;
	border-radius: 5px;
	background-color: white;
	margin: 10px;
	padding: 10px;
	height: 26%;
	overflow: auto;
}
#jarRuningMessage{
	border: 1px solid #eee;
	border-radius: 5px;
	background-color: white;
	margin: 10px;
	padding: 10px;
	height: 65%;
}
.main-title{
    font-weight: bold;
    font-size: 18px;
}
.start-div{
    height: 100%;
    width:100%;
}

td{height: 27px;}

.long-td{
    word-break: break-all;
}

.title-name{text-align: right;}

#baseTable{
    table-layout: fixed;
    width: 100%;
}

#baseTable .basetable-name{
    width: 100px;
}
</style>
</head>
<body>
<div class="start-div">
	<div id="jarBaseMessage">
	    <table id="baseTable">
	        <tr>
	            <td class="title-name basetable-name">项目编码：</td>
	            <td id="jarId" class="long-td"></td>
	            <td class="title-name basetable-name">项目中文名：</td>
	            <td id="jarChName" class="long-td"></td>
	            <td class="title-name basetable-name">项目英文名：</td>
	            <td id="jarEnName" class="long-td"></td>
	        </tr>
	        <tr>
	            <td class="title-name">日志路径：</td>
	            <td id="logPath"></td>
	            <td class="title-name">实例路径：</td>
	            <td id="jarPath"></td>
	            <td class="title-name">随机目录：</td>
	            <td id="dirUuid"></td>
	        </tr>
	        <tr>
	            <td class="title-name">是否运行：</td>
	            <td><span id="isRuning" class="state-change-listener" style="display: none;"></span><span id="new-isRuning"></span></td>
	            <td class="title-name">是否正常：</td>
	            <td><span id="isAlive" class="state-change-listener" style="display: none;"></span><span id="new-isAlive"></span></td>
	            <td class="title-name">进程号：</td>
	            <td id="pid"></td>
	        </tr>
	    </table>
	</div>
	<div id="jarRuningMessage">
        <div id="detailRuntimeMessage" style="height: 100%;">
            <div style="height: 55%; overflow: auto;">
                <div style="float: right;"><a href="javascript:refreshRuntimeMessage();" id="refreshRuntimeMessage">refresh</a></div>
	            <table>
			        <tr>
			            <td colspan="4" style="text-align: left;">
						    <span class="title-name">jmx ip：</span>
					        <span id="jarRefJmxRmiIpPortBean-ip" class="long-td"></span>&nbsp;&nbsp;
					        <span class="title-name">jmx port：</span>
					        <span id="jarRefJmxRmiIpPortBean-port" class="long-td"></span>&nbsp;&nbsp;
					        
						    <span class="title-name">服务器 ip：</span>
					        <span id="rumtime-ip" class="long-td"></span>&nbsp;&nbsp;
					        <span class="title-name">服务器 port：</span>
					        <span id="rumtime-port" class="long-td"></span>
			            </td>
			        </tr> 
			        <tr>
			            <td class="title-name">堆内存信息：</td>
			            <td colspan="3" id="rumtime-heapMemoryUsage"></td>
			        </tr>
			        <tr>
			            <td class="title-name">非堆内存信息：</td>
			            <td colspan="3" id="rumtime-nonHeapMemoryUsage"></td>
	
			        </tr>
			        <tr>
			            <td colspan="4" style="text-align: center;" class="main-title">线程信息</td>
			        </tr> 
			        <tr>
			            <td class="title-name">线程数：</td>
			            <td id="rumtime-threadCount"></td>
			            <td class="title-name">巅峰线程数：</td>
			            <td id="rumtime-peakThreadCount"></td>
			        </tr>
			        <tr>
			            <td class="title-name">历史线程总数：</td>
			            <td id="rumtime-totalStartedThreadCount"></td>
			            <td class="title-name">活动守护线程数：</td>
			            <td id="rumtime-daemonThreadCount"></td>
			        </tr> 
			        <tr>
			            <td colspan="4" style="text-align: center;" class="main-title">线程详细信息</td>
			        </tr> 
	            </table>
            </div>
            <div style="overflow: auto; height: 45%;">
	            <table>
			        <tr>
			            <td colspan="4" style="text-align: left;" id="rumtime-runtimeThreads"></td>
			        </tr>    
	            </table>
            </div>
        </div>
	</div>
</div>
</body>
<script src="${root}/js/jarmanage/jarmanage-jar-request-url.js"></script>
<script src="${root}/js/jarmanage/jarmanage-jar-detail-message.js"></script>
</html>	