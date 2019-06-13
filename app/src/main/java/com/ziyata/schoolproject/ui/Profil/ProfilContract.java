package com.ziyata.schoolproject.ui.Profil;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;

import com.ziyata.schoolproject.model.login.LoginData;

public interface ProfilContract {

    interface View{
        void showProgress();
        void hideProgress();
        void showSuccessUpdate(String message);
        void showDataUser(LoginData loginData);

        boolean onCreateOptionsMenu(Menu menu, MenuInflater inflater);
    }

    interface Presenter{
        void updateDataUser(Context context, LoginData loginData);
        void getDataUser(Context context);
        void logoutSession(Context context);
    }
}
