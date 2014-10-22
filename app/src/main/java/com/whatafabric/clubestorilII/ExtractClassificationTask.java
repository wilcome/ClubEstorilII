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
import android.util.Log;

public class ExtractClassificationTask extends AsyncTask<String, Void, Vector<Vector<String>>>
{
	ClassificationFragment mFragment;

	void setFragment(ClassificationFragment fragment)
	{
		mFragment = fragment;
	}

	@Override
	protected Vector<Vector<String>> doInBackground(String... urls)
	{
		Document document = null;
		Vector<Vector<String>> responseClassification = null;

		try {
			document = Jsoup.parse(new URL(urls[0]).openStream(), "utf-16LE", urls[0]);
			responseClassification = extractClassificationTable(document);
		} catch (IOException e) {
			Log.d("-kk-ExtractClassificationTask","exception");
			e.printStackTrace();
		}

		return responseClassification;
	}
	
	private Vector<Vector<String>> extractClassificationTable(Document doc){
		Log.d("-kk-ExtractClassificationTask", "extractClassificationTable");
		Vector<Vector<String>> responseClassification = new Vector<Vector<String>>();
		
		Elements tables = doc.select("table");
		Element table = tables.get(1);//"1" indicates the index of the table we want
		Elements rows = table.select("tr");

		for (Element row: rows){
			Vector<String> tempRow = new Vector<String>();
			Elements colums = row.select("td");
			for (Element column: colums){
				tempRow.add(column.text()) ;
			}
			responseClassification.add(tempRow);
		}

		return responseClassification;
	}
	
	@Override
	protected void onPostExecute(Vector<Vector<String>> classificationTable)
	{
		if (mFragment == null)
			return;
		Intent data = new Intent();
		Log.d("-kk-ExtractClassificationTask", "put serialized: "+"com.whatafabric.clubestorilII.classificationTable-id-"+mFragment.getTaskClassificationFragmentId());
		data.putExtra("com.whatafabric.clubestorilII.classificationTable-id-"+mFragment.getTaskClassificationFragmentId(),(Serializable)classificationTable);
		mFragment.onActivityResult(mFragment.getTaskClassificationFragmentId(), Activity.RESULT_OK,data);
	}
}