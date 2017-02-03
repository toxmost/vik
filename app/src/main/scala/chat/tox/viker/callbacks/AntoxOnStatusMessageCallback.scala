package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.data.State
import chat.tox.viker.wrapper.FriendInfo
import im.tox.tox4j.core.data.ToxStatusMessage

class AntoxOnStatusMessageCallback(private var ctx: Context) {

  def friendStatusMessage(friendInfo: FriendInfo, message: ToxStatusMessage)(state: Unit): Unit = {

    val db = State.db
    db.updateContactStatusMessage(friendInfo.key, message)
  }
}
