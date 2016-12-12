package com.zhuoxin.News.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.zhuoxin.News.R;
import com.zhuoxin.News.entity.RegisterInfo;
import com.zhuoxin.News.utils.MD5Util;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    @InjectView(R.id.et_user_id)
    EditText et_user_id;
    @InjectView(R.id.et_user_password)
    EditText et_user_password;
    @InjectView(R.id.et_user_email)
    EditText et_user_email;
    @InjectView(R.id.btn_register)
    Button btn_register;
    @InjectView(R.id.btn_login)
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                String user_id = et_user_id.getText().toString();
                String user_password = et_user_password.getText().toString();
                String user_email = et_user_email.getText().toString();
                if (user_id == null || user_email == null || user_password == null) {
                    Toast.makeText(this, "信息不能为空,请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    regiest(user_id, user_password, user_email);
                }
                break;
        }
    }

    private void regiest(String user_id, String user_password, String user_email) {
        //将注册密码用MD5加密
        user_password = MD5Util.getMD5(user_password);
        //用Volley进行网络数据处理
        String url = "http://118.244.212.82:9092/newsClient/user_register?ver=1&uid=" + user_id + "&pwd=" + user_password + "&email=" + user_email;
        //创建一个请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //创建请求(网络地址,成功的返回,失败的返回)
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            //注册成功的返回,返回的内容全部在response中
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                RegisterInfo registerInfo = gson.fromJson(response, RegisterInfo.class);
                //如果返回的状态码为0,表示正确返回
                if (registerInfo.getStatus() == 0) {
                    Toast.makeText(RegisterActivity.this, registerInfo.getData().getExplain(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {//注册失败的返回
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "注册失败,请重试", Toast.LENGTH_SHORT).show();
            }
        });
        //将请求添加到队列中
        requestQueue.add(stringRequest);
    }

}
