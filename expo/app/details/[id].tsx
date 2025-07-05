import { FullScreenErrorView } from "@/components/FullScreenErrorView";
import { ItemDetails } from "@/components/ItemDetails";
import { useItemByIdQuery } from "@/lib/queries/useItemsQuery";
import { DataSourceType } from "@/lib/ui/uiModelTypes";
import { useTheme } from "@react-navigation/native";
import { useLocalSearchParams } from "expo-router";
import { StatusBar } from "expo-status-bar";
import React from "react";
import { ActivityIndicator, View } from "react-native";

export default function DetailsScreen() {
  const { id, dataSource } = useLocalSearchParams<{ id: string; dataSource: string }>();
  const { colors } = useTheme();
  const { item, isLoading } = useItemByIdQuery(id, dataSource as DataSourceType);

  const renderContent = () => {
    if (isLoading) {
      return (
        <View style={{ flex: 1, alignItems: "center", justifyContent: "center" }}>
          <ActivityIndicator
            size="large"
            color={colors.text}
          />
        </View>
      );
    }

    if (item) {
      return <ItemDetails item={item} />;
    } else {
      return <FullScreenErrorView />;
    }
  };

  return (
    <>
      <StatusBar style="light" />
      <View style={{ flex: 1 }}>{renderContent()}</View>
    </>
  );
}
