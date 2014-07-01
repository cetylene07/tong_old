package com.smartstat.listadapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.app.tong.R;
import com.smartstat.info.Info;

public class TotalDurationListAdapter extends BaseAdapter implements
		OnItemClickListener {
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
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.smartstat.info.Info"
	 */
	ArrayList<Info> arSrc;
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
	 * @uml.associationEnd  
	 */
	ImageView image;

	public TotalDurationListAdapter(Context context, int alayout,
			ArrayList<Info> alist) {
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

	// �� �׸��� �� ��
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		TextView name = (TextView) convertView.findViewById(R.id.call_name);
		name.setText(arSrc.get(position).name);

		TextView count = (TextView) convertView.findViewById(R.id.call_value);

		int sum_dur = arSrc.get(position).getSum_dur();


		int hour = sum_dur / 3600;
		int minute = (sum_dur - hour * 3600) / 60;
		int second = ((sum_dur - hour * 3600) - minute * 60);

        String hourText = "", minuteText = "", secondText = "";
        if(hour > 0)     {
            hourText = hour + "시간 ";
        }
        if(minute > 0)  {
            minuteText = minute + "분 ";
        }
        if(second > 0)  {
            secondText = second + "초";
        }

//		debug = String.valueOf(hour + ":" + minute + ":" + second);
        debug = hourText + minuteText + secondText;
		count.setText(debug);

		TextView dur = (TextView) convertView.findViewById(R.id.call_percent);

		// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
		s_value = String
				.format("%.2f", (arSrc.get(position).indur_percent + arSrc
						.get(position).outdur_percent) / 2);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);

		return convertView;

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	public void toggle(int position) {
		// mExpanded[position] = !mExpanded[position];
		notifyDataSetChanged();
	}
}
