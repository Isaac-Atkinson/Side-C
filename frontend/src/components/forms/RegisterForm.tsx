import {Box, Button, TextField} from "@mui/material";
import {useState} from "react";
import {API_BASE_URL} from "../../config/api.ts";

function RegisterForm(){
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
        }

        if(password.length < 8){
            setPasswordError("Password must be at least 8 characters");
            valid = false;
        }

        return valid;
    }

    const handleSubmit = async (e: React.SubmitEvent ) => {
        e.preventDefault();

        if(!validateForm()) return;

        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                username : username,
                password: password,
            })
        });

        if(response.ok){
            console.log("User created");
        }
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

export default RegisterForm;