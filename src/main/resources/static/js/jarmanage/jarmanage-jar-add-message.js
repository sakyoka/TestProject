var urlObject = new URLBuilder(location.href);
var ref = urlObject.get('ref');
var refs = ref.split('-');
var jarId = refs.length > 2 ? '' : refs[1];
//添加上传附件校验
ValidUtils.needUploadFile = function(v){
	if (v == ""){
		return ValidUtils.resultObject(false, "请上传文件");
	}
	return ValidUtils.OK_OBEJCT();
}

/**
 * 添加信息
 */
function submitData(){
	var url = JarUrl.getSaveJar();
	var jarEnName = $("#jarEnName").val();
	var validResult = ParamObjectUtils.valid();
	if (validResult.result === false){
		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: validResult.msg});
		return ;
	}
	var paramObject = validResult.data;
 	$.post(url, paramObject, function(res){
		if (res.code == '200'){
			$("#jarId").val(res.data);
			AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '保存成功', onclose: function(){
				if (jarId == ""){
					//初始保存需要在当前url加载jarId
					location.href = urlObject.add('ref', 'edit-' + res.data).toString();
				}else{
					//已经存在jarId直接刷新
					location.reload();
				}
				//刷新左侧菜单
				parent.JarManage.init();
			}});
		}else{
			AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '保存失败,错误信息:' + res.msg});
		}
	});
}

$(function(){
	
	parent.setContentHeightWidth(ref);
	//这个是问了在添加之后，改变了jarId没有导致没有改变样式
	try{parent.setContentHeightWidth("jar-manage-add-button");}catch(e){}
	
	initFileInput();
	
	addEvent();
});

function initFileInput(){
	
	var initParams = {
	        language: 'zh', //设置语言
	        allowedFileExtensions: ['jar', 'war'],
	        layoutTemplates:{
	        	actionDelete: '',
	        	actionUpload: '',
	        	preview: ''
	        },
	        uploadUrl: JarUrl.getFileUpload(), //上传的地址
	        uploadExtraData: function(){
	        	return {fileName: $("#jarEnName").val()};
	        },
//	      没用
//	         deleteUrl: "/api/jarmanage/file/delete",
//	         deleteExtraData: function(){
//	         	return {fileName: $("#jarEnName").val()};
//	         }
	};
	
	addExtraFileInputInitParams(initParams)
	
    $("#jarFile").fileinput(initParams).on('filebatchselected', function(event, files) {
    	var name = files[0].name;
    	var jarEnName = getFileName(name);
    	$("#jarEnName").val(jarEnName);
    }).on('filecleared', function(event) {
    	//点击清除之后，删除文件
    	var fileName = $("#cacheJarEnName").val();
    	var dirUuid = $("#dirUuid").val();
    	if (fileName != ""){
    		var jarId = $("#jarId").val();
        	deleteFile(fileName, dirUuid, jarId);	
    	}
    	//清除
    	$("#jarEnName").val("");
    	$("#cacheJarEnName").val("");
    	$("#dirUuid").val("");
    	$("#sufixName").val("");
    }).on('fileuploaded', function(event, data, previewId, index){
    	var name = (data.files[0]).name;
    	var dirUuid = data.response.data;
    	var fileName = getFileName(name);
    	$("#cacheJarEnName").val(fileName);
    	$("#dirUuid").val(dirUuid);
    	$("#sufixName").val(getSufixName(name));
    }).on('filesuccessremove', function (event, previewId, extra) {
    	
    }).on('filedeleted', function (event, previewId, extra) {

    });	
}

/**
 * 如果保存过，把文件信息加载到fileinput（内容？）
 */
function addExtraFileInputInitParams(initParams){
	if (jarId == ""){	
		return ;
	}
	
	var url = JarUrl.getOneJar();
    $.ajax({
        url: url,
        dataType: 'json',
        type: 'get',
        async:false,
        data:{jarId: jarId, reloadProperties: true},
        success:function(res){
        	if (res.code != '200'){
        		AlertMessgaeUtils.alert({id:'tip', title:'提示', content: '初始化失败页面数据失败,' + res.msg});
        		return ;
        	}
        	var data = res.data;
        	console.log(data);
        	initParams['initialPreviewConfig'] = [{
        		caption: data.jarEnName + '.jar',
        		//url: '/api/jarmanage',
        		key: data.jarEnName + '.jar',
        		filetype:'jar'
        	}];
        	initParams['initialPreview'] = [data.jarEnName + '.jar'];
        	
        	ParamObjectUtils.fill2Form(data);
        	
        	$("#cacheJarEnName").val(data.jarEnName);
        	ParamObjectUtils.fill2Form(data.jarPropertiesBean);
        	controlDisplayOfBeanData(data);
        }
    });	
}

/**
 * 返回数据控制显示
 * @param data
 */
function controlDisplayOfBeanData(data){

	var jarPropertiesBean = data.jarPropertiesBean;
	if (StringUtils.isNotNull(jarPropertiesBean)){
		var propertiesType = jarPropertiesBean.propertiesType;
		if (propertiesType != 0){
			$(".spingboot-properties-select").css({display: ''});
		}
	}	
	
	var jarJdkManageBean = data.jarJdkManageBean;
	if (StringUtils.isNotNull(jarJdkManageBean)){
		if (StringUtils.isNotBlank(jarJdkManageBean.jvm)){
			$("#jvmtr").css({display: ''});
		}
	}	
}

/**
 * 获取文件名字
 */
function getFileName(name){
	return name.substring(0, name.lastIndexOf("."));
}

/**
 * 获取后缀名
 */
function getSufixName(name){
	return name.substring(name.lastIndexOf(".") + 1, name.length);
}

/**
 * 删除文件
 */
function deleteFile(fileName, dirUuid, jarId){
	var success = false;
    $.ajax({
        url: JarUrl.getFileDelete(),
        dataType: 'json',
        type: 'post',
        async:false,
        data:{fileName: fileName, dirUuid: dirUuid, jarId: jarId, sufixName: $("#sufixName").val()},
        success:function(res){
        	success = (res.code == "200");
        	if (!success){
        		//删除失败，刷新页面
            	AlertMessgaeUtils.alert({id:'tip', title:'提示', content: res.msg, onclose:function(){
            		location.reload();
            	}});        		
        	}

        }
    });
    return success;
}

function addEvent(){
	
	$("#addPropertiesButton").click(function(){
		$(".spingboot-properties-select").css({display:""});
	});
	
	$("#addJVMParamButton").click(function(){
		$("#jvmtr").css({display:""});
	});
	
	$("#propertiesType").change(function(){
		var v = $(this).val();
		if (v == 0){
			$(".spingboot-properties-select").css({display:"none"});
		}
	});
}