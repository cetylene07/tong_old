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

// ������ȭ Ƚ�� ����� Ŭ����
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

	// �� �׸��� �� ����
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3���� �̹����� ��ũ�� �����ֵ��� �Ѵ�.
		if (arSrc.get(position).rank == 1) {
			// 1���� �ݸ޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2���� ���޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3���� ���Ŵ�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4����ʹ� �׳� �ؽ�Ʈ�� ���! ������
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

		// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
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

// ������ȭ���� ����� Ŭ����
class indur_Adapter extends MyListAdapter {

	public indur_Adapter(Context context, int alayout, ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// �� �׸��� �� ����
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3���� �̹����� ��ũ�� �����ֵ��� �Ѵ�.
		if (arSrc.get(position).rank == 1) {
			// 1���� �ݸ޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2���� ���޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3���� ���Ŵ�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4����ʹ� �׳� �ؽ�Ʈ�� ���! ������
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
		// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
		s_value = String.format("%.2f", arSrc.get(position).indur_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);
		return convertView;
	}
}

// ��� ������ȭ���� ����� Ŭ����
class average_indur_Adapter extends MyListAdapter {

	public average_indur_Adapter(Context context, int alayout,
			ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// �� �׸��� �� ����
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3���� �̹����� ��ũ�� �����ֵ��� �Ѵ�.
		if (arSrc.get(position).rank == 1) {
			// 1���� �ݸ޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2���� ���޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3���� ���Ŵ�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4����ʹ� �׳� �ؽ�Ʈ�� ���! ������
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
		// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
		s_value = String.format("%.2f",
				arSrc.get(position).average_in_dur_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);
		return convertView;
	}
}

// �߽���ȭȽ�� ����� Ŭ����
class outcount_Adapter extends MyListAdapter {

	public outcount_Adapter(Context context, int alayout, ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// �� �׸��� �� ����
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3���� �̹����� ��ũ�� �����ֵ��� �Ѵ�.
		if (arSrc.get(position).rank == 1) {
			// 1���� �ݸ޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2���� ���޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3���� ���Ŵ�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4����ʹ� �׳� �ؽ�Ʈ�� ���! ������
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
		// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
		s_value = String.format("%.2f", arSrc.get(position).outcount_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);

		return convertView;

	}
}

// �߽���ȭ���� ����� Ŭ����
class outdur_Adapter extends MyListAdapter {

	public outdur_Adapter(Context context, int alayout, ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// �� �׸��� �� ����
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3���� �̹����� ��ũ�� �����ֵ��� �Ѵ�.
		if (arSrc.get(position).rank == 1) {
			// 1���� �ݸ޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2���� ���޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3���� ���Ŵ�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4����ʹ� �׳� �ؽ�Ʈ�� ���! ������
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
		// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
		s_value = String.format("%.2f", arSrc.get(position).outdur_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);

		return convertView;

	}
}

// ��� �߽���ȭ���� ����� Ŭ����
class average_outdur_Adapter extends MyListAdapter {

	public average_outdur_Adapter(Context context, int alayout,
			ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// �� �׸��� �� ����
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3���� �̹����� ��ũ�� �����ֵ��� �Ѵ�.
		if (arSrc.get(position).rank == 1) {
			// 1���� �ݸ޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2���� ���޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3���� ���Ŵ�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4����ʹ� �׳� �ؽ�Ʈ�� ���! ������
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
		// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
		s_value = String.format("%.2f",
				arSrc.get(position).average_out_dur_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);
		return convertView;
	}
}

// ������ȭȽ�� ����� Ŭ����
class misscount_Adapter extends MyListAdapter {

	public misscount_Adapter(Context context, int alayout, ArrayList<Info> alist) {
		super(context, alayout, alist);
	}

	// �� �׸��� �� ����
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}

		// 1,2,3���� �̹����� ��ũ�� �����ֵ��� �Ѵ�.
		if (arSrc.get(position).rank == 1) {
			// 1���� �ݸ޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.first);

			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 2) {
			// 2���� ���޴�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.second);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");
		} else if (arSrc.get(position).rank == 3) {
			// 3���� ���Ŵ�!
			image = (ImageView) convertView.findViewById(R.id.call_image);
			image.setImageResource(R.drawable.third);
			// �Ʒ��� 2������ ���� ������ textView�� ���� ���ͼ� �ȵ�...�� �̷��� �ؾ� �Ǵ��� ���ذ� �Ȱ�..
			rank = (TextView) convertView.findViewById(R.id.call_rank);
			rank.setText("");

		} else {
			// 4����ʹ� �׳� �ؽ�Ʈ�� ���! ������
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
		// �ۼ�Ʈ �Ҽ��� 2�ڸ����� ǥ���ϴ� ���
		s_value = String.format("%.2f", arSrc.get(position).miss_percent);
		debug = String.valueOf(s_value + "%");
		dur.setText(debug);

		return convertView;

	}
}