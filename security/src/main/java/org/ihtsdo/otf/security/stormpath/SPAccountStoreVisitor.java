package org.ihtsdo.otf.security.stormpath;

import com.stormpath.sdk.directory.AccountStoreVisitor;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.group.Group;

public class SPAccountStoreVisitor implements AccountStoreVisitor {

	private AccountStoreType type = AccountStoreType.DEFAULT;

	public static enum AccountStoreType {
		/** NO VALUE. */
		DEFAULT,
		/** A Directory */
		DIR,
		/** Group. */
		GROUP
	}

	@Override
	public final void visit(final Group groupIn) {
		type = AccountStoreType.GROUP;
	}

	@Override
	public final void visit(final Directory directoryIn) {
		type = AccountStoreType.DIR;
	}

	public final AccountStoreType getType() {
		return type;
	}

	public final void setType(final AccountStoreType typeIn) {
		type = typeIn;
	}

}
