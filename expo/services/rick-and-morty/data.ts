import { FetchItemsResult } from "@/services/dataService";
import { rickAndMortyApi } from "@/services/rick-and-morty/api";
import { mapRickAndMortyCharacterToItem } from "@/services/rick-and-morty/mappers";

export const fetchRickAndMortyItems = async (
  page: number,
  query?: string,
): Promise<FetchItemsResult> => {
  const response = await rickAndMortyApi.getCharacters(page, query);
  const items = response.results.map(mapRickAndMortyCharacterToItem);

  return {
    items,
    hasMore: !!response.info.next,
    totalPages: response.info.pages,
  };
};
