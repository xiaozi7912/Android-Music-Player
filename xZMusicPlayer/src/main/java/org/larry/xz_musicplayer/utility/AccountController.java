package org.larry.xz_musicplayer.utility;

import java.io.File;
import java.util.ArrayList;

import org.larry.xz_musicplayer.R.id;
import org.larry.xz_musicplayer.model.AccountModel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AccountController {
	private static final String LOG_TAG = "AccountController";

	public long insert(ContentValues values) {
		SQLiteDatabase database = SQLManager.getDatabase();
		long result = database.insert(SQLManager.TABLE_ACCOUNT, null, values);
		database.close();
		return result;
	}

	public int delete(int id) {
		SQLiteDatabase database = SQLManager.getDatabase();
		String command = String.format("%s=%s", SQLManager.ACCOUNT_ID, id);
		int result = database.delete(SQLManager.TABLE_ACCOUNT, command, null);
		database.close();
		return result;
	}

	public int update(int id, ContentValues values) {
		SQLiteDatabase database = SQLManager.getDatabase();
		String command = String.format("%s=%s", SQLManager.ACCOUNT_ID, id);
		int result = database.update(SQLManager.TABLE_ACCOUNT, values, command, null);
		database.close();
		return result;
	}

	public ArrayList<AccountModel> list() {
		SQLiteDatabase database = SQLManager.getDatabase();
		ArrayList<AccountModel> resultList = new ArrayList<AccountModel>();
		String[] columns = { SQLManager.ACCOUNT_ID, SQLManager.ACCOUNT_EMAIL, SQLManager.ACCOUNT_ACCESSTOKEN, SQLManager.ACCOUNT_PICTURE };
		Cursor cursor = database.query(SQLManager.TABLE_ACCOUNT, columns, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				AccountModel nAccount = new AccountModel();
				nAccount.id = cursor.getInt(cursor.getColumnIndex(SQLManager.ACCOUNT_ID));
				nAccount.email = cursor.getString(cursor.getColumnIndex(SQLManager.ACCOUNT_EMAIL));
				nAccount.accessToken = cursor.getString(cursor.getColumnIndex(SQLManager.ACCOUNT_ACCESSTOKEN));
				nAccount.picture = cursor.getString(cursor.getColumnIndex(SQLManager.ACCOUNT_PICTURE));
				resultList.add(nAccount);
			} while (cursor.moveToNext());
		}
		cursor.close();
		database.close();
		return resultList;
	}

	public AccountModel get(String email) {
		SQLiteDatabase database = SQLManager.getDatabase();
		AccountModel result = null;
		String[] columns = { SQLManager.ACCOUNT_ID, SQLManager.ACCOUNT_EMAIL, SQLManager.ACCOUNT_ACCESSTOKEN, SQLManager.ACCOUNT_PICTURE };
		String command = String.format("%s='%s'", SQLManager.ACCOUNT_EMAIL, email);
		Cursor cursor = database.query(SQLManager.TABLE_ACCOUNT, columns, command, null, null, null, null);
		int resultCount = cursor.getCount();

		if (resultCount != 0) {
			result = new AccountModel();
			if (cursor.moveToFirst()) {
				result.id = cursor.getInt(cursor.getColumnIndex(SQLManager.ACCOUNT_ID));
				result.email = cursor.getString(cursor.getColumnIndex(SQLManager.ACCOUNT_EMAIL));
				result.accessToken = cursor.getString(cursor.getColumnIndex(SQLManager.ACCOUNT_ACCESSTOKEN));
				result.picture = cursor.getString(cursor.getColumnIndex(SQLManager.ACCOUNT_PICTURE));
			}
		}
		cursor.close();
		database.close();
		return result;
	}
}
