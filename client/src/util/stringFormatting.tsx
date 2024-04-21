
/**
 * Converts a camelCase string to a space-separated string with each word capitalized.
 */
export function formatKeyToMessage(input: string): string {
  const words = input.split(/(?=[A-Z])/);
  // Capitalize the first letter of each word and join them with spaces
  return words.map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');

}
