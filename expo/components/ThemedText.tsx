import { Text, type TextProps } from 'react-native';

import { useThemeColor } from '@/hooks/useThemeColor';

export type ThemedTextProps = TextProps & {
  lightColor?: string;
  darkColor?: string;
  type?: 'default' | 'title' | 'defaultSemiBold' | 'subtitle' | 'link';
};

export function ThemedText({
  style,
  lightColor,
  darkColor,
  type = 'default',
  ...rest
}: ThemedTextProps) {
  const color = useThemeColor({ light: lightColor, dark: darkColor }, 'text');

  let className = '';
  switch (type) {
    case 'title':
      className = 'text-3xl font-bold leading-8';
      break;
    case 'defaultSemiBold':
      className = 'text-base leading-6 font-semibold';
      break;
    case 'subtitle':
      className = 'text-xl font-bold';
      break;
    case 'link':
      className = 'leading-[30px] text-base text-[#0a7ea4]';
      break;
    default:
      className = 'text-base leading-6';
  }

  return <Text className={className} style={[{ color }, style]} {...rest} />;
}
