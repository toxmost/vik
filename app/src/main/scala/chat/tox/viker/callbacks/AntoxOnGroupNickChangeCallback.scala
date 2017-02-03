package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.utils.AntoxLog

class AntoxOnGroupNickChangeCallback(private var ctx: Context) /* extends GroupNickChangeCallback */ {
  //override
  def groupNickChange(groupNumber: Int, peerNumber: Int, nick: Array[Byte]): Unit = {
    AntoxLog.debug(s"Peer $peerNumber nick changed")
    //ToxSingleton.getGroupPeer(groupNumber, peerNumber).name = new String(nick, "UTF-8")
  }
}