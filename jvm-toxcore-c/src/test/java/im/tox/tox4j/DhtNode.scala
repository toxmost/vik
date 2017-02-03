package im.tox.tox4j

import im.tox.core.network.Port
import im.tox.tox4j.core.data.ToxPublicKey
import im.tox.tox4j.testing.GetDisjunction._

final case class DhtNode(ipv4: String, ipv6: String, udpPort: Port, tcpPort: Port, dhtId: ToxPublicKey) {

  def this(ipv4: String, ipv6: String, udpPort: Int, tcpPort: Int, dhtId: String) {
    this(
      ipv4, ipv6,
      Port.unsafeFromInt(udpPort),
      Port.unsafeFromInt(tcpPort),
      ToxPublicKey.fromValue(ToxCoreTestBase.parsePublicKey(dhtId)).get
    )
  }

  def this(ipv4: String, ipv6: String, port: Int, dhtId: String) {
    this(ipv4, ipv6, port, port, dhtId)
  }

}
