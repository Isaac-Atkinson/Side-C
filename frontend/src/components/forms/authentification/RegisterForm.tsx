import {API_BASE_URL} from "../../../config/api.ts";
import AuthForm from "./AuthForm.tsx";
import {useNavigate} from "react-router-dom";
import {useSnackbar} from "../../../context/SnackBarContext.tsx";
import {apiFetch} from "../../../api/ApiFetch.ts";
import {useState} from "react";
import {useUserContext} from "../../../context/UserContext.tsx";


function RegisterForm() {
    const navigate = useNavigate();
    const {getUser} = useUserContext();

    const[loading, setLoading] = useState<boolean>(false);

    const {showSnackbar} = useSnackbar();

    const handleRegister = async (data: {
        username: string;
        password: string;
    }) => {

        setLoading(true);

        try{
            const response = await apiFetch(`${API_BASE_URL}/auth/register`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            });

            if(response.ok){
                await getUser();

                showSnackbar("Account created")
                navigate("/reviews/albums")
                return null;
            }

            if(response.status === 409){
                return "Username already exists"
            }

            return "Something went wrong"
        } catch {
            return "Could not register";
        } finally {
            setLoading(false);
        }

    };

    return (
        <AuthForm
            onSubmit={handleRegister}
            buttonMsg="Register"
            loading={loading}
        />
    )
}

export default RegisterForm;