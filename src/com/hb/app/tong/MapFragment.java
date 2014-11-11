/*
 * 시간 그래프 출력
 */

package com.hb.app.tong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.smartstat.info.DateInfo;
import com.smartstat.info.Info;

public class MapFragment extends Fragment {
	
	double total_duration = 0;
	double total_call_count = 0;
	double incall_count = 0;
	double incall_duration = 0;
	double outcall_count = 0;
	double outcall_duration = 0;
	double miss_count = 0;
	double average_duration = 0;
	double average_call_count = 0;
	double t_value;
	int t1_value;
	String t_time;
	GraphicalView gv;
	GraphicalView hour_gv;
	Date when;
	DateInfo dateinfo = new DateInfo();
	ArrayAdapter<CharSequence> chart_spin;
	LinearLayout chartView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_map, container, false);
	}

	/** Called when the activity is first created. */
	@Override
	public void onStart() {
		super.onStart();

		ContentResolver cr = getActivity().getContentResolver();
		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null,
				CallLog.Calls.DATE + " DESC");

		ArrayList<Info> list = new ArrayList<Info>(); // 통계정보를 동적리스트로 객체 생성

		int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME); // 통화대상자
																		// 이름
		int dateidx = cursor.getColumnIndex(CallLog.Calls.DATE); // 통화 시점.
																	// 1/1000초
																	// 단위의 절대시간
		int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER); // 전화번호
		int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION); // 통화시간
		int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE); // 통화종류(수신,발신,부재중)

		boolean found = false; // 같은 이름을 찾으면 누적횟수를 증가시킨다. 찾지못하면 리스트에 추가

		StringBuilder result = new StringBuilder();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		result.append("총 기록 개수 : " + cursor.getCount() + "개\n");

		int where = 0; // list에 현재 이름값이 어디에 있는지 확인

		while (cursor.moveToNext()) { // cursor가 읽지 못할 때 까지 반복
			// 통화 대상자

			Info temp = new Info();
			Iterator<Info> it = list.iterator(); // iterator로 변환

			String name = cursor.getString(nameidx); // 이름을 문자열로 변환

			if (name == null) {
				name = cursor.getString(numidx); // 이름이 저장되지 않으면 번호로 저장
			}
			// result.append(name);

			temp.name = name;

			found = false;
			where = 0;
			while (it.hasNext()) {
				Info data = it.next();
				if (data.name.equals(name)) { // list에 있는 원소 중 이름이 같은 것을 찾으면
					found = true;

					/*
					 * data의 주소를 temp에 저장시킨다 따라서 temp에서 필드를 연산하면 이게 data에서 동시에
					 * 연산되는 거랑 마찬가지! ㅋㅋ 생각하지도 못한 거였는데 우왕ㅋ 굳 ㅋ
					 */

					temp = data;
					break;
				}
				where++;
			}

			// 통화 날짜
			long date = cursor.getLong(dateidx);
			when = new Date(date);
			Date today = new Date();

			// 통화 종류
			int type = cursor.getInt(typeidx);

			switch (type) {
			case CallLog.Calls.INCOMING_TYPE:
				// 수신전화
				total_call_count++;
				incall_count++;
				incall_duration += cursor.getInt(duridx);
				total_duration += cursor.getInt(duridx);
				temp.in_count++;
				temp.in_dur += cursor.getInt(duridx);

				temp.in_year = cursor.getLong(dateidx);
				SimpleDateFormat t_format = new SimpleDateFormat("yyyy"); // 년만
																			// 출력하도록
				t_time = t_format.format(new Date(temp.in_year));
				temp.in_year = Long.valueOf(t_time);

				dateinfo.hour_in_dur[when.getHours()] += cursor.getInt(duridx);

				break;
			case CallLog.Calls.OUTGOING_TYPE:
				// 발신전화
				total_call_count++;
				outcall_count++;
				outcall_duration += cursor.getInt(duridx);
				temp.out_count++;
				total_duration += cursor.getInt(duridx);
				temp.out_dur += cursor.getInt(duridx);

				dateinfo.hour_out_dur[when.getHours()] += cursor.getInt(duridx);
				break;
			case CallLog.Calls.MISSED_TYPE:
				// 부재중전화
				total_call_count++;
				miss_count++;
				temp.miss_count++;
				break;

			}

		


		}

		cursor.close();

		
	}

}
