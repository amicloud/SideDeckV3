package com.outplaysoftworks.sidedeck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import me.grantland.widget.AutofitHelper;

import static com.outplaysoftworks.sidedeck.AppConstants.DICEROLLANIMATIONDURATION;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LpCalculator.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LpCalculator#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LpCalculator extends Fragment {
    final Evaluator evaluator = new Evaluator();
    final Handler timerHandler = new Handler();
    //Drawable stuff
    private final ArrayList<Drawable> diceDrawables = new ArrayList<>();
    private final Handler diceResetHandler = new Handler();
    private final Handler coinHandler = new Handler();
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
    @BindView(R.id.LpCalculatorButtonDice)
    Button btDice;
    @BindView(R.id.LpCalculatorButtonCoin)
    Button btCoin;
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
    boolean shouldSendToCalc = false;
    //Minicalc stuff
    private String calcWork = "";
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
    private Drawable diceRollBackgroundDrawable;
    @SuppressWarnings("unused")
    private OnFragmentInteractionListener mListener;
    private boolean checkEnteredValue = false;
    private boolean timerRunning = false;
    private Integer currentTimeInSeconds;// = getDefaultTimerTime();
    final Runnable timerTask = new Runnable() {
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
                if (currentTimeInSeconds == 302) {
                    timerAlert();
                }
                decrementTimeOnUiThread();
                timerHandler.postDelayed(this, 999);
            } catch (NullPointerException e) {
                Log.d("CF: ", "timerTask continued after activity was destroyed"); //NON-NLS
            }
        }
    };
    private int currentFrame;

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
        loadDrawables();
        setPickerListener();
        currentTimeInSeconds = getDefaultTimerTime();
        setupTextChangedListerners();
        restoreFromCommandDelegator();
    }

    @Override
    public void onDestroy() {
        coinHandler.removeCallbacksAndMessages(null);
        diceResetHandler.removeCallbacksAndMessages(null);
        timerHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void restoreFromCommandDelegator() {
        CommandDelegator.executeAll();
    }

    @SuppressWarnings("deprecation")
    private void loadDrawables() {
        diceDrawables.add(getResources().getDrawable(R.drawable.dice_1));
        diceDrawables.add(getResources().getDrawable(R.drawable.dice_2));
        diceDrawables.add(getResources().getDrawable(R.drawable.dice_3));
        diceDrawables.add(getResources().getDrawable(R.drawable.dice_4));
        diceDrawables.add(getResources().getDrawable(R.drawable.dice_5));
        diceDrawables.add(getResources().getDrawable(R.drawable.dice_6));
        diceRollBackgroundDrawable = btDice.getBackground();
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
        Context context = getContext();
        soundPool = new SoundPool(32, AudioManager.STREAM_MUSIC, 0);
        lpCounterSoundId = soundPool.load(context, R.raw.lpcountersound, 1);
        lpCounterSound2Id = soundPool.load(context, R.raw.lpcountersound2, 1);
        coinFlipSoundId = soundPool.load(context, R.raw.coinflipsound, 1);
        diceRollSoundId = soundPool.load(context, R.raw.dicerollsound, 1);
        timerWarning1SoundId = soundPool.load(context, R.raw.timer_warning1, 1);
        timerWarning2SoundId = soundPool.load(context, R.raw.timer_warning2, 1);
        timerWarning3SoundId = soundPool.load(context, R.raw.timer_warning3, 1);
        timerBeepSoundId = soundPool.load(context, R.raw.timer_beep, 1);
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
        if (resetPlayerNames) {
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

    public void setupTextChangedListerners() {
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

    private void clearEnteredValue() {
        if (LpCalculatorModel.clearEnteredValue()) {
            tvEnteredValue.setText("");
        }
    }

    public void onEnteredValueUpdated() {
        tvEnteredValue.setText(Integer.toString(LpCalculatorModel.getEnteredValue()));
    }

    @OnClick({R.id.LpCalculatorButton0, R.id.LpCalculatorButton00, R.id.LpCalculatorButton000,
            R.id.LpCalculatorButton1, R.id.LpCalculatorButton2, R.id.LpCalculatorButton3,
            R.id.LpCalculatorButton4, R.id.LpCalculatorButton5, R.id.LpCalculatorButton6,
            R.id.LpCalculatorButton7, R.id.LpCalculatorButton8, R.id.LpCalculatorButton9})
    public void onClickCalculatorNumber(View view) {
        String amount = view.getTag().toString();
        if (LpCalculatorModel.appendToEnteredValue(amount)) {
            onEnteredValueUpdated();
        }
    }

    @OnClick(R.id.LpCalculatorButtonClear)
    public void onClickClear() {
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorTextEnteredValue)
    public void onClickEnteredValue() {
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonPlusPlayer1)
    public void onClickPlusPlayer1() {
        if (tvEnteredValue.getText().toString().equals("")) return;
        if (checkEnteredValue && (Integer.parseInt(tvEnteredValue.getText().toString()) > 100000)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.confirmEntryDialog);
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue.getText().toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, ((dialog, which) -> plusPlayer1()))
                    .setNegativeButton(R.string.notReally, ((dialog, which) -> clearEnteredValue()))
                    .show();
        } else {
            plusPlayer1();
        }
    }

    private void plusPlayer1() {
        LpLog log = MainActivity.getLogFragment();
        AddLpCommand command = new AddLpCommand(1, LpCalculatorModel.getEnteredValue(), tvPlayer1Lp, log);
        command.execute();
        playLpSound(false);
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonMinusPlayer1)
    public void onClickMinusPlayer1() {
        if (tvEnteredValue.getText().toString().equals("")) return;
        if (checkEnteredValue && (Integer.parseInt(tvEnteredValue.getText().toString()) > 100000)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.confirmEntryDialog);
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue.getText().toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, ((dialog, which) -> minusPlayer1()))
                    .setNegativeButton(R.string.notReally, ((dialog, which) -> clearEnteredValue()))
                    .show();
        } else {
            minusPlayer1();
        }
    }

    private void minusPlayer1() {
        LpLog log = MainActivity.getLogFragment();
        SubtractLpCommand command = new SubtractLpCommand(1, LpCalculatorModel.getEnteredValue(), tvPlayer1Lp, log);
        command.execute();
        playLpSound(LpCalculatorModel.getPlayer1Lp() == 0);
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonPlusPlayer2)
    public void onClickPlusPlayer2() {
        if (tvEnteredValue.getText().toString().equals("")) return;
        if (checkEnteredValue && (Integer.parseInt(tvEnteredValue.getText().toString()) > 100000)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.confirmEntryDialog);
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue.getText().toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, ((dialog, which) -> plusPlayer2()))
                    .setNegativeButton(R.string.notReally, ((dialog, which) -> clearEnteredValue()))
                    .show();
        } else {
            plusPlayer2();
        }
    }

    private void plusPlayer2() {
        LpLog log = MainActivity.getLogFragment();
        AddLpCommand command = new AddLpCommand(2, LpCalculatorModel.getEnteredValue(), tvPlayer2Lp, log);
        command.execute();
        playLpSound(false);
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonMinusPlayer2)
    public void onClickMinusPlayer2() {
        if (tvEnteredValue.getText().toString().equals("")) return;
        if (checkEnteredValue && (Integer.parseInt(tvEnteredValue.getText().toString()) > 100000)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.confirmEntryDialog);
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue.getText().toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, ((dialog, which) -> minusPlayer2()))
                    .setNegativeButton(R.string.notReally, ((dialog, which) -> clearEnteredValue()))
                    .show();
        } else {
            minusPlayer2();
        }
    }

    private void minusPlayer2() {
        LpLog log = MainActivity.getLogFragment();
        SubtractLpCommand command = new SubtractLpCommand(2, LpCalculatorModel.getEnteredValue(), tvPlayer2Lp, log);
        command.execute();
        playLpSound(LpCalculatorModel.getPlayer2Lp() == 0);
        clearEnteredValue();
    }

    @OnClick(R.id.LpCalculatorButtonUndo)
    public void onClickUndo() {
        CommandDelegator.undoLastCommand();
    }

    @OnClick(R.id.LpCalculatorButtonTurn)
    public void onClickTurn() {
        LpLog log = MainActivity.getLogFragment();
        IncrementTurnCommand command = new IncrementTurnCommand(btTurn, getString(R.string.turn), log);
        command.execute();
    }

    @OnLongClick(R.id.LpCalculatorButtonTurn)
    public boolean onLongClickTurn() {
        DecrementTurnCommand command = new DecrementTurnCommand(btTurn, getString(R.string.turn));
        command.execute();
        return true;
    }

    @OnClick(R.id.LpCalculatorButtonReset)
    public void onClickReset() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.ResetDialog);
        builder.setTitle(R.string.reset)
                .setMessage(R.string.AreYouSure)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.yes, (dialog, which) -> reset())
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                });
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
    public void showCalculator() {
        holderCalculator.setVisibility(View.VISIBLE);
        //timesCalculatorOpened++;
    }

    @OnClick({R.id.spacerCalculatorTop, R.id.spacerCalculatorBottom})
    public void hideCalculator() {
        holderCalculator.setVisibility(View.GONE);
    }

    @OnClick({R.id.calc0, R.id.calc1, R.id.calc2, R.id.calc3, R.id.calc4, R.id.calc5, R.id.calc6,
            R.id.calc7, R.id.calc8, R.id.calc9, R.id.calcAdd, R.id.calcMinus, R.id.calcMultiply,
            R.id.calcDivide, R.id.calcDecimal, R.id.calcLeftParen, R.id.calcRightParen})
    public void onClickCalculatorNumbersAndOperators(View view) {
        shouldSendToCalc = false;
        buttonCalcEquals.setText("="); //NON-NLS
        //noinspection deprecation
        buttonCalcEquals.setBackgroundDrawable(null);
        buttonCalcEquals.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        String tag = view.getTag().toString();
        calcWork += tag;
        calculatorWork.setText(calcWork);
    }

    @SuppressWarnings("deprecation")
    @OnClick(R.id.calcEquals)
    public void onClickCalculatorEquals() {
        if (!shouldSendToCalc) {
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
            if (calculatorResults.getText().equals("") || calculatorResults.getText().equals(
                    getResources().getString(R.string.error)) || calculatorResults.getText().equals(
                    getResources().getString(R.string.parenMismatch))) {
                buttonCalcEquals.setBackgroundDrawable(null);
                buttonCalcEquals.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                buttonCalcEquals.setText("="); //NON-NLS
                return;
            }
            Double tempVal = (double) Math.round(Double.parseDouble(calculatorResults.getText().toString()));
            Integer tempInt = tempVal.intValue();
            clearEnteredValue();
            if (LpCalculatorModel.setEnteredValue(tempInt)) {
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
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                openCount++;
            } else if (c == ')') {
                closeCount++;
            }
        }
        return closeCount != openCount;
    }

    @OnClick(R.id.calcBackspace)
    public void onClickCalcBackspace() {
        if (calcWork.length() > 0) {
            calcWork = calcWork.substring(0, calcWork.length() - 1);
            calculatorWork.setText(calcWork);
        }
    }

    @OnClick(R.id.calcClear)
    public void onClickCalcClear() {
        if (calcWork.length() == 0) {
            calculatorResults.setText("");
        }
        calculatorWork.setText("");
        calcWork = "";
    }

    public String calcCorrectParens(String expression) {
        if (expression.length() == 0) {
            return "";
        }
        String[] temp = expression.split("");
        ArrayList<String> chars = new ArrayList<>();
        Collections.addAll(chars, temp);
        for (int i = 1; i < chars.size() - 1; i++) {
            if (chars.get(i).equals("(") && (chars.get(i - 1).matches("^[0-9]") || chars.get(i - 1).equals(")"))) {
                chars.add(i, "*");
                i++; //Prevents infinite loop
            }
        }
        for (int i = 0; i < chars.size() - 1; i++) {
            if (chars.get(i).equals(")") && (chars.get(i + 1).matches("^[0-9]") || chars.get(i + 1).equals("("))) {
                chars.add(i + 1, "*");
                i++;
            }
        }
        StringBuilder corrected = new StringBuilder();
        for (String s : chars) {
            corrected.append(s);
        }
        System.out.println(corrected);
        return corrected.toString();
    }

    @OnClick(R.id.LpCalculatorButtonTimer)
    public void onClickTimerShow() {

        getTimeFromSeconds(false);
        holderTimer.setVisibility(View.VISIBLE);
        //timesTimerOpened++;
    }

    @OnClick({R.id.buttonTimerClose, R.id.spacerTimerBottom, R.id.spacerTimerTop})
    public void onClickTimerHide() {
        holderTimer.setVisibility(View.GONE);
    }

    private void timerBeep() {
        if (isSoundEnabled()) {
            soundPool.play(timerBeepSoundId, 1, 1, 1, 0, 1);
        }
    }

    private void timerAlert() {
        //noinspection deprecation
        if (isSoundEnabled()) {
            Random random = new Random();
            int rand = random.nextInt(2);
            switch (rand) {
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

    private void decrementTimeOnUiThread() {
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

    @OnClick(R.id.buttonStartTimer)
    public void startTimer() {
        if (!timerRunning) {
            initializeTimerTask();
        } else //noinspection ConstantConditions
            if (timerRunning) {
                stopTimer();
            }
    }

    public void stopTimer() {
        timerRunning = false;
        timerHandler.removeCallbacksAndMessages(null);
        buttonStartTimer.setText(R.string.start);
    }

    @OnClick(R.id.buttonResetTimer)
    public void resetTimer() {
        stopTimer();
        picker.setValue(getDefaultTimerTime());
        currentTimeInSeconds = getDefaultTimerTime();
        getTimeFromSeconds(false);
        btTimer.setText(R.string.timer);
    }

    public void initializeTimerTask() {
        timerHandler.removeCallbacksAndMessages(null);
        timerHandler.postDelayed(timerTask, 999);
        buttonStartTimer.setText(R.string.stop);

    }

    private void getTimeFromSeconds(Boolean setButtonTimerText) {
        Integer time1 = currentTimeInSeconds / 60;
        Integer time2 = currentTimeInSeconds % 60;
        String timeMinutes = time1.toString();
        if (timeMinutes.length() < 2) {
            timeMinutes = "0" + timeMinutes;
        }
        String timeSeconds = time2.toString();
        if (timeSeconds.length() < 2) {
            timeSeconds = "0" + timeSeconds;
        }
        String theTime = timeMinutes + ":" + timeSeconds;
        textTime.setText(theTime);
        if (setButtonTimerText) {
            btTimer.setText(theTime);
        }
    }

    public Integer getDefaultTimerTime() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String timeInMinutes = sharedPreferences.getString(getString(R.string.KEYdefaultTimer), "40");
        return Integer.parseInt(timeInMinutes) * 60; //Convert to seconds
    }

    public void setPickerListener() {
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
        if (isSoundEnabled()) {
            if (isZero) {
                lpSoundStreamId = soundPool.play(lpCounterSound2Id, 1, 1, 1, 0, 1);
            } else {
                lpSoundStreamId = soundPool.play(lpCounterSoundId, 1, 1, 1, 0, 1);
            }
        }
    }

    public boolean isSoundEnabled() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.getBoolean(getString(R.string.KEYsoundOnOff), true);
    }

    public void playDiceSound() {
        if (isSoundEnabled()) {
            soundPool.play(diceRollSoundId, 1, 1, 1, 0, 1);
        }
    }

    public void playCoinSound() {
        if (isSoundEnabled()) {
            soundPool.play(coinFlipSoundId, 1, 1, 1, 0, 1);
        }
    }

    @OnClick(R.id.LpCalculatorButtonDice)
    public void onClickDice() {
        playDiceSound();
        btDice.setClickable(false);
        final Drawable originalBackgroundDrawable = diceRollBackgroundDrawable;
        Integer diceRollAnimationFrameCount = 12;
        RandomAnimationBuilder randomAnimationBuilder = new
                RandomAnimationBuilder(diceDrawables, DICEROLLANIMATIONDURATION, diceRollAnimationFrameCount);
        AnimationDrawable animation = randomAnimationBuilder.makeAnimation(false);
        btDice.setBackground(animation);
        btDice.setText("");
        animation.start();
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            try {
                SecureRandom secureRandom = new SecureRandom();
                int roll = secureRandom.nextInt(6);
                btDice.setAlpha(1f);
                //noinspection deprecation
                btDice.setBackgroundDrawable(diceDrawables.get(roll));
                btDice.setClickable(true);
            } catch (NullPointerException e) {
                Log.d("CF: ", "Dice handler crashed"); //NON-NLS
            }
        }, DICEROLLANIMATIONDURATION + (randomAnimationBuilder.getFrameDuration() * 2));
        resetDiceRollButtonAfterDelay(originalBackgroundDrawable);
    }

    private void resetDiceRollButtonAfterDelay(final Drawable originalBackground) {
        diceResetHandler.removeCallbacksAndMessages(null);
        diceResetHandler.postDelayed(() -> {
            try {
                btDice.setText(getString(R.string.diceRoll));
                btDice.setBackground(originalBackground);
            } catch (NullPointerException e) {
                Log.d("CF: ", "Dice reset handler crashed"); //NON-NLS
            }
        }, DICEROLLANIMATIONDURATION + 6000);
    }

    private int getCurrentFrame() {
        return currentFrame;
    }

    private void setCurrentFrame(int cf) {
        currentFrame = cf;
    }

    @OnClick(R.id.LpCalculatorButtonCoin)
    public void onClickCoinFlip() {
        playCoinSound();
        currentFrame = 0;
        btCoin.setClickable(false);
        final int frames = 10;
        final Handler handler = new Handler();
        SecureRandom secureRandom = new SecureRandom();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (getCurrentFrame() < frames) {
                        String face;
                        switch (secureRandom.nextInt(2)) {
                            case 0:
                                //noinspection UnusedAssignment
                                face = getString(R.string.coinHeads);
                            case 1:
                                //noinspection UnusedAssignment
                                face = getString(R.string.coinsTails);
                            default:
                                face = "";
                        }
                        btCoin.setText(face);
                        setCurrentFrame(getCurrentFrame() + 1);
                        handler.postDelayed(this, 200);
                    }
                    if (frames == getCurrentFrame()) {
                        resetCoinFlipAfterDelay();
                        btCoin.setClickable(true);
                    }
                } catch (NullPointerException e) {
                    Log.d("CF: ", "coin handler crashed"); //NON-NLS
                }
            }
        }, 400);
    }

    private void resetCoinFlipAfterDelay() {
        coinHandler.removeCallbacksAndMessages(null);
        coinHandler.postDelayed(() -> {
            try {
                btCoin.setText(getString(R.string.coinFlip));
            } catch (NullPointerException e) {
                Log.d("CF: ", "Coin handler 2 crashed"); //NON-NLS
            }
        }, 8000);
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

}
