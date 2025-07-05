import { useMemo } from "react";
import { useWindowDimensions } from "react-native";

const ITEM_MIN_WIDTH = 150;

export function useColumns() {
  const { width } = useWindowDimensions();

  const numColumns = useMemo(() => {
    return Math.max(1, Math.floor(width / ITEM_MIN_WIDTH));
  }, [width]);

  return numColumns;
}
