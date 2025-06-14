import { DATA_SOURCES } from "@/lib/services/dataSources";
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
  clearItems: () => void;

  // Loading and pagination
  isLoading: boolean;
  setIsLoading: (loading: boolean) => void;
  currentPage: number;
  setCurrentPage: (page: number) => void;
  hasMorePages: boolean;
  setHasMorePages: (hasMore: boolean) => void;

  // Initialize store
  initialize: () => Promise<void>;
}

const STORAGE_KEY = "selected_data_source";
const DEFAULT_DATA_SOURCE: DataSourceType = "giphy";

export const useAppStore = create<AppState>((set, get) => ({
  selectedDataSource: DEFAULT_DATA_SOURCE,
  searchQuery: "",
  items: [],
  isLoading: false,
  currentPage: 1,
  hasMorePages: true,

  // Data source management
  setSelectedDataSource: async (dataSource: DataSourceType) => {
    try {
      await AsyncStorage.setItem(STORAGE_KEY, dataSource);
      set({
        selectedDataSource: dataSource,
        items: [], // Clear items when switching data source
        currentPage: 1,
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
      items: [], // Clear items when search changes
      currentPage: 1,
      hasMorePages: true,
    });
  },

  // Items management
  addItems: (items: ItemUiModel[]) => {
    const { items: currentItems } = get();
    set({ items: [...currentItems, ...items] });
  },

  clearItems: () => {
    set({ items: [], currentPage: 1, hasMorePages: true });
  },

  // Loading and pagination
  setIsLoading: (loading: boolean) => {
    set({ isLoading: loading });
  },

  setCurrentPage: (page: number) => {
    set({ currentPage: page });
  },

  setHasMorePages: (hasMore: boolean) => {
    set({ hasMorePages: hasMore });
  },

  initialize: async () => {
    try {
      const savedDataSource = await AsyncStorage.getItem(STORAGE_KEY);
      if (savedDataSource && DATA_SOURCES.some((ds) => ds.type === savedDataSource)) {
        set({ selectedDataSource: savedDataSource as DataSourceType });
      }
    } catch (error) {
      console.error("Failed to load selected data source:", error);
      set({ selectedDataSource: DEFAULT_DATA_SOURCE });
    }
  },
}));
