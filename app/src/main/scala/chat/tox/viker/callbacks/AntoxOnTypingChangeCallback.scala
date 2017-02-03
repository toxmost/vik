package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.data.State
import chat.tox.viker.tox.ToxSingleton
import chat.tox.viker.wrapper.FriendInfo

class AntoxOnTypingChangeCallback(private var ctx: Context) {

  def friendTyping(friendInfo: FriendInfo, isTyping: Boolean)(state: Unit): Unit = {
    ToxSingleton.typingMap.put(friendInfo.key, isTyping)
    State.typing.onNext(true)
  }
}
