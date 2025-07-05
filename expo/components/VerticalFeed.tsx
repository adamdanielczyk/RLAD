import { FullScreenItemCard } from "@/components/FullScreenItemCard";
import { IconButton } from "@/components/IconButton";
import { useAppStore } from "@/lib/store/appStore";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import React from "react";
import { Dimensions, FlatList, View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

interface VerticalFeedProps {
  listKey: string;
  items: ItemUiModel[];
  onEndReached: () => void;
  ListFooterComponent?: React.ComponentType<any> | React.ReactElement | null;
}

export function VerticalFeed({
  listKey,
  items,
  onEndReached,
  ListFooterComponent,
}: VerticalFeedProps) {
  const { height } = Dimensions.get("window");
  const insets = useSafeAreaInsets();
  const toggleViewMode = useAppStore((state) => state.toggleViewMode);
  const onDataSourceButtonClicked = useAppStore((state) => state.onDataSourceButtonClicked);

  return (
    <View style={{ flex: 1 }}>
      <FlatList
        key={listKey}
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

      <View
        style={{
          position: "absolute",
          top: insets.top + 16,
          left: 16,
          right: 16,
          flexDirection: "row",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <IconButton
          onPress={toggleViewMode}
          iconName="apps"
        />

        <IconButton
          onPress={onDataSourceButtonClicked}
          iconName="options-outline"
        />
      </View>
    </View>
  );
}
