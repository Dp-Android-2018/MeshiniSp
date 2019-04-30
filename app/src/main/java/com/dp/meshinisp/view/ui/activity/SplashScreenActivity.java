package com.dp.meshinisp.view.ui.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.dp.meshinisp.R;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class SplashScreenActivity extends BaseActivity {
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 3200;
    Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
//    private CustomUtils customUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            /* Create an Intent that will start the Menu-Activity. */
            if (customUtilsLazy.getValue().getSavedMemberData() != null) {
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
                ConfigurationFile.Constants.AUTHORIZATION = customUtilsLazy.getValue().getSavedMemberData().getApiToken();
            } else {
                Intent mainIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
