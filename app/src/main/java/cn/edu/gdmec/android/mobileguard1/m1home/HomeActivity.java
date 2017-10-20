package cn.edu.gdmec.android.mobileguard1.m1home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import cn.edu.gdmec.android.mobileguard1.R;
import cn.edu.gdmec.android.mobileguard1.m1home.adapter.HomeAdapter;
import cn.edu.gdmec.android.mobileguard1.m2theftguard.dialog.utils.LostFindActivity;
import cn.edu.gdmec.android.mobileguard1.m2theftguard.dialog.utils.MD5Utils;
import cn.edu.gdmec.android.mobileguard1.m2theftguard.dialog.utils.dialog.InterPasswordDialog;
import cn.edu.gdmec.android.mobileguard1.m2theftguard.dialog.utils.dialog.SetUpPasswordDialog;

public class HomeActivity extends AppCompatActivity {
    private GridView gv_home;
    private long mExitTime;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        mSharedPreferences = getSharedPreferences ( "config", MODE_PRIVATE );
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter (HomeActivity.this));
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        if (isSetUpPassword()){
                            showInterPswdDialog();
                        }else {
                            showSetUpPswdDialog();
                        }
                        break;
                }
            }
        });
    }
    public void startActivity(Class<?> cls){
        Intent intent = new Intent ( HomeActivity.this,cls );
        startActivity ( intent );
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) < 2000) {
                System.exit(0);
            } else {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
                mExitTime=System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//弹出设置密码对话框
    private void showSetUpPswdDialog(){
        final SetUpPasswordDialog setUpPasswrodDialog = new SetUpPasswordDialog (HomeActivity.this );
        setUpPasswrodDialog.setCallBack (new SetUpPasswordDialog.MyCallBack (){
            @Override
            public void ok(){
                String firstPwsd = setUpPasswrodDialog.mFirstPWDET.getText().toString ().trim ();
                String affirmPwsd = setUpPasswrodDialog.mAffirmET.getText().toString ().trim ();
                if (!TextUtils.isEmpty ( firstPwsd )&&!TextUtils.isEmpty ( affirmPwsd )){
                    if (firstPwsd.equals (affirmPwsd )){
                        savePswd(affirmPwsd);
                        setUpPasswrodDialog.dismiss ();
                        showInterPswdDialog();
                    }else {
                        Toast.makeText ( HomeActivity.this, "两次密码不一致！", Toast.LENGTH_LONG ).show();
                    }
                }else{
                    Toast.makeText ( HomeActivity.this, "密码不能为空！", Toast.LENGTH_LONG ).show ();
                }
            }
            @Override
            public void cancel(){
                setUpPasswrodDialog.dismiss ();
            }
        } );
        setUpPasswrodDialog.setCancelable ( true );
        setUpPasswrodDialog.show ();
    }
    //弹出输入密码对话框
    private void showInterPswdDialog(){
        final String password = getPassword();
        final InterPasswordDialog mInPswdDialog = new InterPasswordDialog (HomeActivity.this );
        mInPswdDialog.setCallBack (new InterPasswordDialog.MyCallBack (){
            @Override
            public void confirm(){
                if (TextUtils.isEmpty ( mInPswdDialog.getPassword () )){
                    Toast.makeText ( HomeActivity.this, "密码不能为空！",Toast.LENGTH_LONG).show ();
                }else if (password.equals ( MD5Utils.encode ( mInPswdDialog.getPassword () ) )){
                    mInPswdDialog.dismiss ();
                    startActivity(LostFindActivity.class);
                    Toast.makeText ( HomeActivity.this, "可以进入手机防盗模块",Toast.LENGTH_LONG ).show ();
                }else {
                    mInPswdDialog.dismiss ();
                    Toast.makeText ( HomeActivity.this, "密码有误，请重新输入",Toast.LENGTH_LONG).show ();
                }
            }
            @Override
            public void cancel(){
                mInPswdDialog.dismiss ();
            }

            @Override
            public void cancle() {

            }
        });
        mInPswdDialog.setCancelable ( true );
        mInPswdDialog.show ();
    }
    private void savePswd(String affirmPwsd){
        SharedPreferences.Editor editor = mSharedPreferences.edit ();
        editor.putString ("PhoneAntiTheftPWD", MD5Utils.encode ( affirmPwsd ));
        editor.commit();
    }
    //获取密码
    private String getPassword(){
        String password = mSharedPreferences.getString ( "PhoneAntiTheftPWD", null );
        if (TextUtils.isEmpty ( password )){
            return "";
        }
        return password;
    }
    //判断用户是否设置手机防盗密码
    private boolean isSetUpPassword(){
        String password = mSharedPreferences.getString ( "PhoneAntiTheftPWD", null );
        if (TextUtils.isEmpty ( password )){
            return false;
        }
        return true;
    }
}