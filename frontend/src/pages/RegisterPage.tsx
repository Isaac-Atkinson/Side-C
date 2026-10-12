
import RegisterForm from "../components/forms/authentification/RegisterForm.tsx";
import {Link} from "react-router-dom";

function registerPage() {


    return(
        <div className="login-page">
            <RegisterForm/>

            <Link to="/login">
                Login
            </Link>
        </div>
    )

}

export default registerPage;