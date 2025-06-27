import { ItemUiModel } from "@/lib/types/uiModelTypes";
import { useTheme } from "@react-navigation/native";
import { Image } from "expo-image";
import { LinearGradient } from "expo-linear-gradient";
import React from "react";
import { Dimensions, ScrollView, Text, View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

const { width } = Dimensions.get("window");
const HERO_HEIGHT = width * 0.75;

export function ItemDetails({ item }: { item: ItemUiModel }) {
  const insets = useSafeAreaInsets();
  const { colors } = useTheme();

  return (
    <ScrollView contentContainerStyle={{ flexGrow: 1 }}>
      <View style={{ height: HERO_HEIGHT, overflow: "hidden", position: "relative" }}>
        <Image
          source={{ uri: item.imageUrl }}
          style={{ width: "100%", height: "100%" }}
          contentFit="cover"
        />

        <View style={{ position: "absolute", bottom: 0, left: 0, right: 0, height: "30%" }}>
          <LinearGradient
            colors={["transparent", "rgba(0,0,0,0.4)"]}
            style={{ flex: 1 }}
          />
        </View>
      </View>

      <View
        style={{
          position: "relative",
          zIndex: 10,
          marginTop: -24,
          borderTopLeftRadius: 24,
          borderTopRightRadius: 24,
          backgroundColor: colors.card,
        }}
      >
        <View
          style={{
            padding: 24,
            marginLeft: insets.left,
            marginRight: insets.right,
            marginBottom: insets.bottom,
          }}
        >
          {item.detailsKeyValues.map((detail, index) => (
            <View
              key={index}
              style={{ marginBottom: 16 }}
            >
              <Text
                style={{
                  marginBottom: 4,
                  fontSize: 16,
                  fontWeight: "bold",
                  color: colors.text,
                }}
              >
                {detail.key}
              </Text>
              <Text style={{ fontSize: 14, color: colors.text }}>{detail.value}</Text>
            </View>
          ))}
        </View>
      </View>
    </ScrollView>
  );
}
