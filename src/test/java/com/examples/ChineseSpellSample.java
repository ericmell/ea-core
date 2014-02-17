package com.examples;

import com.utils.chinesespell.ChineseSpell;
import com.utils.hardinfo.HardInfo;

import junit.framework.TestCase;
public class ChineseSpellSample {
	/**
	 * 得到系统中所有属性的名称
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ChineseSpell chineseSpell = new ChineseSpell();
		String tmpName = "！中华人民共和国广东省广州市海珠区广州大道南三百六十八号从兴电子开发有限公司四楼质量管理部杨盛贵先生收阅！13729826902 singvi  ";
		// tmpName = "！";
		String userName = "z";// 要查询的字符
		String fullSpellActual = chineseSpell.getSpell(tmpName);
		String shortActual = chineseSpell.getSpellForShort(tmpName);
		String shortExpected = "！zhrmghggdsgzshzqgzddnsblsbhcxdzkfyxgsslzlglbysgxssy！13729826902 singvi  ";
		String fullSpellExpected = "！zhonghuarenmingongheguoguangdongshengguangzhoushihaizhuquguangzhoudadaonansanbailiushibahaocongxingdianzikaifayouxiangongsisilouzhiliangguanlibuyangshengguixianshengshouyue！13729826902 singvi  ";
		if (chineseSpell.compareByLike(tmpName, userName)) {
			System.out.println("已找到：" + userName);
		} else {
			System.out.println("未找到：" + tmpName);
		}
		tmpName = "！吉潇古雅长春和云霄 ";
		userName = "z";// 要查询的字符
		fullSpellActual = chineseSpell.getSpell(tmpName);
		shortActual = chineseSpell.getSpellForShort(tmpName);
		System.out.println("中文：" + tmpName);
		System.out.println("简写拼音：" + shortActual);
		System.out.println("全写拼音：" + fullSpellActual);
	}
	}

