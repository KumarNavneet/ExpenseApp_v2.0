package com.personal.expenseapp;

import org.w3c.dom.Text;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

public class EntryRow implements Parcelable{
	private String entry_date;
	private String entry_desc;
	private String entry_amount;
	private String entry_payee;
	private String entry_div;
	public EntryRow(){
		
	}
	public EntryRow(Parcel in){
		this.entry_desc = in.readString();
		this.entry_amount = in.readString();
		this.entry_date = in.readString();
		this.entry_div = in.readString();
		this.entry_payee = in.readString();
		
	}
	public EntryRow(String date, String dc, String amount, String payee, String div){
		this.entry_amount = amount;
		this.entry_date = date;
		this.entry_desc = dc;
		this.entry_div = div;
		this.entry_payee = payee;
	}
	public void setDesc(String desc){
		this.entry_desc = desc;
	}
	public void setDate(String date){
		this.entry_date = date;
	}
	public void setAmount(String amount){
		this.entry_amount = amount;
	}
	public void setPayee(String payee){
		this.entry_payee = payee;
	}
	public void setDiv(String div){
		this.entry_div = div;
	}
	public String getDesc(){
		return this.entry_desc;
	}
	public String getDate(){
		return this.entry_date;
	}
	public String getAmount(){
		return this.entry_amount;
	}
	public String getPayee(){
		return this.entry_payee;
	}
	public String getDiv(){
		return this.entry_div;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(entry_desc);
		dest.writeString(entry_amount);
		dest.writeString(entry_date);
		dest.writeString(entry_div);
		dest.writeString(entry_payee);
	}
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public EntryRow createFromParcel(Parcel source) {
			return new EntryRow(source);
		}

		@Override
		public EntryRow[] newArray(int size) {
			return new EntryRow[size];
		}
		
	};

}
