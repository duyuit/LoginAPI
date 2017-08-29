package com.example.vukhachoi.loginapi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;

//
//public class MainActivity extends AppCompatActivity {
//
//
//    private LoginButton loginButton;
//    private CallbackManager callbackManager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        try {
////            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.example.vukhachoi.loginapi", PackageManager.GET_SIGNATURES);
////            for(Signature signature: packageInfo.signatures){
////                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
////                messageDigest.update(signature.toByteArray());
////                Log.d("KeyHash", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
////            }
////        }catch (Exception e){
////        }
//
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//
//        setContentView(R.layout.activity_main);
//        loginButton = (LoginButton)findViewById(R.id.login_button);
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                loginResult.getAccessToken().
//                Toast.makeText(MainActivity.this, loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(MainActivity.this,"Login attempt canceled.", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//                Toast.makeText(MainActivity.this,"Login attempt failed.", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    ProfilePictureView profilePictureView;

    TextView txtMail;//=findViewById(R.id.txtMail);
    TextView txtName;//=findViewById(R.id.txtName);
    ImageView imgGG;//=findViewById(R.id.imgGG);

    TextView txtMailFB;
    TextView txtNameFB;

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        profilePictureView=(ProfilePictureView) findViewById(R.id.imgFB);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                ResultLogin();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this,"Login attempt canceled.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this,"Login attempt failed.", Toast.LENGTH_LONG).show();
            }
        });

        //setContentView(R.layout.activity_main);

        Anhxa();
        // Button listeners
        findViewById(R.id.btnSignIn).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        SignInButton signInButton = (SignInButton) findViewById(R.id.btnSignIn);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());


        // [END customize_button]

    }

    private void ResultLogin() {
        GraphRequest graphRequest=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("json",response.getJSONObject().toString());
                try {
                    txtMailFB.setText(txtMailFB.getText()+": "+object.getString("email").toString());
                    txtNameFB.setText(txtNameFB.getText()+": "+object.getString("name").toString());
                    profilePictureView.setProfileId(Profile.getCurrentProfile().getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    private void Anhxa() {
         txtMail=findViewById(R.id.txtMail);
         txtName=findViewById(R.id.txtName);
         imgGG=findViewById(R.id.imgGG);

         txtMailFB=findViewById(R.id.txtMailFB);
         txtNameFB=findViewById(R.id.txtNameFB);



    }


    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            txtName.setText(txtName.getText()+": "+acct.getDisplayName());
            txtMail.setText(txtMail.getText()+": "+acct.getEmail());
            Picasso.with(this).load(acct.getPhotoUrl()).into(imgGG);

        } else {
            // Signed out, show unauthenticated UI.

        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START revokeAccess]

    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                signIn();
                break;
        }
    }
}
