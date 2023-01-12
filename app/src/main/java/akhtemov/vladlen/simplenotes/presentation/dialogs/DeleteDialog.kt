package akhtemov.vladlen.simplenotes.presentation.dialogs

import akhtemov.vladlen.simplenotes.databinding.DialogDeleteBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.octopus.inc.domain.models.NoteModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteDialog : DialogFragment() {

    companion object {
        private const val DELETE_DIALOG_TAG = "delete_dialog_tag"

        fun showDeleteDialog(
            note: NoteModel,
            callbacks: DeleteDialogCallbacks,
            fragmentManager: FragmentManager
        ) {
            DeleteDialog().apply {
                isCancelable = false
                setNote(note)
                setCallbacks(callbacks)
                show(fragmentManager, DELETE_DIALOG_TAG)
            }
        }
    }

    private lateinit var binding: DialogDeleteBinding
    private var deleteDialogCallbacks: DeleteDialogCallbacks? = null
    private var note: NoteModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteBinding.inflate(inflater, container, false)

        binding.yesBtn.setOnClickListener {
            note?.let { note ->
                deleteDialogCallbacks?.onClickYesOnDeleteDialog(note)
                dismiss()
            }
        }

        binding.noBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    fun setCallbacks(callbacks: DeleteDialogCallbacks) {
        this.deleteDialogCallbacks = callbacks
    }

    fun setNote(note: NoteModel) {
        this.note = note
    }
}