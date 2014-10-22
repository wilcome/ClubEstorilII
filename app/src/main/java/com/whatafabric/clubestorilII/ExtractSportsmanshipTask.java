package com.whatafabric.clubestorilII;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;

public class ExtractSportsmanshipTask extends AsyncTask<String, Void, Vector<Vector<String>>>
{
	SportsmanshipFragment mFragment;
	static final int TASK_SPORTSMANSHIP_FRAGMENT = 2;

	void setFragment(SportsmanshipFragment fragment)
	{
		mFragment = fragment;
	}

	@Override
	protected Vector<Vector<String>> doInBackground(String... urls)
	{
		Document document = null;
		Vector<Vector<String>> responseSportsmanship = null;

		try {
			document = Jsoup.parse(new URL(urls[0]).openStream(), "utf-16LE", urls[0]);
			responseSportsmanship = extractSportsmanshipTable(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseSportsmanship;
	}
	
	private static Vector<Vector<String>> extractSportsmanshipTable(Document doc){

		Vector<Vector<String>> responseSportsmanship = new Vector<Vector<String>>();
		
		Elements tables = doc.select("table");
		Element table = tables.get(1);//"1" indicates the index of the table we want
		Elements rows = table.select("tr");

		for (Element row: rows){
			Vector<String> tempRow = new Vector<String>();
			Elements colums = row.select("td");
			for (Element column: colums){
				tempRow.add(column.text()) ;
			}
			responseSportsmanship.add(tempRow);
		}

		return responseSportsmanship;
	}
	
	@Override
	protected void onPostExecute(Vector<Vector<String>> sportsmanshipTable)
	{
		if (mFragment == null)
			return;
		
		Intent data = new Intent();
		data.putExtra("com.whatafabric.clubestorilII.sportsmanshipTable",(Serializable)sportsmanshipTable);
		mFragment.onActivityResult(TASK_SPORTSMANSHIP_FRAGMENT, Activity.RESULT_OK,data);
	}
}