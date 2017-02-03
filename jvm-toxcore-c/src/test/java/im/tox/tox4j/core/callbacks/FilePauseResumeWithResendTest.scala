package im.tox.tox4j.core.callbacks

import im.tox.tox4j.core.ToxCore
import im.tox.tox4j.core.data.{ ToxFriendNumber, ToxFileId, ToxFilename }
import im.tox.tox4j.core.enums.{ ToxFileControl, ToxFileKind }

final class FilePauseResumeWithResendTest extends FilePauseResumeTestBase {

  protected override def newChatClient(name: String, expectedFriendName: String) = new Alice(name, expectedFriendName)

  final class Alice(name: String, expectedFriendName: String) extends super.Alice(name, expectedFriendName) {

    protected override def addFriendMessageTask(
      friendNumber: ToxFriendNumber,
      bobSentFileNumber: Int,
      fileId: ToxFileId,
      tox: ToxCore
    )(state: State): State = {
      if (isAlice) {
        state.copy(
          aliceShouldPause = 1,
          aliceSentFileNumber = tox.fileSend(
            friendNumber,
            ToxFileKind.DATA,
            fileData.length,
            fileId,
            ToxFilename.fromValue(("file for " + expectedFriendName + ".png").getBytes).get
          )
        )
      } else {
        debug("Send resume file transmission control")
        tox.fileControl(friendNumber, bobSentFileNumber, ToxFileControl.RESUME)
        state
      }
    }

    protected override def addFileRecvTask(
      friendNumber: ToxFriendNumber,
      bobSentFileNumber: Int,
      bobOffset: Long,
      tox: ToxCore
    )(state: State): State = {
      debug(s"seek file to $bobOffset")
      tox.fileSeek(friendNumber, bobSentFileNumber, bobOffset)
      debug(s"sending control RESUME for $bobSentFileNumber")
      tox.fileControl(friendNumber, bobSentFileNumber, ToxFileControl.RESUME)
      state.copy(bobShouldPause = 1)
    }

  }

}
