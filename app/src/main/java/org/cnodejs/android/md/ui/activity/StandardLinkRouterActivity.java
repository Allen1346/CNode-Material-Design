package org.cnodejs.android.md.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.cnodejs.android.md.R;
import org.cnodejs.android.md.ui.util.Navigator;
import org.cnodejs.android.md.ui.util.ToastUtils;

public class StandardLinkRouterActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Navigator.openStandardLink(this, getIntent().getDataString())) {
            ToastUtils.with(this).show(R.string.invalid_link);
        }
        finish();
    }

}
