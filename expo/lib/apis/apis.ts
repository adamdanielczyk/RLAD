import { fetchArticItemById, fetchArticItems } from "@/lib/apis/artic/data";
import { fetchGiphyItemById, fetchGiphyItems } from "@/lib/apis/giphy/data";
import { fetchRickAndMortyItemById, fetchRickAndMortyItems } from "@/lib/apis/rick-and-morty/data";
import { DataSourceType, ItemUiModel } from "@/lib/ui/uiModelTypes";

export interface FetchItemsParams {
  dataSource: DataSourceType;
  offset?: number;
  query?: string;
}

export interface FetchItemsResult {
  items: ItemUiModel[];
  hasMorePages: boolean;
  nextOffset: number;
}

export const fetchItems = async ({
  dataSource,
  offset,
  query,
}: FetchItemsParams): Promise<FetchItemsResult> => {
  try {
    switch (dataSource) {
      case "rick-and-morty":
        return await fetchRickAndMortyItems(offset, query);

      case "giphy":
        return await fetchGiphyItems(offset, query);

      case "artic":
        return await fetchArticItems(offset, query);
    }
  } catch (error) {
    console.error(`Error fetching items for ${dataSource}:`, error);
    throw error;
  }
};

export const fetchItemById = async (
  id: string,
  dataSource: DataSourceType,
): Promise<ItemUiModel> => {
  try {
    switch (dataSource) {
      case "rick-and-morty":
        return await fetchRickAndMortyItemById(id);

      case "giphy":
        return await fetchGiphyItemById(id);

      case "artic":
        return await fetchArticItemById(id);
    }
  } catch (error) {
    console.error(`Error fetching item ${id} for ${dataSource}:`, error);
    throw error;
  }
};
