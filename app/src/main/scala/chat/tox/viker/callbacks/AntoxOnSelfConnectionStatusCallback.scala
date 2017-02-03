package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.tox.ToxSingleton
import chat.tox.viker.utils.AntoxLog
import im.tox.tox4j.core.callbacks.SelfConnectionStatusCallback
import im.tox.tox4j.core.enums.ToxConnection
import rx.lang.scala.subjects.BehaviorSubject

object AntoxOnSelfConnectionStatusCallback {
  val connectionStatusSubject = BehaviorSubject(ToxConnection.NONE)
}

class AntoxOnSelfConnectionStatusCallback(ctx: Context) extends SelfConnectionStatusCallback[Unit] {

  override def selfConnectionStatus(toxConnection: ToxConnection)(state: Unit): Unit = {
    ToxSingleton.tox.setSelfConnectionStatus(toxConnection)

    AntoxLog.debug(s"self connection status changed to $toxConnection")
    AntoxOnSelfConnectionStatusCallback.connectionStatusSubject.onNext(toxConnection)
  }
}
