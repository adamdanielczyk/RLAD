import { NAV_THEME } from "@/lib/constants";
import { useAppStore } from "@/lib/store/appStore";
import { useColorScheme } from "@/lib/useColorScheme";
import { DarkTheme, DefaultTheme, Theme, ThemeProvider } from "@react-navigation/native";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { SplashScreen, Stack } from "expo-router";
import { StatusBar } from "expo-status-bar";
import * as React from "react";
import { useEffect } from "react";
import { GestureHandlerRootView } from "react-native-gesture-handler";

export { ErrorBoundary } from "expo-router";

const LIGHT_THEME: Theme = {
  ...DefaultTheme,
  colors: NAV_THEME.light,
};
const DARK_THEME: Theme = {
  ...DarkTheme,
  colors: NAV_THEME.dark,
};

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 2,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

SplashScreen.preventAutoHideAsync();

export default function RootLayout() {
  const { isDarkColorScheme } = useColorScheme();

  useEffect(() => {
    const initializeApp = async () => {
      await useAppStore.getState().initialize();
      SplashScreen.hideAsync();
    };

    initializeApp();
  }, []);

  return (
    <QueryClientProvider client={queryClient}>
      <GestureHandlerRootView>
        <ThemeProvider value={isDarkColorScheme ? DARK_THEME : LIGHT_THEME}>
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
    </QueryClientProvider>
  );
}
