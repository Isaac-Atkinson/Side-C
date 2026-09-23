import {Box, Button, TextField} from "@mui/material";
import {useState} from "react";
import {API_BASE_URL} from "../../config/api.ts";

function RegisterForm(){
    const[username, setUsername] = useState("");
    const[password, setPassword] = useState("");

    const handleSubmit = async (e: React.SubmitEvent ) => {
        e.preventDefault();

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
                />

                <TextField
                    label = "Password"
                    value = {password}
                    onChange={e => setPassword(e.target.value)}
                />

                <Button type="submit">
                    Submit
                </Button>

            </Box>
        </div>
    )
}

export default RegisterForm;