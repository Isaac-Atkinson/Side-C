import {API_BASE_URL} from "../../../config/api.ts";
import AuthForm from "./AuthForm.tsx";

function RegisterForm(){


    const handleRegister = async (data: {
        username: string;
        email?: string;
        password: string;
    }) => {
        await fetch(`${API_BASE_URL}/auth/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });
    };
    return (
        <AuthForm onSubmit={handleRegister}/>
    )
}

export default RegisterForm;