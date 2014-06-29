package com.smartstat.info;

import java.util.ArrayList;

public class DateInfo {

	// 요일 수신길이, 발신길이
	/**
	 * @uml.property  name="sun_in_dur"
	 */
	public int sun_in_dur;
	/**
	 * @uml.property  name="sun_out_dur"
	 */
	public int sun_out_dur;
	/**
	 * @uml.property  name="mon_in_dur"
	 */
	public int mon_in_dur;
	/**
	 * @uml.property  name="mon_out_dur"
	 */
	public int mon_out_dur;
	/**
	 * @uml.property  name="tus_in_dur"
	 */
	public int tus_in_dur;
	/**
	 * @uml.property  name="tus_out_dur"
	 */
	public int tus_out_dur;
	/**
	 * @uml.property  name="wed_in_dur"
	 */
	public int wed_in_dur;
	/**
	 * @uml.property  name="wed_out_dur"
	 */
	public int wed_out_dur;
	/**
	 * @uml.property  name="thr_in_dur"
	 */
	public int thr_in_dur;
	/**
	 * @uml.property  name="thr_out_dur"
	 */
	public int thr_out_dur;
	/**
	 * @uml.property  name="fri_in_dur"
	 */
	public int fri_in_dur;
	/**
	 * @uml.property  name="fri_out_dur"
	 */
	public int fri_out_dur;
	/**
	 * @uml.property  name="sat_in_dur"
	 */
	public int sat_in_dur;
	/**
	 * @uml.property  name="sat_out_dur"
	 */
	public int sat_out_dur;

	// 한달간 수신, 발신 길이
	/**
	 * @uml.property  name="month_in_dur"
	 */
	public int month_in_dur;
	/**
	 * @uml.property  name="month_out_dur"
	 */
	public int month_out_dur;

	// 한달간....30일?? 31일?? 28일?? 수신,발신 길이
	/**
	 * @uml.property  name="days_in_dur"
	 */
	public ArrayList days_in_dur;
	/**
	 * @uml.property  name="days_out_dur"
	 */
	public ArrayList days_out_dur;

	/**
	 * @uml.property  name="hour_in_dur" multiplicity="(0 -1)" dimension="1"
	 */
	public 	int[] hour_in_dur = new int[24];
	/**
	 * @uml.property  name="hour_out_dur" multiplicity="(0 -1)" dimension="1"
	 */
	public int[] hour_out_dur = new int[24];

	public DateInfo() {
		sun_in_dur = 0;
		mon_in_dur = 0;
		tus_in_dur = 0;
		wed_in_dur = 0;
		thr_in_dur = 0;
		fri_in_dur = 0;
		sat_in_dur = 0;

		sun_out_dur = 0;
		mon_out_dur = 0;
		tus_out_dur = 0;
		wed_out_dur = 0;
		thr_out_dur = 0;
		fri_out_dur = 0;
		sat_out_dur = 0;

		for (int i = 0; i < 24; i++) {
			hour_in_dur[i] = 0;
			hour_out_dur[i] = 0;
		}
	}
}