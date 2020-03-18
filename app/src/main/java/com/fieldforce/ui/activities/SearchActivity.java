package com.fieldforce.ui.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.db.DatabaseManager;
import com.fieldforce.models.HistoryModel;
import com.fieldforce.models.RegistrationModel;
import com.fieldforce.models.TodayModel;
import com.fieldforce.ui.adapters.SearchAdapter;
import com.fieldforce.ui.adapters.SearchHistoryAdapter;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;

import java.util.ArrayList;

public class SearchActivity extends ParentActivity implements View.OnClickListener {

    private Context mContext;
    private SearchView searchView;
    private ImageView imgBack;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<TodayModel> searchList = new ArrayList<>();
    private ArrayList<HistoryModel> searchHistoryList = new ArrayList<>();
    private ArrayList<RegistrationModel> searchListRejected = new ArrayList<>();
    private String searchQuery = "", userId = "", selectedModule, userType;
    private TextView txtSearchResult, txtSearchCriteria;
    private Typeface mRegularBold, mRegularItalic, mRegular;
    private SearchAdapter searchAdapter = null;
    private SearchHistoryAdapter searchHistoryAdapter = null;
    private Spinner spinnerModule;
    private int module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mContext = this;

        mRegularBold = App.getMontserratMediumFont();
        mRegularItalic = App.getMontserratBoldFont();
        mRegular = App.getMontserratRegularFont();

