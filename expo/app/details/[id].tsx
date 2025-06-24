import { ItemDetails } from "@/components/ItemDetails";
import { Button } from "@/components/ui/button";
import { Text } from "@/components/ui/text";
import { useAppStore } from "@/lib/store/appStore";
import { Ionicons } from "@expo/vector-icons";
import { Link, Stack, useLocalSearchParams } from "expo-router";
import * as Sharing from "expo-sharing";
import React, { useEffect } from "react";
import { ActivityIndicator, Alert, TouchableOpacity, View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

export default function DetailsScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();

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
        <View className="flex-1 items-center justify-center">
          <ActivityIndicator size="large" />
        </View>
      );
    }

    if (!item) {
      return (
        <View className="flex-1 items-center justify-center p-5">
          <Text className="text-2xl font-bold">Item not found</Text>
          <Link
            href="/"
            replace
            asChild
          >
            <Button
              variant="outline"
              className="mt-4"
            >
              <Text>Go to home screen</Text>
            </Button>
          </Link>
        </View>
      );
    }

    return <ItemDetails item={item} />;
  };

  const headerTitle = isLoading ? "Loading..." : item ? item.name : "Not Found";

  const headerRight =
    !isLoading && item
      ? () => (
          <TouchableOpacity
            onPress={handleShare}
            className="p-2"
          >
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
      <SafeAreaView
        edges={["top", "left", "right"]}
        className="flex-1"
      >
        {renderContent()}
      </SafeAreaView>
    </>
  );
}
