import { Text } from "@/components/ui/text";
import { ItemUiModel } from "@/lib/types/uiModelTypes";
import { Image } from "expo-image";
import { LinearGradient } from "expo-linear-gradient";
import React from "react";
import { Dimensions, ScrollView, View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

const { width } = Dimensions.get("window");
const HERO_HEIGHT = width * 0.75;

export function ItemDetails({ item }: { item: ItemUiModel }) {
  const insets = useSafeAreaInsets();

  return (
    <ScrollView contentContainerStyle={{ flexGrow: 1, paddingBottom: insets.bottom }}>
      <View
        className="relative overflow-hidden"
        style={{ height: HERO_HEIGHT }}
      >
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
        <View style={{ position: "absolute", bottom: 16, left: 16, right: 16 }}>
          <Text className="text-2xl font-bold text-white">
            {item.name}
          </Text>
        </View>
      </View>

      <View className="relative z-10 -mt-6 rounded-t-3xl bg-background">
        <View className="p-6">
          {item.detailsKeyValues.map((detail, index) => (
            <View
              key={index}
              className="mb-4"
            >
              <Text className="mb-1 text-lg font-semibold">{detail.key}</Text>
              <Text className="text-base leading-6">{detail.value}</Text>
            </View>
          ))}
        </View>
      </View>
    </ScrollView>
  );
}
