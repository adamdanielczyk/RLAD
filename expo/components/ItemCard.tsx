import { Card, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { ItemUiModel } from "@/lib/types/uiModelTypes";
import { Image } from "expo-image";
import React from "react";
import { TouchableOpacity } from "react-native";

interface ItemCardProps {
  item: ItemUiModel;
  onPress: (item: ItemUiModel) => void;
}

export function ItemCard({ item, onPress }: ItemCardProps) {
  return (
    <TouchableOpacity
      style={{ width: '50%', flex: 1, padding: 8 }}
      onPress={() => onPress(item)}
    >
      <Card>
        <Image
          source={{ uri: item.imageUrl }}
          style={{ width: "100%", height: 150 }}
          contentFit="cover"
        />
        <CardHeader>
          <CardTitle numberOfLines={1}>{item.name}</CardTitle>
          <CardDescription numberOfLines={1}>{item.cardCaption}</CardDescription>
        </CardHeader>
      </Card>
    </TouchableOpacity>
  );
}
