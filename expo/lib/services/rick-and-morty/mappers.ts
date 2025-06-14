import { RickAndMortyCharacter } from "@/lib/services/rick-and-morty/types";
import { ItemUiModel } from "@/lib/types/uiModelTypes";

export const mapRickAndMortyCharacterToItem = (character: RickAndMortyCharacter): ItemUiModel => {
  return {
    id: character.id.toString(),
    imageUrl: character.image,
    name: character.name,
    cardCaption: `${character.species}`,
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
