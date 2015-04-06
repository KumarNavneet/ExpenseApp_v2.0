package com.personal.expenseapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;

/**
 * Main activity
 */
public class MainActivity extends SalesforceActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mDrawerItemTitles;

	private static DatePicker dp;
	private static CalendarView calendar;
	private static Button okButton;
	private static Button resetButton;
	private static RadioButton radioButton;
	private static RadioGroup radioGroup;
	private static EditText details = null;
	private static EditText amount = null;
	private static int year;
	private static int month;
	private static int day;
	public static RestClient client;
	public static final Calendar c = Calendar.getInstance();
	private static String objectType = "";
	private static String objectType2 = "";
	private static String apiVersion;
	private static CheckBox checkBox1;
	private static CheckBox checkBox2;
	private static CheckBox checkBox3;
	private static CheckBox checkBox4;
	private static CheckBox checkBox5;
	private static String toastMessgae = "";
	private static String PaidBy;
	private static boolean readytoStore = false;
	private static ProgressDialog progress;
	private InputMethodManager imm;
	private EntryRowAdapter entriesAdapter;
	public static ArrayList<EntryRow> RowEntries;
	private static Typeface tf = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		
		tf = Typeface.SERIF;

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mTitle = mDrawerTitle = getTitle();
		mDrawerItemTitles = getResources().getStringArray(
				R.array.nav_drawer_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDrawerItemTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

		apiVersion = getString(R.string.api_version);

		progress = new ProgressDialog(this);

		progress.setCancelable(true);
		progress.setIndeterminate(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.more_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.recentEntries: {
			progress.setTitle("Fetching Entries...");
			progress.setMessage("Please wait...");
			progress.show();
			try {
				String soql = "SELECT Date__c, Description__c, DivisibleAmong__c, PaidBy__c, TotalAmount__c, ID FROM MainTable__c";
				final RestRequest restRequest = RestRequest.getRequestForQuery(
						getString(R.string.api_version), soql);
				sendRequest(restRequest, "Retrieve", null, getApplicationContext());
				imm.hideSoftInputFromWindow(details.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(amount.getWindowToken(), 0);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		}

		case R.id.allEntries: {
			progress.setTitle("Fetching Entries...");
			progress.setMessage("Please wait...");
			progress.show();
			try {
				String soql = "SELECT Date__c, Description__c, DivisibleAmong__c, PaidBy__c, TotalAmount__c, ID FROM MainTable__c";
				final RestRequest restRequest = RestRequest.getRequestForQuery(
						getString(R.string.api_version), soql);
				sendRequest(restRequest, "Retrieve", null, getApplicationContext());
				imm.hideSoftInputFromWindow(details.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(amount.getWindowToken(), 0);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		}
		case R.id.myEntries: {
			break;
		}
		}
		return true;
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new FragmentHome();
		Bundle args = new Bundle();
		String frag_to_show = "";
		if (position == 0)
			frag_to_show = "home";
		else if (position == 1)
			frag_to_show = "recent";
		else if (position == 2)
			frag_to_show = "all";
		else if (position == 3)
			frag_to_show = "my_recent";
		else
			frag_to_show = "my_all";

		args.putString(FragmentHome.ARG_FRAGMENT_NAME, frag_to_show);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerItemTitles[position].toUpperCase());
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class FragmentHome extends Fragment {
		public static final String ARG_FRAGMENT_NAME = "fragment_name";

		public FragmentHome() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView;
			String frag_to_show = getArguments().getString(ARG_FRAGMENT_NAME);

			if (frag_to_show.equals("home"))
				rootView = inflater.inflate(R.layout.main_activity, container,
						false);
			else
				rootView = inflater.inflate(R.layout.entries_table, container,
						false);

			if (frag_to_show.equals("home")) {

				dp = (DatePicker) rootView.findViewById(R.id.datePicker1);
				okButton = (Button) rootView.findViewById(R.id.button1);
				resetButton = (Button) rootView.findViewById(R.id.button2);
				details = (EditText) rootView.findViewById(R.id.editText1);
				amount = (EditText) rootView.findViewById(R.id.editText2);
				radioGroup = (RadioGroup) rootView
						.findViewById(R.id.radioGroup);
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
				dp.setCalendarViewShown(false);
				dp.init(year, month, day, null);
				checkBox1 = (CheckBox) rootView.findViewById(R.id.checkBox1);
				checkBox2 = (CheckBox) rootView.findViewById(R.id.checkBox2);
				checkBox3 = (CheckBox) rootView.findViewById(R.id.checkBox3);
				checkBox4 = (CheckBox) rootView.findViewById(R.id.checkBox4);
				checkBox5 = (CheckBox) rootView.findViewById(R.id.checkBox5);
				resetButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						resetUI();
					}
				});
				okButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						progress.setTitle("Submitting Entry...");
						progress.setMessage("Please wait...");
						objectType = "MainTable__c";
						objectType2 = "ExpenseTable__c";
						toastMessgae = "Please ";
						Date d = new Date(dp.getYear() - 1900, dp.getMonth(),
								dp.getDayOfMonth());
						Editable detail = details.getText();
						Editable amt = amount.getText();

						if (radioGroup.getCheckedRadioButtonId() != -1) {
							radioButton = (RadioButton) rootView
									.findViewById(radioGroup
											.getCheckedRadioButtonId());
							PaidBy = (String) radioButton.getText();
						} else
							toastMessgae += "select the Payee";
						if (!(checkBox1.isChecked() || checkBox2.isChecked()
								|| checkBox3.isChecked() || checkBox4
								.isChecked())) {
							toastMessgae += " Among who to Divide";

						}
						if (details.getText().length() == 0)
							toastMessgae += " Enter Details ";

						if (amount.getText().length() == 0)
							toastMessgae += " Enter Amount ";

						if (toastMessgae.equals("Please ")) {
							readytoStore = true;
						}
						if (readytoStore) {
							progress.show();
							ArrayList<String> names = new ArrayList<String>();
							if (checkBox1.isChecked())
								names.add(checkBox1.getText().toString());
							if (checkBox2.isChecked())
								names.add(checkBox2.getText().toString());
							if (checkBox3.isChecked())
								names.add(checkBox3.getText().toString());
							if (checkBox4.isChecked())
								names.add(checkBox4.getText().toString());
							if (checkBox5.isChecked())
								names.add(checkBox5.getText().toString());
							int perHeadAmount = (int) Double.parseDouble(amt
									.toString()) / names.size();
							Map<String, Object> fields = new HashMap<String, Object>();
							RestRequest request = null;
							for (String name : names) {
								fields.put("Date__c", d);
								fields.put("Details__c", detail.toString());
								fields.put("ExpenseIncurred__c", perHeadAmount);
								fields.put("Name__c", name);
								try {
									request = RestRequest.getRequestForCreate(
											apiVersion, objectType2, fields);
									sendRequest(request, "Insert", null, getActivity().getApplicationContext());

								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							fields = new HashMap<String, Object>();
							fields.put("TotalAmount__c",
									(int) Double.parseDouble(amt.toString()));
							fields.put("Date__c", d);
							fields.put("Description__c", detail.toString());
							fields.put("PaidBy__c", PaidBy);
							fields.put("DivisibleAmong__c", names.size());
							try {
								request = RestRequest.getRequestForCreate(
										apiVersion, objectType, fields);
								sendRequest(request, "Insert", null, null);
								// String q = "";
								// request2 =
								// RestRequest.getRequestForQuery(apiVersion,
								// q);
							} catch (Exception e) {
								e.printStackTrace();
								return;
							}

						} else
							Toast.makeText(
									getActivity().getApplicationContext(),
									toastMessgae, Toast.LENGTH_LONG).show();
					}
				});

			} else if(frag_to_show.equals("recent")|| frag_to_show.equals("all")) {
				progress.setTitle("Fetching Entries...");
				progress.setMessage("Please wait...");
				progress.show();
				
				try {
					String soql = "SELECT Date__c, Description__c, DivisibleAmong__c, PaidBy__c, TotalAmount__c, ID FROM MainTable__c";
					final RestRequest restRequest = RestRequest
							.getRequestForQuery(
									getString(R.string.api_version), soql);
					sendRequest(restRequest, "Retrieve", rootView, getActivity().getApplicationContext());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			else{
				progress.setTitle("Fetching Entries...");
				progress.setMessage("Please wait...");
				progress.show();
				
				try {
					String soql = "SELECT Date__c, Description__c, DivisibleAmong__c, PaidBy__c, TotalAmount__c, ID FROM MainTable__c WHERE PaidBy__c = 'Navneet'";
					final RestRequest restRequest = RestRequest
							.getRequestForQuery(
									getString(R.string.api_version), soql);
					sendRequest(restRequest, "Retrieve", rootView, getActivity().getApplicationContext());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			getActivity().setTitle(frag_to_show);
			return rootView;
		}
	}

	@Override
	public void onResume(RestClient client) {
		super.onResume();
		this.client = client;
//		imm.hideSoftInputFromWindow(details.getWindowToken(), 0);
//		imm.hideSoftInputFromWindow(amount.getWindowToken(), 0);

		// recentEntries.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// progress.setTitle("Fetching Entries...");
		// progress.setMessage("Please wait...");
		// progress.show();
		// try {
		// String soql =
		// "SELECT Date__c, Description__c, DivisibleAmong__c, PaidBy__c, TotalAmount__c, ID FROM MainTable__c";
		// final RestRequest restRequest = RestRequest
		// .getRequestForQuery(
		// getString(R.string.api_version), soql);
		// sendRequest(restRequest, "Retrieve");
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		//
		// }
		// });

	}

	private static void sendRequest(RestRequest restRequest, final String qt,  final View rootView, final Context ctx)
			throws UnsupportedEncodingException {
		client.sendAsync(restRequest, new AsyncRequestCallback() {
			@Override
			public void onSuccess(RestRequest request, RestResponse result) {
				if (qt.equals("Insert"))
					Log.d("Navneet", "Entry done");
				else if (qt.equals("Retrieve")) {
					RowEntries = new ArrayList<EntryRow>();
					RowEntries.add(new EntryRow("Date", "Desc.", "Amount",
							"Paid By", "Div in"));
					try {
						JSONArray records = result.asJSONObject().getJSONArray(
								"records");
						for (int i = 0; i < records.length(); i++) {
							Log.d("Navneet", records.getJSONObject(i)
									.getString("Date__c"));
							Log.d("Navneet", records.getJSONObject(i)
									.getString("Description__c"));
							Log.d("Navneet", records.getJSONObject(i)
									.getString("PaidBy__c"));
							Log.d("Navneet", records.getJSONObject(i)
									.getString("DivisibleAmong__c"));
							Log.d("Navneet", records.getJSONObject(i)
									.getString("TotalAmount__c"));
							EntryRow entry = new EntryRow(records
									.getJSONObject(i).getString("Date__c"),
									records.getJSONObject(i).getString(
											"Description__c"), records
											.getJSONObject(i).getString(
													"TotalAmount__c"), records
											.getJSONObject(i).getString(
													"PaidBy__c"), records
											.getJSONObject(i).getString(
													"DivisibleAmong__c"));
							TableLayout table = (TableLayout)rootView.findViewById(R.id.tableLayout1);
							TableRow tr = new TableRow(ctx);
							tr.setBackgroundColor(Color.BLACK);
							tr.setPadding(0, 0, 0, 2); //Border between rows

							TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
							llp.setMargins(0, 0, 2, 0);//2px right-margin

							LinearLayout cell;
							TextView tv;
							//New Cell
							cell = new LinearLayout(ctx);
							cell.setBackgroundColor(Color.GRAY);
							cell.setLayoutParams(llp);//2px border on the right for the cell
							tv = new TextView(ctx);
							tv.setTypeface(tf);
							tv.setText(entry.getDate());
							//tv.setPadding(0, 0, 4, 3);
							cell.addView(tv);
							tr.addView(cell);
							
							cell = new LinearLayout(ctx);
							cell.setBackgroundColor(Color.GRAY);
							cell.setLayoutParams(llp);//2px border on the right for the cell
							tv = new TextView(ctx);
							tv.setText(entry.getDesc());
							//tv.setPadding(0, 0, 4, 3);
							cell.addView(tv);
							tr.addView(cell);
							
							cell = new LinearLayout(ctx);
							cell.setBackgroundColor(Color.GRAY);
							cell.setLayoutParams(llp);//2px border on the right for the cell
							tv = new TextView(ctx);
							tv.setText(entry.getAmount());
							//tv.setPadding(0, 0, 4, 3);
							cell.addView(tv);
							tr.addView(cell);
							
							cell = new LinearLayout(ctx);
							cell.setBackgroundColor(Color.GRAY);
							cell.setLayoutParams(llp);//2px border on the right for the cell
							tv = new TextView(ctx);
							tv.setText(entry.getPayee());
							//tv.setPadding(0, 0, 4, 3);
							cell.addView(tv);
							tr.addView(cell);
							
							cell = new LinearLayout(ctx);
							cell.setBackgroundColor(Color.GRAY);
							cell.setLayoutParams(llp);//2px border on the right for the cell
							tv = new TextView(ctx);
							tv.setText(entry.getDiv());
							//tv.setPadding(0, 0, 4, 3);
							cell.addView(tv);
							tr.addView(cell);
							
							table.addView(tr);
						}

					} catch (ParseException | JSONException | IOException e) {
						e.printStackTrace();
					}
				}
				resetUI();
				progress.dismiss();
			}

			@Override
			public void onError(Exception exception) {
//				 Toast.makeText(
//				 ctx,
//				 MainActivity.this.getString(SalesforceSDKManager
//				 .getInstance().getSalesforceR()
//				 .stringGenericError(), exception.toString()),
//				 Toast.LENGTH_LONG).show();
				progress.dismiss();
				resetUI();
			}
		});
	}

	private static void resetUI() {
		details.setText("");
		amount.setText("");
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		// dp.setCalendarViewShown(false);
		dp.init(year, month, day, null);
		radioGroup.clearCheck();
		if (checkBox1.isChecked()) {
			checkBox1.setChecked(false);
		}
		if (checkBox2.isChecked()) {
			checkBox2.setChecked(false);
		}
		if (checkBox3.isChecked()) {
			checkBox3.setChecked(false);
		}
		if (checkBox4.isChecked()) {
			checkBox4.setChecked(false);
		}
		if (checkBox5.isChecked()) {
			checkBox5.setChecked(false);
		}
		hideKeyboard();
		details.setFocusable(false);
		amount.setFocusable(false);
	}

	private static void hideKeyboard() {
		// final static InputMethodManager imm = (InputMethodManager)
		// MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(details.getWindowToken(), 0);
		// imm.hideSoftInputFromWindow(amount.getWindowToken(), 0);
	}

}
