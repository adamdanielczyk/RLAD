import {
  RickAndMortyCharacter,
  RickAndMortyCharactersResponse,
} from "@/lib/services/rick-and-morty/types";
import { fetchJson } from "@/lib/services/apiClient";

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

    return fetchJson<RickAndMortyCharactersResponse>(
      `${RICK_AND_MORTY_BASE_URL}/character?${params}`,
    );
  },

  getCharacter: async (id: number): Promise<RickAndMortyCharacter> => {
    return fetchJson<RickAndMortyCharacter>(
      `${RICK_AND_MORTY_BASE_URL}/character/${id}`,
    );
  },
};
