package com.clemfong.metronome.slice;

import com.clemfong.metronome.ResourceTable;
import com.clemfong.metronome.data.TempoMarking;
import com.clemfong.metronome.helpers.Log;
import com.clemfong.metronome.helpers.StringHelper;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.window.service.WindowManager;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.media.audio.SoundPlayer;
import ohos.vibrator.agent.VibratorAgent;
import ohos.vibrator.bean.VibrationPattern;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener, Text.EditorActionListener {
    // Constants
    private static final String TAG = "main_tag";
    private static final int unitMinToSec = 60;
    private static final int unitSecToMs = 1000;
    private static final int editorActionOpen = 16908329;
    private static final int editorActionClose = 16908319;

    // UI Components
    private Button btnTick;
    private TextField txtTempo;
    private Text txtMarking;
    private Button btnDecrement;
    private Button btnIncrement;

    // System Components
    private VibratorAgent vibratorAgent = new VibratorAgent();
    private Timer timer;

    // Variables
    private int vibratorId;
    private boolean isTicking = false;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        setUpVibrator();
        uiInitialize();
    }

    @Override
    public void onActive() {
        super.onActive();
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_SCREEN_ON_ALWAYS);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        getWindow().clearFlags(WindowManager.LayoutConfig.MARK_SCREEN_ON_ALWAYS);
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_btn_tick:
                toggleMetronome();
                break;
            case ResourceTable.Id_btn_decrement:
                Log.d(TAG, "decrease");
                changeTempo(-1);
                break;
            case ResourceTable.Id_btn_increment:
                Log.d(TAG, "increase");
                changeTempo(1);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTextEditorAction(int action) {
        Log.d(TAG, "edit action: "+action);
        String currTempo = getTempoFaceValue();
        switch (action) {
            case editorActionOpen:
                txtTempo.setText("");
                break;
            case editorActionClose:
                Log.d(TAG, "end input: " + getTempoFaceValue());
                if (StringHelper.isEmpty(getTempoFaceValue())) {
                    txtTempo.setText(currTempo);
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void metronomeTick (double tempo) {
        timer.schedule( new TimerTask() {
            public void run() {
                Log.d(TAG, "tick: " + tempo);
                tick();
                double newTempo = getTempo() > 0 ? getTempo() : tempo;
                metronomeTick(newTempo);
            }
        }, (long) tempo);
    }

    private void uiInitialize() {
        btnTick = (Button)this.findComponentById(ResourceTable.Id_btn_tick);
        btnTick.setClickedListener(this);
        txtTempo = (TextField)this.findComponentById(ResourceTable.Id_txt_tempo);
        txtTempo.setEditorActionListener(this);
        txtMarking = (Text) this.findComponentById(ResourceTable.Id_txt_marking);
        btnDecrement = (Button)this.findComponentById(ResourceTable.Id_btn_decrement);
        btnDecrement.setClickedListener(this);
        btnIncrement = (Button)this.findComponentById(ResourceTable.Id_btn_increment);
        btnIncrement.setClickedListener(this);

    }

    private void changeTempo (int change) {
        txtTempo.setText(Integer.parseInt(getTempoFaceValue())+change+"");
    }

    private void setUpVibrator () {
        List<Integer> vibratorList = vibratorAgent.getVibratorIdList();
        if (vibratorList.isEmpty()) {
            Log.d(TAG, "viblistempty");
            return;
        }
        vibratorId = vibratorList.get(0);
        boolean isSupport = vibratorAgent.isEffectSupport(vibratorId,
                VibrationPattern.VIBRATOR_TYPE_WATCH_SYSTEMTYPE_STRENGTH1);
        Log.d(TAG, "issupport: "+isSupport);
    }

    private void tick() {
        SoundPlayer soundPlayer = new SoundPlayer("packageName");
        soundPlayer.playSound(SoundPlayer.SoundType.KEY_CLICK, 1.0f);
        if (!StringHelper.isEmpty(getTempoFaceValue()) && Integer.valueOf(getTempoFaceValue()) <= 120) {
            vibratorAgent.startOnce(vibratorId,
                    VibrationPattern.VIBRATOR_TYPE_WATCH_SYSTEMTYPE_STRENGTH1);
        }
    }

    private void toggleMetronome () {
        Log.d(TAG, "toggle");
        if (isTicking) {
            Log.d(TAG, "stop ticking");
            btnTick.setText(ResourceTable.String_lbl_start);
            timer.cancel();
        }
        else {
            Log.d(TAG, "start ticking");
            btnTick.setText(ResourceTable.String_lbl_stop);
            timer = new Timer();
            metronomeTick(getTempo());
        }
        isTicking = !isTicking;
    }

    private double getTempo () {
        int tempo = 1;
        if (!StringHelper.isEmpty(getTempoFaceValue())){
            tempo = Integer.valueOf(getTempoFaceValue());
            if (tempo <= 0) {
                tempo = 1;
            }
            else if (tempo >=301) {
                tempo = 300;
            }
        }
        Log.d(TAG, "getTempo: " + Double.valueOf(tempo) );
        txtTempo.setText(tempo+"");
        if (txtMarking.getText() != TempoMarking.getName(tempo)) {
            setTempoMarking(tempo);
        }
        return unitSecToMs/(Double.valueOf(tempo)/unitMinToSec);
    }

    private void setTempoMarking (int tempo) {
        TaskDispatcher uiTaskDispatcher = getUITaskDispatcher();
        uiTaskDispatcher.asyncDispatch(() -> txtMarking.setText(TempoMarking.getName(tempo)));
    }

    private String getTempoFaceValue () {
        return txtTempo.getText();
    }
}
