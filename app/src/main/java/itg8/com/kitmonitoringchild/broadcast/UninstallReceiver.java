package itg8.com.kitmonitoringchild.broadcast;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Android itg 8 on 2/9/2017.
 */

public class UninstallReceiver extends DeviceAdminReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // fetching package names from extras
        String[] packageNames = intent.getStringArrayExtra("android.intent.extra.PACKAGES");

        if (packageNames != null) {
            for (String packageName : packageNames) {
                if (packageName != null && packageName.equals("itg8.com.kitmonitoringchild")) {
                    // start your activity here and ask the user for the password
                    Toast.makeText(context, "Ask password here", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        Toast.makeText(context, "Request disable", Toast.LENGTH_SHORT).show();
        return super.onDisableRequested(context, intent);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, "Desabled", Toast.LENGTH_SHORT).show();
    }

}