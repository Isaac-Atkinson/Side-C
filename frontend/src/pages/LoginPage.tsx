import LoginForm from "../components/forms/authentification/LoginForm.tsx";
import {Link} from "react-router-dom";

function LoginPage(){
    return(
        <div  className="login-page">
            <LoginForm/>

            <Link to="/register">
                Register
            </Link>
        </div>
    )
}

export default LoginPage;