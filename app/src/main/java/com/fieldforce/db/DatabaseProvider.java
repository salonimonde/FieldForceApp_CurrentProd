package com.fieldforce.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.fieldforce.db.tables.AddProofTable;
import com.fieldforce.db.tables.AllJobCardTable;
import com.fieldforce.db.tables.AreaTable;
import com.fieldforce.db.tables.AssetJobCardTable;
import com.fieldforce.db.tables.BankTable;
import com.fieldforce.db.tables.BreakDownJobCardTable;
import com.fieldforce.db.tables.CategoryTable;
import com.fieldforce.db.tables.ComplaintJobCardTable;
import com.fieldforce.db.tables.ConsumerEnquiryTable;
import com.fieldforce.db.tables.ConversionJobCardTable;
import com.fieldforce.db.tables.DecommissionJobCardTable;
import com.fieldforce.db.tables.IdProofTable;
import com.fieldforce.db.tables.LandmarkTable;
import com.fieldforce.db.tables.LocationTable;
import com.fieldforce.db.tables.LoginTable;
import com.fieldforce.db.tables.CommissionJobCardTable;
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
 * This class provides Content Provider for application database
 *
 * @author Bynry01
 */
public class DatabaseProvider extends ContentProvider {

    private static final String UNKNOWN_URI = "Unknown URI";

    public static DatabaseHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        dbHelper.getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final int token = ContentDescriptor.URI_MATCHER.match(uri);

        Cursor result = null;

