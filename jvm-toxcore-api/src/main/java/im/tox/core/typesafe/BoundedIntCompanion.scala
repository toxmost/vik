package im.tox.core.typesafe

import scodec.Codec
import scodec.codecs._

abstract class BoundedIntCompanion[T <: AnyVal](
    val MinValue: Int,
    val MaxValue: Int,
    valueCodec: Codec[Int] = int32
) extends IntCompanion[T](valueCodec) {

  protected def unsafeFromInt(value: Int): T

  final def clamp(value: Int): T = {
    unsafeFromInt(value min MaxValue max MinValue)
  }

  final override def fromInt(value: Int): Option[T] = {
    if (MinValue <= value && value <= MaxValue) {
      Some(unsafeFromInt(value))
    } else {
      None
    }
  }

}
