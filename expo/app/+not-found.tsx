import { Link } from "expo-router";

import { Button } from "@/components/ui/button";
import { Text } from "@/components/ui/text";
import React from "react";
import { View } from "react-native";

export default function NotFoundScreen() {
  return (
    <View className="flex-1 items-center justify-center p-4">
      <Text className="text-2xl font-bold">This screen does not exist.</Text>
      <Link
        href="/"
        asChild
      >
        <Button
          variant="outline"
          className="mt-4"
        >
          <Text>Go to home screen</Text>
        </Button>
      </Link>
    </View>
  );
}
