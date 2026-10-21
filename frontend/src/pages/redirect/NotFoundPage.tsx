import {Box, Button, Typography} from "@mui/material";
import PageContainer from "../../components/layout/pageContainer.tsx";
import {Link} from "react-router-dom";

function NotFoundPage() {
    return(
        <PageContainer>
            <Box
                sx={{
                    flex: 1,
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    justifyContent: "center"
                }}
            >
                <Typography variant="h3">
                    404
                </Typography>

                <Typography variant="h6">
                    Page not found
                </Typography>

                <Button component={Link} to="/">
                    Back to login
                </Button>
            </Box>
        </PageContainer>
    )
}

export default NotFoundPage;