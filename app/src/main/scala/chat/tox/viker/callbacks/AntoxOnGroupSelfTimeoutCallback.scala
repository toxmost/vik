package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.data.State
import chat.tox.viker.tox.ToxSingleton

class AntoxOnGroupSelfTimeoutCallback(private var ctx: Context) /* extends GroupSelfTimeoutCallback */ {
  //override
  def groupSelfTimeout(groupNumber: Int): Unit = {
    ToxSingleton.getGroup(groupNumber).connected = false

    val db = State.db
    db.updateContactOnline(ToxSingleton.getGroup(groupNumber).key, false)
  }
}