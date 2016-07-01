// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.api.content;

import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.pointinside.android.api.dao.PIDownloadDataCursor;
import com.pointinside.android.api.dao.PIFileDataCursor;
import com.pointinside.android.api.utils.FrobInputStream;
import java.io.*;

// Referenced classes of package com.pointinside.android.api.content:
//            PIContentStore, ContentManagerUtils, Files, Downloads

public final class PIContentManager
{


    public static  enum ContentType
    {
    	UNKNOWN("UNKNOWN",0),
     VENUE_IMAGE("VENUE_IMAGE", 1),
        VENUE_ZONE_IMAGE("VENUE_ZONE_IMAGE", 2),
        PLACE_IMAGE("PLACE_IMAGE", 3),
        PROMOTION_IMAGE("PROMOTION_IMAGE", 4),
        DATASET("DATASET", 5),
        REFERENCE("REFERENCE", 6),
        ALL_DATASETS("ALL_DATASETS", 7),
        VENUE_PDEMAP("VENUE_PDEMAP", 8);
        private String sType;
        private int iType;

        public String getSType() {
        	return sType;
        }

        public int getIType(){
        	return iType;
        }

        private ContentType(String s, int i)
        {
        	sType = s;
        	iType = i;
//            super(s, i);
        }
    };

    static int[] $SWITCH_TABLE$com$pointinside$android$api$content$PIContentManager$ContentType()
    {
        int ai[] = $SWITCH_TABLE$com$pointinside$android$api$content$PIContentManager$ContentType;
        if(ai != null)
            return ai;
        int ai1[] = new int[ContentType.values().length];
        ContentType tesst = PIContentManager.ContentType.PLACE_IMAGE;
        try
        {
            ai1[ContentType.ALL_DATASETS.ordinal()] = 8;
        }
        catch(NoSuchFieldError nosuchfielderror) { }
        try
        {
            ai1[ContentType.DATASET.ordinal()] = 6;
        }
        catch(NoSuchFieldError nosuchfielderror1) { }
        try
        {
            ai1[ContentType.PLACE_IMAGE.ordinal()] = 4;
        }
        catch(NoSuchFieldError nosuchfielderror2) { }
        try
        {
            ai1[ContentType.PROMOTION_IMAGE.ordinal()] = 5;
        }
        catch(NoSuchFieldError nosuchfielderror3) { }
        try
        {
            ai1[ContentType.REFERENCE.ordinal()] = 7;
        }
        catch(NoSuchFieldError nosuchfielderror4) { }
        try
        {
            ai1[ContentType.UNKNOWN.ordinal()] = 1;
        }
        catch(NoSuchFieldError nosuchfielderror5) { }
        try
        {
            ai1[ContentType.VENUE_IMAGE.ordinal()] = 2;
        }
        catch(NoSuchFieldError nosuchfielderror6) { }
        try
        {
            ai1[ContentType.VENUE_PDEMAP.ordinal()] = 9;
        }
        catch(NoSuchFieldError nosuchfielderror7) { }
        try
        {
            ai1[ContentType.VENUE_ZONE_IMAGE.ordinal()] = 3;
        }
        catch(NoSuchFieldError nosuchfielderror8) { }
        $SWITCH_TABLE$com$pointinside$android$api$content$PIContentManager$ContentType = ai1;
        return ai1;
    }

    public PIContentManager(com.pointinside.android.api.PIMapReference.PIReferenceContentManager pireferencecontentmanager)
    {
    }

    public PIContentManager(com.pointinside.android.api.PIMapVenue.PIVenueContentManager pivenuecontentmanager)
    {
    }

    public static void closeStream(ParcelFileDescriptor parcelfiledescriptor)
    {
        if(parcelfiledescriptor == null)
            return;
        try
        {
            parcelfiledescriptor.close();
            return;
        }
        catch(Throwable throwable)
        {
            return;
        }
    }

    public static void closeStream(Closeable closeable)
    {
        try {
	    	if(closeable == null)
	            return;
	        closeable.close();
	        return;
        } catch(IOException ioexception){
	        Log.e("IO", "Could not close stream", ioexception);
        }
        return;
    }

    public static void copy(InputStream inputstream, OutputStream outputstream)
        throws IOException
    {
        byte abyte0[] = new byte[8192];
        do
        {
            int i = inputstream.read(abyte0);
            if(i == -1)
                return;
            outputstream.write(abyte0, 0, i);
        } while(true);
    }

