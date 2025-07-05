import { EmptyView } from "@/components/EmptyView";
import { ItemCard } from "@/components/ItemCard";
import { useColumns } from "@/lib/hooks/useColumns";
import { useAppStore } from "@/lib/store/appStore";
import { Ionicons } from "@expo/vector-icons";
import { useTheme } from "@react-navigation/native";
import { FlashList } from "@shopify/flash-list";
import { useRouter } from "expo-router";
import React from "react";
import { Text, TouchableOpacity, View } from "react-native";
import { SafeAreaView, useSafeAreaInsets } from "react-native-safe-area-context";

export default function FavoritesScreen() {
  const insets = useSafeAreaInsets();
  const numColumns = useColumns();
  const favorites = useAppStore((state) => state.favorites);

  return (
    <SafeAreaView
      style={{ flex: 1 }}
      edges={["top", "left", "right"]}
    >
      <Header />

      {favorites.length === 0 ? (
        <EmptyView
          icon="heart-outline"
          title="No Favorites Yet"
          description="Start exploring and tap the heart icon to save your favorite items"
        />
      ) : (
        <FlashList
          data={favorites}
          renderItem={({ item }) => <ItemCard item={item} />}
          numColumns={numColumns}
          contentContainerStyle={{ paddingHorizontal: 8, paddingBottom: insets.bottom }}
          estimatedItemSize={200}
          showsVerticalScrollIndicator={false}
        />
      )}
    </SafeAreaView>
  );
}

function Header() {
  const router = useRouter();
  const { colors } = useTheme();

  return (
    <View
      style={{
        paddingTop: 8,
        paddingBottom: 12,
        paddingHorizontal: 20,
        backgroundColor: colors.background,
        flexDirection: "row",
        alignItems: "center",
      }}
    >
      <TouchableOpacity
        onPress={() => router.back()}
        style={{
          padding: 8,
          marginEnd: 16,
        }}
      >
        <Ionicons
          name="arrow-back"
          size={24}
          color={colors.text}
        />
      </TouchableOpacity>
      <Text
        style={{
          fontSize: 20,
          fontWeight: "600",
          color: colors.text,
        }}
      >
        Favorites
      </Text>
    </View>
  );
}
