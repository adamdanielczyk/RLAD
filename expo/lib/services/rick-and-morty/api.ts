import {
  RickAndMortyCharacter,
  RickAndMortyCharactersResponse,
} from "@/lib/services/rick-and-morty/types";

const RICK_AND_MORTY_BASE_URL = "https://rickandmortyapi.com/api";

// Rick and Morty API
export const rickAndMortyApi = {
  getCharacters: async (
    page: number = 1,
    name?: string,
  ): Promise<RickAndMortyCharactersResponse> => {
    const params = new URLSearchParams({
      page: page.toString(),
    });

    if (name) {
      params.append("name", name);
    }

    const response = await fetch(`${RICK_AND_MORTY_BASE_URL}/character?${params}`);

    if (!response.ok) {
      throw new Error(`Rick and Morty API error: ${response.status}`);
    }

    return response.json();
  },

  getCharacter: async (id: number): Promise<RickAndMortyCharacter> => {
    const response = await fetch(`${RICK_AND_MORTY_BASE_URL}/character/${id}`);

    if (!response.ok) {
      throw new Error(`Rick and Morty API error: ${response.status}`);
    }

    return response.json();
  },
};
