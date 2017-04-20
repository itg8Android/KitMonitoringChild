package itg8.com.kitmonitoringchild.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import itg8.com.kitmonitoringchild.CommonMethod.CommonMethod;


/**
 * Created by itg_Android on 12/27/2016.
 */
public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = CommonMethod.getTag(MyAccessibilityService.class);

//    @Override
//    public void onCreate() {
//        getServiceInfo().flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
//    }



    private String getEventType(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                return "TYPE_NOTIFICATION_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                return "TYPE_VIEW_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                return "TYPE_VIEW_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                return "TYPE_VIEW_LONG_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                return "TYPE_VIEW_SELECTED";
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                return "TYPE_WINDOW_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                return "TYPE_VIEW_TEXT_CHANGED";
        }
        return "default";
    }

    private final AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    @Override
    public void onServiceConnected() {
        Cursor query = getContentResolver().query(Uri.parse("content://com.android.chrome.browser/bookmarks"), null, "bookmark!=1", null, "date DESC");
//        Log.d(TAG,"query : "+query.getCount());
        // Set the type of events that this service wants to listen to.
        //Others won't be passed to this service.
        info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED |
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;

        // If you only want this service to work with specific applications, set their
        // package names here.  Otherwise, when the service is activated, it will listen
        // to events from all applications.
        //info.packageNames = new String[]
        //{"com.appone.totest.accessibility", "com.apptwo.totest.accessibility"};

        // Set the type of feedback your service will provide.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        } else {
            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        }

        // Default services are invoked only if no package-specific ones are present
        // for the type of AccessibilityEvent generated.  This service *is*
        // application-specific, so the flag isn't necessary.  If this was a
        // general-purpose service, it would be worth considering setting the
        // DEFAULT flag.

        // info.flags = AccessibilityServiceInfo.DEFAULT;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    public void onAccessibilityEvent(AccessibilityEvent event) {
     //   debug("On accessibility event");
//        getChromeUrl(event.getSource());

        if(AccessibilityEvent.eventTypeToString(event.getEventType()).contains("WINDOW")){
            AccessibilityNodeInfo nodeInfo = event.getSource();
            dfs(nodeInfo);
        }
//        Log.v(TAG, String.format(
//                "onAccessibilityEvent: [type] %s [class] %s [package] %s [time] %s [text] %s",
//                getEventType(event), event.getClassName(), event.getPackageName(),
//                event.getEventTime(), getEventText(event)));


    }

    private void speakToUser(String reportStr) {
        Log.d(TAG,reportStr);
    }

    public void dfs(AccessibilityNodeInfo info){
        if(info == null)
            return;
        if(info.getText() != null && info.getText().length() > 0)
            System.out.println(info.getText() + " class: "+info.getClassName());
        for(int i=0;i<info.getChildCount();i++){
            dfs(info.getChild(i));
        }
    }


    @Override
    public void onInterrupt() {

    }

    public void getChromeUrl(AccessibilityNodeInfo nodeInfo) {
        //Use the node info tree to identify the proper content.
        //For now we'll just log it to logcat.
        Log.d(TAG, toStringHierarchy(nodeInfo, 0));
    }
    private String toStringHierarchy(AccessibilityNodeInfo info, int depth) {
        if (info == null) return "";

        String result = "|";
        for (int i = 0; i < depth; i++) {
            if (result.contains("http")) {
                Log.d(TAG, "Found URL!!!!!!!!!!!!!!" + result);
            }
            result += "  ";
        }

        result += info.toString();

        for (int i = 0; i < info.getChildCount(); i++) {
            result += "\n" + toStringHierarchy(info.getChild(i), depth + 1);
        }

        return result;
    }
    private static void debug(Object object) {
        Log.d(TAG, object.toString());
    }


}
