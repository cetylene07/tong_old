/*
 * 통화 분석 화면
 */
package com.hb.app.tong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class CallListFragment extends Fragment {
	/**
	 * @uml.property  name="list"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.smartstat.info.Info"
	 */
	ArrayList<Info> list = new ArrayList<Info>(); // 통계정보를 동적리스트로 객체 생성
	/**
	 * @uml.property  name="temp_list"
	 */
	ArrayList<Info> temp_list = new ArrayList<Info>(); // 통계정보를 동적리스트로 객체 생성

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
	double total_average_in_dur = 0; // 평균 수신 길이 총합
	/**
	 * @uml.property  name="total_average_out_dur"
	 */
	double total_average_out_dur = 0; // 평균 발신 길이 총합
	/**
	 * @uml.property  name="total_average_sum_dur"
	 */
	double total_average_sum_dur = 0; // 평균 수발신 길이 총합
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

	// 그냥 아무렇게나 쓰는 임시 변수
	/**
	 * @uml.property  name="tmp1"
	 */
	int tmp1;

	/**
	 * @uml.property  name="name"
	 */
	String name; // 이름
	/**
	 * @uml.property  name="number"
	 */
	String number; // 전화번호
	/**
	 * @uml.property  name="date"
	 */
	long date; // 통화 날짜

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
	static int itemPosition = 0;// 리스트를 눌렀을 때 위치

	// 5.16
	// 이미지를 불러오기 위해서 선언
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
		// list 초기화
		for (int i = 0; i < list.size(); i++)
			list.remove(i);
		return inflater.inflate(R.layout.fragment_call_list, container, false);
	}

	public void onStart() {
		super.onStart();
		// setContentView(R.layout.activity_call);

		// 5.16
		linear = (LinearLayout) View.inflate(getActivity(), R.layout.item_view,
				null);// View를 불러옵니다.
		// import android.provider.ContactsContract 필요
		Uri uContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 주소록이
																				// 저장된
																				// Uri

		/*
		 * updating 2013.11.12 월별 검색 기능 추가하려고 함
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

		if (cursor.moveToNext()) // content:sms 의 Uri를 가지면 성공적으로 Uri를 발견한 것으로 설정
			uri_found = true;

		if (uri_found == true) {

			int ididx = cursor.getColumnIndex(ContactsContract.Contacts._ID);

			// 통화대상자(이름)
			int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);

			// 통화 시점(0.001초 단위의 절대시간)
			int dateidx = cursor.getColumnIndex(CallLog.Calls.DATE);

			// 전화번호
			int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER);

			// 통화시간
			int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION);

			// 통화종류(수신,발신,부재중)
			int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE);

			boolean found = false; // 같은 이름을 찾으면 누적횟수를 증가시킨다. 찾지못하면 리스트에 추가

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");

			int where = 0; // list에 현재 이름값이 어디에 있는지 확인

			while (cursor.moveToNext()) { // cursor가 읽지 못할 때 까지 반복

				// 통화 대상자
				Info temp = new Info();
				Iterator<Info> it = list.iterator(); // iterator로 변환

				name = cursor.getString(nameidx); // 이름을 문자열로 변환
				number = cursor.getString(numidx);

				if (name == null) {
					name = cursor.getString(numidx); // 이름이 저장되지 않으면 번호로 저장
				}

				temp.setName(name);

				found = false;
				where = 0;
				while (it.hasNext()) {
					Info data = it.next();
					if (data.getName().equals(name)) { // list에 있는 원소 중 이름이 같은
														// 것을 찾으면
						found = true;

						/*
						 * data의 주소를 temp에 저장시킨다 따라서 temp에서 필드를 연산하면 이게 data에서
						 * 동시에 연산되는 거랑 마찬가지! ㅋㅋ 생각하지도 못한 거였는데 우왕ㅋ 굳 ㅋ
						 */

						temp = data;
						break;
					}
					where++;
				}

				// 통화 종류
				int type = cursor.getInt(typeidx);
				// row.put("type", type);

				String stype;
				switch (type) {
				case CallLog.Calls.INCOMING_TYPE:
					stype = "수신";
					total_incall_count++;
					total_indur += cursor.getInt(duridx);

					temp.inCreaseInCount();
					temp.setIn_dur(temp.getIn_dur() + cursor.getInt(duridx));
					temp.setSum_dur(temp.getSum_dur() + cursor.getInt(duridx));

					break;
				case CallLog.Calls.OUTGOING_TYPE:
					total_outcall_count++;
					stype = "발신";
					temp.inCreaseOutCount();
					total_outdur += cursor.getInt(duridx);
					temp.setOut_dur(temp.getOut_dur() + cursor.getInt(duridx));
					temp.setSum_dur(temp.getSum_dur() + cursor.getInt(duridx));
					break;
				case CallLog.Calls.MISSED_TYPE:
					stype = "부재중";
					total_miss++;
					temp.inCreaseMissCount();
					break;

				}

				// 통화 날짜
				long date = cursor.getLong(dateidx);
				// row.put("date", date);

				sdate = formatter.format(new Date(date));

				// 통화 시간
				int duration = cursor.getInt(duridx);
				// row.put("duration", duration);

				if (found == false) {
					list.add(temp); // 새로 추가
				}

			}
			total_dur = total_indur + total_outdur;

			for (int i = 0; i < list.size(); i++) {
				// 객체의 비율 계산
				list.get(i).setIncount_percent(
						list.get(i).getIn_count() / total_incall_count * 100); // 수신
																				// 횟수
																				// 비율
				list.get(i).indur_percent = list.get(i).in_dur / total_indur
						* 100; // 수신길이비율
				list.get(i).outcount_percent = list.get(i).out_count
						/ total_incall_count * 100; // 발신 횟수 비율
				list.get(i).outdur_percent = list.get(i).out_dur / total_outdur
						* 100; // 발신 길이 비율
				list.get(i).miss_percent = list.get(i).miss_count / total_miss
						* 100; // 부재 횟수 비율

				if (list.get(i).in_count > 0)
					list.get(i).average_in_dur = list.get(i).in_dur
							/ list.get(i).in_count; // 평균 수신 길이
				else
					list.get(i).average_in_dur = 0;
				total_average_in_dur += list.get(i).average_in_dur;

				if (list.get(i).out_count > 0)
					list.get(i).average_out_dur = list.get(i).out_dur
							/ list.get(i).out_count; // 평균 발신 길이
				else
					list.get(i).average_out_dur = 0;
				total_average_out_dur += list.get(i).average_out_dur;

			}

			for (int i = 0; i < list.size(); i++) {
				list.get(i).average_in_dur_percent = list.get(i).average_in_dur
						/ total_average_in_dur * 100;
				list.get(i).average_out_dur_percent = list.get(i).average_out_dur
						/ total_average_out_dur * 100;

				// 수신길이와 발신길이의 합 계산
				list.get(i).sum_dur = list.get(i).in_dur + list.get(i).out_dur;

				list.get(i).sum_dur_percent = list.get(i).sum_dur
						/ (total_indur + total_outdur) * 100;
			}

			cursor.close();

			// 스피너
			Spinner spin = (Spinner) getView().findViewById(R.id.call_spinner1);
			spin.setPrompt("Choice Option");
			adspin = ArrayAdapter.createFromResource(getActivity(),
					R.array.call_choice, android.R.layout.simple_spinner_item);
			adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin.setAdapter(adspin);
			spin.setOnItemSelectedListener(new OnItemSelectedListener() {

				// 스피너 아이템을 선택한 것에 따라
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

					// 리스트에 있는 아이템을 선태했을 때 대화상자가 딱 뜨게 만드는 소스코드!
					OnItemClickListener listener = new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {

							itemPosition = position;
							// showDialog(itemView);

						}

					}; // 대화상자 소스코드 끝!

					// temp_list 삭제 기법
					for (int i = temp_list.size() - 1; i >= 0; i--) {
						temp_list.remove(i);
					}

					switch (position) {
					case 0:
						// 정렬
						Collections.sort(list, new sumDurCompareDesc());

						/*
						 * 순위저장 1부터 차례대로 넣고, 같은 값을 가지면 같은 등수가 되게 한다.
						 */
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).getSum_dur() != list.get(i)
									.getSum_dur()) {
								jj++;
							}
						}

						// // 맨 마지막 데이터의 순위는 따로 코딩해야 된다.
						// if (list.get(list.size() -
						// 1).in_dur+list.get(list.size() - 1).out_dur !=
						// list.get(list.size() - 1).in_dur+list.get(list.size()
						// - 1).out_dur) {
						//
						// list.get(list.size() - 1).rank = jj + 1;
						// } else {
						// list.get(list.size() - 1).rank = jj;
						// }

						// 0이상만 temp_list에 저장한다.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getSum_dur() > 0) {
								temp_list.add(list.get(i));

							}
						}

						// 커스텀 뷰를 이용하여 리스트뷰에 출력
						TotalDurationListAdapter MyAdapter = new TotalDurationListAdapter(
								getActivity(), R.layout.incall_view, temp_list);
						ListView MyList;

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(MyAdapter);

						MyList.setOnItemClickListener(listener);

						break;
					case 1:

						sub_text1.setText("총 수신 길이 : ");
						sub_value1.setText(String.valueOf((int) total_indur)
								+ "sec");
						sub_text2.setText("1인당 평균 수신 길이 : ");
						// 퍼센트 소수점 2자리까지 표시하는 방법
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

						// 순위를 저장하는 반복문
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).in_dur != list.get(i).in_dur) {
								jj++;
							}
						}

						// 맨 마지막 데이터의 순위는 따로 코딩해야 된다.
						if (list.get(list.size() - 1).in_dur != list.get(list
								.size() - 1).in_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0이상만 temp_list에 저장한다.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).in_dur > 0) {
								temp_list.add(list.get(i));

							}
						}

						// 커스텀 뷰를 이용하여 리스트뷰에 출력
						indur_Adapter indurView = new indur_Adapter(
								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(indurView);

						break;

					case 2:

						sub_text1.setText("총 평균 수신 길이 : ");
						sub_value1.setText(String
								.valueOf((int) total_average_in_dur) + "초");
						sub_text2.setText("1인당 평균 수신 길이 : ");
						// 퍼센트 소수점 2자리까지 표시하는 방법
						s_value = String.format("%.2f", total_average_in_dur
								/ list.size());
						sub_value2.setText(s_value + "초");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("순위        이름                평균수신길이             비율");
						// 선택정렬을 이용하여 수신길이를 내림차순으로 정렬함(큰게 먼저오게)
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

						// 순위를 저장하는 반복문
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).average_in_dur != list.get(i).average_in_dur) {
								jj++;
							}
						}

						// 맨 마지막 데이터의 순위는 따로 코딩해야 된다.
						if (list.get(list.size() - 1).average_in_dur != list
								.get(list.size() - 1).average_in_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0이상만 temp_list에 저장한다.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).average_in_dur > 0) {
								temp_list.add(list.get(i));

							}
						}

						// 커스텀 뷰를 이용하여 리스트뷰에 출력
						average_indur_Adapter average_indurView = new average_indur_Adapter(
								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(average_indurView);

						break;

					case 3:

						sub_text1.setText("총 발신 회수 : ");
						sub_value1.setText(String
								.valueOf((int) total_outcall_count) + "회");
						sub_text2.setText("1인당 평균 발신 회수 : ");
						// 퍼센트 소수점 2자리까지 표시하는 방법
						s_value = String.format("%.2f", total_outcall_count
								/ list.size());
						sub_value2.setText(s_value + "회");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("순위        이름                    발신횟수             비율");
						// 선택정렬을 이용하여 발신횟수를 내림차순으로 정렬함(큰게 먼저오게)
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

						// 순위를 저장하는 반복문
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).out_count != list.get(i).out_count) {
								jj++;
							}
						}

						// 맨 마지막 데이터의 순위는 따로 코딩해야 된다.
						if (list.get(list.size() - 1).out_count != list
								.get(list.size() - 1).out_count) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0이상만 temp_list에 저장한다.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).out_count > 0) {
								temp_list.add(list.get(i));
							}
						}

						// 커스텀 뷰를 이용하여 리스트뷰에 출력
						outcount_Adapter OutCallAdapter = new outcount_Adapter(
								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(OutCallAdapter);

						break;
					case 4:

						sub_text1.setText("총 발신 길이 : ");
						sub_value1.setText(String.valueOf((int) total_outdur)
								+ "초");
						sub_text2.setText("1인당 평균 발신 길이 : ");
						// 퍼센트 소수점 2자리까지 표시하는 방법
						s_value = String.format("%.2f",
								total_outdur / list.size());
						sub_value2.setText(s_value + "초");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("순위        이름                    발신길이             비율");
						// 선택정렬을 이용하여 수신횟수를 내림차순으로 정렬함(큰게 먼저오게)
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

						// 순위를 저장하는 반복문
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).out_dur != list.get(i).out_dur) {
								jj++;
							}
						}

						// 맨 마지막 데이터의 순위는 따로 코딩해야 된다.
						if (list.get(list.size() - 1).out_dur != list.get(list
								.size() - 1).out_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0이상만 temp_list에 저장한다.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).out_dur > 0) {
								temp_list.add(list.get(i));
							}
						}

						// 커스텀 뷰를 이용하여 리스트뷰에 출력
						outdur_Adapter OutDurAdapter = new outdur_Adapter(
								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(OutDurAdapter);

						break;

					case 5:

						sub_text1.setText("총 평균 발신 길이 : ");
						sub_value1.setText(String
								.valueOf((int) total_average_out_dur) + "초");
						sub_text2.setText("1인당 평균 발신 길이 : ");
						// 퍼센트 소수점 2자리까지 표시하는 방법
						s_value = String.format("%.2f", total_average_out_dur
								/ list.size());
						sub_value2.setText(s_value + "초");

						// call_text = (TextView)
						// getView().findViewById(R.id.call_text);
						// call_text
						// .setText("순위        이름                평균발신길이             비율");
						// 선택정렬을 이용하여 수신길이를 내림차순으로 정렬함(큰게 먼저오게)
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

						// 순위를 저장하는 반복문
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).average_out_dur != list.get(i).average_out_dur) {
								jj++;
							}
						}

						// 맨 마지막 데이터의 순위는 따로 코딩해야 된다.
						if (list.get(list.size() - 1).average_out_dur != list
								.get(list.size() - 1).average_out_dur) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0이상만 temp_list에 저장한다.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).average_out_dur > 0) {
								temp_list.add(list.get(i));

							}
						}

						// 커스텀 뷰를 이용하여 리스트뷰에 출력
						average_outdur_Adapter average_outdurView = new average_outdur_Adapter(
								getActivity(), R.layout.incall_view, temp_list);

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(average_outdurView);

						break;

					case 6:

						sub_text1.setText("총 부재 회수 : ");
						sub_value1.setText(String.valueOf((int) total_miss)
								+ "회");
						sub_text2.setText("1인당 평균 부재 회수 : ");
						// 퍼센트 소수점 2자리까지 표시하는 방법
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

						// 순위를 저장하는 반복문
						jj = 1;
						for (int i = 0; i < list.size() - 1; i++) {

							list.get(i).rank = jj;
							if (list.get(i + 1).miss_count != list.get(i).miss_count) {
								jj++;
							}
						}

						// 맨 마지막 데이터의 순위는 따로 코딩해야 된다.
						if (list.get(list.size() - 1).miss_count != list
								.get(list.size() - 1).miss_count) {

							list.get(list.size() - 1).rank = jj + 1;
						} else {
							list.get(list.size() - 1).rank = jj;
						}

						// 0이상만 temp_list에 저장한다.
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).miss_count > 0) {
								temp_list.add(list.get(i));
							}
						}

						// 커스텀 뷰를 이용하여 리스트뷰에 출력
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
			// tv1.setText("데이터 읽기 실패");
		}

	}

	// protected Dialog onCreateDialog(int id) {
	//
	// //리소스와 연결
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
	// +"수신횟수 : " + list.get(itemPosition).in_count + "회\n"
	// + "수신길이 : " + list.get(itemPosition).in_dur + "초\n"
	// + "발신횟수 : " + list.get(itemPosition).out_count + "회\n"
	// + "발신길이 : " + list.get(itemPosition).out_dur + "초\n"
	// // + test
	// );
	//
	// if( (list.get(itemPosition).sum_dur * 100 / total_dur) > 5)
	// critic.setText("친한 친구");
	// else if( (list.get(itemPosition).sum_dur * 100 / total_dur) > 3)
	// critic.setText("그냥 친구");
	// else if( (list.get(itemPosition).sum_dur * 100 / total_dur) > 1)
	// critic.setText("지인");
	// else
	// critic.setText("누구?");
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
