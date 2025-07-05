import { IconButton } from "@/components/IconButton";
import { shareItem } from "@/lib/services/shareService";
import { useAppStore } from "@/lib/store/appStore";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import { useTheme } from "@react-navigation/native";
import { Image } from "expo-image";
import { LinearGradient } from "expo-linear-gradient";
import { useRouter } from "expo-router";
import React from "react";
import { Dimensions, Text, View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

export function FullScreenItemCard({ item }: { item: ItemUiModel }) {
  const { width, height } = Dimensions.get("window");

  return (
    <View
      style={{
        width,
        height,
        position: "relative",
      }}
    >
      <Image
        source={item.highResImageUrl}
        style={{
          width: "100%",
          height: "100%",
        }}
        contentFit="cover"
        priority="high"
      />

      <LinearGradient
        colors={["transparent", "transparent", "rgba(0,0,0,0.8)"]}
        style={{
          position: "absolute",
          left: 0,
          right: 0,
          bottom: 0,
          height: height * 0.4,
        }}
      />

      <SideControls item={item} />
      <ItemInfo item={item} />
    </View>
  );
}

function SideControls({ item }: { item: ItemUiModel }) {
  const router = useRouter();
  const { colors } = useTheme();
  const insets = useSafeAreaInsets();
  const isFavoriteItem = useAppStore((state) => state.isFavorite(item.id));
  const toggleFavorite = useAppStore((state) => state.toggleFavorite);

  return (
    <View
      style={{
        position: "absolute",
        right: 16,
        bottom: insets.bottom + 24,
        alignItems: "center",
        gap: 24,
      }}
    >
      <IconButton
        onPress={() => toggleFavorite(item)}
        iconName={isFavoriteItem ? "heart" : "heart-outline"}
        color={isFavoriteItem ? colors.primary : "white"}
      />

      <IconButton
        onPress={() => shareItem(item)}
        iconName="share-social-outline"
      />

      <IconButton
        onPress={() =>
          router.push({
            pathname: "/details/[id]",
            params: { id: item.id, dataSource: item.dataSource },
          })
        }
        iconName="open-outline"
      />
    </View>
  );
}

function ItemInfo({ item }: { item: ItemUiModel }) {
  const insets = useSafeAreaInsets();

  return (
    <View
      style={{
        position: "absolute",
        left: 16,
        right: 80,
        bottom: insets.bottom + 24,
      }}
    >
      <Text
        style={{
          fontSize: 24,
          fontWeight: "700",
          color: "white",
          marginBottom: 8,
          letterSpacing: 0.5,
        }}
        numberOfLines={2}
      >
        {item.name}
      </Text>
      {item.cardCaption && (
        <Text
          style={{
            fontSize: 16,
            color: "white",
            opacity: 0.9,
            lineHeight: 22,
          }}
          numberOfLines={3}
        >
          {item.cardCaption}
        </Text>
      )}
    </View>
  );
}
