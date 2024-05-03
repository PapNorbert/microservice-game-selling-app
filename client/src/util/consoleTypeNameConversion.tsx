export function convertConsoleTypeName(discType : string) {
  switch (discType) {
    case "PS":
      return "PlayStation Series";
    case "XBOX":
      return "Xbox Series";
    case "SWITCH":
      return "Nintendo Switch Series";
  }
}