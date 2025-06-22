import { DATA_SOURCES } from "@/lib/services/dataSources";
import { useAppStore } from "@/lib/store/appStore";
import { DataSourceUiModel } from "@/lib/types/uiModelTypes";
import { cn } from "@/lib/utils";
import { Ionicons } from "@expo/vector-icons";
import BottomSheet, { BottomSheetBackdrop, BottomSheetView } from "@gorhom/bottom-sheet";
import { cssInterop } from "nativewind";
import React, { useCallback, useEffect, useMemo, useRef } from "react";
import { FlatList, Text, TouchableOpacity } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

export const DataSourceBottomSheet = () => {
  const bottomSheetRef = useRef<BottomSheet | null>(null);
  const insets = useSafeAreaInsets();

  const selectedDataSource = useAppStore((state) => state.selectedDataSource);
  const isBottomSheetOpen = useAppStore((state) => state.isBottomSheetOpen);

  useEffect(() => {
    if (isBottomSheetOpen) {
      bottomSheetRef.current?.expand();
    } else {
      bottomSheetRef.current?.close();
    }
  }, [isBottomSheetOpen]);

  const handleDataSourceSelected = useCallback(async (dataSource: DataSourceUiModel) => {
    await useAppStore.getState().setSelectedDataSource(dataSource.type);
  }, []);

  const handleSheetChanges = useCallback((index: number) => {
    if (index === -1) {
      useAppStore.getState().setIsBottomSheetOpen(false);
    }
  }, []);

  const dataSources = useMemo(
    () =>
      DATA_SOURCES.map((config) => ({
        name: config.name,
        type: config.type,
        isSelected: config.type === selectedDataSource,
      })),
    [selectedDataSource],
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
    ({ item }: { item: DataSourceUiModel }) => (
      <TouchableOpacity
        className="flex-row items-center py-4"
        onPress={() => handleDataSourceSelected(item)}
      >
        <Ionicons
          name="checkmark"
          className={cn("text-foreground", item.isSelected ? "visible" : "invisible")}
          size={24}
        />
        <Text className="ms-2 text-base text-foreground">{item.name}</Text>
      </TouchableOpacity>
    ),
    [handleDataSourceSelected],
  );

  return (
    <StyledBottomSheet
      ref={bottomSheetRef}
      index={-1}
      enableDynamicSizing
      enablePanDownToClose
      enableOverDrag={false}
      backdropComponent={renderBackdrop}
      onChange={handleSheetChanges}
      className="bg-background"
      handleIndicatorClassName="bg-foreground"
    >
      <BottomSheetView
        className="p-8"
        style={{ paddingBottom: insets.bottom }}
      >
        <Text className="mb-4 text-xl font-bold text-foreground">Pick data source</Text>
        <FlatList
          data={dataSources}
          renderItem={renderItem}
          keyExtractor={(item) => item.name}
        />
      </BottomSheetView>
    </StyledBottomSheet>
  );
};

const StyledBottomSheet = cssInterop(BottomSheet, {
  className: "backgroundStyle",
  handleIndicatorClassName: "handleIndicatorStyle",
});
