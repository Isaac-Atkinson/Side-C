import AuthForm from "./AuthForm.tsx";
import {API_BASE_URL} from "../../../config/api.ts";
import {useNavigate} from "react-router-dom";
import {useUserContext} from "../../../context/UserContext.tsx";
import {apiFetch} from "../../../api/ApiFetch.ts";

function LoginForm(){
    const navigate = useNavigate();
    const {getUser} = useUserContext();

    const handleLogin = async (data: {
        username: string;
        password: string;
    }) => {
        const response= await apiFetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if(response.ok){
            await getUser();
            navigate("/reviews/albums");
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