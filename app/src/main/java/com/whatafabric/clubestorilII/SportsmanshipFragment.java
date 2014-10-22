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

public class SportsmanshipFragment extends Fragment{

	ClubEstorilIIApplication clubEstorilIIApplication = null;
	LinearLayout sportsmanshipLinearLayout = null;
	TableLayout tableSportsmanship = null;
	ProgressBar progressBarSportsmanship = null;
	ExtractSportsmanshipTask sportsmanshipTask = null;
	TextView noConnectionTextS = null;
	TextView noWSAvailableTextS = null;

	// Code to identify the fragment that is calling onActivityResult(). We don't really need
	// this since we only have one fragment to deal with.
	static final int TASK_SPORTSMANSHIP_FRAGMENT = 2;

	// Tag so we can find the task fragment again, in another instance of this fragment after rotation.
	static final String TASK_SPORTSMANSHIP_FRAGMENT_TAG = "sportsmanship";

	public static SportsmanshipFragment newInstance(){
		SportsmanshipFragment sportsmanshipFragment = new SportsmanshipFragment();
		return sportsmanshipFragment;
	}

	public void handleResponse(Vector<Vector<String>> stringTable){
		Log.d("-kk-SportsmanshipFragment","handleResponse");
		int nameCalPos = -1;
		for(int r = 0; r < stringTable.size(); r++ ){
			if (r == 0)
				continue;
			TableRow row = new TableRow(getActivity());
			for(int c = 0; c < stringTable.get(r).size()-1; c++ ){
				if (stringTable.get(r).get(c).contains("Nombre"))
					nameCalPos = c;
				TextView colTV = new TextView(getActivity());
				if(r == 1 && stringTable.get(r).get(c).equals("puntos")){
					colTV.setText("PTS");
				}else if(r == 1 && stringTable.get(r).get(c).equals("Total")){
					colTV.setText("TR");
				}else if(r == 1 && stringTable.get(r).get(c).equals("Directas")){
					colTV.setText("TR.D");
				}else if(r == 1 && stringTable.get(r).get(c).equals("Acum.")){
					colTV.setText("TR.A");
				}else{
					colTV.setText(stringTable.get(r).get(c));
				}

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
			tableSportsmanship.addView(row);
		}
		for(int c = 0; c< stringTable.get(1).size()-2; c++){
			tableSportsmanship.setColumnStretchable(c, true);
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
		Log.d("-kk-SportsmanshipFragment","Checking Internet ...");
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
		Log.d("-kk-SportsmanshipFragment","onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		Log.d("-kk-SportsmanshipFragment","onCreateView");

		clubEstorilIIApplication = ((ClubEstorilIIApplication)this.getActivity().getApplication());

		View v = inflater.inflate(R.layout.sportsmanship_fragment, container, false);
		sportsmanshipLinearLayout = (LinearLayout)v.findViewById(R.id.SportsmanshipLinearLayout);
		tableSportsmanship = (TableLayout)v.findViewById(R.id.SportsmanshipTable);
		progressBarSportsmanship = (ProgressBar)v.findViewById(R.id.progressBarSportsmanship);
		noConnectionTextS = (TextView)v.findViewById(R.id.NoConnectionTextS);
		noWSAvailableTextS = (TextView)v.findViewById(R.id.NoWSAvailableTextS);


		//first check network state:
		if(!CheckInternet(getActivity())){
			Log.d("-kk-SportsmanshipFragment","NO connectivity");
			noConnectionTextS.setVisibility(View.VISIBLE);
		}else{
			noConnectionTextS.setVisibility(View.GONE);
			Log.d("-kk-SportsmanshipFragment","Connectivity OK");



			// Start the sportsmanship task


			if(clubEstorilIIApplication.getSportsmanshipTaskState() == ClubEstorilIIApplication.STOPPED){
				Log.d("-kk-SportsmanshipFragment","class state STOPPED");
				progressBarSportsmanship.setVisibility(View.VISIBLE);
				sportsmanshipTask = new ExtractSportsmanshipTask();
				sportsmanshipTask.setFragment(this);
				sportsmanshipTask.execute("http://cdestoril2.sagois.eu/FSALA_SENIOR_1314/Clasificacion_Deportividad.htm");
				//Set in the Application class that this task has been started 
				clubEstorilIIApplication.setSportsmanshipTaskState(ClubEstorilIIApplication.RUNNING);
				clubEstorilIIApplication.setSportsmanshipTask(sportsmanshipTask);
			}else if(clubEstorilIIApplication.getSportsmanshipTaskState() == ClubEstorilIIApplication.FINISHED){
				//get the object from Application and handle it
				Log.d("-kk-SportsmanshipFragment","class state FINISHED");
				handleResponse(clubEstorilIIApplication.getSportsmanshipTable());
			}else{
				//The task is already executing so we show the dialog and update the reference
				Log.d("-kk-SportsmanshipFragment","class state EXECUTING");
				progressBarSportsmanship.setVisibility(View.VISIBLE);
				sportsmanshipTask = clubEstorilIIApplication.getSportsmanshipTask();
				sportsmanshipTask.setFragment(this);
			}

		}

		return v;
	}


	/* for future communications between fragments
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		Log.d("-kk-SportsmanshipFragment","onAttach");
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mCallbacks = sDummyCallbacks;
		Log.d("-kk-SportsmanshipFragment","onDetach");
	}*/


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d("-kk-SportsmanshipFargment", "onActivityResult");

		if (requestCode == TASK_SPORTSMANSHIP_FRAGMENT && resultCode == Activity.RESULT_OK)
		{
			// Hide the progress dialog. 
			progressBarSportsmanship.setVisibility(View.GONE);
			clubEstorilIIApplication.setSportsmanshipTaskState(ClubEstorilIIApplication.STOPPED);
			
			Vector<Vector<String>> sportsmanshipTables = null;

			if(data.getSerializableExtra("com.whatafabric.clubestorilII.sportsmanshipTable") != null){
				sportsmanshipTables = (Vector<Vector<String>>)data.getSerializableExtra("com.whatafabric.clubestorilII.sportsmanshipTable");
				handleResponse(sportsmanshipTables);
			}else{
				Log.d("-kk-ClassificationFargment", "noWSAvailableTextS visible");
				noWSAvailableTextS.setVisibility(View.VISIBLE);
			}
			clubEstorilIIApplication.setSportsmanshipTable(sportsmanshipTables);
			clubEstorilIIApplication.setSportsmanshipTaskState(ClubEstorilIIApplication.FINISHED);

		}
	}

}

