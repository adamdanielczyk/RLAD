import { Card } from "@/components/ui/card";
import { Text } from "@/components/ui/text";
import { ItemUiModel } from "@/lib/types/uiModelTypes";
import { Image } from "expo-image";
import { LinearGradient } from "expo-linear-gradient";
import React from "react";
import { TouchableOpacity, View } from "react-native";

interface ItemCardProps {
  item: ItemUiModel;
  onPress: (item: ItemUiModel) => void;
}

export function ItemCard({ item, onPress }: ItemCardProps) {
  return (
    <TouchableOpacity
      className="w-1/2 flex-1 p-2"
      onPress={() => onPress(item)}
    >
      <Card className="overflow-hidden rounded-xl shadow-lg">
        <View className="relative">
          <Image
            source={{ uri: item.imageUrl }}
            style={{ width: "100%", height: 150 }}
            contentFit="cover"
            transition={300}
          />
          <LinearGradient
            colors={["transparent", "rgba(0,0,0,0.6)"]}
            className="absolute bottom-0 left-0 right-0 px-3 py-2"
          >
            <Text className="text-base font-semibold text-white line-clamp-1">
              {item.name}
            </Text>
            {item.cardCaption ? (
              <Text className="text-xs text-white opacity-80 line-clamp-1">
                {item.cardCaption}
              </Text>
            ) : null}
          </LinearGradient>
        </View>
      </Card>
    </TouchableOpacity>
  );
}
