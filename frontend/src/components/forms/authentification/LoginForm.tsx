import AuthForm from "./AuthForm.tsx";
import {API_BASE_URL} from "../../../config/api.ts";
import {useNavigate} from "react-router-dom";

function LoginForm(){
    const navigate = useNavigate();

    const handleLogin = async (data: {
        username: string;
        email?: string;
        password: string;
    }) => {
        const response= await fetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include",
            body: JSON.stringify(data)
        });

        if(response.ok){
            navigate("/albums")
        }
    };

    return(
        <AuthForm onSubmit={handleLogin}/>
    )
}

export default LoginForm;