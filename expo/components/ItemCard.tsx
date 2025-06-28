import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import { useTheme } from "@react-navigation/native";
import { Image } from "expo-image";
import React from "react";
import { Text, TouchableOpacity, View } from "react-native";

interface ItemCardProps {
  item: ItemUiModel;
  onPress: (item: ItemUiModel) => void;
}

export function ItemCard({ item, onPress }: ItemCardProps) {
  const { colors } = useTheme();

  return (
    <TouchableOpacity
      onPress={() => onPress(item)}
      style={{
        flex: 1,
        margin: 8,
        backgroundColor: colors.card,
        borderRadius: 16,
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
        elevation: 2,
      }}
    >
      <View
        style={{
          borderRadius: 16,
          overflow: "hidden",
        }}
      >
        <Image
          source={{ uri: item.imageUrl }}
          style={{ width: "100%", height: 150 }}
          contentFit="cover"
        />
        <View style={{ padding: 16 }}>
          <Text
            numberOfLines={1}
            style={{
              fontSize: 16,
              fontWeight: "bold",
              textOverflow: "ellipsis",
              color: colors.text,
            }}
          >
            {item.name}
          </Text>
          <Text
            numberOfLines={1}
            style={{
              fontSize: 14,
              textOverflow: "ellipsis",
              color: colors.text,
              marginTop: 8,
            }}
          >
            {item.cardCaption}
          </Text>
        </View>
      </View>
    </TouchableOpacity>
  );
}
