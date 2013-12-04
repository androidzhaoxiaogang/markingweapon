package com.edu.markingweapon;

import com.edu.markingweapon.adapter.TextWatcherAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.KeyEvent;
import android.widget.TextView.OnEditorActionListener;
import android.view.inputmethod.EditorInfo;

public class MainActivity extends Activity {

	private AutoCompleteTextView emailText;
	private EditText passwordText;
	private Button signinButton;

	private String email;
	private String password;
	
	private TextWatcher watcher = validationTextWatcher();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		setupViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void handleLogin(View view) {

	}

	/*********************** private apis **************************************/
	private void setupViews() {
		emailText = (AutoCompleteTextView) findViewById(R.id.et_email);
		passwordText = (EditText) findViewById(R.id.et_password);
		signinButton = (Button) findViewById(R.id.b_signin);

//		emailText.setAdapter(new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, userEmailAccounts()));

		passwordText.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event != null && KeyEvent.ACTION_DOWN == event.getAction()
						&& keyCode == KeyEvent.KEYCODE_ENTER && signinButton.isEnabled()) {
					handleLogin(signinButton);
					return true;
				}
				return false;
			}
		});

		passwordText.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE && signinButton.isEnabled()) {
					handleLogin(signinButton);
					return true;
				}
				return false;
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

}
