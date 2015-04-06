package com.personal.expenseapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EntryRowAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<EntryRow> entries;

	public EntryRowAdapter(Context ctx, ArrayList<EntryRow> entries) {
		this.context = ctx;
		this.entries = entries;
	}

	@Override
	public int getCount() {
		return entries.size();
	}

	@Override
	public Object getItem(int position) {
		return entries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.entry_row, null);
        }
		TextView Date = (TextView)convertView.findViewById(R.id.entry_row_date);
		TextView Desc = (TextView)convertView.findViewById(R.id.entry_row_description);
		TextView Amount = (TextView)convertView.findViewById(R.id.entry_row_total_amount);
		TextView Payee = (TextView)convertView.findViewById(R.id.entry_row_paid_by);
		TextView Div = (TextView)convertView.findViewById(R.id.entry_row_div_among);
		
		Date.setText(this.entries.get(position).getDate());
		Desc.setText(this.entries.get(position).getDesc());
		Amount.setText(this.entries.get(position).getAmount());
		Payee.setText(this.entries.get(position).getPayee());
		Div.setText(this.entries.get(position).getDiv());
		
		
		return convertView;
	}

}
