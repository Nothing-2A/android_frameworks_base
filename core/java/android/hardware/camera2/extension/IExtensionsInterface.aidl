/**
 * Copyright (c) 2025, The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.hardware.camera2.extension;

import android.hardware.camera2.extension.ParcelImage;
import android.hardware.camera2.extension.ParcelTotalCaptureResult;
import android.hardware.HardwareBuffer;

/** @hide */
interface IExtensionsInterface {
    void processNight(in ParcelImage[] inImages,
                      out ParcelImage[] outImages,
                      in ParcelTotalCaptureResult[] results,
                      in ParcelImage tuningImage);

    void processOffline(int mode,
                        in ParcelImage[] inImages,
                        out ParcelImage[] outImages,
                        in ParcelTotalCaptureResult[] results,
                        in ParcelImage tuningImage);

    void processOfflineHardwareBuffer(int mode,
                                      in HardwareBuffer inImage,
                                      in HardwareBuffer outImage,
                                      in ParcelTotalCaptureResult result,
                                      in HardwareBuffer tuningImage);

    void processNightWithOutSize(in ParcelImage[] inImages,
                                 out ParcelImage[] outImages,
                                 in ParcelTotalCaptureResult[] results,
                                 in ParcelImage tuningImage,
                                 int outW,
                                 int outH);
}
