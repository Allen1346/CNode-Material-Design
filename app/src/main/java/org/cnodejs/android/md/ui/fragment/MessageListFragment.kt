package org.cnodejs.android.md.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import org.cnodejs.android.md.R
import org.cnodejs.android.md.databinding.FragmentMessageListBinding
import org.cnodejs.android.md.util.Navigator
import org.cnodejs.android.md.vm.AccountViewModel
import org.cnodejs.android.md.vm.MessageListViewModel

class MessageListFragment : BaseFragment() {
    companion object {
        fun open(navigator: Navigator) {
            navigator.push(R.id.fragment_message_list)
        }
    }

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val messageListViewModel: MessageListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMessageListBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            navigator.back()
        }

        observeViewModel(messageListViewModel)

        accountViewModel.accountData.observe(viewLifecycleOwner) { account ->
            if (account == null) {
                navigator.back()
            }
        }

        return binding.root
    }
}
