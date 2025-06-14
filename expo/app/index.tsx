import { fetchItems } from "@/lib/services/dataService";
import { DATA_SOURCES } from "@/lib/services/dataSources";
import { useAppStore } from "@/lib/store/appStore";
import { DataSourceUiModel, ItemUiModel } from "@/lib/types/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
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
import BottomSheet from "@gorhom/bottom-sheet";

import { ItemCard } from "@/components/app/ItemCard";
import { DataSourcePicker } from "@/components/app/DataSourcePicker";

export default function HomeScreen() {
  const router = useRouter();
  const bottomSheetRef = useRef<BottomSheet | null>(null);

  const {
    selectedDataSource,
    searchQuery,
    items,
    isLoading,
    isLoadingMore,
    currentPage,
    hasMorePages,
  } = useAppStore((state) => ({
    selectedDataSource: state.selectedDataSource,
    searchQuery: state.searchQuery,
    items: state.items,
    isLoading: state.isLoading,
    isLoadingMore: state.isLoadingMore,
    currentPage: state.currentPage,
    hasMorePages: state.hasMorePages,
  }));


  useEffect(() => {
    loadData();
  }, [selectedDataSource, searchQuery]);

  const loadData = async () => {
    try {
      const { setIsLoading, setItems, setCurrentPage, setHasMorePages } = useAppStore.getState();

      setIsLoading(true);
      setCurrentPage(1);

      const result = await fetchItems({
        dataSource: selectedDataSource,
        page: 1,
        query: searchQuery || undefined,
      });

      setItems(result.items);
      setHasMorePages(result.hasMore);
      setCurrentPage(1);
    } catch (error) {
      console.error("Failed to load items:", error);
      Alert.alert("Error", "Failed to load items. Please try again.");
    } finally {
      useAppStore.getState().setIsLoading(false);
    }
  };

  const loadMoreData = async () => {
    if (isLoadingMore || !hasMorePages) return;

    try {
      const { setIsLoadingMore, addItems, setCurrentPage, setHasMorePages } =
        useAppStore.getState();

      setIsLoadingMore(true);

      const nextPage = currentPage + 1;
      const result = await fetchItems({
        dataSource: selectedDataSource,
        page: nextPage,
        query: searchQuery || undefined,
      });

      addItems(result.items);
      setHasMorePages(result.hasMore);
      setCurrentPage(nextPage);
    } catch (error) {
      console.error("Failed to load more items:", error);
      Alert.alert("Error", "Failed to load more items. Please try again.");
    } finally {
      useAppStore.getState().setIsLoadingMore(false);
    }
  };

  const handleRefresh = useCallback(() => {
    loadData();
  }, [selectedDataSource, searchQuery]);

  const handleLoadMore = useCallback(() => {
    loadMoreData();
  }, [currentPage, hasMorePages, isLoadingMore, selectedDataSource, searchQuery]);

  const handleItemPress = useCallback(
    (item: ItemUiModel) => {
      router.push({
        pathname: "/details/[id]",
        params: { id: item.id, dataSource: selectedDataSource },
      });
    },
    [router, selectedDataSource],
  );

  const handleDataSourceChange = useCallback(
    async (dataSource: DataSourceUiModel) => {
      if (dataSource.type !== selectedDataSource) {
        await useAppStore.getState().setSelectedDataSource(dataSource.type);
      }
      bottomSheetRef?.current?.close();
    },
    [selectedDataSource, bottomSheetRef],
  );

  const handleSearchChange = useCallback((text: string) => {
    useAppStore.getState().setSearchQuery(text);
  }, []);

  const openDataSourcePicker = useCallback(() => {
    bottomSheetRef?.current?.expand();
  }, [bottomSheetRef]);

  const renderItem = useCallback(
    ({ item }: { item: ItemUiModel }) => (
      <ItemCard item={item} onPress={handleItemPress} />
    ),
    [handleItemPress],
  );

  const dataSourceOptions = DATA_SOURCES.map((config) => ({
    name: config.name,
    type: config.type,
    isSelected: config.type === selectedDataSource,
  }));

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
            refreshing={isLoading}
            onRefresh={handleRefresh}
          />
        }
        onEndReached={handleLoadMore}
        onEndReachedThreshold={0.1}
        ListFooterComponent={
          isLoadingMore ? (
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

      <DataSourcePicker
        ref={bottomSheetRef}
        dataSources={dataSourceOptions}
        onSelect={handleDataSourceChange}
      />

      {isLoading && (
        <View className="absolute inset-0 items-center justify-center bg-black/50">
          <ActivityIndicator size="large" />
        </View>
      )}
    </SafeAreaView>
  );
}
