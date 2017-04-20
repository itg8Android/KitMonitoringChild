package itg8.com.kitmonitoringchild.services;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Browser;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by itg_Android on 12/13/2016.
 */

public class SocialActivityTraceService extends Service {


    private  String CHROME_BOOKMARKS_URI ="content://com.android.chrome.browser/bookmarks";
    private Cursor resolver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(Calendar.DAY_OF_MONTH, beginCal.get(Calendar.DAY_OF_MONTH)-1);

        Calendar endCal = Calendar.getInstance();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            final List<UsageStats> queryUsageStats;
            queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());
            System.out.println("results for " + beginCal.getTime().toGMTString() + " - " + endCal.getTime().toGMTString());
            for (UsageStats app : queryUsageStats) {
                System.out.println(app.getPackageName() + " | " + (float) (app.getTotalTimeInForeground() / 1000));
            }
        }else {

            ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(Integer.MAX_VALUE);
            Log.d("topActivity", "CURRENT Activity ::"
                    + taskInfo.get(0).topActivity.getClassName());
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            componentInfo.getPackageName();
        }

        getBrowserHist();
       return START_STICKY;

//        runAccessSetting();
    }

    public void getBrowserHist()  {


        ChromeOberver observer = new ChromeOberver(new Handler());

        getContentResolver().registerContentObserver(Uri.parse(CHROME_BOOKMARKS_URI), true, observer);
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = null;
//            v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    class ChromeOberver extends ContentObserver {
        private  final String TAG = ChromeOberver.class.getSimpleName();

        public ChromeOberver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);
            Log.d(TAG, "onChange: " + selfChange);

            Cursor cursor = getContentResolver()
                    .query(Uri.parse(CHROME_BOOKMARKS_URI), new String[]{"title", "url"},
                            "bookmark = 0", null, null);
            Log.d(TAG,"cursor " + cursor.getCount());
            // process cursor results
        }
    }

}
