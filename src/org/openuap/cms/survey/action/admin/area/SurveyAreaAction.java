/**
 * $Id: SurveyAreaAction.java 4026 2011-03-22 14:58:42Z orangeforjava $
 */
package org.openuap.cms.survey.action.admin.area;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openuap.base.util.ControllerHelper;
import org.openuap.base.util.QueryInfo;
import org.openuap.base.util.context.PageBuilder;
import org.openuap.cms.core.action.CMSBaseAction;
import org.openuap.cms.node.manager.NodeManager;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.survey.manager.SurveyAreaManager;
import org.openuap.cms.survey.model.SurveyArea;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author Joseph
 * 
 */
public class SurveyAreaAction extends CMSBaseAction {
	private String defaultScreensPath;
	private String defaultViewName;
	//
	private String operationViewName;

	private SurveyAreaManager surveyAreaManager;
	private NodeManager nodeManager;

	public SurveyAreaAction() {
		initDefaultViewName();
	}

	protected void initDefaultViewName() {
		defaultScreensPath = "/plugin/cms/survey/screens/area/";
		defaultViewName = defaultScreensPath + "area_list.html";
		operationViewName = defaultScreensPath + "area_operation_result.html";
	}

	/**
	 * 调查位列表显示
	 */
	public ModelAndView perform(HttpServletRequest request,
			HttpServletResponse response, ControllerHelper helper, Map model)
			throws Exception {
		ModelAndView mv = new ModelAndView(defaultViewName);
		Long nodeId = helper.getLong("nodeId", 0L);
		String state = helper.getString("state","-1");
		String order = helper.getString("order","");
		String order_mode = helper.getString("order_mode","");
		String order_name = helper.getString("order_name","");
		String where = helper.getString("where","");
		String pubDate = request.getParameter("pubDate");
		String pubDate2 = request.getParameter("pubDate2");
		//
		String keyword =helper.getString("keyword", "").trim();
		//
		String fields = helper.getString("fields", "");
		String column_condition = "";
		//
		if (keyword != null && !keyword.equals("")) {
			if (fields != null && !fields.equals("")) {
				String columns[] = fields.split(",");
				if (columns != null) {
					for (int i = 0; i < columns.length; i++) {
						column_condition += " or " + columns[i]
								+ " like '%" + keyword + "%'";

					}
					if (!column_condition.equals("")) {
						column_condition = column_condition
								.substring(4);
						column_condition = " and ("
								+ column_condition + ")";
					}
				}
			}
		}
		//必须是未删除状态
		where+=" status<>-1 ";
		//过滤发布状态
		if(!state.equals("-1")){
			where+=" and publishState="+state;
		}
		//过滤关键字查询
		where += column_condition;
		//排序条件
		order_name = order_name.replaceAll("\\^", "");
		//
		String final_order = "";
		if (!order.equals("") && !order_mode.equals("")) {
			final_order = order + " " + order_mode;
		}
		if(!nodeId.equals(0L)){
			Node node=nodeManager.getNode(nodeId);
			model.put("node", node);
		}
		//
		int p = helper.getInt("p", 1);
		int pp = helper.getInt("pp", 30);
		int offset = (p - 1) * pp;
		QueryInfo qi = new QueryInfo(where, final_order, pp, offset);
		PageBuilder pb = new PageBuilder(pp);
		List<SurveyArea> areas = surveyAreaManager.getAreas(nodeId, qi, pb);
		//
		model.put("areas", areas);
		model.put("p", p);
		model.put("pp", pp);
		model.put("order", order);
		model.put("order_mode", order_mode);
		model.put("order_name", order_name);
		model.put("where", where);
		model.put("keyword", keyword);
		model.put("nodeId", nodeId);
		model.put("state", state);
		model.put("pubDate", pubDate);
		model.put("pubDate2", pubDate2);
		model.put("pb", pb);
		return mv;
	}

	public void setDefaultScreensPath(String defaultScreensPath) {
		this.defaultScreensPath = defaultScreensPath;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setOperationViewName(String operationViewName) {
		this.operationViewName = operationViewName;
	}

	public void setSurveyAreaManager(SurveyAreaManager surveyAreaManager) {
		this.surveyAreaManager = surveyAreaManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

}
