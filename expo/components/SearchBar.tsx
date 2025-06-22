import { useAppStore } from "@/lib/store/appStore";
import { cn } from "@/lib/utils";
import { Ionicons } from "@expo/vector-icons";
import { useEffect, useRef } from "react";
import { TextInput, TouchableOpacity, View } from "react-native";

export const SearchBar = () => {
  const textInputRef = useRef<TextInput | null>(null);

  const searchQuery = useAppStore((state) => state.searchQuery);
  const isSearchFocused = useAppStore((state) => state.isSearchFocused);

  useEffect(() => {
    if (isSearchFocused) {
      textInputRef.current?.focus();
    } else {
      textInputRef.current?.blur();
    }
  }, [isSearchFocused]);

  return (
    <View className="m-4 flex-row items-stretch gap-x-2">
      <View className="flex-1 flex-row items-center rounded-lg border bg-card px-4 py-2">
        <TextInput
          ref={textInputRef}
          className="flex-1 text-card-foreground placeholder:text-card-foreground"
          placeholder="Search..."
          value={searchQuery}
          onChangeText={(text) => useAppStore.getState().setSearchQuery(text)}
          autoCorrect={false}
          autoCapitalize="none"
          onFocus={() => useAppStore.getState().setIsSearchFocused(true)}
          onBlur={() => useAppStore.getState().setIsSearchFocused(false)}
        />
        <TouchableOpacity
          onPress={() => useAppStore.getState().clearSearch()}
          disabled={!searchQuery}
          className={cn(searchQuery ? "opacity-100" : "opacity-0")}
        >
          <Ionicons
            name="close"
            size={16}
            className="text-card-foreground"
          />
        </TouchableOpacity>
      </View>
      <TouchableOpacity
        onPress={() => useAppStore.getState().setIsBottomSheetOpen(true)}
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
