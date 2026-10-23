import LoginForm from "../../components/forms/authentification/LoginForm.tsx";
import {Box, Typography} from "@mui/material";
import PageContainer from "../../components/layout/pageContainer.tsx";
import {Navigate} from "react-router-dom";
import {useUserContext} from "../../context/UserContext.tsx";


function LoginPage(){
    const {username} = useUserContext();

    if (username) {
        return <Navigate to="/reviews/albums" replace />;
    }

    return(

        <PageContainer>

            <Box
                sx={{
                    flex: 1,
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    justifyContent: "center",
                    gap: 3,
                    pb: 8

                }}
            >
                <Typography variant="h3">
                    Log in
                </Typography>

                <LoginForm/>


            </Box>
        </PageContainer>

    )
}

export default LoginPage;