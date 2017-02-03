package im.tox.tox4j.core.callbacks

import im.tox.tox4j.core.data.{ ToxFriendNumber, ToxNickname }
import im.tox.tox4j.testing.autotest.{ AliceBobTest, AliceBobTestBase }

final class NameEmptyTest extends AliceBobTest {

  override type State = Int
  override def initialState: State = 0

  protected override def newChatClient(name: String, expectedFriendName: String) = new ChatClient(name, expectedFriendName) {

    @SuppressWarnings(Array("org.wartremover.warts.Equals"))
    override def friendName(friendNumber: ToxFriendNumber, name: ToxNickname)(state: ChatState): ChatState = {
      debug("friend changed name to: " + new String(name.value))
      assert(friendNumber == AliceBobTestBase.FriendNumber)

      state.get match {
        case 0 =>
          // Initial empty name
          assert(name.value.isEmpty)

          state.addTask { (tox, av, state) =>
            tox.setName(ToxNickname.fromString("One").get)
            state
          }.set(1)

        case 1 =>
          assert(new String(name.value) == "One")

          state.addTask { (tox, av, state) =>
            tox.setName(ToxNickname.fromString("").get)
            state
          }.set(2)

        case 2 =>
          assert(name.value.isEmpty)
          state.finish
      }
    }

  }

}

