package org.ihtsdo.otf.security.dto.query;

import java.util.HashMap;

public class SecurityQueryDTO {

	public SecurityQueryDTO(String queryNameIn, HashMap<String, String> argsIn) {
		super();
		queryName = queryNameIn;
		args = argsIn;
	}

	public SecurityQueryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	String queryName;
	HashMap<String, String> args;

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryNameIn) {
		queryName = queryNameIn;
	}

	public HashMap<String, String> getArgs() {
		return args;
	}

	public void setArgs(HashMap<String, String> argsIn) {
		args = argsIn;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("QueryName = ").append(getQueryName()).append("\n");
		sb.append("Query Args = \n");
		for (String key : args.keySet()) {
			String val = args.get(key);
			sb.append("Key = ").append(key).append(" Value = ").append(val)
					.append("\n");
		}

		return sb.toString();

	}

}
