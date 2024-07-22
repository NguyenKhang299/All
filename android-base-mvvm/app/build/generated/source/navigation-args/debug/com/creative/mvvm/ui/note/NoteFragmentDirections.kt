package com.creative.mvvm.ui.note

import android.os.Bundle
import androidx.navigation.NavDirections
import com.creative.mvvm.R
import kotlin.Int

public class NoteFragmentDirections private constructor() {
  private data class ActionNoteFragmentToUpdateNoteFragment(
    public val id: Int = -1
  ) : NavDirections {
    public override val actionId: Int = R.id.action_noteFragment_to_updateNoteFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("id", this.id)
        return result
      }
  }

  public companion object {
    public fun actionNoteFragmentToUpdateNoteFragment(id: Int = -1): NavDirections =
        ActionNoteFragmentToUpdateNoteFragment(id)
  }
}
