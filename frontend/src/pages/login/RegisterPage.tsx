
import RegisterForm from "../../components/forms/authentification/RegisterForm.tsx";
import {Link} from "react-router-dom";
import {Box, Typography} from "@mui/material";
import PageContainer from "../../components/layout/pageContainer.tsx";


function registerPage() {


    return(
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
                    Register
                </Typography>

                <RegisterForm/>

                <Link to="/login">
                    Login
                </Link>
            </Box>

        </PageContainer>

    )

}

export default registerPage;