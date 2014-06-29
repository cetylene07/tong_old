package com.hb.app.tong;
import com.hb.app.tong.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class extendActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.extend_view);

		final LinearLayout inLayout = (LinearLayout) findViewById(R.id.inLayout);
		// �߰� �� ���� ���� -> R.layout.main �ȿ� ����

		Button btn = (Button) findViewById(R.id.aaa);
		// ��ư�� ������ �� �䰡 �߰���.
		btn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout route_info_tab = (LinearLayout) inflater.inflate(
						R.layout.rank, null);
				// �߰��� �༮(route_info_tab �̶�� �ٸ� xml ���Ͽ� �ִ�.��)
				inLayout.addView(route_info_tab);
				// inLayout�� route_info_tab�� �ִ´�
			}
		});
	}
}