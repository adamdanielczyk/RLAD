export interface ArticResponse {
  data: ArticArtwork[];
  pagination: {
    limit: number;
    offset: number;
  };
}

export interface ArticArtwork {
  id: number;
  title: string;
  image_id: string;
  thumbnail?: {
    alt_text?: string;
  };
  artist_title?: string;
  artist_display?: string;
  place_of_origin?: string;
  department_title?: string;
  date_display?: string;
}
