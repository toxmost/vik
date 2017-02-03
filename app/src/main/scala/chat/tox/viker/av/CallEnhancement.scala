package chat.tox.viker.av

import rx.lang.scala.subscriptions.CompositeSubscription

trait CallEnhancement {
  def call: Call

  val subscriptions: CompositeSubscription = CompositeSubscription()
}
