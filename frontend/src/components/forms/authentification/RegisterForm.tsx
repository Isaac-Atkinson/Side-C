import {API_BASE_URL} from "../../../config/api.ts";
import AuthForm from "./AuthForm.tsx";
import {useNavigate} from "react-router-dom";

function RegisterForm(){
    const navigate = useNavigate();

    const handleRegister = async (data: {
        username: string;
        password: string;
    }) => {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if(response.ok){
            navigate("/login")
            return null;
        } else if (response.status === 409){
            return "Username already exists"
        } else {
            return "Something went wrong"
        }
    };
    return (
        <AuthForm
            onSubmit={handleRegister}
            buttonMsg="Register"
        />
    )
}

export default RegisterForm;