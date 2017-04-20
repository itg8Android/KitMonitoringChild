package itg8.com.kitmonitoringchild.services;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.LoaderManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


import itg8.com.kitmonitoringchild.CommonMethod.CommonMethod;
import itg8.com.kitmonitoringchild.CommonMethod.RetroController;
import itg8.com.kitmonitoringchild.model.Sms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;
import me.everything.providers.android.telephony.TelephonyProvider;
import me.everything.providers.core.Data;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyService extends Service  implements LoaderManager.LoaderCallbacks{
    private static final String TAG = MyService.class.getSimpleName();
    private List<me.everything.providers.android.calllog.Call> ListCall = new ArrayList<>();
     private List<String> PackageNameList = new ArrayList<>();


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TelephonyProvider telephonyProvider = new TelephonyProvider(this);
        Data<me.everything.providers.android.telephony.Sms> ss=telephonyProvider.getSms(TelephonyProvider.Filter.INBOX);
        List<me.everything.providers.android.telephony.Sms> sms=ss.getList();
        for (me.everything.providers.android.telephony.Sms s :
                sms) {
            Log.d(CommonMethod.getTag(this.getClass()),"my sms : "+s.body);
        }

        getCallLog();

        getCurrentRunningApp();

        return START_STICKY;
    }

    private void getCurrentRunningApp() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getRunningTasks();
            }

        }, 0, 1000);//put here time 1000 milliseconds=1 second
    }
    private void getRunningTasks() {
        ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        for (ActivityManager.RunningTaskInfo o :
                runningTasks) {
            Log.d(TAG, "running : " + o.topActivity.getPackageName()+" timestamp: "+Calendar.getInstance().getTimeInMillis());
            PackageNameList.add(o.topActivity.getPackageName());
            Log.d(CommonMethod.getTag(this.getClass()),"PackageName :" +PackageNameList);
        }
    }


    private void getCallLog() {
        CallsProvider callsProvider = new CallsProvider(this);
        List<Call> callData = callsProvider.getCalls().getList();
        for (Call ca :
                callData) {
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy hh:MM:ss",Locale.UK);
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(ca.callDate);
            Log.d(CommonMethod.getTag(this.getClass()),"my Call : "+ca.name+" number: "+ca.number+" callDate: "+ sdf.format(cal.getTime()));
            ListCall.add(ca);
        }
//        SentCallDataToServer(ListCall);
    }

    public Map<Integer, List<Sms>> getAllSms() {
        Map<Integer, List<Sms>> smsMap = new TreeMap<Integer, List<Sms>>();
        Sms objSms = null;
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        Cursor c = cr.query(message, null, null, null, null);
//        startManagingCursor(c);

        int totalSMS = c.getCount();
        Log.d(CommonMethod.getTag(this.getClass()),"TotalSMS :"+c.getCount());

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getLong(c.getColumnIndexOrThrow("date")));


                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                cal.setTimeInMillis(objSms.getTime());
                int month = cal.get(Calendar.MONTH);

                if (!smsMap.containsKey(month))
                    smsMap.put(month, new ArrayList<Sms>());
                smsMap.get(month).add(objSms);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();
      SendSMSDataToServer(smsMap);
        return smsMap;
    }

    private void SendSMSDataToServer(Map<Integer, List<Sms>> smsMap) {

    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
// public void SentCallDataToServer(List<me.everything.providers.android.calllog.Call> listCall)
// {
//     RetroController controller = CommonMethod.getController().create(RetroController.class);
//     retrofit2.Call<me.everything.providers.android.calllog.Call> call = controller.sendCallToServer();
//        call.enqueue(new Callback<Call>() {
//    @Override
//    public void onResponse(retrofit2.Call<Call> call, Response<Call> response) {
//        if(response.isSuccessful())
//        {
//
//        }
//
//    }
//
//    @Override
//    public void onFailure(retrofit2.Call<Call> call, Throwable t) {
//         t.printStackTrace();
//
//    }
//    });
//
// }




}
