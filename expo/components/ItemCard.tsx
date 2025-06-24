import { ItemUiModel } from "@/lib/types/uiModelTypes";
import { Image } from "expo-image";
import { LinearGradient } from "expo-linear-gradient";
import React from "react";
import { Text, TouchableOpacity, View } from "react-native";

interface ItemCardProps {
  item: ItemUiModel;
  onPress: (item: ItemUiModel) => void;
}

export function ItemCard({ item, onPress }: ItemCardProps) {
  return (
    <TouchableOpacity
      className="w-1/2 flex-1 p-2"
      onPress={() => onPress(item)}
      activeOpacity={0.8}
    >
      <View className="overflow-hidden rounded-xl shadow-lg">
        <Image
          source={{ uri: item.imageUrl }}
          style={{ width: "100%", height: 160 }}
          contentFit="cover"
        />
        <LinearGradient
          colors={["transparent", "rgba(0,0,0,0.6)"]}
          className="absolute bottom-0 left-0 right-0 h-2/5"
        />
        <View className="absolute bottom-0 left-0 right-0 p-2">
          <Text
            className="text-base font-semibold text-white"
            numberOfLines={1}
          >
            {item.name}
          </Text>
          <Text className="text-xs text-white/80" numberOfLines={1}>
            {item.cardCaption}
          </Text>
        </View>
      </View>
    </TouchableOpacity>
  );
}
