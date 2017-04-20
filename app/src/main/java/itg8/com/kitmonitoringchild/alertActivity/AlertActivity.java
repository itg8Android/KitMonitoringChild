package itg8.com.kitmonitoringchild.alertActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import itg8.com.kitmonitoringchild.CommonMethod.CommonMethod;
import itg8.com.kitmonitoringchild.CommonMethod.SharePrefrancClass;
import itg8.com.kitmonitoringchild.R;

public class AlertActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_alert)
    ImageView imgAlert;
    @BindView(R.id.showLoginRelative)
    RelativeLayout showRelative;
    @BindView(R.id.content_alert)
    RelativeLayout contentAlert;
    @BindView(R.id.hideImageRelative)
    RelativeLayout hideRelative;
    @BindView(R.id.edt_mobile)
    EditText edtMobile;
    @BindView(R.id.FramLayout)
    FrameLayout FramLayout;
    @BindView(R.id.btn_sendSms)
    Button btn_sendSms;
    @BindView(R.id.edt_smsMessage)
    EditText edtSmsMessage;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private String mobile;
    private String messageSend;
    private String parentMobileNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        ButterKnife.bind(this);
        setView();


    }

    private void setView() {
        setSupportActionBar(toolbar);
        CommonMethod.toggleView(showRelative,hideRelative);
        toolbar.setTitle("Send Message");
        imgAlert.setOnClickListener(this);
        mobile=edtMobile.getText().toString();
         messageSend = edtSmsMessage.getText().toString();
       parentMobileNo= SharePrefrancClass.getInstance(this).getPref(CommonMethod.SHAREDCHILD);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_alert:
                CommonMethod.toggleView(hideRelative, showRelative);
                break;
            case R.id.btn_sendSms:
                sendSMS(mobile,messageSend);
        }
    }

    private void sendSMS(String mobile, String messageSend) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(mobile, parentMobileNo, messageSend, null, null);
    }


}
