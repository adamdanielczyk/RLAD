import { cn } from "@/lib/utils";
import { Ionicons } from "@expo/vector-icons";
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

  useEffect(() => {
    if (isFocused) {
      textInputRef.current?.focus();
    } else {
      textInputRef.current?.blur();
    }
  }, [isFocused]);

  return (
    <View className="m-4 flex-row items-stretch gap-x-2">
      <View className="flex-1 flex-row items-center rounded-lg border bg-card px-4 py-2">
        <TextInput
          ref={textInputRef}
          className="flex-1 text-card-foreground placeholder:text-card-foreground"
          placeholder="Search..."
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
          className={cn(query ? "opacity-100" : "opacity-0")}
        >
          <Ionicons
            name="close"
            size={16}
            className="text-card-foreground"
          />
        </TouchableOpacity>
      </View>
      <TouchableOpacity
        onPress={onFilterButtonClicked}
        className="flex aspect-square items-center justify-center rounded-lg border bg-card"
      >
        <Ionicons
          name="filter"
          size={16}
          className="text-card-foreground"
        />
      </TouchableOpacity>
    </View>
  );
};
