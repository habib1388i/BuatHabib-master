package com.ziyata.schoolproject.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ziyata.schoolproject.R;
import com.ziyata.schoolproject.model.login.LoginData;
import com.ziyata.schoolproject.ui.main.MainActivity;
import com.ziyata.schoolproject.ui.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {


    @BindView(R.id.edt_email_login)
    EditText edtEmailLogin;
    @BindView(R.id.edt_pass_login)
    EditText edtPassLogin;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private ProgressDialog progressDialog;
    LoginPresenter loginPresenter ;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        getData();
        loginPresenter = new LoginPresenter(LoginActivity.this);
        loginPresenter.whenLogin(email, password);

    }

    private void getData() {
        email = edtEmailLogin.getText().toString();
        password = edtPassLogin.getText().toString();
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(" Loading .. ");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();

    }

    @Override
    public void onSukses(String msg, LoginData body) {

        Toast.makeText(this, "Login Anda Berhasil....", Toast.LENGTH_SHORT).show();
        loginPresenter.saveDataUser(LoginActivity.this, body);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void goRegister(View view)  {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
