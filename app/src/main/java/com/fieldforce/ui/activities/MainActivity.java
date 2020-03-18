package com.fieldforce.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.db.DatabaseManager;
import com.fieldforce.models.TodayModel;
import com.fieldforce.ui.adapters.ViewPagerAdapter;
import com.fieldforce.ui.fragments.BottomSheetFragment;
import com.fieldforce.ui.fragments.HistoryFragment;
import com.fieldforce.ui.fragments.TodayFragment;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;
import com.fieldforce.utility.CustomDialog;

import java.util.ArrayList;

public class MainActivity extends ParentActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    public static Activity activity;
    private int menuViewShowing = 0;
    private String userId;
    public static BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Typeface mRegularBold, mRegularItalic, mRegular;
    private TextView txtMenuHeading;
    private Button txtNotify;
    public static int filterType = 0, fragmentSelected = 0;
    private TodayModel todayModel = new TodayModel();
    private Menu menu;
    boolean click = true;
    private SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        activity = this;

        mRegularBold = App.getMontserratMediumFont();
        mRegularItalic = App.getMontserratBoldFont();
        mRegular = App.getMontserratRegularFont();

        txtMenuHeading = findViewById(R.id.txt_menu_heading);
        txtNotify = findViewById(R.id.txt_notify);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageView imgModule = findViewById(R.id.img_module);
        imgModule.setImageResource(R.drawable.ic_action_default_user_icon);
        /*Picasso.get()
                .load("http://68.183.84.255/media/images/METER_CONVERSION_IMAGE_PATH/conversion_assignment_id_2_1548173796411.jpg")
                .resize(50, 50)
                .centerCrop()
                .into(imgModule);*/

        TextView title = findViewById(R.id.txt_title);
        title.setText(AppPreferences.getInstance(mContext).getString(AppConstants.NAME, AppConstants.BLANK_STRING));
        TextView txtSubTitle = findViewById(R.id.txt_sub_title);
        txtSubTitle.setText("ID : #" + AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING));

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, "");

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        menuViewShowing = Integer.parseInt(AppPreferences.getInstance(mContext).getString(AppConstants.SCREEN_NO, AppConstants.BLANK_STRING));

