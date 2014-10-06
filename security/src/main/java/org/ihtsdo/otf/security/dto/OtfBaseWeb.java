package org.ihtsdo.otf.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ihtsdo.otf.security.dto.customfieldmodels.OtfCustomFieldModel;

public abstract class OtfBaseWeb {
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger.getLogger(OtfBaseWeb.class
			.getName());

	@JsonIgnore
	protected Map<String, String> params;
	@JsonIgnore
	private LinkedHashSet<String> tableRows = new LinkedHashSet<String>();
	@JsonIgnore
	private Map<String, String> hiddenRows = new HashMap<String, String>();
	@JsonIgnore
	private String action;

	protected Map<String, List<String>> errors = new HashMap<String, List<String>>();

	public static final String FORM_NAME = "FormName";

	public static final String JSP_FORM_NAME = "<%=FORM_NAME%>";

	public static final String JSP_TITLE = "<%=TITLE%>";
	public static final String JSP_VALUE = "<%=VALUE%>";
	public static final String JSP_ID = "<%=ID%>";
	public static final String JSP_SUBMIT_BUTTON = "<%=SUBMIT_TEXT%>";

	public static final String CSS_TEXT_INPUT = "base_web_text_input";
	public static final String CSS_TEXT_INPUT_ERROR = "base_web_text_input_error";

	public static final String INPUT_DISABLE = " readonly = \"readonly\" ";

	public static final String CSS_TEXT_AREA = "base_web_textarea";
	public static final String CSS_TEXT_SUBMIT = "base_web_submit";
	public static final String CSS_SELECT = "base_web_select";
	public static final String CSS_TABLE = "base_web_table";
	public static final String CSS_TABLE_HEAD = "base_web_table_head";
	public static final String CSS_TABLE_ROW = "base_web_table_row";
	public static final String CSS_TABLE_CELL_LABEL = "base_web_table_cell_label";
	public static final String CSS_TABLE_CELL_INPUT = "base_web_table_cell_input";

	public static final String HTML_INPUT_HIDDEN = "<input type=\"hidden\" name=\"<%=TITLE%>\" value=\"<%=VALUE%>\" />";
	public static final String HTML_INPUT_TEXT = "<input type=\"text\" class=\"base_web_text_input\" name=\"<%=TITLE%>\" value=\"<%=VALUE%>\"/>";

	public static final String HTML_INPUT_SUBMIT = "<input type=\"submit\" class=\"base_web_submit\" name=\"submit\" value=\"<%=VALUE%>\"/>";
	public static final String HTML_INPUT_TEXT_AREA = "<textarea class=\"base_web_textarea\" name=\"<%=TITLE%>\"><%=VALUE%></textarea>";
	public static final String HTML_FORM_HEAD = "<form name=\"<%=TITLE%>\" action=\"<%=VALUE%>\" method=\"post\">";
	public static final String HTML_FORM_HEAD_ID = "<form id=\"<%=ID%>\" name=\"<%=TITLE%>\" action=\"<%=VALUE%>\" method=\"post\">";
	public static final String HTML_FORM_CLOSE = "</form>";

	public static final String HTML_DIV_HIDDEN = "<div id=\"<%=VALUE%>\" style=\"display: none;\">";

	public static final String HTML_DIV_SUB_HEAD_ID = "<div class=\"subformDiv\" id=\"<%=ID%>\" title=\"<%=TITLE%>\" >";
	public static final String HTML_DIV_ERRORS = "<div class=\"errortooltip\" >";

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
	public static final String JS_ADD_ROW_AJAX = "$.get('<%=VALUE%>', function(data) {$('#<%=TITLE%>').append(data);});";

	public static final String JS_MOUSEOVER_ERR = "$.get('<%=VALUE%>', function(data) {$('#<%=TITLE%>').append(data);});";

	public static final String INPUT_KEY_NAME = "Input_Key";

	public abstract void processParams();

	public abstract void validateParams();

	public abstract void addTableRows();

	public abstract void addHiddenRows();

	public abstract void setValsFromParams();

	@JsonIgnore
	public abstract String getTableTitle();

