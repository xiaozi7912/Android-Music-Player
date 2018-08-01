package org.larry.xz_musicplayer.utility;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.ContactsContract.Directory;
import android.util.Log;

public class SQLManager {
	private static final String LOG_TAG = "SQLManager";
	private static final String DB_NAME = "XZMusicPlayer.db";
	private static final String DB_FOLDER = ".XZMusicPlayer";

	private Context mContext = null;

	public static final String TABLE_ACCOUNT = "ACCOUNT";
	public static final String ACCOUNT_ID = "_ID";
	public static final String ACCOUNT_EMAIL = "EMAIL";
	public static final String ACCOUNT_ACCESSTOKEN = "ACCESSTOKEN";
	public static final String ACCOUNT_PICTURE = "PICTURE";

	public SQLManager(Context context) {
		mContext = context;

		createTables();
		updateDatabase();
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

	private void dropTables() {
		SQLiteDatabase database = getDatabase();
		String DROP_TABLE_ACCOUNT = "DROP TABLE IF EXISTS " + TABLE_ACCOUNT;

		database.execSQL(DROP_TABLE_ACCOUNT);
		database.close();
	}

	private void createTables() {
		Log.i(LOG_TAG, "createTables");
		SQLiteDatabase database = getDatabase();
		String CREATE_TABLE_ACCOUNT = "CREATE TABLE IF NOT EXISTS " + TABLE_ACCOUNT + " (" + ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ ACCOUNT_EMAIL + " TEXT," + ACCOUNT_ACCESSTOKEN + " TEXT);";

		database.execSQL(CREATE_TABLE_ACCOUNT);
		database.close();
	}

	private boolean columnExists(String tableName, String columnName) {
		Log.i(LOG_TAG, "columnExists");
		SQLiteDatabase database = getDatabase();
		String command = "SELECT * FROM " + tableName + ";";
		Cursor cursor = database.rawQuery(command, null);
		int columnIndex = cursor.getColumnIndex(columnName);
		boolean result = (columnIndex == -1) ? false : true;
		cursor.close();
		database.close();
		return result;
	}

	private void updateDatabase() {
		Log.i(LOG_TAG, "updateDatabase");
		update0_0_1();
	}

	private void update0_0_1() {
		Log.i(LOG_TAG, "update0_0_1");
		SQLiteDatabase database = getDatabase();
		String command = null;

		if (!columnExists(TABLE_ACCOUNT, ACCOUNT_PICTURE)) {
			Log.i(LOG_TAG, "ACCOUNT_PICTURE");
			command = "ALTER TABLE " + TABLE_ACCOUNT + " ADD COLUMN " + ACCOUNT_PICTURE + " TEXT;";
			database.execSQL(command);
		}

		database.close();
	}
}
