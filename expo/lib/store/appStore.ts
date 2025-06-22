import { DataSourceType, ItemUiModel } from "@/lib/types/uiModelTypes";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { create } from "zustand";

interface AppState {
  // Data source management
  selectedDataSource: DataSourceType;
  setSelectedDataSource: (dataSource: DataSourceType) => Promise<void>;

  // Search management
  searchQuery: string;
  setSearchQuery: (query: string) => void;

  // Items management
  items: ItemUiModel[];
  addItems: (items: ItemUiModel[]) => void;

  // Loading and pagination
  isLoading: boolean;
  setIsLoading: (loading: boolean) => void;
  nextOffset?: number;
  setNextOffset: (offset: number) => void;
  hasMorePages: boolean;
  setHasMorePages: (hasMore: boolean) => void;

  // Initialize store
  isInitialized: boolean;
  initialize: () => Promise<void>;
}

const DATA_SOURCE_STORAGE_KEY = "selected_data_source";
const DEFAULT_DATA_SOURCE: DataSourceType = "giphy";

export const useAppStore = create<AppState>((set, get) => ({
  selectedDataSource: DEFAULT_DATA_SOURCE,
  searchQuery: "",
  items: [],
  isLoading: false,
  nextOffset: undefined,
  hasMorePages: true,
  isInitialized: false,

  // Data source management
  setSelectedDataSource: async (dataSource: DataSourceType) => {
    try {
      await AsyncStorage.setItem(DATA_SOURCE_STORAGE_KEY, dataSource);
      set({
        selectedDataSource: dataSource,
        searchQuery: "",
        items: [],
        nextOffset: undefined,
        hasMorePages: true,
      });
    } catch (error) {
      console.error("Failed to save selected data source:", error);
    }
  },

  // Search management
  setSearchQuery: (query: string) => {
    set({
      searchQuery: query,
      items: [],
      nextOffset: undefined,
      hasMorePages: true,
    });
  },

  // Items management
  addItems: (items: ItemUiModel[]) => {
    const { items: currentItems } = get();
    set({ items: [...currentItems, ...items] });
  },

  // Loading and pagination
  setIsLoading: (loading: boolean) => {
    set({ isLoading: loading });
  },

  setNextOffset: (offset: number) => {
    set({ nextOffset: offset });
  },

  setHasMorePages: (hasMore: boolean) => {
    set({ hasMorePages: hasMore });
  },

  initialize: async () => {
    try {
      const savedDataSource = await AsyncStorage.getItem(DATA_SOURCE_STORAGE_KEY);
      if (savedDataSource) {
        set({
          selectedDataSource: savedDataSource as DataSourceType,
          isInitialized: true,
        });
      } else {
        set({
          selectedDataSource: DEFAULT_DATA_SOURCE,
          isInitialized: true,
        });
      }
    } catch (error) {
      console.error("Failed to load selected data source:", error);
      set({
        selectedDataSource: DEFAULT_DATA_SOURCE,
        isInitialized: true,
      });
    }
  },
}));
