/*
 * Copyright (C) 2026 The LineageOS project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.biometrics;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Controller for managing the UDFPS HBM (High Brightness Mode) Scrim on MediaTek devices lacking LHBM support.
 *
 * Logic reversed from Smali: Lcom/android/systemui/biometrics/PriUdfpsScrimController; from LAVA
 */
public class MtkUdfpsScrimController {
    private static MtkUdfpsScrimController mUdfpsScrimController;

    // Accessed directly by UdfpsController
    public View mScrimView;
    public WindowManager mWindowManager;

    private Context mContext;

    public static MtkUdfpsScrimController getInstance() {
        if (mUdfpsScrimController == null) {
            mUdfpsScrimController = new MtkUdfpsScrimController();
        }
        return mUdfpsScrimController;
    }

    /**
     * Calculates the alpha (transparency) for the HBM overlay based on screen brightness.
     * The formula ensures the sensor gets a consistent amount of light.
     *
     * @param brightness Current system brightness (0-255)
     * @return Alpha value (0.0f to 1.0f)
     */
    public static float calculateAlpha(int brightness) {
        String[] alphaArray = mContext.resources.getStringArray(
            com.android.systemui.res.R.array.config_udfpsDimmingAlphaArray);
        int alphaIndex = 255 - (brightness - 1);

        Log.d("MtkUdfpsScrimController", "Alpha Array Length: " + alphaArray.length);
        Log.d("MtkUdfpsScrimController", "Requested Brightness Index: " + brightness);
        Log.d("MtkUdfpsScrimController", "Alpha Value String: " + alphaArray[alphaIndex]);

        return Integer.parseInt(alphaArray[alphaIndex]) / 255.0f;
    }

    public int getSystemBrightness(Context context) {
        if (context == null) {
            return 127; 
        }

        // Try Float first
        float brightFloat = Settings.System.getFloat(
            context.getContentResolver(),
            "screen_brightness_float", 
            -1.0f 
        );

        if (brightFloat >= 0.0f) {
            return (int) (brightFloat * 255.0f);
        }

        // Fallback
        return Settings.System.getInt(
            context.getContentResolver(),
            Settings.System.SCREEN_BRIGHTNESS,
            127
        );
    }
}
