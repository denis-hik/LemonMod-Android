package ru.denishik.lemonmod;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private LinearLayout linear1;
	private ImageView imageview1;
	private ImageView imageview2;
	
	private TimerTask timer;
	private Intent i = new Intent();
	private SharedPreferences file;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		imageview1 = findViewById(R.id.imageview1);
		imageview2 = findViewById(R.id.imageview2);
		file = getSharedPreferences("filr", Activity.MODE_PRIVATE);
	}
	
	private void initializeLogic() {
		if (!file.contains("start")) {
			file.edit().putString("start", "1").commit();
			_showStartDialog();
		}
		else {
			timer = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							_toHome();
						}
					});
				}
			};
			_timer.schedule(timer, (int)(2000));
		}
	}
	
	public void _showStartDialog() {
		final com.google.android.material.bottomsheet.BottomSheetDialog dialog = new com.google.android.material.bottomsheet.BottomSheetDialog(MainActivity.this); View lay = getLayoutInflater().inflate(R.layout.start_view, null); dialog.setContentView(lay);
		final LinearLayout linear1 = (LinearLayout)lay.findViewById(R.id.linear1); 
		final com.google.android.material.button.MaterialButton go = lay.findViewById(R.id.go); 
		dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
		dialog.show();
		android.graphics.drawable.GradientDrawable wd = new android.graphics.drawable.GradientDrawable(); wd.setColor(Color.BLACK); wd.setCornerRadius((int)10f);
		linear1.setBackground(wd);
		go.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_toHome();
			}
		});
	}
	
	
	public void _toHome() {
		i.setAction(Intent.ACTION_VIEW);
		i.setClass(getApplicationContext(), HomeActivity.class);
		startActivity(i);
		finish();
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}
