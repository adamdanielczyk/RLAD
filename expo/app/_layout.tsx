import { QueryProvider } from "@/lib/queries/QueryProvider";
import { useAppStore } from "@/lib/store/appStore";
import { ThemeProvider } from "@/lib/ui/theme";
import { SplashScreen, Stack } from "expo-router";
import { StatusBar } from "expo-status-bar";
import * as React from "react";
import { useEffect } from "react";
import { GestureHandlerRootView } from "react-native-gesture-handler";

export { ErrorBoundary } from "expo-router";

SplashScreen.preventAutoHideAsync();

export default function RootLayout() {
  useEffect(() => {
    const initializeApp = async () => {
      await useAppStore.getState().initialize();
      SplashScreen.hideAsync();
    };

    initializeApp();
  }, []);

  return (
    <QueryProvider>
      <GestureHandlerRootView>
        <ThemeProvider>
          <StatusBar style="auto" />
          <Stack>
            <Stack.Screen
              name="index"
              options={{ headerShown: false }}
            />
            <Stack.Screen
              name="details/[id]"
              options={{
                presentation: "modal",
              }}
            />
            <Stack.Screen
              name="+not-found"
              options={{ headerShown: false }}
            />
          </Stack>
        </ThemeProvider>
      </GestureHandlerRootView>
    </QueryProvider>
  );
}
