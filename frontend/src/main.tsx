import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from "react-router-dom";
import './index.css'
import App from './App.tsx'
import {CssBaseline, ThemeProvider} from "@mui/material";
import {theme} from "./theme.ts";
import {SnackBarProvider} from "./context/SnackBarContext.tsx";
import {UserProvider} from "./context/UserContext.tsx";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";


createRoot(document.getElementById("root")!).render(
    <StrictMode>
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <BrowserRouter>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <UserProvider>
                        <SnackBarProvider>
                            <App />
                        </SnackBarProvider>
                    </UserProvider>
                </LocalizationProvider>
            </BrowserRouter>
        </ThemeProvider>
    </StrictMode>,
);
