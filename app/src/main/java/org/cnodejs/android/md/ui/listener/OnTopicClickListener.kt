package org.cnodejs.android.md.ui.listener

import org.cnodejs.android.md.model.entity.ITopic
import org.cnodejs.android.md.ui.fragment.TopicDetailFragment
import org.cnodejs.android.md.util.Navigator

interface OnTopicClickListener {
    fun onTopicClick(topic: ITopic)
}

class TopicDetailNavigateListener(private val navigator: Navigator) : OnTopicClickListener {
    override fun onTopicClick(topic: ITopic) {
        TopicDetailFragment.open(navigator, topic)
    }
}
