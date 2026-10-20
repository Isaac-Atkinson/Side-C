import {API_BASE_URL} from "../../../config/api.ts";
import AuthForm from "./AuthForm.tsx";
import {useNavigate} from "react-router-dom";
import {useSnackbar} from "../../../context/SnackBarContext.tsx";
import {apiFetch} from "../../../api/ApiFetch.ts";


function RegisterForm() {
    const navigate = useNavigate();

    const {showSnackbar} = useSnackbar();

    const handleRegister = async (data: {
        username: string;
        password: string;
    }) => {
        const response = await apiFetch(`${API_BASE_URL}/auth/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if(response.ok){

            showSnackbar("Account created")
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