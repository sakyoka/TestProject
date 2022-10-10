/**
 * 参数对象工具类
 * add: 2022-01-16 csy
 */
var ParamObjectUtils = {
	
	/**
	 * 获取参数对象
	 */
	getParamObject: function(attr){
		attr = (attr == undefined ? '.param-field' : attr);
		var paramObject = {};
		$.each($(attr), function(){
			
			var fieldName = getFieldName(this);
			
			var v = '';
			if ($(this).attr('type') == 'radio'){
				v = $('input[name='+ fieldName +']:checked').val();
			}else{
				v = $(this).val();
			}
			v = (v == undefined ? '' : v);
			paramObject[fieldName] = v;
		});
		return paramObject;
	},
	
	/**
	 * 校验
	 */
	valid: function(attr, group){
		attr = (attr == undefined ? '.param-field' : attr);
		try{
			ValidUtils
		}catch(e){
			alert('未引入ValidUtils');
			throw '未引入ValidUtils';
		}
        var paramObject = this.getParamObject(attr);
        var resultObject = {result: true, msg: '校验通过'};
		$.each($(attr), function(){
			
			var valids = $(this).attr('valids');
			if (valids == undefined || valids == ''){
				return true;
			}
			
			var fieldName = getFieldName(this);
			if (group != undefined 
					&& group != ''){
				var groups = $(this).attr('groups');
				var groupArr = groups.split(',');
				if ($.inArray(group, groupArr) == -1){
					return true;
				}
			}
			
			var fieldName = getFieldName(this);
			var validComponents = valids.split(',');
			if (validComponents != undefined && validComponents.length > 0){
				var fieldDesc = $(this).attr('fieldDesc');
				fieldDesc = (fieldDesc == undefined ? fieldName : fieldDesc)
				$.each(validComponents, function(index, item){
					var validObject = ValidUtils[item];
					if (validObject == undefined){
						alert('ValidUtils未编写'+ item +'校验器');
						throw 'ValidUtils未编写'+ item +'校验器';						
					}
					var result = validObject(paramObject[fieldName], fieldDesc);
					if (result.result !== true){
						resultObject = result;
						return false;
					}
				});
			}
			if (resultObject.result !== true){
				return false;
			}
		});
		resultObject['data'] = paramObject;
		return resultObject;
	},
	
	/**
	 * 填充数据到表单
	 */
	fill2Form: function(data, config){
		config = config || {};
		//是否忽略子对象，默认否
		var igonreSub = config.igonreSub || false;
		//处理key value函数
		var keyValueFc = config.keyValue;
		
		if (typeof(data) != 'object'){
			return ;
		}
		
    	for (var k in data){
    		var v = data[k];
    		if (typeof (v) == 'object'){
    			if (!igonreSub){
    				this.fill2Form(v);
    			}
    		}else{
    			var type = $("#" + k).attr('type');
    			if (type == 'radio'){
    				$('input[type=radio][name='+ k +']').removeAttr("checked");
    				$('input[type=radio][name='+ k +'][value='+ v +']').prop("checked", true);
    			}else{
    				if (keyValueFc){
    					keyValueFc(k, v);
    				}else{
    					$("#" + k).val(v);
    				}
    			}
    		}
    	}
	}
}

var getFieldName = function(_this){
	var fieldName = $(_this).attr('name');
	fieldName = (fieldName == undefined || fieldName == "" ? $(_this).attr('id') : fieldName);
	if (fieldName == undefined || fieldName == ''){
		alert('元素未设置id或name');
		throw '元素未设置id或name';
	}
	return fieldName;
}