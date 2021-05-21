package com.clemfong.metronome.helpers;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class Log {
    static private final String defaultTag = "metronome_tag";
    static final HiLogLabel tagLabel = new HiLogLabel(HiLog.LOG_APP, 0, defaultTag);

    private static HiLogLabel createTagLabel (String tag) {
        return new HiLogLabel(HiLog.LOG_APP, 0, StringHelper.isEmpty(tag) ? defaultTag : tag);
    }

    public static void d(String tag, String format) {
        d(tag, format, null);
    }

    public static void d(String tag, String format, Object args[]) {
        try {
            HiLog.debug(createTagLabel(tag), format, args);
        } catch(Error e) {
            HiLog.debug(tagLabel, "Error: " + e, null);
        }
    }
}
