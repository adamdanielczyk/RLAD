import { DATA_SOURCES } from "@/lib/apis/dataSources";
import { useAppStore } from "@/lib/store/appStore";
import { DataSourceType } from "@/lib/ui/uiModelTypes";
import BottomSheet, { BottomSheetBackdrop, BottomSheetView } from "@gorhom/bottom-sheet";
import { useTheme } from "@react-navigation/native";
import React, { useCallback, useEffect, useRef } from "react";
import { FlatList, Text, TouchableOpacity, useWindowDimensions } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

export const DataSourceBottomSheet = () => {
  const bottomSheetRef = useRef<BottomSheet | null>(null);
  const insets = useSafeAreaInsets();
  const { colors } = useTheme();

  const { width } = useWindowDimensions();
  const maxWidth = Math.min(width - 32, 480);
  const isWide = width > 768;
  const sideMargin = isWide ? (width - maxWidth) / 2 : 0;

  const isBottomSheetOpen = useAppStore((state) => state.isBottomSheetOpen);
  const selectedDataSource = useAppStore((state) => state.selectedDataSource);
  const onDataSourceSelected = useAppStore((state) => state.onDataSourceSelected);
  const onBottomSheetClosed = useAppStore((state) => state.onBottomSheetClosed);

  useEffect(() => {
    if (isBottomSheetOpen) {
      bottomSheetRef.current?.expand();
    } else {
      bottomSheetRef.current?.close();
    }
  }, [isBottomSheetOpen]);

  const handleDataSourceSelected = useCallback(
    async (dataSource: DataSourceType) => {
      await onDataSourceSelected(dataSource);
    },
    [onDataSourceSelected],
  );

  const handleSheetChanges = useCallback(
    (index: number) => {
      if (index === -1) {
        onBottomSheetClosed();
      }
    },
    [onBottomSheetClosed],
  );

  const renderBackdrop = useCallback(
    (props: any) => (
      <BottomSheetBackdrop
        {...props}
        disappearsOnIndex={-1}
        appearsOnIndex={0}
      />
    ),
    [],
  );

  const renderItem = useCallback(
    ({ item }: { item: (typeof DATA_SOURCES)[number] }) => (
      <TouchableOpacity
        style={{
          backgroundColor: item.type === selectedDataSource ? colors.primary : colors.card,
          borderRadius: 16,
          padding: 20,
          marginBottom: 12,
          borderWidth: 2,
          borderColor: item.type === selectedDataSource ? colors.primary : "transparent",
          shadowOffset: { width: 0, height: 2 },
          shadowOpacity: 0.1,
          shadowRadius: 4,
          elevation: 2,
        }}
        onPress={() => handleDataSourceSelected(item.type)}
      >
        <Text
          style={{
            color: item.type === selectedDataSource ? "white" : colors.text,
            fontSize: 16,
            fontWeight: "600",
            textAlign: "center",
            letterSpacing: 0.3,
          }}
        >
          {item.name}
        </Text>
      </TouchableOpacity>
    ),
    [handleDataSourceSelected, selectedDataSource, colors],
  );

  return (
    <BottomSheet
      ref={bottomSheetRef}
      index={-1}
      enableDynamicSizing
      enablePanDownToClose
      enableOverDrag={false}
      backdropComponent={renderBackdrop}
      onChange={handleSheetChanges}
      backgroundStyle={{ backgroundColor: colors.background }}
      handleIndicatorStyle={{ backgroundColor: colors.text }}
      style={{
        marginLeft: sideMargin || insets.left,
        marginRight: sideMargin || insets.right,
      }}
    >
      <BottomSheetView
        style={{ paddingHorizontal: 32, paddingTop: 32, paddingBottom: insets.bottom }}
      >
        <Text
          style={{
            marginBottom: 24,
            fontSize: 22,
            fontWeight: "700",
            color: colors.text,
            letterSpacing: 0.5,
          }}
        >
          Pick data source
        </Text>
        <FlatList
          data={DATA_SOURCES}
          renderItem={renderItem}
          keyExtractor={(item) => item.name}
        />
      </BottomSheetView>
    </BottomSheet>
  );
};
