package chat.tox.viker.av

object CallEndReason extends Enumeration {
  type CallEndReason = Value
  val Normal, Missed, Unanswered, Error = Value
}
