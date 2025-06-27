import { useTheme } from "@react-navigation/native";
import { Link } from "expo-router";
import React from "react";
import { Button, Text, View } from "react-native";

export function FullScreenErrorView() {
  const { colors } = useTheme();

  return (
    <View style={{ flex: 1, alignItems: "center", justifyContent: "center", padding: 16 }}>
      <Text style={{ fontSize: 16, fontWeight: "bold", color: colors.text, marginBottom: 16 }}>
        Error has occurred.
      </Text>
      <Link
        href="/"
        replace
        asChild
      >
        <Button
          title="Open home screen"
          color={colors.primary}
        />
      </Link>
    </View>
  );
}
