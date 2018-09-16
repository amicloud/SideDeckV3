package com.outplaysoftworks.sidedeck

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar

import net.sourceforge.jeval.EvaluationException
import net.sourceforge.jeval.Evaluator

import java.security.SecureRandom
import java.util.ArrayList
import java.util.Collections
import java.util.Random

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnLongClick
import me.grantland.widget.AutofitHelper

import com.outplaysoftworks.sidedeck.AppConstants.DICEROLLANIMATIONDURATION


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LpCalculator.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LpCalculator.newInstance] factory method to
 * create an instance of this fragment.
 */
class LpCalculator : Fragment() {
    internal val evaluator = Evaluator()
    internal val timerHandler = Handler()
    //Drawable stuff
    private val diceDrawables = ArrayList<Drawable>()
    private val diceResetHandler = Handler()
    private val coinHandler = Handler()
    //    LpCalculatorModel mLpCalculatorModel;
    //Butterknife Viewbindings
    @BindView(R.id.LpCalculatorTextEnteredValue)
    internal var tvEnteredValue: TextView? = null
    @BindView(R.id.LpCalculatorTextPlayer1Lp)
    internal var tvPlayer1Lp: TextView? = null
    @BindView(R.id.LpCalculatorTextPlayer2Lp)
    internal var tvPlayer2Lp: TextView? = null
    @BindView(R.id.LpCalculatorTextPlayer1Name)
    internal var tvPlayer1Name: EditText? = null
    @BindView(R.id.LpCalculatorTextPlayer2Name)
    internal var tvPlayer2Name: EditText? = null
    @BindView(R.id.LpCalculatorButtonTurn)
    internal var btTurn: Button? = null
    @BindView(R.id.holderTimer)
    internal var holderTimer: LinearLayout? = null
    @BindView(R.id.LpCalculatorButtonTimer)
    internal var btTimer: Button? = null
    @BindView(R.id.LpCalculatorButtonClear)
    internal var btClear: Button? = null
    @BindView(R.id.LpCalculatorButtonReset)
    internal var btReset: Button? = null
    @BindView(R.id.LpCalculatorButtonUndo)
    internal var btUndo: Button? = null
    @BindView(R.id.picker)
    internal var picker: HoloCircleSeekBar? = null
    @BindView(R.id.buttonResetTimer)
    internal var buttonResetTimer: Button? = null
    @BindView(R.id.buttonStartTimer)
    internal var buttonStartTimer: Button? = null
    @BindView(R.id.textTime)
    internal var textTime: TextView? = null
    @BindView(R.id.LpCalculatorButtonDice)
    internal var btDice: Button? = null
    @BindView(R.id.LpCalculatorButtonCoin)
    internal var btCoin: Button? = null
    @BindView(R.id.calculatorWork)
    internal var calculatorWork: TextView? = null
    @BindView(R.id.calculatorResults)
    internal var calculatorResults: TextView? = null
    @BindView(R.id.holderCalculator)
    internal var holderCalculator: LinearLayout? = null
    @BindView(R.id.LpCalculatorButtonCalc)
    internal var buttonShowCalc: Button? = null
    @BindView(R.id.calcEquals)
    internal var buttonCalcEquals: Button? = null
    internal var shouldSendToCalc = false
    //Minicalc stuff
    private var calcWork = ""
    //Sound stuff
    private var soundPool: SoundPool? = null
    private var lpCounterSoundId: Int = 0
    private var lpCounterSound2Id: Int = 0
    private var diceRollSoundId: Int = 0
    private var coinFlipSoundId: Int = 0
    private var timerWarning1SoundId: Int = 0
    private var timerWarning2SoundId: Int = 0
    private var timerWarning3SoundId: Int = 0
    private var lpSoundStreamId: Int? = null
    private var timerBeepSoundId: Int = 0
    private var diceRollBackgroundDrawable: Drawable? = null
    private var mListener: OnFragmentInteractionListener? = null
    private var checkEnteredValue = false
    private var timerRunning = false
    private var currentTimeInSeconds: Int? = null// = getDefaultTimerTime();
    internal val timerTask: Runnable = object : Runnable {
        override fun run() {
            try {
                timerRunning = true
                if (currentTimeInSeconds == 0) {
                    timerFinished()
                    timerBeep()
                    holderTimer!!.visibility = View.VISIBLE
                    return
                }
                if (currentTimeInSeconds == 302) {
                    timerAlert()
                }
                decrementTimeOnUiThread()
                timerHandler.postDelayed(this, 999)
            } catch (e: NullPointerException) {
                Log.d("CF: ", "timerTask continued after activity was destroyed") //NON-NLS
            }

        }
    }
    private var currentFrame: Int = 0

