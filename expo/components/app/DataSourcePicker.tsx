import { cn } from "@/lib/utils";
import { DataSourceUiModel } from "@/lib/types/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
import BottomSheet, { BottomSheetBackdrop, BottomSheetView } from "@gorhom/bottom-sheet";
import { cssInterop } from "nativewind";
import React, { forwardRef, useCallback } from "react";
import { FlatList, Text, TouchableOpacity } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

cssInterop(BottomSheet, {
  className: "backgroundStyle",
  handleIndicatorClassName: "handleIndicatorStyle",
});

export interface DataSourcePickerProps {
  dataSources: DataSourceUiModel[];
  onSelect: (ds: DataSourceUiModel) => void;
}

export const DataSourcePicker = forwardRef<BottomSheet, DataSourcePickerProps>(
  ({ dataSources, onSelect }, ref) => {
    const insets = useSafeAreaInsets();

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
          onPress={() => onSelect(item)}
        >
          <Ionicons
            name="checkmark"
            className={cn(
              "text-foreground",
              item.isSelected ? "visible" : "invisible",
            )}
            size={24}
          />
          <Text className="ms-2 text-base text-foreground">{item.name}</Text>
        </TouchableOpacity>
      ),
      [onSelect],
    );

    return (
      <BottomSheet
        ref={ref}
        index={-1}
        enableDynamicSizing
        enablePanDownToClose
        enableOverDrag={false}
        backdropComponent={renderBackdrop}
        className="bg-background"
        handleIndicatorClassName="bg-foreground"
      >
        <BottomSheetView className="p-8" style={{ paddingBottom: insets.bottom }}>
          <Text className="mb-4 text-xl font-bold text-foreground">
            Pick data source
          </Text>
          <FlatList
            data={dataSources}
            renderItem={renderItem}
            keyExtractor={(item) => item.name}
          />
        </BottomSheetView>
      </BottomSheet>
    );
  },
);

DataSourcePicker.displayName = "DataSourcePicker";
