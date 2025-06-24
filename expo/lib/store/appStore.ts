import { fetchItemById, fetchItems } from "@/lib/services/dataService";
import { DataSourceType, ItemUiModel } from "@/lib/types/uiModelTypes";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { create } from "zustand";

const DATA_SOURCE_STORAGE_KEY = "selected_data_source";
const DEFAULT_DATA_SOURCE: DataSourceType = "giphy";

interface AppState {
  initialize: () => Promise<void>;

  selectedDataSource: DataSourceType;
  onDataSourceSelected: (dataSource: DataSourceType) => Promise<void>;

  searchQuery: string;
  isSearchFocused: boolean;
  onSearchFocused: (isFocused: boolean) => void;
  onSearchQueryChanged: (query: string) => Promise<void>;
  onClearButtonClicked: () => Promise<void>;
  onFilterButtonClicked: () => void;

  isBottomSheetOpen: boolean;
  onBottomSheetClosed: () => void;

  items: ItemUiModel[];
  isLoading: boolean;
  onPullToRefresh: () => Promise<void>;
  onLoadMoreItems: () => Promise<void>;

  detailedItem: ItemUiModel | null;
  isDetailedItemLoading: boolean;
  loadItemById: (id: string) => Promise<void>;
  clearDetailedItem: () => void;
}

export const useAppStore = create<AppState>((set, get) => {
  let nextOffset: number | undefined = undefined;
  let hasMorePages = true;

  const loadItems = async (options?: { forceRefresh?: boolean }) => {
    const { isLoading, searchQuery, selectedDataSource } = get();
    const isForceRefresh = options?.forceRefresh;

    if (!isForceRefresh) {
      if (isLoading) return;
      if (!hasMorePages) return;
    }

    set({ isLoading: true });

    if (isForceRefresh) {
      nextOffset = undefined;
      hasMorePages = true;
      set({ items: [] });
    }

    try {
      const result = await fetchItems({
        dataSource: selectedDataSource,
        offset: nextOffset,
        query: searchQuery,
      });

      nextOffset = result.nextOffset;
      hasMorePages = result.hasMorePages;

      const { items: currentItems } = get();
      set({ items: [...currentItems, ...result.items] });
    } catch (error) {
      console.error(`Failed to load items:`, error);
    } finally {
      set({ isLoading: false });
    }
  };

  return {
    selectedDataSource: DEFAULT_DATA_SOURCE,
    searchQuery: "",
    isSearchFocused: false,
    isBottomSheetOpen: false,
    items: [],
    isLoading: false,

    detailedItem: null,
    isDetailedItemLoading: false,

    initialize: async () => {
      try {
        const savedDataSource = await AsyncStorage.getItem(DATA_SOURCE_STORAGE_KEY);
        set({
          selectedDataSource: savedDataSource
            ? (savedDataSource as DataSourceType)
            : DEFAULT_DATA_SOURCE,
        });
      } catch (error) {
        console.error("Failed to load selected data source:", error);
        set({ selectedDataSource: DEFAULT_DATA_SOURCE });
      } finally {
        await loadItems({ forceRefresh: true });
      }
    },

    onDataSourceSelected: async (newDataSource: DataSourceType) => {
      const { selectedDataSource: currentDataSource } = get();
      set({ isBottomSheetOpen: false });
      if (currentDataSource === newDataSource) {
        return;
      }

      try {
        await AsyncStorage.setItem(DATA_SOURCE_STORAGE_KEY, newDataSource);
        set({ selectedDataSource: newDataSource });
      } catch (error) {
        console.error("Failed to save selected data source:", error);
      }

      set({ searchQuery: "" });
      await loadItems({ forceRefresh: true });
    },

    onSearchFocused: (isFocused: boolean) => {
      set({ isSearchFocused: isFocused });
    },

    onSearchQueryChanged: async (query: string) => {
      set({ searchQuery: query });
      await loadItems({ forceRefresh: true });
    },

    onClearButtonClicked: async () => {
      set({ searchQuery: "", isSearchFocused: false });
      await loadItems({ forceRefresh: true });
    },

    onFilterButtonClicked: () => {
      set({
        isBottomSheetOpen: true,
        isSearchFocused: false,
      });
    },

    onBottomSheetClosed: () => {
      set({
        isBottomSheetOpen: false,
      });
    },

    onPullToRefresh: async () => {
      await loadItems({ forceRefresh: true });
    },

    onLoadMoreItems: async () => {
      await loadItems();
    },

    loadItemById: async (id: string) => {
      const { selectedDataSource } = get();
      set({ isDetailedItemLoading: true });
      try {
        const itemData = await fetchItemById(id, selectedDataSource);
        set({ detailedItem: itemData });
      } catch (error) {
        console.error(`Failed to load item by id: ${id}`, error);
      } finally {
        set({ isDetailedItemLoading: false });
      }
    },

    clearDetailedItem: () => {
      set({ detailedItem: null });
    },
  };
});