    //Convert to seconds
    val defaultTimerTime: Int?
        get() {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val timeInMinutes = sharedPreferences.getString(getString(R.string.KEYdefaultTimer), "40")
            return Integer.parseInt(timeInMinutes) * 60
        }

    val isSoundEnabled: Boolean
        get() {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getBoolean(getString(R.string.KEYsoundOnOff), true)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_lp_calculator, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onStart() {
        super.onStart()
        setupAutoFitTextViews()
        initFromSettings(true)
        initUIFromModel()
        setUpSounds()
        loadDrawables()
        setPickerListener()
        currentTimeInSeconds = defaultTimerTime
        setupTextChangedListerners()
        restoreFromCommandDelegator()
    }

    override fun onDestroy() {
        coinHandler.removeCallbacksAndMessages(null)
        diceResetHandler.removeCallbacksAndMessages(null)
        timerHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun restoreFromCommandDelegator() {
        CommandDelegator.executeAll()
    }

    private fun loadDrawables() {
        diceDrawables.add(resources.getDrawable(R.drawable.dice_1))
        diceDrawables.add(resources.getDrawable(R.drawable.dice_2))
        diceDrawables.add(resources.getDrawable(R.drawable.dice_3))
        diceDrawables.add(resources.getDrawable(R.drawable.dice_4))
        diceDrawables.add(resources.getDrawable(R.drawable.dice_5))
        diceDrawables.add(resources.getDrawable(R.drawable.dice_6))
        diceRollBackgroundDrawable = btDice!!.background
    }

    private fun setupAutoFitTextViews() {
        AutofitHelper.create(tvPlayer1Lp!!)
        AutofitHelper.create(tvPlayer2Lp!!)
        AutofitHelper.create(tvPlayer1Name!!)
        AutofitHelper.create(tvPlayer2Name!!)
        AutofitHelper.create(btTimer!!)
        AutofitHelper.create(buttonShowCalc!!)
        AutofitHelper.create(btClear!!)
        AutofitHelper.create(btReset!!)
        AutofitHelper.create(btUndo!!)
    }

    private fun setUpSounds() {

        val context = context
        soundPool = SoundPool(32, AudioManager.STREAM_MUSIC, 0)
        lpCounterSoundId = soundPool!!.load(context, R.raw.lpcountersound, 1)
        lpCounterSound2Id = soundPool!!.load(context, R.raw.lpcountersound2, 1)
        coinFlipSoundId = soundPool!!.load(context, R.raw.coinflipsound, 1)
        diceRollSoundId = soundPool!!.load(context, R.raw.dicerollsound, 1)
        timerWarning1SoundId = soundPool!!.load(context, R.raw.timer_warning1, 1)
        timerWarning2SoundId = soundPool!!.load(context, R.raw.timer_warning2, 1)
        timerWarning3SoundId = soundPool!!.load(context, R.raw.timer_warning3, 1)
        timerBeepSoundId = soundPool!!.load(context, R.raw.timer_beep, 1)
    }

    private fun initUIFromModel() {
        LpCalculatorModel.resetTurns()
        btTurn!!.text = getString(R.string.turn) + Integer.toString(LpCalculatorModel.currentTurn)
    }

    private fun initFromSettings(resetPlayerNames: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        checkEnteredValue = preferences.getBoolean(getString(R.string.KEYcheckSafeEntry), false)
        LpCalculatorModel.lpDefault = Integer.parseInt(preferences.getString(getString(R.string.KEYdefaultLpSetting), "8000"))
        LpCalculatorModel.player1Name = preferences.getString(getString(R.string.KEYplayerOneDefaultNameSetting), getString(R.string.playerOne))
        LpCalculatorModel.player2Name = preferences.getString(getString(R.string.KEYplayerTwoDefaultNameSetting), getString(R.string.playerTwo))
        LpCalculatorModel.player1Lp = LpCalculatorModel.lpDefault
        LpCalculatorModel.player2Lp = LpCalculatorModel.lpDefault
        LpCalculatorModel.allowsNegativeLp = preferences.getBoolean(getString(R.string.KEYallowNegativeLp), false)
        tvPlayer1Lp!!.text = Integer.toString(LpCalculatorModel.lpDefault)
        tvPlayer2Lp!!.text = Integer.toString(LpCalculatorModel.lpDefault)
        if (resetPlayerNames) {
            LpCalculatorModel.player1Name = preferences.getString(getString(R.string.KEYplayerOneDefaultNameSetting), getString(R.string.playerOne))
            LpCalculatorModel.player2Name = preferences.getString(getString(R.string.KEYplayerTwoDefaultNameSetting), getString(R.string.playerTwo))
            tvPlayer1Name!!.setText(LpCalculatorModel.player1Name)
            tvPlayer2Name!!.setText(LpCalculatorModel.player2Name)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun setupTextChangedListerners() {
        tvPlayer1Name!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                LpCalculatorModel.player1Name = s.toString()
            }
        })
        tvPlayer2Name!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                LpCalculatorModel.player1Name = s.toString()
            }
        })
    }

    private fun clearEnteredValue() {
        if (LpCalculatorModel.clearEnteredValue()) {
            tvEnteredValue!!.text = ""
        }
    }

    fun onEnteredValueUpdated() {
        tvEnteredValue!!.text = Integer.toString(LpCalculatorModel.getEnteredValue())
    }

    @OnClick(R.id.LpCalculatorButton0, R.id.LpCalculatorButton00, R.id.LpCalculatorButton000, R.id.LpCalculatorButton1, R.id.LpCalculatorButton2, R.id.LpCalculatorButton3, R.id.LpCalculatorButton4, R.id.LpCalculatorButton5, R.id.LpCalculatorButton6, R.id.LpCalculatorButton7, R.id.LpCalculatorButton8, R.id.LpCalculatorButton9)
    fun onClickCalculatorNumber(view: View) {
        val amount = view.tag.toString()
        if (LpCalculatorModel.appendToEnteredValue(amount)) {
            onEnteredValueUpdated()
        }
    }

    @OnClick(R.id.LpCalculatorButtonClear)
    fun onClickClear() {
        clearEnteredValue()
    }

    @OnClick(R.id.LpCalculatorTextEnteredValue)
    fun onClickEnteredValue() {
        clearEnteredValue()
    }

    @OnClick(R.id.LpCalculatorButtonPlusPlayer1)
    fun onClickPlusPlayer1() {
        if (tvEnteredValue!!.text.toString() == "") return
        if (checkEnteredValue && Integer.parseInt(tvEnteredValue!!.text.toString()) > 100000) {
            val builder = AlertDialog.Builder(context, R.style.confirmEntryDialog)
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue!!.text.toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, { dialog, which -> plusPlayer1() })
                    .setNegativeButton(R.string.notReally, { dialog, which -> clearEnteredValue() })
                    .show()
        } else {
            plusPlayer1()
        }
    }

    private fun plusPlayer1() {
        val log = MainActivity.logFragment
        val command = AddLpCommand(1, LpCalculatorModel.getEnteredValue(), tvPlayer1Lp, log)
        command.execute()
        playLpSound(false)
        clearEnteredValue()
    }

    @OnClick(R.id.LpCalculatorButtonMinusPlayer1)
    fun onClickMinusPlayer1() {
        if (tvEnteredValue!!.text.toString() == "") return
        if (checkEnteredValue && Integer.parseInt(tvEnteredValue!!.text.toString()) > 100000) {
            val builder = AlertDialog.Builder(context, R.style.confirmEntryDialog)
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue!!.text.toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, { dialog, which -> minusPlayer1() })
                    .setNegativeButton(R.string.notReally, { dialog, which -> clearEnteredValue() })
                    .show()
        } else {
            minusPlayer1()
        }
    }

    private fun minusPlayer1() {
        val log = MainActivity.logFragment
        val command = SubtractLpCommand(1, LpCalculatorModel.getEnteredValue(), tvPlayer1Lp, log)
        command.execute()
        playLpSound(LpCalculatorModel.player1Lp == 0)
        clearEnteredValue()
    }

    @OnClick(R.id.LpCalculatorButtonPlusPlayer2)
    fun onClickPlusPlayer2() {
        if (tvEnteredValue!!.text.toString() == "") return
        if (checkEnteredValue && Integer.parseInt(tvEnteredValue!!.text.toString()) > 100000) {
            val builder = AlertDialog.Builder(context, R.style.confirmEntryDialog)
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue!!.text.toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, { dialog, which -> plusPlayer2() })
                    .setNegativeButton(R.string.notReally, { dialog, which -> clearEnteredValue() })
                    .show()
        } else {
            plusPlayer2()
        }
    }

    private fun plusPlayer2() {
        val log = MainActivity.logFragment
        val command = AddLpCommand(2, LpCalculatorModel.getEnteredValue(), tvPlayer2Lp, log)
        command.execute()
        playLpSound(false)
        clearEnteredValue()
    }

    @OnClick(R.id.LpCalculatorButtonMinusPlayer2)
    fun onClickMinusPlayer2() {
        if (tvEnteredValue!!.text.toString() == "") return
        if (checkEnteredValue && Integer.parseInt(tvEnteredValue!!.text.toString()) > 100000) {
            val builder = AlertDialog.Builder(context, R.style.confirmEntryDialog)
            builder.setTitle(R.string.checkForSafeEntryTitle)
                    .setMessage(getString(R.string.theEnteredNumber) + tvEnteredValue!!.text.toString() + getString(R.string.seemsTooLarge))
                    .setPositiveButton(R.string.yes, { dialog, which -> minusPlayer2() })
                    .setNegativeButton(R.string.notReally, { dialog, which -> clearEnteredValue() })
                    .show()
        } else {
            minusPlayer2()
        }
    }

    private fun minusPlayer2() {
        val log = MainActivity.logFragment
        val command = SubtractLpCommand(2, LpCalculatorModel.getEnteredValue(), tvPlayer2Lp, log)
        command.execute()
        playLpSound(LpCalculatorModel.player2Lp == 0)
        clearEnteredValue()
    }

    @OnClick(R.id.LpCalculatorButtonUndo)
    fun onClickUndo() {
        CommandDelegator.undoLastCommand()
    }

    @OnClick(R.id.LpCalculatorButtonTurn)
    fun onClickTurn() {
        val log = MainActivity.logFragment
        val command = IncrementTurnCommand(btTurn, getString(R.string.turn), log)
        command.execute()
    }

    @OnLongClick(R.id.LpCalculatorButtonTurn)
    fun onLongClickTurn(): Boolean {
        val command = DecrementTurnCommand(btTurn, getString(R.string.turn))
        command.execute()
        return true
    }

    @OnClick(R.id.LpCalculatorButtonReset)
    fun onClickReset() {
        val builder = AlertDialog.Builder(context, R.style.ResetDialog)
        builder.setTitle(R.string.reset)
                .setMessage(R.string.AreYouSure)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.yes, { dialog, which -> reset() })
                .setNegativeButton(R.string.cancel, { dialog, which -> })
        builder.show()
    }

    private fun reset() {
        initFromSettings(false)
        initUIFromModel()
        CommandDelegator.reset()
        clearEnteredValue()
        MainActivity.logFragment!!.reset()
    }

    @OnClick(R.id.LpCalculatorButtonCalc)
    fun showCalculator() {
        holderCalculator!!.visibility = View.VISIBLE
        //timesCalculatorOpened++;
    }

    @OnClick(R.id.spacerCalculatorTop, R.id.spacerCalculatorBottom)
    fun hideCalculator() {
        holderCalculator!!.visibility = View.GONE
    }

    @OnClick(R.id.calc0, R.id.calc1, R.id.calc2, R.id.calc3, R.id.calc4, R.id.calc5, R.id.calc6, R.id.calc7, R.id.calc8, R.id.calc9, R.id.calcAdd, R.id.calcMinus, R.id.calcMultiply, R.id.calcDivide, R.id.calcDecimal, R.id.calcLeftParen, R.id.calcRightParen)
    fun onClickCalculatorNumbersAndOperators(view: View) {
        shouldSendToCalc = false
        buttonCalcEquals!!.text = "=" //NON-NLS

        buttonCalcEquals!!.setBackgroundDrawable(null)
        buttonCalcEquals!!.setBackgroundColor(resources.getColor(R.color.colorAccent))
        val tag = view.tag.toString()
        calcWork += tag
        calculatorWork!!.text = calcWork
    }

    @OnClick(R.id.calcEquals)
    fun onClickCalculatorEquals() {
        if (!shouldSendToCalc) {
            val results: String
            try {
                results = evaluator.evaluate(calcCorrectParens(calcWork))
                calculatorResults!!.text = results
            } catch (e: EvaluationException) {
                Log.e("JEVAL: ", calcWork) //NON-NLS
                calculatorResults!!.setText(R.string.error)
                e.printStackTrace()
                if (calcCheckIsParenMismatch(calcWork)) {
                    calculatorResults!!.setText(R.string.parenMismatch)
                }
            }

            shouldSendToCalc = true
            buttonCalcEquals!!.setBackgroundColor(resources.getColor(R.color.colorAccent))
            buttonCalcEquals!!.setBackgroundDrawable(resources.getDrawable(R.drawable.calc_arrow))
            buttonCalcEquals!!.text = "" //NON-NLS
        } else {
            if (calculatorResults!!.text == "" || calculatorResults!!.text == resources.getString(R.string.error) || calculatorResults!!.text == resources.getString(R.string.parenMismatch)) {
                buttonCalcEquals!!.setBackgroundDrawable(null)
                buttonCalcEquals!!.setBackgroundColor(resources.getColor(R.color.colorAccent))
                buttonCalcEquals!!.text = "=" //NON-NLS
                return
            }
            val tempVal = Math.round(java.lang.Double.parseDouble(calculatorResults!!.text.toString())).toDouble()
            val tempInt = tempVal.toInt()
            clearEnteredValue()
            if (LpCalculatorModel.setEnteredValue(tempInt)) {
                onEnteredValueUpdated()
            }
            shouldSendToCalc = false
            buttonCalcEquals!!.setBackgroundDrawable(null)
            buttonCalcEquals!!.setBackgroundColor(resources.getColor(R.color.colorAccent))
            buttonCalcEquals!!.text = "=" //NON-NLS
            hideCalculator()
        }
    }

    private fun calcCheckIsParenMismatch(expression: String): Boolean {
        var openCount = 0
        var closeCount = 0
        for (c in expression.toCharArray()) {
            if (c == '(') {
                openCount++
            } else if (c == ')') {
                closeCount++
            }
        }
        return closeCount != openCount
    }

    @OnClick(R.id.calcBackspace)
    fun onClickCalcBackspace() {
        if (calcWork.length > 0) {
            calcWork = calcWork.substring(0, calcWork.length - 1)
            calculatorWork!!.text = calcWork
        }
    }

    @OnClick(R.id.calcClear)
    fun onClickCalcClear() {
        if (calcWork.length == 0) {
            calculatorResults!!.text = ""
        }
        calculatorWork!!.text = ""
        calcWork = ""
    }

    fun calcCorrectParens(expression: String): String {
        if (expression.length == 0) {
            return ""
        }
        val temp = expression.split("".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val chars = ArrayList<String>()
        Collections.addAll(chars, *temp)
        run {
            var i = 1
            while (i < chars.size - 1) {
                if (chars[i] == "(" && (chars[i - 1].matches("^[0-9]".toRegex()) || chars[i - 1] == ")")) {
                    chars.add(i, "*")
                    i++ //Prevents infinite loop
                }
                i++
            }
        }
        var i = 0
        while (i < chars.size - 1) {
            if (chars[i] == ")" && (chars[i + 1].matches("^[0-9]".toRegex()) || chars[i + 1] == "(")) {
                chars.add(i + 1, "*")
                i++
            }
            i++
        }
        val corrected = StringBuilder()
        for (s in chars) {
            corrected.append(s)
        }
        println(corrected)
        return corrected.toString()
    }

    @OnClick(R.id.LpCalculatorButtonTimer)
    fun onClickTimerShow() {

        getTimeFromSeconds(false)
        holderTimer!!.visibility = View.VISIBLE
        //timesTimerOpened++;
    }

    @OnClick(R.id.buttonTimerClose, R.id.spacerTimerBottom, R.id.spacerTimerTop)
    fun onClickTimerHide() {
        holderTimer!!.visibility = View.GONE
    }

    private fun timerBeep() {
        if (isSoundEnabled) {
            soundPool!!.play(timerBeepSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    private fun timerAlert() {

        if (isSoundEnabled) {
            val random = Random()
            val rand = random.nextInt(2)
            when (rand) {
                0 -> soundPool!!.play(timerWarning1SoundId, 1f, 1f, 1, 0, 1f)
                1 -> soundPool!!.play(timerWarning2SoundId, 1f, 1f, 1, 0, 1f)
                2 -> soundPool!!.play(timerWarning3SoundId, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    private fun decrementTimeOnUiThread() {
        this.activity.runOnUiThread { this.decrementTime() }
    }

    private fun decrementTime() {
        currentTimeInSeconds--
        picker!!.setValue(currentTimeInSeconds!!.toFloat())
        getTimeFromSeconds(true)
        //Log.d("TIMER", "decrementTime: " + currentTimeInSeconds);

    }

    private fun timerFinished() {
        timerRunning = false
    }

    @OnClick(R.id.buttonStartTimer)
    fun startTimer() {
        if (!timerRunning) {
            initializeTimerTask()
        } else
            if (timerRunning) {
                stopTimer()
            }
    }

    fun stopTimer() {
        timerRunning = false
        timerHandler.removeCallbacksAndMessages(null)
        buttonStartTimer!!.setText(R.string.start)
    }

    @OnClick(R.id.buttonResetTimer)
    fun resetTimer() {
        stopTimer()
        picker!!.setValue(defaultTimerTime!!.toFloat())
        currentTimeInSeconds = defaultTimerTime
        getTimeFromSeconds(false)
        btTimer!!.setText(R.string.timer)
    }

    fun initializeTimerTask() {
        timerHandler.removeCallbacksAndMessages(null)
        timerHandler.postDelayed(timerTask, 999)
        buttonStartTimer!!.setText(R.string.stop)

    }

    private fun getTimeFromSeconds(setButtonTimerText: Boolean?) {
        val time1 = currentTimeInSeconds!! / 60
        val time2 = currentTimeInSeconds!! % 60
        var timeMinutes = time1.toString()
        if (timeMinutes.length < 2) {
            timeMinutes = "0$timeMinutes"
        }
        var timeSeconds = time2.toString()
        if (timeSeconds.length < 2) {
            timeSeconds = "0$timeSeconds"
        }
        val theTime = "$timeMinutes:$timeSeconds"
        textTime!!.text = theTime
        if (setButtonTimerText!!) {
            btTimer!!.text = theTime
        }
    }

    fun setPickerListener() {
        picker!!.setValue(defaultTimerTime!!.toFloat())
        picker!!.setOnSeekBarChangeListener(object : HoloCircleSeekBar.OnCircleSeekBarChangeListener {
            override fun onProgressChanged(holoCircleSeekBar: HoloCircleSeekBar, i: Int, b: Boolean) {
                currentTimeInSeconds = holoCircleSeekBar.value
                getTimeFromSeconds(true)
            }

            override fun onStartTrackingTouch(holoCircleSeekBar: HoloCircleSeekBar) {
                currentTimeInSeconds = holoCircleSeekBar.value
                getTimeFromSeconds(false)
            }

            override fun onStopTrackingTouch(holoCircleSeekBar: HoloCircleSeekBar) {
                currentTimeInSeconds = holoCircleSeekBar.value
                getTimeFromSeconds(false)
            }
        })
    }

    fun playLpSound(isZero: Boolean?) {
        if (lpSoundStreamId != null) {
            soundPool!!.stop(lpSoundStreamId!!)
        }
        if (isSoundEnabled) {
            if (isZero!!) {
                lpSoundStreamId = soundPool!!.play(lpCounterSound2Id, 1f, 1f, 1, 0, 1f)
            } else {
                lpSoundStreamId = soundPool!!.play(lpCounterSoundId, 1f, 1f, 1, 0, 1f)
            }
        }
    }

    fun playDiceSound() {
        if (isSoundEnabled) {
            soundPool!!.play(diceRollSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun playCoinSound() {
        if (isSoundEnabled) {
            soundPool!!.play(coinFlipSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    @OnClick(R.id.LpCalculatorButtonDice)
    fun onClickDice() {
        playDiceSound()
        btDice!!.isClickable = false
        val originalBackgroundDrawable = diceRollBackgroundDrawable
        val diceRollAnimationFrameCount = 12
        val randomAnimationBuilder = RandomAnimationBuilder(diceDrawables, DICEROLLANIMATIONDURATION, diceRollAnimationFrameCount)
        val animation = randomAnimationBuilder.makeAnimation(false)
        btDice!!.background = animation
        btDice!!.text = ""
        animation.start()
        val handler = Handler()
        handler.postDelayed({
            try {
                val secureRandom = SecureRandom()
                val roll = secureRandom.nextInt(6)
                btDice!!.alpha = 1f

                btDice!!.setBackgroundDrawable(diceDrawables[roll])
                btDice!!.isClickable = true
            } catch (e: NullPointerException) {
                Log.d("CF: ", "Dice handler crashed") //NON-NLS
            }
        }, (DICEROLLANIMATIONDURATION + randomAnimationBuilder.frameDuration!! * 2).toLong())
        resetDiceRollButtonAfterDelay(originalBackgroundDrawable)
    }

    private fun resetDiceRollButtonAfterDelay(originalBackground: Drawable?) {
        diceResetHandler.removeCallbacksAndMessages(null)
        diceResetHandler.postDelayed({
            try {
                btDice!!.text = getString(R.string.diceRoll)
                btDice!!.background = originalBackground
            } catch (e: NullPointerException) {
                Log.d("CF: ", "Dice reset handler crashed") //NON-NLS
            }
        }, (DICEROLLANIMATIONDURATION + 6000).toLong())
    }

    @OnClick(R.id.LpCalculatorButtonCoin)
    fun onClickCoinFlip() {
        playCoinSound()
        currentFrame = 0
        btCoin!!.isClickable = false
        val frames = 10
        val handler = Handler()
        val secureRandom = SecureRandom()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    if (currentFrame < frames) {
                        var face: String
                        when (secureRandom.nextInt(2)) {
                            0 -> {

                                face = getString(R.string.coinHeads)

                                face = getString(R.string.coinsTails)
                                face = ""
                            }
                            1 -> {
                                face = getString(R.string.coinsTails)
                                face = ""
                            }
                            else -> face = ""
                        }
                        btCoin!!.text = face
                        currentFrame = currentFrame + 1
                        handler.postDelayed(this, 200)
                    }
                    if (frames == currentFrame) {
                        resetCoinFlipAfterDelay()
                        btCoin!!.isClickable = true
                    }
                } catch (e: NullPointerException) {
                    Log.d("CF: ", "coin handler crashed") //NON-NLS
                }

            }
        }, 400)
    }

    private fun resetCoinFlipAfterDelay() {
        coinHandler.removeCallbacksAndMessages(null)
        coinHandler.postDelayed({
            try {
                btCoin!!.text = getString(R.string.coinFlip)
            } catch (e: NullPointerException) {
                Log.d("CF: ", "Coin handler 2 crashed") //NON-NLS
            }
        }, 8000)
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
    internal interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LpCalculator.
         */
        fun newInstance(): LpCalculator {
            val fragment = LpCalculator()
            val args = Bundle()
            //        args.putString(ARG_PARAM1, param1);
            //        args.putString(ARG_PARAM2, param2);
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
