package itg8.com.kitmonitoringchild.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import itg8.com.kitmonitoringchild.CommonMethod.CommonMethod;

public class IncommingSMSBroadcast extends BroadcastReceiver {
    // Get the object of SmsManager
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";
    public IncommingSMSBroadcast() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(CommonMethod.getTag(this.getClass()), "Intent recieved: " + intent.getAction());

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages;
                if (pdus != null) {
                    messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    }
                    if (messages.length > -1) {
                        Log.i(CommonMethod.getTag(this.getClass()), "Message recieved: " + messages[0].getMessageBody()+" from : "+messages[0].getDisplayOriginatingAddress());
                    }
                }else{
                    Log.d(CommonMethod.getTag(this.getClass()),"pdus is null");
                }
            }else {
                Log.d(CommonMethod.getTag(this.getClass()),"bundle is null");
            }
        }
    }

    public class SendDataToServer{



    }


}
