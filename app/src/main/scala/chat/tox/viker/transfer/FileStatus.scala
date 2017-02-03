package chat.tox.viker.transfer

object FileStatus extends Enumeration {
  type FileStatus = Value
  val REQUEST_SENT, CANCELLED, IN_PROGRESS, FINISHED, PAUSED = Value
}
