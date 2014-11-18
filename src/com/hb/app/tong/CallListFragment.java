package com.hb.app.tong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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

public class CallListFragment extends Fragment  {

	ArrayList<Info> list = new ArrayList<Info>(); 

	ArrayList<Info> temp_list = new ArrayList<Info>(); 
	ArrayList<DateInfo> date_list = new ArrayList<DateInfo>();
	double total_dur = 0;
	double total_incall_count = 0;
	double total_outcall_count = 0;
	double total_average_in_dur = 0; 
	double total_average_out_dur = 0; 
	double total_average_sum_dur = 0; 
	double total_indur = 0;
	double total_outdur = 0;
	double total_miss = 0;
	double temp_value;
	int jj;
	boolean uri_found = false;

	int tmp1;
	String name; 
	String number; 
	long date; 
	String sdate;

	ArrayAdapter<CharSequence> adspin;

	LinearLayout linear;

	final static int itemView = 0;
	static int itemPosition = 0;

    //adapterView is a sort of CustomView
    public MyListAdapter adapterView = null;


	final String[] CONTACTS_PROJECTION = new String[] {
			ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME

	};


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		for (int i = 0; i < list.size(); i++)
			list.remove(i);
		return inflater.inflate(R.layout.fragment_call_list, container, false);
	}

	public void onStart() {
		super.onStart();


        //2014.06.30
        //fix duplicate data when resume activity
        temp_list.clear();
        list.clear();

		// 5.16
		linear = (LinearLayout) View.inflate(getActivity(), R.layout.item_view,
				null);

		Uri uContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; 

		this.buttonControl(); // 월 이동하는 버튼 함수



		ContentResolver cr = getActivity().getContentResolver();		
		Cursor cursorContact = null;
		Cursor c;


        /*
        * 2014.07.02
        * To sort data in Info class instance...
        * Test : TreeSet, Array
        * */

        final TreeMap<Info, Integer> testMap = new TreeMap<Info, Integer>();

		String name = null;

		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null,
				CallLog.Calls.DATE + " DESC");

		if (cursor.moveToNext()) 
			uri_found = true;

		if (uri_found == true) {

			int ididx = cursor.getColumnIndex(ContactsContract.Contacts._ID);

			int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);

			int dateidx = cursor.getColumnIndex(CallLog.Calls.DATE);

			int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER);

			int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION);

			int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE);

			boolean found = false; // 占쏙옙占쏙옙 占싱몌옙占쏙옙 찾占쏙옙占쏙옙 占쏙옙占쏙옙횟占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙킨占쏙옙. 찾占쏙옙占쏙옙占싹몌옙 占쏙옙占쏙옙트占쏙옙 占쌩곤옙

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");

			int where = 0; // list占쏙옙 占쏙옙占쏙옙 占싱몌옙占쏙옙占쏙옙 占쏙옙占�占쌍댐옙占쏙옙 확占쏙옙

			while (cursor.moveToNext()) { // cursor占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙 占쏙옙占쏙옙 占쌥븝옙

				Info temp = new Info();
				Iterator<Info> it = list.iterator(); // iterator占쏙옙 占쏙옙환

				name = cursor.getString(nameidx); // 占싱몌옙占쏙옙 占쏙옙占쌘울옙占쏙옙 占쏙옙환
				number = cursor.getString(numidx);

				if (name == null) {
					name = cursor.getString(numidx); // 占싱몌옙占쏙옙 占쏙옙占쏙옙占쏙옙占�占쏙옙占쏙옙占쏙옙 占쏙옙호占쏙옙 占쏙옙占쏙옙
				}

				temp.setName(name);

				found = false;
				where = 0;
				while (it.hasNext()) {
					Info data = it.next();
					if (data.getName().equals(name)) { 
						
						found = true;
						temp = data;
						break;
					}
					where++;
				}

				int type = cursor.getInt(typeidx);

				String stype;
				switch (type) {
				case CallLog.Calls.INCOMING_TYPE:
					stype = "占쏙옙占쏙옙";
					total_incall_count++;
					total_indur += cursor.getInt(duridx);

					temp.inCreaseInCount();
					temp.setIn_dur(temp.getIn_dur() + cursor.getInt(duridx));
					temp.setSum_dur(temp.getSum_dur() + cursor.getInt(duridx));

					break;
				case CallLog.Calls.OUTGOING_TYPE:
					total_outcall_count++;
					stype = "占쌩쏙옙";
					temp.inCreaseOutCount();
					total_outdur += cursor.getInt(duridx);
					temp.setOut_dur(temp.getOut_dur() + cursor.getInt(duridx));
					temp.setSum_dur(temp.getSum_dur() + cursor.getInt(duridx));
					break;
				case CallLog.Calls.MISSED_TYPE:
					stype = "占쏙옙占쏙옙占쏙옙";
					total_miss++;
					temp.inCreaseMissCount();
					break;

				}

				long date = cursor.getLong(dateidx);

				sdate = formatter.format(new Date(date));

				int duration = cursor.getInt(duridx);

				if (found == false) {
					list.add(temp); 
				}

			}
			total_dur = total_indur + total_outdur;

			for (int i = 0; i < list.size(); i++) {
				list.get(i).setIncount_percent(
						list.get(i).getIn_count() / total_incall_count * 100); // 占쏙옙占쏙옙
																				// 횟占쏙옙
																				// 占쏙옙占쏙옙
				list.get(i).indur_percent = list.get(i).in_dur / total_indur
						* 100; // 占쏙옙占신깍옙占싱븝옙占쏙옙
				list.get(i).outcount_percent = list.get(i).out_count
						/ total_incall_count * 100; // 占쌩쏙옙 횟占쏙옙 占쏙옙占쏙옙
				list.get(i).outdur_percent = list.get(i).out_dur / total_outdur
						* 100; // 占쌩쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙
				list.get(i).miss_percent = list.get(i).miss_count / total_miss
						* 100; // 占쏙옙占쏙옙 횟占쏙옙 占쏙옙占쏙옙

				if (list.get(i).in_count > 0)
					list.get(i).average_in_dur = list.get(i).in_dur
							/ list.get(i).in_count; // 占쏙옙占�占쏙옙占쏙옙 占쏙옙占쏙옙
				else
					list.get(i).average_in_dur = 0;
				total_average_in_dur += list.get(i).average_in_dur;

				if (list.get(i).out_count > 0)
					list.get(i).average_out_dur = list.get(i).out_dur
							/ list.get(i).out_count; // 占쏙옙占�占쌩쏙옙 占쏙옙占쏙옙
				else
					list.get(i).average_out_dur = 0;
				total_average_out_dur += list.get(i).average_out_dur;

			}

			for (int i = 0; i < list.size(); i++) {
				list.get(i).average_in_dur_percent = list.get(i).average_in_dur
						/ total_average_in_dur * 100;
				list.get(i).average_out_dur_percent = list.get(i).average_out_dur
						/ total_average_out_dur * 100;

				// 占쏙옙占신깍옙占싱울옙 占쌩신깍옙占쏙옙占쏙옙 占쏙옙 占쏙옙占�
				list.get(i).sum_dur = list.get(i).in_dur + list.get(i).out_dur;
//				t.get(i).sum_dur = list.get(i).in_dur + list.get(i).out_dur;

				list.get(i).sum_dur_percent = list.get(i).sum_dur
						/ (total_indur + total_outdur) * 100;
			}



			cursor.close();

			// 占쏙옙占실놂옙
			Spinner spin = (Spinner) getView().findViewById(R.id.call_spinner1);
			spin.setPrompt("Choice Option");
			adspin = ArrayAdapter.createFromResource(getActivity(),
					R.array.call_choice, android.R.layout.simple_spinner_item);
			adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin.setAdapter(adspin);
			spin.setOnItemSelectedListener(new OnItemSelectedListener() {

				// 占쏙옙占실놂옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占싶울옙 占쏙옙占�
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

					// 占쏙옙占쏙옙트占쏙옙 占쌍댐옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙 占쏙옙화占쏙옙占쌘곤옙 占쏙옙 占쌩곤옙 占쏙옙占쏙옙占�占쌀쏙옙占쌘듸옙!
					OnItemClickListener listener = new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {

							itemPosition = position;
							// showDialog(itemView);

						}

					}; // 占쏙옙화占쏙옙占쏙옙 占쌀쏙옙占쌘듸옙 占쏙옙!

					// temp_list 占쏙옙占쏙옙 占쏙옙占�
					for (int i = temp_list.size() - 1; i >= 0; i--) {
						temp_list.remove(i);
					}

					switch (position) {
					case 0:
						// 占쏙옙占쏙옙

                        for (int i = 0; i < list.size(); i++) {
                            int max = i;
                            for (int j = i + 1; j < list.size(); j++) {
                                if (list.get(j).sum_dur > list.get(max).sum_dur) {
                                    max = j;
                                }
                            }
                            Info trans = new Info();
                            trans = list.get(i);
                            list.set(i, list.get(max));
                            list.set(max, trans);
                        }

                        // 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쌥븝옙占쏙옙
                        jj = 1;
                        for (int i = 0; i < list.size() - 1; i++) {

                            list.get(i).rank = jj;
                            if (list.get(i + 1).sum_dur != list.get(i).sum_dur) {
                                jj++;
                            }
                        }

                        // 占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占�占쌘듸옙占쌔억옙 占싫댐옙.
                        if (list.get(list.size() - 1).sum_dur != list.get(list
                                .size() - 1).sum_dur) {

                            list.get(list.size() - 1).rank = jj + 1;
                        } else {
                            list.get(list.size() - 1).rank = jj;
                        }

						// 0占싱삼옙 temp_list占쏙옙 占쏙옙占쏙옙占싼댐옙.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getSum_dur() > 0) {
								temp_list.add(list.get(i));

							}
						}

                        adapterView = new MyListAdapter(getActivity(), R.layout.incall_view, temp_list, "sumdur");



						ListView MyList;

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(adapterView);

						MyList.setOnItemClickListener(listener);

						break;
					case 1:

						sub_text1.setText("占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙 : ");
						sub_value1.setText(String.valueOf((int) total_indur)
								+ "sec");
						sub_text2.setText("1占싸댐옙 占쏙옙占�占쏙옙占쏙옙 占쏙옙占쏙옙 : ");
						// 占쌜쇽옙트 占쌀쇽옙占쏙옙 2占쌘몌옙占쏙옙占쏙옙 표占쏙옙占싹댐옙 占쏙옙占�
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

						// 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쌥븝옙占쏙옙
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).in_dur != list.get(i).in_dur) {
								jj++;
							}
						}

						// 占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占�占쌘듸옙占쌔억옙 占싫댐옙.
						if (list.get(list.size() - 1).in_dur != list.get(list
								.size() - 1).in_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0占싱삼옙 temp_list占쏙옙 占쏙옙占쏙옙占싼댐옙.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).in_dur > 0) {
								temp_list.add(list.get(i));

							}
						}

						// 커占쏙옙占쏙옙 占썰를 占싱울옙占싹울옙 占쏙옙占쏙옙트占썰에 占쏙옙占�
                        adapterView = new MyListAdapter(getActivity(), R.layout.incall_view, temp_list, "indur");
