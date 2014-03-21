package org.ihtsdo.otf.security.dto.query;

import java.util.Map;

public class SecurityQueryDTO {
	private String queryName;
	private Map<String, String> args;

	public SecurityQueryDTO(final String queryNameIn,
			final Map<String, String> argsIn) {
		super();
		queryName = queryNameIn;
		args = argsIn;
	}

	public SecurityQueryDTO() {
		super();
	}

	public final String getQueryName() {
		return queryName;
	}

	public final void setQueryName(final String queryNameIn) {
		queryName = queryNameIn;
	}

	public final Map<String, String> getArgs() {
		return args;
	}

	public final void setArgs(final Map<String, String> argsIn) {
		args = argsIn;
	}

	@Override
	public final String toString() {
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
