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
    background: "hsl(0 0% 97.65%)", // --background
    border: "hsl(0 0% 84.71%)", // --border
    card: "hsl(0 0% 98.82%)", // --card
    notification: "hsl(10.16 77.87% 53.92%)", // --destructive
    primary: "hsl(22.93 92.59% 52.35%)", // --primary
    text: "hsl(0 0% 12.55%)", // --foreground
  },
  dark: {
    background: "hsl(0 0% 6.67%)", // --background
    border: "hsl(44 14% 11%)", // --border
    card: "hsl(0 0% 9.8%)", // --card
    notification: "hsl(10.16 77.87% 53.92%)", // --destructive
    primary: "hsl(22.93 92.59% 52.35%)", // --primary
    text: "hsl(0 0% 93.33%)", // --foreground
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
