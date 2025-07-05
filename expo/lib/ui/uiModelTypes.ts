export type DataSourceType = "rick-and-morty" | "giphy" | "artic";

export interface ItemUiModel {
  id: string;
  imageUrl: string;
  highResImageUrl: string;
  name: string;
  cardCaption?: string;
  detailsKeyValues: Array<{ key: string; value: string }>;
  dataSource: DataSourceType;
}
