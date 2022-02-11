package org.cnodejs.android.md.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import org.cnodejs.android.md.R
import org.cnodejs.android.md.bus.AuthInvalidEvent
import org.cnodejs.android.md.ui.dialog.AuthInvalidAlertDialog
import org.cnodejs.android.md.vm.SettingViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NavHostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val settingViewModel: SettingViewModel by viewModels()
        settingViewModel.loadNightModeConfig()

        EventBus.getDefault().register(this)

        setContentView(R.layout.activity_nav_host)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAuthInvalid(event: AuthInvalidEvent) {
        AuthInvalidAlertDialog.show(supportFragmentManager)
    }
}
