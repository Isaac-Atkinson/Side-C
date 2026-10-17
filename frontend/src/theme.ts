// src/theme.ts
import { createTheme } from "@mui/material/styles";

declare module "@mui/material/styles" {
    interface Palette {
        actionButton: Palette["primary"];
    }

    interface PaletteOptions {
        actionButton?: PaletteOptions["primary"];
    }
}

export const theme = createTheme({
    palette: {
        mode: "dark",
        primary: { main: "#4279e1" },
        background: {
            default: "#0e0f11",
            paper: "#181a1c",

        },
        text: {
            primary: "#f2f2f7",
            secondary: "#a0a0b0",
        },
        error: {
            main: "#f44336",
        },
        actionButton: {
            main: "#1619cc",
        }
    },
    typography: {
        fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
        h4: { fontWeight: 700 },
        body1: { fontWeight: "bold" },
        button: { textTransform: "none", fontWeight: 600 },
    },
    shape: {
        borderRadius: 12,
    },
    spacing: 8,
    components: {
        // MuiTab: {
        //     styleOverrides: {
        //         root: ({ theme }) => ({
        //             textTransform: "none",
        //             borderRadius: theme.shape.borderRadius,
        //             minHeight: 40,
        //             "&.Mui-selected": {
        //                 backgroundColor: theme.palette.primary.main,
        //                 color: theme.palette.primary.contrastText,
        //             },
        //         }),
        //     },
        // },
        // MuiTabs: {
        //     styleOverrides: {
        //         indicator: {
        //             display: "none",
        //         },
        //     },
        // },
    },
});