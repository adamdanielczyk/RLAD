import { Link } from "expo-router";

import { Button } from "@/components/ui/button";
import { Text } from "@/components/ui/text";
import React from "react";
import { View } from "react-native";

export default function NotFoundScreen() {
  return (
    <View style={{ flex: 1, alignItems: "center", justifyContent: "center", padding: 16 }}>
      <Text style={{ fontSize: 24, fontWeight: "bold" }}>This screen does not exist.</Text>
      <Link
        href="/"
        replace
        asChild
      >
        <Button variant="outline" style={{ marginTop: 16 }}>
          <Text>Go to home screen</Text>
        </Button>
      </Link>
    </View>
  );
}
