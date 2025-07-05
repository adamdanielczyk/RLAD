import { FetchItemsResult } from "@/lib/apis/apis";
import { giphyApi } from "@/lib/apis/giphy/api";
import { GiphyGif } from "@/lib/apis/giphy/types";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";

const INITIAL_OFFSET = 0;

export const fetchGiphyItems = async (
  offset?: number,
  query?: string,
): Promise<FetchItemsResult> => {
  const resolvedOffset = offset ?? INITIAL_OFFSET;
  const response = await giphyApi.getGifs(resolvedOffset, query);
  const items = response.data.map(mapGiphyGifToItem);

  const hasMorePages = items.length !== 0;
  const nextOffset = response.pagination.offset + response.pagination.count;

  return {
    items,
    hasMorePages,
    nextOffset,
  };
};

export const fetchGiphyItemById = async (id: string): Promise<ItemUiModel> => {
  const response = await giphyApi.getGif(id);
  return mapGiphyGifToItem(response);
};

const mapGiphyGifToItem = (gif: GiphyGif): ItemUiModel => {
  const username = gif.username || "Unknown";
  const importDate = gif.import_datetime ? new Date(gif.import_datetime).toLocaleDateString() : "";
  const trendingDate = gif.trending_datetime
    ? new Date(gif.trending_datetime).toLocaleDateString()
    : "";

  return {
    id: gif.id,
    imageUrl: gif.images.fixed_width.url,
    name: gif.title || "Untitled GIF",
    cardCaption: username !== "Unknown" ? `by ${username}` : undefined,
    dataSource: "giphy",
    detailsKeyValues: [
      { key: "Title", value: gif.title || "Untitled" },
      { key: "Creator", value: username },
      { key: "Import Date", value: importDate },
      { key: "Trending Date", value: trendingDate },
      { key: "URL", value: gif.url },
    ].filter((item) => item.value && item.value !== "Unknown" && item.value.trim() !== ""),
  };
};
