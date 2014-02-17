package com.examples.jdkannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@ Retention, 表明注释信息将可以在运行时刻通过反射机制得到。如果不加入这个标签，上面的代码将没有任何输出
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTag {
	String name();

	int age();
}
