import { Ionicons } from "@expo/vector-icons";
import { useTheme } from "@react-navigation/native";
import { useEffect, useRef } from "react";
import { TextInput, TouchableOpacity, View } from "react-native";

interface SearchBarProps {
  query: string;
  onQueryChanged: (text: string) => Promise<void>;
  isFocused: boolean;
  onFocused: (isFocused: boolean) => void;
  onClearButtonClicked: () => Promise<void>;
  onFilterButtonClicked: () => void;
}

export const SearchBar = ({
  query,
  onQueryChanged,
  isFocused,
  onFocused,
  onClearButtonClicked,
  onFilterButtonClicked,
}: SearchBarProps) => {
  const textInputRef = useRef<TextInput | null>(null);
  const { colors } = useTheme();

  useEffect(() => {
    if (isFocused) {
      textInputRef.current?.focus();
    } else {
      textInputRef.current?.blur();
    }
  }, [isFocused]);

  return (
    <View
      style={{
        margin: 16,
        flexDirection: "row",
        alignItems: "stretch",
        gap: 8,
      }}
    >
      <View
        style={{
          flex: 1,
          flexDirection: "row",
          alignItems: "center",
          borderRadius: 8,
          borderWidth: 1,
          borderColor: colors.border,
          backgroundColor: colors.card,
          paddingHorizontal: 16,
          paddingVertical: 8,
        }}
      >
        <TextInput
          ref={textInputRef}
          style={{ flex: 1, color: colors.text }}
          placeholder="Search..."
          placeholderTextColor={colors.text}
          value={query}
          onChangeText={onQueryChanged}
          autoCorrect={false}
          autoCapitalize="none"
          onFocus={() => onFocused(true)}
          onBlur={() => onFocused(false)}
        />
        <TouchableOpacity
          onPress={onClearButtonClicked}
          disabled={!query}
          style={{ opacity: query ? 1 : 0 }}
        >
          <Ionicons
            name="close"
            size={16}
            color={colors.text}
          />
        </TouchableOpacity>
      </View>
      <TouchableOpacity
        onPress={onFilterButtonClicked}
        style={{
          alignItems: "center",
          justifyContent: "center",
          borderRadius: 8,
          borderWidth: 1,
          borderColor: colors.border,
          backgroundColor: colors.card,
          aspectRatio: 1,
        }}
      >
        <Ionicons
          name="filter"
          size={16}
          color={colors.text}
        />
      </TouchableOpacity>
    </View>
  );
};
