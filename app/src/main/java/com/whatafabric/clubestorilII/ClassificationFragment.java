package com.whatafabric.clubestorilII;

import java.util.Vector;

import com.whatafabric.clubestorilII.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

public class ClassificationFragment extends Fragment{

	ClubEstorilIIApplication clubEstorilIIApplication = null;
	LinearLayout classificationLinearLayout = null;
	TableLayout tableClasification = null;
	ProgressBar progressBarClassification = null;
	ExtractClassificationTask classificationTask = null;
	TextView noConnectionTextC = null;
	TextView noWSAvailableTextC = null;
	int  taskClassificationFragmentId = 0; 
	//The requested website.
	String website = "";

	public static ClassificationFragment newInstance(String website, int classId){
		Log.d("-kk-ClassificationFragment","ClassificationFragment");
		ClassificationFragment classificationFragment = new ClassificationFragment();
		Bundle args = new Bundle();
		args.putInt("taskClassificationFragmentId",classId);
		args.putString("website", website);
		classificationFragment.setArguments(args);
		return classificationFragment;
	}

	public void handleResponse(Vector<Vector<String>> stringTable){
		Log.d("-kk-ClassificationFragment","handleResponse");
		int nameCalPos = -1;
		if (stringTable == null)
			Log.d("-kk-ClassificationFragment","stringTable is null");
		for(int r = 0; r < stringTable.size(); r++ ){
			TableRow row = new TableRow(getActivity());
			for(int c = 0; c < stringTable.get(r).size()-1; c++ ){
				if (stringTable.get(r).get(c).contains("Nombre"))
					nameCalPos = c;
				TextView colTV = new TextView(getActivity());
				colTV.setText(stringTable.get(r).get(c));
				colTV.setSingleLine(true);
				LayoutParams lyp = new TableRow.LayoutParams();
				lyp.setMargins(2, 2, 2, 2);
				colTV.setLayoutParams(lyp);

				if(c == nameCalPos){
					colTV.setEllipsize(TruncateAt.END);
					int portionWith = 0;
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
						portionWith = (getWithScreen()*2)/5;
						colTV.setMaxWidth(portionWith);
					}else{
						portionWith = (getWithScreenOld()*2)/5;
						colTV.setMaxWidth(portionWith);
					}
				}else{
					colTV.setGravity(Gravity.RIGHT);
				}
				row.addView(colTV);
			}
			tableClasification.addView(row);
		}


