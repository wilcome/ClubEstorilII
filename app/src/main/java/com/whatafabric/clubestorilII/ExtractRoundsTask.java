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
import android.util.Log;

public class ExtractRoundsTask extends AsyncTask<String, Void, Vector<Vector<Vector<String>>>>
{
	RoundsFragment mFragment;

	void setFragment(RoundsFragment fragment)
	{
		mFragment = fragment;
	}

	@Override
	protected Vector<Vector<Vector<String>>> doInBackground(String... urls)
	{
		Document document = null;
		Vector<Vector<Vector<String>>> responseRounds = null;

		try {
			document = Jsoup.parse(new URL(urls[0]).openStream(), "utf-16LE", urls[0]);
			responseRounds = extractRoundsTable(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseRounds;
	}

	private Vector<Vector<Vector<String>>> extractRoundsTable(Document doc){
		Log.d("-kk-extractRoundsTask", "extractRoundsTable");
		Vector<Vector<Vector<String>>> responseRounds = new Vector<Vector<Vector<String>>>();

		Elements tables = doc.select("table");
		Element table = tables.get(1);//"1" indicates the index of the table we want
		Elements bigTable = table.select("tr");
		Elements roundTables = bigTable.select("table");

		for (Element roundTable: roundTables){

			//jump fucking cell table 
			if (!roundTable.text().contains("JORNADA")){
				continue;
			}

			Elements rows = roundTable.select("tr");
			Vector<Vector<String>> temRoundTable = new Vector<Vector<String>>();

			for (Element row: rows){
				Vector<String> tempRow = new Vector<String>();
				Elements colums = row.select("td.style2");
				int columnN = 1;
				for (Element column: colums){
					if (columnN == 3 || columnN == 4){
						columnN++;
						continue;
					}
					tempRow.add(column.text()) ;
					columnN++;
				}
				temRoundTable.add(tempRow);
			}

			responseRounds.add(temRoundTable);
		}
		Log.d("-kk-extractRoundsTask", "extractRoundsTable END");
		return responseRounds;
	}

	@Override
	protected void onPostExecute(Vector<Vector<Vector<String>>> roundsTable)
	{
		if (mFragment == null)
			return;
		Intent data = new Intent();
		Log.d("-kk-extractRoundsTask", "put serialized: "+"com.whatafabric.clubestorilII.roundsTable-id-"+mFragment.getTaskRoundsFragmentId());
		data.putExtra("com.whatafabric.clubestorilII.roundsTable-id-"+mFragment.getTaskRoundsFragmentId(),(Serializable)roundsTable);
		mFragment.onActivityResult(mFragment.getTaskRoundsFragmentId(), Activity.RESULT_OK,data);
	}
}