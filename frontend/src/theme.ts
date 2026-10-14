// src/theme.ts
import { createTheme } from "@mui/material/styles";

export const theme = createTheme({
    palette: {
        mode: "dark",
        primary: { main: "#7c5cff" },
        background: {
            default: "#12121a",
            paper: "#1c1c28",
        },
        text: {
            primary: "#f2f2f7",
            secondary: "#a0a0b0",
        },
        error: {
            main: "#f44336",
        }
    },
    typography: {
        fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
        h4: { fontWeight: 700 },
        button: { textTransform: "none", fontWeight: 600 },
    },
    shape: {
        borderRadius: 12,
    },
    spacing: 8,
});