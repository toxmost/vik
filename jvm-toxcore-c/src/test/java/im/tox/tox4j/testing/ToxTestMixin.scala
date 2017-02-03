package im.tox.tox4j.testing

import im.tox.core.error.CoreError
import im.tox.tox4j.DhtNodeSelector._
import im.tox.tox4j.core.ToxCore
import im.tox.tox4j.core.data.ToxFriendNumber
import im.tox.tox4j.core.exceptions.{ ToxBootstrapException, ToxFriendAddException, ToxNewException }
import im.tox.tox4j.impl.jni.ToxCoreImplFactory
import org.jetbrains.annotations.NotNull

import scala.language.implicitConversions
import scalaz.\/

trait ToxTestMixin extends ToxExceptionChecks {

  protected def interceptWithTox[E <: Enum[E]](code: E)(f: ToxCore => Unit) = {
    intercept(code) {
      ToxCoreImplFactory.withToxUnit { tox =>
        addFriends(tox, 1)
        f(tox)
      }
    }
  }

  @throws[ToxNewException]
  @throws[ToxFriendAddException]
  protected def addFriends(@NotNull tox: ToxCore, count: Int): ToxFriendNumber = {
    (0 until count).map { (i: Int) =>
      ToxCoreImplFactory.withToxUnit { friend =>
        tox.addFriendNorequest(friend.getPublicKey)
      }
    }.lastOption match {
      case None => throw new IllegalArgumentException(s"Cannot add less than 1 friend: $count")
      case Some(num) => num
    }
  }

  @throws[ToxBootstrapException]
  def bootstrap(useIPv6: Boolean, udpEnabled: Boolean, @NotNull tox: ToxCore): ToxCore = {
    if (!udpEnabled) {
      tox.addTcpRelay(node.ipv4, node.tcpPort, node.dhtId)
    }
    tox.bootstrap(
      if (useIPv6) node.ipv6 else node.ipv4,
      if (udpEnabled) node.udpPort else node.tcpPort,
      node.dhtId
    )
    tox
  }

  @SuppressWarnings(Array("org.wartremover.warts.ImplicitConversion"))
  implicit final def toGetDisjunction[T](disjunction: CoreError \/ T): GetDisjunction[T] = {
    GetDisjunction.toGetDisjunction(disjunction)
  }

}
