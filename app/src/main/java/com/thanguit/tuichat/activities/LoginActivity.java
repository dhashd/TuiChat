package com.thanguit.tuichat.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.thanguit.tuichat.R;
import com.thanguit.tuichat.animations.AnimationScale;
import com.thanguit.tuichat.databinding.ActivityLoginBinding;
import com.thanguit.tuichat.models.User;
import com.thanguit.tuichat.utils.LoadingDialog;
import com.thanguit.tuichat.utils.MyToast;

import java.util.Arrays;

public class LoginActivity extends AppCompat {
    private static final String TAG = "LoginActivity";
    private static final String TAG_1 = "Login with Google";
    private static final String TAG_2 = "Login with Facebook";
    private ActivityLoginBinding activityLoginBinding;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;

    private AnimationScale animationScale;
    private LoadingDialog loadingDialog;

    private static final String USER_DATABASE = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        callbackManager = CallbackManager.Factory.create();
        animationScale = AnimationScale.getInstance();
        loadingDialog = LoadingDialog.getInstance();

        initializeViews();
        listeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }
    }

    private void initializeViews() {
        animationScale.eventButton(this, activityLoginBinding.btnLoginPhoneNumber);


    }
    private void listeners() {
        activityLoginBinding.btnLoginPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PhoneNumberActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addUserIntoDatabase(User user) {
//        Uri avatar = Uri.parse(user.getAvatar());
        firebaseDatabase.getReference()
                .child(USER_DATABASE.trim())
                .child(user.getUid())
                .setValue(user)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        MyToast.makeText(LoginActivity.this, MyToast.ERROR, getString(R.string.toast3), MyToast.SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateUI();
                    }
                });
    }

    private void updateUI() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}