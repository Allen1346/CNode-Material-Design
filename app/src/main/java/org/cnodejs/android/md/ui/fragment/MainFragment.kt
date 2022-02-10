package org.cnodejs.android.md.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.core.view.GravityCompat
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentMainBinding
import org.cnodejs.android.md.model.entity.Tab
import org.cnodejs.android.md.ui.adapter.TopicListAdapter
import org.cnodejs.android.md.ui.holder.LoadMoreFooter
import org.cnodejs.android.md.ui.listener.TopicDetailNavigateListener
import org.cnodejs.android.md.ui.listener.UserDetailNavigateListener
import org.cnodejs.android.md.util.OnDoubleClickListener
import org.cnodejs.android.md.vm.AccountViewModel
import org.cnodejs.android.md.vm.MainViewModel
import org.cnodejs.android.md.vm.SettingViewModel
import org.cnodejs.android.md.vm.holder.setupView

class MainFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val tabViews = arrayOf(
            binding.navLayout.tabAll,
            binding.navLayout.tabGood,
            binding.navLayout.tabShare,
            binding.navLayout.tabAsk,
            binding.navLayout.tabJob,
            binding.navLayout.tabDev,
        )

        val accountViewModel: AccountViewModel by activityViewModels()
        val settingViewModel: SettingViewModel by activityViewModels()
        val mainViewModel: MainViewModel by viewModels()
        observeBaseLiveHolder(mainViewModel.baseLiveHolder)

        val a = requireContext().obtainStyledAttributes(intArrayOf(android.R.attr.colorAccent))
        @ColorInt val colorAccent = a.getColor(0, Color.TRANSPARENT)
        a.recycle()

        accountViewModel.accountData.observe(viewLifecycleOwner) {
            it?.also { account ->
                binding.navLayout.imgAvatar.load(account.getCompatAvatarUrl()) {
                    placeholder(R.drawable.image_placeholder)
                }
                binding.navLayout.tvLoginName.text = account.loginName
                binding.navLayout.tvScore.text = getString(R.string.score_d, account.score)
                binding.navLayout.btnLogout.visibility = View.VISIBLE
            } ?: run {
                binding.navLayout.imgAvatar.load(R.drawable.image_placeholder)
                binding.navLayout.tvLoginName.setText(R.string.click_avatar_to_login)
                binding.navLayout.tvScore.text = null
                binding.navLayout.btnLogout.visibility = View.GONE
            }
        }

        settingViewModel.nightModeData.observe(viewLifecycleOwner) {
            it?.let { isNightMode ->
                if (isNightMode) {
                    binding.navLayout.btnDayNight.setImageResource(R.drawable.baseline_light_mode_24)
                    binding.navLayout.imgNavHeaderBackground.setImageResource(R.drawable.nav_header_bg_dark)
                } else {
                    binding.navLayout.btnDayNight.setImageResource(R.drawable.baseline_dark_mode_24)
                    binding.navLayout.imgNavHeaderBackground.setImageResource(R.drawable.nav_header_bg_light)
                }
            }
        }

        mainViewModel.topicPagingLiveHolder.tabData.observe(viewLifecycleOwner) {
            it?.let { tab ->
                binding.contentLayout.toolbar.setTitle(tab.titleId)
                for (tabView in tabViews) {
                    tabView.isChecked = tabView.id == tab.tabId
                }
            }
        }

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            // TODO
        })

        binding.contentLayout.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.contentLayout.toolbar.setOnClickListener(object : OnDoubleClickListener(400) {
            override fun onDoubleClick(v: View) {
                binding.contentLayout.recyclerView.scrollToPosition(0)
            }
        })

        binding.contentLayout.refreshLayout.setColorSchemeColors(colorAccent)
        binding.contentLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        val loadMoreFooter = LoadMoreFooter.create(binding.contentLayout.recyclerView)
        val adapter = TopicListAdapter()
        adapter.onTopicClickListener = TopicDetailNavigateListener(this)
        adapter.onUserClickListener = UserDetailNavigateListener(this)
        mainViewModel.topicPagingLiveHolder.setupView(viewLifecycleOwner, adapter, binding.contentLayout.refreshLayout, loadMoreFooter)
        loadMoreFooter.addToRecyclerView(binding.contentLayout.recyclerView)
        binding.contentLayout.recyclerView.adapter = adapter

        val onNavMyInfoClickListener = View.OnClickListener {
            accountViewModel.accountData.value?.also { account ->
                UserDetailFragment.open(this, account, binding.navLayout.imgAvatar)
            } ?: run {
                LoginFragment.open(this)
            }
        }
        binding.navLayout.imgAvatar.setOnClickListener(onNavMyInfoClickListener)
        binding.navLayout.tvLoginName.setOnClickListener(onNavMyInfoClickListener)
        binding.navLayout.tvScore.setOnClickListener(onNavMyInfoClickListener)

        binding.navLayout.btnDayNight.setOnClickListener {
            settingViewModel.toggleNightMode()
        }

        binding.navLayout.btnLogout.setOnClickListener {
            // TODO
        }

        val onNavTabClickListener = View.OnClickListener { v: View ->
            mainViewModel.topicPagingLiveHolder.switchTab(Tab.fromTabId(v.id))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        for (tabView in tabViews) {
            tabView.setOnClickListener(onNavTabClickListener)
        }

        binding.navLayout.btnMessage.setOnClickListener {
            // TODO
        }

        binding.navLayout.btnSetting.setOnClickListener {
            // TODO
        }

        binding.navLayout.btnAbout.setOnClickListener {
            // TODO
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    val secondBackPressedTime = System.currentTimeMillis()
                    if (secondBackPressedTime - mainViewModel.firstBackPressedTime > 2000) {
                        mainViewModel.firstBackPressedTime = secondBackPressedTime
                        showToast(getString(R.string.press_back_again_to_exit))
                    } else {
                        requireActivity().finish()
                    }
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }
}
