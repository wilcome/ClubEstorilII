package com.whatafabric.clubestorilII;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.util.Vector;

public class GoalScorerFragment extends Fragment{

	ClubEstorilIIApplication clubEstorilIIApplication = null;
	LinearLayout goalScorerLinearLayout = null;
	TableLayout tableGoalScorer = null;
	ProgressBar progressBarGoalScorer = null;
	ExtractGoalScorerTask goalScorerTask = null;
	TextView noConnectionTextS = null;
	TextView noWSAvailableTextS = null;

	// Code to identify the fragment that is calling onActivityResult(). We don't really need
	// this since we only have one fragment to deal with.
	static final int TASK_GOALSCORER_FRAGMENT = 5;

	// Tag so we can find the task fragment again, in another instance of this fragment after rotation.
	static final String TASK_GOALSCORER_FRAGMENT_TAG = "goalScorer";

	public static GoalScorerFragment newInstance(){
		GoalScorerFragment goalScorerFragment = new GoalScorerFragment();
		return goalScorerFragment;
	}

	public void handleResponse(Vector<Vector<String>> stringTable){
		Log.d("-kk-GoalScorerFragment","handleResponse");
        Log.d("-kk-GoalScorerFragment","stringTable: " + stringTable.toString());
		for(int r = 0; r < stringTable.size(); r++ ){
			//if (r == 0)
			//	continue;
			TableRow row = new TableRow(getActivity());
			for(int c = 1; c < stringTable.get(r).size(); c++ ){
				TextView colTV = new TextView(getActivity());
                if(r == 0 && stringTable.get(r).get(c).equals("Jugador")){
                    colTV.setText("Jugador");
                }else if(r == 0 && stringTable.get(r).get(c).equals("Equipo")){
					colTV.setText("Equipo");
				}else if(r == 0 && stringTable.get(r).get(c).equals("G")){
					colTV.setText("G");
				}else if(r == 0 && stringTable.get(r).get(c).equals("G/P")){
					colTV.setText("G/P");
				}else if(r == 0 && stringTable.get(r).get(c).equals("PJ")){
					colTV.setText("PJ");
				}else{
                    if(c==1) {
                        String name = stringTable.get(r).get(2).substring(stringTable.get(r).get(2).indexOf(",")+1);
                        String surName = stringTable.get(r).get(2).substring(0, stringTable.get(r).get(2).indexOf(","));
                        colTV.setText(name + " " + surName);
                        //colTV.setText(stringTable.get(r).get(2));
                    }else if(c==2) {
                        colTV.setText(stringTable.get(r).get(1));
                    }else{
                        colTV.setText(stringTable.get(r).get(c));
                    }
				}

				colTV.setSingleLine(true);
				LayoutParams lyp = new LayoutParams();
				lyp.setMargins(2, 2, 2, 2);
				colTV.setLayoutParams(lyp);

				if(c == 1){
					colTV.setEllipsize(TruncateAt.END);
					int portionWith = 0;
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
						portionWith = (getWithScreen()*2)/4;
						colTV.setMaxWidth(portionWith);
					}else{
						portionWith = (getWithScreenOld()*2)/4;
						colTV.setMaxWidth(portionWith);
					}
				}else if(c == 2){
                    colTV.setEllipsize(TruncateAt.END);
                    int portionWith = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                        portionWith = (getWithScreen()*2)/12;
                        colTV.setMaxWidth(portionWith);
                    }else{
                        portionWith = (getWithScreenOld()*2)/12;
                        colTV.setMaxWidth(portionWith);
                    }
                }else{
					colTV.setGravity(Gravity.RIGHT);
				}
				row.addView(colTV);
			}
			tableGoalScorer.addView(row);
		}
		for(int c = 1; c< stringTable.get(1).size()-1; c++){
			tableGoalScorer.setColumnStretchable(c, true);
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
		Log.d("-kk-GoalScorerFragment","Checking Internet ...");
		ConnectivityManager connect = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// Check if wifi or mobile network is available or not. If any of them is
		// available or connected then it will return true, otherwise false;
		return wifi.isConnected() || mobile.isConnected();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d("-kk-GoalScorerFragment","onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		Log.d("-kk-GoalScorerFragment","onCreateView");

		clubEstorilIIApplication = ((ClubEstorilIIApplication)this.getActivity().getApplication());

		View v = inflater.inflate(R.layout.goalscorer_fragment, container, false);
		goalScorerLinearLayout = (LinearLayout)v.findViewById(R.id.GoalScorerLinearLayout);
		tableGoalScorer = (TableLayout)v.findViewById(R.id.GoalScorerTable);
		progressBarGoalScorer = (ProgressBar)v.findViewById(R.id.progressBarGoalScorer);
		noConnectionTextS = (TextView)v.findViewById(R.id.NoConnectionTextS);
		noWSAvailableTextS = (TextView)v.findViewById(R.id.NoWSAvailableTextS);


		//first check network state:
		if(!CheckInternet(getActivity())){
			Log.d("-kk-GoalScorerFragment","NO connectivity");
			noConnectionTextS.setVisibility(View.VISIBLE);
		}else{
			noConnectionTextS.setVisibility(View.GONE);
			Log.d("-kk-GoalScorerFragment","Connectivity OK");



			// Start the goalScorer task


			if(clubEstorilIIApplication.getGoalScorerTaskState() == ClubEstorilIIApplication.STOPPED){
				Log.d("-kk-GoalScorerFragment","class state STOPPED");
				progressBarGoalScorer.setVisibility(View.VISIBLE);
				goalScorerTask = new ExtractGoalScorerTask();
				goalScorerTask.setFragment(this);
				goalScorerTask.execute("http://cdestoril2.sagois.eu/FSALA_SENIOR_2_DIV_1415/Clasificacion_Goleadores.htm");
				//Set in the Application class that this task has been started 
				clubEstorilIIApplication.setGoalScorerTaskState(ClubEstorilIIApplication.RUNNING);
				clubEstorilIIApplication.setGoalScorerTask(goalScorerTask);
			}else if(clubEstorilIIApplication.getGoalScorerTaskState() == ClubEstorilIIApplication.FINISHED){
				//get the object from Application and handle it
				Log.d("-kk-GoalScorerFragment","class state FINISHED");
				handleResponse(clubEstorilIIApplication.getGoalScorerTable());
			}else{
				//The task is already executing so we show the dialog and update the reference
				Log.d("-kk-GoalScorerFragment","class state EXECUTING");
				progressBarGoalScorer.setVisibility(View.VISIBLE);
				goalScorerTask = clubEstorilIIApplication.getGoalScorerTask();
				goalScorerTask.setFragment(this);
			}

		}

		return v;
	}


	/* for future communications between fragments
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		Log.d("-kk-GoalScorerFragment","onAttach");
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mCallbacks = sDummyCallbacks;
		Log.d("-kk-GoalScorerFragment","onDetach");
	}*/


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d("-kk-GoalScorerFargment", "onActivityResult");

		if (requestCode == TASK_GOALSCORER_FRAGMENT && resultCode == Activity.RESULT_OK)
		{
			// Hide the progress dialog. 
			progressBarGoalScorer.setVisibility(View.GONE);
			clubEstorilIIApplication.setGoalScorerTaskState(ClubEstorilIIApplication.STOPPED);
			
			Vector<Vector<String>> goalScorerTables = null;

			if(data.getSerializableExtra("com.whatafabric.clubestorilII.goalScorerTable") != null){
				goalScorerTables = (Vector<Vector<String>>)data.getSerializableExtra("com.whatafabric.clubestorilII.goalScorerTable");
				handleResponse(goalScorerTables);
			}else{
				Log.d("-kk-ClassificationFargment", "noWSAvailableTextS visible");
				noWSAvailableTextS.setVisibility(View.VISIBLE);
			}
			clubEstorilIIApplication.setGoalScorerTable(goalScorerTables);
			clubEstorilIIApplication.setGoalScorerTaskState(ClubEstorilIIApplication.FINISHED);

		}
	}

}

