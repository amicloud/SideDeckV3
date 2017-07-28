package com.outplaysoftworks.sidedeck;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LpLog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LpLog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LpLog extends Fragment {
    private OnFragmentInteractionListener mListener;
    HashMap<Integer, LinearLayout> headers = new HashMap<Integer, LinearLayout>();

    @BindView(R.id.loglist)
    LinearLayout lvEntries;

    public LpLog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LpLog.
     */
    public static LpLog newInstance() {
        LpLog fragment = new LpLog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lp_log, container, false);
        ButterKnife.bind(this, view);
        addHeader();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    /*
        * Preparing the list data
        */


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onTurnIncremented(Command command){
        if(!headers.containsKey(LpCalculatorModel.getCurrentTurn())){
            addHeader();
        }
    }

    public void onAdd(AddLpCommand command){
        if(command.getTarget() == 1){
            addEntryToHeader(command.getTarget(), command.getAmount(), LpCalculatorModel.getPlayer1Lp(), true);
        }
        if(command.getTarget() == 2){
            addEntryToHeader(command.getTarget(), command.getAmount(), LpCalculatorModel.getPlayer2Lp(), true);
        }
    }

    public void onSubtract(SubtractLpCommand command){
        if(command.getTarget() == 1){
            addEntryToHeader(command.getTarget(), command.getAmount(), LpCalculatorModel.getPlayer1Lp(), false);
        }
        if(command.getTarget() == 2){
            addEntryToHeader(command.getTarget(), command.getAmount(), LpCalculatorModel.getPlayer2Lp(), false);
        }
    }

    public void addHeader(){
        String turnString = getString(R.string.turn).trim() + " " + LpCalculatorModel.getCurrentTurn();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout header = (LinearLayout) inflater.inflate(R.layout.log_list_group, null);
        TextView text = ButterKnife.findById(header, R.id.logListHeader);
        text.setText(turnString);
        headers.put(LpCalculatorModel.getCurrentTurn(), header);
        lvEntries.addView(header, 0);
    }

    public void addEntryToHeader(int player, int diff, int lpAfter, boolean isIncrease){
        LinearLayout header = headers.get(LpCalculatorModel.getCurrentTurn());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout entry = (LinearLayout) inflater.inflate(R.layout.log_list_item, null);
        TextView tvName = ButterKnife.findById(entry, R.id.logListItemPlayerName);
        TextView tvLpDifference = ButterKnife.findById(entry, R.id.logListItemLpDifference);
        TextView tvLpAfter = ButterKnife.findById(entry, R.id.logListItemLpFinal);
        ImageView ivArrow = ButterKnife.findById(entry, R.id.logListItemArrow);
        switch(player){
            case 1:
                tvName.setText(LpCalculatorModel.getPlayer1Name());
                break;
            case 2:
                tvName.setText(LpCalculatorModel.getPlayer2Name());
                break;
        }
        //TODO: Arrow drawables
        if(isIncrease){
            tvLpDifference.setTextColor(getResources().getColor(R.color.colorGreen));
        } else{
            tvLpDifference.setTextColor(getResources().getColor(R.color.colorRed));
        }
        tvLpDifference.setText(Integer.toString(diff));
        tvLpAfter.setText(Integer.toString(lpAfter));


        header.addView(entry, 1);
    }














}
