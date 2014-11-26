
/*
 * call log에서 따로 데이터를 추출해서 저장하는 클래스
 */

package com.smartstat.info;


public class Info {
	public String name; 					// 이름
	public int rank;						// 순위
	public int in_count; 					// 수신통화 누적 회수
	public int in_dur; 					// 수신통화 누적 시간
	public int out_count; 					// 발신통산 누적 회수
	public int out_dur;					// 발신통화 누적시간
	public int miss_count;					// 부재통화 누적 회수
	public int sum_dur;					// 합산길이 :  수신길이 + 발신길이
	public double incount_percent; 		// 수신횟수 비율
	public double indur_percent; 			// 수신길이 비율
	public double outcount_percent; 		// 발신횟수 비율
	public double outdur_percent; 			// 발신길이 비율
	public double miss_percent; 			// 부재횟수 비율
	public double sum_dur_percent;
	public double average_sum_dur;			//1인당 평균 수신+발신 길이
	public double average_in_dur;			//1인당 평균 수신 길이
	public double average_out_dur;			//1인당 평균 발신 길이
	public double average_in_dur_percent;	//1인당 평균 수신 길이 비율
	public double average_out_dur_percent;	//1인당 평균 발신 길이 비율
	public long in_year;
	public long in_month[];
	public long in_day[];
	public long in_hour[];

	// 이미지를 불러오기 위한 변수들
	public String imgId;
	public String imgName;
	public String call_photo;				//

	public Info() {
		// 생성자 : 초기화 시킨다.
		in_count = 0;
		in_dur = 0;
		out_count = 0;
		out_dur = 0;
		sum_dur = 0;
		miss_count = 0;
		name = "몰라 ㅋ";
		
		in_hour = new long[24];
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getIn_count() {
		return in_count;
	}

	public void setIn_count(int in_count) {
		this.in_count = in_count;
	}

	public int getIn_dur() {
		return in_dur;
	}

	public void setIn_dur(int in_dur) {
		this.in_dur = in_dur;
	}

	public int getOut_count() {
		return out_count;
	}

	public void setOut_count(int out_count) {
		this.out_count = out_count;
	}

	public int getOut_dur() {
		return out_dur;
	}

	public void setOut_dur(int out_dur) {
		this.out_dur = out_dur;
	}

	public int getMiss_count() {
		return miss_count;
	}

	public void setMiss_count(int miss_count) {
		this.miss_count = miss_count;
	}

	public int getSum_dur() {
		return sum_dur;
	}

	public void setSum_dur(int sum_dur) {
		this.sum_dur = sum_dur;
	}

	public double getIncount_percent() {
		return incount_percent;
	}

	public void setIncount_percent(double incount_percent) {
		this.incount_percent = incount_percent;
	}

	public double getIndur_percent() {
		return indur_percent;
	}

	public void setIndur_percent(double indur_percent) {
		this.indur_percent = indur_percent;
	}

	public double getOutcount_percent() {
		return outcount_percent;
	}

	public void setOutcount_percent(double outcount_percent) {
		this.outcount_percent = outcount_percent;
	}

	public double getOutdur_percent() {
		return outdur_percent;
	}

	public void setOutdur_percent(double outdur_percent) {
		this.outdur_percent = outdur_percent;
	}

	public double getMiss_percent() {
		return miss_percent;
	}

	public void setMiss_percent(double miss_percent) {
		this.miss_percent = miss_percent;
	}

	public double getSum_dur_percent() {
		return sum_dur_percent;
	}

	public void setSum_dur_percent(double sum_dur_percent) {
		this.sum_dur_percent = sum_dur_percent;
	}

	public double getAverage_sum_dur() {
		return average_sum_dur;
	}

	public void setAverage_sum_dur(double average_sum_dur) {
		this.average_sum_dur = average_sum_dur;
	}

	public double getAverage_in_dur() {
		return average_in_dur;
	}

	public void setAverage_in_dur(double average_in_dur) {
		this.average_in_dur = average_in_dur;
	}

	public double getAverage_out_dur() {
		return average_out_dur;
	}

	public void setAverage_out_dur(double average_out_dur) {
		this.average_out_dur = average_out_dur;
	}

	public double getAverage_in_dur_percent() {
		return average_in_dur_percent;
	}

	public void setAverage_in_dur_percent(double average_in_dur_percent) {
		this.average_in_dur_percent = average_in_dur_percent;
	}

	public double getAverage_out_dur_percent() {
		return average_out_dur_percent;
	}

	public void setAverage_out_dur_percent(double average_out_dur_percent) {
		this.average_out_dur_percent = average_out_dur_percent;
	}

	public long getIn_year() {
		return in_year;
	}

	public void setIn_year(long in_year) {
		this.in_year = in_year;
	}

	public long[] getIn_month() {
		return in_month;
	}

	public void setIn_month(long[] in_month) {
		this.in_month = in_month;
	}

	public long[] getIn_day() {
		return in_day;
	}

	public void setIn_day(long[] in_day) {
		this.in_day = in_day;
	}

	public long[] getIn_hour() {
		return in_hour;
	}

	public void setIn_hour(long[] in_hour) {
		this.in_hour = in_hour;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getCall_photo() {
		return call_photo;
	}

	public void setCall_photo(String call_photo) {
		this.call_photo = call_photo;
	}
	
	public void inCreaseInCount()	{
		this.in_count++;
	}
	public void inCreaseOutCount()	{
		this.out_count++;
	}
	public void inCreaseMissCount()	{
		this.miss_count++;
	}


}
