package com.example.ipcplayer.cursorloader;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

public class RiverContentProvider extends ContentProvider {

	public static final Uri CONTENT_URI = Uri
			.parse("content://com.easymorse.cp.rivers");

	public static final String _ID = "_id";

	public static final String NAME = "name";

	public static final String LENGTH = "length";

	public static final String INTRODUCTION = "introduction";

	private static SQLiteDatabase database;

	private static final int DATABASE_VERSION = 3;

	private static final List<River> RIVERS = new ArrayList<River>();

	static {
		River river = new River(
				"闀挎睙",
				6380,
				"闀挎睙锛堝彜绉版睙銆佸ぇ姹熺瓑锛変綅浜庝腑鍥藉鍐咃紝鍙戞簮浜庝腑鍥介潚娴风渷鍞愬彜鎷夊北鍚勬媺涓逛笢闆北鐨勫鏍硅开濡傚啺宸濅腑锛屾槸涓浗銆佷簹娲茬涓€鍜屼笘鐣岀涓夊ぇ锛堝悓鏃朵篃鏄暱锛夋渤娴侊紝鍏堕暱搴︿粎娆′簬灏肩綏娌冲強浜氶┈閫婃渤锛岃秴杩囧湴鐞冨崐寰勩€�");
		RIVERS.add(river);

		river = new River(
				"榛勬渤",
				5464,
				"涓浗鍙や唬绉版渤锛屽彂婧愪簬涓浗闈掓捣鐪佸反棰滃杸鎷夊北鑴夛紝娴佺粡闈掓捣銆佸洓宸濄€佺敇鑲冦€佸畞澶忋€佸唴钂欏彜銆侀檿瑗裤€佸北瑗裤€佹渤鍗椼€佸北涓�9涓渷鍖猴紝鏈€鍚庝簬灞变笢鐪佷笢钀ュ競鍨﹀埄鍘挎敞鍏ユ袱娴凤紝鏄腑鍥界浜岄暱娌筹紝浠呮浜庨暱姹燂紝涔熸槸涓栫晫绗竷闀挎渤娴併€�");
		RIVERS.add(river);

		river = new River("杈芥渤", 1390, "杈芥渤鏄腑鍗庝汉姘戝叡鍜屽浗涓滃寳鍦板尯鍗楅儴鐨勫ぇ娌筹紝娴佺粡娌冲寳銆佸唴钂欏彜銆佸悏鏋椼€佽窘瀹佺瓑鐪佸尯銆�");
		RIVERS.add(river);

		river = new River(
				"娴锋渤",
				73,
				"娴锋渤鐢变簬鍦板娍涓嶉珮锛屾瘡褰撴捣涓定娼椂锛屾渤姘村€掓祦锛岃惤娼椂娌虫按椤烘祦锛屽拰娴锋按鐨勬疆姹愮浉鍚岋紝鍥犳鍙栧悕涓衡€滄捣鈥濇渤銆傛捣娌虫敮娴佺殑姘稿畾娌冲師鍚嶁€滄棤瀹氭渤鈥濓紝灏辨槸鍥犱负娌抽亾缁忓父鏀瑰彉锛屽彧鏄殗甯濅负浜嗙鎰垮畠涓嶅啀娉涙互鎵嶄汉涓烘敼鍚嶄负姘稿畾娌炽€�");
		RIVERS.add(river);

		river = new River(
				"娣渤",
				1000,
				"娣渤鍙戞簮浜庢渤鍗楃渷妗愭煆灞辫€侀甫鍙夛紝涓滄祦缁忔渤鍗楋紝瀹夊窘锛屾睙鑻忎笁鐪侊紝娣渤涓嬫父姘村垎涓夎矾銆備富娴侀€氳繃涓夋渤闂革紝鍑轰笁娌筹紝缁忓疂搴旀箹銆侀珮閭箹鍦ㄤ笁姹熻惀鍏ラ暱姹燂紝鏄负鍏ユ睙姘撮亾锛岃嚦姝ゅ叏闀跨害1,000鍏噷锛涘彟涓€璺湪娲辰婀栦笢宀稿嚭楂樿壇娑ч椄锛岀粡鑻忓寳鐏屾簤鎬绘笭鍦ㄦ墎鎷呮腐鍏ラ粍娴凤紱绗笁璺湪娲辰婀栦笢鍖楀哺鍑轰簩娌抽椄锛岀粡娣箔娌冲寳涓婅繛浜戞腐甯傦紝缁忎复娲彛娉ㄥ叆娴峰窞婀俱€�2003骞村紑閫氭樊娌冲叆娴锋按閬擄紝鑷簩娌抽椄涓嬫父锛岀揣璐磋嫃鍖楃亴婧夋€绘笭鍖楀哺鍏ユ捣銆�");
		RIVERS.add(river);

		river = new River(
				"榛戦緳姹�",
				4444,
				"鍙戞簮浜庤挋鍙ゅ浗鑲壒灞变笢楹擄紝鍦ㄧ煶鍕掑杸娌充笌棰濆皵鍙ょ撼娌充氦姹囧褰㈡垚銆傜粡杩囦腑鍥介粦榫欐睙鐪佸寳鐣屼笌淇勭綏鏂搱宸寸綏澶柉鍏嬭竟鐤嗗尯涓滃崡鐣岋紝娴佸叆閯傞湇娆″厠娴烽瀾闈兼捣宄°€傞粦榫欐睙鏄腑鍥戒笁澶ф渤娴佷箣涓€銆佷笘鐣屽崄澶ф渤涔嬩竴锛堟湁浜涜祫鏂欒涓虹鍏級銆傞粦榫欐睙鏈槸涓浗鐨勫唴娌筹紝19涓栫邯涓悗鏈熸矙淇勫己琛屽崰棰嗕腑鍥介粦榫欐睙浠ュ寳銆佷箤鑻忛噷姹熶互涓滃ぇ鐗囬鍦熶箣鍚庯紝鎵嶆垚涓轰腑淇勭晫娌炽€�2004骞达紝涓浗鍜屼縿缃楁柉绛剧讲鏈€鍚庤竟鐣屽崗瀹氾紝灏嗕袱鍥藉浗鐣屼互榛戦緳姹熶负鍩烘湰鐣岄檺鍒掓竻銆�");
		RIVERS.add(river);

		river = new River(
				"鐝犳睙",
				2400,
				"鍘熸寚骞垮窞鍒板叆娴峰彛鐨勪竴娈垫渤閬擄紝鍚庢潵閫愭笎鎴愪负瑗挎睙銆佸寳姹熴€佷笢姹熷拰鐝犳睙涓夎娲茶娌崇殑鎬荤О銆傚叾骞叉祦瑗挎睙鍙戞簮浜庝簯鍗楃渷涓滃寳閮ㄦ簿鐩婂幙鐨勯┈闆勫北锛屽共娴佹祦缁忎簯鍗椼€佽吹宸炪€佸箍瑗裤€佸箍涓滃洓鐪侊紙鑷不鍖猴級鍙婇娓€佹境闂ㄧ壒鍒鏀垮尯銆�");
		RIVERS.add(river);

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		Log.d("list", "on create cp");
		database = new RiverDatabaseHelper(getContext(), "rivers", null,
				DATABASE_VERSION).getWritableDatabase();

		new Thread() {
			boolean flag;

			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(1000 * 3);
						ContentValues values = new ContentValues();
						if (flag) {
							values.put(RiverContentProvider.NAME, "Long River");
						} else {
							values.put(RiverContentProvider.NAME, "闀挎睙");
						}
						flag = !flag;
						update(RiverContentProvider.CONTENT_URI, values,
								"_id=1", null);
						Log.d("list", ">>>>>>>update record");
					}
				} catch (InterruptedException e) {
					Log.d("list", e.getMessage());
				}
			}
		}.start();

		return database != null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = database.query("rivers", projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int returnValue = database.update("rivers", values, selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return returnValue;
	}

	private static class RiverDatabaseHelper extends SQLiteOpenHelper {

		public RiverDatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL("create table if not exists rivers("
					+ " _id integer primary key autoincrement," + " name text,"
					+ "length integer," + " introduction text" + ");");

			SQLiteStatement statement = database
					.compileStatement("insert into rivers(name,length,introduction) values(?,?,?)");

			for (River r : RIVERS) {
				int index = 1;
				statement.bindString(index++, r.getName());
				statement.bindLong(index++, r.getLength());
				statement.bindString(index++, r.getIntroduction());
				statement.executeInsert();
			}

			statement.close();
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			database.execSQL("drop table if exists rivers");
			onCreate(database);
		}

	}

}