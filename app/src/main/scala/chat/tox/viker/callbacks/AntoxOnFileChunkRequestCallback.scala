package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.data.State
import chat.tox.viker.tox.ToxSingleton
import chat.tox.viker.transfer.FileStatus
import chat.tox.viker.utils.AntoxLog
import chat.tox.viker.wrapper.FriendInfo

class AntoxOnFileChunkRequestCallback(private var ctx: Context) {

  def fileChunkRequest(friendInfo: FriendInfo, fileNumber: Int, position: Long, length: Int)(state: Unit): Unit = {
    val mTransfer = State.transfers.get(friendInfo.key, fileNumber)

    // System.out.println("fileChunkRequest:" + "friendInfo=" + friendInfo + " fileNumber=" + fileNumber + " position=" + position + " length=" + length)

    mTransfer match {
      case Some(t) =>
        t.status = FileStatus.IN_PROGRESS
        if (length <= 0) {
          State.transfers.fileFinished(friendInfo.key, t.fileNumber, ctx)
          State.db.clearFileNumber(friendInfo.key, fileNumber)
        } else {
          val data = t.readData(position, t.progress, length)
          data match {
            case Some(d) =>
              try {
                ToxSingleton.tox.fileSendChunk(friendInfo.key, fileNumber, position, d)
              } catch {
                case e: Exception =>
                  e.printStackTrace()
              }
              t.addToProgress(position + length)
            case None =>
          }
        }

      case None => AntoxLog.debug("Can't find file transfer")
    }
  }
}
