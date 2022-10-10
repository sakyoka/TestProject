<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head> 
    <title>jarLog</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="renderer" content="webkit">
    <jsp:include page="/WEB-INF/views/common/commonstatic.jsp" flush="true" />
    <script type="text/javascript" src="${root}/js/common/ParamObjectUtils.js"></script>
    <script type="text/javascript" src="${root}/js/common/ValidUtils.js"></script>
    <script type="text/javascript" src="${root}/js/common/URLBuilder.js"></script>
    <!-- jquery菜单 -->
    <link rel="stylesheet" type="text/css" href="${root}/components/jquery/jquery.contextMenu.css"/>
    <script type="text/javascript" src="${root}/components/jquery/jquery.contextMenu.js"></script>
</head>
<body style="width:100%; height:100%;">
<div style="background-color:black;width:100%; height:100%; padding: 10px" id="console-parent">
    <div id="console" style="width:99%; height:95%; color:white;overflow-y: auto; overflow-x:hidden;"></div>
    <div id="cmd" style="height:30px; width:100%;">
        <span style="color: white;">输入后回车(enter)：
            <input type="text" style="width: 500px; display: inline-block; height: 27px;" class="form-control" id="cmdstr" title="keyword：help、stop、link、download、clear">
        </span>
   </div>
</div>
</body>
<script type="text/javascript" src="${root}/js/jarmanage/jarmanage-jar-console.js"></script>
<script type="text/javascript" src="${root}/js/jarmanage/jarmanage-jar-websocket.js"></script>
<script type="text/javascript" src="${root}/js/jarmanage/jarmanage-jar-request-url.js"></script>
<script type="text/javascript" src="${root}/components/socketjs/sockjs.js"></script>
<script type="text/javascript" src="${root}/components/socketjs/stomp.js"></script>
<script type="text/javascript" src="${root}/js/jarmanage/jarmanage-jar-websocket-stomp.js"></script>
<script type="text/javascript" src="${root}/js/jarmanage/jarmanage-jar-console-log.js"></script>
<script></script>
</html>	