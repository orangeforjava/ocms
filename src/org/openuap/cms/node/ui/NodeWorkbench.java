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
package org.openuap.cms.node.ui;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openuap.cms.user.security.SecurityUtil;
import org.openuap.runtime.log.Log;
import org.openuap.runtime.plugin.WebApplicationPlugin;
import org.openuap.runtime.util.PropertyEventDispatcher;
import org.openuap.runtime.util.PropertyEventListener;

/**
 * <p>
 * 针对结点的Workbench UI
 * </p>
 * 
 * <p>
 * $Id: NodeWorkbench.java 3952 2010-11-02 10:18:32Z orangeforjava $
 * </p>
 * 
 * @author Joseph
 * @version 1.0
 */
public class NodeWorkbench {

	private Element coreModel;

	private Map<String, Element> overrideModels;

	private Element generatedModel;

	private WebApplicationPlugin plugin;

	private Log log = new Log("workbench");

	/**
	 * 
	 * 
	 */
	public NodeWorkbench() {
		init();
	}

	public NodeWorkbench(WebApplicationPlugin plugin) {
		this.plugin = plugin;
		init();
	}

	public WebApplicationPlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(WebApplicationPlugin plugin) {
		this.plugin = plugin;
	}

	public void init() {
		overrideModels = new LinkedHashMap<String, Element>();
		load();

		// The admin console model has special logic to include an informational
		// Enterprise tab when the Enterprise plugin is not installed. A
		// property
		// controls whether to show that tab. Listen for the property value
		// changing
		// and rebuild the model when that happens.
		PropertyEventDispatcher.addListener(new PropertyEventListener() {

			public void propertySet(String property, Map params) {
				if ("enterpriseInfoEnabled".equals(property)) {
					rebuildModel();
				}
			}

			public void propertyDeleted(String property, Map params) {
				if ("enterpriseInfoEnabled".equals(property)) {
					rebuildModel();
				}
			}

			public void xmlPropertySet(String property, Map params) {
				// Do nothing
			}

			public void xmlPropertyDeleted(String property, Map params) {
				// Do nothing
			}
		});
	}

	/**
	 * Adds XML stream to the tabs/sidebar model.
	 * 
	 * @param name
	 *            the name.
	 * @param in
	 *            the XML input stream.
	 * @throws Exception
	 *             if an error occurs when parsing the XML or adding it to the
	 *             model.
	 */
	public void addModel(String name, InputStream in) throws Exception {
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(in);
		addModel(name, (Element) doc.selectSingleNode("/workbench"));
	}

	/**
	 * Adds an &lt;workbench&gt; Element to the tabs/sidebar model.
	 * 
	 * @param name
	 *            the name.
	 * @param element
	 *            the Element
	 * @throws Exception
	 *             if an error occurs.
	 */
	public void addModel(String name, Element element) throws Exception {
		//
		addModel(name, element, true);
	}

	public void addModel(String name, Element element, boolean rebuild)
			throws Exception {
		//
		overrideModels.put(name, element);
		if (rebuild) {
			rebuildModel();
		}
	}

	/**
	 * Removes an &lt;adminconsole&gt; Element from the tabs/sidebar model.
	 * 
	 * @param name
	 *            the name.
	 */
	public void removeModel(String name) {
		overrideModels.remove(name);
		rebuildModel();
	}

	/**
	 * Returns the name of the application.
	 * 
	 * @return the name of the application.
	 */
	public synchronized String getAppName() {
		Element appName = (Element) generatedModel
				.selectSingleNode("//workbench/global/appname");
		if (appName != null) {
			String pluginName = appName.attributeValue("plugin");
			return getUIText(appName.getText(), pluginName);
		} else {
			return null;
		}
	}

	/**
	 * Returns the URL of the main logo image for the admin console.
	 * 
	 * @return the logo image.
	 */
	public synchronized String getLogoImage() {
		Element globalLogoImage = (Element) generatedModel
				.selectSingleNode("//workbench/global/logo-image");
		if (globalLogoImage != null) {
			String pluginName = globalLogoImage.attributeValue("plugin");
			return getUIText(globalLogoImage.getText(), pluginName);
		} else {
			return null;
		}
	}

	/**
	 * Returns the URL of the login image for the admin console.
	 * 
	 * @return the login image.
	 */
	public synchronized String getLoginLogoImage() {
		Element globalLoginLogoImage = (Element) generatedModel
				.selectSingleNode("//workbench/global/login-image");
		if (globalLoginLogoImage != null) {
			String pluginName = globalLoginLogoImage.attributeValue("plugin");
			return getUIText(globalLoginLogoImage.getText(), pluginName);
		} else {
			return null;
		}
	}

