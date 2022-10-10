/**
 * add 2022-09-13 csy
 * CommonApiParam 构建
 */
var ApiParams = function(serviceCls, serviceMd){
	
	var serviceCls = serviceCls || '';
	
	var serviceMd = serviceMd || '';
	
	var fieldNameValues = [];
	
	this.serviceClass = function(serviceCls){
		serviceClass = serviceCls;
		return this;
	}
	
	this.serviceMethod = function(serviceMd){
		serviceMethod = serviceMd;
		return this;
	}
	
	this.params = function(fieldName, fieldValue){
		fieldNameValues.push({
			fieldName: fieldName,
			fieldValue: fieldValue
		});
		return this;
	}
	
	this.toString = function(){
		return JSON.stringify(this.build());
	}
	
	this.build = function(){
		return {
			serviceClass: serviceCls,
			serviceMethod: serviceMd,
			params: fieldNameValues
		}
	}
}