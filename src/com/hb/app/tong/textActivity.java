package com.hb.app.tong;
///*
// * 통화 분석 화면
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
//	int rank;						//순위
//	String name; 					// 이름
//	int in_count; 					// 수신통화 누적 회수
//	double in_count_percent;		// 수신통화 누적 횟수 비율
//	int out_count; 					// 발신통화 누적 횟수
//	double out_count_percent;		// 발신통화 누적 횟수 비율
//	long date;						// 날짜
//
//	int get_incount() {
//		return in_count;
//	}
//}
//
//public class textActivity extends Activity implements OnItemSelectedListener {
//	int total_in_count = 0;										// 총 수신 메시지 개수
//	int total_out_count = 0;									// 총 발신 메시지 개수
//	TextView text1, text2;
//	TextView value1, value2;
//	ArrayList<sms_info> list = new ArrayList<sms_info>(); 		// 통계정보를 동적리스트로 객체 생성
//	ArrayList<sms_info> temp_list = new ArrayList<sms_info>(); 	// 통계정보를 동적리스트로 임시객체 생성
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
//		if(cursor.moveToNext())	//content:sms 의 Uri를 가지면 성공적으로 Uri를 발견한 것으로 설정
//			uri_found = true;
//		
//		if(cursor1 != null)
//			if(cursor1.moveToNext())	//content:sms 의 Uri를 가지면 성공적으로 Uri를 발견한 것으로 설정
//				uri_found = true;		
//
//
//		if (uri_found == true) {			
//			int nameidx = cursor.getColumnIndex("address"); // 문자대상자
//			int dateidx = cursor.getColumnIndex("date"); // 문자 시점.
//
//			int bodyidx = cursor.getColumnIndex("body");
//			int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER); // 전화번호
//			int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION); // 통화시간
//			int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE); // 통화종류(수신,발신,부재중)
//
//			boolean found = false; // 같은 이름을 찾으면 누적횟수를 증가시킨다. 찾지못하면 리스트에 추가
//
//			StringBuilder result = new StringBuilder();
//
//			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
//			result.append("총 기록 개수 : " + cursor.getCount() + "개\n");
//			int count = 0;
//			int where = 0; // list에 현재 이름값이 어디에 있는지 확인
//
//			while (cursor.moveToNext()) { // cursor가 읽지 못할 때 까지 반복
//				// 통화 대상자
//
//				sms_info temp = new sms_info();
//				Iterator<sms_info> it = list.iterator(); // iterator로 변환
//
//				String name = cursor.getString(nameidx); // 이름을 문자열로 변환
//
//				if (name == null) {
//					name = cursor.getString(numidx); // 이름이 저장되지 않으면 번호로 저장
//				}
//
//				temp.name = name;
//
//				found = false;
//				where = 0;
//				while (it.hasNext()) {
//					sms_info data = it.next();
//					if (data.name.equals(name)) { // list에 있는 원소 중 이름이 같은 것을 찾으면
//						found = true;
//
//						/*
//						 * data의 주소를 temp에 저장시킨다 따라서 temp에서 필드를 연산하면 이게 data에서
//						 * 동시에 연산되는 거랑 마찬가지! ㅋㅋ 생각하지도 못한 거였는데 우왕ㅋ 굳 ㅋ
//						 */
//
//						temp = data;
//						break;
//					}
//					where++;
//				}
//
//				// 통화 종류
//				int type = cursor.getInt(typeidx);
//				String stype;
//				switch (type) {
//
//				// 갤럭시S의 경우 거절은 4, 문자 송수신은 14, 13 으로 MMS 송수신은 16, 15으로 정의되어 있다.
//
//				case CallLog.Calls.INCOMING_TYPE:
//					stype = "문자받음";
//					temp.in_count++;
//					total_in_count++;
//					break;
//				case CallLog.Calls.OUTGOING_TYPE:
//					stype = "문자보냄";
//					temp.out_count++;
//					total_out_count++;
//					break;
//				default:
//					stype = "기타" + type;
//					break;
//
//				}
//
//				// 통화 날짜
//				long date = cursor.getLong(dateidx);
//				temp.date = date;
//				String sdate = formatter.format(new Date(date));
//
//				if (found == false) {
//					list.add(temp); // 새로 추가
//				}
//
//			}
//
//			// 퍼센트 개산하는 반복문
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
//			spin.setPrompt("선택하세유");
////			adspin = ArrayAdapter.createFromResource(this, R.array.sms_choice,
//					android.R.layout.simple_spinner_item);
//			adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			spin.setAdapter(adspin);
//			spin.setOnItemSelectedListener(this);
//
//		} else {
//
//			TextView tv1 = (TextView) findViewById(R.id.text_subject1);
//			tv1.setText("데이터 읽기 실패");
//		}
//	}
//
//	public void onItemSelected(AdapterView<?> parent, View view, int position,
//			long id) {
//
//		// temp_list 삭제 기법
//		for (int i = temp_list.size() - 1; i >= 0; i--) {
//			temp_list.remove(i);
//		}
//
//		TextView tv1 = (TextView) findViewById(R.id.text_subject1);
//		tv1.setText(adspin.getItem(position));
//		switch (position) {
//		case 0:
//			text1 = (TextView) findViewById(R.id.text1);
//			text1.setText("총 수신 메시지 개수 : ");
//			value1 = (TextView) findViewById(R.id.value1);
//			value1.setText(String.valueOf(total_in_count) + "개");
//
//			text2 = (TextView) findViewById(R.id.text2);
//			text2.setText("평균 수신 메시지 개수 : ");
//			value2 = (TextView) findViewById(R.id.value2);
//
//			average = (double) total_in_count / (double) list.size();
//			temp_average = String.format("%.2f", average);
//			value2.setText(temp_average + "개");
//
//			TextView call_text = (TextView) findViewById(R.id.text_title1);
//			call_text
//					.setText("순위        이름                    수신횟수             비율");
//			// 선택정렬을 이용하여 수신횟수를 내림차순으로 정렬함(큰게 먼저오게)
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
//			// 순위를 저장하는 반복문
//			jj = 1;
//			for (int i = 0; i < list.size() - 1; i++) {
//
//				list.get(i).rank = jj;
//				if (list.get(i + 1).in_count != list.get(i).in_count) {
//					jj++;
//				}
//			}
//
//			// 맨 마지막 데이터의 순위는 따로 코딩해야 된다.
//			if (list.get(list.size() - 1).in_count != list.get(list.size() - 1).in_count) {
//
//				list.get(list.size() - 1).rank = jj + 1;
//			} else {
//				list.get(list.size() - 1).rank = jj;
//			}
//
//			// 0이상만 temp_list에 저장한다.
//			for (int i = 0; i < list.size(); i++) {
//				if (list.get(i).in_count > 0) {
//					temp_list.add(list.get(i));
//
//				}
//			}
//
//			// 커스텀 뷰를 이용하여 리스트뷰에 출력
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
//			text1.setText("총 발신 메시지 개수 : ");
//			value1 = (TextView) findViewById(R.id.value1);
//			value1.setText(String.valueOf(total_out_count) + "개");
//
//			text2 = (TextView) findViewById(R.id.text2);
//			text2.setText("평균 발신 메시지 개수 : ");
//			value2 = (TextView) findViewById(R.id.value2);
//
//			average = (double) total_out_count / (double) list.size();
//			temp_average = String.format("%.2f", average);
//			value2.setText(temp_average + "개");
//
//			call_text = (TextView) findViewById(R.id.text_title1);
//			call_text
//					.setText("순위        이름                    발신횟수             비율");
//			// 선택정렬을 이용하여 수신횟수를 내림차순으로 정렬함(큰게 먼저오게)
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
//			// 순위를 저장하는 반복문
//			jj = 1;
//			for (int i = 0; i < list.size() - 1; i++) {
//
//				list.get(i).rank = jj;
//				if (list.get(i + 1).out_count != list.get(i).out_count) {
//					jj++;
//				}
//			}
//
//			// 맨 마지막 데이터의 순위는 따로 코딩해야 된다.
//			if (list.get(list.size() - 1).out_count != list
//					.get(list.size() - 1).out_count) {
//
//				list.get(list.size() - 1).rank = jj + 1;
//			} else {
//				list.get(list.size() - 1).rank = jj;
//			}
//
//			// 0이상만 temp_list에 저장한다.
//			for (int i = 0; i < list.size(); i++) {
//				if (list.get(i).out_count > 0) {
//					temp_list.add(list.get(i));
//
//				}
//			}
//
//			// 커스텀 뷰를 이용하여 리스트뷰에 출력
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
//// 수신 메시지 어댑터 클래스
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
//	// 각 항목의 뷰 생성
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		if (convertView == null) {
//			convertView = Inflater.inflate(layout, parent, false);
//		}
//
//		// 1,2,3등은 이미지로 랭크를 보여주도록 한다.
//		if (arSrc.get(position).rank == 1) {
//			//1등은 금메달!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.first);			
//			
//			
//			//아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 2) {
//			//2등은 은메달!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.second);
//			//아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 3) {
//			//3등은 동매달!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.third);
//			//아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//			
//		} else {
//			//4등부터는 그냥 텍스트로 출력! ㅋㅋㅋ
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
//		// 퍼센트 소수점 2자리까지 표시하는 방법
//		s_value = String.format("%.2f", arSrc.get(position).in_count_percent);
//		debug = String.valueOf(s_value + "%");
//		percent.setText(debug);
//
//		return convertView;
//
//	}
//}
//
//// 발신 메시지 어댑터 클래스
//class sms_Out_Adapter extends sms_Adapter {
//
//	public sms_Out_Adapter(Context context, int alayout,
//			ArrayList<sms_info> alist) {
//
//		super(context, alayout, alist);
//	}
//
//	// 각 항목의 뷰 생성
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		if (convertView == null) {
//			convertView = Inflater.inflate(layout, parent, false);
//		}
//		
//		// 1,2,3등은 이미지로 랭크를 보여주도록 한다.
//		if (arSrc.get(position).rank == 1) {
//			//1등은 금메달!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.first);			
//			
//			
//			//아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 2) {
//			//2등은 은메달!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.second);
//			//아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 3) {
//			//3등은 동매달!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.third);
//			//아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//			
//		} else {
//			//4등부터는 그냥 텍스트로 출력! ㅋㅋㅋ
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
//		// 퍼센트 소수점 2자리까지 표시하는 방법
//		s_value = String.format("%.2f", arSrc.get(position).out_count_percent);
//		debug = String.valueOf(s_value + "%");
//		percent.setText(debug);
//
//		return convertView;
//
//	}
//}
