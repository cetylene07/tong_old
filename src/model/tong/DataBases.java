package model.tong;

import android.provider.BaseColumns;

public final class DataBases {

	// ���̺��� ������ ����
	public static final class CreateDB implements BaseColumns {
		public static final String callID = "callID";
		public static final String NAME = "name";
		public static final String image = "image"; //���� �������� ����...����ó���� ���;� �ϴ� �ǰ�?!
		public static final String DATE = "date";
		public static final String DURATION = "duration";
		public static final String TYPE = "type";
		public static final String _TABLENAME = "calldb";
		public static final String _CREATE = 
				"create table " + _TABLENAME + "("
				+ _ID + " integer primary key autoincrement, "
				+ callID + " text not null, "
				+ NAME + " text not null, "
				+ DATE + " text not null, "
				+ DURATION + " text not null, "
				+ TYPE + " text not null"
				+ ");";

	}
}
