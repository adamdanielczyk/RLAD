import { articApi } from "@/lib/apis/artic/api";
import { ArticArtwork } from "@/lib/apis/artic/types";
import { FetchItemsResult } from "@/lib/apis/dataService";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";

const INITIAL_OFFSET = 0;

export const fetchArticItems = async (
  offset?: number,
  query?: string,
): Promise<FetchItemsResult> => {
  const resolvedOffset = offset ?? INITIAL_OFFSET;
  const response = await articApi.getArtworks(resolvedOffset, query);
  const items = response.data.map(mapArticArtworkToItem);

  const hasMorePages = items.length !== 0;
  const nextOffset = response.pagination.offset + response.pagination.limit + 1;

  return {
    items,
    hasMorePages,
    nextOffset,
  };
};

export const fetchArticItemById = async (id: string): Promise<ItemUiModel> => {
  const response = await articApi.getArtwork(id);
  return mapArticArtworkToItem(response);
};

const mapArticArtworkToItem = (artwork: ArticArtwork): ItemUiModel => {
  return {
    id: artwork.id.toString(),
    imageUrl: `https://artic.edu/iiif/2/${artwork.image_id}/full/200,/0/default.jpg`,
    name: artwork.title,
    cardCaption: artwork.artist_display || artwork.date_display,
    detailsKeyValues: [
      { key: "Title", value: artwork.title },
      { key: "Artist", value: artwork.artist_title || "" },
      { key: "Artist Display", value: artwork.artist_display || "" },
      { key: "Department", value: artwork.department_title || "" },
      { key: "Place of Origin", value: artwork.place_of_origin || "" },
      { key: "Date", value: artwork.date_display || "" },
      { key: "Thumbnail", value: artwork.thumbnail?.alt_text || "" },
    ].filter((item) => item.value && item.value.trim() !== ""),
  };
};
