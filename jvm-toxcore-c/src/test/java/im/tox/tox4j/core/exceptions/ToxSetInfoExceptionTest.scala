package im.tox.tox4j.core.exceptions

import im.tox.tox4j.ToxCoreTestBase
import im.tox.tox4j.core.ToxCoreConstants
import im.tox.tox4j.core.data.{ ToxNickname, ToxStatusMessage }
import im.tox.tox4j.testing.ToxTestMixin
import org.scalatest.FunSuite

final class ToxSetInfoExceptionTest extends FunSuite with ToxTestMixin {

  test("SetNameTooLong") {
    val array = ToxCoreTestBase.randomBytes(ToxCoreConstants.MaxNameLength + 1)

    interceptWithTox(ToxSetInfoException.Code.TOO_LONG)(
      _.setName(ToxNickname.unsafeFromValue(array))
    )
  }

  test("SetStatusMessageTooLong") {
    val array = ToxCoreTestBase.randomBytes(ToxCoreConstants.MaxStatusMessageLength + 1)

    interceptWithTox(ToxSetInfoException.Code.TOO_LONG)(
      _.setStatusMessage(ToxStatusMessage.unsafeFromValue(array))
    )
  }

  test("SetNameNull") {
    interceptWithTox(ToxSetInfoException.Code.NULL)(
      _.setName(ToxNickname.unsafeFromValue(null))
    )
  }

  test("SetStatusMessageNull") {
    interceptWithTox(ToxSetInfoException.Code.NULL)(
      _.setStatusMessage(ToxStatusMessage.unsafeFromValue(null))
    )
  }

}
