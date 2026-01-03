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
 * PRIZE-based controller for managing the UDFPS HBM (High Brightness Mode) Scrim
 * on MediaTek devices lacking LHBM support.
 *
 * Logic reversed from Smali: Lcom/android/systemui/biometrics/PriUdfpsScrimController;
 */
public class PriUdfpsScrimController {

    private static PriUdfpsScrimController mUdfpsScrimController;

    // Accessed directly by UdfpsController
    public View mScrimView;
    public WindowManager mWindowManager;

    public static PriUdfpsScrimController getInstance() {
        if (mUdfpsScrimController == null) {
            mUdfpsScrimController = new PriUdfpsScrimController();
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
        float inverted = (255 - brightness) / 255.0f;

        float alpha = (inverted * 0.707147f) + 0.23499756f;

        // Midpoint bias
        if (brightness < 128) {
            alpha -= 0.005f;
        } else if (brightness > 128) {
            alpha += 0.005f;
        }

        // Clamp
        alpha = Math.max(0.20142648f, Math.min(alpha, 0.9185735f));

        return alpha;
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
