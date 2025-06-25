import React from 'react';
import { Pressable, PressableProps, Text, StyleSheet } from 'react-native';
import { useTheme } from '@react-navigation/native';

export interface ButtonProps extends PressableProps {
  variant?: 'default' | 'outline';
  size?: 'default' | 'sm' | 'lg';
  children: React.ReactNode;
}

export function Button({ variant = 'default', size = 'default', style, children, ...props }: ButtonProps) {
  const { colors } = useTheme();

  const buttonStyle = [
    styles.base,
    variant === 'outline'
      ? { borderColor: colors.border, borderWidth: StyleSheet.hairlineWidth, backgroundColor: 'transparent' }
      : { backgroundColor: colors.primary },
    size === 'sm' ? styles.sm : size === 'lg' ? styles.lg : styles.default,
    props.disabled && styles.disabled,
    style,
  ];

  const textStyle = [
    styles.text,
    variant === 'outline' ? { color: colors.text } : { color: '#fff' },
    size === 'lg' ? styles.textLg : size === 'sm' ? styles.textSm : null,
  ];

  return (
    <Pressable style={buttonStyle} {...props}>
      <Text style={textStyle}>{children}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  base: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 4,
    paddingHorizontal: 16,
  },
  default: {
    height: 48,
    paddingVertical: 12,
  },
  sm: {
    height: 36,
    paddingVertical: 8,
  },
  lg: {
    height: 56,
    paddingVertical: 16,
  },
  text: {
    fontSize: 16,
    fontWeight: '500',
  },
  textSm: {
    fontSize: 14,
  },
  textLg: {
    fontSize: 18,
  },
  disabled: {
    opacity: 0.5,
  },
});