	/**
	 * Returns the version string displayed in the admin console.
	 * 
	 * @return the version string.
	 */
	public synchronized String getVersionString() {
		Element globalVersion = (Element) generatedModel
				.selectSingleNode("//workbench/global/version");
		if (globalVersion != null) {
			String pluginName = globalVersion.attributeValue("plugin");
			return getUIText(globalVersion.getText(), pluginName);
		} else {
			// Default to the Wildfire version if none has been provided via
			// XML.
			//
			return "";
		}
	}

	/**
	 * Returns the model. The model should be considered read-only.
	 * 
	 * @return the model.
	 */
	public synchronized Element getModel() {
		return generatedModel;
	}

	/**
	 * 
	 * @param pageID
	 * @return
	 */
	public Element getSingleTabNode(String pageID) {
		// System.out.println("pageID="+pageID);
		return (Element) (this.getModel().selectSingleNode("//*[@id='" + pageID
				+ "']/ancestor-or-self::tab"));
	}

	/**
	 * Convenience method to select an element from the model by its ID. If an
	 * element with a matching ID is not found, <tt>null</tt> will be returned.
	 * 
	 * @param id
	 *            the ID.
	 * @return the element.
	 */
	public synchronized Element getElemnetByID(String id) {
		return (Element) generatedModel.selectSingleNode("//*[@id='" + id
				+ "']");
	}

	/**
	 * Returns a text element for the admin console, applying the appropriate
	 * locale. Internationalization logic will only be applied if the String is
	 * specially encoded in the format "${key.name}". If it is, the String is
	 * pulled from the resource bundle. If the pluginName is not <tt>null</tt>,
	 * the plugin's resource bundle will be used to look up the key.
	 * 
	 * @param string
	 *            the String.
	 * @param pluginName
	 *            the name of the plugin that the i18n String can be found in,
	 *            or <tt>null</tt> if the standard Wildfire resource bundle
	 *            should be used.
	 * @return the string, or if the string is encoded as an i18n key, the value
	 *         from the appropriate resource bundle.
	 */
	public String getUIText(String string, String pluginName) {
		if (string == null) {
			return null;
		}
		// Look for the key symbol:
		if (string.indexOf("${") == 0
				&& string.indexOf("}") == string.length() - 1) {
			// return LocaleUtils.getLocalizedString(string.substring(2,
			// string.length()-1), pluginName);
		}
		return string;
	}

	/**
	 * 
	 * 装载核心模型
	 */
	private void load() {
		// Load the core model as the workbench-node-ui.xml file from the
		// classpath.
		InputStream in = this.plugin.getClassLoader().getResourceAsStream(
				"org/openuap/cms/workbench-node-ui.xml");
		if (in == null) {
			log
					.error("Failed to load workbench-node-ui.xml file from Workbench classes - admin "
							+ "console will not work correctly.");
			System.out
					.println("Failed to load  workbench-node-ui.xml file from Workbench classes - admin "
							+ "console will not work correctly.");
			return;
		}
		try {
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(in);
			coreModel = (Element) doc.selectSingleNode("/workbench");
		} catch (Exception e) {
			log.error("Failure when parsing main workbench-ui.xml file", e);
		}
		try {
			in.close();
		} catch (Exception ignored) {
			// Ignore.
		}

		// Load other workbench-ui.xml files from the classpath
		ClassLoader[] classLoaders = getClassLoaders();
		for (int i = 0; i < classLoaders.length; i++) {
			URL url = null;
			try {
				if (classLoaders[i] != null) {
					Enumeration e = classLoaders[i]
							.getResources("/META-INF/workbench-node-ui.xml");
					while (e.hasMoreElements()) {
						url = (URL) e.nextElement();
						try {
							in = url.openStream();
							addModel("admin", in);
						} finally {
							try {
								if (in != null) {
									in.close();
								}
							} catch (Exception ignored) {
								// Ignore.
							}
						}
					}
				}
			} catch (Exception e) {
				String msg = "Failed to load workbench-ui.xml";
				if (url != null) {
					msg += " from resource: " + url.toString();
				}
				// Log.warn(msg, e);
			}
		}
		rebuildModel();
	}

