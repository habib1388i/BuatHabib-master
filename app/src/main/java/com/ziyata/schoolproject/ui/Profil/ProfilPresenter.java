package com.ziyata.schoolproject.ui.Profil;

import android.content.Context;
import android.content.SharedPreferences;

import com.ziyata.schoolproject.data.remote.ApiClient;
import com.ziyata.schoolproject.data.remote.ApiInterface;
import com.ziyata.schoolproject.model.login.LoginData;
import com.ziyata.schoolproject.model.login.LoginResponse;
import com.ziyata.schoolproject.utils.Constant;
import com.ziyata.schoolproject.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilPresenter implements ProfilContract.Presenter {

    private final ProfilContract.View view;
    private SharedPreferences pref;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    public ProfilPresenter(ProfilContract.View view) {
        this.view = view;
    }

    @Override
    public void updateDataUser(final Context context, final LoginData loginData) {
        view.showProgress();

        Call<LoginResponse> call = apiInterface.updateUser(Integer.valueOf(
                loginData.getId_user()),
                Integer.valueOf(loginData.getIdKelas()),
                loginData.getNamaSiswa(),
                loginData.getAlamat(),
                loginData.getJenkel(),
                loginData.getNoTelp());

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                view.hideProgress();

                if (response.isSuccessful() && response.body() != null){
                    if (response.body().getResult() == 1){
                        pref = context.getSharedPreferences(Constant.pref_name,0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constant.KEY_USER_NAMA, loginData.getNamaSiswa());
                        editor.putString(Constant.KEY_USER_ALAMAT, loginData.getAlamat());
                        editor.putString(Constant.KEY_USER_NOTELP, loginData.getNoTelp());
                        editor.putString(Constant.KEY_USER_JENKEL,loginData.getJenkel());
                        editor.apply();
                        view.showSuccessUpdate(response.body().getMessage());
                    }else {
                        view.showSuccessUpdate(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                view.hideProgress();
                view.showSuccessUpdate(t.getMessage());
            }
        });
    }

    @Override
    public void getDataUser(Context context) {
        pref = context.getSharedPreferences(Constant.pref_name,0);

        LoginData loginData = new LoginData();

        loginData.setId_user(pref.getString(Constant.KEY_USER_ID,""));
        loginData.setNamaSiswa(pref.getString(Constant.KEY_USER_NAMA,""));
        loginData.setAlamat(pref.getString(Constant.KEY_USER_ALAMAT,""));
        loginData.setNoTelp(pref.getString(Constant.KEY_USER_NOTELP,""));
        loginData.setJenkel(pref.getString(Constant.KEY_USER_JENKEL,""));

        view.showDataUser(loginData);

    }

    @Override
    public void logoutSession(Context context) {
        SessionManager msessionManager = new SessionManager(context);
        msessionManager.logout();
    }
}
