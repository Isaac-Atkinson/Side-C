import AuthForm from "./AuthForm.tsx";
import {API_BASE_URL} from "../../../config/api.ts";
import {useNavigate} from "react-router-dom";

function LoginForm(){
    const navigate = useNavigate();

    const handleLogin = async (data: {
        username: string;
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
            navigate("/reviews/albums")
            return null;
        } else if (response.status === 403){
            return "Wrong username or password";
        } else {
            return "Something went wrong";
        }
    };

    return(
        <AuthForm
            onSubmit={handleLogin}
            buttonMsg="Login"
        />
    )
}

export default LoginForm;