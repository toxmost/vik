package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.data.State
import chat.tox.viker.tox.MessageHelper
import chat.tox.viker.wrapper.FriendInfo
import im.tox.tox4j.core.data.ToxFriendMessage
import im.tox.tox4j.core.enums.ToxMessageType

class AntoxOnMessageCallback(private var ctx: Context) {

  def friendMessage(friendInfo: FriendInfo, messageType: ToxMessageType, timeDelta: Int, message: ToxFriendMessage)(state: Unit): Unit = {
    State.setLastIncomingMessageAction()
    MessageHelper.handleMessage(ctx, friendInfo, message, messageType)
  }
}
