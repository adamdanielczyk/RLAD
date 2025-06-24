import { Text } from "@/components/ui/text";
import { ItemUiModel } from "@/lib/types/uiModelTypes";
import { Image } from "expo-image";
import { LinearGradient } from "expo-linear-gradient";
import React, { useEffect, useRef } from "react";
import { Animated, StyleSheet, TouchableOpacity, View } from "react-native";

interface ItemCardProps {
  item: ItemUiModel;
  onPress: (item: ItemUiModel) => void;
}

export function ItemCard({ item, onPress }: ItemCardProps) {
  const fadeAnim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    Animated.timing(fadeAnim, {
      toValue: 1,
      duration: 300,
      useNativeDriver: true,
    }).start();
  }, [fadeAnim]);

  return (
    <TouchableOpacity
      className="w-1/2 flex-1 p-2"
      onPress={() => onPress(item)}
    >
      <Animated.View
        style={{ opacity: fadeAnim }}
        className="overflow-hidden rounded-2xl shadow-lg"
      >
        <View className="relative">
          <Image
            source={{ uri: item.imageUrl }}
            style={{ width: '100%', height: 150 }}
            contentFit="cover"
          />
          <LinearGradient
            colors={["transparent", "rgba(0,0,0,0.6)"]}
            style={StyleSheet.absoluteFill}
          />
          <View className="absolute bottom-0 left-0 right-0 p-2">
            <Text className="text-base font-bold text-white">
              {item.name}
            </Text>
            {item.cardCaption ? (
              <Text className="text-xs text-gray-200">
                {item.cardCaption}
              </Text>
            ) : null}
          </View>
        </View>
      </Animated.View>
    </TouchableOpacity>
  );
}
