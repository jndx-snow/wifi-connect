package com.example.connectwifi.Activity;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.connectwifi.R;
import com.example.connectwifi.utils.wifitool;

import java.util.List;

public class MainActivity extends Activity {

    private EditText et_ssid, et_pwd;
    private Button btn_connect;
    private wifitool wifitool;
    private Context mContext;

    // get the wifi list
    private List<ScanResult> resultList;
    // label whether the wifi is exists or not
    private boolean isHave = false;
    // the capabilities of this wifi
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;

        wifitool = new wifitool(mContext);
       /* wifitool.startScan();
        resultList = wifitool.getWifiList();*/

        et_ssid = (EditText) findViewById(R.id.et_ssid);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        btn_connect = (Button) findViewById(R.id.btn_connect);

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHave = false;
                wifitool.openWifi();
                wifitool.startScan();
                resultList = wifitool.getWifiList();

                String ssid = et_ssid.getText().toString();
                String pwd = et_pwd.getText().toString();
                Log.e("检查wifi的数量: ", String.valueOf(resultList.size()));

                // check whether this wifi is exists or not
                if (resultList.size() > 0 && (!ssid.equals("") || ssid != "")) {
                    for (int i = 0; i < resultList.size(); i++) {
                        if (resultList.get(i).SSID.equals(ssid)) {
                            isHave = true;
                            String capabilities = resultList.get(i).capabilities;
                            type = wifitool.getTypeofPwd(capabilities);
                        }
                    }
                }
                Log.e("检查" + ssid + "的加密类型: ", String.valueOf(type));
                // check encryption type of this wifi
                if (isHave) {
                    int flag=0;
                    if (type != 1) {
                        if(pwd.equals("") || pwd == "") {
                            Toast.makeText(mContext, "请输入 " + ssid + " 的密码!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "正在连接 " + ssid,
                                    Toast.LENGTH_SHORT).show();
                           flag=wifitool.addNetwork(wifitool.CreateWifiInfo(ssid, pwd, type));
                        }
                    }else if(type==1){
                        Toast.makeText(mContext, "正在连接 " + ssid,
                                Toast.LENGTH_SHORT).show();
                        flag=wifitool.addNetwork(wifitool.CreateWifiInfo(ssid,pwd, type));
                    }
                    if(flag==0){
                        Toast.makeText(mContext, "连接 " + ssid+" 失败!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "wifi " + ssid + " 不存在!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        resultList.clear();
        resultList = wifitool.getWifiList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resultList.clear();
    }
}
