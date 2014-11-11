/*
 * �ð� �׷��� ���
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

		ArrayList<Info> list = new ArrayList<Info>(); // ��������� ��������Ʈ�� ��ü ����

		int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME); // ��ȭ�����
																		// �̸�
		int dateidx = cursor.getColumnIndex(CallLog.Calls.DATE); // ��ȭ ����.
																	// 1/1000��
																	// ������ ����ð�
		int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER); // ��ȭ��ȣ
		int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION); // ��ȭ�ð�
		int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE); // ��ȭ����(����,�߽�,������)

		boolean found = false; // ���� �̸��� ã���� ����Ƚ���� ������Ų��. ã�����ϸ� ����Ʈ�� �߰�

		StringBuilder result = new StringBuilder();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		result.append("�� ��� ���� : " + cursor.getCount() + "��\n");

		int where = 0; // list�� ���� �̸����� ��� �ִ��� Ȯ��

		while (cursor.moveToNext()) { // cursor�� ���� ���� �� ���� �ݺ�
			// ��ȭ �����

			Info temp = new Info();
			Iterator<Info> it = list.iterator(); // iterator�� ��ȯ

			String name = cursor.getString(nameidx); // �̸��� ���ڿ��� ��ȯ

			if (name == null) {
				name = cursor.getString(numidx); // �̸��� ������� ������ ��ȣ�� ����
			}
			// result.append(name);

			temp.name = name;

			found = false;
			where = 0;
			while (it.hasNext()) {
				Info data = it.next();
				if (data.name.equals(name)) { // list�� �ִ� ���� �� �̸��� ���� ���� ã����
					found = true;

					/*
					 * data�� �ּҸ� temp�� �����Ų�� ���� temp���� �ʵ带 �����ϸ� �̰� data���� ���ÿ�
					 * ����Ǵ� �Ŷ� ��������! ���� ���������� ���� �ſ��µ� ��դ� �� ��
					 */

					temp = data;
					break;
				}
				where++;
			}

			// ��ȭ ��¥
			long date = cursor.getLong(dateidx);
			when = new Date(date);
			Date today = new Date();

			// ��ȭ ����
			int type = cursor.getInt(typeidx);

			switch (type) {
			case CallLog.Calls.INCOMING_TYPE:
				// ������ȭ
				total_call_count++;
				incall_count++;
				incall_duration += cursor.getInt(duridx);
				total_duration += cursor.getInt(duridx);
				temp.in_count++;
				temp.in_dur += cursor.getInt(duridx);

				temp.in_year = cursor.getLong(dateidx);
				SimpleDateFormat t_format = new SimpleDateFormat("yyyy"); // �⸸
																			// ����ϵ���
				t_time = t_format.format(new Date(temp.in_year));
				temp.in_year = Long.valueOf(t_time);

				dateinfo.hour_in_dur[when.getHours()] += cursor.getInt(duridx);

				break;
			case CallLog.Calls.OUTGOING_TYPE:
				// �߽���ȭ
				total_call_count++;
				outcall_count++;
				outcall_duration += cursor.getInt(duridx);
				temp.out_count++;
				total_duration += cursor.getInt(duridx);
				temp.out_dur += cursor.getInt(duridx);

				dateinfo.hour_out_dur[when.getHours()] += cursor.getInt(duridx);
				break;
			case CallLog.Calls.MISSED_TYPE:
				// ��������ȭ
				total_call_count++;
				miss_count++;
				temp.miss_count++;
				break;

			}

		


		}

		cursor.close();

		
	}

}
