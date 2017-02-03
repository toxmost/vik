package im.tox.tox4j.core.exceptions

import im.tox.tox4j.core.data.ToxPublicKey
import im.tox.tox4j.testing.ToxTestMixin
import org.scalatest.FunSuite

final class ToxFriendByPublicKeyExceptionTest extends FunSuite with ToxTestMixin {

  test("Null") {
    interceptWithTox(ToxFriendByPublicKeyException.Code.NULL)(
      _.friendByPublicKey(ToxPublicKey.unsafeFromValue(null))
    )
  }

  test("NotFound") {
    interceptWithTox(ToxFriendByPublicKeyException.Code.NOT_FOUND) { tox =>
      tox.friendByPublicKey(tox.getPublicKey)
    }
  }

}
