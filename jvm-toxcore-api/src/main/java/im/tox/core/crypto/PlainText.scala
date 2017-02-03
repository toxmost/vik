package im.tox.core.crypto

import im.tox.core.ModuleCompanion
import im.tox.core.typesafe.Security
import scodec.bits.{ BitVector, ByteVector }
import scodec.codecs._

import scala.language.implicitConversions

final case class PlainText[+S <: Security](private val value: ByteVector) extends AnyVal {

  private[core] def unsafeIgnoreSecurity: PlainText[Security.NonSensitive] = PlainText(value)

}

case object PlainText extends ModuleCompanion[PlainText[Security.NonSensitive], Security.NonSensitive] {

  override val codec = bytes.xmap[PlainText[Security.NonSensitive]](PlainText.apply, _.value)
  override def nullable: Boolean = true

  @SuppressWarnings(Array("org.wartremover.warts.ImplicitConversion"))
  object Conversions {

    final class NonSensitiveConversions private[Conversions] (private val value: ByteVector) extends AnyVal {
      def toByteVector: ByteVector = value
      def toBitVector: BitVector = toByteVector.toBitVector
      def toByteArray: Array[Byte] = toByteVector.toArray
      def toSeq: Seq[Byte] = toByteVector.toSeq
      def size: Long = value.size
    }

    implicit def plainTextNonSensitiveConversions(plainText: PlainText[Security.NonSensitive]): NonSensitiveConversions = {
      new NonSensitiveConversions(plainText.value)
    }

    final class SensitiveConversions private[Conversions] (private val value: ByteVector) extends AnyVal {
      def size: Long = value.size
    }

    implicit def plainTextSensitiveConversions(plainText: PlainText[Security.Sensitive]): SensitiveConversions = {
      new SensitiveConversions(plainText.value)
    }

  }

}
