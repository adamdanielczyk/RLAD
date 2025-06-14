import { articApi } from "@/lib/services/artic/api";
import { fetchArticItems } from "@/lib/services/artic/data";
import { mapArticArtworkToItem } from "@/lib/services/artic/mappers";
import { giphyApi } from "@/lib/services/giphy/api";
import { fetchGiphyItems } from "@/lib/services/giphy/data";
import { mapGiphyGifToItem } from "@/lib/services/giphy/mappers";
import { rickAndMortyApi } from "@/lib/services/rick-and-morty/api";
import { fetchRickAndMortyItems } from "@/lib/services/rick-and-morty/data";
import { mapRickAndMortyCharacterToItem } from "@/lib/services/rick-and-morty/mappers";
import { DataSourceType, ItemUiModel } from "@/lib/types/uiModelTypes";

export interface FetchItemsParams {
  dataSource: DataSourceType;
  page: number;
  query?: string;
  limit?: number;
}

export interface FetchItemsResult {
  items: ItemUiModel[];
  hasMore: boolean;
  totalPages?: number;
}

const listFetchers: Record<DataSourceType, (
  page: number,
  query?: string,
  limit?: number,
) => Promise<FetchItemsResult>> = {
  "rick-and-morty": fetchRickAndMortyItems,
  giphy: fetchGiphyItems,
  artic: fetchArticItems,
};

export const fetchItems = async ({
  dataSource,
  page,
  query,
  limit = 25,
}: FetchItemsParams): Promise<FetchItemsResult> => {
  try {
    return await listFetchers[dataSource](page, query, limit);
  } catch (error) {
    console.error(`Error fetching items for ${dataSource}:`, error);
    throw error;
  }
};

const itemFetchers: Record<DataSourceType, (id: string) => Promise<ItemUiModel>> = {
  "rick-and-morty": async (id) =>
    mapRickAndMortyCharacterToItem(
      await rickAndMortyApi.getCharacter(parseInt(id)),
    ),
  giphy: async (id) => mapGiphyGifToItem(await giphyApi.getGif(id)),
  artic: async (id) =>
    mapArticArtworkToItem(await articApi.getArtwork(parseInt(id))),
};

export const fetchItemById = async (
  dataSource: DataSourceType,
  id: string,
): Promise<ItemUiModel> => {
  try {
    return await itemFetchers[dataSource](id);
  } catch (error) {
    console.error(`Error fetching item ${id} for ${dataSource}:`, error);
    throw error;
  }
};
