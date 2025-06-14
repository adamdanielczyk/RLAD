import { GiphyGif } from "@/lib/services/giphy/types";
import { ItemUiModel } from "@/lib/types/uiModelTypes";

export const mapGiphyGifToItem = (gif: GiphyGif): ItemUiModel => {
  const username = gif.username || "Unknown";
  const importDate = gif.import_datetime ? new Date(gif.import_datetime).toLocaleDateString() : "";

  return {
    id: gif.id,
    imageUrl: gif.images.fixed_width.url,
    name: gif.title || "Untitled GIF",
    cardCaption: username !== "Unknown" ? `by ${username}` : undefined,
    detailsKeyValues: [
      { key: "Title", value: gif.title || "Untitled" },
      { key: "Creator", value: username },
      { key: "Import Date", value: importDate },
      { key: "URL", value: gif.url },
    ].filter((item) => item.value && item.value !== "Unknown" && item.value.trim() !== ""),
  };
};
