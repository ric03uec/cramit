package com.dev.cramit;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey="dHpnS19TVm1xemV6SldCemZqUzJ1WXc6MQ", mode= ReportingInteractionMode.TOAST, forceCloseDialogAfterToast=true, resToastText=R.string.crashToastText)
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		ACRA.init(this);
		super.onCreate();
	}
}
