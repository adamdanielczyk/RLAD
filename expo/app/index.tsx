import { Ionicons } from "@expo/vector-icons";
import BottomSheet, { BottomSheetBackdrop, BottomSheetView } from "@gorhom/bottom-sheet";
import { Image } from "expo-image";
import { useRouter } from "expo-router";
import React, { useCallback, useEffect, useRef } from "react";
import {
  ActivityIndicator,
  Alert,
  Dimensions,
  FlatList,
  RefreshControl,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

import { useColorScheme } from "@/hooks/useColorScheme";
import { fetchItems } from "@/services/dataService";
import { DATA_SOURCES } from "@/services/dataSources";
import { useAppStore } from "@/store/appStore";
import { DataSourceUiModel, ItemUiModel } from "@/types/uiModelTypes";

const { width } = Dimensions.get("window");
const CARD_MARGIN = 8;
const CARDS_PER_ROW = 2;
const CARD_WIDTH = (width - CARD_MARGIN * 2 * CARDS_PER_ROW - CARD_MARGIN * 2) / CARDS_PER_ROW;

export default function HomeScreen() {
  const router = useRouter();
  const colorScheme = useColorScheme();
  const bottomSheetRef = useRef<BottomSheet | null>(null);

  const selectedDataSource = useAppStore((state) => state.selectedDataSource);
  const searchQuery = useAppStore((state) => state.searchQuery);
  const items = useAppStore((state) => state.items);
  const isLoading = useAppStore((state) => state.isLoading);
  const isLoadingMore = useAppStore((state) => state.isLoadingMore);
  const currentPage = useAppStore((state) => state.currentPage);
  const hasMorePages = useAppStore((state) => state.hasMorePages);

  const isDark = colorScheme === "dark";

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

  const renderBackdrop = useCallback(
    (props: any) => <BottomSheetBackdrop {...props} disappearsOnIndex={-1} appearsOnIndex={0} />,
    [],
  );

  const renderItem = useCallback(
    ({ item }: { item: ItemUiModel }) => (
      <TouchableOpacity
        className="m-2 rounded-lg"
        style={{
          width: CARD_WIDTH,
          backgroundColor: isDark ? "#333" : "#fff",
          elevation: 3,
          shadowOffset: { width: 0, height: 2 },
          shadowOpacity: 0.1,
          shadowRadius: 4,
        }}
        onPress={() => handleItemPress(item)}
      >
        <Image
          source={{ uri: item.imageUrl }}
          className="w-full rounded-t-lg"
          style={{ height: 150 }}
          contentFit="cover"
        />
        <View className="p-3">
          <Text className="mb-1 text-base font-bold" style={{ color: isDark ? "#fff" : "#000" }} numberOfLines={1}>
            {item.name}
          </Text>
          {item.cardCaption && (
            <Text className="text-sm" style={{ color: isDark ? "#ccc" : "#666" }} numberOfLines={1}>
              {item.cardCaption}
            </Text>
          )}
        </View>
      </TouchableOpacity>
    ),
    [isDark, handleItemPress],
  );

  const renderDataSourceItem = useCallback(
    ({ item }: { item: DataSourceUiModel }) => (
      <TouchableOpacity
        className="my-1 flex-row items-center justify-between rounded-lg p-4"
        style={{ backgroundColor: isDark ? "#444" : "#f0f0f0" }}
        onPress={() => handleDataSourceChange(item)}
      >
        <Text className="text-base" style={{ color: isDark ? "#fff" : "#000" }}>
          {item.name}
        </Text>
        {item.isSelected && (
          <Ionicons name="checkmark" size={24} color={isDark ? "#fff" : "#000"} />
        )}
      </TouchableOpacity>
    ),
    [isDark, handleDataSourceChange],
  );

  const dataSourceOptions = DATA_SOURCES.map((config) => ({
    name: config.name,
    type: config.type,
    isSelected: config.type === selectedDataSource,
  }));

  return (
    <SafeAreaView className="flex-1" style={{ backgroundColor: isDark ? "#000" : "#fff" }}>
      <View className="m-4 flex-row items-center rounded-lg p-3" style={{ backgroundColor: isDark ? "#222" : "#f0f0f0" }}>
        <Ionicons name="search" size={20} color={isDark ? "#ccc" : "#666"} />
        <TextInput
          className="ml-2 flex-1 text-base"
          style={{ color: isDark ? "#fff" : "#000" }}
          placeholder="Search..."
          placeholderTextColor={isDark ? "#ccc" : "#666"}
          value={searchQuery}
          onChangeText={handleSearchChange}
        />
        <TouchableOpacity onPress={openDataSourcePicker}>
          <Ionicons name="filter" size={24} color={isDark ? "#ccc" : "#666"} />
        </TouchableOpacity>
      </View>

      <FlatList
        data={items}
        renderItem={renderItem}
        keyExtractor={(item) => item.id}
        numColumns={CARDS_PER_ROW}
        contentContainerStyle={{ padding: CARD_MARGIN }}
        refreshControl={<RefreshControl refreshing={isLoading} onRefresh={handleRefresh} />}
        onEndReached={handleLoadMore}
        onEndReachedThreshold={0.1}
        ListFooterComponent={
          isLoadingMore ? (
            <View className="items-center p-5">
              <ActivityIndicator size="large" color={isDark ? "#fff" : "#000"} />
            </View>
          ) : null
        }
        ListEmptyComponent={
          !isLoading ? (
            <View className="flex-1 items-center justify-center pt-[100px]">
              <Text className="text-lg text-center" style={{ color: isDark ? "#ccc" : "#666" }}>
                No items found
              </Text>
            </View>
          ) : null
        }
      />

      <BottomSheet
        ref={bottomSheetRef}
        index={-1}
        enableDynamicSizing
        enablePanDownToClose
        enableOverDrag={false}
        backgroundStyle={{ backgroundColor: isDark ? "#333" : "#fff" }}
        backdropComponent={renderBackdrop}
      >
        <BottomSheetView className="flex-1 p-4">
          <Text className="mb-4 text-xl font-bold text-center" style={{ color: isDark ? "#fff" : "#000" }}>
            Select Data Source
          </Text>
          <FlatList
            data={dataSourceOptions}
            renderItem={renderDataSourceItem}
            keyExtractor={(item) => item.name}
          />
        </BottomSheetView>
      </BottomSheet>

      {isLoading && (
        <View
          className="absolute inset-0 items-center justify-center"
          style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}
        >
          <ActivityIndicator size="large" color={isDark ? "#fff" : "#000"} />
        </View>
      )}
    </SafeAreaView>
  );
}

