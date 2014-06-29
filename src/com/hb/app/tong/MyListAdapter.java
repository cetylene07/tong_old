package com.hb.app.tong;

import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstat.info.Info;

// 수신전화 횟수 어댑터 클래스
public class MyListAdapter extends BaseAdapter implements OnItemClickListener {
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

	public MyListAdapter(Context context, int alayout, ArrayList<Info> alist) {
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

	// 각 항목의 뷰 생성
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3등은 이미지로 랭크를 보여주도록 한다.
		if (arSrc.get(position).rank == 1) {
			// 1등은 금메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2등은 은메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3등은 동매달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4등부터는 그냥 텍스트로 출력! ㅋㅋㅋ
			image = (ImageView) convertView.findViewById(R.id.call_image);
//			image.setVisibility(View.INVISIBLE);
			image.setImageResource(Color.parseColor("#f6f7ef"));
			rank = (TextView) convertView.findViewById(R.id.call_rank);
//			debug = String.valueOf(arSrc.get(position).rank);
			rank.setText("[" + arSrc.get(position).rank + "]");
		}

		TextView name = (TextView) convertView.findViewById(R.id.call_name);
		name.setText(arSrc.get(position).name);

		
		TextView count = (TextView) convertView.findViewById(R.id.call_value);
		debug = String.valueOf(arSrc.get(position).in_count + " times");
		count.setText(debug);

		TextView dur = (TextView) convertView.findViewById(R.id.call_percent);

		// 퍼센트 소수점 2자리까지 표시하는 방법
		s_value = String.format("%.2f", arSrc.get(position).incount_percent);
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

// 수신전화길이 어댑터 클래스
class indur_Adapter extends MyListAdapter {

	public indur_Adapter(Context context, int alayout, ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// 각 항목의 뷰 생성
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3등은 이미지로 랭크를 보여주도록 한다.
		if (arSrc.get(position).rank == 1) {
			// 1등은 금메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2등은 은메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3등은 동매달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4등부터는 그냥 텍스트로 출력! ㅋㅋㅋ
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(Color.parseColor("#f6f7ef"));
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			debug = String.valueOf(arSrc.get(position).rank);
			rank.setText("[" + debug + "]");
		}

		TextView name = (TextView) convertView.findViewById(R.id.call_name);
		name.setText(arSrc.get(position).name);

		TextView count = (TextView) convertView.findViewById(R.id.call_value);
		debug = String.valueOf(arSrc.get(position).in_dur + " sec");
		count.setText(debug);

		TextView dur = (TextView) convertView.findViewById(R.id.call_percent);
		// 퍼센트 소수점 2자리까지 표시하는 방법
		s_value = String.format("%.2f", arSrc.get(position).indur_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);
		return convertView;
	}
}

// 평균 수신전화길이 어댑터 클래스
class average_indur_Adapter extends MyListAdapter {

	public average_indur_Adapter(Context context, int alayout,
			ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// 각 항목의 뷰 생성
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3등은 이미지로 랭크를 보여주도록 한다.
		if (arSrc.get(position).rank == 1) {
			// 1등은 금메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2등은 은메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3등은 동매달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4등부터는 그냥 텍스트로 출력! ㅋㅋㅋ
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(Color.parseColor("#f6f7ef"));
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			debug = String.valueOf(arSrc.get(position).rank);
			rank.setText("[" + debug + "]");
		}

		TextView name = (TextView) convertView.findViewById(R.id.call_name);
		name.setText(arSrc.get(position).name);

		TextView count = (TextView) convertView.findViewById(R.id.call_value);
		debug = String.valueOf((int) arSrc.get(position).average_in_dur + " sec");
		count.setText(debug);

		TextView dur = (TextView) convertView.findViewById(R.id.call_percent);
		// 퍼센트 소수점 2자리까지 표시하는 방법
		s_value = String.format("%.2f",
				arSrc.get(position).average_in_dur_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);
		return convertView;
	}
}

// 발신전화횟수 어댑터 클래스
class outcount_Adapter extends MyListAdapter {

	public outcount_Adapter(Context context, int alayout, ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// 각 항목의 뷰 생성
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3등은 이미지로 랭크를 보여주도록 한다.
		if (arSrc.get(position).rank == 1) {
			// 1등은 금메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2등은 은메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3등은 동매달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4등부터는 그냥 텍스트로 출력! ㅋㅋㅋ
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(Color.parseColor("#f6f7ef"));
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			debug = String.valueOf(arSrc.get(position).rank);
			rank.setText("[" + debug + "]");
		}
		TextView name = (TextView) convertView.findViewById(R.id.call_name);
		name.setText(arSrc.get(position).name);

		TextView count = (TextView) convertView.findViewById(R.id.call_value);
		debug = String.valueOf(arSrc.get(position).out_count + " times");
		count.setText(debug);

		TextView dur = (TextView) convertView.findViewById(R.id.call_percent);
		// 퍼센트 소수점 2자리까지 표시하는 방법
		s_value = String.format("%.2f", arSrc.get(position).outcount_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);

		return convertView;

	}
}

// 발신전화길이 어댑터 클래스
class outdur_Adapter extends MyListAdapter {

	public outdur_Adapter(Context context, int alayout, ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// 각 항목의 뷰 생성
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3등은 이미지로 랭크를 보여주도록 한다.
		if (arSrc.get(position).rank == 1) {
			// 1등은 금메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2등은 은메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3등은 동매달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4등부터는 그냥 텍스트로 출력! ㅋㅋㅋ
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(Color.parseColor("#f6f7ef"));
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			debug = String.valueOf(arSrc.get(position).rank);
			rank.setText("[" + debug + "]");
		}
		TextView name = (TextView) convertView.findViewById(R.id.call_name);
		name.setText(arSrc.get(position).name);

		TextView count = (TextView) convertView.findViewById(R.id.call_value);
		debug = String.valueOf(arSrc.get(position).out_dur + " sec");
		count.setText(debug);

		TextView dur = (TextView) convertView.findViewById(R.id.call_percent);
		// 퍼센트 소수점 2자리까지 표시하는 방법
		s_value = String.format("%.2f", arSrc.get(position).outdur_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);

		return convertView;

	}
}

// 평균 발신전화길이 어댑터 클래스
class average_outdur_Adapter extends MyListAdapter {

	public average_outdur_Adapter(Context context, int alayout,
			ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// 각 항목의 뷰 생성
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3등은 이미지로 랭크를 보여주도록 한다.
		if (arSrc.get(position).rank == 1) {
			// 1등은 금메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2등은 은메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3등은 동매달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4등부터는 그냥 텍스트로 출력! ㅋㅋㅋ
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(Color.parseColor("#f6f7ef"));
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			debug = String.valueOf(arSrc.get(position).rank);
			rank.setText("[" + debug + "]");
		}
		TextView name = (TextView) convertView.findViewById(R.id.call_name);
		name.setText(arSrc.get(position).name);
		TextView count = (TextView) convertView.findViewById(R.id.call_value);
		debug = String.valueOf((int) arSrc.get(position).average_out_dur + " sec");
		count.setText(debug);

		TextView dur = (TextView) convertView.findViewById(R.id.call_percent);
		// 퍼센트 소수점 2자리까지 표시하는 방법
		s_value = String.format("%.2f",
				arSrc.get(position).average_out_dur_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);
		return convertView;
	}
}

// 부재전화횟수 어댑터 클래스
class misscount_Adapter extends MyListAdapter {

	public misscount_Adapter(Context context, int alayout, ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// 각 항목의 뷰 생성
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3등은 이미지로 랭크를 보여주도록 한다.
		if (arSrc.get(position).rank == 1) {
			// 1등은 금메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2등은 은메달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3등은 동매달!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// 아래의 2문장을 쓰지 않으면 textView에 값이 나와서 안되...왜 이렇게 해야 되는지 이해가 안가..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4등부터는 그냥 텍스트로 출력! ㅋㅋㅋ
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(Color.parseColor("#f6f7ef"));
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			debug = String.valueOf(arSrc.get(position).rank);
			rank.setText("[" + debug + "]");
		}

		TextView name = (TextView) convertView.findViewById(R.id.call_name);
		name.setText(arSrc.get(position).name);
		TextView count = (TextView) convertView.findViewById(R.id.call_value);
		debug = String.valueOf(arSrc.get(position).miss_count + " times");
		count.setText(debug);

		TextView dur = (TextView) convertView.findViewById(R.id.call_percent);
		// 퍼센트 소수점 2자리까지 표시하는 방법
		s_value = String.format("%.2f", arSrc.get(position).miss_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);

		return convertView;

	}
}