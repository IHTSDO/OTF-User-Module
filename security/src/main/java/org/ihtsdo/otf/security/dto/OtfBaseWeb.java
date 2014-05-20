package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class OtfBaseWeb {
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfBaseWeb.class
			.getName());

	@JsonIgnore
	private Map<String, String> params;
	@JsonIgnore
	private List<String> tableRows = new ArrayList<String>();
	@JsonIgnore
	private List<String> hiddenRows = new ArrayList<String>();
	@JsonIgnore
	private String action;

	public static final String FORM_NAME = "FormName";

	public static final String JSP_FORM_NAME = "<%=FORM_NAME%>";

	public static final String JSP_TITLE = "<%=TITLE%>";
	public static final String JSP_VALUE = "<%=VALUE%>";
	public static final String JSP_ID = "<%=ID%>";
	public static final String JSP_SUBMIT_BUTTON = "<%=SUBMIT_TEXT%>";

	public static final String CSS_TEXT_INPUT = "base_web_text_input";
	public static final String CSS_TEXT_AREA = "base_web_textarea";
	public static final String CSS_TEXT_SUBMIT = "base_web_submit";
	public static final String CSS_SELECT = "base_web_select";
	public static final String CSS_TABLE = "base_web_table";
	public static final String CSS_TABLE_HEAD = "base_web_table_head";
	public static final String CSS_TABLE_ROW = "base_web_table_row";
	public static final String CSS_TABLE_CELL_LABEL = "base_web_table_cell_label";
	public static final String CSS_TABLE_CELL_INPUT = "base_web_table_cell_input";

	public static final String HTML_INPUT_HIDDEN = "<input type=\"hidden\" name=\"<%=TITLE%>\" value=\"<%=VALUE%>\" />";
	public static final String HTML_INPUT_TEXT = "<input type=\"text\" class=\"base_web_text_input\" name=\"<%=TITLE%>\" value=\"<%=VALUE%>\">";

	public static final String HTML_INPUT_SUBMIT = "<input type=\"submit\" class=\"base_web_submit\" name=\"submit\" value=\"<%=VALUE%>\">";
	public static final String HTML_INPUT_TEXT_AREA = "<textarea class=\"base_web_textarea\" name=\"<%=TITLE%>\"><%=VALUE%></textarea>";
	public static final String HTML_FORM_HEAD = "<form name=\"<%=TITLE%>\" action=\"<%=VALUE%>\" method=\"post\">";
	public static final String HTML_FORM_HEAD_ID = "<form id=\"<%=ID%>\" name=\"<%=TITLE%>\" action=\"<%=VALUE%>\" method=\"post\">";
	public static final String HTML_FORM_CLOSE = "</form>";

	public static final String HTML_DIV_HIDDEN = "<div id=\"<%=VALUE%>\" style=\"display: none;\">";

	public static final String HTML_DIV_SUB_HEAD_ID = "<div class=\"subformDiv\" id=\"<%=ID%>\" title=\"<%=TITLE%>\" >";

	public static final String HTML_FORM_DIV_HEAD = "<div class=\"formDiv\">";
	public static final String HTML_DIV_CLOSE = "</div>";

	public static final String HTML_FORM_TITLE = "<h1 class=\"blue\">";
	public static final String HTML_FORM_TITLE_CLOSE = "</h1>";

	public static final String HTML_SUB_FORM_TITLE = "<h2 class=\"blue\">";
	public static final String HTML_SUB_FORM_TITLE_CLOSE = "</h2>";

	public static final String HTML_DIV_CONTAINER = "<div class=\"container\">";
	public static final String HTML_DIV_ROW = "<div class=\"row\">";
	public static final String HTML_DIV_COL_LEFT = "<div class=\"columnLeft\">";
	public static final String HTML_DIV_COL_RIGHT = "<div class=\"columnRight\">";

	public static final String HTML_REM_PARENT_BTN = "<input type=\"button\" value=\"Remove\" onclick=\"$(this).parent().remove();\">";
	public static final String HTML_ADD_ROW_BTN = "<input type=\"button\" value=\"<%=TITLE%>\" onclick=\"<%=VALUE%>\">";

	public static final String JS_ADD_ROW = "jQuery('#<%=TITLE%>').append(document.getElementById('<%=VALUE%>').innerHTML);";

	public static final String INPUT_KEY_NAME = "Input_Key";

	public abstract void processParams(Map<String, String> params);

	public abstract void addTableRows();

	public abstract void addHiddenRows();

	@JsonIgnore
	public abstract String getTableTitle();

	public static final String getRepeatingSubForms(String title,
			Collection<? extends OtfBaseWeb> items) {
		StringBuilder sbuild = new StringBuilder();
		// sbuild.append(getSubForm(title));
		// getTableSubview
		for (OtfBaseWeb obw : items) {
			sbuild.append(obw.getRHS());
		}
		return getTableSubview(getForm(title), sbuild.toString());

	}

	public static final String getTableSubview(String leftContent,
			String rightContent) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(HTML_DIV_CONTAINER).append(HTML_DIV_ROW)
				.append(HTML_DIV_COL_LEFT);
		sbuild.append(leftContent).append(HTML_DIV_CLOSE)
				.append(HTML_DIV_COL_RIGHT);
		sbuild.append(rightContent).append(HTML_DIV_CLOSE)
				.append(HTML_DIV_CLOSE).append(HTML_DIV_CLOSE);
		return sbuild.toString();
	}

	public static final String getSubForm(String content) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(HTML_SUB_FORM_TITLE).append(content)
				.append(HTML_SUB_FORM_TITLE_CLOSE);
		return sbuild.toString();
	}

	public static final String getForm(String content) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(HTML_FORM_TITLE).append(content)
				.append(HTML_FORM_TITLE_CLOSE);
		return sbuild.toString();
	}

	// /remember to add hidden field inc id
	@JsonIgnore
	protected String getHtmlForm(String formName) {
		// clear rows
		clearRows();
		// get rows
		addTableRows();
		addHiddenRows();
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlFormHead(formName, getAction())).append("\n");
		for (String hidden : getHiddenRows()) {
			sbuild.append(hidden).append("\n");
		}
		sbuild.append(getHtmlTable("", getTableRows(), true)).append("\n");

		sbuild.append(HTML_FORM_CLOSE);
		return getFormDiv(sbuild.toString());
	}

	@JsonIgnore
	protected String getHtmlForm(String formName, String htmlToAppend) {
		// clear rows
		clearRows();
		// get rows
		addTableRows();
		addHiddenRows();
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlFormHead(formName, getAction())).append("\n");
		for (String hidden : getHiddenRows()) {
			sbuild.append(hidden).append("\n");
		}
		sbuild.append(getHtmlTable("", getTableRows(), true)).append("\n");
		sbuild.append(htmlToAppend);
		sbuild.append(HTML_FORM_CLOSE);
		return getFormDiv(sbuild.toString());
	}

	@JsonIgnore
	public final String getRHS() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getForm(getTableTitle()));
		sbuild.append(getHtmlForm(getTableTitle()));

		return sbuild.toString();
	}

	@JsonIgnore
	public final String getSubFormHead(String title) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getSubForm(title));
		return sbuild.toString();
	}

	public final String getHtmlForm(String action, String inputKey,
			String content, String formName) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlFormHead(formName, action)).append("\n");
		sbuild.append(getHtmlInputHidden(INPUT_KEY_NAME, inputKey))
				.append("\n");
		sbuild.append(content).append("\n");
		sbuild.append(HTML_FORM_CLOSE);
		return getFormDiv(sbuild.toString());
	}

	public final String getFormDiv(String html) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(HTML_FORM_DIV_HEAD);
		sbuild.append(html);
		sbuild.append(HTML_DIV_CLOSE);
		return sbuild.toString();

	}

	public final String getHiddenDiv(String html, String id) {
		StringBuilder sbuild = new StringBuilder();

		sbuild.append(replaceJspValue(HTML_DIV_HIDDEN, id));
		sbuild.append(html);
		sbuild.append(HTML_DIV_CLOSE);
		return sbuild.toString();

	}

	public final String getHtmlTable(final String thead, List<String> rows,
			boolean addSubmit) {
		StringBuilder sbuild = new StringBuilder();
		if (thead != null && thead.length() > 0) {
			sbuild.append(getHtmlDiv(thead, CSS_TABLE_HEAD)).append("\n");
		}
		for (String row : rows) {
			sbuild.append(row).append("\n");
		}
		if (addSubmit) {
			sbuild.append(getHtmlRowSubmit(getTableTitle()));
		}
		return getHtmlDiv(sbuild.toString(), CSS_TABLE);
	}

	public final String getHtmlRow(List<String> cells) {
		StringBuilder sbuild = new StringBuilder();
		for (String cell : cells) {
			sbuild.append(cell).append("\n");
		}
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRow(String label, String value) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlLabelCell(label)).append("\n");
		sbuild.append(getHtmlInputCell(value)).append("\n");
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRow(String value) {
		return getHtmlDiv(value, CSS_TABLE_ROW);
	}

	public final String getHtmlRowSubmit(String value) {

		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlDiv("", CSS_TABLE_CELL_LABEL));
		sbuild.append(getHtmlDiv(getHtmlInputSubmit(value), CSS_TEXT_SUBMIT));
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRowPlainText(String label, String value) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlLabelCell(label)).append("\n");
		sbuild.append(getHtmlInputCell(value)).append("\n");
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRowTextInput(String label, String value) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlLabelCell(label)).append("\n");
		sbuild.append(getHtmlInputCell(getHtmlInputText(label, value))).append(
				"\n");
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRowTextArea(String label, String value,
			String name) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlLabelCell(label)).append("\n");
		sbuild.append(getHtmlInputCell(getHtmlInputTextArea(value, name)))
				.append("\n");
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRowOptions(String label, List<String> vals,
			String selval, String name, String onclickJS, String id) {
		StringBuilder sbuild = new StringBuilder();
		if (stringOK(label)) {
			sbuild.append(getHtmlLabelCell(label)).append("\n");
		}
		sbuild.append(
				getHtmlInputCell(getHtmlOptions(vals, selval, name, onclickJS,
						id))).append("\n");
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	// public final String getHtmlRowOptionsNoLabel(List<String> vals,
	// String selval, String name) {
	// StringBuilder sbuild = new StringBuilder();
	// sbuild.append(getHtmlInputCell(getHtmlOptions(vals, selval, name)))
	// .append("\n");
	// return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	// }

	public final String getHtmlOptions(List<String> vals, String selval,
			String name, String onclickJS, String id) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<select class=\"").append(CSS_SELECT)
				.append("\" name=\"").append(name).append("\"");

		if (stringOK(id)) {
			sbuild.append(" id=\"").append(id).append("\"");
		}
		if (stringOK(onclickJS)) {
			sbuild.append(" onchange=\"").append(onclickJS).append("\"");
		}
		sbuild.append(" >");
		for (String cell : vals) {
			sbuild.append(getHtmlOption(cell, selval)).append("\n");
		}
		sbuild.append("</select>");
		return sbuild.toString();
	}

	public final String getHtmlOption(String val, String selval) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<option");
		if (val.equals(selval)) {
			sbuild.append(" selected=\"selected\" ");
		}
		sbuild.append(">").append(val).append("</option>");
		return sbuild.toString();

	}

	public final String getHtmlLabelCell(final String value) {
		return getHtmlDiv(value, CSS_TABLE_CELL_LABEL);
	}

	public final String getHtmlInputCell(final String value) {
		return getHtmlDiv(value, CSS_TABLE_CELL_INPUT);
	}

	public final String getHtmlInputText(final String title, final String value) {
		return replaceJspTitleValue(HTML_INPUT_TEXT, value, title);
	}

	public final String getHtmlInputHidden(final String title,
			final String value) {
		return replaceJspTitleValue(HTML_INPUT_HIDDEN, value, title);
	}

	public final String getHtmlFormHead(final String title, final String value) {
		return replaceJspTitleValue(HTML_FORM_HEAD, value, title);
	}

	public final String getHtmlFormHeadId(final String id, final String title,
			final String value) {
		return replaceJspTitleValueId(HTML_FORM_HEAD_ID, value, title, id);
	}

	public final String getHtmlDivHeadId(final String id, final String title) {
		return replaceJspTitleId(HTML_DIV_SUB_HEAD_ID, id, title);
	}

	public final String getHtmlInputTextArea(final String value,
			final String name) {
		return replaceJspTitleValue(HTML_INPUT_TEXT_AREA, value, name);
	}

	public final String getHtmlInputSubmit(final String value) {
		return replaceJspValue(HTML_INPUT_SUBMIT, value);
	}

	public final String getHtmlDiv(String content, String cssClass) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<div class=\"").append(cssClass).append("\">");
		sbuild.append(content).append("</div>");
		return sbuild.toString();
	}

	public final String getHtmlAddRowBtn(String btnTitle, String onClickAction) {
		// LOG.info("getHtmlAddRowBtn title = " + btnTitle + " onClickAction = "
		// + onClickAction);
		return replaceJspTitleValue(HTML_ADD_ROW_BTN, onClickAction, btnTitle);
	}

	public final String getJavaScriptAddRow(String formID, String hiddenhtml) {
		// LOG.info("getJavaScriptAddRow formID = " + formID + " rowToAppend = "
		// + rowToAppend);
		return replaceJspTitleValue(JS_ADD_ROW, hiddenhtml, formID);

	}

	public final String getHtmlRemBtnAction(String content, String cssClass) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(content);
		sbuild.append(HTML_REM_PARENT_BTN);
		return getHtmlDiv(sbuild.toString(), cssClass);
	}

	public final String replaceJspTitleValue(String in, String replVal,
			String replTitle) {
		String val = replaceJspValue(in, replVal);
		return replaceJspTitle(val, replTitle);
	}

	public final String replaceJspTitleId(String in, String replId,
			String replTitle) {
		String val = replaceJspId(in, replId);
		return replaceJspTitle(val, replTitle);
	}

	public final String replaceJspTitleValueId(String in, String replVal,
			String replTitle, String replId) {
		String val = replaceJspTitleValue(in, replVal, replTitle);
		return replaceJspId(val, replId);
	}

	public final String replaceJspValue(String in, String replVal) {
		return replaceStr(JSP_VALUE, replVal, in);
	}

	public final String replaceJspTitle(String in, String replVal) {
		return replaceStr(JSP_TITLE, replVal, in);
	}

	public final String replaceJspId(String in, String replVal) {
		return replaceStr(JSP_ID, replVal, in);
	}

	public final String replaceStr(final String target,
			final String replacement, String source) {
		while ((source != null) && (source.indexOf(target) >= 0)) {
			source = source.replace(target, replacement);
		}
		return source;
	}

	public final String getAposText(String initT) {
		return new StringBuilder().append("\"").append(initT).append("\"")
				.toString();
	}

	public final void clearRows() {
		setTableRows(null);
		setHiddenRows(null);
		getTableRows();
		getHiddenRows();
	}

	public final List<String> getTableRows() {
		if (tableRows == null) {
			tableRows = new ArrayList<String>();
		}
		return tableRows;
	}

	public final void setTableRows(List<String> rowsIn) {
		tableRows = rowsIn;
	}

	public final Map<String, String> getParams() {
		return params;
	}

	public final void setParams(Map<String, String> paramsIn) {
		params = paramsIn;
	}

	public final List<String> getHiddenRows() {
		if (hiddenRows == null) {
			hiddenRows = new ArrayList<String>();
		}
		return hiddenRows;
	}

	public final void setHiddenRows(List<String> hiddenRowsIn) {
		hiddenRows = hiddenRowsIn;
	}

	public final String getAction() {
		if (action == null) {
			action = "ActionNOTSet";
		}
		return action;
	}

	public final void setAction(String actionIn) {
		action = actionIn;
	}

	public static final boolean stringOK(final String toCheck) {
		return toCheck != null && toCheck.length() > 0;
	}

}
