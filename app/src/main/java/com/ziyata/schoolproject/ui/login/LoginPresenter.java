package com.ziyata.schoolproject.ui.login;

import android.content.Context;
import android.util.Log;

import com.ziyata.schoolproject.data.remote.ApiClient;
import com.ziyata.schoolproject.data.remote.ApiInterface;
import com.ziyata.schoolproject.model.login.LoginData;
import com.ziyata.schoolproject.model.login.LoginResponse;
import com.ziyata.schoolproject.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements LoginContract.Presenter {
    LoginContract.View view;
    ApiInterface apiLoginInterface = ApiClient.getClient().create(ApiInterface.class);

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }


    @Override
    public void whenLogin(String email, String password) {
        if (email == null || email.isEmpty()){
            view.onFailed("Masukan Email Anda");
            return;
        }
        if (password == null || password.isEmpty()) {
            view.onFailed("Masukan Password Anda");
            return;
    }
    view.showProgress();
        Call<LoginResponse> loginResponseCall = apiLoginInterface.loginUser(email, password);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().getResult() == 1) {
                        view.onSukses(response.body().getMessage(), response.body().getData());
                    } else {
                        view.onFailed(response.body().getMessage());
                    }
                } else {
                    view.onFailed("Data Kosong");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                view.hideProgress();
                    Log.e("Error_request", t.getMessage());
                view.onFailed("Login Anda Gagal .. ");
            }
            });
    }

    @Override
    public void saveDataUser(Context context, LoginData loginData) {
        SessionManager sm = new SessionManager(context);
        sm.createSession(loginData);
    }
}
