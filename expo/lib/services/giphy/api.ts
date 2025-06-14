import { GiphyResponse, GiphyGif } from "@/lib/services/giphy/types";
import { fetchJson } from "@/lib/services/apiClient";

const GIPHY_BASE_URL = "https://api.giphy.com/v1/gifs";

export const giphyApi = {
  searchGifs: async (
    query: string = "",
    limit: number = 25,
    offset: number = 0,
  ): Promise<GiphyResponse> => {
    const endpoint = query ? "search" : "trending";
    const params = new URLSearchParams({
      api_key: process.env.EXPO_PUBLIC_GIPHY_API_KEY!,
      limit: limit.toString(),
      offset: offset.toString(),
    });

    if (query) {
      params.append("q", query);
    }

    return fetchJson<GiphyResponse>(
      `${GIPHY_BASE_URL}/${endpoint}?${params}`,
    );
  },

  getGif: async (id: string) => {
    const result = await fetchJson<{ data: GiphyGif }>(
      `${GIPHY_BASE_URL}/${id}?api_key=${process.env.EXPO_PUBLIC_GIPHY_API_KEY}`,
    );
    return result.data;
  },
};
