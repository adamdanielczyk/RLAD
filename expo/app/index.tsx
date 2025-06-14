import { fetchItems } from "@/lib/services/dataService";
import { DATA_SOURCES } from "@/lib/services/dataSources";
import { useAppStore } from "@/lib/store/appStore";
import { DataSourceUiModel, ItemUiModel } from "@/lib/types/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
import { useRouter } from "expo-router";
import React, { useCallback, useEffect, useRef, useState } from "react";
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
  const [loadingPage, setLoadingPage] = useState<number | null>(null);

  const {
    selectedDataSource,
    searchQuery,
    items,
    isLoading,
    currentPage,
    hasMorePages,
  } = useAppStore((state) => ({
    selectedDataSource: state.selectedDataSource,
    searchQuery: state.searchQuery,
    items: state.items,
    isLoading: state.isLoading,
    currentPage: state.currentPage,
    hasMorePages: state.hasMorePages,
  }));


  const loadItems = useCallback(
    async (page: number) => {
      const {
        setIsLoading,
        setItems,
        addItems,
        setCurrentPage,
        setHasMorePages,
        isLoading: storeIsLoading,
        hasMorePages: storeHasMorePages,
      } = useAppStore.getState();

      const isInitialLoad = page === 1;

      if (storeIsLoading) {
        return;
      }
      if (!isInitialLoad && !storeHasMorePages) {
        return;
      }

      try {
        setIsLoading(true);
        setLoadingPage(page);

        const result = await fetchItems({
          dataSource: selectedDataSource,
          page,
          query: searchQuery || undefined,
        });

        if (isInitialLoad) {
          setItems(result.items);
        } else {
          addItems(result.items);
        }

        setHasMorePages(result.hasMore);
        setCurrentPage(page);
      } catch (error) {
        console.error(
          `Failed to load${isInitialLoad ? '' : ' more'} items:`,
          error,
        );
        Alert.alert(
          'Error',
          `Failed to load${isInitialLoad ? '' : ' more'} items. Please try again.`,
        );
      } finally {
        useAppStore.getState().setIsLoading(false);
        setLoadingPage(null);
      }
    },
    [selectedDataSource, searchQuery],
  );

  useEffect(() => {
    loadItems(1);
  }, [loadItems]);

  const handleRefresh = useCallback(() => {
    loadItems(1);
  }, [loadItems]);

  const handleLoadMore = useCallback(() => {
    if (!isLoading && hasMorePages) {
      loadItems(currentPage + 1);
    }
  }, [currentPage, hasMorePages, isLoading, loadItems]);

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

  const isLoadingMore = isLoading && loadingPage !== null && loadingPage > 1;

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
            refreshing={isLoading && loadingPage === 1}
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
    </SafeAreaView>
  );
}
