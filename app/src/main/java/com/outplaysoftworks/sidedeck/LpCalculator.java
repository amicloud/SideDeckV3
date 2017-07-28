package com.outplaysoftworks.sidedeck;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import me.grantland.widget.AutofitHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LpCalculator.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LpCalculator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LpCalculator extends Fragment {
//    LpCalculatorModel mLpCalculatorModel;
    //Butterknife Viewbindings
    @BindView(R.id.LpCalculatorTextEnteredValue)
    TextView tvEnteredValue;
    @BindView(R.id.LpCalculatorTextPlayer1Lp)
    TextView tvPlayer1Lp;
    @BindView(R.id.LpCalculatorTextPlayer2Lp)
    TextView tvPlayer2Lp;
    @BindView(R.id.LpCalculatorTextPlayer1Name)
    EditText tvPlayer1Name;
    @BindView(R.id.LpCalculatorTextPlayer2Name)
    EditText tvPlayer2Name;
    @BindView(R.id.LpCalculatorButtonTurn)
    Button btTurn;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lp_calculator, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupAutoFitTextViews();
        initFromSettings(true);
        initUIFromModel();
        setupTextChangedListerners();
//        MainActivity.getLogFragment().reset();
        restoreFromCommandDelegator();
    }

    private void restoreFromCommandDelegator() {
        CommandDelegator.executeAll();
    }

    private void setupAutoFitTextViews() {
        AutofitHelper.create(tvPlayer1Lp);
        AutofitHelper.create(tvPlayer2Lp);
        AutofitHelper.create(tvPlayer1Name);
        AutofitHelper.create(tvPlayer2Name);
        AutofitHelper.create(tvEnteredValue);
    }

    private void initUIFromModel() {
        LpCalculatorModel.resetTurns();
        btTurn.setText(getString(R.string.turn) + Integer.toString(LpCalculatorModel.getCurrentTurn()));
    }

    private void initFromSettings(boolean resetPlayerNames) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        LpCalculatorModel.setLpDefault(Integer.parseInt(preferences.getString(getString(R.string.KEYdefaultLpSetting), "8000")));
        LpCalculatorModel.setPlayer1Name(preferences.getString(getString(R.string.KEYplayerOneDefaultNameSetting), getString(R.string.playerOne)));
        LpCalculatorModel.setPlayer2Name(preferences.getString(getString(R.string.KEYplayerTwoDefaultNameSetting), getString(R.string.playerTwo)));
        LpCalculatorModel.setPlayer1Lp(LpCalculatorModel.getLpDefault());
        LpCalculatorModel.setPlayer2Lp(LpCalculatorModel.getLpDefault());
        LpCalculatorModel.setAllowsNegativeLp(preferences.getBoolean(getString(R.string.KEYallowNegativeLp), false));
        tvPlayer1Lp.setText(Integer.toString(LpCalculatorModel.getLpDefault()));
        tvPlayer2Lp.setText(Integer.toString(LpCalculatorModel.getLpDefault()));
        if(resetPlayerNames) {
            LpCalculatorModel.setPlayer1Name(preferences.getString(getString(R.string.KEYplayerOneDefaultNameSetting), getString(R.string.playerOne)));
            LpCalculatorModel.setPlayer2Name(preferences.getString(getString(R.string.KEYplayerTwoDefaultNameSetting), getString(R.string.playerTwo)));
            tvPlayer1Name.setText(LpCalculatorModel.getPlayer1Name());
            tvPlayer2Name.setText(LpCalculatorModel.getPlayer2Name());
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setupTextChangedListerners(){
        tvPlayer1Name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LpCalculatorModel.setPlayer1Name(s.toString());
            }
        });
        tvPlayer2Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LpCalculatorModel.setPlayer1Name(s.toString());
            }
        });
    }

    private void clearEnteredValue(){
        if(LpCalculatorModel.clearEnteredValue()){
            tvEnteredValue.setText("");
        }
    }

    public void onEnteredValueUpdated(){
        //TODO: Animations!
        tvEnteredValue.setText(Integer.toString(LpCalculatorModel.getEnteredValue()));
    }



    @OnClick({R.id.LpCalculatorButton0, R.id.LpCalculatorButton00, R.id.LpCalculatorButton000,
            R.id.LpCalculatorButton1, R.id.LpCalculatorButton2, R.id.LpCalculatorButton3,
            R.id.LpCalculatorButton4, R.id.LpCalculatorButton5, R.id.LpCalculatorButton6,
            R.id.LpCalculatorButton7, R.id.LpCalculatorButton8, R.id.LpCalculatorButton9})
    public void onClickCalculatorNumber(View view){
        String amount = view.getTag().toString();
        if(LpCalculatorModel.appendToEnteredValue(amount)){
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

    @OnClick(R.id.LpCalculatorButtonPlusPlayer1)
    public void onClickPlusPlayer1(View view){
        if(tvEnteredValue.getText().equals("")) return;
        LpLog log = MainActivity.getLogFragment();
        AddLpCommand command = new AddLpCommand(1, LpCalculatorModel.getEnteredValue(), tvPlayer1Lp, log);
        command.execute();
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonMinusPlayer1)
    public void onClickMinusPlayer1(View view){
        if(tvEnteredValue.getText().equals("")) return;
        LpLog log = MainActivity.getLogFragment();
        SubtractLpCommand command = new SubtractLpCommand(1, LpCalculatorModel.getEnteredValue(), tvPlayer1Lp, log);
        command.execute();
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonPlusPlayer2)
    public void onClickPlusPlayer2(View view){
        if(tvEnteredValue.getText().equals("")) return;
        LpLog log = MainActivity.getLogFragment();
        AddLpCommand command = new AddLpCommand(2, LpCalculatorModel.getEnteredValue(), tvPlayer2Lp, log);
        command.execute();
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonMinusPlayer2)
    public void onClickMinusPlayer2(View view){
        if(tvEnteredValue.getText().equals("")) return;
        LpLog log = MainActivity.getLogFragment();
        SubtractLpCommand command = new SubtractLpCommand(2, LpCalculatorModel.getEnteredValue(), tvPlayer2Lp, log);
        command.execute();
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonUndo)
    public void onClickUndo(){
        CommandDelegator.undoLastCommand();
    }

    @OnClick(R.id.LpCalculatorButtonTurn)
    public void onClickTurn(){
        LpLog log = MainActivity.getLogFragment();
        IncrementTurnCommand command = new IncrementTurnCommand(btTurn, getString(R.string.turn), log);
        command.execute();
    }

    @OnLongClick(R.id.LpCalculatorButtonTurn)
    public boolean onLongClickTurn(){
        DecrementTurnCommand command = new DecrementTurnCommand(btTurn, getString(R.string.turn));
        command.execute();
        return true;
    }

    @OnClick(R.id.LpCalculatorButtonReset)
    public void onClickReset(){
        initFromSettings(false);
        initUIFromModel();
        CommandDelegator.reset();
        clearEnteredValue();
        MainActivity.getLogFragment().reset();
    }
}
