package im.tox.gui.util

import javax.swing._

import im.tox.tox4j.core.callbacks.ToxCoreEventListener
import im.tox.tox4j.core.data._
import im.tox.tox4j.core.enums.{ ToxConnection, ToxFileControl, ToxMessageType, ToxUserStatus }
import org.jetbrains.annotations.NotNull

// scalastyle:off line.size.limit
final class InvokeLaterToxEventListener[ToxCoreState](underlying: ToxCoreEventListener[ToxCoreState]) extends ToxCoreEventListener[ToxCoreState] {

  private def invokeLater(callback: ToxCoreState => ToxCoreState)(state: ToxCoreState): ToxCoreState = {
    SwingUtilities.invokeLater(new Runnable {
      override def run(): Unit = {
        callback(state)
      }
    })
    state
  }

  override def selfConnectionStatus(@NotNull connectionStatus: ToxConnection)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.selfConnectionStatus(connectionStatus))(state)
  }

  override def fileRecvControl(friendNumber: ToxFriendNumber, fileNumber: Int, @NotNull control: ToxFileControl)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.fileRecvControl(friendNumber, fileNumber, control))(state)
  }

  override def fileRecv(friendNumber: ToxFriendNumber, fileNumber: Int, kind: Int, fileSize: Long, @NotNull filename: ToxFilename)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.fileRecv(friendNumber, fileNumber, kind, fileSize, filename))(state)
  }

  override def fileRecvChunk(friendNumber: ToxFriendNumber, fileNumber: Int, position: Long, @NotNull data: Array[Byte])(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.fileRecvChunk(friendNumber, fileNumber, position, data))(state)
  }

  override def fileChunkRequest(friendNumber: ToxFriendNumber, fileNumber: Int, position: Long, length: Int)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.fileChunkRequest(friendNumber, fileNumber, position, length))(state)
  }

  override def friendConnectionStatus(friendNumber: ToxFriendNumber, @NotNull connectionStatus: ToxConnection)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.friendConnectionStatus(friendNumber, connectionStatus))(state)
  }

  override def friendLosslessPacket(friendNumber: ToxFriendNumber, @NotNull data: ToxLosslessPacket)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.friendLosslessPacket(friendNumber, data))(state)
  }

  override def friendLossyPacket(friendNumber: ToxFriendNumber, @NotNull data: ToxLossyPacket)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.friendLossyPacket(friendNumber, data))(state)
  }

  override def friendMessage(friendNumber: ToxFriendNumber, @NotNull messageType: ToxMessageType, timeDelta: Int, @NotNull message: ToxFriendMessage)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.friendMessage(friendNumber, messageType, timeDelta, message))(state)
  }

  override def friendName(friendNumber: ToxFriendNumber, @NotNull name: ToxNickname)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.friendName(friendNumber, name))(state)
  }

  override def friendRequest(@NotNull publicKey: ToxPublicKey, timeDelta: Int, @NotNull message: ToxFriendRequestMessage)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.friendRequest(publicKey, timeDelta, message))(state)
  }

  override def friendStatus(friendNumber: ToxFriendNumber, @NotNull status: ToxUserStatus)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.friendStatus(friendNumber, status))(state)
  }

  override def friendStatusMessage(friendNumber: ToxFriendNumber, @NotNull message: ToxStatusMessage)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.friendStatusMessage(friendNumber, message))(state)
  }

  override def friendTyping(friendNumber: ToxFriendNumber, isTyping: Boolean)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.friendTyping(friendNumber, isTyping))(state)
  }

  override def friendReadReceipt(friendNumber: ToxFriendNumber, messageId: Int)(state: ToxCoreState): ToxCoreState = {
    invokeLater(underlying.friendReadReceipt(friendNumber, messageId))(state)
  }

}