	public static final String getRepeatingSubForms(final String title,
			final Collection<? extends OtfBaseWeb> items) {
		StringBuilder sbuild = new StringBuilder();
		// getTableSubview
		for (OtfBaseWeb obw : items) {
			sbuild.append(obw.getRHS());
		}
		return getTableSubview(getForm(title), sbuild.toString());

	}

	public final void printParams() {
		if (params != null) {
			LOG.info("Printing Params \n");
			for (String key : params.keySet()) {
				String val = params.get(key);
				LOG.info("Key = " + key + " Val = " + val);
			}
		}
	}

	public static final String getErrorsDiv(final List<String> errors) {
		StringBuilder sbuild = new StringBuilder();

		sbuild.append(HTML_DIV_ERRORS);
		for (String error : errors) {
			sbuild.append(error).append("<br/>");
		}
		sbuild.append(HTML_DIV_CLOSE);
		return sbuild.toString();
	}

	public static final String getTableSubview(final String leftContent,
			final String rightContent) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(HTML_DIV_CONTAINER).append(HTML_DIV_ROW)
				.append(HTML_DIV_COL_LEFT);
		sbuild.append(leftContent).append(HTML_DIV_CLOSE)
				.append(HTML_DIV_COL_RIGHT);
		sbuild.append(rightContent).append(HTML_DIV_CLOSE)
				.append(HTML_DIV_CLOSE).append(HTML_DIV_CLOSE);
		return sbuild.toString();
	}

	public static final String getSubForm(final String content) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(HTML_SUB_FORM_TITLE).append(content)
				.append(HTML_SUB_FORM_TITLE_CLOSE);
		return sbuild.toString();
	}

	public static final String getForm(final String content) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(HTML_FORM_TITLE).append(content)
				.append(HTML_FORM_TITLE_CLOSE);
		return sbuild.toString();
	}

	@JsonIgnore
	protected String getHtmlForm(final String formName) {
		// clear rows
		clearRows();
		// get rows
		addTableRows();
		addHiddenRows();
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlFormHead(formName, getAction())).append("\n");
		for (String hidden : getHiddenRows().values()) {
			sbuild.append(hidden).append("\n");
		}
		sbuild.append(getHtmlTable("", getTableRows(), true)).append("\n");

		sbuild.append(HTML_FORM_CLOSE);
		return getFormDiv(sbuild.toString());
	}

	@JsonIgnore
	protected String getHtmlForm(final String formName,
			final String htmlToAppend) {
		// clear rows
		clearRows();
		// get rows
		addTableRows();
		addHiddenRows();
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlFormHead(formName, getAction())).append("\n");
		for (String hidden : getHiddenRows().values()) {
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
	public final String getSubFormHead(final String title) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getSubForm(title));
		return sbuild.toString();
	}

	public final String getHtmlForm(final String actionIn,
			final String inputKey, final String content, final String formName) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlFormHead(formName, actionIn)).append("\n");
		sbuild.append(getHtmlInputHidden(INPUT_KEY_NAME, inputKey))
				.append("\n");
		sbuild.append(content).append("\n");
		sbuild.append(HTML_FORM_CLOSE);
		return getFormDiv(sbuild.toString());
	}

	public final String getFormDiv(final String html) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(HTML_FORM_DIV_HEAD);
		sbuild.append(html);
		sbuild.append(HTML_DIV_CLOSE);
		return sbuild.toString();

	}

	public final String getHiddenDiv(final String html, final String id) {
		StringBuilder sbuild = new StringBuilder();

		sbuild.append(replaceJspValue(HTML_DIV_HIDDEN, id));
		sbuild.append(html);
		sbuild.append(HTML_DIV_CLOSE);
		return sbuild.toString();

	}

	public final String getHtmlTable(final String thead,
			final Collection<String> rows, final boolean addSubmit) {
		StringBuilder sbuild = new StringBuilder();
		for (String row : rows) {
			sbuild.append(row).append("\n");
		}
		return getHtmlTable(thead, sbuild.toString(), addSubmit);
	}

	public final String getHtmlTable(final String thead, final String rows,
			final boolean addSubmit) {
		StringBuilder sbuild = new StringBuilder();
		if (thead != null && thead.length() > 0) {
			sbuild.append(getHtmlDiv(thead, CSS_TABLE_HEAD)).append("\n");
		}
		sbuild.append(rows);
		if (addSubmit) {
			sbuild.append(getHtmlRowSubmit(getTableTitle()));
		}
		return getHtmlDiv(sbuild.toString(), CSS_TABLE);
	}

	public final String getHtmlRow(final Collection<String> cells) {
		StringBuilder sbuild = new StringBuilder();
		for (String cell : cells) {
			sbuild.append(cell).append("\n");
		}
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRow(final String label, final String value) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlLabelCell(label)).append("\n");
		sbuild.append(getHtmlInputCell(value)).append("\n");
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRow(final String value) {
		return getHtmlDiv(value, CSS_TABLE_ROW);
	}

	public final String getHtmlRowSubmit(final String value) {

		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlDiv("", CSS_TABLE_CELL_LABEL));
		sbuild.append(getHtmlDiv(getHtmlInputSubmit(value), CSS_TEXT_SUBMIT));
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRowPlainText(final String label,
			final String value) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlLabelCell(label)).append("\n");
		sbuild.append(getHtmlInputCell(value)).append("\n");
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRowTextInput(final String label,
			final String value, final List<String> errorsIn) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlLabelCell(label)).append("\n");
		String inputT = getHtmlInputText(label, value);
		if (errorsIn != null && !errorsIn.isEmpty()) {
			LOG.info("errors found for label " + label);
			inputT = inputT.replace(CSS_TEXT_INPUT, CSS_TEXT_INPUT_ERROR);
			inputT = inputT + "\n" + getErrorsDiv(errorsIn);
			LOG.info("inputT = " + inputT);
		}
		sbuild.append(getHtmlInputCell(inputT)).append("\n");
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRowTextArea(final String label,
			final String value, final String name) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(getHtmlLabelCell(label)).append("\n");
		sbuild.append(getHtmlInputCell(getHtmlInputTextArea(value, name)))
				.append("\n");
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlRowOptions(final String label,
			final List<String> vals, final String selval, final String name,
			final String onclickJS, final String id) {
		StringBuilder sbuild = new StringBuilder();
		if (stringOK(label)) {
			sbuild.append(getHtmlLabelCell(label)).append("\n");
		}
		sbuild.append(
				getHtmlInputCell(getHtmlOptions(vals, selval, name, onclickJS,
						id))).append("\n");
		return getHtmlDiv(sbuild.toString(), CSS_TABLE_ROW);
	}

	public final String getHtmlOptions(final List<String> vals,
			final String selval, final String name, final String onclickJS,
			final String id) {
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

	public final String getHtmlOption(final String val, final String selval) {
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

	public final String getHtmlDiv(final String content, final String cssClass) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("<div class=\"").append(cssClass).append("\">");
		sbuild.append(content).append("</div>");
		return sbuild.toString();
	}

	public final String getHtmlAddRowBtn(final String btnTitle,
			final String onClickAction) {
		return replaceJspTitleValue(HTML_ADD_ROW_BTN, onClickAction, btnTitle);
	}

	public final String getJavaScriptAddRow(final String formID,
			final String getUrl) {
		return replaceJspTitleValue(JS_ADD_ROW_AJAX, getUrl, formID);
	}

	public final String getNewCfModelElement(final OtfCustomFieldModel cfm,
			final String cssClass) {
		return getHtmlRemBtnAction(getCDTable(cfm.getLabelValuesMap()),
				cssClass);

	}

	public final String getHtmlRemBtnAction(final String content,
			final String cssClass) {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(content);
		sbuild.append(HTML_REM_PARENT_BTN);
		return getHtmlDiv(sbuild.toString(), cssClass);
	}

	public final String replaceJspTitleValue(final String in,
			final String replVal, final String replTitle) {
		String val = replaceJspValue(in, replVal);
		return replaceJspTitle(val, replTitle);
	}

	public final String replaceJspTitleId(final String in, final String replId,
			final String replTitle) {
		String val = replaceJspId(in, replId);
		return replaceJspTitle(val, replTitle);
	}

	public final String replaceJspTitleValueId(final String in,
			final String replVal, final String replTitle, final String replId) {
		String val = replaceJspTitleValue(in, replVal, replTitle);
		return replaceJspId(val, replId);
	}

	public final String replaceJspValue(final String in, final String replVal) {
		return replaceStr(JSP_VALUE, replVal, in);
	}

	public final String replaceJspTitle(final String in, final String replVal) {
		return replaceStr(JSP_TITLE, replVal, in);
	}

	public final String replaceJspId(final String in, final String replVal) {
		return replaceStr(JSP_ID, replVal, in);
	}

	public final String getCDTable(final Map<String, String> rowsMap) {
		List<String> rows = new ArrayList<String>();
		for (String key : rowsMap.keySet()) {
			String content = rowsMap.get(key);
			rows.add(getHtmlRowPlainText(key, content));
		}
		return getHtmlTable("", rows, false);
	}

	public final String replaceStr(final String target,
			final String replacement, String source) {
		while ((source != null) && (source.indexOf(target) >= 0)) {
			source = source.replace(target, replacement);
		}
		return source;
	}

	public final String getAposText(final String initT) {
		return new StringBuilder().append("\"").append(initT).append("\"")
				.toString();
	}

	public final void clearRows() {
		setTableRows(null);
		setHiddenRows(null);
		getTableRows();
		getHiddenRows();
	}

	public final LinkedHashSet<String> getTableRows() {
		if (tableRows == null) {
			tableRows = new LinkedHashSet<String>();
		}
		return tableRows;
	}

	public final void setTableRows(final LinkedHashSet<String> rowsIn) {
		tableRows = rowsIn;
	}

	public final Map<String, String> getParams() {
		return params;
	}

	public final void setParams(final Map<String, String> paramsIn) {
		params = paramsIn;
	}

	public final Map<String, String> getHiddenRows() {
		if (hiddenRows == null) {
			hiddenRows = new HashMap<String, String>();
		}
		return hiddenRows;
	}

	public final void setHiddenRows(final Map<String, String> hiddenRowsIn) {
		hiddenRows = hiddenRowsIn;
	}

	public final String getAction() {
		if (action == null) {
			action = "ActionNOTSet";
		}
		return action;
	}

	public final void setAction(final String actionIn) {
		action = actionIn;
	}

	public static final boolean stringOK(final String toCheck) {
		return toCheck != null && !toCheck.isEmpty();
	}

	@JsonIgnore
	public final Map<String, List<String>> getErrors() {
		if (errors == null) {
			errors = new HashMap<String, List<String>>();
		}
		return errors;
	}

	public final void setErrors(final Map<String, List<String>> errorsIn) {
		errors = errorsIn;
	}

	public final void logErrors() {
		StringBuilder sbuild = new StringBuilder();
		sbuild.append("Log Errors :\n");
		for (String key : errors.keySet()) {
			sbuild.append("Input Control name = ").append(key).append("\n");
			for (String msg : errors.get(key)) {
				sbuild.append("Error Message = ").append(msg).append("\n");
			}
		}
		LOG.info(sbuild.toString());
	}

	public final void resetErrors() {
		errors = new HashMap<String, List<String>>();
	}

	public final void addError(final String webName, final String errormessage) {
		List<String> erWn = errors.get(webName);
		if (erWn == null) {
			erWn = new ArrayList<String>();
		}
		erWn.add(errormessage);
		errors.put(webName, erWn);
	}

	public final void checkWebFieldNotEmpty(final String toCheck,
			final String webName) {
		if (!stringOK(toCheck)) {
			addError(webName, "Can't be empty");
		}
	}

	public final void checkWebFieldInList(final String toCheck,
			final String webName, final Collection<String> vals,
			final boolean inList, final String errMsg) {
		boolean listContains = vals.contains(toCheck);
		boolean err = (listContains != inList);
		if (err) {
			addError(webName, errMsg);
		}
	}

	public final String getNotNullParam(final String pname) {

		String val = getParams().get(pname);
		if (stringOK(val)) {
			return val;
		} else {
			return "";
		}
	}

}
