import { Button } from "@/components/ui/button";
import { Text } from "@/components/ui/text";
import { ItemDetails } from "@/components/app/ItemDetails";
import { fetchItemById } from "@/lib/services/dataService";
import { DataSourceType, ItemUiModel } from "@/lib/types/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
import { Stack, useLocalSearchParams, useRouter } from "expo-router";
import * as Sharing from "expo-sharing";
import React, { useEffect, useState } from "react";
import { ActivityIndicator, Alert, TouchableOpacity, View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

export default function DetailsScreen() {
  const router = useRouter();
  const { id, dataSource } =
    useLocalSearchParams<{ id: string; dataSource: string }>();

  const [item, setItem] = useState<ItemUiModel | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (dataSource && id) {
      loadItemDetails();
    }
  }, [dataSource, id]);

  const loadItemDetails = async () => {
    try {
      setIsLoading(true);
      const itemData = await fetchItemById(dataSource as DataSourceType, id);
      setItem(itemData);
    } catch (error) {
      console.error("Failed to load item details:", error);
      Alert.alert("Error", "Failed to load item details. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

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

  const handleBack = () => {
    router.back();
  };

  const renderContent = () => {
    if (isLoading) {
      return (
        <View className="flex-1 items-center justify-center">
          <ActivityIndicator size="large" />
        </View>
      );
    }

    if (!item) {
      return (
        <View className="flex-1 items-center justify-center p-5">
          <Text className="mb-5 text-center text-lg">Item not found</Text>
          <Button
            variant="outline"
            onPress={handleBack}
          >
            <Text>Go back</Text>
          </Button>
        </View>
      );
    }

    return <ItemDetails item={item} />;
  };

  const headerTitle = isLoading
    ? "Loading..."
    : item
      ? item.name
      : "Not Found";

  const headerRight =
    !isLoading && item
      ? () => (
          <TouchableOpacity onPress={handleShare} className="p-2">
            <Ionicons
              name="share-outline"
              size={24}
              className="text-foreground"
            />
          </TouchableOpacity>
        )
      : undefined;

  return (
    <>
      <Stack.Screen options={{ title: headerTitle, headerRight }} />
      <SafeAreaView edges={["bottom", "left", "right"]} className="flex-1">
        {renderContent()}
      </SafeAreaView>
    </>
  );
}
