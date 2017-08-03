package com.outplaysoftworks.sidedeck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
    @BindView(R.id.holderTimer)
    LinearLayout holderTimer;
    @BindView(R.id.LpCalculatorButtonTimer)
    Button btTimer;
    @BindView(R.id.LpCalculatorButtonClear)
    Button btClear;
    @BindView(R.id.LpCalculatorButtonReset)
    Button btReset;
    @BindView(R.id.LpCalculatorButtonUndo)
    Button btUndo;
    @BindView(R.id.picker)
    HoloCircleSeekBar picker;
    @BindView(R.id.buttonResetTimer)
    Button buttonResetTimer;
    @BindView(R.id.buttonStartTimer)
    Button buttonStartTimer;
    @BindView(R.id.textTime)
    TextView textTime;



    //Minicalc stuff
    private String calcWork = "";
    final Evaluator evaluator = new Evaluator();
    @BindView(R.id.calculatorWork)
    TextView calculatorWork;
    @BindView(R.id.calculatorResults)
    TextView calculatorResults;
    @BindView(R.id.holderCalculator)
    LinearLayout holderCalculator;
    @BindView(R.id.LpCalculatorButtonCalc)
    Button buttonShowCalc;
    @BindView(R.id.calcEquals)
    Button buttonCalcEquals;

    //Sound stuff
    private SoundPool soundPool;
    private int lpCounterSoundId;
    private int lpCounterSound2Id;
    private int diceRollSoundId;
    private int coinFlipSoundId;
    private int timerWarning1SoundId;
    private int timerWarning2SoundId;
    private int timerWarning3SoundId;
    private Integer lpSoundStreamId;
    private int timerBeepSoundId;
    //Drawable stuff


    @SuppressWarnings("unused")
    private OnFragmentInteractionListener mListener;
    private boolean checkEnteredValue = false;

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
        setUpSounds();
        setPickerListener();
        currentTimeInSeconds = getDefaultTimerTime();
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
        AutofitHelper.create(btTimer);
        AutofitHelper.create(buttonShowCalc);
        AutofitHelper.create(btClear);
        AutofitHelper.create(btReset);
        AutofitHelper.create(btUndo);
    }

    private void setUpSounds() {
        //noinspection deprecation
        soundPool = new SoundPool(32, AudioManager.STREAM_MUSIC, 0);
        lpCounterSoundId = soundPool.load(getContext(), R.raw.lpcountersound, 1);
        lpCounterSound2Id = soundPool.load(getContext(), R.raw.lpcountersound2, 1);
        coinFlipSoundId = soundPool.load(getContext(), R.raw.coinflipsound, 1);
        diceRollSoundId = soundPool.load(getContext(), R.raw.dicerollsound, 1);
        timerWarning1SoundId = soundPool.load(getContext(), R.raw.timer_warning1, 1);
        timerWarning2SoundId = soundPool.load(getContext(), R.raw.timer_warning2, 1);
        timerWarning3SoundId = soundPool.load(getContext(), R.raw.timer_warning3, 1);
        timerBeepSoundId = soundPool.load(getContext(), R.raw.timer_beep, 1);
    }

    private void initUIFromModel() {
        LpCalculatorModel.resetTurns();
        btTurn.setText(getString(R.string.turn) + Integer.toString(LpCalculatorModel.getCurrentTurn()));
    }

    private void initFromSettings(boolean resetPlayerNames) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        checkEnteredValue = preferences.getBoolean(getString(R.string.KEYcheckSafeEntry), false);
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
    @SuppressWarnings("unused")
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
    public void onClickClear(){
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorTextEnteredValue)
    public void onClickEnteredValue(){
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonPlusPlayer1)
    public void onClickPlusPlayer1(){
        if(tvEnteredValue.getText().toString().equals("")) return;
        if(checkEnteredValue && (Integer.parseInt(tvEnteredValue.getText().toString()) > 100000)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.confirmEntryDialog);
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue.getText().toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, ((dialog, which) -> plusPlayer1()))
                    .setNegativeButton(R.string.notReally, ((dialog, which) -> clearEnteredValue()))
                    .show();
        } else{
            plusPlayer1();
        }
    }

    private void plusPlayer1(){
        LpLog log = MainActivity.getLogFragment();
        AddLpCommand command = new AddLpCommand(1, LpCalculatorModel.getEnteredValue(), tvPlayer1Lp, log);
        command.execute();
        playLpSound(false);
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonMinusPlayer1)
    public void onClickMinusPlayer1(){
        if(tvEnteredValue.getText().toString().equals("")) return;
        if(checkEnteredValue && (Integer.parseInt(tvEnteredValue.getText().toString()) > 100000)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.confirmEntryDialog);
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue.getText().toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, ((dialog, which) -> minusPlayer1()))
                    .setNegativeButton(R.string.notReally, ((dialog, which) -> clearEnteredValue()))
                    .show();
        } else{
            minusPlayer1();
        }
    }

    private void minusPlayer1(){
        LpLog log = MainActivity.getLogFragment();
        SubtractLpCommand command = new SubtractLpCommand(1, LpCalculatorModel.getEnteredValue(), tvPlayer1Lp, log);
        command.execute();
        playLpSound(LpCalculatorModel.getPlayer1Lp() == 0);
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonPlusPlayer2)
    public void onClickPlusPlayer2(){
        if(tvEnteredValue.getText().toString().equals("")) return;
        if(checkEnteredValue && (Integer.parseInt(tvEnteredValue.getText().toString()) > 100000)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.confirmEntryDialog);
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue.getText().toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, ((dialog, which) -> plusPlayer2()))
                    .setNegativeButton(R.string.notReally, ((dialog, which) -> clearEnteredValue()))
                    .show();
        } else{
            plusPlayer2();
        }
    }

    private void plusPlayer2(){
        LpLog log = MainActivity.getLogFragment();
        AddLpCommand command = new AddLpCommand(2, LpCalculatorModel.getEnteredValue(), tvPlayer2Lp, log);
        command.execute();
        playLpSound(false);
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonMinusPlayer2)
    public void onClickMinusPlayer2(){
        if(tvEnteredValue.getText().toString().equals("")) return;
        if(checkEnteredValue && (Integer.parseInt(tvEnteredValue.getText().toString()) > 100000)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.confirmEntryDialog);
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue.getText().toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, ((dialog, which) -> minusPlayer2()))
                    .setNegativeButton(R.string.notReally, ((dialog, which) -> clearEnteredValue()))
                    .show();
        } else{
            minusPlayer2();
        }
    }

    private void minusPlayer2(){
        LpLog log = MainActivity.getLogFragment();
        SubtractLpCommand command = new SubtractLpCommand(2, LpCalculatorModel.getEnteredValue(), tvPlayer2Lp, log);
        command.execute();
        playLpSound(LpCalculatorModel.getPlayer2Lp() == 0);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.ResetDialog);
        builder.setTitle(R.string.reset)
                .setMessage(R.string.AreYouSure)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.yes, (dialog, which) -> reset())
                .setNegativeButton(R.string.cancel, (dialog, which) -> {});
        builder.show();
    }

    private void reset() {
        initFromSettings(false);
        initUIFromModel();
        CommandDelegator.reset();
        clearEnteredValue();
        MainActivity.getLogFragment().reset();
    }

    @OnClick(R.id.LpCalculatorButtonCalc)
    public void showCalculator(){
        holderCalculator.setVisibility(View.VISIBLE);
        //timesCalculatorOpened++;
    }

    @OnClick({R.id.spacerCalculatorTop, R.id.spacerCalculatorBottom})
    public void hideCalculator(){
        holderCalculator.setVisibility(View.GONE);
    }
    @OnClick({R.id.calc0, R.id.calc1, R.id.calc2, R.id.calc3, R.id.calc4, R.id.calc5, R.id.calc6,
            R.id.calc7, R.id.calc8, R.id.calc9, R.id.calcAdd, R.id.calcMinus, R.id.calcMultiply,
            R.id.calcDivide, R.id.calcDecimal, R.id.calcLeftParen, R.id.calcRightParen})
    public void onClickCalculatorNumbersAndOperators(View view){
        shouldSendToCalc = false;
        buttonCalcEquals.setText("="); //NON-NLS
        //noinspection deprecation
        buttonCalcEquals.setBackgroundDrawable(null);
        buttonCalcEquals.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        String tag = view.getTag().toString();
        calcWork += tag;
        calculatorWork.setText(calcWork);
    }
    boolean shouldSendToCalc = false;
    @SuppressWarnings("deprecation")
    @OnClick(R.id.calcEquals)
    public void onClickCalculatorEquals(){
        if(!shouldSendToCalc) {
            String results;
            try {
                results = evaluator.evaluate(calcCorrectParens(calcWork));
                calculatorResults.setText(results);
            } catch (EvaluationException e) {
                Log.e("JEVAL: ", calcWork); //NON-NLS
                calculatorResults.setText(R.string.error);
                e.printStackTrace();
                if (calcCheckIsParenMismatch(calcWork)) {
                    calculatorResults.setText(R.string.parenMismatch);
                }
            }
            shouldSendToCalc = true;
            buttonCalcEquals.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            buttonCalcEquals.setBackgroundDrawable(getResources().getDrawable(R.drawable.calc_arrow));
            buttonCalcEquals.setText(""); //NON-NLS
        } else {
            if(calculatorResults.getText().equals("") || calculatorResults.getText().equals(
                    getResources().getString(R.string.error)) || calculatorResults.getText().equals(
                    getResources().getString(R.string.parenMismatch))){
                buttonCalcEquals.setBackgroundDrawable(null);
                buttonCalcEquals.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                buttonCalcEquals.setText("="); //NON-NLS
                return;
            }
            Double tempVal = (double) Math.round(Double.parseDouble(calculatorResults.getText().toString()));
            Integer tempInt = tempVal.intValue();
            clearEnteredValue();
            if(LpCalculatorModel.setEnteredValue(tempInt)) {
                onEnteredValueUpdated();
            }
            shouldSendToCalc = false;
            buttonCalcEquals.setBackgroundDrawable(null);
            buttonCalcEquals.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            buttonCalcEquals.setText("="); //NON-NLS
            hideCalculator();
        }
    }

    private boolean calcCheckIsParenMismatch(String expression) {
        int openCount = 0;
        int closeCount = 0;
        for (char c : expression.toCharArray()){
            if(c == '(') {
                openCount++;
            } else if(c == ')'){
                closeCount++;
            }
        }
        return closeCount != openCount;
    }

    @OnClick(R.id.calcBackspace)
    public void onClickCalcBackspace(){
        if(calcWork.length() > 0) {
            calcWork = calcWork.substring(0, calcWork.length() - 1);
            calculatorWork.setText(calcWork);
        }
    }

    @OnClick(R.id.calcClear)
    public void onClickCalcClear(){
        if(calcWork.length() == 0){
            calculatorResults.setText("");
        }
        calculatorWork.setText("");
        calcWork = "";
    }

    public String calcCorrectParens(String expression){
        if(expression.length() == 0){
            return "";
        }
        String[] temp = expression.split("");
        ArrayList<String> chars = new ArrayList<>();
        Collections.addAll(chars, temp);
        for(int i = 1; i < chars.size() - 1; i++) {
            if(chars.get(i).equals("(") && (chars.get(i-1).matches("^[0-9]") || chars.get(i-1).equals(")"))){
                chars.add(i, "*");
                i++; //Prevents infinite loop
            }
        }
        for(int i = 0; i < chars.size() - 1; i++) {
            if(chars.get(i).equals(")") && (chars.get(i+1).matches("^[0-9]") || chars.get(i+1).equals("("))){
                chars.add(i+1, "*");
                i++;
            }
        }
        StringBuilder corrected = new StringBuilder();
        for(String s : chars){
            corrected.append(s);
        }
        System.out.println(corrected);
        return corrected.toString();
    }

    @OnClick(R.id.LpCalculatorButtonTimer)
    public void onClickTimerShow(){

        getTimeFromSeconds(false);
        holderTimer.setVisibility(View.VISIBLE);
        //timesTimerOpened++;
    }

    @OnClick({R.id.buttonTimerClose, R.id.spacerTimerBottom, R.id.spacerTimerTop})
    public void onClickTimerHide(){
        holderTimer.setVisibility(View.GONE);
    }

    final Handler timerHandler = new Handler();
    final Runnable timerTask = new Runnable(){
        @Override
        public void run() {
            try {
                timerRunning = true;
                if (currentTimeInSeconds == 0) {
                    timerFinished();
                    timerBeep();
                    holderTimer.setVisibility(View.VISIBLE);
                    return;
                }
                if (currentTimeInSeconds == 301) {
                    timerAlert();
                }
                decrementTimeOnUiThread();
                timerHandler.postDelayed(this, 999);
            } catch (NullPointerException e){
                Log.d("CF: ", "timerTask continued after activity was destroyed"); //NON-NLS
            }
        }
    };

    private void timerBeep() {
        if(getIsSoundEnabled()){
            soundPool.play(timerBeepSoundId, 1, 1, 1, 0, 1);
        }
    }

    private void timerAlert() {
        //noinspection deprecation
        if(getIsSoundEnabled()){
            Random random = new Random();
            int rand = random.nextInt(2);
            switch (rand){
                case 0:
                    soundPool.play(timerWarning1SoundId, 1, 1, 1, 0, 1);
                    break;
                case 1:
                    soundPool.play(timerWarning2SoundId, 1, 1, 1, 0, 1);
                    break;
                case 2:
                    soundPool.play(timerWarning3SoundId, 1, 1, 1, 0, 1);
                    break;
            }
        }
    }

    private void decrementTimeOnUiThread(){
        this.getActivity().runOnUiThread(this::decrementTime);
    }
    private void decrementTime() {
        currentTimeInSeconds--;
        picker.setValue(currentTimeInSeconds);
        getTimeFromSeconds(true);
        //Log.d("TIMER", "decrementTime: " + currentTimeInSeconds);

    }

    private void timerFinished() {
        timerRunning = false;
    }




    private boolean timerRunning = false;
    private Integer currentTimeInSeconds;// = getDefaultTimerTime();
    @OnClick(R.id.buttonStartTimer)
    public void startTimer(){
        if(!timerRunning) {
            initializeTimerTask();
        }else //noinspection ConstantConditions
            if(timerRunning){
                stopTimer();
            }
    }

    public void stopTimer(){
        timerRunning = false;
        timerHandler.removeCallbacksAndMessages(null);
        buttonStartTimer.setText(R.string.start);
    }

    @OnClick(R.id.buttonResetTimer)
    public void resetTimer(){
        stopTimer();
        picker.setValue(getDefaultTimerTime());
        currentTimeInSeconds = getDefaultTimerTime();
        getTimeFromSeconds(false);
        btTimer.setText(R.string.timer);
    }

    public void initializeTimerTask(){
        timerHandler.removeCallbacksAndMessages(null);
        timerHandler.postDelayed(timerTask, 999);
        buttonStartTimer.setText(R.string.stop);

    }
    private void getTimeFromSeconds(Boolean setButtonTimerText){
        Integer time1 = currentTimeInSeconds / 60;
        Integer time2 = currentTimeInSeconds % 60;
        String timeMinutes = time1.toString();
        if(timeMinutes.length() < 2){
            timeMinutes = "0" + timeMinutes;
        }
        String timeSeconds = time2.toString();
        if(timeSeconds.length() < 2){
            timeSeconds = "0" + timeSeconds;
        }
        String theTime = timeMinutes + ":" + timeSeconds;
        textTime.setText(theTime);
        if(setButtonTimerText) {
            btTimer.setText(theTime);
        }
    }

    public Integer getDefaultTimerTime(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String timeInMinutes = sharedPreferences.getString(getString(R.string.KEYdefaultTimer), "40");
        return Integer.parseInt(timeInMinutes)*60; //Convert to seconds
    }

    public void setPickerListener(){
        picker.setValue(getDefaultTimerTime());
        picker.setOnSeekBarChangeListener(new HoloCircleSeekBar.OnCircleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(HoloCircleSeekBar holoCircleSeekBar, int i, boolean b) {
                currentTimeInSeconds = holoCircleSeekBar.getValue();
                getTimeFromSeconds(true);
            }

            @Override
            public void onStartTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {
                currentTimeInSeconds = holoCircleSeekBar.getValue();
                getTimeFromSeconds(false);
            }

            @Override
            public void onStopTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {
                currentTimeInSeconds = holoCircleSeekBar.getValue();
                getTimeFromSeconds(false);
            }
        });
    }

    public void playLpSound(Boolean isZero) {
        if (lpSoundStreamId != null) {
            soundPool.stop(lpSoundStreamId);
        }
        if(getIsSoundEnabled()) {
            if(isZero){
                lpSoundStreamId = soundPool.play(lpCounterSound2Id, 1, 1, 1, 0, 1);
            }
            else {
                lpSoundStreamId = soundPool.play(lpCounterSoundId, 1, 1, 1, 0, 1);
            }
        }
    }

    public boolean getIsSoundEnabled(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        return prefs.getBoolean(getString(R.string.KEYsoundOnOff), true);
    }
    public void playDiceSound() {
        if (getIsSoundEnabled()) {
            soundPool.play(diceRollSoundId, 1, 1, 1, 0, 1);
        }
    }

    public void playCoinSound() {
        if(getIsSoundEnabled()) {
            soundPool.play(coinFlipSoundId, 1, 1, 1, 0, 1);
        }
    }
}