//        setMainMenus();

        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        setValues();

    }

    private void setupViewPager(final ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TodayFragment(), getString(R.string.today));
        adapter.addFragment(new HistoryFragment(), getString(R.string.history));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                    fragmentSelected = 0;
                } else if (i == 1) {
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    fragmentSelected = 1;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void setMainMenus() {
        ArrayList<Integer> arrayListCounts;
        arrayListCounts = new ArrayList<>();
        int countEnquiry = DatabaseManager.getEnquiryJobCardCount(userId, AppConstants.CARD_STATUS_OPEN);
        int countComplaints = DatabaseManager.getComplaintTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
        int countServices = DatabaseManager.getServiceTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
        int countPreventive = DatabaseManager.getPreventiveTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
        int countBreakDown = DatabaseManager.getBreakDownTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
        int countSiteVerification = DatabaseManager.getSiteVerificationTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
        int countCommissioning = DatabaseManager.getCommissionTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
        int countDecommissioning = DatabaseManager.getDISCTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
        int countMeterInstall = DatabaseManager.getMeterInstallTodayCount(userId, AppConstants.CARD_STATUS_OPEN);

        int countAssetIndexing = DatabaseManager.getAssetJobCardCount(userId, AppConstants.CARD_STATUS_OPEN);
        int countConvert = DatabaseManager.getConversionJobCardCount(userId, AppConstants.CARD_STATUS_OPEN);

//        int countRecovery = DatabaseManager.getAssetJobCardCount(userId, AppConstants.CARD_STATUS_OPEN);
        int countRecovery = 0;

        arrayListCounts.add(countEnquiry);
        arrayListCounts.add(countSiteVerification);
        arrayListCounts.add(countMeterInstall);
        arrayListCounts.add(countConvert);
        arrayListCounts.add(countServices);
        arrayListCounts.add(countComplaints);
        arrayListCounts.add(countAssetIndexing);
        arrayListCounts.add(countPreventive);
        arrayListCounts.add(countBreakDown);
        arrayListCounts.add(countCommissioning);
        arrayListCounts.add(countDecommissioning);
        arrayListCounts.add(countRecovery);

//        final HorizontalScrollView horizontalScrollView = findViewById(R.id.hsv);
        /*final LinearLayout mLinearLayout = findViewById(R.id.linear_layout_menus);
        mLinearLayout.removeAllViews();
        int[] menuImages = new int[]{R.drawable.ic_action_asset_indexing, R.drawable.ic_action_site_verification,
                R.drawable.ic_action_meter_installation, R.drawable.ic_action_convert, R.drawable.ic_action_services,
                R.drawable.ic_action_complaints, R.drawable.ic_action_preventive, R.drawable.ic_action_breakdown,
                R.drawable.ic_action_commissioning, R.drawable.ic_action_decommissioning, R.drawable.ic_action_all};
        int[] menuNames = new int[]{R.string.asset_indexing, R.string.site_verification, R.string.installation,
                R.string.convert, R.string.services, R.string.complaint, R.string.preventive, R.string.breakdown,
                R.string.commissioning, R.string.decommissioning, R.string.recovery};
        int[] menuColor = new int[]{R.color.colorMenuCream, R.color.colorMenuBlue, R.color.colorMenuGrey, R.color.colorMenuOrange,
                R.color.colorMenuCream, R.color.colorMenuBlue, R.color.colorMenuGrey, R.color.colorMenuOrange,
                R.color.colorMenuCream, R.color.colorMenuBlue, R.color.colorMenuGrey};*/


        int[] menuImages = new int[]{R.drawable.ic_action_all, R.drawable.ic_action_site_verification,
                R.drawable.ic_action_meter_installation, R.drawable.ic_action_convert, R.drawable.ic_action_services,
                R.drawable.ic_action_complaints};

        int[] menuNames = new int[]{R.string.enquiry, R.string.site_verification, R.string.installation,
                R.string.convert, R.string.services, R.string.complaint};

        int[] menuColor = new int[]{R.color.colorMenuCream, R.color.colorMenuBlue, R.color.colorMenuGrey,
                R.color.colorMenuOrange, R.color.colorMenuCream, R.color.colorMenuBlue,};

        for (int i = 0; i < menuNames.length; i++) {

            final View menuView = LayoutInflater.from(getApplication()).inflate(R.layout.cell_main_menus, null);


           /* ImageView imgMenus = findViewById(R.id.img_menu);
            imgMenus.setImageResource(menuImages[i]);*/


            ((GradientDrawable) txtNotify.getBackground()).setColor(CommonUtility.getColor(mContext, menuColor[i]));
            txtNotify.setText("" + arrayListCounts.get(i));

            ImageView imgDownArrow = findViewById(R.id.img_down_arrow);

            if (menuViewShowing == i) {
                imgDownArrow.setVisibility(View.VISIBLE);
            } else {
                imgDownArrow.setVisibility(View.GONE);
            }

            LinearLayout linearLayout = menuView.findViewById(R.id.linear_layout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int showingViewTag = (int) menuView.getTag();
                   /* if (mLinearLayout.getChildCount() > 0)
                        mLinearLayout.removeAllViews();
                    if (menuViewShowing < showingViewTag)
                        horizontalScrollView.scrollTo(horizontalScrollView.getScrollX() + 150, horizontalScrollView.getScrollY());
                    else if (menuViewShowing > showingViewTag)
                        horizontalScrollView.scrollTo(horizontalScrollView.getScrollX() - 150, horizontalScrollView.getScrollY());
                    else
                        horizontalScrollView.scrollTo(horizontalScrollView.getScrollX(), horizontalScrollView.getScrollY());*/


                    menuViewShowing = showingViewTag;
//                    setMainMenus();
//                    setRecycler();
                }
            });

//            mLinearLayout.addView(menuView);
            menuView.setTag(i);
//            setRecycler();
        }
    }

    private void setRecycler() {
        if (menuViewShowing == 0)
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.enquiry));
        else if (menuViewShowing == 1)
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.site_verification));
        else if (menuViewShowing == 2)
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.installation));
        else if (menuViewShowing == 3)
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.convert));
        else if (menuViewShowing == 4)
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.services));
        else if (menuViewShowing == 5)
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.complaint));
        else if (menuViewShowing == 6)
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.preventive));
        else if (menuViewShowing == 7)
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.breakdown));
        else if (menuViewShowing == 8)
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.commissioning));
        else if (menuViewShowing == 9)
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.decommissioning));
        else if (menuViewShowing == 10)
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.recovery));

        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        setupViewPager(viewPager);
    }


    @Override
    public void onResume() {
        super.onResume();

//        setMainMenus();
        setValues();
        setupViewPager(viewPager);
        if (fragmentSelected == 0)
            viewPager.setCurrentItem(0, true);
        else if (fragmentSelected == 1)
            viewPager.setCurrentItem(1, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setValues();
//        setMainMenus();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        setMainMenus();
        setValues();

    }


    public void showBottomSheetDialogFragment() {

        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.setCancelable(false);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.SELECTED_MODULE, "" + AppPreferences.getInstance(mContext).getString(AppConstants.SCREEN_NO, AppConstants.BLANK_STRING));
        bottomSheetFragment.setArguments(bundle);

    }


    public void setValues() {

        String screenNo = AppPreferences.getInstance(mContext).getString(AppConstants.SCREEN_NO, AppConstants.BLANK_STRING);

        txtMenuHeading.setText(AppPreferences.getInstance(mContext).getString(AppConstants.COMING_FROM, AppConstants.BLANK_STRING));

        if (screenNo.equals("1")) {
            int countEnquiry = DatabaseManager.getEnquiryJobCardCount(userId, AppConstants.CARD_STATUS_OPEN);
            txtNotify.setText(Integer.toString(countEnquiry));
        } else if (screenNo.equals("2")) {
            invalidateOptionsMenu();
            int countSiteVerification = DatabaseManager.getSiteVerificationTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
            txtNotify.setText(Integer.toString(countSiteVerification));
        } else if (screenNo.equals("3")) {
            int countMeterInstall = DatabaseManager.getMeterInstallTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
            txtNotify.setText(Integer.toString(countMeterInstall));
        } else if (screenNo.equals("4")) {
            int countConvert = DatabaseManager.getConversionJobCardCount(userId, AppConstants.CARD_STATUS_OPEN);
            txtNotify.setText(Integer.toString(countConvert));
        } else if (screenNo.equals("5")) {
            int countServices = DatabaseManager.getServiceTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
            txtNotify.setText(Integer.toString(countServices));
        } else if (screenNo.equals("6")) {
            int countComplaints = DatabaseManager.getComplaintTodayCount(userId, AppConstants.CARD_STATUS_OPEN);
            txtNotify.setText(Integer.toString(countComplaints));
        } /*else {
            int countEnquiry = DatabaseManager.getEnquiryJobCardCount(userId, AppConstants.CARD_STATUS_OPEN);
            txtNotify.setText(Integer.toString(countEnquiry));
        }*/
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_open:
                    viewPager.setCurrentItem(0, true);
                    fragmentSelected = 0;
                    return true;
                case R.id.navigation_history:
                    viewPager.setCurrentItem(1, true);
                    fragmentSelected = 1;
                    return true;
                case R.id.navigation_download:
//                    setMainMenus();
                    String screenName = AppPreferences.getInstance(mContext).getString(AppConstants.COMING_FROM, AppConstants.BLANK_STRING);
                    TodayFragment.getTodayCards(screenName);
//                    bottomNavigationView.getMenu().getItem(0).setChecked(true);

//                    setMainMenus();
                    viewPager.setCurrentItem(0, true);
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
//                    setMainMenus();
                    setValues();

                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_search:
                intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra(AppConstants.SELECTED_MODULE, "" + menuViewShowing);
                startActivity(intent);
                return true;
            case R.id.action_notification:
                intent = new Intent(mContext, NotificationActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_filter:
                showBottomSheetDialogFragment();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        CustomDialog customDialog = new CustomDialog((Activity) mContext, getString(R.string.do_you_want_to_close_exit_app_now),
                getString(R.string.main_activity), false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();
        customDialog.setCancelable(false);
    }

    @Override
    public void onRefresh() {
        setupViewPager(viewPager);
        if (fragmentSelected == 0)
            viewPager.setCurrentItem(0, true);
        else if (fragmentSelected == 1)
            viewPager.setCurrentItem(1, true);
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}