	/**
	 * 重建workbench Model
	 */
	public synchronized void rebuildModel() {
		//
		Document doc = DocumentFactory.getInstance().createDocument();
		// 核心模型
		generatedModel = coreModel.createCopy();
		doc.add(generatedModel);

		// 模型重写
		if (overrideModels != null) {

			for (Element element : overrideModels.values()) {
				// See if global settings are overriden.
				Element appName = (Element) element
						.selectSingleNode("//global/appname");
				//

				if (appName != null) {
					Element existingAppName = (Element) generatedModel
							.selectSingleNode("//global/appname");
					overrideGlobal(existingAppName, appName);
					if (appName.attributeValue("plugin") != null) {
						existingAppName.addAttribute("plugin", appName
								.attributeValue("plugin"));
					}
				}
				Element appLogoImage = (Element) element
						.selectSingleNode("//global/logo-image");
				if (appLogoImage != null) {
					Element existingLogoImage = (Element) generatedModel
							.selectSingleNode("//global/logo-image");
					overrideGlobal(existingLogoImage, appLogoImage);
					if (appLogoImage.attributeValue("plugin") != null) {
						existingLogoImage.addAttribute("plugin", appLogoImage
								.attributeValue("plugin"));
					}
				}
				Element appLoginImage = (Element) element
						.selectSingleNode("//global/login-image");
				if (appLoginImage != null) {
					Element existingLoginImage = (Element) generatedModel
							.selectSingleNode("//global/login-image");
					overrideGlobal(existingLoginImage, appLoginImage);
					if (appLoginImage.attributeValue("plugin") != null) {
						existingLoginImage.addAttribute("plugin", appLoginImage
								.attributeValue("plugin"));
					}
				}
				Element appVersion = (Element) element
						.selectSingleNode("//global/version");
				if (appVersion != null) {
					Element existingVersion = (Element) generatedModel
							.selectSingleNode("//global/version");
					if (existingVersion != null) {
						overrideGlobal(existingVersion, appVersion);
						if (appVersion.attributeValue("plugin") != null) {
							existingVersion.addAttribute("plugin", appVersion
									.attributeValue("plugin"));
						}
					} else {
						((Element) generatedModel.selectSingleNode("//global"))
								.add(appVersion.createCopy());
					}
				}
				// 获得tabs
				for (Iterator i = element.selectNodes("//tab").iterator(); i
						.hasNext();) {
					Element tab = (Element) i.next();
					//
					String id = tab.attributeValue("id");
					Element existingTab = getElemnetByID(id);
					// 不存在，加入
					if (existingTab == null) {
						generatedModel.add(tab.createCopy());
					}
					// 已经存在，重写
					else {
						overrideTab(existingTab, tab);
					}
				}
			}
		}
		//
	}

	/**
	 * tab重载
	 * 
	 * @param tab
	 * @param overrideTab
	 */
	private void overrideTab(Element tab, Element overrideTab) {
		// Override name, url, description.
		if (overrideTab.attributeValue("name") != null) {
			tab.addAttribute("name", overrideTab.attributeValue("name"));
		}
		if (overrideTab.attributeValue("url") != null) {
			tab.addAttribute("url", overrideTab.attributeValue("url"));
		}
		if (overrideTab.attributeValue("description") != null) {
			tab.addAttribute("description", overrideTab
					.attributeValue("description"));
		}
		if (overrideTab.attributeValue("plugin") != null) {
			tab.addAttribute("plugin", overrideTab.attributeValue("plugin"));
		}
		if (overrideTab.attributeValue("pos") != null) {
			tab.addAttribute("pos", overrideTab.attributeValue("pos"));
		}
		if (overrideTab.attributeValue("type") != null) {
			tab.addAttribute("type", overrideTab.attributeValue("type"));
		}
		if (overrideTab.attributeValue("permission") != null) {
			tab.addAttribute("permission", overrideTab
					.attributeValue("permission"));
		}
		// Override sidebar items.
		for (Iterator i = overrideTab.elementIterator(); i.hasNext();) {
			Element sidebar = (Element) i.next();
			String id = sidebar.attributeValue("id");
			//
			if (sidebar.attributeValue("plugin") == null) {
				sidebar.addAttribute("plugin", tab.attributeValue("plugin"));
			}
			Element existingSidebar = getElemnetByID(id);
			// Simple case, there is no existing sidebar with the same id.
			if (existingSidebar == null) {

				tab.add(sidebar.createCopy());
			}
			// More complex case -- a sidebar with the same id already exists.
			// In this case, we have to overrite only the difference between
			// the two elements.
			else {
				overrideSidebar(existingSidebar, sidebar);
			}
		}
	}

