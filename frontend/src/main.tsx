import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from "react-router-dom";
import './index.css'
import App from './App.tsx'
import {CssBaseline, ThemeProvider} from "@mui/material";
import {theme} from "./theme.ts";
import {SnackBarProvider} from "./context/SnackBarContext.tsx";
import {UserProvider} from "./context/UserContext.tsx";


createRoot(document.getElementById("root")!).render(
    <StrictMode>
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <BrowserRouter>
                <UserProvider>
                    <SnackBarProvider>
                        <App />
                    </SnackBarProvider>
                </UserProvider>
            </BrowserRouter>
        </ThemeProvider>
    </StrictMode>,
);