		for(int c = 0; c< stringTable.get(1).size()-1; c++){
			tableClasification.setColumnStretchable(c, true);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public int getWithScreen(){
		int width = 0;
		Display display = getActivity().getWindowManager().getDefaultDisplay(); 
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		return width;
	}

	public int getWithScreenOld(){
		int width = 0;
		Display display = getActivity().getWindowManager().getDefaultDisplay(); 
		width = display.getWidth();  // deprecated
		return width;
	}

	public boolean CheckInternet(Context ctx) {
		Log.d("-kk-ClassificationFragment","Checking Internet ...");
		ConnectivityManager connec = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// Check if wifi or mobile network is available or not. If any of them is
		// available or connected then it will return true, otherwise false;
		return wifi.isConnected() || mobile.isConnected();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d("-kk-ClassificationFragment","onCreate");
		website = getArguments().getString("website");
		taskClassificationFragmentId = getArguments().getInt("taskClassificationFragmentId");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		Log.d("-kk-ClassificationFragment",taskClassificationFragmentId + " onCreateView");

		clubEstorilIIApplication = ((ClubEstorilIIApplication)this.getActivity().getApplication());

		View v = inflater.inflate(R.layout.classification_fragment, container, false);
		classificationLinearLayout = (LinearLayout)v.findViewById(R.id.ClassificationLinearLayout);
		tableClasification = (TableLayout)v.findViewById(R.id.ClassificationTable);
		progressBarClassification = (ProgressBar)v.findViewById(R.id.progressBarClassification);
		noConnectionTextC = (TextView)v.findViewById(R.id.NoConnectionTextC);
		noWSAvailableTextC = (TextView)v.findViewById(R.id.NoWSAvailableTextC);

		//first check network state:
		if(!CheckInternet(getActivity())){
			Log.d("-kk-ClassificationFragment","NO connectivity");
			noConnectionTextC.setVisibility(View.VISIBLE);
		}else{
			noConnectionTextC.setVisibility(View.GONE);
			Log.d("-kk-ClassificationFragment","Connectivity OK");

			// Start the classification task

			if(clubEstorilIIApplication.getClassificationTaskState(taskClassificationFragmentId) == ClubEstorilIIApplication.STOPPED){
				progressBarClassification.setVisibility(View.VISIBLE);
				Log.d("-kk-ClassificationFragment","class state STOPPED");
				classificationTask = new ExtractClassificationTask();
				classificationTask.setFragment(this);
				classificationTask.execute(website);
				//Set in the Application class that this task has been started 
				clubEstorilIIApplication.setClassificationTaskState(ClubEstorilIIApplication.RUNNING,taskClassificationFragmentId);
				clubEstorilIIApplication.setClassificationTask(classificationTask,taskClassificationFragmentId);
			}else if(clubEstorilIIApplication.getClassificationTaskState(taskClassificationFragmentId) == ClubEstorilIIApplication.FINISHED){
				//get the object from Application and handle it
				Log.d("-kk-ClassificationFragment","class state FINISHED");
				handleResponse(clubEstorilIIApplication.getClassificationTable(taskClassificationFragmentId));
			}else{
				//The task is already executing so we show the dialog and update the reference
				Log.d("-kk-ClassificationFragment","class state EXECUTING");
				progressBarClassification.setVisibility(View.VISIBLE);
				classificationTask = clubEstorilIIApplication.getClassificationTask(taskClassificationFragmentId);

				classificationTask.setFragment(this);
			}
		}

		return v;
	}


	/* for future communications between fragments
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		Log.d("-kk-ClassificationFragment","onAttach");

	}

	@Override
	public void onDetach()
	{

		super.onDetach();
		mCallbacks = sDummyCallbacks;
		Log.d("-kk-ClassificationFragment","onDetach");
	}*/


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d("-kk-ClassificationFargment", "onActivityResult");
		Log.d("-kk-RoundsFragment", "resultCode stored "+ taskClassificationFragmentId);
		Log.d("-kk-RoundsFragment", "resultCode received "+ requestCode);


		if (requestCode == taskClassificationFragmentId && resultCode == Activity.RESULT_OK)
		{
			// Hide the progress dialog. 
			//clubEstorilIIApplication.setClassificationTaskState(ClubEstorilIIApplication.STOPPED,taskClassificationFragmentId);
			progressBarClassification.setVisibility(View.GONE);
			noWSAvailableTextC.setVisibility(View.GONE);



			if(data.getSerializableExtra("com.whatafabric.clubestorilII.classificationTable-id-"+taskClassificationFragmentId) != null){
				Vector<Vector<String>> classificationTables = null;
				classificationTables = (Vector<Vector<String>>)data.getSerializableExtra("com.whatafabric.clubestorilII.classificationTable-id-"+taskClassificationFragmentId);
				handleResponse(classificationTables);
				clubEstorilIIApplication.setClassificationTable(classificationTables,taskClassificationFragmentId);
				clubEstorilIIApplication.setClassificationTaskState(ClubEstorilIIApplication.FINISHED,taskClassificationFragmentId);
			}else{
				Log.d("-kk-ClassificationFargment", "noWSAvailableTextC visible");
				noWSAvailableTextC.setVisibility(View.VISIBLE);
				clubEstorilIIApplication.setClassificationTaskState(ClubEstorilIIApplication.STOPPED,taskClassificationFragmentId);
			}
		}
	}

	public int getTaskClassificationFragmentId(){
		return taskClassificationFragmentId;
	}

	public void setTaskClassificationFragmentId(int trfId){
		taskClassificationFragmentId = trfId;
	}
}

