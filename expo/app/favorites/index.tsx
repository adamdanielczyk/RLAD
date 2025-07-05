import { EmptyView } from "@/components/EmptyView";
import { ItemCard } from "@/components/ItemCard";
import { useColumns } from "@/lib/hooks/useColumns";
import { useAppStore } from "@/lib/store/appStore";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
import { useTheme } from "@react-navigation/native";
import { FlashList } from "@shopify/flash-list";
import { useRouter } from "expo-router";
import React, { useCallback } from "react";
import { Text, TouchableOpacity, View } from "react-native";
import { SafeAreaView, useSafeAreaInsets } from "react-native-safe-area-context";

export default function FavoritesScreen() {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const favorites = useAppStore((state) => state.favorites);

  const numColumns = useColumns();

  const renderItem = useCallback(
    ({ item }: { item: ItemUiModel }) => (
      <ItemCard
        item={item}
        onPress={(item) =>
          router.push({
            pathname: "/details/[id]",
            params: { id: item.id, dataSource: item.dataSource },
          })
        }
      />
    ),
    [router],
  );

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
          renderItem={renderItem}
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
