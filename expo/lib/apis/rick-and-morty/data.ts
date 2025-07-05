import { FetchItemsResult } from "@/lib/apis/apis";
import { rickAndMortyApi } from "@/lib/apis/rick-and-morty/api";
import { RickAndMortyCharacter } from "@/lib/apis/rick-and-morty/types";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";

const INITIAL_OFFSET = 1;

export const fetchRickAndMortyItems = async (
  offset?: number,
  query?: string,
): Promise<FetchItemsResult> => {
  const resolvedOffset = offset ?? INITIAL_OFFSET;
  let response;
  try {
    response = await rickAndMortyApi.getCharacters(resolvedOffset, query);
  } catch (error) {
    if (error instanceof Response && error.status === 404) {
      return {
        items: [],
        hasMorePages: false,
        nextOffset: resolvedOffset,
      };
    }
    throw error;
  }
  const items = response.results.map(mapRickAndMortyCharacterToItem);

  const hasMorePages = !!response.info.next;
  const nextOffset = resolvedOffset + 1;

  return {
    items,
    hasMorePages,
    nextOffset,
  };
};

export const fetchRickAndMortyItemById = async (id: string): Promise<ItemUiModel> => {
  const response = await rickAndMortyApi.getCharacter(parseInt(id));
  return mapRickAndMortyCharacterToItem(response);
};

const mapRickAndMortyCharacterToItem = (character: RickAndMortyCharacter): ItemUiModel => {
  return {
    id: character.id.toString(),
    imageUrl: character.image,
    name: character.name,
    cardCaption: `${character.species}`,
    dataSource: "rick-and-morty",
    detailsKeyValues: [
      { key: "Name", value: character.name },
      { key: "Status", value: character.status },
      { key: "Species", value: character.species },
      { key: "Type", value: character.type || "Unknown" },
      { key: "Gender", value: character.gender },
      { key: "Origin", value: character.origin.name },
      { key: "Location", value: character.location.name },
      { key: "Created", value: new Date(character.created).toLocaleDateString() },
    ].filter((item) => item.value && item.value !== "Unknown" && item.value.trim() !== ""),
  };
};
