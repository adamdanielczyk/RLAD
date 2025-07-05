import { FullScreenItemCard } from "@/components/FullScreenItemCard";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import React from "react";
import { Dimensions, FlatList } from "react-native";

interface VerticalFeedProps {
  items: ItemUiModel[];
  onEndReached: () => void;
  ListFooterComponent?: React.ComponentType<any> | React.ReactElement | null;
}

export function VerticalFeed({ items, onEndReached, ListFooterComponent }: VerticalFeedProps) {
  const { height } = Dimensions.get("window");
  return (
    <FlatList
      data={items}
      renderItem={({ item }) => <FullScreenItemCard item={item} />}
      keyExtractor={(item) => item.id}
      pagingEnabled
      showsVerticalScrollIndicator={false}
      snapToInterval={height}
      snapToAlignment="start"
      decelerationRate="fast"
      onEndReached={onEndReached}
      onEndReachedThreshold={2}
      ListFooterComponent={ListFooterComponent}
      getItemLayout={(_, index) => ({
        length: height,
        offset: height * index,
        index,
      })}
    />
  );
}
