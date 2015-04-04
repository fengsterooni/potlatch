package org.coursera.androidcapstone.potlatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.interfaces.GiftSvcApi;
import org.coursera.androidcapstone.potlatch.interfaces.TaskCallback;
import org.coursera.androidcapstone.potlatch.models.Gift;
import org.coursera.androidcapstone.potlatch.models.User;
import org.coursera.androidcapstone.potlatch.services.CallableTask;
import org.coursera.androidcapstone.potlatch.services.GiftSvc;

import java.util.Collection;
import java.util.concurrent.Callable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 
 * This application uses ButterKnife. AndroidStudio has better support for
 * ButterKnife than Eclipse, but Eclipse was used for consistency with the other
 * courses in the series. If you have trouble getting the login button to work,
 * please follow these directions to enable annotation processing for this
 * Eclipse project:
 * 
 * http://jakewharton.github.io/butterknife/ide-eclipse.html
 * 
 */
public class LoginScreenActivity extends ActionBarActivity {
    private static final String TAG = LoginScreenActivity.class.getSimpleName();

	@InjectView(R.id.userName)
	protected EditText userName_;

	@InjectView(R.id.password)
	protected EditText password_;

	@InjectView(R.id.server)
	protected EditText server_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);

		ButterKnife.inject(this);
	}

	@OnClick(R.id.loginButton)
	public void login() {
		String user = userName_.getText().toString();
		String pass = password_.getText().toString();
		String server = server_.getText().toString();
        
        PotlatchApplication.currentUser = new User(userName_.getText().toString(), 0, 0);
		final GiftSvcApi svc = GiftSvc.init(server, user, pass);
        PotlatchApplication.svc = svc;

		CallableTask.invoke(new Callable<Collection<Gift>>() {

            @Override
            public Collection<Gift> call() throws Exception {
                Collection<Gift> list = svc.getGiftList();
                svc.addUser(PotlatchApplication.currentUser);
                Log.i(TAG, "Current User: " + userName_ + " added.");
                return list;
            }
        }, new TaskCallback<Collection<Gift>>() {

            @Override
            public void success(Collection<Gift> result) {
                // OAuth 2.0 grant was successful and we
                // can talk to the server, open up the gift listing
                startActivity(new Intent(
                        LoginScreenActivity.this,
                        MainActivity.class));
                Log.i("INFO", "User: " + PotlatchApplication.currentUser.getName() + " logged in.");
            }

            @Override
            public void error(Exception e) {
                Log.e(LoginScreenActivity.class.getName(), "Error logging in via OAuth.", e);

                Toast.makeText(
                        LoginScreenActivity.this,
                        "Login failed, check your Internet connection and credentials.",
                        Toast.LENGTH_SHORT).show();
            }
        });
	}

}
