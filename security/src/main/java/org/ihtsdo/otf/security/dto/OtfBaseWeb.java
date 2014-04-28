package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
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
	public static final String HTML_FORM_HEAD = "<form name=\"UserManform\" action=\"<%=VALUE%>\" method=\"post\">";

	public static final String INPUT_KEY_NAME = "Input_Key";

	public abstract void processParams(Map<String, String> params);

	public abstract void addTableRows();

	public abstract void addHiddenRows();

	@JsonIgnore
	public abstract String getTableTitle();

	// /remember to add hidden field inc id
	@JsonIgnore
	protected String getHtmlForm() {
		// clear rows
		clearRows();
		// get rows
		addTableRows();
		addHiddenRows();
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlFormHead(getAction())).append("\n");
		for (String hidden : getHiddenRows()) {
			sbuild.append(hidden).append("\n");
		}
		sbuild.append(getHtmlTable("", getTableRows())).append("\n");

		sbuild.append("</form>");
		return sbuild.toString();
	}

	@JsonIgnore
	public final String getRHS() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<h1 class=\"blue\">").append(getTableTitle())
				.append("</h1>");
		sbuild.append(getHtmlForm());

		return sbuild.toString();
	}

	public final String getHtmlForm(String action, String inputKey,
			String content) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlFormHead(action)).append("\n");
		sbuild.append(getHtmlInputHidden(inputKey, INPUT_KEY_NAME))
				.append("\n");
		sbuild.append(content).append("\n");
		sbuild.append("</form>");
		return sbuild.toString();
	}

	public final String getHtmlTable(final String thead, List<String> rows) {
		StringBuilder sbuild = new StringBuilder();
		if (thead != null && thead.length() > 0) {
			sbuild.append(getHtmlDiv(thead, CSS_TABLE_HEAD)).append("\n");
		}
		for (String row : rows) {
			sbuild.append(row).append("\n");
		}
		sbuild.append(getHtmlRowSubmit(getTableTitle()));
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

	public final String getHtmlRowTextInput(String label, String value) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlLabelCell(label)).append("\n");
		sbuild.append(getHtmlInputCell(getHtmlInputText(value, label))).append(
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
			String selval, String name) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlLabelCell(label)).append("\n");
		sbuild.append(getHtmlInputCell(getHtmlOptions(vals, selval, name)))
				.append("\n");
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlOptions(List<String> vals, String selval,
			String name) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<select class=\"").append(CSS_SELECT)
				.append("\" name=\"").append(name).append("\">");
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

	public final String getHtmlInputText(final String value, final String title) {
		return replaceJspTitleValue(HTML_INPUT_TEXT, value, title);
	}

	public final String getHtmlInputHidden(final String value,
			final String title) {
		return replaceJspTitleValue(HTML_INPUT_HIDDEN, value, title);
	}

	public final String getHtmlFormHead(final String value) {
		return replaceJspValue(HTML_FORM_HEAD, value);
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

	public final String replaceJspTitleValue(String in, String replVal,
			String replTitle) {
		String val = replaceJspValue(in, replVal);
		return replaceJspTitle(val, replTitle);
	}

	public final String replaceJspValue(String in, String replVal) {
		return replaceStr(JSP_VALUE, replVal, in);
	}

	public final String replaceJspTitle(String in, String replVal) {
		return replaceStr(JSP_TITLE, replVal, in);
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

}
