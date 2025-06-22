import { fetchArticItemById, fetchArticItems } from "@/lib/services/artic/data";
import { fetchGiphyItemById, fetchGiphyItems } from "@/lib/services/giphy/data";
import {
  fetchRickAndMortyItemById,
  fetchRickAndMortyItems,
} from "@/lib/services/rick-and-morty/data";
import { useAppStore } from "@/lib/store/appStore";
import { ItemUiModel } from "@/lib/types/uiModelTypes";

export interface FetchItemsParams {
  offset?: number;
  query?: string;
}

export interface FetchItemsResult {
  items: ItemUiModel[];
  hasMorePages: boolean;
  nextOffset: number;
}

export const fetchItems = async ({
  offset,
  query,
}: FetchItemsParams): Promise<FetchItemsResult> => {
  const { selectedDataSource } = useAppStore.getState();
  try {
    switch (selectedDataSource) {
      case "rick-and-morty":
        return await fetchRickAndMortyItems(offset, query);

      case "giphy":
        return await fetchGiphyItems(offset, query);

      case "artic":
        return await fetchArticItems(offset, query);
    }
  } catch (error) {
    console.error(`Error fetching items for ${selectedDataSource}:`, error);
    throw error;
  }
};

export const fetchItemById = async (id: string): Promise<ItemUiModel> => {
  const { selectedDataSource } = useAppStore.getState();
  try {
    switch (selectedDataSource) {
      case "rick-and-morty":
        return await fetchRickAndMortyItemById(id);

      case "giphy":
        return await fetchGiphyItemById(id);

      case "artic":
        return await fetchArticItemById(id);
    }
  } catch (error) {
    console.error(`Error fetching item ${id} for ${selectedDataSource}:`, error);
    throw error;
  }
};
