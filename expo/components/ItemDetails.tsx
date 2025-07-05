import { useAppStore } from "@/lib/store/appStore";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
import { useTheme } from "@react-navigation/native";
import { Image } from "expo-image";
import { LinearGradient } from "expo-linear-gradient";
import { useRouter } from "expo-router";
import React from "react";
import { Dimensions, Platform, ScrollView, Text, TouchableOpacity, View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

const { width } = Dimensions.get("window");
const HERO_HEIGHT = width * 0.75;

export function ItemDetails({ item, onShare }: { item: ItemUiModel; onShare: () => void }) {
  return (
    <View style={{ flex: 1 }}>
      <ScrollView
        contentContainerStyle={{ flexGrow: 1 }}
        showsVerticalScrollIndicator={false}
      >
        <HeroImage item={item} />
        <MainContent item={item} />
      </ScrollView>
      <HeaderButtons
        item={item}
        onShare={onShare}
      />
    </View>
  );
}

function HeaderButtons({ item, onShare }: { item: ItemUiModel; onShare: () => void }) {
  const insets = useSafeAreaInsets();
  const router = useRouter();
  const { colors } = useTheme();
  const isFavoriteItem = useAppStore((state) => state.isFavorite(item.id));
  const toggleFavorite = useAppStore((state) => state.toggleFavorite);

  const button = (icon: keyof typeof Ionicons.glyphMap, color: string, onPress: () => void) => {
    return (
      <TouchableOpacity
        onPress={onPress}
        style={{
          backgroundColor: colors.card,
          borderRadius: 24,
          width: 48,
          height: 48,
          justifyContent: "center",
          alignItems: "center",
          shadowOffset: { width: 0, height: 4 },
          shadowOpacity: 0.2,
          shadowRadius: 8,
          elevation: 8,
          opacity: 0.9,
        }}
      >
        <Ionicons
          name={icon}
          size={24}
          color={color}
        />
      </TouchableOpacity>
    );
  };

  return (
    <View
      style={{
        position: "absolute",
        top: Platform.OS === "ios" ? 32 : 16 + insets.top,
        left: 16 + insets.left,
        right: 16 + insets.right,
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
      }}
    >
      {button("close", colors.text, () => router.back())}
      <View style={{ flexDirection: "row", gap: 12 }}>
        {button(
          isFavoriteItem ? "heart" : "heart-outline",
          isFavoriteItem ? colors.primary : colors.text,
          () => toggleFavorite(item),
        )}
        {button("share-outline", colors.text, onShare)}
      </View>
    </View>
  );
}

function HeroImage({ item }: { item: ItemUiModel }) {
  return (
    <View style={{ height: HERO_HEIGHT }}>
      <Image
        source={{ uri: item.imageUrl }}
        style={{ width: "100%", height: "100%" }}
        contentFit="cover"
      />

      <LinearGradient
        colors={["rgba(0,0,0,0.3)", "transparent", "transparent", "rgba(0,0,0,0.4)"]}
        style={{ position: "absolute", top: 0, left: 0, right: 0, bottom: 0 }}
      />
    </View>
  );
}

function MainContent({ item }: { item: ItemUiModel }) {
  const insets = useSafeAreaInsets();
  const { colors } = useTheme();

  return (
    <View
      style={{
        marginTop: -32,
        borderTopLeftRadius: 32,
        borderTopRightRadius: 32,
        backgroundColor: colors.background,
        flex: 1,
      }}
    >
      <View
        style={{
          padding: 32,
          marginLeft: insets.left,
          marginRight: insets.right,
          marginBottom: insets.bottom,
        }}
      >
        <Text
          style={{
            fontSize: 32,
            fontWeight: "700",
            color: colors.text,
            marginBottom: 32,
            letterSpacing: 0.5,
            lineHeight: 38,
          }}
        >
          {item.name}
        </Text>

        {item.detailsKeyValues.map((detail, index) => (
          <View key={index}>
            <View
              style={{
                marginBottom: 20,
                paddingBottom: index < item.detailsKeyValues.length - 1 ? 20 : 0,
                borderBottomWidth: index < item.detailsKeyValues.length - 1 ? 1 : 0,
                borderBottomColor: colors.border,
              }}
            >
              <Text
                style={{
                  fontSize: 18,
                  fontWeight: "600",
                  color: colors.text,
                  marginBottom: 8,
                  letterSpacing: 0.3,
                }}
              >
                {detail.key}
              </Text>
              <Text
                style={{
                  fontSize: 16,
                  color: colors.text,
                  lineHeight: 22,
                  opacity: 0.9,
                }}
              >
                {detail.value}
              </Text>
            </View>
          </View>
        ))}
      </View>
    </View>
  );
}
