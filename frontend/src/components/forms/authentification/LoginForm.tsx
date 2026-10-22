import AuthForm from "./AuthForm.tsx";
import {API_BASE_URL} from "../../../config/api.ts";
import {useNavigate} from "react-router-dom";
import {useUserContext} from "../../../context/UserContext.tsx";
import {apiFetch} from "../../../api/ApiFetch.ts";
import {Box, Link} from "@mui/material";
import {Link as RouterLink} from "react-router-dom";
import {useState} from "react";




function LoginForm() {
    const navigate = useNavigate();
    const {getUser} = useUserContext();

    const[loading, setLoading] = useState<boolean>(false);



    const handleLogin = async (data: {
        username: string;
        password: string;
    }) => {

        setLoading(true);

        try {
            const response = await apiFetch(`${API_BASE_URL}/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                await getUser();

                navigate("/reviews/albums");
                return null;
            }

            if (response.status === 403) {
                return "Wrong username or password";
            }

            return "Something went wrong";
        } catch {
            return "Could not log in";
        } finally {
            setLoading(false);
        }
    }




    return(
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
                alignItems: "center",
                gap: 3
            }}
        >
            <AuthForm
                onSubmit={handleLogin}
                buttonMsg="Login"
                loading={loading}
            />

            <Link component={RouterLink} to="/register">
                Create an account
            </Link>
        </Box>

    )
}

export default LoginForm;