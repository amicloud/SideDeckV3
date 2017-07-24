package com.outplaysoftworks.sidedeck;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LpCalculator.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LpCalculator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LpCalculator extends Fragment {
    LpCalculatorModel mLpCalculatorModel;
    //Butterknife Viewbindings
    @BindView(R.id.LpCalculatorTextEnteredValue)
    TextView tvEnteredValue;
    @BindView(R.id.LpCalculatorTextPlayer1Lp)
    TextView tvPlayer1Lp;
    @BindView(R.id.LpCalculatorTextPlayer2Lp)
    TextView tvPlayer2Lp;
    @BindView(R.id.LpCalculatorTextPlayer1Name)
    TextView tvPlayer1Name;
    @BindView   (R.id.LpCalculatorTextPlayer2Name)
    TextView tvPlayer2Name;


    private OnFragmentInteractionListener mListener;

    public LpCalculator() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LpCalculator.
     */
    public static LpCalculator newInstance() {
        LpCalculator fragment = new LpCalculator();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lp_calculator, container, false);
        ButterKnife.bind(this, view);
        mLpCalculatorModel = new LpCalculatorModel(getContext());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initFromSettings();
    }

    private void initFromSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mLpCalculatorModel.setLpDefault(Integer.parseInt(preferences.getString(getString(R.string.KEYdefaultLpSetting), "8000")));
        mLpCalculatorModel.setPlayer1Name(preferences.getString(getString(R.string.KEYplayerOneDefaultNameSetting), getString(R.string.playerOne)));
        mLpCalculatorModel.setPlayer2Name(preferences.getString(getString(R.string.KEYplayerTwoDefaultNameSetting), getString(R.string.playerTwo)));
        mLpCalculatorModel.setAllowsNegativeLp(preferences.getBoolean(getString(R.string.KEYallowNegativeLp), false));
        tvPlayer1Lp.setText(Integer.toString(mLpCalculatorModel.getLpDefault()));
        tvPlayer2Lp.setText(Integer.toString(mLpCalculatorModel.getLpDefault()));
        tvPlayer1Name.setText(mLpCalculatorModel.getPlayer1Name());
        tvPlayer2Name.setText(mLpCalculatorModel.getPlayer2Name());
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        void onFragmentInteraction(Uri uri);
    }

    private void clearEnteredValue(){
        if(mLpCalculatorModel.clearEnteredValue()){
            tvEnteredValue.setText("");
        }
    }

    public void onEnteredValueUpdated(){
        //TODO: Animations!
        tvEnteredValue.setText(Integer.toString(mLpCalculatorModel.getEnteredValue()));
    }

    @OnClick({R.id.LpCalculatorButton0, R.id.LpCalculatorButton00, R.id.LpCalculatorButton000,
            R.id.LpCalculatorButton1, R.id.LpCalculatorButton2, R.id.LpCalculatorButton3,
            R.id.LpCalculatorButton4, R.id.LpCalculatorButton5, R.id.LpCalculatorButton6,
            R.id.LpCalculatorButton7, R.id.LpCalculatorButton8, R.id.LpCalculatorButton9})
    public void onClickCalculatorNumber(View view){
        int amount = Integer.parseInt(view.getTag().toString());
        if(mLpCalculatorModel.appendToEnteredValue(amount)){
            onEnteredValueUpdated();
        }
    }


    @OnClick( R.id.LpCalculatorButtonClear)
    public void onClickClear(View view){
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorTextEnteredValue)
    public void onClickEnteredValue(View view){
        clearEnteredValue();
    }


}
