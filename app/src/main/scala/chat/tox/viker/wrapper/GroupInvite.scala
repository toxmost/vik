package chat.tox.viker.wrapper

final case class GroupInvite(groupKey: ContactKey, inviter: FriendKey, data: Array[Byte])