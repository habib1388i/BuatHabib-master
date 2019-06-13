package com.ziyata.schoolproject.ui.Profil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ziyata.schoolproject.R;
import com.ziyata.schoolproject.model.login.LoginData;
import com.ziyata.schoolproject.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements ProfilContract.View {

    @BindView(R.id.picture)
    CircleImageView picture;
    @BindView(R.id.fabChoosePic)
    FloatingActionButton fabChoosePic;
    @BindView(R.id.layoutPicture)
    RelativeLayout layoutPicture;
    @BindView(R.id.edt_nama)
    EditText edtNama;
    @BindView(R.id.edt_alamat)
    EditText edtAlamat;
    @BindView(R.id.edt_notelp)
    EditText edtNotelp;
    @BindView(R.id.spin_gender)
    Spinner spinGender;
    @BindView(R.id.layoutProfil)
    CardView layoutProfil;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.layoutJenkel)
    CardView layoutJenkel;

    private ProfilPresenter mProfilpresenter = new ProfilPresenter(this);

    private String idSiswa, nama, alamat, notelp;
    private int gender;
    private Menu action;

    private String mGender;
    private static final int GENDER_MALE = 1;
    private static final int GENDER_FEMALE = 2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setupSpinner();

        mProfilpresenter.getDataUser(this);

    }

    private void setupSpinner() {
        final ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender_options, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinGender.setAdapter(genderSpinnerAdapter);
        spinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selection = (String) adapterView.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = "L";
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = "P";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("saving...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showSuccessUpdate(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        mProfilpresenter.getDataUser(this);
    }

    @Override
    public void showDataUser(LoginData loginData) {
        idSiswa = loginData.getId_user();
        nama = loginData.getNamaSiswa();
        alamat = loginData.getAlamat();
        notelp = loginData.getNoTelp();
        if (loginData.getJenkel().equals("L")) {
            gender = 1;
        } else {
            gender = 2;
        }
        if (!TextUtils.isEmpty(idSiswa)) {
            ((AppCompatActivity) this).getSupportActionBar().setTitle("Profil" + nama);
            edtNama.setText(nama);
            edtAlamat.setText(alamat);
            edtNotelp.setText(notelp);
            switch (gender) {
                case GENDER_MALE:
                    Log.i("cek male", String.valueOf(gender));
                    spinGender.setSelection(0);
                    break;

                case GENDER_FEMALE:
                    spinGender.setSelection(1);
                    break;
            }
        } else {
            ((AppCompatActivity) this).getSupportActionBar().setTitle("Profil");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.fabChoosePic, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fabChoosePic:
                showFileChooser();
                break;
            case R.id.btn_logout:
                mProfilpresenter.logoutSession(this);
                this.finish();
                break;
        }
    }

    private void showFileChooser() {
        Intent intentGalery = new Intent(Intent.ACTION_PICK);
        intentGalery.setType("image/*");
        intentGalery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentGalery, "Select picture"), Constant.REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                editMode();
                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);
                return true;
            case R.id.menu_save:
                if (!TextUtils.isEmpty(idSiswa)) {
                    if (TextUtils.isEmpty(edtNama.getText().toString()) ||
                            TextUtils.isEmpty(edtAlamat.getText().toString()) ||
                            TextUtils.isEmpty(edtNotelp.getText().toString())) {
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
                        alertdialog.setMessage("please compalate the field!");
                        alertdialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertdialog.show();
                    } else {
                        LoginData loginData = new LoginData();
                        loginData.setId_user(idSiswa);
                        loginData.setNamaSiswa(edtNama.getText().toString());
                        loginData.setAlamat(edtAlamat.getText().toString());
                        loginData.setNoTelp(edtNotelp.getText().toString());
                        loginData.setJenkel(mGender);
                        mProfilpresenter.updateDataUser(this, loginData);
                        readMode();
                        action.findItem(R.id.menu_edit).setVisible(true);
                        action.findItem(R.id.menu_save).setVisible(false);
                    }
                }else {
                    readMode();
                    action.findItem(R.id.menu_edit).setVisible(true);
                    action.findItem(R.id.menu_save).setVisible(false);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    @SuppressLint("RestrictedApi")
    private void readMode() {
        edtNama.setFocusableInTouchMode(true);
        edtAlamat.setFocusableInTouchMode(true);
        edtNotelp.setFocusableInTouchMode(true);
        spinGender.setEnabled(true);
        fabChoosePic.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("RestrictedApi")
    private void editMode() {
        edtNama.setFocusableInTouchMode(true);
        edtNotelp.setFocusableInTouchMode(true);
        edtAlamat.setFocusableInTouchMode(true);
        edtNama.setFocusable(false);
        edtNama.setFocusable(false);
        edtNama.setFocusable(false);
        spinGender.setEnabled(true);
        fabChoosePic.setVisibility(View.INVISIBLE);

    }
}
