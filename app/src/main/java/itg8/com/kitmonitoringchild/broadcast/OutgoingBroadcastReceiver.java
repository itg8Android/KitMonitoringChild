package itg8.com.kitmonitoringchild.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by itg_Android on 1/3/2017.
 */

public class OutgoingBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction (). equals (Intent. ACTION_NEW_OUTGOING_CALL)) {
// If it is to call (outgoing)
//            Intent i = new Intent(context, OutgoingCallScreenDisplay.class);
//            i.putExtras(intent);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
            Log.d(OutgoingBroadcastReceiver.class.getSimpleName(), "outgoing from: "+intent.getExtras().getString("android.intent.extra.PHONE_NUMBER"));
        }
    }
}
