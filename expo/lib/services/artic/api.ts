import { ArticArtwork, ArticResponse } from "@/lib/services/artic/types";
import { fetchJson } from "@/lib/services/apiClient";

const ARTIC_BASE_URL = "https://api.artic.edu/api/v1";

export const articApi = {
  getArtworks: async (
    page: number = 1,
    query?: string,
    limit: number = 25,
  ): Promise<ArticResponse> => {
    const params = new URLSearchParams({
      page: page.toString(),
      limit: limit.toString(),
      fields:
        "id,title,image_id,thumbnail,artist_title,artist_display,place_of_origin,department_title,date_display",
    });

    if (query) {
      params.append("q", query);
    }

    return fetchJson<ArticResponse>(
      `${ARTIC_BASE_URL}/artworks/search?${params}`,
    );
  },

  getArtwork: async (id: number): Promise<ArticArtwork> => {
    const params = new URLSearchParams({
      fields:
        "id,title,image_id,thumbnail,artist_title,artist_display,place_of_origin,department_title,date_display",
    });

    const result = await fetchJson<{ data: ArticArtwork }>(
      `${ARTIC_BASE_URL}/artworks/${id}?${params}`,
    );
    return result.data;
  },
};

export const getArticImageUrl = (
  imageId: string,
  size: "small" | "medium" | "large" = "medium",
): string => {
  const sizeMap = {
    small: "400",
    medium: "843",
    large: "1686",
  };

  return `https://www.artic.edu/iiif/2/${imageId}/full/${sizeMap[size]},/0/default.jpg`;
};
