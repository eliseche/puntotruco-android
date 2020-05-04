package com.quitarts.puntotruco.views

import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.quitarts.puntotruco.BR
import com.quitarts.puntotruco.R
import com.quitarts.puntotruco.databinding.FragmentPlayerBinding
import com.quitarts.puntotruco.enums.PlayerType
import com.quitarts.puntotruco.viewmodels.ViewModelPlayer
import kotlinx.android.synthetic.main.fragment_player.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Shows modal to allow Player to edit his name.
 */
class FragmentPlayer : DialogFragment() {
    private lateinit var viewModelPlayer: ViewModelPlayer
    internal lateinit var listener: IFragmentPlayerListener
    private var playerType: PlayerType? = null

    interface IFragmentPlayerListener {
        fun onDialogPositiveClick(playerName: String, playerType: PlayerType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playerType = arguments?.getSerializable("playerType") as PlayerType
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModelPlayer = ViewModelProvider(this).get(ViewModelPlayer::class.java)

        val binding: FragmentPlayerBinding? = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        val rootView = binding?.root
        binding?.setVariable(BR.viewModelPlayer, viewModelPlayer)
        binding?.run {
            lifecycleOwner = activity
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        viewModelPlayer.init()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as IFragmentPlayerListener
        } catch (e: Exception) {
            throw ClassCastException("It must implement IFragmentPlayerListener")
        }
    }

    override fun onResume() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                dismiss()
                true
            }

            false
        }

        super.onResume()
    }

    //region Setup
    private fun init() {
        player_name_confirm.onClick {
            val playerName = player_name.text.toString().trim()
            if (playerName.isNotEmpty())
                listener.onDialogPositiveClick(playerName, playerType!!)
        }
    }
    //endregion
}