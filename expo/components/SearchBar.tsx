import { Ionicons } from "@expo/vector-icons";
import { useTheme } from "@react-navigation/native";
import { useEffect, useRef } from "react";
import { TextInput, TouchableOpacity, View } from "react-native";

interface SearchBarProps {
  query: string;
  onQueryChanged: (text: string) => void;
  isFocused: boolean;
  onFocused: (isFocused: boolean) => void;
  onClearButtonClicked: () => void;
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
        alignItems: "center",
        gap: 12,
      }}
    >
      <View
        style={{
          flex: 1,
          flexDirection: "row",
          alignItems: "center",
          borderRadius: 16,
          backgroundColor: colors.card,
          paddingHorizontal: 20,
          paddingVertical: 14,
          shadowOffset: { width: 0, height: 4 },
          shadowOpacity: 0.1,
          shadowRadius: 8,
          elevation: 4,
          borderWidth: 2,
          borderColor: isFocused ? colors.primary : "transparent",
        }}
      >
        <TextInput
          ref={textInputRef}
          style={{
            flex: 1,
            color: colors.text,
            textAlignVertical: "center",
            padding: 0,
            fontSize: 16,
          }}
          placeholder="Search..."
          placeholderTextColor="rgba(128, 128, 128, 0.7)"
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
          style={{
            opacity: query ? 1 : 0,
            padding: 4,
          }}
        >
          <Ionicons
            name="close"
            size={18}
            color={colors.text}
          />
        </TouchableOpacity>
      </View>

      <TouchableOpacity
        onPress={onFilterButtonClicked}
        style={{
          alignItems: "center",
          justifyContent: "center",
          borderRadius: 50,
          backgroundColor: colors.primary,
          width: 52,
          height: 52,
          shadowOffset: { width: 0, height: 4 },
          shadowOpacity: 0.2,
          shadowRadius: 8,
          elevation: 5,
        }}
      >
        <Ionicons
          name="options-outline"
          size={20}
          color="white"
        />
      </TouchableOpacity>
    </View>
  );
};
