/**
 * for object operate
 * add:2021-10-20 by csy
 */
var ObjectUtils = {
		
		/**
		 * 提取集合中对象的某一属性为一个集合
		 * @param objects
		 * @param fieldName
		 * @returns {Array}
		 */
		getAttrArrByObjects: function(objects , fieldName){
			var arr = [];
			if (this.isDefined(objects)){
				for (var index in objects){
					var v = objects[index][fieldName];
					arr.push(v);
				}				
			}
			return arr;
		},
		
		/**
		 * 判断属性为空(null)返回true
		 * @param o
		 * @returns {Boolean}
		 */
		isNull: function(o){
			return o === null;
		},
		
		/**
		 * 判断属性不为空(null)返回true
		 * @param o
		 * @returns {Boolean}
		 */
		isNotNull: function(o){
			return !this.isNull(o);
		},
		
		/**
		 * 判断属性为undefined返回true
		 * @param o
		 * @returns {Boolean}
		 */
		isUndefined: function(o){
			return o === undefined;
		},
		
		/**
		 * 判断属性不为undefined返回true
		 * @param o
		 * @returns {Boolean}
		 */
		isDefined: function(o){
			return !this.isUndefined(o);
		},
		
		/**
		 * 如果v值为空，返回默认值defaultValue
		 * @param v
		 * @param defaultValue
		 * @returns
		 */
		ifBlankDefault: function(v, defaultValue){
			return StringUtils.isNotBlank(v) ? v : defaultValue;
		},
		
		/**
		 * 如果v为null或undefined，返回默认值defaultValue
		 * @param v
		 * @param defaultValue
		 * @returns
		 */
		ifNullDefault: function(v, defaultValue){
			return this.isNull(v) || this.isUndefined(v) ? defaultValue : v; 
		}
}
