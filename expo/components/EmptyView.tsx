import { Ionicons } from "@expo/vector-icons";
import { useTheme } from "@react-navigation/native";
import React from "react";
import { Text, View } from "react-native";

interface EmptyViewProps {
  icon: keyof typeof Ionicons.glyphMap;
  title: string;
  description: string;
}

export function EmptyView({ icon, title, description }: EmptyViewProps) {
  const { colors } = useTheme();

  return (
    <View
      style={{
        flex: 1,
        padding: 32,
        alignItems: "center",
        minHeight: 200,
      }}
    >
      <Ionicons
        name={icon}
        size={64}
        color={colors.text}
        style={{ opacity: 0.3, marginBottom: 16 }}
      />
      <Text
        style={{
          fontSize: 20,
          fontWeight: "600",
          color: colors.text,
          marginBottom: 8,
          textAlign: "center",
        }}
      >
        {title}
      </Text>
      <Text
        style={{
          fontSize: 16,
          color: colors.text,
          opacity: 0.7,
          textAlign: "center",
          lineHeight: 22,
        }}
      >
        {description}
      </Text>
    </View>
  );
}
