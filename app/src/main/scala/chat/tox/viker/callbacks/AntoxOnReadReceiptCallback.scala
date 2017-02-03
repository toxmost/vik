package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.data.State
import chat.tox.viker.utils.AntoxLog
import chat.tox.viker.wrapper.FriendInfo

class AntoxOnReadReceiptCallback(private var ctx: Context) {
  def friendReadReceipt(friendInfo: FriendInfo, messageId: Int)(state: Unit): Unit = {
    val db = State.db
    db.setMessageReceived(messageId)
    AntoxLog.debug("read receipt, for message " + messageId)
  }
}
