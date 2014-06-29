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

public class GraphFragment extends Fragment {
	/*
	 * callActivity에 있는 데이터를 재 사용하기 위해서 상속받았다. callActivity에는 Activity를 상속받기 때문에
	 * 원래 TimeActivity에서 쓰려던 Activity도 포함되어 있어서 상속가능
	 */
	/**
	 * @uml.property  name="total_duration"
	 */
	double total_duration = 0;
	/**
	 * @uml.property  name="total_call_count"
	 */
	double total_call_count = 0;
	/**
	 * @uml.property  name="incall_count"
	 */
	double incall_count = 0;
	/**
	 * @uml.property  name="incall_duration"
	 */
	double incall_duration = 0;
	/**
	 * @uml.property  name="outcall_count"
	 */
	double outcall_count = 0;
	/**
	 * @uml.property  name="outcall_duration"
	 */
	double outcall_duration = 0;
	/**
	 * @uml.property  name="miss_count"
	 */
	double miss_count = 0;
	/**
	 * @uml.property  name="average_duration"
	 */
	double average_duration = 0;
	/**
	 * @uml.property  name="average_call_count"
	 */
	double average_call_count = 0;
	/**
	 * @uml.property  name="t_value"
	 */
	double t_value;
	/**
	 * @uml.property  name="t1_value"
	 */
	int t1_value;
	/**
	 * @uml.property  name="t_time"
	 */
	String t_time;

	/**
	 * @uml.property  name="gv"
	 * @uml.associationEnd  
	 */
	GraphicalView gv;
	/**
	 * @uml.property  name="hour_gv"
	 * @uml.associationEnd  
	 */
	GraphicalView hour_gv;
	/**
	 * @uml.property  name="when"
	 */
	Date when;

	/**
	 * @uml.property  name="dateinfo"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	DateInfo dateinfo = new DateInfo();

	/**
	 * @uml.property  name="chart_spin"
	 * @uml.associationEnd  
	 */
	ArrayAdapter<CharSequence> chart_spin;

