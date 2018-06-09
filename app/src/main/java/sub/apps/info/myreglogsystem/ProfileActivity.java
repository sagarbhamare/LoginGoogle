package sub.apps.info.myreglogsystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.net.URI;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

  private GoogleSignInClient mGoogleSignInClient;
  private GoogleApiClient mGoogleApiClient;
  @BindView(R.id.user_collapsing_toolbar)CollapsingToolbarLayout collapsingToolbarLayout;
  @BindView(R.id.email)TextView tvEmail;
  @BindView(R.id.prfl_img)ImageView imgView;
  String personName,personGivenName,personFamilyName,personEmail,personId,url;
  Uri personPhoto;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ButterKnife.bind(this);
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        signOut();
      }
    });
    // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build();
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();
    mGoogleApiClient.connect();

    Bundle extras = getIntent().getExtras();
    String account = null;

    if (extras != null) {
      account = extras.getString("account");
      // and get whatever type user account id is
      JSONObject acct= null;
      try {
        acct = new JSONObject(account);

       personName = acct.getString("displayName");
       personGivenName = acct.getString("givenName");
       personFamilyName =  acct.getString("familyName");
       personEmail =  acct.getString("email");
       personId =  acct.getString("id");
       url=acct.getString("photoUrl");
       personPhoto = Uri.parse(url);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      Log.d("url",""+url);
    }
   collapsingToolbarLayout.setTitle(personName);
    tvEmail.setText(personEmail);
    Glide
        .with(this)
        .load(url)
        .into(imgView);
  }

  private void signOut1() {
    mGoogleSignInClient.signOut()
        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            Intent intent=new Intent(ProfileActivity.this,LoginActivity.class);
            startActivity(intent);
          }
        });
  }
  private void signOut() {
    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
        new ResultCallback<Status>() {
          @Override
          public void onResult(Status status) {
            // ...
            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
          }
        });
  }
}
