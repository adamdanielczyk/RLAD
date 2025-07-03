import { FullScreenErrorView } from "@/components/FullScreenErrorView";
import { ItemDetails } from "@/components/ItemDetails";
import { useItemByIdQuery } from "@/lib/queries/useItemsQuery";
import { useAppStore } from "@/lib/store/appStore";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import { useTheme } from "@react-navigation/native";
import * as FileSystem from "expo-file-system";
import { useLocalSearchParams } from "expo-router";
import * as Sharing from "expo-sharing";
import { StatusBar } from "expo-status-bar";
import React from "react";
import { ActivityIndicator, Alert, Platform, View } from "react-native";

export default function DetailsScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const { colors } = useTheme();

  const selectedDataSource = useAppStore((state) => state.selectedDataSource);
  const { item, isLoading } = useItemByIdQuery(id, selectedDataSource);

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
          onShare={() => handleShare(item)}
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

const handleShare = async (item: ItemUiModel) => {
  try {
    if (!(await Sharing.isAvailableAsync())) {
      Alert.alert("Sharing not available", "Sharing is not available on this device.");
      return;
    }

    // Android doesn't support sharing images directly so it needs to be downloaded first
    if (Platform.OS === "android") {
      const localUri = await downloadImageForSharing(item.imageUrl);
      await Sharing.shareAsync(localUri, {
        dialogTitle: item.name,
      });
    } else {
      await Sharing.shareAsync(item.imageUrl, {
        dialogTitle: item.name,
      });
    }
  } catch (error) {
    console.error("Failed to share:", error);
    Alert.alert("Error", "Failed to share item.");
  }
};

const getExtensionFromMimeType = (mimeType: string): string => {
  const mimeToExt: Record<string, string> = {
    "image/jpeg": "jpg",
    "image/jpg": "jpg",
    "image/png": "png",
    "image/gif": "gif",
    "image/webp": "webp",
    "image/bmp": "bmp",
    "image/svg+xml": "svg",
  };
  return mimeToExt[mimeType.toLowerCase()] || "jpg";
};

const downloadImageForSharing = async (imageUrl: string): Promise<string> => {
  const response = await fetch(imageUrl, { method: "HEAD" });
  const contentType = response.headers.get("content-type") || "image/jpeg";

  const fileExtension = getExtensionFromMimeType(contentType);
  const fileName = `shared_image_${Date.now()}.${fileExtension}`;
  const localUri = `${FileSystem.cacheDirectory}${fileName}`;

  const downloadResult = await FileSystem.downloadAsync(imageUrl, localUri);

  if (downloadResult.status === 200) {
    return downloadResult.uri;
  } else {
    throw new Error("Failed to download image");
  }
};