        userType = AppPreferences.getInstance(mContext).getString(AppConstants.USER_TYPE, AppConstants.BLANK_STRING);

        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);
        txtSearchResult = findViewById(R.id.txt_search_result);
        txtSearchCriteria = findViewById(R.id.txt_search_text);
        txtSearchCriteria.setTypeface(mRegular);
        txtSearchResult.setTypeface(mRegular);
        spinnerModule = findViewById(R.id.spinner_module);

        recyclerView = findViewById(R.id.recycler_view_search);
        layoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        if (intent != null) {
            selectedModule = intent.getStringExtra(AppConstants.SELECTED_MODULE);
            if (Integer.parseInt(selectedModule) == 0) {
                module = 0;
            } else {
                module = Integer.parseInt(selectedModule) - 1;
            }

        }

        setSpinner();
    }

    private void setSpinner() {


        int[] menuNames = new int[]{R.string.enquiry, R.string.site_verification, R.string.installation,
                R.string.convert, R.string.services, R.string.complaint, R.string.rejected_registrations};

        final int[] menuColor = new int[]{R.color.colorMenuCream, R.color.colorMenuBlue, R.color.colorMenuGrey,
                R.color.colorMenuOrange, R.color.colorMenuCream, R.color.colorMenuBlue, R.color.colorMenuGrey};

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < menuNames.length; i++) {
            arrayList.add(getString(menuNames[i]));
        }
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, arrayList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, menuColor[position]));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, menuColor[position]));
                return v;
            }
        };
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModule.setAdapter(dataAdapter3);
        spinnerModule.setSelection(module);
        spinnerModule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                invalidateOptionsMenu();
                resetRecycler(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.CENTER));
        EditText searchEditText = searchView.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorBlack));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorTextFaint));
        LinearLayout searchEditFrame = searchView.findViewById(R.id.search_edit_frame);
        ((LinearLayout.LayoutParams) searchEditFrame.getLayoutParams()).topMargin = 15;
        ImageView searchClose = searchView.findViewById(R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_action_cross);

        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));
        searchView.onActionViewExpanded();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setQuery(searchQuery, false);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(final String newText) {
                searchAdapter = null;
                searchList.clear();
                String screenName = spinnerModule.getSelectedItem().toString();
                if (newText.length() > 1 && !newText.contains("%") && !newText.contains("_")) {
                    if (MainActivity.fragmentSelected == 0) {
                        if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.enquiry))) {
                            if (newText.length() > 1) {
                                searchList = DatabaseManager.getEnquirySearchedCards(userId, AppConstants.CARD_STATUS_OPEN, newText);
                                if ((searchList.size() > 0)) {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, screenName, searchList);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, screenName);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.VISIBLE);
                                }
                            }
                        } else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.installation))) {

                            if (newText.length() > 1) {
                                searchList = DatabaseManager.getMeterInstallSearchedCards(userId, AppConstants.CARD_STATUS_OPEN, newText);
                                if ((searchList.size() > 0)) {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, screenName, searchList);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        }
                        else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.site_verification))) {

                            if (newText.length() > 1) {
                                searchList = DatabaseManager.getSiteVerificationSearchedCards(userId, AppConstants.CARD_STATUS_OPEN, newText);
                                if ((searchList.size() > 0)) {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, screenName, searchList);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        }else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.convert))) {

                            if (newText.length() > 1) {
                                searchList = DatabaseManager.getConversionSearchedCards(userId, AppConstants.CARD_STATUS_OPEN, newText);
                                if ((searchList.size() > 0)) {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, screenName, searchList);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        } else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.services))) {

                            if (newText.length() > 1) {
                                searchList = DatabaseManager.getServiceSearchedCards(userId, AppConstants.CARD_STATUS_OPEN, newText);
                                if ((searchList.size() > 0)) {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, screenName, searchList);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        } else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.complaint))) {

                            if (newText.length() > 1) {
                                searchList = DatabaseManager.getComplaintsSearchedCards(userId, AppConstants.CARD_STATUS_OPEN, newText);
                                if ((searchList.size() > 0)) {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, screenName, searchList);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }

                            }
                        }
                        else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.rejected_registrations))) {


                            if (newText.length() > 1) {
                                searchListRejected = DatabaseManager.getRejectedSearchedCards(userId, AppConstants.CARD_STATUS_OPEN, newText);
                                if ((searchListRejected.size() > 0)) {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, searchListRejected, screenName);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        }else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.commissioning))) {
                            searchAdapter = null;
                            searchList.clear();
                            if (newText.length() > 1) {
                                searchList = DatabaseManager.getNSCSearchedCards(userId, AppConstants.CARD_STATUS_OPEN, newText);
                                if ((searchList.size() > 0)) {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, screenName, searchList);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        } else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.decommissioning))) {

                            if (newText.length() > 1) {
                                searchList = DatabaseManager.getDISCSearchedCards(userId, AppConstants.CARD_STATUS_OPEN, newText);
                                if ((searchList.size() > 0)) {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, screenName, searchList);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        } else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.preventive))) {

                            if (newText.length() > 1) {
                                searchList = DatabaseManager.getPreventiveSearchedCards(userId, AppConstants.CARD_STATUS_OPEN, newText);
                                if ((searchList.size() > 0)) {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, screenName, searchList);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        } else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.breakdown))) {

                            if (newText.length() > 1) {
                                searchList = DatabaseManager.getBreakdownSearchedCards(userId, AppConstants.CARD_STATUS_OPEN, newText);
                                if ((searchList.size() > 0)) {
                                    searchAdapter = null;
                                    searchAdapter = new SearchAdapter(mContext, screenName, searchList);
                                    recyclerView.setAdapter(searchAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }

                        }

                    } else {

                        if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.enquiry))) {
                            if (newText.length() > 1) {
                                searchHistoryList = DatabaseManager.getEnquiryHistorySearchedCards(userId, AppConstants.CARD_STATUS_CLOSED, newText);
                                if ((searchHistoryList.size() > 0)) {
                                    searchHistoryAdapter = null;
                                    searchHistoryAdapter = new SearchHistoryAdapter(mContext, screenName, searchHistoryList);
                                    recyclerView.setAdapter(searchHistoryAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    searchHistoryAdapter = null;
                                    searchHistoryAdapter = new SearchHistoryAdapter(mContext, screenName);
                                    recyclerView.setAdapter(searchHistoryAdapter);
                                    txtSearchResult.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.site_verification))) {
                            if (newText.length() > 1) {

                                searchHistoryList = DatabaseManager.getSiteVerificationHistorySearchedCards(userId, AppConstants.CARD_STATUS_CLOSED, newText);
                                if ((searchHistoryList.size() > 0)) {
                                    searchHistoryAdapter = null;
                                    searchHistoryAdapter = new SearchHistoryAdapter(mContext, screenName, searchHistoryList);
                                    recyclerView.setAdapter(searchHistoryAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        } else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.installation))) {

                            if (newText.length() > 1) {
                                searchHistoryList = DatabaseManager.getMeterInstallHistorySearchedCards(userId, AppConstants.CARD_STATUS_CLOSED, newText);
                                if ((searchHistoryList.size() > 0)) {
                                    searchHistoryAdapter = null;
                                    searchHistoryAdapter = new SearchHistoryAdapter(mContext, screenName, searchHistoryList);
                                    recyclerView.setAdapter(searchHistoryAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        } else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.convert))) {

                            if (newText.length() > 1) {
                                searchHistoryList = DatabaseManager.getConversionHistorySearchedCards(userId, AppConstants.CARD_STATUS_CLOSED, newText);
                                if ((searchHistoryList.size() > 0)) {
                                    searchHistoryAdapter = null;
                                    searchHistoryAdapter = new SearchHistoryAdapter(mContext, screenName, searchHistoryList);
                                    recyclerView.setAdapter(searchHistoryAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        } else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.services))) {

                            if (newText.length() > 1) {
                                searchHistoryList = DatabaseManager.getServiceHistorySearchedCards(userId, AppConstants.CARD_STATUS_CLOSED, newText);
                                if ((searchHistoryList.size() > 0)) {
                                    searchHistoryAdapter = null;
                                    searchHistoryAdapter = new SearchHistoryAdapter(mContext, screenName, searchHistoryList);
                                    recyclerView.setAdapter(searchHistoryAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        } else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.complaint))) {

                            if (newText.length() > 1) {
                                searchHistoryList = DatabaseManager.getComplaintsHistorySearchedCards(userId, AppConstants.CARD_STATUS_CLOSED, newText);
                                if ((searchHistoryList.size() > 0)) {
                                    searchHistoryAdapter = null;
                                    searchHistoryAdapter = new SearchHistoryAdapter(mContext, screenName, searchHistoryList);
                                    recyclerView.setAdapter(searchHistoryAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        }
                        else if (spinnerModule.getSelectedItem().equals(CommonUtility.getString(mContext, R.string.rejected_registrations))) {

                            if (newText.length() > 1) {
                                searchHistoryList = DatabaseManager.getRejectedHistorySearchedCards(userId, AppConstants.CARD_STATUS_CLOSED, newText);
                                if ((searchHistoryList.size() > 0)) {
                                    searchHistoryAdapter = null;
                                    searchHistoryAdapter = new SearchHistoryAdapter(mContext, screenName, searchHistoryList);
                                    recyclerView.setAdapter(searchHistoryAdapter);
                                    txtSearchResult.setVisibility(View.GONE);
                                } else {
                                    resetRecycler(true);
                                }
                            }
                        }
                    }


                } else {
                    resetRecycler(true);
                }

                if (newText.length() == 0) {
                    resetRecycler(false);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

        });
        return true;

    }

    private void resetRecycler(boolean isVisible) {
        searchAdapter = null;
        searchAdapter = new SearchAdapter(mContext, spinnerModule.getSelectedItem().toString());
        recyclerView.setAdapter(searchAdapter);

        if (isVisible)
            txtSearchResult.setVisibility(View.VISIBLE);
        else
            txtSearchResult.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v == imgBack) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        resetRecycler(false);
    }
}