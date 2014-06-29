/*
 * 메뉴 탭을 눌렀을 때, 프로그램 소개 화면
 */

package com.hb.app.tong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hb.app.tong.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SettingActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

//		aboutView.setBackgroundColor(Color.parseColor("#f6f7ef"));

		ListView aboutList = (ListView) findViewById(android.R.id.list);
//		aboutList.setBackgroundColor(Color.parseColor("#f6f7ef"));
		aboutList.setFastScrollEnabled(false);// 리스트가 움직이지 않게 하려고 하는건데.....

		List<Map<String, String>> dataList = this.createData();

		aboutList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder dlg = new AlertDialog.Builder(SettingActivity.this);

				switch (position) {
				case 0: // 버전에 관한 역사
					dlg.setMessage(R.string.history	);

					// dlg.setIcon(R.drawable.first);
					dlg.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					dlg.show();
					break;
				case 1: // 제작자
					break;
				case 2: // 이메일
					break;
				case 3: // 홈페이지
					break;
				case 4:	//추가 사항
					dlg.setMessage(getString(R.string.more)	);

					// dlg.setIcon(R.drawable.first);
					dlg.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					dlg.show();					
					break;
					
				}

			}

		});

		SimpleAdapter adapter = new SimpleAdapter(this, dataList,
				android.R.layout.simple_list_item_2, new String[] { "title",
						"comment" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		setListAdapter(adapter);

	}

	private List<Map<String, String>> createData() {
		List<Map<String, String>> retDataList = new ArrayList<Map<String, String>>();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", "Version");
		data.put("comment", "2.2");
		retDataList.add(data);

		data = new HashMap<String, String>();
		data.put("title", "Producer");
		data.put("comment", "YoungHun Kim");
		retDataList.add(data);

		data = new HashMap<String, String>();
		data.put("title", "E-Mail");
		data.put("comment", "cetylene07@gmail.com");
		retDataList.add(data);

		data = new HashMap<String, String>();
		data.put("title", "HomePage");
		data.put("comment", "http://stronguy13.tistory.com/");
		retDataList.add(data);
		
		data = new HashMap<String, String>();
		data.put("title", "More...");
		data.put("comment", "To Be Continue");
		retDataList.add(data);
		



		return retDataList;
	}
}