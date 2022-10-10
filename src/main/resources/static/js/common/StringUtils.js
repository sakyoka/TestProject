/**
 * 字符串操作
 * add:2019-11-20 csy
 */
var StringUtils = {
	/**
	 * 判空串
	 * @param v
	 * @returns true 非空 false空
	 */
	isNotBlank: function( v ){
		return StringUtils.isNotNull( v ) && v != '';
	},
	/**
	 * 判空串
	 * @param v true 空 false非空
	 * @returns
	 */
	isBlank: function( v ){
		return !StringUtils.isNotBlank( v );
	},
	/**
	 * 判空
	 * @param v
	 * @returns true 非空 false空
	 */
	isNotNull: function( v ){
		return v != undefined && v != null;
	},
	/**
	 * 判空
	 * @param v
	 * @returns true 空 false非空
	 */
	isNull: function( v ){
		return !StringUtils.isNotNull( v );
	}
}