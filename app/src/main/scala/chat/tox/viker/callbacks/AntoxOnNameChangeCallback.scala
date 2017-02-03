package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.data.State
import chat.tox.viker.utils.UiUtils
import chat.tox.viker.wrapper.FriendInfo
import im.tox.tox4j.core.data.ToxNickname

class AntoxOnNameChangeCallback(private var ctx: Context) {
  def friendName(friendInfo: FriendInfo, nameBytes: ToxNickname)(state: Unit): Unit = {
    val name = UiUtils.removeNewlines(new String(nameBytes.value, "UTF-8"))

    val db = State.db
    db.updateContactName(friendInfo.key, name)
  }
}