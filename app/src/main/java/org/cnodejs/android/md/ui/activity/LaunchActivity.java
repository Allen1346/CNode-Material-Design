package org.cnodejs.android.md.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.ui.util.ActivityUtils;
import org.cnodejs.android.md.util.HandlerUtils;

public class LaunchActivity extends BaseActivity implements Runnable {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        if (!isTaskRoot()) {
            finish();
            return;
        }

        HandlerUtils.handler.postDelayed(this, 1000);
    }

    @Override
    protected void onDestroy() {
        HandlerUtils.handler.removeCallbacks(this);
        super.onDestroy();
    }

    @Override
    public void run() {
        if (ActivityUtils.isAlive(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
