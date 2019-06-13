package com.ziyata.schoolproject.ui.login;

import android.content.Context;

import com.ziyata.schoolproject.model.login.LoginData;

public interface LoginContract {
    interface View {
        void showProgress();
        void hideProgress();
        void onSukses(String msg, LoginData body);
        void onFailed(String msg);

    }
    interface Presenter {
        void whenLogin(String email, String password);
        void saveDataUser(Context context, LoginData loginData);
    }
}
