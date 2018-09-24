package com.hingecloud.apppubs.pub.tools;

/**
 * 错误代码
 * 
 * <pre>
 * 代码	 说明 
 * 0	请求成功
 * 4000	通用错误,系统繁忙等
 * </pre>
 */
public final class CodeConst {

	/**
	 * 请求成功
	 */
	public static final int SUCCESS = 0;

	/**
	 * 通用错误,系统繁忙等
	 */
	public static final int GENERIC = 4000;

	/**
	 * 地址不存在
	 */
	public static final int INVALID_TOKEN = 4001;

	/**
	 * 参数不合法
	 */
	public static final int ILLEGAL_ARGUMENT = 4002;

	/**
	 * 创建编译任务失败
	 */
	public static final int TASK_CREATE_ERROR = 5001;

}
