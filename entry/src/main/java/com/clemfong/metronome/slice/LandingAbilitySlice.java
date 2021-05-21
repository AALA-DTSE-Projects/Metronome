package com.clemfong.metronome.slice;

import com.clemfong.metronome.ResourceTable;
import com.clemfong.metronome.helpers.Log;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class LandingAbilitySlice extends AbilitySlice  implements Component.ClickedListener{

    private static final String TAG = "landing_tag";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_landing);
        uiInitialize();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_btn_start:
                Log.d(TAG, "clickStart");
                present(new MainAbilitySlice(), new Intent());
                break;
            default:
                break;
        }
    }

    private void uiInitialize() {
        Button btnStart = (Button)this.findComponentById(ResourceTable.Id_btn_start);
        btnStart.setClickedListener(this);
    }
}
