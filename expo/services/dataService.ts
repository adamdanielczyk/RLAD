import { articApi } from "@/services/artic/api";
import { fetchArticItems } from "@/services/artic/data";
import { mapArticArtworkToItem } from "@/services/artic/mappers";
import { giphyApi } from "@/services/giphy/api";
import { fetchGiphyItems } from "@/services/giphy/data";
import { mapGiphyGifToItem } from "@/services/giphy/mappers";
import { rickAndMortyApi } from "@/services/rick-and-morty/api";
import { fetchRickAndMortyItems } from "@/services/rick-and-morty/data";
import { mapRickAndMortyCharacterToItem } from "@/services/rick-and-morty/mappers";
import { DataSourceType, ItemUiModel } from "@/types/uiModelTypes";

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

export const fetchItems = async ({
  dataSource,
  page,
  query,
  limit = 25,
}: FetchItemsParams): Promise<FetchItemsResult> => {
  try {
    switch (dataSource) {
      case "rick-and-morty":
        return await fetchRickAndMortyItems(page, query);

      case "giphy":
        return await fetchGiphyItems(page, query, limit);

      case "artic":
        return await fetchArticItems(page, query, limit);
    }
  } catch (error) {
    console.error(`Error fetching items for ${dataSource}:`, error);
    throw error;
  }
};

export const fetchItemById = async (
  dataSource: DataSourceType,
  id: string,
): Promise<ItemUiModel> => {
  try {
    switch (dataSource) {
      case "rick-and-morty":
        const character = await rickAndMortyApi.getCharacter(parseInt(id));
        return mapRickAndMortyCharacterToItem(character);

      case "giphy":
        const gif = await giphyApi.getGif(id);
        return mapGiphyGifToItem(gif);

      case "artic":
        const artwork = await articApi.getArtwork(parseInt(id));
        return mapArticArtworkToItem(artwork);
    }
  } catch (error) {
    console.error(`Error fetching item ${id} for ${dataSource}:`, error);
    throw error;
  }
};
