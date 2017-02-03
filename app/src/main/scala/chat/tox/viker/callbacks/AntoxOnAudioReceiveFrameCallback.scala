package chat.tox.viker.callbacks

import android.content.Context
import chat.tox.viker.data.State
import chat.tox.viker.wrapper.CallNumber
import im.tox.tox4j.av.data.{AudioChannels, SamplingRate}

class AntoxOnAudioReceiveFrameCallback(private var ctx: Context) {

  def audioReceiveFrame(callNumber: CallNumber, pcm: Array[Short], channels: AudioChannels, samplingRate: SamplingRate)(state: Unit): Unit = {
    State.callManager.get(callNumber).foreach(_.onAudioFrame(pcm, channels, samplingRate))
  }
}