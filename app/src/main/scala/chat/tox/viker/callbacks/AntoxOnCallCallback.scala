package chat.tox.viker.callbacks

import android.content.{Context, Intent}
import chat.tox.viker.activities.CallActivity
import chat.tox.viker.av.Call
import chat.tox.viker.data.State
import chat.tox.viker.tox.ToxSingleton
import chat.tox.viker.utils.AntoxLog
import chat.tox.viker.wrapper.CallNumber
import im.tox.tox4j.core.data.ToxFriendNumber

class AntoxOnCallCallback(private var ctx: Context) {

  def call(callNumber: CallNumber, audioEnabled: Boolean, videoEnabled: Boolean)(state: Unit): Unit = {
    AntoxLog.debug("New call from " + callNumber)

    try {
      State.callManager.add(new Call(callNumber, ToxSingleton.tox.getFriendKey(ToxFriendNumber.unsafeFromInt(callNumber.value)), incoming = true))
      State.callManager.get(callNumber).foreach { call =>
        call.onIncoming(audioEnabled, videoEnabled)

        new Thread(new Runnable {
          override def run(): Unit = {
            val callActivity = new Intent(ctx, classOf[CallActivity])

            callActivity.putExtra("key", call.contactKey.toString)
            callActivity.putExtra("call_number", call.callNumber.value)
            callActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(callActivity)
          }
        }).start()
      }
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }
}
