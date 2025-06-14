import { Button } from "@/components/ui/button";
import { Text } from "@/components/ui/text";
import { fetchItemById } from "@/lib/services/dataService";
import { DataSourceType, ItemUiModel } from "@/lib/types/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
import { Image } from "expo-image";
import { Stack, useLocalSearchParams, useRouter } from "expo-router";
import * as Sharing from "expo-sharing";
import React, { useEffect, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  Dimensions,
  ScrollView,
  TouchableOpacity,
  View,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";

const { width } = Dimensions.get("window");

export default function DetailsScreen() {
  const router = useRouter();
  const { id, dataSource } = useLocalSearchParams<{ id: string; dataSource: string }>();

  const [item, setItem] = useState<ItemUiModel | null>(null);
  const [isLoading, setIsLoading] = useState(true);

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

    return (
      <ScrollView contentContainerStyle={{ flexGrow: 1 }}>
        <View className="relative">
          <Image
            source={{ uri: item.imageUrl }}
            style={{ width, height: width * 0.75 }}
            contentFit="cover"
          />
        </View>

        <View className="p-6">
          {item.detailsKeyValues.map((detail, index) => (
            <View
              key={index}
              className="mb-4"
            >
              <Text className="mb-1 text-lg font-semibold">{detail.key}</Text>
              <Text className="text-base leading-6">{detail.value}</Text>
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
      >
        {renderContent()}
      </SafeAreaView>
    </>
  );
}
