import { useAppStore } from "@/lib/store/appStore";
import { ItemUiModel } from "@/lib/types/uiModelTypes";
import { useRouter } from "expo-router";
import React, { useCallback } from "react";
import { ActivityIndicator, FlatList, RefreshControl, Text, View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

import { DataSourceBottomSheet } from "@/components/DataSourceBottomSheet";
import { ItemCard } from "@/components/ItemCard";
import { SearchBar } from "@/components/SearchBar";

export default function HomeScreen() {
  const router = useRouter();

  const items = useAppStore((state) => state.items);
  const isLoading = useAppStore((state) => state.isLoading);

  const searchQuery = useAppStore((state) => state.searchQuery);
  const isSearchFocused = useAppStore((state) => state.isSearchFocused);
  const onSearchQueryChanged = useAppStore((state) => state.onSearchQueryChanged);
  const onSearchFocused = useAppStore((state) => state.onSearchFocused);
  const onClearButtonClicked = useAppStore((state) => state.onClearButtonClicked);
  const onFilterButtonClicked = useAppStore((state) => state.onFilterButtonClicked);

  const onPullToRefresh = useAppStore((state) => state.onPullToRefresh);
  const onLoadMoreItems = useAppStore((state) => state.onLoadMoreItems);

  const isBottomSheetOpen = useAppStore((state) => state.isBottomSheetOpen);
  const selectedDataSource = useAppStore((state) => state.selectedDataSource);
  const onDataSourceSelected = useAppStore((state) => state.onDataSourceSelected);
  const onBottomSheetClosed = useAppStore((state) => state.onBottomSheetClosed);

  const renderItem = useCallback(
    ({ item }: { item: ItemUiModel }) => (
      <ItemCard
        item={item}
        onPress={(item) =>
          router.push({
            pathname: "/details/[id]",
            params: { id: item.id },
          })
        }
      />
    ),
    [],
  );

  return (
    <SafeAreaView
      className="flex-1"
      edges={["top", "left", "right"]}
    >
      <SearchBar
        query={searchQuery}
        onQueryChanged={onSearchQueryChanged}
        isFocused={isSearchFocused}
        onFocused={onSearchFocused}
        onClearButtonClicked={onClearButtonClicked}
        onFilterButtonClicked={onFilterButtonClicked}
      />

      <FlatList
        data={items}
        renderItem={renderItem}
        keyExtractor={(item) => item.id}
        numColumns={2}
        contentContainerStyle={{ padding: 8 }}
        refreshControl={
          <RefreshControl
            refreshing={isLoading && items.length === 0}
            onRefresh={onPullToRefresh}
          />
        }
        onEndReached={onLoadMoreItems}
        onEndReachedThreshold={0.1}
        ListFooterComponent={
          isLoading && items.length > 0 ? (
            <View className="items-center p-5">
              <ActivityIndicator size="large" />
            </View>
          ) : null
        }
        ListEmptyComponent={
          !isLoading ? (
            <View className="flex-1 items-center justify-center pt-[100px]">
              <Text className="text-center text-lg">No items found</Text>
            </View>
          ) : null
        }
      />

      <DataSourceBottomSheet
        isOpen={isBottomSheetOpen}
        onClose={onBottomSheetClosed}
        selectedDataSource={selectedDataSource}
        onDataSourceSelected={onDataSourceSelected}
      />
    </SafeAreaView>
  );
}
