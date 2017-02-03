package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.tox.ToxSingleton
import chat.tox.viker.utils.AntoxLog

class AntoxOnPeerExitCallback(private var ctx: Context) /* extends GroupPeerExitCallback */ {
  def groupPeerExit(groupNumber: Int, peerNumber: Int, partMessage: Array[Byte]): Unit = {
    ToxSingleton.getGroup(groupNumber).peers.removeGroupPeer(peerNumber)
    AntoxLog.debug(s"peer $peerNumber exited group $groupNumber")
  }
}