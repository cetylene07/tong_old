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
		// 추가 될 곳을 지정 -> R.layout.main 안에 지정

		Button btn = (Button) findViewById(R.id.aaa);
		// 버튼을 누르면 새 뷰가 추가됨.
		btn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout route_info_tab = (LinearLayout) inflater.inflate(
						R.layout.rank, null);
				// 추가할 녀석(route_info_tab 이라는 다른 xml 파일에 있다.ㅎ)
				inLayout.addView(route_info_tab);
				// inLayout에 route_info_tab을 넣는다
			}
		});
	}
}