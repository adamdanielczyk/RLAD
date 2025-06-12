export type DataSourceType = "rick-and-morty" | "giphy" | "artic";

export interface ItemUiModel {
  id: string;
  imageUrl: string;
  name: string;
  cardCaption?: string;
  detailsKeyValues: Array<{ key: string; value: string }>;
}

export interface DataSourceUiModel {
  name: string;
  type: DataSourceType;
  isSelected: boolean;
}
