import { FullScreenErrorView } from "@/components/FullScreenErrorView";
import { ItemDetails } from "@/components/ItemDetails";
import { useItemByIdQuery } from "@/lib/queries/useItemsQuery";
import { useAppStore } from "@/lib/store/appStore";
import { useTheme } from "@react-navigation/native";
import { useLocalSearchParams } from "expo-router";
import * as Sharing from "expo-sharing";
import { StatusBar } from "expo-status-bar";
import React from "react";
import { ActivityIndicator, Alert, View } from "react-native";

export default function DetailsScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const { colors } = useTheme();

  const selectedDataSource = useAppStore((state) => state.selectedDataSource);
  const { item, isLoading } = useItemByIdQuery(id, selectedDataSource);

  const handleShare = async () => {
    if (!item) return;

    try {
      if (await Sharing.isAvailableAsync()) {
        await Sharing.shareAsync(item.imageUrl, {
          dialogTitle: item.name,
        });
      } else {
        Alert.alert("Sharing not available", "Sharing is not available on this device.");
      }
    } catch (error) {
      console.error("Failed to share:", error);
      Alert.alert("Error", "Failed to share item.");
    }
  };

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
      return (
        <ItemDetails
          item={item}
          onShare={handleShare}
        />
      );
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
