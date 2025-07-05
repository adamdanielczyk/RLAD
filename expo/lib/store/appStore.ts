import { DataSourceType, ItemUiModel } from "@/lib/ui/uiModelTypes";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { create } from "zustand";

const DATA_SOURCE_STORAGE_KEY = "selected_data_source";
const FAVORITES_STORAGE_KEY = "favorites";
const DEFAULT_DATA_SOURCE: DataSourceType = "giphy";

export type ViewMode = "grid" | "feed";

interface AppState {
  initialize: () => Promise<void>;

  selectedDataSource: DataSourceType;
  onDataSourceSelected: (dataSource: DataSourceType) => Promise<void>;

  searchQuery: string;
  isSearchFocused: boolean;
  onSearchFocused: (isFocused: boolean) => void;
  onSearchQueryChanged: (query: string) => void;
  onClearButtonClicked: () => void;
  onDataSourceButtonClicked: () => void;

  viewMode: ViewMode;
  toggleViewMode: () => void;

  isBottomSheetOpen: boolean;
  onBottomSheetClosed: () => void;

  favorites: ItemUiModel[];
  isFavorite: (itemId: string) => boolean;
  toggleFavorite: (item: ItemUiModel) => Promise<void>;
}

export const useAppStore = create<AppState>((set, get) => {
  return {
    selectedDataSource: DEFAULT_DATA_SOURCE,
    searchQuery: "",
    isSearchFocused: false,
    viewMode: "grid" as ViewMode,
    isBottomSheetOpen: false,
    favorites: [],

    initialize: async () => {
      try {
        const [savedDataSource, savedFavorites] = await Promise.all([
          AsyncStorage.getItem(DATA_SOURCE_STORAGE_KEY),
          AsyncStorage.getItem(FAVORITES_STORAGE_KEY),
        ]);

        set({
          selectedDataSource: savedDataSource
            ? (savedDataSource as DataSourceType)
            : DEFAULT_DATA_SOURCE,
          favorites: savedFavorites ? JSON.parse(savedFavorites) : [],
        });
      } catch (error) {
        console.error("Failed to load app data:", error);
        set({
          selectedDataSource: DEFAULT_DATA_SOURCE,
          favorites: [],
        });
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

    onDataSourceButtonClicked: () => {
      set({
        isBottomSheetOpen: true,
        isSearchFocused: false,
      });
    },

    toggleViewMode: () => {
      const { viewMode } = get();
      set({ viewMode: viewMode === "grid" ? "feed" : "grid" });
    },

    onBottomSheetClosed: () => {
      set({
        isBottomSheetOpen: false,
      });
    },

    isFavorite: (itemId: string) => {
      const { favorites } = get();
      return favorites.some((item) => item.id === itemId);
    },

    toggleFavorite: async (item: ItemUiModel) => {
      const { favorites } = get();
      const isFavorited = favorites.some((fav) => fav.id === item.id);

      let newFavorites: ItemUiModel[];
      if (isFavorited) {
        newFavorites = favorites.filter((fav) => fav.id !== item.id);
      } else {
        newFavorites = [...favorites, item];
      }

      try {
        await AsyncStorage.setItem(FAVORITES_STORAGE_KEY, JSON.stringify(newFavorites));
        set({ favorites: newFavorites });
      } catch (error) {
        console.error("Failed to save favorites:", error);
      }
    },
  };
});
