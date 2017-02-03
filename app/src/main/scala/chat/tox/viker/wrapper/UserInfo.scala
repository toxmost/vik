package chat.tox.viker.wrapper

import chat.tox.viker.toxme.ToxMeName
import im.tox.tox4j.core.data.{ToxNickname, ToxStatusMessage}

case class UserInfo(toxMeName: ToxMeName,
                    password: String,
                    nickname: ToxNickname,
                    status: String,
                    statusMessage: ToxStatusMessage,
                    loggingEnabled: Boolean,
                    avatarName: String) {

  def profileName: String = toxMeName.username
}