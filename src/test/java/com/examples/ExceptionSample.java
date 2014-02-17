package com.examples;

import com.utils.cache.CacheMemcacheException;

/**
 * 本测试案例目的：要清楚什么情况下使用运行时异常 有的异常
 * 
 * RuntimeException可以由系统自动抛出，可以不进行try...catch
 * 
 * 场景： 通过标识元数据及一般模式来持续描述企业中出现的各类错误。该数据可包含常用的属性，这些属性包括日期
 * 、时间、错误代码、描述、严重级别、消息来源、相关ID等等。对于该元数据的全方面分析将十分有助于建立有效的服务监测
 * 
 * BaseException
 * 
 * CacheException extends BaseException
 * 
 * WorkflowException extends BaseException
 * 
 * 
 * 原则1：谨慎的抛出检查型异常。或者你认为，你可以处理它。否则，包装为运行时异常。
 * 
 * 原则2：再次强调，能捕捉就捕捉。
 * 
 * 
 * 理论： 异常有两个模型：中止模型和继续模型
 * 
 * 中止模型认为异常不应该再回来，他做的是善后工作。而继续模型保持异常时环境，希望再一次能运行成功。
 * 
 * 
 * 
 * 2种情况 错误大致可分为两类：
 * 
 * 1.可恢复性错误——可恢复性错误是指那些可通过恰当的可选执行路径来恢复用户程序的错误。这些错误是由于不遵守某一特定业务规则所导致的。
 * 
 * 2.不可恢复性错误——这些错误是客户程序无法恢复的。此类错误是在运行时中由某些不可预期的错误产生的，诸如空指针、资源不可用等编程性错误
 * 运行时异常，表示你的代码本身存在BUG，比如你提到的ArrayIndexOutOfBoundsException，数组下标越界，这个属于代码有问题，
 * 数组定义的长度不够实际使用，不处理肯定会报错，如果你操作某个模块发现能正常运行，那只是因为代码还没跑到这个错误的地方而已。。
 * */
public class ExceptionSample {

	public void test1() throws Throwable {
		String s = null;
		boolean eq = s.equals(""); // NullPointerException
	}

	/** 不能直观看出是什么模块错误 */
	public void test2() {
		String s = null;
		if (s == null)
			throw new NullPointerException("参数不能为空");

	}

	/** 这种做法会让程序一定要捕获，开发不方便 */
	public void test3() throws Throwable {
		String s = null;
		try {
			if (s == null)
				throw new NullPointerException("参数不能为空");
		} catch (Exception e) {
			System.out.println("处理了");
		}

	}

	/**
	 * 
	 * 能够看到是哪一个模块的出错，方便程序员定位 建议各个模块都去拥有自己异常类，并这样抛出 所以
	 * 
	 * abstract class BaseException（ 的定义,是不能实例化的）
	 * 
	 * BaseException--> CacheException -->CacheMemcacheException 通过title
	 * 的层次来方便错误定位
	 * 
	 * 
	 * 让程序员不能直接乱抛出异常
	 * */
	public void test4() {
		String s = null;
		if (s == null)
			throw new CacheMemcacheException("参数不能为空",
					new NullPointerException());
	}

}
