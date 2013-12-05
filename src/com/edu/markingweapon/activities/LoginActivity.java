package com.edu.markingweapon.activities;

import java.util.HashMap;

import com.edu.markingweapon.R;
import com.edu.markingweapon.adapters.TextWatcherAdapter;
import com.edu.markingweapon.pojo.UserInfo;
import com.edu.markingweapon.utils.Constants;
import com.edu.markingweapon.utils.Toaster;

import fast.rocket.Rocket;
import fast.rocket.config.JsonCallback;
import fast.rocket.error.RocketError;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.KeyEvent;
import android.widget.TextView.OnEditorActionListener;
import android.view.inputmethod.EditorInfo;

public class LoginActivity extends Activity {

	private ProgressDialog progressDialog;
	private AutoCompleteTextView emailText;
	private EditText passwordText;
	private Button signinButton;

	private TextWatcher watcher = validationTextWatcher();
	
	private String email;
    private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		setupViews();
	}

	private void setupViews() {
		emailText = (AutoCompleteTextView) findViewById(R.id.et_email);
		passwordText = (EditText) findViewById(R.id.et_password);
		signinButton = (Button) findViewById(R.id.b_signin);

		// emailText.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_dropdown_item_1line, ));

		passwordText.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event != null && KeyEvent.ACTION_DOWN == event.getAction()
						&& keyCode == KeyEvent.KEYCODE_ENTER
						&& signinButton.isEnabled()) {
					handleLogin();
					return true;
				}
				return false;
			}
		});

		passwordText.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE
						&& signinButton.isEnabled()) {
					handleLogin();
					return true;
				}
				return false;
			}
		});

		signinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handleLogin();
			}
		});

		emailText.addTextChangedListener(watcher);
		passwordText.addTextChangedListener(watcher);
	}

	private TextWatcher validationTextWatcher() {
		return new TextWatcherAdapter() {
			public void afterTextChanged(Editable gitDirEditText) {
				updateUIWithValidation();
			}

		};
	}

	private void updateUIWithValidation() {
		boolean populated = populated(emailText) && populated(passwordText);
		signinButton.setEnabled(populated);
	}

	private boolean populated(EditText editText) {
		return editText.length() > 0;
	}

	private void showProgressDialog() {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage(getText(R.string.message_signing_in));
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				// TODO: cancel operation
			}
		});
		dialog.show();
		progressDialog = dialog;
	}

	private void dissmissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	private void handleLogin() {
		showProgressDialog();
		
		email = emailText.getText().toString();
        password = passwordText.getText().toString();

		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("method", "onLogin");
		params.put("account", email);
		params.put("passWord", password);

		Rocket.with(this).enableCookie(true)
				.requestParams(params)
				.targetType(UserInfo.class)
				.invoke(callback)
				.load(Constants.loginInfoUrl);
	}
	
	private JsonCallback<UserInfo> callback = new JsonCallback<UserInfo>() {

		@Override
		public void onCompleted(RocketError error, UserInfo result) {
			dissmissProgressDialog();
			if (error != null) {
				Toaster.errroMessage(error, LoginActivity.this);
			} else {
				
			}
		}
	};
}