    public static Bitmap decodeUri(Uri uri)
        throws IOException
    {
    	android.os.ParcelFileDescriptor.AutoCloseInputStream autocloseinputstream = null;
        android.graphics.BitmapFactory.Options options;
        try {
        autocloseinputstream = new android.os.ParcelFileDescriptor.AutoCloseInputStream(makeInputStream(uri));
        options = new android.graphics.BitmapFactory.Options();
        Object obj;
        options.inJustDecodeBounds = false;
        options.inDither = true;
        options.inPreferredConfig = android.graphics.Bitmap.Config.RGB_565;
        if(!shouldXOR(uri.toString()))
            obj = autocloseinputstream;
        else
        	obj = new FrobInputStream(autocloseinputstream);

        Bitmap bitmap = BitmapFactory.decodeStream(((InputStream) (obj)), null, options);
        closeStream(autocloseinputstream);
        return bitmap;

    } catch(OutOfMemoryError outofmemoryerror) {
        closeStream(autocloseinputstream);
        return null;
    } catch(Exception exception){
        closeStream(autocloseinputstream);
        return null;
    }
    }

    static void deleteDownloadItem(Context context, Uri uri)
    {
        PIContentStore.getInstance(context).delete(uri, null, null);
    }

    private void deleteReferenceDownload(Context context)
    {
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        int i = picontentstore.delete(DOWNLOADS_CONTENT_URI, (new StringBuilder("content_type=")).append(ContentType.REFERENCE.ordinal()).toString(), null);
        if(i > 0)
            i = picontentstore.delete(FILES_CONTENT_URI, (new StringBuilder("content_type=")).append(ContentType.REFERENCE.ordinal()).toString(), null);
        if(i > 0)
            deleteReferenceFile(context);
    }

    private static void deleteVenueFiles(Context context, String s)
    {
        File file = new File((new StringBuilder(String.valueOf(ContentManagerUtils.getDownloadDirectory(context, true)))).append("/").append(s).toString());
        if(file != null && file.exists())
            ContentManagerUtils.deleteDir(file);
        File file1 = new File((new StringBuilder(String.valueOf(ContentManagerUtils.getDownloadDirectory(context, false)))).append("/").append(s).toString());
        if(file1 != null && file1.exists())
            ContentManagerUtils.deleteDir(file1);
    }

    public static PIFileDataCursor getVenueZoneImageFile(Context context, String s, String s1)
    {
        Uri uri = Uri.parse(makeContentUri(context, s, ContentType.VENUE_ZONE_IMAGE, "files"));
        return PIFileDataCursor.getInstance(PIContentStore.getInstance(context).query(uri, null, " file_name = ?", new String[] {
            s1
        }, null));
    }

    public static PIFileDataCursor getVenueZoneImages(Context context, String s)
    {
        Uri uri = Uri.parse(makeContentUri(context, s, ContentType.VENUE_ZONE_IMAGE, "files"));
        return PIFileDataCursor.getInstance(PIContentStore.getInstance(context).query(uri, null, null, null, null));
    }

    static void insertFile(Context context, String s, String s1, String s2, long l, long l1,
            ContentType contenttype, long l2)
    {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("file_uri", s);
        contentvalues.put("file_name", s1);
        contentvalues.put("venue_uuid", s2);
        contentvalues.put("size", Long.valueOf(l));
        contentvalues.put("download_id", Long.valueOf(l1));
        contentvalues.put("datetoken", Long.valueOf(l2));
        contentvalues.put("content_type", Integer.valueOf(contenttype.ordinal()));
        PIContentStore.getInstance(context).insert(Files.CONTENT_URI, contentvalues);
    }

    static String makeContentUri(Context context, String s, ContentType contenttype, String s1)
    {
        if(!"downloads".equals(s1) && !"files".equals(s1))
            throw new IllegalArgumentException((new StringBuilder("Unknown Uri Type: ")).append(s1).toString());
        if(contenttype != ContentType.REFERENCE && contenttype != ContentType.ALL_DATASETS && s == null)
            throw new IllegalArgumentException((new StringBuilder("venueUUID is null!!! You must provide a venueUUID for contentType: ")).append(contenttype).toString());
        StringBuilder stringbuilder = (new StringBuilder()).append("content://com.pointinside.android.api.content/");
        switch($SWITCH_TABLE$com$pointinside$android$api$content$PIContentManager$ContentType()[contenttype.ordinal()])
        {
        default:
            throw new IllegalStateException((new StringBuilder("Unknown Type: ")).append(contenttype).toString());

        case 2: // '\002'
            stringbuilder.append(s1).append("/venue").append("/").append(s).append("/venue_image");
            return stringbuilder.toString();

        case 3: // '\003'
            stringbuilder.append(s1).append("/venue").append("/").append(s).append("/zone_images");
            return stringbuilder.toString();

        case 9: // '\t'
            stringbuilder.append(s1).append("/venue").append("/").append(s).append("/venue_pdemap");
            return stringbuilder.toString();

        case 4: // '\004'
            stringbuilder.append(s1).append("/venue").append("/").append(s).append("/place_image");
            return stringbuilder.toString();

        case 5: // '\005'
            stringbuilder.append(s1).append("/venue").append("/").append(s).append("/promo_image");
            return stringbuilder.toString();

        case 6: // '\006'
            stringbuilder.append(s1).append("/venue").append("/").append(s).append("/dataset");
            return stringbuilder.toString();

        case 8: // '\b'
            stringbuilder.append(s1).append("/dataset");
            return stringbuilder.toString();

        case 7: // '\007'
            stringbuilder.append(s1).append("/reference");
            return stringbuilder.toString();
        }
    }

