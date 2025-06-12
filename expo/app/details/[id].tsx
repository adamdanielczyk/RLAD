import { Ionicons } from "@expo/vector-icons";
import { Image } from "expo-image";
import { Stack, useLocalSearchParams, useRouter } from "expo-router";
import * as Sharing from "expo-sharing";
import React, { useEffect, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  Dimensions,
  Pressable,
  ScrollView,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

import { useColorScheme } from "@/hooks/useColorScheme";
import { fetchItemById } from "@/services/dataService";
import { DataSourceType, ItemUiModel } from "@/types/uiModelTypes";

const { width } = Dimensions.get("window");

export default function DetailsScreen() {
  const router = useRouter();
  const colorScheme = useColorScheme();
  const { id, dataSource } = useLocalSearchParams<{ id: string; dataSource: string }>();

  const [item, setItem] = useState<ItemUiModel | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const isDark = colorScheme === "dark";

  useEffect(() => {
    if (id && dataSource) {
      loadItemDetails();
    }
  }, [id, dataSource]);

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
          <ActivityIndicator size="large" color={isDark ? "#fff" : "#000"} />
        </View>
      );
    }

    if (!item) {
      return (
        <View className="flex-1 items-center justify-center p-5">
          <Text className="mb-5 text-lg text-center" style={{ color: isDark ? "#fff" : "#000" }}>
            Item not found
          </Text>
          <TouchableOpacity onPress={handleBack} className="rounded-lg bg-blue-500 p-3">
            <Text className="text-base font-semibold text-white">Go Back</Text>
          </TouchableOpacity>
        </View>
      );
    }

    return (
      <ScrollView contentContainerStyle={{ flexGrow: 1 }}>
        <View className="relative">
          <Image source={{ uri: item.imageUrl }} style={{ width, height: width * 0.75 }} contentFit="cover" />
        </View>

        <View className="p-5">
          {item.detailsKeyValues.map((detail, index) => (
            <View key={index} className="mb-4">
              <Text className="text-lg font-semibold mb-1" style={{ color: isDark ? "#fff" : "#000" }}>
                {detail.key}
              </Text>
              <Text className="text-base leading-6" style={{ color: isDark ? "#ccc" : "#666" }}>
                {detail.value}
              </Text>
            </View>
          ))}
        </View>
      </ScrollView>
    );
  };

  const getHeaderTitle = () => {
    if (isLoading) {
      return "Loading...";
    }
    if (!item) {
      return "Not Found";
    }
    return item.name;
  };

  const getHeaderRight = () => {
    if (isLoading || !item) {
      return undefined;
    }
    return () => (
      <Pressable
        onPress={handleShare}
        className="p-2"
        style={({ pressed }) => [{ opacity: pressed ? 0.7 : 1 }]}
        android_ripple={{ color: "rgba(128, 128, 128, 0.3)", borderless: true, radius: 20 }}
      >
        <Ionicons name="share-outline" size={24} color={isDark ? "#fff" : "#000"} />
      </Pressable>
    );
  };

  return (
    <>
      <Stack.Screen
        options={{
          title: getHeaderTitle(),
          headerRight: getHeaderRight(),
        }}
      />
      <SafeAreaView
        edges={["bottom", "left", "right"]}
        className="flex-1"
        style={{ backgroundColor: isDark ? "#000" : "#fff" }}
      >
        {renderContent()}
      </SafeAreaView>
    </>
  );
}

