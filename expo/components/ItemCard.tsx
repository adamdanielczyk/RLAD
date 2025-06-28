import { ItemUiModel } from "@/lib/ui/uiModelTypes";
import { useTheme } from "@react-navigation/native";
import { Image } from "expo-image";
import { LinearGradient } from "expo-linear-gradient";
import React from "react";
import { Text, TouchableOpacity, View } from "react-native";
import Animated, { useAnimatedStyle, useSharedValue, withSpring } from "react-native-reanimated";

interface ItemCardProps {
  item: ItemUiModel;
  onPress: (item: ItemUiModel) => void;
}

export function ItemCard({ item, onPress }: ItemCardProps) {
  const { colors } = useTheme();
  const scale = useSharedValue(1);

  const cardAnimatedStyle = useAnimatedStyle(() => {
    return {
      transform: [{ scale: scale.value }],
    };
  });

  const handlePressIn = () => {
    scale.value = withSpring(0.96, {
      damping: 20,
      stiffness: 400,
    });
  };

  const handlePressOut = () => {
    scale.value = withSpring(1, {
      damping: 20,
      stiffness: 400,
    });
  };

  return (
    <Animated.View style={[cardAnimatedStyle, { flex: 1, margin: 8 }]}>
      <TouchableOpacity
        onPress={() => onPress(item)}
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
              numberOfLines={2}
              style={{
                fontSize: 13,
                textOverflow: "ellipsis",
                color: colors.text,
                opacity: 0.7,
                marginTop: 4,
                lineHeight: 18,
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
