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
  StyleSheet,
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
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color={isDark ? "#fff" : "#000"} />
        </View>
      );
    }

    if (!item) {
      return (
        <View style={styles.errorContainer}>
          <Text style={[styles.errorText, { color: isDark ? "#fff" : "#000" }]}>
            Item not found
          </Text>
          <TouchableOpacity onPress={handleBack} style={styles.backButton}>
            <Text style={styles.backButtonText}>Go Back</Text>
          </TouchableOpacity>
        </View>
      );
    }

    return (
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <View style={styles.imageContainer}>
          <Image source={{ uri: item.imageUrl }} style={styles.image} contentFit="cover" />
        </View>

        <View style={styles.detailsContainer}>
          {item.detailsKeyValues.map((detail, index) => (
            <View key={index} style={styles.detailItem}>
              <Text style={[styles.detailKey, { color: isDark ? "#fff" : "#000" }]}>
                {detail.key}
              </Text>
              <Text style={[styles.detailValue, { color: isDark ? "#ccc" : "#666" }]}>
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
        style={({ pressed }) => [styles.shareButton, { opacity: pressed ? 0.7 : 1 }]}
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
        style={[styles.container, { backgroundColor: isDark ? "#000" : "#fff" }]}
      >
        {renderContent()}
      </SafeAreaView>
    </>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  errorContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 20,
  },
  errorText: {
    fontSize: 18,
    marginBottom: 20,
    textAlign: "center",
  },
  backButton: {
    padding: 12,
    borderRadius: 8,
    backgroundColor: "#007AFF",
  },
  backButtonText: {
    color: "#fff",
    fontSize: 16,
    fontWeight: "600",
  },
  shareButton: {
    padding: 8,
  },
  scrollContent: {
    flexGrow: 1,
  },
  imageContainer: {
    position: "relative",
  },
  image: {
    width: width,
    height: width * 0.75, // 4:3 aspect ratio
  },
  detailsContainer: {
    padding: 20,
  },
  detailItem: {
    marginBottom: 16,
  },
  detailKey: {
    fontSize: 18,
    fontWeight: "600",
    marginBottom: 4,
  },
  detailValue: {
    fontSize: 16,
    lineHeight: 24,
  },
});
