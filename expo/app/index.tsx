import { useItemsQuery } from "@/lib/queries/useItemsQuery";
import { useAppStore } from "@/lib/store/appStore";
import { useRouter } from "expo-router";
import React, { useCallback, useMemo } from "react";
import { ActivityIndicator, RefreshControl, Text, useWindowDimensions, View } from "react-native";
import { SafeAreaView, useSafeAreaInsets } from "react-native-safe-area-context";

import { DataSourceBottomSheet } from "@/components/DataSourceBottomSheet";
import { ItemCard } from "@/components/ItemCard";
import { SearchBar } from "@/components/SearchBar";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import Ionicons from "@expo/vector-icons/build/Ionicons";
import { useTheme } from "@react-navigation/native";
import { FlashList } from "@shopify/flash-list";

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
  const listKey = useMemo(() => `list-${selectedDataSource}`, [selectedDataSource]);

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

      {isLoading && items.length === 0 ? (
        <FullscreenLoader />
      ) : (
        <FlashList
          key={listKey}
          data={items}
          renderItem={renderItem}
          numColumns={numColumns}
          contentContainerStyle={{ paddingHorizontal: 8, paddingBottom: insets.bottom }}
          estimatedItemSize={200}
          showsVerticalScrollIndicator={false}
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
          ListFooterComponent={isFetchingNextPage ? <FooterLoader /> : null}
          ListEmptyComponent={!isFetching ? <EmptyStateView /> : null}
          removeClippedSubviews
          drawDistance={500}
          keyboardDismissMode="on-drag"
        />
      )}

      <DataSourceBottomSheet
        isOpen={isBottomSheetOpen}
        onClose={onBottomSheetClosed}
        selectedDataSource={selectedDataSource}
        onDataSourceSelected={onDataSourceSelected}
      />
    </SafeAreaView>
  );
}

function EmptyStateView() {
  const { colors } = useTheme();
  return (
    <View
      style={{
        flex: 1,
        padding: 32,
        alignItems: "center",
        justifyContent: "center",
        minHeight: 200,
      }}
    >
      <Ionicons
        name="search-outline"
        size={64}
        color={colors.text}
        style={{ opacity: 0.3, marginBottom: 16 }}
      />
      <Text
        style={{
          textAlign: "center",
          fontSize: 18,
          fontWeight: "600",
          color: colors.text,
          marginBottom: 8,
          letterSpacing: 0.3,
        }}
      >
        No items found
      </Text>
      <Text
        style={{
          textAlign: "center",
          fontSize: 14,
          color: colors.text,
          opacity: 0.6,
        }}
      >
        Try adjusting your search or data source
      </Text>
    </View>
  );
}

function FullscreenLoader() {
  const { colors } = useTheme();
  return (
    <View style={{ flex: 1, alignItems: "center", justifyContent: "center" }}>
      <ActivityIndicator
        size="large"
        color={colors.text}
      />
    </View>
  );
}

function FooterLoader() {
  const { colors } = useTheme();
  return (
    <View style={{ padding: 16, alignItems: "center" }}>
      <ActivityIndicator
        size="large"
        color={colors.text}
      />
    </View>
  );
}
