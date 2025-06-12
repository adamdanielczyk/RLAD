import { articApi } from "@/services/artic/api";
import { mapArticArtworkToItem } from "@/services/artic/mappers";
import { FetchItemsResult } from "@/services/dataService";

export const fetchArticItems = async (
  page: number,
  query?: string,
  limit: number = 25,
): Promise<FetchItemsResult> => {
  const response = await articApi.getArtworks(page, query, limit);
  const items = response.data.map(mapArticArtworkToItem);

  return {
    items,
    hasMore: page < response.pagination.total_pages,
    totalPages: response.pagination.total_pages,
  };
};