    private static ParcelFileDescriptor makeInputStream(Uri uri)
    {
        ParcelFileDescriptor parcelfiledescriptor;
        try
        {
            parcelfiledescriptor = ParcelFileDescriptor.open(new File(uri.toString()), 0x10000000);
        }
        catch(IOException ioexception)
        {
            Log.e("PIMaps", ioexception.toString(), ioexception);
            return null;
        }
        return parcelfiledescriptor;
    }

    public static void removeCachedZipFiles(Context context)
    {
        ContentManagerUtils.removeCachedZipFiles(context);
    }

    static boolean shouldXOR(String s)
    {
        while(s == null || !s.toLowerCase().endsWith(".ing"))
            return false;
        return true;
    }

    private void validateVenueDownloadFromRow(Context context, PIFileDataCursor pifiledatacursor)
    {
        String s = pifiledatacursor.getFileUri();
        String s1 = pifiledatacursor.getVenueUUID();
        File file = new File(s);
        boolean flag;
        if(file != null && file.exists() && venueZoneImagesExists(context, s1))
            flag = true;
        else
            flag = false;
        if(!flag)
            deleteVenueDownload(context, s1);
    }

    public void cancelDownload(Context context, Uri uri)
    {
        PIDownloadDataCursor pidownloaddatacursor;
        pidownloaddatacursor = getDownloadItem(context, uri);
        try {
        if(pidownloaddatacursor == null)
            return;
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("control", Integer.valueOf(1));
        contentvalues.put("status", Integer.valueOf(490));
        picontentstore.update(pidownloaddatacursor.getUri(), contentvalues, null, null);
        context.sendBroadcast(new Intent("pointinside.intent.action.DOWNLOAD_CANCEL"));
        pidownloaddatacursor.close();
        return;
        } catch(Exception exception) {
	        pidownloaddatacursor.close();
        }
    }

    public void delete(Context context, Uri uri)
    {
        PIContentStore.getInstance(context).delete(uri, null, null);
    }

    public boolean deleteFile(String s)
    {
        if(s == null)
            return false;
        boolean flag;
        try {
        File file = new File(s);
        if(!file.exists())
            return false;
        flag = file.delete();
        return flag;
        } catch(Exception exception) {
        return false;
        }
    }

    public boolean deleteReferenceFile(Context context)
    {
        PIFileDataCursor pifiledatacursor;
        pifiledatacursor = getReferenceFile(context);
        try {
	        if(pifiledatacursor == null)
	            return false;
	        boolean flag;
	        File file = new File(pifiledatacursor.getFileUri());
	        if(!file.exists()){
		        pifiledatacursor.close();
	            return false;	        }
	        flag = file.delete();
	        pifiledatacursor.close();
	        return flag;
        } catch(Exception exception) {
	        pifiledatacursor.close();
	        return false;
        }
    }

    public void deleteVenueDownload(Context context, String s)
    {
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        int i = picontentstore.delete(DOWNLOADS_CONTENT_URI, "venue_uuid=?", new String[] {
            s
        });
        if(i > 0)
            i = picontentstore.delete(FILES_CONTENT_URI, "venue_uuid=?", new String[] {
                s
            });
        if(i > 0)
            deleteVenueFiles(context, s);
    }

    public PIDownloadDataCursor getDownloadItem(Context context, Uri uri)
    {
        return PIDownloadDataCursor.getInstance(PIContentStore.getInstance(context), uri);
    }

