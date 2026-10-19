import LoginForm from "../../components/forms/authentification/LoginForm.tsx";
import {Link as RouterLink} from "react-router-dom";
import {Link} from "@mui/material";
import {Box, Typography} from "@mui/material";
import PageContainer from "../../components/layout/pageContainer.tsx";

function LoginPage(){
    return(

        <PageContainer>

            <Box
                sx={{
                    flex: 1,
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    gap: 3,

                }}
            >
                <Typography variant="h3">
                    Log in
                </Typography>

                <LoginForm/>

                <Link component={RouterLink} to="/register">
                    Create an account
                </Link>
            </Box>
        </PageContainer>

    )
}

export default LoginPage;