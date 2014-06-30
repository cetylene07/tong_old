/*
 * ��ȭ �м� ȭ��
 */
package com.hb.app.tong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartstat.info.DateInfo;
import com.smartstat.info.Info;
import com.smartstat.listadapter.TotalDurationListAdapter;

public class CallListFragment extends Fragment  {
	/**
	 * @uml.property  name="list"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.smartstat.info.Info"
	 */
	ArrayList<Info> list = new ArrayList<Info>(); // ��������� ����Ʈ�� ��ü ��
	/**
	 * @uml.property  name="temp_list"
	 */
	ArrayList<Info> temp_list = new ArrayList<Info>(); // ��������� ����Ʈ�� ��ü ��

	/**
	 * @uml.property  name="date_list"
	 */
	ArrayList<DateInfo> date_list = new ArrayList<DateInfo>();

	/**
	 * @uml.property  name="total_dur"
	 */
	double total_dur = 0;
	/**
	 * @uml.property  name="total_incall_count"
	 */
	double total_incall_count = 0;
	/**
	 * @uml.property  name="total_outcall_count"
	 */
	double total_outcall_count = 0;
	/**
	 * @uml.property  name="total_average_in_dur"
	 */
	double total_average_in_dur = 0; // ��� ���� ���� ����
	/**
	 * @uml.property  name="total_average_out_dur"
	 */
	double total_average_out_dur = 0; // ��� �߽� ���� ����
	/**
	 * @uml.property  name="total_average_sum_dur"
	 */
	double total_average_sum_dur = 0; // ��� ���߽� ���� ����
	/**
	 * @uml.property  name="total_indur"
	 */
	double total_indur = 0;
	/**
	 * @uml.property  name="total_outdur"
	 */
	double total_outdur = 0;
	/**
	 * @uml.property  name="total_miss"
	 */
	double total_miss = 0;
	/**
	 * @uml.property  name="temp_value"
	 */
	double temp_value;
	/**
	 * @uml.property  name="jj"
	 */
	int jj;
	/**
	 * @uml.property  name="uri_found"
	 */
	boolean uri_found = false;

	// �׳� �ƹ����Գ� ���� �ӽ� ����
	/**
	 * @uml.property  name="tmp1"
	 */
	int tmp1;

	/**
	 * @uml.property  name="name"
	 */
	String name; // �̸�
	/**
	 * @uml.property  name="number"
	 */
	String number; // ��ȭ��ȣ
	/**
	 * @uml.property  name="date"
	 */
	long date; // ��ȭ ��¥

	/**
	 * @uml.property  name="sdate"
	 */
	String sdate;

	/**
	 * @uml.property  name="adspin"
	 * @uml.associationEnd  
	 */
	ArrayAdapter<CharSequence> adspin;

	/**
	 * @uml.property  name="linear"
	 * @uml.associationEnd  
	 */
	LinearLayout linear;

	final static int itemView = 0;
	static int itemPosition = 0;// ����Ʈ�� ������ �� ��ġ

	// 5.16
	// �̹����� �ҷ����� ���ؼ� ����
	/**
	 * @uml.property  name="cONTACTS_PROJECTION" multiplicity="(0 -1)" dimension="1"
	 */
	final String[] CONTACTS_PROJECTION = new String[] {
			ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME

	};

	// private Context mcontext;
	// public CallListFragment(Context context) {
	// mcontext = context;
	// }

	@Override
	public void onSaveInstanceState(Bundle outState) {
//		Log.d(this.getClass().getSimpleName(), "onSaveInstanceState()");
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// list �ʱ�ȭ
		for (int i = 0; i < list.size(); i++)
			list.remove(i);
		return inflater.inflate(R.layout.fragment_call_list, container, false);
	}

