import { Ionicons } from "@expo/vector-icons";
import React from "react";
import { TouchableOpacity } from "react-native";

interface IconButtonProps {
  onPress: () => void;
  iconName: keyof typeof Ionicons.glyphMap;
  color?: string;
}

export function IconButton({ onPress, iconName, color = "white" }: IconButtonProps) {
  return (
    <TouchableOpacity
      onPress={onPress}
      style={{
        backgroundColor: "rgba(0,0,0,0.3)",
        borderRadius: 25,
        width: 50,
        height: 50,
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Ionicons
        name={iconName}
        size={24}
        color={color}
      />
    </TouchableOpacity>
  );
}
