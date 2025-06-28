import { DATA_SOURCES } from "@/lib/apis/dataSources";
import { DataSourceType } from "@/lib/ui/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
import BottomSheet, { BottomSheetBackdrop, BottomSheetView } from "@gorhom/bottom-sheet";
import { useTheme } from "@react-navigation/native";
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
  const { colors } = useTheme();

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
        style={{ flexDirection: "row", alignItems: "center", paddingVertical: 16, gap: 8 }}
        onPress={() => handleDataSourceSelected(item.type)}
      >
        <Ionicons
          name="checkmark"
          color={colors.text}
          style={{ opacity: item.type === selectedDataSource ? 1 : 0 }}
          size={24}
        />
        <Text style={{ color: colors.text }}>{item.name}</Text>
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
      style={{ marginLeft: insets.left, marginRight: insets.right }}
    >
      <BottomSheetView
        style={{ paddingHorizontal: 32, paddingTop: 32, paddingBottom: insets.bottom }}
      >
        <Text style={{ marginBottom: 16, fontSize: 18, fontWeight: "bold", color: colors.text }}>
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
