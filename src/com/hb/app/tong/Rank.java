package com.hb.app.tong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.hb.app.tong.R;
import com.smartstat.info.Info;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


class Rank_data	{
	/**
	 * @uml.property  name="name"
	 */
	String name;
	/**
	 * @uml.property  name="gold"
	 */
	int gold;
	/**
	 * @uml.property  name="silver"
	 */
	int silver;
	/**
	 * @uml.property  name="bronze"
	 */
	int bronze;
	/**
	 * @uml.property  name="rank"
	 */
	int rank;
	/**
	 * @uml.property  name="score"
	 */
	int score;
	
	Rank_data()	{
		name = "null";
		gold = 0;
		silver = 0;
		bronze = 0;
		rank = 0;
		score = 0;
	}
}

public class Rank extends Activity {
	/**
	 * @uml.property  name="list"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.smartstat.info.Info"
	 */
	ArrayList<Info> list = new ArrayList<Info>(); // ��������� ��������Ʈ�� ��ü ����
	/**
	 * @uml.property  name="temp_list"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.hb.app.tong.Rank"
	 */
	ArrayList<Info> temp_list = new ArrayList<Info>(); // ��������� ��������Ʈ�� ��ü ����
	
	/**
	 * @uml.property  name="rank_list"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.hb.app.tong.Rank_data"
	 */
	ArrayList<Rank_data> rank_list = new ArrayList<Rank_data>();
	/**
	 * @uml.property  name="temp_rank_list"
	 */
	ArrayList<Rank_data> temp_rank_list = new ArrayList<Rank_data>();
	
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
	
	/**
	 * @uml.property  name="gold_found"
	 */
	boolean gold_found = false;
	/**
	 * @uml.property  name="silver_found"
	 */
	boolean silver_found = false;
	/**
	 * @uml.property  name="bronze_found"
	 */
	boolean bronze_found = false;
	/**
	 * @uml.property  name="isend"
	 */
	boolean isend = false;

