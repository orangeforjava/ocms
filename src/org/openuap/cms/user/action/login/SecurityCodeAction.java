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
package org.openuap.cms.user.action.login;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.StringUtil;
import org.openuap.base.web.mvc.BaseController;
import org.openuap.cms.CmsPlugin;
import org.openuap.runtime.plugin.WebApplicationPlugin;
import org.openuap.runtime.plugin.WebPluginManagerUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;

import Acme.JPM.Encoders.GifEncoder;

import com.eteks.awt.PJAGraphicsExtension;
import com.eteks.awt.PJAGraphicsManager;
import com.eteks.awt.PJAImage;
import com.eteks.filter.Web216ColorsFilter;

/**
 * <p>
 * 安全校验码产生控制器
 * </p>
 * 
 * <p>
 * $Id: SecurityCodeAction.java 3936 2010-10-27 02:31:05Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class SecurityCodeAction extends BaseController implements InitializingBean{
	/**
	 * 字体路径
	 */
	private String FONT_PATH;

	private PJAGraphicsManager graphicsManager;

	public SecurityCodeAction() {
		
	}
	public void afterPropertiesSet() throws Exception{

		WebApplicationPlugin plugin=(WebApplicationPlugin)WebPluginManagerUtils.getPlugin("base", CmsPlugin.PLUGIN_ID);
		//
		
		FONT_PATH = plugin.getServletContext().getRealPath("WEB-INF/fonts");
		graphicsManager = PJAGraphicsManager.getDefaultGraphicsManager();
		graphicsManager.loadFonts(FONT_PATH);
	}
	
	@Override
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		String str = null;
		String ostr = request.getParameter("key");
		try {
			//ostr = ostr.substring(1, ostr.lastIndexOf("."));
			str = decryptKey(ostr);
			//System.out.println(str);
		} catch (Exception exception) {
		}
		if (str == null || str.length() < 1) {
			System.err.println("Error Security Code:" + ostr);
			str = "Oops!";
		}
		try {
			Image image = createImage(46, 20);
			Graphics gc = image.getGraphics();
			((PJAGraphicsExtension) gc).setFont("", 1, 14);
			gc.setColor(Color.gray);
			gc.draw3DRect(0, 0, 45, 19, false);
			gc.setColor(Color.black);
			gc.drawString(str, 5, 16);
			Random random = new Random(System.currentTimeMillis());
			for (int i = 0; i < 80; i++) {
				int j = random.nextInt(46);
				int k = random.nextInt(20);
				gc.drawLine(j, k, j, k);
			}

			sendGIFImage(image, response);
		} catch (Exception ex) {
			response.setContentType("text/plain");
			PrintWriter writer = response.getWriter();
			writer.print("Sorry,Can't generate image\n");
			ex.printStackTrace(writer);
			writer.flush();
			writer.close();
		}
		return null;
	}

	public Image createImage(int width, int height) {
		return new PJAImage(width, height);
	}

	public void sendGIFImage(Image image, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/gif");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "Thu, 01 Jan 2002 00:00:00 GMT");
		OutputStream out = response.getOutputStream();
		try {
			(new GifEncoder(image, out)).encode();
		} catch (IOException ioexception) {
			(new GifEncoder(new FilteredImageSource(image.getSource(),
					new Web216ColorsFilter()), out)).encode();
		}
		out.flush();
	}

	public String decryptKey(String input) {
		return StringUtil.decrypt(input);
	}
}
