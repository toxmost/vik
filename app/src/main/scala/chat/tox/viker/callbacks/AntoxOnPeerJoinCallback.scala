package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.tox.ToxSingleton
import chat.tox.viker.utils.AntoxLog

class AntoxOnPeerJoinCallback(private var ctx: Context) /* extends GroupPeerJoinCallback */ {
  def groupPeerJoin(groupNumber: Int, peerNumber: Int): Unit = {
    ToxSingleton.getGroup(groupNumber).addPeer(ToxSingleton.tox, peerNumber)
    AntoxLog.debug(s"peer $peerNumber joined group $groupNumber")
  }
}