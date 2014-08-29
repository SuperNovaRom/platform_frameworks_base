package com.android.systemui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback;

public class BatteryPercentView extends TextView implements
		BatteryStateChangeCallback {
	
	BatteryController mBatteryController;
	Context mContext;

	public BatteryPercentView(Context context) {
		this(context, null, 0);
	}

    public BatteryPercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryPercentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTextSize(11);
        mContext = context;
        SettingsObserver observer = new SettingsObserver(new Handler());
        observer.observe();
    }
    
    public void addBatteryController(BatteryController b) {
    	mBatteryController = b;
    	b.addStateChangedCallback(this);
    }

	@Override
	public void onBatteryLevelChanged(int level, boolean plugged) {
		setText(level+"");
	}
	
	class SettingsObserver extends ContentObserver {

		public SettingsObserver(Handler handler) {
			super(handler);
		}
		
		public void observe() {
			ContentResolver cont = mContext.getContentResolver();
			cont.registerContentObserver(Settings.System.getUriFor(
					Settings.System.SHOW_BATTERY_PERCENT), false, this,
					UserHandle.USER_ALL);
			update();
		}
		
		@Override
		public void onChange(boolean selfChange) {
			update();
		}
		
		private void update() {
			if(Settings.System.getInt(mContext.getContentResolver(),
					Settings.System.SHOW_BATTERY_PERCENT, 1) == 0)
				setVisibility(View.GONE);
			else
				setVisibility(View.VISIBLE);
		}
	}

}
