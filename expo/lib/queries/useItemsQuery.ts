import { fetchItemById, fetchItems } from "@/lib/apis/apis";
import { DataSourceType } from "@/lib/ui/uiModelTypes";
import { useInfiniteQuery, useQuery, useQueryClient } from "@tanstack/react-query";
import { useCallback, useEffect, useMemo, useState } from "react";

export function useItemsQuery(selectedDataSource: DataSourceType, query?: string) {
  const debouncedQuery = useDebouncedSearch(query || "");
  const queryClient = useQueryClient();

  const {
    data,
    isLoading,
    isFetching,
    isRefetching,
    hasNextPage,
    fetchNextPage,
    isFetchingNextPage,
    refetch,
  } = useInfiniteQuery({
    queryKey: ["items", selectedDataSource, debouncedQuery],
    queryFn: ({ pageParam }) =>
      fetchItems({
        dataSource: selectedDataSource,
        offset: pageParam,
        query: debouncedQuery,
      }),
    getNextPageParam: (lastPage) => (lastPage.hasMorePages ? lastPage.nextOffset : undefined),
    initialPageParam: undefined as number | undefined,
  });

  const items = useMemo(() => {
    return data?.pages.flatMap((page) => page.items) ?? [];
  }, [data]);

  const loadMoreItems = useCallback(() => {
    if (hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  }, [hasNextPage, isFetchingNextPage, fetchNextPage]);

  const refetchFirstPage = useCallback(() => {
    queryClient.resetQueries({ queryKey: ["items", selectedDataSource] });
    return refetch();
  }, [queryClient, selectedDataSource, refetch]);

  return {
    items,
    isLoading,
    isRefetching,
    isFetching,
    isFetchingNextPage,
    loadMoreItems,
    refetch: refetchFirstPage,
  };
}

function useDebouncedSearch(value: string) {
  const [debouncedValue, setDebouncedValue] = useState(value);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, 400);

    return () => {
      clearTimeout(handler);
    };
  }, [value]);

  return debouncedValue;
}

export function useItemByIdQuery(id: string, selectedDataSource: DataSourceType) {
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: ["item", selectedDataSource, id],
    queryFn: () => fetchItemById(id, selectedDataSource),
    enabled: !!id,
    initialData: () => {
      const queries = queryClient.getQueriesData({
        queryKey: ["items", selectedDataSource],
      });

      for (const [_, queryData] of queries) {
        if (queryData && typeof queryData === "object" && "pages" in queryData) {
          const infiniteQueryData = queryData as { pages: { items: any[] }[] };
          for (const page of infiniteQueryData.pages) {
            const item = page.items?.find((item) => item.id === id);
            if (item) {
              return item;
            }
          }
        }
      }

      return undefined;
    },
  });

  return {
    item: data,
    isLoading,
  };
}