	/**
	 * 重载sidebar
	 * 
	 * @param sidebar
	 * @param overrideSidebar
	 */
	private void overrideSidebar(Element sidebar, Element overrideSidebar) {
		// Override name.
		if (overrideSidebar.attributeValue("name") != null) {
			sidebar
					.addAttribute("name", overrideSidebar
							.attributeValue("name"));
		}
		if (overrideSidebar.attributeValue("plugin") != null) {
			sidebar.addAttribute("plugin", overrideSidebar
					.attributeValue("plugin"));
		}
		if (overrideSidebar.attributeValue("pos") != null) {
			sidebar.addAttribute("pos", overrideSidebar.attributeValue("pos"));
		}
		if (overrideSidebar.attributeValue("type") != null) {
			sidebar
					.addAttribute("type", overrideSidebar
							.attributeValue("type"));
		}
		// Override entries.
		for (Iterator i = overrideSidebar.elementIterator(); i.hasNext();) {
			Element entry = (Element) i.next();
			String id = entry.attributeValue("id");
			Element existingEntry = getElemnetByID(id);
			// 根据父元素补全plugin属性
			if (entry.attributeValue("plugin") == null) {
				entry.addAttribute("plugin", sidebar.attributeValue("plugin"));
			}
			if (existingEntry == null) {
				sidebar.add(entry.createCopy());
			}
			// More complex case -- an entry with the same id already exists.
			// In this case, we have to overrite only the difference between
			// the two elements.
			else {
				overrideEntry(existingEntry, entry);
			}
		}
	}

	/**
	 * 重写item
	 * 
	 * @param entry
	 * @param overrideEntry
	 */
	private void overrideEntry(Element entry, Element overrideEntry) {
		// Override name.
		if (overrideEntry.attributeValue("name") != null) {
			entry.addAttribute("name", overrideEntry.attributeValue("name"));
		}
		if (overrideEntry.attributeValue("url") != null) {
			entry.addAttribute("url", overrideEntry.attributeValue("url"));
		}
		if (overrideEntry.attributeValue("description") != null) {
			entry.addAttribute("description", overrideEntry
					.attributeValue("description"));
		}
		if (overrideEntry.attributeValue("plugin") != null) {
			entry
					.addAttribute("plugin", overrideEntry
							.attributeValue("plugin"));
		}
		if (overrideEntry.attributeValue("pos") != null) {
			entry.addAttribute("pos", overrideEntry.attributeValue("pos"));
		}
		if (overrideEntry.attributeValue("type") != null) {
			entry.addAttribute("type", overrideEntry.attributeValue("type"));
		}
		// Override any sidebars contained in the entry.
		for (Iterator i = overrideEntry.elementIterator(); i.hasNext();) {
			Element sidebar = (Element) i.next();
			String id = sidebar.attributeValue("id");
			Element existingSidebar = getElemnetByID(id);
			// Simple case, there is no existing sidebar with the same id.
			if (existingSidebar == null) {
				entry.add(sidebar.createCopy());
			}
			// More complex case -- a sidebar with the same id already exists.
			// In this case, we have to overrite only the difference between
			// the two elements.
			else {
				overrideSidebar(existingSidebar, sidebar);
			}
		}
	}

	/**
	 * Returns an array of class loaders to load resources from.
	 * 
	 * @return an array of class loaders to load resources from.
	 */
	private ClassLoader[] getClassLoaders() {
		ClassLoader[] classLoaders = new ClassLoader[3];
		classLoaders[0] = NodeWorkbench.class.getClass().getClassLoader();
		classLoaders[1] = Thread.currentThread().getContextClassLoader();
		classLoaders[2] = ClassLoader.getSystemClassLoader();
		return classLoaders;
	}

