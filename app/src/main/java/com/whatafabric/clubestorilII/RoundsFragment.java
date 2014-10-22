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
import android.widget.TableRow.LayoutParams;

public class RoundsFragment extends Fragment{

	ClubEstorilIIApplication clubEstorilIIApplication = null;
	LinearLayout roundsLinearLayout = null;
	ProgressBar progressBarRounds = null;
	ExtractRoundsTask roundsTask = null;
	TextView noConnectionTextR = null;
	TextView noWSAvailableTextR = null;
	int  taskRoundsFragmentId = 0; 
	//The requested website.
	String website = "";

	public static RoundsFragment newInstance(String website, int roundsId){
		RoundsFragment roundsFragment = new RoundsFragment();
		Bundle args = new Bundle();
		args.putInt("taskRoundsFragmentId",roundsId);
		args.putString("website", website);
		roundsFragment.setArguments(args);
		return roundsFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d("-kk-RoundsFragment", "onCreate");
		website = getArguments().getString("website");
		taskRoundsFragmentId = getArguments().getInt("taskRoundsFragmentId");
	}

	public void handleResponse(Vector<Vector<Vector<String>>> roundTables){

		int localNameCalPos = -1;
		int foreignNameCalPos = -1;

		android.widget.TableLayout.LayoutParams params = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

		for (int n = 0; n < roundTables.size(); n++){
			//Add round table textView tittle 
			TextView tittleRound = new TextView(getActivity());
			tittleRound.setGravity(Gravity.CENTER_HORIZONTAL);
			tittleRound.setText("JORNADA " + (n+1));
			roundsLinearLayout.addView(tittleRound);

			Vector<Vector<String>> roundTable = roundTables.get(n);

			TableLayout roundTableView = new TableLayout(getActivity());
			roundTableView.setLayoutParams(params);

			for(int r = 0; r < roundTable.size(); r++ ){
				TableRow row = new TableRow(getActivity());
				for(int c = 0; c < roundTable.get(r).size(); c++ ){
					if (roundTable.get(r).get(c).contains("Local"))
						localNameCalPos = c;
					if (roundTable.get(r).get(c).contains("Visitante"))
						foreignNameCalPos = c;
					TextView colTV = new TextView(getActivity());
					colTV.setText(roundTable.get(r).get(c));
					colTV.setSingleLine(true);
					LayoutParams lyp = new TableRow.LayoutParams();
					lyp.setMargins(2, 2, 2, 2);
					colTV.setLayoutParams(lyp);

					if(c == localNameCalPos){
						colTV.setEllipsize(TruncateAt.END);
						int portionWith = 0;
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
							portionWith = (getWithScreen())/4;
							colTV.setMaxWidth(portionWith);
						}else{
							portionWith = (getWithScreenOld())/4;
							colTV.setMaxWidth(portionWith);
						}
						colTV.setGravity(Gravity.RIGHT);
					}else if(c == foreignNameCalPos){
						colTV.setEllipsize(TruncateAt.END);
						int portionWith = 0;
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
							portionWith = (getWithScreen())/5;
							colTV.setMaxWidth(portionWith);
						}else{
							portionWith = (getWithScreenOld())/5;
							colTV.setMaxWidth(portionWith);
						}
						colTV.setGravity(Gravity.LEFT);
					}else{
						colTV.setGravity(Gravity.CENTER_HORIZONTAL);
					}
					row.addView(colTV);
				}
				roundTableView.addView(row);
			}
			for(int c = 0; c< roundTable.get(1).size(); c++){
				roundTableView.setColumnStretchable(c, true);
			}
			roundsLinearLayout.addView(roundTableView);
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
		Log.d("-kk-RoundsFragment","Checking Internet ...");
		ConnectivityManager connec = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// Check if wifi or mobile network is available or not. If any of them is
		// available or connected then it will return true, otherwise false;
		return wifi.isConnected() || mobile.isConnected();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		Log.d("-kk-RoundsFragment", taskRoundsFragmentId + " onCreateView");

		clubEstorilIIApplication = ((ClubEstorilIIApplication)this.getActivity().getApplication());