	public void onStart() {
		super.onStart();
		// setContentView(R.layout.activity_call);

        //2014.06.30
        //fix duplicate data when resume activity
        temp_list.clear();
        list.clear();

		// 5.16
		linear = (LinearLayout) View.inflate(getActivity(), R.layout.item_view,
				null);// View�� �ҷ��ɴϴ�.
		// import android.provider.ContactsContract �ʿ�
		Uri uContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // �ּҷ���
																				// �����
																				// Uri

		/*
		 * updating 2013.11.12 �� �˻� ��� �߰��Ϸ��� ��
		 */
		Button btnBeforeMonth = (Button) getView().findViewById(
				R.id.btnBeforeMonth);
		Button btnAfterMonth = (Button) getView().findViewById(
				R.id.btnAfterMonth);
		Button btnSelectData = (Button) getView().findViewById(
				R.id.btnSelectDate);
		btnSelectData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});

		ContentResolver cr = getActivity().getContentResolver();
		
		Cursor cursorContact = null;

		Cursor c;

		String name = null;

		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null,
				CallLog.Calls.DATE + " DESC");

		if (cursor.moveToNext()) // content:sms �� Uri�� ������ ���������� Uri�� �߰��� ������ ����
			uri_found = true;

		if (uri_found == true) {

			int ididx = cursor.getColumnIndex(ContactsContract.Contacts._ID);

			// ��ȭ�����(�̸�)
			int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);

			// ��ȭ ����(0.001�� ������ ���ð�)
			int dateidx = cursor.getColumnIndex(CallLog.Calls.DATE);

			// ��ȭ��ȣ
			int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER);

			// ��ȭ�ð�
			int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION);

			// ��ȭ����(����,�߽�,������)
			int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE);

			boolean found = false; // ���� �̸��� ã���� ����Ƚ���� ������Ų��. ã�����ϸ� ����Ʈ�� �߰�

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");

			int where = 0; // list�� ���� �̸����� ��� �ִ��� Ȯ��

			while (cursor.moveToNext()) { // cursor�� ���� ���� �� ���� �ݺ�

				// ��ȭ �����
				Info temp = new Info();
				Iterator<Info> it = list.iterator(); // iterator�� ��ȯ

				name = cursor.getString(nameidx); // �̸��� ���ڿ��� ��ȯ
				number = cursor.getString(numidx);

				if (name == null) {
					name = cursor.getString(numidx); // �̸��� ������� ������ ��ȣ�� ����
				}

				temp.setName(name);

				found = false;
				where = 0;
				while (it.hasNext()) {
					Info data = it.next();
					if (data.getName().equals(name)) { // list�� �ִ� ��� �� �̸��� ����
														// ���� ã����
						found = true;

						/*
						 * data�� �ּҸ� temp�� �����Ų�� ��� temp���� �ʵ带 �����ϸ� �̰� data����
						 * ���ÿ� ����Ǵ� �Ŷ� ������! ���� �������� ���� �ſ��µ� ��դ� �� ��
						 */

						temp = data;
						break;
					}
					where++;
				}

				// ��ȭ ����
				int type = cursor.getInt(typeidx);
				// row.put("type", type);

				String stype;
				switch (type) {
				case CallLog.Calls.INCOMING_TYPE:
					stype = "����";
					total_incall_count++;
					total_indur += cursor.getInt(duridx);

					temp.inCreaseInCount();
					temp.setIn_dur(temp.getIn_dur() + cursor.getInt(duridx));
					temp.setSum_dur(temp.getSum_dur() + cursor.getInt(duridx));

					break;
				case CallLog.Calls.OUTGOING_TYPE:
					total_outcall_count++;
					stype = "�߽�";
					temp.inCreaseOutCount();
					total_outdur += cursor.getInt(duridx);
					temp.setOut_dur(temp.getOut_dur() + cursor.getInt(duridx));
					temp.setSum_dur(temp.getSum_dur() + cursor.getInt(duridx));
					break;
				case CallLog.Calls.MISSED_TYPE:
					stype = "������";
					total_miss++;
					temp.inCreaseMissCount();
					break;

				}

				// ��ȭ ��¥
				long date = cursor.getLong(dateidx);
				// row.put("date", date);

				sdate = formatter.format(new Date(date));

				// ��ȭ �ð�
				int duration = cursor.getInt(duridx);
				// row.put("duration", duration);

				if (found == false) {
					list.add(temp); // ���� �߰�
				}

			}
			total_dur = total_indur + total_outdur;

			for (int i = 0; i < list.size(); i++) {
				// ��ü�� ���� ���
				list.get(i).setIncount_percent(
						list.get(i).getIn_count() / total_incall_count * 100); // ����
																				// Ƚ��
																				// ����
				list.get(i).indur_percent = list.get(i).in_dur / total_indur
						* 100; // ���ű��̺���
				list.get(i).outcount_percent = list.get(i).out_count
						/ total_incall_count * 100; // �߽� Ƚ�� ����
				list.get(i).outdur_percent = list.get(i).out_dur / total_outdur
						* 100; // �߽� ���� ����
				list.get(i).miss_percent = list.get(i).miss_count / total_miss
						* 100; // ���� Ƚ�� ����

				if (list.get(i).in_count > 0)
					list.get(i).average_in_dur = list.get(i).in_dur
							/ list.get(i).in_count; // ��� ���� ����
				else
					list.get(i).average_in_dur = 0;
				total_average_in_dur += list.get(i).average_in_dur;

				if (list.get(i).out_count > 0)
					list.get(i).average_out_dur = list.get(i).out_dur
							/ list.get(i).out_count; // ��� �߽� ����
				else
					list.get(i).average_out_dur = 0;
				total_average_out_dur += list.get(i).average_out_dur;

			}

			for (int i = 0; i < list.size(); i++) {
				list.get(i).average_in_dur_percent = list.get(i).average_in_dur
						/ total_average_in_dur * 100;
				list.get(i).average_out_dur_percent = list.get(i).average_out_dur
						/ total_average_out_dur * 100;

				// ���ű��̿� �߽ű����� �� ���
				list.get(i).sum_dur = list.get(i).in_dur + list.get(i).out_dur;

				list.get(i).sum_dur_percent = list.get(i).sum_dur
						/ (total_indur + total_outdur) * 100;
			}

			cursor.close();

			// ���ǳ�
			Spinner spin = (Spinner) getView().findViewById(R.id.call_spinner1);
			spin.setPrompt("Choice Option");
			adspin = ArrayAdapter.createFromResource(getActivity(),
					R.array.call_choice, android.R.layout.simple_spinner_item);
			adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin.setAdapter(adspin);
			spin.setOnItemSelectedListener(new OnItemSelectedListener() {

				// ���ǳ� �������� ������ �Ϳ� ���
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					String s_value;
					TextView sub_text1 = (TextView) getView().findViewById(
							R.id.sub_call_text1);
					TextView sub_value1 = (TextView) getView().findViewById(
							R.id.sub_call_value1);
					TextView sub_text2 = (TextView) getView().findViewById(
							R.id.sub_call_text2);
					TextView sub_value2 = (TextView) getView().findViewById(
							R.id.sub_call_value2);

					// ����Ʈ�� �ִ� �������� �������� �� ��ȭ���ڰ� �� �߰� ����� �ҽ��ڵ�!
					OnItemClickListener listener = new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {

							itemPosition = position;
							// showDialog(itemView);

						}

					}; // ��ȭ���� �ҽ��ڵ� ��!

					// temp_list ���� ���
					for (int i = temp_list.size() - 1; i >= 0; i--) {
						temp_list.remove(i);
					}

					switch (position) {
					case 0:
						// ����
						Collections.sort(list, new sumDurCompareDesc());

						/*
						 * �������� 1���� ���ʴ�� �ְ�, ���� ���� ������ ���� ����� �ǰ� �Ѵ�.
						 */
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).getSum_dur() != list.get(i)
									.getSum_dur()) {
								jj++;
							}
						}

						// // �� ������ �������� ������ ��� �ڵ��ؾ� �ȴ�.
						// if (list.get(list.size() -
						// 1).in_dur+list.get(list.size() - 1).out_dur !=
						// list.get(list.size() - 1).in_dur+list.get(list.size()
						// - 1).out_dur) {
						//
						// list.get(list.size() - 1).rank = jj + 1;
						// } else {
						// list.get(list.size() - 1).rank = jj;
						// }

						// 0�̻� temp_list�� �����Ѵ�.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getSum_dur() > 0) {
								temp_list.add(list.get(i));

							}
						}



						// Ŀ���� �並 �̿��Ͽ� ����Ʈ�信 ���
						TotalDurationListAdapter MyAdapter = new TotalDurationListAdapter(
								getActivity(), R.layout.incall_view, temp_list);
						ListView MyList;

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(MyAdapter);

						MyList.setOnItemClickListener(listener);

						break;
					case 1:

						sub_text1.setText("�� ���� ���� : ");
						sub_value1.setText(String.valueOf((int) total_indur)
								+ "sec");
						sub_text2.setText("1�δ� ��� ���� ���� : ");
						// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
						s_value = String.format("%.2f",
								total_indur / list.size());
						sub_value2.setText(s_value + "sec");

						for (int i = 0; i < list.size(); i++) {
							int max = i;
							for (int j = i + 1; j < list.size(); j++) {
								if (list.get(j).in_dur > list.get(max).in_dur) {
									max = j;
								}
							}
							Info trans = new Info();
							trans = list.get(i);
							list.set(i, list.get(max));
							list.set(max, trans);
						}

						// ������ �����ϴ� �ݺ���
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).in_dur != list.get(i).in_dur) {
								jj++;
							}
						}

						// �� ������ �������� ������ ��� �ڵ��ؾ� �ȴ�.
						if (list.get(list.size() - 1).in_dur != list.get(list
								.size() - 1).in_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0�̻� temp_list�� �����Ѵ�.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).in_dur > 0) {
								temp_list.add(list.get(i));

							}
						}

						// Ŀ���� �並 �̿��Ͽ� ����Ʈ�信 ���
						indur_Adapter indurView = new indur_Adapter(
								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(indurView);

						break;

					case 2:

						sub_text1.setText("�� ��� ���� ���� : ");
						sub_value1.setText(String
								.valueOf((int) total_average_in_dur) + "��");
						sub_text2.setText("1�δ� ��� ���� ���� : ");
						// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
						s_value = String.format("%.2f", total_average_in_dur
								/ list.size());
						sub_value2.setText(s_value + "��");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("����        �̸�                ��ռ��ű���             ����");
						// ���������� �̿��Ͽ� ���ű��̸� ������������ ������(ū�� �������)
						for (int i = 0; i < list.size(); i++) {
							int max = i;
							for (int j = i + 1; j < list.size(); j++) {
								if (list.get(j).average_in_dur > list.get(max).average_in_dur) {
									max = j;
								}
							}
							Info trans = new Info();
							trans = list.get(i);
							list.set(i, list.get(max));
							list.set(max, trans);
						}

						// ������ �����ϴ� �ݺ���
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).average_in_dur != list.get(i).average_in_dur) {
								jj++;
							}
						}

						// �� ������ �������� ������ ��� �ڵ��ؾ� �ȴ�.
						if (list.get(list.size() - 1).average_in_dur != list
								.get(list.size() - 1).average_in_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0�̻� temp_list�� �����Ѵ�.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).average_in_dur > 0) {
								temp_list.add(list.get(i));

							}
						}

						// Ŀ���� �並 �̿��Ͽ� ����Ʈ�信 ���
						average_indur_Adapter average_indurView = new average_indur_Adapter(
								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(average_indurView);

						break;

					case 3:

						sub_text1.setText("�� �߽� ȸ�� : ");
						sub_value1.setText(String
								.valueOf((int) total_outcall_count) + "ȸ");
						sub_text2.setText("1�δ� ��� �߽� ȸ�� : ");
						// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
						s_value = String.format("%.2f", total_outcall_count
								/ list.size());
						sub_value2.setText(s_value + "ȸ");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("����        �̸�                    �߽�Ƚ��             ����");
						// ���������� �̿��Ͽ� �߽�Ƚ���� ������������ ������(ū�� �������)
						for (int i = 0; i < list.size(); i++) {
							int max = i;
							for (int j = i + 1; j < list.size(); j++) {
								if (list.get(j).out_count > list.get(max).out_count) {
									max = j;
								}
							}
							Info trans = new Info();
							trans = list.get(i);
							list.set(i, list.get(max));
							list.set(max, trans);
						}

						// ������ �����ϴ� �ݺ���
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).out_count != list.get(i).out_count) {
								jj++;
							}
						}

						// �� ������ �������� ������ ��� �ڵ��ؾ� �ȴ�.
						if (list.get(list.size() - 1).out_count != list
								.get(list.size() - 1).out_count) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0�̻� temp_list�� �����Ѵ�.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).out_count > 0) {
								temp_list.add(list.get(i));
							}
						}

						// Ŀ���� �並 �̿��Ͽ� ����Ʈ�信 ���
						outcount_Adapter OutCallAdapter = new outcount_Adapter(
								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(OutCallAdapter);

						break;
					case 4:

						sub_text1.setText("�� �߽� ���� : ");
						sub_value1.setText(String.valueOf((int) total_outdur)
								+ "��");
						sub_text2.setText("1�δ� ��� �߽� ���� : ");
						// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
						s_value = String.format("%.2f",
								total_outdur / list.size());
						sub_value2.setText(s_value + "��");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("����        �̸�                    �߽ű���             ����");
						// ���������� �̿��Ͽ� ����Ƚ���� ������������ ������(ū�� �������)
						for (int i = 0; i < list.size(); i++) {
							int max = i;
							for (int j = i + 1; j < list.size(); j++) {
								if (list.get(j).out_dur > list.get(max).out_dur) {
									max = j;
								}
							}
							Info trans = new Info();
							trans = list.get(i);
							list.set(i, list.get(max));
							list.set(max, trans);
						}

						// ������ �����ϴ� �ݺ���
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).out_dur != list.get(i).out_dur) {
								jj++;
							}
						}

						// �� ������ �������� ������ ��� �ڵ��ؾ� �ȴ�.
						if (list.get(list.size() - 1).out_dur != list.get(list
								.size() - 1).out_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0�̻� temp_list�� �����Ѵ�.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).out_dur > 0) {
								temp_list.add(list.get(i));
							}
						}

						// Ŀ���� �並 �̿��Ͽ� ����Ʈ�信 ���
						outdur_Adapter OutDurAdapter = new outdur_Adapter(
								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(OutDurAdapter);

						break;

					case 5:

						sub_text1.setText("�� ��� �߽� ���� : ");
						sub_value1.setText(String
								.valueOf((int) total_average_out_dur) + "��");
						sub_text2.setText("1�δ� ��� �߽� ���� : ");
						// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
						s_value = String.format("%.2f", total_average_out_dur
								/ list.size());
						sub_value2.setText(s_value + "��");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("����        �̸�                ��չ߽ű���             ����");
						// ���������� �̿��Ͽ� ���ű��̸� ������������ ������(ū�� �������)
						for (int i = 0; i < list.size(); i++) {
							int max = i;
							for (int j = i + 1; j < list.size(); j++) {
								if (list.get(j).average_out_dur > list.get(max).average_out_dur) {
									max = j;
								}
							}
							Info trans = new Info();
							trans = list.get(i);
							list.set(i, list.get(max));
							list.set(max, trans);
						}

						// ������ �����ϴ� �ݺ���
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).average_out_dur != list.get(i).average_out_dur) {
								jj++;
							}
						}

						// �� ������ �������� ������ ��� �ڵ��ؾ� �ȴ�.
						if (list.get(list.size() - 1).average_out_dur != list
								.get(list.size() - 1).average_out_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0�̻� temp_list�� �����Ѵ�.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).average_out_dur > 0) {
								temp_list.add(list.get(i));

							}
						}

						// Ŀ���� �並 �̿��Ͽ� ����Ʈ�信 ���
						average_outdur_Adapter average_outdurView = new average_outdur_Adapter(
								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(average_outdurView);

						break;

					case 6:

						sub_text1.setText("�� ���� ȸ�� : ");
						sub_value1.setText(String.valueOf((int) total_miss)
								+ "ȸ");
						sub_text2.setText("1�δ� ��� ���� ȸ�� : ");
						// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
						s_value = String.format("%.2f",
								total_miss / list.size());
						sub_value2.setText(s_value + "ȸ");

						for (int i = 0; i < list.size(); i++) {
							int max = i;
							for (int j = i + 1; j < list.size(); j++) {
								if (list.get(j).miss_count > list.get(max).miss_count) {
									max = j;
								}
							}
							Info trans = new Info();
							trans = list.get(i);
							list.set(i, list.get(max));
							list.set(max, trans);
						}

						// ������ �����ϴ� �ݺ���
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).miss_count != list.get(i).miss_count) {
								jj++;
							}
						}

						// �� ������ �������� ������ ��� �ڵ��ؾ� �ȴ�.
						if (list.get(list.size() - 1).miss_count != list
								.get(list.size() - 1).miss_count) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0�̻� temp_list�� �����Ѵ�.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).miss_count > 0) {
								temp_list.add(list.get(i));
							}
						}

						// Ŀ���� �並 �̿��Ͽ� ����Ʈ�信 ���
						misscount_Adapter missCallAdapter = new misscount_Adapter(
								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(missCallAdapter);

						break;
					}
				}

				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});

		} else {
			// TextView tv1 = (TextView) getView().findViewById(R.id.subject1);
			// tv1.setText("������ �б� ����");
		}

	}


    // protected Dialog onCreateDialog(int id) {
	//
	// //���ҽ��� ����
	// TextView listName
	// =(TextView)linear.getView().findViewById(R.id.dialogName);
	// TextView dialogText
	// =(TextView)linear.getView().findViewById(R.id.dialogText);
	// TextView critic = (TextView)linear.getView().findViewById(R.id.critic);
	// RatingBar rate =
	// (RatingBar)linear.getView().findViewById(R.id.ratingbar);
	//
	// // TextView dateText = (TextView)
	// getActivity().getView().findViewById(R.id.selectedDateLabel);
	//
	//
	// switch(id) {
	//
	//
	// case itemView:
	// // rating.setRating(3);
	// listName.setText(list.get(itemPosition).name);
	// double test = list.get(itemPosition).sum_dur * 100 / total_dur;
	// dialogText.setText(R.string.totalTime + list.get(itemPosition).sum_dur +
	// R.string.sec + "\n"
	// +"����Ƚ�� : " + list.get(itemPosition).in_count + "ȸ\n"
	// + "���ű��� : " + list.get(itemPosition).in_dur + "��\n"
	// + "�߽�Ƚ�� : " + list.get(itemPosition).out_count + "ȸ\n"
	// + "�߽ű��� : " + list.get(itemPosition).out_dur + "��\n"
	// // + test
	// );
	//
	// if( (list.get(itemPosition).sum_dur * 100 / total_dur) > 5)
	// critic.setText("ģ�� ģ��");
	// else if( (list.get(itemPosition).sum_dur * 100 / total_dur) > 3)
	// critic.setText("�׳� ģ��");
	// else if( (list.get(itemPosition).sum_dur * 100 / total_dur) > 1)
	// critic.setText("����");
	// else
	// critic.setText("����?");
	//
	//
	// if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 30) {
	// rate.setRating(4);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 25) {
	// rate.setRating((float)4.5);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 20) {
	// rate.setRating((float)4.0);
	// }
	//
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 15) {
	// rate.setRating((float)2.5);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 10) {
	// rate.setRating((float)2.0);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 5) {
	// rate.setRating((float)1.5);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 3) {
	// rate.setRating((float)1.0);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 0.5) {
	// rate.setRating((float)0.5);
	// }
	// else {
	// rate.setRating(0);
	// }
	// return new AlertDialog.Builder(callFragment.getActivity())
	// .setView(linear)
	// // .setTitle("test")
	// .setPositiveButton(R.string.confirm, new
	// DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {}
	// })
	// .create();
	// }
	// return null;
	// }
	//
	// protected void onPrepareDialog(int id, Dialog dialog) {
	//
	// TextView listName
	// =(TextView)linear.getView().findViewById(R.id.dialogName);
	// TextView dialogText
	// =(TextView)linear.getView().findViewById(R.id.dialogText);
	// TextView critic = (TextView)linear.getView().findViewById(R.id.critic);
	// RatingBar rate =
	// (RatingBar)linear.getView().findViewById(R.id.ratingbar);
	// super.onPrepareDialog(id, dialog);
	// switch(id) {
	// case itemView:
	// listName.setText(list.get(itemPosition).name);
	// double test = list.get(itemPosition).sum_dur * 100 / total_dur;
	// dialogText.setText(getString(R.string.totalTime) + " : " +
	// list.get(itemPosition).sum_dur + getString(R.string.sec) + "\n"
	// + getString(R.string.receivingCount) +" : " +
	// list.get(itemPosition).in_count + getString(R.string.times) + "\n"
	// + getString(R.string.receivingTime) +" : " +
	// list.get(itemPosition).in_dur + getString(R.string.sec) + "\n"
	// + getString(R.string.outGoingCount) +" : " +
	// list.get(itemPosition).out_count + getString(R.string.times) + "\n"
	// + getString(R.string.outGoingTime) +" : " +
	// list.get(itemPosition).out_dur + getString(R.string.sec) + "\n"
	// // + test
	// );
	//
	// if( (list.get(itemPosition).sum_dur * 100 / total_dur) > 5)
	// critic.setText(R.string.Rank5);
	// else if( (list.get(itemPosition).sum_dur * 100 / total_dur) > 3)
	// critic.setText(R.string.Rank4);
	// else if( (list.get(itemPosition).sum_dur * 100 / total_dur) > 1)
	// critic.setText(R.string.Rank3);
	// else
	// critic.setText(R.string.Rank2);
	//
	// if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 30) {
	// rate.setRating(4);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 25) {
	// rate.setRating((float)4.5);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 20) {
	// rate.setRating((float)4.0);
	// }
	// // else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 12) {
	// // rate.setRating((float)3.5);
	// // }
	// // else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 8) {
	// // rate.setRating((float)3.0);
	// // }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 15) {
	// rate.setRating((float)2.5);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 10) {
	// rate.setRating((float)2.0);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 5) {
	// rate.setRating((float)1.5);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 3) {
	// rate.setRating((float)1.0);
	// }
	// else if(( list.get(itemPosition).sum_dur * 100 / total_dur) > 0.5) {
	// rate.setRating((float)0.5);
	// }
	// else {
	// rate.setRating(0);
	// }
	// // listName.setText(list.get(itemPosition).name);
	// // dialog.setTitle(list.get(itemPosition).name);
	// break;
	// }
	// }

	static class sumDurCompareDesc implements Comparator<Info> {

		@Override
		public int compare(Info one, Info two) {
			// TODO Auto-generated method stub

			return (two.getSum_dur() > one.getSum_dur() ? 1 : 0);
		}

	}
}
