import { getArticImageUrl } from "@/lib/services/artic/api";
import { ArticArtwork } from "@/lib/services/artic/types";
import { ItemUiModel } from "@/lib/types/uiModelTypes";

export const mapArticArtworkToItem = (artwork: ArticArtwork): ItemUiModel => {
  const imageUrl = artwork.image_id
    ? getArticImageUrl(artwork.image_id, "medium")
    : "https://via.placeholder.com/400x400?text=No+Image";

  const artistInfo = artwork.artist_display || artwork.artist_title || "";

  return {
    id: artwork.id.toString(),
    imageUrl,
    name: artwork.title,
    cardCaption: artistInfo,
    detailsKeyValues: [
      { key: "Title", value: artwork.title },
      { key: "Artist", value: artwork.artist_title || "" },
      { key: "Artist Display", value: artwork.artist_display || "" },
      { key: "Department", value: artwork.department_title || "" },
      { key: "Place of Origin", value: artwork.place_of_origin || "" },
      { key: "Date", value: artwork.date_display || "" },
    ].filter((item) => item.value && item.value.trim() !== ""),
  };
};
