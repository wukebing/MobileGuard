package cn.edu.gdmec.android.mobileguard1.m2theftguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import cn.edu.gdmec.android.mobileguard1.R;

/**
 * Created by lenovo on 2017/10/15.
 */

public class LostFindActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mSafePhoneTV;
    private RelativeLayout mInterSetupRL;
    private SharedPreferences msharedPreferences;
    private ToggleButton mToggleButton;
    private TextView mProtectingStatusTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState );
        setContentView (R.layout.activity_lost_find );
        msharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        if(!isSetUp()){
            startSetup1Activity();
        }
        initView();
    }
    private boolean isSetUp(){
        return msharedPreferences.getBoolean("isSetUp",false);
    }
    private void initView() {
        TextView mTitleTV = (TextView) findViewById(R.id.tv_title);
        mTitleTV.setText("手机防盗");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.purple));
        mSafePhoneTV = (TextView) findViewById(R.id.tv_safephone);
        mSafePhoneTV.setText(msharedPreferences.getString("safephone",""));
        mToggleButton = (ToggleButton) findViewById(R.id.togglebtn_lostfind);
        mInterSetupRL = (RelativeLayout) findViewById(R.id.rl_inter_setup_wizard);
        mInterSetupRL.setOnClickListener(this);
        mProtectingStatusTV = (TextView) findViewById(R.id.tv_lostfind_protectstauts);
        boolean protecting = msharedPreferences.getBoolean("protecting",true);
        if(protecting){
            mProtectingStatusTV.setText("防盗保护已经开启");
            mToggleButton.setChecked(true);
        }else {
            mProtectingStatusTV.setText("防盗保护没有开启");
            mToggleButton.setChecked(false);
        }
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mProtectingStatusTV.setText("防盗保护已经开启");
                }else {
                    mProtectingStatusTV.setText("防盗保护没有开启");
                }
                SharedPreferences.Editor editor = msharedPreferences.edit();
                editor.putBoolean("protecting",isChecked);
                editor.commit();
            }
        });
    }

    private void startSetup1Activity() {
        Intent intent = new Intent ( LostFindActivity.this, cn.edu.gdmec.android.mobileguard1.m2theftguard.Setup1Activity.class );
        startActivity ( intent );
        finish ();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_inter_setup_wizard:
                startSetup1Activity();
                break;
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
}
