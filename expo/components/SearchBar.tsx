import { useAppStore } from "@/lib/store/appStore";
import { Ionicons } from "@expo/vector-icons";
import { useTheme } from "@react-navigation/native";
import { useRouter } from "expo-router";
import { useEffect, useRef } from "react";
import { TextInput, TouchableOpacity, View } from "react-native";

export const SearchBar = ({ hasItems }: { hasItems: boolean }) => {
  const router = useRouter();
  const textInputRef = useRef<TextInput | null>(null);
  const { colors } = useTheme();

  const searchQuery = useAppStore((state) => state.searchQuery);
  const isSearchFocused = useAppStore((state) => state.isSearchFocused);
  const onSearchQueryChanged = useAppStore((state) => state.onSearchQueryChanged);
  const onSearchFocused = useAppStore((state) => state.onSearchFocused);
  const onClearButtonClicked = useAppStore((state) => state.onClearButtonClicked);
  const onDataSourceButtonClicked = useAppStore((state) => state.onDataSourceButtonClicked);
  const isBottomSheetOpen = useAppStore((state) => state.isBottomSheetOpen);
  const toggleViewMode = useAppStore((state) => state.toggleViewMode);

  useEffect(() => {
    if (isSearchFocused) {
      textInputRef.current?.focus();
    } else {
      textInputRef.current?.blur();
    }
  }, [isSearchFocused]);

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
          shadowOffset: { width: 0, height: isSearchFocused ? 8 : 4 },
          shadowOpacity: isSearchFocused ? 0.15 : 0.08,
          shadowRadius: isSearchFocused ? 12 : 8,
          elevation: isSearchFocused ? 8 : 4,
          borderWidth: 2,
          borderColor: isSearchFocused ? colors.primary : "transparent",
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
          value={searchQuery}
          onChangeText={onSearchQueryChanged}
          autoCorrect={false}
          autoCapitalize="none"
          onFocus={() => onSearchFocused(true)}
          onBlur={() => onSearchFocused(false)}
          editable={!isBottomSheetOpen}
        />
        <TouchableOpacity
          onPress={onClearButtonClicked}
          disabled={!searchQuery}
          style={{
            opacity: searchQuery ? 1 : 0,
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

      <ActionButton
        onPress={() => router.push("/favorites")}
        icon="heart"
        isPrimary={false}
        enabled={true}
      />
      <ActionButton
        onPress={toggleViewMode}
        icon="reorder-four"
        isPrimary={false}
        enabled={hasItems}
      />
      <ActionButton
        onPress={onDataSourceButtonClicked}
        icon="options-outline"
        isPrimary={true}
        enabled={true}
      />
    </View>
  );
};

function ActionButton({
  onPress,
  icon,
  isPrimary,
  enabled,
}: {
  onPress: () => void;
  icon: keyof typeof Ionicons.glyphMap;
  isPrimary: boolean;
  enabled: boolean;
}) {
  const { colors } = useTheme();

  return (
    <TouchableOpacity
      onPress={onPress}
      disabled={!enabled}
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
        !enabled && {
          opacity: 0.5,
          shadowOpacity: 0.1,
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
}
