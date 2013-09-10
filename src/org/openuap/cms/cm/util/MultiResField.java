/*
 * Copyright 2005-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openuap.cms.cm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 多资源属性
 * </p>
 * 
 * @author Joseph
 * @version 4.0
 */
public class MultiResField {
	private int nums;
	private List<ResRefBean> reses = null;
	public static String splitter_1 = "\u25A4";// ▤
	public static String splitter_2 = "\u25D9";// ◙

	public int getNums() {
		return nums;
	}

	public void setNums(int nums) {
		this.nums = nums;
	}

	public List<ResRefBean> getReses() {
		return reses;
	}

	public void setReses(List<ResRefBean> reses) {
		this.reses = reses;
	}

	public void fromString(String input) {
		String[] ary = input.split(splitter_1, -1);
		if (ary.length > 1) {
			this.nums = new Integer(ary[0]);
			List<ResRefBean> reses = new ArrayList<ResRefBean>();
			for (int i = 0; i < nums; i++) {
				ResRefBean bean = new ResRefBean();
				String[] ary2 = ary[i + 1].split(splitter_2, -1);
				bean.setId(new Long(ary2[0]));
				bean.setUrl(ary2[1]);
				bean.setTitle(ary2[2]);
				bean.setDescription(ary2[3]);
				reses.add(bean);
			}
			this.reses = reses;
		}

	}

	public static MultiResField fieldFromString(String input) {
		MultiResField field = new MultiResField();
		String[] ary = input.split(splitter_1, -1);
		if (ary.length > 1) {
			field.nums = new Integer(ary[0]);
			List<ResRefBean> reses = new ArrayList<ResRefBean>();
			for (int i = 0; i < field.nums; i++) {
				ResRefBean bean = new ResRefBean();
				String[] ary2 = ary[i + 1].split(splitter_2, -1);
				bean.setId(new Long(ary2[0]));
				bean.setUrl(ary2[1]);
				bean.setTitle(ary2[2]);
				bean.setDescription(ary2[3]);
				reses.add(bean);
			}
			field.reses = reses;
		}
		return field;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.nums);
		if (nums > 0) {
			for (ResRefBean res : reses) {
				sb.append(splitter_1);//
				sb.append(res.getId());
				sb.append(splitter_2);//
				sb.append(res.getUrl());
				sb.append(splitter_2);
				sb.append(res.getTitle());
				sb.append(splitter_2);
				sb.append(res.getDescription());
			}
		}
		return sb.toString();
	}

	public static String toString(MultiResField bean) {
		StringBuilder sb = new StringBuilder();
		sb.append(bean.nums);
		if (bean.nums > 0) {
			for (ResRefBean res : bean.reses) {
				sb.append(splitter_1);//
				sb.append(res.getId());
				sb.append(splitter_2);//
				sb.append(res.getUrl());
				sb.append(splitter_2);
				sb.append(res.getTitle());
				sb.append(splitter_2);
				sb.append(res.getDescription());
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		MultiResField bean = new MultiResField();
		bean.setNums(2);
		List<ResRefBean> reses = new ArrayList<ResRefBean>();
		ResRefBean b = new ResRefBean();
		b.setId(1001);
		b.setUrl("../resource/2010/01/10/10.jpg");
		b.setTitle("您好图片");
		b.setDescription("这个老好了，呵呵\n，必须的<br/>");
		reses.add(b);
		b = new ResRefBean();
		b.setId(1002);
		b.setUrl("../resource/2010/01/10/11.jpg");
		b.setTitle("我很喜欢这个图片！");
		b.setDescription("呵呵，我的测试\n，必须的<br/>");
		reses.add(b);
		bean.setReses(reses);
		//
		System.out.println(bean.toString());
		//
		System.out.println("----------------------");
		MultiResField bean2 = null;
		bean2=MultiResField.fieldFromString(bean.toString());
		System.out.println(bean2.toString());
	}
}