    public PIDownloadDataCursor getOrCreatePlaceImagesDownloadItem(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        PIDownloadDataCursor pidownloaddatacursor = getDownloadItem(context, Uri.parse(makeContentUri(context, pimapvenuesummary.getVenueUUID(), ContentType.PLACE_IMAGE, "downloads")));
        if(pidownloaddatacursor == null)
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("remote_uri", Downloads.DownloadConstants.PLACE_IMAGES_URI.buildUpon().appendPath(pimapvenuesummary.getVenuePlaceImagesFile()).toString());
            contentvalues.put("content_type", Integer.valueOf(ContentType.PLACE_IMAGE.ordinal()));
            contentvalues.put("control", Integer.valueOf(1));
            contentvalues.put("status", Integer.valueOf(190));
            contentvalues.put("venue_uuid", pimapvenuesummary.getVenueUUID());
            contentvalues.put("updated_identifier", pimapvenuesummary.getVenuePlaceImagesMD5());
            pidownloaddatacursor = getDownloadItem(context, picontentstore.insert(Downloads.CONTENT_URI, contentvalues));
        }
        return pidownloaddatacursor;
    }

    public PIDownloadDataCursor getOrCreatePromotionImagesDownloadItem(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        PIDownloadDataCursor pidownloaddatacursor = getDownloadItem(context, Uri.parse(makeContentUri(context, pimapvenuesummary.getVenueUUID(), ContentType.PROMOTION_IMAGE, "downloads")));
        if(pidownloaddatacursor == null)
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("remote_uri", Downloads.DownloadConstants.PROMOTION_IMAGES_URI.buildUpon().appendPath(pimapvenuesummary.getVenuePromotionsImagesFile()).toString());
            contentvalues.put("content_type", Integer.valueOf(ContentType.PROMOTION_IMAGE.ordinal()));
            contentvalues.put("control", Integer.valueOf(1));
            contentvalues.put("status", Integer.valueOf(190));
            contentvalues.put("venue_uuid", pimapvenuesummary.getVenueUUID());
            contentvalues.put("updated_identifier", pimapvenuesummary.getVenuePromotionsImagesMD5());
            pidownloaddatacursor = getDownloadItem(context, picontentstore.insert(Downloads.CONTENT_URI, contentvalues));
        }
        return pidownloaddatacursor;
    }

    public PIDownloadDataCursor getOrCreateReferenceDownloadItem(Context context)
    {
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        PIDownloadDataCursor pidownloaddatacursor = getDownloadItem(context, Uri.parse(makeContentUri(context, null, ContentType.REFERENCE, "downloads")));
        if(pidownloaddatacursor == null)
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("remote_uri", Downloads.DownloadConstants.REFERENCE_URI.toString());
            contentvalues.put("content_type", Integer.valueOf(ContentType.REFERENCE.ordinal()));
            contentvalues.put("control", Integer.valueOf(1));
            contentvalues.put("status", Integer.valueOf(190));
            pidownloaddatacursor = getDownloadItem(context, picontentstore.insert(Downloads.CONTENT_URI, contentvalues));
        }
        return pidownloaddatacursor;
    }

    public PIDownloadDataCursor getOrCreateVenueDatasetDownloadItem(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        PIDownloadDataCursor pidownloaddatacursor = getDownloadItem(context, Uri.parse(makeContentUri(context, pimapvenuesummary.getVenueUUID(), ContentType.DATASET, "downloads")));
        if(pidownloaddatacursor == null)
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("remote_uri", Downloads.DownloadConstants.DATASET_URI.buildUpon().appendPath(pimapvenuesummary.getVenueDatasetFile()).toString());
            contentvalues.put("content_type", Integer.valueOf(ContentType.DATASET.ordinal()));
            contentvalues.put("control", Integer.valueOf(1));
            contentvalues.put("status", Integer.valueOf(190));
            contentvalues.put("venue_uuid", pimapvenuesummary.getVenueUUID());
            contentvalues.put("updated_identifier", pimapvenuesummary.getVenueDatasetMD5());
            pidownloaddatacursor = getDownloadItem(context, picontentstore.insert(Downloads.CONTENT_URI, contentvalues));
        }
        return pidownloaddatacursor;
    }

    public PIDownloadDataCursor getOrCreateVenueImagesDownloadItem(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        PIDownloadDataCursor pidownloaddatacursor = getDownloadItem(context, Uri.parse(makeContentUri(context, pimapvenuesummary.getVenueUUID(), ContentType.VENUE_IMAGE, "downloads")));
        if(pidownloaddatacursor == null)
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("remote_uri", Downloads.DownloadConstants.VENUE_IMAGE_URI.buildUpon().appendPath(pimapvenuesummary.getVenueImagesFile()).toString());
            contentvalues.put("content_type", Integer.valueOf(ContentType.VENUE_IMAGE.ordinal()));
            contentvalues.put("control", Integer.valueOf(1));
            contentvalues.put("status", Integer.valueOf(190));
            contentvalues.put("venue_uuid", pimapvenuesummary.getVenueUUID());
            contentvalues.put("download_identifier", pimapvenuesummary.getVenueImagesMD5());
            pidownloaddatacursor = getDownloadItem(context, picontentstore.insert(Downloads.CONTENT_URI, contentvalues));
        }
        return pidownloaddatacursor;
    }

    public PIDownloadDataCursor getOrCreateVenuePDEMapDownloadItem(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        PIDownloadDataCursor pidownloaddatacursor = getDownloadItem(context, Uri.parse(makeContentUri(context, pimapvenuesummary.getVenueUUID(), ContentType.VENUE_PDEMAP, "downloads")));
        if(pidownloaddatacursor == null)
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("remote_uri", Downloads.DownloadConstants.PDEMAP_URI.buildUpon().appendPath(pimapvenuesummary.getVenuePDEMapFile()).toString());
            contentvalues.put("content_type", Integer.valueOf(ContentType.VENUE_PDEMAP.ordinal()));
            contentvalues.put("control", Integer.valueOf(1));
            contentvalues.put("status", Integer.valueOf(190));
            contentvalues.put("venue_uuid", pimapvenuesummary.getVenueUUID());
            contentvalues.put("updated_identifier", pimapvenuesummary.getVenuePDEMapMD5());
            pidownloaddatacursor = getDownloadItem(context, picontentstore.insert(Downloads.CONTENT_URI, contentvalues));
        }
        return pidownloaddatacursor;
    }

    public PIDownloadDataCursor getOrCreateVenueZoneImagesDownloadItem(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        PIDownloadDataCursor pidownloaddatacursor = getDownloadItem(context, Uri.parse(makeContentUri(context, pimapvenuesummary.getVenueUUID(), ContentType.VENUE_ZONE_IMAGE, "downloads")));
        if(pidownloaddatacursor == null)
        {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("remote_uri", Downloads.DownloadConstants.ZONE_IMAGES_URI.buildUpon().appendPath(pimapvenuesummary.getVenueZoneImagesFile()).toString());
            contentvalues.put("content_type", Integer.valueOf(ContentType.VENUE_ZONE_IMAGE.ordinal()));
            contentvalues.put("control", Integer.valueOf(1));
            contentvalues.put("status", Integer.valueOf(190));
            contentvalues.put("venue_uuid", pimapvenuesummary.getVenueUUID());
            contentvalues.put("updated_identifier", pimapvenuesummary.getVenueZoneImagesMD5());
            pidownloaddatacursor = getDownloadItem(context, picontentstore.insert(Downloads.CONTENT_URI, contentvalues));
        }
        return pidownloaddatacursor;
    }

    public Bitmap getPlaceImageFile(Context context, String s, String s1)
    {
        PIFileDataCursor pifiledatacursor = null;
        Bitmap bitmap;
        try {
	        Uri uri = Uri.parse(makeContentUri(context, s, ContentType.PLACE_IMAGE, "files")).buildUpon().appendPath(s1).build();
	        pifiledatacursor = PIFileDataCursor.getInstance(PIContentStore.getInstance(context).query(uri, null, " file_name = ?", new String[] {
	            s1
	        }, null));
	        bitmap = null;
	        if(pifiledatacursor == null)
	            return null;
	        Bitmap bitmap1 = decodeUri(Uri.parse(pifiledatacursor.getFileUri()));
	        bitmap = bitmap1;
	        pifiledatacursor.close();
	        return bitmap;

        } catch(Exception exception1){
	        Log.e("PIContentManager", "Unable to decode bitmap", exception1);
	        pifiledatacursor.close();
	        return null;
        }
    }

    public Bitmap getPromoImageFile(Context context, String s, String s1)
    {
        PIFileDataCursor pifiledatacursor = null;
        Bitmap bitmap;
        try {
        Uri uri = Uri.parse(makeContentUri(context, s, ContentType.PROMOTION_IMAGE, "files")).buildUpon().appendPath(s1).build();
        pifiledatacursor = PIFileDataCursor.getInstance(PIContentStore.getInstance(context).query(uri, null, " file_name = ?", new String[] {
            s1
        }, null));
        bitmap = null;
        if(pifiledatacursor == null)
            return null;
        Bitmap bitmap1 = decodeUri(Uri.parse(pifiledatacursor.getFileUri()));
        bitmap = bitmap1;
        pifiledatacursor.close();
        return bitmap;
        } catch(Exception exception1){
	        Log.e("PIContentManager", "Unable to decode bitmap", exception1);
	        pifiledatacursor.close();
	        return null;
        }
    }

    public PIFileDataCursor getReferenceFile(Context context)
    {
        Uri uri = Uri.parse(makeContentUri(context, null, ContentType.REFERENCE, "files"));
        return PIFileDataCursor.getInstance(PIContentStore.getInstance(context), uri);
    }

    public PIFileDataCursor getVenueDatasetFile(Context context, String s)
    {
        Uri uri = Uri.parse(makeContentUri(context, s, ContentType.DATASET, "files"));
        return PIFileDataCursor.getInstance(PIContentStore.getInstance(context), uri);
    }

    public PIFileDataCursor getVenueDatasets(Context context)
    {
        Uri uri = Uri.parse(makeContentUri(context, null, ContentType.ALL_DATASETS, "files"));
        return PIFileDataCursor.getInstance(PIContentStore.getInstance(context), uri);
    }

    public Bitmap getVenueImageFile(Context context, String s, String s1)
    {
        PIFileDataCursor pifiledatacursor = null;
        Bitmap bitmap;
       try {
        Uri uri = Uri.parse(makeContentUri(context, s, ContentType.VENUE_IMAGE, "files")).buildUpon().appendPath(s1).build();
        pifiledatacursor = PIFileDataCursor.getInstance(PIContentStore.getInstance(context).query(uri, null, " file_name = ?", new String[] {
            s1
        }, null));
        bitmap = null;
        if(pifiledatacursor == null)
            return null;
        Bitmap bitmap1 = decodeUri(Uri.parse(pifiledatacursor.getFileUri()));
        bitmap = bitmap1;
        pifiledatacursor.close();
        return bitmap;
       } catch(Exception exception1) {
        Log.e("PIContentManager", "Unable to decode bitmap", exception1);
        pifiledatacursor.close();
        return null;
       }
    }

    public PIFileDataCursor getVenuePDEMapFile(Context context, String s)
    {
        Uri uri = Uri.parse(makeContentUri(context, s, ContentType.VENUE_PDEMAP, "files"));
        return PIFileDataCursor.getInstance(PIContentStore.getInstance(context), uri);
    }

    public boolean isVenueOnSDCard(Context context, String s)
    {
        PIFileDataCursor pifiledatacursor = null;
        pifiledatacursor = getVenueDatasetFile(context, s);
        if(pifiledatacursor == null)
            return false;
        try {
        boolean flag = ContentManagerUtils.isOnSDCard(pifiledatacursor.getFileUri());
        pifiledatacursor.close();
        return flag;
        } catch(Exception exception) {
	        pifiledatacursor.close();
	        return false;
        }
    }

    public boolean referenceFileExists(Context context)
    {
        PIFileDataCursor pifiledatacursor = null;
        try {
        pifiledatacursor = getReferenceFile(context);
        if(pifiledatacursor == null)
            return false;
        boolean flag = (new File(pifiledatacursor.getFileUri())).exists();
        pifiledatacursor.close();
        return flag;
        } catch(Exception exception) {
	        pifiledatacursor.close();
	        return false;
        }
    }

    public void refreshReferenceFile(Context context)
    {
        PIDownloadDataCursor pidownloaddatacursor = null;
        try {
        pidownloaddatacursor = getOrCreateReferenceDownloadItem(context);
        if(pidownloaddatacursor == null)
            return;
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("remote_uri", Downloads.DownloadConstants.REFERENCE_URI.toString());
        contentvalues.put("control", Integer.valueOf(0));
        contentvalues.put("status", Integer.valueOf(190));
        contentvalues.put("total_bytes", Integer.valueOf(0));
        contentvalues.put("current_bytes", Integer.valueOf(0));
        picontentstore.update(pidownloaddatacursor.getUri(), contentvalues, null, null);
        context.sendBroadcast(new Intent("pointinside.intent.action.DOWNLOAD_WAKEUP"));
        pidownloaddatacursor.close();
        return;
        } catch(Exception exception) {
        	pidownloaddatacursor.close();
        }
    }

    public void updatePlaceImages(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIDownloadDataCursor pidownloaddatacursor = null;
        try {
        pidownloaddatacursor = getOrCreatePlaceImagesDownloadItem(context, pimapvenuesummary);
        if(pidownloaddatacursor == null)
            return;
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("remote_uri", Downloads.DownloadConstants.PLACE_IMAGES_URI.buildUpon().appendPath(pimapvenuesummary.getVenuePlaceImagesFile()).toString());
        contentvalues.put("control", Integer.valueOf(0));
        contentvalues.put("status", Integer.valueOf(190));
        contentvalues.put("updated_identifier", pimapvenuesummary.getVenueDatasetMD5());
        contentvalues.put("total_bytes", Integer.valueOf(0));
        contentvalues.put("current_bytes", Integer.valueOf(0));
        picontentstore.update(pidownloaddatacursor.getUri(), contentvalues, null, null);
        context.sendBroadcast(new Intent("pointinside.intent.action.DOWNLOAD_WAKEUP"));
        pidownloaddatacursor.close();
        return;

        } catch(Exception exception) {
        	pidownloaddatacursor.close();
        }
    }

    public void updatePromotionImages(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIDownloadDataCursor pidownloaddatacursor = null;
        try {
        pidownloaddatacursor = getOrCreatePromotionImagesDownloadItem(context, pimapvenuesummary);
        if(pidownloaddatacursor == null)
            return;
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("remote_uri", Downloads.DownloadConstants.PROMOTION_IMAGES_URI.buildUpon().appendPath(pimapvenuesummary.getVenuePromotionsImagesFile()).toString());
        contentvalues.put("control", Integer.valueOf(0));
        contentvalues.put("status", Integer.valueOf(190));
        contentvalues.put("updated_identifier", pimapvenuesummary.getVenueDatasetMD5());
        contentvalues.put("total_bytes", Integer.valueOf(0));
        contentvalues.put("current_bytes", Integer.valueOf(0));
        picontentstore.update(pidownloaddatacursor.getUri(), contentvalues, null, null);
        context.sendBroadcast(new Intent("pointinside.intent.action.DOWNLOAD_WAKEUP"));
        pidownloaddatacursor.close();
        return;
        } catch(Exception exception){
        pidownloaddatacursor.close();
        }
    }

    public void updateVenueDatasetFile(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIDownloadDataCursor pidownloaddatacursor = null;
        try {
        pidownloaddatacursor = getOrCreateVenueDatasetDownloadItem(context, pimapvenuesummary);
        if(pidownloaddatacursor == null)
            return;
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("remote_uri", Downloads.DownloadConstants.DATASET_URI.buildUpon().appendPath(pimapvenuesummary.getVenueDatasetFile()).toString());
        contentvalues.put("control", Integer.valueOf(0));
        contentvalues.put("status", Integer.valueOf(190));
        contentvalues.put("updated_identifier", pimapvenuesummary.getVenueDatasetMD5());
        contentvalues.put("total_bytes", Integer.valueOf(0));
        contentvalues.put("current_bytes", Integer.valueOf(0));
        picontentstore.update(pidownloaddatacursor.getUri(), contentvalues, null, null);
        context.sendBroadcast(new Intent("pointinside.intent.action.DOWNLOAD_WAKEUP"));
        pidownloaddatacursor.close();
        return;
        }catch(Exception exception) {
        	pidownloaddatacursor.close();
        }
    }

    public void updateVenueImages(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIDownloadDataCursor pidownloaddatacursor = null;
        try {
        pidownloaddatacursor = getOrCreateVenueImagesDownloadItem(context, pimapvenuesummary);
        if(pidownloaddatacursor == null)
            return;
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("remote_uri", Downloads.DownloadConstants.VENUE_IMAGE_URI.buildUpon().appendPath(pimapvenuesummary.getVenueImagesFile()).toString());
        contentvalues.put("control", Integer.valueOf(0));
        contentvalues.put("status", Integer.valueOf(190));
        contentvalues.put("updated_identifier", pimapvenuesummary.getVenueDatasetMD5());
        contentvalues.put("total_bytes", Integer.valueOf(0));
        contentvalues.put("current_bytes", Integer.valueOf(0));
        picontentstore.update(pidownloaddatacursor.getUri(), contentvalues, null, null);
        context.sendBroadcast(new Intent("pointinside.intent.action.DOWNLOAD_WAKEUP"));
        pidownloaddatacursor.close();
        return;
        } catch(Exception exception) {
        pidownloaddatacursor.close();
        }
    }

    public void updateVenuePDEMap(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIDownloadDataCursor pidownloaddatacursor = null;
        try {
        pidownloaddatacursor = getOrCreateVenuePDEMapDownloadItem(context, pimapvenuesummary);
        if(pidownloaddatacursor == null)
            return;
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("remote_uri", Downloads.DownloadConstants.PDEMAP_URI.buildUpon().appendPath(pimapvenuesummary.getVenuePDEMapFile()).toString());
        contentvalues.put("control", Integer.valueOf(0));
        contentvalues.put("status", Integer.valueOf(190));
        contentvalues.put("updated_identifier", pimapvenuesummary.getVenuePDEMapMD5());
        contentvalues.put("total_bytes", Integer.valueOf(0));
        contentvalues.put("current_bytes", Integer.valueOf(0));
        picontentstore.update(pidownloaddatacursor.getUri(), contentvalues, null, null);
        context.sendBroadcast(new Intent("pointinside.intent.action.DOWNLOAD_WAKEUP"));
        pidownloaddatacursor.close();
        return;
        } catch(Exception exception) {
        pidownloaddatacursor.close();
        }
    }

    public void updateVenueZoneImages(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        PIDownloadDataCursor pidownloaddatacursor = null;
        try {
        pidownloaddatacursor = getOrCreateVenueZoneImagesDownloadItem(context, pimapvenuesummary);
        if(pidownloaddatacursor == null)
            return;
        PIContentStore picontentstore = PIContentStore.getInstance(context);
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("remote_uri", Downloads.DownloadConstants.ZONE_IMAGES_URI.buildUpon().appendPath(pimapvenuesummary.getVenueZoneImagesFile()).toString());
        contentvalues.put("control", Integer.valueOf(0));
        contentvalues.put("status", Integer.valueOf(190));
        contentvalues.put("updated_identifier", pimapvenuesummary.getVenueDatasetMD5());
        contentvalues.put("total_bytes", Integer.valueOf(0));
        contentvalues.put("current_bytes", Integer.valueOf(0));
        picontentstore.update(pidownloaddatacursor.getUri(), contentvalues, null, null);
        context.sendBroadcast(new Intent("pointinside.intent.action.DOWNLOAD_WAKEUP"));
        pidownloaddatacursor.close();
        return;
        } catch(Exception exception) {
        pidownloaddatacursor.close();
        }
    }

    public boolean validateReferenceDownload(Context context)
    {
        if(!referenceFileExists(context))
        {
            deleteReferenceDownload(context);
            return false;
        } else
        {
            return true;
        }
    }

    public void validateVenueDownload(Context context, String s)
    {
        PIFileDataCursor pifiledatacursor = null;
        try {
        pifiledatacursor = getVenueDatasetFile(context, s);
        if(pifiledatacursor == null)
            return;
        validateVenueDownloadFromRow(context, pifiledatacursor);
        pifiledatacursor.close();
        return;
        } catch(Exception exception) {
        pifiledatacursor.close();
        }
    }

    public void validateVenueDownloads(Context context)
    {
        PIFileDataCursor pifiledatacursor = null;
        try {
        pifiledatacursor = getVenueDatasets(context);
        if(pifiledatacursor == null)
            return;
        boolean flag;
        do
        {
            validateVenueDownloadFromRow(context, pifiledatacursor);
            flag = pifiledatacursor.moveToNext();
        } while(flag);
        pifiledatacursor.close();
        return;
        } catch(Exception exception){
        //pifiledatacursor.close();

        }
    }
    public boolean venueDatasetFileExists(Context context, com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary pimapvenuesummary)
    {
        return venueDatasetFileExists(context, pimapvenuesummary.getVenueUUID());
    }

    public boolean venueDatasetFileExists(Context context, String s)
    {
        PIFileDataCursor pifiledatacursor = null;
        try {
        pifiledatacursor = getVenueDatasetFile(context, s);
        if(pifiledatacursor == null)
            return false;
        boolean flag = (new File(pifiledatacursor.getFileUri())).exists();
        pifiledatacursor.close();
        return flag;
        } catch(Exception exception){
        pifiledatacursor.close();
        return false;
        }
    }

    public boolean venueZoneImagesExists(Context context, String s)
    {
        PIFileDataCursor pifiledatacursor = null;
        pifiledatacursor = getVenueZoneImages(context, s);
        try {
        if(pifiledatacursor == null)
            return false;
        int i = pifiledatacursor.getCount();
        if(i != 0){
            boolean flag1 = pifiledatacursor.moveToNext();
        	while(flag1) {
            File file = new File(pifiledatacursor.getFileUri());
            if(file != null) {
	            boolean flag = file.exists();
	            if(!flag)
	            {
	                pifiledatacursor.close();
	                return false;
	            }
            }
            flag1 = pifiledatacursor.moveToNext();
        	}
        }
        pifiledatacursor.close();
        return true;
    } catch(Exception exception) {
        pifiledatacursor.close();
    }
        return false;
    }

    private static int $SWITCH_TABLE$com$pointinside$android$api$content$PIContentManager$ContentType[];
    static final String AUTHORITY = "com.pointinside.android.api.content";
    static final String CONTENT_AUTHORITY_SLASH = "content://com.pointinside.android.api.content/";
    static final Uri DOWNLOADS_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.content/downloads");
    static final Uri FILES_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.content/files");
    public static final int IO_BUFFER_SIZE = 8192;
    private static final String TAG = "PIContentManager";

}
