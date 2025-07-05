import { useAppStore } from "@/lib/store/appStore";
import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import { Ionicons } from "@expo/vector-icons";
import { useTheme } from "@react-navigation/native";
import { Image } from "expo-image";
import { LinearGradient } from "expo-linear-gradient";
import { useRouter } from "expo-router";
import React from "react";
import { Text, TouchableOpacity, View } from "react-native";
import Animated, { useAnimatedStyle, useSharedValue, withSpring } from "react-native-reanimated";

export function ItemCard({ item }: { item: ItemUiModel }) {
  const router = useRouter();
  const { colors } = useTheme();
  const isFavoriteItem = useAppStore((state) => state.isFavorite(item.id));
  const toggleFavorite = useAppStore((state) => state.toggleFavorite);

  const scale = useSharedValue(1);

  const cardAnimatedStyle = useAnimatedStyle(() => {
    return {
      transform: [{ scale: scale.value }],
    };
  });

  const springConfig = {
    damping: 20,
    stiffness: 400,
  };

  const handlePressIn = () => {
    scale.value = withSpring(0.96, springConfig);
  };

  const handlePressOut = () => {
    scale.value = withSpring(1, springConfig);
  };

  return (
    <Animated.View style={[cardAnimatedStyle, { flex: 1, margin: 8 }]}>
      <TouchableOpacity
        onPress={() =>
          router.push({
            pathname: "/details/[id]",
            params: { id: item.id, dataSource: item.dataSource },
          })
        }
        onPressIn={handlePressIn}
        onPressOut={handlePressOut}
        activeOpacity={0.8}
        style={{
          backgroundColor: colors.card,
          borderRadius: 20,
          shadowOffset: { width: 0, height: 4 },
          shadowOpacity: 0.15,
          shadowRadius: 8,
          elevation: 5,
        }}
      >
        <View
          style={{
            borderRadius: 20,
            overflow: "hidden",
          }}
        >
          <View style={{ position: "relative" }}>
            <Image
              key={item.id}
              source={item.imageUrl}
              style={{
                width: "100%",
                aspectRatio: 1,
                height: undefined,
              }}
              contentFit="cover"
            />

            <LinearGradient
              colors={["transparent", "rgba(0,0,0,0.2)"]}
              style={{
                position: "absolute",
                left: 0,
                right: 0,
                bottom: 0,
                height: 60,
              }}
            />

            {isFavoriteItem && (
              <View
                style={[
                  {
                    position: "absolute",
                    top: 12,
                    end: 12,
                    backgroundColor: colors.card,
                    borderRadius: 20,
                    padding: 8,
                    shadowOffset: { width: 0, height: 2 },
                    shadowOpacity: 0.2,
                    shadowRadius: 4,
                    elevation: 3,
                  },
                ]}
              >
                <TouchableOpacity onPress={() => toggleFavorite(item)}>
                  <Ionicons
                    name="heart"
                    size={20}
                    color={colors.primary}
                  />
                </TouchableOpacity>
              </View>
            )}
          </View>
          <View style={{ padding: 16 }}>
            <Text
              numberOfLines={1}
              style={{
                fontSize: 17,
                fontWeight: "600",
                textOverflow: "ellipsis",
                color: colors.text,
                letterSpacing: 0.3,
              }}
            >
              {item.name}
            </Text>
            <Text
              numberOfLines={1}
              style={{
                fontSize: 13,
                textOverflow: "ellipsis",
                color: colors.text,
                opacity: 0.7,
                marginTop: 4,
              }}
            >
              {item.cardCaption?.replace("\n", " ")}
            </Text>
          </View>
        </View>
      </TouchableOpacity>
    </Animated.View>
  );
}
