package com.hb.app.tong;
///*
// * ��ȭ �м� ȭ��
// */
//package com.ts.app.tong;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.CallLog;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.AdapterView.OnItemSelectedListener;
//
//class sms_info {
//	int rank;						//����
//	String name; 					// �̸�
//	int in_count; 					// ������ȭ ���� ȸ��
//	double in_count_percent;		// ������ȭ ���� Ƚ�� ����
//	int out_count; 					// �߽���ȭ ���� Ƚ��
//	double out_count_percent;		// �߽���ȭ ���� Ƚ�� ����
//	long date;						// ��¥
//
//	int get_incount() {
//		return in_count;
//	}
//}
//
//public class textActivity extends Activity implements OnItemSelectedListener {
//	int total_in_count = 0;										// �� ���� �޽��� ����
//	int total_out_count = 0;									// �� �߽� �޽��� ����
//	TextView text1, text2;
//	TextView value1, value2;
//	ArrayList<sms_info> list = new ArrayList<sms_info>(); 		// ��������� ��������Ʈ�� ��ü ����
//	ArrayList<sms_info> temp_list = new ArrayList<sms_info>(); 	// ��������� ��������Ʈ�� �ӽð�ü ����
//																
//	ArrayAdapter<CharSequence> adspin;
//	double average;
//	String temp_average;
//	Cursor cursor, cursor1;
//	
//	ContentResolver cr;
//	boolean uri_found = false;
//
//	int jj;
//
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.textactivity);
//
//		cr = getContentResolver();
//		cursor = cr.query(Uri.parse("content://sms"), null, null, null, null);
//		cursor1 = cr.query(Uri.parse("content://com.sec.mms.provider/message"), null, null, null, null);
//		if(cursor.moveToNext())	//content:sms �� Uri�� ������ ���������� Uri�� �߰��� ������ ����
//			uri_found = true;
//		
//		if(cursor1 != null)
//			if(cursor1.moveToNext())	//content:sms �� Uri�� ������ ���������� Uri�� �߰��� ������ ����
//				uri_found = true;		
//
//
//		if (uri_found == true) {			
//			int nameidx = cursor.getColumnIndex("address"); // ���ڴ����
//			int dateidx = cursor.getColumnIndex("date"); // ���� ����.
//
//			int bodyidx = cursor.getColumnIndex("body");
//			int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER); // ��ȭ��ȣ
//			int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION); // ��ȭ�ð�
//			int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE); // ��ȭ����(����,�߽�,������)
//
//			boolean found = false; // ���� �̸��� ã���� ����Ƚ���� ������Ų��. ã�����ϸ� ����Ʈ�� �߰�
//
//			StringBuilder result = new StringBuilder();
//
//			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
//			result.append("�� ��� ���� : " + cursor.getCount() + "��\n");
//			int count = 0;
//			int where = 0; // list�� ���� �̸����� ��� �ִ��� Ȯ��
//
//			while (cursor.moveToNext()) { // cursor�� ���� ���� �� ���� �ݺ�
//				// ��ȭ �����
//
//				sms_info temp = new sms_info();
//				Iterator<sms_info> it = list.iterator(); // iterator�� ��ȯ
//
//				String name = cursor.getString(nameidx); // �̸��� ���ڿ��� ��ȯ
//
//				if (name == null) {
//					name = cursor.getString(numidx); // �̸��� ������� ������ ��ȣ�� ����
//				}
//
//				temp.name = name;
//
//				found = false;
//				where = 0;
//				while (it.hasNext()) {
//					sms_info data = it.next();
//					if (data.name.equals(name)) { // list�� �ִ� ���� �� �̸��� ���� ���� ã����
//						found = true;
//
//						/*
//						 * data�� �ּҸ� temp�� �����Ų�� ���� temp���� �ʵ带 �����ϸ� �̰� data����
//						 * ���ÿ� ����Ǵ� �Ŷ� ��������! ���� ���������� ���� �ſ��µ� ��դ� �� ��
//						 */
//
//						temp = data;
//						break;
//					}
//					where++;
//				}
//
//				// ��ȭ ����
//				int type = cursor.getInt(typeidx);
//				String stype;
//				switch (type) {
//
//				// ������S�� ��� ������ 4, ���� �ۼ����� 14, 13 ���� MMS �ۼ����� 16, 15���� ���ǵǾ� �ִ�.
//
//				case CallLog.Calls.INCOMING_TYPE:
//					stype = "���ڹ���";
//					temp.in_count++;
//					total_in_count++;
//					break;
//				case CallLog.Calls.OUTGOING_TYPE:
//					stype = "���ں���";
//					temp.out_count++;
//					total_out_count++;
//					break;
//				default:
//					stype = "��Ÿ" + type;
//					break;
//
//				}
//
//				// ��ȭ ��¥
//				long date = cursor.getLong(dateidx);
//				temp.date = date;
//				String sdate = formatter.format(new Date(date));
//
//				if (found == false) {
//					list.add(temp); // ���� �߰�
//				}
//
//			}
//
//			// �ۼ�Ʈ �����ϴ� �ݺ���
//			for (int i = 0; i < list.size(); i++) {
//				if (total_in_count > 0)
//					list.get(i).in_count_percent = (double) list.get(i).in_count
//							/ (double) total_in_count * 100;
//				else
//					list.get(i).in_count_percent = 0;
//
//				if (total_out_count > 0)
//					list.get(i).out_count_percent = (double) list.get(i).out_count
//							/ (double) total_out_count * 100;
//				else
//					list.get(i).out_count_percent = 0;
//			}
//
//			cursor.close();
//
//			Spinner spin = (Spinner) findViewById(R.id.text_spinner1);
//			spin.setPrompt("�����ϼ���");
////			adspin = ArrayAdapter.createFromResource(this, R.array.sms_choice,
//					android.R.layout.simple_spinner_item);
//			adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			spin.setAdapter(adspin);
//			spin.setOnItemSelectedListener(this);
//
//		} else {
//
//			TextView tv1 = (TextView) findViewById(R.id.text_subject1);
//			tv1.setText("������ �б� ����");
//		}
//	}
//
//	public void onItemSelected(AdapterView<?> parent, View view, int position,
//			long id) {
//
//		// temp_list ���� ���
//		for (int i = temp_list.size() - 1; i >= 0; i--) {
//			temp_list.remove(i);
//		}
//
//		TextView tv1 = (TextView) findViewById(R.id.text_subject1);
//		tv1.setText(adspin.getItem(position));
//		switch (position) {
//		case 0:
//			text1 = (TextView) findViewById(R.id.text1);
//			text1.setText("�� ���� �޽��� ���� : ");
//			value1 = (TextView) findViewById(R.id.value1);
//			value1.setText(String.valueOf(total_in_count) + "��");
//
//			text2 = (TextView) findViewById(R.id.text2);
//			text2.setText("��� ���� �޽��� ���� : ");
//			value2 = (TextView) findViewById(R.id.value2);
//
//			average = (double) total_in_count / (double) list.size();
//			temp_average = String.format("%.2f", average);
//			value2.setText(temp_average + "��");
//
//			TextView call_text = (TextView) findViewById(R.id.text_title1);
//			call_text
//					.setText("����        �̸�                    ����Ƚ��             ����");
//			// ���������� �̿��Ͽ� ����Ƚ���� ������������ ������(ū�� ��������)
//			for (int i = 0; i < list.size(); i++) {
//				int max = i;
//				for (int j = i + 1; j < list.size(); j++) {
//					if (list.get(j).in_count > list.get(max).in_count) {
//						max = j;
//					}
//				}
//				sms_info trans = new sms_info();
//				trans = list.get(i);
//				list.set(i, list.get(max));
//				list.set(max, trans);
//			}
//
//			// ������ �����ϴ� �ݺ���
//			jj = 1;
//			for (int i = 0; i < list.size() - 1; i++) {
//
//				list.get(i).rank = jj;
//				if (list.get(i + 1).in_count != list.get(i).in_count) {
//					jj++;
//				}
//			}
//
//			// �� ������ �������� ������ ���� �ڵ��ؾ� �ȴ�.
//			if (list.get(list.size() - 1).in_count != list.get(list.size() - 1).in_count) {
//
//				list.get(list.size() - 1).rank = jj + 1;
//			} else {
//				list.get(list.size() - 1).rank = jj;
//			}
//
//			// 0�̻� temp_list�� �����Ѵ�.
//			for (int i = 0; i < list.size(); i++) {
//				if (list.get(i).in_count > 0) {
//					temp_list.add(list.get(i));
//
//				}
//			}
//
//			// Ŀ���� �並 �̿��Ͽ� ����Ʈ�信 ���
//			sms_Adapter MyAdapter = new sms_Adapter(this, R.layout.sms_view,
//					temp_list);
//
//			ListView MyList;
//			MyList = (ListView) findViewById(R.id.sms_list);
//			MyList.setAdapter(MyAdapter);
//			break;
//		case 1:
//
//			text1 = (TextView) findViewById(R.id.text1);
//			text1.setText("�� �߽� �޽��� ���� : ");
//			value1 = (TextView) findViewById(R.id.value1);
//			value1.setText(String.valueOf(total_out_count) + "��");
//
//			text2 = (TextView) findViewById(R.id.text2);
//			text2.setText("��� �߽� �޽��� ���� : ");
//			value2 = (TextView) findViewById(R.id.value2);
//
//			average = (double) total_out_count / (double) list.size();
//			temp_average = String.format("%.2f", average);
//			value2.setText(temp_average + "��");
//
//			call_text = (TextView) findViewById(R.id.text_title1);
//			call_text
//					.setText("����        �̸�                    �߽�Ƚ��             ����");
//			// ���������� �̿��Ͽ� ����Ƚ���� ������������ ������(ū�� ��������)
//			for (int i = 0; i < list.size(); i++) {
//				int max = i;
//				for (int j = i + 1; j < list.size(); j++) {
//					if (list.get(j).out_count > list.get(max).out_count) {
//						max = j;
//					}
//				}
//				sms_info trans = new sms_info();
//				trans = list.get(i);
//				list.set(i, list.get(max));
//				list.set(max, trans);
//			}
//
//			// ������ �����ϴ� �ݺ���
//			jj = 1;
//			for (int i = 0; i < list.size() - 1; i++) {
//
//				list.get(i).rank = jj;
//				if (list.get(i + 1).out_count != list.get(i).out_count) {
//					jj++;
//				}
//			}
//
//			// �� ������ �������� ������ ���� �ڵ��ؾ� �ȴ�.
//			if (list.get(list.size() - 1).out_count != list
//					.get(list.size() - 1).out_count) {
//
//				list.get(list.size() - 1).rank = jj + 1;
//			} else {
//				list.get(list.size() - 1).rank = jj;
//			}
//
//			// 0�̻� temp_list�� �����Ѵ�.
//			for (int i = 0; i < list.size(); i++) {
//				if (list.get(i).out_count > 0) {
//					temp_list.add(list.get(i));
//
//				}
//			}
//
//			// Ŀ���� �並 �̿��Ͽ� ����Ʈ�信 ���
//			sms_Out_Adapter out_Adapter = new sms_Out_Adapter(this,
//					R.layout.sms_view, temp_list);
//
//			MyList = (ListView) findViewById(R.id.sms_list);
//			MyList.setAdapter(out_Adapter);
//			break;
//		}
//	}
//
//	public void onNothingSelected(AdapterView<?> parent) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
//
//// ���� �޽��� ����� Ŭ����
//class sms_Adapter extends BaseAdapter {
//	Context maincon;
//	LayoutInflater Inflater;
//	ArrayList<sms_info> arSrc;
//	int layout;
//	String s_value;
//	String debug;
//	ImageView image;
//	TextView rank;
//	
//	public sms_Adapter(Context context, int alayout, ArrayList<sms_info> alist) {
//		maincon = context;
//		Inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		arSrc = alist;
//		layout = alayout;
//	}
//
//	public int getCount() {
//		return arSrc.size();
//	}
//
//	public String getItem(int position) {
//		return arSrc.get(position).name;
//	}
//
//	public long getItemId(int position) {
//		return position;
//	}
//
//	// �� �׸��� �� ����
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		if (convertView == null) {
//			convertView = Inflater.inflate(layout, parent, false);
//		}
//
//		// 1,2,3���� �̹����� ��ũ�� �����ֵ��� �Ѵ�.
//		if (arSrc.get(position).rank == 1) {
//			//1���� �ݸ޴�!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.first);			
//			
//			
//			//�Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 2) {
//			//2���� ���޴�!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.second);
//			//�Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 3) {
//			//3���� ���Ŵ�!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.third);
//			//�Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//			
//		} else {
//			//4����ʹ� �׳� �ؽ�Ʈ�� ���! ������
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.black);	
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			debug = String.valueOf(arSrc.get(position).rank);
//			rank.setText("[" + debug + "]");
//		}
//
//		TextView name = (TextView) convertView.findViewById(R.id.sms_name);
//		name.setText(arSrc.get(position).name);
//
//		TextView count = (TextView) convertView.findViewById(R.id.sms_incount);
//		debug = String.valueOf(arSrc.get(position).in_count);
//		count.setText(debug);
//
//		TextView percent = (TextView) convertView
//				.findViewById(R.id.sms_incount_percent);
//		// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
//		s_value = String.format("%.2f", arSrc.get(position).in_count_percent);
//		debug = String.valueOf(s_value + "%");
//		percent.setText(debug);
//
//		return convertView;
//
//	}
//}
//
//// �߽� �޽��� ����� Ŭ����
//class sms_Out_Adapter extends sms_Adapter {
//
//	public sms_Out_Adapter(Context context, int alayout,
//			ArrayList<sms_info> alist) {
//
//		super(context, alayout, alist);
//	}
//
//	// �� �׸��� �� ����
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		if (convertView == null) {
//			convertView = Inflater.inflate(layout, parent, false);
//		}
//		
//		// 1,2,3���� �̹����� ��ũ�� �����ֵ��� �Ѵ�.
//		if (arSrc.get(position).rank == 1) {
//			//1���� �ݸ޴�!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.first);			
//			
//			
//			//�Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 2) {
//			//2���� ���޴�!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.second);
//			//�Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 3) {
//			//3���� ���Ŵ�!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.third);
//			//�Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//			
//		} else {
//			//4����ʹ� �׳� �ؽ�Ʈ�� ���! ������
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.black);	
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			debug = String.valueOf(arSrc.get(position).rank);
//			rank.setText("[" + debug + "]");
//		}
//
//
//		TextView name = (TextView) convertView.findViewById(R.id.sms_name);
//		name.setText(arSrc.get(position).name);
//
//		TextView count = (TextView) convertView.findViewById(R.id.sms_incount);
//		debug = String.valueOf(arSrc.get(position).out_count);
//		count.setText(debug);
//
//		TextView percent = (TextView) convertView
//				.findViewById(R.id.sms_incount_percent);
//		// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
//		s_value = String.format("%.2f", arSrc.get(position).out_count_percent);
//		debug = String.valueOf(s_value + "%");
//		percent.setText(debug);
//
//		return convertView;
//
//	}
//}
