import { fetchJson } from "@/lib/services/apiClient";
import { GiphyGif, GiphyResponse } from "@/lib/services/giphy/types";

const GIPHY_BASE_URL = "https://api.giphy.com/v1/gifs";
const BUNDLE = "clips_grid_picker";
const LIMIT = 20;

export const giphyApi = {
  getGifs: async (offset: number, query?: string): Promise<GiphyResponse> => {
    const endpoint = query ? "search" : "trending";
    const params = new URLSearchParams({
      api_key: process.env.EXPO_PUBLIC_GIPHY_API_KEY!,
      limit: LIMIT.toString(),
      offset: offset.toString(),
      bundle: BUNDLE,
    });

    if (query) {
      params.append("q", query);
    }

    return fetchJson<GiphyResponse>(`${GIPHY_BASE_URL}/${endpoint}?${params}`);
  },

  getGif: async (id: string) => {
    const result = await fetchJson<{ data: GiphyGif }>(
      `${GIPHY_BASE_URL}/${id}?api_key=${process.env.EXPO_PUBLIC_GIPHY_API_KEY}`,
    );
    return result.data;
  },
};
