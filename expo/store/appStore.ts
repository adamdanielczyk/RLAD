import AsyncStorage from "@react-native-async-storage/async-storage";
import { create } from "zustand";
import { DataSourceType, ItemUiModel } from "../types/uiModelTypes";

interface AppState {
  // Data source management
  selectedDataSource: DataSourceType;
  setSelectedDataSource: (dataSource: DataSourceType) => Promise<void>;

  // Search management
  searchQuery: string;
  setSearchQuery: (query: string) => void;

  // Items management
  items: ItemUiModel[];
  setItems: (items: ItemUiModel[]) => void;
  addItems: (items: ItemUiModel[]) => void;
  clearItems: () => void;

  // Loading and pagination
  isLoading: boolean;
  setIsLoading: (loading: boolean) => void;
  isLoadingMore: boolean;
  setIsLoadingMore: (loading: boolean) => void;
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
  isLoadingMore: false,
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
  setItems: (items: ItemUiModel[]) => {
    set({ items });
  },

  addItems: (newItems: ItemUiModel[]) => {
    const { items } = get();
    set({ items: [...items, ...newItems] });
  },

  clearItems: () => {
    set({ items: [], currentPage: 1, hasMorePages: true });
  },

  // Loading and pagination
  setIsLoading: (loading: boolean) => {
    set({ isLoading: loading });
  },

  setIsLoadingMore: (loading: boolean) => {
    set({ isLoadingMore: loading });
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
      if (savedDataSource && ["rick-and-morty", "giphy", "artic"].includes(savedDataSource)) {
        set({ selectedDataSource: savedDataSource as DataSourceType });
      }
    } catch (error) {
      console.error("Failed to load selected data source:", error);
    }
  },
}));
