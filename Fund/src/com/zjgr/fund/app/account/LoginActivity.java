/**
 * 
 * @fileName: LoginActivity.java
 * @description: 登录界面 
 * @author: jin pang
 * @date： 2013/12/14
 * @version: 1.0
 * @modify: 
 *
 */

package com.zjgr.fund.app.account;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zjgr.fund.R;
import com.zjgr.fund.app.BaseActivity;
import com.zjgr.fund.utils.ThreadPoolUtil;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private Button loginBtn;
	private Button cteateAccountBtn;
	private EditText accountEdit;
	private EditText pswEdit;
	ProgressDialog progress_dialog;
	private ImageView logo;
	private CheckBox checkbox;

	Intent intent = null;
	ComponentName componetName;

	public static final int KEY_PROGRESS_BEGIN = 0;
	public static final int KEY_LOGIN_BEGIN = 1;
	public static final int KEY_START_LOGIN = 2;
	public static final int KEY_LOGIN_SUCCESS = 3;
	public static final int KEY_LOGIN_FALSE = 4;
	public static final int KEY_LOGIN_USER_PASSWOR_NULL = 5;
	public static final int KEY_SELECTED_PROTOCOL = 6;

	private final Handler mHandle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case KEY_PROGRESS_BEGIN: {
				progress_dialog.show();
				break;
			}
			case KEY_LOGIN_BEGIN: {

				break;
			}
			case KEY_LOGIN_SUCCESS: {
				progress_dialog.dismiss();
				componetName = new ComponentName("com.zjgr.fund",
						"com.zjgr.fund.app.main.MainActivity");
				intent.setComponent(componetName);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
				break;
			}
			case KEY_LOGIN_FALSE: {
				progress_dialog.dismiss();
				Toast.makeText(
						LoginActivity.this,
						LoginActivity.this.getResources().getString(
								R.string.account_not_exist_or_wrong_password),
						Toast.LENGTH_LONG).show();
				break;
			}
			case KEY_LOGIN_USER_PASSWOR_NULL: {
				progress_dialog.dismiss();
				Toast.makeText(
						LoginActivity.this,
						LoginActivity.this.getResources().getString(
								R.string.enter_username_or_password),
						Toast.LENGTH_LONG).show();
				break;
			}
			case KEY_SELECTED_PROTOCOL: {
				progress_dialog.dismiss();
				Toast.makeText(
						LoginActivity.this,
						LoginActivity.this.getResources().getString(
								R.string.select_protocol),
						Toast.LENGTH_LONG).show();
			}
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		intView();
	}

	/**
	 * 初始化控件，创建点击事件监听
	 * 
	 * */
	@SuppressLint("ShowToast")
	private void intView() {
		progress_dialog = new ProgressDialog(this);
		progress_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress_dialog.setMessage(this.getResources().getString(
				R.string.progress_doing));
		progress_dialog.setCancelable(true);

		initView();

		loginBtn = (Button) findViewById(R.id.loginBtn);
		cteateAccountBtn = (Button) findViewById(R.id.createBtn);
		accountEdit = (EditText) findViewById(R.id.accountEdit);
		pswEdit = (EditText) findViewById(R.id.pswEdit);
		checkbox = (CheckBox) findViewById(R.id.checkBox1);
		checkbox.setSelected(true);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				buttonView.setSelected(isChecked);
			}
		});
		intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		String userCode = null;
		if (bundle != null) {
			userCode = bundle.getString("userCode");
			if (userCode != null && !"".equals(userCode.trim())) {
				accountEdit.setEnabled(false);
			}
		}
		accountEdit.setText(userCode);
		loginBtn.setOnClickListener(this);
		cteateAccountBtn.setOnClickListener(this);
		accountEdit.setOnClickListener(this);
		pswEdit.setOnClickListener(this);
		accountEdit.addTextChangedListener(accountWatcher);
		pswEdit.addTextChangedListener(pswWatcher);

		initData();
	}

	private TextWatcher accountWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String str = accountEdit.getText().toString();
			try {
				if (str != null && str.length() > 20) {
					str = str.substring(0, str.length() - 1);
					accountEdit.setText(str);
					accountEdit.setError("亲，账号太长了");
				}
			} catch (Exception e) {
			}
		}
	};

	private TextWatcher pswWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String str = pswEdit.getText().toString();
			String pwdRegEx = ".*([\u4e00-\u9fa5]+).*";
			try {
				Pattern p = Pattern.compile(pwdRegEx);
				Matcher m = p.matcher(str);
				if (str != null && str.length() > 20) {
					str = str.substring(0, 20 - 1);
					pswEdit.setText(str);
					pswEdit.setError(LoginActivity.this.getResources()
							.getString(R.string.password_too_long));
				} else if (m.matches()) {
					pswEdit.setError(LoginActivity.this.getResources()
							.getString(R.string.can_not_input_chinese));
				}
			} catch (Exception e) {
			}
		}
	};

	private void initView() {

		Display d = this.getWindowManager().getDefaultDisplay();
		int weidth = d.getWidth();
		int height = d.getHeight();
		LinearLayout loginLayout = (LinearLayout) findViewById(R.id.login_layout);
		if (weidth > height) {
			LinearLayout userNamePassLayout = (LinearLayout) findViewById(R.id.userNamePassLayout);
			LinearLayout btnLine = (LinearLayout) findViewById(R.id.btn_line_layout);
			LayoutParams layoutPara = new LinearLayout.LayoutParams(height,
					LayoutParams.WRAP_CONTENT);
			userNamePassLayout.setLayoutParams(layoutPara);
			btnLine.setLayoutParams(layoutPara);
			loginLayout.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_login_pad));
		} else {
			loginLayout.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_login_p));
		}

	}

	public void initData() {
		ThreadPoolUtil.execute(new Runnable() {
			//
			@Override
			public void run() {
				// RegisterBL.initData();
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 创建账号
		case R.id.createBtn:
			break;

		// 登录
		case R.id.loginBtn:
			String accountNo = accountEdit.getText().toString();
			String password = pswEdit.getText().toString();
			if (accountNo == null || "".equals(accountNo.trim())) {
				accountEdit.setError(LoginActivity.this.getResources()
						.getString(R.string.account_is_null));
				return;
			} else if (password == null || "".equals(password.trim())) {
				pswEdit.setError(LoginActivity.this.getResources().getString(
						R.string.password_is_null));
				return;
			} else {

				ThreadPoolUtil.execute(new Runnable() {
					//
					@Override
					public void run() {
						String accountNo = accountEdit.getText().toString();
						String password = pswEdit.getText().toString();
						if (accountNo == null || "".equals(accountNo.trim())) {
							mHandle.sendEmptyMessage(KEY_LOGIN_USER_PASSWOR_NULL);
							return;
						}
						mHandle.sendEmptyMessage(KEY_PROGRESS_BEGIN);
						if(!checkbox.isSelected()){
							mHandle.sendEmptyMessage(KEY_SELECTED_PROTOCOL);
							return;
						}
						Boolean flag = Boolean.TRUE;
						if (flag) {
							mHandle.sendEmptyMessage(KEY_LOGIN_SUCCESS);
						} else {
							mHandle.sendEmptyMessage(KEY_LOGIN_FALSE);
						}

					}
				});
			}
			// 跳转到主界面
			break;

		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
