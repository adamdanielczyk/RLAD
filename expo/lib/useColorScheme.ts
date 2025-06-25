import { useColorScheme as useSystemColorScheme } from 'react-native';
import React from 'react';

export function useColorScheme() {
  const systemScheme = useSystemColorScheme();
  const [colorScheme, setColorScheme] = React.useState<'light' | 'dark'>(
    systemScheme ?? 'light'
  );

  const toggleColorScheme = React.useCallback(() => {
    setColorScheme((prev) => (prev === 'light' ? 'dark' : 'light'));
  }, []);

  React.useEffect(() => {
    if (systemScheme) {
      setColorScheme(systemScheme);
    }
  }, [systemScheme]);

  return {
    colorScheme,
    isDarkColorScheme: colorScheme === 'dark',
    setColorScheme,
    toggleColorScheme,
  };
}
