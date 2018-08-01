package org.larry.xz_musicplayer.utility;

import org.larry.xz_musicplayer.model.AccountModel;

public interface MyEventListener {
	void onAccountSelected(AccountModel account);

	void onRootFolderPressedBack();
}