	/**
	 * @uml.property  name="chartView"
	 * @uml.associationEnd  readOnly="true"
	 */
	LinearLayout chartView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_graph, container, false);
	}

	/** Called when the activity is first created. */
	@Override
	public void onStart() {
		super.onStart();
		// setContentView(R.layout.activity_graph);
		// setListAdapter(new SpeechListAdapter(getActivity()));

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

			// if (when.getMonth() == today.getMonth()) {
			if (true) {
				switch (when.getDay()) {
				case 0: // 일요일
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.sun_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.sun_out_dur += cursor.getInt(duridx);
					}
					break;
				case 1: // 월요일
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.mon_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.mon_out_dur += cursor.getInt(duridx);
					}
					break;
				case 2: // 화요일
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.tus_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.tus_out_dur += cursor.getInt(duridx);
					}
					break;
				case 3: // 수요일
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.wed_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.wed_out_dur += cursor.getInt(duridx);
					}
					break;
				case 4: // 목요일
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.thr_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.thr_out_dur += cursor.getInt(duridx);
					}
					break;
				case 5: // 금요일
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.fri_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.fri_out_dur += cursor.getInt(duridx);
					}
					break;
				case 6: // 토요일
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.sat_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.sat_out_dur += cursor.getInt(duridx);
					}
					break;
				}
			}

			if (found == false) {
				list.add(temp); // 새로 추가
			}

		}

		cursor.close();

		// 표시할 수치값
		List<int[]> values = new ArrayList<int[]>();

		// values.add(new int[] { 1, 25, 3, 46, 5, 76, 7 });
		// values.add(new int[] {11,22,63,4,55,66,77});
		values.add(new int[] { dateinfo.sun_in_dur, dateinfo.mon_in_dur,
				dateinfo.tus_in_dur, dateinfo.wed_in_dur, dateinfo.thr_in_dur,
				dateinfo.fri_in_dur, dateinfo.sat_in_dur });

		values.add(new int[] { dateinfo.sun_out_dur, dateinfo.mon_out_dur,
				dateinfo.tus_out_dur, dateinfo.wed_out_dur,
				dateinfo.thr_out_dur, dateinfo.fri_out_dur,
				dateinfo.sat_out_dur });

		// 시간별 데이터 추가
		List<int[]> hour_values = new ArrayList<int[]>();
		hour_values.add(dateinfo.hour_in_dur);
		hour_values.add(dateinfo.hour_out_dur);

		// 그래프 출력을 위한 그래프 속성 지정 객체
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		XYMultipleSeriesRenderer hour_renderer = new XYMultipleSeriesRenderer();

		// 상단 표시 제목과 크기
		// renderer.setChartTitle("요일별 통화길이");
		renderer.setChartTitleTextSize(40);

		// 분류에 대한 이름
		String[] titles = new String[] { getString(R.string.receivingTime),
				getString(R.string.outGoingTime) };
		String[] test_titles = new String[] {
				getString(R.string.receivingTime),
				getString(R.string.outGoingTime) };

		// 항목을 표시하는 데 사용될 색상값
		int[] colors = new int[] { Color.argb(100, 55, 128, 71),
				Color.argb(100, 40, 55, 142) };

		// int[] test_colors = new int[] { Color.argb(100, 94, 154, 210) };

		// 분류명 글자 크기 및 각 색상 지정
		// renderer.setLabelsTextSize(35);
		// hour_renderer.setLabelsTextSize(75);
		// renderer.setChartTitleTextSize(30);
		renderer.setLegendTextSize(30);
		hour_renderer.setLegendTextSize(30);
		// renderer.setAxisTitleTextSize(30);
		// renderer.setChartValuesTextSize(60);

		int length = colors.length;
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
			hour_renderer.addSeriesRenderer(r);
		}

		// X,Y 축 항목이름과 글자 크기
		// renderer.setXTitle("요일");
		// renderer.setYTitle("길이(초)");
		renderer.setAxisTitleTextSize(25);
		hour_renderer.setAxisTitleTextSize(18);

		// 수치값 글자 크기 , X축 최소 최대값 , Y축 최소 최대값 설정
		renderer.setLabelsTextSize(14);
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(7.5);
		renderer.setYAxisMin(0.5);

		hour_renderer.setLabelsTextSize(14);
		hour_renderer.setXAxisMin(0.5);
		hour_renderer.setXAxisMax(24.5);
		hour_renderer.setYAxisMin(0.5);

		// 안티알레이싱 설정
		renderer.setAntialiasing(true);

		renderer.setXLabels(0);

		renderer.addXTextLabel(1, getString(R.string.sun));
		renderer.addXTextLabel(2, getString(R.string.mon));
		renderer.addXTextLabel(3, getString(R.string.tus));
		renderer.addXTextLabel(4, getString(R.string.wed));
		renderer.addXTextLabel(5, getString(R.string.thr));
		renderer.addXTextLabel(6, getString(R.string.fri));
		renderer.addXTextLabel(7, getString(R.string.sat));

		hour_renderer.setXLabels(0);
		for (int i = 0; i < 24; i++)
			hour_renderer.addXTextLabel(i, i + "");

		// X축과 Y축의 색상 지정
		renderer.setAxesColor(Color.BLACK);

		// 상단제목, X,Y축 제목, 수치값의 글자 색상
		renderer.setLabelsColor(Color.BLACK);

		// X축 표시 간격
		// renderer.setXLabels(7);
		//
		// Y축 표시 간격
		// renderer.setYLabels(5);

		// X,Y축 정렬방향
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		hour_renderer.setXLabelsAlign(Align.LEFT);
		hour_renderer.setYLabelsAlign(Align.LEFT);

		// X,Y축 스크롤 여부 ON/OFF
		renderer.setPanEnabled(false, false);
		hour_renderer.setPanEnabled(false, false);

		// ZOOM기능 ON/OFF
		renderer.setZoomEnabled(false, false);
		hour_renderer.setZoomEnabled(false, false);

		// ZOOM 비율
		renderer.setZoomRate(1.0f);
		hour_renderer.setZoomRate(1.0f);

		// 막대간 간격
		renderer.setBarSpacing(0.5f);
		hour_renderer.setBarSpacing(0.5f);

		// 배경 색상
		 renderer.setMarginsColor(Color.parseColor("#ffffff"));
		 hour_renderer.setMarginsColor(Color.parseColor("#ffffff"));

		// 설정 정보 설정
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		for (int i = 0; i < titles.length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			int[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}

		XYMultipleSeriesDataset test_dataset = new XYMultipleSeriesDataset();
		for (int i = 0; i < test_titles.length; i++) {
			CategorySeries test_series = new CategorySeries(test_titles[i]);
			int[] test_v = hour_values.get(i);
			int test_seriesLength = test_v.length;
			for (int k = 0; k < test_seriesLength; k++) {
				test_series.add(test_v[k]);
			}
			test_dataset.addSeries(test_series.toXYSeries());
		}

		gv = ChartFactory.getBarChartView(getActivity(), dataset, renderer,
				Type.DEFAULT);

		hour_gv = ChartFactory.getBarChartView(getActivity(), test_dataset,
				hour_renderer, Type.DEFAULT);
		//

		// 스피너
		Spinner spin = (Spinner) getView().findViewById(R.id.chart_spinner);
		spin.setPrompt("Choice Option");
		chart_spin = ArrayAdapter.createFromResource(getActivity(),
				R.array.chart_choice, android.R.layout.simple_spinner_item);
		chart_spin
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(chart_spin);
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				chartView = (LinearLayout) getView().findViewById(
						R.id.chartView);
				// hourChart = (LinearLayout) getView().findViewById
				// (R.id.hour_chart);

				// dayChart.addView(gv);
				// dayChart.addView(hour_gv);

				switch (position) {
				case 0:
					// // 그래프를 Layout에 추가
					chartView.removeAllViews();
					// hourChart.setVisibility(View.INVISIBLE);
					// dayChart.setVisibility(View.VISIBLE);
					chartView.addView(gv);
					// chartView.setBackgroundColor(Color.parseColor("#f6f7ef"));

					break;

				case 1:
					// // 그래프를 Layout에 추가
					chartView.removeAllViews();
					// dayChart.setVisibility(View.INVISIBLE);
					// hourChart.setVisibility(View.VISIBLE);
					chartView.addView(hour_gv);
					// hourChart.addView(hour_gv);
					// chartView.setBackgroundColor(Color.parseColor("#f6f7ef"));
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

}
