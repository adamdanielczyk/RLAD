import { useItemsQuery } from "@/lib/queries/useItemsQuery";
import { useAppStore } from "@/lib/store/appStore";
import React, { useMemo } from "react";
import { ActivityIndicator, RefreshControl, View } from "react-native";
import { SafeAreaView, useSafeAreaInsets } from "react-native-safe-area-context";

import { DataSourceBottomSheet } from "@/components/DataSourceBottomSheet";
import { EmptyView } from "@/components/EmptyView";
import { ItemCard } from "@/components/ItemCard";
import { SearchBar } from "@/components/SearchBar";
import { useColumns } from "@/lib/hooks/useColumns";
import { useTheme } from "@react-navigation/native";
import { FlashList } from "@shopify/flash-list";

export default function HomeScreen() {
  const searchQuery = useAppStore((state) => state.searchQuery);
  const selectedDataSource = useAppStore((state) => state.selectedDataSource);

  const { items, isLoading, isRefetching, isFetching, isFetchingNextPage, loadMoreItems, refetch } =
    useItemsQuery(selectedDataSource, searchQuery);

  const { colors } = useTheme();
  const insets = useSafeAreaInsets();
  const numColumns = useColumns();
  const listKey = useMemo(() => `list-${selectedDataSource}`, [selectedDataSource]);

  return (
    <SafeAreaView
      style={{ flex: 1 }}
      edges={["top", "left", "right"]}
    >
      <SearchBar />

      {isLoading && items.length === 0 ? (
        <FullscreenLoader />
      ) : (
        <FlashList
          key={listKey}
          data={items}
          renderItem={({ item }) => <ItemCard item={item} />}
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
          ListEmptyComponent={
            !isFetching ? (
              <EmptyView
                icon="search-outline"
                title="No items found"
                description="Try adjusting your search or data source"
              />
            ) : null
          }
          removeClippedSubviews
          keyboardDismissMode="on-drag"
        />
      )}

      <DataSourceBottomSheet />
    </SafeAreaView>
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
