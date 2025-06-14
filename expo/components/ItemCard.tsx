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
      className="w-1/2 p-2"
      onPress={() => onPress(item)}
    >
      <Card className="w-full">
        <Image
          source={{ uri: item.imageUrl }}
          style={{ width: "100%", height: 150 }}
          contentFit="cover"
        />
        <CardHeader>
          <CardTitle className="line-clamp-1">{item.name}</CardTitle>
          <CardDescription className="line-clamp-1">{item.cardCaption}</CardDescription>
        </CardHeader>
      </Card>
    </TouchableOpacity>
  );
}
