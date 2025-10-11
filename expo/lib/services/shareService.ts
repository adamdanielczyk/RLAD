import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import * as FileSystem from "expo-file-system";
import * as Sharing from "expo-sharing";
import { Alert } from "react-native";

export async function shareItem(item: ItemUiModel): Promise<void> {
  const title = item.name;
  const imageUrl = item.highResImageUrl;

  try {
    if (!(await Sharing.isAvailableAsync())) {
      Alert.alert("Sharing not available", "Sharing is not available on this device.");
      return;
    }

    const localUri = await downloadImageForSharing(imageUrl);
    await Sharing.shareAsync(localUri, {
      dialogTitle: title,
    });
  } catch (error) {
    console.error("Failed to share:", error);
    Alert.alert("Error", "Failed to share item.");
  }
}

const downloadImageForSharing = async (imageUrl: string): Promise<string> => {
  const response = await fetch(imageUrl, { method: "HEAD" });
  const contentType = response.headers.get("content-type") || "image/jpeg";

  const fileExtension = getExtensionFromMimeType(contentType);
  const fileName = `shared_image_${Date.now()}.${fileExtension}`;
  const file = new FileSystem.File(FileSystem.Paths.cache, fileName);
  const downloadedFile = await FileSystem.File.downloadFileAsync(imageUrl, file);
  return downloadedFile.uri;
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
