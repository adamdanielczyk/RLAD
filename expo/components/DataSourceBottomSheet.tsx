import { DATA_SOURCES } from "@/lib/apis/dataSources";
import { DataSourceType } from "@/lib/ui/uiModelTypes";
import BottomSheet, { BottomSheetBackdrop, BottomSheetView } from "@gorhom/bottom-sheet";
import { useTheme } from "@react-navigation/native";
import React, { useCallback, useEffect, useRef } from "react";
import { FlatList, Text, TouchableOpacity, View } from "react-native";
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
        style={{
          flexDirection: "row",
          alignItems: "center",
          paddingVertical: 18,
          gap: 16,
          paddingHorizontal: 16,
          marginBottom: 4,
        }}
        onPress={() => handleDataSourceSelected(item.type)}
      >
        <Text
          style={{
            color: colors.text,
            fontSize: 16,
            fontWeight: item.type === selectedDataSource ? "600" : "400",
            letterSpacing: 0.3,
            flex: 1,
          }}
        >
          {item.name}
        </Text>
        <View
          style={{
            width: 20,
            height: 20,
            borderRadius: 10,
            borderWidth: 2,
            borderColor: item.type === selectedDataSource ? colors.primary : colors.border,
            backgroundColor: item.type === selectedDataSource ? colors.primary : "transparent",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          {item.type === selectedDataSource && (
            <View
              style={{
                width: 8,
                height: 8,
                borderRadius: 4,
                backgroundColor: "white",
              }}
            />
          )}
        </View>
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
