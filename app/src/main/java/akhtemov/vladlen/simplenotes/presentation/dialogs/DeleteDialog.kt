package akhtemov.vladlen.simplenotes.presentation.dialogs

import akhtemov.vladlen.simplenotes.databinding.DialogDeleteBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class DeleteDialog : DialogFragment() {

    companion object {
        const val TAG = "delete_dialog_tag"
        const val POSITION_KEY = "position_key"

        fun showDeleteDialog(
            position: Int,
            callbacks: DeleteDialogCallbacks,
            fragmentManager: FragmentManager
        ) {
//            val bundle = Bundle()
//            bundle.putString(POSITION_KEY, position)

            DeleteDialog().apply {
                isCancelable = false
                setCallbacks(callbacks)
                setPosition(position)
//                arguments = bundle
                show(fragmentManager, TAG)
            }
        }
    }

    private lateinit var binding: DialogDeleteBinding
    private var deleteDialogCallbacks: DeleteDialogCallbacks? = null
    private var position: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteBinding.inflate(inflater, container, false)

        binding.yesBtn.setOnClickListener {
            if (position != null) {
                deleteDialogCallbacks?.onClickDeleteDialogYes(position!!)
                dismiss()
            }
        }

        binding.noBtn.setOnClickListener {
            deleteDialogCallbacks?.onClickDeleteDialogNo()
            dismiss()
        }

        return binding.root
    }

    fun setCallbacks(callbacks: DeleteDialogCallbacks) {
        this.deleteDialogCallbacks = callbacks
    }

    fun setPosition(position: Int) {
        this.position = position
    }
}