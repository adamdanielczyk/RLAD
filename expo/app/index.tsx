import { fetchItems } from "@/lib/services/dataService";
import { useAppStore } from "@/lib/store/appStore";
import { ItemUiModel } from "@/lib/types/uiModelTypes";
import { useRouter } from "expo-router";
import React, { useCallback, useEffect } from "react";
import { ActivityIndicator, Alert, FlatList, RefreshControl, Text, View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

import { DataSourceBottomSheet } from "@/components/DataSourceBottomSheet";
import { ItemCard } from "@/components/ItemCard";
import { SearchBar } from "@/components/SearchBar";

export default function HomeScreen() {
  const router = useRouter();

  const items = useAppStore((state) => state.items);
  const isLoading = useAppStore((state) => state.isLoading);
  const isInitialized = useAppStore((state) => state.isInitialized);

  const loadItems = useCallback(async (offset?: number) => {
    const { isLoading, hasMorePages, searchQuery, isInitialized } = useAppStore.getState();

    if (!isInitialized) return;
    if (isLoading) return;
    if (!hasMorePages) return;

    const { addItems, setNextOffset, setHasMorePages, setIsLoading } = useAppStore.getState();
    try {
      setIsLoading(true);

      const result = await fetchItems({
        offset,
        query: searchQuery || undefined,
      });

      addItems(result.items);
      setNextOffset(result.nextOffset);
      setHasMorePages(result.hasMorePages);
    } catch (error) {
      console.error(`Failed to load items:`, error);
      Alert.alert("Error", `Failed to load items. Please try again.`);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    if (isInitialized) {
      loadItems();
    }
  }, [loadItems, isInitialized]);

  const handleRefresh = useCallback(() => {
    loadItems();
  }, [loadItems]);

  const handleLoadMore = useCallback(() => {
    const { nextOffset } = useAppStore.getState();
    loadItems(nextOffset);
  }, [loadItems]);

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
      <SearchBar />

      <FlatList
        data={items}
        renderItem={renderItem}
        keyExtractor={(item) => item.id}
        numColumns={2}
        contentContainerStyle={{ padding: 8 }}
        refreshControl={
          <RefreshControl
            refreshing={isLoading && items.length === 0}
            onRefresh={handleRefresh}
          />
        }
        onEndReached={handleLoadMore}
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

      <DataSourceBottomSheet />
    </SafeAreaView>
  );
}