//						indur_Adapter indurView = new indur_Adapter(
//								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(adapterView);

						break;

					case 2:

						sub_text1.setText("占쏙옙 占쏙옙占�占쏙옙占쏙옙 占쏙옙占쏙옙 : ");
						sub_value1.setText(String
								.valueOf((int) total_average_in_dur) + "占쏙옙");
						sub_text2.setText("1占싸댐옙 占쏙옙占�占쏙옙占쏙옙 占쏙옙占쏙옙 : ");
						// 占쌜쇽옙트 占쌀쇽옙占쏙옙 2占쌘몌옙占쏙옙占쏙옙 표占쏙옙占싹댐옙 占쏙옙占�
						s_value = String.format("%.2f", total_average_in_dur
								/ list.size());
						sub_value2.setText(s_value + "占쏙옙");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("占쏙옙占쏙옙        占싱몌옙                占쏙옙欖占쏙옙킥占쏙옙占�            占쏙옙占쏙옙");
						// 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占싱울옙占싹울옙 占쏙옙占신깍옙占싱몌옙 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙(큰占쏙옙 占쏙옙占쏙옙占쏙옙占�
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

						// 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쌥븝옙占쏙옙
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).average_in_dur != list.get(i).average_in_dur) {
								jj++;
							}
						}

						// 占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占�占쌘듸옙占쌔억옙 占싫댐옙.
						if (list.get(list.size() - 1).average_in_dur != list
								.get(list.size() - 1).average_in_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0占싱삼옙 temp_list占쏙옙 占쏙옙占쏙옙占싼댐옙.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).average_in_dur > 0) {
								temp_list.add(list.get(i));

							}
						}

						// 커占쏙옙占쏙옙 占썰를 占싱울옙占싹울옙 占쏙옙占쏙옙트占썰에 占쏙옙占�
                        adapterView = new MyListAdapter(getActivity(), R.layout.incall_view, temp_list, "average_indur");

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(adapterView);

						break;

					case 3:

						sub_text1.setText("占쏙옙 占쌩쏙옙 회占쏙옙 : ");
						sub_value1.setText(String
								.valueOf((int) total_outcall_count) + "회");
						sub_text2.setText("1占싸댐옙 占쏙옙占�占쌩쏙옙 회占쏙옙 : ");
						// 占쌜쇽옙트 占쌀쇽옙占쏙옙 2占쌘몌옙占쏙옙占쏙옙 표占쏙옙占싹댐옙 占쏙옙占�
						s_value = String.format("%.2f", total_outcall_count
								/ list.size());
						sub_value2.setText(s_value + "회");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("占쏙옙占쏙옙        占싱몌옙                    占쌩쏙옙횟占쏙옙             占쏙옙占쏙옙");
						// 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占싱울옙占싹울옙 占쌩쏙옙횟占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙(큰占쏙옙 占쏙옙占쏙옙占쏙옙占�
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

						// 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쌥븝옙占쏙옙
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).out_count != list.get(i).out_count) {
								jj++;
							}
						}

						// 占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占�占쌘듸옙占쌔억옙 占싫댐옙.
						if (list.get(list.size() - 1).out_count != list
								.get(list.size() - 1).out_count) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0占싱삼옙 temp_list占쏙옙 占쏙옙占쏙옙占싼댐옙.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).out_count > 0) {
								temp_list.add(list.get(i));
							}
						}

						// 커占쏙옙占쏙옙 占썰를 占싱울옙占싹울옙 占쏙옙占쏙옙트占썰에 占쏙옙占�
                        adapterView = new MyListAdapter(getActivity(), R.layout.incall_view, temp_list, "outcount");

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(adapterView);

						break;
					case 4:

						sub_text1.setText("占쏙옙 占쌩쏙옙 占쏙옙占쏙옙 : ");
						sub_value1.setText(String.valueOf((int) total_outdur)
								+ "占쏙옙");
						sub_text2.setText("1占싸댐옙 占쏙옙占�占쌩쏙옙 占쏙옙占쏙옙 : ");
						// 占쌜쇽옙트 占쌀쇽옙占쏙옙 2占쌘몌옙占쏙옙占쏙옙 표占쏙옙占싹댐옙 占쏙옙占�
						s_value = String.format("%.2f",
								total_outdur / list.size());
						sub_value2.setText(s_value + "占쏙옙");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("占쏙옙占쏙옙        占싱몌옙                    占쌩신깍옙占쏙옙             占쏙옙占쏙옙");
						// 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占싱울옙占싹울옙 占쏙옙占쏙옙횟占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙(큰占쏙옙 占쏙옙占쏙옙占쏙옙占�
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

						// 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쌥븝옙占쏙옙
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).out_dur != list.get(i).out_dur) {
								jj++;
							}
						}

						// 占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占�占쌘듸옙占쌔억옙 占싫댐옙.
						if (list.get(list.size() - 1).out_dur != list.get(list
								.size() - 1).out_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0占싱삼옙 temp_list占쏙옙 占쏙옙占쏙옙占싼댐옙.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).out_dur > 0) {
								temp_list.add(list.get(i));
							}
						}

						// 커占쏙옙占쏙옙 占썰를 占싱울옙占싹울옙 占쏙옙占쏙옙트占썰에 占쏙옙占�
                        adapterView = new MyListAdapter(getActivity(), R.layout.incall_view, temp_list, "outdur");

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(adapterView);

						break;

					case 5:

						sub_text1.setText("占쏙옙 占쏙옙占�占쌩쏙옙 占쏙옙占쏙옙 : ");
						sub_value1.setText(String
								.valueOf((int) total_average_out_dur) + "占쏙옙");
						sub_text2.setText("1占싸댐옙 占쏙옙占�占쌩쏙옙 占쏙옙占쏙옙 : ");
						// 占쌜쇽옙트 占쌀쇽옙占쏙옙 2占쌘몌옙占쏙옙占쏙옙 표占쏙옙占싹댐옙 占쏙옙占�
						s_value = String.format("%.2f", total_average_out_dur
								/ list.size());
						sub_value2.setText(s_value + "占쏙옙");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("占쏙옙占쏙옙        占싱몌옙                占쏙옙嵐颯킥占쏙옙占�            占쏙옙占쏙옙");
						// 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占싱울옙占싹울옙 占쏙옙占신깍옙占싱몌옙 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙(큰占쏙옙 占쏙옙占쏙옙占쏙옙占�
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

						// 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쌥븝옙占쏙옙
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).average_out_dur != list.get(i).average_out_dur) {
								jj++;
							}
						}

						// 占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占�占쌘듸옙占쌔억옙 占싫댐옙.
						if (list.get(list.size() - 1).average_out_dur != list
								.get(list.size() - 1).average_out_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0占싱삼옙 temp_list占쏙옙 占쏙옙占쏙옙占싼댐옙.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).average_out_dur > 0) {
								temp_list.add(list.get(i));

							}
						}

						// 커占쏙옙占쏙옙 占썰를 占싱울옙占싹울옙 占쏙옙占쏙옙트占썰에 占쏙옙占�
                        adapterView = new MyListAdapter(getActivity(), R.layout.incall_view, temp_list, "average_outdur");

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(adapterView);

						break;

					case 6:

						sub_text1.setText("占쏙옙 占쏙옙占쏙옙 회占쏙옙 : ");
						sub_value1.setText(String.valueOf((int) total_miss)
								+ "회");
						sub_text2.setText("1占싸댐옙 占쏙옙占�占쏙옙占쏙옙 회占쏙옙 : ");
						// 占쌜쇽옙트 占쌀쇽옙占쏙옙 2占쌘몌옙占쏙옙占쏙옙 표占쏙옙占싹댐옙 占쏙옙占�
						s_value = String.format("%.2f",
								total_miss / list.size());
						sub_value2.setText(s_value + "회");

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

						// 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쌥븝옙占쏙옙
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).miss_count != list.get(i).miss_count) {
								jj++;
							}
						}

						// 占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占�占쌘듸옙占쌔억옙 占싫댐옙.
						if (list.get(list.size() - 1).miss_count != list
								.get(list.size() - 1).miss_count) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0占싱삼옙 temp_list占쏙옙 占쏙옙占쏙옙占싼댐옙.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).miss_count > 0) {
								temp_list.add(list.get(i));
							}
						}

						// 커占쏙옙占쏙옙 占썰를 占싱울옙占싹울옙 占쏙옙占쏙옙트占썰에 占쏙옙占�
                        adapterView = new MyListAdapter(getActivity(), R.layout.incall_view, temp_list, "misscount");

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(adapterView);

						break;
					}
				}

				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});

		} else {
			// TextView tv1 = (TextView) getView().findViewById(R.id.subject1);
			// tv1.setText("占쏙옙占쏙옙占쏙옙 占싻깍옙 占쏙옙占쏙옙");
		}

	}

	
	void buttonControl()	{
		// 표시할 년,월을 보여주도록 하는 함수
		
		final Calendar cal = Calendar.getInstance();
		
		Button btnBeforeMonth = (Button) getView().findViewById(
				R.id.btnBeforeMonth);
		final Button btnAfterMonth = (Button) getView().findViewById(
				R.id.btnAfterMonth);
		final Button btnListMonth = (Button) getView().findViewById(
				R.id.btnListMonth);
		
		btnBeforeMonth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cal.add(Calendar.MONTH, -1);
				btnListMonth.setText(cal.get(Calendar.YEAR) + "년 "+ (cal.get(Calendar.MONTH)+1) + "월");
				
				if(cal.get(Calendar.MONTH) != Calendar.getInstance().get(Calendar.MONTH))	{
					//  미래의 월은 의미가 없으므로 안보이도록 한다.
					btnAfterMonth.setVisibility(View.VISIBLE);
				}
			}
		});
		
		btnAfterMonth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cal.add(Calendar.MONTH, 1);
				btnListMonth.setText(cal.get(Calendar.YEAR) + "년 "+ (cal.get(Calendar.MONTH)+1) + "월");
				
				if(cal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH))	{
					//  미래의 월은 의미가 없으므로 안보이도록 한다.
					btnAfterMonth.setVisibility(View.INVISIBLE);
				}
					
			}
		});

		
		btnListMonth.setText(cal.get(Calendar.YEAR) + "년 "+ (cal.get(Calendar.MONTH)+1) + "월");
		
		}


}
