#!/bin/bash

echo "Setting up Android demo mode..."

# Enable demo mode
adb shell settings put global sysui_demo_allowed 1
adb shell am broadcast -a com.android.systemui.demo -e command enter

# Perfect status bar for screenshots
adb shell am broadcast -a com.android.systemui.demo -e command clock -e hhmm 1000
adb shell am broadcast -a com.android.systemui.demo -e command battery -e level 100 -e plugged false -e powersave false
adb shell am broadcast -a com.android.systemui.demo -e command network -e wifi show -e level 4 -e fully true
adb shell am broadcast -a com.android.systemui.demo -e command network -e mobile hide
adb shell am broadcast -a com.android.systemui.demo -e command notifications -e visible false

echo "✅ Demo mode configured with WiFi only!"
echo "🔄 Run following command when done:"
echo "adb shell am broadcast -a com.android.systemui.demo -e command exit"
