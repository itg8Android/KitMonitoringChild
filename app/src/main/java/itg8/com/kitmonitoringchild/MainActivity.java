package itg8.com.kitmonitoringchild;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.kitmonitoringchild.CommonMethod.CommonMethod;
import itg8.com.kitmonitoringchild.alertActivity.AlertActivity;
import itg8.com.kitmonitoringchild.broadcast.UninstallReceiver;
import itg8.com.kitmonitoringchild.services.LocationService;
import itg8.com.kitmonitoringchild.services.MyAccessibilityService;
import itg8.com.kitmonitoringchild.services.MyService;
import itg8.com.kitmonitoringchild.services.SocialActivityTraceService;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_login)
    ImageView imgLogin;
    @BindView(R.id.edt_login)
    EditText edtLogin;
    @BindView(R.id.img_pswd)
    ImageView imgPswd;
    @BindView(R.id.edt_pswd)
    EditText edtPswd;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.txt_setError)
    TextView txtSetError;

    private DevicePolicyManager devicePolicyManager;
    private ComponentName demoDeviceAdmin;
    private UsageStatsManager mUsageStatsManager;
    private List<UsageStats> listOfApp;
    private final static int RC_LOCATION_SERVICE = 101;
    private final static int RC_SMS = 100;
    private static final int REQUEST_CODE_ENABLE_ADMIN = 102;
    private String paswd;
    private String loginChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setView();
        checkNetworkConnectivity();

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        demoDeviceAdmin = new ComponentName(this, UninstallReceiver.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE); //Context.USAGE_STATS_SERVICE
        }
        getRunningTasks();
        setAccesebility();
        startService(new Intent(this, SocialActivityTraceService.class));
        startService(new Intent(this, MyAccessibilityService.class));
//        sendLoginInfoTOServer();
    }

    private void sendLoginInfoTOServer() {

        if (!getValidate()) {
            getValidateMesaage();
        }
        else
        {
            //call webservice
             callLogin();
        }

    }

    private void callLogin() {

    }

    private void getValidateMesaage() {

    }

    private boolean getValidate() {

        boolean valid = true;
         if(loginChild.isEmpty())
         {
             txtSetError.setVisibility(View.VISIBLE);
             txtSetError.setText("Field is empty");
             valid= false;
         }
        else if(isvalidateMobile(loginChild))
         {
             txtSetError.setVisibility(View.VISIBLE);
             txtSetError.setText("Field is empty");
             valid = false;
         }
        txtSetError.setVisibility(View.GONE);
        if(paswd.isEmpty())
        {
            txtSetError.setVisibility(View.VISIBLE);
            txtSetError.setText("Field is empty");
            valid= false;
        }


        return valid;
    }

    private boolean isvalidateMobile(String loginChild) {
         return loginChild.length() < 10
                 || loginChild.length() > 10;

    }


    private void setView() {
        setSupportActionBar(toolbar);
//        loginChild = edtLogin.getText().toString();
//        paswd = edtLogin.getText().toString();
        btnLogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, AlertActivity.class));
    }


    private void setAccesebility() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    private void getRunningTasks() {
        ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        for (ActivityManager.RunningTaskInfo o :
                runningTasks) {
            Log.d(CommonMethod.getTag(this.getClass()), "running : " + o.topActivity.getPackageName());
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void accessUsageState(Context context) {
        listOfApp = getUsageStatistics(UsageStatsManager.INTERVAL_DAILY);
        for (UsageStats st :
                listOfApp) {
            Log.d(CommonMethod.getTag(this.getClass()), st.getPackageName() + " time start : " + st.getFirstTimeStamp() + " last time : " + st.getLastTimeUsed());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.YEAR, );

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        if (queryUsageStats.size() == 0) {
            Log.i(CommonMethod.getTag(this.getClass()), "The user may not allow the access to apps usage. ");

            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

        return queryUsageStats;
    }

    private boolean checkAdminEnabled() {
        DevicePolicyManager devicePolicyManager
                = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminComponentName
                = new ComponentName(this, UninstallReceiver.class);
        return devicePolicyManager.isAdminActive(deviceAdminComponentName);
    }

    @AfterPermissionGranted(RC_SMS)
    private void startGettingSMS() {
        startService(new Intent(this, MyService.class));
    }

    private void getDeviceAdminPerm() {
        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, demoDeviceAdmin);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "device admin enabled");
        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
        // return false - don't update checkbox until we're really active
    }


    @AfterPermissionGranted(RC_LOCATION_SERVICE)
    private void checkNetworkConnectivity() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG};
        if (EasyPermissions.hasPermissions(this, perms[0])) {
            startLocationService();
        }
        if (EasyPermissions.hasPermissions(this, perms[2])) {
            startGettingSMS();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.access_location),
                    RC_LOCATION_SERVICE, perms);
        }
    }


    private void startLocationService() {
        startService(new Intent(this, LocationService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }





}
