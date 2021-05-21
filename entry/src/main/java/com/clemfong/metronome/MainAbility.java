package com.clemfong.metronome;

import com.clemfong.metronome.slice.LandingAbilitySlice;
import com.clemfong.metronome.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(LandingAbilitySlice.class.getName());
    }
}
