package org.larry.xz_musicplayer.utility;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.ContactsContract.Directory;
import android.util.Log;

public class SQLManager {
	private static final String LOG_TAG = "SQLManager";
	private static final String DB_NAME = "XZMusicPlayer.db";
	private static final String DB_FOLDER = ".XZMusicPlayer";

	private Context mContext = null;
	private SQLiteDatabase mDatabase = null;

	public static final String TABLE_ACCOUNT = "ACCOUNT";
	public static final String ACCOUNT_ID = "_ID";
	public static final String ACCOUNT_EMAIL = "EMAIL";
	public static final String ACCOUNT_ACCESSTOKEN = "ACCESSTOKEN";
	public static final String ACCOUNT_PICTURE = "PICTURE";

	public SQLManager(Context context) {
		mContext = context;
		mDatabase = getDatabase();
		createTables();
	}

	public static SQLiteDatabase getDatabase() {
		return SQLiteDatabase.openOrCreateDatabase(getDatabaseFile(), null);
	}

	public AccountController Account() {
		return new AccountController();
	}

	private static File getDatabaseFile() {
		Log.i(LOG_TAG, "getDatabaseFile");
		File rootDir = new File(Environment.getExternalStorageDirectory(), DB_FOLDER);
		File resultFile = new File(rootDir, DB_NAME);

		try {
			rootDir.mkdirs();
			if (!resultFile.exists()) {
				resultFile.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(LOG_TAG, e.getMessage());
		}

		Log.v(LOG_TAG, "DB_NAME : " + DB_NAME);
		Log.v(LOG_TAG, "DB_FOLDER : " + DB_FOLDER);
		Log.v(LOG_TAG, "rootDir.getAbsolutePath : " + rootDir.getAbsolutePath());
		Log.i(LOG_TAG, "--------------------------------------------------");
		return resultFile;
	}

	private void createTables() {
		String CREATE_TABLE_ACCOUNT = "CREATE TABLE IF NOT EXISTS " + TABLE_ACCOUNT + " (" + ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ ACCOUNT_EMAIL + " TEXT," + ACCOUNT_ACCESSTOKEN + " TEXT);";

		mDatabase.execSQL(CREATE_TABLE_ACCOUNT);
	}

	private void updateDatabase() {
		update0_0_1();
	}

	private void update0_0_1() {
		
	}
}
