import AuthForm from "./AuthForm.tsx";
import {API_BASE_URL} from "../../../config/api.ts";

function LoginForm(){

    const handleLogin = async (data: {
        username: string;
        email?: string;
        password: string;
    }) => {
        await fetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include",
            body: JSON.stringify(data)
        });
    };

    return(
        <AuthForm onSubmit={handleLogin}/>
    )
}

export default LoginForm;