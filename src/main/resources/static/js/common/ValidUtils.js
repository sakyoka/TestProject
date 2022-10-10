/**
 * 校验工具类
 * add: 2022-01-16 csy
 */
var ValidUtils = {

	resultObject: function(result, msg){
		return {
			result: result,
			msg: msg
		};
	},
	
	OK_OBEJCT: function(){
		return ValidUtils.resultObject(true, '校验通过');
	},
	
	/**
	 * 非空
	 */
	notBlank: function(v, desc){
		if (v == undefined || v == ""){
			return ValidUtils.resultObject(false, desc + "不能为空");
		}
		return ValidUtils.OK_OBEJCT();
	}
}