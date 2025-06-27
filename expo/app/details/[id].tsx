import { FullScreenErrorView } from "@/components/FullScreenErrorView";
import { ItemDetails } from "@/components/ItemDetails";
import { useAppStore } from "@/lib/store/appStore";
import { Ionicons } from "@expo/vector-icons";
import { useTheme } from "@react-navigation/native";
import { Stack, useLocalSearchParams } from "expo-router";
import * as Sharing from "expo-sharing";
import React, { useEffect } from "react";
import { ActivityIndicator, Alert, TouchableOpacity, View } from "react-native";

export default function DetailsScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const { colors } = useTheme();

  const item = useAppStore((state) => state.detailedItem);
  const isLoading = useAppStore((state) => state.isDetailedItemLoading);
  const loadItemById = useAppStore((state) => state.loadItemById);
  const clearDetailedItem = useAppStore((state) => state.clearDetailedItem);

  useEffect(() => {
    if (id) {
      loadItemById(id);
    }
    return () => {
      clearDetailedItem();
    };
  }, [id, loadItemById, clearDetailedItem]);

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
      return <ItemDetails item={item} />;
    } else {
      return <FullScreenErrorView />;
    }
  };

  const headerTitle = isLoading ? "Loading..." : item ? item.name : "Not Found";

  const headerRight =
    !isLoading && item
      ? () => (
          <TouchableOpacity
            onPress={handleShare}
            style={{ padding: 8 }}
          >
            <Ionicons
              name="share-outline"
              size={24}
              color={colors.text}
            />
          </TouchableOpacity>
        )
      : undefined;

  return (
    <>
      <Stack.Screen options={{ title: headerTitle, headerRight }} />
      <View style={{ flex: 1 }}>{renderContent()}</View>
    </>
  );
}
