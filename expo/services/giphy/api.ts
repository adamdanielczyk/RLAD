import { GiphyResponse } from "@/services/giphy/types";

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

    const response = await fetch(`${GIPHY_BASE_URL}/${endpoint}?${params}`);

    if (!response.ok) {
      throw new Error(`Giphy API error: ${response.status}`);
    }

    return response.json();
  },

  getGif: async (id: string) => {
    const response = await fetch(
      `${GIPHY_BASE_URL}/${id}?api_key=${process.env.EXPO_PUBLIC_GIPHY_API_KEY}`,
    );

    if (!response.ok) {
      throw new Error(`Giphy API error: ${response.status}`);
    }

    const result = await response.json();
    return result.data;
  },
};
