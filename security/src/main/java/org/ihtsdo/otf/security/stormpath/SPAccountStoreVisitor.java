package org.ihtsdo.otf.security.stormpath;

import java.util.logging.Logger;

import com.stormpath.sdk.directory.AccountStoreVisitor;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.group.Group;

public class SPAccountStoreVisitor implements AccountStoreVisitor {
	/**
	 * <p>
	 * logger.
	 * </p>
	 */
	private static final Logger LOG = Logger
			.getLogger(SPAccountStoreVisitor.class.getName());

	public AccountStoreType type = AccountStoreType.DEFAULT;

	public static enum AccountStoreType {
		/** NO VALUE. */
		DEFAULT,
		/** A Directory */
		DIR,
		/** Group. */
		GROUP
	}

	@Override
	public void visit(Group groupIn) {
		// LOG.info("IS A GROUP");
		type = AccountStoreType.GROUP;

	}

	@Override
	public void visit(Directory directoryIn) {
		// LOG.info("IS A DIR");
		type = AccountStoreType.DIR;
	}

	public AccountStoreType getType() {
		return type;
	}

	public void setType(AccountStoreType typeIn) {
		type = typeIn;
	}

}
