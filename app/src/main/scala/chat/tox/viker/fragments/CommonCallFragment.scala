package chat.tox.viker.fragments

import android.content.Context
import android.media.AudioManager
import android.os.{Bundle, PowerManager}
import android.support.v4.app.Fragment
import android.view.View.OnClickListener
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.{RelativeLayout, TextView}
import chat.tox.viker.R
import chat.tox.viker.av.Call
import chat.tox.viker.data.State
import chat.tox.viker.utils.{AntoxLog, BitmapManager}
import chat.tox.viker.wrapper.{CallNumber, ContactKey, FriendInfo, FriendKey}
import de.hdodenhof.circleimageview.CircleImageView
import im.tox.tox4j.exceptions.ToxException
import rx.lang.scala.Subscription
import rx.lang.scala.subscriptions.CompositeSubscription

import scala.concurrent.{Future, Promise}
import scala.util.Try

object CommonCallFragment {
  val EXTRA_CALL_NUMBER = "call_number"
  val EXTRA_ACTIVE_KEY = "active_key"
  val EXTRA_FRAGMENT_LAYOUT = "fragment_layout"
}

abstract class CommonCallFragment extends Fragment {

  var call: Call = _
  var activeKey: ContactKey = _
  var callLayout: Int = _

  var upperCallHalfView: RelativeLayout = _
  var callStateView: TextView = _
  var nameView: TextView = _
  var avatarView: CircleImageView = _

  var endCallButton: View = _

  val compositeSubscription = CompositeSubscription()

  var audioManager: AudioManager = _

  private var powerManager: PowerManager = _
  var maybeWakeLock: Option[PowerManager#WakeLock] = None
  var videoWakeLockSubscription: Option[Subscription] = None

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    call =
      State.callManager.get(CallNumber(getArguments.getInt(CommonCallFragment.EXTRA_CALL_NUMBER)))
        .getOrElse({
          AntoxLog.debug(s"Ending call which has an invalid call number")
          getActivity.finish()
          return
        })

    activeKey = FriendKey(getArguments.getString(CommonCallFragment.EXTRA_ACTIVE_KEY))
    callLayout = getArguments.getInt(CommonCallFragment.EXTRA_FRAGMENT_LAYOUT)

    audioManager = getActivity.getSystemService(Context.AUDIO_SERVICE).asInstanceOf[AudioManager]

    // power manager used to turn off screen when the proximity sensor is triggered
    powerManager = getActivity.getSystemService(Context.POWER_SERVICE).asInstanceOf[PowerManager]
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      if (powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)) {
        maybeWakeLock = Some(powerManager.newWakeLock(
          PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK,
          AntoxLog.DEFAULT_TAG.toString))
      }
    }
  }

  private def updateDisplayedState(friendInfoList: Seq[FriendInfo]): Unit = {
    val mFriend: Option[FriendInfo] = friendInfoList.find(f => f.key == activeKey)

    mFriend match {
      case Some(friend) => {
        // need to run on UI Thread
        try {
          nameView.getHandler().post(new Runnable(){def run {nameView.setText(friend.getDisplayName)}})
          // nameView.setText(friend.getDisplayName)
        }
        catch
          {
            case e: Exception =>
              System.out.println("onUiThread:001")
          }
      }

        val avatar = friend.avatar
        avatar.foreach(avatar => {
          val bitmap = BitmapManager.loadBlocking(avatar, isAvatar = true)

          // need to run on UI Thread
          try {
            avatarView.getHandler().post(new Runnable(){def run {avatarView.setImageBitmap(bitmap)}})
            // avatarView.setImageBitmap(bitmap)
          }
          catch
            {
              case e: Exception =>
                System.out.println("onUiThread:002")
            }
        })

      case None =>
        nameView.setText("")
    }
  }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    super.onCreateView(inflater, container, savedInstanceState)
    val rootView = inflater.inflate(callLayout, container, false)

    callStateView = rootView.findViewById(R.id.call_state_text).asInstanceOf[TextView]

    upperCallHalfView = rootView.findViewById(R.id.call_upper_half).asInstanceOf[RelativeLayout]
    avatarView = rootView.findViewById(R.id.call_avatar).asInstanceOf[CircleImageView]
    nameView = rootView.findViewById(R.id.friend_name).asInstanceOf[TextView]

    // Set up the end call and av buttons
    endCallButton = rootView.findViewById(R.id.end_call_circle)

    endCallButton.setOnClickListener(new OnClickListener {
      override def onClick(view: View) = {
        call.end()
        endCallButton.setEnabled(false) //don't let the user press end call more than once
      }
    })

    // update displayed friend info on change
    compositeSubscription +=
      State.db.friendInfoList
        .subscribe(fi => {
          updateDisplayedState(fi)
        })

    rootView
  }

  override def onResume(): Unit = {
    super.onResume()

    videoWakeLockSubscription =
      Some(call.videoEnabledObservable.subscribe(video => {
        if (video) {
          maybeWakeLock.foreach(wakeLock => {
            if (wakeLock.isHeld) {
              wakeLock.release()
            }
          })
        } else {
          maybeWakeLock.foreach(wakeLock => {
            if (!wakeLock.isHeld) {
              wakeLock.acquire()
            }
          })
        }
      }))
  }

  override def onPause(): Unit = {
    super.onPause()
    videoWakeLockSubscription.foreach(_.unsubscribe())

    maybeWakeLock.foreach(wakeLock => {
      if (wakeLock.isHeld) {
        wakeLock.release()
      }
    })
  }

  override def onDestroy(): Unit = {
    super.onDestroy()

    compositeSubscription.unsubscribe()
  }
}
