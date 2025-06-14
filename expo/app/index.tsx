import { fetchItems } from "@/lib/services/dataService";
import { useAppStore } from "@/lib/store/appStore";
import { DataSourceUiModel, ItemUiModel } from "@/lib/types/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
import BottomSheet from "@gorhom/bottom-sheet";
import { useRouter } from "expo-router";
import React, { useCallback, useEffect, useRef } from "react";
import {
  ActivityIndicator,
  Alert,
  FlatList,
  RefreshControl,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

import { DataSourceBottomSheet } from "@/components/DataSourceBottomSheet";
import { ItemCard } from "@/components/ItemCard";

export default function HomeScreen() {
  const router = useRouter();
  const bottomSheetRef = useRef<BottomSheet | null>(null);

  const selectedDataSource = useAppStore((state) => state.selectedDataSource);
  const searchQuery = useAppStore((state) => state.searchQuery);
  const items = useAppStore((state) => state.items);
  const isLoading = useAppStore((state) => state.isLoading);
  const currentPage = useAppStore((state) => state.currentPage);

  const loadItems = useCallback(
    async (page: number) => {
      const { isLoading, hasMorePages } = useAppStore.getState();

      if (isLoading) return;
      const isInitialLoad = page === 1;
      if (!isInitialLoad && !hasMorePages) return;
      if (page === currentPage) return;

      try {
        useAppStore.getState().setIsLoading(true);

        const result = await fetchItems({
          dataSource: selectedDataSource,
          page,
          query: searchQuery || undefined,
        });

        const { addItems, setCurrentPage, setHasMorePages } = useAppStore.getState();
        addItems(result.items);
        setCurrentPage(page);
        setHasMorePages(result.hasMore);
      } catch (error) {
        console.error(`Failed to load items:`, error);
        Alert.alert("Error", `Failed to load items. Please try again.`);
      } finally {
        useAppStore.getState().setIsLoading(false);
      }
    },
    [selectedDataSource, searchQuery],
  );

  useEffect(() => {
    loadItems(1);
  }, [selectedDataSource, searchQuery]);

  const handleRefresh = useCallback(() => {
    loadItems(1);
  }, [loadItems]);

  const handleLoadMore = useCallback(() => {
    loadItems(currentPage + 1);
  }, [currentPage, loadItems]);

  const handleSearchChange = useCallback((text: string) => {
    useAppStore.getState().setSearchQuery(text);
  }, []);

  const openDataSourcePicker = useCallback(() => {
    bottomSheetRef?.current?.expand();
  }, [bottomSheetRef]);

  const handleDataSourceSelected = useCallback(
    async (dataSource: DataSourceUiModel) => {
      if (dataSource.type !== selectedDataSource) {
        await useAppStore.getState().setSelectedDataSource(dataSource.type);
      }
      bottomSheetRef?.current?.close();
    },
    [selectedDataSource, bottomSheetRef],
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
      className="flex-1"
      edges={["top", "left", "right"]}
    >
      <View className="m-4 flex-row items-center rounded-lg border bg-card px-4 py-2">
        <TextInput
          className="flex-1 placeholder:text-card-foreground"
          placeholder="Search..."
          value={searchQuery}
          onChangeText={handleSearchChange}
        />
        <TouchableOpacity onPress={openDataSourcePicker}>
          <Ionicons
            name="filter"
            size={24}
            className="text-card-foreground"
          />
        </TouchableOpacity>
      </View>

      <FlatList
        data={items}
        renderItem={renderItem}
        keyExtractor={(item) => item.id}
        numColumns={2}
        contentContainerStyle={{ padding: 8 }}
        refreshControl={
          <RefreshControl
            refreshing={isLoading && currentPage === 1}
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

      <DataSourceBottomSheet
        ref={bottomSheetRef}
        onSelect={handleDataSourceSelected}
      />
    </SafeAreaView>
  );
}