		View v = inflater.inflate(R.layout.rounds_fragment, container, false);
		roundsLinearLayout = (LinearLayout)v.findViewById(R.id.RoundsLinearLayout);
		progressBarRounds = (ProgressBar)v.findViewById(R.id.progressBarRounds);
		noConnectionTextR = (TextView)v.findViewById(R.id.NoConnectionTextR);
		noWSAvailableTextR = (TextView)v.findViewById(R.id.NoWSAvailableTextR);


		//first check network state:
		if(!CheckInternet(getActivity())){
			Log.d("-kk-RoundsFragment","NO Connectivity");
			noConnectionTextR.setVisibility(View.VISIBLE);
		}else{
			noConnectionTextR.setVisibility(View.GONE);
			Log.d("-kk-RoundsFragment","Connectivity OK");


			// Start the rounds task
			if(clubEstorilIIApplication.getRoundsTaskState(taskRoundsFragmentId) == ClubEstorilIIApplication.STOPPED){
				Log.d("-kk-RoundsFragment","rounds state STOPPED");
				progressBarRounds.setVisibility(View.VISIBLE);
				roundsTask = new ExtractRoundsTask();
				roundsTask.setFragment(this);
				roundsTask.execute(website);
				//Set in the Application class that this task has been started 
				clubEstorilIIApplication.setRoundsTaskState(ClubEstorilIIApplication.RUNNING,taskRoundsFragmentId);
				clubEstorilIIApplication.setRoundsTask(roundsTask,taskRoundsFragmentId);
			}else if(clubEstorilIIApplication.getRoundsTaskState(taskRoundsFragmentId) == ClubEstorilIIApplication.FINISHED){
				//get the object from Application and handle it
				Log.d("-kk-RoundsFragment","rounds state FINISHED");
				handleResponse(clubEstorilIIApplication.getRoundsTable(taskRoundsFragmentId));

			}else{
				//The task is already executing so we show the dialog and update the reference
				Log.d("-kk-RoundsFragment","rounds state EXECUTING");
				progressBarRounds.setVisibility(View.VISIBLE);
				roundsTask = clubEstorilIIApplication.getRoundsTask(taskRoundsFragmentId);
				roundsTask.setFragment(this);
			}
		}

		return v;
	}

	public int getTaskRoundsFragmentId(){
		return taskRoundsFragmentId;
	}

	public void setTaskRoundsFragmentId(int trfId){
		taskRoundsFragmentId = trfId;
	}
	
	/* for future communications between fragments
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		Log.d("-kk-RoundsFragment","onAttach");
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mCallbacks = sDummyCallbacks;
		Log.d("-kk-RoundsFragment","onDetach");
	}*/

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d("-kk-RoundsFragment", "onActivityResult");
		Log.d("-kk-RoundsFragment", "resultCode stored "+ taskRoundsFragmentId);
		Log.d("-kk-RoundsFragment", "resultCode received "+ requestCode);

		if (requestCode == taskRoundsFragmentId && resultCode == Activity.RESULT_OK)
		{

			Log.d("-kk-RoundsFragment", "dentro del if - inicio");
			// Hide the progress dialog. 
			//clubEstorilIIApplication.setRoundsTaskState(ClubEstorilIIApplication.STOPPED,taskRoundsFragmentId);
			progressBarRounds.setVisibility(View.GONE);
			noWSAvailableTextR.setVisibility(View.GONE);

			if(data.getSerializableExtra("com.whatafabric.clubestorilII.roundsTable-id-"+taskRoundsFragmentId) != null){
				Vector<Vector<Vector<String>>> roundsTables = null;
				roundsTables = (Vector<Vector<Vector<String>>>)data.getSerializableExtra("com.whatafabric.clubestorilII.roundsTable-id-"+taskRoundsFragmentId);
				handleResponse(roundsTables);
				clubEstorilIIApplication.setRoundsTable(roundsTables,taskRoundsFragmentId);
				clubEstorilIIApplication.setRoundsTaskState(ClubEstorilIIApplication.FINISHED,taskRoundsFragmentId);
			}else{
				Log.d("-kk-RoundsFragment", "noWSAvailableTextR visible");
				noWSAvailableTextR.setVisibility(View.VISIBLE);
				clubEstorilIIApplication.setRoundsTaskState(ClubEstorilIIApplication.STOPPED,taskRoundsFragmentId);
			}
		}
	}
}