        switch (token) {
            case LoginTable.PATH_TOKEN: {
                result = doQuery(db, uri, LoginTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case CommissionJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, CommissionJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case DecommissionJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, DecommissionJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case PreventiveJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, PreventiveJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case BreakDownJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, BreakDownJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case SiteVerificationJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, SiteVerificationJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case MeterInstalltionJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, MeterInstalltionJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case AssetJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, AssetJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case ComplaintJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, ComplaintJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case ServiceJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, ServiceJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case ConsumerEnquiryTable.PATH_TOKEN: {
                result = doQuery(db, uri, ConsumerEnquiryTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case ConversionJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, ConversionJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case NotificationTable.PATH_TOKEN: {
                result = doQuery(db, uri, NotificationTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case AllJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, AllJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case RegistrationTable.PATH_TOKEN: {
                result = doQuery(db, uri, RegistrationTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case RejectedJobCardTable.PATH_TOKEN: {
                result = doQuery(db, uri, RejectedJobCardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case AreaTable.PATH_TOKEN: {
                result = doQuery(db, uri, AreaTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case BankTable.PATH_TOKEN: {
                result = doQuery(db, uri, BankTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case PaymentTable.PATH_TOKEN: {
                result = doQuery(db, uri, PaymentTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case IdProofTable.PATH_TOKEN: {
                result = doQuery(db, uri, IdProofTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case AddProofTable.PATH_TOKEN: {
                result = doQuery(db, uri, AddProofTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case WardTable.PATH_TOKEN: {
                result = doQuery(db, uri, WardTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case CategoryTable.PATH_TOKEN: {
                result = doQuery(db, uri, CategoryTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case SubCategoryTable.PATH_TOKEN: {
                result = doQuery(db, uri, SubCategoryTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case Pincode.PATH_TOKEN: {
                result = doQuery(db, uri, Pincode.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case LocationTable.PATH_TOKEN: {
                result = doQuery(db, uri, LocationTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case LandmarkTable.PATH_TOKEN: {
                result = doQuery(db, uri, LandmarkTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }

        }

        return result;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        Uri result = null;

        switch (token) {
            case LoginTable.PATH_TOKEN: {
                result = doInsert(db, LoginTable.TABLE_NAME,
                        LoginTable.CONTENT_URI, uri, values);
                break;
            }
            case CommissionJobCardTable.PATH_TOKEN: {
                result = doInsert(db, CommissionJobCardTable.TABLE_NAME,
                        CommissionJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case DecommissionJobCardTable.PATH_TOKEN: {
                result = doInsert(db, DecommissionJobCardTable.TABLE_NAME,
                        DecommissionJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case PreventiveJobCardTable.PATH_TOKEN: {
                result = doInsert(db, PreventiveJobCardTable.TABLE_NAME,
                        PreventiveJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case BreakDownJobCardTable.PATH_TOKEN: {
                result = doInsert(db, BreakDownJobCardTable.TABLE_NAME,
                        BreakDownJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case MeterInstalltionJobCardTable.PATH_TOKEN: {
                result = doInsert(db, MeterInstalltionJobCardTable.TABLE_NAME,
                        MeterInstalltionJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case SiteVerificationJobCardTable.PATH_TOKEN: {
                result = doInsert(db, SiteVerificationJobCardTable.TABLE_NAME,
                        SiteVerificationJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case AssetJobCardTable.PATH_TOKEN: {
                result = doInsert(db, AssetJobCardTable.TABLE_NAME,
                        AssetJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case ComplaintJobCardTable.PATH_TOKEN: {
                result = doInsert(db, ComplaintJobCardTable.TABLE_NAME,
                        ComplaintJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case ServiceJobCardTable.PATH_TOKEN: {
                result = doInsert(db, ServiceJobCardTable.TABLE_NAME,
                        ServiceJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case ConsumerEnquiryTable.PATH_TOKEN: {
                result = doInsert(db, ConsumerEnquiryTable.TABLE_NAME,
                        ConsumerEnquiryTable.CONTENT_URI, uri, values);
                break;
            }
            case ConversionJobCardTable.PATH_TOKEN: {
                result = doInsert(db, ConversionJobCardTable.TABLE_NAME,
                        ConversionJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case NotificationTable.PATH_TOKEN: {
                result = doInsert(db, NotificationTable.TABLE_NAME,
                        NotificationTable.CONTENT_URI, uri, values);
                break;
            }
            case AllJobCardTable.PATH_TOKEN: {
                result = doInsert(db, AllJobCardTable.TABLE_NAME,
                        AllJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case RegistrationTable.PATH_TOKEN: {
                result = doInsert(db, RegistrationTable.TABLE_NAME,
                        RegistrationTable.CONTENT_URI, uri, values);
                break;
            }
            case RejectedJobCardTable.PATH_TOKEN: {
                result = doInsert(db, RejectedJobCardTable.TABLE_NAME,
                        RejectedJobCardTable.CONTENT_URI, uri, values);
                break;
            }
            case AreaTable.PATH_TOKEN: {
                result = doInsert(db, AreaTable.TABLE_NAME,
                        BankTable.CONTENT_URI, uri, values);
                break;
            }
            case BankTable.PATH_TOKEN: {
                result = doInsert(db, BankTable.TABLE_NAME,
                        BankTable.CONTENT_URI, uri, values);
                break;
            }

            case PaymentTable.PATH_TOKEN: {
                result = doInsert(db, PaymentTable.TABLE_NAME,
                        PaymentTable.CONTENT_URI, uri, values);
                break;
            }
            case IdProofTable.PATH_TOKEN: {
                result = doInsert(db, IdProofTable.TABLE_NAME,
                        IdProofTable.CONTENT_URI, uri, values);
                break;
            }
            case AddProofTable.PATH_TOKEN: {
                result = doInsert(db, AddProofTable.TABLE_NAME,
                        AddProofTable.CONTENT_URI, uri, values);
                break;
            }
            case WardTable.PATH_TOKEN: {
                result = doInsert(db, WardTable.TABLE_NAME,
                        WardTable.CONTENT_URI, uri, values);
                break;
            }
            case CategoryTable.PATH_TOKEN: {
                result = doInsert(db, CategoryTable.TABLE_NAME,
                        CategoryTable.CONTENT_URI, uri, values);
                break;
            }
            case SubCategoryTable.PATH_TOKEN: {
                result = doInsert(db, SubCategoryTable.TABLE_NAME,
                        SubCategoryTable.CONTENT_URI, uri, values);
                break;
            }
            case Pincode.PATH_TOKEN: {
                result = doInsert(db, Pincode.TABLE_NAME,
                        Pincode.CONTENT_URI, uri, values);
                break;
            }
            case LocationTable.PATH_TOKEN: {
                result = doInsert(db, LocationTable.TABLE_NAME,
                        LocationTable.CONTENT_URI, uri, values);
                break;
            }
            case LandmarkTable.PATH_TOKEN: {
                result = doInsert(db, LandmarkTable.TABLE_NAME,
                        LandmarkTable.CONTENT_URI, uri, values);
                break;
            }
        }

        if (result == null) {
            throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }

        return result;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        String table = null;
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        switch (token) {
            case LoginTable.PATH_TOKEN: {
                table = LoginTable.TABLE_NAME;
                break;
            }
            case CommissionJobCardTable.PATH_TOKEN: {
                table = CommissionJobCardTable.TABLE_NAME;
                break;
            }
            case DecommissionJobCardTable.PATH_TOKEN: {
                table = DecommissionJobCardTable.TABLE_NAME;
                break;
            }
            case PreventiveJobCardTable.PATH_TOKEN: {
                table = PreventiveJobCardTable.TABLE_NAME;
                break;
            }
            case BreakDownJobCardTable.PATH_TOKEN: {
                table = BreakDownJobCardTable.TABLE_NAME;
                break;
            }
            case SiteVerificationJobCardTable.PATH_TOKEN: {
                table = SiteVerificationJobCardTable.TABLE_NAME;
                break;
            }

            case MeterInstalltionJobCardTable.PATH_TOKEN: {
                table = MeterInstalltionJobCardTable.TABLE_NAME;
                break;
            }
            case AssetJobCardTable.PATH_TOKEN: {
                table = AssetJobCardTable.TABLE_NAME;
                break;
            }
            case ComplaintJobCardTable.PATH_TOKEN: {
                table = ComplaintJobCardTable.TABLE_NAME;
                break;
            }
            case ServiceJobCardTable.PATH_TOKEN: {
                table = ServiceJobCardTable.TABLE_NAME;
                break;
            }
            case ConsumerEnquiryTable.PATH_TOKEN: {
                table = ConsumerEnquiryTable.TABLE_NAME;
                break;
            }
            case ConversionJobCardTable.PATH_TOKEN: {
                table = ConversionJobCardTable.TABLE_NAME;
                break;
            }
            case NotificationTable.PATH_TOKEN: {
                table = NotificationTable.TABLE_NAME;
                break;
            }
            case AllJobCardTable.PATH_TOKEN: {
                table = AllJobCardTable.TABLE_NAME;
                break;
            }
            case RegistrationTable.PATH_TOKEN: {
                table = RegistrationTable.TABLE_NAME;
                break;
            }
            case RejectedJobCardTable.PATH_TOKEN: {
                table = RejectedJobCardTable.TABLE_NAME;
                break;
            }
            case AreaTable.PATH_TOKEN: {
                table = AreaTable.TABLE_NAME;
                break;
            }
            case BankTable.PATH_TOKEN: {
                table = BankTable.TABLE_NAME;
                break;
            }
            case PaymentTable.PATH_TOKEN: {
                table = PaymentTable.TABLE_NAME;
                break;
            }
            case IdProofTable.PATH_TOKEN: {
                table = IdProofTable.TABLE_NAME;
                break;
            }
            case AddProofTable.PATH_TOKEN: {
                table = AddProofTable.TABLE_NAME;
                break;
            }
            case WardTable.PATH_TOKEN: {
                table = AddProofTable.TABLE_NAME;
                break;
            }
            case CategoryTable.PATH_TOKEN: {
                table = CategoryTable.TABLE_NAME;
                break;
            }
            case SubCategoryTable.PATH_TOKEN: {
                table = SubCategoryTable.TABLE_NAME;
                break;
            }
            case Pincode.PATH_TOKEN: {
                table = Pincode.TABLE_NAME;
                break;
            }
            case LocationTable.PATH_TOKEN: {
                table = LocationTable.TABLE_NAME;
                break;
            }
            case LandmarkTable.PATH_TOKEN: {
                table = LandmarkTable.TABLE_NAME;
                break;
            }
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();

        for (ContentValues cv : values) {
            db.insert(table, null, cv);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return values.length;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        int result = 0;

        switch (token) {
            case LoginTable.PATH_TOKEN: {
                result = doDelete(db, uri, LoginTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case CommissionJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, CommissionJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case DecommissionJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, DecommissionJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case PreventiveJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, PreventiveJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case BreakDownJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, BreakDownJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case SiteVerificationJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, SiteVerificationJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }

            case MeterInstalltionJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, MeterInstalltionJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case AssetJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, AssetJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case ComplaintJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, ComplaintJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case ServiceJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, ServiceJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case ConsumerEnquiryTable.PATH_TOKEN: {
                result = doDelete(db, uri, ConsumerEnquiryTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case ConversionJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, ConversionJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case NotificationTable.PATH_TOKEN: {
                result = doDelete(db, uri, NotificationTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case AllJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, AllJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case RegistrationTable.PATH_TOKEN: {
                result = doDelete(db, uri, RegistrationTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case RejectedJobCardTable.PATH_TOKEN: {
                result = doDelete(db, uri, RejectedJobCardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case AreaTable.PATH_TOKEN: {
                result = doDelete(db, uri, AreaTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case BankTable.PATH_TOKEN: {
                result = doDelete(db, uri, BankTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case PaymentTable.PATH_TOKEN: {
                result = doDelete(db, uri, PaymentTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case IdProofTable.PATH_TOKEN: {
                result = doDelete(db, uri, IdProofTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case AddProofTable.PATH_TOKEN: {
                result = doDelete(db, uri, AddProofTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case WardTable.PATH_TOKEN: {
                result = doDelete(db, uri, WardTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case CategoryTable.PATH_TOKEN: {
                result = doDelete(db, uri, CategoryTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case SubCategoryTable.PATH_TOKEN: {
                result = doDelete(db, uri, SubCategoryTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case Pincode.PATH_TOKEN: {
                result = doDelete(db, uri, Pincode.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case LocationTable.PATH_TOKEN: {
                result = doDelete(db, uri, LocationTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case LandmarkTable.PATH_TOKEN: {
                result = doDelete(db, uri, LandmarkTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
        }
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        int result = 0;

        switch (token) {
            case LoginTable.PATH_TOKEN: {
                result = doUpdate(db, uri, LoginTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case CommissionJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, CommissionJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case DecommissionJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, DecommissionJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case PreventiveJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, PreventiveJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case BreakDownJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, BreakDownJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case SiteVerificationJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, SiteVerificationJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case MeterInstalltionJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, MeterInstalltionJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case AssetJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, AssetJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case ComplaintJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, ComplaintJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case ServiceJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, ServiceJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case ConsumerEnquiryTable.PATH_TOKEN: {
                result = doUpdate(db, uri, ConsumerEnquiryTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case ConversionJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, ConversionJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case NotificationTable.PATH_TOKEN: {
                result = doUpdate(db, uri, NotificationTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case AllJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, AllJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case RegistrationTable.PATH_TOKEN: {
                result = doUpdate(db, uri, RegistrationTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case RejectedJobCardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, RejectedJobCardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case AreaTable.PATH_TOKEN: {
                result = doUpdate(db, uri, AreaTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case BankTable.PATH_TOKEN: {
                result = doUpdate(db, uri, BankTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case PaymentTable.PATH_TOKEN: {
                result = doUpdate(db, uri, PaymentTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case IdProofTable.PATH_TOKEN: {
                result = doUpdate(db, uri, IdProofTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case AddProofTable.PATH_TOKEN: {
                result = doUpdate(db, uri, AddProofTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case WardTable.PATH_TOKEN: {
                result = doUpdate(db, uri, WardTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case CategoryTable.PATH_TOKEN: {
                result = doUpdate(db, uri, CategoryTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case SubCategoryTable.PATH_TOKEN: {
                result = doUpdate(db, uri, SubCategoryTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case Pincode.PATH_TOKEN: {
                result = doUpdate(db, uri, Pincode.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case LocationTable.PATH_TOKEN: {
                result = doUpdate(db, uri, LocationTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case LandmarkTable.PATH_TOKEN: {
                result = doUpdate(db, uri, LandmarkTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }


        }

        return result;
    }

    /**
     * Performs query to specified table using the projection, selection and
     * sortOrder
     *
     * @param db            SQLiteDatabase instance
     * @param uri           ContentUri for watch
     * @param tableName     Name of table on which query has to perform
     * @param projection    needed projection
     * @param selection     needed selection cases
     * @param selectionArgs needed selection arguments
     * @param sortOrder     sort order if necessary
     * @return Cursor cursor from the table tableName matching all the criterion
     */
    private Cursor doQuery(SQLiteDatabase db, Uri uri, String tableName,
                           String[] projection, String selection, String[] selectionArgs,
                           String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(tableName);
        Cursor result = builder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);

        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    /**
     * performs update to the specified table row or rows
     *
     * @param db            SQLiteDatabase instance
     * @param uri           uri of the content that was changed
     * @param tableName     Name of table on which query has to perform
     * @param selection     needed selection cases
     * @param selectionArgs needed selection arguments
     * @param values        content values to update
     * @return success or failure
     */
    private int doUpdate(SQLiteDatabase db, Uri uri, String tableName,
                         String selection, String[] selectionArgs, ContentValues values) {
        int result = db.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    /**
     * deletes the row/rows from the table
     *
     * @param db            SQLiteDatabase instance
     * @param uri           uri of the content that was changed
     * @param tableName     Name of table on which query has to perform
     * @param selection     needed selection cases
     * @param selectionArgs needed selection arguments
     * @return success or failure
     */
    private int doDelete(SQLiteDatabase db, Uri uri, String tableName,
                         String selection, String[] selectionArgs) {
        int result = db.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    /**
     * insert rows to the specified table
     *
     * @param db         SQLiteDatabase instance
     * @param tableName  Name of table on which query has to perform
     * @param contentUri ContentUri to build the path
     * @param uri        uri of the content that was changed
     * @param values     content values to update
     * @return success or failure
     */
    private Uri doInsert(SQLiteDatabase db, String tableName, Uri contentUri,
                         Uri uri, ContentValues values) {
        long id = db.insert(tableName, null, values);
        Uri result = contentUri.buildUpon().appendPath(String.valueOf(id))
                .build();
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

}