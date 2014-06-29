package com.hb.app.tong;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class UserDialogFragment extends DialogFragment{

	/**
	 * @uml.property  name="builder"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(savedInstanceState);
	}

}
