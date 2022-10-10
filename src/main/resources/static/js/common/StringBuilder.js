/**
 * 字符串拼接
 * add: 2022-01-16 csy
 */
var StringBuilder = function(){
	
	var arr = [];
	
	this.append = function(object){
		object = object === undefined ? "" : object;
		arr.push(object);
		return this;
	}
	
	this.clear = function(){
		arr.length = 0;
	}
	
	this.toString = function(){
		return arr.join("");
	}
}