import { Ionicons } from "@expo/vector-icons";
import { LinearGradient } from "expo-linear-gradient";
import React from "react";
import { Text, TouchableOpacity, View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

import { useColorScheme } from "@/lib/useColorScheme";

export function Header() {
  const { colorScheme, toggleColorScheme } = useColorScheme();
  const insets = useSafeAreaInsets();

  const gradientColors =
    colorScheme === "dark"
      ? ["#20102b", "#38124d"]
      : ["#fef3c7", "#fde68a"];

  return (
    <LinearGradient
      colors={gradientColors}
      start={{ x: 0, y: 0 }}
      end={{ x: 1, y: 1 }}
      style={{ paddingTop: insets.top }}
      className="px-4 pb-4"
    >
      <View className="flex-row items-center justify-between">
        <Text className="text-3xl font-extrabold text-foreground">RLAD</Text>
        <TouchableOpacity
          onPress={toggleColorScheme}
          className="rounded-full bg-background/50 p-2"
        >
          <Ionicons
            name={colorScheme === "dark" ? "sunny-outline" : "moon-outline"}
            size={20}
            className="text-foreground"
          />
        </TouchableOpacity>
      </View>
    </LinearGradient>
  );
}
