package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.data.State
import chat.tox.viker.tox.ToxSingleton

class AntoxOnGroupTopicChangeCallback(private var ctx: Context) /* extends GroupTopicChangeCallback */ {
  /* override */ def groupTopicChange(groupNumber: Int, peerNumber: Int, topic: Array[Byte]): Unit = {
    val group = ToxSingleton.getGroup(groupNumber)
    group.topic = new String(topic, "UTF-8")

    val db = State.db
    //    db.updateContactStatusMessage(group.key, group.topic)
  }
}