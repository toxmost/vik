package im.tox.tox4j.core.callbacks

import im.tox.tox4j.core.data.{ ToxFriendNumber, ToxNickname }
import im.tox.tox4j.core.enums.ToxConnection
import im.tox.tox4j.testing.autotest.{ AliceBobTest, AliceBobTestBase }

final class FriendNameCallbackTest extends AliceBobTest {

  override type State = Int
  override def initialState: State = 0

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  protected override def newChatClient(name: String, expectedFriendName: String) = new ChatClient(name, expectedFriendName) {

    override def friendConnectionStatus(friendNumber: ToxFriendNumber, connectionStatus: ToxConnection)(state: ChatState): ChatState = {
      super.friendConnectionStatus(friendNumber, connectionStatus)(state)
      if (connectionStatus != ToxConnection.NONE) {
        state.addTask { (tox, av, state) =>
          tox.setName(ToxNickname.fromValue(selfName.getBytes).get)
          state
        }
      } else {
        state
      }
    }

    override def friendName(friendNumber: ToxFriendNumber, name: ToxNickname)(state: ChatState): ChatState = {
      debug(s"friend changed name to: ${new String(name.value)}")
      assert(friendNumber == AliceBobTestBase.FriendNumber)

      state.get match {
        case 0 =>
          assert(name.value.isEmpty)
          state.set(1)
        case 1 =>
          assert(new String(name.value) == expectedFriendName)
          state.finish
      }
    }

  }

}
