package com.examples.jdkannotation;

import java.lang.annotation.Annotation;

/**
 * 获取注释信息之后如何处理确很值得推敲，我们可以用他们来做一些语法检查， 文件相关性检查，进行各种统计等等
 * */
public class TagTest {
	@MyTag(name = "MyTag", age = 1)
	public void testTag() {
	}

	public static void main(String[] args) {
		TagTest tt = new TagTest();
		try {
			Annotation[] annotation = tt.getClass().getMethod("testTag")
					.getAnnotations();
			for (Annotation tag : annotation) {
				System.out.println("Tag is:" + tag);
				System.out.println("tag.name()" + ((MyTag) tag).name());
				System.out.println("tag.age()" + ((MyTag) (tag)).age());
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

}
