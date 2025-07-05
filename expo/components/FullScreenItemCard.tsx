import { useAppStore } from "@/lib/store/appStore";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
import { useTheme } from "@react-navigation/native";
import { Image } from "expo-image";
import { LinearGradient } from "expo-linear-gradient";
import { useRouter } from "expo-router";
import React from "react";
import { Dimensions, Text, TouchableOpacity, View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

interface FullScreenItemCardProps {
  item: ItemUiModel;
}

export function FullScreenItemCard({ item }: FullScreenItemCardProps) {
  const router = useRouter();
  const { colors } = useTheme();
  const insets = useSafeAreaInsets();
  const { width, height } = Dimensions.get("window");

  const isFavoriteItem = useAppStore((state) => state.isFavorite(item.id));
  const toggleFavorite = useAppStore((state) => state.toggleFavorite);
  const toggleViewMode = useAppStore((state) => state.toggleViewMode);
  const onFilterButtonClicked = useAppStore((state) => state.onFilterButtonClicked);

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

      <TopControls
        insets={insets}
        toggleViewMode={toggleViewMode}
        onFilterButtonClicked={onFilterButtonClicked}
      />

      <SideControls
        insets={insets}
        isFavoriteItem={isFavoriteItem}
        colors={colors}
        handleFavoritePress={() => toggleFavorite(item)}
        handlePress={() =>
          router.push({
            pathname: "/details/[id]",
            params: { id: item.id, dataSource: item.dataSource },
          })
        }
      />

      <ItemInfo
        item={item}
        insets={insets}
      />
    </View>
  );
}

function TopControls({
  insets,
  toggleViewMode,
  onFilterButtonClicked,
}: {
  insets: { top: number };
  toggleViewMode: () => void;
  onFilterButtonClicked: () => void;
}) {
  return (
    <View
      style={{
        position: "absolute",
        top: insets.top + 16,
        left: 16,
        right: 16,
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
      }}
    >
      <IconButton
        onPress={toggleViewMode}
        iconName="apps"
      />

      <IconButton
        onPress={onFilterButtonClicked}
        iconName="options-outline"
      />
    </View>
  );
}

function SideControls({
  insets,
  isFavoriteItem,
  colors,
  handleFavoritePress,
  handlePress,
}: {
  insets: { bottom: number };
  isFavoriteItem: boolean;
  colors: { primary: string };
  handleFavoritePress: () => void;
  handlePress: () => void;
}) {
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
        onPress={handleFavoritePress}
        iconName={isFavoriteItem ? "heart" : "heart-outline"}
        color={isFavoriteItem ? colors.primary : "white"}
      />

      <IconButton
        onPress={handlePress}
        iconName="open-outline"
      />
    </View>
  );
}

function ItemInfo({ item, insets }: { item: ItemUiModel; insets: { bottom: number } }) {
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

function IconButton({
  onPress,
  iconName,
  color = "white",
}: {
  onPress: () => void;
  iconName: keyof typeof Ionicons.glyphMap;
  color?: string;
}) {
  return (
    <TouchableOpacity
      onPress={onPress}
      style={{
        backgroundColor: "rgba(0,0,0,0.3)",
        borderRadius: 25,
        width: 50,
        height: 50,
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Ionicons
        name={iconName}
        size={24}
        color={color}
      />
    </TouchableOpacity>
  );
}
