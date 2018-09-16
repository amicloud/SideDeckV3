package com.outplaysoftworks.sidedeck

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import java.util.HashMap

import butterknife.BindView
import butterknife.ButterKnife
import me.grantland.widget.AutofitHelper


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LpLog.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LpLog.newInstance] factory method to
 * create an instance of this fragment.
 */
class LpLog : Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    @SuppressLint("UseSparseArrays")
    private val headers = HashMap<Int, LinearLayout>()

    @BindView(R.id.loglist)
    internal var lvEntries: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_lp_log, container, false)
        ButterKnife.bind(this, view)
        addHeader()
        return view
    }

    override fun onStart() {
        super.onStart()
        //        reset();
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onPause() {
        reset()
        super.onPause()
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun reset() {
        val layout = ButterKnife.findById<LinearLayout>(activity, R.id.loglist)
        layout.removeAllViews()
        headers.clear()
        addHeader()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    fun onTurnIncremented(command: Command) {
        if (!headers.containsKey(LpCalculatorModel.currentTurn)) {
            addHeader()
        }
    }

    fun onAdd(command: AddLpCommand) {
        if (command.target == 1) {
            addEntryToHeader(command.target, command.amount, LpCalculatorModel.player1Lp, true)
        }
        if (command.target == 2) {
            addEntryToHeader(command.target, command.amount, LpCalculatorModel.player2Lp, true)
        }
    }

    fun onSubtract(command: SubtractLpCommand) {
        if (command.target == 1) {
            addEntryToHeader(command.target, command.amount, LpCalculatorModel.player1Lp, false)
        }
        if (command.target == 2) {
            addEntryToHeader(command.target, command.amount, LpCalculatorModel.player2Lp, false)
        }
    }

    private fun addHeader() {
        val turnString = getString(R.string.turn).trim { it <= ' ' } + " " + LpCalculatorModel.currentTurn
        val inflater = activity.layoutInflater
        @SuppressLint("InflateParams") val header = inflater.inflate(R.layout.log_list_group, null) as LinearLayout
        val text = ButterKnife.findById<TextView>(header, R.id.logListHeader)
        text.setText(turnString)
        headers[LpCalculatorModel.currentTurn] = header
        lvEntries!!.addView(header, 0)
    }

    private fun addEntryToHeader(player: Int, diff: Int, lpAfter: Int, isIncrease: Boolean) {
        val header = headers[LpCalculatorModel.currentTurn]
        val inflater = activity.layoutInflater
        val entry = inflater.inflate(R.layout.log_list_item, null) as LinearLayout
        val tvName = ButterKnife.findById<TextView>(entry, R.id.logListItemPlayerName)
        AutofitHelper.create(tvName)
        val tvLpDifference = ButterKnife.findById<TextView>(entry, R.id.logListItemLpDifference)
        val tvLpAfter = ButterKnife.findById<TextView>(entry, R.id.logListItemLpFinal)
        val ivArrow = ButterKnife.findById<ImageView>(entry, R.id.logListItemArrow)
        when (player) {
            1 -> tvName.text = LpCalculatorModel.player1Name
            2 -> tvName.text = LpCalculatorModel.player2Name
        }
        if (isIncrease) {
            tvLpDifference.setTextColor(resources.getColor(R.color.colorGreen))
            ivArrow.setImageDrawable(resources.getDrawable(R.drawable.arrow_up))
        } else {
            tvLpDifference.setTextColor(resources.getColor(R.color.colorRed))
            ivArrow.setImageDrawable(resources.getDrawable(R.drawable.arrow_down))
        }
        tvLpDifference.text = Integer.toString(diff)
        tvLpAfter.text = Integer.toString(lpAfter)
        header.addView(entry, 1)
    }

    fun onAddSubtractUndo() {
        if (headers[LpCalculatorModel.currentTurn].getChildCount() > 1) {
            headers[LpCalculatorModel.currentTurn].removeViewAt(1)
        }
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LpLog.
         */
        fun newInstance(): LpLog {
            val fragment = LpLog()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor
