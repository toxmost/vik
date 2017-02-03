package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.data.State
import chat.tox.viker.tox.MessageHelper
import chat.tox.viker.utils.AntoxLog
import chat.tox.viker.wrapper.{GroupInfo, GroupPeer}
import im.tox.tox4j.core.enums.ToxMessageType

class AntoxOnGroupMessageCallback(private var ctx: Context) /* extends GroupMessageCallback */ {

  //override
  def groupMessage(groupInfo: GroupInfo, peerInfo: GroupPeer, timeDelta: Int, message: Array[Byte]): Unit = {
    AntoxLog.debug("new group message callback for id " + groupInfo.key)
    State.setLastIncomingMessageAction()
    MessageHelper.handleGroupMessage(ctx, groupInfo, peerInfo, new String(message, "UTF-8"), ToxMessageType.NORMAL)
  }
}
