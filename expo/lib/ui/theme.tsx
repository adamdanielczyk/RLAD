import { useColorScheme } from "@/lib/ui/useColorScheme";
import {
  DarkTheme,
  DefaultTheme,
  ThemeProvider as ReactThemeProvider,
  Theme,
} from "@react-navigation/native";

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const { isDarkColorScheme } = useColorScheme();
  return (
    <ReactThemeProvider value={isDarkColorScheme ? DARK_THEME : LIGHT_THEME}>
      {children}
    </ReactThemeProvider>
  );
}

const NAV_THEME = {
  light: {
    background: "hsl(0 0% 96%)", // Slightly warmer background
    border: "hsl(0 0% 84.71%)", // --border
    card: "hsl(0 0% 100%)", // Pure white cards for contrast
    notification: "hsl(10.16 77.87% 53.92%)", // --destructive
    primary: "hsl(22.93 92.59% 58%)", // More vibrant orange
    text: "hsl(0 0% 8%)", // Darker text for better contrast
  },
  dark: {
    background: "hsl(0 0% 4%)", // Deeper dark background
    border: "hsl(44 14% 11%)", // --border
    card: "hsl(0 0% 12%)", // Lighter cards for contrast
    notification: "hsl(10.16 77.87% 53.92%)", // --destructive
    primary: "hsl(22.93 92.59% 60%)", // Brighter orange for dark mode
    text: "hsl(0 0% 96%)", // Brighter text
  },
};

const LIGHT_THEME: Theme = {
  ...DefaultTheme,
  colors: NAV_THEME.light,
};
const DARK_THEME: Theme = {
  ...DarkTheme,
  colors: NAV_THEME.dark,
};
