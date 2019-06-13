package com.ziyata.schoolproject.ui.register;

import com.ziyata.schoolproject.model.login.LoginData;

public interface RegisterContract {
    interface View{
        void showProgress();
        void hideProgress();
        void showError(String message);
        void showRegisterSucces(String message);
    }

    interface Presenter{
        void doRegisterUser(LoginData loginData);
    }
}
