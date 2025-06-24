import { DATA_SOURCES } from "@/lib/services/dataSources";
import { DataSourceType } from "@/lib/types/uiModelTypes";
import { cn } from "@/lib/utils";
import { Ionicons } from "@expo/vector-icons";
import BottomSheet, { BottomSheetBackdrop, BottomSheetView } from "@gorhom/bottom-sheet";
import { cssInterop } from "nativewind";
import React, { useCallback, useEffect, useRef } from "react";
import { FlatList, Text, TouchableOpacity } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

interface DataSourceBottomSheetProps {
  isOpen: boolean;
  onClose: () => void;
  selectedDataSource: DataSourceType;
  onDataSourceSelected: (dataSource: DataSourceType) => Promise<void>;
}

export const DataSourceBottomSheet = ({
  isOpen,
  onClose,
  selectedDataSource,
  onDataSourceSelected,
}: DataSourceBottomSheetProps) => {
  const bottomSheetRef = useRef<BottomSheet | null>(null);
  const insets = useSafeAreaInsets();

  useEffect(() => {
    if (isOpen) {
      bottomSheetRef.current?.expand();
    } else {
      bottomSheetRef.current?.close();
    }
  }, [isOpen]);

  const handleDataSourceSelected = useCallback(
    async (dataSource: DataSourceType) => {
      await onDataSourceSelected(dataSource);
    },
    [onDataSourceSelected],
  );

  const handleSheetChanges = useCallback(
    (index: number) => {
      if (index === -1) {
        onClose();
      }
    },
    [onClose],
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
        className="flex-row items-center py-4"
        onPress={() => handleDataSourceSelected(item.type)}
      >
        <Ionicons
          name="checkmark"
          className={cn(
            "text-foreground",
            item.type === selectedDataSource ? "visible" : "invisible",
          )}
          size={24}
        />
        <Text className="ms-2 text-base text-foreground">{item.name}</Text>
      </TouchableOpacity>
    ),
    [handleDataSourceSelected, selectedDataSource],
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
      style={{
        marginLeft: insets.left,
        marginRight: insets.right,
      }}
    >
      <BottomSheetView
        className="p-8"
        style={{ paddingBottom: insets.bottom }}
      >
        <Text className="mb-4 text-xl font-bold text-foreground">Pick data source</Text>
        <FlatList
          data={DATA_SOURCES}
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
