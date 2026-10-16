import LoginForm from "../../components/forms/authentification/LoginForm.tsx";
import {Link} from "react-router-dom";
import {Box, Typography} from "@mui/material";
import PageContainer from "../../components/layout/pageContainer.tsx";

function LoginPage(){
    return(
        // <div  className="login-page">
        //     <Typography variant="h1" >
        //         Log in
        //     </Typography>
        //     <LoginForm/>
        //
        //     <Link to="/register">
        //         Create an account
        //     </Link>
        // </div>

        <PageContainer>

            <Box
                sx={{
                    flex: 1,
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "center",
                    alignItems: "center",
                    gap: 3,
                }}
            >
                <Typography variant="h3">
                    Log in
                </Typography>

                <LoginForm/>

                <Link to="/register">
                    Create an account
                </Link>
            </Box>
        </PageContainer>

    )
}

export default LoginPage;