	/**
	 * @uml.property  name="adspin"
	 * @uml.associationEnd  readOnly="true"
	 */
	ArrayAdapter<CharSequence> adspin;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rank);

		ContentResolver cr = getContentResolver();

		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null,
				CallLog.Calls.DATE + " DESC");

		if (cursor.moveToNext()) // content:sms �� Uri�� ������ ���������� Uri�� �߰��� ������ ����
			uri_found = true;

		if (uri_found == true) {
			int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME); // ��ȭ�����
																			// �̸�
			int dateidx = cursor.getColumnIndex(CallLog.Calls.DATE); // ��ȭ ����.
																		// 1/1000��
																		// ������
																		// ����ð�
			int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER); // ��ȭ��ȣ
			int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION); // ��ȭ�ð�
			int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE); // ��ȭ����(����,�߽�,������)

			boolean found = false; // ���� �̸��� ã���� ����Ƚ���� ������Ų��. ã�����ϸ� ����Ʈ�� �߰�

			StringBuilder result = new StringBuilder();

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
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
				result.append(name);

				temp.name = name;

				found = false;
				where = 0;
				while (it.hasNext()) {
					Info data = it.next();
					if (data.name.equals(name)) { // list�� �ִ� ���� �� �̸��� ���� ���� ã����
						found = true;

						/*
						 * data�� �ּҸ� temp�� �����Ų�� ���� temp���� �ʵ带 �����ϸ� �̰� data����
						 * ���ÿ� ����Ǵ� �Ŷ� ��������! ���� ���������� ���� �ſ��µ� ��դ� �� ��
						 */

						temp = data;
						break;
					}
					where++;
				}

				// ��ȭ ����
				int type = cursor.getInt(typeidx);
				String stype;
				switch (type) {
				case CallLog.Calls.INCOMING_TYPE:
					stype = "����";
					total_incall_count++;
					total_indur += cursor.getInt(duridx);
					temp.in_count++;
					temp.in_dur += cursor.getInt(duridx);
					break;
				case CallLog.Calls.OUTGOING_TYPE:
					total_outcall_count++;
					stype = "�߽�";
					temp.out_count++;
					total_outdur += cursor.getInt(duridx);
					temp.out_dur += cursor.getInt(duridx);
					break;
				case CallLog.Calls.MISSED_TYPE:
					stype = "������";
					total_miss++;
					temp.miss_count++;
					break;

				}

				// ��ȭ ��¥
				long date = cursor.getLong(dateidx);
				String sdate = formatter.format(new Date(date));
				result.append(sdate + ",");

				// ��ȭ �ð�
				int duration = cursor.getInt(duridx);
				result.append(duration + "��\n");

				if (found == false) {
					list.add(temp); // ���� �߰�
				}

			}	// 	while (cursor.moveToNext()) 
			
			for (int i = 0; i < list.size(); i++) {
				// ��ü�� ���� ���
				list.get(i).incount_percent = list.get(i).in_count
						/ total_incall_count * 100; // ���� Ƚ�� ����
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
			}

			cursor.close();

		} else {
//			TextView tv1 = (TextView) findViewById(R.id.subject1);
//			tv1.setText("������ �б� ����");
		}

		// ���������� �̿��Ͽ� ����Ƚ���� ������������ ������(ū�� ��������)
		for (int i = 0; i < list.size(); i++) {
			int max = i;
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(j).in_count > list.get(max).in_count) {
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
			if (list.get(i + 1).in_count != list.get(i).in_count) {
				jj++;
			}
		}

		// �� ������ �������� ������ ���� �ڵ��ؾ� �ȴ�.
		if (list.get(list.size() - 1).in_count != list.get(list.size() - 1).in_count) {

			list.get(list.size() - 1).rank = jj + 1;
		} else {
			list.get(list.size() - 1).rank = jj;
		}

		// 0�̻� temp_list�� �����Ѵ�.
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).in_count > 0) {
				temp_list.add(list.get(i));

			}
		}		
		this.save();

		
		
		
		
		// ���ű��� ��ŷ
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

		// �� ������ �������� ������ ���� �ڵ��ؾ� �ȴ�.
		if (list.get(list.size() - 1).in_dur != list.get(list.size() - 1).in_dur) {

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
		isend = false;
		this.save();
		
		
		
		// ��ռ��ű��� ��ŷ
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

		// �� ������ �������� ������ ���� �ڵ��ؾ� �ȴ�.
		if (list.get(list.size() - 1).average_in_dur != list.get(list.size() - 1).average_in_dur) {

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
		isend = false;
		this.save();	
		
		
		
		// �߽�Ƚ�� ��ŷ
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

		// �� ������ �������� ������ ���� �ڵ��ؾ� �ȴ�.
		if (list.get(list.size() - 1).out_count != list.get(list.size() - 1).out_count) {

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
		isend = false;
		this.save();	
		

		// �߽ű��� ��ŷ
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

		// �� ������ �������� ������ ���� �ڵ��ؾ� �ȴ�.
		if (list.get(list.size() - 1).out_dur != list.get(list.size() - 1).out_dur) {

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
		isend = false;
		this.save();	
		
		
		// ��չ߽ű��� ��ŷ
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

		// �� ������ �������� ������ ���� �ڵ��ؾ� �ȴ�.
		if (list.get(list.size() - 1).average_out_dur != list.get(list.size() - 1).average_out_dur) {

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
		isend = false;
		this.save();	
		
		
		
		// ����Ƚ�� ��ŷ
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

		// �� ������ �������� ������ ���� �ڵ��ؾ� �ȴ�.
		if (list.get(list.size() - 1).miss_count != list.get(list.size() - 1).miss_count) {

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
		isend = false;
		this.save();	
		
		
		
		
		
		/*
		 * rank_data�� �������ķ� ������� ���Ľ�Ų��.
		 */
		// ���������� �̿��Ͽ�
		for (int i = 0; i < rank_list.size(); i++) {
			int max = i;
			for (int j = i + 1; j < rank_list.size(); j++) {
				if (rank_list.get(j).score > rank_list.get(max).score) {
					max = j;
				}
			}
			Rank_data rank_trans = new Rank_data();
			rank_trans = rank_list.get(i);
			rank_list.set(i, rank_list.get(max));
			rank_list.set(max, rank_trans);
		}

		// ������ �����ϴ� �ݺ���
		jj = 1;
		for (int i = 0; i < rank_list.size() - 1; i++) {

			rank_list.get(i).rank = jj;
			if (rank_list.get(i + 1).score != rank_list.get(i).score) {
				jj++;
			}
		}

		// �� ������ �������� ������ ���� �ڵ��ؾ� �ȴ�.
		if (rank_list.get(rank_list.size() - 1).score != rank_list.get(rank_list.size() - 1).score) {

			rank_list.get(rank_list.size() - 1).rank = jj + 1;
		} else {
			rank_list.get(rank_list.size() - 1).rank = jj;
		}
		
		
		
		
		// Ŀ���� �並 �̿��Ͽ� ����Ʈ�信 ���
		MyRankAdapter MyAdapter = new MyRankAdapter(this, R.layout.inrank_view,
				rank_list);
		ListView MyList;

		MyList = (ListView) findViewById(R.id.rank_list);
		MyList.setAdapter(MyAdapter);

	}
	
	void save()	{
		
		for(int i=0; i< list.size(); i++)	{
			if(isend)
				break;
			
			switch(list.get(i).rank)	{
			case 1:
				for(int j=0; j< rank_list.size(); j++)	{
					if(rank_list.get(j).name.equals(list.get(i).name))	{
						/*
						 * ���࿡ list.get(i)�� �ִ� �����Ͱ� rank_list���� �ִٸ�
						 * i ��ġ�� gold�� 1 ������Ų��
						 */
						rank_list.get(j).gold++;
						rank_list.get(j).score += 3;
						gold_found = true;
						break;						
					}
				}
				if(!gold_found)	{
					// ���� rank_list�� ���� ���� �����Ͱ� ���ٸ� ���� ����� �־��ش�.
					rank_list.add(new Rank_data());
					rank_list.get(rank_list.size()-1).gold++;
					rank_list.get(rank_list.size()-1).score += 3;
					rank_list.get(rank_list.size()-1).name = list.get(i).name;
				}
				gold_found = false;
				break;
				
			case 2:
				for(int j=0; j< rank_list.size(); j++)	{
					if(rank_list.get(j).name.equals(list.get(i).name))	{
						/*
						 * ���࿡ list.get(i)�� �ִ� �����Ͱ� rank_list���� �ִٸ�
						 * i ��ġ�� silver�� 1 ������Ų��
						 */
						rank_list.get(j).silver++;
						rank_list.get(j).score += 2;
						silver_found = true;
						break;						
					}
				}
				if(!silver_found)	{
					// ���� rank_list�� ���� ���� �����Ͱ� ���ٸ� ���� ����� �־��ش�.
					rank_list.add(new Rank_data());
					rank_list.get(rank_list.size()-1).silver++;
					rank_list.get(rank_list.size()-1).score += 2;
					rank_list.get(rank_list.size()-1).name = list.get(i).name;
				}
				silver_found = false;
				break;
				
			case 3:
				for(int j=0; j< rank_list.size(); j++)	{
					if(rank_list.get(j).name.equals(list.get(i).name))	{
						/*
						 * ���࿡ list.get(i)�� �ִ� �����Ͱ� rank_list���� �ִٸ�
						 * i ��ġ�� bronze�� 1 ������Ų��
						 */
						rank_list.get(j).bronze++;
						rank_list.get(j).score += 1;
						bronze_found = true;
						break;						
					}
				}
				if(!bronze_found)	{
					// ���� rank_list�� ���� ���� �����Ͱ� ���ٸ� ���� ����� �־��ش�.
					rank_list.add(new Rank_data());
					rank_list.get(rank_list.size()-1).bronze++;
					rank_list.get(rank_list.size()-1).score += 1;
					rank_list.get(rank_list.size()-1).name = list.get(i).name;
				}
				bronze_found = false;
				break;
				
			default:
				isend = true;
				break;	
			}
		}	
	}

}

class MyRankAdapter extends BaseAdapter {
	/**
	 * @uml.property  name="maincon"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	Context maincon;
	/**
	 * @uml.property  name="inflater"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	LayoutInflater Inflater;
	/**
	 * @uml.property  name="arSrc"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.hb.app.tong.Rank_data"
	 */
	ArrayList<Rank_data> arSrc;
	/**
	 * @uml.property  name="layout"
	 */
	int layout;
	/**
	 * @uml.property  name="s_value"
	 */
	String s_value;
	/**
	 * @uml.property  name="rank"
	 * @uml.associationEnd  
	 */
	TextView rank;
	/**
	 * @uml.property  name="debug"
	 */
	String debug;
	/**
	 * @uml.property  name="image"
	 * @uml.associationEnd  readOnly="true"
	 */
	ImageView image;

	public MyRankAdapter(Context context, int alayout, ArrayList<Rank_data> alist) {
		maincon = context;
		Inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arSrc = alist;
		layout = alayout;
	}

	public int getCount() {
		return arSrc.size();
	}

	public String getItem(int position) {
		return arSrc.get(position).name;
	}

	public long getItemId(int position) {
		return position;
	}

	// �� �׸��� �� ����
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		rank = (TextView) convertView.findViewById(R.id.rank_rank);
		debug = String.valueOf(arSrc.get(position).rank);
		rank.setText("[" + debug + "]");

		TextView name = (TextView) convertView.findViewById(R.id.rank_name);
		name.setText(arSrc.get(position).name);
		
		TextView gold = (TextView) convertView.findViewById(R.id.rank_gold);
		debug = String.valueOf(arSrc.get(position).gold);
		gold.setText("[" + debug + "]");
		
		TextView silver = (TextView) convertView.findViewById(R.id.rank_silver);
		debug = String.valueOf(arSrc.get(position).silver);
		silver.setText("[" + debug + "]");
		
		TextView bronze = (TextView) convertView.findViewById(R.id.rank_bronze);
		debug = String.valueOf(arSrc.get(position).bronze);
		bronze.setText("[" + debug + "]");

		return convertView;

	}
}