	/**
	 * 获得指定Tab的子菜单列表,这些元素已经经过排序
	 * 
	 * @param tabId
	 * @return
	 */
	public synchronized List getSubMenu(String tabId) {
		// 获得指定的Tab元素
		Element tabElement = getSingleTabNode(tabId);
		List<Element> rs = null;
		if (tabElement != null) {
			List<Element> childSideBars = tabElement.selectNodes("//tab[@id='"
					+ tabId + "']/sidebar", "@pos");
			rs = new ArrayList<Element>(childSideBars.size());
			for (Element sidebar : childSideBars) {
				// 判断是否应该显示，枚举遍历
				if (isShow(sidebar)) {
					rs.add(sidebar);
				}
			}
			Collections.sort(rs, new ElementComparator());
			return rs;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * 返回经过排序的Tab
	 * 
	 * @return
	 */
	public synchronized List<Element> getTabs() {

		final List<Element> tabs = this.getModel().selectNodes("//tab", "@pos");
		List<Element> rs = null;
		if (tabs != null) {
			rs = new ArrayList<Element>(tabs.size());
			for (Element tab : tabs) {
				// 判断是否应该显示，枚举遍历
				if (isShow(tab)) {
					// System.out.println("tab url="+tab.attributeValue("url"));
					if (tab.attributeValue("url") == null) {
						Element element = this.getTabDefaultItems(tab);
						if (element != null) {
							tab.addAttribute("url", element
									.attributeValue("url"));
						}
					}
					rs.add(tab);
				}
			}
		}

		return rs;
	}

	public synchronized List<Element> getInternalTabs() {
		final List<Element> tabs = this.getModel().selectNodes("//tab", "@pos");
		return tabs;
	}

	public synchronized List<Element> getSidebars(String pageID) {
		Element tab = getSingleTabNode(pageID);
		List<Element> sidebars = tab.elements();
		List<Element> rs = null;
		if (sidebars != null) {
			rs = new ArrayList<Element>(sidebars.size());
			for (Element sidebar : sidebars) {
				// 判断是否应该显示，枚举遍历
				if (isShow(sidebar)) {
					rs.add(sidebar);
				}
			}
			Collections.sort(rs, new ElementComparator());
		}

		return rs;
	}

	public synchronized List<Element> getItems(Element sidebar) {
		List<Element> items = sidebar.elements();
		List<Element> rs = null;
		if (items != null) {
			rs = new ArrayList<Element>(items.size());
			for (Element item : items) {
				// 判断是否应该显示，枚举遍历
				if (isShow(item)) {
					rs.add(item);
				}
			}
			Collections.sort(rs, new ElementComparator());
		}
		return rs;
	}

	public synchronized Element getTabDefaultItems(Element tab) {
		//
		List<Element> sidebars = getSidebars(tab.attributeValue("id"));
		for (Element sidebar : sidebars) {
			// System.out.println("sidebar="+sidebar.attributeValue("id"));
			List<Element> items = this.getItems(sidebar);
			if (items != null && items.size() > 0) {
				// System.out.println("item="+items.get(0).attributeValue("id"));
				return items.get(0);
			}
		}
		return null;
	}

	protected void overrideGlobal(Element entry, Element overrideEntry) {
		String opos = overrideEntry.attributeValue("pos");
		long lopos = 0L;
		if (opos != null) {
			lopos = Long.parseLong(opos);
		} else {
			opos = "0";
		}
		//
		String pos = entry.attributeValue("pos");
		long lpos = 0L;
		if (pos != null) {
			lpos = Long.parseLong(pos);
		}
		if (lopos >= lpos) {
			entry.setText(overrideEntry.getText());
			entry.addAttribute("pos", opos);
			//

		}
	}

	/**
	 * 
	 * @author juweiping
	 * 
	 */
	private class ElementComparator implements Comparator<org.dom4j.Element> {

		public int compare(org.dom4j.Element o1, org.dom4j.Element o2) {
			org.dom4j.Element e1 = (org.dom4j.Element) o1;
			org.dom4j.Element e2 = (org.dom4j.Element) o2;
			String pos1 = e1.attributeValue("pos");
			String pos2 = e2.attributeValue("pos");
			if (pos1 == null) {
				pos1 = "0";
			}
			if (pos2 == null) {
				pos2 = "0";
			}
			long p1 = Long.parseLong(pos1);
			long p2 = Long.parseLong(pos2);
			if (p1 > p2) {
				return 1;
			} else if (p1 < p2) {
				return -1;
			} else {
				return 0;
			}

		}
	}

	/**
	 * 判断某个界面元素是否应该显示
	 * 
	 * @param element
	 * @return
	 */
	protected boolean isShow(Element element) {
		String plugin = element.attributeValue("plugin");
		String viewPermission = element.attributeValue("permission");
		// String id = element.attributeValue("id");
		// System.out.println("id=" + id);
		// System.out.println("plugin=" + plugin);
		// System.out.println("viewPermission=" + viewPermission);
		// System.out.println("-----------------------------------");
		if (plugin == null) {
			plugin = "default";
		}
		if (viewPermission == null) {
			viewPermission = "0";
		}
		// 如果权限设定为any，即任何人都有此权限
		if (viewPermission.equals("any")) {
			return true;
		}
		if (SecurityUtil.hasPermission(plugin, "0", viewPermission)) {
			return true;
		}
		for (Iterator i = element.elementIterator(); i.hasNext();) {
			Element el = (Element) i.next();
			if (isShow(el)) {
				return true;
			}
		}
		return false;
	}
}
