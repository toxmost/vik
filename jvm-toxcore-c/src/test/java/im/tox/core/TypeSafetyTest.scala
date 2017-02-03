package im.tox.core

import im.tox.core.crypto._
import org.scalatest.WordSpec
import scodec.bits.ByteVector

final class TypeSafetyTest extends WordSpec {

  s"no classes except $PlainText" should {
    "be instantiated outside the crypto module" in {
      val byteVector = ByteVector.empty
      val byteArray = Array.empty[Byte]
      assertCompiles("""new PlainText(byteVector)""")
      assertTypeError("""new CipherText(byteVector)""")
      assertTypeError("""new PublicKey(byteArray)""")
      assertTypeError("""new SecretKey(byteArray)""")
      assertTypeError("""new Nonce(byteArray)""")
    }
  }

}
