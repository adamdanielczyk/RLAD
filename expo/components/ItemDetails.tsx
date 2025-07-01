import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
import { useTheme } from "@react-navigation/native";
import { Image } from "expo-image";
import { LinearGradient } from "expo-linear-gradient";
import { useRouter } from "expo-router";
import React from "react";
import { Dimensions, ScrollView, Text, TouchableOpacity, View } from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";

const { width } = Dimensions.get("window");
const HERO_HEIGHT = width * 0.75;

export function ItemDetails({ item, onShare }: { item: ItemUiModel; onShare: () => void }) {
  return (
    <View style={{ flex: 1 }}>
      <ScrollView contentContainerStyle={{ flexGrow: 1 }}>
        <HeroImage item={item} />
        <MainContent item={item} />
      </ScrollView>
      <HeaderButtons onShare={onShare} />
    </View>
  );
}

function HeaderButtons({ onShare }: { onShare: () => void }) {
  const insets = useSafeAreaInsets();
  const router = useRouter();

  const button = (icon: keyof typeof Ionicons.glyphMap, onPress: () => void) => {
    return (
      <TouchableOpacity
        onPress={onPress}
        style={{
          backgroundColor: "rgba(0,0,0,0.5)",
          borderRadius: 20,
          width: 40,
          height: 40,
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <Ionicons
          name={icon}
          size={24}
          color="white"
        />
      </TouchableOpacity>
    );
  };

  return (
    <View
      style={{
        position: "absolute",
        top: 8 + insets.top,
        left: 16 + insets.left,
        right: 16 + insets.right,
        flexDirection: "row",
        justifyContent: "space-between",
        alignItems: "center",
      }}
    >
      {button("close", () => router.back())}
      {button("share-outline", onShare)}
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
        marginTop: -24,
        borderTopLeftRadius: 24,
        borderTopRightRadius: 24,
        backgroundColor: colors.background,
      }}
    >
      <View
        style={{
          padding: 24,
          marginLeft: insets.left,
          marginRight: insets.right,
          marginBottom: insets.bottom,
        }}
      >
        <Text
          style={{
            fontSize: 28,
            fontWeight: "bold",
            color: colors.text,
            marginBottom: 24,
          }}
        >
          {item.name}
        </Text>

        {item.detailsKeyValues.map((detail, index) => (
          <View
            key={index}
            style={{ marginBottom: 16 }}
          >
            <Text
              style={{
                fontSize: 16,
                fontWeight: "bold",
                color: colors.text,
                marginBottom: 4,
              }}
            >
              {detail.key}
            </Text>
            <Text style={{ fontSize: 14, color: colors.text }}>{detail.value}</Text>
          </View>
        ))}
      </View>
    </View>
  );
}
