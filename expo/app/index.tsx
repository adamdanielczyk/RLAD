import { useItemsQuery } from "@/lib/queries/useItemsQuery";
import { useAppStore } from "@/lib/store/appStore";
import * as ScreenOrientation from "expo-screen-orientation";
import React, { useEffect, useMemo } from "react";
import { ActivityIndicator, RefreshControl, View } from "react-native";
import Animated, { FadeIn, FadeOut } from "react-native-reanimated";
import { SafeAreaView, useSafeAreaInsets } from "react-native-safe-area-context";

import { DataSourceBottomSheet } from "@/components/DataSourceBottomSheet";
import { EmptyView } from "@/components/EmptyView";
import { ItemCard } from "@/components/ItemCard";
import { SearchBar } from "@/components/SearchBar";
import { VerticalFeed } from "@/components/VerticalFeed";
import { useColumns } from "@/lib/hooks/useColumns";
import { useTheme } from "@react-navigation/native";
import { FlashList } from "@shopify/flash-list";

export default function HomeScreen() {
  const searchQuery = useAppStore((state) => state.searchQuery);
  const selectedDataSource = useAppStore((state) => state.selectedDataSource);
  const viewMode = useAppStore((state) => state.viewMode);

  useEffect(() => {
    const setOrientation = async () => {
      if (viewMode === "feed") {
        await ScreenOrientation.lockAsync(ScreenOrientation.OrientationLock.PORTRAIT_UP);
      } else {
        await ScreenOrientation.unlockAsync();
      }
    };

    setOrientation();
  }, [viewMode]);

  const { items, isLoading, isRefetching, isFetching, isFetchingNextPage, loadMoreItems, refetch } =
    useItemsQuery(selectedDataSource, searchQuery);

  const { colors } = useTheme();
  const insets = useSafeAreaInsets();
  const numColumns = useColumns();
  const listKey = useMemo(() => `list-${selectedDataSource}`, [selectedDataSource]);

  const fadeInAnimation = FadeIn.duration(300);
  const fadeOutAnimation = FadeOut.duration(200);

  return (
    <SafeAreaView
      style={{ flex: 1 }}
      edges={viewMode === "grid" ? ["top", "left", "right"] : []}
    >
      {viewMode === "grid" && <SearchBar hasItems={items.length > 0} />}

      {isLoading && items.length === 0 ? (
        <FullscreenLoader />
      ) : viewMode === "grid" ? (
        <Animated.View
          key="grid"
          style={{ flex: 1 }}
          entering={fadeInAnimation}
          exiting={fadeOutAnimation}
        >
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
        </Animated.View>
      ) : (
        <Animated.View
          key="feed"
          style={{ flex: 1 }}
          entering={fadeInAnimation}
          exiting={fadeOutAnimation}
        >
          <VerticalFeed
            listKey={listKey}
            items={items}
            onEndReached={loadMoreItems}
            ListFooterComponent={isFetchingNextPage ? <FooterLoader /> : null}
          />
        </Animated.View>
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
