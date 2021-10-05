package software.sauce.easyledger.presentation.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import software.sauce.easyledger.presentation.components.GenericDialogInfo
import software.sauce.easyledger.presentation.components.PositiveAction
import java.util.*

class DialogQueue {

  // Queue for "First-In-First-Out" behavior
  val queue: MutableState<Queue<GenericDialogInfo>> = mutableStateOf(LinkedList())

  fun removeHeadMessage(){
    if (queue.value.isNotEmpty()) {
      val update = queue.value
      update.remove() // remove first (oldest message)
      queue.value = ArrayDeque() // force recompose (bug?)
      queue.value = update
    }
  }

  fun appendErrorMessage(title: String, description: String){
    queue.value.offer(
      GenericDialogInfo.Builder()
        .title(title)
        .onDismiss(this::removeHeadMessage)
        .description(description)
        .positive(
          PositiveAction(
            positiveBtnTxt = "Ok",
            onPositiveAction = this::removeHeadMessage,
          )
        )
        .build()
    )
  }
}