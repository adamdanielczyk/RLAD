import { DataSourceType } from "@/lib/ui/uiModelTypes";
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
  onSearchQueryChanged: (query: string) => void;
  onClearButtonClicked: () => void;
  onFilterButtonClicked: () => void;

  isBottomSheetOpen: boolean;
  onBottomSheetClosed: () => void;
}

export const useAppStore = create<AppState>((set, get) => {
  return {
    selectedDataSource: DEFAULT_DATA_SOURCE,
    searchQuery: "",
    isSearchFocused: false,
    isBottomSheetOpen: false,

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
      }
    },

    onDataSourceSelected: async (newDataSource: DataSourceType) => {
      const { selectedDataSource: currentDataSource } = get();
      set({ isBottomSheetOpen: false });

      if (currentDataSource === newDataSource) {
        return;
      }

      set({ searchQuery: "" });

      try {
        await AsyncStorage.setItem(DATA_SOURCE_STORAGE_KEY, newDataSource);
        set({ selectedDataSource: newDataSource });
      } catch (error) {
        console.error("Failed to save selected data source:", error);
      }
    },

    onSearchFocused: (isFocused: boolean) => {
      set({ isSearchFocused: isFocused });
    },

    onSearchQueryChanged: (query: string) => {
      set({ searchQuery: query });
    },

    onClearButtonClicked: () => {
      set({ searchQuery: "", isSearchFocused: false });
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
  };
});
