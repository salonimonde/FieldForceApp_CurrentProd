package com.fieldforce.db;

import android.content.UriMatcher;
import android.net.Uri;

import com.fieldforce.db.tables.AddProofTable;
import com.fieldforce.db.tables.AllJobCardTable;
import com.fieldforce.db.tables.AreaTable;
import com.fieldforce.db.tables.AssetJobCardTable;
import com.fieldforce.db.tables.BankTable;
import com.fieldforce.db.tables.BreakDownJobCardTable;
import com.fieldforce.db.tables.CategoryTable;
import com.fieldforce.db.tables.CommissionJobCardTable;
import com.fieldforce.db.tables.ComplaintJobCardTable;
import com.fieldforce.db.tables.ConsumerEnquiryTable;
import com.fieldforce.db.tables.ConversionJobCardTable;
import com.fieldforce.db.tables.DecommissionJobCardTable;
import com.fieldforce.db.tables.IdProofTable;
import com.fieldforce.db.tables.LandmarkTable;
import com.fieldforce.db.tables.LocationTable;
import com.fieldforce.db.tables.LoginTable;
import com.fieldforce.db.tables.MeterInstalltionJobCardTable;
import com.fieldforce.db.tables.NotificationTable;
import com.fieldforce.db.tables.PaymentTable;
import com.fieldforce.db.tables.Pincode;
import com.fieldforce.db.tables.PreventiveJobCardTable;
import com.fieldforce.db.tables.RegistrationTable;
import com.fieldforce.db.tables.RejectedJobCardTable;
import com.fieldforce.db.tables.ServiceJobCardTable;
import com.fieldforce.db.tables.SiteVerificationJobCardTable;
import com.fieldforce.db.tables.SubCategoryTable;
import com.fieldforce.db.tables.WardTable;


/**
 * This class contains description about
 * application database content providers
 *
 * @author Bynry01
 */
public class ContentDescriptor
{

    public static final String AUTHORITY = "com.fieldforce";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final UriMatcher URI_MATCHER = buildUriMatcher();


    /**
     * @return UriMatcher for database table Uris
     */
    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, LoginTable.PATH, LoginTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, NotificationTable.PATH, NotificationTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, CommissionJobCardTable.PATH, CommissionJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, DecommissionJobCardTable.PATH, DecommissionJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, PreventiveJobCardTable.PATH, PreventiveJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, BreakDownJobCardTable.PATH, BreakDownJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, SiteVerificationJobCardTable.PATH, SiteVerificationJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, MeterInstalltionJobCardTable.PATH, MeterInstalltionJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, ComplaintJobCardTable.PATH, ComplaintJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, AssetJobCardTable.PATH, AssetJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, ServiceJobCardTable.PATH, ServiceJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, ConsumerEnquiryTable.PATH, ConsumerEnquiryTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, ConversionJobCardTable.PATH, ConversionJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, AllJobCardTable.PATH, AllJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, RegistrationTable.PATH, RegistrationTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, RejectedJobCardTable.PATH, RejectedJobCardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, AreaTable.PATH, AreaTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, BankTable.PATH, BankTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, PaymentTable.PATH, PaymentTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, IdProofTable.PATH, IdProofTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, AddProofTable.PATH, AddProofTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, WardTable.PATH, WardTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, CategoryTable.PATH, CategoryTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, SubCategoryTable.PATH, SubCategoryTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, Pincode.PATH, Pincode.PATH_TOKEN);
        matcher.addURI(AUTHORITY, LocationTable.PATH, LocationTable.PATH_TOKEN);
        matcher.addURI(AUTHORITY, LandmarkTable.PATH, LandmarkTable.PATH_TOKEN);

        return matcher;
    }
}