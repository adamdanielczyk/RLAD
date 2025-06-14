import { FetchItemsResult } from "@/lib/services/dataService";
import { giphyApi } from "@/lib/services/giphy/api";
import { mapGiphyGifToItem } from "@/lib/services/giphy/mappers";

export const fetchGiphyItems = async (
  page: number,
  query?: string,
  limit: number = 25,
): Promise<FetchItemsResult> => {
  const offset = (page - 1) * limit;
  const response = await giphyApi.searchGifs(query, limit, offset);
  const items = response.data.map(mapGiphyGifToItem);

  const totalItems = response.pagination.total_count;
  const currentOffset = response.pagination.offset;
  const currentCount = response.pagination.count;

  return {
    items,
    hasMore: currentOffset + currentCount < totalItems,
  };
};
