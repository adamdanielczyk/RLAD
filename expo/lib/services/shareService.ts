import * as FileSystem from "expo-file-system";
import * as Sharing from "expo-sharing";
import { Alert, Platform } from "react-native";

export async function shareItem(imageUrl: string, title: string): Promise<void> {
  try {
    if (!(await Sharing.isAvailableAsync())) {
      Alert.alert("Sharing not available", "Sharing is not available on this device.");
      return;
    }

    // Android doesn't support sharing images directly so it needs to be downloaded first
    if (Platform.OS === "android") {
      const localUri = await downloadImageForSharing(imageUrl);
      await Sharing.shareAsync(localUri, {
        dialogTitle: title,
      });
    } else {
      await Sharing.shareAsync(imageUrl, {
        dialogTitle: title,
      });
    }
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
  const localUri = `${FileSystem.cacheDirectory}${fileName}`;

  const downloadResult = await FileSystem.downloadAsync(imageUrl, localUri);

  if (downloadResult.status === 200) {
    return downloadResult.uri;
  } else {
    throw new Error("Failed to download image");
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
