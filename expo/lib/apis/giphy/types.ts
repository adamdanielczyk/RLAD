export interface GiphyResponse {
  data: GiphyGif[];
  pagination: {
    count: number;
    offset: number;
  };
}

export interface GiphyGif {
  id: string;
  url: string;
  username: string;
  title: string;
  import_datetime: string;
  trending_datetime: string;
  images: {
    fixed_width: {
      url: string;
      width: string;
      height: string;
    };
    downsized: {
      url: string;
      width: string;
      height: string;
    };
    original: {
      url: string;
      width: string;
      height: string;
    };
  };
}
