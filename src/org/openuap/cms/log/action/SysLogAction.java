/*
 * Copyright 2002-2006 the original author or authors.
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
package org.openuap.cms.log.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.DateUtil;
import org.openuap.base.util.FileUtil;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.core.action.AdminAction;
import org.openuap.util.QuickSort;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 系统日志控制器
 * </p>
 * 
 * <p>
 * $Id: SysLogAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 * </p>
 * 
 * @author joseph
 * @version 4.0
 */
public class SysLogAction extends AdminAction {

	private String defaultViewName;

	private String defaultScreensPath;

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd kk:mm");

	public SysLogAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/base/screens/log/";
		defaultViewName = defaultScreensPath + "sys_log.htm";
	}

	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model) {
		String logPath = CMSConfig.getInstance().getStringProperty("sys.path.logs-dir", "");
		String log = helper.getString("log");
		File file = new File(logPath);
		File[] files = file.listFiles();
		if (files != null && files.length > 1) {
			QuickSort.quickSort(files, 0, files.length - 1,
					new org.openuap.util.Comparable() {
						public int compare(Object obj1, Object obj2) {
							File file1 = (File) obj1;
							File file2 = (File) obj2;
							if (file1.lastModified() > file2.lastModified()) {
								return -1;
							} else if (file1.lastModified() < file2
									.lastModified()) {
								return 1;
							}
							return 0;
						}
					});
		}
		if (log != null) {
			String numLinesParam = helper.getString("lines", "50");
			String mode = helper.getString("mode", "asc");

			File logFile = new File(logPath, log);
			boolean tooBig = (logFile.length() / (1024)) > 250;
			if (tooBig) {
				model.put("tooBig", new Boolean(true));
			}

			StringBuffer sb = new StringBuffer();
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(logFile));
				String line = null;
				int totalNumLines = 0;
				while ((line = in.readLine()) != null) {
					totalNumLines++;
				}
				in.close();
				int numLines = 0;
				if (numLinesParam.equals("All")) {
					numLines = totalNumLines;
				} else {
					numLines = Math.min(Integer.parseInt(numLinesParam),
							totalNumLines);
				}
				String[] lines = new String[numLines];
				in = new BufferedReader(new FileReader(logFile));
				// skip lines
				int start = totalNumLines - numLines;
				if (start < 0) {
					start = 0;
				}
				for (int i = 0; i < start; i++) {
					in.readLine();
				}
				int k = 0;
				if ("asc".equals(mode)) {
					while ((line = in.readLine()) != null && k < numLines) {
						line = parseDate(line);
						line = hilite(line);
						lines[k] = line;
						k++;
					}
				} else {
					int end = lines.length - 1;
					while ((line = in.readLine()) != null && k < numLines) {
						line = parseDate(line);
						line = hilite(line);
						lines[end - k] = line;
						k++;
					}
				}
				numLines = start + k;
				int[] nums = new int[lines.length];
				int i = 0;
				if ("asc".equals(mode)) {
					for (int j = start + 1; j <= numLines; j++) {
						nums[i++] = j;
					}
				} else {
					for (int j = numLines; j >= start + 1; j--) {
						nums[i++] = j;
					}
				}
				model.put("nums", nums);
				model.put("lines", lines);
			} catch (Exception ex) {
				sb.append(ex.getMessage());
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception ex) {
					}
				}
			}
			model.put("body", sb.toString());
			model.put("logFile", logFile);
			model.put("lastModified", new Date(logFile.lastModified()));
			model.put("fileLength", FileUtil.getFileLength(logFile.length()));
		}
		model.put("files", files);
		return new ModelAndView(defaultViewName);
	}

	private static final String parseDate(String input) {
		if (input == null || "".equals(input)) {
			return input;
		}
		if (input.length() < 16) {
			return input;
		}
		String d = input.substring(0, 16);
		// try to parse it
		try {
			Date date = formatter.parse(d);
			StringBuffer buf = new StringBuffer(input.length());
			buf.append("<span class=\"date\" title=\"").append(
					DateUtil.asHtml(date)).append("\">");
			buf.append(d).append("</span>");
			buf.append(input.substring(16, input.length()));
			return buf.toString();
		} catch (ParseException pe) {
			return input;
		}
	}

	private static final String hilite(String input) {
		if (input == null || "".equals(input)) {
			return input;
		}
		if (input.indexOf(" - ") > -1) {
			StringBuffer buf = new StringBuffer();
			buf.append("<span class=\"hilite\">").append(input).append(
					"</span>");
			return buf.toString();
		}
		return input;
	}
}
