import {createContext, type ReactNode, useContext, useState} from "react";
import {Alert, Snackbar} from "@mui/material";

type SnackBarContextType = {
    showSnackbar: (
        message: string,
    ) => void;
}

const SnackBarContext =
    createContext<SnackBarContextType | undefined>(undefined);

export function SnackBarProvider({ children }: { children: ReactNode }) {
    const [open, setOpen] = useState(false);
    const [message, setMessage] = useState("");

    const showSnackbar = (message: string) => {
        setOpen(true);
        setMessage(message);
    }

    const handleClose = (
        _event?: React.SyntheticEvent | Event | null,
        reason?: string
    ) => {
        if (reason === "clickaway") {
            return;
        }

        setOpen(false);
    };

    return (
        <SnackBarContext.Provider value={{showSnackbar}}>
            {children}

            <Snackbar
                open={open}
                autoHideDuration={3000}
                onClose={handleClose}
                anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
            >
            <Alert
                severity= "success"
                onClose= {() => setOpen(false)}
            >
                {message}
            </Alert>
        </Snackbar>

        </SnackBarContext.Provider>
    )
}

export function useSnackbar() {
    const context = useContext(SnackBarContext);

    if (context === undefined) {
        throw new Error(
            "useSnackbar must be used inside SnackBarProvider"
        );
    }

    return context;
}

