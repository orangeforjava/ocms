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
package org.openuap.cms.util.ui;

/**
 * 
 * 颜色类型
 * 
 * @author Joseph
 * 
 */
public class ColorType implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6103532102847048603L;

	private String colorValue;
	private String color;
	public static final ColorType[] DEFAULT_COLOR_TYPES = new ColorType[] {
			new ColorType("color:#000000;", "#000000"),
			new ColorType("color:#FFFFFF;", "#FFFFFF"),
			new ColorType("color:#008000;", "#008000"),
			new ColorType("color:#800000;", "#800000"),
			new ColorType("color:#808000;", "#808000"),
			new ColorType("color:#000080;", "#000080"),
			new ColorType("color:#800080;", "#800080"),
			new ColorType("color:#808080;", "#808080"),
			new ColorType("color:#FFFF00;", "#FFFF00"),
			new ColorType("color:#00FF00;", "#00FF00"),
			new ColorType("color:#00FFFF;", "#00FFFF"),
			new ColorType("color:#FF00FF;", "#FF00FF"),
			new ColorType("color:#FF0000;", "#FF0000"),
			new ColorType("color:#0000FF;", "#0000FF"),
			new ColorType("color:#008080;", "#008080") };

	public ColorType(String color, String colorValue) {
		this.color = color;
		this.colorValue = colorValue;
	}

	public String getColorValue() {
		return colorValue;
	}

	public void setColorValue(String colorValue) {
		this.colorValue = colorValue;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
