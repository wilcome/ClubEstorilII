package com.whatafabric.clubestorilII;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Vector;

public class ExtractGoalScorerTask extends AsyncTask<String, Void, Vector<Vector<String>>>
{
	GoalScorerFragment mFragment;
	static final int TASK_GOALSCORER_FRAGMENT = 5;

	void setFragment(GoalScorerFragment fragment)
	{
		mFragment = fragment;
	}

	@Override
	protected Vector<Vector<String>> doInBackground(String... urls)
	{
		Document document = null;
		Vector<Vector<String>> responseGoalScorer = null;

		try {
			document = Jsoup.parse(new URL(urls[0]).openStream(), "utf-16LE", urls[0]);
			responseGoalScorer = extractGoalScorerTable(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseGoalScorer;
	}
	
	private static Vector<Vector<String>> extractGoalScorerTable(Document doc){

		Vector<Vector<String>> responseGoalScorer = new Vector<Vector<String>>();
		
		Elements tables = doc.select("table");
		Element table = tables.get(1);//"1" indicates the index of the table we want
		Elements rows = table.select("tr");

		for (Element row: rows){
			Vector<String> tempRow = new Vector<String>();
			Elements colums = row.select("td");
			for (Element column: colums){
				tempRow.add(column.text()) ;
			}
			responseGoalScorer.add(tempRow);
		}

		return responseGoalScorer;
	}
	
	@Override
	protected void onPostExecute(Vector<Vector<String>> goalScorerTable)
	{
		if (mFragment == null)
			return;
		
		Intent data = new Intent();
		data.putExtra("com.whatafabric.clubestorilII.goalScorerTable",(Serializable)goalScorerTable);
		mFragment.onActivityResult(TASK_GOALSCORER_FRAGMENT, Activity.RESULT_OK,data);
	}
}