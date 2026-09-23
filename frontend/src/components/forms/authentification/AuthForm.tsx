import {useState} from "react";
import {Box, Button, TextField} from "@mui/material";

type AuthFormProps = {
    onSubmit: (data: {
        username: string,
        password: string,
    }) => void
}
function AuthForm({onSubmit}: AuthFormProps) {
    const[username, setUsername] = useState("");
    const[password, setPassword] = useState("");

    const [usernameError, setUsernameError] = useState("");
    const [passwordError, setPasswordError] = useState("");

    const validateForm = () => {
        let valid = true;

        setUsernameError("");
        setPasswordError("");

        if(!username){
            setUsernameError("Username is required");
            valid = false;
        }

        if(!password){
            setPasswordError("Password is required");
            valid = false;
        } else if(password.length < 8){
            setPasswordError("Password must be at least 8 characters");
            valid = false;
        }

        return valid;
    }

    const handleSubmit = async (e: React.SubmitEvent ) => {
        e.preventDefault();

        if(!validateForm()) return;

        onSubmit({username, password});
    }
    return (
        <div>
            <Box
                component="form"
                onSubmit={handleSubmit}
                sx={{ display: "flex", flexDirection: "column", gap: 2 }}
            >
                <TextField
                    label = "Username"
                    value = {username}
                    onChange={e => setUsername(e.target.value)}
                    error={Boolean(usernameError)}
                    helperText={usernameError}
                />

                <TextField
                    label = "Password"
                    value = {password}
                    onChange={e => setPassword(e.target.value)}
                    error={Boolean(passwordError)}
                    helperText={passwordError}
                />

                <Button type="submit">
                    Submit
                </Button>

            </Box>
        </div>
    )
}

export default AuthForm;