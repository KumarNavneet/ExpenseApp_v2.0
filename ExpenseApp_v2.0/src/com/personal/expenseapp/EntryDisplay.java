package com.personal.expenseapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class EntryDisplay extends Activity{
	private ListView entries;
	private EntryRowAdapter entriesAdapter;
	//private ArrayList<EntryRow> RowEntries;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_display);
		entries = (ListView) findViewById(R.id.RecentEntriesList);
		Bundle extras = getIntent().getExtras();
		//RowEntries = (ArrayList<EntryRow>)extras.getParcelable("data");
		entriesAdapter = new EntryRowAdapter(this, MainActivity.RowEntries);
		entries.setAdapter(entriesAdapter);
		//MainActivity.RowEntries.add(new EntryRow("abc","abc","abc", "bcd","bcd"));
		entriesAdapter.notifyDataSetChanged();
	}
	
}
