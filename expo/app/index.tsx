import { useItemsQuery } from "@/lib/queries/useItemsQuery";
import { useAppStore } from "@/lib/store/appStore";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import { useRouter } from "expo-router";
import React, { useCallback, useMemo } from "react";
import {
  ActivityIndicator,
  FlatList,
  RefreshControl,
  Text,
  useWindowDimensions,
  View,
} from "react-native";
import { SafeAreaView, useSafeAreaInsets } from "react-native-safe-area-context";

import { DataSourceBottomSheet } from "@/components/DataSourceBottomSheet";
import { ItemCard } from "@/components/ItemCard";
import { SearchBar } from "@/components/SearchBar";
import { useTheme } from "@react-navigation/native";

const ITEM_MIN_WIDTH = 150;

export default function HomeScreen() {
  const router = useRouter();
  const { colors } = useTheme();
  const insets = useSafeAreaInsets();

  const searchQuery = useAppStore((state) => state.searchQuery);
  const isSearchFocused = useAppStore((state) => state.isSearchFocused);
  const onSearchQueryChanged = useAppStore((state) => state.onSearchQueryChanged);
  const onSearchFocused = useAppStore((state) => state.onSearchFocused);
  const onClearButtonClicked = useAppStore((state) => state.onClearButtonClicked);
  const onFilterButtonClicked = useAppStore((state) => state.onFilterButtonClicked);

  const isBottomSheetOpen = useAppStore((state) => state.isBottomSheetOpen);
  const selectedDataSource = useAppStore((state) => state.selectedDataSource);
  const onDataSourceSelected = useAppStore((state) => state.onDataSourceSelected);
  const onBottomSheetClosed = useAppStore((state) => state.onBottomSheetClosed);

  const { items, isLoading, isRefetching, isFetching, isFetchingNextPage, loadMoreItems, refetch } =
    useItemsQuery(selectedDataSource, searchQuery);

  const { width } = useWindowDimensions();
  const numColumns = useMemo(() => {
    return Math.max(1, Math.floor(width / ITEM_MIN_WIDTH));
  }, [width]);
  const listKey = useMemo(
    () => `flatlist-${numColumns}-columns-${selectedDataSource}`,
    [numColumns, selectedDataSource],
  );

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
    [router],
  );

  return (
    <SafeAreaView
      style={{ flex: 1 }}
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
        key={listKey}
        keyExtractor={(item) => item.id}
        numColumns={numColumns}
        contentContainerStyle={{ marginHorizontal: 8, paddingBottom: insets.bottom }}
        refreshControl={
          <RefreshControl
            tintColor={colors.text}
            progressBackgroundColor={colors.card}
            colors={[colors.text]}
            refreshing={isRefetching}
            onRefresh={refetch}
          />
        }
        onEndReached={loadMoreItems}
        onEndReachedThreshold={2}
        ListFooterComponent={
          isLoading || isFetchingNextPage ? (
            <View style={{ padding: 16 }}>
              <ActivityIndicator
                size="large"
                color={colors.text}
              />
            </View>
          ) : null
        }
        ListEmptyComponent={
          !isFetching ? (
            <View style={{ padding: 16 }}>
              <Text
                style={{
                  textAlign: "center",
                  fontSize: 16,
                  fontWeight: "bold",
                  color: colors.text,
                }}
              >
                No items found
              </Text>
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
