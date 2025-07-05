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
  onFavoritesButtonClicked: () => void;
  isTextInputEditable: boolean;
}

export const SearchBar = ({
  query,
  onQueryChanged,
  isFocused,
  onFocused,
  onClearButtonClicked,
  onFilterButtonClicked,
  onFavoritesButtonClicked,
  isTextInputEditable,
}: SearchBarProps) => {
  const textInputRef = useRef<TextInput | null>(null);
  const { colors } = useTheme();

  const createActionButton = (
    onPress: () => void,
    icon: keyof typeof Ionicons.glyphMap,
    isPrimary: boolean,
  ) => (
    <TouchableOpacity
      onPress={onPress}
      style={[
        {
          alignItems: "center",
          justifyContent: "center",
          borderRadius: 16,
          backgroundColor: isPrimary ? colors.primary : colors.card,
          width: 48,
          height: 48,
          shadowOffset: { width: 0, height: 4 },
          shadowOpacity: 0.2,
          shadowRadius: 8,
          elevation: 5,
        },
      ]}
    >
      <Ionicons
        name={icon}
        size={20}
        color={isPrimary ? "white" : colors.primary}
      />
    </TouchableOpacity>
  );

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
        gap: 16,
        flexDirection: "row",
        alignItems: "center",
      }}
    >
      <View
        style={{
          flex: 1,
          height: 48,
          flexDirection: "row",
          alignItems: "center",
          borderRadius: 24,
          backgroundColor: colors.card,
          shadowOffset: { width: 0, height: isFocused ? 8 : 4 },
          shadowOpacity: isFocused ? 0.15 : 0.08,
          shadowRadius: isFocused ? 12 : 8,
          elevation: isFocused ? 8 : 4,
          borderWidth: 2,
          borderColor: isFocused ? colors.primary : "transparent",
        }}
      >
        <TextInput
          ref={textInputRef}
          style={{
            flex: 1,
            color: colors.text,
            fontSize: 16,
            paddingStart: 20,
          }}
          placeholder="Search..."
          placeholderTextColor={colors.text}
          value={query}
          onChangeText={onQueryChanged}
          autoCorrect={false}
          autoCapitalize="none"
          onFocus={() => onFocused(true)}
          onBlur={() => onFocused(false)}
          editable={isTextInputEditable}
        />
        <TouchableOpacity
          onPress={onClearButtonClicked}
          disabled={!query}
          style={{
            opacity: query ? 1 : 0,
            padding: 6,
            marginEnd: 8,
          }}
        >
          <Ionicons
            name="close"
            size={20}
            color={colors.text}
          />
        </TouchableOpacity>
      </View>

      {createActionButton(onFavoritesButtonClicked, "heart", false)}
      {createActionButton(onFilterButtonClicked, "options-outline", true)}
    </View>
  );
};
