package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.data.State
import chat.tox.viker.utils.{AntoxLog, AntoxNotificationManager}
import chat.tox.viker.wrapper.FriendKey
import im.tox.tox4j.core.callbacks.FriendRequestCallback
import im.tox.tox4j.core.data.{ToxFriendRequestMessage, ToxPublicKey}

class AntoxOnFriendRequestCallback(private var ctx: Context) extends FriendRequestCallback[Unit] {

  override def friendRequest(publicKey: ToxPublicKey, timeDelta: Int, message: ToxFriendRequestMessage)(state: Unit): Unit = {
    val db = State.db
    val key = new FriendKey(publicKey.toHexString)
    if (!db.isContactBlocked(key)) {
      db.addFriendRequest(key, new String(message.value))
    }

    AntoxLog.debug("New Friend Request")
    AntoxNotificationManager.createRequestNotification(key, Some(new String(message.value)), ctx)
  }
}
