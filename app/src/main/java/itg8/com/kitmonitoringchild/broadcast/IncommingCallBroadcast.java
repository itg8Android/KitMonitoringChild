package itg8.com.kitmonitoringchild.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by itg_Android on 1/3/2017.
 */

public class IncommingCallBroadcast extends BroadcastReceiver {

    private static final String TAG = IncommingCallBroadcast.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
        // TELEPHONY MANAGER class object to register one listner
        TelephonyManager tmgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        //Create Listner
        MyPhoneStateListener PhoneListener = new MyPhoneStateListener();

        // Register listener for LISTEN_CALL_STATE
        tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {

            // Log.d("MyPhoneListener",state+"   incoming no:"+incomingNumber);


            // state = 1 means when phone is ringing
            if (state == 1) {
                if(incomingNumber==null)
                {
                    //outgoing call
                    String msg = " New Phone Call Event. Outgoing Number : "+incomingNumber;
                    Log.d(TAG,"call from : "+msg + " ontime: "+ Calendar.getInstance().getTime().toString());
                }
                else
                {
                    //incoming call
                    String msg = " New Phone Call Event. Incomming Number : "+incomingNumber;
                    Log.d(TAG,"call from : "+msg + "ontime: "+ Calendar.getInstance().getTime().toString());
                }

            }
        }
    }


}
