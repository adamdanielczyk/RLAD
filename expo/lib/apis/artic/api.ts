import { fetchJson } from "@/lib/apis/apiClient";
import { ArticArtwork, ArticResponse } from "@/lib/apis/artic/types";

const ARTIC_BASE_URL = "https://api.artic.edu/api/v1";
const FIELDS =
  "fields=id,title,image_id,artist_title,artist_display,department_title,place_of_origin,date_display,thumbnail";
const ELASTICSEARCH_QUERY_ONLY_WITH_IMAGE_IDS = "query[exists][field]=image_id";
const LIMIT = 20;

export const articApi = {
  getArtworks: async (from: number, query?: string): Promise<ArticResponse> => {
    const params = new URLSearchParams({
      from: from.toString(),
      size: LIMIT.toString(),
    });

    if (query) {
      params.append("q", query);
    }

    return fetchJson<ArticResponse>(
      `${ARTIC_BASE_URL}/artworks/search?${params}&${FIELDS}&${ELASTICSEARCH_QUERY_ONLY_WITH_IMAGE_IDS}`,
    );
  },

  getArtwork: async (id: string): Promise<ArticArtwork> => {
    const result = await fetchJson<{ data: ArticArtwork }>(
      `${ARTIC_BASE_URL}/artworks/${id}?${FIELDS}`,
    );
    return result.data;
  },
};
