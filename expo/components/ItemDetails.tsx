import { Text } from "@/components/ui/text";
import { ItemUiModel } from "@/lib/types/uiModelTypes";
import { Image } from "expo-image";
import React from "react";
import { Dimensions, ScrollView, View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

const { width } = Dimensions.get("window");

export function ItemDetails({ item }: { item: ItemUiModel }) {
  const insets = useSafeAreaInsets();
  return (
    <ScrollView contentContainerStyle={{ flexGrow: 1, paddingBottom: insets.bottom }}>
      <View className="relative">
        <Image
          source={{ uri: item.imageUrl }}
          style={{ width, height: width * 0.75 }}
          contentFit="cover"
        />
      </View>

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
    </ScrollView>
  );
}
