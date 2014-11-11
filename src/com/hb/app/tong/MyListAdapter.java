package com.hb.app.tong;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstat.info.Info;

public class MyListAdapter extends BaseAdapter implements OnItemClickListener {
	Context context;
	LayoutInflater Inflater;
	ArrayList<Info> arSrc;
	int layout;
	String s_value;
	TextView rank;
	String debug;
	ImageView image;

    String attribute;

	public MyListAdapter(Context _context, int alayout, ArrayList<Info> alist, String _attribute) {
		context = _context;
		Inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arSrc = alist;
		layout = alayout;
        attribute = _attribute;
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

    public String secToHourMinuteSecond(int time)   {
        
        int hour = time / 3600;
        int minute = (time - hour * 3600) / 60;
        int second = ((time - hour * 3600) - minute * 60);

        String hourText = "", minuteText = "", secondText = "";
        if(hour > 0)     {
            hourText = hour + "시간 ";
        }
        if(minute > 0)  {
            minuteText = minute + "분";
        }
        if(second > 0)  {
            secondText = second + "초";
        }
        return hourText + minuteText + secondText;
    }
    public String secToHourMinuteSecond(double doubleTime)   {
        int time = (int)doubleTime;
        return secToHourMinuteSecond(time);
    }

	public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = Inflater.inflate(layout, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.call_name);
        name.setText(arSrc.get(position).name);
		TextView count = (TextView) convertView.findViewById(R.id.call_value);
        debug = "";

        if(attribute == "sumdur")    {
            debug = secToHourMinuteSecond(arSrc.get(position).sum_dur);
        }
        else if(attribute == "incount")    {
            debug = String.valueOf(arSrc.get(position).in_count) + " " + context.getString(R.string.times);
        }
        else if(attribute == "indur")    {
            debug = secToHourMinuteSecond(arSrc.get(position).in_dur);
        }
        else if(attribute == "average_indur")   {
            debug = secToHourMinuteSecond(arSrc.get(position).average_in_dur);
        }
        else if(attribute == "outdur")  {
            debug = secToHourMinuteSecond(arSrc.get(position).out_dur);
        }
        else if(attribute == "average_outdur")  {
            debug = secToHourMinuteSecond(arSrc.get(position).average_out_dur);
        }
        else if(attribute == "misscount")  {
            debug = String.valueOf((int) arSrc.get(position).miss_count) + " " + context.getString(R.string.times);
        }
        else if(attribute == "outcount")  {
            debug = String.valueOf((int) arSrc.get(position).out_count) + " " + context.getString(R.string.times);
        }
		count.setText(debug);
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


